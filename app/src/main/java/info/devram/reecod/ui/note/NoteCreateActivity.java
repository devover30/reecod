package info.devram.reecod.ui.note;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.activity.OnBackPressedCallback;
import androidx.core.content.ContextCompat;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.rxjava2.RxPreferenceDataStoreBuilder;
import androidx.datastore.rxjava2.RxDataStore;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import info.devram.reecod.BaseActivity;
import info.devram.reecod.R;
import info.devram.reecod.data.model.NoteTagEntity;
import info.devram.reecod.data.model.UserEntity;
import info.devram.reecod.databinding.ActivityNoteCreateBinding;
import info.devram.reecod.dtos.RemoteNoteCreateDto;
import info.devram.reecod.exceptions.EmptyNoteDescriptionException;
import info.devram.reecod.exceptions.EmptyNoteHeadingException;
import info.devram.reecod.libs.Constants;
import info.devram.reecod.libs.DataStoreHelper;
import info.devram.reecod.libs.DataStoreSingleton;
import info.devram.reecod.libs.Helpers;
import info.devram.reecod.ui.dashboard.DashboardActivity;
import info.devram.reecod.ui.dashboard.DashboardViewModel;
import info.devram.reecod.ui.dashboard.DashboardViewModelFactory;

public class NoteCreateActivity extends BaseActivity implements
        View.OnClickListener,
        AdapterView.OnItemSelectedListener {

    private static final String TAG = "NoteCreateActivity";
    private DashboardViewModel viewModel;
    private NoteCreateViewModel noteCreateViewModel;
    private DataStoreHelper dataStoreHelper;
    private ActivityNoteCreateBinding binding;
    private String authToken = null;
    private String tag = null;
    private List<String> spinnerList;
    private List<NoteTagEntity> notesTagsList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNoteCreateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = new ViewModelProvider(this, new DashboardViewModelFactory())
                .get(DashboardViewModel.class);
        noteCreateViewModel = new ViewModelProvider(this, new NoteCreateViewModelFactory())
                .get(NoteCreateViewModel.class);
        DataStoreSingleton dataStoreSingleton = DataStoreSingleton.getInstance();
        RxDataStore<Preferences> dataStoreRX;
        if (dataStoreSingleton.getDataStore() == null) {
            dataStoreRX = new RxPreferenceDataStoreBuilder(this, Constants.USER_DATA).build();
        } else {
            dataStoreRX = dataStoreSingleton.getDataStore();
        }
        dataStoreSingleton.setDataStore(dataStoreRX);
        dataStoreHelper = new DataStoreHelper(dataStoreRX);
        Thread.setDefaultUncaughtExceptionHandler(handleAppCrash);
        MaterialToolbar toolbar = binding.toolbarEntryCreate;
        toolbar.setNavigationIcon(R.drawable.arrow_back);
        toolbar.setNavigationOnClickListener(v -> {
            Intent intent = new Intent(NoteCreateActivity.this, DashboardActivity.class);
            startActivity(intent);
            finish();
        });
        getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(NoteCreateActivity.this, DashboardActivity.class);
                startActivity(intent);
                finish();
            }
        });
        binding.noteSaveProgressBar.setVisibility(View.INVISIBLE);
        binding.noteSaveButton.setOnClickListener(this);
        binding.notesTagsSpinner.setOnItemSelectedListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        subscribeObservers();
    }

    private void subscribeObservers() {
        String userEntity = dataStoreHelper.getStringValue(Constants.USER_DATA);
        if (!Objects.equals(userEntity, "null")) {
            Gson gson = new Gson();
            UserEntity user = gson.fromJson(userEntity, UserEntity.class);
            viewModel.setAuthTokenLiveData(user.getToken());
        }

        viewModel.authTokenObserver().observe(this, token -> {
            if (token != null) {
                authToken = token;
                noteCreateViewModel.fetchNotesTags(authToken);
            }
        });

        noteCreateViewModel.notesTagsResult().observe(this, notesTagsResult -> {
            switch (notesTagsResult.getStatus()) {
                case ERROR -> Log.d(TAG, "subscribeObservers: " + notesTagsResult.getException());
                case NOTES_TAGS_GET_SUCCESS -> {
                    if (notesTagsResult.getData() != null) {
                        notesTagsList = notesTagsResult.getData();
                        spinnerList = new ArrayList<>();
                        spinnerList.add("Tag");
                        for (NoteTagEntity tag: notesTagsList) {
                            spinnerList.add(tag.getTag());
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                                this,
                                android.R.layout.simple_spinner_dropdown_item,
                                spinnerList
                        );
                        adapter.setDropDownViewResource(R.layout.create_spinner_list);
                        binding.notesTagsSpinner.setAdapter(adapter);
                    }
                }
            }
        });

        noteCreateViewModel.noteCreateResult().observe(this, noteCreateResult -> {
            switch (noteCreateResult.getStatus()) {
                case ERROR -> {
                    if (noteCreateResult.getException() != null) {
                        noteCreateViewModel.dialogShownObserver().observe(this, isShown -> {
                            if (!isShown) {
                                MaterialAlertDialogBuilder builder = buildAlertDialog(
                                        "Error!",
                                        noteCreateResult.getException().getMessage()
                                );
                                Handler handler = new Handler(Looper.getMainLooper());
                                handler.postDelayed(() -> {
                                    binding.noteSaveProgressBar.setVisibility(View.INVISIBLE);
                                    builder.show();
                                    binding.notesTagsSpinner.setSelection(0);
                                    binding.noteHeadingEditText.clearFocus();
                                    binding.noteHeadingEditText.setText("");
                                    binding.noteDescEditText.clearFocus();
                                    binding.noteDescEditText.setText("");
                                }, 1000);
                                noteCreateViewModel.setIsDialogShown(true);
                            }
                        });
                    }
                }
                case NOTE_CREATE_SUCCESS -> {
                    if (noteCreateResult.getData() != null) {
                        binding.noteHeadingEditText.clearFocus();
                        binding.noteHeadingEditText.setText("");
                        binding.noteDescEditText.clearFocus();
                        binding.noteDescEditText.setText("");
                        binding.notesTagsSpinner.setSelection(0, true);

                        noteCreateViewModel.dialogShownObserver().observe(this, isShown -> {
                            if (!isShown) {
                                MaterialAlertDialogBuilder builder = buildAlertDialog(
                                        "Success",
                                        "New Note Created"
                                );
                                Handler handler = new Handler(Looper.getMainLooper());
                                handler.postDelayed(() -> {
                                    binding.noteSaveProgressBar.setVisibility(View.INVISIBLE);
                                    builder.show();
                                }, 1000);
                                noteCreateViewModel.setIsDialogShown(true);
                            }
                        });
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == binding.noteSaveButton.getId()) {
            binding.noteSaveProgressBar.setVisibility(View.VISIBLE);
            noteCreateViewModel.setIsDialogShown(false);
            try {
                if (binding.noteHeadingEditText.getText() == null ||
                        binding.noteHeadingEditText.getText().toString().isEmpty()) {
                    throw new EmptyNoteHeadingException("Heading is Required");
                }

                if (binding.noteDescEditText.getText() == null ||
                        binding.noteDescEditText.getText().toString().isEmpty()) {
                    throw new EmptyNoteDescriptionException("Description is Required");
                }

                RemoteNoteCreateDto dto = new RemoteNoteCreateDto();
                dto.setHeading(binding.noteHeadingEditText.getText().toString());
                dto.setDesc(binding.noteDescEditText.getText().toString());
                dto.setTag(tag);

                Helpers.dismissKeyboard(this, v);

                noteCreateViewModel.createNote(authToken, dto);
            } catch (EmptyNoteHeadingException | EmptyNoteDescriptionException ex) {
                binding.noteSaveProgressBar.setVisibility(View.INVISIBLE);
                MaterialAlertDialogBuilder builder = buildAlertDialog("Error", ex.getMessage());
                builder.show();
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        TextView textView = (TextView) parent.getChildAt(0);
        if (textView != null) {
            textView.setTextColor(ContextCompat.getColor(this, android.R.color.white));
        }
        Log.d(TAG, "onItemSelected: " + spinnerList.get(position));
        if (!Objects.equals(spinnerList.get(position), "Tag")) {
            tag = spinnerList.get(position);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private MaterialAlertDialogBuilder buildAlertDialog(String title, String message) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this, R.style.MaterialAlertDialog);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        return builder;
    }
}
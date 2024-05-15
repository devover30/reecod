package info.devram.reecod.ui.note;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.rxjava2.RxPreferenceDataStoreBuilder;
import androidx.datastore.rxjava2.RxDataStore;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.gson.Gson;

import java.util.Objects;

import info.devram.reecod.BaseActivity;
import info.devram.reecod.R;
import info.devram.reecod.data.model.UserEntity;
import info.devram.reecod.databinding.ActivityNoteCreateBinding;
import info.devram.reecod.libs.Constants;
import info.devram.reecod.libs.DataStoreHelper;
import info.devram.reecod.libs.DataStoreSingleton;
import info.devram.reecod.ui.dashboard.DashboardActivity;
import info.devram.reecod.ui.dashboard.DashboardViewModel;
import info.devram.reecod.ui.dashboard.DashboardViewModelFactory;

public class NoteCreateActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "NoteCreateActivity";
    private DashboardViewModel viewModel;

    private DataStoreHelper dataStoreHelper;
    private ActivityNoteCreateBinding binding;
    private String authToken = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_note_create);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        binding = ActivityNoteCreateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = new ViewModelProvider(this, new DashboardViewModelFactory())
                .get(DashboardViewModel.class);
//        entryCreateViewModel = new ViewModelProvider(this, new EntryCreateViewModelFactory())
//                .get(EntryCreateViewModel.class);
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
                //entryCreateViewModel.acquireEntryTypes(authToken);
            }
        });

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == binding.noteSaveButton.getId()) {
            binding.noteSaveProgressBar.setVisibility(View.VISIBLE);
        }
    }
}
package info.devram.reecod.ui.dashboard;

import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.rxjava2.RxPreferenceDataStoreBuilder;
import androidx.datastore.rxjava2.RxDataStore;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;

import java.util.Objects;

import info.devram.reecod.BaseActivity;
import info.devram.reecod.R;
import info.devram.reecod.adapters.DashboardAdapter;
import info.devram.reecod.data.model.UserEntity;
import info.devram.reecod.databinding.ActivityDashboardBinding;
import info.devram.reecod.libs.Constants;
import info.devram.reecod.libs.DataStoreHelper;
import info.devram.reecod.libs.DataStoreSingleton;

public class DashboardActivity extends BaseActivity {
    private static final String TAG = "DashboardActivity";

    private NavController navController;
    private ActivityDashboardBinding binding;
    private DashboardViewModel viewModel;
    private DataStoreHelper dataStoreHelper;
    private String authToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        viewModel = new ViewModelProvider(this, new DashboardViewModelFactory())
                .get(DashboardViewModel.class);
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
        binding.floatingActionButton.setOnClickListener(view -> {
//            Intent intent = new Intent(DashboardActivity.this, EntryCreateActivity.class);
//            startActivity(intent);
//            finish();
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        subscribeObservers();
    }

    private void subscribeObservers() {
        String userEntity = dataStoreHelper.getStringValue(Constants.USER_DATA);
        Log.d(TAG, "subscribeObservers: " + userEntity);
        if (!Objects.equals(userEntity, "null")) {
            Gson gson = new Gson();
            UserEntity user = gson.fromJson(userEntity, UserEntity.class);
            viewModel.setAuthTokenLiveData(user.getToken());
        }

        viewModel.authTokenObserver().observe(this, token -> {
            this.authToken = token;
            viewModel.fetchNotes(authToken);
        });

        viewModel.notesResult().observe(this, resultData -> {

            switch (resultData.getStatus()) {
                case ERROR -> {
                    Log.d(TAG, "subscribeObservers: " + resultData.getException());
                }
                case NOTES_LIST_SUCCESS -> {
                    LinearLayoutManager layout = new LinearLayoutManager(this);
                    DashboardAdapter adapter = new DashboardAdapter(resultData.getData());
                    binding.dashboardRV.setLayoutManager(layout);
                    binding.dashboardRV.setAdapter(adapter);
                }
            }
        });
    }
}
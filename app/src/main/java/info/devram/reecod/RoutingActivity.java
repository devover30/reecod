package info.devram.reecod;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.core.splashscreen.SplashScreen;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.rxjava2.RxPreferenceDataStoreBuilder;
import androidx.datastore.rxjava2.RxDataStore;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;

import java.util.Objects;

import info.devram.reecod.data.model.UserEntity;
import info.devram.reecod.libs.AppInstallInfo;
import info.devram.reecod.libs.Constants;
import info.devram.reecod.libs.DataStoreHelper;
import info.devram.reecod.libs.DataStoreSingleton;
import info.devram.reecod.ui.Auth.AuthActivity;
import info.devram.reecod.ui.Auth.AuthViewModel;
import info.devram.reecod.ui.Auth.LoginViewModelFactory;
import info.devram.reecod.ui.MainActivity;

public class RoutingActivity extends BaseActivity {

    private static final String TAG = "RoutingActivity";
    private AuthViewModel authViewModel;
    private DataStoreHelper dataStoreHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        splashScreen.setKeepOnScreenCondition(() -> true);
        DataStoreSingleton dataStoreSingleton = DataStoreSingleton.getInstance();
        RxDataStore<Preferences> dataStoreRX;
        if (dataStoreSingleton.getDataStore() == null) {
            dataStoreRX = new RxPreferenceDataStoreBuilder(this, Constants.USER_DATA).build();
        } else {
            dataStoreRX = dataStoreSingleton.getDataStore();
        }
        dataStoreSingleton.setDataStore(dataStoreRX);
        dataStoreHelper = new DataStoreHelper(dataStoreRX);

        if (AppInstallInfo.checkFirstRun(this)) {
            startLoginActivity();
            return;
        }
        if (AppInstallInfo.checkUpgrade(this)) {
            dataStoreHelper.clearStringValue(Constants.USER_DATA);
            startLoginActivity();
            return;
        }
        authViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(AuthViewModel.class);
        subscribeObservers();
        Thread.setDefaultUncaughtExceptionHandler(handleAppCrash);
    }

    private void subscribeObservers() {
        String data;
        data = dataStoreHelper.getStringValue(Constants.USER_DATA);
        Log.d(TAG, "subscribeObservers: " + data);
        if (!Objects.equals(data, "null")) {
            Gson gson = new Gson();
            UserEntity user = gson.fromJson(data, UserEntity.class);
            authViewModel.validateAuthToken(user);
        } else {
            startLoginActivity();
        }

        authViewModel.getAuthToken().observe(this, hashMapResource -> {
            switch (hashMapResource.getStatus()) {
                case ERROR -> {
                    if (hashMapResource.getException() != null) {
                        String userDetails = dataStoreHelper.getStringValue(Constants.USER_DATA);
                        if (!Objects.equals(userDetails, "null")) {
                            Gson gson = new Gson();
                            UserEntity user = gson.fromJson(userDetails, UserEntity.class);
                            Log.d(TAG, "subscribeObservers: observe auth token error " + user);
                            authViewModel.loginWithEmail(user.getEmail(), user.getPassword());
                        }
                    } else {
                        startLoginActivity();
                    }
                }
                case TOKEN_VERIFY_SUCCESS -> {
                    if (hashMapResource.getData() != null) {
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.postDelayed(this::startDashboardActivity, 500);

                    }
                }
            }
        });

        authViewModel.getRemoteLoginResultData().observe(this, result -> {
            switch (result.getStatus()) {
                case ERROR:
                    if (result.getException() != null) {
                        startLoginActivity();
                    }
                    break;
                case LOGIN_SUCCESS:
                    if (result.getData() != null) {
                        //TODO: navigate to dashboard
                        Gson gson = new Gson();
                        String userEntity = gson.toJson(result.getData());
                        Log.d(TAG, "subscribeObservers: user " + userEntity);
                        dataStoreHelper.putStringValue(Constants.USER_DATA, userEntity);
                        startDashboardActivity();
                    }
                    break;
            }
        });

    }

    private void startLoginActivity() {
        Intent intent = new Intent(RoutingActivity.this, AuthActivity.class);
        startActivity(intent);
        finish();
    }

    private void startDashboardActivity() {
        Intent intent = new Intent(RoutingActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
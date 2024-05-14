package info.devram.reecod.ui.Auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.rxjava2.RxPreferenceDataStoreBuilder;
import androidx.datastore.rxjava2.RxDataStore;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;

import info.devram.reecod.databinding.FragmentAuthStartBinding;
import info.devram.reecod.libs.Constants;
import info.devram.reecod.libs.DataStoreHelper;
import info.devram.reecod.libs.DataStoreSingleton;
import info.devram.reecod.ui.dashboard.DashboardActivity;


public class AuthLoginFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "AuthLoginFragment";

    private FragmentAuthStartBinding binding;

    private AuthViewModel authViewModel;
    private DataStoreHelper dataStoreHelper;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAuthStartBinding.inflate(inflater, container, false);
        DataStoreSingleton dataStoreSingleton = DataStoreSingleton.getInstance();
        RxDataStore<Preferences> dataStoreRX;
        if (dataStoreSingleton.getDataStore() == null) {
            dataStoreRX = new RxPreferenceDataStoreBuilder(requireActivity(), Constants.USER_DATA).build();
        } else {
            dataStoreRX = dataStoreSingleton.getDataStore();
        }
        dataStoreSingleton.setDataStore(dataStoreRX);
        dataStoreHelper = new DataStoreHelper(dataStoreRX);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.loginStartProgress.setVisibility(View.INVISIBLE);
        binding.emailLoginBtn.setOnClickListener(this);
        authViewModel = new ViewModelProvider(requireActivity(), new LoginViewModelFactory())
                .get(AuthViewModel.class);
    }

    @Override
    public void onResume() {
        super.onResume();
        subscribeObservers();
    }

    private void subscribeObservers() {


        authViewModel.getRemoteLoginResultData().observe(getViewLifecycleOwner(), result -> {
            switch (result.getStatus()) {
                case ERROR:
                    if (result.getException() != null) {
                        binding.loginStartProgress.setVisibility(View.INVISIBLE);
                        showLoginFailed(result.getException().getMessage());
                    }
                    break;
                case LOGIN_SUCCESS:
                    if (result.getData() != null) {
                        //TODO: navigate to dashboard
                        binding.loginStartProgress.setVisibility(View.INVISIBLE);
                        //SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(Constants.USER_DATA, Context.MODE_PRIVATE);
                        Gson gson = new Gson();
                        String userEntity = gson.toJson(result.getData());
                        Log.d(TAG, "subscribeObservers: user " + userEntity);
                        dataStoreHelper.putStringValue(Constants.USER_DATA, userEntity);
                        Log.d(TAG, "subscribeObservers: " + dataStoreHelper.getStringValue(Constants.USER_DATA));
                        //sharedPreferences.edit().putString(Constants.USER_DETAILS, userEntity).apply();
                        Intent intent = new Intent(requireActivity(), DashboardActivity.class);

                        startActivity(intent);
                        requireActivity().finish();
                    }
                    break;
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == binding.emailLoginBtn.getId()) {
            binding.loginStartProgress.setVisibility(View.VISIBLE);
            String email = binding.email.getText().toString();
            String password = binding.password.getText().toString();
            authViewModel.loginWithEmail(email, password);
        }
    }

    private void showLoginFailed(String errorString) {
        Toast.makeText(requireActivity(), errorString, Toast.LENGTH_LONG).show();
    }
}
package info.devram.reecod.ui.Auth;

import android.os.Bundle;

import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import info.devram.reecod.BaseActivity;
import info.devram.reecod.R;
import info.devram.reecod.databinding.ActivityAuthBinding;

public class AuthActivity extends BaseActivity {

    private static final String TAG = "LoginActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityAuthBinding binding = ActivityAuthBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setUpNavigation();
        Thread.setDefaultUncaughtExceptionHandler(handleAppCrash);
    }

    private void setUpNavigation() {
        NavHostFragment navHostFragment = (NavHostFragment)
                getSupportFragmentManager().findFragmentById(R.id.loginFragmentContainer);
        assert navHostFragment != null;
        NavController navController = navHostFragment.getNavController();
    }
}
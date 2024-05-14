package info.devram.reecod.ui.Auth;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import info.devram.reecod.databinding.FragmentAuthVerifyBinding;
import info.devram.reecod.dtos.RemoteLoginDTO;


public class AuthVerifyFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "LoginVerifyFragment";

    private FragmentAuthVerifyBinding binding;

    private AuthViewModel authViewModel;

    private RemoteLoginDTO remoteLoginDTO = null;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAuthVerifyBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.loginEndProgress.setVisibility(View.INVISIBLE);
        binding.otpEditTextFirst.addTextChangedListener(new GenericTextWatcher(binding.otpEditTextFirst, binding.otpEditTextSecond));
        binding.otpEditTextSecond.addTextChangedListener(new GenericTextWatcher(binding.otpEditTextSecond, binding.otpEditTextThird));
        binding.otpEditTextThird.addTextChangedListener(new GenericTextWatcher(binding.otpEditTextThird, binding.otpEditTextFourth));
        binding.otpEditTextFourth.addTextChangedListener(new GenericTextWatcher(binding.otpEditTextFourth, binding.otpEditTextFifth));
        binding.otpEditTextFifth.addTextChangedListener(new GenericTextWatcher(binding.otpEditTextFifth, binding.otpEditTextSixth));
        binding.otpEditTextSixth.addTextChangedListener(new GenericTextWatcher(binding.otpEditTextSixth, null));

        binding.otpEditTextSecond.setOnKeyListener(new GenericKeyEvent(binding.otpEditTextSecond, binding.otpEditTextFirst));
        binding.otpEditTextThird.setOnKeyListener(new GenericKeyEvent(binding.otpEditTextThird, binding.otpEditTextSecond));
        binding.otpEditTextFourth.setOnKeyListener(new GenericKeyEvent(binding.otpEditTextFourth, binding.otpEditTextThird));
        binding.otpEditTextFifth.setOnKeyListener(new GenericKeyEvent(binding.otpEditTextFifth, binding.otpEditTextFourth));
        binding.otpEditTextSixth.setOnKeyListener(new GenericKeyEvent(binding.otpEditTextSixth, binding.otpEditTextFifth));
        binding.verify.setOnClickListener(this);
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
                    binding.loginEndProgress.setVisibility(View.INVISIBLE);
                    if (result.getException() != null) {
                        showLoginFailed(result.getException().getMessage());
                    }
                    break;
                case LOGIN_VERIFY_SUCCESS:
                    if (result.getData() != null) {
                        //TODO: navigate to dashboard activity
//                        remoteLoginDTO = result.getData();
//                        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(Constants.AUTH_PREFS_NAME, Context.MODE_PRIVATE);
//                        sharedPreferences.edit().putString(Constants.AUTH_PREF_TOKEN_KEY, remoteLoginDTO.getToken()).apply();
//                        binding.loginEndProgress.setVisibility(View.INVISIBLE);
//                        Intent intent = new Intent(requireActivity(), DashboardActivity.class);
//                        intent.putExtra(Constants.TOKEN_STRING, (String) result
//                                .getData().getToken());
//                        startActivity(intent);
//                        requireActivity().finish();
                    }
                    break;
            }
        });
    }

    private void showLoginFailed(String errorString) {
        Toast.makeText(requireActivity(), errorString, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == binding.verify.getId()) {
            binding.loginEndProgress.setVisibility(View.VISIBLE);
            Integer otp = Integer.parseInt(
                    binding.otpEditTextFirst.getText().toString() +
                            binding.otpEditTextSecond.getText().toString() +
                            binding.otpEditTextThird.getText().toString() +
                            binding.otpEditTextFourth.getText().toString() +
                            binding.otpEditTextFifth.getText().toString() +
                            binding.otpEditTextSixth.getText().toString()
            );
            authViewModel.verify(otp, remoteLoginDTO);
        }
    }

    private static class GenericTextWatcher implements TextWatcher {
        private final EditText currentView;
        private final EditText nextView;

        private GenericTextWatcher(EditText currentView, EditText nextView) {
            this.currentView = currentView;
            this.nextView = nextView;
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // TODO Auto-generated method stub
            String text = editable.toString();
            if (nextView != null && text.length() == 1) {
                nextView.requestFocus();
            }
            if (text.length() > 1) {
                currentView.setText(String.valueOf(text.charAt(text.length() - 1)));
                currentView.setSelection(1);
            }
        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
        }
    }

    private static class GenericKeyEvent implements View.OnKeyListener {

        private final EditText currentView;
        private final EditText previousView;

        public GenericKeyEvent(EditText currentView, EditText previousView) {
            this.currentView = currentView;
            this.previousView = previousView;
        }

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL && currentView.getText().toString().isEmpty()) {
                if (previousView != null) {
                    previousView.requestFocus();
                }
                return true;
            }
            return false;
        }


    }
}
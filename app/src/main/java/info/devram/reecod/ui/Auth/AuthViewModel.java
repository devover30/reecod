package info.devram.reecod.ui.Auth;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.math.BigInteger;

import info.devram.reecod.data.LoginService;
import info.devram.reecod.data.model.UserEntity;
import info.devram.reecod.dtos.RemoteLoginDTO;
import info.devram.reecod.libs.Result;

public class AuthViewModel extends ViewModel {

    private static final String TAG = "LoginViewModel";

    private final MutableLiveData<Result<UserEntity>> remoteLoginResult =
            new MutableLiveData<>();
    private final LoginService loginService;
    private final MutableLiveData<Result<UserEntity>> authToken = new MutableLiveData<>();

    AuthViewModel(LoginService loginService) {
        this.loginService = loginService;
    }


    public LiveData<Result<UserEntity>> getRemoteLoginResultData() {
        return remoteLoginResult;
    }

    public LiveData<Result<UserEntity>> getAuthToken() {
        return authToken;
    }

    public void loginWithEmail(String email, String password) {
        loginService.loginByEmail(email, password, remoteLoginResult);
    }

    public void verify(Integer otp, RemoteLoginDTO dto) {
        dto.setOtp(otp);
        loginService.verify(dto);
    }

    public void validateAuthToken(UserEntity user) {
        loginService.verifyAuthToken(user, authToken);
    }

}
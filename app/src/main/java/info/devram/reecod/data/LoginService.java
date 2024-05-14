package info.devram.reecod.data;

import androidx.lifecycle.MutableLiveData;

import info.devram.reecod.data.model.UserEntity;
import info.devram.reecod.dtos.RemoteLoginDTO;
import info.devram.reecod.dtos.RemoteLoginTokenResponse;
import info.devram.reecod.dtos.RemoteUserEntityDTO;
import info.devram.reecod.libs.Result;


/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class LoginService implements LoginDataSource.OnRemoteResponse {

    //private static final String TAG = "LoginService";

    private static volatile LoginService instance;

    private final LoginDataSource dataSource;

    private MutableLiveData<Result<UserEntity>> remoteLoginResult = null;

    private MutableLiveData<Result<UserEntity>> authTokenValidateResult = null;
    private UserEntity authTokenValidateUser = null;

    // private constructor : singleton access
    private LoginService(LoginDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static LoginService getInstance(LoginDataSource dataSource) {
        if (instance == null) {
            instance = new LoginService(dataSource);
        }
        return instance;
    }

    public void loginByEmail(String email, String password, MutableLiveData<Result<UserEntity>> remoteLoginResult) {
        this.remoteLoginResult = remoteLoginResult;
        dataSource.login(email, password,  this);
    }

    public void verify(RemoteLoginDTO dto) {
        dataSource.getToken(dto, this);
    }

    public void verifyAuthToken(UserEntity user, MutableLiveData<Result<UserEntity>> authTokenMutable) {
        this.authTokenValidateResult = authTokenMutable;
        this.authTokenValidateUser = user;
        dataSource.validateToken(user.getToken(), this);
    }

    @Override
    public void onLoginSuccess(RemoteLoginTokenResponse success, String email, String password) {
        if (remoteLoginResult != null) {
            UserEntity userEntity = new UserEntity();
            userEntity.setToken(success.getToken());
            userEntity.setEmail(email);
            userEntity.setPassword(password);
            remoteLoginResult.setValue(Result.loginSuccess(userEntity));
        }
    }

//    @Override
//    public void onTokenSuccess(RemoteLoginDTO dto) {
//        if (loginStartResult != null) {
//            loginStartResult.setValue(Result.loginVerifySuccess(dto));
//        }
//    }

    @Override
    public void onTokenValidateSuccess(RemoteUserEntityDTO user, String token) {
        if (authTokenValidateResult != null && authTokenValidateUser != null) {
            authTokenValidateUser.setEmail(user.getEmail());
            authTokenValidateUser.setPassword(user.getPassword());
            authTokenValidateUser.setToken(token);
            authTokenValidateResult.setValue(Result.verifyTokenSuccess(authTokenValidateUser));
        }
    }

    @Override
    public void onError(Result error) {
        if (remoteLoginResult != null) {
            remoteLoginResult.setValue(error);
        }
    }

    @Override
    public void onTokenValidateError(Result error) {
        if (authTokenValidateResult != null) {
            authTokenValidateResult.setValue(error);
        }
    }
}
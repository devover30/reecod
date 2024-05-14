package info.devram.reecod.data;

import android.util.Log;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


import info.devram.reecod.dtos.RemoteLoginDTO;
import info.devram.reecod.dtos.RemoteLoginTokenResponse;
import info.devram.reecod.dtos.RemoteUserEntityDTO;
import info.devram.reecod.exceptions.AppException;
import info.devram.reecod.libs.Result;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    //private static final String TAG = "LoginDataSource";

    public interface OnRemoteResponse {
        void onLoginSuccess(RemoteLoginTokenResponse success, String email, String password);

        void onTokenValidateSuccess(RemoteUserEntityDTO user, String token);

        void onError(Result error);

        void onTokenValidateError(Result error);
    }

    public void login(String email, String password, OnRemoteResponse callback) {

        Retrofit retrofit = RetrofitInstance.getInstance().getAuthClient();
        RemoteAuthInterface loginRemote = retrofit.create(RemoteAuthInterface.class);
        Call<RemoteLoginTokenResponse> call = loginRemote.loginAction(email, password);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<RemoteLoginTokenResponse> call, @NonNull Response<RemoteLoginTokenResponse> response) {
                if (!response.isSuccessful() && response.errorBody() != null) {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        String message = jObjError.getString("error");
                        callback.onError(Result.error(new AppException(message, response.code())));
                        return;
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                        return;
                    }
                }
                RemoteLoginTokenResponse res = response.body();
                callback.onLoginSuccess(res, email , password);
            }

            @Override
            public void onFailure(@NonNull Call<RemoteLoginTokenResponse> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void getToken(RemoteLoginDTO dto, OnRemoteResponse callback) {
        Retrofit retrofit = RetrofitInstance.getInstance().getAuthClient();
        RemoteAuthInterface loginRemote = retrofit.create(RemoteAuthInterface.class);
        Call<RemoteLoginTokenResponse> call = loginRemote.loginEndAction(dto);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<RemoteLoginTokenResponse> call, @NonNull Response<RemoteLoginTokenResponse> response) {
                if (!response.isSuccessful() && response.errorBody() != null) {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        String message = jObjError.getString("error");
                        callback.onError(Result.error(new AppException(message, response.code())));
                        return;
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                        return;
                    }
                }
                RemoteLoginTokenResponse res = response.body();
                assert res != null;
                dto.setToken(res.getToken());
                //callback.onTokenSuccess(dto);
            }

            @Override
            public void onFailure(@NonNull Call<RemoteLoginTokenResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void validateToken(String authToken, OnRemoteResponse callback) {
        Retrofit retrofit = RetrofitInstance.getInstance().getAuthClient();
        RemoteAuthInterface loginRemote = retrofit.create(RemoteAuthInterface.class);
        Call<RemoteUserEntityDTO> call = loginRemote.verifyToken("Bearer " + authToken);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<RemoteUserEntityDTO> call, @NonNull Response<RemoteUserEntityDTO> response) {
                if (!response.isSuccessful() && response.errorBody() != null) {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        String message = jObjError.getString("error");
                        callback.onTokenValidateError(Result.error(new AppException(message, response.code())));
                        return;
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                        return;
                    }
                }
                RemoteUserEntityDTO user = response.body();
                callback.onTokenValidateSuccess(user, authToken);
            }

            @Override
            public void onFailure(@NonNull Call<RemoteUserEntityDTO> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
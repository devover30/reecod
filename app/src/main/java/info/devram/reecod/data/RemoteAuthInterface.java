package info.devram.reecod.data;


import info.devram.reecod.dtos.RemoteLoginDTO;
import info.devram.reecod.dtos.RemoteLoginTokenResponse;
import info.devram.reecod.dtos.RemoteUserEntityDTO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface RemoteAuthInterface {
    @FormUrlEncoded
    @POST("auth")
    Call<RemoteLoginTokenResponse> loginAction(@Field("email") String email, @Field("password") String password);

    @POST("authentication/verify")
    Call<RemoteLoginTokenResponse> loginEndAction(@Body RemoteLoginDTO dto);

    @GET("auth")
    Call<RemoteUserEntityDTO> verifyToken(@Header("Authorization") String token);
}

package info.devram.reecod.dtos;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

@Keep
public class RemoteLoginTokenResponse {

    @SerializedName("token")
    private String token;


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


    @Override
    public String toString() {
        return "RemoteLoginEndResponse{" +
                "token='" + token + '\'' +
                '}';
    }
}

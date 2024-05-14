package info.devram.reecod.dtos;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

import java.math.BigInteger;

@Keep
public class RemoteUserEntityDTO {
    @SerializedName("id")
    private String id;
    @SerializedName("mobile")
    private BigInteger mobile;
    @SerializedName("email")
    private String email;
    @SerializedName("password")
    private String password;
    @SerializedName("is_verified")
    private String is_verified;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BigInteger getMobile() {
        return mobile;
    }

    public void setMobile(BigInteger mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIs_verified() {
        return is_verified;
    }

    public void setIs_verified(String is_verified) {
        this.is_verified = is_verified;
    }
}

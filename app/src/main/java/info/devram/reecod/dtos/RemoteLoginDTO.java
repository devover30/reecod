package info.devram.reecod.dtos;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

import java.math.BigInteger;

@Keep
public class RemoteLoginDTO {

    @SerializedName("hash")
    private String hash;
    @SerializedName("mobile")
    private BigInteger mobile;
    @SerializedName("otp")
    private Integer otp;
    @SerializedName("token")
    private String token;
    @SerializedName("refresh")
    private String refresh;
    @SerializedName("email")
    private String email;
    @SerializedName("password")
    private String password;

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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getOtp() {
        return otp;
    }

    public void setOtp(Integer otp) {
        this.otp = otp;
    }

    public BigInteger getMobile() {
        return mobile;
    }

    public void setMobile(BigInteger mobile) {
        this.mobile = mobile;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getRefresh() {
        return refresh;
    }

    public void setRefresh(String refresh) {
        this.refresh = refresh;
    }

    @Override
    public String toString() {
        return "RemoteLoginDTO{" +
                "hash='" + hash + '\'' +
                ", mobile=" + mobile +
                ", otp=" + otp +
                ", token='" + token + '\'' +
                ", refresh='" + refresh + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}

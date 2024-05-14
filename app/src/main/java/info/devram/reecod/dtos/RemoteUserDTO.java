package info.devram.reecod.dtos;

import com.google.gson.annotations.SerializedName;

public class RemoteUserDTO {
    @SerializedName("id")
    private String id;
    @SerializedName("mobile")
    private String mobile;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}

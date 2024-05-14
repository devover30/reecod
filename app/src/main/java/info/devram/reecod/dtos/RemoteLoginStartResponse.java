package info.devram.reecod.dtos;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

@Keep
public class RemoteLoginStartResponse {
    @SerializedName("hash")
    private String hash;

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    @Override
    public String toString() {
        return "RemoteLoginStartResponse{" +
                "hash='" + hash + '\'' +
                '}';
    }
}

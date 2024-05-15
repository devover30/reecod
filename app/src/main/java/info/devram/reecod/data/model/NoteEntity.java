package info.devram.reecod.data.model;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

@Keep
public class NoteEntity {
    @SerializedName("id")
    private String id;
    @SerializedName("heading")
    private String heading;
    @SerializedName("description")
    private String desc;
    @SerializedName("tag")
    private String tag;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("modified_at")
    private String modifiedAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(String modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    @Override
    public String toString() {
        return "NoteEntity{" +
                "id='" + id + '\'' +
                ", heading='" + heading + '\'' +
                ", desc='" + desc + '\'' +
                ", tag='" + tag + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", modifiedAt='" + modifiedAt + '\'' +
                '}';
    }
}

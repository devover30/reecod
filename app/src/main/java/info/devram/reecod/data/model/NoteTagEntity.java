package info.devram.reecod.data.model;

import androidx.annotation.Keep;

@Keep
public class NoteTagEntity {
    private String tag;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return "NoteTagEntity{" +
                "tag='" + tag + '\'' +
                '}';
    }
}

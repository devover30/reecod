package info.devram.reecod.data;

import java.util.List;

import info.devram.reecod.data.model.NoteEntity;
import info.devram.reecod.data.model.NoteTagEntity;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface RemoteAppDataInterface {
    @GET("notes")
    Call<List<NoteEntity>> getNotesFromApi(@Header("Authorization") String token);

    @GET("notes/tags")
    Call<List<NoteTagEntity>> getNotesTagsFromApi(@Header("Authorization") String token);
}

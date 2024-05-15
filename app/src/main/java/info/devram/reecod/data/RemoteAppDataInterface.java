package info.devram.reecod.data;

import java.util.List;

import info.devram.reecod.data.model.NoteEntity;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface RemoteAppDataInterface {
    @GET("notes")
    Call<List<NoteEntity>> getNotesFromApi(@Header("Authorization") String token);
}

package info.devram.reecod.data;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import info.devram.reecod.data.model.NoteEntity;
import info.devram.reecod.data.model.NoteTagEntity;
import info.devram.reecod.exceptions.AppException;
import info.devram.reecod.libs.Result;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class NotesDataSource {

    //private static final String TAG = "DashboardDataSource";

    public interface RemoteResponseListener {
        void onGetNotes(List<NoteEntity> noteEntities);
        void onGetNotesTags(List<NoteTagEntity> noteTagEntities);
        void onError(Result error);
    }

    public void getNotes(String token, RemoteResponseListener listener) {
        Retrofit retrofit = RetrofitInstance.getInstance().getAPIClient();
        RemoteAppDataInterface remote = retrofit.create(RemoteAppDataInterface.class);
        Call<List<NoteEntity>> call = remote.getNotesFromApi("Bearer " + token);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<List<NoteEntity>> call, @NonNull Response<List<NoteEntity>> response) {
                if (!response.isSuccessful() && response.errorBody() != null) {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        String message = jObjError.getString("error");
                        listener.onError(Result.error(new AppException(message, response.code())));
                        return;
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                        return;
                    }
                }
                List<NoteEntity> list = response.body();
                listener.onGetNotes(list);
            }

            @Override
            public void onFailure(@NonNull Call<List<NoteEntity>> call, @NonNull Throwable t) {

            }
        });
    }

    public void getNotesTags(String token, RemoteResponseListener listener) {
        Retrofit retrofit = RetrofitInstance.getInstance().getAPIClient();
        RemoteAppDataInterface remote = retrofit.create(RemoteAppDataInterface.class);
        Call<List<NoteTagEntity>> call = remote.getNotesTagsFromApi("Bearer " + token);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<List<NoteTagEntity>> call, @NonNull Response<List<NoteTagEntity>> response) {
                if (!response.isSuccessful() && response.errorBody() != null) {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        String message = jObjError.getString("error");
                        listener.onError(Result.error(new AppException(message, response.code())));
                        return;
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                        return;
                    }
                }
                List<NoteTagEntity> list = response.body();
                listener.onGetNotesTags(list);
            }

            @Override
            public void onFailure(@NonNull Call<List<NoteTagEntity>> call, @NonNull Throwable t) {

            }
        });
    }
}

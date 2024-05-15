package info.devram.reecod.data;


import androidx.lifecycle.MutableLiveData;

import java.util.List;

import info.devram.reecod.data.model.NoteEntity;
import info.devram.reecod.data.model.NoteTagEntity;
import info.devram.reecod.libs.Result;

public class NotesDataService implements NotesDataSource.RemoteResponseListener {

    private static final String TAG = "DashboardService";

    private static volatile NotesDataService instance;
    private final NotesDataSource dataSource;
    private MutableLiveData<Result<List<NoteEntity>>> notesResult = null;
    private MutableLiveData<Result<List<NoteTagEntity>>> notesTagsResult = null;

    private NotesDataService(NotesDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static NotesDataService getInstance(NotesDataSource dataSource) {
        if (instance == null) {
            instance = new NotesDataService(dataSource);
        }
        return instance;
    }

    public void getNotes(String token, MutableLiveData<Result<List<NoteEntity>>> liveData) {
        this.notesResult = liveData;
        dataSource.getNotes(token, this);
    }

    public void getNotesTags(String token, MutableLiveData<Result<List<NoteTagEntity>>> liveData) {
        this.notesTagsResult = liveData;
        dataSource.getNotesTags(token, this);
    }

    @Override
    public void onGetNotes(List<NoteEntity> noteEntities) {
        if (this.notesResult != null) {
            notesResult.setValue(Result.notesListFetchSuccess(noteEntities));
        }
    }

    @Override
    public void onGetNotesTags(List<NoteTagEntity> noteTagEntities) {
        if (this.notesTagsResult != null) {
            notesTagsResult.setValue(Result.notesTagsFetchSuccess(noteTagEntities));
        }
    }

    @Override
    public void onError(Result error) {

    }
}

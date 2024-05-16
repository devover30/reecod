package info.devram.reecod.data;


import androidx.lifecycle.MutableLiveData;

import java.util.List;

import info.devram.reecod.data.model.NoteEntity;
import info.devram.reecod.data.model.NoteTagEntity;
import info.devram.reecod.dtos.RemoteNoteCreateDto;
import info.devram.reecod.libs.Result;

public class NotesDataService implements NotesDataSource.RemoteResponseListener {

    private static final String TAG = "DashboardService";

    private static volatile NotesDataService instance;
    private final NotesDataSource dataSource;
    private MutableLiveData<Result<List<NoteEntity>>> notesResult = null;
    private MutableLiveData<Result<List<NoteTagEntity>>> notesTagsResult = null;
    private MutableLiveData<Result<NoteEntity>> noteCreateResult = null;

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

    public void postNote(String token, RemoteNoteCreateDto dto, MutableLiveData<Result<NoteEntity>> liveData) {
        this.noteCreateResult = liveData;
        dataSource.postNote(token, dto, this);
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
    public void onCreateNote(NoteEntity entity) {
        if (this.noteCreateResult != null) {
            noteCreateResult.postValue(Result.noteCreateSuccess(entity));
        }
    }

    @Override
    public void onError(Result error, String errorCalledFrom) {
        switch (errorCalledFrom) {
            case "get notes" -> {
                if (this.notesResult != null) {
                    notesResult.setValue(error);
                }
            }
            case "get notes tags" -> {
                if (this.notesTagsResult != null) {
                    notesTagsResult.setValue(error);
                }
            }
            case "post note" -> {
                if (this.noteCreateResult != null) {
                    noteCreateResult.setValue(error);
                }
            }
        }
    }
}

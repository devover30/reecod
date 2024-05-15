package info.devram.reecod.ui.note;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import info.devram.reecod.data.NotesDataService;
import info.devram.reecod.data.model.NoteTagEntity;
import info.devram.reecod.libs.Result;

public class NoteCreateViewModel extends ViewModel {

    private final NotesDataService service;

    private final MutableLiveData<Result<List<NoteTagEntity>>> notesTagsResult = new MutableLiveData<>();

    public NoteCreateViewModel(NotesDataService service) {
        this.service = service;
    }

    public LiveData<Result<List<NoteTagEntity>>> notesTagsResult() {
        return notesTagsResult;
    }

    public void fetchNotesTags(String token) {
        service.getNotesTags(token, notesTagsResult);
    }
}

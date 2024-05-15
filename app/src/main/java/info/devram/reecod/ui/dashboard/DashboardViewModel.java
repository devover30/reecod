package info.devram.reecod.ui.dashboard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import info.devram.reecod.data.NotesDataService;
import info.devram.reecod.data.model.NoteEntity;
import info.devram.reecod.libs.Result;

public class DashboardViewModel extends ViewModel {

    private final NotesDataService service;
    private final MutableLiveData<String> authTokenLiveData = new MutableLiveData<>();
    private final MutableLiveData<Result<List<NoteEntity>>> notesResult = new MutableLiveData<>();

    public DashboardViewModel(NotesDataService service) {
        this.service = service;
    }

    public LiveData<String> authTokenObserver() {
        return authTokenLiveData;
    }

    public LiveData<Result<List<NoteEntity>>> notesResult() {
        return  notesResult;
    }

    public void setAuthTokenLiveData(String token) {
        authTokenLiveData.setValue(token);
    }

    public void fetchNotes(String token) {
        service.getNotes(token, notesResult);
    }
}
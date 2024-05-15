package info.devram.reecod.ui.note;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import info.devram.reecod.data.NotesDataService;
import info.devram.reecod.data.NotesDataSource;

public class NoteCreateViewModelFactory implements ViewModelProvider.Factory {
    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(NoteCreateViewModel.class)) {
            return (T) new NoteCreateViewModel(NotesDataService.getInstance(new NotesDataSource()));
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}

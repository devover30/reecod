package info.devram.reecod.data;


import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import info.devram.reecod.data.model.NoteEntity;
import info.devram.reecod.libs.Result;

public class DashboardService implements DashboardDataSource.RemoteResponseListener {

    private static final String TAG = "DashboardService";

    private static volatile DashboardService instance;
    private final DashboardDataSource dataSource;
    private MutableLiveData<Result<List<NoteEntity>>> notesResult = null;

    private DashboardService(DashboardDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static DashboardService getInstance(DashboardDataSource dataSource) {
        if (instance == null) {
            instance = new DashboardService(dataSource);
        }
        return instance;
    }

    public void getNotes(String token, MutableLiveData<Result<List<NoteEntity>>> liveData) {
        this.notesResult = liveData;
        dataSource.getNotes(token, this);
    }

    @Override
    public void onGetNotes(List<NoteEntity> noteEntities) {
        if (this.notesResult != null) {
            notesResult.setValue(Result.notesListFetchSuccess(noteEntities));
        }
    }

    @Override
    public void onError(Result error) {

    }
}

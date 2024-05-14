package info.devram.reecod.ui.dashboard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import info.devram.reecod.data.DashboardService;

public class DashboardViewModel extends ViewModel {

    private final DashboardService service;
    private final MutableLiveData<String> authTokenLiveData = new MutableLiveData<>();

    public DashboardViewModel(DashboardService service) {
        this.service = service;
    }

    public LiveData<String> authTokenObserver() {
        return authTokenLiveData;
    }

    public void setAuthTokenLiveData(String token) {
        authTokenLiveData.setValue(token);
    }

}
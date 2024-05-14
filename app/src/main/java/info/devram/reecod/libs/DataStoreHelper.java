package info.devram.reecod.libs;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.datastore.preferences.core.MutablePreferences;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.core.PreferencesKeys;
import androidx.datastore.rxjava2.RxDataStore;

import java.util.Map;
import io.reactivex.Single;

public class DataStoreHelper {
    private static final String TAG = "DataStoreHelper";
    RxDataStore<Preferences> dataStoreRX;
    Preferences pref_error = new Preferences() {
        @Nullable
        @Override
        public <T> T get(@NonNull Key<T> key) {
            return null;
        }

        @Override
        public <T> boolean contains(@NonNull Key<T> key) {
            return false;
        }

        @NonNull
        @Override
        public Map<Key<?>, Object> asMap() {
            return null;
        }
    };

    public DataStoreHelper(RxDataStore<Preferences> dataStoreRX) {
        this.dataStoreRX = dataStoreRX;
    }

    public boolean putStringValue(String Key, String value){
        boolean returnable;
        Preferences.Key<String> PREF_KEY = PreferencesKeys.stringKey(Key);
        Single<Preferences> updateResult =  dataStoreRX.updateDataAsync(prefsIn -> {
            MutablePreferences mutablePreferences = prefsIn.toMutablePreferences();
            mutablePreferences.set(PREF_KEY, value);
            return Single.just(mutablePreferences);
        }).onErrorReturnItem(pref_error);
        returnable = updateResult.blockingGet() != pref_error;
        return returnable;
    }
    public String getStringValue(String Key) {
        Preferences.Key<String> PREF_KEY = PreferencesKeys.stringKey(Key);
        Single<String> value = dataStoreRX.data().firstOrError().map(prefs -> prefs.get(PREF_KEY)).onErrorReturnItem("null");
        return value.blockingGet();
    }

    public boolean putIntValue(String key, Integer value) {
        boolean returnable;
        Preferences.Key<Integer> PREF_KEY = PreferencesKeys.intKey(key);
        Log.d(TAG, "putIntValue: " + PREF_KEY);
        Single<Preferences> updateResult = dataStoreRX.updateDataAsync(preferences -> {
            MutablePreferences mutablePreferences = preferences.toMutablePreferences();
            mutablePreferences.set(PREF_KEY, value);
            return Single.just(mutablePreferences);
        }).onErrorReturnItem(pref_error);
        returnable = updateResult.blockingGet() != pref_error;
        return returnable;
    }

    public Integer getIntegerValue(String key) {
        Preferences.Key<Integer> PREF_KEY = PreferencesKeys.intKey(key);
        Single<Integer> value = dataStoreRX.data().firstOrError()
                .map(preferences -> preferences.get(PREF_KEY))
                .onErrorReturnItem(Constants.NOT_EXIST);
        return value.blockingGet();
    }

    public void clearStringValue(String key) {
        Preferences.Key<Integer> PREF_KEY = PreferencesKeys.intKey(key);
        Log.d(TAG, "putIntValue: " + PREF_KEY);
        Single<Preferences> updateResult = dataStoreRX.updateDataAsync(preferences -> {
            MutablePreferences mutablePreferences = preferences.toMutablePreferences();
            mutablePreferences.clear();
            return Single.just(mutablePreferences);
        }).onErrorReturnItem(pref_error);
    }

    public void clearIntegerValue(String key) {
        Preferences.Key<String> PREF_KEY = PreferencesKeys.stringKey(key);
        Log.d(TAG, "putIntValue: " + PREF_KEY);
        Single<Preferences> updateResult = dataStoreRX.updateDataAsync(preferences -> {
            MutablePreferences mutablePreferences = preferences.toMutablePreferences();
            mutablePreferences.clear();
            return Single.just(mutablePreferences);
        }).onErrorReturnItem(pref_error);
    }
}

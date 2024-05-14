package info.devram.reecod.libs;

import android.content.Context;

import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.rxjava2.RxPreferenceDataStoreBuilder;
import androidx.datastore.rxjava2.RxDataStore;

import info.devram.reecod.BuildConfig;


public class AppInstallInfo {

    private static final String TAG = "AppInstallInfo";
    private static final String PREFS_NAME = "app_check_run";
    private static final String PREF_VERSION_CODE_KEY = "version_code";
    private static final int NOT_EXIST = -1;

    private static final Integer currentVersionCode = BuildConfig.VERSION_CODE;

    public static Boolean checkFirstRun(Context context) {
        DataStoreSingleton dataStoreSingleton = DataStoreSingleton.getInstance();
        RxDataStore<Preferences> dataStoreRX;
        if (dataStoreSingleton.getDataStore() == null) {
            dataStoreRX = new RxPreferenceDataStoreBuilder(context, PREFS_NAME).build();
        } else {
            dataStoreRX = dataStoreSingleton.getDataStore();
        }
        dataStoreSingleton.setDataStore(dataStoreRX);
        DataStoreHelper dataStoreHelper = new DataStoreHelper(dataStoreRX);
        int savedVersionCode = dataStoreHelper.getIntegerValue(PREF_VERSION_CODE_KEY);
        if (savedVersionCode == NOT_EXIST) {
            // This is a new install (or the user cleared the shared preferences)
            dataStoreHelper.putIntValue(PREF_VERSION_CODE_KEY, currentVersionCode);
            return true;
        }
        return false;
    }

    public static Boolean checkUpgrade(Context context) {
        DataStoreSingleton dataStoreSingleton = DataStoreSingleton.getInstance();
        RxDataStore<Preferences> dataStoreRX;
        if (dataStoreSingleton.getDataStore() == null) {
            dataStoreRX = new RxPreferenceDataStoreBuilder(context, PREFS_NAME).build();
        } else {
            dataStoreRX = dataStoreSingleton.getDataStore();
        }
        dataStoreSingleton.setDataStore(dataStoreRX);
        DataStoreHelper dataStoreHelper = new DataStoreHelper(dataStoreRX);
        int savedVersionCode = dataStoreHelper.getIntegerValue(PREF_VERSION_CODE_KEY);
        if (currentVersionCode > savedVersionCode) {
            dataStoreHelper.putIntValue(PREF_VERSION_CODE_KEY, currentVersionCode);
            return true;
        }
        return false;
    }
}

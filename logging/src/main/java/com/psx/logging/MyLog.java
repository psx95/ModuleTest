package com.psx.logging;

import androidx.work.BackoffPolicy;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.psx.commons.MainApplication;
import com.psx.logging.RealmDB.RealmLoggingModule;
import com.psx.logging.RealmDB.RealmLogs;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import timber.log.Timber;

//TODO : Remove Realm's Set DefaultInstance
public class MyLog {

    public static final String UPLOAD_URL = "https://sometesturl.com/upload_logs";
    private static MainApplication applicationInstance;
    private static RealmConfiguration libraryConfig;
    private static final long REPEAT_INTERVAL_HRS_UPLOAD_TASK = 5; //TODO : Change it to 24

    public static void init(MainApplication applicationInstance) {
        MyLog.applicationInstance = applicationInstance;
        if (BuildConfig.DEBUG) Timber.plant(new Timber.DebugTree());
        Realm.init(applicationInstance.getCurrentApplication());
        libraryConfig = new RealmConfiguration.Builder()
                .name("library.realm")
                .modules(new RealmLoggingModule())
                .build();
        Realm.setDefaultConfiguration(libraryConfig);
        scheduleUploadJob();
    }

    public static void e(String message, String className) {
        Timber.tag(className);
        Timber.e(message);
        saveLogToDBAsync(message, className);
    }

    public static void d(String message, String className) {
        Timber.tag(className);
        Timber.d(message);
        //saveLogToDBAsync(message, className);
    }

    public static void i(String message, String className) {
        Timber.tag(className);
        Timber.i(message);
        saveLogToDBAsync(message, className);
    }

    public static void v(String message, String className) {
        Timber.tag(className);
        Timber.v(message);
        saveLogToDBAsync(message, className);
    }

    private static void saveLogToDBAsync(final String message, final String className) {
        try (Realm realm = Realm.getInstance(libraryConfig)) {
            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(@NotNull Realm realm) {
                    RealmLogs realmLogs = realm.createObject(RealmLogs.class, System.currentTimeMillis());
                    realmLogs.className = className;
                    realmLogs.message = message;
                    realm.insertOrUpdate(realmLogs);
                }
            });
        }
    }

    private static void scheduleUploadJob() {
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresBatteryNotLow(true)
                .build();
        // TODO : Change the Repeat interval to HOURS (24)
        PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(UploadWorker.class, REPEAT_INTERVAL_HRS_UPLOAD_TASK, TimeUnit.SECONDS)
                .setConstraints(constraints)
                .setBackoffCriteria(BackoffPolicy.LINEAR,
                        PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS,
                        TimeUnit.MILLISECONDS)
                .build();

        WorkManager.getInstance().enqueue(periodicWorkRequest);
    }

    public static MainApplication getApplicationInstance() {
        return applicationInstance;
    }
}

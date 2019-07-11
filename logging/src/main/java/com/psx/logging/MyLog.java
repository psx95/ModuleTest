package com.psx.logging;

import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.psx.commons.MainApplication;
import com.psx.logging.RealmDB.RealmLoggingModule;
import com.psx.logging.RealmDB.RealmLogs;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import timber.log.Timber;

public class MyLog {

    private static MainApplication applicationInstance;
    static String uploadUrl;
    private static RealmConfiguration libraryConfig;
    private static LoggingLevel loggingLevel;
    private static final long INITIAL_DELAY_SECONDS_ONE_TIME_UPLOAD = 10;
    private static final String UPLOAD_TASK_NAME = "logs_upload";

    public static void init(MainApplication applicationInstance, String upload_url, LoggingLevel loggingLevel) {
        MyLog.applicationInstance = applicationInstance;
        if (BuildConfig.DEBUG) Timber.plant(new Timber.DebugTree());
        MyLog.uploadUrl = upload_url;
        MyLog.loggingLevel = loggingLevel;
        Realm.init(applicationInstance.getCurrentApplication());
        libraryConfig = new RealmConfiguration.Builder()
                .name("library.realm")
                .modules(new RealmLoggingModule())
                .build();
        scheduleUploadJob();
    }

    public static void e(String message, String className) {
        Timber.tag(className);
        Timber.e(message);
        if (loggingLevel == LoggingLevel.ERRORS_ONLY || loggingLevel == LoggingLevel.VERBOSE)
            saveLogToDBAsync(message, className);
    }

    public static void d(String message, String className) {
        Timber.tag(className);
        Timber.d(message);
        if (loggingLevel == LoggingLevel.VERBOSE || loggingLevel == LoggingLevel.DEBUG_INFO_ONLY || loggingLevel == LoggingLevel.DEBUG_ONLY)
            saveLogToDBAsync(message, className);
    }

    public static void i(String message, String className) {
        Timber.tag(className);
        Timber.i(message);
        if (loggingLevel == LoggingLevel.VERBOSE || loggingLevel == LoggingLevel.DEBUG_INFO_ONLY)
            saveLogToDBAsync(message, className);
    }

    public static void v(String message, String className) {
        Timber.tag(className);
        Timber.v(message);
        if (loggingLevel == LoggingLevel.VERBOSE)
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

    //Note: The minimum repeat interval that can be defined is 15 minutes (same as the JobScheduler API).
    private static void scheduleUploadJob() {
        Timber.d("Scheduling task");
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresBatteryNotLow(true)
                .build();
        OneTimeWorkRequest uploadLogsWorkRequest = new OneTimeWorkRequest.Builder(UploadWorker.class)
                .setConstraints(constraints)
                .setInitialDelay(INITIAL_DELAY_SECONDS_ONE_TIME_UPLOAD, TimeUnit.SECONDS)
                .build();

        WorkManager.getInstance().cancelAllWork();
        WorkManager.getInstance().enqueue(uploadLogsWorkRequest);
    }

    public static MainApplication getApplicationInstance() {
        return applicationInstance;
    }

    static RealmConfiguration getLibraryConfig() {
        return libraryConfig;
    }
}

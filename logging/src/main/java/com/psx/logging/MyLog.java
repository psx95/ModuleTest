package com.psx.logging;

import com.psx.commons.MainApplication;
import com.psx.logging.RealmDB.RealmLoggingModule;
import com.psx.logging.RealmDB.RealmLogs;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import timber.log.Timber;

public class MyLog {

    private static MainApplication applicationInstance;
    private static RealmConfiguration libraryConfig;

    public static void init(MainApplication applicationInstance) {
        MyLog.applicationInstance = applicationInstance;
        if (BuildConfig.DEBUG) Timber.plant(new Timber.DebugTree());
        Realm.init(applicationInstance.getCurrentApplication());
        libraryConfig = new RealmConfiguration.Builder()
                .name("library.realm")
                .modules(new RealmLoggingModule())
                .build();
        Realm.setDefaultConfiguration(libraryConfig);
    }

    public static void e(String message, String className) {
        Timber.tag(className);
        Timber.e(message);
        saveLogToDBAsync(message,className);
    }

    public static void d(String message, String className) {
        Timber.tag(className);
        Timber.d(message);
        saveLogToDBAsync(message,className);
    }

    public static void i(String message, String className) {
        Timber.tag(className);
        Timber.i(message);
        saveLogToDBAsync(message,className);
    }

    public static void v(String message, String className) {
        Timber.tag(className);
        Timber.v(message);
        saveLogToDBAsync(message,className);
    }

    private static void saveLogToDBAsync(final String message, final String className) {
        try (Realm realm = Realm.getInstance(libraryConfig)) {
            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    RealmLogs realmLogs = realm.createObject(RealmLogs.class, System.currentTimeMillis());
                    realmLogs.className = className;
                    realmLogs.message = message;
                    realm.insertOrUpdate(realmLogs);
                }
            });
        }
    }
}

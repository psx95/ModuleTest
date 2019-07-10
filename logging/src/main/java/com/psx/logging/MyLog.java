package com.psx.logging;

import com.psx.commons.MainApplication;

import timber.log.Timber;

public class MyLog {

    private static MainApplication applicationInstance;

    public static void init(MainApplication applicationInstance) {
        MyLog.applicationInstance = applicationInstance;
        if (BuildConfig.DEBUG) Timber.plant(new Timber.DebugTree());
    }

    public static void e(String message, String className) {
        Timber.tag(className);
        Timber.e(message);
    }

    public static void d(String message, String className) {
        Timber.tag(className);
        Timber.d(message);
    }

    public static void i(String message, String className) {
        Timber.tag(className);
        Timber.i(message);
    }

    public static void v(String message, String className) {
        Timber.v(className);
        Timber.v(message);
    }
}

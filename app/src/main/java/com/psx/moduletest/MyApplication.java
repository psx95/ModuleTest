package com.psx.moduletest;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.psx.aftereffects.AfterEffects;
import com.psx.commons.Constants;
import com.psx.commons.MainApplication;
import com.psx.commons.Modules;
import com.psx.commons.RxBus;
import com.psx.logging.LoggingLevel;
import com.psx.logging.Grove;
import com.psx.simplemaths.SimpleMath;

public class MyApplication extends Application implements MainApplication {

    private Activity currentActivity = null;
    private RxBus eventBus = null;

    @Override
    public void onCreate() {
        super.onCreate();
        eventBus = new RxBus();
        Grove.init(this, Constants.LOGS_UPLOAD_API, LoggingLevel.ERRORS_ONLY);
        AfterEffects.init(this);
        SimpleMath.init(this);
        setupActivityLifecycleListeners();
    }

    @Override
    public Activity getCurrentActivity() {
        return currentActivity;
    }

    @Override
    public Application getCurrentApplication() {
        return this;
    }

    @Override
    public RxBus getEventBus() {
        return bus();
    }

    @Override
    public void teardownModule(Modules module) {
        switch (module) {
            case AFTER_EFFECTS:
                AfterEffects.teardown();
                break;
            case SIMPLE_MATHS:
                break;
        }
    }

    private void setupActivityLifecycleListeners() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {
                currentActivity = activity;
            }

            @Override
            public void onActivityPaused(Activity activity) {
                currentActivity = null;
            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }

    private RxBus bus() {
        return eventBus;
    }
}

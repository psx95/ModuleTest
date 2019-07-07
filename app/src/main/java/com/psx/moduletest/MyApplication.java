package com.psx.moduletest;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.psx.aftereffects.ActivityAwareness;
import com.psx.aftereffects.AfterEffects;
import com.psx.simplemaths.SimpleMath;

public class MyApplication extends Application implements ActivityAwareness {

    private Activity currentActivity = null;

    @Override
    public void onCreate() {
        super.onCreate();
        AfterEffects.init(this);
        SimpleMath.init(this, AfterEffects::showAnimation);
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
}

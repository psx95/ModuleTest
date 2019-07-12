package com.psx.commons;

import android.app.Activity;
import android.app.Application;

/**
 * The Application class must implement this interface
 */
public interface MainApplication {

    Activity getCurrentActivity();

    Application getCurrentApplication();

    RxBus getEventBus();

    void teardownModule(Modules module);
}

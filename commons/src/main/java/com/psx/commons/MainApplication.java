package com.psx.commons;

import android.app.Activity;
import android.app.Application;

public interface MainApplication {

    Activity getCurrentActivity();

    Application getCurrentApplication();

    RxBus getEventBus();
}

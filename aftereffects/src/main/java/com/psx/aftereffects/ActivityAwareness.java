package com.psx.aftereffects;

import android.app.Activity;
import android.app.Application;

public interface ActivityAwareness {
    Activity getCurrentActivity();

    Application getCurrentApplication();
}

package com.psx.aftereffects;

import android.app.Application;

import androidx.appcompat.app.AppCompatActivity;

public interface ActivityAwareness {
    AppCompatActivity getCurrentActivity();

    Application getCurrentApplication();
}

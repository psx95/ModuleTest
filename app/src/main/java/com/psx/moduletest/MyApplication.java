package com.psx.moduletest;

import android.app.Application;
import android.widget.Toast;

import com.psx.simplemaths.SimpleMath;
import com.psx.simplemaths.SimpleMathCallbacks;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SimpleMath.init(this, new SimpleMathCallbacks() {
            @Override
            public void onCalculationCompleted(String performedOperation) {
                Toast.makeText(getApplicationContext(), "Calculation Done", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

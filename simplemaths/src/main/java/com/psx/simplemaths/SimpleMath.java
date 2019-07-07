package com.psx.simplemaths;

import android.app.Application;
import android.content.Intent;

import androidx.annotation.NonNull;

public class SimpleMath {

    private static Application applicationInstance = null;
    private static SimpleMathCallbacks simpleMathCallbacks = null;


    public static void init(@NonNull Application applicationInstance, @NonNull SimpleMathCallbacks callbacks) {
        SimpleMath.simpleMathCallbacks = callbacks;
        init(applicationInstance);
    }

    private static void init(@NonNull Application applicationInstance) {
        SimpleMath.applicationInstance = applicationInstance;
        Intent intent = new Intent(applicationInstance.getApplicationContext(), CalculationActivity.class);
        SimpleMath.applicationInstance.getApplicationContext().startActivity(intent);
    }

    static double performCalculation(double op1, double op2, SupportedOperations supportedOperations) {
        switch (supportedOperations) {
            case ADDITION:
                return op1 + op2;
            case DIVISION:
                return op1 / op2;
            case SUBTRACTION:
                return op1 - op2;
            case MULTIPLICATION:
                return op1 * op2;
        }
        return 0;
    }

    static Application getApplicationInstance() {
        return applicationInstance;
    }

    static SimpleMathCallbacks getSimpleMathCallbacks() {
        return simpleMathCallbacks;
    }
}

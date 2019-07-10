package com.psx.simplemaths;

import android.content.Intent;

import androidx.annotation.NonNull;

import com.psx.commons.ExchangeObject;
import com.psx.commons.MainApplication;

public class SimpleMath {

    private static MainApplication applicationInstance = null;

    public static void init(@NonNull MainApplication applicationInstance) {
        SimpleMath.applicationInstance = applicationInstance;
        Intent intent = new Intent(applicationInstance.getCurrentApplication(), CalculationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        SimpleMath.applicationInstance.getCurrentApplication().startActivity(intent);
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

    static MainApplication getApplicationInstance() {
        return applicationInstance;
    }

    static void sendCalculationCompleteEvent(ExchangeObject exchangeObject) {
        SimpleMath.applicationInstance.getEventBus().send(exchangeObject);
    }
}

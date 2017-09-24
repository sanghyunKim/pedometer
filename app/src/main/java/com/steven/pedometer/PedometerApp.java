package com.steven.pedometer;

import android.app.Application;
import android.content.Context;

import com.steven.pedometer.data.system.StepStream;
import com.steven.pedometer.ui.pedometer.PedometerService;

public class PedometerApp extends Application {

    private static PedometerApp sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }

    public static Context getContext() {
        return sInstance.getApplicationContext();
    }
}

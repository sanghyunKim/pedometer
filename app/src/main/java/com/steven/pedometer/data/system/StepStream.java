package com.steven.pedometer.data.system;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.steven.pedometer.PedometerApp;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

import static android.content.Context.SENSOR_SERVICE;

public class StepStream implements SensorEventListener {

    private static StepStream sInstance;

    private static StepDetector mStepDetector;

    private SensorManager mSensorManager;

    private Sensor mSensor;

    private boolean mIsListening;

    private static final PublishSubject<SensorEvent> mStepPublishSubject = PublishSubject.create();

    private StepStream() {
        mSensorManager = (SensorManager)PedometerApp.getContext().getSystemService(SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mStepDetector = new StepDetector();
    }

    public synchronized static StepStream getInstance() {
        if (sInstance == null) {
            sInstance = new StepStream();
        }

        return sInstance;
    }

    public void startListening() {
        if (!mIsListening) {
            mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_FASTEST);
            mIsListening = true;
        }
    }

    public void stopListening() {
        if (mIsListening) {
            mSensorManager.unregisterListener(this);
            mIsListening = false;
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        mStepPublishSubject.onNext(event);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public static Observable<Step> observe() {
        return mStepPublishSubject.filter(sensorEvent -> sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
                .filter(sensorEvent ->
                    mStepDetector.updateAccel(sensorEvent.timestamp, sensorEvent.values[0],
                            sensorEvent.values[1], sensorEvent.values[2]))
                .flatMap(sensorEvent -> Observable.just(new Step()));
    }
}

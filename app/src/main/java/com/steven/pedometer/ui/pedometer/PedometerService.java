package com.steven.pedometer.ui.pedometer;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.steven.pedometer.data.disk.DiskDataSource;
import com.steven.pedometer.data.disk.Pedometer;
import com.steven.pedometer.data.system.StepStream;

import java.util.Calendar;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class PedometerService extends Service {

    public static final String ACTION_STEP = "com.steven.pedometer.action.step";

    private long mCurrentStepNum;

    private Disposable mStepDisposable;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        StepStream.getInstance().startListening();

        Calendar today = Calendar.getInstance();

        DiskDataSource.getInstance().getPedometer(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH))
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(pedometer -> mCurrentStepNum = pedometer.getStepCount());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
        startForeground(1, builder.build());

        mStepDisposable = StepStream.getInstance().observe().subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(step -> {
                    mCurrentStepNum++;

                    sendBroadcast(new Intent(ACTION_STEP).putExtra("step_num", mCurrentStepNum));

                    Calendar today = Calendar.getInstance();
                    DiskDataSource.getInstance().updatePedometer(new Pedometer(today.get(Calendar.YEAR),
                            today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH), mCurrentStepNum, 0));
                });

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        StepStream.getInstance().stopListening();
        mStepDisposable.dispose();
    }
}

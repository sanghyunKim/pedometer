package com.steven.pedometer.ui.pedometer;

import android.content.Intent;

import com.steven.pedometer.PedometerApp;
import com.steven.pedometer.data.disk.DiskDataSource;
import com.steven.pedometer.data.disk.Pedometer;
import com.steven.pedometer.data.system.StepStream;

import java.util.Calendar;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.steven.pedometer.data.disk.DiskDataSource.RUNNING_STATE;
import static com.steven.pedometer.data.disk.DiskDataSource.STOPPED_STATE;

public class PedometerPresenter implements PedometerContract.Presenter {

    private PedometerContract.View mPedometerView;

    private int mCurrentState;

    public PedometerPresenter(PedometerContract.View view) {
        mPedometerView = view;
    }

    @Override
    public void pressButton() {
        if (mCurrentState == STOPPED_STATE) {
            startPedometer();
        } else {
            stopPedometer();
        }
    }

    private void startPedometer() {
        mCurrentState = RUNNING_STATE;
        DiskDataSource.getInstance().saveCurrentState(RUNNING_STATE);
        mPedometerView.showStopButton();
        PedometerApp.getContext().startService(new Intent(PedometerApp.getContext(), PedometerService.class));
    }

    private void stopPedometer() {
        mCurrentState = STOPPED_STATE;
        DiskDataSource.getInstance().saveCurrentState(STOPPED_STATE);
        mPedometerView.showStartButton();
        PedometerApp.getContext().stopService(new Intent(PedometerApp.getContext(), PedometerService.class));
    }

    @Override
    public void onResume() {
        mCurrentState = DiskDataSource.getInstance().
                getCurrentState();

        if (mCurrentState == STOPPED_STATE) {
            mPedometerView.showStartButton();
        } else {
            StepStream.getInstance().startListening();
            mPedometerView.showStopButton();
        }

        final Calendar today = Calendar.getInstance();

        DiskDataSource.getInstance().getPedometer(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(pedometer -> {
                    if (pedometer != null) {
                        mPedometerView.setStepCount(Long.toString(pedometer.getStepCount()));
                        mPedometerView.setDistance(Long.toString(pedometer.getDistance()));
                    }
                },
                        error -> {
                            Pedometer initialPedometer = new Pedometer(
                                    today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH), 0, 0);
                            DiskDataSource.getInstance().createPedometer(initialPedometer);
                            mPedometerView.setStepCount(Long.toString(initialPedometer.getStepCount()));
                            mPedometerView.setDistance(Long.toString(initialPedometer.getDistance()));
                        });
    }
}

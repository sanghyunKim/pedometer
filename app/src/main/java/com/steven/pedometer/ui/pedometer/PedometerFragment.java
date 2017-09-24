package com.steven.pedometer.ui.pedometer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.steven.pedometer.PedometerApp;
import com.steven.pedometer.R;

import static com.steven.pedometer.ui.pedometer.PedometerService.ACTION_STEP;

public class PedometerFragment extends Fragment implements PedometerContract.View {

    private PedometerContract.Presenter mPresenter;

    private Button mButton;

    private TextView mStepCount;

    private TextView mDistance;

    private TextView mLocation;

    public PedometerFragment() {
    }

    @Override
    public void setPresenter(PedometerContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_STEP);
        PedometerApp.getContext().registerReceiver(mBroadcastReceiver, filter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_pedometer, container, false);

        mButton = (Button)view.findViewById(R.id.button);
        mButton.setOnClickListener(v -> mPresenter.pressButton());

        mStepCount = (TextView)view.findViewById(R.id.step_count);
        mDistance = (TextView)view.findViewById(R.id.distance);
        mLocation = (TextView)view.findViewById(R.id.location);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.onResume();
    }

    @Override
    public void showStartButton() {
        mButton.setText(getContext().getString(R.string.start));
    }

    @Override
    public void showStopButton() {
        mButton.setText(getContext().getString(R.string.stop));
    }

    @Override
    public void setStepCount(String stepCount) {
        mStepCount.setText(stepCount);
    }

    @Override
    public void setDistance(String distance) {
        mDistance.setText(distance);
    }

    @Override
    public void setCurrentLocation(String location) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        PedometerApp.getContext().unregisterReceiver(mBroadcastReceiver);
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mStepCount.setText(Long.toString(intent.getLongExtra("step_num", 0)));
        }
    };
}

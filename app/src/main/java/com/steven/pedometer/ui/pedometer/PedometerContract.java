package com.steven.pedometer.ui.pedometer;

public interface PedometerContract {

    interface View {

        void setPresenter(Presenter presenter);

        void showStartButton();

        void showStopButton();

        void setStepCount(String stepCount);

        void setDistance(String distance);

        void setCurrentLocation(String location);

    }

    interface Presenter {

        void pressButton();

        void onResume();

    }

}

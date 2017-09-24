package com.steven.pedometer.data.disk;

public final class Pedometer {

    private final int mYear;

    private final int mMonth;

    private final int mDay;

    private final long mStepCount;

    private final long mDistance;

    public Pedometer(int year, int month, int day, long stepCount, long distance) {
        mYear = year;
        mMonth = month;
        mDay = day;
        mStepCount = stepCount;
        mDistance = distance;
    }

    public int getYear() {
        return mYear;
    }

    public int getMonth() {
        return mMonth;
    }

    public int getDay() {
        return mDay;
    }

    public long getStepCount() {
        return mStepCount;
    }

    public long getDistance() {
        return mDistance;
    }
}

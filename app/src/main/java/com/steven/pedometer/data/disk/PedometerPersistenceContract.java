package com.steven.pedometer.data.disk;

import android.provider.BaseColumns;

public final class PedometerPersistenceContract {

    private PedometerPersistenceContract() {}

    public static abstract class PedometerEntry implements BaseColumns {
        public static final String TABLE_NAME = "pedometer";
        public static final String COLUMN_NAME_ROW_ID = "_id";
        public static final String COLUMN_NAME_YEAR = "year";
        public static final String COLUMN_NAME_MONTH = "month";
        public static final String COLUMN_NAME_DAY = "day";
        public static final String COLUMN_NAME_STEP_COUNT = "step_count";
        public static final String COLUMN_NAME_DISTANCE = "distance";
    }
}

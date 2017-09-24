package com.steven.pedometer.data.disk;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PedometerDbHelper extends SQLiteOpenHelper {
    public static final int DB_VERSION = 1;

    public static final String DB_NAME = "pedometer.db";

    private static final String INTEGER_TYPE = " INTEGER";

    private static final String COMMA_SEP = ",";

    private static final String PRIMARY_KEY = " PRIMARY KEY (" + PedometerPersistenceContract.PedometerEntry.COLUMN_NAME_YEAR + COMMA_SEP +
            PedometerPersistenceContract.PedometerEntry.COLUMN_NAME_MONTH + COMMA_SEP +
            PedometerPersistenceContract.PedometerEntry.COLUMN_NAME_DAY + ")";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + PedometerPersistenceContract.PedometerEntry.TABLE_NAME + " ( " +
                    PedometerPersistenceContract.PedometerEntry.COLUMN_NAME_ROW_ID + INTEGER_TYPE + COMMA_SEP +
                    PedometerPersistenceContract.PedometerEntry.COLUMN_NAME_YEAR + INTEGER_TYPE + " NOT NULL," +
                    PedometerPersistenceContract.PedometerEntry.COLUMN_NAME_MONTH + INTEGER_TYPE + " NOT NULL," +
                    PedometerPersistenceContract.PedometerEntry.COLUMN_NAME_DAY + INTEGER_TYPE + " NOT NULL," +
                    PedometerPersistenceContract.PedometerEntry.COLUMN_NAME_STEP_COUNT + INTEGER_TYPE + COMMA_SEP +
                    PedometerPersistenceContract.PedometerEntry.COLUMN_NAME_DISTANCE + INTEGER_TYPE + COMMA_SEP +
                    PRIMARY_KEY +
                    " )";

    public PedometerDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) { }
}

package com.steven.pedometer.data.disk;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.steven.pedometer.PedometerApp;

import io.reactivex.Observable;

import static com.steven.pedometer.data.disk.PedometerPersistenceContract.PedometerEntry.COLUMN_NAME_DAY;
import static com.steven.pedometer.data.disk.PedometerPersistenceContract.PedometerEntry.COLUMN_NAME_DISTANCE;
import static com.steven.pedometer.data.disk.PedometerPersistenceContract.PedometerEntry.COLUMN_NAME_MONTH;
import static com.steven.pedometer.data.disk.PedometerPersistenceContract.PedometerEntry.COLUMN_NAME_STEP_COUNT;
import static com.steven.pedometer.data.disk.PedometerPersistenceContract.PedometerEntry.COLUMN_NAME_YEAR;
import static com.steven.pedometer.data.disk.PedometerPersistenceContract.PedometerEntry.TABLE_NAME;

public class DiskDataSource {

    public static final int STOPPED_STATE = 0;

    public static final int RUNNING_STATE = 1;

    private static final String PREFERENCE_NAME = "pedometer_preference";

    private static final String PREFERENCE_KEY_NAME_STATE = "state";

    private static DiskDataSource sInstance;

    private PedometerDbHelper mPedometerDbHelper;

    private Context mApplicationContext;

    private DiskDataSource() {
        mApplicationContext = PedometerApp.getContext();
        mPedometerDbHelper = new PedometerDbHelper(mApplicationContext);
    }

    public synchronized static DiskDataSource getInstance() {
        if (sInstance == null) {
            sInstance = new DiskDataSource();
        }

        return sInstance;
    }

    public void saveCurrentState(int state) {
        SharedPreferences preferences = mApplicationContext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(PREFERENCE_KEY_NAME_STATE, state);
        editor.commit();
    }

    public int getCurrentState() {
        SharedPreferences preferences = mApplicationContext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return preferences.getInt(PREFERENCE_KEY_NAME_STATE, STOPPED_STATE);
    }

    public Observable<Pedometer> getPedometer(int year, int month, int day) {

        SQLiteDatabase db = mPedometerDbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("Select * from " + TABLE_NAME + " where year = ? AND month = ? AND day = ?", new String[]{ Integer.toString(year),
                Integer.toString(month), Integer.toString(day) });

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToNext();

            int y = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_NAME_YEAR));
            int m = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_NAME_MONTH));
            int d = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_NAME_DAY));
            long stepCount = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_NAME_STEP_COUNT));
            long distance = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_NAME_DISTANCE));

            return Observable.just(new Pedometer(y, m, d, stepCount, distance));
        }

        return Observable.error(new IllegalStateException("No result"));
    }

    public void createPedometer(Pedometer pedometer) {
        SQLiteDatabase db = mPedometerDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_YEAR, pedometer.getYear());
        values.put(COLUMN_NAME_MONTH, pedometer.getMonth());
        values.put(COLUMN_NAME_DAY, pedometer.getDay());
        values.put(COLUMN_NAME_STEP_COUNT, pedometer.getStepCount());
        values.put(COLUMN_NAME_DISTANCE, pedometer.getDistance());

        db.insert(TABLE_NAME, null, values);

        db.close();
    }

    public void updatePedometer(Pedometer pedometer) {
        SQLiteDatabase db = mPedometerDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_YEAR, pedometer.getYear());
        values.put(COLUMN_NAME_MONTH, pedometer.getMonth());
        values.put(COLUMN_NAME_DAY, pedometer.getDay());
        values.put(COLUMN_NAME_STEP_COUNT, pedometer.getStepCount());
        values.put(COLUMN_NAME_DISTANCE, pedometer.getDistance());

        String selection = " year = ? AND month = ? AND day = ?";
        String[] selectionArgs = { Integer.toString(pedometer.getYear()),
                Integer.toString(pedometer.getMonth()),
                Integer.toString(pedometer.getDay()) };

        db.update(TABLE_NAME, values, selection, selectionArgs);
    }

}

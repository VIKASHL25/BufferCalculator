package com.example.buffercalculator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "facultyBuffer.db";
    public static final int DB_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE faculty (id TEXT PRIMARY KEY, name TEXT, dept TEXT, buffer_time INTEGER, last_reset TEXT)");
        db.execSQL("CREATE TABLE late_records (id INTEGER PRIMARY KEY AUTOINCREMENT, faculty_id TEXT, date TEXT, late_mins INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS faculty");
        db.execSQL("DROP TABLE IF EXISTS late_records");
        onCreate(db);
    }

    public boolean registerFaculty(String id, String name, String dept) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("id", id);
        v.put("name", name);
        v.put("dept", dept);
        v.put("buffer_time", 120);
        v.put("last_reset", getCurrentMonth());
        long res = db.insert("faculty", null, v);
        return res != -1;
    }

    public boolean validateFaculty(String id, String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM faculty WHERE id=? AND name=?", new String[]{id, name});
        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }


    public Cursor getFaculty(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM faculty WHERE id=?", new String[]{id});
    }

    public void addLateTime(String id, int lateMins) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT buffer_time FROM faculty WHERE id=?", new String[]{id});
        if (c.moveToFirst()) {
            int remaining = c.getInt(0);
            int newTime = Math.max(0, remaining - lateMins);
            ContentValues v = new ContentValues();
            v.put("buffer_time", newTime);
            db.update("faculty", v, "id=?", new String[]{id});

            ContentValues log = new ContentValues();
            log.put("faculty_id", id);
            log.put("date", new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date()));
            log.put("late_mins", lateMins);
            db.insert("late_records", null, log);
        }
    }

    public Cursor getLateRecords(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM late_records WHERE faculty_id=?", new String[]{id});
    }

    public void resetMonthlyBufferIfNeeded(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT last_reset FROM faculty WHERE id=?", new String[]{id});
        if (c.moveToFirst()) {
            String lastMonth = c.getString(0);
            String currentMonth = getCurrentMonth();
            if (!lastMonth.equals(currentMonth)) {
                ContentValues v = new ContentValues();
                v.put("buffer_time", 120);
                v.put("last_reset", currentMonth);
                db.update("faculty", v, "id=?", new String[]{id});
            }
        }
    }

    private String getCurrentMonth() {
        return new SimpleDateFormat("MM-yyyy", Locale.getDefault()).format(new Date());
    }
}

package com.example.buffercalculator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {

    public static final String DB_NAME = "Buffer.db";
    public static final int DB_VERSION = 1;

    public Database(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE teachers (id TEXT PRIMARY KEY, name TEXT, buffer_time INTEGER)");
        db.execSQL("CREATE TABLE hod (username TEXT PRIMARY KEY, password TEXT)");
        db.execSQL("INSERT INTO hod VALUES('HOD','0001')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS teachers");
        db.execSQL("DROP TABLE IF EXISTS hod");
        onCreate(db);
    }

    // Register new faculty
    public boolean registerTeacher(String id, String name) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor c = db.rawQuery("SELECT id FROM teachers WHERE id=?", new String[]{id});
        if (c.moveToFirst()) {
            c.close();
            return false;
        }
        c.close();

        ContentValues values = new ContentValues();
        values.put("id", id);
        values.put("name", name);
        values.put("buffer_time", 120);

        long result = db.insert("teachers", null, values);
        return result != -1;
    }

    // Getting faculty details in Hod Dashboard
    public Cursor getTeacher(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM teachers WHERE id=?", new String[]{id});
    }

    //HOD login
    public boolean validateHOD(String user, String pass) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM hod WHERE username=? AND password=?", new String[]{user, pass});
        boolean valid = c.getCount() > 0;
        c.close();
        return valid;
    }

    // Reset buffer to 120
    public void resetAllTeachersBuffer() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("buffer_time", 120);
        db.update("teachers", v, null, null);
    }
}
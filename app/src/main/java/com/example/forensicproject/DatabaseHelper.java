package com.example.forensicproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Forensic.db";
    public static final String TABLE_NAME = "Call_logs";

    public static final String COLS_1 = "ID";
    public static final String COLS_2 = "phoneNumber";
    public static final String COLS_3 = "callType";
    public static final String COLS_4 = "callDate";
    public static final String COLS_5 = "callDuration";


    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,phoneNumber TEXT,callType TEXT,callDate TEXT,callDuration TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}

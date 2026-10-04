package com.example.hearingtest.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final int version = 1;

    public DBHelper(Context context) {
        super(context, Query.DB_NAME, null, version);
    }

    // Создаем и заполняем БД
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Query.createTableUsers());
        db.execSQL(Query.createTableAudiograms());
        db.execSQL(Query.createTableMediana());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(Query.drobTableUsers());
        db.execSQL(Query.drobTableAudiograms());
        db.execSQL(Query.drobTableMedians());
        onCreate(db);
    }
}
package com.tarasevich.rss_reader.Datebase;

import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class FeedContactSQLiteOpenHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "feeddb.db";
    private static final int DB_VERSION = 1;

    public FeedContactSQLiteOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(FeedContract.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(FeedContract.DROP_TABLE);
        onCreate(db);
    }
}
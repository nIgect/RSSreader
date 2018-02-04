package com.tarasevich.rss_reader;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tarasevich.rss_reader.Datebase.FeedContactSQLiteOpenHelper;
import com.tarasevich.rss_reader.Datebase.FeedContract;


public class OnlinerContentProvider extends ContentProvider {

    public static final String AUTHORITY = BuildConfig.APPLICATION_ID + ".OnlinerContentProvider";
    private static final int CONTACT_ITEM = 1;
    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        URI_MATCHER.addURI(AUTHORITY, FeedContract.TABLE_NAME, CONTACT_ITEM);
    }

    private FeedContactSQLiteOpenHelper mSQLiteOpenHelper;

    @Override
    public boolean onCreate() {
        mSQLiteOpenHelper = new FeedContactSQLiteOpenHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        int match = URI_MATCHER.match(uri);
        if (match == CONTACT_ITEM) {
            //selection = DatabaseUtils.concatenateWhere(FeedContract.Entity._ID + " = " + ContentUris.parseId(uri), selection);
        } else {
            throw new IllegalArgumentException();
        }
        final SQLiteDatabase sqLiteDatabase = mSQLiteOpenHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(FeedContract.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
        if (cursor != null) {
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        }
        return cursor;

    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        if (URI_MATCHER.match(uri) == -1) {
            throw new IllegalArgumentException();

        }

        final SQLiteDatabase sqLiteDatabase = mSQLiteOpenHelper.getWritableDatabase();
        long row = sqLiteDatabase.insert(FeedContract.TABLE_NAME, null, contentValues);
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, row);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        int match = URI_MATCHER.match(uri);
        if (match == CONTACT_ITEM) {
            //selection = DatabaseUtils.concatenateWhere(FeedContract.Entity._ID + " = " + ContentUris.parseId(uri), selection);
        } else {
            throw new IllegalArgumentException();
        }
        final SQLiteDatabase sqLiteDatabase = mSQLiteOpenHelper.getWritableDatabase();
        int row = sqLiteDatabase.delete(FeedContract.TABLE_NAME, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return row;

    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {

        int match = URI_MATCHER.match(uri);
        if (match == CONTACT_ITEM) {
            //selection = DatabaseUtils.concatenateWhere(FeedContract.Entity._ID + " = " + ContentUris.parseId(uri), selection);
        } else {
            throw new IllegalArgumentException();
        }

        final SQLiteDatabase sqLiteDatabase = mSQLiteOpenHelper.getWritableDatabase();
        int row = sqLiteDatabase.update(FeedContract.TABLE_NAME, contentValues, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return row;
    }


}


package com.tarasevich.rss_reader.Datebase;

import android.net.Uri;
import android.provider.BaseColumns;

import static android.provider.BaseColumns._ID;
import static com.tarasevich.rss_reader.Datebase.FeedContract.Entity.CATEGORY;
import static com.tarasevich.rss_reader.Datebase.FeedContract.Entity.DESCRIPTION;
import static com.tarasevich.rss_reader.Datebase.FeedContract.Entity.IMAGE;
import static com.tarasevich.rss_reader.Datebase.FeedContract.Entity.LINK;
import static com.tarasevich.rss_reader.Datebase.FeedContract.Entity.PUBDATE;
import static com.tarasevich.rss_reader.Datebase.FeedContract.Entity.TITLE;
import static com.tarasevich.rss_reader.OnlinerContentProvider.AUTHORITY;

public final class FeedContract {

    public static final String TABLE_NAME = "feed";
    public static final Uri URI=Uri.parse("content://"+AUTHORITY).buildUpon().appendPath(TABLE_NAME).build();

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
            _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            TITLE + " TEXT, " +
            PUBDATE + " TEXT, " +
            DESCRIPTION + " TEXT, " +
            CATEGORY + " TEXT, " +
            LINK + " TEXT, " +
            IMAGE + " TEXT)";


    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    private FeedContract() {
    }

    public static class Entity implements BaseColumns {
        public static final String TITLE = "title";
        public static final String PUBDATE = "pubDate";
        public static final String DESCRIPTION = "description";
        public static final String CATEGORY = "category";
        public static final String IMAGE = "thumbnail";
        public static final String LINK = "link";

    }
}

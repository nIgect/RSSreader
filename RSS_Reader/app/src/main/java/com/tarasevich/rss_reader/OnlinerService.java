package com.tarasevich.rss_reader;

import android.app.Service;
import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.tarasevich.rss_reader.Datebase.FeedContract;
import com.tarasevich.rss_reader.Model.FeedItem;
import com.tarasevich.rss_reader.Model.OnlinerRss;
import com.tarasevich.rss_reader.Model.RetroInterface;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.OkHttpClient;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

import static com.tarasevich.rss_reader.OnlinerContentProvider.AUTHORITY;

public class OnlinerService extends Service {

    public class OnlinerBinder extends Binder {

        public OnlinerService getService() {
            return OnlinerService.this;
        }
    }

    public class ServiceHandler extends Handler {

        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            OnlinerRss rss = loadNewsFromNetwork();
            cacheNews(rss);
            mNewsFetched = true;
        }
    }

    private RetroInterface mRetroInterface;
    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;

    private boolean mNewsFetched;

    public static Intent newIntent(Context context) {
        return new Intent(context, OnlinerService.class);
    }

    @Override
    public void onCreate() {
        mRetroInterface = new Retrofit.Builder()
                .baseUrl("https://www.onliner.by")
                .client(new OkHttpClient())
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build()
                .create(RetroInterface.class);

        HandlerThread thread = new HandlerThread(this.getClass().getSimpleName());
        thread.start();

        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new OnlinerBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return true;
    }

    @Override
    public void onDestroy() {
        mServiceLooper.quit();
    }

    public void fetchNews() {
        mNewsFetched = false;
        Message message = mServiceHandler.obtainMessage();
        mServiceHandler.sendMessage(message);
    }

    private OnlinerRss loadNewsFromNetwork() {
        OnlinerRss rss = null;
        try {
            Response<OnlinerRss> response = mRetroInterface.getNews().execute();
            if (response.isSuccessful()) {
                rss = response.body();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rss;
    }

    private void cacheNews(OnlinerRss rss) {
        if (rss != null) {
            // поля которые буду сохранять в базу данных используя список констант
            ArrayList<ContentProviderOperation> operations = new ArrayList<>();
            ContentProviderOperation delete = ContentProviderOperation.newDelete(FeedContract.URI).build();
            operations.add(delete);
            for (FeedItem feed : rss.feeds) {
                ContentValues values = new ContentValues();
                values.put(FeedContract.Entity.TITLE, feed.title);
                values.put(FeedContract.Entity.PUBDATE,cutPubDate(feed.pubDate));
                values.put(FeedContract.Entity.DESCRIPTION,cutDescription(feed.content));
                values.put(FeedContract.Entity.CATEGORY, feed.category);
                values.put(FeedContract.Entity.IMAGE, (feed.image.url));
                values.put(FeedContract.Entity.LINK, feed.link);
                ContentProviderOperation insert = ContentProviderOperation.newInsert(FeedContract.URI).withValues(values).build();
                operations.add(insert);
            }
            try {
                getContentResolver().applyBatch(AUTHORITY, operations);
            } catch (RemoteException | OperationApplicationException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isNewsFetched() {
        return mNewsFetched;
    }
    public  String cutDescription(String description) {
        int a = description.indexOf("<p>");
        int b = description.indexOf("</p>");

        String cut = description.substring(0,a) + description.substring(b);
        return cut;

    }

    public String cutPubDate(String pubDate){

       pubDate = pubDate.substring(0,25);
       return pubDate;
    }

}

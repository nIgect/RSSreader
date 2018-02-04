package com.tarasevich.rss_reader;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.IBinder;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.rohit.recycleritemclicksupport.RecyclerItemClickSupport;
import com.tarasevich.rss_reader.Datebase.FeedContract;
import com.tarasevich.rss_reader.FeedRecyclerView.NewsAdapter;
import com.tarasevich.rss_reader.FeedRecyclerView.SpaceItemDecoration;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    @BindView(R.id.list_source)
    RecyclerView recyclerView;
    @BindView(R.id.my_toolBar)
    Toolbar toolBar;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefreshLayout;

    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mOnlinerService = ((OnlinerService.OnlinerBinder) iBinder).getService();
            mOnlinerService.fetchNews();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mOnlinerService = null;
        }
    };

    private OnlinerService mOnlinerService;
    private NewsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolBar);
        setTitle(R.string.news);
        mAdapter = new NewsAdapter(this, null, Glide.with(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new SpaceItemDecoration(10));
        getSupportLoaderManager().initLoader(0, null, this);


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mOnlinerService != null) {
                    mOnlinerService.fetchNews();
                    Toast.makeText(getApplicationContext(),R.string.ifRefresh,Toast.LENGTH_SHORT).show();
                } else {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });



        RecyclerItemClickSupport.addTo(recyclerView)
                .setOnItemClickListener(new RecyclerItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerV, int position, View v) {
                Uri uri  = Uri.parse(mAdapter.getLinkDetails(position));
                CustomTabsIntent.Builder intentBuilder = new CustomTabsIntent.Builder();
                intentBuilder.setToolbarColor(ContextCompat.getColor(getApplication(),
                        R.color.colorPrimary));
                intentBuilder.setSecondaryToolbarColor(ContextCompat.getColor(getApplicationContext(),
                        R.color.colorPrimaryDark));
                intentBuilder.setStartAnimations(getApplicationContext(), R.anim.slide_in_right,
                        R.anim.slide_out_left);
                intentBuilder.setExitAnimations(getApplicationContext(), android.R.anim.slide_in_left,
                        android.R.anim.slide_out_right);

                CustomTabsIntent customTabsIntent = intentBuilder.build();
                customTabsIntent.launchUrl(getApplicationContext(), uri);
            }
        });
    }




    @Override
    protected void onStart() {
        super.onStart();
        bindService(OnlinerService.newIntent(this), mServiceConnection, BIND_AUTO_CREATE);
        Toast.makeText(getApplicationContext(),R.string.ifRefresh,Toast.LENGTH_SHORT).show();
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(mServiceConnection);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, FeedContract.URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
        if (mOnlinerService != null && mOnlinerService.isNewsFetched()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
}

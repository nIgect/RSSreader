package com.tarasevich.rss_reader.FeedRecyclerView;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.tarasevich.rss_reader.Datebase.FeedContract;
import com.tarasevich.rss_reader.R;

public class NewsAdapter extends CursorRecyclerViewAdapter<NewsHolder> {




    private RequestManager mRequestManager;

    public NewsAdapter(Context context,
                       Cursor cursor,
                       RequestManager requestManager) {
        super(context, cursor);
        mRequestManager = requestManager;

    }


    @Override
    public void onBindViewHolder(NewsHolder viewHolder,
                                 Cursor cursor) {
        viewHolder.bind(cursor, mRequestManager);
    }

    @Override
    public NewsHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.source_layout, parent, false);
        return new NewsHolder(itemView);
    }

public String getLinkDetails(int position){
    
    Cursor cursor = getCursor();
    cursor.moveToPosition(position);
    return cursor.getString(cursor.getColumnIndex(FeedContract.Entity.LINK));

}




}

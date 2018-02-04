package com.tarasevich.rss_reader.FeedRecyclerView;

import android.database.Cursor;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.tarasevich.rss_reader.Datebase.FeedContract;
import com.tarasevich.rss_reader.R;


import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsHolder extends RecyclerView.ViewHolder {




    @BindView(R.id.txtTitle) TextView title;
    @BindView(R.id.txtContent) TextView content;
    @BindView(R.id.txtCategory) TextView category;
    @BindView(R.id.txtImage) ImageView  image;
    @BindView(R.id.txtPubDate) TextView pubDate;



    public NewsHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);

    }
   public void bind(Cursor cursor,
                    RequestManager requestManager){
       requestManager.load(cursor.getString(cursor.getColumnIndex(FeedContract.Entity.IMAGE)))
               .centerCrop()
               .into(image);
       title.setText(cursor.getString(cursor.getColumnIndex(FeedContract.Entity.TITLE)));
       content.setText(Html.fromHtml(cursor.getString(cursor.getColumnIndex(FeedContract.Entity.DESCRIPTION))));
       category.setText(cursor.getString(cursor.getColumnIndex(FeedContract.Entity.CATEGORY)));
       pubDate.setText(cursor.getString(cursor.getColumnIndex(FeedContract.Entity.PUBDATE)));

   }

}

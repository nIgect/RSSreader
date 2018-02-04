package com.tarasevich.rss_reader;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;

public class WelcomeActivity extends AppCompatActivity {

    @BindView(R.id.my_toolBar)
    Toolbar toolBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);


        setSupportActionBar(toolBar);
        setTitle(R.string.app_name);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(WelcomeActivity.
                                      this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        },2*1000);


    }


}

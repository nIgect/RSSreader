package com.tarasevich.rss_reader.Model;


import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RetroInterface {

    @GET("/feed")
    Call<OnlinerRss> getNews();


}


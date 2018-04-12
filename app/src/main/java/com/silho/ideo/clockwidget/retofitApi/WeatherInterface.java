package com.silho.ideo.clockwidget.retofitApi;

import com.silho.ideo.clockwidget.model.Root;
import com.silho.ideo.clockwidget.model.RootDays;
import com.silho.ideo.clockwidget.model.RootHours;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Samuel on 03/04/2018.
 */

public interface WeatherInterface {

    interface Weather{
        @GET("data/2.5/weather?appid=984694a6e23ef030e8c6cc0ab4d1a7b5")
        Call<Root> weather(@Query("lat") double latitude,
                           @Query(("lon")) double longitude,
                           @Query("units") String metric);

        @GET("data/2.5/forecast?appid=984694a6e23ef030e8c6cc0ab4d1a7b5")
        Call<RootHours> weatherHours(@Query("lat") double latitude,
                                     @Query(("lon")) double longitude,
                                     @Query("units") String metric);

        @GET("data/2.5/forecast/daily?appid=984694a6e23ef030e8c6cc0ab4d1a7b5")
        Call<RootDays> weatherDays(@Query("lat") double latitude,
                                   @Query(("lon")) double longitude,
                                   @Query("units") String metric);
    }
}

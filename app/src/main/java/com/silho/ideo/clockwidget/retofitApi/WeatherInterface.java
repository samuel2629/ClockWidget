package com.silho.ideo.clockwidget.retofitApi;

import com.silho.ideo.clockwidget.model.Root;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Samuel on 03/04/2018.
 */

public interface WeatherInterface {

    interface WeatherRoot {
        @GET("forecast/7d7b6cc9ff78fa87f6efdec5f0f9c465/{latitude},{longitude}")
        Call<Root> weatherRoot(@Path("latitude") double latitude, @Path("longitude") double longitude);
    }

    interface Weather{
        @GET("data/2.5/weather?appid=984694a6e23ef030e8c6cc0ab4d1a7b5")
        Call<com.silho.ideo.clockwidget.model.openweathermap.Root> weather(@Query("lat") double latitude,
                                                                           @Query(("lon")) double longitude,
                                                                           @Query("units") String metric);
    }
}

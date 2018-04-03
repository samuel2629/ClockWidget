package com.silho.ideo.clockwidget.retofitApi;

import com.silho.ideo.clockwidget.model.Root;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Samuel on 03/04/2018.
 */

public interface WeatherInterface {

    interface WeatherRoot {
        @GET("forecast/7d7b6cc9ff78fa87f6efdec5f0f9c465/{latitude},{longitude}")
        Call<Root> weatherRoot(@Path("latitude") double latitude, @Path("longitude") double longitude);
    }
}

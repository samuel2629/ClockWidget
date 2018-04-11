package com.silho.ideo.clockwidget.retofitApi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Samuel on 03/04/2018.
 */

public class WeatherService {

    private static final String URL = "https://api.openweathermap.org/";

    public static WeatherInterface.Weather getWeather(){
        return new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WeatherInterface.Weather.class);
    }
}

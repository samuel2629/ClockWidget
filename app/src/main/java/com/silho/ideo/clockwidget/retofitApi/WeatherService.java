package com.silho.ideo.clockwidget.retofitApi;

import com.silho.ideo.clockwidget.model.Root;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Samuel on 03/04/2018.
 */

public class WeatherService {

    private static final String BASE_URL = "https://api.darksky.net/";
    private static final String URL = "https://api.openweathermap.org/";

    public static WeatherInterface.WeatherRoot getRootWeather(){
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WeatherInterface.WeatherRoot.class);
    }

    public static WeatherInterface.Weather getCurrentWeather(){
        return new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WeatherInterface.Weather.class);
    }
}

package com.silho.ideo.clockwidget.widget;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.silho.ideo.clockwidget.R;
import com.silho.ideo.clockwidget.model.ListDays;
import com.silho.ideo.clockwidget.model.ListHours;
import com.silho.ideo.clockwidget.model.Root;
import com.silho.ideo.clockwidget.model.RootDays;
import com.silho.ideo.clockwidget.model.RootHours;
import com.silho.ideo.clockwidget.retofitApi.WeatherService;
import com.silho.ideo.clockwidget.ui.HourlyFragment;
import com.silho.ideo.clockwidget.utils.MyLocation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Samuel on 11/04/2018.
 */

public class RemoteFetchService extends Service {

    private int appWidgetId;
    private MyLocation myLocation;
    public static ArrayList<ListHours> mListDays;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent.hasExtra(AppWidgetManager.EXTRA_APPWIDGET_ID))
            appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        fetchDataFromWeb();
        return super.onStartCommand(intent, flags, startId);
    }

    private void fetchDataFromWeb() {
        myLocation = new MyLocation();
        MyLocation.LocationResult mLocationResult = new MyLocation.LocationResult() {
            @Override
            public void gotLocation(Location location) {

                Geocoder geocoder = new Geocoder(RemoteFetchService.this, Locale.getDefault());
                List<Address> addresses = null;
                try {
                    addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String mPlace = addresses.get(0).getLocality();

                if(isNetworkAvailable() &&  mPlace != null)
                    getHourlyWeather(location.getLatitude(), location.getLongitude(), true);


            }
        };

        myLocation.getLocation(this, mLocationResult, 0);
    }

//    private void getDailyWeather(double latitude, double longitude, final boolean isCelsius){
//        WeatherService.getWeather().weatherDays(latitude, longitude, "metric").enqueue(new Callback<RootDays>() {
//            @Override
//            public void onResponse(Call<RootDays> call, Response<RootDays> response) {
//                mListDays = (ArrayList<ListDays>) response.body().getList();
//                populateWidget();
//            }
//
//            @Override
//            public void onFailure(Call<RootDays> call, Throwable t) {
//
//            }
//        });
//    }

    private void getHourlyWeather(double latitude, double longitude, final boolean isCelsius){
        WeatherService.getWeather().weatherHours(latitude, longitude, "metric").enqueue(new Callback<RootHours>() {
            @Override
            public void onResponse(Call<RootHours> call, Response<RootHours> response) {
                mListDays = (ArrayList<ListHours>) response.body().getList();
            }

            @Override
            public void onFailure(Call<RootHours> call, Throwable t) {
                t.getMessage();
            }
        });
    }


    protected boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }
        return isAvailable;
    }

    private void populateWidget() {

        Intent widgetUpdateIntent = new Intent();
        widgetUpdateIntent.setAction(ClockAppWidget.DATA_FETCHED);
        widgetUpdateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                appWidgetId);
        sendBroadcast(widgetUpdateIntent);

        this.stopSelf();
    }

    }

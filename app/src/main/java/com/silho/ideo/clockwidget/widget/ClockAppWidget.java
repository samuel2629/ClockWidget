package com.silho.ideo.clockwidget.widget;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.AlarmClock;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;

import android.widget.RemoteViews;
import android.widget.Toast;


import com.silho.ideo.clockwidget.model.ListHours;
import com.silho.ideo.clockwidget.model.Root;
import com.silho.ideo.clockwidget.model.RootHours;
import com.silho.ideo.clockwidget.retofitApi.WeatherService;
import com.silho.ideo.clockwidget.ui.MainActivity;
import com.silho.ideo.clockwidget.R;
import com.silho.ideo.clockwidget.utils.MyLocation;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClockAppWidget extends AppWidgetProvider {

    public static final String PACKAGE_NAME = "com.silho.ideo.clockwidget";
    private static String mPlace;
    private static String mWeatherTemp;
    private static String mWeatherTempMinMax;
    private static int mIcon;
    private static ArrayList<ListHours> mListDays;
    public static String CLICKED = "clicked";
    private static boolean isClicked = false;
    private static boolean isSmall = false;
    private static boolean isMedium = false;
    private static boolean isBig = false;

    public static class ClockUpdateService extends Service {

        private MyLocation myLocation;

        public ClockUpdateService(){}

        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }

        public static final String ACTION_UPDATE = "com.silho.clockwidget.action.UPDATE";
        private IntentFilter mIntentFilter;

        private final BroadcastReceiver clockChangedReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                updateTime(context);
            }
        };

        @SuppressLint("MissingPermission")
        @Override
        public void onCreate() {
            myLocation = new MyLocation();
            MyLocation.LocationResult mLocationResult = new MyLocation.LocationResult() {
                @Override
                public void gotLocation(Location location) {

                    Geocoder geocoder = new Geocoder(ClockUpdateService.this, Locale.getDefault());
                    List<Address> addresses = null;
                    try {
                        addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mPlace = addresses.get(0).getLocality();
                    updateTime(getApplicationContext());

                    if(isNetworkAvailable() &&  mPlace != null) {
                        getCurrentWeather(location.getLatitude(), location.getLongitude());
                        getHourlyWeather(location.getLatitude(), location.getLongitude());
                    }

//                        getWeatherRoot(location.getLatitude(), location.getLongitude(), mPlace,
//                                PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
//                                        .getBoolean(getString(R.string.on_celsius), true));

                }
            };

            myLocation.getLocation(this, mLocationResult, 0);
            super.onCreate();
            mIntentFilter = new IntentFilter();
            mIntentFilter.addAction(Intent.ACTION_TIME_TICK);
            mIntentFilter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
            mIntentFilter.addAction(Intent.ACTION_TIME_CHANGED);
            mIntentFilter.addAction(ACTION_UPDATE);
            registerReceiver(clockChangedReceiver, mIntentFilter);
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            if(myLocation != null)
                myLocation.removeUpdate();
            unregisterReceiver(clockChangedReceiver);
        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            if(intent != null && intent.getAction() != null){
                if(intent.getAction().equals(ACTION_UPDATE)){
                    updateTime(this);
                }
            }
            return super.onStartCommand(intent, flags, startId);
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

        private void getHourlyWeather(double latitude, double longitude){
            WeatherService.getWeather().weatherHours(latitude, longitude, "metric").enqueue(new Callback<RootHours>() {
                @Override
                public void onResponse(Call<RootHours> call, Response<RootHours> response) {
                    mListDays = (ArrayList<ListHours>) response.body().getList();
                    updateTime(getApplicationContext());
                }

                @Override
                public void onFailure(Call<RootHours> call, Throwable t) {
                    t.getMessage();
                }
            });
        }

        private void getCurrentWeather(double latitude, double longitude){
            WeatherService.getWeather().weather(latitude, longitude, "metric").enqueue(new Callback<Root>() {
                @Override
                public void onResponse(Call<Root> call, Response<Root> response) {
                    mIcon = response.body().getWeather().get(0).getIconId(response.body().getWeather().get(0).getIcon());
                    mWeatherTemp = String.valueOf(Math.round(response.body().getMain().getTemp())) + "°";
                    mWeatherTempMinMax = String.valueOf(Math.round(response.body().getMain().getTempMin()) +  "/"
                            + Math.round(response.body().getMain().getTempMax()) + "°");
                    updateTime(getApplicationContext());
                }

                @Override
                public void onFailure(Call<Root> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), R.string.error_getting_weather, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        updateAppWidget(context, appWidgetManager, appWidgetId);
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for(int appWidgetId: appWidgetIds) {
            getGoodLayout(context, appWidgetManager, appWidgetId);
            Intent intent = new Intent(ClockUpdateService.ACTION_UPDATE);
            intent.setPackage(PACKAGE_NAME);
        }
    }

    private static RemoteViews getGoodLayout(Context context, AppWidgetManager appWidgetManager, int appWidgetId){
        RemoteViews rv = null;
        if(isSmall){
            if(isClicked){
                rv = getLayoutForecastWeather2_1(context, appWidgetManager, appWidgetId);
            } else {
                rv = getLayoutCurrentWeather2_1(context, appWidgetManager, appWidgetId);
            }
        } else if(isMedium){
            if(isClicked){
                rv = getLayoutForecastWeather3_1(context, appWidgetManager, appWidgetId);
            } else {
                rv = getLayoutCurrentWeather3_1(context, appWidgetManager, appWidgetId);
            }
        } else if(isBig){
            if(isClicked){
                rv = getLayoutForecastWeather4_1(context, appWidgetManager, appWidgetId);
            } else {
                rv = getLayoutCurrentWeather4_1(context, appWidgetManager, appWidgetId);
            }
        }
        return rv;
    }

    @Override
    public void onEnabled(Context context) {
        context.startService(new Intent(context, ClockUpdateService.class));
    }

    @Override
    public void onDisabled(Context context) {
        context.stopService(new Intent(context, ClockUpdateService.class));
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction() != null && intent.getAction().equals(CLICKED)){
            if(isClicked)
                isClicked = false;
            else isClicked = true;
            updateTime(context);
        }
        super.onReceive(context, intent);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        Bundle options = appWidgetManager.getAppWidgetOptions(appWidgetId);
        int width = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH);
        RemoteViews rv;
        if (width > 100 && width < 200) {
            isSmall = true;
            isMedium = false;
            isBig = false;
            rv = getGoodLayout(context, appWidgetManager, appWidgetId);
        } else if(width > 200 && width < 300){
            isMedium = true;
            isSmall = false;
            isBig = false;
            rv = getGoodLayout(context, appWidgetManager, appWidgetId);
        } else {
            isBig = true;
            isSmall = false;
            isMedium = false;
            rv = getGoodLayout(context, appWidgetManager, appWidgetId);
        }
        appWidgetManager.updateAppWidget(appWidgetId, rv);
    }

    private static RemoteViews getLayoutCurrentWeather4_1(Context context, AppWidgetManager appWidgetManager, int appWidgetId){
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_current_weather_4_1);

        long timeInMillis = Calendar.getInstance().getTimeInMillis();
        SimpleDateFormat formatterTime = new SimpleDateFormat("HH:mm");
        SimpleDateFormat formatterDate = new SimpleDateFormat("EEEE dd MMMM");
        String time = formatterTime.format(timeInMillis);
        String date = formatterDate.format(timeInMillis).toUpperCase();

        remoteViews.setImageViewBitmap(R.id.appwidgetTimeTv, createBitmap(context, time, 150, "CaviarDreams", true));
        remoteViews.setImageViewBitmap(R.id.appwidgetDateTv, createBitmap(context, date, 35, "CaviarDreams", false));

        if (mWeatherTemp != null && mIcon != -1) {

            remoteViews.setImageViewBitmap(R.id.appwidgetPlaceTv, createBitmap(context, mPlace, 40, "CaviarDreams", false));
            remoteViews.setImageViewBitmap(R.id.appWidgetWeatherText, createBitmap(context, mWeatherTemp, 60, "CaviarDreams", false));
            remoteViews.setImageViewBitmap(R.id.appWidgetMinMaxTemp, createBitmap(context, mWeatherTempMinMax, 35, "CaviarDreams", false));
            remoteViews.setImageViewResource(R.id.appWidgetIconIv, mIcon);

        }

        Intent intentMainActivity = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0 , intentMainActivity,
                PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.weatherContainer, pendingIntent);

        Intent intentShowAlarms = new Intent(AlarmClock.ACTION_SHOW_ALARMS);
        PendingIntent pendingIntent1 = PendingIntent.getActivity(context, 0, intentShowAlarms,
                PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.dateAndTimeContainer, pendingIntent1);

        Intent intentToForecast = new Intent(context, ClockAppWidget.class);
        intentToForecast.setAction(CLICKED);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intentToForecast, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.buttonToForecast, pi);

        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);

        return remoteViews;
    }

    private static RemoteViews getLayoutCurrentWeather3_1(Context context, AppWidgetManager appWidgetManager, int appWidgetId){
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_current_weather_3_1);

        long timeInMillis = Calendar.getInstance().getTimeInMillis();
        SimpleDateFormat formatterTime = new SimpleDateFormat("HH:mm");
        SimpleDateFormat formatterDate = new SimpleDateFormat("EEEE dd MMMM ");
        String time = formatterTime.format(timeInMillis);
        String date = formatterDate.format(timeInMillis).toUpperCase();

        remoteViews.setImageViewBitmap(R.id.appwidgetTimeTv, createBitmap(context, time, 125, "CaviarDreams", true));
        remoteViews.setImageViewBitmap(R.id.appwidgetDateTv, createBitmap(context, date, 35, "CaviarDreams", false));

        if (mWeatherTemp != null && mIcon != -1) {

            remoteViews.setImageViewBitmap(R.id.appWidgetWeatherText, createBitmap(context, mWeatherTemp, 55, "CaviarDreams", false));
            remoteViews.setImageViewBitmap(R.id.appWidgetMinMaxTemp, createBitmap(context, mWeatherTempMinMax, 35, "CaviarDreams", false));
            remoteViews.setImageViewResource(R.id.appWidgetIconIv, mIcon);

        }

        Intent intentMainActivity = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0 , intentMainActivity,
                PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.weatherContainer, pendingIntent);

        Intent intentShowAlarms = new Intent(AlarmClock.ACTION_SHOW_ALARMS);
        PendingIntent pendingIntent1 = PendingIntent.getActivity(context, 0, intentShowAlarms,
                PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.dateAndTimeContainer, pendingIntent1);

        Intent intentToForecast = new Intent(context, ClockAppWidget.class);
        intentToForecast.setAction(CLICKED);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intentToForecast, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.buttonToForecast, pi);

        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);

        return remoteViews;
    }

    private static RemoteViews getLayoutCurrentWeather2_1(Context context, AppWidgetManager appWidgetManager, int appWidgetId){
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_current_weather_2_1);

        long timeInMillis = Calendar.getInstance().getTimeInMillis();
        SimpleDateFormat formatterTime = new SimpleDateFormat("HH:mm");
        SimpleDateFormat formatterDate = new SimpleDateFormat("EEEE dd MMMM ");
        String time = formatterTime.format(timeInMillis);
        String date = formatterDate.format(timeInMillis).toUpperCase();

        remoteViews.setImageViewBitmap(R.id.appwidgetTimeTv, createBitmap(context, time, 100, "CaviarDreams", true));
        remoteViews.setImageViewBitmap(R.id.appwidgetDateTv, createBitmap(context, date, 25, "CaviarDreams", false));

        if (mWeatherTemp != null && mIcon != -1) {

            remoteViews.setImageViewBitmap(R.id.appWidgetWeatherText, createBitmap(context, mWeatherTemp, 50, "CaviarDreams", false));
            remoteViews.setImageViewResource(R.id.appWidgetIconIv, mIcon);

        }

        Intent intentMainActivity = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0 , intentMainActivity,
                PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.weatherContainer, pendingIntent);

        Intent intentShowAlarms = new Intent(AlarmClock.ACTION_SHOW_ALARMS);
        PendingIntent pendingIntent1 = PendingIntent.getActivity(context, 0, intentShowAlarms,
                PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.dateAndTimeContainer, pendingIntent1);

        Intent intentToForecast = new Intent(context, ClockAppWidget.class);
        intentToForecast.setAction(CLICKED);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intentToForecast, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.buttonToForecast, pi);

        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);

        return remoteViews;
    }

    private static RemoteViews getLayoutForecastWeather4_1(Context context, AppWidgetManager appWidgetManager, int appWidgetId){
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_forecast_weather_4_1);

        if(mListDays != null){

            SimpleDateFormat formatterDate = new SimpleDateFormat("EEE dd");
            String date1 = formatterDate.format(mListDays.get(0).getDt());
            String date2 = formatterDate.format(mListDays.get(1).getDt());
            String date3 = formatterDate.format(mListDays.get(2).getDt());
            String date4 = formatterDate.format(mListDays.get(3).getDt());
            String date5 = formatterDate.format(mListDays.get(4).getDt());
            String date6 = formatterDate.format(mListDays.get(5).getDt());
            String date7 = formatterDate.format(mListDays.get(6).getDt());

            remoteViews.setImageViewBitmap(R.id.appwidgetDateTv, createBitmap(context, date1, 25, "CaviarDreams", false));
            remoteViews.setImageViewBitmap(R.id.appwidgetDateTv1, createBitmap(context, date2, 25, "CaviarDreams", false));
            remoteViews.setImageViewBitmap(R.id.appwidgetDateTv2, createBitmap(context, date3, 25, "CaviarDreams", false));
            remoteViews.setImageViewBitmap(R.id.appwidgetDateTv3, createBitmap(context, date4, 25, "CaviarDreams", false));
            remoteViews.setImageViewBitmap(R.id.appwidgetDateTv4, createBitmap(context, date5, 25, "CaviarDreams", false));
            remoteViews.setImageViewBitmap(R.id.appwidgetDateTv5, createBitmap(context, date6, 25, "CaviarDreams", false));
            remoteViews.setImageViewBitmap(R.id.appwidgetDateTv6, createBitmap(context, date7, 25, "CaviarDreams", false));

            remoteViews.setImageViewBitmap(R.id.appWidgetWeatherText, createBitmap(context, String.valueOf(Math.round(mListDays.get(0).getMain().getTemp())) + "°", 30, "CaviarDreams", false));
            remoteViews.setImageViewBitmap(R.id.appWidgetWeatherText1, createBitmap(context, String.valueOf(Math.round(mListDays.get(1).getMain().getTemp())) + "°", 30, "CaviarDreams", false));
            remoteViews.setImageViewBitmap(R.id.appWidgetWeatherText2, createBitmap(context, String.valueOf(Math.round(mListDays.get(2).getMain().getTemp())) + "°", 30, "CaviarDreams", false));
            remoteViews.setImageViewBitmap(R.id.appWidgetWeatherText3, createBitmap(context, String.valueOf(Math.round(mListDays.get(3).getMain().getTemp())) + "°", 30, "CaviarDreams", false));
            remoteViews.setImageViewBitmap(R.id.appWidgetWeatherText4, createBitmap(context, String.valueOf(Math.round(mListDays.get(4).getMain().getTemp())) + "°", 30, "CaviarDreams", false));
            remoteViews.setImageViewBitmap(R.id.appWidgetWeatherText5, createBitmap(context, String.valueOf(Math.round(mListDays.get(5).getMain().getTemp())) + "°", 30, "CaviarDreams", false));
            remoteViews.setImageViewBitmap(R.id.appWidgetWeatherText6, createBitmap(context, String.valueOf(Math.round(mListDays.get(6).getMain().getTemp())) + "°", 30, "CaviarDreams", false));

            remoteViews.setImageViewResource(R.id.iconIv, mListDays.get(0).getWeather().get(0).getIconId(mListDays.get(0).getWeather().get(0).getIcon()));
            remoteViews.setImageViewResource(R.id.iconIv, mListDays.get(1).getWeather().get(0).getIconId(mListDays.get(1).getWeather().get(0).getIcon()));
            remoteViews.setImageViewResource(R.id.iconIv, mListDays.get(2).getWeather().get(0).getIconId(mListDays.get(2).getWeather().get(0).getIcon()));
            remoteViews.setImageViewResource(R.id.iconIv, mListDays.get(3).getWeather().get(0).getIconId(mListDays.get(3).getWeather().get(0).getIcon()));
            remoteViews.setImageViewResource(R.id.iconIv, mListDays.get(4).getWeather().get(0).getIconId(mListDays.get(4).getWeather().get(0).getIcon()));
            remoteViews.setImageViewResource(R.id.iconIv, mListDays.get(5).getWeather().get(0).getIconId(mListDays.get(5).getWeather().get(0).getIcon()));
            remoteViews.setImageViewResource(R.id.iconIv, mListDays.get(6).getWeather().get(0).getIconId(mListDays.get(6).getWeather().get(0).getIcon()));
        }


        Intent intentToForecast = new Intent(context, ClockAppWidget.class);
        intentToForecast.setAction(CLICKED);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intentToForecast, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.buttonToCurrent, pi);

        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);

        return remoteViews;
    }

    private static RemoteViews getLayoutForecastWeather3_1(Context context, AppWidgetManager appWidgetManager, int appWidgetId){
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_forecast_weather_3_1);

        if(mListDays != null){

            SimpleDateFormat formatterDate = new SimpleDateFormat("EEE dd");
            String date1 = formatterDate.format(mListDays.get(0).getDt());
            String date2 = formatterDate.format(mListDays.get(1).getDt());
            String date3 = formatterDate.format(mListDays.get(2).getDt());
            String date4 = formatterDate.format(mListDays.get(3).getDt());
            String date5 = formatterDate.format(mListDays.get(4).getDt());

            remoteViews.setImageViewBitmap(R.id.appwidgetDateTv, createBitmap(context, date1, 25, "CaviarDreams", false));
            remoteViews.setImageViewBitmap(R.id.appwidgetDateTv1, createBitmap(context, date2, 25, "CaviarDreams", false));
            remoteViews.setImageViewBitmap(R.id.appwidgetDateTv2, createBitmap(context, date3, 25, "CaviarDreams", false));
            remoteViews.setImageViewBitmap(R.id.appwidgetDateTv3, createBitmap(context, date4, 25, "CaviarDreams", false));
            remoteViews.setImageViewBitmap(R.id.appwidgetDateTv4, createBitmap(context, date5, 25, "CaviarDreams", false));

            remoteViews.setImageViewBitmap(R.id.appWidgetWeatherText, createBitmap(context, String.valueOf(Math.round(mListDays.get(0).getMain().getTemp())) + "°", 30, "CaviarDreams", false));
            remoteViews.setImageViewBitmap(R.id.appWidgetWeatherText1, createBitmap(context, String.valueOf(Math.round(mListDays.get(1).getMain().getTemp())) + "°", 30, "CaviarDreams", false));
            remoteViews.setImageViewBitmap(R.id.appWidgetWeatherText2, createBitmap(context, String.valueOf(Math.round(mListDays.get(2).getMain().getTemp())) + "°", 30, "CaviarDreams", false));
            remoteViews.setImageViewBitmap(R.id.appWidgetWeatherText3, createBitmap(context, String.valueOf(Math.round(mListDays.get(3).getMain().getTemp())) + "°", 30, "CaviarDreams", false));
            remoteViews.setImageViewBitmap(R.id.appWidgetWeatherText4, createBitmap(context, String.valueOf(Math.round(mListDays.get(4).getMain().getTemp())) + "°", 30, "CaviarDreams", false));

            remoteViews.setImageViewResource(R.id.iconIv, mListDays.get(0).getWeather().get(0).getIconId(mListDays.get(0).getWeather().get(0).getIcon()));
            remoteViews.setImageViewResource(R.id.iconIv, mListDays.get(1).getWeather().get(0).getIconId(mListDays.get(1).getWeather().get(0).getIcon()));
            remoteViews.setImageViewResource(R.id.iconIv, mListDays.get(2).getWeather().get(0).getIconId(mListDays.get(2).getWeather().get(0).getIcon()));
            remoteViews.setImageViewResource(R.id.iconIv, mListDays.get(3).getWeather().get(0).getIconId(mListDays.get(3).getWeather().get(0).getIcon()));
            remoteViews.setImageViewResource(R.id.iconIv, mListDays.get(4).getWeather().get(0).getIconId(mListDays.get(4).getWeather().get(0).getIcon()));
        }


        Intent intentToForecast = new Intent(context, ClockAppWidget.class);
        intentToForecast.setAction(CLICKED);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intentToForecast, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.buttonToCurrent, pi);

        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);

        return remoteViews;
    }

    private static RemoteViews getLayoutForecastWeather2_1(Context context, AppWidgetManager appWidgetManager, int appWidgetId){
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_forecast_weather_2_1);

        if(mListDays != null){

            SimpleDateFormat formatterDate = new SimpleDateFormat("EEE dd");
            String date1 = formatterDate.format(mListDays.get(0).getDt());
            String date2 = formatterDate.format(mListDays.get(1).getDt());
            String date3 = formatterDate.format(mListDays.get(2).getDt());

            remoteViews.setImageViewBitmap(R.id.appwidgetDateTv, createBitmap(context, date1, 25, "CaviarDreams", false));
            remoteViews.setImageViewBitmap(R.id.appwidgetDateTv1, createBitmap(context, date2, 25, "CaviarDreams", false));
            remoteViews.setImageViewBitmap(R.id.appwidgetDateTv2, createBitmap(context, date3, 25, "CaviarDreams", false));

            remoteViews.setImageViewBitmap(R.id.appWidgetWeatherText, createBitmap(context, String.valueOf(Math.round(mListDays.get(0).getMain().getTemp())) + "°", 30, "CaviarDreams", false));
            remoteViews.setImageViewBitmap(R.id.appWidgetWeatherText1, createBitmap(context, String.valueOf(Math.round(mListDays.get(1).getMain().getTemp())) + "°", 30, "CaviarDreams", false));
            remoteViews.setImageViewBitmap(R.id.appWidgetWeatherText2, createBitmap(context, String.valueOf(Math.round(mListDays.get(2).getMain().getTemp())) + "°", 30, "CaviarDreams", false));

            remoteViews.setImageViewResource(R.id.iconIv, mListDays.get(0).getWeather().get(0).getIconId(mListDays.get(0).getWeather().get(0).getIcon()));
            remoteViews.setImageViewResource(R.id.iconIv, mListDays.get(1).getWeather().get(0).getIconId(mListDays.get(1).getWeather().get(0).getIcon()));
            remoteViews.setImageViewResource(R.id.iconIv, mListDays.get(2).getWeather().get(0).getIconId(mListDays.get(2).getWeather().get(0).getIcon()));
        }


        Intent intentToForecast = new Intent(context, ClockAppWidget.class);
        intentToForecast.setAction(CLICKED);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intentToForecast, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.buttonToCurrent, pi);

        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);

        return remoteViews;
    }

    private static Bitmap createBitmap(Context context, String text, int textSize, String font, boolean isShadowed){

        Typeface clock = Typeface.createFromAsset(context.getAssets(),"fonts/"+ font +".ttf");

        TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(textSize);
        textPaint.setTypeface(clock);
        if(isShadowed)
            textPaint.setShadowLayer(7.5f, 2.5f, 2.5f, Color.BLACK);
        int width = (int) textPaint.measureText(text);

        StaticLayout mTextLayout = new StaticLayout(text, textPaint, width, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);

        Bitmap b = Bitmap.createBitmap(width, mTextLayout.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.TRANSPARENT);

        c.drawPaint(paint);
        c.save();
        c.translate(0, 0);
        mTextLayout.draw(c);
        c.restore();

        return b;
    }

    private static void updateTime(Context context){
        RemoteViews remoteViews;
        remoteViews = getGoodLayout(context, AppWidgetManager.getInstance(context), 0);
        ComponentName clockWidget = new ComponentName(context, ClockAppWidget.class);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        appWidgetManager.updateAppWidget(clockWidget, remoteViews);
    }

}


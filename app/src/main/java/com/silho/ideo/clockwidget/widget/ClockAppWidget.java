package com.silho.ideo.clockwidget.widget;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import android.widget.RemoteViews;
import android.widget.Toast;

import com.silho.ideo.clockwidget.model.Root;
import com.silho.ideo.clockwidget.retofitApi.WeatherService;
import com.silho.ideo.clockwidget.ui.MainActivity;
import com.silho.ideo.clockwidget.R;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClockAppWidget extends AppWidgetProvider {

    public static final String PACKAGE_NAME = "com.silho.ideo.clockwidget";
    public static String mForeignPlace;
    public static String mPlace;
    private static String mWeatherSum;
    private static int mIcon;

    public static class ClockUpdateService extends Service implements LocationListener {

        private static final String TAG = "CLOCK_WIDGET";
        private LocationManager mLocationManager = null;
        private static final int LOCATION_INTERVAL = 1800000;
        private static final float LOCATION_DISTANCE = 1000f;
        private Geocoder mGeocoder;

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
            getLocation();
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
            if (mLocationManager != null) {
                mLocationManager.removeUpdates(this);
            }
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

        private void initializeLocationManager() {
            Log.e(TAG, "initializeLocationManager");
            if (mLocationManager == null) {
                mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
            }
        }

        private void getLocation() {
            Log.e(TAG, "onStartCommand");
            mGeocoder = new Geocoder(this, Locale.getDefault());

            Log.e(TAG, "onCreate");
            initializeLocationManager();
            try {
                mLocationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                        this);
            } catch (SecurityException ex) {
                Log.i(TAG, "fail to request location update, ignore", ex);
            } catch (IllegalArgumentException ex) {
                Log.d(TAG, "network provider does not exist, " + ex.getMessage());
            }
            try {
                mLocationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                        this);
            } catch (SecurityException ex) {
                Log.i(TAG, "fail to request location update, ignore", ex);
            } catch (IllegalArgumentException ex) {
                Log.d(TAG, "gps provider does not exist " + ex.getMessage());
            }
        }

        @Override
        public void onLocationChanged(Location location) {
            String place = null;
            try {
                List<Address> addresses = mGeocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                place = addresses.get(0).getLocality();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mPlace = place;
            getWeatherRoot(location.getLatitude(), location.getLongitude(), place, true);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }

        private void getWeatherRoot(double latitude, double longitude, final String place, final boolean isCelsius){
            WeatherService.getRootWeather().weatherRoot(latitude, longitude).enqueue(new Callback<Root>() {
                @Override
                public void onResponse(Call<Root> call, Response<Root> response) {
                    mIcon = response.body().getCurrently().getIconId(response.body().getCurrently().getIcon());
                    String temp = String.valueOf(response.body().getCurrently().getTemperature());
                    mWeatherSum = temp;
                    updateTime(getApplicationContext());
                }

                @Override
                public void onFailure(Call<Root> call, Throwable t) {
                    t.getMessage();
                    Toast.makeText(getApplicationContext(), R.string.error_getting_weather, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private static RemoteViews updateViews(Context context, AppWidgetManager appWidgetManager, int appWidgetId){
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.app_wid);
        // TODO : if automatic gmt timezone to normal
//        if(mForeignPlace != null){
//            remoteViews.setTextViewText(R.id.appwidgetPlaceTv, mForeignPlace);
//        } else {
//            remoteViews.setTextViewText(R.id.appwidgetPlaceTv, mPlace);
//        }

        long timeInMillis = Calendar.getInstance().getTimeInMillis();
        SimpleDateFormat formatterTime = new SimpleDateFormat("HH:mm");
        SimpleDateFormat formatterDate = new SimpleDateFormat("EEEE dd MMMM ");
        String time = formatterTime.format(timeInMillis);
        String date = formatterDate.format(timeInMillis);

        remoteViews.setTextViewText(R.id.appwidgetTimeTv, time);
        remoteViews.setTextViewText(R.id.appwidgetDateTv, date);
        if(mWeatherSum != null && mIcon != -1) {
            remoteViews.setTextViewText(R.id.appWidgetWeatherText, mWeatherSum);
            remoteViews.setImageViewResource(R.id.appWidgetIconIv, mIcon);
        }

        onClickWidget(context, remoteViews, appWidgetId, appWidgetManager);

        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);

        return remoteViews;

    }

    private static void onClickWidget(Context context, RemoteViews remoteViews,
                                      int appWidgetId, AppWidgetManager appWidgetManager) {
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0 , intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.containerWidget, pendingIntent);
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }

    private static void updateTime(Context context){
        RemoteViews remoteViews = updateViews(context, AppWidgetManager.getInstance(context), 0);
        ComponentName clockWidget = new ComponentName(context, ClockAppWidget.class);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        appWidgetManager.updateAppWidget(clockWidget, remoteViews);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        for(int appWidgetId: appWidgetIds) {
            updateViews(context, appWidgetManager, appWidgetId);
            Intent intent = new Intent(ClockUpdateService.ACTION_UPDATE);
            intent.setPackage(PACKAGE_NAME);
        }
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
        try {
            mForeignPlace = intent.getStringExtra(context.getString(R.string.foreign_place_key));
        } catch (Exception e){
            e.printStackTrace();
        }
        super.onReceive(context, intent);
    }
}


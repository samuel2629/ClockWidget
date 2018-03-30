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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.silho.ideo.clockwidget.ui.MainActivity;
import com.silho.ideo.clockwidget.R;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static com.silho.ideo.clockwidget.widget.ClockAppWidget.ClockUpdateService.mPlace;

/**
 * Implementation of App Widget functionality.
 */
public class ClockAppWidget extends AppWidgetProvider {

    public static final String PACKAGE_NAME = "com.silho.ideo.clockwidget";

    public static class ClockUpdateService extends Service {


        private static final long LOCATION_REFRESH_TIME = 30;
        private float LOCATION_REFRESH_DISTANCE = 1;

        private LocationManager mLocationManager;
        private Geocoder mGeocoder;

        private List<Address> mAddresses;
        public static String mPlace;

        private LocationListener mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                location.getLongitude();
                location.getLatitude();
                location.describeContents();
                try {
                    mAddresses = mGeocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    mPlace = mAddresses.get(0).getLocality();

                } catch (IOException e) {
                    e.printStackTrace();
                }
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
        };


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
            super.onCreate();

            mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            mGeocoder = new Geocoder(this, Locale.getDefault());

            if(isNetworkAvailable()) {
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        LOCATION_REFRESH_TIME, LOCATION_REFRESH_DISTANCE, mLocationListener);

                Location location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                try {
                    mAddresses = mGeocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 5);
                    mPlace = mAddresses.get(0).getLocality();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(this, R.string.network_error, Toast.LENGTH_SHORT).show();
            }
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
    }

    private static RemoteViews updateViews(Context context, AppWidgetManager appWidgetManager, int appWidgetId){
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.clock_app_widget_4_cells);
        long timeInMillis = Calendar.getInstance().getTimeInMillis();
        SimpleDateFormat formatterTime = new SimpleDateFormat("HH:mm");
        SimpleDateFormat formatterDate = new SimpleDateFormat("EEEE dd MMMM ");
        String time = formatterTime.format(timeInMillis);
        String date = formatterDate.format(timeInMillis);

        remoteViews.setTextViewText(R.id.appwidgetTimeTv, time);
        remoteViews.setTextViewText(R.id.appwidgetDateTv, date);
        remoteViews.setTextViewText(R.id.appwidgetPlaceTv, mPlace);

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
        updateTime(context);
        context.startService(new Intent(context, ClockUpdateService.class));
    }

    @Override
    public void onDisabled(Context context) {
        context.stopService(new Intent(context, ClockUpdateService.class));

    }
}


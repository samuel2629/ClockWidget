package com.silho.ideo.clockwidget.utils;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
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
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.silho.ideo.clockwidget.R;
import com.silho.ideo.clockwidget.widget.ClockAppWidget;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by Samuel on 03/04/2018.
 */

public class LocationTracker extends Service {

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

    @SuppressLint("MissingPermission")
    @Override
    public void onCreate() {
        super.onCreate();


        if (isNetworkAvailable()) {
            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                    LOCATION_REFRESH_TIME, LOCATION_REFRESH_DISTANCE, mLocationListener);

            Location locationNet = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            Location locationGps = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            try {
                if (locationNet != null) {
                    mAddresses = mGeocoder.getFromLocation(locationNet.getLatitude(), locationNet.getLongitude(), 5);
                    mPlace = mAddresses.get(0).getLocality();
                } else if (locationGps != null) {
                    mAddresses = mGeocoder.getFromLocation(locationGps.getLatitude(), locationGps.getLongitude(), 5);
                    mPlace = mAddresses.get(0).getLocality();
                }
                Intent intent = new Intent(ClockAppWidget.ClockUpdateService.ACTION_UPDATE);
                intent.putExtra("place", mPlace);
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, R.string.network_error, Toast.LENGTH_SHORT).show();
        }
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


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public String getPlace() {
        return mPlace;
    }
}

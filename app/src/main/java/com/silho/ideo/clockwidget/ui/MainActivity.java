package com.silho.ideo.clockwidget.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.AlarmClock;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.silho.ideo.clockwidget.R;
import com.silho.ideo.clockwidget.settings.SettingsActivity;
import com.silho.ideo.clockwidget.utils.TimeZoneProvider;
import com.silho.ideo.clockwidget.widget.ClockAppWidget;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import static com.silho.ideo.clockwidget.R.color.colorAccent;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    public static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 6;
    public static final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        setTitle("Weather");
        toolbar.setTitleMargin(4,2,2,0);
        toolbar.setTitleTextColor(Color.WHITE);

        setUpSharedPref();

        if(isNetworkAvailable()) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
            }

            getHourlyFragment();
            getDailyFragment();
        } else {
            Toast.makeText(this, R.string.network_error, Toast.LENGTH_SHORT).show();
        }
    }

    private void setUpSharedPref() {
        SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(getString(R.string.on_celsius), true).apply();
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getSharedPreferences(getPackageName(), MODE_PRIVATE).unregisterOnSharedPreferenceChangeListener(this);
    }

    private void getDailyFragment() {
        DailyFragment dailyFragment = new DailyFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.dailyContainer, dailyFragment).commit();
    }

    private void getHourlyFragment() {
        HourlyFragment hourlyFragment = new HourlyFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.hourlyContainer, hourlyFragment).commit();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.accessAlarm:
                Intent i = new Intent(AlarmClock.ACTION_SET_ALARM);
                i.putExtra(AlarmClock.EXTRA_MESSAGE, "New Alarm");
                i.putExtra(AlarmClock.EXTRA_HOUR, 10);
                i.putExtra(AlarmClock.EXTRA_MINUTES, 30);
                startActivity(i);
                return true;
            case R.id.accessSettings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals(R.string.on_celsius)){

        }
    }
}


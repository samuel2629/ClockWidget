package com.silho.ideo.clockwidget.ui;

import android.Manifest;
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
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.provider.AlarmClock;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.silho.ideo.clockwidget.R;
import com.silho.ideo.clockwidget.model.Currently;
import com.silho.ideo.clockwidget.model.Datum_;
import com.silho.ideo.clockwidget.model.Datum__;
import com.silho.ideo.clockwidget.model.Root;
import com.silho.ideo.clockwidget.retofitApi.WeatherService;
import com.silho.ideo.clockwidget.settings.SettingsActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements LocationListener,
        SharedPreferences.OnSharedPreferenceChangeListener{

    public static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 6;
    public static final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 7;
    private static final String TAG = "CLOCK_WIDGET";
    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 1800000;
    private static final float LOCATION_DISTANCE = 1000f;
    private Geocoder mGeocoder;
    private List<Datum_> mHours;
    private List<Datum__> mDays;
    private SharedPreferences mSharedPreferences;



    @BindView(R.id.temperatureLabel) TextView mTempLabel;
    @BindView(R.id.locationLabel) TextView mLocationLabel;
    @BindView(R.id.humidityValue) TextView mHumidityValue;
    @BindView(R.id.precipValue) TextView mPrecipValue;
    @BindView(R.id.iconTime) ImageView mIconTime;
    @BindView(R.id.swipeRefresher) SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mSwipeRefreshLayout.setRefreshing(true);
        setupSharedPref();
        if(isNetworkAvailable())
            getLocation();

        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.title_toolbar));
        toolbar.setTitleMargin(4, 2, 2, 0);
        toolbar.setTitleTextColor(Color.WHITE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
        }

        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                getLocation();
            }
        });

    }

    private void setupSharedPref(){
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mSharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION || requestCode == MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION){
            getLocation();
        } else {
            Toast.makeText(this, R.string.permission_not_granted, Toast.LENGTH_SHORT).show();
        }
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
    protected void onDestroy() {
        super.onDestroy();
        if (mLocationManager != null) {
            mLocationManager.removeUpdates(this);
        }
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
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

        if(isNetworkAvailable() &&  place != null)
            getWeatherRoot(location.getLatitude(), location.getLongitude(), place, mSharedPreferences.getBoolean(getString(R.string.on_celsius), true));
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        Log.e(TAG, "onStatusChanged");
    }

    @Override
    public void onProviderEnabled(String s) {
        Log.e(TAG, "onProviderEnabled");
    }

    @Override
    public void onProviderDisabled(String s) {
        Log.e(TAG, "onProviderDisabled");
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if(s.equals(getString(R.string.pref_celsius_farhneit))){
            mSwipeRefreshLayout.setRefreshing(true);
            if(sharedPreferences.getBoolean(getString(R.string.on_celsius), false)) {
                sharedPreferences.edit().putBoolean(getString(R.string.on_celsius), false).apply();
            } else {
                sharedPreferences.edit().putBoolean(getString(R.string.on_celsius), true).apply();
            }
            getLocation();
        }
    }

    private void getWeatherRoot(double latitude, double longitude, final String place, final boolean isCelsius){
        WeatherService.getRootWeather().weatherRoot(latitude, longitude).enqueue(new Callback<Root>() {
            @Override
            public void onResponse(Call<Root> call, Response<Root> response) {
                mSwipeRefreshLayout.setRefreshing(false);
                mHours = response.body().getHourly().getData();
                mDays = response.body().getDaily().getData();
                Currently currently = response.body().getCurrently();
                if(isCelsius)
                    mTempLabel.setText(String.format("%s°", String.valueOf((currently.getTemperatureCelsius()))));
                else
                    mTempLabel.setText(currently.getTemperature());
                getHourlyFragment(isCelsius);
                getDailyFragment(isCelsius);
                mLocationLabel.setText(place);
                mPrecipValue.setText(String.format("%s %%", String.valueOf(Math.round(currently.getPrecipProbability() * 100))));
                mHumidityValue.setText(String.format("%s %%", String.valueOf(Math.round(currently.getHumidity() * 100))));
                mIconTime.setImageResource(currently.getIconId(currently.getIcon()));
            }

            @Override
            public void onFailure(Call<Root> call, Throwable t) {
                t.getMessage();
                Toast.makeText(MainActivity.this, R.string.error_getting_weather, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getDailyFragment(boolean isCelsius) {
        DailyFragment dailyFragment = new DailyFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(getResources().getString(R.string.days_arraylist), (ArrayList<? extends Parcelable>) mDays);
        bundle.putBoolean(getString(R.string.on_celsius), isCelsius);
        dailyFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.dailyContainer, dailyFragment).commitAllowingStateLoss();
    }

    private void getHourlyFragment(boolean isCelsius) {
        HourlyFragment hourlyFragment = new HourlyFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(getResources().getString(R.string.hours_arraylist), (ArrayList<? extends Parcelable>) mHours);
        bundle.putBoolean(getString(R.string.on_celsius), isCelsius);
        hourlyFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.hourlyContainer, hourlyFragment).commitAllowingStateLoss();
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

}


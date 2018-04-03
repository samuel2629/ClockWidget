package com.silho.ideo.clockwidget.ui;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Parcelable;
import android.provider.AlarmClock;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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
import com.silho.ideo.clockwidget.utils.LocationService;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener{

    public static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 6;
    public static final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 7;
    @BindView(R.id.temperatureLabel) TextView mTempLabel;
    @BindView(R.id.locationLabel) TextView mLocationLabel;
    @BindView(R.id.humidityValue) TextView mHumidityValue;
    @BindView(R.id.precipValue) TextView mPrecipValue;
    @BindView(R.id.iconTime) ImageView mIconTime;
    @BindView(R.id.progressBar) ProgressBar mProgressBar;

    private List<Datum_> mHours;
    private List<Datum__> mDays;
    private SharedPreferences.Editor mEditor;

    private BroadcastReceiver mMessageReceiver =  new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {
            double latitude = intent.getDoubleExtra(getResources().getString(R.string.latitude), 0);
            double longitude = intent.getDoubleExtra(getResources().getString(R.string.longitude), 0);
            String place = intent.getStringExtra(getResources().getString(R.string.place));
            saveLocationInfos(latitude, longitude, place);
            if(isNetworkAvailable())
                getWeatherRoot(latitude, longitude, place);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.title_toolbar));
        toolbar.setTitleMargin(4,2,2,0);
        toolbar.setTitleTextColor(Color.WHITE);

        startService(new Intent(this, LocationService.class));
        setUpSharedPref();

        if(isNetworkAvailable()) {
            retrieveLocationInfos();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
            }
        } else {
            Toast.makeText(this, R.string.network_error, Toast.LENGTH_SHORT).show();
        }
    }

    private void retrieveLocationInfos() {
        if(getSharedPreferences(getPackageName(), MODE_PRIVATE).contains(getString(R.string.latitude)) &&
                getSharedPreferences(getPackageName(), MODE_PRIVATE).contains(getString(R.string.longitude)) &&
                getSharedPreferences(getPackageName(), MODE_PRIVATE).contains(getString(R.string.place))) {
            double latitude = Double.parseDouble(getSharedPreferences(getPackageName(), MODE_PRIVATE).getString(getResources().getString(R.string.latitude), null));
            double longitude = Double.parseDouble(getSharedPreferences(getPackageName(), MODE_PRIVATE).getString(getResources().getString(R.string.longitude), null));
            String place = getSharedPreferences(getPackageName(), MODE_PRIVATE).getString(getResources().getString(R.string.place), null);
            getWeatherRoot(latitude, longitude, place);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION || requestCode == MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION){
            startService(new Intent(this, LocationService.class));
        } else {
            Toast.makeText(this, R.string.permission_not_granted, Toast.LENGTH_SHORT).show();
        }
    }

    private void saveLocationInfos(double latitude, double longitude, String place) {
        mEditor.putString(getResources().getString(R.string.latitude), Double.toString(latitude));
        mEditor.putString(getResources().getString(R.string.longitude), Double.toString(longitude));
        mEditor.putString(getResources().getString(R.string.place), place);
        mEditor.apply();
    }

    private void setUpSharedPref() {
        SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
        mEditor = sharedPreferences.edit();
        mEditor.putBoolean(getString(R.string.on_celsius), true).apply();
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getSharedPreferences(getPackageName(), MODE_PRIVATE).unregisterOnSharedPreferenceChangeListener(this);
    }

    private void getWeatherRoot(double latitude, double longitude, final String place){
        WeatherService.getRootWeather().weatherRoot(latitude, longitude).enqueue(new Callback<Root>() {
            @Override
            public void onResponse(Call<Root> call, Response<Root> response) {
                mProgressBar.setVisibility(View.GONE);
                mHours = response.body().getHourly().getData();
                mDays = response.body().getDaily().getData();
                getHourlyFragment();
                getDailyFragment();
                Currently currently = response.body().getCurrently();
                mTempLabel.setText(String.valueOf(Math.round(currently.getTemperature())));
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

    private void getDailyFragment() {
        DailyFragment dailyFragment = new DailyFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(getResources().getString(R.string.days_arraylist), (ArrayList<? extends Parcelable>) mDays);
        dailyFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.dailyContainer, dailyFragment).commit();
    }

    private void getHourlyFragment() {
        HourlyFragment hourlyFragment = new HourlyFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(getResources().getString(R.string.hours_arraylist), (ArrayList<? extends Parcelable>) mHours);
        hourlyFragment.setArguments(bundle);
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

    @Override
    protected void onResume() {
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter(getResources().getString(R.string.location_intent_filter_key)));
        super.onResume();
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onPause();
    }
}


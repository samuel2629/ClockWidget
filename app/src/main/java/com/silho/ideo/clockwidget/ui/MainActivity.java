package com.silho.ideo.clockwidget.ui;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.provider.AlarmClock;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.silho.ideo.clockwidget.R;
import com.silho.ideo.clockwidget.model.ListDays;
import com.silho.ideo.clockwidget.model.ListHours;
import com.silho.ideo.clockwidget.model.Root;
import com.silho.ideo.clockwidget.model.RootDays;
import com.silho.ideo.clockwidget.model.RootHours;
import com.silho.ideo.clockwidget.retofitApi.WeatherService;
import com.silho.ideo.clockwidget.settings.SettingsActivity;
import com.silho.ideo.clockwidget.utils.FontHelper;
import com.silho.ideo.clockwidget.utils.MyLocation;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener{

    public static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 6;
    public static final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 7;
    private SharedPreferences mSharedPreferences;

    @BindView(R.id.temperatureLabel) TextView mTempLabel;
    @BindView(R.id.locationLabel) TextView mLocationLabel;
    @BindView(R.id.humidityValue) TextView mHumidityValue;
    @BindView(R.id.precipValue) TextView mPrecipValue;
    @BindView(R.id.iconTime) ImageView mIconTime;
    @BindView(R.id.humidityLabel) TextView mHumidityLabel;
    @BindView(R.id.precipLabel) TextView mWindSpeedLabel;
    @BindView(R.id.swipeRefresher) SwipeRefreshLayout mSwipeRefreshLayout;

    private MyLocation.LocationResult mLocationResult;
    private MyLocation myLocation;

    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        FontHelper.setCustomTypeface(mTempLabel);
        FontHelper.setCustomTypeface(mLocationLabel);
        FontHelper.setCustomTypeface(mHumidityValue);
        FontHelper.setCustomTypeface(mPrecipValue);
        FontHelper.setCustomTypeface(mWindSpeedLabel);
        FontHelper.setCustomTypeface(mHumidityLabel);


        myLocation = new MyLocation();
        mLocationResult = new MyLocation.LocationResult() {
            @Override
            public void gotLocation(Location location) {

                Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                List<Address> addresses = null;
                String place = "";
                try {
                    addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(addresses != null)
                    place = addresses.get(0).getLocality();

                if(isNetworkAvailable() &&  place != null)
                    getCurrentWeather(location.getLatitude(), location.getLongitude(), place, mSharedPreferences.getBoolean(getString(R.string.on_celsius), true));
                    getHourlyWeather(location.getLatitude(), location.getLongitude(), mSharedPreferences.getBoolean(getString(R.string.on_celsius), true));
                    //getDailyWeather(location.getLatitude(), location.getLongitude(), mSharedPreferences.getBoolean(getString(R.string.on_celsius), true));
            }
        };

        getWindow().setStatusBarColor(getColor(R.color.colorAccent));

        mSwipeRefreshLayout.setRefreshing(true);
        setupSharedPref();

        Toolbar toolbar = findViewById(R.id.my_toolbar);
        TextView toolbarTitleTv = toolbar.findViewById(R.id.toolbar_title);
        setTitle("");
        setSupportActionBar(toolbar);

        toolbarTitleTv.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/CaviarDreams_Bold.ttf"));
        toolbarTitleTv.setText("Weather");

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
        } else {
            myLocation.getLocation(this, mLocationResult, 0);
        }

        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                myLocation.getLocation(MainActivity.this, mLocationResult, 0);
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
            myLocation.getLocation(MainActivity.this, mLocationResult,0);

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
                Intent i = new Intent(AlarmClock.ACTION_SHOW_ALARMS);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
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
            myLocation.getLocation(MainActivity.this, mLocationResult, 0);
        }
    }

    private void getCurrentWeather(double latitude, double longitude, final String place, boolean isCelsius){
        if(isCelsius) {
            WeatherService.getWeather().weather(latitude, longitude, "metric").enqueue(new Callback<Root>() {
                @Override
                public void onResponse(Call<Root> call, Response<Root> response) {
                    mSwipeRefreshLayout.setRefreshing(false);
                    mLocationLabel.setText(place);
                    mHumidityValue.setText(String.format("%s %%", String.valueOf(response.body().getMain().getHumidity())));
                    mTempLabel.setText(String.format("%s°", String.valueOf(Math.round(response.body().getMain().getTemp()))));
                    mPrecipValue.setText(String.format("%s m/s", String.valueOf(Math.round(response.body().getWind().getSpeed()))));
                    mIconTime.setImageResource(response.body().getWeather().get(0).getIconId(response.body().getWeather().get(0).getIcon()));
                }

                @Override
                public void onFailure(Call<Root> call, Throwable t) {
                    Toast.makeText(MainActivity.this, R.string.error_getting_weather, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            WeatherService.getWeather().weather(latitude, longitude, "imperial").enqueue(new Callback<Root>() {
                @Override
                public void onResponse(Call<Root> call, Response<Root> response) {
                    mSwipeRefreshLayout.setRefreshing(false);
                    mLocationLabel.setText(place);
                    mHumidityValue.setText(String.format("%s %%", String.valueOf(response.body().getMain().getHumidity())));
                    mTempLabel.setText(String.format("%s°", String.valueOf(Math.round(response.body().getMain().getTemp()))));
                    mPrecipValue.setText(String.format("%s m/s", String.valueOf(Math.round(response.body().getWind().getSpeed()))));
                    mIconTime.setImageResource(response.body().getWeather().get(0).getIconId(response.body().getWeather().get(0).getIcon()));
                }

                @Override
                public void onFailure(Call<Root> call, Throwable t) {
                    Toast.makeText(MainActivity.this, R.string.error_getting_weather, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void getHourlyWeather(double latitude, double longitude, final boolean isCelsius){
        if(isCelsius) {
            WeatherService.getWeather().weatherHours(latitude, longitude, "metric").enqueue(new Callback<RootHours>() {
                @Override
                public void onResponse(Call<RootHours> call, Response<RootHours> response) {
                    List<ListHours> mHours = response.body().getList();
                    getHourlyFragment(isCelsius, mHours);
                }

                @Override
                public void onFailure(Call<RootHours> call, Throwable t) {
                    Toast.makeText(MainActivity.this, R.string.error_getting_weather, Toast.LENGTH_SHORT).show();

                }
            });
        } else {
            WeatherService.getWeather().weatherHours(latitude, longitude, "imperial").enqueue(new Callback<RootHours>() {
                @Override
                public void onResponse(Call<RootHours> call, Response<RootHours> response) {
                    List<ListHours> mHours = response.body().getList();
                    getHourlyFragment(isCelsius, mHours);
                }

                @Override
                public void onFailure(Call<RootHours> call, Throwable t) {
                    Toast.makeText(MainActivity.this, R.string.error_getting_weather, Toast.LENGTH_SHORT).show();

                }
            });
        }
    }

    private void getDailyWeather(double latitude, double longitude, final boolean isCelsius) {
        if (isCelsius) {
            WeatherService.getWeather().weatherDays(latitude, longitude, "metric").enqueue(new Callback<RootDays>() {
                @Override
                public void onResponse(Call<RootDays> call, Response<RootDays> response) {
                    response.body();
                    List<ListDays> days = response.body().getList();
                    getDailyFragment(isCelsius, days);
                }

                @Override
                public void onFailure(Call<RootDays> call, Throwable t) {
                    Toast.makeText(MainActivity.this, R.string.error_getting_weather, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            WeatherService.getWeather().weatherDays(latitude, longitude, "imperial").enqueue(new Callback<RootDays>() {
                @Override
                public void onResponse(Call<RootDays> call, Response<RootDays> response) {
                    response.body();
                    List<ListDays> days = response.body().getList();
                    getDailyFragment(isCelsius, days);
                }

                @Override
                public void onFailure(Call<RootDays> call, Throwable t) {
                    Toast.makeText(MainActivity.this, R.string.error_getting_weather, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void getDailyFragment(boolean isCelsius, List<ListDays> days) {
        DailyFragment dailyFragment = new DailyFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(getResources().getString(R.string.days_arraylist), (ArrayList<? extends Parcelable>) days);
        bundle.putBoolean(getString(R.string.on_celsius), isCelsius);
        dailyFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.dailyContainer, dailyFragment).commitAllowingStateLoss();
    }

    private void getHourlyFragment(boolean isCelsius, List<ListHours> hours) {
        HourlyFragment hourlyFragment = new HourlyFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(getResources().getString(R.string.hours_arraylist), (ArrayList<? extends Parcelable>) hours);
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

}


package com.silho.ideo.clockwidget.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.silho.ideo.clockwidget.R;

public class MainActivity extends AppCompatActivity {

    private static final long LOCATION_REFRESH_TIME = 1000 * 3600;
    private float LOCATION_REFRESH_DISTANCE = 60;
    private LocationListener mLocationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                LOCATION_REFRESH_TIME, LOCATION_REFRESH_DISTANCE, mLocationListener);

        getHourlyFragment();
        getDailyFragment();



    }

    private void getDailyFragment() {
        DailyFragment dailyFragment = new DailyFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.dailyContainer, dailyFragment).commit();
    }

    private void getHourlyFragment() {
        HourlyFragment hourlyFragment = new HourlyFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.hourlyContainer, hourlyFragment).commit();
    }
}

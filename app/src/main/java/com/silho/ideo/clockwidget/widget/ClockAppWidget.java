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
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.provider.AlarmClock;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;

import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.silho.ideo.clockwidget.model.Root;
import com.silho.ideo.clockwidget.retofitApi.WeatherService;
import com.silho.ideo.clockwidget.ui.MainActivity;
import com.silho.ideo.clockwidget.R;
import com.silho.ideo.clockwidget.utils.MyLocation;

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

                    if(isNetworkAvailable() &&  mPlace != null)
                        getWeatherRoot(location.getLatitude(), location.getLongitude(), mPlace,
                                PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                                        .getBoolean(getString(R.string.on_celsius), true));
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


        private void getWeatherRoot(double latitude, double longitude, final String place, final boolean isCelsius){
            WeatherService.getRootWeather().weatherRoot(latitude, longitude).enqueue(new Callback<Root>() {
                @Override
                public void onResponse(Call<Root> call, Response<Root> response) {
                    mIcon = response.body().getCurrently().getIconId(response.body().getCurrently().getIcon());
                    String temp = String.valueOf(response.body().getCurrently().getTemperatureCelsius()) + "°";
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

    private static RemoteViews getLayout4_2(Context context, AppWidgetManager appWidgetManager, int appWidgetId){
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_4_2);

        long timeInMillis = Calendar.getInstance().getTimeInMillis();
        SimpleDateFormat formatterTime = new SimpleDateFormat("HH:mm");
        SimpleDateFormat formatterDate = new SimpleDateFormat("EEEE dd MMMM ");
        String time = formatterTime.format(timeInMillis);
        String date = formatterDate.format(timeInMillis);

        remoteViews.setImageViewBitmap(R.id.appwidgetTimeTv, createBitmap(context, time, 250, "hvBold"));
        remoteViews.setImageViewBitmap(R.id.appwidgetDateTv, createBitmap(context, date, 60, "hvItalic"));

        if (mWeatherSum != null && mIcon != -1) {

            remoteViews.setImageViewBitmap(R.id.appwidgetPlaceTv, createBitmap(context, mPlace, 60, "hvBold"));
            remoteViews.setImageViewBitmap(R.id.appWidgetWeatherText, createBitmap(context, mWeatherSum, 125, "hvBold"));
            remoteViews.setImageViewResource(R.id.appWidgetIconIv, mIcon);

        }

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0 , intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.iconIv, pendingIntent);

        Intent intent1 = new Intent(AlarmClock.ACTION_SHOW_ALARMS);
        PendingIntent pendingIntent1 = PendingIntent.getActivity(context, 0, intent1,
                PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.dateAndTimeContainer, pendingIntent1);

        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);

        return remoteViews;

    }

    private static RemoteViews getLayout3_2(Context context, AppWidgetManager appWidgetManager, int appWidgetId){
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_3_2);

        long timeInMillis = Calendar.getInstance().getTimeInMillis();
        SimpleDateFormat formatterTime = new SimpleDateFormat("HH:mm");
        SimpleDateFormat formatterDate = new SimpleDateFormat("EEEE dd MMMM ");
        String time = formatterTime.format(timeInMillis);
        String date = formatterDate.format(timeInMillis);

        remoteViews.setImageViewBitmap(R.id.appwidgetTimeTv, createBitmap(context, time, 250, "hvBold"));
        remoteViews.setImageViewBitmap(R.id.appwidgetDateTv, createBitmap(context, date, 60, "hvItalic"));

        if (mWeatherSum != null && mIcon != -1) {

            remoteViews.setImageViewBitmap(R.id.appWidgetWeatherText, createBitmap(context, mWeatherSum, 125, "hvBold"));
            remoteViews.setImageViewResource(R.id.appWidgetIconIv, mIcon);

        }

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0 , intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.iconIv, pendingIntent);

        Intent intent1 = new Intent(AlarmClock.ACTION_SHOW_ALARMS);
        PendingIntent pendingIntent1 = PendingIntent.getActivity(context, 0, intent1,
                PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.dateAndTimeContainer, pendingIntent1);

        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);

        return remoteViews;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        Bundle options = appWidgetManager.getAppWidgetOptions(appWidgetId);
        int width = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH);
        RemoteViews rv;
        if (width < 300) {
            rv = getLayout3_2(context, appWidgetManager, appWidgetId);
        } else {
            rv = getLayout4_2(context, appWidgetManager, appWidgetId);
        }
        appWidgetManager.updateAppWidget(appWidgetId, rv);
    }


    public static Bitmap buildUpdate(String text, Context context, int widthBitmap, int heightBitmap,
                                     String font, int xText, int yText, int textSize, boolean isTooLong) {
        Bitmap myBitmap = Bitmap.createBitmap(widthBitmap, heightBitmap, Bitmap.Config.ARGB_8888);

        Canvas myCanvas = new Canvas(myBitmap);
        TextPaint paint = new TextPaint();
        Typeface clock = Typeface.createFromAsset(context.getAssets(),"fonts/"+ font +".ttf");
        paint.setAntiAlias(true);
        paint.setSubpixelText(true);
        paint.setTypeface(clock);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        paint.setTextSize(textSize);
        paint.setTextAlign(Paint.Align.CENTER);

        if(isTooLong){
            text = (String) TextUtils.ellipsize(text, paint, myBitmap.getWidth() - 10, TextUtils.TruncateAt.END);
        }

        myCanvas.drawText(text, xText, yText, paint);
        return myBitmap;
    }

    private static Bitmap createBitmap(Context context, String text, int textSize, String font){

        Typeface clock = Typeface.createFromAsset(context.getAssets(),"fonts/"+ font +".ttf");

        TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(textSize);
        textPaint.setTypeface(clock);
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
        RemoteViews remoteViews = getLayout4_2(context, AppWidgetManager.getInstance(context), 0);
        ComponentName clockWidget = new ComponentName(context, ClockAppWidget.class);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        appWidgetManager.updateAppWidget(clockWidget, remoteViews);
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        updateAppWidget(context, appWidgetManager, appWidgetId);
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        for(int appWidgetId: appWidgetIds) {
            getLayout4_2(context, appWidgetManager, appWidgetId);
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


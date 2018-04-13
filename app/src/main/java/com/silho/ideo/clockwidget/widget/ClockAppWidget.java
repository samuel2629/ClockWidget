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
import android.content.pm.PackageManager;
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
import android.support.v7.preference.PreferenceManager;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;

import android.view.animation.AnimationUtils;
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

import static com.silho.ideo.clockwidget.widget.ClockUpdateService.updateAppWidget;

public class ClockAppWidget extends AppWidgetProvider {

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        updateAppWidget(context, appWidgetManager, appWidgetId);
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        ComponentName componentName = new ComponentName(context, ClockAppWidget.class);
        int[] allWidgetIds = appWidgetManager.getAppWidgetIds(componentName);

        Intent intent = new Intent(context, ClockUpdateService.class);
        intent.setAction(ClockUpdateService.ACTION_UPDATE);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, allWidgetIds);
        context.startService(intent);
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
        System.out.println(intent.getAction());
        super.onReceive(context, intent);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName componentName = new ComponentName(context, ClockAppWidget.class);
        onUpdate(context, appWidgetManager, appWidgetManager.getAppWidgetIds(componentName));
    }
}


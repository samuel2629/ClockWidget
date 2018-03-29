package com.silho.ideo.clockwidget.widget;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.RemoteViews;

import com.silho.ideo.clockwidget.ui.MainActivity;
import com.silho.ideo.clockwidget.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Implementation of App Widget functionality.
 */
public class ClockAppWidget extends AppWidgetProvider {

    public static final String PACKAGE_NAME = "com.silho.ideo.clockwidget";

    public static class ClockUpdateService extends Service {

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

        @Override
        public void onCreate() {
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
    }

    private static RemoteViews updateViews(Context context, AppWidgetManager appWidgetManager, int appWidgetId){
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.clock_app_widget_4_cells);
        long timeInMillis = Calendar.getInstance().getTimeInMillis();
        SimpleDateFormat formatterTime = new SimpleDateFormat("HH:mm");
        SimpleDateFormat formatterDate = new SimpleDateFormat("EEEE dd MMMM ");
        String time = formatterTime.format(timeInMillis);
        String date = formatterDate.format(timeInMillis);
        String place = "Aix-en-provence-les-milles-sur-seine-des-bouches-du-rhone";

        remoteViews.setTextViewText(R.id.appwidgetTimeTv, time);
        remoteViews.setTextViewText(R.id.appwidgetDateTv, date);
        remoteViews.setTextViewText(R.id.appwidgetPlaceTv, place);

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

//    @Override
//    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
//        Bundle options = appWidgetManager.getAppWidgetOptions(appWidgetId);
//        int minWidth = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH);
//        int minHeight = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT);
//        appWidgetManager.updateAppWidget(appWidgetId, getRemoteViews(context, minWidth, minHeight));
//
//        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
//    }
//
//    private RemoteViews getRemoteViews(Context context, int minWidth, int minHeight) {
//
//        int rows = getCellsForSize(minHeight);
//        int columns = getCellsForSize(minWidth);
//
//        if(columns == 4){
//            return new RemoteViews(context.getPackageName(), R.layout.clock_app_widget_4_cells);
//        } else if(columns == 2){
//            return new RemoteViews(context.getPackageName(), R.layout.clock_app_widget_2_cells);
//        } else {
//            return new RemoteViews(context.getPackageName(), R.layout.clock_app_widget_4_cells);
//        }
//    }
//
//    private int getCellsForSize(int size) {
//        return (int)(Math.ceil(size + 30d)/70d);
//    }

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


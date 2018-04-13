package com.silho.ideo.clockwidget.widget;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import static com.silho.ideo.clockwidget.widget.ClockUpdateService.updateAppWidget;

/**
 * Created by Samuel on 13/04/2018.
 */

public class ClockAppWidgetReceiver extends BroadcastReceiver {

    public static String CLICKED = "com.silho.ideo.action.clicked";
    private static boolean isClicked = false;

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction() != null && intent.getAction().equals(CLICKED)){
            if(isClicked)
                isClicked = false;
            else isClicked = true;
            for(int id : AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, ClockAppWidget.class))){
                updateAppWidget(context, AppWidgetManager.getInstance(context), id);
            }
        }
    }
}

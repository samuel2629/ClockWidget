package com.silho.ideo.clockwidget.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.silho.ideo.clockwidget.R;
import com.silho.ideo.clockwidget.model.ListDays;
import com.silho.ideo.clockwidget.model.ListHours;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Samuel on 11/04/2018.
 */

public class WidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private int appWidgetId;
    private ArrayList<ListHours> listItem;

    public WidgetRemoteViewsFactory(Context context, Intent intent){
        mContext = context;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        populateListItem();
    }

    private void populateListItem() {
        if(RemoteFetchService.mListDays != null)
            listItem = (ArrayList<ListHours>) RemoteFetchService.mListDays.clone();
    }


    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return listItem.size();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.daily_list_item);
        remoteViews.setTextViewText(R.id.dayNameLabel, listItem.get(i).getDtTxt());
        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}

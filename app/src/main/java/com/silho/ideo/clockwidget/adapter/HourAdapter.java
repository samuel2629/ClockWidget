package com.silho.ideo.clockwidget.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.silho.ideo.clockwidget.R;

import java.util.ArrayList;

/**
 * Created by Samuel on 29/03/2018.
 */

public class HourAdapter extends RecyclerView.Adapter<HourAdapter.ViewHolder> {

    private final ArrayList<Object> mHours;
    private Context mContext;

    public HourAdapter(Context context, ArrayList<Object> hours){
        mContext = context;
        mHours = hours;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hourly_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindHour(mHours.get(position));
    }

    @Override
    public int getItemCount() {
        return mHours.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }

        public void bindHour(Object o) {

        }
    }
}

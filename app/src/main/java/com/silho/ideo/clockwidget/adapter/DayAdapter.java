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

public class DayAdapter extends RecyclerView.Adapter<DayAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<Object> mDays;

    public DayAdapter(Context context, ArrayList<Object> days){
        mContext = context;
        mDays = days;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.daily_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindDay(mDays.get(position));
    }

    @Override
    public int getItemCount() {
        return mDays.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }

        public void bindDay(Object o) {

        }
    }
}

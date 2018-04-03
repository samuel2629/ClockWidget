package com.silho.ideo.clockwidget.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.silho.ideo.clockwidget.R;
import com.silho.ideo.clockwidget.model.Datum__;

import java.util.ArrayList;

/**
 * Created by Samuel on 29/03/2018.
 */

public class DayAdapter extends RecyclerView.Adapter<DayAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<Datum__> mDays;

    public DayAdapter(Context context, ArrayList<Datum__> days){
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

        TextView tempTv;
        TextView dayTv;
        ImageView iconIv;

        public ViewHolder(View itemView) {
            super(itemView);

            tempTv = itemView.findViewById(R.id.temperatureLabel);
            dayTv = itemView.findViewById(R.id.dayNameLabel);
            iconIv = itemView.findViewById(R.id.iconIv);
        }

        public void bindDay(Datum__ daily) {
            tempTv.setText(String.format("%s°", String.valueOf(Math.round(daily.getTemperatureMin()))
                    + "/" + String.valueOf(Math.round(daily.getTemperatureMax()))));
            dayTv.setText(daily.getDayOfTheWeek());
            iconIv.setImageResource(daily.getIconId(daily.getIcon()));
        }
    }
}

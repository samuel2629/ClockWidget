package com.silho.ideo.clockwidget.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.silho.ideo.clockwidget.R;
import com.silho.ideo.clockwidget.model.openweathermap.ListDays;

import java.util.List;

/**
 * Created by Samuel on 29/03/2018.
 */

public class DayAdapter extends RecyclerView.Adapter<DayAdapter.ViewHolder> {

    private final boolean mIsCelsius;
    private Context mContext;
    private List<ListDays> mDays;

    public DayAdapter(Context context, List<ListDays> days, boolean isCelsius){
        mContext = context;
        mDays =  days.subList(0, 10);
        mIsCelsius = isCelsius;
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

        public void bindDay(ListDays daily) {
            if(mIsCelsius)
                tempTv.setText(String.format("%s°", String.valueOf(Math.round(daily.getTemp().getMin()))
                        + "/" + String.valueOf(Math.round(daily.getTemp().getMax()))));
            else
                tempTv.setText(String.format("%s°", String.valueOf(Math.round(daily.getTemp().getMin()))
                    + "/" + String.valueOf(Math.round(daily.getTemp().getMax()))));
            dayTv.setText(daily.getDayOfTheWeek());
            for(int i = 0; i < daily.getWeather().size(); i++) {
                iconIv.setImageResource(daily.getWeather().get(i).getIconId(daily.getWeather().get(i).getIcon()));
            }
        }
    }
}

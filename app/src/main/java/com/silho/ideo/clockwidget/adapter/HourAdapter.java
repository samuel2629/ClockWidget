package com.silho.ideo.clockwidget.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.silho.ideo.clockwidget.R;
import com.silho.ideo.clockwidget.model.openweathermap.ListHours;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Samuel on 29/03/2018.
 */

public class HourAdapter extends RecyclerView.Adapter<HourAdapter.ViewHolder> {

    private final List<ListHours> mHours;
    private final boolean mIsCelsius;
    private Context mContext;

    public HourAdapter(Context context, List<ListHours> hours, boolean isCelsius){
        mContext = context;
        mHours = hours.subList(0,9);
        mIsCelsius = isCelsius;
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

        TextView timeTv;
        TextView tempTv;
        TextView sumTv;
        ImageView iconIv;

        public ViewHolder(View itemView) {
            super(itemView);

            timeTv = itemView.findViewById(R.id.timeLabel);
            tempTv = itemView.findViewById(R.id.temperatureLabel);
            sumTv = itemView.findViewById(R.id.summaryLabel);
            iconIv = itemView.findViewById(R.id.iconIv);

        }

        public void bindHour(ListHours hourly) {
            BigInteger bigInteger = new BigInteger(String.valueOf(hourly.getDt()));
            bigInteger = bigInteger.multiply(new BigInteger("1000"));
            SimpleDateFormat formatterTime = new SimpleDateFormat("HH:mm");
            String date = formatterTime.format(bigInteger);
            timeTv.setText(date);
            if(mIsCelsius)
                tempTv.setText(String.format("%s°", String.valueOf(Math.round(hourly.getMain().getTemp()))));
            else
                tempTv.setText(String.format("%s°", String.valueOf(Math.round(hourly.getMain().getTemp()))));
            sumTv.setVisibility(View.GONE);
            for(int i = 0; i < hourly.getWeather().size(); i++) {
                iconIv.setImageResource(hourly.getWeather().get(i).getIconId(hourly.getWeather().get(i).getIcon()));
            }
        }
    }
}

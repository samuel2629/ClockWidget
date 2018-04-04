package com.silho.ideo.clockwidget.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.silho.ideo.clockwidget.R;
import com.silho.ideo.clockwidget.model.Datum_;
import com.silho.ideo.clockwidget.model.Hourly;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Samuel on 29/03/2018.
 */

public class HourAdapter extends RecyclerView.Adapter<HourAdapter.ViewHolder> {

    private final ArrayList<Datum_> mHours;
    private final boolean mIsCelsius;
    private Context mContext;

    public HourAdapter(Context context, ArrayList<Datum_> hours, boolean isCelsius){
        mContext = context;
        mHours = hours;
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

        public void bindHour(Datum_ hourly) {
            BigInteger bigInteger = new BigInteger(String.valueOf(hourly.getTime()));
            bigInteger = bigInteger.multiply(new BigInteger("1000"));
            SimpleDateFormat formatterTime = new SimpleDateFormat("HH:mm");
            String date = formatterTime.format(bigInteger);
            timeTv.setText(date);
            if(mIsCelsius)
                tempTv.setText(String.format("%s°", String.valueOf(hourly.getTemperatureCelsius())));
            else
                tempTv.setText(String.format("%s°", String.valueOf(Math.round(hourly.getTemperature()))));
            sumTv.setVisibility(View.GONE);
            iconIv.setImageResource(hourly.getIconId(hourly.getIcon()));
        }
    }
}

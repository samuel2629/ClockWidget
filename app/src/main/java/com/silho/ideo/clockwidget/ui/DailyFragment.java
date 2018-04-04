package com.silho.ideo.clockwidget.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.silho.ideo.clockwidget.R;
import com.silho.ideo.clockwidget.adapter.DayAdapter;
import com.silho.ideo.clockwidget.model.Datum__;

import java.util.ArrayList;

/**
 * Created by Samuel on 29/03/2018.
 */

public class DailyFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.daily_forecast_fragment, container, false);
        ArrayList<Datum__> days = getArguments().getParcelableArrayList(getString(R.string.days_arraylist));
        boolean isCelsius = getArguments().getBoolean(getString(R.string.on_celsius));
        DayAdapter dayAdapter = new DayAdapter(getContext(), days, isCelsius);
        RecyclerView recyclerView = view.findViewById(R.id.dailyRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(dayAdapter);
        recyclerView.hasFixedSize();
        return view;
    }
}

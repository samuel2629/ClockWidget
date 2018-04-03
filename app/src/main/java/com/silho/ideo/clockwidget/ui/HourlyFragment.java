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
import com.silho.ideo.clockwidget.adapter.HourAdapter;
import com.silho.ideo.clockwidget.model.Datum_;

import java.util.ArrayList;

/**
 * Created by Samuel on 29/03/2018.
 */

public class HourlyFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.hourly_forecast_fragment, container, false);
        ArrayList<Datum_> hours = getArguments().getParcelableArrayList("hours");
        HourAdapter hourAdapter = new HourAdapter(getContext(), hours);
        RecyclerView recyclerView = view.findViewById(R.id.hourlyRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(hourAdapter);
        recyclerView.hasFixedSize();
        return view;
    }
}

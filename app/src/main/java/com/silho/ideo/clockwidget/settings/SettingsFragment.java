package com.silho.ideo.clockwidget.settings;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.util.ArrayMap;


import com.silho.ideo.clockwidget.R;
import com.silho.ideo.clockwidget.utils.TimeZoneProvider;
import com.silho.ideo.clockwidget.widget.ClockAppWidget;

import java.util.Calendar;
import java.util.TimeZone;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Samuel on 30/03/2018.
 */

public class SettingsFragment extends PreferenceFragmentCompat {

    private static ArrayMap<String, TimeZone> mMap;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.pref_visualiser);

        ListPreference listPreference = (ListPreference) findPreference(getString(R.string.gmt_pref));
        setListPreferenceData(listPreference);
        listPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {


                TimeZone t = mMap.get(newValue);
                TimeZone.setDefault(t);
                saveAndUpdateWidget(t.getID());
                return true;
            }
        });

        Preference button = findPreference("button");
        button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                TimeZone.setDefault(Calendar.getInstance().getTimeZone());
                saveAndUpdateWidget(null);
                return true;
            }
        });
    }

    protected static void setListPreferenceData(ListPreference listPreference) {
        String[] tz = TimeZone.getAvailableIDs();
        mMap = new ArrayMap<>();
        TimeZoneProvider timeZoneProvider = new TimeZoneProvider();
        String[] timeZoneArray = new String[tz.length];
        for(int i  = 0; i < tz.length; i++){
            timeZoneArray[i] =  timeZoneProvider.displayTimeZone(TimeZone.getTimeZone(tz[i]));
            mMap.put(timeZoneProvider.displayTimeZone(TimeZone.getTimeZone(tz[i])), TimeZone.getTimeZone(tz[i]));
        }

        listPreference.setEntries(timeZoneArray);
        listPreference.setEntryValues(timeZoneArray);
    }


    private void saveAndUpdateWidget(String timezoneId) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getActivity().getPackageName(), MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getString(R.string.gmt), TimeZone.getDefault().getDisplayName()).apply();
        Intent intent = new Intent(getContext(), ClockAppWidget.class);
        intent.putExtra(getString(R.string.foreign_place_key), timezoneId);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int[] ids = AppWidgetManager.getInstance(getActivity().getApplication()).getAppWidgetIds(new ComponentName(getActivity().getApplication(), ClockAppWidget.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        getActivity().sendBroadcast(intent);
    }
}

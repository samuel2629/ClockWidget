package com.silho.ideo.clockwidget.settings;

import android.os.Bundle;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.util.ArrayMap;
import android.widget.Toast;

import com.silho.ideo.clockwidget.R;
import com.silho.ideo.clockwidget.utils.TimeZoneProvider;

import java.util.TimeZone;

/**
 * Created by Samuel on 30/03/2018.
 */

public class SettingsFragment extends PreferenceFragmentCompat {

    private static ArrayMap<String, TimeZone> mMap;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.pref_visualiser);

        final String defaultTimeZone = TimeZone.getDefault().getID().toLowerCase();

        ListPreference listPreference = (ListPreference) findPreference("gmt-preferences");
        setListPreferenceData(listPreference);
        listPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {

                TimeZone t = mMap.get(newValue);
                TimeZone.setDefault(t);
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
        listPreference.setDefaultValue("1");

    }
}

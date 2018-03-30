package com.silho.ideo.clockwidget.utils;

import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * Created by Samuel on 30/03/2018.
 */

public class TimeZoneProvider {

    public String displayTimeZone(TimeZone timeZone){
        long hours = TimeUnit.MILLISECONDS.toHours(timeZone.getRawOffset());
        long minutes = TimeUnit.MILLISECONDS.toMinutes(timeZone.getRawOffset())
                - TimeUnit.HOURS.toMinutes(hours);
        minutes = Math.abs(minutes);
        String result = "";
        if(hours > 0){
            result = String.format("(GMT+%d:%02d) %s", hours, minutes, timeZone.getID());
        } else {
            result = String.format("(GMT%d:%02d) %s", hours, minutes, timeZone.getID());
        }
        return result;
    }
}

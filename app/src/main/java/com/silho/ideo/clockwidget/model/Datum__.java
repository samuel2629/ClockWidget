
package com.silho.ideo.clockwidget.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.silho.ideo.clockwidget.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Datum__ implements Parcelable
{

    @SerializedName("time")
    @Expose
    private Integer time;
    @SerializedName("summary")
    @Expose
    private String summary;
    @SerializedName("icon")
    @Expose
    private String icon;
    @SerializedName("sunriseTime")
    @Expose
    private Integer sunriseTime;
    @SerializedName("sunsetTime")
    @Expose
    private Integer sunsetTime;
    @SerializedName("moonPhase")
    @Expose
    private Double moonPhase;
    @SerializedName("precipIntensity")
    @Expose
    private Double precipIntensity;
    @SerializedName("precipIntensityMax")
    @Expose
    private Double precipIntensityMax;
    @SerializedName("precipIntensityMaxTime")
    @Expose
    private Integer precipIntensityMaxTime;
    @SerializedName("precipProbability")
    @Expose
    private Double precipProbability;
    @SerializedName("precipType")
    @Expose
    private String precipType;
    @SerializedName("temperatureHigh")
    @Expose
    private Double temperatureHigh;
    @SerializedName("temperatureHighTime")
    @Expose
    private Integer temperatureHighTime;
    @SerializedName("temperatureLow")
    @Expose
    private Double temperatureLow;
    @SerializedName("temperatureLowTime")
    @Expose
    private Integer temperatureLowTime;
    @SerializedName("apparentTemperatureHigh")
    @Expose
    private Double apparentTemperatureHigh;
    @SerializedName("apparentTemperatureHighTime")
    @Expose
    private Integer apparentTemperatureHighTime;
    @SerializedName("apparentTemperatureLow")
    @Expose
    private Double apparentTemperatureLow;
    @SerializedName("apparentTemperatureLowTime")
    @Expose
    private Integer apparentTemperatureLowTime;
    @SerializedName("dewPoint")
    @Expose
    private Double dewPoint;
    @SerializedName("humidity")
    @Expose
    private Double humidity;
    @SerializedName("pressure")
    @Expose
    private Double pressure;
    @SerializedName("windSpeed")
    @Expose
    private Double windSpeed;
    @SerializedName("windGust")
    @Expose
    private Double windGust;
    @SerializedName("windGustTime")
    @Expose
    private Integer windGustTime;
    @SerializedName("windBearing")
    @Expose
    private Integer windBearing;
    @SerializedName("cloudCover")
    @Expose
    private Double cloudCover;
    @SerializedName("uvIndex")
    @Expose
    private Integer uvIndex;
    @SerializedName("uvIndexTime")
    @Expose
    private Integer uvIndexTime;
    @SerializedName("visibility")
    @Expose
    private Double visibility;
    @SerializedName("ozone")
    @Expose
    private Double ozone;
    @SerializedName("temperatureMin")
    @Expose
    private Double temperatureMin;
    @SerializedName("temperatureMinTime")
    @Expose
    private Integer temperatureMinTime;
    @SerializedName("temperatureMax")
    @Expose
    private Double temperatureMax;
    @SerializedName("temperatureMaxTime")
    @Expose
    private Integer temperatureMaxTime;
    @SerializedName("apparentTemperatureMin")
    @Expose
    private Double apparentTemperatureMin;
    @SerializedName("apparentTemperatureMinTime")
    @Expose
    private Integer apparentTemperatureMinTime;
    @SerializedName("apparentTemperatureMax")
    @Expose
    private Double apparentTemperatureMax;
    @SerializedName("apparentTemperatureMaxTime")
    @Expose
    private Integer apparentTemperatureMaxTime;
    public final static Parcelable.Creator<Datum__> CREATOR = new Creator<Datum__>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Datum__ createFromParcel(Parcel in) {
            return new Datum__(in);
        }

        public Datum__[] newArray(int size) {
            return (new Datum__[size]);
        }

    }
    ;

    protected Datum__(Parcel in) {
        this.time = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.summary = ((String) in.readValue((String.class.getClassLoader())));
        this.icon = ((String) in.readValue((String.class.getClassLoader())));
        this.sunriseTime = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.sunsetTime = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.moonPhase = ((Double) in.readValue((Double.class.getClassLoader())));
        this.precipIntensity = ((Double) in.readValue((Double.class.getClassLoader())));
        this.precipIntensityMax = ((Double) in.readValue((Double.class.getClassLoader())));
        this.precipIntensityMaxTime = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.precipProbability = ((Double) in.readValue((Double.class.getClassLoader())));
        this.precipType = ((String) in.readValue((String.class.getClassLoader())));
        this.temperatureHigh = ((Double) in.readValue((Double.class.getClassLoader())));
        this.temperatureHighTime = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.temperatureLow = ((Double) in.readValue((Double.class.getClassLoader())));
        this.temperatureLowTime = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.apparentTemperatureHigh = ((Double) in.readValue((Double.class.getClassLoader())));
        this.apparentTemperatureHighTime = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.apparentTemperatureLow = ((Double) in.readValue((Double.class.getClassLoader())));
        this.apparentTemperatureLowTime = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.dewPoint = ((Double) in.readValue((Double.class.getClassLoader())));
        this.humidity = ((Double) in.readValue((Double.class.getClassLoader())));
        this.pressure = ((Double) in.readValue((Double.class.getClassLoader())));
        this.windSpeed = ((Double) in.readValue((Double.class.getClassLoader())));
        this.windGust = ((Double) in.readValue((Double.class.getClassLoader())));
        this.windGustTime = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.windBearing = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.cloudCover = ((Double) in.readValue((Double.class.getClassLoader())));
        this.uvIndex = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.uvIndexTime = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.visibility = ((Double) in.readValue((Double.class.getClassLoader())));
        this.ozone = ((Double) in.readValue((Double.class.getClassLoader())));
        this.temperatureMin = ((Double) in.readValue((Double.class.getClassLoader())));
        this.temperatureMinTime = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.temperatureMax = ((Double) in.readValue((Double.class.getClassLoader())));
        this.temperatureMaxTime = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.apparentTemperatureMin = ((Double) in.readValue((Double.class.getClassLoader())));
        this.apparentTemperatureMinTime = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.apparentTemperatureMax = ((Double) in.readValue((Double.class.getClassLoader())));
        this.apparentTemperatureMaxTime = ((Integer) in.readValue((Integer.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     * 
     */
    public Datum__() {
    }

    /**
     * 
     * @param temperatureMinTime
     * @param sunsetTime
     * @param summary
     * @param precipIntensityMaxTime
     * @param visibility
     * @param temperatureLowTime
     * @param temperatureHighTime
     * @param temperatureLow
     * @param precipIntensity
     * @param precipIntensityMax
     * @param ozone
     * @param time
     * @param apparentTemperatureMaxTime
     * @param uvIndex
     * @param apparentTemperatureHighTime
     * @param temperatureHigh
     * @param icon
     * @param windGust
     * @param apparentTemperatureLowTime
     * @param temperatureMaxTime
     * @param pressure
     * @param cloudCover
     * @param apparentTemperatureMinTime
     * @param temperatureMin
     * @param precipType
     * @param apparentTemperatureLow
     * @param dewPoint
     * @param sunriseTime
     * @param windSpeed
     * @param humidity
     * @param apparentTemperatureMax
     * @param windBearing
     * @param moonPhase
     * @param precipProbability
     * @param windGustTime
     * @param apparentTemperatureMin
     * @param uvIndexTime
     * @param temperatureMax
     * @param apparentTemperatureHigh
     */
    public Datum__(Integer time, String summary, String icon, Integer sunriseTime, Integer sunsetTime, Double moonPhase, Double precipIntensity, Double precipIntensityMax, Integer precipIntensityMaxTime, Double precipProbability, String precipType, Double temperatureHigh, Integer temperatureHighTime, Double temperatureLow, Integer temperatureLowTime, Double apparentTemperatureHigh, Integer apparentTemperatureHighTime, Double apparentTemperatureLow, Integer apparentTemperatureLowTime, Double dewPoint, Double humidity, Double pressure, Double windSpeed, Double windGust, Integer windGustTime, Integer windBearing, Double cloudCover, Integer uvIndex, Integer uvIndexTime, Double visibility, Double ozone, Double temperatureMin, Integer temperatureMinTime, Double temperatureMax, Integer temperatureMaxTime, Double apparentTemperatureMin, Integer apparentTemperatureMinTime, Double apparentTemperatureMax, Integer apparentTemperatureMaxTime) {
        super();
        this.time = time;
        this.summary = summary;
        this.icon = icon;
        this.sunriseTime = sunriseTime;
        this.sunsetTime = sunsetTime;
        this.moonPhase = moonPhase;
        this.precipIntensity = precipIntensity;
        this.precipIntensityMax = precipIntensityMax;
        this.precipIntensityMaxTime = precipIntensityMaxTime;
        this.precipProbability = precipProbability;
        this.precipType = precipType;
        this.temperatureHigh = temperatureHigh;
        this.temperatureHighTime = temperatureHighTime;
        this.temperatureLow = temperatureLow;
        this.temperatureLowTime = temperatureLowTime;
        this.apparentTemperatureHigh = apparentTemperatureHigh;
        this.apparentTemperatureHighTime = apparentTemperatureHighTime;
        this.apparentTemperatureLow = apparentTemperatureLow;
        this.apparentTemperatureLowTime = apparentTemperatureLowTime;
        this.dewPoint = dewPoint;
        this.humidity = humidity;
        this.pressure = pressure;
        this.windSpeed = windSpeed;
        this.windGust = windGust;
        this.windGustTime = windGustTime;
        this.windBearing = windBearing;
        this.cloudCover = cloudCover;
        this.uvIndex = uvIndex;
        this.uvIndexTime = uvIndexTime;
        this.visibility = visibility;
        this.ozone = ozone;
        this.temperatureMin = temperatureMin;
        this.temperatureMinTime = temperatureMinTime;
        this.temperatureMax = temperatureMax;
        this.temperatureMaxTime = temperatureMaxTime;
        this.apparentTemperatureMin = apparentTemperatureMin;
        this.apparentTemperatureMinTime = apparentTemperatureMinTime;
        this.apparentTemperatureMax = apparentTemperatureMax;
        this.apparentTemperatureMaxTime = apparentTemperatureMaxTime;
    }

    public String getDayOfTheWeek(){
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE");
        Date dateTime = new Date(time*1000);
        String date = formatter.format(dateTime);
        return date.substring(0,1).toUpperCase() + date.substring(1);
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getSunriseTime() {
        return sunriseTime;
    }

    public void setSunriseTime(Integer sunriseTime) {
        this.sunriseTime = sunriseTime;
    }

    public Integer getSunsetTime() {
        return sunsetTime;
    }

    public void setSunsetTime(Integer sunsetTime) {
        this.sunsetTime = sunsetTime;
    }

    public Double getMoonPhase() {
        return moonPhase;
    }

    public void setMoonPhase(Double moonPhase) {
        this.moonPhase = moonPhase;
    }

    public Double getPrecipIntensity() {
        return precipIntensity;
    }

    public void setPrecipIntensity(Double precipIntensity) {
        this.precipIntensity = precipIntensity;
    }

    public Double getPrecipIntensityMax() {
        return precipIntensityMax;
    }

    public void setPrecipIntensityMax(Double precipIntensityMax) {
        this.precipIntensityMax = precipIntensityMax;
    }

    public Integer getPrecipIntensityMaxTime() {
        return precipIntensityMaxTime;
    }

    public void setPrecipIntensityMaxTime(Integer precipIntensityMaxTime) {
        this.precipIntensityMaxTime = precipIntensityMaxTime;
    }

    public Double getPrecipProbability() {
        return precipProbability;
    }

    public void setPrecipProbability(Double precipProbability) {
        this.precipProbability = precipProbability;
    }

    public String getPrecipType() {
        return precipType;
    }

    public void setPrecipType(String precipType) {
        this.precipType = precipType;
    }

    public Double getTemperatureHigh() {
        return temperatureHigh;
    }

    public void setTemperatureHigh(Double temperatureHigh) {
        this.temperatureHigh = temperatureHigh;
    }

    public Integer getTemperatureHighTime() {
        return temperatureHighTime;
    }

    public void setTemperatureHighTime(Integer temperatureHighTime) {
        this.temperatureHighTime = temperatureHighTime;
    }

    public Double getTemperatureLow() {
        return temperatureLow;
    }

    public void setTemperatureLow(Double temperatureLow) {
        this.temperatureLow = temperatureLow;
    }

    public Integer getTemperatureLowTime() {
        return temperatureLowTime;
    }

    public void setTemperatureLowTime(Integer temperatureLowTime) {
        this.temperatureLowTime = temperatureLowTime;
    }

    public Double getApparentTemperatureHigh() {
        return apparentTemperatureHigh;
    }

    public void setApparentTemperatureHigh(Double apparentTemperatureHigh) {
        this.apparentTemperatureHigh = apparentTemperatureHigh;
    }

    public Integer getApparentTemperatureHighTime() {
        return apparentTemperatureHighTime;
    }

    public void setApparentTemperatureHighTime(Integer apparentTemperatureHighTime) {
        this.apparentTemperatureHighTime = apparentTemperatureHighTime;
    }

    public Double getApparentTemperatureLow() {
        return apparentTemperatureLow;
    }

    public void setApparentTemperatureLow(Double apparentTemperatureLow) {
        this.apparentTemperatureLow = apparentTemperatureLow;
    }

    public Integer getApparentTemperatureLowTime() {
        return apparentTemperatureLowTime;
    }

    public void setApparentTemperatureLowTime(Integer apparentTemperatureLowTime) {
        this.apparentTemperatureLowTime = apparentTemperatureLowTime;
    }

    public Double getDewPoint() {
        return dewPoint;
    }

    public void setDewPoint(Double dewPoint) {
        this.dewPoint = dewPoint;
    }

    public Double getHumidity() {
        return humidity;
    }

    public void setHumidity(Double humidity) {
        this.humidity = humidity;
    }

    public Double getPressure() {
        return pressure;
    }

    public void setPressure(Double pressure) {
        this.pressure = pressure;
    }

    public Double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(Double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public Double getWindGust() {
        return windGust;
    }

    public void setWindGust(Double windGust) {
        this.windGust = windGust;
    }

    public Integer getWindGustTime() {
        return windGustTime;
    }

    public void setWindGustTime(Integer windGustTime) {
        this.windGustTime = windGustTime;
    }

    public Integer getWindBearing() {
        return windBearing;
    }

    public void setWindBearing(Integer windBearing) {
        this.windBearing = windBearing;
    }

    public Double getCloudCover() {
        return cloudCover;
    }

    public void setCloudCover(Double cloudCover) {
        this.cloudCover = cloudCover;
    }

    public Integer getUvIndex() {
        return uvIndex;
    }

    public void setUvIndex(Integer uvIndex) {
        this.uvIndex = uvIndex;
    }

    public Integer getUvIndexTime() {
        return uvIndexTime;
    }

    public void setUvIndexTime(Integer uvIndexTime) {
        this.uvIndexTime = uvIndexTime;
    }

    public Double getVisibility() {
        return visibility;
    }

    public void setVisibility(Double visibility) {
        this.visibility = visibility;
    }

    public Double getOzone() {
        return ozone;
    }

    public void setOzone(Double ozone) {
        this.ozone = ozone;
    }

    public Double getTemperatureMin() {
        return temperatureMin;
    }

    public void setTemperatureMin(Double temperatureMin) {
        this.temperatureMin = temperatureMin;
    }

    public Integer getTemperatureMinTime() {
        return temperatureMinTime;
    }

    public void setTemperatureMinTime(Integer temperatureMinTime) {
        this.temperatureMinTime = temperatureMinTime;
    }

    public Double getTemperatureMax() {
        return temperatureMax;
    }

    public void setTemperatureMax(Double temperatureMax) {
        this.temperatureMax = temperatureMax;
    }

    public Integer getTemperatureMaxTime() {
        return temperatureMaxTime;
    }

    public void setTemperatureMaxTime(Integer temperatureMaxTime) {
        this.temperatureMaxTime = temperatureMaxTime;
    }

    public Double getApparentTemperatureMin() {
        return apparentTemperatureMin;
    }

    public void setApparentTemperatureMin(Double apparentTemperatureMin) {
        this.apparentTemperatureMin = apparentTemperatureMin;
    }

    public Integer getApparentTemperatureMinTime() {
        return apparentTemperatureMinTime;
    }

    public void setApparentTemperatureMinTime(Integer apparentTemperatureMinTime) {
        this.apparentTemperatureMinTime = apparentTemperatureMinTime;
    }

    public Double getApparentTemperatureMax() {
        return apparentTemperatureMax;
    }

    public void setApparentTemperatureMax(Double apparentTemperatureMax) {
        this.apparentTemperatureMax = apparentTemperatureMax;
    }

    public Integer getApparentTemperatureMaxTime() {
        return apparentTemperatureMaxTime;
    }

    public void setApparentTemperatureMaxTime(Integer apparentTemperatureMaxTime) {
        this.apparentTemperatureMaxTime = apparentTemperatureMaxTime;
    }
    public int getIconId(String iconString){
        // clear-day, clear-night, rain, snow, sleet, wind, fog, cloudy, partly-cloudy-day, or partly-cloudy-night.
        int iconId = R.mipmap.ic_weather_sunny_white_36dp;

        if (iconString.equals("clear-day")) {
            iconId = R.mipmap.ic_weather_sunny_white_36dp;
        }
        else if (iconString.equals("clear-night")) {
            iconId = R.mipmap.ic_weather_night_white_36dp;
        }
        else if (iconString.equals("rain")) {
            iconId = R.mipmap.ic_weather_rainy_white_36dp;
        }
        else if (iconString.equals("snow")) {
            iconId = R.mipmap.ic_weather_snowy_white_36dp;
        }
        else if (iconString.equals("sleet")) {
            iconId = R.mipmap.ic_weather_snowy_rainy_white_36dp;
        }
        else if (iconString.equals("wind")) {
            iconId = R.mipmap.ic_weather_windy_variant_white_36dp;
        }
        else if (iconString.equals("fog")) {
            iconId = R.mipmap.ic_weather_fog_white_36dp;
        }
        else if (iconString.equals("cloudy")) {
            iconId = R.mipmap.ic_weather_cloudy_white_36dp;
        }
        else if (iconString.equals("partly-cloudy-day")) {
            iconId = R.mipmap.ic_weather_partlycloudy_white_36dp;
        }
        else if (iconString.equals("partly-cloudy-night")) {
            iconId = R.mipmap.ic_weather_cloudy_white_36dp;
        }

        return iconId;
    }


    public int getMinTempCelsius(){
        double temp = (getTemperatureMin() * 5)/41;
        return (int) Math.round(temp);
    }


    public int getMaxTempCelsius(){
        double temp = (getTemperatureMax() * 5)/41;
        return (int) Math.round(temp);
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(time);
        dest.writeValue(summary);
        dest.writeValue(icon);
        dest.writeValue(sunriseTime);
        dest.writeValue(sunsetTime);
        dest.writeValue(moonPhase);
        dest.writeValue(precipIntensity);
        dest.writeValue(precipIntensityMax);
        dest.writeValue(precipIntensityMaxTime);
        dest.writeValue(precipProbability);
        dest.writeValue(precipType);
        dest.writeValue(temperatureHigh);
        dest.writeValue(temperatureHighTime);
        dest.writeValue(temperatureLow);
        dest.writeValue(temperatureLowTime);
        dest.writeValue(apparentTemperatureHigh);
        dest.writeValue(apparentTemperatureHighTime);
        dest.writeValue(apparentTemperatureLow);
        dest.writeValue(apparentTemperatureLowTime);
        dest.writeValue(dewPoint);
        dest.writeValue(humidity);
        dest.writeValue(pressure);
        dest.writeValue(windSpeed);
        dest.writeValue(windGust);
        dest.writeValue(windGustTime);
        dest.writeValue(windBearing);
        dest.writeValue(cloudCover);
        dest.writeValue(uvIndex);
        dest.writeValue(uvIndexTime);
        dest.writeValue(visibility);
        dest.writeValue(ozone);
        dest.writeValue(temperatureMin);
        dest.writeValue(temperatureMinTime);
        dest.writeValue(temperatureMax);
        dest.writeValue(temperatureMaxTime);
        dest.writeValue(apparentTemperatureMin);
        dest.writeValue(apparentTemperatureMinTime);
        dest.writeValue(apparentTemperatureMax);
        dest.writeValue(apparentTemperatureMaxTime);
    }

    public int describeContents() {
        return  0;
    }

}

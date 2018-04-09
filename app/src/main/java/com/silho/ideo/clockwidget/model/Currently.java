
package com.silho.ideo.clockwidget.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.silho.ideo.clockwidget.R;

public class Currently implements Parcelable
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
    @SerializedName("nearestStormDistance")
    @Expose
    private Integer nearestStormDistance;
    @SerializedName("nearestStormBearing")
    @Expose
    private Integer nearestStormBearing;
    @SerializedName("precipIntensity")
    @Expose
    private Double precipIntensity;
    @SerializedName("precipProbability")
    @Expose
    private Double precipProbability;
    @SerializedName("temperature")
    @Expose
    private Double temperature;
    @SerializedName("apparentTemperature")
    @Expose
    private Double apparentTemperature;
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
    @SerializedName("windBearing")
    @Expose
    private Integer windBearing;
    @SerializedName("cloudCover")
    @Expose
    private Double cloudCover;
    @SerializedName("uvIndex")
    @Expose
    private Integer uvIndex;
    @SerializedName("visibility")
    @Expose
    private Double visibility;
    @SerializedName("ozone")
    @Expose
    private Double ozone;
    public final static Parcelable.Creator<Currently> CREATOR = new Creator<Currently>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Currently createFromParcel(Parcel in) {
            return new Currently(in);
        }

        public Currently[] newArray(int size) {
            return (new Currently[size]);
        }

    }
    ;

    protected Currently(Parcel in) {
        this.time = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.summary = ((String) in.readValue((String.class.getClassLoader())));
        this.icon = ((String) in.readValue((String.class.getClassLoader())));
        this.nearestStormDistance = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.nearestStormBearing = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.precipIntensity = ((Double) in.readValue((Integer.class.getClassLoader())));
        this.precipProbability = ((Double) in.readValue((Integer.class.getClassLoader())));
        this.temperature = ((Double) in.readValue((Double.class.getClassLoader())));
        this.apparentTemperature = ((Double) in.readValue((Double.class.getClassLoader())));
        this.dewPoint = ((Double) in.readValue((Double.class.getClassLoader())));
        this.humidity = ((Double) in.readValue((Double.class.getClassLoader())));
        this.pressure = ((Double) in.readValue((Double.class.getClassLoader())));
        this.windSpeed = ((Double) in.readValue((Double.class.getClassLoader())));
        this.windGust = ((Double) in.readValue((Double.class.getClassLoader())));
        this.windBearing = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.cloudCover = ((Double) in.readValue((Integer.class.getClassLoader())));
        this.uvIndex = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.visibility = ((Double) in.readValue((Double.class.getClassLoader())));
        this.ozone = ((Double) in.readValue((Double.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     * 
     */
    public Currently() {
    }

    /**
     * 
     * @param summary
     * @param windGust
     * @param icon
     * @param nearestStormBearing
     * @param pressure
     * @param visibility
     * @param cloudCover
     * @param apparentTemperature
     * @param precipIntensity
     * @param temperature
     * @param dewPoint
     * @param ozone
     * @param windSpeed
     * @param time
     * @param humidity
     * @param windBearing
     * @param nearestStormDistance
     * @param uvIndex
     * @param precipProbability
     */
    public Currently(Integer time, String summary, String icon, Integer nearestStormDistance, Integer nearestStormBearing, Double precipIntensity, Double precipProbability, Double temperature, Double apparentTemperature, Double dewPoint, Double humidity, Double pressure, Double windSpeed, Double windGust, Integer windBearing, Double cloudCover, Integer uvIndex, Double visibility, Double ozone) {
        super();
        this.time = time;
        this.summary = summary;
        this.icon = icon;
        this.nearestStormDistance = nearestStormDistance;
        this.nearestStormBearing = nearestStormBearing;
        this.precipIntensity = precipIntensity;
        this.precipProbability = precipProbability;
        this.temperature = temperature;
        this.apparentTemperature = apparentTemperature;
        this.dewPoint = dewPoint;
        this.humidity = humidity;
        this.pressure = pressure;
        this.windSpeed = windSpeed;
        this.windGust = windGust;
        this.windBearing = windBearing;
        this.cloudCover = cloudCover;
        this.uvIndex = uvIndex;
        this.visibility = visibility;
        this.ozone = ozone;
    }

    public int getIconId(String iconString){
        // clear-day, clear-night, rain, snow, sleet, wind, fog, cloudy, partly-cloudy-day, or partly-cloudy-night.
        int iconId = R.mipmap.ic_weather_sunny_white_48dp;

        if (iconString.equals("clear-day")) {
            iconId = R.drawable.icon_3;
        }
        else if (iconString.equals("clear-night")) {
            iconId = R.drawable.icon_1;
        }
        else if (iconString.equals("rain")) {
            iconId = R.drawable.icon_5;
        }
        else if (iconString.equals("snow")) {
            iconId = R.mipmap.ic_weather_snowy_white_48dp;
        }
        else if (iconString.equals("sleet")) {
            iconId = R.mipmap.ic_weather_snowy_rainy_white_48dp;
        }
        else if (iconString.equals("wind")) {
            iconId = R.mipmap.ic_weather_windy_variant_white_48dp;
        }
        else if (iconString.equals("fog")) {
            iconId = R.mipmap.ic_weather_fog_white_48dp;
        }
        else if (iconString.equals("cloudy")) {
            iconId = R.drawable.icon_4;
        }
        else if (iconString.equals("partly-cloudy-day")) {
            iconId = R.drawable.icon_6;
        }
        else if (iconString.equals("partly-cloudy-night")) {
            iconId = R.mipmap.ic_weather_cloudy_white_48dp;
        }

        return iconId;
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

    public Integer getNearestStormDistance() {
        return nearestStormDistance;
    }

    public void setNearestStormDistance(Integer nearestStormDistance) {
        this.nearestStormDistance = nearestStormDistance;
    }

    public Integer getNearestStormBearing() {
        return nearestStormBearing;
    }

    public void setNearestStormBearing(Integer nearestStormBearing) {
        this.nearestStormBearing = nearestStormBearing;
    }

    public Double getPrecipIntensity() {
        return precipIntensity;
    }

    public void setPrecipIntensity(Double precipIntensity) {
        this.precipIntensity = precipIntensity;
    }

    public Double getPrecipProbability() {
        return precipProbability;
    }

    public void setPrecipProbability(Double precipProbability) {
        this.precipProbability = precipProbability;
    }

    public String getTemperature() {
        return String.format("%s°", String.valueOf(Math.round(temperature)));
    }

    public int getTemperatureCelsius(){
        double temp = (temperature - 32)/1.8;
        return (int) Math.round(temp);
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Double getApparentTemperature() {
        return apparentTemperature;
    }

    public void setApparentTemperature(Double apparentTemperature) {
        this.apparentTemperature = apparentTemperature;
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

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(time);
        dest.writeValue(summary);
        dest.writeValue(icon);
        dest.writeValue(nearestStormDistance);
        dest.writeValue(nearestStormBearing);
        dest.writeValue(precipIntensity);
        dest.writeValue(precipProbability);
        dest.writeValue(temperature);
        dest.writeValue(apparentTemperature);
        dest.writeValue(dewPoint);
        dest.writeValue(humidity);
        dest.writeValue(pressure);
        dest.writeValue(windSpeed);
        dest.writeValue(windGust);
        dest.writeValue(windBearing);
        dest.writeValue(cloudCover);
        dest.writeValue(uvIndex);
        dest.writeValue(visibility);
        dest.writeValue(ozone);
    }

    public int describeContents() {
        return  0;
    }

}

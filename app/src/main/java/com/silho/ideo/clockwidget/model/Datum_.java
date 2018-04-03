
package com.silho.ideo.clockwidget.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.silho.ideo.clockwidget.R;

public class Datum_ implements Parcelable
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
    @SerializedName("precipType")
    @Expose
    private String precipType;
    public final static Parcelable.Creator<Datum_> CREATOR = new Creator<Datum_>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Datum_ createFromParcel(Parcel in) {
            return new Datum_(in);
        }

        public Datum_[] newArray(int size) {
            return (new Datum_[size]);
        }

    }
    ;

    protected Datum_(Parcel in) {
        this.time = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.summary = ((String) in.readValue((String.class.getClassLoader())));
        this.icon = ((String) in.readValue((String.class.getClassLoader())));
        this.precipIntensity = ((Double) in.readValue((Double.class.getClassLoader())));
        this.precipProbability = ((Double) in.readValue((Double.class.getClassLoader())));
        this.temperature = ((Double) in.readValue((Double.class.getClassLoader())));
        this.apparentTemperature = ((Double) in.readValue((Double.class.getClassLoader())));
        this.dewPoint = ((Double) in.readValue((Double.class.getClassLoader())));
        this.humidity = ((Double) in.readValue((Double.class.getClassLoader())));
        this.pressure = ((Double) in.readValue((Double.class.getClassLoader())));
        this.windSpeed = ((Double) in.readValue((Double.class.getClassLoader())));
        this.windGust = ((Double) in.readValue((Double.class.getClassLoader())));
        this.windBearing = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.cloudCover = ((Double) in.readValue((Double.class.getClassLoader())));
        this.uvIndex = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.visibility = ((Double) in.readValue((Integer.class.getClassLoader())));
        this.ozone = ((Double) in.readValue((Double.class.getClassLoader())));
        this.precipType = ((String) in.readValue((String.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     * 
     */
    public Datum_() {
    }

    /**
     * 
     * @param summary
     * @param icon
     * @param windGust
     * @param pressure
     * @param visibility
     * @param cloudCover
     * @param apparentTemperature
     * @param precipType
     * @param precipIntensity
     * @param temperature
     * @param dewPoint
     * @param ozone
     * @param time
     * @param windSpeed
     * @param humidity
     * @param windBearing
     * @param uvIndex
     * @param precipProbability
     */
    public Datum_(Integer time, String summary, String icon, Double precipIntensity, Double precipProbability, Double temperature, Double apparentTemperature, Double dewPoint, Double humidity, Double pressure, Double windSpeed, Double windGust, Integer windBearing, Double cloudCover, Integer uvIndex, Double visibility, Double ozone, String precipType) {
        super();
        this.time = time;
        this.summary = summary;
        this.icon = icon;
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
        this.precipType = precipType;
    }


    public int getIconId(String iconString){
        // clear-day, clear-night, rain, snow, sleet, wind, fog, cloudy, partly-cloudy-day, or partly-cloudy-night.
        int iconId = R.mipmap.ic_weather_sunny_white_24dp;

        if (iconString.equals("clear-day")) {
            iconId = R.mipmap.ic_weather_sunny_white_24dp;
        }
        else if (iconString.equals("clear-night")) {
            iconId = R.mipmap.ic_weather_night_white_24dp;
        }
        else if (iconString.equals("rain")) {
            iconId = R.mipmap.ic_weather_rainy_white_24dp;
        }
        else if (iconString.equals("snow")) {
            iconId = R.mipmap.ic_weather_snowy_white_24dp;
        }
        else if (iconString.equals("sleet")) {
            iconId = R.mipmap.ic_weather_snowy_rainy_white_24dp;
        }
        else if (iconString.equals("wind")) {
            iconId = R.mipmap.ic_weather_windy_variant_white_24dp;
        }
        else if (iconString.equals("fog")) {
            iconId = R.mipmap.ic_weather_fog_white_24dp;
        }
        else if (iconString.equals("cloudy")) {
            iconId = R.mipmap.ic_weather_cloudy_white_24dp;
        }
        else if (iconString.equals("partly-cloudy-day")) {
            iconId = R.mipmap.ic_weather_partlycloudy_white_24dp;
        }
        else if (iconString.equals("partly-cloudy-night")) {
            iconId = R.mipmap.ic_weather_cloudy_white_24dp;
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

    public Double getTemperature() {
        return temperature;
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

    public String getPrecipType() {
        return precipType;
    }

    public void setPrecipType(String precipType) {
        this.precipType = precipType;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(time);
        dest.writeValue(summary);
        dest.writeValue(icon);
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
        dest.writeValue(precipType);
    }

    public int describeContents() {
        return  0;
    }

}

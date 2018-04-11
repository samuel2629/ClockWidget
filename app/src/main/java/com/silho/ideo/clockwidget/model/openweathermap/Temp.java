
package com.silho.ideo.clockwidget.model.openweathermap;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Temp implements Parcelable
{

    @SerializedName("day")
    @Expose
    private Double day;
    @SerializedName("min")
    @Expose
    private Double min;
    @SerializedName("max")
    @Expose
    private Double max;
    @SerializedName("night")
    @Expose
    private Double night;
    @SerializedName("eve")
    @Expose
    private Double eve;
    @SerializedName("morn")
    @Expose
    private Double morn;
    public final static Parcelable.Creator<Temp> CREATOR = new Creator<Temp>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Temp createFromParcel(Parcel in) {
            return new Temp(in);
        }

        public Temp[] newArray(int size) {
            return (new Temp[size]);
        }

    }
    ;

    protected Temp(Parcel in) {
        this.day = ((Double) in.readValue((Double.class.getClassLoader())));
        this.min = ((Double) in.readValue((Double.class.getClassLoader())));
        this.max = ((Double) in.readValue((Double.class.getClassLoader())));
        this.night = ((Double) in.readValue((Double.class.getClassLoader())));
        this.eve = ((Double) in.readValue((Double.class.getClassLoader())));
        this.morn = ((Double) in.readValue((Double.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     * 
     */
    public Temp() {
    }

    /**
     * 
     * @param min
     * @param eve
     * @param max
     * @param morn
     * @param night
     * @param day
     */
    public Temp(Double day, Double min, Double max, Double night, Double eve, Double morn) {
        super();
        this.day = day;
        this.min = min;
        this.max = max;
        this.night = night;
        this.eve = eve;
        this.morn = morn;
    }

    public Double getDay() {
        return day;
    }

    public void setDay(Double day) {
        this.day = day;
    }

    public Double getMin() {
        return min;
    }

    public void setMin(Double min) {
        this.min = min;
    }

    public Double getMax() {
        return max;
    }

    public void setMax(Double max) {
        this.max = max;
    }

    public Double getNight() {
        return night;
    }

    public void setNight(Double night) {
        this.night = night;
    }

    public Double getEve() {
        return eve;
    }

    public void setEve(Double eve) {
        this.eve = eve;
    }

    public Double getMorn() {
        return morn;
    }

    public void setMorn(Double morn) {
        this.morn = morn;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(day);
        dest.writeValue(min);
        dest.writeValue(max);
        dest.writeValue(night);
        dest.writeValue(eve);
        dest.writeValue(morn);
    }

    public int describeContents() {
        return  0;
    }

}

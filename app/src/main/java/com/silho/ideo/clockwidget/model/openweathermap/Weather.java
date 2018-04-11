
package com.silho.ideo.clockwidget.model.openweathermap;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.silho.ideo.clockwidget.R;

public class Weather implements Parcelable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("main")
    @Expose
    private String main;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("icon")
    @Expose
    private String icon;
    public final static Parcelable.Creator<Weather> CREATOR = new Creator<Weather>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Weather createFromParcel(Parcel in) {
            return new Weather(in);
        }

        public Weather[] newArray(int size) {
            return (new Weather[size]);
        }

    }
    ;

    protected Weather(Parcel in) {
        this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.main = ((String) in.readValue((String.class.getClassLoader())));
        this.description = ((String) in.readValue((String.class.getClassLoader())));
        this.icon = ((String) in.readValue((String.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     * 
     */
    public Weather() {
    }

    /**
     * 
     * @param id
     * @param icon
     * @param description
     * @param main
     */
    public Weather(Integer id, String main, String description, String icon) {
        super();
        this.id = id;
        this.main = main;
        this.description = description;
        this.icon = icon;
    }

    public Integer getId() {
        return id;
    }

    public int getIconId(String iconString){
        // clear-day, clear-night, rain, snow, sleet, wind, fog, cloudy, partly-cloudy-day, or partly-cloudy-night.
        int iconId = R.drawable.meteo_2;

        if (iconString.equals("01d")) {
            iconId = R.drawable.meteo_2;
        }
        else if(iconString.equals("01n")){
            iconId = R.drawable.meteo_15;
        }
        else if (iconString.equals("09d") || iconString.equals("09n") || iconString.equals("10d") || iconString.equals("10n")) {
            iconId = R.drawable.meteo_6;
        }
        else if (iconString.equals("13d") || iconString.equals("13n")) {
            iconId = R.drawable.meteo_22;
        }
        else if (iconString.equals("03d") || iconString.equals("04d") ) {
            iconId = R.drawable.meteo_5;
        }
        else if (iconString.equals("02d")) {
            iconId = R.drawable.meteo_4;
        }
        else if(iconString.equals("02n") || iconString.equals("03n") || iconString.equals("04n")){
            iconId = R.drawable.meteo_3;
        }
        else if(iconString.equals("11d") || iconString.equals("11n"))
            iconId = R.drawable.meteo_7;

        else if(iconString.equals("50d") || iconString.equals("50n")){
            iconId = R.drawable.meteo_10;
        }

        return iconId;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(main);
        dest.writeValue(description);
        dest.writeValue(icon);
    }

    public int describeContents() {
        return  0;
    }

}

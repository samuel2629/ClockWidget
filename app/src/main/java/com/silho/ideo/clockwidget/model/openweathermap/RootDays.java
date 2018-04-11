
package com.silho.ideo.clockwidget.model.openweathermap;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RootDays implements Parcelable
{

    @SerializedName("cod")
    @Expose
    private String cod;
    @SerializedName("message")
    @Expose
    private Integer message;
    @SerializedName("city")
    @Expose
    private City city;
    @SerializedName("cnt")
    @Expose
    private Integer cnt;
    @SerializedName("list")
    @Expose
    private java.util.List<ListDays> list = null;
    public final static Parcelable.Creator<RootDays> CREATOR = new Creator<RootDays>() {


        @SuppressWarnings({
            "unchecked"
        })
        public RootDays createFromParcel(Parcel in) {
            return new RootDays(in);
        }

        public RootDays[] newArray(int size) {
            return (new RootDays[size]);
        }

    }
    ;

    protected RootDays(Parcel in) {
        this.cod = ((String) in.readValue((String.class.getClassLoader())));
        this.message = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.city = ((City) in.readValue((City.class.getClassLoader())));
        this.cnt = ((Integer) in.readValue((Integer.class.getClassLoader())));
        in.readList(this.list, (ListDays.class.getClassLoader()));
    }

    /**
     * No args constructor for use in serialization
     * 
     */
    public RootDays() {
    }

    /**
     * 
     * @param message
     * @param cnt
     * @param cod
     * @param list
     * @param city
     */
    public RootDays(String cod, Integer message, City city, Integer cnt, java.util.List<ListDays> list) {
        super();
        this.cod = cod;
        this.message = message;
        this.city = city;
        this.cnt = cnt;
        this.list = list;
    }

    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public Integer getMessage() {
        return message;
    }

    public void setMessage(Integer message) {
        this.message = message;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Integer getCnt() {
        return cnt;
    }

    public void setCnt(Integer cnt) {
        this.cnt = cnt;
    }

    public java.util.List<ListDays> getList() {
        return list;
    }

    public void setList(java.util.List<ListDays> list) {
        this.list = list;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(cod);
        dest.writeValue(message);
        dest.writeValue(city);
        dest.writeValue(cnt);
        dest.writeList(list);
    }

    public int describeContents() {
        return  0;
    }

}

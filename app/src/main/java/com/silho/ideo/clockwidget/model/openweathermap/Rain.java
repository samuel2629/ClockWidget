package com.silho.ideo.clockwidget.model.openweathermap;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class Rain implements Parcelable
{

    public final static Parcelable.Creator<Rain> CREATOR = new Creator<Rain>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Rain createFromParcel(Parcel in) {
            return new Rain(in);
        }

        public Rain[] newArray(int size) {
            return (new Rain[size]);
        }

    }
    ;

    protected Rain(Parcel in) {
    }

    public void writeToParcel(Parcel dest, int flags) {
    }

    public int describeContents() {
        return  0;
    }

}

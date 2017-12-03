package com.example.natan.movietralierapp1.Adapter;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by natan on 12/2/2017.
 */

public class MovieTrailer implements Parcelable {
    String Base_url="https://www.youtube.com/watch?v=styIneSvfoA";

    String trailer_key;

    public MovieTrailer(String trailer_key) {
        this.trailer_key = trailer_key;
    }

    public String getTrailer_key() {
        return trailer_key;
    }

    public void setTrailer_key(String trailer_key) {
        this.trailer_key = trailer_key;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.Base_url);
        dest.writeString(this.trailer_key);
    }

    protected MovieTrailer(Parcel in) {
        this.Base_url = in.readString();
        this.trailer_key = in.readString();
    }

    public static final Parcelable.Creator<MovieTrailer> CREATOR = new Parcelable.Creator<MovieTrailer>() {
        @Override
        public MovieTrailer createFromParcel(Parcel source) {
            return new MovieTrailer(source);
        }

        @Override
        public MovieTrailer[] newArray(int size) {
            return new MovieTrailer[size];
        }
    };
}

package com.application.team480.kitty_pokedex;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Comparator;

/**
 * Created by MingKie on 11/11/2017.
 */

public class Result implements Parcelable {
    private String breed;
    private float percentage;

    public Result(String breed, float percentage) {
        this.breed = breed;
        this.percentage = percentage;
    }

    protected Result(Parcel in) {
        breed = in.readString();
        percentage = in.readFloat();
    }

    public static final Creator<Result> CREATOR = new Creator<Result>() {
        @Override
        public Result createFromParcel(Parcel in) {
            return new Result(in);
        }

        @Override
        public Result[] newArray(int size) {
            return new Result[size];
        }
    };

    public String getBreed() {
        return breed;
    }

    public float getPercentage() {
        return percentage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(breed);
        parcel.writeFloat(percentage);
    }
}

package com.zachary.lynch.bakingapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Ingredients implements Parcelable {
    @SerializedName("quantity")
    private double mQuality;
    @SerializedName("measure")
    private String mMeasure;
    @SerializedName("ingredient")
    private String mIngredient;

    public double getQuality() {
        return mQuality;
    }

    public String getMeasure() {
        return mMeasure;
    }

    public String getIngredient() {
        return mIngredient;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeDouble(mQuality);
        dest.writeString(mMeasure);
        dest.writeString(mIngredient);
    }
    private Ingredients(Parcel in){
        mQuality = in.readDouble();
        mMeasure = in.readString();
        mIngredient = in.readString();
    }
    public static final Creator<Ingredients> CREATOR = new Creator<Ingredients>() {
        @Override
        public Ingredients createFromParcel(Parcel in) {
            return new Ingredients(in);
        }

        @Override
        public Ingredients[] newArray(int size) {
            return new Ingredients[size];
        }
    };
}

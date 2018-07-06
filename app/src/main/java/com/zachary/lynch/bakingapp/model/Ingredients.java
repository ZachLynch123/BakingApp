package com.zachary.lynch.bakingapp.model;

import com.google.gson.annotations.SerializedName;

public class Ingredients {
    @SerializedName("quantity")
    private double mQuality;
    @SerializedName("measure")
    private String mMeasure;
    @SerializedName("ingredient")
    private String mIngredient;
}

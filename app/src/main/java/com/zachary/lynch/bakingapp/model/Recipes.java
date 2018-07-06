package com.zachary.lynch.bakingapp.model;

import com.google.gson.annotations.SerializedName;


public class Recipes {
    @SerializedName("id")
    private int recipeId;
    @SerializedName("name")
    private String recipeName;

    public int getRecipeId() {
        return recipeId;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public int getServings() {
        return servings;
    }

    @SerializedName("servings")
    private int servings;
}

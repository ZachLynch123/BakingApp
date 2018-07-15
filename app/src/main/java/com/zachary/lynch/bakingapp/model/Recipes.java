package com.zachary.lynch.bakingapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class Recipes {
    @SerializedName("id")
    private int recipeId;
    @SerializedName("name")
    private String recipeName;
    @SerializedName("ingredients")
    private ArrayList<Ingredients> mIngredientsList;
    @SerializedName("steps")
    private ArrayList<Steps> mStepsList;

    public ArrayList<Ingredients> getIngredientsList() {
        return mIngredientsList;
    }

    public ArrayList<Steps> getStepsList() {
        return mStepsList;
    }

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

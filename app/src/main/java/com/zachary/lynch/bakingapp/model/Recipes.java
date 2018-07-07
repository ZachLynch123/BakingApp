package com.zachary.lynch.bakingapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class Recipes {
    @SerializedName("id")
    private int recipeId;
    @SerializedName("name")
    private String recipeName;
    @SerializedName("ingredients")
    private List<Ingredients> mIngredientsList;
    @SerializedName("steps")
    private List<Steps> mStepsList;

    public List<Ingredients> getIngredientsList() {
        return mIngredientsList;
    }

    public List<Steps> getStepsList() {
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

package com.teamalpha.aichef;

import java.util.ArrayList;

public class RecipeItem {
    private int recipeImg;
    private String recipeText;
    private ArrayList<String> ingredientsList;

    public RecipeItem(int recipeImg, String recipeText, ArrayList<String> ingredientsList){
        this.recipeImg = recipeImg;
        this.recipeText = recipeText;
        this.ingredientsList = ingredientsList;
    }

    public int getRecipeImg() {
        return recipeImg;
    }

    public String getRecipeText() {
        return recipeText;
    }

    public ArrayList<String> getIngredientsList() {
        return ingredientsList;
    }
}

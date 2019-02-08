package api;

import java.util.List;

class Recipe {
    final private String recipeName;
    final private String recipeID;
    final private String recipeImageLink;
    final private int usedIngredientCount;
    final private List<Ingredient> ingredients;

    //-------------GETTERS-----------//
    public String getRecipeName(){
        return recipeName;
    }

    public String getRecipeID(){
        return recipeID;
    }

    public String getRecipeImageLink(){
        return recipeImageLink;
    }

    public int getUsedIngredientCount(){
        return usedIngredientCount;
    }


    public List<Ingredient> getIngredients(){
        return ingredients;
    }


    //-------------SETTERS----------------//
    public Recipe(String name, String id, String image, String count, List<Ingredient> ingredients){
        this.recipeName = name;
        this.recipeID = id;
        this.recipeImageLink = image;
        this.usedIngredientCount = Integer.parseInt(count);
        this.ingredients = ingredients;
    }

}

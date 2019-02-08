package api;


import java.util.List;

//front end need to check whether the list mRecipe is empty: if it is, show prompt for the users to select from the suggested recipes
public class GetSelectedRecipeData {

    private List<Recipe> mSelectedRecipes;

    public void setSelectedRecipes(List<Recipe> selectedRecipesFromUser){
        mSelectedRecipes = selectedRecipesFromUser;
    }

    //info of ingredients of the currently chosen recipe is shown upon clicking that particular recipes
    public void callIngredientsListAPI(Recipe selectedRecipe){
    }

}

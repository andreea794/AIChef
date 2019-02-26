package com.teamalpha.aichef;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import api.GetSelectedRecipeData;
import api.Recipe;

public class RecipesList extends AppCompatActivity {
    ListView recipesListView;
    //List<Recipe> recipesList;

    static List<String> ingredientList = new LinkedList<String>();
    static RecipesListAdapter recipesListAdapter;

    @Override
    protected void onDestroy(){
        recipesListAdapter.executor.shutdownNow();
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final RequestQueue queue = Volley.newRequestQueue(this);
        RecipeRequestFinishedListener listener = new RecipeRequestFinishedListener();
        listener.setQueue(queue);
        queue.addRequestFinishedListener(listener);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes_list);
        Resources res = getResources();
//        ingredientList = new LinkedList<Ingredient>();

        /**
         * attach the current recipe list to its adapter
         */

        recipesListView = (ListView)findViewById(R.id.recipesList);
        recipesListAdapter = new RecipesListAdapter(getApplicationContext(), res);
        recipesListView.setAdapter(recipesListAdapter);

        /**
         * Make an API call to populate the ingredients List if passed new recipes, otherwise
         * display old recipes
         */

        if(getIntent().hasExtra("selected")){
            ArrayList<Recipe> temp = getIntent().getExtras().getParcelableArrayList("selected");
            LinkedList<Recipe> result = new LinkedList<Recipe>();
            for(Recipe recipe: temp){
                System.out.println("Passed in recipe: " + recipe.getRecipeName() + recipe.getRecipeID() + recipe.getRecipeImageLink());
                result.add(recipe);
            }
            RecipesListAdapter.recipesList = result;
            System.out.println("Ingredient list before API call: " + RecipesList.ingredientList);
            GetSelectedRecipeData.callIngredientsListAPI(RecipesListAdapter.recipesList, queue, RecipesList.ingredientList);
        }
        else{
            if (RecipesListAdapter.recipesList.size() == 0) {
                List<Recipe> recipeList = new LinkedList<Recipe>();
                //test data
                recipeList.add(new Recipe("Cinnamon Sugar Fried Apples", "639487", "https://spoonacular.com/recipeImages/Cinnamon-Sugar-Fried-Apples-639487.jpg"));
                recipeList.add(new Recipe("Quick Apple Ginger Pie", "657563", "https://spoonacular.com/recipeImages/Quick-Apple-Ginger-Pie-657563.jpg"));
                RecipesListAdapter.recipesList = recipeList;
                GetSelectedRecipeData.callIngredientsListAPI(recipeList, queue, RecipesList.ingredientList);
            }
        }

        /**
         * Button to move to the shopping list
         */
        Button generateIngredientsBtn  = (Button)findViewById(R.id.spawnIngredientsList);
        generateIngredientsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //start a shopping list activity and pass in the ingredients list to buy
                ArrayList<String> ingredients = new ArrayList<String>();

                for(String ingredient : ingredientList){
                    String name = ingredient;
                    name = (Character.toString(name.charAt(0)).toUpperCase() + name.substring(1));
                    ingredients.add(name);

                }
                Intent shoppingList = new Intent(getApplicationContext(), ShoppingActivity.class);
                System.out.println(ingredients);
                shoppingList.putStringArrayListExtra("Ingredients", ingredients);
                startActivity(shoppingList);
            }
        });


    }

    /**
     * Callback mechanism to refresh the recipe list and shopping list, if the user is currently in
     * the shopping list
     */
    private class RecipeRequestFinishedListener implements RequestQueue.RequestFinishedListener<JsonArrayRequest>{

        RequestQueue queue = null;
        public void setQueue(RequestQueue queue){
            this.queue = queue;
        }
        @Override
        public void onRequestFinished(Request<JsonArrayRequest> request){
            System.out.println("Finished ingredients API");
            System.out.println(RecipesList.ingredientList);
            recipesListAdapter.notifyDataSetChanged();
            if(ShoppingActivity.adapter != null){
                //update the shopping list whenever the API call returns
                ShoppingActivity.adapter.ingredientList = RecipesList.ingredientList;
                ShoppingActivity.adapter.notifyDataSetChanged();
            }

        }
    }

}

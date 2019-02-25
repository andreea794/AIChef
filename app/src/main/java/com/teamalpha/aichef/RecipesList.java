package com.teamalpha.aichef;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.teamalpha.aichef.slideuppanel.IngredientFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import api.GetRecipeList;
import api.GetSelectedRecipeData;
import api.Ingredient;
import api.Recipe;

import static com.teamalpha.aichef.RecipesListAdapter.recipesList;

public class RecipesList extends AppCompatActivity {
    ListView recipesListView;
    //List<Recipe> recipesList;
    static List<Ingredient> ingredientList;
    static RecipesListAdapter recipesListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final RequestQueue queue = Volley.newRequestQueue(this);
        RecipeRequestFinishedListener listener = new RecipeRequestFinishedListener();
        listener.setQueue(queue);
        queue.addRequestFinishedListener(listener);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes_list);
        Resources res = getResources();
        ingredientList = new LinkedList<Ingredient>();

        recipesListView = (ListView)findViewById(R.id.recipesList);
        recipesListAdapter = new RecipesListAdapter(getApplicationContext(), res);
        recipesListView.setAdapter(recipesListAdapter);

        if(getIntent().hasExtra("selected")){
            ArrayList<Recipe> temp = getIntent().getExtras().getParcelableArrayList("selected");
            LinkedList<Recipe> result = new LinkedList<Recipe>();
            for(Recipe recipe: temp){
                System.out.println(recipe.getRecipeName() + recipe.getRecipeID() + recipe.getRecipeImageLink());
                result.add(recipe);
            }
            System.out.println("inside selected");
            RecipesListAdapter.recipesList = result;
            GetSelectedRecipeData.callIngredientsListAPI(RecipesListAdapter.recipesList, queue, ingredientList);
        }
        else{
            if (RecipesListAdapter.recipesList.size() == 0) {
                List<Recipe> recipeList = new LinkedList<Recipe>();
                recipeList.add(new Recipe("Cinnamon Sugar Fried Apples", "639487", "https://spoonacular.com/recipeImages/Cinnamon-Sugar-Fried-Apples-639487.jpg"));
                recipeList.add(new Recipe("Quick Apple Ginger Pie", "657563", "https://spoonacular.com/recipeImages/Quick-Apple-Ginger-Pie-657563.jpg"));
                RecipesListAdapter.recipesList = recipeList;
                System.out.println("im here");
                GetSelectedRecipeData.callIngredientsListAPI(recipeList, queue, ingredientList);
            }
            else{
                System.out.println("inside else of non-selected");
                for(Recipe recipe : RecipesListAdapter.recipesList){
                    System.out.println(recipe.getRecipeName());
                }
            }
        }

        Button generateIngredientsBtn  = (Button)findViewById(R.id.spawnIngredientsList);
        generateIngredientsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //start a shopping list activity and pass in the ingredients list to buy
                ArrayList<String> ingredients = new ArrayList<String>();
                for(Ingredient ingredient : ingredientList){
                    ingredients.add(ingredient.getName());
                }
                Intent shoppingList = new Intent(getApplicationContext(), ShoppingActivity.class);
                shoppingList.putStringArrayListExtra("Ingredients", ingredients);
                startActivity(shoppingList);
            }
        });


    }


    private class RecipeRequestFinishedListener implements RequestQueue.RequestFinishedListener<JsonArrayRequest>{

        RequestQueue queue = null;
        public void setQueue(RequestQueue queue){
            this.queue = queue;
        }
        @Override
        public void onRequestFinished(Request<JsonArrayRequest> request){
            System.out.println("Finished ingredients API");
            recipesListAdapter.notifyDataSetChanged();
            if(ShoppingActivity.adapter != null){
                //update the shopping list whenever the API call returns
                ShoppingActivity.adapter.ingredientList = ingredientList;
                ShoppingActivity.adapter.notifyDataSetChanged();
            }
        }
    }

}

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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import api.GetRecipeList;
import api.GetSelectedRecipeData;
import api.Ingredient;
import api.Recipe;

public class RecipesList extends AppCompatActivity {
    ListView recipesListView;
    //List<Recipe> recipesList;
    List<Ingredient> ingredientList;
    boolean ingredientListAPICalled = false;
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



        if(getIntent().hasExtra("recipes")){
//            ArrayList<Recipe> temp = getIntent().getExtras().getParcelableArrayList("recipes");
//            recipesList = temp.stream()

        }
        else{
            List<Recipe> recipeList = new LinkedList<Recipe>();
            recipeList.add(new Recipe("Easy & Delish! ~ Apple Crumble", "641803", "https://spoonacular.com/recipeImages/Easy---Delish--Apple-Crumble-641803.jpg"));
            RecipesListAdapter.recipesList = recipeList;
            GetSelectedRecipeData.callIngredientsListAPI(recipeList, queue, ingredientList);
        }

        Button generateIngredientsBtn  = (Button)findViewById(R.id.spawnIngredientsList);
        generateIngredientsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> ingredients = new ArrayList<String>();
                for(Ingredient ingredient : ingredientList){
                    ingredients.add(ingredient.getName());
                }
                Intent shoppingList = new Intent(getApplicationContext(), ShoppingActivity.class);
                shoppingList.putStringArrayListExtra("Ingredients", ingredients);
                startActivity(shoppingList);
                //TODO: Convert to parcelable
            }
        });


    }


    //FOR TESTING
    private class RecipeRequestFinishedListener implements RequestQueue.RequestFinishedListener<JsonArrayRequest>{

        RequestQueue queue = null;
        public void setQueue(RequestQueue queue){
            this.queue = queue;
        }
        @Override
        public void onRequestFinished(Request<JsonArrayRequest> request){
            recipesListAdapter.notifyDataSetChanged();
            if(ShoppingActivity.adapter != null){
                recipesListAdapter.notifyDataSetChanged();
            }
        }

    }

}

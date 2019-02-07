package com.teamalpha.aichef;

import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

public class RecipesList extends AppCompatActivity {
    ListView recipesListView;
    LinkedList<RecipeItem> recipesList;
    LinkedList<RecipeItem> selectedRecipes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes_list);

        Resources res = getResources();

        recipesListView = (ListView)findViewById(R.id.recipesList);

        if(getIntent().hasExtra("recipes")){
            recipesList = (LinkedList)getIntent().getExtras().get("recipes");
        }
        else{
            recipesList = initialiseTestList();
        }

        final RecipesListAdapter recipesListAdapter = new RecipesListAdapter(getApplicationContext(), recipesList, res);
        recipesListView.setAdapter(recipesListAdapter);

        Button generateIngredientsBtn  = (Button)findViewById(R.id.spawnIngredientsList);
        generateIngredientsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<RecipeItem> recipes = recipesListAdapter.getPickedRecipeNames();
                Collections.sort(recipes, new Comparator<RecipeItem>() {
                    @Override
                    public int compare(RecipeItem recipeItem, RecipeItem t1) {
                        return recipeItem.getRecipeText().compareTo(t1.getRecipeText());
                    }
                });
                for(RecipeItem recipe : recipes){
                    System.out.println(recipe.getRecipeText());
                    System.out.flush();
                }
            }
        });

    }

    private static LinkedList<RecipeItem> initialiseTestList(){
        LinkedList<RecipeItem> testList = new LinkedList<RecipeItem>();
        for(int i = 0; i < 20; i++){
            RecipeItem recipe = new RecipeItem(R.drawable.pepe, "Item: " + i, null);
            testList.add(recipe);
        }
        return testList;
    }
}

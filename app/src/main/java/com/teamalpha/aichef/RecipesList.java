package com.teamalpha.aichef;

import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.LinkedList;

public class RecipesList extends AppCompatActivity {
    ListView recipesListView;
    LinkedList<String> recipesList;
    LinkedList<String> selectedRecipes;
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

        RecipesListAdapter recipesListAdapter = new RecipesListAdapter(getApplicationContext(), recipesList, res);
        recipesListView.setAdapter(recipesListAdapter);

    }



    private static LinkedList<String> initialiseTestList(){
        LinkedList<String> testList = new LinkedList<String>();
        for(int i = 0; i < 20; i++){
            testList.add("Item : " + String.valueOf(i));
        }
        return testList;
    }
}

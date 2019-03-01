package com.teamalpha.aichef;

import android.content.res.Resources;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
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

import api.Recipe;

public class ShoppingActivity extends AppCompatActivity {
    ListView shoppingListView;
    static ShoppingListAdapter adapter;
    static List<Recipe> shoppingList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final RequestQueue queue = Volley.newRequestQueue(this);
        queue.addRequestFinishedListener(new RecipeRequestFinishedListener());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping);
        Resources res = getResources();
        List<String> ingredientsList = new LinkedList<>();

        //Obtain the ingredients passed by recipesList
        if(getIntent().hasExtra("Ingredients")){
            ArrayList<String> temp = (ArrayList)getIntent().getExtras().getStringArrayList("Ingredients");
            for(String id : temp){
                ingredientsList.add(id);
            }
        }


        if(adapter == null){
            adapter = new ShoppingListAdapter(ingredientsList, getApplicationContext());
        }

        shoppingListView = (ListView)findViewById(R.id.shoppingList);
        shoppingListView.setAdapter(adapter);

        Button mCameraButton = findViewById(R.id.cameraButton);
        mCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        Button mRecipesListButton = findViewById(R.id.recipesListButton);
        mRecipesListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }


    /*
    Callback mechanism to refresh the shopping list upon the return of the API call
     */
    private class RecipeRequestFinishedListener implements RequestQueue.RequestFinishedListener<JsonArrayRequest>{
        @Override
        public void onRequestFinished(Request<JsonArrayRequest> request){
            adapter.notifyDataSetChanged();
            RecipesList.recipesListAdapter.notifyDataSetChanged();
        }
    }
}

package com.teamalpha.aichef;

import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

        if(getIntent().hasExtra("Ingredients")){
            ArrayList<String> temp = (ArrayList)getIntent().getExtras().getStringArrayList("Ingredients");
            for(String id : temp){
                ingredientsList.add(id);
            }
        }

        adapter = new ShoppingListAdapter(getApplicationContext(), res, ingredientsList);
        shoppingListView = (ListView)findViewById(R.id.shoppingList);
        shoppingListView.setAdapter(adapter);



        //GetSelectedRecipeData.callIngredientsListAPI(shoppingList, queue, ShoppingListAdapter.ingredientList);
    }



    private class RecipeRequestFinishedListener implements RequestQueue.RequestFinishedListener<JsonArrayRequest>{
        @Override
        public void onRequestFinished(Request<JsonArrayRequest> request){
            adapter.notifyDataSetChanged();
            RecipesList.recipesListAdapter.notifyDataSetChanged();
        }

    }
}

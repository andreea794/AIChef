package com.teamalpha.aichef;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.teamalpha.aichef.slideuppanel.IngredientPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import api.Ingredient;
import api.Recipe;

public class MainActivity extends AppCompatActivity {
    FragmentPagerAdapter mAdapter;
    List<String> mScannedIngredients;

    List<Ingredient> scannedIngredients = new ArrayList<>();
    List<Recipe> recipeList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final RequestQueue queue = Volley.newRequestQueue(this);
        queue.addRequestFinishedListener(new RecipeRequestFinishedListener());
        setContentView(R.layout.activity_main);
//        GetRecipeList.setRequestQueue(queue);
        System.out.println("Set request queue finishes");


        ////////////FOR TESTING//////////////////
        Ingredient broccoli = new Ingredient("broccoli");
        Ingredient potato = new Ingredient("potato");
        scannedIngredients.add(broccoli);
        scannedIngredients.add(potato);
        //////////////////////////////////////////

        ////////////FOR TESTING//////////////////
        Recipe r1 = new Recipe("592735");
        Recipe r2 = new Recipe("109376");
        Recipe r3 = new Recipe("1062408");
        List<Recipe> selectedRecipes = new ArrayList<>();
        selectedRecipes.add(r1);
        selectedRecipes.add(r2);
        selectedRecipes.add(r3);
        /////////////////////////////////////////

//        for (int j=0; j < selectedRecipes.size(); j++) {
//
//            System.out.println("Recipe " + (j+1) + ":");
//            List<Ingredient> curlist = GetSelectedRecipeData.callIngredientsListAPI(selectedRecipes.get(j), queue);
//           = GetSelectedRecipeData.getShoppingList();
//            System.out.println("Shopping list length: " + curlist.size());
//            for (int i=0; i<curlist.size(); i++){
//                System.out.println(curlist.get(i).getName());
//            }

        mScannedIngredients = new ArrayList<>();
        mScannedIngredients.add("Broccoli");
        mScannedIngredients.add("Tomato");
        mScannedIngredients.add("Onion");

        //TODO: Save the scanned ingredients state when you transition to other activity.
        SlidingUpPanelLayout slidingUpPanelLayout = findViewById(R.id.layout_slidinguppanel);
        slidingUpPanelLayout.addPanelSlideListener(new SlidingUpPanelListener());


        ViewPager vp = findViewById(R.id.vpPager);
        mAdapter = new IngredientPagerAdapter(getSupportFragmentManager());
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(vp);
        vp.setAdapter(mAdapter);
    }


    public List<String> getScannedIngredients() {
        List<String> ingredients = new ArrayList<>(mScannedIngredients);
        return ingredients;
    }

    public void removeIngredient(String ingredient) {
        mScannedIngredients.remove(ingredient);
    }

    private class RecipeRequestFinishedListener implements RequestQueue.RequestFinishedListener<JsonArrayRequest> {
        @Override
        public void onRequestFinished(Request<JsonArrayRequest> request) {
            System.out.println("BREAKPOINT 3");
            Recipe curRecipe;
            for (int i = 0; i < recipeList.size(); i++) {
                curRecipe = recipeList.get(i);
                System.out.println(curRecipe.getRecipeID() + " " + curRecipe.getRecipeName()
                        + " " + curRecipe.getUsedIngredientCount() + " " + curRecipe.getRecipeImageLink());
            }
            System.out.println("Recipe list size: " + recipeList.size());
        }
    }

    /**
     * Simple PanelSlideListener which implements camera feed pausing when the slide up panel is
     * expanded.
     */
    private class SlidingUpPanelListener implements SlidingUpPanelLayout.PanelSlideListener {
        @Override
        public void onPanelSlide(View panel, float slideOffset) {
        }

        @Override
        public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
            //TODO: Pause and resume camera based on the previous state
            //In the following line, the Toast is only shown when you expand the panel.
            if (previousState == SlidingUpPanelLayout.PanelState.COLLAPSED)
                Toast.makeText(MainActivity.this, "Panel Slide", Toast.LENGTH_LONG).show();
        }
    }

}


package com.teamalpha.aichef;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.List;

import api.GetRecipeList;
import api.GetSelectedRecipeData;
import api.Ingredient;
import api.Recipe;

public class MainActivity extends AppCompatActivity {

    List<Ingredient> scannedIngredients = new ArrayList<>();
    List<Recipe> recipeList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final RequestQueue queue = Volley.newRequestQueue(this);
        setContentView(R.layout.activity_main);
//        GetRecipeList.setRequestQueue(queue);
        System.out.println("Set request queue finishes");


        ////////////FOR TESTING//////////////////
        Ingredient broccoli = new Ingredient("broccoli");
        Ingredient potato = new Ingredient("potato");
        List<Ingredient> inputList = new ArrayList<>();
        inputList.add(broccoli);
        inputList.add(potato);
        //////////////////////////////////////////
//
        GetRecipeList.callRecipeListAPI(inputList, queue, recipeList);
        System.out.println("Get recipe list finishes");
        System.out.println(recipeList.size());
        Recipe curRecipe;

        //TODO: Use AsyncTask to create callback mechanism for the network call.

        for (int i=0; i<recipeList.size(); i++){
            curRecipe = recipeList.get(i);
            System.out.println(curRecipe.getRecipeID() + " " + curRecipe.getRecipeName()
            + " " + curRecipe.getUsedIngredientCount() + " " + curRecipe.getRecipeImageLink());
        }
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
        }

    }


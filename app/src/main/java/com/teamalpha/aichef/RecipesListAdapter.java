package com.teamalpha.aichef;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.LinkedList;

public class RecipesListAdapter extends BaseAdapter {

    private LinkedList<RecipeItem> recipesList;
    private Context c;
    private LayoutInflater mInflater;
    private Resources res;
    private TypedArray tickImage;
    private ArrayList<Boolean> checkedRecipes;
    private ArrayList<RecipeItem> pickedRecipeNames;



    public ArrayList<RecipeItem> getPickedRecipeNames(){
        return pickedRecipeNames;
    }

    public RecipesListAdapter(Context c, LinkedList<RecipeItem> recipesList, Resources res){
        this.c = c;
        this.recipesList = recipesList;
        this.mInflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.res = res;
        tickImage = this.res.obtainTypedArray(R.array.tickImages);
        this.checkedRecipes = new ArrayList<Boolean>(recipesList.size());
        this.pickedRecipeNames = new ArrayList<RecipeItem>();
        for(int i = 0; i<recipesList.size(); i++){
            checkedRecipes.add(false);
        }
    }

    @Override
    public int getCount() {
        return recipesList.size();
    }

    @Override
    public Object getItem(int i) {
        if(recipesList.contains(i)){
            return recipesList.get(i);
        }
        else{
            return null;
        }
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        View v;
        if(view == null){
            v = mInflater.inflate(R.layout.recipes_list_layout, null);
        }
        else{
            v = view;
        }
        RecipeItem recipe = recipesList.get(i);
        final ImageView recipeImg= (ImageView)v.findViewById(R.id.recipeImg);
        final CheckedTextView recipeText = (CheckedTextView)v.findViewById(R.id.recipeText);
        recipeText.setText(recipesList.get(i).getRecipeText());
        recipeImg.setImageResource(recipe.getRecipeImg());
        //make sure recipe is consistent with Current View as determined
        recipeText.setChecked(checkedRecipes.get(i));
        if(recipeText.isChecked()){
            recipeText.setCheckMarkDrawable(R.drawable.checked);
        }
        else{
            recipeText.setCheckMarkDrawable(0);
        }
        //by checkedRecipes
        recipeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkedRecipes.get(i)){
                    recipeText.setCheckMarkDrawable(0);
                    recipeText.setChecked(false);
                    checkedRecipes.set(i, false);
                    pickedRecipeNames.remove(recipesList.get(i)); //remove from pickedRecipes
                }
                else {
                    recipeText.setCheckMarkDrawable(R.drawable.checked);
                    recipeText.setChecked(true);
                    checkedRecipes.set(i, true);
                    pickedRecipeNames.add(recipesList.get(i)); //add to pickedRecipes
                }
            }
        });
        return v;
    }
}

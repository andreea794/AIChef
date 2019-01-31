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

import java.util.ArrayList;
import java.util.LinkedList;

public class RecipesListAdapter extends BaseAdapter {

    private LinkedList<String> recipesList;
    private Context c;
    private LayoutInflater mInflater;
    private Resources res;
    private TypedArray tickImage;
    final ArrayList<Boolean> checkedRecipes;

    public RecipesListAdapter(Context c, LinkedList<String> recipesList, Resources res){
        this.c = c;
        this.recipesList = recipesList;
        this.mInflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.res = res;
        tickImage = this.res.obtainTypedArray(R.array.tickImages);
        this.checkedRecipes = new ArrayList<Boolean>(recipesList.size());
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
        final CheckedTextView recipe = (CheckedTextView)v.findViewById(R.id.recipe);
        recipe.setText(recipesList.get(i));
        //make sure recipe is consistent with Current View as determined
        recipe.setChecked(checkedRecipes.get(i));
        if(recipe.isChecked()){
            recipe.setCheckMarkDrawable(R.drawable.checked);
        }
        else{
            recipe.setCheckMarkDrawable(0);
        }
        //by checkedRecipes
        recipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Position : " + i);
                if(checkedRecipes.get(i)){
                    recipe.setCheckMarkDrawable(0);
                    recipe.setChecked(false);
                    checkedRecipes.set(i, false);
                }
                else {
                    recipe.setCheckMarkDrawable(R.drawable.checked);
                    recipe.setChecked(true);
                    checkedRecipes.set(i, true);
                }
            }
        });
        return v;
    }
}

package com.teamalpha.aichef;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

import api.Ingredient;
import api.Recipe;

public class ShoppingListAdapter extends BaseAdapter {
    static List<Ingredient> ingredientList;
    private Context c;
    private LayoutInflater mInflater;

    public ShoppingListAdapter(List<Ingredient> ingredientList, Context c){
        this.c = c;
        this.ingredientList = ingredientList;
        this.mInflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return ingredientList.size();
    }

    @Override
    public Object getItem(int i) {
        return ingredientList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        System.out.println("Recipe list size now: " + ingredientList.size());
        View v;
        if(view == null){
            v = mInflater.inflate(R.layout.activity_shopping_list_layout, null);
        }
        else{
            v = view;
        }
        Ingredient ingredient = ingredientList.get(i);
        final ImageView recipeImg= (ImageView)v.findViewById(R.id.recipeImg);
        final TextView recipeText = (CheckedTextView)v.findViewById(R.id.ingredientText);
        recipeText.setText(ingredientList.get(i).getName());


        return v;
    }
}

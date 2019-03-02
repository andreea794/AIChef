package com.teamalpha.aichef;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ShoppingListAdapter extends BaseAdapter {
    static List<String> ingredientList;
    private Context c;
    private LayoutInflater mInflater;


    public ShoppingListAdapter(List<String> ingredientList, Context c){

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
        View v;
        if(view == null){
            v = mInflater.inflate(R.layout.activity_shopping_list_layout, null);
        }
        else{
            v = view;
        }


        //Write the ingredient name on each list entry
        String ingredient = ingredientList.get(i);
        final TextView recipeText = (CheckedTextView)v.findViewById(R.id.ingredientText);
        recipeText.setText(ingredient);

        final ImageView checked = (ImageView)v.findViewById(R.id.shopping_checked);
        recipeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //toggle visibilities of the checked icon of each shopping list item
                int visibility = checked.getVisibility();
                if(visibility == View.INVISIBLE){
                    checked.setVisibility(View.VISIBLE);
                }
                else{
                    checked.setVisibility(View.INVISIBLE);
                }
            }
        });

        return v;
    }
}

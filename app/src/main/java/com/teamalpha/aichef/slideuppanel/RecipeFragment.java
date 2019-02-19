package com.teamalpha.aichef.slideuppanel;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.teamalpha.aichef.R;

import java.util.ArrayList;
import java.util.List;

import api.Recipe;

public class RecipeFragment extends Fragment {

    static List<Recipe> recipes;
    static RecipeAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recipes = new ArrayList<>();
    }

    public static void refresh() {
        adapter.notifyDataSetChanged();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_slideup_recipe, container, false);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        RecyclerView recyclerView = view.findViewById(R.id.rv_recipes_frag);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new RecipeAdapter();
        recyclerView.setAdapter(adapter);

        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getContext()));
        return view;
    }

    private class RecipeAdapter extends RecyclerView.Adapter {

        @Override
        public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View recipeView = LayoutInflater.from(parent.getContext())
                    .inflate(android.R.layout.simple_list_item_1, parent, false);
            return new RecipeViewHolder(recipeView);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            String title = recipes.get(position).getRecipeName();
            ((RecipeViewHolder) holder).mTextView.setText(title);
        }

        @Override
        public int getItemCount() {
            return recipes.size();
        }

        private class RecipeViewHolder extends RecyclerView.ViewHolder {

            TextView mTextView;

            RecipeViewHolder(View itemView) {
                super(itemView);
                mTextView = (TextView) itemView;
            }
        }
    }


}

package com.teamalpha.aichef.slideuppanel;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.teamalpha.aichef.MainActivity;
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
        //Test data
        recipes.add(new Recipe("Example 1", "1", "1"));
        recipes.add(new Recipe("Really really long example recipe name to test whether list item view can handle new line", "2", "2"));
    }

    public static void refresh() {
        adapter.notifyDataSetChanged();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_slideup_recipe, container, false);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        final RecyclerView recyclerView = view.findViewById(R.id.rv_recipes_frag);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new RecipeAdapter();
        recyclerView.setAdapter(adapter);

        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getContext()));

        Button selectionButton = view.findViewById(R.id.button_show_selected);
        selectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Recipe> selectedRecipes = new ArrayList<>();
                for (int i = 0; i < recyclerView.getChildCount(); i++) {
                    RecipeAdapter.RecipeViewHolder curr = (RecipeAdapter.RecipeViewHolder) recyclerView.findViewHolderForAdapterPosition(i);
                    if (curr.selected) selectedRecipes.add(recipes.get(i));
                }
                /*
                Intent intent = new Intent(getContext(), RecipeListActivity.class);
                intent.putParcelableArrayListExtra("selected", selectedRecipes);
                startActivity(intent);
                */
            }
        });
        return view;
    }

    private class RecipeAdapter extends RecyclerView.Adapter {

        @Override
        public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View recipeView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_recipe, parent, false);
            return new RecipeViewHolder(recipeView);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            String title = recipes.get(position).getRecipeName();
            final RecipeViewHolder currHolder = (RecipeViewHolder) holder;
            TextView recipeView = currHolder.mTextView;
            final ImageView selected = currHolder.mImageView;
            recipeView.setText(title);
            recipeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toggle the visibility of the check mark on each recipe
                    int newVisibility = (selected.getVisibility() == View.VISIBLE) ? View.INVISIBLE : View.VISIBLE;
                    selected.setVisibility(newVisibility);
                    currHolder.selected = !currHolder.selected;
                }
            });

        }

        @Override
        public int getItemCount() {
            return recipes.size();
        }

        private class RecipeViewHolder extends RecyclerView.ViewHolder {

            TextView mTextView;
            ImageView mImageView;
            boolean selected;

            RecipeViewHolder(View itemView) {
                super(itemView);
                mTextView = itemView.findViewById(R.id.tv_recipe_name);
                mImageView = itemView.findViewById(R.id.iv_add_recipe);
                selected = false;
            }
        }
    }


}

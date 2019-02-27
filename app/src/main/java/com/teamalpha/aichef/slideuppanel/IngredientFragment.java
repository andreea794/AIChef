package com.teamalpha.aichef.slideuppanel;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.teamalpha.aichef.MainActivity;
import com.teamalpha.aichef.R;

import java.util.ArrayList;
import java.util.List;

import api.GetRecipeList;

import static com.teamalpha.aichef.slideuppanel.RecipeFragment.spinner;

public class IngredientFragment extends Fragment {

    public static List<String> scannedIngredients;
    static IngredientAdapter adapter;
    static LinearLayout mEmptyView;
    RequestQueue mQueue;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        scannedIngredients = new ArrayList<String>();
        mQueue = Volley.newRequestQueue(getContext());
        mQueue.addRequestFinishedListener(new RecipeRequestFinishedListener());
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_slideup_ingredient, container, false);

        mEmptyView = view.findViewById(R.id.tv_empty_view_ingredient);
        if (scannedIngredients.size() == 0) mEmptyView.setVisibility(View.VISIBLE);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        RecyclerView recyclerView = view.findViewById(R.id.rv_ingredients_frag);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new IngredientAdapter();
        recyclerView.setAdapter(adapter);

        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getContext()));


        Button apiCallButton = view.findViewById(R.id.button_get_recipe);
        apiCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinner.setVisibility(View.VISIBLE);
                RecipeFragment.refresh();
                GetRecipeList.callRecipeListAPI(scannedIngredients, mQueue, RecipeFragment.recipes);
                MainActivity.moveSlideUpPanel(1);
            }
        });
        return view;
    }

    /**
     * Simple RecyclerView adapter for the Ingredient fragment
     */
    private class IngredientAdapter extends RecyclerView.Adapter {

        @Override
        public IngredientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View ingredientView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_ingredient, parent, false);
            return new IngredientViewHolder(ingredientView);
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
            final String msg = scannedIngredients.get(position);
            ((IngredientViewHolder) holder).mTextView.setText(msg);
            ((IngredientViewHolder) holder).mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    scannedIngredients.remove(holder.getAdapterPosition());
                    refresh();
                }
            });
        }

        @Override
        public int getItemCount() {
            return scannedIngredients.size();
        }

        private class IngredientViewHolder extends RecyclerView.ViewHolder {

            TextView mTextView;
            ImageView mImageView;

            IngredientViewHolder(View itemView) {
                super(itemView);
                LinearLayout layout = (LinearLayout) itemView;
                mTextView = layout.findViewById(R.id.tv_ingredient_name);
                mImageView = layout.findViewById(R.id.iv_remove_ingredient);
            }
        }
    }

    static public void refresh() {
        adapter.notifyDataSetChanged();
        int visibility = (scannedIngredients.size() == 0) ? View.VISIBLE : View.GONE;
        mEmptyView.setVisibility(visibility);
    }

    /**
     * Simple callback mechanism for recipe API call, which triggers the adapter refresh
     * in RecipeFragment.
     */
    private class RecipeRequestFinishedListener implements RequestQueue.RequestFinishedListener<JsonArrayRequest> {
        @Override
        public void onRequestFinished(Request<JsonArrayRequest> request) {
            spinner.setVisibility(View.GONE);
            RecipeFragment.refresh();
        }
    }
}

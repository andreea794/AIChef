package com.teamalpha.aichef.slideuppanel;

import android.content.Context;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.teamalpha.aichef.MainActivity;
import com.teamalpha.aichef.R;

public class IngredientFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_slideup_ingredient, container, false);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        RecyclerView recyclerView = view.findViewById(R.id.rv_ingredients_frag);
        recyclerView.setLayoutManager(layoutManager);

        IngredientAdapter adapter = new IngredientAdapter(getActivity());
        recyclerView.setAdapter(adapter);

        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getContext()));

        Button apiCallButton = view.findViewById(R.id.button_get_recipe);
        apiCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: Track the recipe list in the RecipeFragment, but access it here.
                Toast.makeText(getContext(), "HELLO", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }


    /**
     * Simple RecyclerView adapter for the Ingredient fragment
     */
    private class IngredientAdapter extends RecyclerView.Adapter {

        MainActivity mainActivity;

        public IngredientAdapter(Context context) {
            mainActivity = (MainActivity) context;
        }


        @Override
        public IngredientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View ingredientView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_ingredient, parent, false);
            return new IngredientViewHolder(ingredientView);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            final String msg = mainActivity.getScannedIngredients().get(position);
            ((IngredientViewHolder) holder).mTextView.setText(msg);
            ((IngredientViewHolder) holder).mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mainActivity.removeIngredient(msg);
                    IngredientAdapter.this.notifyDataSetChanged();
                }
            });
        }

        @Override
        public int getItemCount() {
            return mainActivity.getScannedIngredients().size();
        }

        private class IngredientViewHolder extends RecyclerView.ViewHolder {

            TextView mTextView;
            ImageView mImageView;

            public IngredientViewHolder(View itemView) {
                super(itemView);
                LinearLayout layout = (LinearLayout) itemView;
                mTextView = layout.findViewById(R.id.tv_ingredient_name);
                mImageView = layout.findViewById(R.id.iv_remove_ingredient);
            }
        }
    }
}

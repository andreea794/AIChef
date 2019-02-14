package com.teamalpha.aichef.slideuppanel;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.teamalpha.aichef.MainActivity;
import com.teamalpha.aichef.R;

import java.util.ArrayList;

public class IngredientAdapter extends RecyclerView.Adapter {

    MainActivity mainActivity;

    public IngredientAdapter(Context context) {
        mainActivity = (MainActivity) context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View exerciseView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_ingredient, parent, false);
        return new ViewHolder(exerciseView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //TODO: Receive ingredient list information from current recipe and iterate over it.

        final String msg = mainActivity.getScannedIngredients().get(position);
        ((ViewHolder) holder).mTextView.setText(msg);
        ((ViewHolder) holder).mImageView.setOnClickListener(new View.OnClickListener() {
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

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView mTextView;
        ImageView mImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            LinearLayout layout = (LinearLayout) itemView;
            mTextView = layout.findViewById(R.id.tv_ingredient_name);
            mImageView = layout.findViewById(R.id.iv_remove_ingredient);
        }
    }
}

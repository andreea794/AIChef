package com.teamalpha.aichef.slideuppanel;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class IngredientAdapter extends RecyclerView.Adapter {

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TextView exerciseView = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ViewHolder(exerciseView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //TODO: Receive ingredient list information from current recipe and iterate over it.
        String msg;
        switch (position) {
            case 0:
                msg = "Apple";
                break;
            case 1:
                msg = "Onion";
                break;
            case 2:
                msg = "Carrot";
                break;
            default:
                msg = "Tomato";
        }

        ((ViewHolder) holder).mTextView.setText(msg);
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView mTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView;
        }
    }
}

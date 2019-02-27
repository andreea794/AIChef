package com.teamalpha.aichef.slideuppanel;

import android.view.View;
import android.widget.ImageView;

import com.teamalpha.aichef.R;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.ViewAssertion;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class RecipeUnselectedAssertion implements ViewAssertion {

    private int mPosition;

    public static RecipeUnselectedAssertion withRecipeUnselectedAt(int pos) {
        return new RecipeUnselectedAssertion(pos);
    }

    private RecipeUnselectedAssertion(int pos) {
        mPosition = pos;
    }

    @Override
    public void check(View view, NoMatchingViewException noViewFoundException) {
        if (noViewFoundException != null) throw noViewFoundException;

        RecyclerView rv = (RecyclerView) view;
        ImageView selected = rv.findViewHolderForAdapterPosition(mPosition).itemView.findViewById(R.id.iv_add_recipe);
        assertThat(selected.getVisibility(), is(View.INVISIBLE));
    }
}

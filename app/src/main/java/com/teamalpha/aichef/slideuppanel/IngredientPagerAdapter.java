package com.teamalpha.aichef.slideuppanel;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class IngredientPagerAdapter extends FragmentPagerAdapter {

    private static int NUM_ITEMS = 2;

    public IngredientPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new IngredientFragment();
            case 1:
                //TODO: Implement RecipeFragment and add field in PagerAdapter to toggle between
                //ingredients and recipes
                return new IngredientFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return (position == 0) ? "INGREDIENTS" : "RECIPES";
    }
}

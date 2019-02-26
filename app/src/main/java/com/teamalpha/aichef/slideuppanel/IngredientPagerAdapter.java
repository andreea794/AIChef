package com.teamalpha.aichef.slideuppanel;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

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
                return new RecipeFragment();
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

package com.teamalpha.aichef.slideuppanel;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ExamplePagerAdapter extends FragmentPagerAdapter {

    private static int NUM_ITEMS = 2;

    public ExamplePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        //TODO: Receive ingredient list information from current recipe and iterate over it.
        switch (position) {
            case 0:
                return new IngredientFragment();
            case 1:
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

package com.teamalpha.aichef.slideuppanel;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.teamalpha.aichef.R;

import org.hamcrest.Matcher;

import androidx.slidingpanelayout.widget.SlidingPaneLayout;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.viewpager.widget.ViewPager;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;

public class SlideUpViewActions {

    public static SlideUpAction slideUpAction(int position, boolean expanded) {
        return new SlideUpAction(position, expanded);
    }

    public static RemoveIngredientAction removeIngredientAction() {
        return new RemoveIngredientAction();
    }

    public static SelectRecipeAction selectRecipeAction() {
        return new SelectRecipeAction();
    }

    private static class SlideUpAction implements ViewAction {

        int mPosition;
        boolean mExpanded;

        public SlideUpAction(int position, boolean expanded) {
            mPosition = position;
            mExpanded = expanded;
        }

        @Override
        public Matcher<View> getConstraints() {
            return isAssignableFrom(SlidingUpPanelLayout.class);
        }

        @Override
        public String getDescription() {
            return "expand or collapse the panel to specified position";
        }

        @Override
        public void perform(UiController uiController, View view) {
            SlidingUpPanelLayout layout = (SlidingUpPanelLayout) view;
            ViewPager viewPager = layout.findViewById(R.id.vpPager);
            viewPager.setCurrentItem(mPosition);
            SlidingUpPanelLayout.PanelState state = mExpanded ? SlidingUpPanelLayout.PanelState.EXPANDED : SlidingUpPanelLayout.PanelState.COLLAPSED;
            layout.setPanelState(state);
        }
    }

    private static class RemoveIngredientAction implements ViewAction {

        ViewAction click = click();

        @Override
        public Matcher<View> getConstraints() {
            return click.getConstraints();
        }

        @Override
        public String getDescription() {
            return "click on remove icon of one ingredient";
        }

        @Override
        public void perform(UiController uiController, View view) {
            ImageView delete = view.findViewById(R.id.iv_remove_ingredient);
            click.perform(uiController, delete);
        }
    }

    private static class SelectRecipeAction implements ViewAction {

        ViewAction click = click();

        @Override
        public Matcher<View> getConstraints() {
            return click.getConstraints();
        }

        @Override
        public String getDescription() {
            return "click on a suggested recipe";
        }

        @Override
        public void perform(UiController uiController, View view) {
            TextView recipe = view.findViewById(R.id.tv_recipe_name);
            click.perform(uiController, recipe);
        }
    }
}

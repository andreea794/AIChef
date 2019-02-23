package com.teamalpha.aichef;

import android.view.View;
import android.widget.ImageView;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.hamcrest.Matcher;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;

public class SlideUpViewActions {

    public static SlideUpAction slideUpAction() {
        return new SlideUpAction();
    }

    public static RemoveIngredientAction removeIngredientAction() {
        return new RemoveIngredientAction();
    }

    static class SlideUpAction implements ViewAction {

        @Override
        public Matcher<View> getConstraints() {
            return isAssignableFrom(SlidingUpPanelLayout.class);
        }

        @Override
        public String getDescription() {
            return "slide up the panel";
        }

        @Override
        public void perform(UiController uiController, View view) {
            SlidingUpPanelLayout layout = (SlidingUpPanelLayout) view;
            layout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
        }
    }

    static class RemoveIngredientAction implements ViewAction {

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
}

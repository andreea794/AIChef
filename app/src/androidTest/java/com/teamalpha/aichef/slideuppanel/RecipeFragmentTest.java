package com.teamalpha.aichef.slideuppanel;

import com.teamalpha.aichef.MainActivity;
import com.teamalpha.aichef.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;
import api.Recipe;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread;
import static com.teamalpha.aichef.slideuppanel.RecipeUnselectedAssertion.withRecipeUnselectedAt;
import static com.teamalpha.aichef.slideuppanel.RecyclerViewItemCountAssertion.withItemCount;

@RunWith(AndroidJUnit4.class)
public class RecipeFragmentTest {
    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void recipe_list_is_empty_at_startup() {
        onView(withId(R.id.rv_recipes_frag)).check(withItemCount(0));
        onView(withId(R.id.tv_empty_view_recipe)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    @Test
    public void recipe_list_size_upon_insertion() throws Throwable {
        addDummyRecipe();
        onView(withId(R.id.rv_recipes_frag)).check(withItemCount(1));
    }

    @Test
    public void selection_cleared_upon_new_recipes() throws Throwable {
        addDummyRecipe();
        onView(withId(R.id.layout_slidinguppanel)).perform(SlideUpViewActions.slideUpAction(1, true));
        Thread.sleep(1000);
        onView(withId(R.id.rv_recipes_frag)).perform(actionOnItemAtPosition(0, SlideUpViewActions.selectRecipeAction()));
        updateDummyRecipe();
        onView(withId(R.id.rv_recipes_frag)).check(withRecipeUnselectedAt(0));
    }

    private void addDummyRecipe() throws Throwable {
        RecipeFragment.recipes.add(new Recipe("Recipe", "Recipe", "Recipe"));
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                RecipeFragment.refresh();
            }
        });
    }

    private void updateDummyRecipe() throws Throwable {
        RecipeFragment.recipes.clear();
        addDummyRecipe();
    }
}
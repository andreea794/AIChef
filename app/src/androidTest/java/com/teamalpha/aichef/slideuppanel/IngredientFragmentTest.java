package com.teamalpha.aichef.slideuppanel;

import com.teamalpha.aichef.MainActivity;
import com.teamalpha.aichef.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.Visibility;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread;
import static com.teamalpha.aichef.slideuppanel.RecyclerViewItemCountAssertion.withItemCount;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
public class IngredientFragmentTest {

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void scanned_list_is_empty_at_startup() {
        onView(withId(R.id.rv_ingredients_frag)).check(withItemCount(0));
        onView(withId(R.id.tv_empty_view_ingredient)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)));
    }

    @Test
    public void scanned_list_size_upon_insertion() throws Throwable {
        addDummyIngredient();
        onView(withId(R.id.rv_ingredients_frag)).check(withItemCount(1));
        onView(withId(R.id.tv_empty_view_ingredient)).check(matches(withEffectiveVisibility(Visibility.GONE)));
    }

    @Test
    public void scanned_list_size_upon_deletion() throws Throwable {
        addDummyIngredient();
        onView(withId(R.id.layout_slidinguppanel)).perform(SlideUpViewActions.slideUpAction(0, true));

        //Wait for the slideup panel to expand
        Thread.sleep(1000);

        onView(allOf(withId(R.id.rv_ingredients_frag), isDisplayed()))
                .perform(actionOnItemAtPosition(0, SlideUpViewActions.removeIngredientAction()))
                .check(withItemCount(0));
        onView(withId(R.id.tv_empty_view_ingredient)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)));
    }

    public static void addDummyIngredient() throws Throwable {
        IngredientFragment.scannedIngredients.add("Ingredient");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                IngredientFragment.refresh();
            }
        });
    }

}
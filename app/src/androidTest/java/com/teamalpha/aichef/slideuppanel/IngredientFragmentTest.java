package com.teamalpha.aichef.slideuppanel;

import com.teamalpha.aichef.MainActivity;
import com.teamalpha.aichef.R;
import com.teamalpha.aichef.SlideUpViewActions;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;
import api.Ingredient;

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

    /*
    WRITE TESTS FOR:

    - RECIPE LIST IS EMPTY AT STARTUP
    - RECIPE LIST SIZE CHANGE AFTER INSERTING RECIPES
    - RECIPE LIST SELECTION / SELECTION CLEARING AFTER NEW API CALL


    - DATA PERSISTENCE WHEN YOU LEAVE THE APP TEMPORARILY/SLIDEUP PANEL POSITION CHANGE
    - DATA PASSING BETWEEN FRAGMENTS (MOCK API CALL)
    - TAB CHANGE UPON SEARCHING RECIPES

    - TEST FOR DATA PASSING BETWEEN MA AND RECIPELIST
    - TEST FOR SCANNED LIST SIZE CHANGE AFTER ACCEPTING A SCAN
    - TEST FOR CAMERA PAUSE/RESUME ON SLIDEUP CHANGE
    - TEST FOR INGREDIENT LIST GENERATION  (RECIPES WITH NO INGREDIENTS, UNION)
    - TEST FOR MANUAL INGREDIENT ADDITION
    - TEST FOR SCANNED LIST SIZE AFTER REFUSING A SCAN
    - TEST FOR WEB BROWSER LAUNCH IN RECIPE LIST ACTIVITY (WHAT HAPPENS WHEN THE URL IS DEAD? WHAT ABOUT IMAGE LINKS?)


    - TEST FOR API CALL (ENSURE NO CALL WHEN SCANNED LIST IS EMPTY, SHOW SOME VIEW WHEN RESPONSE IS EMPTY, MAYBE LIMIT INPUT SIZE?)
    - TEST FOR API CALL - HOW CAN USER REQUEST MORE RECIPES? CAN THE USER DECIDE HOW MANY TO SEARCH FOR IN THE FIRST PLACE?


    */
    @Test
    public void scanned_list_is_empty_at_startup() {
        onView(withId(R.id.rv_ingredients_frag)).check(withItemCount(0));
        onView(withId(R.id.tv_empty_view_ingredient)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)));
    }

    @Test
    public void scanned_list_size_upon_insertion() throws Throwable {
        addDummyData();
        onView(withId(R.id.rv_ingredients_frag)).check(withItemCount(1));
        onView(withId(R.id.tv_empty_view_ingredient)).check(matches(withEffectiveVisibility(Visibility.GONE)));
    }

    @Test
    public void scanned_list_size_upon_deletion() throws Throwable {
        addDummyData();
        onView(withId(R.id.layout_slidinguppanel)).perform(SlideUpViewActions.slideUpAction());

        //Wait for the slideup panel to expand
        Thread.sleep(1000);

        onView(allOf(withId(R.id.rv_ingredients_frag), isDisplayed()))
                .perform(actionOnItemAtPosition(0, SlideUpViewActions.removeIngredientAction()))
                .check(withItemCount(0));
        onView(withId(R.id.tv_empty_view_ingredient)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)));
    }


    private void addDummyData() throws Throwable {
        IngredientFragment.mScannedIngredients.add(new Ingredient("Ingredient"));
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                IngredientFragment.refresh();
            }
        });
    }

}
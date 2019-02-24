package com.teamalpha.aichef;

import com.teamalpha.aichef.slideuppanel.SlideUpViewActions;

import org.junit.Rule;
import org.junit.Test;

import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static com.teamalpha.aichef.slideuppanel.CameraPausedMatcher.withCameraPaused;
import static org.hamcrest.Matchers.allOf;

public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void camera_paused_on_slideup () throws InterruptedException {
        onView(withId(R.id.camFrame)).check(matches(hasDescendant(allOf(isAssignableFrom(CameraPreview.class), withCameraPaused(false)))));
        onView(withId(R.id.layout_slidinguppanel)).perform(SlideUpViewActions.slideUpAction(0, true));
        Thread.sleep(1000);
        onView(withId(R.id.camFrame)).check(matches(hasDescendant(allOf(isAssignableFrom(CameraPreview.class), withCameraPaused(true)))));
        onView(withId(R.id.layout_slidinguppanel)).perform(SlideUpViewActions.slideUpAction(0, false));
        Thread.sleep(1000);
        onView(withId(R.id.camFrame)).check(matches(hasDescendant(allOf(isAssignableFrom(CameraPreview.class), withCameraPaused(false)))));
    }

}
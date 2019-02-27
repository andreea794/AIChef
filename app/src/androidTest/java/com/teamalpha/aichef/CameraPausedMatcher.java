package com.teamalpha.aichef;

import android.view.View;

import com.teamalpha.aichef.CameraPreview;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

import androidx.test.espresso.matcher.BoundedMatcher;

public class CameraPausedMatcher {
    public static Matcher<View> withCameraPaused(final boolean paused) {
        return new BoundedMatcher<View, CameraPreview>(CameraPreview.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("Checking whether the camera is paused: ");
                description.appendText("expecting status: " + paused);
            }

            @Override
            protected boolean matchesSafely(CameraPreview item) {
                return item.isPaused() == paused;
            }
        };
    }
}

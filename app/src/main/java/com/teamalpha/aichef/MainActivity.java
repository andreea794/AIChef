package com.teamalpha.aichef;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.teamalpha.aichef.slideuppanel.IngredientPagerAdapter;

public class MainActivity extends AppCompatActivity {
    FragmentPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TODO: Save the scanned ingredients state when you transition to other activity.
        SlidingUpPanelLayout slidingUpPanelLayout = findViewById(R.id.layout_slidinguppanel);
        slidingUpPanelLayout.addPanelSlideListener(new SlidingUpPanelListener());


        ViewPager vp = findViewById(R.id.vpPager);
        adapter = new IngredientPagerAdapter(getSupportFragmentManager());
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(vp);
        vp.setAdapter(adapter);
    }

    /**
     * Simple PanelSlideListener which implements camera feed pausing when the slide up panel is
     * expanded.
     */
    private class SlidingUpPanelListener implements SlidingUpPanelLayout.PanelSlideListener {
        @Override
        public void onPanelSlide(View panel, float slideOffset) {
        }

        @Override
        public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
            //TODO: Pause and resume camera based on the previous state
            //In the following line, the Toast is only shown when you expand the panel.
            if (previousState == SlidingUpPanelLayout.PanelState.COLLAPSED)
                Toast.makeText(MainActivity.this, "Panel Slide", Toast.LENGTH_LONG).show();
        }
    }
}

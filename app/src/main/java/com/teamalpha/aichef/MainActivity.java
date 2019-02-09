package com.teamalpha.aichef;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.teamalpha.aichef.slideuppanel.IngredientPagerAdapter;

public class MainActivity extends AppCompatActivity {
    FragmentPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager vp = findViewById(R.id.vpPager);
        adapter = new IngredientPagerAdapter(getSupportFragmentManager());
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(vp);
        vp.setAdapter(adapter);
    }
}

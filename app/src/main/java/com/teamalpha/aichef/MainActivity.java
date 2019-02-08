package com.teamalpha.aichef;

import android.support.v7.app.AppCompatActivity;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import android.os.Bundle;

import api.GetRecipeList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final RequestQueue queue = Volley.newRequestQueue(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GetRecipeList.setRequestQueue(queue);
        System.out.println("Set request queue finishes");
        GetRecipeList.callRecipeListAPI();
        System.out.println("Get recipe list finishes");
    }
}

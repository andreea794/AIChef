package com.teamalpha.aichef;

import android.support.v7.app.AppCompatActivity;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private static RequestQueue mQueue;


    RequestQueue queue = (RequestQueue) Volley.newRequestQueue(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}

package com.teamalpha.aichef;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final RequestQueue queue = Volley.newRequestQueue(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button recipesListBtn = (Button)findViewById(R.id.recipesListBtn);
        recipesListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent recipesList = new Intent(getApplicationContext(), RecipesList.class);
                startActivity(recipesList);
            }
        });


    }

}

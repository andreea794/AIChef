package com.teamalpha.aichef;

import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.SearchView;

public class MainActivity extends AppCompatActivity {
    private SearchView searchView = null;
    private Camera mCamera = null;
    private CameraPreview mPreview = null;
    private FrameLayout mCamFrame = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchView = findViewById(R.id.searchView);
        searchView.setQueryHint("Search for ingredient");

        // Create an instance of Camera
        try {
            mCamera = Camera.open();
        } catch (Exception e) {
            Log.d("Camera", "Could not open camera");
            e.printStackTrace();
            return;
        }

        // Create preview and set it as the content of the frame
        mPreview = new CameraPreview(this, mCamera);
        mCamFrame = findViewById(R.id.camFrame);
        mCamFrame.addView(mPreview);

        searchView.bringToFront();
    }
}

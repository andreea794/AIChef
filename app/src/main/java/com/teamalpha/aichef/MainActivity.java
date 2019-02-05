package com.teamalpha.aichef;

import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
    private static final int CAMERA_REQUEST_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchView = findViewById(R.id.searchView);
        mCamFrame = findViewById(R.id.camFrame);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                // Have been denied permission before, so show user an explanation as to why we
                // need camera permission
                Snackbar explanation = Snackbar.make(
                        mCamFrame,
                        "This app needs camera permission in order to scan your ingredients.",
                        Snackbar.LENGTH_LONG);
                explanation.show();
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        CAMERA_REQUEST_ID);
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        CAMERA_REQUEST_ID);
            }
        }
        // Permission has been granted
        initialiseState();
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case CAMERA_REQUEST_ID: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission has been granted
                    initialiseState();
                } else {
                    // Permission has been denied; since camera permission is critical, show user an
                    // explanation of why it needed it
                    Snackbar explanation = Snackbar.make(
                            mCamFrame,
                            "This app needs camera permission in order to scan your ingredients.",
                            Snackbar.LENGTH_LONG);
                    explanation.show();
                }
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    private void initialiseState() {
        searchView.setQueryHint("Search for ingredient");

        // Create an instance of Camera
        try {
            mCamera = Camera.open();
        } catch (Exception e) {
            Log.d("CAMERA", "Could not open camera");
            e.printStackTrace();
            return;
        }

        // Create preview and set it as the content of the frame
        mPreview = new CameraPreview(this, mCamera);
        mCamFrame.addView(mPreview);

        searchView.bringToFront();
    }
}

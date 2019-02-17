package com.teamalpha.aichef;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.SearchView;

public class MainActivity extends AppCompatActivity implements CameraPreview.PreviewListener{
    private SearchView searchView = null;
    private CameraPreview mPreview;
    private boolean isPaused = false; // whether the preview is paused
    private FrameLayout mCamFrame = null;
    private static final int CAMERA_REQUEST_ID = 1;

    private Button b;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchView = findViewById(R.id.searchView);
        mCamFrame = findViewById(R.id.camFrame);

        b = findViewById(R.id.button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseOrResumeCamera();
            }
        });

        askCameraPermission();

        // Permission has been granted
        initialiseState();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
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

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void initialiseState() {
        searchView.setQueryHint("Search for ingredient");

        // Create preview and set it as the content of the frame
        mPreview = new CameraPreview(this);
        mCamFrame.addView(mPreview);

        searchView.bringToFront();
    }

    private void askCameraPermission() {
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
    }

    @Override
    public void onPreviewUpdated(byte[] data, int width, int height) {
        if (data != null) {
            /* TODO: send image data to back-end. */
            if (isPaused)
                Log.e("PREVIEW", "Paused, but still in preview!");
        }
    }

    public void pauseOrResumeCamera() {
        isPaused = !isPaused;
        mPreview.pauseOrResume(isPaused);
        if (!isPaused) {
            mPreview.resetBuffer();
        }
    }
}

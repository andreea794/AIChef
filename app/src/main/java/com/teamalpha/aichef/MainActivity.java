package com.teamalpha.aichef;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.teamalpha.aichef.classifier.AIChefClassifier;
import com.teamalpha.aichef.slideuppanel.IngredientFragment;
import com.teamalpha.aichef.slideuppanel.IngredientPagerAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class MainActivity extends AppCompatActivity implements CameraPreview.PreviewListener {
    FragmentPagerAdapter mAdapter;
    static ViewPager mViewPager;
    private SearchView mSearchView = null;
    private CameraPreview mPreview;
    private boolean isPaused = false; // whether the preview is paused
    private FrameLayout mCamFrame = null;
    private static final int CAMERA_REQUEST_ID = 1;
    private AIChefClassifier imageClassifier = null;
    private ProgressBar mSpinner = null;
    private boolean canScan = false;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageClassifier = new AIChefClassifier(this.getAssets(), this);

        SlidingUpPanelLayout slidingUpPanelLayout = findViewById(R.id.layout_slidinguppanel);
        slidingUpPanelLayout.addPanelSlideListener(new SlidingUpPanelListener());

        mViewPager = findViewById(R.id.vpPager);
        mAdapter = new IngredientPagerAdapter(getSupportFragmentManager());
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(mViewPager);
        mViewPager.setAdapter(mAdapter);
        mCamFrame = findViewById(R.id.camFrame);

        // Implement searching for an ingredient manually
        mSearchView = findViewById(R.id.searchView);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Check format of query string and send it to
                // image classifier to check against vegetable classes
                if (!query.matches("[a-zA-Z\\s]+")) {
                    // Wrong format of input query
                    Snackbar wrongFormat = Snackbar.make(
                            mCamFrame,
                            "An ingredient should only contain letters and whitespaces.",
                            Snackbar.LENGTH_LONG);
                    wrongFormat.getView().setBackgroundColor(Color.RED);
                    wrongFormat.show();
                    clearSearchView();
                } else {
                    // Make input all lowercase and replace multiple whitespaces with just one
                    query = query.toLowerCase().replaceAll("\\s+", " ");
                    if (validIngredient(query)) {
                        if (!inList(query)) {
                            if (!isPaused)
                                pauseOrResumeCamera();
                            popIngredientDialog(query);
                        } else
                            Toast.makeText(MainActivity.this, "Ingredient already in list", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(MainActivity.this, "Ingredient not found", Toast.LENGTH_SHORT).show();
                    clearSearchView();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Don't want to do anything on text change
                return false;
            }
        });

        final Button mShoppingListButton = findViewById(R.id.shoppingListButton);
        mShoppingListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ShoppingActivity.class);
                startActivity(intent);
            }
        });

        Button mRecipesListButton = findViewById(R.id.recipesListButton);
        mRecipesListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RecipesList.class);
                startActivity(intent);
            }
        });

        mSpinner = findViewById(R.id.spinner);

        Button mCameraButton = findViewById(R.id.cameraButton);
        mCameraButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    // Start scanning on button held
                    canScan = true;
                    mSpinner.setVisibility(View.VISIBLE);
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        // Stop scanning on release of button
                        canScan = false;
                        mSpinner.setVisibility(View.GONE);
                }
                return true;
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
        mSearchView.setQueryHint("Search for ingredient");

        // Create preview and set it as the content of the frame
        mPreview = new CameraPreview(this);

        mCamFrame.addView(mPreview);

        mSearchView.bringToFront();
        mSpinner.bringToFront();
    }

    private void askCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
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

    private boolean validIngredient(final String ingredient) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(this.getAssets().open("labels.mp3")));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.equals(ingredient))
                    return true;
            }
            return false;
        } catch (IOException e) {
            Log.e("FILE", "Asset file not found.");
        }
        return false;
    }


    public void validClassificationFound(final String ingredient) {
        // First check whether camera is paused due to the search bar triggering a pop-up dialog
        // If that's the case, discard the data completely (i.e. don't do anything)
        if (!isPaused) {
            if (!ingredient.equals("NOT FOUND")) {
                // API reply is a valid ingredient
                if (!inList(ingredient)) {
                    // Ingredient isn't already in the list, so we can potentially add it
                    popIngredientDialog(ingredient);

                    // Pause camera on pop-up of ingredient dialog
                    if (!isPaused)
                        pauseOrResumeCamera();
                }
            }
        }
    }

    @Override
    public void onPreviewUpdated(Bitmap data, int width, int height) {
        if (data != null) {
            if (canScan && imageClassifier.canAcceptImage()) {
                imageClassifier.classify(data, getBaseContext(), this);
            }
        }
    }

    public void pauseOrResumeCamera() {
        isPaused = !isPaused;
        mPreview.pauseOrResume(isPaused);
        if (!isPaused) {
            mPreview.resetBuffer();
        }
    }

    private void popIngredientDialog(final String ingredient) {
        AddIngredientDialogFragment dialog = AddIngredientDialogFragment.newInstance(ingredient);

        // Disable possibility of cancelling dialog when touching outside it
        dialog.setCancelable(false);

        // Set listeners for Yes/No buttons
        dialog.setOnYesNoClick(new AddIngredientDialogFragment.OnYesNoClick() {
            @Override
            public void onYesClicked() {
                //Resume camera
                if (isPaused)
                    pauseOrResumeCamera();

                // Add ingredient and refresh the list
                IngredientFragment.scannedIngredients.add(ingredient);
                IngredientFragment.refresh();

                Toast.makeText(MainActivity.this, ingredient + " added", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNoClicked() {
                // Resume camera; nothing else to do on No click
                if (isPaused)
                    pauseOrResumeCamera();
            }
        });
        dialog.show(getFragmentManager(), "New ingredient");
    }

    private void clearSearchView() {
        if (mSearchView != null) {
            mSearchView.setQuery("", false);
            mSearchView.clearFocus();
        } else
            Log.e("SEARCH BAR", "Trying to clear a null search view!");
    }

    private boolean inList(String ingredient) {
        return IngredientFragment.scannedIngredients.contains(ingredient);
    }


    public static void moveSlideUpPanel(int position) {
        mViewPager.setCurrentItem(position);
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
            // Pause camera whenever panel isn't closed
            if (newState == SlidingUpPanelLayout.PanelState.DRAGGING && !isPaused)
                    pauseOrResumeCamera();
            else
                if(newState == SlidingUpPanelLayout.PanelState.COLLAPSED && isPaused)
                    pauseOrResumeCamera();
        }
    }
}


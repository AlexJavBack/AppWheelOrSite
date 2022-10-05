package com.example.appwheelorsite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Random;

public class ActivityGame extends AppCompatActivity {
    private static final String [] sectors = {"60","0","55","10","50","15","45","20","40","25","35","30"};
    private static final int [] sectorDegrees = new int[sectors.length]; // sectors on image
    private static final Random random = new Random();
    private int degree = 0;
    private boolean isSpinning = false;
    private ImageView wheel;
    private TextView textScore;
    private String result;
    private Button button;
    private final String resultKey = "NAME_VARIABLE"; // kay for String result
    RelativeLayout.LayoutParams layoutParamsText; // layout param result
    RelativeLayout.LayoutParams layoutParamsButton; // layout param button

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);


        wheel = findViewById(R.id.imageView);
        button = findViewById(R.id.button);
        textScore = findViewById(R.id.player_score);
        getDegreesForSectors();

    }

    private void getDegreesForSectors() {
        int sectorDegree = 360/sectors.length;

        for(int i = 0; i < sectors.length; i++){
            sectorDegrees[i] = (i+1) * sectorDegree;
        }
    }
    // start spin game
    public void SpinStart (View view){

        if(!isSpinning) {
            spin();
            isSpinning = true;
        }
    }
    // spin wheel
    private void spin() {
        degree = random.nextInt(sectors.length -1);

        RotateAnimation rotateAnimation = new RotateAnimation(0, (360 * sectors.length) +
                sectorDegrees[degree], RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(3600);
        rotateAnimation.setFillAfter(true);
        rotateAnimation.setInterpolator(new DecelerateInterpolator());
        rotateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                result = sectors[sectors.length - (degree + 1)];
                textScore.setText("Your score " + result);
                isSpinning = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        wheel.startAnimation(rotateAnimation);
    }
    // save result text
    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putString(resultKey, result);

        super.onSaveInstanceState(outState);
    }
    // load result text
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        result = savedInstanceState.getString(resultKey);
        textScore.setText("Your score " + result);
    }
    // Changed sizes for elements layout where using manifest - "orientation|keyboard|screenSize"
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            layoutParamsText = (RelativeLayout.LayoutParams) textScore.getLayoutParams();
            layoutParamsText.topMargin = dpToPx(10);
            textScore.setTextSize(24);

            layoutParamsButton  = (RelativeLayout.LayoutParams) button.getLayoutParams();
            layoutParamsButton.topMargin = dpToPx(-10);
            layoutParamsButton.width = dpToPx(195);
            layoutParamsButton.height = dpToPx(48);
        }
        else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            layoutParamsText = (RelativeLayout.LayoutParams) textScore.getLayoutParams();
            layoutParamsText.topMargin = dpToPx(75);
            textScore.setTextSize(48);

            layoutParamsButton  = (RelativeLayout.LayoutParams) button.getLayoutParams();
            layoutParamsButton.topMargin = dpToPx(100);
            layoutParamsButton.width = dpToPx(213);
            layoutParamsButton.height = dpToPx(55);
        }
    }
    // convert pixel in dp for method onConfigurationChanged
    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }
}
package com.gregapp.medcount;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashActivity extends AppCompatActivity {

    private static final int DELAY = 2000;
    private final Handler mHandler = new Handler();

    private MyPreference myPreference;

    private final Runnable mStartActivity = new Runnable() {
        @Override
        public void run() {
            if(myPreference.isAcceptedTerms()){
                if(myPreference.getPattern().isEmpty()){
                    Intent i = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }else{
                    Intent i = new Intent(SplashActivity.this, PatternActivity.class);
                    i.putExtra(PatternActivity.IS_LOGIN, true);
                    startActivity(i);
                    finish();
                }
            }else{
                Intent i = new Intent(SplashActivity.this, TermsActivity.class);
                startActivity(i);
                finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myPreference = new MyPreference(this);

        setContentView(R.layout.activity_splash);

        mHandler.postDelayed(mStartActivity, DELAY);
    }
}

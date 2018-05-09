package com.gregapp.medcount;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;
import com.andrognito.patternlockview.utils.ResourceUtils;

import java.util.List;

public class PatternActivity extends AppCompatActivity {

    public static final String IS_LOGIN = "IS_LOGIN";

    private boolean isLogin;

    private PatternLockView mPatternLockView;
    private MyPreference myPreference;

    private LinearLayout buttonBar;
    private Button btnCancel;
    private Button btnContinue;
    private TextView mInfoLabel;

    private String currentPattern;
    private String newPattern;
    private String confirmPattern;

    private boolean isSetPattern;

    private enum State {
        INIT,
        READY_FOR_CURRENT_PATTERN,
        DRAWN_CURRENT_PATTERN,
        READY_FOR_NEW_PATTERN,
        DRAWN_NEW_PATTERN,
        READY_FOR_CONFIRM_PATTERN,
        DRAWN_CONFIRM_PATTERN
    }

    private State state = State.INIT;

    private PatternLockViewListener mPatternLockViewListener = new PatternLockViewListener() {
        @Override
        public void onStarted() {
            Log.d(getClass().getName(), "Pattern drawing started");
        }

        @Override
        public void onProgress(List<PatternLockView.Dot> progressPattern) {
            Log.d(getClass().getName(), "Pattern progress: " +
                    PatternLockUtils.patternToString(mPatternLockView, progressPattern));
        }

        @Override
        public void onComplete(List<PatternLockView.Dot> pattern) {
            Log.d(getClass().getName(), "Pattern complete: " +
                    PatternLockUtils.patternToString(mPatternLockView, pattern));
            String drawnPattern = PatternLockUtils.patternToString(mPatternLockView, pattern);
            if(state == State.READY_FOR_CURRENT_PATTERN){
                if(drawnPattern.equals(myPreference.getPattern())){
                    if (isLogin){
                            Intent i = new Intent(PatternActivity.this, MainActivity.class);
                            startActivity(i);
                            finish();
                    }else{
                        state = State.READY_FOR_NEW_PATTERN;
                        mInfoLabel.setText(R.string.draw_new_pattern);
                        mPatternLockView.clearPattern();
                        buttonBar.setVisibility(View.VISIBLE);
                        btnContinue.setEnabled(false);
                        btnContinue.setTextColor(getResources().getColor(R.color.disabled));
                    }
                }else{
                    state = State.READY_FOR_CURRENT_PATTERN;
                    mInfoLabel.setText(R.string.try_again);
                    mPatternLockView.setViewMode(PatternLockView.PatternViewMode.WRONG);
//                    mPatternLockView.clearPattern();
                    buttonBar.setVisibility(View.INVISIBLE);
                }
            }else if(state == State.READY_FOR_NEW_PATTERN){
                state = State.DRAWN_NEW_PATTERN;
                mPatternLockView.setInputEnabled(false);
                btnContinue.setEnabled(true);
                btnContinue.setTextColor(getResources().getColor(R.color.gray));
                btnCancel.setText(R.string.pattern_retry);
            }else if(state == State.READY_FOR_CONFIRM_PATTERN){
                confirmPattern = drawnPattern;
                if(newPattern.equals(confirmPattern)){
                    state = State.DRAWN_CONFIRM_PATTERN;
                    mPatternLockView.setInputEnabled(false);
                    btnContinue.setEnabled(true);
                    btnContinue.setTextColor(getResources().getColor(R.color.gray));
                    btnCancel.setText(R.string.pattern_cancel);
                }else{
                    mPatternLockView.setViewMode(PatternLockView.PatternViewMode.WRONG);
//                    mPatternLockView.clearPattern();
                }
            }
        }

        @Override
        public void onCleared() {
            Log.d(getClass().getName(), "Pattern has been cleared");
        }
    };

    private View.OnClickListener btnCancelListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(state == State.DRAWN_NEW_PATTERN){
                state = State.READY_FOR_NEW_PATTERN;
                mPatternLockView.setInputEnabled(true);
                mPatternLockView.clearPattern();
                buttonBar.setVisibility(View.VISIBLE);
                btnContinue.setEnabled(false);
                btnContinue.setTextColor(getResources().getColor(R.color.disabled));
                btnCancel.setText(R.string.pattern_cancel);
            }else{
                finish();
            }
        }
    };

    private View.OnClickListener btnContinueListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(state == State.DRAWN_NEW_PATTERN){
                state = State.READY_FOR_CONFIRM_PATTERN;
                mInfoLabel.setText(R.string.draw_confirm_pattern);
                newPattern = PatternLockUtils.patternToString(mPatternLockView, mPatternLockView.getPattern());
                mPatternLockView.clearPattern();
                mPatternLockView.setInputEnabled(true);
                btnContinue.setEnabled(false);
                btnContinue.setText(R.string.pattern_confirm);
                btnContinue.setTextColor(getResources().getColor(R.color.disabled));
                btnCancel.setText(R.string.pattern_cancel);
            }else if(state == State.DRAWN_CONFIRM_PATTERN){
                myPreference.setPattern(newPattern);
                finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_pattern);

        isLogin = getIntent().getBooleanExtra(IS_LOGIN, false);

        myPreference = new MyPreference(this);

        mPatternLockView = (PatternLockView) findViewById(R.id.patter_lock_view);
        mPatternLockView.setDotCount(3);
        mPatternLockView.setDotNormalSize((int) ResourceUtils.getDimensionInPx(this, R.dimen.pattern_lock_dot_size));
        mPatternLockView.setDotSelectedSize((int) ResourceUtils.getDimensionInPx(this, R.dimen.pattern_lock_dot_selected_size));
        mPatternLockView.setPathWidth((int) ResourceUtils.getDimensionInPx(this, R.dimen.pattern_lock_path_width));
        mPatternLockView.setAspectRatioEnabled(true);
        mPatternLockView.setAspectRatio(PatternLockView.AspectRatio.ASPECT_RATIO_HEIGHT_BIAS);
        mPatternLockView.setViewMode(PatternLockView.PatternViewMode.CORRECT);
        mPatternLockView.setDotAnimationDuration(150);
        mPatternLockView.setPathEndAnimationDuration(100);
        mPatternLockView.setCorrectStateColor(ResourceUtils.getColor(this, R.color.white));
        mPatternLockView.setInStealthMode(false);
        mPatternLockView.setTactileFeedbackEnabled(true);
        mPatternLockView.setInputEnabled(true);
        mPatternLockView.addPatternLockListener(mPatternLockViewListener);

        buttonBar = (LinearLayout) findViewById(R.id.buttonBar);

        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(btnCancelListener);
        btnContinue = (Button) findViewById(R.id.btnContinue);
        btnContinue.setOnClickListener(btnContinueListener);

        mInfoLabel = (TextView) findViewById(R.id.infoLabel);

        currentPattern = myPreference.getPattern();
        isSetPattern = !currentPattern.isEmpty();

        if (isSetPattern){
            buttonBar.setVisibility(View.INVISIBLE);
            this.state = State.READY_FOR_CURRENT_PATTERN;
            if (isLogin){
                mInfoLabel.setText(R.string.draw_unlock_pattern);
            }else{
                mInfoLabel.setText(R.string.draw_current_pattern);
            }
        }else{
            this.state = State.READY_FOR_NEW_PATTERN;
            mInfoLabel.setText(R.string.draw_new_pattern);
            buttonBar.setVisibility(View.VISIBLE);
            btnContinue.setEnabled(false);
            btnContinue.setTextColor(getResources().getColor(R.color.disabled));
        }
    }
}

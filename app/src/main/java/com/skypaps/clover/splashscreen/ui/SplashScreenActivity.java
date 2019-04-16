package com.skypaps.clover.splashscreen.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.skypaps.clover.R;
import com.skypaps.clover.core.ui.AbstractActivity;

public class SplashScreenActivity extends AbstractActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen_activity);
    }
}

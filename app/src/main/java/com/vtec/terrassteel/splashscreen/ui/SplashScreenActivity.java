package com.vtec.terrassteel.splashscreen.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.vtec.terrassteel.BuildConfig;
import com.vtec.terrassteel.R;
import com.vtec.terrassteel.common.utils.CommonUtil;
import com.vtec.terrassteel.core.ui.AbstractActivity;
import com.vtec.terrassteel.home.ui.HomeActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashScreenActivity extends AbstractActivity {

    public static final int DELAY_MILLIS = 1500;

    @BindView(R.id.version_textview)
    TextView versionTextview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTranslucideStatusBar();
        setContentView(R.layout.splashscreen_activity);
        ButterKnife.bind(this);

        versionTextview.setText(getString(R.string.version, BuildConfig.VERSION_NAME));

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (CommonUtil.hasConnectivity(this)) {

            new Handler().postDelayed(
                    this::standardStarting,
                    DELAY_MILLIS
            );

        } else {
            showError(getString(R.string.no_internet_title), getString(R.string.no_internet_message));
        }
    }

    private void standardStarting() {

        //GetConfig
        startActivity(new Intent(this, HomeActivity.class));
        finishAffinity();
    }
}

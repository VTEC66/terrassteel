package com.vtec.terrassteel.common.ui;

import android.content.Intent;
import android.os.Bundle;

import com.google.zxing.Result;
import com.vtec.terrassteel.R;
import com.vtec.terrassteel.core.ui.AbstractActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanActivity extends AbstractActivity implements ZXingScannerView.ResultHandler{

    public static final String CODE = "code";
    public static final int MANUAL = 7;

    @BindView(R.id.zxscan)
    public ZXingScannerView mScannerView;

    @OnClick(R.id.mannual_button)
    public void OnClickManualButton(){
        setResult(MANUAL);
        finish();
    }

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.scan_activity);
        ButterKnife.bind(this);

        mScannerView.setAutoFocus(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {

        Intent intent = new Intent();
        intent.putExtra
                (CODE, rawResult.getText());

        setResult(RESULT_OK, intent);
        finish();
    }
}




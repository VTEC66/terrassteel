package com.vtec.terrassteel.common.ui;

import android.content.Intent;
import android.os.Bundle;

import com.google.zxing.Result;
import com.vtec.terrassteel.core.ui.AbstractActivity;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanActivity extends AbstractActivity implements ZXingScannerView.ResultHandler{

    public static final String CODE = "code";

    private ZXingScannerView mScannerView;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        mScannerView = new ZXingScannerView(this);
        mScannerView.setAutoFocus(true);
        setContentView(mScannerView);
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




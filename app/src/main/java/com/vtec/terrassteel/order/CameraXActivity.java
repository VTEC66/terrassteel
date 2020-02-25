package com.vtec.terrassteel.order;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.Image;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.util.Rational;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.vtec.terrassteel.R;
import com.vtec.terrassteel.common.model.Picture;
import com.vtec.terrassteel.core.model.DefaultResponse;
import com.vtec.terrassteel.core.task.DatabaseOperationCallBack;
import com.vtec.terrassteel.core.ui.AbstractActivity;
import com.vtec.terrassteel.database.DatabaseManager;

import java.io.File;
import java.nio.ByteBuffer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.CameraX;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCapture.OnImageCapturedListener;
import androidx.camera.core.ImageCaptureConfig;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.core.PreviewConfig;
import androidx.lifecycle.LifecycleOwner;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CameraXActivity extends AbstractActivity {

    @BindView(R.id.view_finder)
    TextureView textureView;

    @BindView(R.id.render)
    ImageView previewlayout;

    @BindView(R.id.taken_relativelayout)
    RelativeLayout takenRelativeLayout;

    @BindView(R.id.untaken_relativelayout)
    RelativeLayout untakenRelativeLayout;

    ImageCapture imgCap;

    String tempFileName;

    @OnClick(R.id.take_picture_button)
    void captureImage() {

                String root = Environment.getExternalStorageDirectory().toString() + "/terrassteel/commandes/" + sessionManager.getOrder().getOrderCode() ;

                File myDir = new File(root);
                if (!myDir.exists()) {
                    boolean res = myDir.mkdirs();
                    Log.d("mkdir result :", String.valueOf(res));
                }

                tempFileName = System.currentTimeMillis() +".jpg";
                File file = new File(myDir, tempFileName);

                imgCap.takePicture(file, new ImageCapture.OnImageSavedListener() {
                    @Override
                    public void onImageSaved(@NonNull File file) {

                        Glide.with(getBaseContext())
                                .load(file.getAbsolutePath())
                                .centerCrop()
                                .into(previewlayout);

                        showTakenView();
                    }

                    @Override
                    public void onError(@NonNull ImageCapture.UseCaseError useCaseError, @NonNull String message, @Nullable Throwable cause) {
                        String msg = "Pic capture failed : " + message;
                        Toast.makeText(getBaseContext(), msg,Toast.LENGTH_LONG).show();
                        if(cause != null){
                            cause.printStackTrace();
                        }
                    }
                });

            }



    @OnClick(R.id.retry_button)
    void retry() {

        File file = new File(tempFileName);
        file.delete();
        if(file.exists()){
            getApplicationContext().deleteFile(file.getName());
        }

        showTakingView();
    }


    @OnClick(R.id.send_picture_button)
    void endCapture() {

        if (!TextUtils.isEmpty(tempFileName)) {

            DatabaseManager.getInstance(this).addPicture(
                    new Picture()
                            .withPictureName(tempFileName)
                            .withOrder(sessionManager.getOrder()),
                    new DatabaseOperationCallBack<DefaultResponse>() {
                        @Override
                        public void onSuccess(DefaultResponse defaultResponse) {
                            Intent resultIntent = new Intent();
                            setResult(Activity.RESULT_OK, resultIntent);
                            finish();
                        }
                    }
            );
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_x_activity);

        ButterKnife.bind(this);


        ImageCaptureConfig imageCaptureConfig = new ImageCaptureConfig.Builder().setCaptureMode(ImageCapture.CaptureMode.MIN_LATENCY)
                .setTargetRotation(getWindowManager().getDefaultDisplay().getRotation()).build();
        imgCap = new ImageCapture(imageCaptureConfig);

        setTranslucentStatusBar();
        startCamera();

    }

    private void startCamera() {

        CameraX.unbindAll();


        Rational aspectRatio = new Rational(textureView.getWidth(), textureView.getHeight());
        Size screen = new Size(textureView.getWidth(), textureView.getHeight()); //size of the screen


        PreviewConfig pConfig = new PreviewConfig.Builder().setTargetAspectRatio(aspectRatio).setTargetResolution(screen).build();
        Preview preview = new Preview(pConfig);

        preview.setOnPreviewOutputUpdateListener(
                output -> {
                    ViewGroup parent = (ViewGroup) textureView.getParent();
                    parent.removeView(textureView);
                    parent.addView(textureView, 0);

                    textureView.setSurfaceTexture(output.getSurfaceTexture());
                    updateTransform();
                });

        //bind to lifecycle:
        CameraX.bindToLifecycle((LifecycleOwner)this, preview, imgCap);
    }

    void showTakingView() {
        textureView.setVisibility(View.VISIBLE);
        takenRelativeLayout.setVisibility(View.GONE);
        previewlayout.setVisibility(View.GONE);
        untakenRelativeLayout.setVisibility(View.VISIBLE);
    }

    void showTakenView() {
        takenRelativeLayout.setVisibility(View.VISIBLE);
        previewlayout.setVisibility(View.VISIBLE);
        textureView.setVisibility(View.GONE);
        untakenRelativeLayout.setVisibility(View.GONE);
    }


    private void updateTransform(){
        Matrix mx = new Matrix();
        float w = textureView.getMeasuredWidth();
        float h = textureView.getMeasuredHeight();

        float cX = w / 2f;
        float cY = h / 2f;

        int rotationDgr;
        int rotation = (int)textureView.getRotation();

        switch(rotation){
            case Surface.ROTATION_0:
                rotationDgr = 0;
                break;
            case Surface.ROTATION_90:
                rotationDgr = 90;
                break;
            case Surface.ROTATION_180:
                rotationDgr = 180;
                break;
            case Surface.ROTATION_270:
                rotationDgr = 270;
                break;
            default:
                return;
        }

        mx.postRotate((float)rotationDgr, cX, cY);
        textureView.setTransform(mx);
    }

}

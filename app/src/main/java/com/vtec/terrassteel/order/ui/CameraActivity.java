package com.vtec.terrassteel.order.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vtec.terrassteel.R;
import com.vtec.terrassteel.camera.CameraView;
import com.vtec.terrassteel.common.model.Picture;
import com.vtec.terrassteel.common.utils.BitmapUtil;
import com.vtec.terrassteel.core.model.DefaultResponse;
import com.vtec.terrassteel.core.task.DatabaseOperationCallBack;
import com.vtec.terrassteel.core.ui.AbstractActivity;
import com.vtec.terrassteel.database.DatabaseManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CameraActivity extends AbstractActivity {

    private static final String TAG = CameraActivity.class.getSimpleName();

    @BindView(R.id.camera)
    CameraView cameraView;

    @BindView(R.id.imageView)
    ImageView imageView;

    @BindView(R.id.taken_relativelayout)
    RelativeLayout takenRelativeLayout;

    @BindView(R.id.untaken_relativelayout)
    RelativeLayout untakenRelativeLayout;

    private Bitmap croppedBitmap;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.camera_activity);

        ButterKnife.bind(this);

        setTranslucentStatusBar();

        showTakingView();
    }


    @Override
    public void onResume() {
        super.onResume();

        cameraView.onResume();
    }

    @Override
    public void onPause() {
        cameraView.onPause();
        super.onPause();
    }


    @OnClick(R.id.retry_button)
    void retry() {
        showTakingView();
    }


    @OnClick(R.id.send_picture_button)
    void endCapture() {

        if (croppedBitmap == null) {
            finish();
        }

        String fileName = saveImage(croppedBitmap);

        if (!TextUtils.isEmpty(fileName)) {

            DatabaseManager.getInstance(this).addPicture(
                    new Picture()
                            .withPictureName(fileName)
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

    @OnClick(R.id.take_picture_button)
    void captureImage() {
        if (cameraView != null) {
            cameraView.captureImage((view, photo) -> runOnUiThread(() -> {
                croppedBitmap = BitmapUtil.scaleCenterCrop(BitmapFactory.decodeByteArray(photo, 0, photo.length), cameraView.getHeight(), cameraView.getWidth());
                imageView.setImageBitmap(croppedBitmap);
            }));
            showTakenView();
        }
    }

    void showTakingView() {
        cameraView.setVisibility(View.VISIBLE);
        takenRelativeLayout.setVisibility(View.GONE);
        imageView.setVisibility(View.GONE);
        untakenRelativeLayout.setVisibility(View.VISIBLE);
    }

    void showTakenView() {
        takenRelativeLayout.setVisibility(View.VISIBLE);
        imageView.setVisibility(View.VISIBLE);
        cameraView.setVisibility(View.GONE);
        untakenRelativeLayout.setVisibility(View.GONE);
    }

    private String saveImage(Bitmap finalBitmap) {

        String root = Environment.getExternalStorageDirectory().toString() + "/terrassteel/commandes/" + sessionManager.getOrder().getOrderCode() ;

        File myDir = new File(root);
        if (!myDir.exists()) {
            boolean res = myDir.mkdirs();
            Log.d("mkdir result :", String.valueOf(res));
        }

        String fileName = System.currentTimeMillis() +".jpg";
        File file = new File(myDir, fileName);

        if (file.exists())
            file.delete();
        try (FileOutputStream out = new FileOutputStream(file)) {
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 30, out);
            out.flush();
            out.close();
            return fileName;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}

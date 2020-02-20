package com.vtec.terrassteel.order.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.vtec.terrassteel.R;
import com.vtec.terrassteel.common.listener.ActionBarListener;
import com.vtec.terrassteel.common.listener.ConfirmationDialogCallback;
import com.vtec.terrassteel.common.model.OrderStatus;
import com.vtec.terrassteel.common.model.Picture;
import com.vtec.terrassteel.common.ui.ActionBar;
import com.vtec.terrassteel.common.ui.ConfirmationDialog;
import com.vtec.terrassteel.core.model.DefaultResponse;
import com.vtec.terrassteel.core.task.DatabaseOperationCallBack;
import com.vtec.terrassteel.core.ui.AbstractActivity;
import com.vtec.terrassteel.database.DatabaseManager;

import java.io.File;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;

public class PicturePreviewActivity extends AbstractActivity {

    private static final String PICTURE = "PICTURE";
    private static final String CONFIRMATION_DIALOG = "CONFIRMATION_DIALOG";
    public static final int PICTURE_DELETED = 55;

    @BindView(R.id.action_bar)
    ActionBar actionBar;

    @BindView(R.id.picture_preview)
    ImageView picturePreview;

    Picture picture;

    public static Intent newIntent(Context context, Picture picture) {
        Intent intent = new Intent(context, PicturePreviewActivity.class);

        Bundle bundle = new Bundle();
        bundle.putSerializable(PICTURE, picture);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.picture_preview_layout);
        ButterKnife.bind(this);

        actionBar.setListener(new ActionBarListener() {
            @Override
            public void onBackArrowClick() {
                onBackPressed();
            }

            @Override
            public void onActionButtonClick() {
                new ConfirmationDialog()
                        .setConfirmationMessage(getString(R.string.delete_picture_confirmation_message))
                        .setCallBack(new ConfirmationDialogCallback() {
                            @Override
                            public void onConfirm() {
                                DatabaseManager.getInstance(getBaseContext()).deletePicture(picture, new DatabaseOperationCallBack<DefaultResponse>() {
                                    @Override
                                    public void onSuccess(DefaultResponse defaultResponse) {

                                        File file = new File(getCompletePath());
                                        file.delete();
                                        if(file.exists()){
                                            getApplicationContext().deleteFile(file.getName());
                                        }

                                        setResult(PICTURE_DELETED);
                                        finish();
                                    }
                                });
                            }
                        })
                        .show(getSupportFragmentManager(), CONFIRMATION_DIALOG);
            }
        });

        if(sessionManager.getOrder().getStatus().equals(OrderStatus.FINISHED)){
            actionBar.showActionButton(false);
        }else{
            actionBar.showActionButton(true);
        }


        if (getIntent() != null && getIntent().hasExtra(PICTURE)) {
            Bundle bundle = getIntent().getExtras();
            picture = (Picture) bundle.getSerializable(PICTURE);
        }

        File file = new File(getCompletePath());
        Uri imageUri = Uri.fromFile(file);

        Glide.with(this)
                .load(imageUri)
                .centerCrop()
                .into(picturePreview);
    }

    private String getCompletePath() {
        return Environment.getExternalStorageDirectory().toString() + "/terrassteel/commandes/" + sessionManager.getOrder().getOrderCode() + "-" + sessionManager.getOrder().getCustomer() + "/" + picture.getPictureName();
    }
}

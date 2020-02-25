package com.vtec.terrassteel.order.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

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
import com.vtec.terrassteel.order.CameraXActivity;
import com.vtec.terrassteel.order.adapter.PictureGridAdapter;
import com.vtec.terrassteel.order.callback.PictureOrderCallback;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OrderMainActivity  extends AbstractActivity implements PictureOrderCallback {

    private static final String CONFIRMATION_DIALOG = "CONFIRMATION_DIALOG";
    private static final int TAKE_PICTURE_REQUEST_CODE = 12;
    private static final int SHOW_PICTURE = 13;

    @BindView(R.id.picture_order_listview)
    RecyclerView pictureOrderGridView;

    @BindView(R.id.action_bar)
    ActionBar actionBar;

    @BindView(R.id.picture_order_view)
    View pictureOrderView;

    @BindView(R.id.empty_view)
    View emptyView;

    @BindView(R.id.add_fab)
    View addFab;


    @OnClick({R.id.add_fab, R.id.add_construction_button})
    public void onClicAddButton(){
        if (getMissingPermissions().size() > 0) {
            askPermission(getMissingPermissions());
        } else {
            startCameraActivity();
        }
    }

    PictureGridAdapter pictureGridAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.order_main_activity);
        ButterKnife.bind(this);

        actionBar.setTitle(sessionManager.getOrder().getOrderCode());
        actionBar.setListener(new ActionBarListener() {
            @Override
            public void onBackArrowClick() {
                onBackPressed();
            }

            @Override
            public void onActionButtonClick() {
                new ConfirmationDialog()
                        .setConfirmationMessage(getString(R.string.order_close_confirmation_message))
                        .setCallBack(new ConfirmationDialogCallback() {
                            @Override
                            public void onConfirm() {
                                DatabaseManager.getInstance(getBaseContext()).closeOrder(sessionManager.getOrder(), new DatabaseOperationCallBack<DefaultResponse>() {
                                    @Override
                                    public void onSuccess(DefaultResponse defaultResponse) {
                                        sessionManager.getOrder().withStatus(OrderStatus.FINISHED);
                                        onResume();
                                    }
                                });
                            }
                        })
                        .show(getSupportFragmentManager(), CONFIRMATION_DIALOG);
            }
        });

        pictureOrderGridView.setLayoutManager(new GridLayoutManager(this, 3));
        pictureOrderGridView.setHasFixedSize(true);

        pictureGridAdapter = new PictureGridAdapter(this, sessionManager.getOrder());
        pictureGridAdapter.setCallback(this);

        pictureOrderGridView.setAdapter(pictureGridAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        DatabaseManager.getInstance(this).getAllPictureForOrder(sessionManager.getOrder(), new DatabaseOperationCallBack<ArrayList<Picture>>() {
            @Override
            public void onSuccess(ArrayList<Picture> pictures) {
                setupVisibility(pictures.isEmpty());
                pictureGridAdapter.setData(pictures);
                pictureGridAdapter.notifyDataSetChanged();
            }
        });


        if(sessionManager.getOrder().getStatus().equals(OrderStatus.FINISHED)){
            addFab.setVisibility(View.GONE);
            actionBar.showActionButton(false);
        }else{
            addFab.setVisibility(View.VISIBLE);
            actionBar.showActionButton(true);
        }
    }

    private void setupVisibility(boolean isDataEmpty) {
        if(isDataEmpty){
            emptyView.setVisibility(View.VISIBLE);
            pictureOrderView.setVisibility(View.GONE);
        }else{
            emptyView.setVisibility(View.GONE);
            pictureOrderView.setVisibility(View.VISIBLE);
        }
    }


    protected List<String> getMissingPermissions() {
        List<String> manifestPermissions = new ArrayList<>();

        if (Build.VERSION.SDK_INT < 23) {
            return manifestPermissions;
        }

        if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            manifestPermissions.add(Manifest.permission.CAMERA);
        }

        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            manifestPermissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        return manifestPermissions;
    }

    protected void askPermission(List<String> missingPermissions) {
        if (Build.VERSION.SDK_INT >= 23 && missingPermissions.size() > 0) {

            List<String> requestPermissions = new ArrayList<>(missingPermissions);

            if (requestPermissions.size() > 0) {
                requestPermissions(requestPermissions.toArray(new String[0]), AbstractActivity.PERMISSION_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            int approvedPermissions = 0;

            for (int i = 0; i < permissions.length; i++) {

                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    approvedPermissions = approvedPermissions + 1;
                }
            }

            if (approvedPermissions == permissions.length) {
                startCameraActivity();
            }
        }
    }

    private void startCameraActivity() {
        startActivityForResult(new Intent(this, CameraXActivity.class), TAKE_PICTURE_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == TAKE_PICTURE_REQUEST_CODE) {
            switch (resultCode) {
                case RESULT_OK:
                    onResume();
                    break;
            }
        }

        if(requestCode == SHOW_PICTURE) {
            switch (resultCode) {
                case PicturePreviewActivity.PICTURE_DELETED:
                    onResume();
                    break;
            }
        }
    }

    @Override
    public void onPictureSelected(Picture picture) {
        startActivityForResult(PicturePreviewActivity.newIntent(this, picture), SHOW_PICTURE);
    }
}

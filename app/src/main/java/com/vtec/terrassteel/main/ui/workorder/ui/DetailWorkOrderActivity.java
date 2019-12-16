package com.vtec.terrassteel.main.ui.workorder.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.vtec.terrassteel.BuildConfig;
import com.vtec.terrassteel.R;
import com.vtec.terrassteel.common.listener.ActionBarListener;
import com.vtec.terrassteel.common.listener.ConfirmationDialogCallback;
import com.vtec.terrassteel.common.model.WorkOrder;
import com.vtec.terrassteel.common.model.WorkOrderStatus;
import com.vtec.terrassteel.common.ui.ActionBar;
import com.vtec.terrassteel.common.ui.ConfirmationDialog;
import com.vtec.terrassteel.common.utils.CsvUtil;
import com.vtec.terrassteel.core.model.DefaultResponse;
import com.vtec.terrassteel.core.task.DatabaseOperationCallBack;
import com.vtec.terrassteel.core.ui.AbstractActivity;
import com.vtec.terrassteel.database.DatabaseManager;
import com.vtec.terrassteel.main.ui.assign.ui.AddAssignActivity;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.vtec.terrassteel.main.ui.workorder.ui.WorkOrderFragment.EXTRA_WORK_ORDER;


public class DetailWorkOrderActivity extends AbstractActivity {

    private static final String CONFIRMATION_DIALOG = "CONFIRMATION_DIALOG";

    WorkOrder workOrder;

    @BindView(R.id.action_bar)
    ActionBar actionBar;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.status_indicator_tv)
    TextView statusIndicatorTextView;

    @BindView(R.id.status_indicator_container)
    View statusIndicatorContainer;

    @BindView(R.id.assign_access)
    View assignAccessView;

    @BindView(R.id.unavailability_indicator)
    View unavailableIndicatorView;

    @BindView(R.id.reference_affaire_tv)
    TextView referenceAffaireTV;

    @BindView(R.id.affected_time_textview)
    TextView affectedTimeTextview;

    @BindView(R.id.consumed_time_textview)
    TextView consumedTimeTextview;

    @BindView(R.id.close_workorder_button)
    View closeWorkOrderButton;

    @OnClick(R.id.assign_management_view)
    public void clicShowAssign(){

        if(workOrder.getWorkOrderStatus().equals(WorkOrderStatus.FINISHED)){
            return;
        }

        Intent intent = new Intent(this, AddAssignActivity.class);

        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_WORK_ORDER, workOrder);
        intent.putExtras(bundle);

        startActivity(intent);
    }

    @OnClick(R.id.imputation_management_view)
    public void clicShowImputation(){
        Intent intent = new Intent(this, ListImputationActivity.class);

        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_WORK_ORDER, workOrder);
        intent.putExtras(bundle);

        startActivity(intent);

    }

    @OnClick(R.id.close_workorder_button)
    public void clicCloseWorkorder() {

                new ConfirmationDialog()
                        .setConfirmationMessage(getString(R.string.workorder_close_confirmation_message))
                        .setCallBack(new ConfirmationDialogCallback() {
                            @Override
                            public void onConfirm() {
                                DatabaseManager.getInstance(getBaseContext()).closeWorkOrder(workOrder, new DatabaseOperationCallBack<WorkOrder>() {
                                    @Override
                                    public void onSuccess(WorkOrder defaultResponse) {
                                        onResume();
                                    }
                                });
                            }
                        })
                        .show(getSupportFragmentManager(), CONFIRMATION_DIALOG);

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.detail_workorder_activity);
        ButterKnife.bind(this);

        if (getIntent() != null && getIntent().hasExtra(EXTRA_WORK_ORDER)) {
            Bundle bundle = getIntent().getExtras();

            workOrder = (WorkOrder) bundle.getSerializable(EXTRA_WORK_ORDER);
        }

        actionBar.setListener(new ActionBarListener() {
            @Override
            public void onBackArrowClick() {
                finish();
            }

            @Override
            public void onActionButtonClick() {
                if (getMissingPermissions().size() > 0) {
                    askPermission(getMissingPermissions());
                } else {
                    makeCSV();
                }
            }
        });

        actionBar.setTitle(getString(R.string.work_order, workOrder.getWorkOrderReference()));

    }

    private void makeCSV() {
        new CsvUtil().makeCSV(getBaseContext(), workOrder);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(workOrder.getWorkOrderStatus() == WorkOrderStatus.FINISHED){
            closeWorkOrderButton.setVisibility(View.GONE);
        }else{
            closeWorkOrderButton.setVisibility(View.VISIBLE);
        }

        int affected = workOrder.getWorkOrderAllocatedHour();
        int consummed = DatabaseManager.getInstance(this).getConsumedTimeForWorkOrder(workOrder);

        affectedTimeTextview.setText(String.valueOf(affected)+ "h");
        consumedTimeTextview.setText(String.valueOf(consummed/3600)+ "h");

        progressBar.setMax(affected*60);
        progressBar.setProgress(consummed/60);

        statusIndicatorTextView.setText(workOrder.getWorkOrderStatus().getRessourceReference());
        referenceAffaireTV.setText(workOrder.getWorkOrderReference());

        switch (workOrder.getWorkOrderStatus()){
            case IN_PROGRESS:
                statusIndicatorContainer.setBackground(getResources().getDrawable(R.drawable.bg_status_indicator_inprogress));
                unavailableIndicatorView.setVisibility(View.GONE);
                assignAccessView.setVisibility(View.VISIBLE);
                break;
            case FINISHED:
                statusIndicatorContainer.setBackground(getResources().getDrawable(R.drawable.bg_status_indicator_finished));
                unavailableIndicatorView.setVisibility(View.VISIBLE);
                assignAccessView.setVisibility(View.GONE);
                break;
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
                makeCSV();
            }
        }
    }

    protected void askPermission(List<String> missingPermissions) {
        if (Build.VERSION.SDK_INT >= 23 && missingPermissions.size() > 0) {

            List<String> requestPermissions = new ArrayList<>(missingPermissions);

            if (requestPermissions.size() > 0) {
                requestPermissions(requestPermissions.toArray(new String[0]), AbstractActivity.PERMISSION_REQUEST_CODE);
            }
        }
    }

    protected List<String> getMissingPermissions() {
        List<String> manifestPermissions = new ArrayList<>();

        if (Build.VERSION.SDK_INT < 23) {
            return manifestPermissions;
        }

        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            manifestPermissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        return manifestPermissions;
    }

}

package com.vtec.terrassteel.construction.ui.workorder.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;

import com.vtec.terrassteel.R;
import com.vtec.terrassteel.common.model.WorkOrder;
import com.vtec.terrassteel.common.model.WorkOrderStatus;
import com.vtec.terrassteel.construction.ui.ConstructionMainActivity;
import com.vtec.terrassteel.core.model.DefaultResponse;
import com.vtec.terrassteel.core.task.DatabaseOperationCallBack;
import com.vtec.terrassteel.core.ui.AbstractActivity;
import com.vtec.terrassteel.database.DatabaseManager;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.vtec.terrassteel.core.Const.NO_ERROR_CODE;
import static com.vtec.terrassteel.core.Const.VIBRATION_DURATION;

public class AddWorkOrderActivity extends AbstractActivity {


    private static final int ERROR_EMPTY_WORK_ORDER_REFERENCE_FIELD = 2;
    private static final int ERROR_EMPTY_AFFAIRE_REFERENCE_FIELD = 3;
    private static final int ERROR_EMPTY_WORK_ORDER_HOUR_FIELD = 5;

    @OnClick(R.id.validate_button)
    public void onClickValidateButton(){

        clearHighlightErrors();

        int error = controlField();

        if (error == NO_ERROR_CODE) {
            addNewWorkOrder();
        } else {

            Vibrator vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);

            if (vibrator != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(VIBRATION_DURATION, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    vibrator.vibrate(VIBRATION_DURATION);
                }
            }
            highlightError(error);
        }
    }
    @BindView(R.id.workorder_reference_textinputlayout)
    View workOrderReferenceTil;

    @BindView(R.id.workorder_reference_edittext)
    EditText workOrderReferenceEdittext;

    @BindView(R.id.workorder_affaire_textinputlayout)
    View workOrderAffaireTil;

    @BindView(R.id.workorder_affaire_edittext)
    EditText workOrderAffaireEdittext;

    @BindView(R.id.workorder_hours_textinputlayout)
    View workOrderHoursTil;

    @BindView(R.id.workorder_hours_edittext)
    EditText workOrderHoursEdittext;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.add_workorder_activity);

        ButterKnife.bind(this);
    }

    private int controlField() {
        if (workOrderReferenceEdittext.getText().length() == 0) {
            return ERROR_EMPTY_WORK_ORDER_REFERENCE_FIELD;
        }

        if (workOrderAffaireEdittext.getText().length() == 0) {
            return ERROR_EMPTY_AFFAIRE_REFERENCE_FIELD;
        }

        if (workOrderHoursEdittext.getText().length() == 0) {
            return ERROR_EMPTY_WORK_ORDER_HOUR_FIELD;
        }

        return NO_ERROR_CODE;
    }

    private void highlightError(int error) {
        switch (error) {
            case ERROR_EMPTY_WORK_ORDER_REFERENCE_FIELD:
                workOrderReferenceTil.startAnimation(AnimationUtils.loadAnimation(this,R.anim.shake));
                workOrderReferenceEdittext.setError(getString(R.string.standard_mandatory_field_message));
                break;
            case ERROR_EMPTY_AFFAIRE_REFERENCE_FIELD:
                workOrderAffaireTil.startAnimation(AnimationUtils.loadAnimation(this,R.anim.shake));
                workOrderAffaireEdittext.setError(getString(R.string.customer_mandatory_field_message));
                break;
            case ERROR_EMPTY_WORK_ORDER_HOUR_FIELD:
                workOrderHoursTil.startAnimation(AnimationUtils.loadAnimation(this,R.anim.shake));
                workOrderHoursEdittext.setError(getString(R.string.customer_mandatory_field_message));
                break;
        }
    }

    private void clearHighlightErrors() {
        workOrderReferenceEdittext.setError(null);
        workOrderAffaireEdittext.setError(null);
        workOrderHoursEdittext.setError(null);
    }

    private void addNewWorkOrder() {
        WorkOrder newWorkOrder = new WorkOrder()
                .withWorkOrderReference(workOrderReferenceEdittext.getText().toString())
                .withWorkOrderAffaire(workOrderAffaireEdittext.getText().toString())
                .withWorkOrderAllocatedHour(Integer.parseInt(workOrderHoursEdittext.getText().toString()))
                .withworkOrderStatus(WorkOrderStatus.IN_PROGRESS)
                .withConstruction(sessionManager.getContruction());


        DatabaseManager.getInstance(getApplicationContext()).addWorkOrder(newWorkOrder, new DatabaseOperationCallBack<DefaultResponse>() {

            @Override
            public void onSuccess(DefaultResponse defaultResponse) {
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onError() {
                super.onError();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, ConstructionMainActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }
}

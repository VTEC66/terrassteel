package com.vtec.terrassteel.home.order.ui;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;

import com.vtec.terrassteel.R;
import com.vtec.terrassteel.common.listener.ActionBarListener;
import com.vtec.terrassteel.common.model.Construction;
import com.vtec.terrassteel.common.model.ConstructionStatus;
import com.vtec.terrassteel.common.model.Order;
import com.vtec.terrassteel.common.model.OrderStatus;
import com.vtec.terrassteel.common.ui.ActionBar;
import com.vtec.terrassteel.core.model.DefaultResponse;
import com.vtec.terrassteel.core.task.DatabaseOperationCallBack;
import com.vtec.terrassteel.core.ui.AbstractActivity;
import com.vtec.terrassteel.database.DatabaseManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.vtec.terrassteel.core.Const.NO_ERROR_CODE;
import static com.vtec.terrassteel.core.Const.VIBRATION_DURATION;

public class AddOrderActivity extends AbstractActivity {

    private static final int ERROR_EMPTY_CODE_ORDER_FIELD = 1;
    private static final int ERROR_EMPTY_CUSTOMER_NAME_FIELD = 2;

    @BindView(R.id.action_bar)
    ActionBar actionBar;

    @BindView(R.id.ordercode_name_til)
    View codeOrderTIL;

    @BindView(R.id.ordercode_edittext)
    EditText codeOrderEditText;

    @BindView(R.id.customer_name_til)
    View customerNameTil;

    @BindView(R.id.customer_name_edittext)
    EditText customerNameEditText;

    @OnClick(R.id.validate_button)
    public void onClickValidateButton(){

        clearHighlightErrors();
        int error = controlField();

        if (error == NO_ERROR_CODE) {
            addNewOrder();
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.add_order_activity);
        ButterKnife.bind(this);


        actionBar.setListener(new ActionBarListener() {
            @Override
            public void onBackArrowClick() {
                onBackPressed();
            }

            @Override
            public void onActionButtonClick() { }
        });
    }

    private void addNewOrder() {
        Order newOrder = new Order()
                .withOrderCode(codeOrderEditText.getText().toString())
                .withCustomer(customerNameEditText.getText().toString())
                .withStatus(OrderStatus.IN_PROGRESS);

        DatabaseManager.getInstance(getApplicationContext()).addOrder(newOrder, new DatabaseOperationCallBack<DefaultResponse>() {

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

    private void highlightError(int error) {
        switch (error) {
            case ERROR_EMPTY_CODE_ORDER_FIELD:
                codeOrderTIL.startAnimation(AnimationUtils.loadAnimation(this,R.anim.shake));
                codeOrderEditText.setError(getString(R.string.standard_mandatory_field_message));
                break;
            case ERROR_EMPTY_CUSTOMER_NAME_FIELD:
                customerNameTil.startAnimation(AnimationUtils.loadAnimation(this,R.anim.shake));
                customerNameEditText.setError(getString(R.string.standard_mandatory_field_message));
                break;

        }
    }

    private int controlField() {
        if (codeOrderEditText.getText().length() == 0) {
            return ERROR_EMPTY_CODE_ORDER_FIELD;
        }

        if (customerNameEditText.getText().length() == 0) {
            return ERROR_EMPTY_CUSTOMER_NAME_FIELD;
        }

        return NO_ERROR_CODE;
    }

    private void clearHighlightErrors() {
        codeOrderEditText.setError(null);
        customerNameEditText.setError(null);
    }
}

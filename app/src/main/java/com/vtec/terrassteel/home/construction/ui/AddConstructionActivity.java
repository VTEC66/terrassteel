package com.vtec.terrassteel.home.construction.ui;

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
import com.vtec.terrassteel.common.listener.ActionBarListener;
import com.vtec.terrassteel.common.model.Construction;
import com.vtec.terrassteel.common.model.ConstructionStatus;
import com.vtec.terrassteel.common.model.Customer;
import com.vtec.terrassteel.common.ui.ActionBar;
import com.vtec.terrassteel.database.DatabaseManager;
import com.vtec.terrassteel.core.model.DefaultResponse;
import com.vtec.terrassteel.core.task.DatabaseOperationCallBack;
import com.vtec.terrassteel.core.ui.AbstractActivity;
import com.vtec.terrassteel.home.company.customer.ui.EditCustomerActivity;
import com.vtec.terrassteel.home.company.customer.ui.SelectCustomerDialogFragment;
import com.vtec.terrassteel.home.construction.callback.SelectCustomerDialogCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.vtec.terrassteel.core.Const.NO_ERROR_CODE;
import static com.vtec.terrassteel.core.Const.VIBRATION_DURATION;

public class AddConstructionActivity extends AbstractActivity implements SelectCustomerDialogCallback {


    private static final String SELECT_CUSTOMER_DIALOG = "SELECT_CUSTOMER_DIALOG" ;

    private static final int ERROR_EMPTY_CONSTRUCTION_NAME_FIELD = 1;
    private static final int ERROR_EMPTY_CUSTOMER_FIELD = 2;

    private static final int ADD_CUSTOMER_INTENT_CODE = 17;

    private Customer selectedCustomer;

    @OnClick(R.id.validate_button)
    public void onClickValidateButton(){

        clearHighlightErrors();

        int error = controlField();

        if (error == NO_ERROR_CODE) {
            addNewConstruction();
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

    @BindView(R.id.action_bar)
    ActionBar actionBar;

    @BindView(R.id.construction_name_til)
    View constructionNameTIL;

    @BindView(R.id.construction_name_edittext)
    EditText constructionNameEditText;

    @BindView(R.id.customer_edittext)
    EditText customerEditText;

    @BindView(R.id.customer_textinputlayout)
    View customerTIL;

    @BindView(R.id.construction_address1_edittext)
    EditText constructionAddress1EditText;

    @BindView(R.id.construction_address2_edittext)
    EditText constructionAddress2EditText;

    @BindView(R.id.construction_zip_edittext)
    EditText constructionZipEditText;

    @BindView(R.id.construction_city_edittext)
    EditText constructionCityEditText;

    final SelectCustomerDialogFragment selectCustomerDialogFragment =
            new SelectCustomerDialogFragment()
                    .setCallBack(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.add_construction_activity);
        ButterKnife.bind(this);


        actionBar.setListener(new ActionBarListener() {
            @Override
            public void onBackArrowClick() {
                finish();
            }

            @Override
            public void onActionButtonClick() { }
        });

        customerEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                customerEditText.setCursorVisible(true);
                if(hasFocus) {
                    selectCustomerDialogFragment.show(getSupportFragmentManager(), SELECT_CUSTOMER_DIALOG);
                }
            }
        });
    }



    private void addNewConstruction() {
        Construction newConstruction = new Construction()
                .withConstructionName(constructionNameEditText.getText().toString())
                .withConstructionAddress1(constructionAddress1EditText.getText().toString())
                .withConstructionAddress2(constructionAddress2EditText.getText().toString())
                .withConstructionZip(constructionZipEditText.getText().toString())
                .withConstructionCity(constructionCityEditText.getText().toString())
                .withConstructionStatus(ConstructionStatus.IN_PROGRESS);

        DatabaseManager.getInstance(getApplicationContext()).addConstruction(newConstruction, new DatabaseOperationCallBack<DefaultResponse>() {

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
    public void onCustomerSelected(Customer customer) {
        this.selectedCustomer = customer;
        customerEditText.setText(customer.getCustomerName());
        selectCustomerDialogFragment.dismiss();
        customerEditText.clearFocus();
    }

    @Override
    public void onSelectAddCustomer() {
        startActivityForResult(new Intent(this, EditCustomerActivity.class), ADD_CUSTOMER_INTENT_CODE);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        customerEditText.clearFocus();
    }

    private int controlField() {
        if (constructionNameEditText.getText().length() == 0) {
            return ERROR_EMPTY_CONSTRUCTION_NAME_FIELD;
        }

        if (customerEditText.getText().length() == 0) {
            return ERROR_EMPTY_CUSTOMER_FIELD;
        }

        return NO_ERROR_CODE;
    }

    private void highlightError(int error) {
        switch (error) {
            case ERROR_EMPTY_CONSTRUCTION_NAME_FIELD:
                constructionNameTIL.startAnimation(AnimationUtils.loadAnimation(this,R.anim.shake));
                constructionNameEditText.setError(getString(R.string.standard_mandatory_field_message));
                break;
            case ERROR_EMPTY_CUSTOMER_FIELD:
                customerTIL.startAnimation(AnimationUtils.loadAnimation(this,R.anim.shake));
                customerEditText.setError(getString(R.string.customer_mandatory_field_message));
                break;
        }
    }

    private void clearHighlightErrors() {
        constructionNameEditText.setError(null);
        customerEditText.setError(null);
    }

}

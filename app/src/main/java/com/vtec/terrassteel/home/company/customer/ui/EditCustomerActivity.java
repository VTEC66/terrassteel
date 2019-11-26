package com.vtec.terrassteel.home.company.customer.ui;

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
import com.vtec.terrassteel.common.model.Customer;
import com.vtec.terrassteel.common.ui.ActionBar;
import com.vtec.terrassteel.database.DatabaseManager;
import com.vtec.terrassteel.core.model.DefaultResponse;
import com.vtec.terrassteel.core.task.DatabaseOperationCallBack;
import com.vtec.terrassteel.core.ui.AbstractActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.vtec.terrassteel.core.Const.NO_ERROR_CODE;
import static com.vtec.terrassteel.core.Const.VIBRATION_DURATION;

public class EditCustomerActivity extends AbstractActivity {

    private static final int ERROR_EMPTY_CUSTOMER_NAME_FIELD = 2;

    public static final String EXTRA_CUSTOMER = "EXTRA_CUSTOMER";
    public static final String SERIALIZED_CUSTOMER = "SERIALIZED_CUSTOMER";

    private boolean isEditMode = false;

    @OnClick(R.id.validate_button)
    public void onClickValidateButton(){

        clearHighlightErrors();

        int error = controlField();

        if (error == NO_ERROR_CODE) {
            if(isEditMode){
                editCustomer();
            }else{
                addNewCustomer();
            }
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

    @BindView(R.id.customer_name_til)
    View customerNameTIL;

    @BindView(R.id.customer_name_edittext)
    EditText customerNameEditText;

    @BindView(R.id.customer_address1_edittext)
    EditText customerAddress1EditText;

    @BindView(R.id.customer_address2_edittext)
    EditText customerAddress2EditText;

    @BindView(R.id.customer_zip_edittext)
    EditText customerZipEditText;

    @BindView(R.id.customer_city_edittext)
    EditText customerCityEditText;

    @BindView(R.id.customer_phone_edittext)
    EditText customerPhoneEditText;

    @BindView(R.id.customer_email_edittext)
    EditText customerEmailEditText;

    Customer customerToEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.add_customer_activity);
        ButterKnife.bind(this);

        if (getIntent() != null && getIntent().hasExtra(EXTRA_CUSTOMER)) {
            Bundle bundle = getIntent().getExtras();
            customerToEdit = (Customer) bundle.getSerializable(EXTRA_CUSTOMER);
            if(customerToEdit != null) {
                setupEditMode();
            }
        }

        actionBar.setListener(new ActionBarListener() {
            @Override
            public void onBackArrowClick() {
                finish();
            }

            @Override
            public void onActionButtonClick() { }
        });
    }

    private void setupEditMode() {

        actionBar.setTitle(getString(R.string.edit_customer_title));

        customerNameEditText.setText(customerToEdit.getCustomerName());
        customerAddress1EditText.setText(customerToEdit.customerAddress1);
        customerAddress2EditText.setText(customerToEdit.customerAddress2);
        customerZipEditText.setText(customerToEdit.customerZip);
        customerCityEditText.setText(customerToEdit.customerCity);
        customerPhoneEditText.setText(customerToEdit.customerPhone);
        customerEmailEditText.setText(customerToEdit.customerEmail);

        this.isEditMode = true;
    }


    private int controlField() {

        if (customerNameEditText.getText().length() == 0) {
            return ERROR_EMPTY_CUSTOMER_NAME_FIELD;
        }

        return NO_ERROR_CODE;
    }

    private void highlightError(int error) {
        switch (error) {
            case ERROR_EMPTY_CUSTOMER_NAME_FIELD:
                customerNameTIL.startAnimation(AnimationUtils.loadAnimation(this,R.anim.shake));
                customerNameEditText.setError(getString(R.string.standard_mandatory_field_message));
                break;
        }
    }

    private void clearHighlightErrors() {
        customerNameEditText.setError(null);
    }

    private void addNewCustomer() {
        Customer newCustomer = new Customer()
                .withCustomerName(customerNameEditText.getText().toString())
                .withCustomerAddress1(customerAddress1EditText.getText().toString())
                .withCustomerAddress2(customerAddress2EditText.getText().toString())
                .withCustomerZip(customerZipEditText.getText().toString())
                .withCustomerCity(customerCityEditText.getText().toString())
                .withCustomerPhone(customerPhoneEditText.getText().toString())
                .withCustomerEmail(customerEmailEditText.getText().toString());

        DatabaseManager.getInstance(getApplicationContext()).addCustomer(newCustomer, new DatabaseOperationCallBack<DefaultResponse>() {

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

    private void editCustomer() {

        customerToEdit
                .withCustomerName(customerNameEditText.getText().toString())
                .withCustomerAddress1(customerAddress1EditText.getText().toString())
                .withCustomerAddress2(customerAddress2EditText.getText().toString())
                .withCustomerZip(customerZipEditText.getText().toString())
                .withCustomerCity(customerCityEditText.getText().toString())
                .withCustomerPhone(customerPhoneEditText.getText().toString())
                .withCustomerEmail(customerEmailEditText.getText().toString());

        DatabaseManager.getInstance(getApplicationContext()).editCustomer(customerToEdit, new DatabaseOperationCallBack<DefaultResponse>() {

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
}

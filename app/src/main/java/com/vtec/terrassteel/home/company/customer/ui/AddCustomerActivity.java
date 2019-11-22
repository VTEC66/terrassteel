package com.vtec.terrassteel.home.company.customer.ui;

import android.os.Bundle;
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

public class AddCustomerActivity extends AbstractActivity {

    @OnClick(R.id.validate_button)
    public void onClickValidateButton(){
        addNewCustomer();
    }

    @BindView(R.id.action_bar)
    ActionBar actionBar;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.add_customer_activity);
        ButterKnife.bind(this);


        actionBar.setListener(new ActionBarListener() {
            @Override
            public void onBackArrowClick() {
                finish();
            }

            @Override
            public void onActionButtonClick() { }
        });
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
}

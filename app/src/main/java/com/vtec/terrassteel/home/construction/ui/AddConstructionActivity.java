package com.vtec.terrassteel.home.construction.ui;

import android.os.Bundle;
import android.view.View;
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
import com.vtec.terrassteel.home.company.customer.ui.SelectCustomerDialogFragment;
import com.vtec.terrassteel.home.company.employee.ui.SelectJobDialogFragment;
import com.vtec.terrassteel.home.construction.callback.SelectCustomerCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddConstructionActivity extends AbstractActivity implements SelectCustomerCallback {


    private static final String SELECT_CUSTOMER_DIALOG = "SELECT_CUSTOMER_DIALOG" ;

    private Customer selectedCustomer;

    @OnClick(R.id.validate_button)
    public void onClickValidateButton(){
        addNewConstruction();
    }

    @BindView(R.id.action_bar)
    ActionBar actionBar;

    @BindView(R.id.construction_name_edittext)
    EditText constructionNameEditText;

    @BindView(R.id.customer_edittext)
    EditText customerEditText;

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
                .withCustomer(selectedCustomer)
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
}

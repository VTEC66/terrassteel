package com.vtec.terrassteel.home.company.employee.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;
import com.vtec.terrassteel.R;
import com.vtec.terrassteel.common.listener.ActionBarListener;
import com.vtec.terrassteel.common.model.Employee;
import com.vtec.terrassteel.common.model.Job;
import com.vtec.terrassteel.common.ui.ActionBar;
import com.vtec.terrassteel.core.model.DefaultResponse;
import com.vtec.terrassteel.core.task.DatabaseOperationCallBack;
import com.vtec.terrassteel.core.ui.AbstractActivity;
import com.vtec.terrassteel.database.DatabaseManager;
import com.vtec.terrassteel.home.company.employee.callback.SelectJobCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddEmployeeActivity extends AbstractActivity implements SelectJobCallback {

    private static final String SELECT_JOB_DIALOG = "SELECT_JOB_DIALOG";

    @OnClick(R.id.validate_button)
    public void onClickValidateButton(){
        addNewEmployee();
    }


    @BindView(R.id.action_bar)
    ActionBar actionBar;

    @BindView(R.id.employee_name_edittext)
    EditText employeeNameEditText;

    @BindView(R.id.employee_job_til)
    TextInputLayout employeeJobTIL;

    @BindView(R.id.employee_job_edittext)
    EditText employeeJobEditText;

    @BindView(R.id.employee_address1_edittext)
    EditText employeeAddress1EditText;

    @BindView(R.id.employee_address2_edittext)
    EditText employeeAddress2EditText;

    @BindView(R.id.employee_zip_edittext)
    EditText employeeZipEditText;

    @BindView(R.id.employee_city_edittext)
    EditText employeeCityEditText;

    @BindView(R.id.employee_phone_edittext)
    EditText employeePhoneEditText;

    @BindView(R.id.employee_email_edittext)
    EditText employeeEmailEditText;

    private Job selectedJob;

    final SelectJobDialogFragment selectJobDialogFragment =
            new SelectJobDialogFragment()
                .setCallBack(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.add_employee_activity);
        ButterKnife.bind(this);

        actionBar.setListener(new ActionBarListener() {
            @Override
            public void onBackArrowClick() {
                finish();
            }

            @Override
            public void onActionButtonClick() { }
        });

        employeeJobEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                employeeJobEditText.setCursorVisible(true);
                if(hasFocus) {
                    selectJobDialogFragment.show(getSupportFragmentManager(), SELECT_JOB_DIALOG);
                }
            }
        });
    }


    private void addNewEmployee() {

        Employee newEmployee = new Employee()
                .withEmployeeName(employeeNameEditText.getText().toString())
                .withEmployeeJob(selectedJob)
                .withEmployeeAddress1(employeeAddress1EditText.getText().toString())
                .withEmployeeAddress2(employeeAddress2EditText.getText().toString())
                .withEmployeeZip(employeeZipEditText.getText().toString())
                .withEmployeeCity(employeeCityEditText.getText().toString())
                .withEmployeePhone(employeePhoneEditText.getText().toString())
                .withEmployeeEmail(employeeEmailEditText.getText().toString());

        DatabaseManager.getInstance(getApplicationContext()).addEmployee(newEmployee, new DatabaseOperationCallBack<DefaultResponse>() {

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
    public void onJobSelected(Job job) {

        selectedJob = job;
        employeeJobEditText.setText(job.getRessourceReference());
        selectJobDialogFragment.dismiss();
        employeeJobEditText.clearFocus();


    }
}

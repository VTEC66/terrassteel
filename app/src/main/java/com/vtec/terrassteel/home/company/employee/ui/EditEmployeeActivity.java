package com.vtec.terrassteel.home.company.employee.ui;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;
import com.vtec.terrassteel.R;
import com.vtec.terrassteel.common.listener.ActionBarListener;
import com.vtec.terrassteel.common.model.Customer;
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

import static com.vtec.terrassteel.core.Const.NO_ERROR_CODE;
import static com.vtec.terrassteel.core.Const.VIBRATION_DURATION;

public class EditEmployeeActivity extends AbstractActivity implements SelectJobCallback {

    private static final String SELECT_JOB_DIALOG = "SELECT_JOB_DIALOG";

    public static final String EXTRA_EMPLOYEE = "EXTRA_EMPLOYEE";

    private static final int ERROR_EMPTY_EMPLOYEE_NAME_FIELD = 2;
    private static final int ERROR_EMPTY_JOB_FIELD = 3;
    private static final int ERROR_EMPTY_EMPLOYEE_CODE_FIELD = 4;


    private Employee employeeToEdit;

    private boolean isEditMode;

    @OnClick(R.id.validate_button)
    public void onClickValidateButton() {

        clearHighlightErrors();

        int error = controlField();

        if (error == NO_ERROR_CODE) {
            if(isEditMode){
                editEmployee();
            }else{
                addNewEmployee();
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

    @BindView(R.id.employee_name_til)
    View employeeNameTIL;

    @BindView(R.id.employee_name_edittext)
    EditText employeeNameEditText;

    @BindView(R.id.employee_job_til)
    TextInputLayout employeeJobTIL;

    @BindView(R.id.employee_job_edittext)
    EditText employeeJobEditText;

    @BindView(R.id.employee_code_til)
    TextInputLayout employeeCodeTIL;

    @BindView(R.id.employee_code_edittext)
    EditText employeeCodeEditText;

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

        if (getIntent() != null && getIntent().hasExtra(EXTRA_EMPLOYEE)) {
            Bundle bundle = getIntent().getExtras();
            employeeToEdit = (Employee) bundle.getSerializable(EXTRA_EMPLOYEE);
            if(employeeToEdit != null) {
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

    private void setupEditMode() {

        actionBar.setTitle(getString(R.string.edit_employee_title));

        employeeNameEditText.setText(employeeToEdit.getEmployeeName());
        employeeJobEditText.setText(employeeToEdit.getEmployeeJob().getRessourceReference());
        employeeCodeEditText.setText(employeeToEdit.getEmployeeCode());
        employeeAddress1EditText.setText(employeeToEdit.getEmployeeAddress1());
        employeeAddress2EditText.setText(employeeToEdit.getEmployeeAddress2());
        employeeZipEditText.setText(employeeToEdit.getEmployeeZip());
        employeeCityEditText.setText(employeeToEdit.getEmployeeCity());
        employeePhoneEditText.setText(employeeToEdit.getEmployeePhone());
        employeeEmailEditText.setText(employeeToEdit.getEmployeeEmail());

        this.isEditMode = true;
    }

    @Override
    public void onJobSelected(Job job) {

        selectedJob = job;
        employeeJobEditText.setText(job.getRessourceReference());
        selectJobDialogFragment.dismiss();
        employeeJobEditText.clearFocus();
    }

    private int controlField() {

        if (employeeNameEditText.getText().length() == 0) {
            return ERROR_EMPTY_EMPLOYEE_NAME_FIELD;
        }

        if (employeeJobEditText.getText().length() == 0) {
            return ERROR_EMPTY_JOB_FIELD;
        }

        if (employeeCodeEditText.getText().length() == 0) {
            return ERROR_EMPTY_EMPLOYEE_CODE_FIELD;
        }

        return NO_ERROR_CODE;
    }

    private void highlightError(int error) {
        switch (error) {
            case ERROR_EMPTY_EMPLOYEE_NAME_FIELD:
                employeeNameTIL.startAnimation(AnimationUtils.loadAnimation(this,R.anim.shake));
                employeeNameEditText.setError(getString(R.string.standard_mandatory_field_message));
                break;
            case ERROR_EMPTY_JOB_FIELD:
                employeeJobTIL.startAnimation(AnimationUtils.loadAnimation(this,R.anim.shake));
                employeeJobEditText.setError(getString(R.string.customer_mandatory_field_message));
                break;
            case ERROR_EMPTY_EMPLOYEE_CODE_FIELD:
                employeeCodeTIL.startAnimation(AnimationUtils.loadAnimation(this,R.anim.shake));
                employeeCodeEditText.setError(getString(R.string.standard_mandatory_field_message));
                break;
        }
    }

    private void clearHighlightErrors() {
        employeeNameEditText.setError(null);
        employeeJobEditText.setError(null);
        employeeCityEditText.setError(null);
    }


    private void addNewEmployee() {

        Employee newEmployee = new Employee()
                .withEmployeeName(employeeNameEditText.getText().toString())
                .withEmployeeJob(selectedJob)
                .withEmployeeCode(employeeCodeEditText.getText().toString())
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

    private void editEmployee() {

        employeeToEdit
                .withEmployeeName(employeeNameEditText.getText().toString())
                .withEmployeeJob( (selectedJob!=null) ? selectedJob : employeeToEdit.getEmployeeJob())
                .withEmployeeCode(employeeCodeEditText.getText().toString())
                .withEmployeeAddress1(employeeAddress1EditText.getText().toString())
                .withEmployeeAddress2(employeeAddress2EditText.getText().toString())
                .withEmployeeZip(employeeZipEditText.getText().toString())
                .withEmployeeCity(employeeCityEditText.getText().toString())
                .withEmployeePhone(employeePhoneEditText.getText().toString())
                .withEmployeeEmail(employeeEmailEditText.getText().toString());

        DatabaseManager.getInstance(getApplicationContext()).editEmployee(employeeToEdit, new DatabaseOperationCallBack<DefaultResponse>() {

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

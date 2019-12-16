package com.vtec.terrassteel.home.construction.ui;

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

public class AddConstructionActivity extends AbstractActivity  {


    private static final String SELECT_CUSTOMER_DIALOG = "SELECT_CUSTOMER_DIALOG" ;

    private static final int ERROR_EMPTY_CONSTRUCTION_NAME_FIELD = 1;
    private static final int ERROR_EMPTY_CUSTOMER_NAME_FIELD = 2;

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

    @BindView(R.id.customer_name_til)
    View customerNameTil;

    @BindView(R.id.customer_name_edittext)
    EditText customerNameEditText;




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

    }



    private void addNewConstruction() {
        Construction newConstruction = new Construction()
                .withConstructionName(constructionNameEditText.getText().toString())
                .withCustomer(customerNameEditText.getText().toString())
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





    private int controlField() {
        if (constructionNameEditText.getText().length() == 0) {
            return ERROR_EMPTY_CONSTRUCTION_NAME_FIELD;
        }

        if (customerNameEditText.getText().length() == 0) {
            return ERROR_EMPTY_CUSTOMER_NAME_FIELD;
        }



        return NO_ERROR_CODE;
    }

    private void highlightError(int error) {
        switch (error) {
            case ERROR_EMPTY_CONSTRUCTION_NAME_FIELD:
                constructionNameTIL.startAnimation(AnimationUtils.loadAnimation(this,R.anim.shake));
                constructionNameEditText.setError(getString(R.string.standard_mandatory_field_message));
                break;
            case ERROR_EMPTY_CUSTOMER_NAME_FIELD:
                customerNameTil.startAnimation(AnimationUtils.loadAnimation(this,R.anim.shake));
                customerNameEditText.setError(getString(R.string.standard_mandatory_field_message));
                break;

        }
    }

    private void clearHighlightErrors() {

        constructionNameEditText.setError(null);
        customerNameEditText.setError(null);
    }

}

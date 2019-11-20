package com.vtec.terrassteel.home.construction.ui;

import android.os.Bundle;
import android.widget.EditText;

import com.vtec.terrassteel.R;
import com.vtec.terrassteel.common.listener.ActionBarListener;
import com.vtec.terrassteel.common.model.Construction;
import com.vtec.terrassteel.common.model.ConstructionStatus;
import com.vtec.terrassteel.common.ui.ActionBar;
import com.vtec.terrassteel.core.manager.DatabaseManager;
import com.vtec.terrassteel.core.model.DefaultResponse;
import com.vtec.terrassteel.core.task.DatabaseOperationCallBack;
import com.vtec.terrassteel.core.ui.AbstractActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddConstructionActivity extends AbstractActivity {


    @OnClick(R.id.validate_button)
    public void onClickValidateButton(){
        addNewConstruction();
    }

    @BindView(R.id.action_bar)
    ActionBar actionBar;

    @BindView(R.id.construction_name_edittext)
    EditText constructionNameEditText;

    @BindView(R.id.construction_address1_edittext)
    EditText constructionAddress1EditText;

    @BindView(R.id.construction_address2_edittext)
    EditText constructionAddress2EditText;

    @BindView(R.id.construction_zip_edittext)
    EditText constructionZipEditText;

    @BindView(R.id.construction_city_edittext)
    EditText constructionCityEditText;


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

}

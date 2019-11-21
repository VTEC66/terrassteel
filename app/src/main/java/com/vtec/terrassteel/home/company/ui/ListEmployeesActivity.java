package com.vtec.terrassteel.home.company.ui;

import android.os.Bundle;

import com.vtec.terrassteel.R;
import com.vtec.terrassteel.common.listener.ActionBarListener;
import com.vtec.terrassteel.common.ui.ActionBar;
import com.vtec.terrassteel.core.ui.AbstractActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListEmployeesActivity extends AbstractActivity {

    @BindView(R.id.action_bar)
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.list_employees_activity);
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
}

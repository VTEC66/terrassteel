package com.vtec.terrassteel.home.construction.ui;

import android.os.Bundle;

import com.vtec.terrassteel.R;
import com.vtec.terrassteel.core.ui.AbstractActivity;

import butterknife.ButterKnife;

public class AddConstructionActivity extends AbstractActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.add_construction_activity);
        ButterKnife.bind(this);


    }

}

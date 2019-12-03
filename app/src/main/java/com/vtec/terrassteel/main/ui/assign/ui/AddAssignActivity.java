package com.vtec.terrassteel.main.ui.assign.ui;

import android.os.Bundle;

import com.vtec.terrassteel.R;
import com.vtec.terrassteel.common.listener.ActionBarListener;
import com.vtec.terrassteel.common.model.Customer;
import com.vtec.terrassteel.common.model.WorkOrder;
import com.vtec.terrassteel.common.ui.ActionBar;
import com.vtec.terrassteel.core.ui.AbstractActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.vtec.terrassteel.main.ui.pointing.ui.PointingTimeFragment.EXTRA_WORK_ORDER;

public class AddAssignActivity extends AbstractActivity {

    WorkOrder workOrder;


    @BindView(R.id.action_bar)
    ActionBar actionBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.add_assign_activity);
        ButterKnife.bind(this);

        if (getIntent() != null && getIntent().hasExtra(EXTRA_WORK_ORDER)) {
            Bundle bundle = getIntent().getExtras();

            workOrder = (WorkOrder) bundle.getSerializable(EXTRA_WORK_ORDER);
        }

        actionBar.setListener(new ActionBarListener() {
            @Override
            public void onBackArrowClick() {
                finish();
            }

            @Override
            public void onActionButtonClick() { }
        });

        actionBar.setTitle(workOrder.getWorkOrderReference());
    }
}

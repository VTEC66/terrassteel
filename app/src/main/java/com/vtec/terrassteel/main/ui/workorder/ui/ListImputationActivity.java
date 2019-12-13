package com.vtec.terrassteel.main.ui.workorder.ui;

import android.os.Bundle;

import com.vtec.terrassteel.R;
import com.vtec.terrassteel.common.listener.ActionBarListener;
import com.vtec.terrassteel.common.model.Employee;
import com.vtec.terrassteel.common.model.Pointing;
import com.vtec.terrassteel.common.model.WorkOrder;
import com.vtec.terrassteel.common.ui.ActionBar;
import com.vtec.terrassteel.core.task.DatabaseOperationCallBack;
import com.vtec.terrassteel.core.ui.AbstractActivity;
import com.vtec.terrassteel.database.DatabaseManager;
import com.vtec.terrassteel.home.company.employee.adapter.EmployeesAdapter;
import com.vtec.terrassteel.main.ui.workorder.adapter.ImputationAdapter;

import java.util.ArrayList;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.vtec.terrassteel.main.ui.pointing.ui.PointingTimeFragment.EXTRA_WORK_ORDER;

public class ListImputationActivity extends AbstractActivity {

    WorkOrder workOrder;

    @BindView(R.id.action_bar)
    ActionBar actionBar;

    @BindView(R.id.imputation_listview)
    RecyclerView imputationrecyclerView;

    private ImputationAdapter imputationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.list_imputation_activity);
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
            public void onActionButtonClick() {
            }
        });

        actionBar.setTitle(workOrder.getWorkOrderReference());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);

        imputationrecyclerView.setLayoutManager(linearLayoutManager);

        imputationAdapter = new ImputationAdapter();

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(imputationrecyclerView.getContext(),
                linearLayoutManager.getOrientation());

        imputationrecyclerView.addItemDecoration(dividerItemDecoration);
        imputationrecyclerView.setAdapter(imputationAdapter);

    }

    @Override
    public void onResume() {
        super.onResume();

        DatabaseManager.getInstance(this).getImputationsForWorkorder(workOrder, new DatabaseOperationCallBack<ArrayList<Pointing>>() {
            @Override
            public void onSuccess(ArrayList<Pointing> imputations) {
                imputationAdapter.setData(imputations);
                imputationAdapter.notifyDataSetChanged();
            }
        });

    }
}

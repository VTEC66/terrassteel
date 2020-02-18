package com.vtec.terrassteel.construction.ui.workorder.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.vtec.terrassteel.R;
import com.vtec.terrassteel.common.listener.ActionBarListener;
import com.vtec.terrassteel.common.model.Imputation;
import com.vtec.terrassteel.common.model.WorkOrder;
import com.vtec.terrassteel.common.ui.ActionBar;
import com.vtec.terrassteel.core.task.DatabaseOperationCallBack;
import com.vtec.terrassteel.core.ui.AbstractActivity;
import com.vtec.terrassteel.database.DatabaseManager;
import com.vtec.terrassteel.construction.ui.workorder.adapter.ImputationAdapter;

import java.util.ArrayList;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.vtec.terrassteel.construction.ui.imputation.ui.ImputationFragment.EXTRA_WORK_ORDER;


public class ListImputationActivity extends AbstractActivity {

    WorkOrder workOrder;

    @BindView(R.id.action_bar)
    ActionBar actionBar;

    @BindView(R.id.empty_view)
    View emptyView;

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
                onBackPressed();
            }

            @Override
            public void onActionButtonClick() {
            }
        });

        actionBar.setTitle(getString(R.string.work_order, workOrder.getWorkOrderReference()));

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

        DatabaseManager.getInstance(this).getImputationsForWorkorder(workOrder, new DatabaseOperationCallBack<ArrayList<Imputation>>() {
            @Override
            public void onSuccess(ArrayList<Imputation> imputations) {
                imputationAdapter.setData(imputations);
                imputationAdapter.notifyDataSetChanged();

                setupVisibility(imputations.isEmpty());
            }
        });
    }

    private void setupVisibility(boolean isDataEmpty) {

        if (isDataEmpty) {
            emptyView.setVisibility(View.VISIBLE);
            imputationrecyclerView.setVisibility(View.GONE);
        } else {
            emptyView.setVisibility(View.GONE);
            imputationrecyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, DetailWorkOrderActivity.class);

        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_WORK_ORDER, workOrder);
        intent.putExtras(bundle);

        startActivity(intent);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);


        finishAffinity();
    }
}

package com.vtec.terrassteel.main.ui.workorder.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vtec.terrassteel.R;
import com.vtec.terrassteel.common.model.WorkOrder;
import com.vtec.terrassteel.core.task.DatabaseOperationCallBack;
import com.vtec.terrassteel.core.ui.AbstractFragment;
import com.vtec.terrassteel.database.DatabaseManager;
import com.vtec.terrassteel.home.construction.adapter.ConstructionAdapter;
import com.vtec.terrassteel.main.ui.workorder.adapter.WorkorderAdapter;
import com.vtec.terrassteel.main.ui.workorder.callback.WorkorderCallback;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WorkOrderFragment extends AbstractFragment implements WorkorderCallback {

    private static  final int ADD_CUSTOMER_INTENT_CODE = 19;

    public static final String TAG = WorkOrderFragment.class.getSimpleName();

    @BindView(R.id.workorder_listview)
    RecyclerView workorderRecyclerView;

    @BindView(R.id.workorder_view)
    View workorderView;

    @BindView(R.id.empty_view)
    View emptyView;

    private WorkorderAdapter workorderAdapter;


    @OnClick({R.id.add_workorder_button, R.id.add_fab})
    public void onClickAddWorkOrder(){
        startActivityForResult(new Intent(getContext(), AddWorkOrderActivity.class), ADD_CUSTOMER_INTENT_CODE);
        getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View thisView = inflater.inflate(R.layout.working_order_fragment, container, false);

        ButterKnife.bind(this, thisView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);

        workorderRecyclerView.setLayoutManager(linearLayoutManager);

        workorderAdapter = new WorkorderAdapter(getContext());
        workorderAdapter.setCallback(this);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(workorderRecyclerView.getContext(),
                linearLayoutManager.getOrientation());

        workorderRecyclerView.addItemDecoration(dividerItemDecoration);
        workorderRecyclerView.setAdapter(workorderAdapter);

        return thisView;
    }

    @Override
    public void onResume() {
        super.onResume();

        DatabaseManager.getInstance(getContext()).getAllWorkOrderForConstruction(sessionManager.getContruction(), new DatabaseOperationCallBack<ArrayList<WorkOrder>>() {
            @Override
            public void onSuccess(ArrayList<WorkOrder> workOrders) {

                setupVisibility(workOrders.isEmpty());
                workorderAdapter.setData(workOrders);
                workorderAdapter.notifyDataSetChanged();

            }

            @Override
            public void onError() {
                super.onError();
            }
        });
    }

    private void setupVisibility(boolean isDataEmpty) {
        if(isDataEmpty){
            emptyView.setVisibility(View.VISIBLE);
            workorderView.setVisibility(View.GONE);
        }else{
            emptyView.setVisibility(View.GONE);
            workorderView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onWorkOrderSelected(WorkOrder workOrder) {

    }
}

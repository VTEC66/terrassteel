package com.vtec.terrassteel.main.ui.pointing.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vtec.terrassteel.R;
import com.vtec.terrassteel.common.model.Assign;
import com.vtec.terrassteel.common.model.Employee;
import com.vtec.terrassteel.common.model.WorkOrder;
import com.vtec.terrassteel.core.task.DatabaseOperationCallBack;
import com.vtec.terrassteel.core.ui.AbstractFragment;
import com.vtec.terrassteel.database.DatabaseManager;
import com.vtec.terrassteel.home.company.customer.ui.EditCustomerActivity;
import com.vtec.terrassteel.main.ui.assign.ui.AddAssignActivity;
import com.vtec.terrassteel.main.ui.pointing.adapter.PointingAdapter;
import com.vtec.terrassteel.main.ui.pointing.callback.PointingCallback;
import com.vtec.terrassteel.main.ui.workorder.adapter.WorkorderAdapter;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.vtec.terrassteel.home.company.customer.ui.EditCustomerActivity.EXTRA_CUSTOMER;

public class PointingTimeFragment extends AbstractFragment implements PointingCallback {

    public static final String TAG = PointingTimeFragment.class.getSimpleName();
    public static final String EXTRA_WORK_ORDER = "EXTRA_WORK_ORDER";
    private static final int ADD_ASSIGN_INTENT_CODE = 20;

    @BindView(R.id.pointing_time_listview)
    RecyclerView pointingTimeRecyclerView;

    @BindView(R.id.pointing_time_view)
    View pointingView;

    @BindView(R.id.empty_view)
    View emptyView;

    private PointingAdapter pointingAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View thisView = inflater.inflate(R.layout.pointing_time_fragment, container, false);

        ButterKnife.bind(this, thisView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);

        pointingTimeRecyclerView.setLayoutManager(linearLayoutManager);

        pointingAdapter = new PointingAdapter(getContext());
        pointingAdapter.setCallback(this);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(pointingTimeRecyclerView.getContext(),
                linearLayoutManager.getOrientation());

        pointingTimeRecyclerView.addItemDecoration(dividerItemDecoration);
        pointingTimeRecyclerView.setAdapter(pointingAdapter);


        return thisView;
    }

    @Override
    public void onResume() {
        super.onResume();

        DatabaseManager.getInstance(getContext()).getAllAssign(new DatabaseOperationCallBack<ArrayList<Assign>>() {
            @Override
            public void onSuccess(ArrayList<Assign> assigns) {

                        setupVisibility(assigns.isEmpty());
                        pointingAdapter.setData(assigns);
                        pointingAdapter.notifyDataSetChanged();
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
            pointingView.setVisibility(View.GONE);
        }else{
            emptyView.setVisibility(View.GONE);
            pointingView.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void actionPointing() {
        //TODO
    }

    @Override
    public void addAssignToWorkOrder(WorkOrder workOrder) {
        Intent intent = new Intent(getContext(), AddAssignActivity.class);

        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_WORK_ORDER, workOrder);
        intent.putExtras(bundle);

        startActivityForResult(intent, ADD_ASSIGN_INTENT_CODE);
        getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}

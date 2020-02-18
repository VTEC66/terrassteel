package com.vtec.terrassteel.construction.ui.imputation.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vtec.terrassteel.R;
import com.vtec.terrassteel.common.listener.ActionBarListener;
import com.vtec.terrassteel.common.model.Assign;
import com.vtec.terrassteel.common.model.WorkOrder;
import com.vtec.terrassteel.common.ui.ActionBar;
import com.vtec.terrassteel.core.model.DefaultResponse;
import com.vtec.terrassteel.core.task.DatabaseOperationCallBack;
import com.vtec.terrassteel.core.ui.AbstractFragment;
import com.vtec.terrassteel.database.DatabaseManager;
import com.vtec.terrassteel.construction.ui.assign.ui.AddAssignActivity;
import com.vtec.terrassteel.construction.ui.imputation.adapter.ImputationAdapter;
import com.vtec.terrassteel.construction.ui.imputation.callback.ImputationCallback;
import com.vtec.terrassteel.construction.ui.workorder.ui.DetailWorkOrderActivity;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;


public class ImputationFragment extends AbstractFragment implements ImputationCallback {

    public static final String TAG = ImputationFragment.class.getSimpleName();
    public static final String EXTRA_WORK_ORDER = "EXTRA_WORK_ORDER";
    private static final int ADD_ASSIGN_INTENT_CODE = 20;

    @BindView(R.id.imputation_time_listview)
    RecyclerView imputationTimeRecyclerView;

    @BindView(R.id.action_bar)
    ActionBar actionBar;

    @BindView(R.id.imputation_time_view)
    View imputationView;

    @BindView(R.id.empty_view)
    View emptyView;

    private ImputationAdapter imputationAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View thisView = inflater.inflate(R.layout.imputation_fragment, container, false);

        ButterKnife.bind(this, thisView);

        actionBar.setListener(new ActionBarListener() {
            @Override
            public void onBackArrowClick() {

            }

            @Override
            public void onActionButtonClick() {
                DatabaseManager.getInstance(getContext()).stopAllActiveImputation(sessionManager.getContruction(),
                new DatabaseOperationCallBack<DefaultResponse>() {
                    @Override
                    public void onSuccess(DefaultResponse defaultResponse) {
                       onResume();
                    }
                });
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);

        imputationTimeRecyclerView.setLayoutManager(linearLayoutManager);

        imputationAdapter = new ImputationAdapter(getContext());
        imputationAdapter.setCallback(this);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(imputationTimeRecyclerView.getContext(),
                linearLayoutManager.getOrientation());

        imputationTimeRecyclerView.addItemDecoration(dividerItemDecoration);
        imputationTimeRecyclerView.setAdapter(imputationAdapter);


        return thisView;
    }

    @Override
    public void onResume() {
        super.onResume();

        DatabaseManager.getInstance(getContext()).getAllAssignForConstruction(sessionManager.getContruction(), new DatabaseOperationCallBack<ArrayList<Assign>>() {
            @Override
            public void onSuccess(ArrayList<Assign> assigns) {

                        setupVisibility(assigns.isEmpty());
                        imputationAdapter.setData(assigns);
                        imputationAdapter.notifyDataSetChanged();
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
            imputationView.setVisibility(View.GONE);
        }else{
            emptyView.setVisibility(View.GONE);
            imputationView.setVisibility(View.VISIBLE);
        }
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

    @Override
    public void workOrderSelected(WorkOrder workOrder) {

        Intent intent = new Intent(getContext(), DetailWorkOrderActivity.class);

        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_WORK_ORDER, workOrder);
        intent.putExtras(bundle);

        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}

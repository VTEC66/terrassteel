package com.vtec.terrassteel.main.ui.workorder.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vtec.terrassteel.R;
import com.vtec.terrassteel.common.model.WorkOrder;
import com.vtec.terrassteel.common.model.WorkOrderStatus;
import com.vtec.terrassteel.common.ui.ScanActivity;
import com.vtec.terrassteel.core.model.DefaultResponse;
import com.vtec.terrassteel.core.task.DatabaseOperationCallBack;
import com.vtec.terrassteel.core.ui.AbstractFragment;
import com.vtec.terrassteel.database.DatabaseManager;
import com.vtec.terrassteel.main.ui.workorder.adapter.WorkorderAdapter;
import com.vtec.terrassteel.main.ui.workorder.callback.WorkorderCallback;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;
import static com.vtec.terrassteel.common.ui.ScanActivity.CODE;
import static com.vtec.terrassteel.core.ui.AbstractActivity.PERMISSION_REQUEST_CODE;

public class WorkOrderFragment extends AbstractFragment implements WorkorderCallback {

    private static  final int ADD_CUSTOMER_INTENT_CODE = 19;
    private static final int SCAN_QR_REQUEST_CODE = 23;

    public static final String EXTRA_WORK_ORDER = "EXTRA_WORK_ORDER";

    public static final String TAG = WorkOrderFragment.class.getSimpleName();

    @BindView(R.id.workorder_listview)
    RecyclerView workorderRecyclerView;

    @BindView(R.id.workorder_view)
    View workorderView;

    @BindView(R.id.empty_view)
    View emptyView;

    private WorkorderAdapter workorderAdapter;


    /*@OnClick({R.id.add_workorder_button, R.id.add_fab})
    public void onClickAddWorkOrder(){
        startActivityForResult(new Intent(getContext(), AddWorkOrderActivity.class), ADD_CUSTOMER_INTENT_CODE);
        getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }*/

    @OnClick({R.id.add_workorder_button, R.id.add_fab})
    public void onClickScanButton(){
        if (getMissingPermissions().size() > 0) {
            askPermission(getMissingPermissions());
        } else {
            startScanActivity();
        }
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
        Intent intent = new Intent(getContext(), DetailWorkOrderActivity.class);

        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_WORK_ORDER, workOrder);
        intent.putExtras(bundle);

        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    protected List<String> getMissingPermissions() {
        List<String> manifestPermissions = new ArrayList<>();

        if (Build.VERSION.SDK_INT < 23) {
            return manifestPermissions;
        }

        if (getCloverActivity().checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            manifestPermissions.add(Manifest.permission.CAMERA);
        }

        return manifestPermissions;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SCAN_QR_REQUEST_CODE &&
                resultCode == RESULT_OK) {
            if(!TextUtils.isEmpty(data.getStringExtra(CODE))){
                String extractedData = data.getStringExtra(CODE);
                String[] parts = extractedData.replace("\"","").trim().split(";");

                if(parts.length!=3){
                    showError(getString(R.string.qr_wo_notrecognized));
                }else{
                    WorkOrder newWorkOrder = new WorkOrder()
                            .withConstruction(sessionManager.getContruction())
                            .withworkOrderStatus(WorkOrderStatus.IN_PROGRESS)
                            .withWorkOrderReference(parts[1])
                            .withWorkOrderAffaire(parts[0])
                            .withWorkOrderAllocatedHour(Integer.parseInt(parts[2]));

                    DatabaseManager.getInstance(getContext())
                            .addWorkOrder(newWorkOrder, new DatabaseOperationCallBack<DefaultResponse>() {
                                @Override
                                public void onSuccess(DefaultResponse defaultResponse) {
                                    onResume();
                                }
                            });
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            int approvedPermissions = 0;

            for (int i = 0; i < permissions.length; i++) {

                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    approvedPermissions = approvedPermissions + 1;
                }
            }

            if (approvedPermissions == permissions.length) {
                startScanActivity();
            }
        }
    }

    private void startScanActivity() {
        startActivityForResult(new Intent(getActivity(), ScanActivity.class), SCAN_QR_REQUEST_CODE);
    }

}

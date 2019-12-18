package com.vtec.terrassteel.home.construction.ui;

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
import com.vtec.terrassteel.common.model.Construction;
import com.vtec.terrassteel.common.model.ConstructionStatus;
import com.vtec.terrassteel.common.model.WorkOrder;
import com.vtec.terrassteel.common.model.WorkOrderStatus;
import com.vtec.terrassteel.common.ui.ScanActivity;
import com.vtec.terrassteel.core.model.DefaultResponse;
import com.vtec.terrassteel.database.DatabaseManager;
import com.vtec.terrassteel.core.task.DatabaseOperationCallBack;
import com.vtec.terrassteel.core.ui.AbstractFragment;
import com.vtec.terrassteel.home.construction.adapter.ConstructionAdapter;
import com.vtec.terrassteel.home.construction.callback.ConstructionCallback;
import com.vtec.terrassteel.main.ui.MainActivity;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;
import static com.vtec.terrassteel.common.ui.ScanActivity.CODE;
import static com.vtec.terrassteel.common.ui.ScanActivity.MANUAL;
import static com.vtec.terrassteel.core.ui.AbstractActivity.PERMISSION_REQUEST_CODE;

public class MyConstructionsFragment extends AbstractFragment implements ConstructionCallback {

    private static final int SCAN_QR_REQUEST_CODE = 23;
    private static final int ADD_MANUAL_CONSTRUCTION_REQUEST_CODE = 24;

    @BindView(R.id.construction_listview)
    RecyclerView constructionRecyclerView;

    @BindView(R.id.construction_view)
    View constructionView;

    @BindView(R.id.empty_view)
    View emptyView;

    @OnClick({R.id.add_fab, R.id.add_construction_button})
    public void onClicAddButton(){
        if (getMissingPermissions().size() > 0) {
            askPermission(getMissingPermissions());
        } else {
            startScanActivity();
        }
    }

    private ConstructionAdapter constructionAdapter;

    public static Fragment newInstance() {
        return (new MyConstructionsFragment());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_constructions_fragment, container, false);

        ButterKnife.bind(this, view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);

        constructionRecyclerView.setLayoutManager(linearLayoutManager);

        constructionAdapter = new ConstructionAdapter(getContext());
        constructionAdapter.setCallback(this);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(constructionRecyclerView.getContext(),
                linearLayoutManager.getOrientation());

        constructionRecyclerView.addItemDecoration(dividerItemDecoration);
        constructionRecyclerView.setAdapter(constructionAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        DatabaseManager.getInstance(getContext()).getAllConstructions(new DatabaseOperationCallBack<ArrayList<Construction>>() {
            @Override
            public void onSuccess(ArrayList<Construction> constructions) {
                setupVisibility(constructions.isEmpty());
                constructionAdapter.setData(constructions);
                constructionAdapter.notifyDataSetChanged();
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
            constructionView.setVisibility(View.GONE);
        }else{
            emptyView.setVisibility(View.GONE);
            constructionView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onConstructionSelected(Construction construction) {

        sessionManager.setConstruction(construction);
        startActivity(new Intent(getContext(), MainActivity.class));

        if(getActivity() != null){
            getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            getActivity().finishAffinity();
        }
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

        if (requestCode == SCAN_QR_REQUEST_CODE) {
            switch (resultCode) {
                case RESULT_OK:

                    if (!TextUtils.isEmpty(data.getStringExtra(CODE))) {
                        String extractedData = data.getStringExtra(CODE);
                        String[] parts = extractedData.replace("\"", "").trim().split(";");

                        if (parts.length != 2) {
                            showError(getString(R.string.qr_construction_notrecognized));
                        } else {
                            Construction newConstruction = new Construction()
                                    .withConstructionName(parts[1])
                                    .withCustomer(parts[0])
                                    .withConstructionStatus(ConstructionStatus.IN_PROGRESS);

                            DatabaseManager.getInstance(getContext())
                                    .addConstruction(newConstruction, new DatabaseOperationCallBack<DefaultResponse>() {
                                        @Override
                                        public void onSuccess(DefaultResponse defaultResponse) {
                                            onResume();
                                        }
                                    });
                        }
                    }
                    break;

                case MANUAL:
                    startActivityForResult(new Intent(getActivity(), AddConstructionActivity.class), ADD_MANUAL_CONSTRUCTION_REQUEST_CODE);
                    break;

            }
        }else if(requestCode == ADD_MANUAL_CONSTRUCTION_REQUEST_CODE && resultCode == RESULT_OK){
            onResume();
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

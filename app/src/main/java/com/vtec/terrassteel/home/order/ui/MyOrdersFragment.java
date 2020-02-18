package com.vtec.terrassteel.home.order.ui;

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
import com.vtec.terrassteel.common.model.Order;
import com.vtec.terrassteel.common.model.OrderStatus;
import com.vtec.terrassteel.common.ui.ScanActivity;
import com.vtec.terrassteel.construction.ui.ConstructionMainActivity;
import com.vtec.terrassteel.core.model.DefaultResponse;
import com.vtec.terrassteel.core.task.DatabaseOperationCallBack;
import com.vtec.terrassteel.core.ui.AbstractFragment;
import com.vtec.terrassteel.database.DatabaseManager;
import com.vtec.terrassteel.home.construction.adapter.ConstructionAdapter;
import com.vtec.terrassteel.home.construction.callback.ConstructionCallback;
import com.vtec.terrassteel.home.construction.ui.AddConstructionActivity;
import com.vtec.terrassteel.home.construction.ui.MyConstructionsFragment;
import com.vtec.terrassteel.home.order.adapter.OrderAdapter;
import com.vtec.terrassteel.home.order.callback.OrderCallback;
import com.vtec.terrassteel.order.ui.OrderMainActivity;

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

public class MyOrdersFragment extends AbstractFragment implements OrderCallback {

    private static final int ADD_MANUAL_ORDER_REQUEST_CODE = 24;

    @BindView(R.id.order_listview)
    RecyclerView orderRecyclerView;

    @BindView(R.id.order_view)
    View orderView;

    @BindView(R.id.empty_view)
    View emptyView;

    private OrderAdapter orderAdapter;

    @OnClick({R.id.add_fab, R.id.add_order_button})
    public void onClicAddButton(){
        if (getMissingPermissions().size() > 0) {
            askPermission(getMissingPermissions());
        } else {
            startScanActivity();
        }
    }


    public static Fragment newInstance() {
        return (new MyOrdersFragment());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_orders_fragment, container, false);

        ButterKnife.bind(this, view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);

        orderRecyclerView.setLayoutManager(linearLayoutManager);

        orderAdapter = new OrderAdapter(getContext());
        orderAdapter.setCallback(this);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(orderRecyclerView.getContext(),
                linearLayoutManager.getOrientation());

        orderRecyclerView.addItemDecoration(dividerItemDecoration);
        orderRecyclerView.setAdapter(orderAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        DatabaseManager.getInstance(getContext()).getAllOrders(new DatabaseOperationCallBack<ArrayList<Order>>() {
            @Override
            public void onSuccess(ArrayList<Order> orders) {
                setupVisibility(orders.isEmpty());
                orderAdapter.setData(orders);
                orderAdapter.notifyDataSetChanged();
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
            orderView.setVisibility(View.GONE);
        }else{
            emptyView.setVisibility(View.GONE);
            orderView.setVisibility(View.VISIBLE);
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
                        String[] parts = extractedData.replace("\"", "").trim().split("-");

                        if (parts.length != 2) {
                            showError(getString(R.string.qr_order_notrecognized));
                        } else {
                            Order newOrder = new Order()
                                    .withOrderCode(parts[0])
                                    .withCustomer(parts[1])
                                    .withStatus(OrderStatus.IN_PROGRESS);

                            DatabaseManager.getInstance(getContext())
                                    .addOrder(newOrder, new DatabaseOperationCallBack<DefaultResponse>() {
                                        @Override
                                        public void onSuccess(DefaultResponse defaultResponse) {
                                            onResume();
                                        }
                                    });
                        }
                    }
                    break;

                case MANUAL:
                    startActivityForResult(new Intent(getActivity(), AddOrderActivity.class), ADD_MANUAL_ORDER_REQUEST_CODE);
                    break;

            }
        }else if(requestCode == ADD_MANUAL_ORDER_REQUEST_CODE && resultCode == RESULT_OK){
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

    @Override
    public void onOrderSelected(Order order) {
        sessionManager.setCurrentOrder(order);
        startActivity(new Intent(getContext(), OrderMainActivity.class));

        if(getActivity() != null){
            getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            getActivity().finishAffinity();
        }
    }
}

package com.vtec.terrassteel.main.ui.workingorder.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vtec.terrassteel.R;
import com.vtec.terrassteel.core.ui.AbstractFragment;
import com.vtec.terrassteel.home.company.customer.ui.EditCustomerActivity;
import com.vtec.terrassteel.main.ui.dashboard.ui.DashboardFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WorkingOrderFragment extends AbstractFragment {

    private static  final int ADD_CUSTOMER_INTENT_CODE = 19;

    public static final String TAG = WorkingOrderFragment.class.getSimpleName();

    @OnClick(R.id.add_workorder_button)
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

        return thisView;
    }
}

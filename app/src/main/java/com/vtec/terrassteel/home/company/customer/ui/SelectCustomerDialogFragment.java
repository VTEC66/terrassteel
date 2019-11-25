package com.vtec.terrassteel.home.company.customer.ui;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.vtec.terrassteel.R;
import com.vtec.terrassteel.common.model.Customer;
import com.vtec.terrassteel.core.task.DatabaseOperationCallBack;
import com.vtec.terrassteel.database.DatabaseManager;
import com.vtec.terrassteel.home.construction.adapter.SelectCustomersAdapter;
import com.vtec.terrassteel.home.construction.callback.SelectCustomerDialogCallback;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SelectCustomerDialogFragment extends DialogFragment {

    private SelectCustomerDialogCallback callback;

    @BindView(R.id.customer_recyclerview)
    RecyclerView customerRecyclerView;

    @BindView(R.id.empty_view)
    View emptyView;

    @BindView(R.id.customer_view)
    View customerView;

    @OnClick(R.id.add_customer_button)
    public void onClicAddCustomer(){
        dismiss();
        callback.onSelectAddCustomer();
    }


    public SelectCustomerDialogFragment setCallBack(SelectCustomerDialogCallback callBack) {
        this.callback = callBack;
        return this;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup parent, Bundle state) {
        super.onCreateView(inflater, parent, state);

        View thisView = inflater.inflate(R.layout.select_customer_dialog_fragment, parent, false);

        ButterKnife.bind(this, thisView);

        SelectCustomersAdapter selectCustomersAdapter =
                new SelectCustomersAdapter(callback);

        DatabaseManager.getInstance(getContext()).getAllCustomers(new DatabaseOperationCallBack<ArrayList<Customer>>() {
                    @Override
                    public void onSuccess(ArrayList<Customer> customers) {
                        setupVisibility(customers.isEmpty());
                        selectCustomersAdapter.setData(customers);
                        selectCustomersAdapter.notifyDataSetChanged();
                    }
                });

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);

        customerRecyclerView.setLayoutManager(linearLayoutManager);

        customerRecyclerView.addItemDecoration(new DividerItemDecoration(customerRecyclerView.getContext(), DividerItemDecoration.VERTICAL));

        customerRecyclerView.setAdapter(selectCustomersAdapter);

        return thisView;
    }

    private void setupVisibility(boolean isDataEmpty) {

        if (isDataEmpty) {
            emptyView.setVisibility(View.VISIBLE);
            customerView.setVisibility(View.GONE);
        } else {
            emptyView.setVisibility(View.GONE);
            customerView.setVisibility(View.VISIBLE);
        }
    }

}

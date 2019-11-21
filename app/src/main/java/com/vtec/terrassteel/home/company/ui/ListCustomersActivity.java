package com.vtec.terrassteel.home.company.ui;

import android.content.Intent;
import android.os.Bundle;

import com.vtec.terrassteel.R;
import com.vtec.terrassteel.common.listener.ActionBarListener;
import com.vtec.terrassteel.common.model.Construction;
import com.vtec.terrassteel.common.model.Customer;
import com.vtec.terrassteel.common.ui.ActionBar;
import com.vtec.terrassteel.core.manager.DatabaseManager;
import com.vtec.terrassteel.core.task.DatabaseOperationCallBack;
import com.vtec.terrassteel.core.ui.AbstractActivity;
import com.vtec.terrassteel.home.company.adapter.CustomersAdapter;
import com.vtec.terrassteel.home.construction.adapter.ConstructionAdapter;
import com.vtec.terrassteel.home.construction.ui.AddConstructionActivity;

import java.util.ArrayList;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ListCustomersActivity extends AbstractActivity {

    private static final int ADD_CUSTOMER_INTENT_CODE = 17;

    @BindView(R.id.action_bar)
    ActionBar actionBar;

    @BindView(R.id.customer_listview)
    RecyclerView customersRecyclerView;

    @OnClick(R.id.add_fab)
    public void onClicAddButton(){
        startActivityForResult(new Intent(this, AddCustomerActivity.class), ADD_CUSTOMER_INTENT_CODE);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    private CustomersAdapter customersAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.list_customers_activity);
        ButterKnife.bind(this);

        actionBar.setListener(new ActionBarListener() {
            @Override
            public void onBackArrowClick() {
                finish();
            }

            @Override
            public void onActionButtonClick() { }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);

        customersRecyclerView.setLayoutManager(linearLayoutManager);

        customersAdapter = new CustomersAdapter(getBaseContext());

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(customersRecyclerView.getContext(),
                linearLayoutManager.getOrientation());

        customersRecyclerView.addItemDecoration(dividerItemDecoration);
        customersRecyclerView.setAdapter(customersAdapter);

    }

    @Override
    public void onResume() {
        super.onResume();

        DatabaseManager.getInstance(this).getAllCustomers(new DatabaseOperationCallBack<ArrayList<Customer>>() {
            @Override
            public void onSuccess(ArrayList<Customer> customers) {
                customersAdapter.setData(customers);
                customersAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError() {
                super.onError();
            }
        });

        /*constructionAdapter.setData(DatabaseManager.getInstance(getContext()).getAllConstructions());//makeMock());
        constructionAdapter.notifyDataSetChanged();*/

    }
}

package com.vtec.terrassteel.home.company.employee.ui;

import android.content.Intent;
import android.os.Bundle;

import com.vtec.terrassteel.R;
import com.vtec.terrassteel.common.listener.ActionBarListener;
import com.vtec.terrassteel.common.model.Employee;
import com.vtec.terrassteel.common.ui.ActionBar;
import com.vtec.terrassteel.core.task.DatabaseOperationCallBack;
import com.vtec.terrassteel.core.ui.AbstractActivity;
import com.vtec.terrassteel.database.DatabaseManager;
import com.vtec.terrassteel.home.company.employee.adapter.EmployeesAdapter;

import java.util.ArrayList;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ListEmployeesActivity extends AbstractActivity {

    private static final int ADD_EMPLOYEE_INTENT_CODE = 18;

    @BindView(R.id.action_bar)
    ActionBar actionBar;

    @BindView(R.id.employee_listview)
    RecyclerView employeesRecyclerView;

    @OnClick(R.id.add_fab)
    public void onClicAddButton(){
        startActivityForResult(new Intent(this, AddEmployeeActivity.class), ADD_EMPLOYEE_INTENT_CODE);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    private EmployeesAdapter employeesAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.list_employees_activity);
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

        employeesRecyclerView.setLayoutManager(linearLayoutManager);

        employeesAdapter = new EmployeesAdapter(getBaseContext());

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(employeesRecyclerView.getContext(),
                linearLayoutManager.getOrientation());

        employeesRecyclerView.addItemDecoration(dividerItemDecoration);
        employeesRecyclerView.setAdapter(employeesAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();

        DatabaseManager.getInstance(this).getAllEmployees(new DatabaseOperationCallBack<ArrayList<Employee>>() {
            @Override
            public void onSuccess(ArrayList<Employee> employees) {
                employeesAdapter.setData(employees);
                employeesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError() {
                super.onError();
            }
        });
    }

}

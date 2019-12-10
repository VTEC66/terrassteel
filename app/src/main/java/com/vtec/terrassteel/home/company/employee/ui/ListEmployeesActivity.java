package com.vtec.terrassteel.home.company.employee.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.vtec.terrassteel.R;
import com.vtec.terrassteel.common.listener.ActionBarListener;
import com.vtec.terrassteel.common.model.Employee;
import com.vtec.terrassteel.common.ui.ActionBar;
import com.vtec.terrassteel.core.task.DatabaseOperationCallBack;
import com.vtec.terrassteel.core.ui.AbstractActivity;
import com.vtec.terrassteel.database.DatabaseManager;
import com.vtec.terrassteel.home.company.employee.adapter.EmployeesAdapter;
import com.vtec.terrassteel.home.company.employee.callback.SelectEmployeeCallback;

import java.util.ArrayList;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.vtec.terrassteel.home.company.employee.ui.EditEmployeeActivity.EXTRA_EMPLOYEE;

public class ListEmployeesActivity extends AbstractActivity implements SelectEmployeeCallback {

    private static final int ADD_EMPLOYEE_INTENT_CODE = 18;
    public static final int EDIT_EMPLOYEE_INTENT_CODE = 19;

    @BindView(R.id.empty_view)
    View emptyView;

    @BindView(R.id.available_employee_view)
    View employeeView;

    @BindView(R.id.action_bar)
    ActionBar actionBar;

    @BindView(R.id.available_employee_listview)
    RecyclerView employeesRecyclerView;

    @OnClick({R.id.add_fab, R.id.add_employee_button})
    public void onClicAddButton(){
        startActivityForResult(new Intent(this, EditEmployeeActivity.class), ADD_EMPLOYEE_INTENT_CODE);
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
        employeesAdapter.setCallback(this);

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

                setupVisibility(employees.isEmpty());

                employeesAdapter.setData(employees);
                employeesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError() {
                super.onError();
            }
        });
    }

    private void setupVisibility(boolean isDataEmpty) {

            if (isDataEmpty) {
                emptyView.setVisibility(View.VISIBLE);
                employeeView.setVisibility(View.GONE);
            } else {
                emptyView.setVisibility(View.GONE);
                employeeView.setVisibility(View.VISIBLE);
            }
    }


    @Override
    public void onEmployeeSelected(Employee employee) {

        Intent intent = new Intent(getBaseContext(), EditEmployeeActivity.class);

        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_EMPLOYEE, employee);
        intent.putExtras(bundle);

        startActivityForResult(intent, EDIT_EMPLOYEE_INTENT_CODE);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}

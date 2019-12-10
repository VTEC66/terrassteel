package com.vtec.terrassteel.main.ui.assign.ui;

import android.os.Bundle;

import com.vtec.terrassteel.R;
import com.vtec.terrassteel.common.listener.ActionBarListener;
import com.vtec.terrassteel.common.model.Assign;
import com.vtec.terrassteel.common.model.Employee;
import com.vtec.terrassteel.common.model.WorkOrder;
import com.vtec.terrassteel.common.ui.ActionBar;
import com.vtec.terrassteel.core.model.DefaultResponse;
import com.vtec.terrassteel.core.task.DatabaseOperationCallBack;
import com.vtec.terrassteel.core.ui.AbstractActivity;
import com.vtec.terrassteel.database.DatabaseManager;
import com.vtec.terrassteel.main.ui.assign.adapter.AssignEmployeeAdapter;
import com.vtec.terrassteel.main.ui.assign.callback.AssignEmployeeCallback;

import java.util.ArrayList;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.vtec.terrassteel.main.ui.pointing.ui.PointingTimeFragment.EXTRA_WORK_ORDER;

public class AddAssignActivity extends AbstractActivity implements AssignEmployeeCallback {

    WorkOrder workOrder;


    @BindView(R.id.action_bar)
    ActionBar actionBar;

    @BindView(R.id.available_employee_listview)
    RecyclerView availableEmployeesRecyclerView;

    @BindView(R.id.assigned_employee_listview)
    RecyclerView assignedEmployeesRecyclerView;

    private AssignEmployeeAdapter availableEmployeesAdapter;
    private AssignEmployeeAdapter assignedEmployeesAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.add_assign_activity);
        ButterKnife.bind(this);

        if (getIntent() != null && getIntent().hasExtra(EXTRA_WORK_ORDER)) {
            Bundle bundle = getIntent().getExtras();

            workOrder = (WorkOrder) bundle.getSerializable(EXTRA_WORK_ORDER);
        }

        actionBar.setListener(new ActionBarListener() {
            @Override
            public void onBackArrowClick() {
                finish();
            }

            @Override
            public void onActionButtonClick() { }
        });

        actionBar.setTitle(workOrder.getWorkOrderReference());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);

        LinearLayoutManager linear2LayoutManager = new LinearLayoutManager(this);
        linear2LayoutManager.setOrientation(RecyclerView.VERTICAL);

        availableEmployeesRecyclerView.setLayoutManager(linearLayoutManager);
        assignedEmployeesRecyclerView.setLayoutManager(linear2LayoutManager);

        availableEmployeesAdapter = new AssignEmployeeAdapter(getBaseContext(), true);
        assignedEmployeesAdapter = new AssignEmployeeAdapter(getBaseContext(), false);

        availableEmployeesAdapter.setCallback(this);
        assignedEmployeesAdapter.setCallback(this);

        DividerItemDecoration availabledividerItemDecoration = new DividerItemDecoration(availableEmployeesRecyclerView.getContext(),
                linearLayoutManager.getOrientation());

        DividerItemDecoration assigndividerItemDecoration = new DividerItemDecoration(availableEmployeesRecyclerView.getContext(),
                linearLayoutManager.getOrientation());

        availableEmployeesRecyclerView.addItemDecoration(availabledividerItemDecoration);
        assignedEmployeesRecyclerView.addItemDecoration(assigndividerItemDecoration);

        availableEmployeesRecyclerView.setAdapter(availableEmployeesAdapter);
        assignedEmployeesRecyclerView.setAdapter(assignedEmployeesAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        refreshAvailableEmployee();
    }

    private void refreshAvailableEmployee() {

        DatabaseManager.getInstance(this).getAvailableEmployee(new DatabaseOperationCallBack<ArrayList<Employee>>() {
            @Override
            public void onSuccess(ArrayList<Employee> employees) {

                availableEmployeesAdapter.setData(employees);
                availableEmployeesAdapter.notifyDataSetChanged();

                refreshAssignedEmployee();
            }
        });


    }

    private void refreshAssignedEmployee() {
        DatabaseManager.getInstance(this).getAssignedEmployee(workOrder, new DatabaseOperationCallBack<ArrayList<Employee>>() {
            @Override
            public void onSuccess(ArrayList<Employee> employees) {

                assignedEmployeesAdapter.setData(employees);
                assignedEmployeesAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void removeEmployee(Employee employee) {
        DatabaseManager.getInstance(this).deleteAssign(workOrder, employee , new DatabaseOperationCallBack<DefaultResponse>() {
            @Override
            public void onSuccess(DefaultResponse defaultResponse) {
                refreshAvailableEmployee();
            }
        });
    }

    @Override
    public void assignEmployee(Employee employee) {
        Assign assign = new Assign()
                .withWorkOrder(workOrder)
                .withEmployee(employee);

        DatabaseManager.getInstance(this).addAssign(assign, new DatabaseOperationCallBack<DefaultResponse>() {
            @Override
            public void onSuccess(DefaultResponse defaultResponse) {
                refreshAvailableEmployee();
            }
        });
    }
}

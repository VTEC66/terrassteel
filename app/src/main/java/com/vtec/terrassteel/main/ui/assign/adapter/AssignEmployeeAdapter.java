package com.vtec.terrassteel.main.ui.assign.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vtec.terrassteel.R;
import com.vtec.terrassteel.common.model.Employee;
import com.vtec.terrassteel.home.company.employee.adapter.EmployeesAdapter;
import com.vtec.terrassteel.home.company.employee.callback.SelectEmployeeCallback;
import com.vtec.terrassteel.main.ui.assign.callback.AssignEmployeeCallback;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AssignEmployeeAdapter extends RecyclerView.Adapter<AssignEmployeeAdapter.ViewHolder> {

    private Context context;

    final private boolean available;

    private ArrayList<Employee> elements = new ArrayList<>();
    private AssignEmployeeCallback callback;

    public AssignEmployeeAdapter(Context context, boolean available) {
        this.available = available;
        this.context = context;
    }

    public void setCallback(AssignEmployeeCallback callback) {
        this.callback = callback;
    }

    @NonNull
    @Override
    public AssignEmployeeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AssignEmployeeAdapter.ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.assign_employee_itemview, parent, false));    }

    @Override
    public void onBindViewHolder(@NonNull AssignEmployeeAdapter.ViewHolder holder, int position) {
        Employee employee = elements.get(position);

        holder.employeeNameTextView.setText(employee.getEmployeeName());
        holder.employeeJobTextView.setText(employee.getEmployeeJob().getRessourceReference());


        if(available){
            holder.actionView.setImageDrawable(context.getDrawable(R.drawable.ic_add_circle));
        }else{
            holder.actionView.setImageDrawable(context.getDrawable(R.drawable.ic_remove_circle));
        }

        holder.actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(available){
                    callback.assignEmployee(employee);
                }else{
                    callback.removeEmployee(employee);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        if (elements.size() > 0)
            return elements.size();
        return 0;
    }

    public void setData(ArrayList<Employee> elements) {
        this.elements = elements;
    }



    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.employee_name_tv)
        TextView employeeNameTextView;

        @BindView(R.id.employee_job_tv)
        TextView employeeJobTextView;

        @BindView(R.id.action_view)
        ImageView actionView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
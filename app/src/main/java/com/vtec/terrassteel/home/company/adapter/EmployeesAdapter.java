package com.vtec.terrassteel.home.company.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vtec.terrassteel.R;
import com.vtec.terrassteel.common.model.Employee;
import com.vtec.terrassteel.home.construction.callback.ConstructionCallback;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class EmployeesAdapter extends RecyclerView.Adapter<EmployeesAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Employee> elements = new ArrayList<>();

    public EmployeesAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EmployeesAdapter.ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.employee_itemview, parent, false));    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Employee employee = elements.get(position);

        holder.employeeNameTextView.setText(employee.getEmployeeName());
        holder.employeeJobTextView.setText(employee.getEmployeeJob().getRessourceReference());

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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
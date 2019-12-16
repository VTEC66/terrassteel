package com.vtec.terrassteel.main.ui.workorder.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vtec.terrassteel.R;
import com.vtec.terrassteel.common.model.Employee;
import com.vtec.terrassteel.common.model.Imputation;
import com.vtec.terrassteel.home.company.employee.adapter.EmployeesAdapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ImputationAdapter extends RecyclerView.Adapter<ImputationAdapter.ViewHolder> {

    private ArrayList<Imputation> elements = new ArrayList<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ImputationAdapter.ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.imputation_itemview, parent, false));    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Imputation imputation = elements.get(position);



        holder.imputationTimeTextView.setText(calculateTime(imputation.getImputationTotalTime()));//String.valueOf(imputation.getImputationTotalTime()));
        holder.imputationEmployeeTextView.setText(imputation.getEmployee().getEmployeeName());
        holder.imputationEmployeeJobTextView.setText(imputation.getEmployee().getEmployeeJob().getRessourceReference());

    }

    @Override
    public int getItemCount() {
        if (elements.size() > 0)
            return elements.size();
        return 0;
    }

    public void setData(ArrayList<Imputation> imputations) {
        this.elements = imputations;
    }

    public static String calculateTime(long seconds) {
        long p1 = seconds % 60;
        long p2 = seconds / 60;
        long p3 = p2 % 60;
        p2 = p2 / 60;
        StringBuilder timeSb = new StringBuilder();

        if(p2 >0){
            timeSb.append( p2 + " heures ");
        }

        timeSb.append( p3 + " minutes");

        return timeSb.toString();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.employee_name_tv)
        TextView imputationEmployeeTextView;

        @BindView(R.id.employee_job_tv)
        TextView imputationEmployeeJobTextView;

        @BindView(R.id.imputation_time_name_tv)
        TextView imputationTimeTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

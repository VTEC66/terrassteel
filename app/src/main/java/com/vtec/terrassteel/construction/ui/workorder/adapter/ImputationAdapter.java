package com.vtec.terrassteel.construction.ui.workorder.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vtec.terrassteel.R;
import com.vtec.terrassteel.common.model.Imputation;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
        DateFormat f = new SimpleDateFormat(" dd/MM/yyyy  HH:mm");

        Date start = new Date(imputation.getImputationStart());
        holder.imputationStartTimeTextView.setText("Début : " + f.format(start));//calculateTime(imputation.getImputationTotalTime()));//String.valueOf(imputation.getImputationTotalTime()));

        if(imputation.getImputationEnd() != 0){
            holder.imputationEndTimeTextView.setVisibility(View.VISIBLE);
            holder.statusIndicatorView.setVisibility(View.GONE);
            Date end = new Date(imputation.getImputationEnd());
            holder.imputationEndTimeTextView.setText("Fin : " + f.format(end));//calculateTime(imputation.getImputationTotalTime()));//String.valueOf(imputation.getImputationTotalTime()));
        }else{
            holder.imputationEndTimeTextView.setVisibility(View.GONE);
            holder.statusIndicatorView.setVisibility(View.VISIBLE);
        }

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

        @BindView(R.id.start_time_tv)
        TextView imputationStartTimeTextView;

        @BindView(R.id.end_time_tv)
        TextView imputationEndTimeTextView;

        @BindView(R.id.status_indicator_container)
        View statusIndicatorView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

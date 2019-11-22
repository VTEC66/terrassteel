package com.vtec.terrassteel.home.company.employee.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vtec.terrassteel.R;
import com.vtec.terrassteel.common.model.Job;
import com.vtec.terrassteel.home.company.employee.callback.SelectJobCallback;

import java.util.Arrays;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class JobAdapter extends RecyclerView.Adapter<JobAdapter.ViewHolder> {

    private final SelectJobCallback callback;

    public JobAdapter(SelectJobCallback callback) {
        this.callback = callback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
           return new JobAdapter.ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.simple_itemview, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Job job = Arrays.asList(Job.values()).get(position);

        holder.simpleTextView.setText(job.getRessourceReference());

        holder.itemView.setOnClickListener(v -> callback.onJobSelected(job));

    }

    @Override
    public int getItemCount() {
        return Job.values().length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.simple_textview)
        TextView simpleTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}

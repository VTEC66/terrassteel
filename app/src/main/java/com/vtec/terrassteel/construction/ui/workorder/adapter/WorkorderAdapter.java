package com.vtec.terrassteel.construction.ui.workorder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vtec.terrassteel.R;
import com.vtec.terrassteel.common.model.WorkOrder;
import com.vtec.terrassteel.construction.ui.workorder.callback.WorkorderCallback;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class WorkorderAdapter extends RecyclerView.Adapter<WorkorderAdapter.ViewHolder> {


    private Context context;
    private ArrayList<WorkOrder> elements = new ArrayList<>();
    private WorkorderCallback callback;

    public WorkorderAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new WorkorderAdapter.ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.workorder_itemview, parent, false));    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        WorkOrder workOrder = elements.get(position);

        holder.workorderNameTextView.setText(context.getString(R.string.work_order,workOrder.getWorkOrderReference()));
        holder.workorderaffaireTextView.setText(workOrder.getWorkOrderAffaire());
        holder.statusIndicatorTextView.setText(workOrder.getWorkOrderStatus().getRessourceReference());

        switch (workOrder.getWorkOrderStatus()){
            case IN_PROGRESS:
                holder.statusIndicatorContainer.setBackground(context.getResources().getDrawable(R.drawable.bg_status_indicator_inprogress));
                break;
            case FINISHED:
                holder.statusIndicatorContainer.setBackground(context.getResources().getDrawable(R.drawable.bg_status_indicator_finished));
                break;
        }

        holder.itemView.setOnClickListener(v -> callback.onWorkOrderSelected(workOrder));
    }

    @Override
    public int getItemCount() {
        if (elements.size() > 0)
            return elements.size();
        return 0;
    }

    public void setCallback(WorkorderCallback callback) {
        this.callback = callback;
    }

    public void setData(ArrayList<WorkOrder> elements) {
        this.elements = elements;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.workorder_name_tv)
        TextView workorderNameTextView;

        @BindView(R.id.affaire_tv)
        TextView workorderaffaireTextView;

        @BindView(R.id.status_indicator_tv)
        TextView statusIndicatorTextView;

        @BindView(R.id.status_indicator_container)
        View statusIndicatorContainer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

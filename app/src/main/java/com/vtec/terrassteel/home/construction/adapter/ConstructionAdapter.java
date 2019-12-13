package com.vtec.terrassteel.home.construction.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vtec.terrassteel.R;
import com.vtec.terrassteel.common.model.Construction;
import com.vtec.terrassteel.home.construction.callback.ConstructionCallback;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ConstructionAdapter extends RecyclerView.Adapter<ConstructionAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Construction> elements = new ArrayList<>();
    private ConstructionCallback callback;

    public ConstructionAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ConstructionAdapter.ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.construction_itemview, parent, false));    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Construction construction = elements.get(position);

        holder.constructionNameTextView.setText(construction.getConstructionName());
        holder.customerTextView.setText(construction.getCustomer());
        holder.statusIndicatorTextView.setText(construction.getConstructionStatus().getRessourceReference());

        switch (construction.getConstructionStatus()){
            case IN_PROGRESS:
                holder.statusIndicatorContainer.setBackground(context.getResources().getDrawable(R.drawable.bg_status_indicator_inprogress));
                break;
            case FINISHED:
                holder.statusIndicatorContainer.setBackground(context.getResources().getDrawable(R.drawable.bg_status_indicator_finished));
                break;
        }

        holder.itemView.setOnClickListener(v -> callback.onConstructionSelected(construction));

    }

    @Override
    public int getItemCount() {
        if (elements.size() > 0)
            return elements.size();
        return 0;
    }

    public void setCallback(ConstructionCallback callback) {
        this.callback = callback;
    }

    public void setData(ArrayList<Construction> elements) {
        this.elements = elements;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.construction_name_tv)
        TextView constructionNameTextView;

        @BindView(R.id.customer_tv)
        TextView customerTextView;

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
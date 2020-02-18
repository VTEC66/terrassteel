package com.vtec.terrassteel.home.order.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vtec.terrassteel.R;
import com.vtec.terrassteel.common.model.Construction;
import com.vtec.terrassteel.common.model.Order;
import com.vtec.terrassteel.home.construction.adapter.ConstructionAdapter;
import com.vtec.terrassteel.home.order.callback.OrderCallback;
import com.vtec.terrassteel.home.order.ui.MyOrdersFragment;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    private final Context context;
    private ArrayList<Order> elements = new ArrayList<>();
    private OrderCallback callback;

    public OrderAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public OrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrderAdapter.ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.order_itemview, parent, false));    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.ViewHolder holder, int position) {
        Order order = elements.get(position);

        holder.codeOrderTextView.setText(order.getOrderCode());
        holder.customerTextView.setText(order.getCustomer());
        holder.statusIndicatorTextView.setText(order.getStatus().getRessourceReference());

        switch (order.getStatus()){
            case IN_PROGRESS:
                holder.statusIndicatorContainer.setBackground(context.getResources().getDrawable(R.drawable.bg_status_indicator_inprogress));
                break;
            case FINISHED:
                holder.statusIndicatorContainer.setBackground(context.getResources().getDrawable(R.drawable.bg_status_indicator_finished));
                break;
        }

        holder.itemView.setOnClickListener(v -> callback.onOrderSelected(order));
    }

    @Override
    public int getItemCount() {
        if (elements.size() > 0)
            return elements.size();
        return 0;
    }

    public void setCallback(OrderCallback callback) {
        this.callback = callback;
    }

    public void setData(ArrayList<Order> orders) {
            this.elements = orders;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.code_order_tv)
        TextView codeOrderTextView;

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

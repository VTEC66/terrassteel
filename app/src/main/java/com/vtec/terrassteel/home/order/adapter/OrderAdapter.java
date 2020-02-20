package com.vtec.terrassteel.home.order.adapter;

import android.content.Context;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vtec.terrassteel.R;
import com.vtec.terrassteel.common.model.Construction;
import com.vtec.terrassteel.common.model.Order;
import com.vtec.terrassteel.common.model.OrderStatus;
import com.vtec.terrassteel.home.construction.adapter.ConstructionAdapter;
import com.vtec.terrassteel.home.order.callback.OrderCallback;
import com.vtec.terrassteel.home.order.ui.MyOrdersFragment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    private final Context context;
    private ArrayList<Order> elements = new ArrayList<>();
    private ArrayList<Order> filteredElements = new ArrayList<>();


    private OrderCallback callback;
    private boolean shouldShowAll = false;


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
        Order order = filteredElements.get(position);

        holder.codeOrderTextView.setText(order.getOrderCode());
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
        if (filteredElements.size() > 0)
            return filteredElements.size();
        return 0;
    }

    public void setCallback(OrderCallback callback) {
        this.callback = callback;
    }

    public void setData(ArrayList<Order> elements) {
        this.elements = new ArrayList<>();
        this.filteredElements = new ArrayList<>();

        if(elements != null){
            this.elements.addAll(elements);
            this.filteredElements.addAll(getElementToShow());
        }
    }

    public void clearData() {
        filteredElements.clear();
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        filteredElements.clear();
        if (charText.length() == 0) {
            filteredElements.addAll(getElementToShow());
        } else {
            for (Order order : getElementToShow()) {
                if (order.getOrderCode().toLowerCase(Locale.getDefault()).contains(charText)) {
                        filteredElements.add(order);
                }
            }
        }
        notifyDataSetChanged();
    }

    private ArrayList<Order> getElementToShow() {

        if(shouldShowAll){
            return elements;
        }

        ArrayList<Order> elementToShow =new ArrayList<Order>();

        for (Order order : elements){
            if(order.getStatus().equals(OrderStatus.IN_PROGRESS)){
                elementToShow.add(order);
            }
        }

        return elementToShow;
    }

    public void setShowAll(boolean shouldShowAll) {
        this.shouldShowAll = shouldShowAll;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.code_order_tv)
        TextView codeOrderTextView;

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

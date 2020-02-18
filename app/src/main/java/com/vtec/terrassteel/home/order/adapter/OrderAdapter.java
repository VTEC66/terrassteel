package com.vtec.terrassteel.home.order.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.vtec.terrassteel.common.model.Construction;
import com.vtec.terrassteel.common.model.Order;
import com.vtec.terrassteel.home.construction.adapter.ConstructionAdapter;
import com.vtec.terrassteel.home.order.callback.OrderCallback;
import com.vtec.terrassteel.home.order.ui.MyOrdersFragment;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public void setCallback(OrderCallback callback) {
        this.callback = callback;
    }

    public void setData(ArrayList<Order> orders) {
            this.elements = orders;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}

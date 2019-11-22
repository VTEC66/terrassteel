package com.vtec.terrassteel.home.construction.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vtec.terrassteel.R;
import com.vtec.terrassteel.common.model.Customer;
import com.vtec.terrassteel.home.construction.callback.SelectCustomerCallback;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectCustomersAdapter extends RecyclerView.Adapter<SelectCustomersAdapter.ViewHolder> {

    private final SelectCustomerCallback callback;

    private ArrayList<Customer> elements = new ArrayList<>();

    public SelectCustomersAdapter(SelectCustomerCallback callback) {
        this.callback = callback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SelectCustomersAdapter.ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.simple_itemview, parent, false));    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Customer customer = elements.get(position);

        holder.simpleTextView.setText(customer.getCustomerName());

        holder.itemView.setOnClickListener(v -> callback.onCustomerSelected(customer));
    }

    @Override
    public int getItemCount() {
        if (elements.size() > 0)
            return elements.size();
        return 0;
    }

    public void setData(ArrayList<Customer> elements) {
        this.elements = elements;
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
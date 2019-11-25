package com.vtec.terrassteel.home.company.customer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vtec.terrassteel.R;
import com.vtec.terrassteel.common.model.Customer;
import com.vtec.terrassteel.home.company.customer.callback.SelectCustomerCallback;
import com.vtec.terrassteel.home.company.customer.ui.ListCustomersActivity;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomersAdapter extends RecyclerView.Adapter<CustomersAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Customer> elements = new ArrayList<>();
    private SelectCustomerCallback callback;

    public CustomersAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CustomersAdapter.ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_itemview, parent, false));    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Customer customer = elements.get(position);

        holder.customerNameTextView.setText(customer.getCustomerName());

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

    public void setCallback(SelectCustomerCallback callback) {
        this.callback = callback;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.customer_name_tv)
        TextView customerNameTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
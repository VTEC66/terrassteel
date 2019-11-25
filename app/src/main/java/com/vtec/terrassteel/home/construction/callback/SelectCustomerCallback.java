package com.vtec.terrassteel.home.construction.callback;

import com.vtec.terrassteel.common.model.Customer;

public interface SelectCustomerCallback {

    public void onCustomerSelected(Customer customer);

    public void onSelectAddCustomer();
}

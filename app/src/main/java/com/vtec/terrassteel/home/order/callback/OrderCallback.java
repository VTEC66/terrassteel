package com.vtec.terrassteel.home.order.callback;

import com.vtec.terrassteel.common.model.Order;

public interface OrderCallback {

    void onOrderSelected(Order order);
}

package com.vtec.terrassteel.common.model;

public class Order {

    private long orderId;
    private String orderCode;
    private String customer;
    private OrderStatus status;

    public Order withId(long id){
        this.orderId = id;
        return this;
    }

    public Order withOrderCode(String orderCode) {
        this.orderCode = orderCode;
        return this;
    }

    public Order withCustomer(String customerName) {
        this.customer = customerName;
        return this;
    }

    public Order withStatus(OrderStatus status) {
        this.status = status;
        return this;
    }

    public long getOrderId() {
        return orderId;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public String getCustomer() {
        return customer;
    }

    public OrderStatus getStatus() {
        return status;
    }
}

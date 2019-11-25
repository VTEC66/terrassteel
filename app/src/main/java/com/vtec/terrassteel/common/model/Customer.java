package com.vtec.terrassteel.common.model;

import java.io.Serializable;

public class Customer implements Serializable {

    public long customerId;
    public String customerName;
    public String customerAddress1;
    public String customerAddress2;
    public String customerZip;
    public String customerCity;
    public String customerPhone;
    public String customerEmail;

    public Customer withCustomerId(long customerId){
        this.customerId = customerId;
        return this;
    }

    public Customer withCustomerName(String customerName){
        this.customerName = customerName;
        return this;
    }

    public Customer withCustomerAddress1(String customerAddress1){
        this.customerAddress1 = customerAddress1;
        return this;
    }

    public Customer withCustomerAddress2(String customerAddress2){
        this.customerAddress2 = customerAddress2;
        return this;
    }

    public Customer withCustomerZip(String customerZip){
        this.customerZip = customerZip;
        return this;
    }

    public Customer withCustomerCity(String customerCity){
        this.customerCity = customerCity;
        return this;
    }

    public Customer withCustomerPhone(String customerPhone){
        this.customerPhone = customerPhone;
        return this;
    }

    public Customer withCustomerEmail(String customerEmail){
        this.customerEmail = customerEmail;
        return this;
    }

    public long getCustomerId() {
        return customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerAddress1() {
        return customerAddress1;
    }

    public String getCustomerAddress2() {
        return customerAddress2;
    }

    public String getCustomerZip() {
        return customerZip;
    }

    public String getCustomerCity() {
        return customerCity;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }
}

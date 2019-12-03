package com.vtec.terrassteel.common.model;

import java.io.Serializable;

public class Construction implements Serializable {

    public long constructionId;
    public String constructionName;
    public String constructionAddress1;
    public String constructionAddress2;
    public String constructionZip;
    public String constructionCity;
    public ConstructionStatus constructionStatus;

    public Customer customer;

    public Construction() { }

    public Construction withConstructionId(long constructionId){
        this.constructionId = constructionId;
        return this;
    }

    public Construction withConstructionName(String constructionName){
        this.constructionName = constructionName;
        return this;
    }

    public Construction withCustomer(Customer customer) {
        this.customer = customer;
        return this;
    }

    public Construction withConstructionAddress1(String constructionAddress1){
        this.constructionAddress1 = constructionAddress1;
        return this;
    }

    public Construction withConstructionAddress2(String constructionAddress2){
        this.constructionAddress2 = constructionAddress2;
        return this;
    }

    public Construction withConstructionZip(String constructionZip){
        this.constructionZip = constructionZip;
        return this;
    }

    public Construction withConstructionCity(String constructionCity){
        this.constructionCity = constructionCity;
        return this;
    }

    public Construction withConstructionStatus(ConstructionStatus constructionStatus){
        this.constructionStatus = constructionStatus;
        return this;
    }

    public ConstructionStatus getConstructionStatus() {
        return constructionStatus;
    }

    public String getConstructionName() {
        return constructionName;
    }

    public Customer getCustomer() {
        if(customer == null){
            return new Customer();
        }
        return customer;
    }

    public String getCustomerName() {
        return getCustomer().getCustomerName();
        /*if(customer != null){
            return customer.getCustomerName();
        }else{
            return "";
        }*/
    }

    public long getConstructionId() {
        return constructionId;
    }

    public String getConstructionAddress1() {
        return constructionAddress1;
    }

    public String getConstructionAddress2() {
        return constructionAddress2;
    }

    public String getConstructionZip() {
        return constructionZip;
    }

    public String getConstructionCity() {
        return constructionCity;
    }


}

package com.vtec.terrassteel.common.model;

import java.io.Serializable;

public class Construction implements Serializable {

    public long constructionId;
    public String constructionName;
    public String customer;
    public ConstructionStatus constructionStatus;


    public Construction() { }

    public Construction withConstructionId(long constructionId){
        this.constructionId = constructionId;
        return this;
    }

    public Construction withConstructionName(String constructionName){
        this.constructionName = constructionName;
        return this;
    }

    public Construction withCustomer(String customer) {
        this.customer = customer;
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

    public String getCustomer() {
        return customer;
    }


    public long getConstructionId() {
        return constructionId;
    }


}

package com.vtec.terrassteel.common.model;

public class Construction {

    public ConstructionStatus status;
    public String constructionName;
    public String customer;

    public Construction(String constructionName, String customer, ConstructionStatus status) {
        this.constructionName = constructionName;
        this.customer = customer;
        this.status = status;
    }

    public ConstructionStatus getStatus() {
        return status;
    }

    public String getConstructionName() {
        return constructionName;
    }

    public String getCustomer() {
        return customer;
    }


}

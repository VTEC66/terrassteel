package com.vtec.terrassteel.common.model;

import java.io.Serializable;

public class Imputation implements Serializable {

    public long imputationId;

    public WorkOrder workOrder;

    public Assign assign;

    public long imputationStart;

    public long imputationEnd;

    public Employee employee;


    public Imputation() { }

    public Imputation withImputationId(long imputationId){
        this.imputationId = imputationId;
        return this;
    }

    public Imputation withWorkOrder(WorkOrder workOrder){
        this.workOrder = workOrder;
        return this;
    }

    public Imputation withAssign(Assign assign){
        this.assign = assign;
        return this;
    }

    public Imputation withImputationStart(long imputationStart){
        this.imputationStart = imputationStart;
        return this;
    }

    public Imputation withImputationEnd(long imputationEnd){
        this.imputationEnd = imputationEnd;
        return this;
    }

    public Imputation withEmployee(Employee employee) {
        this.employee = employee;
        return this;
    }

    public long getImputationId() {
        return imputationId;
    }

    public WorkOrder getWorkOrder() {
        return workOrder;
    }

    public Assign getAssign() {
        return assign;
    }

    public long getImputationStart() {
        return imputationStart;
    }

    public Employee getEmployee() {
        return employee;
    }

    public long getImputationEnd() {
        return imputationEnd;
    }

    public String getObservation() {
        //TODO
        return "";
    }
}

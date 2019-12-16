package com.vtec.terrassteel.common.model;

import java.io.Serializable;

public class Imputation implements Serializable {

    public long imputationId;

    public WorkOrder workOrder;

    public Assign assign;

    public long imputationTotalTime;

    public long imputationStart;

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

    public Imputation withTotalTime(long totalTime){
        this.imputationTotalTime = totalTime;
        return this;
    }

    public Imputation withImputationStart(long imputationStart){
        this.imputationStart = imputationStart;
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

    public long getImputationTotalTime() {
        return imputationTotalTime;
    }

    public long getImputationStart() {
        return imputationStart;
    }

    public Employee getEmployee() {
        return employee;
    }

    public long getImputationEnd() {
        //TODO
        return 0;
    }

    public String getObservation() {
        //TODO
        return "";
    }
}

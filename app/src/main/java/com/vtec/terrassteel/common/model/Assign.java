package com.vtec.terrassteel.common.model;

public class Assign {

    public long assignId;

    public WorkOrder withWorkOrder;

    public Employee employee;

    public boolean isWorking;

    public Assign(){}

    public Assign(WorkOrder withWorkOrder, Employee employee) {
        this.withWorkOrder = withWorkOrder;
        this.employee = employee;
    }

    public Assign withAssignId(long id){
        this.assignId = id;
        return this;
    }

    public Assign withWorkOrder(WorkOrder workOrder){
        this.withWorkOrder = workOrder;
        return this;
    }

    public Assign withEmployee(Employee employee){
        this.employee = employee;
        return this;
    }

    public Assign isWorking(boolean isWorking){
        this.isWorking = isWorking;
        return this;
    }

    public long getAssignId() {
        return assignId;
    }

    public WorkOrder getWorkOrder() {
        return withWorkOrder;
    }

    public Employee getEmployee() {
        return employee;
    }

    public boolean isWorking() {
        return isWorking;
    }
}

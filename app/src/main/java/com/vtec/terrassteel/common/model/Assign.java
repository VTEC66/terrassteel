package com.vtec.terrassteel.common.model;

public class Assign {

    public long assignId;

    public WorkOrder workOrder;

    public Employee employee;

    public boolean isWorking;

    public Assign(WorkOrder workOrder, Employee employee, boolean isWorking) {
        this.workOrder = workOrder;
        this.employee = employee;
        this.isWorking = isWorking;
    }

    public Assign withAssignId(long id){
        this.assignId = id;
        return this;
    }

    public Assign withWorkOrder(WorkOrder workOrder){
        this.workOrder = workOrder;
        return this;
    }

    public Assign withEmployee(Employee employee){
        this.employee = employee;
        return this;
    }

    public long getAssignId() {
        return assignId;
    }

    public WorkOrder getWorkOrder() {
        return workOrder;
    }

    public Employee getEmployee() {
        return employee;
    }

    public boolean isWorking() {
        return isWorking;
    }
}

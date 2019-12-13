package com.vtec.terrassteel.common.model;

import java.io.Serializable;

public class Pointing implements Serializable {

    public long pointingId;

    public WorkOrder workOrder;

    public Assign assign;

    public long pointingTotalTime;

    public long pointingStart;

    public Employee employee;


    public Pointing() { }

    public Pointing withPointingId(long pointingId){
        this.pointingId = pointingId;
        return this;
    }

    public Pointing withWorkOrder(WorkOrder workOrder){
        this.workOrder = workOrder;
        return this;
    }

    public Pointing withAssign(Assign assign){
        this.assign = assign;
        return this;
    }

    public Pointing withTotalTime(long totalTime){
        this.pointingTotalTime = totalTime;
        return this;
    }

    public Pointing withPointingStart(long pointingStart){
        this.pointingStart = pointingStart;
        return this;
    }

    public Pointing withEmployee(Employee employee) {
        this.employee = employee;
        return this;
    }

    public long getPointingId() {
        return pointingId;
    }

    public WorkOrder getWorkOrder() {
        return workOrder;
    }

    public Assign getAssign() {
        return assign;
    }

    public long getPointingTotalTime() {
        return pointingTotalTime;
    }

    public long getPointingStart() {
        return pointingStart;
    }

    public Employee getEmployee() {
        return employee;
    }

    public long getPointingEnd() {
        //TODO
        return 0;
    }

    public String getObservation() {
        //TODO
        return "";
    }
}

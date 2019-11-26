package com.vtec.terrassteel.common.model;

public class WorkOrder {

    public long workOrderId;
    public String workOrderReference;
    public String workOrderAffaire;
    public String workOrderProductType;
    public int workOrderAllocatedHour;
    public WorkOrderStatus workOrderStatus;

    public Construction construction;

    public WorkOrder() { }

    public WorkOrder withWorkOrderId(long workOrderId){
        this.workOrderId = workOrderId;
        return this;
    }

    public WorkOrder withWorkOrderReference(String workOrderReference){
        this.workOrderReference = workOrderReference;
        return this;
    }

    public WorkOrder withWorkOrderAffaire(String workOrderAffaire){
        this.workOrderAffaire = workOrderAffaire;
        return this;
    }

    public WorkOrder withWorkOrderProductType(String workOrderProductType){
        this.workOrderProductType = workOrderProductType;
        return this;
    }

    public WorkOrder withWorkOrderAllocatedHour(int workOrderAllocatedHour){
        this.workOrderAllocatedHour = workOrderAllocatedHour;
        return this;
    }

    public WorkOrder withworkOrderStatus(WorkOrderStatus workOrderStatus){
        this.workOrderStatus = workOrderStatus;
        return this;
    }

    public WorkOrder withConstruction(Construction construction){
        this.construction = construction;
        return this;
    }

    public long getWorkOrderId() {
        return workOrderId;
    }

    public String getWorkOrderReference() {
        return workOrderReference;
    }

    public String getWorkOrderAffaire() {
        return workOrderAffaire;
    }

    public String getWorkOrderProductType() {
        return workOrderProductType;
    }

    public int getWorkOrderAllocatedHour() {
        return workOrderAllocatedHour;
    }

    public WorkOrderStatus getWorkOrderStatus() {
        return workOrderStatus;
    }

    public Construction getConstruction() {
        return construction;
    }
}

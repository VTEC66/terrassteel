package com.vtec.terrassteel.common.model;

import java.io.Serializable;

public class Employee implements Serializable {

    public long employeeId;
    public String employeeName;
    public Job employeeJob;
    public String employeeAddress1;
    public String employeeAddress2;
    public String employeeZip;
    public String employeeCity;
    public String employeePhone;
    public String employeeEmail;

    public Employee withEmployeeId(long employeeId){
        this.employeeId = employeeId;
        return this;
    }

    public Employee withEmployeeName(String employeeName){
        this.employeeName = employeeName;
        return this;
    }

    public Employee withEmployeeJob(Job employeeJob){
        this.employeeJob = employeeJob;
        return this;
    }

    public Employee withEmployeeAddress1(String employeeAddress1){
        this.employeeAddress1 = employeeAddress1;
        return this;
    }

    public Employee withEmployeeAddress2(String employeeAddress2){
        this.employeeAddress2 = employeeAddress2;
        return this;
    }

    public Employee withEmployeeZip(String employeeZip){
        this.employeeZip = employeeZip;
        return this;
    }

    public Employee withEmployeeCity(String employeeCity){
        this.employeeCity = employeeCity;
        return this;
    }

    public Employee withEmployeePhone(String employeePhone){
        this.employeePhone = employeePhone;
        return this;
    }

    public Employee withEmployeeEmail(String employeeEmail){
        this.employeeEmail = employeeEmail;
        return this;
    }

    public long getEmployeeId() {
        return employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }


    public Job getEmployeeJob() {
        return employeeJob;
    }

    public String getEmployeeAddress1() {
        return employeeAddress1;
    }

    public String getEmployeeAddress2() {
        return employeeAddress2;
    }

    public String getEmployeeZip() {
        return employeeZip;
    }

    public String getEmployeeCity() {
        return employeeCity;
    }

    public String getEmployeePhone() {
        return employeePhone;
    }

    public String getEmployeeEmail() {
        return employeeEmail;
    }
}

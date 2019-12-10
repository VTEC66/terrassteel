package com.vtec.terrassteel.main.ui.assign.callback;

import com.vtec.terrassteel.common.model.Employee;

public interface AssignEmployeeCallback {

    public void removeEmployee(Employee employee);

    public void assignEmployee(Employee employee);
}

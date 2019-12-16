package com.vtec.terrassteel.main.ui.imputation.callback;

import com.vtec.terrassteel.common.model.Assign;
import com.vtec.terrassteel.common.model.WorkOrder;

public interface ImputationCallback {

    void addAssignToWorkOrder(WorkOrder workOrder);

    void workOrderSelected(WorkOrder workOrder);
}

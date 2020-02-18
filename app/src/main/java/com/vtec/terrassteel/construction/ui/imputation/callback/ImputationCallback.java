package com.vtec.terrassteel.construction.ui.imputation.callback;

import com.vtec.terrassteel.common.model.WorkOrder;

public interface ImputationCallback {

    void addAssignToWorkOrder(WorkOrder workOrder);

    void workOrderSelected(WorkOrder workOrder);
}

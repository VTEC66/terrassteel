package com.vtec.terrassteel.main.ui.pointing.callback;

import com.vtec.terrassteel.common.model.Assign;
import com.vtec.terrassteel.common.model.WorkOrder;

public interface PointingCallback {

    void addAssignToWorkOrder(WorkOrder workOrder);

    void workOrderSelected(WorkOrder workOrder);
}

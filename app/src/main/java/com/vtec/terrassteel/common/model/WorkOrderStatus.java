package com.vtec.terrassteel.common.model;

import com.vtec.terrassteel.R;

public enum WorkOrderStatus {

    IN_PROGRESS(R.string.workorder_status_inprogress),
    FINISHED(R.string.workorder_status_finished);

    private int ressourceReference;

    WorkOrderStatus(int ressourceReference) {
        this.ressourceReference = ressourceReference;
    }

    public int getRessourceReference(){
        return ressourceReference;
    }
}

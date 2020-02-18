package com.vtec.terrassteel.common.model;

import com.vtec.terrassteel.R;

public enum OrderStatus {

    IN_PROGRESS(R.string.workorder_status_inprogress),
    FINISHED(R.string.workorder_status_finished);

    private int ressourceReference;

    OrderStatus(int ressourceReference) {
        this.ressourceReference = ressourceReference;
    }

    public int getRessourceReference(){
        return ressourceReference;
    }
}

package com.vtec.terrassteel.common.model;

import com.vtec.terrassteel.R;

public enum ConstructionStatus {

    IN_PROGRESS(R.string.construction_status_inprogress),
    FINISHED(R.string.construction_status_finished);

    private int ressourceReference;

    ConstructionStatus(int ressourceReference) {
        this.ressourceReference = ressourceReference;
    }

    public int getRessourceReference(){
        return ressourceReference;
    }
}

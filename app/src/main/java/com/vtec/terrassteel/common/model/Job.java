package com.vtec.terrassteel.common.model;

import com.vtec.terrassteel.R;

public enum Job {

    CONSTRUCTION_MANAGER(R.string.construction_manager),
    POSEUR(R.string.poseur),
    COMMERCIAL(R.string.commercial);

    private int ressourceReference;

    Job(int ressourceReference) {
        this.ressourceReference = ressourceReference;
    }

    public int getRessourceReference(){
        return ressourceReference;
    }
}

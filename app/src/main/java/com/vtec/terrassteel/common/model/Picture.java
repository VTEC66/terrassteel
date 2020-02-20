package com.vtec.terrassteel.common.model;

import java.io.Serializable;

public class Picture implements Serializable {



    private long pictureId;
    private String pictureName;

    public Order order;

    public long getPictureId() {
        return pictureId;
    }

    public String getPictureName() {
        return pictureName;
    }

    public Order getOrder() {
        return order;
    }

    public Picture withId(long id){
        this.pictureId = id;
        return this;
    }

    public Picture withPictureName(String pictureName) {
        this.pictureName = pictureName;
        return this;
    }

    public Picture withOrder(Order order) {
        this.order = order;
        return this;
    }



}

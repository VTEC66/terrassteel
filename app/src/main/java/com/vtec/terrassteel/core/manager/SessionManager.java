package com.vtec.terrassteel.core.manager;

import com.vtec.terrassteel.common.model.Construction;
import com.vtec.terrassteel.common.model.Order;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SessionManager {

    private String token ="";
    private boolean isLoggedIn;

    private Construction construction;
    private Order order;


    @Inject
    public SessionManager() { }

    public void saveAccessToken(String token){
        this.token = token;
    }

    public String getAccessToken(){
        return token;
    }


    public void saveIsLoggedIn(boolean logged){
        this.isLoggedIn = logged;
    }

    public boolean isLoggedIn(){
        return isLoggedIn;
    }

    public void setConstruction(Construction construction) {
        this.construction = construction;
    }

    public Construction getContruction(){
        return construction;
    }

    public void setCurrentOrder(Order order) {
        this.order = order;
    }

    public Order getOrder(){
        return order;
    }
}

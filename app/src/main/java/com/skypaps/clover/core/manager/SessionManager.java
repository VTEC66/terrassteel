package com.skypaps.clover.core.manager;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SessionManager {

    private String token ="";
    private boolean isLoggedIn;


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

}

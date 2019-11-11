package com.vtec.terrassteel.services;

import com.google.gson.Gson;
import com.vtec.terrassteel.core.network.ApiEndPoint;
import com.vtec.terrassteel.core.service.AbstractService;

import javax.inject.Inject;

public class UserService extends AbstractService {


    @Inject
    public UserService(ApiEndPoint apiEndPoint, Gson gson) {
        super(apiEndPoint, gson);
    }

    /*public AppConfig getConfig() throws IOException, BackendException {
        Call<AppConfig> call = apiEndPoint.getConfig();
        return execute(call);
    }

    public SignInResponse signIn(String email, String password) throws IOException, BackendException {
        Call<SignInResponse> call = apiEndPoint.signIn(new SignInPayload(email, password));
        return execute(call);
    }

    public SignUpResponse signUp(String email, String password, String fidNumber) throws IOException, BackendException {
        Call<SignUpResponse> call = apiEndPoint.signUp(new SignUpPayload(email, password, fidNumber));
        return execute(call);
    }*/
}

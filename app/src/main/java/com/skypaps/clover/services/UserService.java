package com.skypaps.clover.services;

import android.os.Environment;

import com.google.gson.Gson;
import com.skypaps.clover.core.network.ApiEndPoint;
import com.skypaps.clover.core.service.AbstractService;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;

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

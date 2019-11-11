package com.vtec.terrassteel.core.service;

import com.google.gson.Gson;
import com.vtec.terrassteel.core.model.exception.BackendException;
import com.vtec.terrassteel.core.network.ApiEndPoint;

import java.io.IOException;
import java.util.Objects;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Response;

public abstract class AbstractService {

    @Inject
    protected ApiEndPoint apiEndPoint;

    @Inject
    protected Gson gson;

    public AbstractService(ApiEndPoint apiEndPoint, Gson gson) {
        this.apiEndPoint = apiEndPoint;
        this.gson = gson;
    }

    protected <R> R execute(Call<R> call) throws IOException, BackendException {
        Response<R> response = call.execute();
        if (response.isSuccessful()) {
            return response.body();
        }

        throw new BackendException(response.code(), Objects.requireNonNull(response.errorBody()).string());
    }
}

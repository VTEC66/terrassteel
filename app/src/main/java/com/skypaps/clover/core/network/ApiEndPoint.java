package com.skypaps.clover.core.network;



import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

import static com.skypaps.clover.core.Config.DEFAULT_ENDPOINT;

public interface ApiEndPoint {

    String V1 = "/v1";
    String AUTH = "/auth";
    String LOGIN = "/login";
    String REGISTER = "/register";


    /*@GET(DEFAULT_ENDPOINT + V1 + "/config")
    Call<AppConfig> getConfig();

    @POST(DEFAULT_ENDPOINT + V1 + AUTH + LOGIN)
    Call<SignInResponse> signIn(@Body SignInPayload payload);

    @POST(DEFAULT_ENDPOINT + V1 + AUTH + REGISTER)
    Call<SignUpResponse> signUp(@Body SignUpPayload payload);*/


}

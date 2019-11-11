package com.vtec.terrassteel.core.network;


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

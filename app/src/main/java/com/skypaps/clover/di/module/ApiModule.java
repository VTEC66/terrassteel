package com.skypaps.clover.di.module;

import com.google.gson.Gson;
import com.skypaps.clover.core.model.annotation.ApplicationScope;
import com.skypaps.clover.core.network.ApiEndPoint;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module(includes = {NetworkModule.class, GsonModule.class})
public class ApiModule {

    private final String baseUrl;

    public ApiModule(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    /* Morning */
    @Provides
    @ApplicationScope
    public ApiEndPoint providesApiEndPoint(Retrofit retrofit) {
        return retrofit.create(ApiEndPoint.class);
    }

    @Provides
    @ApplicationScope
    public Retrofit provideRetrofitApi(Gson gson, OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .build();
    }
}

package com.vtec.terrassteel.di.module;

import android.app.Application;
import android.os.Build;

import com.google.gson.Gson;
import com.vtec.terrassteel.BuildConfig;
import com.vtec.terrassteel.core.manager.PreferenceManager;
import com.vtec.terrassteel.core.manager.SessionManager;
import com.vtec.terrassteel.core.model.annotation.ApplicationScope;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.CertificatePinner;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;

@Module(includes = {GsonModule.class})
public class NetworkModule {


    @Provides
    @ApplicationScope
    PreferenceManager providesSharedPreferences(Application application, Gson gson) {
        return new PreferenceManager(application, gson);
    }

    @Provides
    @ApplicationScope
    Cache provideOkHttpCache(Application application) {
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        return new Cache(application.getCacheDir(), cacheSize);
    }

    @Provides
    @ApplicationScope
    public CertificatePinner certificatePinner() {
        return new CertificatePinner.Builder()
                .add("clover", "sha256/AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA=")
                .build();
    }

    @Provides
    @ApplicationScope
    CookieJar provideCookieJar(final PreferenceManager preferenceHelper) {
        return new CookieJar() {
            @Override
            public void saveFromResponse(@NonNull HttpUrl url, @NonNull List<Cookie> cookies) {

            }

            @Override
            public List<Cookie> loadForRequest(@NonNull HttpUrl url) {

                return new ArrayList<>();
            }
        };
    }

    @Provides
    @ApplicationScope
    OkHttpClient provideOkHttpClient(CookieJar cookieJar, final SessionManager sessionManager) {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .cookieJar(cookieJar)
                //.addInterceptor(new RemoveCharacterInterceptor())
                .addInterceptor(chain -> {
                    Request original = chain.request();
                    Request request = original.newBuilder()
                            .method(original.method(), original.body())
                            .build();
                    return chain.proceed(request);
                })
                .addInterceptor(chain -> {
                    Request original = chain.request();
                    Request.Builder requestBuilder = original.newBuilder()
                            .addHeader("x-access-token", sessionManager.getAccessToken())
                            .method(original.method(), original.body());


                    if (BuildConfig.DEBUG) {
                        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                    }

                    return chain.proceed(requestBuilder.build());
                })
                .addInterceptor(chain -> {
                    Request original = chain.request();
                    Request request = original.newBuilder()
                            .header("User-Agent", "Android " + Build.VERSION.RELEASE + "; Clover " + BuildConfig.VERSION_NAME)
                            .method(original.method(), original.body())
                            .build();
                    return chain.proceed(request);
                });

        if (BuildConfig.DEBUG) {
            builder.addInterceptor(loggingInterceptor);
        }


        return builder.build();
    }

}

package com.skypaps.clover.core;

import android.app.Application;
import android.content.Context;

import com.karumi.dexter.BuildConfig;
import com.karumi.dexter.Dexter;
import com.skypaps.clover.core.di.ApplicationComponent;
import com.skypaps.clover.core.di.DaggerApplicationComponent;
import com.skypaps.clover.core.manager.SessionManager;
import com.skypaps.clover.di.module.ApiModule;
import com.skypaps.clover.di.module.AppModule;

import static com.skypaps.clover.core.Config.BASE_URL;

public class CloverApplication extends Application {

    private static ApplicationComponent mAppComponent;

    private static Context context;


    Runnable initAppRunnable = new Runnable() {
        @Override
        public void run() {
            Dexter.initialize(getAppContext());
        }
    };

    public static ApplicationComponent getComponent() {
        return mAppComponent;
    }

    public static ApplicationComponent getComponent(Context context) {
        return ((CloverApplication) context.getApplicationContext()).getComponent();
    }

    public static Context getAppContext() {
        return CloverApplication.context;
    }

    public void onCreate() {
        super.onCreate();

        CloverApplication.context = getApplicationContext();
        mAppComponent = DaggerApplicationComponent
                .builder()
                .appModule(new AppModule(this, new SessionManager()))
                .apiServiceModule(new ApiModule(BASE_URL))
                .build();


        Thread thread = new Thread(initAppRunnable);
        thread.start();

    }



}

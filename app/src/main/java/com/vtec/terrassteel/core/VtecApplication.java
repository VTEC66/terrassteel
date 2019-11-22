package com.vtec.terrassteel.core;

import android.app.Application;
import android.content.Context;

import com.karumi.dexter.Dexter;
import com.vtec.terrassteel.core.di.ApplicationComponent;
import com.vtec.terrassteel.core.di.DaggerApplicationComponent;
import com.vtec.terrassteel.database.DatabaseManager;
import com.vtec.terrassteel.core.manager.SessionManager;
import com.vtec.terrassteel.di.module.ApiModule;
import com.vtec.terrassteel.di.module.AppModule;

public class VtecApplication extends Application {

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
        return ((VtecApplication) context.getApplicationContext()).getComponent();
    }

    public static Context getAppContext() {
        return VtecApplication.context;
    }

    public void onCreate() {
        super.onCreate();

        VtecApplication.context = getApplicationContext();
        mAppComponent = DaggerApplicationComponent
                .builder()
                .appModule(new AppModule(this, new SessionManager()))
                .apiServiceModule(new ApiModule(Config.BASE_URL))
                .build();


        Thread thread = new Thread(initAppRunnable);
        thread.start();

        DatabaseManager.getInstance(this);
    }



}

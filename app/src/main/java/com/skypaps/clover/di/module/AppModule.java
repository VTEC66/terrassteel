package com.skypaps.clover.di.module;

import android.app.Application;
import android.content.Context;

import com.skypaps.clover.core.manager.SessionManager;
import com.skypaps.clover.core.model.annotation.ApplicationScope;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    Application application;
    SessionManager sessionManager;

    public AppModule(Application application, SessionManager sessionManager) {
        this.sessionManager = sessionManager;
        this.application = application;
    }

    @Provides
    Application providesApplication() {
        return application;
    }

    @Provides
    Context providesContext() {
        return application.getApplicationContext();
    }

    @Provides
    @ApplicationScope
    SessionManager providesSessionManager() {return sessionManager;}

}

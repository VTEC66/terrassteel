package com.skypaps.clover.di.module;

import dagger.Module;

@Module(includes = {
        AppModule.class,
        NetworkModule.class,
        GsonModule.class})
public class ServiceModule {

}

package com.vtec.terrassteel.di.module;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vtec.terrassteel.core.model.annotation.ApplicationScope;
import com.vtec.terrassteel.core.model.annotation.SkipSerialisation;

import dagger.Module;
import dagger.Provides;

@Module
public class GsonModule {

    @Provides
    @ApplicationScope
    public Gson gson() {
        return new GsonBuilder()
                //.registerTypeAdapter(FinancialCaseWrapper.class, new FinancialCaseDeserializer())
                .addSerializationExclusionStrategy(new ExclusionStrategy() {

                    @Override
                    public boolean shouldSkipField(FieldAttributes f) {
                        return f.getAnnotation(SkipSerialisation.class) != null;
                    }

                    @Override
                    public boolean shouldSkipClass(Class<?> clazz) {
                        return false;
                    }
                })
                .create();
    }
}

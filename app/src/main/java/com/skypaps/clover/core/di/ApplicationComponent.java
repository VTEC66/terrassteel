package com.skypaps.clover.core.di;

import android.app.Application;

import com.skypaps.clover.core.model.Data;
import com.skypaps.clover.core.model.annotation.ApplicationScope;
import com.skypaps.clover.core.task.AbstractTask;
import com.skypaps.clover.core.ui.AbstractActivity;
import com.skypaps.clover.core.ui.AbstractFragment;
import com.skypaps.clover.di.module.ApiModule;
import com.skypaps.clover.di.module.AppModule;
import com.skypaps.clover.services.UserService;

import dagger.Component;
import dagger.android.AndroidInjector;

@ApplicationScope
@Component(modules = {
        AppModule.class,
        ApiModule.class
})
public interface ApplicationComponent extends AndroidInjector<Application> {

    UserService getUserService();

    void inject(AbstractActivity activity);

    void inject(AbstractTask<Data> abstractTask);

    void inject(AbstractFragment abstractFragment);

    void inject(UserService userService);



    @Component.Builder
    interface Builder {
        ApplicationComponent build();

        Builder appModule(AppModule appModule);

        Builder apiServiceModule(ApiModule apiModule);
    }

}

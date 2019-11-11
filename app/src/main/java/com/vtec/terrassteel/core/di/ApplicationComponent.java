package com.vtec.terrassteel.core.di;

import android.app.Application;

import com.vtec.terrassteel.core.model.Data;
import com.vtec.terrassteel.core.model.annotation.ApplicationScope;
import com.vtec.terrassteel.core.task.AbstractTask;
import com.vtec.terrassteel.core.ui.AbstractActivity;
import com.vtec.terrassteel.core.ui.AbstractFragment;
import com.vtec.terrassteel.di.module.ApiModule;
import com.vtec.terrassteel.di.module.AppModule;
import com.vtec.terrassteel.services.UserService;

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

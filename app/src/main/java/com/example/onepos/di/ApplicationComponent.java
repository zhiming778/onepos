package com.example.onepos.di;

import android.app.Application;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;
//TODO modules don't need to be listed here?
@Component(modules = {AndroidInjectionModule.class, ViewModelModule.class, ActivityBuilder.class})
public interface ApplicationComponent extends AndroidInjector<OnePosApplication> {

    void inject(Application application);

    @Component.Builder
    interface Builder{
        @BindsInstance
        Builder application(Application application);
        ApplicationComponent build();
    }
}

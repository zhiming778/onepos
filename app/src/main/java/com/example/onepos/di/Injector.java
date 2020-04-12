package com.example.onepos.di;

import android.app.Application;

public class Injector{
    private Injector() {

    }

    private static ApplicationComponent applicationComponent;

    public static ApplicationComponent getInstance(Application application) {
        if (applicationComponent == null) {
            applicationComponent = DaggerApplicationComponent
                    .builder()
                    .application(application)
                    .build();
        }
        return applicationComponent;
    }

    public static ApplicationComponent get() {
        if (applicationComponent == null) {
            throw new NullPointerException("applicationComponent is null.");
        }
        else
            return applicationComponent;
    }
}

package com.example.onepos.di;

import com.example.onepos.view.activity.MainActivity;
import com.example.onepos.view.activity.ModifyActivity;
import com.example.onepos.view.activity.OfficeActivity;
import com.example.onepos.view.activity.OrderActivity;
import com.example.onepos.viewmodel.ModifyViewModel;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = {OrderActivityModule.class})
    abstract OrderActivity contributeOrderActivityInjector();

    @ContributesAndroidInjector(modules = {OfficeActivityModule.class, OrderActivityModule.class})
    abstract OfficeActivity contributeOfficeActivityInjector(); //TODO check back later

    @ContributesAndroidInjector(modules = {MainActivityModule.class, OrderActivityModule.class})
    abstract MainActivity contributeMainActivityInjector();

    @ContributesAndroidInjector(modules = {ModifyActivityModule.class, OrderActivityModule.class})
    abstract ModifyActivity contributeModifyActivityInjector();
}

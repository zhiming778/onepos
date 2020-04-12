package com.example.onepos.di;

import android.app.Application;

import androidx.lifecycle.ViewModel;

import com.example.onepos.repo.ModifyRepository;
import com.example.onepos.viewmodel.ModifyViewModel;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;

@Module
public class ModifyActivityModule {
    @Provides
    @IntoMap
    @ViewModelKey(ModifyViewModel.class)
    ViewModel providesModifyViewModel(ModifyRepository repository, Application application) {
        return new ModifyViewModel(repository, application);
    }
}

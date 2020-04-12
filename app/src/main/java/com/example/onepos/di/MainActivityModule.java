package com.example.onepos.di;

import android.app.Application;

import androidx.lifecycle.ViewModel;

import com.example.onepos.repo.MainRepository;
import com.example.onepos.viewmodel.MainViewModel;
import com.example.onepos.viewmodel.OfficeViewModel;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;

@Module
public class MainActivityModule {

    @Provides
    @IntoMap
    @ViewModelKey(MainViewModel.class)
    ViewModel provideMainViewModel(Application application, MainRepository repository) {
        return new MainViewModel(application, repository);
    }
}

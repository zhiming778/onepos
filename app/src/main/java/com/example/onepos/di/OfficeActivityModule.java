package com.example.onepos.di;

import android.app.Application;

import androidx.lifecycle.ViewModel;

import com.example.onepos.repo.OfficeRepository;
import com.example.onepos.viewmodel.OfficeViewModel;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;

@Module
public class OfficeActivityModule {
    @Provides
    @IntoMap
    @ViewModelKey(OfficeViewModel.class)
    ViewModel provideOfficeViewModel(Application application, OfficeRepository repository) {
        return new OfficeViewModel(application, repository);
    }

}

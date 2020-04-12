package com.example.onepos.di;

import android.app.Application;

import androidx.lifecycle.ViewModel;

import com.example.onepos.repo.OrderRepository;
import com.example.onepos.viewmodel.OrderViewModel;
import com.example.onepos.viewmodel.ViewModelFactory;

import java.util.Map;

import javax.inject.Provider;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;

@Module
public class OrderActivityModule {

    @Provides
    @IntoMap
    @ViewModelKey(OrderViewModel.class)
    ViewModel provideOrderViewModel(Application application, OrderRepository repository) {
        return new OrderViewModel(application, repository);
    }

    @Provides
    ViewModelFactory provideViewModelFactory(Map<Class<? extends ViewModel>, Provider<ViewModel>> providerMap) {
        return new ViewModelFactory(providerMap);
    }
}

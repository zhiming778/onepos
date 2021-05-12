package com.example.onepos.di;

import android.app.Application;

import com.example.onepos.model.api.RemoteDataSource;
import com.example.onepos.repo.MainRepository;
import com.example.onepos.repo.ModifyRepository;
import com.example.onepos.repo.OfficeRepository;
import com.example.onepos.repo.OrderRepository;

import dagger.Module;
import dagger.Provides;

@Module
public class ViewModelModule {

    @Provides
    public OrderRepository provideOrderRepository(Application application, RemoteDataSource remoteDataSource) {
        return new OrderRepository(application, remoteDataSource);
    }

    @Provides
    public OfficeRepository provideOfficeRepository(Application application) {
        return new OfficeRepository(application);
    }

    @Provides
    public MainRepository provideMainRepository(Application application) {
        return new MainRepository(application);
    }

    @Provides
    public ModifyRepository provideModifyRepository(Application application) {
        return new ModifyRepository(application);
    }

    @Provides
    public RemoteDataSource provideRemoteDataSource() {
        return new RemoteDataSource();
    }
}

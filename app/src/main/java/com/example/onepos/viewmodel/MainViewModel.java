package com.example.onepos.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.onepos.model.TimeClock;
import com.example.onepos.model.local.PosDatabase;
import com.example.onepos.repo.MainRepository;
import com.example.onepos.util.MLog;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.schedulers.SchedulerPoolFactory;
import io.reactivex.schedulers.Schedulers;

public class MainViewModel extends BaseViewModel<MainRepository> {

    private CompositeDisposable disposables;
    private MainRepository repository;

    @Inject
    public MainViewModel(@NonNull Application application, MainRepository repository) {
        super(repository, application);
        disposables = new CompositeDisposable();
        this.repository = repository;
    }

    public void punchIn(String password) {
        Disposable disposable = repository.punchInOrOut(TimeClock.TYPE_PUNCH_IN, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(ifSuccess->{
                    if (ifSuccess)
                        liveFlag.setValue(0);
                    else
                        liveFlag.setValue(1);
                });
        disposables.add(disposable);
    }

    public void punchOut(String password) {
        Disposable disposable = repository.punchInOrOut(TimeClock.TYPE_PUNCH_OUT, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(ifSuccess->{
                    if (ifSuccess)
                        liveFlag.setValue(2);
                    else
                        liveFlag.setValue(3);
                });
        disposables.add(disposable);
    }


    @Override
    protected void onCleared() {
        super.onCleared();
    }
}

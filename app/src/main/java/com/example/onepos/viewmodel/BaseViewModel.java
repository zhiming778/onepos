package com.example.onepos.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.onepos.model.local.PosDatabase;
import com.example.onepos.repo.BaseRepository;
import com.example.onepos.util.MLog;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public abstract class BaseViewModel<T extends BaseRepository> extends AndroidViewModel {
    T repository;
    CompositeDisposable disposables;
    private boolean eligible;
    MutableLiveData<Integer> liveFlag; //Use it as Message. 0: punch in 1: punch in error 2: punch out 3: punch out error 4: check permission level

    BaseViewModel(T repository, Application application) {
        super(application);
        this.repository = repository;
        disposables = new CompositeDisposable();
        eligible = false;
    }

    public void isTitleEligible(String password, int requiredLevel) {
        Disposable disposable = repository.isTitleEligible(password, requiredLevel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(eligible->{
                    this.eligible = eligible;
                    liveFlag.setValue(4);
                });
        disposables.add(disposable);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
        PosDatabase.clearInstance();
    }

    public MutableLiveData<Integer> getLiveFlag() {
        if (liveFlag==null||!liveFlag.hasObservers())
            liveFlag = new MutableLiveData<>();
        return liveFlag;
    }

    public boolean isEligible() {
        return eligible;
    }
}

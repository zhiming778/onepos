package com.example.onepos.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.onepos.model.Staff;
import com.example.onepos.model.local.PosDatabase;
import com.example.onepos.repo.BaseRepository;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public abstract class BaseViewModel<T extends BaseRepository> extends AndroidViewModel {
    T repository;
    CompositeDisposable disposables;
    int lang;
    Staff staff;
    /*Use it as Message.
    0: punch in
    1: punch in error
    2: punch out
    3: punch out error
    4: action approved
    5: don't have permission
    6: refresh receiptAdapter
    7: finish loading map
    8: suggest addresses
    9: order history
    */
    MutableLiveData<Integer> liveFlag;


    BaseViewModel(T repository, Application application) {
        super(application);
        this.repository = repository;
        disposables = new CompositeDisposable();
    }

    public void isTitleEligible(String password, int requiredLevel) {
        Disposable disposable = repository.getStaff(password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(staff->{
                    final boolean eligible = staff.getTitle() >= requiredLevel;
                    if (eligible) {
                        this.staff = staff;
                        liveFlag.setValue(4);
                    }
                    else
                        liveFlag.setValue(5);
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


    public void setLang(int lang) {
        this.lang = lang;
    }

    public int getLang() {
        return lang;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }
}

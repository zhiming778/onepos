package com.example.onepos.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.onepos.model.Staff;
import com.example.onepos.repo.OfficeRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class OfficeViewModel extends BaseViewModel<OfficeRepository> {

    private MutableLiveData<List<Staff>> liveListStaff;
    private MutableLiveData<Staff> liveStaff;

    @Inject
    public OfficeViewModel(@NonNull Application application, OfficeRepository repository) {
        super(repository, application);
        disposables = new CompositeDisposable();
        liveListStaff = new MutableLiveData<>();
    }

    public void getAllStaff() {
        Disposable disposable = repository
                .getAllStaff()
                .subscribeOn(io.reactivex.rxjava3.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(liveListStaff::setValue);
        disposables.add(disposable);
    }

    public void getStaffById(long id) {
        Disposable disposable = repository
                .getStaffById(id)
                .subscribeOn(io.reactivex.rxjava3.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(staff -> {
                    liveStaff.setValue(staff);
                });
        disposables.add(disposable);
    }

    public void saveStaff(String name, int title, int lang, long dayOfBirth, String phoneNum, String address, String ssn,String password) {
        Staff staff = new Staff(liveStaff.getValue()==null?0:liveStaff.getValue().getId(), name, title, lang, phoneNum, dayOfBirth, address, ssn, password);
        Disposable disposable = repository.saveStaff(staff)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
        disposables.add(disposable);
    }

    public void deleteCurrentStaff() {
        Disposable disposable = repository
                .deleteStaffById(liveStaff.getValue().getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
        disposables.add(disposable);
    }

    public MutableLiveData<List<Staff>> getLiveListStaff() {
        return liveListStaff;
    }

    public MutableLiveData<Staff> getLiveStaff() {
        if (liveStaff==null||!liveStaff.hasObservers())
            liveStaff = new MutableLiveData<>();
        return liveStaff;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }

}

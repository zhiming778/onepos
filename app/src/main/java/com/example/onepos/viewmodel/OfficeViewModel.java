package com.example.onepos.viewmodel;

import android.app.Application;
import android.database.Cursor;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.onepos.model.Staff;
import com.example.onepos.model.local.PosDatabase;
import com.example.onepos.repo.OfficeRepository;
import com.example.onepos.util.MLog;

import javax.inject.Inject;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.schedulers.SchedulerPoolFactory;
import io.reactivex.schedulers.Schedulers;

public class OfficeViewModel extends BaseViewModel<OfficeRepository> {

    private MutableLiveData<Cursor> liveCursorStaff;
    private MutableLiveData<Staff> liveStaff;

    @Inject
    public OfficeViewModel(@NonNull Application application, OfficeRepository repository) {
        super(repository, application);
        disposables = new CompositeDisposable();
        liveCursorStaff = new MutableLiveData<>();
    }

    public void getAllStaff() {
        Disposable disposable = repository
                .getAllStaff()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(cursor->{
                    Cursor c = liveCursorStaff.getValue();
                    if (c!=null)
                        c.close();
                    liveCursorStaff.setValue(cursor);
                });
        disposables.add(disposable);
    }

    public void getStaffById(long id) {
        Disposable disposable = repository
                .getStaffById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(staff -> {
                    liveStaff.setValue(staff);
                });
        disposables.add(disposable);
    }

    public void saveStaff(String name, int title, long dayOfBirth, String phoneNum, String address, String ssn,String password) {
        Staff staff = new Staff(liveStaff==null||liveStaff.getValue()==null?0:liveStaff.getValue().getId(), name, title, phoneNum, dayOfBirth, address, ssn, password);
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

    public MutableLiveData<Cursor> getLiveCursorStaff() {
        return liveCursorStaff;
    }

    public MutableLiveData<Staff> getLiveStaff() {
        if (liveStaff==null||!liveStaff.hasObservers())
            liveStaff = new MutableLiveData<>();
        return liveStaff;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables = null;
        if (liveCursorStaff.getValue()!=null)
            liveCursorStaff.getValue().close();
        liveCursorStaff.setValue(null);
        liveCursorStaff = null;
        liveStaff = null;
    }
}

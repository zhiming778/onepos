package com.example.onepos.viewmodel;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;

import com.example.onepos.model.CustomerOrder;
import com.example.onepos.repo.ModifyRepository;
import com.example.onepos.util.MLog;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ModifyViewModel extends BaseViewModel<ModifyRepository>{
    private MutableLiveData<List<CustomerOrder>> liveListCurstomerOrder;

    @Inject
    public ModifyViewModel(ModifyRepository repository, Application application) {
        super(repository, application);
        liveListCurstomerOrder = new MutableLiveData<>();
    }

    public void getAllCustomerOrder(long startMillis, long endMills) {
        getCustomerOrderByOrderType(-1, startMillis, endMills);
    }
    public void getCustomerOrderByOrderType(int orderType, long startMillis, long endMills) {
        Disposable disposable = repository.getCustomerOrderByOrderType(orderType, startMillis, endMills)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list->{
                    liveListCurstomerOrder.setValue(list);
                });
        disposables.add(disposable);
    }
    public MutableLiveData<List<CustomerOrder>> getLiveListCurstomerOrder() {
        return liveListCurstomerOrder;
    }

}

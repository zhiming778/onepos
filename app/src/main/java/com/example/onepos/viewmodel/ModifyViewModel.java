package com.example.onepos.viewmodel;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;

import com.example.onepos.model.CustomerOrder;
import com.example.onepos.repo.ModifyRepository;
import com.example.onepos.util.DateUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class ModifyViewModel extends BaseViewModel<ModifyRepository>{
    private MutableLiveData<List<CustomerOrder>> liveListCurstomerOrder;
    private List<CustomerOrder> allCustomerOrders;

    @Inject
    public ModifyViewModel(ModifyRepository repository, Application application) {
        super(repository, application);
        liveListCurstomerOrder = new MutableLiveData<>();
        allCustomerOrders = new ArrayList<>();
    }

    public void getCustomerOrderByDate(int type, long dateMillis) {
        long startMillis = DateUtil.getStartMillis(dateMillis);
        long endMillis = DateUtil.getEndMillis(dateMillis);
        Disposable disposable = repository.getCustomerOrderByDate(startMillis, endMillis)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list->{
                    allCustomerOrders = list;
                    liveListCurstomerOrder.setValue(getCustomerOrderByOrderType(type));
                });
        disposables.add(disposable);
    }

    public List<CustomerOrder> getCustomerOrderByOrderType(int type) {
        if (type == -1) {
            return allCustomerOrders;
        }
        else
            if(type>=CustomerOrder.ORDER_TYPE_DINE_IN && type<=CustomerOrder.ORDER_TYPE_DELIVERY){
                return allCustomerOrders.stream()
                        .filter(customerOrder -> customerOrder.getOrderType()==type)
                        .collect(Collectors.toList());
            }
        return null;
    }

    public CustomerOrder findOrder(long id) {
        CustomerOrder customerOrder = null;
        for (CustomerOrder order: allCustomerOrders) {
            if (order.getId() == id) {
                customerOrder = order;
                break;
            }
        }
        return customerOrder;
    }
    public MutableLiveData<List<CustomerOrder>> getLiveListCurstomerOrder() {
        return liveListCurstomerOrder;
    }

}

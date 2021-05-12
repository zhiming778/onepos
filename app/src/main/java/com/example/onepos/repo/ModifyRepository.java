package com.example.onepos.repo;

import android.app.Application;

import com.example.onepos.model.CustomerOrder;
import com.example.onepos.model.local.CustomerOrderDAO;
import com.example.onepos.model.local.PosDatabase;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class ModifyRepository extends BaseRepository{

    @Inject
    public ModifyRepository(Application application) {
        super(application);
    }

    public Single<List<CustomerOrder>> getCustomerOrderByDate(long startMillis, long endMillis) {
        return Single.create(emitter -> {
            CustomerOrderDAO dao = PosDatabase.getInstance(application).customerOrderDAO();
            final List<CustomerOrder> customerOrders = dao.getCustomerOrderByDate(startMillis, endMillis);
            emitter.onSuccess(customerOrders);
        });
    }
}

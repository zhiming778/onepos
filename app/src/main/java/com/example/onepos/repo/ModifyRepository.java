package com.example.onepos.repo;

import android.app.Application;

import com.example.onepos.model.CustomerOrder;
import com.example.onepos.model.local.CustomerOrderDAO;
import com.example.onepos.model.local.PosDatabase;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;

public class ModifyRepository extends BaseRepository{

    @Inject
    public ModifyRepository(Application application) {
        super(application);
    }

    public Single<List<CustomerOrder>> getCustomerOrderByOrderType(int orderType, long startMillis, long endMillis) {
        return Single.create(emitter -> {
            CustomerOrderDAO dao = PosDatabase.getInstance(application).customerOrderDAO();
            if (orderType==-1)
                emitter.onSuccess(dao.getCustomerOrderByDate(startMillis, endMillis));
            else
                if (orderType>=0&&orderType<4)
                    emitter.onSuccess(dao.getCustomerOrderByOrderTypeAndDate(orderType, startMillis, endMillis));
                else
                    emitter.onError(new Throwable());
        });
    }
}

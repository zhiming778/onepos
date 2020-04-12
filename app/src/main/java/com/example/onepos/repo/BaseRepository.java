package com.example.onepos.repo;

import android.app.Application;

import com.example.onepos.model.Staff;
import com.example.onepos.model.local.PosDatabase;

import java.util.List;

import io.reactivex.Single;

public abstract class BaseRepository {

    Application application;

    public BaseRepository(Application application) {
        this.application = application;
    }

    public Single<Boolean> isTitleEligible(String password, int requiredLevel) {
        return Single.create(emitter -> {
            List<Staff> listStaff = PosDatabase.getInstance(application)
                    .staffDAO()
                    .getStaffByPassword(password);
            if (listStaff.size()>1)
                emitter.onError(new Throwable());
            else{
                if (listStaff.size()==0)
                    emitter.onSuccess(Boolean.FALSE);
                else
                    emitter.onSuccess(listStaff.get(0).getTitle()>=requiredLevel);
            }
        });
    }

}

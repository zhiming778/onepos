package com.example.onepos.repo;

import android.app.Application;

import com.example.onepos.model.Staff;
import com.example.onepos.model.local.PosDatabase;

import java.util.List;

import io.reactivex.rxjava3.core.Single;

public abstract class BaseRepository {

    Application application;

    public BaseRepository(Application application) {
        this.application = application;
    }

    public Single<Staff> getStaff(String password) {
        return Single.create(emitter -> {
            List<Staff> listStaff = PosDatabase.getInstance(application)
                    .staffDAO()
                    .getStaffByPassword(password);
            if (listStaff.size()>1)  //TODO fix it!
                emitter.onError(new Throwable());
            else if (listStaff.size() == 1) {
                emitter.onSuccess(listStaff.get(0));
            } else {
                final Staff staff = new Staff();
                emitter.onSuccess(staff);
            }

        });
    }

}

package com.example.onepos.repo;


import android.app.Application;

import com.example.onepos.model.Staff;
import com.example.onepos.model.TimeClock;
import com.example.onepos.model.local.PosDatabase;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class MainRepository extends BaseRepository{

    @Inject
    public MainRepository(Application application) {
        super(application);
    }

    public Single<Boolean> punchInOrOut(int punchType, String password) {
        PosDatabase posDatabase = PosDatabase.getInstance(application);
        return Single.create(emitter -> {
            List<Staff> listStaff = posDatabase.staffDAO().getStaffByPassword(password);
            if (listStaff.size() > 1) {
                emitter.onError(new Throwable());
            } else {
                long staffId = listStaff.get(0).getId();
                TimeClock timeClock = posDatabase.timeClockDAO().getTimeClockByStaffId(staffId);
                if (punchType == TimeClock.TYPE_PUNCH_IN) {
                    if (timeClock == null || timeClock.getPunchType() == TimeClock.TYPE_PUNCH_OUT) {
                        posDatabase.timeClockDAO().insertTimeClock(new TimeClock(punchType, staffId));
                        emitter.onSuccess(Boolean.TRUE);
                    }
                    else
                        emitter.onSuccess(Boolean.FALSE);
                }
                else if (timeClock.getPunchType() == TimeClock.TYPE_PUNCH_IN) {
                    posDatabase.timeClockDAO().insertTimeClock(new TimeClock(punchType, staffId));
                    emitter.onSuccess(Boolean.TRUE);
                }
                else
                    emitter.onSuccess(Boolean.FALSE);
            }
        });
    }
}

package com.example.onepos.repo;

import android.app.Application;

import com.example.onepos.model.Staff;
import com.example.onepos.model.local.PosDatabase;
import com.example.onepos.model.local.StaffDAO;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public class OfficeRepository extends BaseRepository{

    @Inject
    public OfficeRepository(Application application) {
        super(application);
    }


    public Single<List<Staff>> getAllStaff() {
        return Single.create(emitter -> {
            List<Staff> list = Staff.fromCursor(PosDatabase.getInstance(application).staffDAO().getAllStaff());
            emitter.onSuccess(list);
        });
    }

    public Single<Staff> getStaffById(long id) {
        return Single.create(emitter -> {
            Staff staff = PosDatabase.getInstance(application)
                    .staffDAO()
                    .getStaffById(id);
            emitter.onSuccess(staff);
        });
    }

    public Completable saveStaff(Staff staff) {
        StaffDAO staffDAO = PosDatabase.getInstance(application).staffDAO();
        return Completable.create(emitter -> {
            if (staff.getId() == 0) {
                staffDAO.insertStaff(staff);
            } else
                staffDAO.updateStaff(staff);
            emitter.onComplete();
        });
    }
    public Completable deleteStaffById(long id) {
        return Completable.create(emitter -> {
            PosDatabase.getInstance(application)
                    .staffDAO()
                    .deleteStaffById(id);
            emitter.onComplete();
        });
    }
}

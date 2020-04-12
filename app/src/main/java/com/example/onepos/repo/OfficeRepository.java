package com.example.onepos.repo;

import android.app.Application;
import android.database.Cursor;

import com.example.onepos.model.Staff;
import com.example.onepos.model.local.PosDatabase;
import com.example.onepos.model.local.StaffDAO;
import com.example.onepos.util.MLog;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

public class OfficeRepository extends BaseRepository{

    @Inject
    public OfficeRepository(Application application) {
        super(application);
    }


    public Maybe<Cursor> getAllStaff() {
        return Maybe.create(emitter -> {
            Cursor cursor = PosDatabase.getInstance(application)
                    .staffDAO()
                    .getAllStaff();
            if (cursor!=null)
                emitter.onSuccess(cursor);
            emitter.onComplete();
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
                staff.setId(0);
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

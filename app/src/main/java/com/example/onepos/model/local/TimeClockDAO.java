package com.example.onepos.model.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.onepos.model.TimeClock;

@Dao
public interface TimeClockDAO {

    @Query("SELECT * FROM table_time_clock WHERE FK_staff_id = :fkStaffId ORDER BY date DESC LIMIT 1")
    TimeClock getTimeClockByStaffId(long fkStaffId);

    @Insert
    long insertTimeClock(TimeClock timeClock);
}

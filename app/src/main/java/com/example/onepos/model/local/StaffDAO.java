package com.example.onepos.model.local;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.onepos.model.Staff;
import com.example.onepos.model.TimeClock;

import java.util.List;

@Dao
public interface StaffDAO {

    @Query("SELECT id, title, name FROM table_staff")
    Cursor getAllStaff();

    @Query("SELECT * FROM table_staff WHERE id = :id")
    Staff getStaffById(long id);

    @Query("SELECT * FROM table_staff WHERE password = :password")
    List<Staff> getStaffByPassword(String password);

    @Update
    int updateStaff(Staff staff);

    @Insert
    long insertStaff(Staff staff);

    @Query("DELETE FROM table_staff WHERE id = :id")
    int deleteStaffById(long id);


}

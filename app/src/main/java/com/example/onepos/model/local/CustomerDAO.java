package com.example.onepos.model.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.onepos.model.Customer;

@Dao
public interface CustomerDAO {

    @Query("SELECT * FROM table_customer WHERE phone_number = :phoneNumber LIMIT 1")
    Customer getCustomerByPhoneNum(String phoneNumber);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insertCustomer(Customer customer);

    @Query("SELECT * FROM table_customer WHERE id = :id")
    Customer getCustomerById(Long id);

    @Update
    int updateCustomer(Customer customer);

    @Query("DELETE FROM table_customer WHERE id = :id")
    int deleteCustomer(Long id);
}

package com.example.onepos.model.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.onepos.model.CustomerOrder;

import java.util.List;

@Dao
public interface CustomerOrderDAO {

    @Insert
    long insertCustomerOrder(CustomerOrder customerOrder);

    @Query("SELECT * FROM table_customer_order WHERE FK_customer_id = :fkCustomerId")
    List<CustomerOrder> getCustomerOrderByCustomerId(Long fkCustomerId);

    @Query("SELECT * FROM table_customer_order WHERE date BETWEEN :startingTime AND :endingTime")
    List<CustomerOrder> getCustomerOrderByDate(long startingTime, long endingTime);

    @Query("SELECT * FROM table_customer_order WHERE payment_type = :paymentType AND date BETWEEN :startingTime AND :endingTime")
    List<CustomerOrder> getCustomerOrderByPaymentTypeAndDate(int paymentType, long startingTime, long endingTime);

    @Query("SELECT * FROM table_customer_order WHERE order_type = :orderType AND date BETWEEN :startingTime AND :endingTime")
    List<CustomerOrder> getCustomerOrderByOrderTypeAndDate(int orderType, long startingTime, long endingTime);

    @Query("SELECT * FROM table_customer_order WHERE id = :id")
    CustomerOrder getCustomerOrderById(long id);

    @Query("SELECT * FROM table_customer_order WHERE FK_customer_id IN (SELECT id FROM table_customer WHERE phone_number = :phoneNumber)")
    List<CustomerOrder> getCustomerOrderByPhoneNumber(String phoneNumber);

    @Update
    int updateCustomerOrder(CustomerOrder customerOrder);
}

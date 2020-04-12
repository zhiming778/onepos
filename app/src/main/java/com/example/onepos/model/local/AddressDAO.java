package com.example.onepos.model.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.onepos.model.Address;
import com.example.onepos.model.CustomerOrder;

import java.util.List;

@Dao
public interface AddressDAO {
    //@Query("SELECT * FROM table_address WHERE FK_customer_id = :fKCustomerId")
    //List<Address> getAddressesByCustomerId(Long fKCustomerId);

    @Query("SELECT * FROM table_address WHERE id IN (" +
            "SELECT DISTINCT table_customer_order.FK_address_id FROM table_customer_order WHERE table_customer_order.order_type = "+
            CustomerOrder.ORDER_TYPE_DELIVERY +" AND table_customer_order.FK_customer_id = :fKCustomerId ORDER BY date DESC LIMIT 3)")
    List<Address> getAddressesByCustomerId(Long fKCustomerId);

    @Query("SELECT * FROM table_address WHERE id = :id")
    Address getAddressById(Long id);

    @Insert
    Long insertAddress(Address address);

    @Query("DELETE FROM table_address WHERE id = :id")
    int deleteAddressById(Long id);
}

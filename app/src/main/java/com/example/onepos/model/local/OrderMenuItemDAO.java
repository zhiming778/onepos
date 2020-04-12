package com.example.onepos.model.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.example.onepos.model.OrderMenuItem;
import com.example.onepos.model.OrderModifierItem;

import java.util.List;

@Dao
public interface OrderMenuItemDAO {

    @Insert
    long[] insertOrderMenuItems(OrderMenuItem[] orderMenuItems);

    @Query("SELECT * FROM table_order_menu_item WHERE id = :id")
    List<OrderMenuItem> getOrderMenuItem(long id);

    @Query("SELECT * FROM table_order_menu_item WHERE FK_customer_order_id = :fkCustomerOrderId")
    List<OrderMenuItem> getOrderMenuItemById(long fkCustomerOrderId);

    @Query("DELETE FROM table_order_menu_item WHERE id = :id")
    int deleteOrderMenuItem(long id);

    @Query("SELECT * FROM table_order_modifier_item WHERE FK_customer_order_id = :fkCustomerOrderId")
    List<OrderModifierItem> getOrderModifierItemByOrderId(long fkCustomerOrderId);



}

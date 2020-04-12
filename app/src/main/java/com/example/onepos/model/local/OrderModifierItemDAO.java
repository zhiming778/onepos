package com.example.onepos.model.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.onepos.model.OrderModifierItem;

import java.util.List;

@Dao
public interface OrderModifierItemDAO {
    @Insert
    long insertOrderModifierItem(OrderModifierItem orderModifierItem);

    @Insert(onConflict = OnConflictStrategy.ABORT)
    void insertOrderModifierItems(OrderModifierItem[] orderModifierItems);

    @Query("SELECT * FROM table_order_modifier_item WHERE FK_customer_order_id = :fkCustomerOrderId")
    List<OrderModifierItem> getItemsByCustomerOrderId(long fkCustomerOrderId);

    @Query("DELETE FROM table_order_modifier_item WHERE id = :id")
    int deleteOrderModifierItem(long id);
}

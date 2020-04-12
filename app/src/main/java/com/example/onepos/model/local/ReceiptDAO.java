package com.example.onepos.model.local;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.onepos.model.OrderMenuItem;
import com.example.onepos.model.OrderModifierItem;

import java.util.LinkedList;
import java.util.List;

@Dao
public abstract class ReceiptDAO{

    @Query("SELECT * FROM table_order_menu_item WHERE FK_customer_order_id = :fkCustomerOrderId")
    abstract List<OrderMenuItem> getOrderMenuItemById(long fkCustomerOrderId);

    @Query("SELECT * FROM table_order_modifier_item WHERE FK_customer_order_id = :fkCustomerOrderId")
    abstract List<OrderModifierItem> getOrderModifierItemByOrderId(long fkCustomerOrderId);

    @Transaction
    List<Object> getReceiptByOrderId(long id) {
        List<OrderMenuItem> listOrderMenuItem = getOrderMenuItemById(id);
        List<OrderModifierItem> listOrderModifierItem = getOrderModifierItemByOrderId(id);
        List<Object> list = new LinkedList<>();
        for (OrderMenuItem orderMenuItem : listOrderMenuItem) {     //TODO optimize with lambda ?
            list.add(orderMenuItem);
            for (int i = 0; i < listOrderModifierItem.size(); i++) {
                if (listOrderModifierItem.get(i).getFkOrderMenuitemId() == orderMenuItem.getId()) {
                    list.add(listOrderModifierItem.remove(i));
                }
            }
        }
        return list;
    }

}

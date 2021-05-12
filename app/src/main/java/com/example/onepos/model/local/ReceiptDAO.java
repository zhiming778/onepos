package com.example.onepos.model.local;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.onepos.model.OrderItem;
import com.example.onepos.model.OrderMenuItem;
import com.example.onepos.model.OrderModifierItem;

import java.util.LinkedList;
import java.util.List;

@Dao
public abstract class ReceiptDAO{


    @Transaction
    String[] updateOrderMenuItemTitles(int lang, long[] itemIds) {
        int length = itemIds.length;
        final String[] titles = new String[length];
        for(int i=0;i<titles.length;i++) {
            titles[i] = getOrderMenuItemTitleByItemId(lang, itemIds[i]);
        }
        return titles;
    }

    @Query("SELECT title FROM table_modifieritem_translation WHERE lang =:lang AND FK_item_id = :itemId")
    abstract String getOrderMenuItemTitleByItemId(int lang, long itemId);

    @Transaction
    String[] updateOrderModifierItemTitles(int lang, long[] itemIds) {
        int length = itemIds.length;
        final String[] titles = new String[length];
        for(int i=0;i<titles.length;i++) {
            titles[i] = getOrderModifierItemTitleByItemId(lang, itemIds[i]);
        }
        return titles;
    }

    @Query("SELECT title FROM table_modifieritem_translation WHERE lang =:lang AND FK_item_id = :itemId")
    abstract String getOrderModifierItemTitleByItemId(int lang, long itemId);


    /*@Query("SELECT * FROM table_order_menu_item WHERE FK_customer_order_id = :fkCustomerOrderId")
    abstract List<OrderMenuItem> getOrderMenuItemByOrderId(long fkCustomerOrderId);

    @Query("SELECT * FROM table_order_modifier_item WHERE FK_customer_order_id = :fkCustomerOrderId")
    abstract List<OrderModifierItem> getOrderModifierItemByOrderId(long fkCustomerOrderId);

    @Transaction
    List<OrderItem> getReceiptByOrderId(long id) {
        List<OrderMenuItem> listOrderMenuItem = getOrderMenuItemByOrderId(id);
        List<OrderModifierItem> listOrderModifierItem = getOrderModifierItemByOrderId(id);
        List<OrderItem> list = new LinkedList<>();
        for (OrderMenuItem orderMenuItem : listOrderMenuItem) {     //TODO optimize with lambda ?
            list.add(orderMenuItem);
            for (int i = 0; i < listOrderModifierItem.size(); i++) {
                if (listOrderModifierItem.get(i).getFkOrderMenuitemId() == orderMenuItem.getId()) {
                    list.add(listOrderModifierItem.remove(i));
                }
            }
        }
        return list;
    }*/

}

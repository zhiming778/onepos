package com.example.onepos.model.local;

import android.database.Cursor;

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

    @Query("DELETE FROM table_order_menu_item WHERE id = :id")
    int deleteOrderMenuItem(long id);

    @Query("SELECT table_menu_item.price, table_order_menu_item.id, table_order_menu_item.FK_item_id, table_order_menu_item.quantity, table_order_menu_item.discount, table_order_menu_item.cook_instruction, table_menuitem_translation.title " +
            "FROM table_order_menu_item " +
            "INNER JOIN table_menuitem_translation " +
            "ON table_order_menu_item.FK_item_id = table_menuitem_translation.FK_item_id " +
            "INNER JOIN table_menu_item ON table_menu_item.id = table_order_menu_item.FK_item_id " +
            "WHERE table_order_menu_item.FK_customer_order_id = :orderId AND table_menuitem_translation.lang =:lang")
    Cursor getOrderMenuItemsByLangAndOrderId(int lang, long orderId);

}

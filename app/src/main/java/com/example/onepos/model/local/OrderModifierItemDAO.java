package com.example.onepos.model.local;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.onepos.model.OrderModifierItem;

import java.util.List;

@Dao
public interface OrderModifierItemDAO {


    @Insert
    void insertOrderModifierItems(OrderModifierItem[] orderModifierItems);


    @Query("DELETE FROM table_order_modifier_item WHERE id = :id")
    int deleteOrderModifierItem(long id);

    @Query("SELECT table_modifier_item.price, table_order_modifier_item.quantity, table_order_modifier_item.FK_item_id, table_order_modifier_item.discount, table_order_modifier_item.FK_order_menuitem_id, table_modifieritem_translation.title " +
            "FROM table_order_modifier_item " +
            "INNER JOIN table_modifieritem_translation " +
            "ON table_order_modifier_item.FK_item_id = table_modifieritem_translation.FK_item_id " +
            "INNER JOIN table_modifier_item ON table_order_modifier_item.FK_item_id = table_modifier_item.id " +
            "WHERE table_order_modifier_item.FK_customer_order_id = :orderId AND table_modifieritem_translation.lang =:lang")
    Cursor getOrderModifierItemsByLangAndOrderId(int lang, long orderId);

}

package com.example.onepos.model;

import android.database.Cursor;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "table_order_menu_item",
        foreignKeys = {@ForeignKey(entity = CustomerOrder.class,
                parentColumns = "id",
                childColumns = "FK_customer_order_id",
                onDelete = ForeignKey.CASCADE,
                onUpdate = ForeignKey.CASCADE),
                @ForeignKey(entity = MenuItem.class,
                parentColumns = "id",
                childColumns = PosContract.OrderItemEntry.COLUMN_FK_ITEM_ID,
                onDelete = ForeignKey.CASCADE,
                onUpdate = ForeignKey.CASCADE)})
public class OrderMenuItem extends OrderItem<MenuItem>{

    @ColumnInfo(name = PosContract.OrderItemEntry.COLUMN_COOK_INSTRUCTION)
    private String cookInstruction;

    public OrderMenuItem() {
    }

    @Ignore
    public OrderMenuItem(MenuItem item, int mode) {
        this.item = item;
        this.mode = mode;
        quantity = 1;
        discount = 0;
        id = 0;
        fkitemid = item.id;
    }

    public String getCookInstruction() {
        return cookInstruction;
    }

    public void setCookInstruction(String cookInstruction) {
        this.cookInstruction = cookInstruction;
    }

    public int getQuantity() {
        return quantity;
    }


    public static List<OrderMenuItem> fromCursor(Cursor cursor) {
        final List<OrderMenuItem> list = new ArrayList<>();
        final int indexOfId = cursor.getColumnIndex(PosContract.OrderItemEntry.COLUMN_ID);
        final int indexOfItemId = cursor.getColumnIndex(PosContract.OrderItemEntry.COLUMN_FK_ITEM_ID);
        final int indexOfQuantity = cursor.getColumnIndex(PosContract.OrderItemEntry.COLUMN_QUANTITY);
        final int indexOfDiscount = cursor.getColumnIndex(PosContract.OrderItemEntry.COLUMN_DISCOUNT);
        final int indexOfCookInstruction = cursor.getColumnIndex(PosContract.OrderItemEntry.COLUMN_COOK_INSTRUCTION);
        final int indexOfTitle = cursor.getColumnIndex(PosContract.ItemTranslationEntry.COLUMN_TITLE);
        final int indexOfPrice = cursor.getColumnIndex(PosContract.ItemEntry.COLUMN_PRICE);
        while (cursor.moveToNext()) {
            final OrderMenuItem orderMenuItem = new OrderMenuItem();
            final MenuItem menuItem = new MenuItem();
            final long id = cursor.getLong(indexOfId);
            final long itemId = cursor.getLong(indexOfItemId);
            final int quantity = cursor.getInt(indexOfQuantity);
            final double discount = cursor.getDouble(indexOfDiscount);
            final String cookInstruction = cursor.getString(indexOfCookInstruction);
            final String title = cursor.getString(indexOfTitle);
            final double price = cursor.getDouble(indexOfPrice);
            menuItem.setTitle(title);
            menuItem.setPrice(price);
            orderMenuItem.setItem(menuItem);
            orderMenuItem.setFkitemid(itemId);
            orderMenuItem.setId(id);
            orderMenuItem.setQuantity(quantity);
            orderMenuItem.setDiscount(discount);
            orderMenuItem.setCookInstruction(cookInstruction);
            list.add(orderMenuItem);
        }
        cursor.close();
        return list;
    }
}

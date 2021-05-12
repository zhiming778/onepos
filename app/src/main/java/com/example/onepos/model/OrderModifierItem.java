package com.example.onepos.model;

import android.database.Cursor;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "table_order_modifier_item", foreignKeys =
                {@ForeignKey(entity = OrderMenuItem.class,
                parentColumns = "id",
                childColumns = "FK_order_menuitem_id",
                onDelete = ForeignKey.CASCADE,
                onUpdate = ForeignKey.CASCADE),
                @ForeignKey(entity = CustomerOrder.class,
                parentColumns = "id",
                childColumns = "FK_customer_order_id",
                onDelete = ForeignKey.CASCADE,
                onUpdate = ForeignKey.CASCADE),
                @ForeignKey(entity = ModifierItem.class,
                parentColumns = "id",
                childColumns = PosContract.OrderItemEntry.COLUMN_FK_ITEM_ID,
                onDelete = ForeignKey.CASCADE,
                onUpdate = ForeignKey.CASCADE)})

public class OrderModifierItem extends OrderItem<ModifierItem>{

    @ColumnInfo(name = PosContract.OrderItemEntry.COLUMN_FK_ORDER_MENUITEM_ID)
    private long fkOrderMenuitemId;

    public OrderModifierItem() {
    }

    @Ignore
    public OrderModifierItem(ModifierItem item, int mode) {
        this.item = item;
        this.mode = mode;
        quantity = 1;
        discount = 0;
        id = 0;
        fkitemid = item.id;
    }

    public void setType(int type) {
        item.setType(type);
    }

    public int getType() {
        return item.getType();
    }

    public long getFkOrderMenuitemId() {
        return fkOrderMenuitemId;
    }

    public void setFkOrderMenuitemId(long fkOrderMenuitemId) {
        this.fkOrderMenuitemId = fkOrderMenuitemId;
    }
    public int getQuantity() {
        return quantity;
    }

    @Override
    public void quantityIncrement() {
        if (item.getType() == 0 || item.getType() == 1)
            return;
        super.quantityIncrement();
    }

    public static List<OrderModifierItem> fromCursor(Cursor cursor) {
        final List<OrderModifierItem> list = new ArrayList<>();
        final int indexOfMenuitemId = cursor.getColumnIndex(PosContract.OrderItemEntry.COLUMN_FK_ORDER_MENUITEM_ID);
        final int indexOfItemId = cursor.getColumnIndex(PosContract.OrderItemEntry.COLUMN_FK_ITEM_ID);
        final int indexOfQuantity = cursor.getColumnIndex(PosContract.OrderItemEntry.COLUMN_QUANTITY);
        final int indexOfDiscount = cursor.getColumnIndex(PosContract.OrderItemEntry.COLUMN_DISCOUNT);
        final int indexOfPrice = cursor.getColumnIndex(PosContract.ItemEntry.COLUMN_PRICE);
        final int indexOfTitle = cursor.getColumnIndex(PosContract.ItemTranslationEntry.COLUMN_TITLE);
        while (cursor.moveToNext()) {
            final OrderModifierItem orderModifierItem = new OrderModifierItem();
            final ModifierItem modifierItem = new ModifierItem();
            final long MenuitemId = cursor.getLong(indexOfMenuitemId);
            final long itemId = cursor.getLong(indexOfItemId);
            final int quantity = cursor.getInt(indexOfQuantity);
            final double discount = cursor.getDouble(indexOfDiscount);
            final double price = cursor.getDouble(indexOfPrice);
            final String title = cursor.getString(indexOfTitle);
            modifierItem.setTitle(title);
            modifierItem.setPrice(price);
            orderModifierItem.setItem(modifierItem);
            orderModifierItem.setFkitemid(itemId);
            orderModifierItem.setFkOrderMenuitemId(MenuitemId);
            orderModifierItem.setQuantity(quantity);
            orderModifierItem.setDiscount(discount);
            list.add(orderModifierItem);
        }
        cursor.close();
        return list;
    }
}

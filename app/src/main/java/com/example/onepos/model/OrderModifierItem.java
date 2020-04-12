package com.example.onepos.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;

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
                onUpdate = ForeignKey.CASCADE)})
public class OrderModifierItem extends OrderItem<ModifierItem>{

    @ColumnInfo(name = "FK_order_menuitem_id")
    private long fkOrderMenuitemId;

    public OrderModifierItem(ModifierItem item) {
        this(item, MODE_OLD);
    }
    @Ignore
    public OrderModifierItem(ModifierItem item, int mode) {
        super(item, mode);
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
}

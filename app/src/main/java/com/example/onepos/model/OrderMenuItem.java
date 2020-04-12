package com.example.onepos.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;

@Entity(tableName = "table_order_menu_item",
        foreignKeys = @ForeignKey(entity = CustomerOrder.class,
                parentColumns = "id",
                childColumns = "FK_customer_order_id",
                onDelete = ForeignKey.CASCADE,
                onUpdate = ForeignKey.CASCADE)
        )
public class OrderMenuItem extends OrderItem<MenuItem>{

    @ColumnInfo(name = "cook_instruction")
    private String cookInstruction;

    public OrderMenuItem(MenuItem item) {
        this(item, MODE_OLD);
    }

    @Ignore
    public OrderMenuItem(MenuItem item, int mode) {
        super(item, mode);
        this.item = item;
        this.cookInstruction = null;
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
}

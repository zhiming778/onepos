package com.example.onepos.model;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.Update;

import com.example.onepos.util.MoneyUtil;

public abstract class OrderItem <T extends Item> {

    @Ignore
    public static final int MODE_OLD = 0;

    @Ignore
    public static final int MODE_ADDED = 1;

    @Update
    public static final int MODE_DELETED = 2;

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    protected long id;

    @ColumnInfo(name = "quantity")
    protected int quantity;

    @ColumnInfo(name = "discount")
    private double discount;

    @ColumnInfo(name = "FK_customer_order_id")
    protected long fkCustomerOrderId;

    @Embedded(prefix = "menu_")
    protected T item;

    @Ignore
    protected int mode;

    public OrderItem(T item, int mode) {
        this.item = item;
        quantity = 1;
        this.fkCustomerOrderId = -1;
        this.mode = mode;
        discount = 0;
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getItemId() {
        return item.getId();
    }

    public int getQuantity() {
        return quantity;
    }
    public void quantityIncrement() {
        ++quantity;
    }

    public boolean quantityDecrement() {
        return (--quantity) == 0;
    }

    public long getFkCustomerOrderId() {
        return fkCustomerOrderId;
    }

    public void setFkCustomerOrderId(long fkCustomerOrderId) {
        this.fkCustomerOrderId = fkCustomerOrderId;
    }

    public double getPrice() {
        double price = MoneyUtil.intTimesDouble(quantity, item.getPrice());
        return (price - MoneyUtil.doubleTimesDouble(price, discount));
    }

    public T getItem() {
        return item;
    }

    public void setItem(T item) {
        this.item = item;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }


    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }
}

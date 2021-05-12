package com.example.onepos.model;

import androidx.room.ColumnInfo;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.onepos.util.MoneyUtil;

public abstract class OrderItem <T extends Item> {

    @Ignore
    public static final int MODE_OLD = 0;

    @Ignore
    public static final int MODE_ADDED = 1;

    @Ignore
    public static final int MODE_DELETED = 2;

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = PosContract.OrderItemEntry.COLUMN_ID)
    protected long id;

    @ColumnInfo(name = PosContract.OrderItemEntry.COLUMN_QUANTITY)
    protected int quantity;

    @ColumnInfo(name = PosContract.OrderItemEntry.COLUMN_DISCOUNT)
    protected double discount;

    @ColumnInfo(name = PosContract.OrderItemEntry.COLUMN_FK_CUSTOMER_ORDER_ID)
    protected long fkCustomerOrderId;

    @ColumnInfo(name = PosContract.OrderItemEntry.COLUMN_FK_ITEM_ID)
    protected long fkitemid;

    @Ignore
    protected T item;

    @Ignore
    protected int mode;



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

    public String getTitle(String DISCOUNT_FORMAT) {
        if (discount>0)
            return item.getTitle().concat(String.format(DISCOUNT_FORMAT, (int) (discount * 100)));
        else
            return item.getTitle();
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

    public long getFkitemid() {
        return fkitemid;
    }

    public void setFkitemid(long fkitemid) {
        this.fkitemid = fkitemid;
    }

}

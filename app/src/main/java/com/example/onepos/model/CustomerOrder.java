package com.example.onepos.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.onepos.util.MLog;

@Entity(tableName = "table_customer_order",
        foreignKeys = {@ForeignKey(entity = Customer.class,
                parentColumns = "id",
                childColumns = "FK_customer_id",
                onDelete = ForeignKey.CASCADE,
                onUpdate = ForeignKey.CASCADE),
                @ForeignKey(entity = Address.class,
                parentColumns = "id",
                childColumns = "FK_address_id")})
public class CustomerOrder {


    @Ignore
    public static final int PAYMENT_TYPE_NOT_PAID = 0;

    @Ignore
    public static final int PAYMENT_TYPE_CASH = 1;

    @Ignore
    public static final int PAYMENT_TYPE_VISA = 2;

    @Ignore
    public static final int PAYMENT_TYPE_MASTER = 3;

    @Ignore
    public static final int PAYMENT_TYPE_DISCOVER = 4;

    @Ignore
    public static final int PAYMENT_TYPE_AMEX = 5;

    @Ignore
    public static final int PAYMENT_TYPE_MOBILE = 6;

    @Ignore
    public static final int PAYMENT_TYPE_CHECK = 7;

    @Ignore
    public static final int ORDER_TYPE_DINE_IN = 0;

    @Ignore
    public static final int ORDER_TYPE_WALK_IN = 1;

    @Ignore
    public static final int ORDER_TYPE_PICK_UP = 2;

    @Ignore
    public static final int ORDER_TYPE_DELIVERY = 3;

    @Ignore
    public static final String EXTRA_ORDER_TYPE = "extra_order_type";

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;

    @ColumnInfo(name = "date") //time is saved in millis
    private long date;

    @ColumnInfo(name = "total")
    private double total;

    @ColumnInfo(name = "discount")
    private double discount;

    //0: not paied
    //1: cash
    //2: visa
    //3: master card
    //4: discover
    //5: american express
    //6: mobile pay
    //7: check
    @ColumnInfo(name = "payment_type")
    private int paymentType;

    //0: dine in
    //1: walk in
    //2: pick up
    //3: delivery
    @ColumnInfo(name = "order_type")
    private int orderType;

    @ColumnInfo(name = "FK_customer_id")
    private Long fkCustomerId;

    @ColumnInfo(name = "FK_address_id")
    private Long fkAddressId;

    @Ignore
    private double tax;

    @Ignore
    private double subtotal;


    public CustomerOrder() {
        id = -1;
        discount = 0;
        tax = 0;
        subtotal = 0;
        total = 0;
    }



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public int getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(int paymentType) {
        this.paymentType = paymentType;
    }

    public Long getFkCustomerId() {
        return fkCustomerId;
    }

    public void setFkCustomerId(Long fkCustomerId) {
        this.fkCustomerId = fkCustomerId;
    }

    public Long getFkAddressId() {
        return fkAddressId;
    }

    public void setFkAddressId(Long fkAddressId) {
        this.fkAddressId = fkAddressId;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    @NonNull
    @Override
    public String toString() {
        return id + ": orderType=" + orderType + " total=" + total;
    }
}

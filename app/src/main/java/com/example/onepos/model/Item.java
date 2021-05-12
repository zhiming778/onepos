package com.example.onepos.model;

import androidx.room.ColumnInfo;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

public abstract class Item {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = PosContract.ItemEntry.COLUMN_ID)
    protected long id;

    @ColumnInfo(name = PosContract.ItemEntry.COLUMN_PRICE)
    protected double price;

    @Ignore
    protected String title;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

}

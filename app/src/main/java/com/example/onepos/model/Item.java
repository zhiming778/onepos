package com.example.onepos.model;

import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;

public abstract class Item {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    protected long id;

    @ColumnInfo(name = "name")
    protected String name;

    @ColumnInfo(name = "price")
    protected double price;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}

package com.example.onepos.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;

@Entity(tableName = "table_modifier_item"
        , foreignKeys = @ForeignKey(entity = MenuItem.class,
        parentColumns = "id",
        childColumns = "FK_category_id",
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE))
public class ModifierItem extends Item{

    @Ignore
    public static final int TYPE_SIDE_1 = 0;
    @Ignore
    public static final int TYPE_SIDE_2 = 1;
    @Ignore
    public static final int TYPE_ADD = 2;
    @Ignore
    public static final int TYPE_NO = 3;
    @Ignore
    public static final int TYPE_REPLACE = 4;
    @Ignore
    public static final int TYPE_COOK = 5;
    @Ignore
    public static final int NUM_OF_SIDE = 2;

    //0: side1 1: side2 2: add 3: no 4: replace 5:cook
    @ColumnInfo(name = "type")
    private int type;

    @ColumnInfo(name = "FK_category_id")
    private long fkCategoryId;

    public ModifierItem(String name, int type, double price, long fkCategoryId) {
        this.name = name;
        this.type = type;
        this.price = price;
        this.fkCategoryId = fkCategoryId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getFkCategoryId() {
        return fkCategoryId;
    }

    public void setFkCategoryId(long fkCategoryId) {
        this.fkCategoryId = fkCategoryId;
    }

    public static boolean isModifierSide(int type) {
        return type == ModifierItem.TYPE_SIDE_1 || type == ModifierItem.TYPE_SIDE_2;
    }
}

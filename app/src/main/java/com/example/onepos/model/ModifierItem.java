package com.example.onepos.model;

import android.database.Cursor;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;

import java.util.ArrayList;
import java.util.List;

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
    @ColumnInfo(name = PosContract.ItemEntry.COLUMN_TYPE)
    private int type;

    @ColumnInfo(name = PosContract.ItemEntry.COLUMN_FK_CATEGORY_ID)
    private long fkCategoryId;

    public ModifierItem() {
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

    public static List<ModifierItem> fromCursor(Cursor cursor) {
        final List<ModifierItem> list = new ArrayList<>();
        final int indexOfId = cursor.getColumnIndex(PosContract.ItemEntry.COLUMN_ID);
        final int indexOfPrice = cursor.getColumnIndex(PosContract.ItemEntry.COLUMN_PRICE);
        final int indexOfType = cursor.getColumnIndex(PosContract.ItemEntry.COLUMN_TYPE);
        final int indexOfTitle = cursor.getColumnIndex(PosContract.ItemTranslationEntry.COLUMN_TITLE);
        while (cursor.moveToNext()) {
            final ModifierItem modifierItem = new ModifierItem();
            final long id = cursor.getLong(indexOfId);
            final double price = cursor.getDouble(indexOfPrice);
            final int type = cursor.getInt(indexOfType);
            final String title = cursor.getString(indexOfTitle);
            modifierItem.setId(id);
            modifierItem.setPrice(price);
            modifierItem.setTitle(title);
            modifierItem.setType(type);
            list.add(modifierItem);
        }
        cursor.close();
        return list;
    }

}

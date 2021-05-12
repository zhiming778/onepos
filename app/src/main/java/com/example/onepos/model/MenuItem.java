package com.example.onepos.model;

import android.database.Cursor;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Entity(tableName = "table_menu_item")
public class MenuItem extends Item{


    @ColumnInfo(name = PosContract.ItemEntry.COLUMN_PARENT_ID)
    private long parentId;

    @ColumnInfo(name = PosContract.ItemEntry.COLUMN_HAS_DESCENDANT)
    private boolean hasDescendant;

    @Ignore
    public static final int CATEGORY_MENUITEM_PARENT_ID = 0;

    public MenuItem() {
    }

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public boolean isHasDescendant() {
        return hasDescendant;
    }

    public void setHasDescendant(boolean hasDescendant) {
        this.hasDescendant = hasDescendant;
    }

    @NonNull
    @Override
    public String toString() {
        return id + ": " + title + " price:" + price;
    }

    @NonNull
    @Override
    public MenuItem clone(){
        MenuItem menuItem = new MenuItem();
        menuItem.setPrice(price);
        menuItem.setTitle(title);
        menuItem.setParentId(parentId);
        menuItem.setId(id);
        menuItem.setHasDescendant(hasDescendant);
        return menuItem;
    }

    public static List<MenuItem> fromCursor(Cursor cursor) {
        final List<MenuItem> list = new ArrayList<>();
        final int indexOfId = cursor.getColumnIndex(PosContract.ItemEntry.COLUMN_ID);
        final int indexOfParentId = cursor.getColumnIndex(PosContract.ItemEntry.COLUMN_PARENT_ID);
        final int indexOfPrice = cursor.getColumnIndex(PosContract.ItemEntry.COLUMN_PRICE);
        final int indexOfHasDescendant = cursor.getColumnIndex(PosContract.ItemEntry.COLUMN_HAS_DESCENDANT);
        final int indexOfTitle = cursor.getColumnIndex(PosContract.ItemTranslationEntry.COLUMN_TITLE);
        while (cursor.moveToNext()) {
            final MenuItem menuItem = new MenuItem();
            final long id = cursor.getLong(indexOfId);
            final long parentId = cursor.getLong(indexOfParentId);
            final double price = cursor.getDouble(indexOfPrice);
            final boolean hasDescendant = cursor.getInt(indexOfHasDescendant) == 1;
            final String title = cursor.getString(indexOfTitle);
            menuItem.setId(id);
            menuItem.setHasDescendant(hasDescendant);
            menuItem.setParentId(parentId);
            menuItem.setPrice(price);
            menuItem.setTitle(title);
            list.add(menuItem);
        }
        cursor.close();
        return list;
    }
}

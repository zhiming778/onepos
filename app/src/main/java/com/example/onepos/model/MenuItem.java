package com.example.onepos.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;

@Entity(tableName = "table_menu_item")
public class MenuItem extends Item{

    @Ignore
    public static final int CATEGORY_MENUITEM_PARENT_ID = 0;

    @ColumnInfo(name = "parent_id")
    private long parentId;

    @ColumnInfo(name = "has_descendant")
    private boolean hasDescendant;

    public MenuItem(long id, String name, long parentId, boolean hasDescendant, double price) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
        this.hasDescendant = hasDescendant;
        this.price = price;
    }

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public boolean hasDescendant() {
        return hasDescendant;
    }

    public void setHasDescendant(boolean hasDescendant) {
        this.hasDescendant = hasDescendant;
    }

    @NonNull
    @Override
    public String toString() {
        return "name = "+name+" hasDescendant = "+hasDescendant;
    }
}

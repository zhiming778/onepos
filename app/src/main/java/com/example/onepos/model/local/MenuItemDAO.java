package com.example.onepos.model.local;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.example.onepos.model.MenuItem;

import java.util.List;

@Dao
public interface MenuItemDAO {
    @Insert
    long insertMenuItem(MenuItem menuItem);

    @Query("DELETE FROM table_menu_item WHERE id = :id")
    int deleteMenuItem(long id);

    @Query("SELECT table_menu_item.parent_id, table_menu_item.id, table_menu_item.price, table_menu_item.has_descendant, table_menuitem_translation.title " +
            "FROM table_menu_item " +
            "INNER JOIN table_menuitem_translation " +
            "ON table_menu_item.id = table_menuitem_translation.FK_item_id " +
            "WHERE table_menu_item.parent_id =:parentId AND table_menuitem_translation.lang =:lang")
    Cursor getMenuItemsByLangAndParentId(long parentId, int lang);

    @Query("SELECT table_menu_item.parent_id, table_menu_item.id, table_menu_item.price, table_menu_item.has_descendant, table_menuitem_translation.title " +
            "FROM table_menu_item " +
            "INNER JOIN table_menuitem_translation " +
            "ON table_menu_item.id = table_menuitem_translation.FK_item_id " +
            "WHERE table_menuitem_translation.lang =:lang")
    Cursor getAllMenuitemsByLang(int lang);
}

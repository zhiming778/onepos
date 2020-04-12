package com.example.onepos.model.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.example.onepos.model.MenuItem;
import com.example.onepos.model.typeconverter.CursorTypeConverter;

import java.util.List;

@Dao
public interface MenuItemDAO {
    @Insert
    long insertMenuItem(MenuItem menuItem);

    @Query("SELECT * FROM table_menu_item WHERE parent_id = :parentId")
    List<MenuItem> getMenuItems(long parentId);


    @Query("SELECT id, name, price FROM table_menu_item WHERE id = 50")
    @TypeConverters(CursorTypeConverter.class)
    MenuItem getMenuitem();

    @Query("DELETE FROM table_menu_item WHERE id = :id")
    int deleteMenuItem(long id);

}

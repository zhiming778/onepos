package com.example.onepos.model.typeconverter;

import android.database.Cursor;

import androidx.room.TypeConverter;

import com.example.onepos.model.Item;
import com.example.onepos.model.MenuItem;

public class CursorTypeConverter {
    @TypeConverter
    public static MenuItem fromCursor(Cursor cursor) {
        long id = cursor.getLong(cursor.getColumnIndex("id"));
        String name = cursor.getString(cursor.getColumnIndex("name"));
        double price = cursor.getDouble(cursor.getColumnIndex("price"));
        return new MenuItem(id, name, 0, false, price);
    }
}

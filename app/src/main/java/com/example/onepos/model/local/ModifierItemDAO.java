package com.example.onepos.model.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.onepos.model.ModifierItem;

import java.util.List;

@Dao
public interface ModifierItemDAO {
    @Insert
    long insertModifierItem(ModifierItem modifierItem);

    @Query("SELECT * FROM table_modifier_item WHERE id = :id")
    ModifierItem getModifierItem(long id);

    @Query("DELETE FROM table_modifier_item WHERE id = :id")
    int deleteModifierItem(long id);

    @Query("SELECT * FROM table_modifier_item WHERE type = :type AND FK_category_id = :fkCategoryId")
    List<ModifierItem> getModifierItemsByType(int type, long fkCategoryId);
}

package com.example.onepos.model.local;

import android.database.Cursor;

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

    @Query("SELECT table_modifier_item.id, table_modifier_item.price, table_modifier_item.type, table_modifieritem_translation.title " +
            "FROM table_modifier_item " +
            "INNER JOIN table_modifieritem_translation " +
            "ON table_modifier_item.id = table_modifieritem_translation.FK_item_id " +
            "WHERE table_modifieritem_translation.lang =:lang AND table_modifier_item.FK_category_id =:fkCategoryId")
    Cursor getModifierItemsByCategoryAndLang(long fkCategoryId, int lang);
}

package com.example.onepos.model.local;

import androidx.room.Dao;
import androidx.room.Query;

@Dao
public interface ItemTranslationDAO {

    @Query("SELECT title FROM table_menuitem_translation WHERE lang = :lang AND FK_item_id = :fkItemId")
    String getOrderMenuItemByLangAndItemId(int lang, long fkItemId);

    @Query("SELECT title FROM table_modifieritem_translation WHERE lang = :lang AND FK_item_id = :fkItemId")
    String getOrderModifierItemByLangAndItemId(int lang, long fkItemId);
}

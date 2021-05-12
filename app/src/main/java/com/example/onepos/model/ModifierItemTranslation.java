package com.example.onepos.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(tableName = "table_modifieritem_translation",
        foreignKeys = @ForeignKey(entity = ModifierItem.class,
                parentColumns = "id",
                childColumns = "FK_item_id",
                onDelete = ForeignKey.CASCADE,
                onUpdate = ForeignKey.CASCADE))
public class ModifierItemTranslation extends ItemTranslation{

    public ModifierItemTranslation(long id, long fkItemId, int lang, String title) {
        this.id = id;
        this.fkItemId = fkItemId;
        this.lang = lang;
        this.title = title;
    }
}

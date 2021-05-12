package com.example.onepos.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Locale;

public abstract class ItemTranslation {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = PosContract.ItemTranslationEntry.COLUMN_ID)
    protected long id;

    @ColumnInfo(name = PosContract.ItemTranslationEntry.COLUMN_FK_ITEM_ID)
    protected long fkItemId;

    @ColumnInfo(name = PosContract.ItemTranslationEntry.COLUMN_LANG)
    protected int lang;

    @ColumnInfo(name = PosContract.ItemTranslationEntry.COLUMN_TITLE)
    protected String title;

    @Ignore
    public static final int LANG_US = 0;

    @Ignore
    public static final int LANG_CN = 1;



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getFkItemId() {
        return fkItemId;
    }

    public void setFkItemId(long fkItemId) {
        this.fkItemId = fkItemId;
    }

    public int getLang() {
        return lang;
    }

    public void setLang(int lang) {
        this.lang = lang;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

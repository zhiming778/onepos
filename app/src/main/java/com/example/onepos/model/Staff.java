package com.example.onepos.model;

import android.database.Cursor;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "table_staff")
public class Staff implements Serializable {

    @Ignore
    public static final int NUM_OF_TITLES = 5;

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = PosContract.StaffEntry.COLUMN_ID)
    private long id;

    @ColumnInfo(name = PosContract.StaffEntry.COLUMN_NAME)
    private String name;

    /*
    * 0: owner
    * 1: manager
    * 2: cashier
    * 3: server
    * 4: chef
    * */
    @ColumnInfo(name = PosContract.StaffEntry.COLUMN_TITLE)
    private int title;

    /*
    0: english
    1: chinese
    */
    @ColumnInfo(name = PosContract.StaffEntry.COLUMN_LANG)
    private int lang;

    @ColumnInfo(name = PosContract.StaffEntry.COLUMN_PHONE_NUMBER)
    private String phoneNum;

    @ColumnInfo(name = PosContract.StaffEntry.COLUMN_BIRTH)
    private long dateOfBirth;

    @ColumnInfo(name = PosContract.StaffEntry.COLUMN_ADDRESS)
    private String address;

    @ColumnInfo(name = PosContract.StaffEntry.COLUMN_SSN)
    private String ssn;

    @ColumnInfo(name = PosContract.StaffEntry.COLUMN_PASSWORD)
    private String password;


    public Staff(long id, String name, int title, int lang, String phoneNum, long dateOfBirth, String address, String ssn, String password) {
        this.id = id;
        this.name = name;
        this.title = title;
        this.lang = lang;
        this.phoneNum = phoneNum;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.ssn = ssn;
        this.password = password;
    }

    @Ignore
    public Staff() {
        title = -1;
        lang = ItemTranslation.LANG_US;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public long getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(long dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getLang() {
        return lang;
    }

    public void setLang(int lang) {
        this.lang = lang;
    }

    public static List<Staff> fromCursor(Cursor cursor) {
        final List<Staff> list = new ArrayList<>();
        final int indexOfId = cursor.getColumnIndex(PosContract.StaffEntry.COLUMN_ID);
        final int indexOfTitle = cursor.getColumnIndex(PosContract.StaffEntry.COLUMN_TITLE);
        final int indexOfName = cursor.getColumnIndex(PosContract.StaffEntry.COLUMN_NAME);
        while (cursor.moveToNext()) {
            final Staff staff = new Staff();
            final long id = cursor.getLong(indexOfId);
            final String name = cursor.getString(indexOfName);
            final int title = cursor.getInt(indexOfTitle);
            staff.setId(id);
            staff.setName(name);
            staff.setTitle(title);
            list.add(staff);
        }
        cursor.close();
        return list;
    }

    @NonNull
    @Override
    public String toString() {
        return id + ": " + name + " title:" + title + " lang:" + lang;
    }
}

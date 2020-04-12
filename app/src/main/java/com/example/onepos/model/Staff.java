package com.example.onepos.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "table_staff")
public class Staff{

    @Ignore
    public static final int NUM_OF_TITLES = 5;

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;

    @ColumnInfo(name = "name")
    private String name;

    /*
    * 0: owner
    * 1: manager
    * 2: cashier
    * 3: server
    * 4: chef
    * */
    @ColumnInfo(name = "title")
    private int title;

    @ColumnInfo(name = "phone_number")
    private String phoneNum;

    @ColumnInfo(name = "date_of_birth")
    private long dateOfBirth;

    @ColumnInfo(name = "address")
    private String address;

    @ColumnInfo(name = "ssn")
    private String ssn;

    @ColumnInfo(name = "password")
    private String password;


    public Staff(long id, String name, int title, String phoneNum, long dateOfBirth, String address, String ssn, String password) {
        this.id = id;
        this.name = name;
        this.title = title;
        this.phoneNum = phoneNum;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.ssn = ssn;
        this.password = password;
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
}

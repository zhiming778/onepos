package com.example.onepos.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "table_time_clock"
        ,foreignKeys = @ForeignKey(entity = Staff.class
        ,parentColumns = "id"
        ,childColumns = "FK_staff_id"
        ,onDelete = ForeignKey.CASCADE
        ,onUpdate = ForeignKey.CASCADE))
public class TimeClock {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;

    @ColumnInfo(name = "punch_type")
    private int punchType;

    @ColumnInfo(name = "date")
    private long date;

    @ColumnInfo(name = "FK_staff_id")
    private long FKStaffId;


    @Ignore
    public static final int TYPE_PUNCH_IN = 0;

    @Ignore
    public static final int TYPE_PUNCH_OUT = 1;

    public TimeClock(int punchType, long FKStaffId) {
        this.punchType = punchType;
        date = System.currentTimeMillis();
        this.FKStaffId = FKStaffId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getPunchType() {
        return punchType;
    }

    public void setPunchType(int punchType) {
        this.punchType = punchType;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public long getFKStaffId() {
        return FKStaffId;
    }

    public void setFKStaffId(long FKStaffId) {
        this.FKStaffId = FKStaffId;
    }

    @NonNull
    @Override
    public String toString() {
        return new StringBuilder()
                .append(FKStaffId)
                .append(" : Punch Type (")
                .append(punchType)
                .append(") :")
                .append(date).toString();
    }
}

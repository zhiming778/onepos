package com.example.onepos.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "table_address",
        foreignKeys = @ForeignKey(entity = Customer.class,
                parentColumns = "id",
                childColumns = "FK_customer_id",
                onDelete = ForeignKey.CASCADE,
                onUpdate = ForeignKey.CASCADE))
public class Address{

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private Long id;

    @ColumnInfo(name = "street_address")
    private String streetAddress;

    @ColumnInfo(name = "apt_room")
    private String aptRoom;

    @ColumnInfo(name = "zipcode")
    private int zipcode;

    @ColumnInfo(name = "city")
    private String city;

    @ColumnInfo(name = "delivery_instruction")
    private String deliveryInstruction;

    @ColumnInfo(name = "delivery_charge")
    private double deliveryCharge;

    @ColumnInfo(name = "FK_customer_id")
    private long fkCustomerId;

    public Address(String streetAddress, String aptRoom, int zipcode, String city, String deliveryInstruction) {
        this.streetAddress = streetAddress;
        this.aptRoom = aptRoom;
        this.zipcode = zipcode;
        this.city = city;
        this.deliveryInstruction = deliveryInstruction;
        this.fkCustomerId = -1;
    }
    @Ignore
    public Address(Long id, String streetAddress, String aptRoom, int zipcode, String city, String deliveryInstruction) {
        this.id = id;
        this.streetAddress = streetAddress;
        this.aptRoom = aptRoom;
        this.zipcode = zipcode;
        this.city = city;
        this.deliveryInstruction = deliveryInstruction;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getAptRoom() {
        return aptRoom;
    }

    public void setAptRoom(String aptRoom) {
        this.aptRoom = aptRoom;
    }

    public int getZipcode() {
        return zipcode;
    }

    public void setZipcode(int zipcode) {
        this.zipcode = zipcode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDeliveryInstruction() {
        return deliveryInstruction;
    }

    public void setDeliveryInstruction(String deliveryInstruction) {
        this.deliveryInstruction = deliveryInstruction;
    }

    public double getDeliveryCharge() {
        return deliveryCharge;
    }

    public void setDeliveryCharge(double deliveryCharge) {
        this.deliveryCharge = deliveryCharge;
    }

    public long getFkCustomerId() {
        return fkCustomerId;
    }

    public void setFkCustomerId(long fkCustomerId) {
        this.fkCustomerId = fkCustomerId;
    }

    public boolean isEmpty() {
        return streetAddress.equals("");
    }

    @NonNull
    @Override
    public String toString() {
        return id+" street = " + streetAddress + " apt/rm = " + aptRoom + " city = " + city;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof Address))
            return false;
        Address address = (Address) obj;
        return streetAddress.equals(address.getStreetAddress())
                && aptRoom.equals(address.getAptRoom())
                && deliveryInstruction.equals(address.getDeliveryInstruction())
                && zipcode == address.getZipcode()
                && city.equals(address.getCity());
    }

    @NonNull
    @Override
    public Address clone(){
        return new Address(this.id, this.streetAddress, this.aptRoom, this.zipcode, this.city, this.deliveryInstruction);
    }
}

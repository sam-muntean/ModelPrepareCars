package com.example.sam.model.domain;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

/**
 * Created by Sam on 31.01.2018.
 */

@Entity(tableName = "purchasedCar")
public class PurchasedCar {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int carId;

    private String name;

    private int quantity;

    private String type;

    private String status;

    private Date date;

    public PurchasedCar(int id, int carId, String name, int quantity, String type, String status, Date date) {
        this.id = id;
        this.carId = carId;
        this.name = name;
        this.quantity = quantity;
        this.type = type;
        this.status = status;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "PurchasedCar{" +
                "id=" + id +
                ", carId=" + carId +
                ", name='" + name + '\'' +
                ", quantity=" + quantity +
                ", type='" + type + '\'' +
                ", status='" + status + '\'' +
                ", date=" + date +
                '}';
    }
}

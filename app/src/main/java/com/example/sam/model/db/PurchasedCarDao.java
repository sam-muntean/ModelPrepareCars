package com.example.sam.model.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.sam.model.domain.PurchasedCar;

import java.util.List;

/**
 * Created by Sam on 31.01.2018.
 */

@Dao
public interface PurchasedCarDao {

    @Insert
    void addCar(PurchasedCar car);


    @Insert
    void addCars(List<PurchasedCar> car);

    @Delete
    void deleteCar(PurchasedCar car);

    @Query("delete from purchasedCar")
    void deleteAll();

    @Update
    void updateCar(PurchasedCar car);

    @Query("select * from purchasedCar")
    LiveData<List<PurchasedCar>> getCars();

    @Query("select * from purchasedCar")
    List<PurchasedCar> getNormalCars();

}

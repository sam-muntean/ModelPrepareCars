package com.example.sam.model.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.sam.model.domain.Car;

import java.util.List;

/**
 * Created by Sam on 31.01.2018.
 */

@Dao
public interface CarDao {

    @Insert
    void addCar(Car car);

    @Insert
    void addCars(List<Car> cars);

    @Delete
    void deleteCar(Car car);

    @Query("delete from car")
    void deleteCars();

    @Update
    void updateCar(Car car);

    @Query("select * from car")
    LiveData<List<Car>> getCars();

    @Query("SELECT * FROM car LIMIT 1 OFFSET :id")
    Car findByID(int id);
}

package com.example.sam.model.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import com.example.sam.model.domain.Car;
import com.example.sam.model.domain.PurchasedCar;

/**
 * Created by Sam on 31.01.2018.
 */

@Database(entities = {Car.class, PurchasedCar.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class CarDatabase extends RoomDatabase {
    public abstract CarDao getCarDao();

    public abstract PurchasedCarDao getPCarDao();
}
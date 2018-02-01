package com.example.sam.model.manager;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.example.sam.model.db.CarDatabase;

import timber.log.Timber;

/**
 * Created by Sam on 31.01.2018.
 */

public class CarApp extends Application {
    public CarDatabase db;

    @Override
    public void onCreate(){
        super.onCreate();
        Timber.plant(new Timber.DebugTree());
        db = Room.databaseBuilder(getApplicationContext(), CarDatabase.class, "cars2").allowMainThreadQueries().build();
    }
}

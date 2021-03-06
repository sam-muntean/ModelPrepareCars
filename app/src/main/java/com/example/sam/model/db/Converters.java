package com.example.sam.model.db;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

/**
 * Created by alexandraneamtu on 30/01/2018.
 */

public class Converters {
    @TypeConverter
    public Date fromTimestamp(Long value) {
    return value == null ? null : new Date(value);
}

    @TypeConverter
    public Long dateToTimestamp(Date date) {
        if (date == null) {
            return null;
        } else {
            return date.getTime();
        }
    }
}
package com.bridge.androidtechnicaltest.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {
        Pupil.class
}, version = 1, exportSchema = false)

public abstract class AppDatabase extends RoomDatabase {
    public abstract PupilDao getPupilDao();
}
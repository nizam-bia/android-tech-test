package com.bridge.androidtechnicaltest.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface PupilDao {

    @Query("SELECT * FROM Pupils ORDER BY name ASC")
    Single<List<Pupil>> getPupils();
}

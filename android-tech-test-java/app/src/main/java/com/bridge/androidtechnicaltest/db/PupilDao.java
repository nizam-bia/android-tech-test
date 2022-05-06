package com.bridge.androidtechnicaltest.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface PupilDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<Pupil> pupilList);

    @Query("SELECT * FROM Pupils ORDER BY name ASC")
    Single<List<Pupil>> getPupils();

    @Query("DELETE FROM Pupils")
    void clear();
}

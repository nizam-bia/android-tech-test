package com.bridge.androidtechnicaltest.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Single

@Dao
interface PupilDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(pupilList: List<Pupil>)

    @Query("SELECT * FROM Pupils ORDER BY name ASC")
    fun getPupils(): Single<List<Pupil>>

    @Query("SELECT * FROM Pupils ORDER BY name ASC")
    suspend fun getAllPupils(): List<Pupil>

    @Query("DELETE FROM Pupils")
    fun clear()
}
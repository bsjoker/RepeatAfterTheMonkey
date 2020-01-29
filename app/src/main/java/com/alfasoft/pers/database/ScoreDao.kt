package com.alfasoft.pers.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.alfasoft.pers.models.ScoreModel

@Dao
interface ScoreDao {
    @Query("SELECT * FROM scoreData")
    fun getAll(): LiveData<List<ScoreModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(scoreModel: ScoreModel)
}
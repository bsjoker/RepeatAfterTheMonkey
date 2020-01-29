package com.alfasoft.pers.database

import androidx.lifecycle.LiveData
import com.alfasoft.pers.database.ScoreDao
import com.alfasoft.pers.models.ScoreModel

class ScoreRepository(private val scoreDao: ScoreDao) {
    val allScore:LiveData<List<ScoreModel>> = scoreDao.getAll()

    suspend fun insert(scoreModel: ScoreModel){
        scoreDao.insert(scoreModel)
    }
}
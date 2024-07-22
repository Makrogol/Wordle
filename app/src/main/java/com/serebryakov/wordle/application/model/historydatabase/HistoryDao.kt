package com.serebryakov.wordle.application.model.historydatabase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.serebryakov.wordle.application.model.historydatabase.entities.HistoryDbEntity

@Dao
interface HistoryDao {

    @Insert(entity = HistoryDbEntity::class)
    fun insertNewHistoryData(history: HistoryDbEntity)

    @Query("SELECT * FROM history")
    fun getAllHistoryData(): List<HistoryDbEntity>
}
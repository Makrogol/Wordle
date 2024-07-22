package com.serebryakov.wordle.application.model.historydatabase

import androidx.room.Database
import androidx.room.RoomDatabase
import com.serebryakov.wordle.application.model.historydatabase.entities.HistoryDbEntity

@Database(
    version = 1,
    entities = [
        HistoryDbEntity::class
    ]
)
abstract class HistoryDatabase: RoomDatabase() {
    abstract fun getHistoryDao(): HistoryDao
}
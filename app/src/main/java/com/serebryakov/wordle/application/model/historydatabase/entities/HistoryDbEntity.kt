package com.serebryakov.wordle.application.model.historydatabase.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.serebryakov.wordle.application.model.HistoryData
import java.sql.Date

@Entity(tableName = "history")
data class HistoryDbEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo(name = "word") val word: String,
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "attempt") val attempt: Int,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "game_time") val gameTime: Float,
) {
    fun toHistoryData(): HistoryData = HistoryData(
        id = id,
        word = word,
        date = date,
        attempt = attempt,
        description = description,
        gameTime = gameTime
    )
}
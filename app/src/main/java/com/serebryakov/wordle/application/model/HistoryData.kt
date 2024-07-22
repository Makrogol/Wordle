package com.serebryakov.wordle.application.model

import com.serebryakov.wordle.application.model.historydatabase.entities.HistoryDbEntity

data class HistoryData(
    val id: Long,
    val word: String,
    val date: String,
    val attempt: Int,
    val description: String,
    val gameTime: Float,
) {
    fun toHistoryDbEntity(): HistoryDbEntity = HistoryDbEntity(
        id = id,
        word = word,
        date = date,
        attempt = attempt,
        description = description,
        gameTime = gameTime
    )

    override fun toString(): String =
        "${this.id} ${this.word} ${this.date} ${this.attempt} ${this.description} ${this.gameTime}\n"
}

fun String.toHistoryData(): HistoryData {
    val listData = this.trim().split(' ')
    println("HistoryData listData size: ${listData.size}")
    for (el in listData)
        println("StatsData listData el: $el")
    return HistoryData(
        id = listData[0].toLong(),
        word = listData[1],
        date = listData[2],
        attempt = listData[3].toInt(),
        description = listData[4],
        gameTime = listData[5].toFloat()
    )
}
package com.serebryakov.wordle.application.model

data class StatsData(
    var countGame: Int = 0,
    var countFinishedGame: Int = 0,
    var countGuessedWord: Int = 0,
    var averageAttemptToGuess: Float = 0f,
    var totalGameTime: Float = 0f,
    var averageGameTime: Float = 0f,
    var sumAttemptToGuess: Int = 0,
) {
    override fun toString(): String =
        "${this.countGame} ${this.countFinishedGame} ${this.countGuessedWord} ${this.averageAttemptToGuess} ${this.totalGameTime} ${this.averageGameTime} ${this.sumAttemptToGuess}\n"
}


fun String.toStatsData(): StatsData {
    val listData = this.trim().split(' ')
    println("StatsData listData size: ${listData.size}")
    for (el in listData)
        println("StatsData listData el: $el")
    return StatsData(
        countGame = listData[0].toInt(),
        countFinishedGame = listData[1].toInt(),
        countGuessedWord = listData[2].toInt(),
        averageAttemptToGuess = listData[3].toFloat(),
        totalGameTime = listData[4].toFloat(),
        averageGameTime = listData[5].toFloat(),
        sumAttemptToGuess = listData[6].toInt()
    )
}

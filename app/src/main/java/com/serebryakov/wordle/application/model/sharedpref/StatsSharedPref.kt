package com.serebryakov.wordle.application.model.sharedpref

import com.serebryakov.wordle.application.model.StatsData

interface StatsSharedPref {

    fun getStats(): StatsData

    fun updateAllStats(statsData: StatsData)

    fun updateCountGame(countGame: Int = 1)

    fun updateCountFinishedGame(countFinishedGame: Int = 1)

    fun updateCountGuessedWord(countGuessedWord: Int = 1)

    fun updateSumAttemptToGuess(attempt: Int)

    fun updateTotalGameTime(gameTime: Float)
}
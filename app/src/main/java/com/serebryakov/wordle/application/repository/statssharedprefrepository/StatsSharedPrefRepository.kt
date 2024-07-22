package com.serebryakov.wordle.application.repository.statssharedprefrepository

import com.serebryakov.wordle.application.model.StatsData
import com.serebryakov.wordle.foundation.model.Repository

interface StatsSharedPrefRepository: Repository {

    suspend fun getStats(): StatsData

    suspend fun updateStats(statsData: StatsData)

    suspend fun updateCountGame(countGame: Int = 1)

    suspend fun updateCountFinishedGame(countFinishedGame: Int = 1)

    suspend fun updateCountGuessedWord(countGuessedWord: Int = 1)

    suspend fun updateAverageAttemptToGuess(attempt: Int)

    suspend fun updateTotalGameTime(gameTime: Float)

}
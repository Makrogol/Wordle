package com.serebryakov.wordle.application.repository.statssharedprefrepository

import com.serebryakov.wordle.application.model.StatsData
import com.serebryakov.wordle.application.model.sharedpref.StatsSharedPref
import com.serebryakov.wordle.foundation.model.IoDispatcher
import kotlinx.coroutines.withContext

class StatsSharedPrefRepositoryImpl(
    private val ioDispatcher: IoDispatcher,
    private val sharedPref: StatsSharedPref
) : StatsSharedPrefRepository {

    override suspend fun getStats(): StatsData = withContext(ioDispatcher.value) {
        return@withContext sharedPref.getStats()
    }

    override suspend fun updateStats(statsData: StatsData) = withContext(ioDispatcher.value) {
        return@withContext sharedPref.updateAllStats(statsData)
    }

    override suspend fun updateCountGame(countGame: Int) = withContext(ioDispatcher.value) {
        sharedPref.updateCountGame(countGame)
    }

    override suspend fun updateCountFinishedGame(countFinishedGame: Int) = withContext(ioDispatcher.value) {
        sharedPref.updateCountFinishedGame(countFinishedGame)
    }

    override suspend fun updateCountGuessedWord(countGuessedWord: Int) = withContext(ioDispatcher.value) {
        sharedPref.updateCountGuessedWord(countGuessedWord)
    }

    override suspend fun updateAverageAttemptToGuess(attempt: Int) =
        withContext(ioDispatcher.value) {
            sharedPref.updateSumAttemptToGuess(attempt)
        }

    override suspend fun updateTotalGameTime(gameTime: Float) = withContext(ioDispatcher.value) {
        sharedPref.updateTotalGameTime(gameTime)
    }
}
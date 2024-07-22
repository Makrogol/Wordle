package com.serebryakov.wordle.application.model.sharedpref

import android.content.Context
import com.serebryakov.wordle.application.model.StatsData
import com.serebryakov.wordle.application.model.toStatsData

class StatSharedPrefImpl(
    context: Context
) : StatsSharedPref {

    private val settings = context.getSharedPreferences(STORAGE_NAME, Context.MODE_PRIVATE)
    private val editor = settings.edit()

    override fun getStats(): StatsData {
        val statsData = StatsData(
            countGame = settings.getInt(COUNT_GAME_NAME, 0),
            countFinishedGame = settings.getInt(COUNT_FINISHED_GAME_NAME, 0),
            countGuessedWord = settings.getInt(COUNT_GUESSED_WORD_NAME, 0),
            totalGameTime = settings.getFloat(TOTAL_GAME_TIME_NAME, 0f),
            sumAttemptToGuess = settings.getInt(SUM_ATTEMPT_TO_GUESS_NAME, 0),
        )
        with(statsData) {
            averageAttemptToGuess =
                if (countGuessedWord == 0) 0f else sumAttemptToGuess.toFloat() / countGuessedWord
            averageGameTime = if (countGame == 0) 0f else totalGameTime / countGame
        }
        return statsData
    }

    override fun updateCountGame(countGame: Int) {
        editor.putInt(COUNT_GAME_NAME, settings.getInt(COUNT_GAME_NAME, 0) + countGame).apply()
    }

    override fun updateCountFinishedGame(countFinishedGame: Int) {
        editor.putInt(
            COUNT_FINISHED_GAME_NAME,
            settings.getInt(COUNT_FINISHED_GAME_NAME, 0) + countFinishedGame
        ).apply()
    }

    override fun updateCountGuessedWord(countGuessedWord: Int) {
        editor.putInt(
            COUNT_GUESSED_WORD_NAME,
            settings.getInt(COUNT_GUESSED_WORD_NAME, 0) + countGuessedWord
        ).apply()
    }

    override fun updateSumAttemptToGuess(attempt: Int) {
        editor.putInt(
            SUM_ATTEMPT_TO_GUESS_NAME,
            settings.getInt(SUM_ATTEMPT_TO_GUESS_NAME, 0) + attempt
        ).apply()
    }

    override fun updateTotalGameTime(gameTime: Float) {
        editor.putFloat(
            TOTAL_GAME_TIME_NAME,
            settings.getFloat(TOTAL_GAME_TIME_NAME, 0f) + gameTime
        ).apply()
    }

    override fun updateAllStats(statsData: StatsData) {
       updateCountGame(statsData.countGame)
        updateCountFinishedGame(statsData.countFinishedGame)
        updateCountGuessedWord(statsData.countGuessedWord)
        updateSumAttemptToGuess(statsData.sumAttemptToGuess)
        updateTotalGameTime(statsData.totalGameTime)
    }

    companion object {
        private const val STORAGE_NAME = "userStats"
        private const val COUNT_GAME_NAME = "countGame"
        private const val COUNT_FINISHED_GAME_NAME = "countFinishedGame"
        private const val COUNT_GUESSED_WORD_NAME = "countGuessedWord"
        private const val SUM_ATTEMPT_TO_GUESS_NAME = "sumAttemptToGuess"
        private const val TOTAL_GAME_TIME_NAME = "totalGameTime"

    }
}
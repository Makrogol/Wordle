package com.serebryakov.wordle.application.view.mainscreen

import com.serebryakov.wordle.application.model.HistoryData
import com.serebryakov.wordle.application.repository.historydatabaserepository.HistoryDatabaseRepository
import com.serebryakov.wordle.application.repository.statssharedprefrepository.StatsSharedPrefRepository
import com.serebryakov.wordle.application.repository.wordlistrepository.WordListRepository
import com.serebryakov.wordle.foundation.model.EmptyResult
import com.serebryakov.wordle.foundation.model.PendingResult
import com.serebryakov.wordle.foundation.navigator.Navigator
import com.serebryakov.wordle.foundation.uiactions.UiActions
import com.serebryakov.wordle.foundation.views.BaseScreen
import com.serebryakov.wordle.foundation.views.BaseViewModel
import com.serebryakov.wordle.foundation.views.LiveResult
import com.serebryakov.wordle.foundation.views.MutableLiveResult

class MainScreenViewModel(
    private val navigator: Navigator,
    private val uiActions: UiActions,
    private val wordListRepository: WordListRepository,
    private val sharedPrefRepository: StatsSharedPrefRepository,
    private val historyDatabaseRepository: HistoryDatabaseRepository
) : BaseViewModel() {

    private val _updateState = MutableLiveResult<Unit>(EmptyResult())
    val updateState: LiveResult<Unit> = _updateState

    private val _addHistoryToDatabase = MutableLiveResult<Unit>(PendingResult())
    val addHistoryToDatabase: LiveResult<Unit> = _addHistoryToDatabase

    fun addHistoryDataToDatabase(historyData: HistoryData) = into(_addHistoryToDatabase) {
        historyDatabaseRepository.insertNewHistoryData(historyData)
    }

    fun generateRandomWord() {
        wordListRepository.generateRandomWord()
    }

    fun isWordCorrect(word: String): Boolean {
        return wordListRepository.isWordCorrect(word)
    }

    fun validateWord(word: String): List<List<Int>> {
        return wordListRepository.validateWord(word)
    }

    fun getCurrentWord(): String {
        return wordListRepository.getCurrentWord()
    }

    fun toast(message: String) {
        uiActions.toast(message)
    }

    fun launch(screen: BaseScreen) {
        navigator.launch(screen)
    }

    fun updateCountGame() = into(_updateState) {
        sharedPrefRepository.updateCountGame()
    }

    fun updateCountFinishedGame() = into(_updateState) {
        sharedPrefRepository.updateCountFinishedGame()
    }

    fun updateCountGuessedWord() = into(_updateState) {
        sharedPrefRepository.updateCountGuessedWord()
    }

    fun updateAverageAttemptToGuess(attempt: Int) = into(_updateState) {
        sharedPrefRepository.updateAverageAttemptToGuess(attempt)
    }

    fun updateTotalGameTime(gameTime: Float) = into(_updateState) {
        sharedPrefRepository.updateTotalGameTime(gameTime)
    }

}
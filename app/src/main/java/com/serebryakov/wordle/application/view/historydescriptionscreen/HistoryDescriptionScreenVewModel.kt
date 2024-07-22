package com.serebryakov.wordle.application.view.historydescriptionscreen


import com.serebryakov.wordle.application.model.HistoryData
import com.serebryakov.wordle.application.repository.historydatabaserepository.HistoryDatabaseRepository
import com.serebryakov.wordle.foundation.navigator.Navigator
import com.serebryakov.wordle.foundation.uiactions.UiActions
import com.serebryakov.wordle.foundation.views.BaseScreen
import com.serebryakov.wordle.foundation.model.PendingResult
import com.serebryakov.wordle.foundation.views.BaseViewModel
import com.serebryakov.wordle.application.view.historydescriptionscreen.HistoryDescriptionScreenFragment.Screen
import com.serebryakov.wordle.foundation.views.LiveResult
import com.serebryakov.wordle.foundation.views.MutableLiveResult

class HistoryDescriptionScreenVewModel(
    screen: Screen,
    private val navigator: Navigator,
    private val uiActions: UiActions,
) : BaseViewModel() {

    private val _historyData = MutableLiveResult<HistoryData>(PendingResult())
    val historyData: LiveResult<HistoryData> = _historyData

    init {
        into(_historyData) {
            screen.historyData
        }
    }

    fun launch(screen: BaseScreen) {
        navigator.launch(screen)
    }

    fun toast(message: String) {
        uiActions.toast(message)
    }
}
package com.serebryakov.wordle.application.view.historyscreen

import com.serebryakov.wordle.application.model.HistoryData
import com.serebryakov.wordle.application.repository.historydatabaserepository.HistoryDatabaseRepository
import com.serebryakov.wordle.application.view.historydescriptionscreen.HistoryDescriptionScreenFragment
import com.serebryakov.wordle.foundation.model.PendingResult
import com.serebryakov.wordle.foundation.navigator.Navigator
import com.serebryakov.wordle.foundation.uiactions.UiActions
import com.serebryakov.wordle.foundation.views.BaseScreen
import com.serebryakov.wordle.foundation.views.BaseViewModel
import com.serebryakov.wordle.foundation.views.LiveResult
import com.serebryakov.wordle.foundation.views.MutableLiveResult

class HistoryScreenViewModel(
    private val navigator: Navigator,
    private val uiActions: UiActions,
    private val historyDatabaseRepository: HistoryDatabaseRepository
) : BaseViewModel(), HistoryAdapter.Listener {

    private val _historyData = MutableLiveResult<List<HistoryData>>(PendingResult())
    var historyData: LiveResult<List<HistoryData>> = _historyData

    init {
        getAllHistoryData()
    }

    private fun getAllHistoryData() = into(_historyData) {
        historyDatabaseRepository.getAllHistoryData()
    }

    fun toast(message: String) {
        uiActions.toast(message)
    }

    fun launch(screen: BaseScreen) {
        navigator.launch(screen)
    }

    override fun onHistoryDataClick(historyData: HistoryData) {
        launch(HistoryDescriptionScreenFragment.Screen(historyData))
    }
}
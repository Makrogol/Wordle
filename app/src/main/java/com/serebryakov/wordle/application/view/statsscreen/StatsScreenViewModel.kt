package com.serebryakov.wordle.application.view.statsscreen

import com.serebryakov.wordle.application.model.StatsData
import com.serebryakov.wordle.application.repository.statssharedprefrepository.StatsSharedPrefRepository
import com.serebryakov.wordle.foundation.model.PendingResult
import com.serebryakov.wordle.foundation.navigator.Navigator
import com.serebryakov.wordle.foundation.uiactions.UiActions
import com.serebryakov.wordle.foundation.views.BaseScreen
import com.serebryakov.wordle.foundation.views.BaseViewModel
import com.serebryakov.wordle.foundation.views.LiveResult
import com.serebryakov.wordle.foundation.views.MutableLiveResult

class StatsScreenViewModel(
    private val navigator: Navigator,
    private val uiActions: UiActions,
    private val statsSharedPrefRepository: StatsSharedPrefRepository
) : BaseViewModel() {

    private val _statsData = MutableLiveResult<StatsData>(PendingResult())
    val statsData: LiveResult<StatsData> = _statsData

    init {
        getStats()
    }

    private fun getStats() = into(_statsData) {
        statsSharedPrefRepository.getStats()
    }

    fun toast(message: String) {
        uiActions.toast(message)
    }

    fun launch(screen: BaseScreen) {
        navigator.launch(screen)
    }
}
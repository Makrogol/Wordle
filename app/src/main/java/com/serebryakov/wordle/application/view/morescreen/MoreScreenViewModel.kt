package com.serebryakov.wordle.application.view.morescreen

import com.serebryakov.wordle.foundation.navigator.Navigator
import com.serebryakov.wordle.foundation.uiactions.UiActions
import com.serebryakov.wordle.foundation.views.BaseScreen
import com.serebryakov.wordle.foundation.views.BaseViewModel

class MoreScreenViewModel(
    private val navigator: Navigator,
    private val uiActions: UiActions,
) : BaseViewModel() {

    fun launch(screen: BaseScreen) {
        navigator.launch(screen)
    }
}
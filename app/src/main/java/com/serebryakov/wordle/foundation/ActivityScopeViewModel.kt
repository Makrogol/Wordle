package com.serebryakov.wordle.foundation

import androidx.lifecycle.ViewModel
import com.serebryakov.wordle.foundation.navigator.IntermediateNavigator
import com.serebryakov.wordle.foundation.navigator.Navigator
import com.serebryakov.wordle.foundation.uiactions.UiActions

const val ARG_SCREEN = "ARG_SCREEN"

// Вью модель для активити

class ActivityScopeViewModel(
    val uiActions: UiActions,
    val navigator: IntermediateNavigator
) : ViewModel(),
    Navigator by navigator,
    UiActions by uiActions {

    override fun onCleared() {
        super.onCleared()
        navigator.clear()
    }

}
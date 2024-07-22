package com.serebryakov.wordle.foundation.navigator

import com.serebryakov.wordle.foundation.tools.ResourceActions
import com.serebryakov.wordle.foundation.views.BaseScreen


//Навигатор для работы на стороне вьюмодели

class IntermediateNavigator : Navigator {

    private val targetNavigator = ResourceActions<Navigator>()

    override fun launch(screen: BaseScreen) = targetNavigator {
        it.launch(screen)
    }

    fun setTarget(navigator: Navigator?) {
        targetNavigator.resource = navigator
    }

    fun clear() {
        targetNavigator.clear()
    }

}
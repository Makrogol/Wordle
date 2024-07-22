package com.serebryakov.wordle.foundation.navigator

import com.serebryakov.wordle.foundation.views.BaseScreen

interface Navigator {

    fun launch(screen: BaseScreen)
}
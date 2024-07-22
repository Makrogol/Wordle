package com.serebryakov.wordle.foundation.views

import com.serebryakov.wordle.foundation.ActivityScopeViewModel

// Базовый класс для активити

interface FragmentsHolder {
    fun getActivityScopeViewModel() : ActivityScopeViewModel
}
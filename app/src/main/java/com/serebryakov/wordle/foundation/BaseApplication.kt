package com.serebryakov.wordle.foundation

import com.serebryakov.wordle.foundation.model.Repository

interface BaseApplication {

    val singletonScopeDependencies: List<Any>
}
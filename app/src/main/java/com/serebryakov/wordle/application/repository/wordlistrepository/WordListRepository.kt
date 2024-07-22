package com.serebryakov.wordle.application.repository.wordlistrepository

import com.serebryakov.wordle.foundation.model.Repository

interface WordListRepository: Repository {

    fun generateRandomWord()

    fun isWordCorrect(word: String): Boolean

    fun validateWord(word: String): List<List<Int>>

    fun getCurrentWord(): String

}
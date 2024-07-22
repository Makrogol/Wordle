package com.serebryakov.wordle.application.model.wordlist

interface WordListModel  {

    fun generateRandomWord()

    fun isWordCorrect(word: String): Boolean

    fun validateWord(word: String): List<List<Int>>

    fun getCurrentWord(): String

}

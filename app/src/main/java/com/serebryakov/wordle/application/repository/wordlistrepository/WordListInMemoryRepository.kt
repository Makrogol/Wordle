package com.serebryakov.wordle.application.repository.wordlistrepository

import com.serebryakov.wordle.application.model.wordlist.WordListModel

class WordListInMemoryRepository(
    private val api: WordListModel
): WordListRepository {


    override fun generateRandomWord() {
        api.generateRandomWord()
    }

    override fun isWordCorrect(word: String): Boolean {
        return api.isWordCorrect(word)
    }

    override fun validateWord(word: String): List<List<Int>> {
        return api.validateWord(word)
    }

    override fun getCurrentWord(): String {
        return api.getCurrentWord()
    }
}
package com.serebryakov.wordle.application.model.wordlist

import com.serebryakov.wordle.application.model.wordlistdata.wordList
import kotlin.random.Random

class InMemoryWordListModel : WordListModel {

    //    private var wordList = wordList1 + wordList2 + wordList3 + wordList4 + wordList5 + wordList6
    private var currentWord = ""

    override fun generateRandomWord() {
        currentWord = wordList[Random.nextInt(wordList.size)]
        println(currentWord)
    }

    override fun isWordCorrect(word: String): Boolean {
        return word in wordList
    }

    override fun validateWord(word: String): List<List<Int>> {
        //0 - правильные и на правильных местах, 1 - правильные, но не на правильных местах, 2 - неправильные
        val correctIncorrectLetters =
            mutableListOf(mutableListOf<Int>(), mutableListOf(), mutableListOf())
        val usedLetterIndices = mutableListOf<Int>()

        for (i in currentWord.indices) {
            if (word[i] == currentWord[i]) {
                correctIncorrectLetters[0] += i
                usedLetterIndices += i
            }
        }
        for (i in currentWord.indices) {
            if (i !in correctIncorrectLetters[0]) {
                if (checkCurrentLetter(word, i, usedLetterIndices))
                    correctIncorrectLetters[1] += i
                else
                    correctIncorrectLetters[2] += i
            }
        }
        return correctIncorrectLetters
    }

    private fun checkCurrentLetter(word: String, idx: Int, usedLetters: MutableList<Int>): Boolean {
        for (i in currentWord.indices) {
            if (word[idx] == currentWord[i] && i !in usedLetters) {
                usedLetters += i
                return true
            }
        }
        return false
    }

    override fun getCurrentWord(): String {
        return currentWord
    }
}
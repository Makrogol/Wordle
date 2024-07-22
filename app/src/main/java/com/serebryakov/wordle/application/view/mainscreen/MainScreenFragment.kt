package com.serebryakov.wordle.application.view.mainscreen

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import com.serebryakov.wordle.R
import com.serebryakov.wordle.application.model.HistoryData
import com.serebryakov.wordle.application.renderSimpleResult
import com.serebryakov.wordle.application.view.morescreen.MoreScreenFragment
import com.serebryakov.wordle.databinding.MainScreenFragmentBinding
import com.serebryakov.wordle.foundation.tools.round
import com.serebryakov.wordle.foundation.views.BaseFragment
import com.serebryakov.wordle.foundation.views.BaseScreen
import com.serebryakov.wordle.foundation.views.screenViewModel
import java.text.SimpleDateFormat
import java.util.Calendar

class MainScreenFragment : BaseFragment() {


    class Screen : BaseScreen

    private lateinit var binding: MainScreenFragmentBinding
    override val viewModel by screenViewModel<MainScreenViewModel>()

    @SuppressLint("SimpleDateFormat")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MainScreenFragmentBinding.inflate(inflater, container, false)

        for (i in 0 until binding.keyboardLayout.childCount - 1) {
            for (j in 0 until getCountElementInKeyboardLine(i)) {
                getKeyboardButton(i, j).setOnClickListener {
                    keyboardListeners(i, j)
                }
            }
        }

        with(binding) {
            moreButton.setOnClickListener {
                viewModel.launch(MoreScreenFragment.Screen())
            }

            startNewGameButton.setOnClickListener {
                onStartGame()
            }


            verifyWorld.setOnClickListener {
                val currentWord = readCurrentWord(currentCellI)
                if (viewModel.isWordCorrect(currentWord)) {
                    val letters = viewModel.validateWord(currentWord)
                    fillBgDrawableIdxs(currentCellI, currentCellJ, R.drawable.default_letter_bg)
                    fillCorrectIncorrectLetter(currentCellI, letters)
                    fillCorrectIncorrectKeyboard(currentCellI, letters)
                    updateParams()

                    checkGameState(letters, currentWord)
                } else {
                    viewModel.toast(getString(R.string.that_word_isnt_in_dictionary_string))
                }
            }
        }

        viewModel.addHistoryToDatabase.observe(viewLifecycleOwner) { result ->
            renderSimpleResult(
                root = binding.root,
                result = result,
                onError = { viewModel.toast(getString(R.string.error_message_string)) },
                onSuccess = {
                    if (currentCellJ == 0 && currentCellI == 0)
                        setStartGameVisibility()
                    else
                        setEndGameVisibility()
                }
            )
        }

        viewModel.updateState.observe(viewLifecycleOwner) { result ->
            renderSimpleResult(
                root = binding.root,
                result = result,
                onError = { viewModel.toast(getString(R.string.error_message_string)) },
                onEmpty = { setEndGameVisibility() },
                onSuccess = {
                    if (currentCellJ == 0 && currentCellI == 0)
                        setStartGameVisibility()
                    else
                        setEndGameVisibility()
                }
            )
        }

        return binding.root
    }


    private fun onStartGame() {
        viewModel.generateRandomWord()
        clearCells()
        clearKeyboard()
        setStartGameVisibility()
        setStartGameParams()
        fillBgDrawableIdxs(currentCellI, currentCellJ, R.drawable.selected_letter_bg)
        viewModel.updateCountGame()
        startTime = System.currentTimeMillis()
    }

    private fun checkGameState(letters: List<List<Int>>, currentWord: String) {
        if (letters[0].size == currentWord.length)
            onWinGame(currentWord)
        else if (currentCellI == countRow)
            onLooseGame(viewModel.getCurrentWord())
        else
            onContinuousGame()
    }

    private fun updateParams() {
        currentCellI++
        currentCellJ = 0
    }

    private fun onContinuousGame() {
        fillBgDrawableIdxs(currentCellI, currentCellJ, R.drawable.selected_letter_bg)
    }

    private fun onEndGame() {
        setEndGameVisibility()
        gameTime = ((System.currentTimeMillis() - startTime) / 1000f).round(2)
    }

    private fun onLooseGame(currentWord: String) {
        onEndGame()
        binding.endGameTextview.text = getString(
            R.string.attempt_are_over_the_word_was_string,
            currentWord
        )

        viewModel.addHistoryDataToDatabase(
            HistoryData(
                id = 0,
                word = currentWord,
                date = getCurrentDateTime(),
                attempt = -1,
                description = getDescription(),
                gameTime = gameTime
            )
        )
        updateStatistics(winGame = false)
    }

    private fun getDescription(): String {
        var description = ""
        for (i in 0 until currentCellI)
            for (j in 0 until countColumn)
                description += getBgCellId(i, j) + " "
        return description.substring(0, description.length - 1)
    }

    private fun getBgCellId(currentI: Int, currentJ: Int): String =
        when (getWordLayoutButton(currentI, currentJ).tag) {
            R.drawable.incorrect_letter_bg -> INCORRECT_LETTER_ID
            R.drawable.correct_letter_in_incorrect_position_bg -> CORRECT_LETTER_IN_INCORRECT_POSITION_ID
            R.drawable.correct_letter_in_correct_position_bg -> CORRECT_LETTER_IN_CORRECT_POSITION_ID
            else -> ""
        }

    @SuppressLint("SimpleDateFormat")
    private fun getCurrentDateTime(): String {
        val c: Calendar = Calendar.getInstance()
        val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
        return sdf.format(c.time)
    }

    private fun updateStatistics(winGame: Boolean) {
        viewModel.updateCountFinishedGame()
        viewModel.updateTotalGameTime(gameTime)
        if (winGame) {
            viewModel.updateCountGuessedWord()
            viewModel.updateAverageAttemptToGuess(currentCellI)
        }
    }

    private fun onWinGame(currentWord: String) {
        onEndGame()
        binding.endGameTextview.text = getString(R.string.you_are_guessed_word_string)

        viewModel.addHistoryDataToDatabase(
            HistoryData(
                id = 0,
                word = currentWord,
                date = getCurrentDateTime(),
                attempt = currentCellI,
                description = getDescription(),
                gameTime = gameTime
            )
        )
        updateStatistics(winGame = true)
    }

    private fun setStartGameParams() {
        currentCellJ = 0
        currentCellI = 0
    }

    private fun setEndGameVisibility() = with(binding) {
        keyboardLayout.visibility = View.GONE
        verifyWorld.visibility = View.GONE
        startNewGameButton.visibility = View.VISIBLE
        endGameTextview.visibility = View.VISIBLE
    }

    private fun setStartGameVisibility() = with(binding) {
        keyboardLayout.visibility = View.VISIBLE
        verifyWorld.visibility = View.VISIBLE
        startNewGameButton.visibility = View.GONE
        endGameTextview.visibility = View.GONE
    }

    private fun getKeyboardButton(i: Int, j: Int): Button = with(binding.keyboardLayout) {
        ((getChildAt(i) as LinearLayout).getChildAt(j) as Button)
    }

    private fun getWordLayoutButton(i: Int, j: Int): Button = with(binding.worldLayout) {
        ((getChildAt(i) as LinearLayout).getChildAt(j) as Button)
    }

    private fun keyboardListeners(i: Int, j: Int) {
        if (getLetterFromKeyboard(i, j) != "←") {
            if (currentCellJ in 0 until countColumn) {
                setLetterInCellIdxs(currentCellI, currentCellJ, letterInKeyboardFromPosition(i, j))
                fillBgDrawableIdxs(currentCellI, currentCellJ, R.drawable.default_letter_bg)
                currentCellJ += if (currentCellJ < countColumn - 1) 1 else 0
                fillBgDrawableIdxs(currentCellI, currentCellJ, R.drawable.selected_letter_bg)
            }
        } else {
            if (currentCellJ in 0 until countColumn) {
                if (checkCurrentLetterIsEmpty(currentCellI, currentCellJ)) {
                    fillBgDrawableIdxs(currentCellI, currentCellJ, R.drawable.default_letter_bg)
                    currentCellJ -= if (currentCellJ > 0) 1 else 0
                }
                setLetterInCellIdxs(currentCellI, currentCellJ, emptyLetter)
                fillBgDrawableIdxs(currentCellI, currentCellJ, R.drawable.selected_letter_bg)
            }
        }
    }

    private fun letterInKeyboardFromPosition(currentI: Int, currentJ: Int): String {
        return indexToLetter[if (currentI == 0) currentJ else if (currentI == 1) currentJ + 12 else currentJ + 23]
    }

    private fun fillCorrectIncorrectKeyboard(currentI: Int, lettersPosition: List<List<Int>>) {
        for (i in lettersPosition[2]) {
            fillBgKeyboardLetter(
                getLetterFromCell(currentI, i),
                R.drawable.incorrect_keyboard_bg
            )
        }
        for (i in lettersPosition[1]) {
            fillBgKeyboardLetter(
                getLetterFromCell(currentI, i),
                R.drawable.correct_in_incorrect_position_keyboard_bg
            )
        }
        for (i in lettersPosition[0]) {
            fillBgKeyboardLetter(
                getLetterFromCell(currentI, i),
                R.drawable.correct_in_correct_position_keyboard_bg
            )
        }
    }

    private fun fillCorrectIncorrectLetter(currentI: Int, lettersPosition: List<List<Int>>) {
        for (i in lettersPosition[2])
            fillBgDrawableIdxs(currentI, i, R.drawable.incorrect_letter_bg)
        for (i in lettersPosition[1])
            fillBgDrawableIdxs(currentI, i, R.drawable.correct_letter_in_incorrect_position_bg)
        for (i in lettersPosition[0])
            fillBgDrawableIdxs(currentI, i, R.drawable.correct_letter_in_correct_position_bg)
    }

    private fun getIdxsLetter(letter: String): Pair<Int, Int> {
        val idx = letterToIndex[letter]!!
        return when {
            (idx in 0..11) -> Pair(0, idx)
            (idx in 12..22) -> Pair(1, idx - 12)
            else -> Pair(2, idx - 23)
        }
    }

    private fun fillBgKeyboardIdxs(currentI: Int, currentJ: Int, drawable: Int) {
        with(getKeyboardButton(currentI, currentJ)) {
            if (tag != R.drawable.correct_in_correct_position_keyboard_bg) {
                setBackgroundResource(drawable)
                tag = drawable
            }
        }
    }

    private fun fillBgKeyboardLetter(letter: String, drawable: Int) {
        val idxs = getIdxsLetter(letter)
        with(getKeyboardButton(idxs.first, idxs.second)) {
            if (tag != R.drawable.correct_in_correct_position_keyboard_bg) {
                setBackgroundResource(drawable)
                tag = drawable
            }
        }
    }

    private fun checkCurrentLetterIsEmpty(currentI: Int, currentJ: Int): Boolean =
        (getWordLayoutButton(currentI, currentJ).text == emptyLetter)

    private fun fillBgDrawableIdxs(currentI: Int, currentJ: Int, drawable: Int) {
        with(getWordLayoutButton(currentI, currentJ)) {
            setBackgroundResource(drawable)
            tag = drawable
        }
    }

    private fun getCountElementInKeyboardLine(line: Int): Int {
        return (binding.keyboardLayout.getChildAt(line) as LinearLayout).childCount
    }

    private fun clearKeyboard() {
        for (i in 0 until binding.keyboardLayout.childCount - 1) {
            for (j in 0 until getCountElementInKeyboardLine(i)) {
                setTagKeyboardLetter(i, j, R.drawable.default_keyboard_bg)
                fillBgKeyboardIdxs(i, j, R.drawable.default_keyboard_bg)
            }
        }
    }

    private fun setTagKeyboardLetter(currentI: Int, currentJ: Int, tag: Int) {
        getKeyboardButton(currentI, currentJ).tag = tag
    }

    private fun clearCells() {
        for (i in 0 until countRow) {
            for (j in 0 until countColumn) {
                setLetterInCellIdxs(i, j, emptyLetter)
                fillBgDrawableIdxs(i, j, R.drawable.default_letter_bg)
            }
        }
    }

    private fun readCurrentWord(currentI: Int): String {
        var currentWord = ""
        for (j in 0 until countColumn)
            currentWord += getWordLayoutButton(currentI, j).text
        return currentWord
    }

    private fun getLetterFromCell(currentI: Int, currentJ: Int): String =
        getWordLayoutButton(currentI, currentJ).text.toString()


    private fun getLetterFromKeyboard(currentI: Int, currentJ: Int): String =
        getKeyboardButton(currentI, currentJ).text.toString()

    private fun setLetterInCellIdxs(currentI: Int, currentJ: Int, letter: String) {
        getWordLayoutButton(currentI, currentJ).text = letter
    }


    companion object {

        var currentCellI = 0
        var currentCellJ = 0

        var startTime: Long = 0
        var gameTime: Float = 0f

        const val INCORRECT_LETTER_ID = "0"
        const val CORRECT_LETTER_IN_INCORRECT_POSITION_ID = "1"
        const val CORRECT_LETTER_IN_CORRECT_POSITION_ID = "2"

        const val countRow = 6
        const val countColumn = 5

        const val emptyLetter = ""
        val letterToIndex = mapOf(
            "й" to 0,
            "ц" to 1,
            "у" to 2,
            "к" to 3,
            "е" to 4,
            "н" to 5,
            "г" to 6,
            "ш" to 7,
            "щ" to 8,
            "з" to 9,
            "х" to 10,
            "ъ" to 11,
            "ф" to 12,
            "ы" to 13,
            "в" to 14,
            "а" to 15,
            "п" to 16,
            "р" to 17,
            "о" to 18,
            "л" to 19,
            "д" to 20,
            "ж" to 21,
            "э" to 22,
            "я" to 23,
            "ч" to 24,
            "с" to 25,
            "м" to 26,
            "и" to 27,
            "т" to 28,
            "ь" to 29,
            "б" to 30,
            "ю" to 31,
        )
        val indexToLetter = listOf(
            "й",
            "ц",
            "у",
            "к",
            "е",
            "н",
            "г",
            "ш",
            "щ",
            "з",
            "х",
            "ъ",
            "ф",
            "ы",
            "в",
            "а",
            "п",
            "р",
            "о",
            "л",
            "д",
            "ж",
            "э",
            "я",
            "ч",
            "с",
            "м",
            "и",
            "т",
            "ь",
            "б",
            "ю"
        )
    }
}
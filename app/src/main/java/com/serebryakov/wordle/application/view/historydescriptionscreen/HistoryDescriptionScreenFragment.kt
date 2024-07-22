package com.serebryakov.wordle.application.view.historydescriptionscreen

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.serebryakov.wordle.R
import com.serebryakov.wordle.application.model.HistoryData
import com.serebryakov.wordle.application.renderSimpleResult
import com.serebryakov.wordle.application.view.historyscreen.HistoryScreenFragment
import com.serebryakov.wordle.application.view.mainscreen.MainScreenFragment
import com.serebryakov.wordle.databinding.HistoryDescriptionScreenFragmentBinding
import com.serebryakov.wordle.foundation.tools.ScreenUtils
import com.serebryakov.wordle.foundation.tools.round
import com.serebryakov.wordle.foundation.views.BaseFragment
import com.serebryakov.wordle.foundation.views.BaseScreen
import com.serebryakov.wordle.foundation.views.screenViewModel


class HistoryDescriptionScreenFragment : BaseFragment() {

    class Screen(val historyData: HistoryData) : BaseScreen

    private lateinit var binding: HistoryDescriptionScreenFragmentBinding
    override val viewModel by screenViewModel<HistoryDescriptionScreenVewModel>()


    @SuppressLint("StringFormatMatches", "ResourceAsColor")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = HistoryDescriptionScreenFragmentBinding.inflate(inflater, container, false)

        binding.closeButton.setOnClickListener { viewModel.launch(HistoryScreenFragment.Screen()) }
        viewModel.historyData.observe(viewLifecycleOwner) { result ->
            renderSimpleResult(
                root = binding.root,
                result = result,
                onError = { viewModel.toast(getString(R.string.error_message_string)) },
                onSuccess = { historyData ->
                    with(binding) {
                        gameIdTextview.text = getString(
                            R.string.history_description_screen_game_id_string,
                            historyData.id
                        )

                        val size = generateElementSize()
                        for ((i, el) in historyData.description.split(' ').withIndex()) {
                            with(descriptionGridlayout.getChildAt(i)) {
                                setBackgroundResource(getResourcesFromElementDescription(el))
                                visibility = View.VISIBLE
                                layoutParams.width = size
                                layoutParams.height = size
                            }
                        }

                        wordTextview.text = getString(
                            R.string.history_description_screen_word_string,
                            historyData.word
                        )
                        dateTextview.text = getString(
                            R.string.history_description_screen_date_string,
                            historyData.date
                        )
                        attemptTextview.text = if (historyData.attempt == -1)
                            getString(R.string.attempt_in_loose_game_string)
                        else
                            getString(R.string.attempt_in_win_game_string, historyData.attempt)
                        gameTimeTextview.text = getString(
                            R.string.history_description_screen_game_time_string,
                            (historyData.gameTime / 3600).toInt(), (historyData.gameTime / 60).toInt(), (historyData.gameTime % 60).round(2)
                        )
                    }
                }
            )
        }
        return binding.root
    }

    private fun generateElementSize(): Int =
        context?.let { (ScreenUtils.getScreenWidth(it) / MainScreenFragment.countColumn) - 50 }!!

    private fun getResourcesFromElementDescription(element: String): Int = when (element) {
        MainScreenFragment.INCORRECT_LETTER_ID -> R.drawable.incorrect_keyboard_bg
        MainScreenFragment.CORRECT_LETTER_IN_INCORRECT_POSITION_ID -> R.drawable.correct_in_incorrect_position_keyboard_bg
        MainScreenFragment.CORRECT_LETTER_IN_CORRECT_POSITION_ID -> R.drawable.correct_in_correct_position_keyboard_bg
        else -> R.drawable.default_keyboard_bg
    }

    @SuppressLint("ViewConstructor")
    internal class DrawDescriptionView(context: Context?, private val description: String) :
        View(context) {
        @SuppressLint("DrawAllocation", "ResourceAsColor")
        override fun onDraw(canvas: Canvas) {
            super.onDraw(canvas)

            val greenPaint = Paint()
            greenPaint.color = Color.GREEN
            greenPaint.style = Paint.Style.FILL
            greenPaint.strokeWidth = 2f

            val yellowPaint = Paint()
            yellowPaint.color = Color.YELLOW
            yellowPaint.style = Paint.Style.FILL
            yellowPaint.strokeWidth = 2f
            val grayPaint = Paint()
            grayPaint.color = Color.GRAY
            grayPaint.style = Paint.Style.FILL
            grayPaint.strokeWidth = 2f

            for ((i, el) in description.split(' ').withIndex()) {
                val left = 10 + 100 * (i % 5)
                val top = 100 * (i / 5)
                val right = left + 100
                val bottom = top + 100
                canvas.drawRect(
                    Rect(left, top, right, bottom),
                    if (el == "0") grayPaint else if (el == "1") yellowPaint else greenPaint
                )
            }
        }
    }

}
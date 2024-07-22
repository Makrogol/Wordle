package com.serebryakov.wordle.application.view.statsscreen

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.serebryakov.wordle.R
import com.serebryakov.wordle.application.model.StatsData
import com.serebryakov.wordle.application.renderSimpleResult
import com.serebryakov.wordle.application.view.morescreen.MoreScreenFragment
import com.serebryakov.wordle.databinding.StatsScreenFragmentBinding
import com.serebryakov.wordle.foundation.tools.round
import com.serebryakov.wordle.foundation.tools.toPercent
import com.serebryakov.wordle.foundation.views.BaseFragment
import com.serebryakov.wordle.foundation.views.BaseScreen
import com.serebryakov.wordle.foundation.views.screenViewModel
import kotlin.math.min

class StatsScreenFragment : BaseFragment() {

    class Screen : BaseScreen

    private lateinit var binding: StatsScreenFragmentBinding
    override val viewModel by screenViewModel<StatsScreenViewModel>()

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = StatsScreenFragmentBinding.inflate(inflater, container, false)

        var currentState = PERCENT
        var statsData = StatsData()

        with(binding) {
            changeStateButton.text = getString(R.string.change_state_string, COUNT)

            closeButton.setOnClickListener {
                viewModel.launch(MoreScreenFragment.Screen())
            }

            changeStateButton.setOnClickListener {
                changeStateButton.text = getString(R.string.change_state_string, currentState)
                currentState = if (currentState == PERCENT) COUNT else PERCENT
                outStatsDataWithCurrentState(statsData, currentState)
            }
        }

        viewModel.statsData.observe(viewLifecycleOwner) { result ->
            renderSimpleResult(
                root = binding.root,
                result = result,
                onError = { viewModel.toast("Ошибка") },
                onSuccess = { statsDataViewModel ->
                    statsData = statsDataViewModel
                    outStatsDataWithCurrentState(statsData, currentState)
                }
            )
        }
        return binding.root
    }

    @SuppressLint("SetTextI18n", "StringFormatMatches")
    private fun outIfZeroGame(currentState: String) = with(binding) {
        countGameTextview.text = getString(R.string.empty_int_data)
        averageAttemptToGuessTextview.text = getString(
            R.string.average_game_time_fill_string, 0, 0, 0
        )
        totalGameTimeTextview.text = getString(
            R.string.total_game_time_fill_string, 0, 0, 0, 0
        )
        averageGameTimeTextview.text = getString(R.string.empty_float_data)
        if (currentState == PERCENT) {
            countFinishedGameTextview.text = getString(R.string.empty_percent_data)
            countGuessedWordTextview.text = getString(R.string.empty_percent_data)
        } else {
            countFinishedGameTextview.text = getString(R.string.empty_int_data)
            countGuessedWordTextview.text = getString(R.string.empty_int_data)
        }
    }

    private fun formGameTimeString(
        day: Int,
        hours: Int,
        minutes: Int,
        seconds: Float
    ): String {
        var res = ""
        if (day > 0)
            res += "$day дн, "
        if (hours > 0)
            res += "$hours час, "
        if (minutes > 0)
            res += "$minutes мин, "
        res += "$seconds сек"
        return res
    }

    @SuppressLint("SetTextI18n", "StringFormatMatches")
    private fun outStatsDataWithCurrentState(statsData: StatsData, currentState: String) =
        with(binding) {
            if (statsData.countGame == 0) {
                outIfZeroGame(currentState)
            } else {
                countGameTextview.text = statsData.countGame.toString()
                averageAttemptToGuessTextview.text =
                    statsData.averageAttemptToGuess.round(2).toString()
                totalGameTimeTextview.text = formGameTimeString(
                    (statsData.totalGameTime / 86400).toInt(),
                    (statsData.totalGameTime / 3600).toInt(),
                    (statsData.totalGameTime / 60).toInt(),
                    (statsData.totalGameTime % 60).round(2)
                )

                averageGameTimeTextview.text = formGameTimeString(
                    0,
                    (statsData.averageGameTime / 3600).toInt(),
                    (statsData.averageGameTime / 60).toInt(),
                    (statsData.averageGameTime % 60).round(2)
                )

                if (currentState == PERCENT) {
                    countFinishedGameTextview.text =
                        (statsData.countFinishedGame.toFloat() / statsData.countGame).toPercent()
                    countGuessedWordTextview.text =
                        (statsData.countGuessedWord.toFloat() / statsData.countGame).toPercent()
                } else {
                    countFinishedGameTextview.text = statsData.countFinishedGame.toString()
                    countGuessedWordTextview.text = statsData.countGuessedWord.toString()
                }
            }
        }

    companion object {
        val PERCENT = "Процент"
        val COUNT = "Количество"
    }
}
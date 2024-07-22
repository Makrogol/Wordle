package com.serebryakov.wordle.application.view.morescreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.serebryakov.wordle.application.view.historyscreen.HistoryScreenFragment
import com.serebryakov.wordle.application.view.mainscreen.MainScreenFragment
import com.serebryakov.wordle.application.view.referencescreen.ReferenceScreenFragment
import com.serebryakov.wordle.application.view.statsscreen.StatsScreenFragment
import com.serebryakov.wordle.databinding.MainScreenFragmentBinding
import com.serebryakov.wordle.databinding.MoreScreenFragmentBinding
import com.serebryakov.wordle.foundation.views.BaseFragment
import com.serebryakov.wordle.foundation.views.BaseScreen
import com.serebryakov.wordle.foundation.views.BaseViewModel
import com.serebryakov.wordle.foundation.views.screenViewModel

class MoreScreenFragment: BaseFragment() {

    class Screen : BaseScreen

    private lateinit var binding: MoreScreenFragmentBinding
    override val viewModel by screenViewModel<MoreScreenViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MoreScreenFragmentBinding.inflate(inflater, container, false)

        with(binding) {
            statsButton.setOnClickListener {
                viewModel.launch(StatsScreenFragment.Screen())
            }

            closeButton.setOnClickListener {
                viewModel.launch(MainScreenFragment.Screen())
            }

            historyButton.setOnClickListener {
                viewModel.launch(HistoryScreenFragment.Screen())
            }

            referenceButton.setOnClickListener {
                viewModel.launch(ReferenceScreenFragment.Screen())
            }
        }

        return binding.root
    }
}
package com.serebryakov.wordle.application.view.historyscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.serebryakov.wordle.R
import com.serebryakov.wordle.application.renderSimpleResult
import com.serebryakov.wordle.application.view.morescreen.MoreScreenFragment
import com.serebryakov.wordle.databinding.HistoryScreenFragmentBinding
import com.serebryakov.wordle.foundation.views.BaseFragment
import com.serebryakov.wordle.foundation.views.BaseScreen
import com.serebryakov.wordle.foundation.views.screenViewModel

class HistoryScreenFragment : BaseFragment() {

    class Screen : BaseScreen

    private lateinit var binding: HistoryScreenFragmentBinding
    override val viewModel by screenViewModel<HistoryScreenViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = HistoryScreenFragmentBinding.inflate(inflater, container, false)


        viewModel.historyData.observe(viewLifecycleOwner) { result ->
            renderSimpleResult(
                root = binding.root,
                result = result,
                onError = { viewModel.toast(getString(R.string.error_message_string)) },
                onSuccess = { historyData ->
                    val adapter = HistoryAdapter(viewModel)
                    binding.historyRecyclerview.adapter = adapter
                    adapter.addHistoryData(historyData.reversed())
                }
            )
        }

        binding.closeButton.setOnClickListener {
            viewModel.launch(MoreScreenFragment.Screen())
        }

        return binding.root
    }

}
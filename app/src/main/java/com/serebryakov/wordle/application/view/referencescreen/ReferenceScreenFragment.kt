package com.serebryakov.wordle.application.view.referencescreen

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.text.Selection
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.serebryakov.wordle.R
import com.serebryakov.wordle.application.MainActivity
import com.serebryakov.wordle.application.model.HistoryData
import com.serebryakov.wordle.application.model.StatsData
import com.serebryakov.wordle.application.model.toHistoryData
import com.serebryakov.wordle.application.model.toStatsData
import com.serebryakov.wordle.application.renderSimpleResult
import com.serebryakov.wordle.application.view.morescreen.MoreScreenFragment
import com.serebryakov.wordle.databinding.ReferenceScreenFragmentBinding
import com.serebryakov.wordle.foundation.views.BaseFragment
import com.serebryakov.wordle.foundation.views.BaseScreen
import com.serebryakov.wordle.foundation.views.screenViewModel

class ReferenceScreenFragment : BaseFragment() {

    class Screen : BaseScreen

    private lateinit var binding: ReferenceScreenFragmentBinding
    override val viewModel by screenViewModel<ReferenceScreenViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ReferenceScreenFragmentBinding.inflate(inflater, container, false)

        with(binding) {
            closeButton.setOnClickListener {
                viewModel.launch(MoreScreenFragment.Screen())
            }

            referenceTextview.text = createReferenceText()
            referenceTextview.movementMethod = LinkMovementMethod.getInstance()

            saveStatsButton.setOnClickListener {
                (activity as MainActivity).createFile(
                    "txt",
                    "wordle_app_stats_data",
                    (activity as MainActivity).TYPE_STATS_SAVING
                )
                viewModel.loadStatsData()
            }
            saveHistoryButton.setOnClickListener {
                (activity as MainActivity).createFile(
                    "txt",
                    "wordle_app_history_data",
                    (activity as MainActivity).TYPE_HISTORY_SAVING
                )
                viewModel.loadHistoryData()
            }
        }

        viewModel.statsData.observe(viewLifecycleOwner) { result ->
            renderSimpleResult(
                root = binding.root,
                result = result,
                onError = { viewModel.toast("Ошибка") },
                onSuccess = { statsData ->
                    (activity as MainActivity).historySavingCallback = { uri ->
                        viewModel.saveStatsData(statsData, requireContext(), uri)
                    }
                }
            )
        }

        viewModel.historyDataList.observe(viewLifecycleOwner) { result ->
            renderSimpleResult(
                root = binding.root,
                result = result,
                onError = { viewModel.toast("Ошибка") },
                onSuccess = { historyDataList ->
                    (activity as MainActivity).historySavingCallback = { uri ->
                        viewModel.saveHistoryData(historyDataList, requireContext(), uri)
                    }
                }
            )
        }

        viewModel.historySaving.observe(viewLifecycleOwner) { result ->
            renderSimpleResult(
                root = binding.root,
                result = result,
                onError = { viewModel.toast("Ошибка") },
                onSuccess = {
                    viewModel.toast("История успешно сохранена")
                }
            )
        }

        viewModel.statsSaving.observe(viewLifecycleOwner) { result ->
            renderSimpleResult(
                root = binding.root,
                result = result,
                onError = { viewModel.toast("Ошибка") },
                onSuccess = {
                    viewModel.toast("Статистика успешно сохранена")
                }
            )
        }

        return binding.root
    }

    private fun createReferenceText(): SpannableString {
        val refTgText = getString(R.string.programmer_link_text)

        val textRef1 = TextRef(getString(R.string.reference_text1))
        val textRef2 = TextRef(getString(R.string.reference_text2), textRef1.end)
        val textRef3 = TextRef(getString(R.string.reference_text3), textRef2.end)
        val textRef4 = TextRef(getString(R.string.reference_text4), textRef3.end)
        val textRef5 = TextRef(getString(R.string.reference_text5), textRef4.end)
        val textRef6 = TextRef(getString(R.string.reference_text6), textRef5.end)
        val textRef7 = TextRef(getString(R.string.reference_text7, refTgText), textRef6.end)

        val clickableSpan = object : ClickableSpan() {
            override fun onClick(view: View) {
                Selection.setSelection((view as TextView).text as Spannable, 0)
                view.invalidate()
                context!!.copyToClipboard(refTgText)
                viewModel.toast(getString(R.string.copying_text))
            }
        }

        val textRef =
            textRef1.text + textRef2.text + textRef3.text + textRef4.text + textRef5.text + textRef6.text + textRef7.text
        val text = SpannableString(textRef)
        text.setSpan(
            StyleSpan(Typeface.BOLD),
            textRef1.start,
            textRef1.end,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        text.setSpan(
            StyleSpan(Typeface.BOLD),
            textRef3.start,
            textRef3.end,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        text.setSpan(
            StyleSpan(Typeface.BOLD),
            textRef5.start,
            textRef5.end,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        text.setSpan(
            clickableSpan,
            textRef7.end - refTgText.length,
            textRef7.end,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return text
    }

    private class TextRef(
        val text: String,
        val start: Int = 0,
        val end: Int = start + text.length
    )

    fun Context.copyToClipboard(text: CharSequence) {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("", text)
        clipboard.setPrimaryClip(clip)
    }
}
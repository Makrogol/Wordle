package com.serebryakov.wordle.application.view.historyscreen

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.serebryakov.wordle.R
import com.serebryakov.wordle.application.model.HistoryData
import com.serebryakov.wordle.databinding.ItemHistoryRecyclerviewBinding


class HistoryAdapter(
    private val listener: Listener
) : RecyclerView.Adapter<HistoryAdapter.HistoryDataViewHolder>(), View.OnClickListener {

    private val _history = mutableListOf<HistoryData>()
    val history: List<HistoryData> = _history

    @SuppressLint("NotifyDataSetChanged")
    fun addHistoryData(historyData: List<HistoryData>) {
        _history.addAll(historyData)
        notifyDataSetChanged()
    }

    override fun onClick(v: View) {
        val historyData = v.tag as HistoryData
        listener.onHistoryDataClick(historyData)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryDataViewHolder {
        val binding = ItemHistoryRecyclerviewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return HistoryDataViewHolder(binding)
    }


    @SuppressLint("StringFormatMatches")
    override fun onBindViewHolder(holder: HistoryDataViewHolder, position: Int) {
        val historyData = _history[position]
        holder.itemView.tag = historyData
        holder.binding.root.setOnClickListener(this)
        val context = holder.itemView.context


        with(holder.binding) {
            wordTextview.text = historyData.word
            dateTextview.text = historyData.date
            attemptTextview.text = if (historyData.attempt == -1)
                context.getString(R.string.attempt_in_loose_game_string)
            else
                context.getString(R.string.attempt_in_win_game_string, historyData.attempt)

        }
    }


    override fun getItemCount() = _history.size

    class HistoryDataViewHolder(
        val binding: ItemHistoryRecyclerviewBinding,
    ) : RecyclerView.ViewHolder(binding.root)


    interface Listener {
        fun onHistoryDataClick(historyData: HistoryData)
    }

}
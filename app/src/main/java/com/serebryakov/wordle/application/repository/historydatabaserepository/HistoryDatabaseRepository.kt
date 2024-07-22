package com.serebryakov.wordle.application.repository.historydatabaserepository

import com.serebryakov.wordle.application.model.HistoryData
import com.serebryakov.wordle.foundation.model.Repository

interface HistoryDatabaseRepository: Repository {

    suspend fun insertNewHistoryData(historyData: HistoryData)

    suspend fun getAllHistoryData(): List<HistoryData>
}
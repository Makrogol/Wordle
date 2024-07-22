package com.serebryakov.wordle.application.repository.historydatabaserepository

import com.serebryakov.wordle.application.model.HistoryData
import com.serebryakov.wordle.application.model.historydatabase.HistoryDao
import com.serebryakov.wordle.application.model.historydatabase.entities.HistoryDbEntity
import com.serebryakov.wordle.foundation.model.IoDispatcher
import kotlinx.coroutines.withContext

class HistoryDataBaseRepositoryImpl(
    private val ioDispatcher: IoDispatcher,
    private val historyDao: HistoryDao
): HistoryDatabaseRepository {

    override suspend fun insertNewHistoryData(historyData: HistoryData) = withContext(ioDispatcher.value) {
        historyDao.insertNewHistoryData(historyData.toHistoryDbEntity())
    }

    override suspend fun getAllHistoryData(): List<HistoryData> = withContext(ioDispatcher.value) {
        val historyDataDbEntity = historyDao.getAllHistoryData()
        val historyData = mutableListOf<HistoryData>()
        for (el in historyDataDbEntity) {
            historyData.add(el.toHistoryData())
        }
        historyData
    }

}
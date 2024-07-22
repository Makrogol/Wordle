package com.serebryakov.wordle.application.view.referencescreen

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import com.serebryakov.wordle.application.model.HistoryData
import com.serebryakov.wordle.application.model.StatsData
import com.serebryakov.wordle.application.repository.historydatabaserepository.HistoryDatabaseRepository
import com.serebryakov.wordle.application.repository.statssharedprefrepository.StatsSharedPrefRepository
import com.serebryakov.wordle.foundation.model.EmptyResult
import com.serebryakov.wordle.foundation.model.PendingResult
import com.serebryakov.wordle.foundation.navigator.Navigator
import com.serebryakov.wordle.foundation.uiactions.UiActions
import com.serebryakov.wordle.foundation.views.BaseScreen
import com.serebryakov.wordle.foundation.views.BaseViewModel
import com.serebryakov.wordle.foundation.views.LiveResult
import com.serebryakov.wordle.foundation.views.MutableLiveResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.PrintStream

class ReferenceScreenViewModel(
    private val navigator: Navigator,
    private val uiActions: UiActions,
    private val statsSharedPrefRepository: StatsSharedPrefRepository,
    private val historyDatabaseRepository: HistoryDatabaseRepository
) : BaseViewModel() {

    private val _historyDataList = MutableLiveResult<List<HistoryData>>(EmptyResult())
    var historyDataList: LiveResult<List<HistoryData>> = _historyDataList

    private val _statsData = MutableLiveResult<StatsData>(EmptyResult())
    val statsData: LiveResult<StatsData> = _statsData

    private val _historySaving = MutableLiveResult<Unit>(EmptyResult())
    val historySaving: LiveResult<Unit> = _historySaving

    private val _statsSaving = MutableLiveResult<Unit>(EmptyResult())
    val statsSaving: LiveResult<Unit> = _statsSaving

    fun loadHistoryData() = into(_historyDataList) {
        historyDatabaseRepository.getAllHistoryData()
    }

    fun loadStatsData() = into(_statsData) {
        statsSharedPrefRepository.getStats()
    }

    fun saveHistoryData(historyDataList: List<HistoryData>, applicationContext: Context, uri: Uri) =
        into(_historySaving) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val dst = applicationContext.contentResolver.openOutputStream(uri)
                val printStream = PrintStream(dst)
                for (historyData in historyDataList)
                    printStream.print(historyData.toString())
                withContext(Dispatchers.IO) {
                    printStream.close()
                    dst!!.close()
                }
            } else throw Exception("Не удалось сохранить историю")
        }

    fun saveStatsData(statsData: StatsData, applicationContext: Context, uri: Uri) =
        into(_statsSaving) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val dst = applicationContext.contentResolver.openOutputStream(uri)
                val printStream = PrintStream(dst)
                printStream.print(statsData.toString())
                withContext(Dispatchers.IO) {
                    printStream.close()
                    dst!!.close()
                }
            } else throw Exception("Не удалось сохранить статистику")
        }

    fun launch(screen: BaseScreen) {
        navigator.launch(screen)
    }

    fun toast(message: String) {
        uiActions.toast(message)
    }
}
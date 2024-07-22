package com.serebryakov.wordle.application

import android.app.Application
import androidx.room.Room
import com.serebryakov.wordle.application.model.historydatabase.HistoryDatabase
import com.serebryakov.wordle.application.model.wordlist.InMemoryWordListModel
import com.serebryakov.wordle.application.model.sharedpref.StatSharedPrefImpl
import com.serebryakov.wordle.application.model.sharedpref.StatsSharedPref
import com.serebryakov.wordle.application.repository.historydatabaserepository.HistoryDataBaseRepositoryImpl
import com.serebryakov.wordle.application.repository.historydatabaserepository.HistoryDatabaseRepository
import com.serebryakov.wordle.application.repository.statssharedprefrepository.StatsSharedPrefRepository
import com.serebryakov.wordle.application.repository.statssharedprefrepository.StatsSharedPrefRepositoryImpl
import com.serebryakov.wordle.application.repository.wordlistrepository.WordListInMemoryRepository
import com.serebryakov.wordle.foundation.BaseApplication
import com.serebryakov.wordle.foundation.model.IoDispatcher
import kotlinx.coroutines.Dispatchers


//Singltone Scope

class App : Application(), BaseApplication {

    private val wordListRepository = WordListInMemoryRepository(InMemoryWordListModel())
    private val ioDispatcher = IoDispatcher(Dispatchers.IO)

    private lateinit var sharedPref: StatsSharedPref
    private lateinit var sharedPrefRepository: StatsSharedPrefRepository
    private lateinit var historyDatabase: HistoryDatabase
    private lateinit var historyDatabaseRepository: HistoryDatabaseRepository
    
    override fun onCreate() {
        super.onCreate()
        sharedPref = StatSharedPrefImpl(applicationContext)
        sharedPrefRepository = StatsSharedPrefRepositoryImpl(ioDispatcher, sharedPref)

        historyDatabase = Room.databaseBuilder(applicationContext, HistoryDatabase::class.java, "history_database.db").build()
        historyDatabaseRepository = HistoryDataBaseRepositoryImpl(ioDispatcher, historyDatabase.getHistoryDao())

        singletonScopeDependencies.add(historyDatabaseRepository)
        singletonScopeDependencies.add(sharedPrefRepository)
    }


    override val singletonScopeDependencies = mutableListOf<Any>(
        wordListRepository,
    )
}
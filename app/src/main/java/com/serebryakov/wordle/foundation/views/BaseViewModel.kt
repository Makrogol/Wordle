package com.serebryakov.wordle.foundation.views

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.serebryakov.wordle.foundation.model.Result
import com.serebryakov.wordle.foundation.model.ErrorResult
import com.serebryakov.wordle.foundation.model.PendingResult
import com.serebryakov.wordle.foundation.model.SuccessResult
import com.serebryakov.wordle.foundation.tools.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.lang.Exception


typealias LiveEvent<T> = LiveData<Event<T>>
typealias MutableLiveEvent<T> = MutableLiveData<Event<T>>

typealias LiveResult<T> = LiveData<Result<T>>
typealias MutableLiveResult<T> = MutableLiveData<Result<T>>
typealias MediatorLiveResult<T> = MediatorLiveData<Result<T>>


//Базовый класс для вью моделей

open class BaseViewModel : ViewModel() {

    private val coroutineContext = SupervisorJob() + Dispatchers.Main.immediate
    protected val viewModelScope: CoroutineScope = CoroutineScope(coroutineContext)

    override fun onCleared() {
        super.onCleared()
        clearTasks()
    }

    fun onBackPressed() {
        clearTasks()
    }

    fun <T> into(liveResult: MutableLiveResult<T>, block: suspend () -> T) {
        liveResult.postValue(PendingResult())
        viewModelScope.launch {
            try {
                liveResult.postValue(SuccessResult(block()))
            } catch (e: Exception) {
                liveResult.postValue(ErrorResult(e))
            }
        }
    }

    private fun clearTasks() {
        viewModelScope.cancel()
    }

    open fun onResult(result: Any) {

    }

}
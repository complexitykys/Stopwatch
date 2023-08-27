package sab.todoapp.stopwatch.presentation.stopwatch

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel


class StopwatchViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {
    private val elapsedTimeLiveData: MutableLiveData<Long> = savedStateHandle.getLiveData(KEY, 0L)
    val timeLiveData: LiveData<Long> = elapsedTimeLiveData

    private var isRunning: Boolean = false
    private var baseTime: Long = 0

    private val handler = Handler(Looper.getMainLooper())

    fun startChronometer() {
        if (!isRunning) {
            isRunning = true
            baseTime = System.currentTimeMillis() - elapsedTimeLiveData.value!!
            updateElapsedTime()
        }
    }

    fun pauseChronometer() {
        isRunning = false
        savedStateHandle[KEY] = elapsedTimeLiveData.value
    }

    fun resetChronometer() {
        pauseChronometer()
        baseTime = 0
        elapsedTimeLiveData.value = 0L
    }

    private fun updateElapsedTime() {
        if (isRunning) {
            val currentTime = System.currentTimeMillis()
            val elapsedTime = currentTime - baseTime
            elapsedTimeLiveData.postValue(elapsedTime)
            handler.postDelayed({ updateElapsedTime() }, 1000)
        }
    }
    companion object {
        const val KEY = "elapsedTime"
    }
}
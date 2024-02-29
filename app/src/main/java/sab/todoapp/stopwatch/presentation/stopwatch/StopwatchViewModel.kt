package sab.todoapp.stopwatch.presentation.stopwatch

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class StopwatchViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {
    private val _elapsedTimeFlow: MutableStateFlow<Long> =
        MutableStateFlow(savedStateHandle[KEY] ?: 0L)
    val elapsedTimeFlow: StateFlow<Long> = _elapsedTimeFlow

    private var isRunning: Boolean = false
    private var baseTime: Long = 0

    init {
        // Start watching the data stream to keep track of the change
        // and update the difference between the current time and the base time
        startObservingElapsedTime()
    }

    fun startChronometer() {
        if (!isRunning) {
            isRunning = true
            baseTime = System.currentTimeMillis() - _elapsedTimeFlow.value
            updateElapsedTime()
        }
    }

    fun pauseChronometer() {
        isRunning = false
        savedStateHandle[KEY] = _elapsedTimeFlow.value
    }

    fun resetChronometer() {
        pauseChronometer()
        baseTime = 0
        _elapsedTimeFlow.value = 0L
    }

    private fun updateElapsedTime() {
        viewModelScope.launch {
            while (isRunning) {
                val currentTime = System.currentTimeMillis()
                val elapsedTime = currentTime - baseTime
                _elapsedTimeFlow.value = elapsedTime
                delay(1000)
            }
        }
    }

    private fun startObservingElapsedTime() {
        //The operation takes place in a coroutine to track changes in MutableStateFlow
        // and saving them in savedStateHandle
        viewModelScope.launch {
            _elapsedTimeFlow.collect { elapsedTime ->
                savedStateHandle[KEY] = elapsedTime
            }
        }
    }

    companion object {
        const val KEY = "elapsedTime"
    }
}
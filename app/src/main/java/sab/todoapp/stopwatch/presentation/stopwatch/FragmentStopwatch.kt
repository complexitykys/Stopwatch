package sab.todoapp.stopwatch.presentation.stopwatch

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import sab.todoapp.stopwatch.R
import sab.todoapp.stopwatch.base.BaseFragment
import sab.todoapp.stopwatch.databinding.FragmentStopwatchBinding
import java.util.*


class FragmentStopwatch : BaseFragment() {

    private lateinit var binding: FragmentStopwatchBinding
    private var timeWhenStopped: Long = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentStopwatchBinding.inflate(inflater, container, false)

        with(binding) {
            startButton.setOnClickListener {
                stopwatchView.base = SystemClock.elapsedRealtime() + timeWhenStopped
                stopwatchView.start()
                startButton.text = getString(R.string.start)
            }
        }

        with(binding) {
            pauseButton.setOnClickListener {
                timeWhenStopped = (stopwatchView.base - SystemClock.elapsedRealtime())
                stopwatchView.stop()
                startButton.text = getString(R.string.resume)
            }
        }

        with(binding) {
            resetButton.setOnClickListener {
                stopwatchView.base = SystemClock.elapsedRealtime()
                stopwatchView.stop()
                timeWhenStopped = 0;
            }
        }

        //TODO("create an info button")

        return binding.root
    }

    companion object {
        private const val TIME_KEY = "time_key"
    }
}

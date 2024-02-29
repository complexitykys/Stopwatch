package sab.todoapp.stopwatch.presentation.stopwatch

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import sab.todoapp.stopwatch.R
import sab.todoapp.stopwatch.databinding.FragmentStopwatchBinding


class FragmentStopwatch : Fragment() {

    private var _binding: FragmentStopwatchBinding? = null
    private val binding get() = _binding!!
    private val viewModel: StopwatchViewModel by viewModels {
        SavedStateViewModelFactory(
            requireActivity().application,
            this
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStopwatchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.elapsedTimeFlow.collect {elapsedTime ->
                val formattedTime = formatElapsedTime(elapsedTime)
                binding.stopwatchView.text = formattedTime
            }
        }

        with(binding) {
            startButton.setOnClickListener {
                viewModel.startChronometer()
                startButton.text = getString(R.string.start)
            }
            pauseButton.setOnClickListener {
                viewModel.pauseChronometer()
                startButton.text = getString(R.string.resume)
            }
            resetButton.setOnClickListener {
                viewModel.resetChronometer()
                startButton.text = getString(R.string.start)
            }
            changeThemeBtn.setOnClickListener {
                toggleTheme()
            }
        }
    }

    private suspend fun formatElapsedTime(elapsedTime: Long): String = withContext(Dispatchers.IO) {
        val seconds = (elapsedTime / 1000).toInt()
        val minutes = seconds / 60
        val remainingSeconds = seconds % 60
        val hours = minutes / 60

        return@withContext String.format(FORMAT_VALUE, hours, minutes, remainingSeconds)
    }

    private fun toggleTheme() {
        val sharedPreferences = activity?.getSharedPreferences(MODE, Context.MODE_PRIVATE)
        val nightMode = sharedPreferences?.getBoolean(MODE_NAME, false)
        val editor = sharedPreferences?.edit()
        if (nightMode == true) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            editor?.putBoolean(MODE_NAME, false)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            editor?.putBoolean(MODE_NAME, true)
        }
        editor?.apply()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val FORMAT_VALUE = "%02d:%02d:%02d"
        const val MODE_NAME = "NIGHT"
        const val MODE = "CHANGE_THEME_MODE"
    }
}




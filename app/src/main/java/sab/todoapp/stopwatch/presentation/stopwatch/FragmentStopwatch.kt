package sab.todoapp.stopwatch.presentation.stopwatch

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.SavedStateViewModelFactory
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

        viewModel.timeLiveData.observe(viewLifecycleOwner) {
            val formattedTime = formatElapsedTime(it)
            binding.stopwatchView.text = formattedTime
        }

        setStatusBarColor()

        with(binding) {
            startButton.setOnClickListener {
                viewModel.startChronometer()
            }
            pauseButton.setOnClickListener {
                viewModel.pauseChronometer()
            }
            resetButton.setOnClickListener {
                viewModel.resetChronometer()
            }
            changeThemeBtn.setOnClickListener {
                toggleTheme()
            }
        }
    }

    private fun formatElapsedTime(elapsedTime: Long): String {
        val seconds = (elapsedTime / 1000).toInt()
        val minutes = seconds / 60
        val remainingSeconds = seconds % 60
        val hours = minutes / 60

        return String.format(FORMAT_VALUE, hours, minutes, remainingSeconds)
    }

    private fun toggleTheme() {
        val sharedPreferences = activity?.getSharedPreferences(MODE, Context.MODE_PRIVATE)
        val nightMode = sharedPreferences?.getBoolean(MODE_NAME, false)
        val editor = sharedPreferences?.edit()
        if(nightMode == true) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            editor?.putBoolean(MODE_NAME, false)
        }else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            editor?.putBoolean(MODE_NAME, true)
        }
        editor?.apply()
    }
    private fun setStatusBarColor() {
        val nightModeFlags = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        val statusBarColor = if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
            ContextCompat.getColor(requireContext(), R.color.gray)
        } else {
            ContextCompat.getColor(requireContext(), R.color.white)
        }

        activity?.window?.statusBarColor = statusBarColor
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




package sab.todoapp.stopwatch.presentation.stopwatch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.SavedStateViewModelFactory
import sab.todoapp.stopwatch.databinding.FragmentStopwatchBinding


class FragmentStopwatch : Fragment() {

    private lateinit var binding: FragmentStopwatchBinding
    private val viewModel: StopwatchViewModel by viewModels {SavedStateViewModelFactory(requireActivity().application, this)}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentStopwatchBinding.inflate(inflater, container, false)
        //TODO("create an info button")

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.timeLiveData.observe(viewLifecycleOwner) {
            val formattedTime = formatElapsedTime(it)
            binding.stopwatchView.text = formattedTime
        }

        with(binding){
            startButton.setOnClickListener {
                viewModel.startChronometer()
            }
            pauseButton.setOnClickListener {
                viewModel.pauseChronometer()
            }
            resetButton.setOnClickListener {
                viewModel.resetChronometer()
            }
        }
    }
    private fun formatElapsedTime(elapsedTime: Long): String {
        val seconds = (elapsedTime / 1000).toInt()
        val minutes = seconds / 60
        val remainingSeconds = seconds % 60
        val hours = minutes / 60

        return String.format("%02d:%02d:%02d", hours, minutes, remainingSeconds)
    }
}

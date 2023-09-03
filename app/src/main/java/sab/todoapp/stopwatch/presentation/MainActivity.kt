package sab.todoapp.stopwatch.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.WindowCompat
import sab.todoapp.stopwatch.R
import sab.todoapp.stopwatch.presentation.stopwatch.FragmentStopwatch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        WindowCompat.setDecorFitsSystemWindows(window,false)
        if (savedInstanceState == null) {
            val fragment = FragmentStopwatch()
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragmentContainer, fragment)
                .commit()
        }
    }
}
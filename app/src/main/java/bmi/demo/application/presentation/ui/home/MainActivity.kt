package bmi.demo.application.presentation.ui.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import bmi.demo.application.R
import bmi.demo.application.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * The main activity of the application responsible for hosting fragments and managing UI.
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_MyApplication)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}

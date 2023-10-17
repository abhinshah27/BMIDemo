package bmi.demo.application.presentation

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Application class for the BMI app.
 * This class serves as the entry point for the application and initializes Hilt for dependency injection.
 */
@HiltAndroidApp
class App : Application()
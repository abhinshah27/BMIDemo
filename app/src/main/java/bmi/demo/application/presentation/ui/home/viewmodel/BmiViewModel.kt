package bmi.demo.application.presentation.ui.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import bmi.demo.application.data.model.BmiCalData
import bmi.demo.application.data.model.BmiResultData
import bmi.demo.application.domain.usecase.BmiUseCase
import com.google.android.gms.ads.interstitial.InterstitialAd
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * ViewModel for calculating BMI
 */
@HiltViewModel
class BmiViewModel @Inject constructor(private var bmiUseCase: BmiUseCase): ViewModel() {

    // Ad object for displaying interstitial advertisements.
    var mInterstitialAd: InterstitialAd? = null

    // Array containing gender options for the user to choose from.
    val pickerGender = arrayOf("Male", "Female")

    // Variables to store user input data.
    var name = ""
    var weight = "65"
    var height = "150"
    var gender = "Male"

    // LiveData that holds the result of the BMI calculation.
    val bmiLiveData: LiveData<BmiResultData>
        get() = bmiUseCase.bmiData

    // Calculate BMI based on user input data.
    fun calculateBmi() {
        bmiUseCase.calculateBmi(BmiCalData(
            name = name,
            weight = weight.toDouble(),
            height = height.toDouble(),
            gender = gender
        ))
    }

    /**
     * Generate a list of weight & height options as an array of strings.
     * @return  array of weight & height options
     */
    fun weightHeightList(): Array<String> {
        val weightData = mutableListOf<String>()
        (1..201).map {
            weightData.add(it.toString())
        }
        return weightData.toTypedArray()
    }
}
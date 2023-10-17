package bmi.demo.application.domain.usecase

import androidx.lifecycle.LiveData
import bmi.demo.application.data.model.BmiCalData
import bmi.demo.application.data.model.BmiResultData
import bmi.demo.application.domain.repository.BmiRepository
import javax.inject.Inject

/**
 * use case class for handling BMI calculations
 */
class BmiUseCase @Inject constructor(private val bmiRepository: BmiRepository) {

    // BMI data as a LiveData for observing.
    val bmiData: LiveData<BmiResultData>
        get() = bmiRepository.bmiValue

    /**
     * Initiate BMI calculation based on the provided BmiCalData.
     *
     * @param bmiCalData  input data for BMI calculation.
     */
    fun calculateBmi(bmiCalData: BmiCalData) {
        bmiRepository.calculateBmi(bmiCalData)
    }
}
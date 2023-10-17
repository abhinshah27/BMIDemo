package bmi.demo.application.domain.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import bmi.demo.application.data.model.BmiCalData
import bmi.demo.application.data.model.BmiResultData
import javax.inject.Inject
import kotlin.math.pow

/***
 * repository class for handling BMI calculations and results.
 */
class BmiRepository @Inject constructor() {

    // LiveData property to hold BMI calculation results.
    private val _bmiValue = MutableLiveData(
        BmiResultData(
            name = "",
            resultText = "",
            bmiValue = ""
        )
    )

    // Expose the BMI calculation result as a LiveData for observing.
    val bmiValue: LiveData<BmiResultData> get() = _bmiValue

    /**
     * Function to calculate BMI based on the provided BmiCalData.
     *
     * @param bmiCalData The input data for BMI calculation.
     */
    fun calculateBmi(bmiCalData: BmiCalData) {
        // Calculate BMI using the provided data.
        val heightInMeters = bmiCalData.height / 100.0
        val bmi = bmiCalData.weight / (heightInMeters.pow(2.0))

        // Format the BMI value as a string with two decimal places.
        val bmiValue = String.format("%.2f", bmi)

        // Create a new BmiResultData and update the LiveData with the result.
        _bmiValue.postValue(
            BmiResultData(
                name = bmiCalData.name,
                resultText = resultContent(bmiCalData.name, bmi),
                bmiValue = bmiValue
            )
        )
    }

    /**
     * Function to generate a result content message based on BMI value and name.
     *
     * @param name name of the person.
     * @param bmi calculated BMI value.
     * @return result of BIM as String value.
     */
    private fun resultContent(name: String, bmi: Double): String {
        val answer: String = if (bmi < 18.5) {
            "HELLO $name, YOU ARE Underweight".uppercase()
        } else if (bmi > 18.5 && bmi < 24.9) {
            "HELLO $name, YOU ARE Normal".uppercase()
        } else if (bmi > 24.9 && bmi < 30) {
            "HELLO $name, YOU ARE Overweight".uppercase()
        } else {
            "HELLO $name, YOU ARE Obese".uppercase()
        }
        return answer
    }
}
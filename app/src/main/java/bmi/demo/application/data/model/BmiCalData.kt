package bmi.demo.application.data.model

/**
 * Define a data class to represent BMI calculation data.
 */
data class BmiCalData(
    val name: String,
    val weight: Double,
    val height: Double,
    val gender: String
)

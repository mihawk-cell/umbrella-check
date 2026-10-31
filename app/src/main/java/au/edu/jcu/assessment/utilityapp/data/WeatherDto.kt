package au.edu.jcu.assessment.utilityapp.data

import com.google.gson.annotations.SerializedName

/** Raw JSON shape returned by the Open-Meteo forecast endpoint. */
data class ForecastResponse(
    val current: CurrentDto,
    val daily: DailyDto
)

data class CurrentDto(
    @SerializedName("temperature_2m") val temperature: Double,
    @SerializedName("apparent_temperature") val feelsLike: Double,
    @SerializedName("relative_humidity_2m") val humidity: Int,
    @SerializedName("wind_speed_10m") val windSpeed: Double,
    @SerializedName("weather_code") val weatherCode: Int
)

data class DailyDto(
    @SerializedName("temperature_2m_max") val maxTemps: List<Double>,
    @SerializedName("temperature_2m_min") val minTemps: List<Double>,
    @SerializedName("precipitation_probability_max") val rainChance: List<Int?>
)

/** Clean domain model used by the UI (UI never sees DTOs). */
data class Weather(
    val temperature: Int,
    val feelsLike: Int,
    val humidity: Int,
    val windSpeed: Int,
    val high: Int,
    val low: Int,
    val rainChance: Int,
    val description: String,
    val icon: String
) {
    /** Core "at a glance" answer. */
    val needsUmbrella: Boolean get() = rainChance >= 40
}

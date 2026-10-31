package au.edu.jcu.assessment.utilityapp.data

import kotlin.math.roundToInt

data class City(val name: String, val latitude: Double, val longitude: Double)

val availableCities = listOf(
    City("Singapore", 1.3521, 103.8198),
    City("Townsville", -19.2590, 146.8169),
    City("Cairns", -16.9186, 145.7781),
    City("Sydney", -33.8688, 151.2093)
)

/** Single source of truth for weather data; hides networking and mapping from the ViewModel. */
interface WeatherRepository {
    suspend fun getWeather(city: City, fahrenheit: Boolean): Result<Weather>
}

class NetworkWeatherRepository(private val api: WeatherApi) : WeatherRepository {

    override suspend fun getWeather(city: City, fahrenheit: Boolean): Result<Weather> =
        runCatching {
            val unit = if (fahrenheit) "fahrenheit" else "celsius"
            val r = api.getForecast(city.latitude, city.longitude, unit)
            val (description, icon) = describe(r.current.weatherCode)
            Weather(
                temperature = r.current.temperature.roundToInt(),
                feelsLike = r.current.feelsLike.roundToInt(),
                humidity = r.current.humidity,
                windSpeed = r.current.windSpeed.roundToInt(),
                high = r.daily.maxTemps.first().roundToInt(),
                low = r.daily.minTemps.first().roundToInt(),
                rainChance = r.daily.rainChance.firstOrNull() ?: 0,
                description = description,
                icon = icon
            )
        }

    /** Maps WMO weather codes to a short label and emoji. */
    private fun describe(code: Int): Pair<String, String> = when (code) {
        0 -> "Clear sky" to "☀️"
        1, 2 -> "Partly cloudy" to "⛅"
        3 -> "Overcast" to "☁️"
        45, 48 -> "Foggy" to "🌫️"
        in 51..57 -> "Drizzle" to "🌦️"
        in 61..67, in 80..82 -> "Rain" to "🌧️"
        in 71..77, 85, 86 -> "Snow" to "❄️"
        in 95..99 -> "Thunderstorm" to "⛈️"
        else -> "Unknown" to "❓"
    }
}

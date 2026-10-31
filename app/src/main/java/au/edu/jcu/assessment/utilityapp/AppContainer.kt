package au.edu.jcu.assessment.utilityapp

import android.app.Application
import au.edu.jcu.assessment.utilityapp.data.NetworkWeatherRepository
import au.edu.jcu.assessment.utilityapp.data.WeatherApi
import au.edu.jcu.assessment.utilityapp.data.WeatherRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/** Manual dependency injection container: builds and holds app-wide dependencies. */
class AppContainer {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://api.open-meteo.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api: WeatherApi = retrofit.create(WeatherApi::class.java)

    val weatherRepository: WeatherRepository = NetworkWeatherRepository(api)
}

/** Register in AndroidManifest: android:name=".UtilityApplication" */
class UtilityApplication : Application() {
    val container = AppContainer()
}

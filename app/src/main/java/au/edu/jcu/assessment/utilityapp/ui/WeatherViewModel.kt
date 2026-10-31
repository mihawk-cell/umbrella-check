package au.edu.jcu.assessment.utilityapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import au.edu.jcu.assessment.utilityapp.UtilityApplication
import au.edu.jcu.assessment.utilityapp.data.City
import au.edu.jcu.assessment.utilityapp.data.Weather
import au.edu.jcu.assessment.utilityapp.data.WeatherRepository
import au.edu.jcu.assessment.utilityapp.data.availableCities
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface WeatherUiState {
    data object Loading : WeatherUiState
    data class Success(val weather: Weather) : WeatherUiState
    data class Error(val message: String) : WeatherUiState
}

/** Settings shared by both screens (not persisted, per assignment brief). */
data class Settings(
    val city: City = availableCities.first(),
    val useFahrenheit: Boolean = false,
    val showDetails: Boolean = true
)

class WeatherViewModel(private val repository: WeatherRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<WeatherUiState>(WeatherUiState.Loading)
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()

    private val _settings = MutableStateFlow(Settings())
    val settings: StateFlow<Settings> = _settings.asStateFlow()

    init { refresh() }

    fun refresh() {
        _uiState.value = WeatherUiState.Loading
        viewModelScope.launch {
            val s = _settings.value
            repository.getWeather(s.city, s.useFahrenheit).fold(
                onSuccess = { _uiState.value = WeatherUiState.Success(it) },
                onFailure = { _uiState.value = WeatherUiState.Error("Couldn't load weather. Check your connection.") }
            )
        }
    }

    // Changing city/unit changes fetched data, so we reload; showDetails is UI-only.
    fun selectCity(city: City) { _settings.update { it.copy(city = city) }; refresh() }
    fun setFahrenheit(enabled: Boolean) { _settings.update { it.copy(useFahrenheit = enabled) }; refresh() }
    fun setShowDetails(enabled: Boolean) { _settings.update { it.copy(showDetails = enabled) } }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as UtilityApplication
                WeatherViewModel(app.container.weatherRepository)
            }
        }
    }
}

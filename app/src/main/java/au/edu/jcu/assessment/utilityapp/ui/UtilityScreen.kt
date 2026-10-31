package au.edu.jcu.assessment.utilityapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import au.edu.jcu.assessment.utilityapp.data.Weather

/** Main screen: answers "Do I need an umbrella?" at a glance. */
@Composable
fun UtilityScreen(viewModel: WeatherViewModel, modifier: Modifier = Modifier) {
    val state by viewModel.uiState.collectAsState()
    val settings by viewModel.settings.collectAsState()

    Column(
        modifier = modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(settings.city.name, style = MaterialTheme.typography.titleLarge)

        when (val s = state) {
            is WeatherUiState.Loading -> CircularProgressIndicator()
            is WeatherUiState.Error -> ErrorContent(s.message, onRetry = viewModel::refresh)
            is WeatherUiState.Success -> WeatherContent(
                weather = s.weather,
                unit = if (settings.useFahrenheit) "°F" else "°C",
                showDetails = settings.showDetails
            )
        }

        Spacer(Modifier.weight(1f))
        FilledTonalButton(onClick = viewModel::refresh) {
            Icon(Icons.Default.Refresh, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Refresh")
        }
    }
}

@Composable
private fun WeatherContent(weather: Weather, unit: String, showDetails: Boolean) {
    UmbrellaCard(weather)
    Text("${weather.icon}  ${weather.temperature}$unit", fontSize = 56.sp)
    Text(weather.description, style = MaterialTheme.typography.titleMedium)
    if (showDetails) DetailsRow(weather, unit)
}

/** Headline card: the single most important piece of information. */
@Composable
private fun UmbrellaCard(weather: Weather) {
    val needs = weather.needsUmbrella
    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (needs) MaterialTheme.colorScheme.primaryContainer
            else MaterialTheme.colorScheme.tertiaryContainer
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(20.dp).fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(if (needs) "☂️ Bring an umbrella" else "😎 No umbrella needed",
                style = MaterialTheme.typography.headlineSmall, textAlign = TextAlign.Center)
            Text("${weather.rainChance}% chance of rain today", style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Composable
private fun DetailsRow(weather: Weather, unit: String) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
        Detail("Feels like", "${weather.feelsLike}$unit")
        Detail("High / Low", "${weather.high}° / ${weather.low}°")
        Detail("Humidity", "${weather.humidity}%")
        Detail("Wind", "${weather.windSpeed} km/h")
    }
}

@Composable
private fun Detail(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, style = MaterialTheme.typography.titleSmall)
        Text(label, style = MaterialTheme.typography.labelSmall)
    }
}

@Composable
private fun ErrorContent(message: String, onRetry: () -> Unit) {
    Text(message, color = MaterialTheme.colorScheme.error, textAlign = TextAlign.Center)
    Button(onClick = onRetry) { Text("Try again") }
}

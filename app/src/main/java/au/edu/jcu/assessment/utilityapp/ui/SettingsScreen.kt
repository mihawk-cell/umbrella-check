package au.edu.jcu.assessment.utilityapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import au.edu.jcu.assessment.utilityapp.data.availableCities

/** Settings screen: controls the city, temperature unit and detail visibility on the main screen. */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SettingsScreen(viewModel: WeatherViewModel, modifier: Modifier = Modifier) {
    val settings by viewModel.settings.collectAsState()

    Column(modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Location", style = MaterialTheme.typography.titleMedium)
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            availableCities.forEach { city ->
                FilterChip(
                    selected = city == settings.city,
                    onClick = { viewModel.selectCity(city) },
                    label = { Text(city.name) }
                )
            }
        }
        HorizontalDivider()
        SettingSwitch("Use Fahrenheit", settings.useFahrenheit, viewModel::setFahrenheit)
        SettingSwitch("Show extra details", settings.showDetails, viewModel::setShowDetails)
    }
}

@Composable
private fun SettingSwitch(label: String, checked: Boolean, onChange: (Boolean) -> Unit) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, style = MaterialTheme.typography.bodyLarge)
        Switch(checked = checked, onCheckedChange = onChange)
    }
}

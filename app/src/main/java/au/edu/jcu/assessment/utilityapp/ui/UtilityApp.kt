package au.edu.jcu.assessment.utilityapp.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding
import androidx.lifecycle.viewmodel.compose.viewModel

/** Scaffold with bottom navigation. Replace the starter's UtilityApp() with this. */
@Composable
fun UtilityApp() {
    // One ViewModel shared by both screens so settings affect the main screen.
    val viewModel: WeatherViewModel = viewModel(factory = WeatherViewModel.Factory)
    var showSettings by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = !showSettings, onClick = { showSettings = false },
                    icon = { Icon(Icons.Default.Home, null) }, label = { Text("Today") })
                NavigationBarItem(
                    selected = showSettings, onClick = { showSettings = true },
                    icon = { Icon(Icons.Default.Settings, null) }, label = { Text("Settings") })
            }
        }
    ) { padding ->
        if (showSettings) SettingsScreen(viewModel, Modifier.padding(padding))
        else UtilityScreen(viewModel, Modifier.padding(padding))
    }
}

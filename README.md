# ☂️ Umbrella Check

An at-a-glance utility app that answers one question: **"Do I need an umbrella today?"**

## Core features
- **Headline answer** – a colour-coded card says "Bring an umbrella" / "No umbrella needed" (rain chance ≥ 40%).
- **Current conditions** – temperature, description and emoji icon.
- **Optional details** – feels-like, high/low, humidity, wind.
- **Settings screen** – change city, switch °C/°F, hide/show details. Changes immediately update the main screen (not persisted, per brief).
- **Loading / error states** with a retry button.

## Implementation
| Topic | How it is used |
|---|---|
| Kotlin / Android Studio | Kotlin, coroutines, `StateFlow` |
| Jetpack Compose | Small reusable composables (`UmbrellaCard`, `DetailsRow`, `SettingSwitch`) |
| Material Design 3 | `Scaffold`, `NavigationBar`, `Card`, `FilterChip`, `Switch`, theme colours/typography |
| Architecture | `WeatherViewModel` (UI state) → `WeatherRepository` (data) → `WeatherApi` (network); DTOs mapped to a domain `Weather` model |
| Dependency injection | Manual DI via `AppContainer`, accessed in a ViewModel `initializer` factory |
| Web API | Retrofit + Gson calling the free [Open-Meteo](https://open-meteo.com) API (no key) |

## Project structure
```
data/   WeatherDto.kt, WeatherApi.kt, WeatherRepository.kt
ui/     UtilityApp.kt, UtilityScreen.kt, SettingsScreen.kt, WeatherViewModel.kt
AppContainer.kt
```

## Credits
Weather data: Open-Meteo (CC BY 4.0). AI assistance declared in `AI_DECLARATION.md`.

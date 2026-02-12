# Advanced

## Layers
- **Data**: Provides advanced configuration values.
- **Domain**: Encapsulates logic for expert options.
- **UI**: `AdvancedSettingsScreen` composable exposes the controls.

## Primary Screens
- `AdvancedSettingsScreen` – list of power‑user preferences.

## Integration
```kotlin
val snackbarHostState = remember { SnackbarHostState() }
AdvancedSettingsScreen(snackbarHostState = snackbarHostState)
```

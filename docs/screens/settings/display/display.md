# Display

## Layers
- **UI**: `DisplaySettingsScreen` and reusable display components.

## Primary Screens
- `DisplaySettingsScreen` – controls for theme and density related options.

## Integration
```kotlin
val snackbarHostState = remember { SnackbarHostState() }
DisplaySettingsScreen(snackbarHostState = snackbarHostState)
```

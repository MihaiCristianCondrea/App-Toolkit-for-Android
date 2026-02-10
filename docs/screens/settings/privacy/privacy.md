# Privacy

## Layers
- **UI**: `PrivacySettingsScreen` provides privacy related preferences.

## Primary Screens
- `PrivacySettingsScreen` – toggles telemetry and data sharing options.

## Integration
```kotlin
val snackbarHostState = remember { SnackbarHostState() }
PrivacySettingsScreen(snackbarHostState = snackbarHostState)
```

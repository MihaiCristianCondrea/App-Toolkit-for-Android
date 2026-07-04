# Privacy

## Layers

- **UI**: `PrivacySettingsList` provides privacy related preferences.

## Primary Screens

- `PrivacySettingsList` – toggles telemetry and data sharing options.

## Integration

```kotlin
val snackbarHostState = remember { SnackbarHostState() }
PrivacySettingsList(snackbarHostState = snackbarHostState)
```

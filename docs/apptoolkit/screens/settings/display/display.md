# Display

## Layers

- **UI**: `DisplaySettingsList` and reusable display components.

## Primary Screens

- `DisplaySettingsList` – controls for theme and density related options.

## Integration

```kotlin
val snackbarHostState = remember { SnackbarHostState() }
DisplaySettingsList(snackbarHostState = snackbarHostState)
```

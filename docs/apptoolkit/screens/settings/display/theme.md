# Theme

## Layers

- **UI**: `ThemeSettingsList` and style resources for color schemes.

## Primary Screens

- `ThemeSettingsList` – lets users switch between light, dark or system themes.

## Integration

```kotlin
val snackbarHostState = remember { SnackbarHostState() }
ThemeSettingsList(snackbarHostState = snackbarHostState)
```

# Apps & Tools screen

The Apps & Tools screen is a top-level destination in the host app. It presents the developer app
catalog in a Material 3 grid and now owns favorites filtering directly in the same surface instead
of sending users to a separate Favorites destination.

## Filtering

The screen exposes a horizontal row of Material 3 filter chips above the app grid:

- **All**: shows the complete catalog.
- **Installed**: shows catalog apps that are currently installed on the device.
- **Not installed**: shows catalog apps that are not installed on the device.
- **Favorites**: shows catalog apps whose package names are saved in the favorites data source.

Installed-package checks are owned by `AppsListViewModel` through package-manager domain use cases
and stored in `AppListUiState`, keeping PackageManager work out of composition hot paths. Favorite
mutations still flow through `ToggleFavoriteUseCase`, while the selected filter is part of
`AppListUiState` and changes through `HomeEvent.FilterSelected`.

## Navigation

Apps & Tools is the default top-level destination. Favorites are not a standalone top-level
destination; existing persisted startup values that still contain the legacy `favorite_apps` route
fall back to Apps & Tools, where users can pick the Favorites chip.

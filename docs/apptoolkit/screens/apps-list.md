# Apps & Tools screen

The Apps & Tools screen is a top-level destination in the host app. It presents the developer app
catalog in a Material 3 grid and now owns favorites filtering directly in the same surface instead
of sending users to a separate Favorites destination.

## Metadata loading

The screen uses the public Android Apps Metadata API in two stages:

- `GET /api/v1/apps` supplies compact `AppSummary` values for the grid.
- `GET /api/v1/apps/{package_name}` supplies `AppDetails` only after an item is selected.

The compact summary lets the details sheet open immediately while the full description,
screenshots, links, and optional latest-version metadata load. A failed details request keeps the
summary visible and exposes a scoped retry action. Responses are guarded by package name so an
older request cannot replace the currently selected app.

See [Android Apps Metadata API](../network/android-apps-metadata-api.md) for the endpoint, DTO,
mapping, Koin, and fallback contracts.

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

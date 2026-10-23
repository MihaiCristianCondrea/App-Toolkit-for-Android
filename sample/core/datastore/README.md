# `:sample:core:datastore` Logic Graph

## Purpose

Stores the sample's own preferences in the Toolkit's shared settings file.

## Owns

- `AppPreferencesDataSource` and `DefaultAppPreferencesDataSource`, covering the components-showcase
  unlock flag shared by the settings and components features.

## Does not own

- Toolkit preferences, owned by their shared core, feature or integration modules.
- Startup decisions and navigation mapping. The app reads `OnboardingRepository` and
  `DisplayPreferencesRepository` directly and maps the stored route with `startKeyFor`.
- Favorites, owned by `:sample:feature:apps`.
- The meaning of the showcase flag, owned by the feature repositories that consume it.

## Depends on

- [`:library:core:datastore`](../../../library/core/datastore/README.md) for the shared preferences file.

## Used by

- `:sample:feature:components` and `:sample:feature:settings`.

## Data flow

The feature repositories read and write `AppPreferencesDataSource`. Its implementation accesses
the qualified Toolkit settings DataStore directly, without navigation types or other repositories.

## Architectural decisions

- The sample keeps its own keys in the Toolkit's single file, using
  `named(DataStoreQualifiers.SETTINGS)`.
- A data source represents storage. Startup composition stays in `:sample:app` instead of passing
  through this source.

## Public contracts

- `AppPreferencesDataSource`, exposing only the showcase flag.

## Internal implementations

- The showcase preference key and DataStore access.

## Current risks

The showcase key shares the settings file with Toolkit keys; keep its name stable.

# `:library:core:datastore` Logic Graph

## Purpose

Provides the single Toolkit Preferences DataStore file and preferences shared by several modules.
Capability-specific sources live with their owning features or integrations.

## Owns

- The single `settings` Preferences DataStore instance, exposed by `Context.commonDataStore`.
- Its existing migrations and corruption handler. Corrupt preferences are replaced with empty
  preferences, retaining the existing reset-to-default behavior.
- The theme, seasonal-theme and display sources, which several modules read.
- Theme, display and seasonal-theme repository implementations and bindings.
- `startupValueFlow`, which maps the stored startup route through a caller-supplied function.
- Storage error translation through `storageCall` and `toStorageException`.
- The `DataStore<Preferences>` Koin binding, qualified by `DataStoreQualifiers.SETTINGS`, consumed by feature-owned sources.

## Does not own

- Ads sources and their eagerly shared enablement state, owned by `:library:integration:ads`.
- Review sources, owned by `:library:integration:review`.
- Onboarding and changelog sources, owned by their respective feature modules.
- Consent and usage-diagnostics sources, owned by `:library:integration:consent`.
- Issue report history, owned by `:library:feature:issuereporter`.
- An app's own preferences, such as the sample's favorites and showcase flag, owned by the app.
- Compose collection and rendering, owned by the design system and UI modules.

## Depends on

- `:library:core:common` for shared contracts, constants, models and failures.

## Used by

Core UI, the design system, feature and integration repositories, and the application composition
root use this module. A source in another module receives the same `DataStore<Preferences>`
instance from the graph using `get(qualifier = named(DataStoreQualifiers.SETTINGS))`.

## Architectural decisions

- Module ownership does not split the stored file. Every moved source retains its existing keys,
  defaults and transactions in `settings`. No installed-data migration is needed for the moves.
- Shared appearance remains in core because onboarding, About, design-system UI and theme settings
  need it without sibling feature dependencies.
- Consent and diagnostics retain one shared implementation because their keys overlap and bulk
  consent writes must remain atomic. It lives in `:library:integration:consent`, which the
  diagnostics feature already depends on; integration code cannot depend on the feature.
- A preference stays here only when more than one module reads it. One owner keeps its source,
  with its keys, in its own `data/local`.
- Repositories expose application data to UI. UI must not read sources directly.
- `SeasonalThemeRepository` owns holiday palette application and restoration across the shared
  theme and seasonal preferences. Its source atomically reads and updates appearance and snapshot
  keys with `updateHolidayTheme`, so a concurrent user choice survives restoration.
  Existing snapshots, occurrence keys and weather-effect values persist. Seasonal unlocking also
  checks and writes in one source transaction, so only one concurrent caller announces the unlock.

## Public contracts

- `commonDataStore` and `dataStoreModule`.
- Shared preference-source interfaces, and theme, seasonal-theme and display repositories.
- The graph's `DataStore<Preferences>` binding for sources owned by other modules.
- `startupValueFlow` maps stored startup identifiers through a caller-supplied function. The caller
  retains ownership of navigation keys and unknown identifiers.

## Current risks

All owners share one settings file. Key names, defaults and migrations remain compatibility-sensitive.
Never create another DataStore instance pointing to that file.

## Migration notes

`CommonDataStore` is removed. Ask the graph for `DataStore<Preferences>` with
`named(DataStoreQualifiers.SETTINGS)` and keep a source over it
for your own keys, or use the owning module's repository: `ThemePreferencesRepository`,
`DisplayPreferencesRepository`, `AdsSettingsRepository`, `OnboardingRepository`. The ads source
owns its collector, and its Koin binding closes it when the graph closes. `startupValueFlow` is an
extension of `DisplayPreferencesRepository` in `core.datastore.utils.extensions`.

`AdsSettingsRepository` and `OnboardingRepository` contracts now live in
`core.common.data.repositories`. Their implementations remain in the owning integration or feature.
The full Toolkit graph supplies their bindings. Custom graphs must supply these repositories,
including the appearance repositories consumed by `AppTheme`.

# `:library:core:datastore` Logic Graph

## Purpose

Provides the single Toolkit Preferences DataStore file and preferences shared by several modules.
Capability-specific sources live with their owning features or integrations.

## Owns

- The single `settings` Preferences DataStore instance, exposed by `Context.commonDataStore`.
- Its existing migrations and corruption handler. Corrupt preferences are replaced with empty
  preferences, retaining the existing reset-to-default behavior.
- Shared theme, seasonal-theme, display, consent, diagnostics, app-state and favorites sources.
- `CommonDataStore`, the adapter for those shared sources.
- Theme, display and seasonal-theme repository implementations and bindings.
- Storage error translation through `storageCall` and `toStorageException`.
- The `DataStore<Preferences>` Koin binding consumed by feature-owned sources.

## Does not own

- Ads sources and their eagerly shared enablement state, owned by `:library:integration:ads`.
- Review sources, owned by `:library:integration:review`.
- Onboarding and changelog sources, owned by their respective feature modules.
- Compose collection and rendering, owned by the design system and UI modules.

## Depends on

- `:library:core:common` for shared contracts, constants, models and failures.

## Used by

Core UI, the design system, feature and integration repositories, and the application composition
root use this module. A source in another module receives the same `DataStore<Preferences>`
instance from the graph.

## Architectural decisions

- Module ownership does not split the stored file. Every moved source retains its existing keys,
  defaults and transactions in `settings`. No installed-data migration is needed for the moves.
- Shared appearance remains in core because onboarding, About, design-system UI and theme settings
  need it without sibling feature dependencies.
- Consent and diagnostics retain one shared implementation because their keys overlap and bulk
  consent writes must remain atomic. Integration code cannot depend on the diagnostics feature.
- Favorites and app-state preferences remain shared APIs; they are not owned by the sample app.
- Repositories expose application data to UI. UI must not read sources directly.
- `SeasonalThemeRepository` owns holiday palette application and restoration across the shared
  theme and seasonal sources. Existing snapshots, occurrence keys and weather-effect values persist.

## Public contracts

- `commonDataStore`, `CommonDataStore` and `dataStoreModule`.
- Shared preference-source interfaces, and theme, seasonal-theme and display repositories.
- The graph's `DataStore<Preferences>` binding for sources owned by other modules.
- `startupValueFlow` maps stored startup identifiers through a caller-supplied function. The caller
  retains ownership of navigation keys and unknown identifiers.

## Current risks

All owners share one settings file. Key names, defaults and migrations remain compatibility-sensitive.
Never create another DataStore instance pointing to that file.

## Migration notes

Ads, review, onboarding and changelog methods and source properties are removed from
`CommonDataStore`. Use the owning sources from repositories, and use repository contracts from UI.
`CommonDataStore` no longer takes dispatchers or an ads default and has no `close()` method.
Its removed `CommonDataStoreCore` lifecycle contract is no longer needed. The ads source owns its
collector, and its Koin binding closes it when the graph closes.

`AdsSettingsRepository` and `OnboardingRepository` contracts now live in
`core.common.data.repositories`. Their implementations remain in the owning integration or feature.
The full Toolkit graph supplies their bindings. Custom graphs must supply these repositories,
including the appearance repositories consumed by `AppTheme`.

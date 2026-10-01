# `:sample:feature:apps` Logic Graph

## Purpose

The developer's app catalogue: listing, details, favorites, and install state.

## Owns

- `AppErrors`, the feature's error surface over the toolkit's network errors.
- `DeveloperAppsRepository`, `InstalledAppsRepository`, `FavoritesRepository` and their `Default`
  implementations.
- `DeveloperAppsRemoteDataSource`, which owns Ktor requests, DTO decoding, and remote failure
  normalization.
- `DeveloperAppsLocalDataSource`, which persists the last successfully downloaded compact
  catalogue as an atomic JSON file, plus `InstalledAppsLocalDataSource` for PackageManager access.
- `AppsListViewModel`, the list and detail-sheet composables, and the native-ad placement in the
  list.
- Localized app-catalogue strings and app-specific error-to-text mapping.
- `AppsListRoute`, this feature's tab key.
- The Get it on Google Play badge the detail sheet shows.
- The random-app button, declared by `AppsListScreen` with `ScaffoldFabs` so the tab's scaffold
  draws it; it scales out while there is no app to open.
- Filtering the grid by the tab's search field (`LocalShellSearch`): an app matches by name,
  package or description, after the filter chips.

## Does not own

- Its registration as a tab, done by `:sample:app`'s `appGraph`.
- Widget rendering, owned by [`:sample:widget`](../../widget/README.md), which reads this module's
  repository.

## Depends on

- `:sample:core:analytics`, `:sample:core:datastore` and `:sample:integration:ads`.
- [`:library:apptoolkit`](../../../library/apptoolkit/README.md) for ad slots, state contracts and
  Ktor.

## Used by

- `:sample:widget` for the catalogue, and `:sample:app` for DI and the navigation graph.

## Flow chart

```mermaid
flowchart TD
    Screen[AppsListScreen] -->|events| VM[AppsListViewModel]
    VM --> Developer[DeveloperAppsRepository]
    Developer --> Remote[DeveloperAppsRemoteDataSource]
    Remote --> Api[Apps metadata API]
    Api -->|successful compact catalog| Cache[Atomic JSON snapshot]
    Api -->|failure| Fallback{Snapshot available?}
    Cache --> Fallback
    Fallback -->|yes| Stale[Stale catalog plus network error]
    Fallback -->|no| Error[Error-only DataState]
    Developer --> VM
    VM --> Installed[InstalledAppsRepository]
    Installed --> Packages[PackageManager local source]
    VM --> Favorites[FavoritesRepository]
    Favorites --> Store[DataStoreInterface]
    VM --> Items[UI models and ad interleaving]
    Items --> Screen
    Screen --> Actions[Launch app / store / details / favorite]
    Actions --> VM
```

## Architectural decisions

- The remote catalog is authoritative; the atomic JSON snapshot exists for offline/stale fallback
  and is replaced only after a successful decode.
- Corrupt snapshots are deleted and treated as misses so malformed cached data cannot create a
  permanent failure loop.
- Installed state and favorites have separate repositories because PackageManager and preferences
  are independent sources with different lifetimes.
- Ad interleaving and action/chip models are presentation transformations and remain outside the
  source-neutral repositories.

## Installed-package metadata

AndroidInstalledAppsLocalDataSource shares core common's getVersionMetadata lookup with Issue
Reporter, then maps into the feature's existing AppVersionInfo. Blank names, package visibility,
and unavailable version behavior are unchanged; the feature still owns install-state decisions.

## Public contracts

- The three repositories, `AppsListViewModel`, `AppsListScreen`, `AppsListRoute`,
  `AppInfo`/`AppSummary`/`AppDetails`.

## Internal implementations

- Remote DTO mapping, local cache-model mapping and serialization, ad interleaving, and
  PackageManager inspection.

## Source of truth and failure behavior

The remote endpoint remains authoritative. Each successful compact-catalogue response atomically
replaces the local JSON snapshot. When a request fails, `DeveloperAppsRepository` exposes that
snapshot as stale data together with the network error, allowing non-screen consumers such as the
widget to remain useful offline. Corrupt snapshots are deleted and treated as cache misses.

## Current risks

The module applies the Kotlin serialization plugin because its DTOs and its route are
`@Serializable`. Compiling
without it succeeds and fails only at decode time, so the plugin has to stay even though nothing
about the source makes the dependency visible.

## Migration notes

`AppsListViewModel` used to take six pass-through use cases wrapping these three repositories. It
now
takes the repositories; the use cases added a duplicated breadcrumb and nothing else.

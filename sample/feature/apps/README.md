# `:sample:feature:apps` Logic Graph

## Purpose

The developer's app catalogue: listing, details, favorites, and install state.

## Owns

- `DeveloperAppsRepository`, `InstalledAppsRepository`, `FavoritesRepository` and their `Default`
  implementations.
- `DeveloperAppsRemoteDataSource`, which owns Ktor requests, DTO decoding, and remote failure
  normalization.
- `DeveloperAppsLocalDataSource`, which persists the last successfully downloaded compact
  catalogue as an atomic JSON file, plus `InstalledAppsLocalDataSource` for PackageManager access.
- `AppsListViewModel`, the list and detail-sheet composables, and the native-ad placement in the
  list.
- Localized app-catalogue strings and app-specific error-to-text mapping.
- `appsListEntryBuilder`, this feature's navigation entry.
- `FavoritesChangedReceiver`, which keeps the widget in step with favorites.

## Does not own

- The route keys it registers against, owned by [
  `:sample:core:navigation`](../../core/navigation/README.md).
- Widget rendering, owned by [`:sample:widget`](../../widget/README.md), which reads this module's
  repository.

## Depends on

- `:sample:core:navigation`, `:sample:core:common`, `:sample:core:datastore`, `:sample:core:ui`.
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
    Favorites --> Store[DatastoreInterface]
    VM --> Items[UI models and ad interleaving]
    Items --> Screen
    Screen --> Actions[Launch app / store / details / favorite]
    Actions --> VM
    Receiver[FavoritesChangedReceiver] --> Favorites
    Receiver --> Widget[Request widget update]
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

## Public contracts

- The three repositories, `AppsListViewModel`, `appsListEntryBuilder`, `AppInfo`/`AppSummary`/
  `AppDetails`.

## Internal implementations

- Remote DTO mapping, local cache-model mapping and serialization, ad interleaving, and
  PackageManager inspection.

## Source of truth and failure behavior

The remote endpoint remains authoritative. Each successful compact-catalogue response atomically
replaces the local JSON snapshot. When a request fails, `DeveloperAppsRepository` exposes that
snapshot as stale data together with the network error, allowing non-screen consumers such as the
widget to remain useful offline. Corrupt snapshots are deleted and treated as cache misses.

## Current risks

The module applies the Kotlin serialization plugin because its DTOs are `@Serializable`. Compiling
without it succeeds and fails only at decode time, so the plugin has to stay even though nothing
about the source makes the dependency visible.

## Migration notes

`AppsListViewModel` used to take six pass-through use cases wrapping these three repositories. It
now
takes the repositories; the use cases added a duplicated breadcrumb and nothing else.

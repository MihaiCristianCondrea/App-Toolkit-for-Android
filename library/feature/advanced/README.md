# `:library:feature:advanced` Logic Graph

## Purpose

Owns the advanced settings page: the bug report entry, clearing the app's cache, and the developer
options entry once it is unlocked.

## Owns

- Feature event builders and their bounded keys in `ui/analytics`; screens only report events.
- `AdvancedSettingsScreen`, which wires the ViewModel, tracking, messages, the issue reporter sheet
  and navigation, and `AdvancedSettingsScreenContent`, the stateless list of rows.
- `AdvancedSettingsViewModel`, `AdvancedSettingsUiState`, `CacheClearStatus` and
  `AdvancedSettingsEvent`.
- `CacheRepository` and `DefaultCacheRepository`, which delete the app's cache directories.
- `advancedSettingsPage()`, the registration of `AdvancedSettingsRoute` as a detail of the settings
  list, and `advancedSettingsModule`.
- Its clear cache row in the settings search, a `settingsSearchProvider` bound as `advanced`.
- The page's localized strings, including the cache confirmation and failure texts.

## Does not own

- The issue reporter sheet. It is the `IssueReporterSheet` contract of `:library:core:ui`, bound by
  `:library:feature:issuereporter`.
- The developer options page, owned by [`:library:feature:developer`](../developer/README.md). This
  page opens `DeveloperOptionsScreen` by key.
- The easter egg unlock, stored by `SeasonalThemeRepository` in `:library:core:datastore` and
  recorded by [`:library:feature:about`](../about/README.md).
- The settings list whose row opens this page, owned by
  [`:library:feature:settings`](../settings/README.md).

## Host requirements

The assembled Toolkit graph provides the standard settings bindings. Bind `IssueReporterSheet`
to enable bug reporting. With no sheet binding, Advanced Settings omits the bug report category.

## Depends on

- `:library:core:common` and `:library:core:ui` for diagnostics, error handling, and presentation.
- [`:library:core:datastore`](../../core/datastore/README.md) for `SeasonalThemeRepository`, which
  stores the developer options unlock.
- [`:library:navigation`](../../navigation/README.md) for the route keys and shell graph builder.

## Used by

- [`:library:apptoolkit`](../../apptoolkit/README.md), which calls `advancedSettingsPage()` from
  `toolkitPages()` and assembles `advancedSettingsModule`.

## Flow chart

```mermaid
flowchart TD
    Entry["advancedSettingsPage(): AdvancedSettingsRoute"] --> Screen["AdvancedSettingsScreen"]
    Screen --> Host["ScreenHost"]
    Host --> Content["AdvancedSettingsScreenContent"]
    Content -->|"Bug report action"| Sheet["IssueReporterSheet from Koin"]
    Content -->|"AdvancedSettingsEvent.ClearCache"| VM["AdvancedSettingsViewModel"]
    Content -->|"Developer options action"| Developer["DeveloperOptionsRoute"]
    VM --> Repo["CacheRepository / DefaultCacheRepository"]
    Repo --> IO["App cache directories on IO"]
    Unlock["SeasonalThemeRepository"] --> Transformer["toDeveloperOptionsUnlocked()"]
    Transformer --> VM
    VM -->|"AdvancedSettingsUiState and messages"| Host
    Host -->|"ObserveDeveloperOptions on retry"| VM
```

## Architectural decisions

- The page is on `core.ui.screen`. Its rows are there from the start, so nothing in
  `AdvancedSettingsUiState` is a `Loadable`. `cacheClear` is a `CacheClearStatus`, a
  `TrackedStatus` whose labels are the ones the page has always reported in `screen_state`:
  `success` while idle, `loading` while clearing and `error` after a failed clear.
- The clear cache row shows loading and is disabled while a clear runs. Repeated requests during the
  same clear are ignored. A successful clear shows a snackbar, and a failure shows an error snackbar. The
  failure text comes from `toErrorMessage`, so full or busy storage shows its own text and anything
  else shows "Cache cleared with some errors".
- `CacheRepository.clearCache()` is a main-safe suspend function that throws a `StorageException`:
  `FAILED` when a directory was not fully deleted, `UNAVAILABLE` when the system refused access to
  one. `DefaultCacheRepository` runs the blocking recursive delete on the injected IO dispatcher,
  so the ViewModel takes no dispatcher. It logs an incomplete delete as a breadcrumb, since only it
  can see how many directories were left.
- The bug report row shows the `IssueReporterSheet` resolved from Koin with `getOrNull`. When no
  module binds one, the error reporting category is left out. The sheet's visibility is saved, so a
  rotation does not close it.
- The developer options row is hidden until the About screen's version easter egg is found. The
  ViewModel takes that as a `Flow<Boolean>` (`developerOptionsUnlocked`). The transformer in
  `ui/transformers/` reads `SeasonalThemeRepository`'s unlock, the same flag that keeps the seasonal themes all year,
  so one discovery unlocks both. A failed read keeps the last known unlock state. The error
  snackbar offers Try again when the failure is retryable.
- There is no domain layer: the ViewModel calls one repository and holds no reusable business rule.

## Public contracts

- `AdvancedSettingsScreen`, `advancedSettingsPage()`, `AdvancedSettingsViewModel`,
  `AdvancedSettingsUiState`, `CacheClearStatus`, `AdvancedSettingsEvent`, `CacheRepository`,
  `DefaultCacheRepository` and `advancedSettingsModule`. `AdvancedSettingsScreenContent` is
  internal.

## Internal implementations

- The unlock transformer projects `SeasonalThemeRepository` state into developer options visibility.
- The cache repository runs recursive file deletion on the injected IO dispatcher.

## Current risks

Cache deletion is restricted to the app's cache locations (`cacheDir`, `codeCacheDir` and
`externalCacheDir`). Changes must keep that scope and keep the file work off the main thread.
`AdvancedSettingsViewModelTest` and `DefaultCacheRepositoryTest` cover the state and data behavior.

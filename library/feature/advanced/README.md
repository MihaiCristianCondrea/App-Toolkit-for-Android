# :library:feature:advanced

## Responsibility and consumers

Owns the advanced settings screen, cache-clearing state and actions,
CacheRepository/DefaultCacheRepository, and advancedSettingsModule. `advancedSettingsPage()`
registers the screen for `AdvancedSettingsRoute` as a detail of the settings list; the main toolkit
module calls it and assembles its DI module. The feature asks nothing of its host: the removed
AdvancedSettingsProvider existed only to supply a bug-report URL, which the list stopped using once
the issue reporter began submitting reports itself.

## Dependencies and flow

Depends on core common, network and UI (which exposes navigation), and on no other feature module. The bug-report
row shows the `IssueReporterSheet` contract of core UI, resolved from Koin with `getOrNull`: the
issue reporter binds it, and when no module does, the bug-report category is left out. The sheet is
held by rememberSaveable state over this screen. AdvancedSettingsScreen sends events to AdvancedSettingsViewModel, which calls
CacheRepository.
DefaultCacheRepository performs cache operations on the injected dispatcher and reports results
through the existing screen state. The feature owns its localized resources.

## Contracts and boundaries

Public entry points are AdvancedSettingsScreen, `advancedSettingsPage()`,
AdvancedSettingsViewModel, CacheRepository, and advancedSettingsModule. Cache operations belong to data/repositories;
presentation and provider callbacks belong to ui. There is no domain layer because these actions
do not require a separate business-operation abstraction. Navigation owns the key.

## Validation and risks

AdvancedSettingsViewModelTest and DefaultCacheRepositoryTest cover the state and data behavior.
Cache deletion is restricted to application cache locations; changes must preserve that scope
and keep filesystem work off the main thread.

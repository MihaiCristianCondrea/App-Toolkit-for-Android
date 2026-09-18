# :library:feature:advanced

## Responsibility and consumers

Owns the advanced settings list, cache-clearing state and actions,
CacheRepository/DefaultCacheRepository, and advancedSettingsModule. Settings composes this content;
the main toolkit module assembles its DI module. The feature asks nothing of its host: the removed
AdvancedSettingsProvider existed only to supply a bug-report URL, which the list stopped using once
the issue reporter began submitting reports itself.

## Dependencies and flow

Depends on core common, network, and UI, plus Issue Reporter for the report action. The bug-report
row opens the reporter's bottom sheet over this screen through IssueReporterLauncher, rather than
starting an activity. AdvancedSettingsList sends events to AdvancedSettingsViewModel, which calls
CacheRepository.
DefaultCacheRepository performs cache operations on the injected dispatcher and reports results
through the existing screen state. The feature owns its localized resources.

## Contracts and boundaries

Public entry points are AdvancedSettingsList, AdvancedSettingsViewModel, CacheRepository, and
advancedSettingsModule. Cache operations belong to data/repositories;
presentation and provider callbacks belong to ui. There is no domain layer because these actions
do not require a separate business-operation abstraction. Settings owns the category route.

## Validation and risks

AdvancedSettingsViewModelTest and DefaultCacheRepositoryTest cover the state and data behavior.
Cache deletion is restricted to application cache locations; changes must preserve that scope
and keep filesystem work off the main thread.

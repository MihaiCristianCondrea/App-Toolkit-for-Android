# :library:feature:advanced

## Responsibility and consumers

Owns the advanced settings list, cache-clearing state and actions, AdvancedSettingsProvider,
CacheRepository/DefaultCacheRepository, and advancedSettingsModule. Settings composes this content;
the facade assembles its DI module. Hosts supply the advanced settings provider.

## Dependencies and flow

Depends on core common, network, and UI, plus Issue Reporter for the report action.
AdvancedSettingsList sends events to AdvancedSettingsViewModel, which calls CacheRepository.
DefaultCacheRepository performs cache operations on the injected dispatcher and reports results
through the existing screen state. The feature owns its localized resources.

## Contracts and boundaries

Public entry points are AdvancedSettingsList, AdvancedSettingsViewModel, AdvancedSettingsProvider,
CacheRepository, and advancedSettingsModule. Cache operations belong to data/repositories;
presentation and provider callbacks belong to ui. There is no domain layer because these actions
do not require a separate business-operation abstraction. Settings owns the category route.

## Validation and risks

AdvancedSettingsViewModelTest and DefaultCacheRepositoryTest cover the state and data behavior.
Cache deletion is restricted to application cache locations; changes must preserve that scope
and keep filesystem work off the main thread.

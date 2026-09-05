# :library:feature:display

## Responsibility and consumers

Owns DisplaySettingsList, DisplaySettingsViewModel, DisplaySettingsProvider, language/startup
selection dialogs, displaySettingsModule, and localized display settings resources.
Settings composes this content; the facade assembles DI and hosts supply DisplaySettingsProvider.

## Dependencies and flow

Depends on core common, DataStore, UI, and navigation. The ViewModel reads and updates the shared
DisplayPreferencesRepository and ThemePreferencesRepository. Host startup selection returns a
confirmed route; the state holder persists it. Core DataStore remains the source of truth.

## Contracts and boundaries

The list, ViewModel, provider, dialogs, UI state/events, and displaySettingsModule are exposed.
This feature needs ui and di only: shared repositories already own its data, and no independent
domain operation is necessary. Settings owns the category-content route; navigation owns route
contracts. Do not introduce duplicate preference storage.

## Validation and risks

DisplaySettingsViewModelTest covers preference updates. Hosts must preserve the startup selection
callback contract and account for saved route identifiers that are no longer available.

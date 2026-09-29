# :library:feature:display

## Responsibility and consumers

Owns DisplaySettingsScreen, DisplaySettingsViewModel, DisplaySettingsProvider, language/startup
selection dialogs, displaySettingsModule, and localized display settings resources.
`displaySettingsPage()` registers the screen for `DisplaySettingsRoute` as a detail of the settings
list; the main toolkit module calls it and assembles DI, and hosts supply DisplaySettingsProvider.

## Dependencies and flow

Depends on core common, DataStore, UI, and navigation, and on no other feature module. The dark
theme row opens the theme page by navigating to `ThemeSettingsRoute`, so the provider no longer
supplies an `openThemeSettings` callback. The ViewModel reads and updates the shared
DisplayPreferencesRepository and ThemePreferencesRepository. Host startup selection returns a
confirmed route; the state holder persists it. Core DataStore remains the source of truth.

## Contracts and boundaries

The screen, `displaySettingsPage()`, ViewModel, provider, dialogs, UI state/events, and
displaySettingsModule are exposed.
This feature needs ui and di only: shared repositories already own its data, and no independent
domain operation is necessary. Navigation owns the keys. Do not introduce duplicate preference storage.

## Validation and risks

DisplaySettingsViewModelTest covers preference updates. Hosts must preserve the startup selection
callback contract and account for saved route identifiers that are no longer available.

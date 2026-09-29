# :library:feature:display

## Responsibility and consumers

Owns DisplaySettingsScreen, DisplaySettingsViewModel, DisplaySettingsProvider, language/startup
selection dialogs, displaySettingsModule, and localized display settings resources.
`displaySettingsPage()` registers the screen for `DisplaySettingsRoute` as a detail of the settings
list; the main toolkit module calls it and assembles DI, and hosts supply DisplaySettingsProvider.

## Dependencies and flow

Depends on core common, DataStore, UI, navigation and the shell, and on no other feature module.

The screen offers the shell's layout choices as the person's own settings: under Appearance the app
bar style, the navigation colour beside a rail or drawer, the content width limit and the banner
style; under Navigation the start page, the bottom bar style, its labels, hiding it on scroll, the
tab transition and the right-edge back swipe. They are read from `LocalShellSettings` and written
through `LocalShellPreferences` (the rows are `ShellDisplayRows`), so they apply to the whole app at
once. Each is offered only when the app's graph (`LocalShellGraph`) gives it something to change:
nothing about a bottom bar without tabs, no banner style without a banner, no content width limit
when the app's `ShellLayoutPolicy` sets no maximum width, no tab transition or start page with a
single tab. Like the other settings rows, they draw no leading icon. The screen therefore works only as a page of `ShellHost`.

The dark
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

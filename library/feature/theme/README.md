# :library:feature:theme

## Responsibility and consumers

Owns ThemeSettingsList, ThemeSettingsViewModel, themeSettingsModule, theme-selection presentation,
and localized resources. Settings composes the list and the facade assembles DI.

## Dependencies and flow

Depends on core common, DataStore, UI, and design system. The ViewModel consumes the shared
ThemePreferencesRepository directly. Core DataStore persists values; the design system renders
the application theme. The same preferences also serve onboarding appearance selection.

## Contracts and boundaries

Public entry points include ThemeSettingsList, ThemeSettingsViewModel, its state/events, and
themeSettingsModule. The DI module registers the built-in qualified palettes and resolves the
host's default palette override, falling back to blue. Palette definitions remain in the design system.
Only ui and di layers are needed; there is no duplicate data layer or pass-through domain layer.

## Validation and risks

ThemeSettingsViewModelTest covers preference changes. Keep palette qualifiers, stored identifiers,
and default selection compatible with host overrides and existing preferences.

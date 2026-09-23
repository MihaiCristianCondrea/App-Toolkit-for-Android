# :library:feature:theme

## Responsibility and consumers

Owns ThemeSettingsList, ThemeSettingsViewModel, themeSettingsModule, theme-selection presentation,
the seasonal themes (holiday greeting, holiday snowfall, and the easter egg controls), and localized
resources. Settings composes the list and its top app bar action, and the main toolkit module
assembles DI.

## Dependencies and flow

Depends on core common, DataStore, UI, and design system. The ViewModels consume the shared
`ThemePreferencesRepository` and `SeasonalThemeRepository` directly. Core DataStore persists values
and owns the holiday rules; the design system renders the application theme and the snowfall. The
same preferences also serve onboarding appearance selection.

## Contracts and boundaries

Public entry points include ThemeSettingsList, ThemeSettingsViewModel, its state/events, and
themeSettingsModule. The DI module registers the built-in qualified palettes and resolves the
host's default palette override, falling back to blue. Palette definitions remain in the design system.
The purple and orange palettes are available through the `purple` and `orange` static IDs and the
`purplePalette` and `orangePalette` qualifiers. Android and Halloween have qualifiers too.
Only ui and di layers are needed; there is no duplicate data layer or pass-through domain layer.

## Seasonal themes

`SeasonalThemeManager` puts `SeasonalThemeOverlay` over every activity of the host, the same way
shake-to-report follows activities. A host opts in by calling `install()` from
`Application.onCreate`:

```kotlin
getKoin().get<SeasonalThemeManager>().install()
```

The overlay is a full-size `ComposeView` added on top of each resumed activity's content. It is
never added to an empty content view: `ComponentActivity.setContent` adopts the first child when it
is a `ComposeView`, so an activity that sets its content after resuming would otherwise run inside
the overlay without its navigation owners. Such activities get the overlay after their content's
first layout. It only draws, so touches reach the screen underneath, and it is hidden from accessibility services and
focus. Activities from Google Play services, Play Billing and Firebase are skipped. A host that
prefers to place the overlay itself can compose `SeasonalThemeOverlay()` last in its root `Box`
instead of installing the manager.

What people see:

- On the first screen they open during Christmas (December 24 to January 7) or Halloween
  (October 31 to November 2), a greeting offers the holiday theme with a checkbox that starts
  ticked. It appears once per holiday occurrence.
- Accepting applies the holiday palette. When the holiday ends, the palette and wallpaper-colors
  setting they had before come back, unless they picked another palette during the holiday.
- Snow falls over the app while the Christmas palette is on screen, during the Christmas season.
  Snow is skipped when animations are turned off system-wide.
- Tapping the build version five times on the About screen unlocks the seasonal themes controls.
  `SeasonalThemesAction` then shows a top app bar action on the theme page that opens a dialog to
  keep the seasonal palettes all year, switch between Christmas and Halloween, and turn snowfall
  on or off. With the easter egg, snow also follows the Christmas palette outside the season.

## Validation and risks

ThemeSettingsViewModelTest covers preference changes. SeasonalThemeOverlayViewModelTest covers when
snow falls, the greeting flow, and that two activities never stack two greetings.
SeasonalThemesViewModelTest covers the easter egg controls. Keep palette qualifiers, stored
identifiers, and default selection compatible with host overrides and existing preferences.

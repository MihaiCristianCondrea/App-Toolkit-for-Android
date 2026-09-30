# :library:feature:theme

## Responsibility and consumers

Owns ThemeSettingsScreen, ThemeSettingsViewModel, themeSettingsModule, theme-selection presentation,
the seasonal themes (holiday greeting, holiday snowfall, the weather effect, and what the easter
egg unlocks), and
localized resources. `themeSettingsPage()` registers the screen for `ThemeSettingsRoute`, and the
main toolkit module calls it and assembles DI. The display settings' dark theme row opens it by key.
The page is `PaneRole.None`: display is itself a detail beside the settings list, and a detail
opened from a detail would replace it instead of stacking on it. It also registers its theme mode, AMOLED, wallpaper colours and palette rows with the settings search, as a `SettingsSearchProvider`.

## Dependencies and flow

Depends on core common, DataStore, UI, and design system. The ViewModels consume the shared
`ThemePreferencesRepository` and `SeasonalThemeRepository` directly. `ThemeSettingsViewModel`
emits nothing until both the stored preferences and the easter egg unlock have loaded, so the
palette rows open positioned on the stored selection rather than on a placeholder. Core DataStore persists values
and owns the holiday rules; the design system renders the application theme, the snow and the rain. The
same preferences also serve onboarding appearance selection.

## Contracts and boundaries

Public entry points include ThemeSettingsScreen, `themeSettingsPage()`, ThemeSettingsViewModel,
its state/events, and themeSettingsModule. The DI module registers the built-in qualified palettes and resolves the
host's default palette override, falling back to blue. Palette definitions remain in the design system.
The purple and orange palettes are available through the `purple` and `orange` static IDs and the
`purplePalette` and `orangePalette` qualifiers. Android and Halloween have qualifiers too.
No other feature module is a dependency. Only ui and di layers are needed; there is no duplicate data layer or pass-through domain layer.

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
first layout. It only draws, so touches reach the screen underneath, and it is hidden from
accessibility services and focus. Activities from Google Play services, Play Billing and Firebase
are skipped. The overlay composes no app theme of its own until a greeting is due, so outside
Christmas it costs one small composition per activity. A host that prefers to place the overlay itself can compose `SeasonalThemeOverlay()`
last in its root `Box` instead of installing the manager.

What people see:

- On the first screen they open during Christmas (December 24 to January 7) or Halloween
  (October 31 to November 2), a greeting offers the holiday theme with a checkbox that starts
  ticked. It appears once per holiday occurrence.
- Accepting applies the holiday palette. When the holiday ends, the palette and wallpaper-colors
  setting they had before come back, unless they picked another palette during the holiday.
- Snow falls over the app while the Christmas palette is on screen, during the Christmas season.
  Snow is skipped when animations are turned off system-wide.
- Tapping the build version five times on the About screen unlocks the seasonal themes. From then
  on the Christmas and Halloween palettes stay in the palette list all year, snow follows the
  Christmas palette outside the season too, and the theme page's app bar gets a weather effect
  action. Its icon shows the effect in use, and it opens a dialog of radio rows, applied with Done,
  like the display settings' startup page dialog:
  - Automatic, the default: snow with the Christmas palette, as described above.
  - Snow: snow falls over the app, whatever the palette and the season.
  - Rain: rain falls over the app, whatever the palette. It gusts, comes and goes in showers,
    and splashes where it lands.
  - Off: nothing falls, and the Christmas palette stays.

  The choice is stored with `SeasonalThemeRepository.setWeatherEffect` as a `WeatherEffect`. The
  action is `WeatherEffectAction`, which shares the page's `ThemeSettingsViewModel`.

## Validation and risks

ThemeSettingsViewModelTest covers preference changes, the easter egg unlock, and the weather effect.
SeasonalThemeOverlayViewModelTest covers when snow or rain falls, the greeting flow, and that two
activities never stack two greetings. Keep palette qualifiers, stored identifiers, and default
selection compatible with host overrides and existing preferences.

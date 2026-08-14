# `:library:core:designsystem` Logic Graph

## Purpose

Defines the AppToolkit Compose theme, typography, color palettes, dynamic wallpaper colors, and
theme-selection visuals.

## Owns

- `AppTheme`, theme configuration, typography, and palette selection.
- Static, seasonal, monochrome, rose, and Material You color definitions.
- `ColorPalette`, `ThemeSettingOption`, and wallpaper swatch models.
- Theme option/swatch composables.

## Does not own

- General reusable widgets and screen state, owned by [`:library:core:ui`](../ui/README.md).
- Persisted preference infrastructure, owned by [`:library:core:datastore`](../datastore/README.md).
- Settings navigation and ViewModels, owned by `:library:feature:settings`.

## Depends on

- [`:library:core:common`](../common/README.md) for the application-facing theme preference model
  and shared helpers.
- [`:library:core:datastore`](../datastore/README.md) to observe persisted theme settings.

## Used by

- `:sample` for application theming.
- `:library:apptoolkit` as part of the public toolkit façade.
- `:library:core:ui` for themed reusable components.

## Flow chart

```mermaid
flowchart TD
    Store[Persisted theme preferences] --> AppTheme[AppTheme]
    Palette[ThemePaletteProvider] --> AppTheme
    Dynamic[Wallpaper colors] --> AppTheme
    AppTheme --> Material[Material 3 color scheme and typography]
```

## Public contracts

- `AppTheme`, `AppThemeConfig`, `ColorPalette`, palette providers/values, theme models, and
  selection composables.

## Internal implementations

- `AppTheme` observes global bounce-animation, bottom-bar-label, and ad-slot preferences once at the
  root and provides them to reusable UI without per-component DataStore collectors.
- Compose collection of `themePreferencesState()` at the design-system boundary.
- `LocalBouncyAnimationsEnabled`, the design-system-owned UI contract used by interactive core UI
  components without introducing a dependency cycle back from the design system to core UI.
- `bounceClick`, the shared press-feedback modifier consumed by core and navigation UI.

- Concrete palette color tables, seasonal filtering, typography definitions, and dynamic-color
  resolution.

## Current risks

The module still depends directly on the preference contracts needed by `AppTheme`. The persisted
model and non-Compose flow combination remain outside this module so presentation-specific
collection does not leak back into `:library:core:datastore`.

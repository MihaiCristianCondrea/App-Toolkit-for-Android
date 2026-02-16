# Onboarding theme

This document describes the **Theme onboarding page** and how it persists and previews theme
choices (light/dark/system, AMOLED mode, dynamic vs static palettes, and dynamic palette variants).

The implementation lives primarily in:

- `ThemeOnboardingPageTab()`
- `AmoledModeToggleCard`
- `ThemeChoicePreviewCard` (core UI)
- `WallpaperColorOptionCard` (theme UI)
- `ThemePalettePager` (core UI)
- DataStore helpers: `rememberCommonDataStore()` + `rememberThemePreferencesState()`

---

## Goals

The theme onboarding page should:

- Let users choose **theme mode**: Light / Dark / Follow system
- Optionally enable **AMOLED** (only when allowed for the current theme mode)
- Let users pick colors:
  - **Dynamic** (Android 12+ / API 31+), with **variant** selection
  - **Static palettes**, including **seasonal** palettes (Halloween / Christmas)
- Persist choices immediately in DataStore
- Preview choices clearly in the UI

---

## Data sources

### Preferences state

`ThemeOnboardingPageTab()` reads current preferences via:

- `val dataStore = rememberCommonDataStore()`
- `val themePreferences = rememberThemePreferencesState()`

The page uses these fields:

- `themePreferences.themeMode: String`
- `themePreferences.amoledMode: Boolean`
- `themePreferences.dynamicColors: Boolean`
- `themePreferences.dynamicPaletteVariant: Int`
- `themePreferences.staticPaletteId: String`

If `themeMode` is blank, it falls back to:

- `DataStoreNamesConstants.THEME_MODE_FOLLOW_SYSTEM`

### Device capability: dynamic colors

Dynamic colors are supported only when:

- `Build.VERSION.SDK_INT >= Build.VERSION_CODES.S`

The page sets:

```kotlin
val supportsDynamic = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
````

When `supportsDynamic == false`, only static palettes are shown.

---

## Theme mode selection

The page offers three `OnboardingThemeChoice` entries:

* `THEME_MODE_LIGHT`
* `THEME_MODE_DARK`
* `THEME_MODE_FOLLOW_SYSTEM`

Each option renders with `ThemeChoicePreviewCard` and an inline mini preview:

* Light → `LightModePreview`
* Dark → `DarkModePreview`
* System → `SystemModePreview`

### Persistence

On click, theme mode is saved immediately:

```kotlin
dataStore.saveThemeMode(mode = choice.key)
```

### AMOLED safety rule

If the user switches to **Light mode**, AMOLED is force-disabled:

```kotlin
if (choice.key == THEME_MODE_LIGHT && isAmoledMode) {
    dataStore.saveAmoledMode(isChecked = false)
}
```

This keeps preferences consistent (AMOLED typically makes sense only for dark/system-dark).

---

## AMOLED mode

### Availability rule

AMOLED availability is derived from the current theme mode:

```kotlin
val amoledAllowed = isAmoledAllowed(currentThemeMode)
```

* If not allowed, the toggle card is disabled and visually de-emphasized (`alpha(0.55f)`).
* When disabled, clicks are ignored.

### UI component

`AmoledModeToggleCard` is a clickable `Card` with a `CustomSwitch`:

* haptics + click sound
* switch icon changes (Contrast / Tonality)
* elevation changes slightly when enabled

### Persistence

When allowed, changes are persisted immediately:

```kotlin
dataStore.saveAmoledMode(isChecked = isChecked)
```

---

## Palette selection

Palette selection depends on whether dynamic colors are supported.

### When dynamic colors are supported (Android 12+)

The page shows:

1. A **segmented control**:

* "Wallpaper colors" (dynamic)
* "Other colors" (static)

2. A `ThemePalettePager` with two pages.

#### Initial page logic

If dynamic colors are supported and currently enabled, start on dynamic page:

```kotlin
val initialPagerPage = if (supportsDynamic && isDynamicColors) 0 else 1
```

A `LaunchedEffect` ensures pager sync if preference changes externally.

---

## Dynamic palette variants

Dynamic palette variants are computed from the system's dynamic scheme and displayed as swatches.

### How swatches are generated

1. Create a base dynamic `ColorScheme` depending on current system theme:

```kotlin
val wallpaperPreviewScheme: ColorScheme? =
    if (!supportsDynamic) null
    else if (isSystemInDarkThemeNow) dynamicDarkColorScheme(context)
    else dynamicLightColorScheme(context)
```

2. For each `DynamicPaletteVariant` index:

* apply variant: `base.applyDynamicVariant(variant)`
* map to a simple swatch model `WallpaperSwatchColors(primary, secondary, tertiaryContainer)`

### Persisting a variant selection

Clicking a variant swatch:

```kotlin
dataStore.saveDynamicColors(true)
dataStore.saveDynamicPaletteVariant(index)
```

Selection state is:

* `selected = isDynamicColors && index == dynamicVariantIndex`

---

## Static palettes (including seasonal)

Static palette options come from `StaticPaletteIds.withDefault`, then filtered and deduped.

### Seasonal detection

The page detects season once per composition:

* `isChristmasSeason`
* `isHalloweenSeason`

Using date helpers:

* `LocalDate.now(...).isChristmasSeason`
* `LocalDate.now(...).isHalloweenSeason`

### Filtering and deduping

```kotlin
val seasonalOptions = filterSeasonalStaticPalettes(
    baseOptions = StaticPaletteIds.withDefault,
    isChristmasSeason = isChristmasSeason,
    isHalloweenSeason = isHalloweenSeason,
    selectedPaletteId = staticPaletteId
)

val staticOptions = dedupeStaticPaletteIds(
    options = seasonalOptions,
    selectedPaletteId = staticPaletteId
)
```

This ensures:

* seasonal palettes appear only in their season (but keep selected palette stable)
* no duplicate palette IDs
* "default" stays present

### Swatch generation

Each static palette ID maps to a palette object (`paletteById(id)`), and then picks light/dark scheme
based on the current system theme:

```kotlin
val scheme = if (isSystemInDarkThemeNow) p.darkColorScheme else p.lightColorScheme
WallpaperSwatchColors(scheme.primary, scheme.secondary, scheme.tertiary)
```

### Persisting static selection

Clicking a static swatch:

```kotlin
dataStore.saveDynamicColors(false)
dataStore.saveStaticPaletteId(id)
```

Selection state is:

* `selected = !isDynamicColors && id == staticPaletteId`

### Seasonal badge

The UI may show a badge when the option is seasonal and it's currently that season:

```kotlin
showSeasonalBadge =
    (isChristmasSeason && id == StaticPaletteIds.CHRISTMAS) ||
    (isHalloweenSeason && id == StaticPaletteIds.HALLOWEEN)
```

---

## When dynamic colors are NOT supported (pre-Android 12)

Only static palettes are shown in a single `LazyRow`:

* no segmented control
* no dynamic variants
* persists `dynamicColors=false` + `staticPaletteId`

This keeps the experience consistent across older devices.

---

## UX / performance notes

### Immediate persistence

Theme selections are persisted as soon as the user taps them, which is ideal for onboarding:

* no extra "Apply" step
* the rest of onboarding can reflect theme changes instantly

### Avoid recomputation

The implementation uses `remember(...)` to avoid unnecessary work:

* scheme generation is remembered against stable inputs
* swatch lists are remembered against scheme/options inputs
* season flags are remembered once

### System theme dependency

Some previews and swatches depend on `isSystemInDarkTheme()`:

* dynamic scheme base changes with system theme
* static scheme picks light/dark palette accordingly
  This is intentional for consistent previews.

---

## Extension points

You can extend the theme onboarding page by adding:

* additional static palettes (via `StaticPaletteIds`)
* additional dynamic variants (via `DynamicPaletteVariant.indices`)
* a “reset to default” action (persist defaults)
* contextual hints (e.g., AMOLED only recommended for OLED screens)

Keep the persistence rules consistent and avoid introducing blocking operations in the composable.

---

## Related docs

* `pages.md` – onboarding page model and selection lifecycle
* `privacy-consent.md` – consent UI and rules (Crashlytics / ads)
* `../onboarding.md` – host app integration and completion observation

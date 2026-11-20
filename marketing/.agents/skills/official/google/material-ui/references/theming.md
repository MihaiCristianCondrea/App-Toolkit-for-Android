# Theming and dynamic color

How to set up a Material 3 theme on each platform: color scheme, dynamic color, dark theme,
contrast levels, brand colors, and scoped themes. Role meanings are in `color.md`. Numeric scales
are in `tokens.md`.

Official pages: `documentation/styles/color/choosing-a-scheme.md`,
`documentation/styles/color/static/`, `documentation/styles/color/dynamic/`,
`documentation/styles/color/advanced/`, `documentation/foundations/customization.md`.

## One set of roles, three theme entry points

| Platform | Where the theme lives |
|---|---|
| Jetpack Compose | `MaterialTheme(colorScheme, typography, shapes, motionScheme)` near the app root. |
| Android Views | A `Theme.Material3.*` (or `Theme.Material3Expressive.*`) theme with color, type, and shape attributes. |
| Web | `--md-sys-*` CSS custom properties on `:root` or on a subtree. |

Provide the theme once and read roles below it. Never scatter literal colors, type sizes, or radii
through screens.

## Choosing a scheme

Decide first whether the product uses dynamic color.

- Dynamic color fits products where personalization matters and brand color is not the main
  identifier. It needs Android 12 (API 31) or later for wallpaper-based color.
- A static scheme fits products where brand recognition matters, and it is the fallback on devices
  without dynamic color.
- Most Android apps ship both: dynamic when available and enabled, static otherwise.

Generate static schemes with Material Theme Builder or Material Color Utilities from one source
color. Review the generated output against the product's brand and status colors before committing
it. A generated file is a starting point, not a finished theme.

## Compose

```kotlin
@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> AppDarkColorScheme
        else -> AppLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        shapes = AppShapes,
        content = content,
    )
}
```

This is a pattern. If the project already has a theme function, extend it instead of adding a second
one.

- Define complete light and dark fallback schemes (`lightColorScheme(...)`, `darkColorScheme(...)`
  or generated code). Do not leave roles at library defaults by accident.
- Keep status colors (success, warning) accessible in dynamic and fallback schemes. Material has no
  success or warning role; add them as a small explicit extension, for example a
  `CompositionLocal` holding extra role pairs, defined the same way Material defines roles.
- Read colors through `MaterialTheme.colorScheme`, never through `Color.White` or `Color.Black` as a
  foreground on a themed container.
- For M3 Expressive, Compose Material 3 1.5.0 graduated the Expressive theming APIs. Confirm the exact
  theme and scheme function names against the installed version before using them (see
  `sources.md`).

## Android Views

- Inherit from a Material 3 theme: `Theme.Material3.DayNight.NoActionBar` or the Expressive family
  `Theme.Material3Expressive.*` (MDC-Android 1.14).
- Read colors through theme attributes such as `?attr/colorPrimary` and
  `?attr/colorSurfaceContainer`, not hard-coded resources.
- Apply dynamic color with `DynamicColors.applyToActivitiesIfAvailable(application)` in the
  `Application`. It is a no-op below Android 12.
- Harmonize custom colors with `MaterialColors.harmonizeWithPrimary(context, color)` or the
  `HarmonizedColors` API.

## Web

### Generate a scheme

Use `@material/material-color-utilities` to produce role values from a source color, then write them
as CSS custom properties.

```javascript
import {
  argbFromHex,
  hexFromArgb,
  Hct,
  SchemeTonalSpot,
} from '@material/material-color-utilities';

const ROLES = [
  'primary', 'onPrimary', 'primaryContainer', 'onPrimaryContainer',
  'secondary', 'onSecondary', 'secondaryContainer', 'onSecondaryContainer',
  'tertiary', 'onTertiary', 'tertiaryContainer', 'onTertiaryContainer',
  'error', 'onError', 'errorContainer', 'onErrorContainer',
  'surface', 'onSurface', 'onSurfaceVariant', 'surfaceDim', 'surfaceBright',
  'surfaceContainerLowest', 'surfaceContainerLow', 'surfaceContainer',
  'surfaceContainerHigh', 'surfaceContainerHighest',
  'outline', 'outlineVariant',
  'inverseSurface', 'inverseOnSurface', 'inversePrimary',
];

const toKebab = (name) => name.replace(/[A-Z]/g, (c) => `-${c.toLowerCase()}`);

// contrastLevel: 0 = standard, 0.5 = medium, 1 = high
export function schemeTokens(sourceHex, isDark = false, contrastLevel = 0) {
  const scheme = new SchemeTonalSpot(Hct.fromInt(argbFromHex(sourceHex)), isDark, contrastLevel);
  return Object.fromEntries(
    ROLES.map((role) => [`--md-sys-color-${toKebab(role)}`, hexFromArgb(scheme[role])]),
  );
}

export function applyScheme(target, tokens) {
  for (const [property, value] of Object.entries(tokens)) {
    target.style.setProperty(property, value);
  }
}
```

`SchemeTonalSpot` is the default Android-style scheme. `SchemeContent` keeps colors closer to the
source, which suits content-based color. Check the installed package version for available scheme
classes and property names.

For static products, generate once, review, and commit the CSS instead of computing it at runtime.

### Dark theme and user choice

```css
:root { /* light role values */ }

@media (prefers-color-scheme: dark) {
  :root:not([data-theme='light']) { /* dark role values */ }
}

:root[data-theme='dark'] { /* dark role values */ }
```

Follow the system preference by default and keep an explicit user choice when the product offers one.

### Content-based color

To theme from an image, quantize its pixels and score the result to pick a source color:

```javascript
import { QuantizerCelebi, Score, argbFromRgb, hexFromArgb } from '@material/material-color-utilities';

export function sourceColorFromImageData(imageData) {
  const pixels = [];
  for (let i = 0; i < imageData.data.length; i += 4) {
    if (imageData.data[i + 3] < 255) continue; // skip transparent pixels
    pixels.push(argbFromRgb(imageData.data[i], imageData.data[i + 1], imageData.data[i + 2]));
  }
  const [best] = Score.score(QuantizerCelebi.quantize(pixels, 128));
  return hexFromArgb(best);
}
```

Browsers have no wallpaper color API, so web dynamic color is always content-based or user-picked.

### Component tokens

Material Web components expose component-level custom properties in the form
`--md-<component>-<part>-<property>`, for example `--md-filled-button-container-color`. Use them for
a narrow, deliberate exception and point them at system roles:

```css
md-fab.compose {
  --md-fab-container-color: var(--md-sys-color-tertiary-container);
  --md-fab-icon-color: var(--md-sys-color-on-tertiary-container);
}
```

Check the component's documentation page on material-web.dev for the tokens it actually supports.

## Brand colors

| Brand asset | Where it goes |
|---|---|
| Main brand color | Source color for the scheme. |
| Second brand color | Custom color, harmonized, or an override of `secondary` after checking pairs. |
| Accent color | `tertiary`, or a harmonized custom color. |
| Danger color | Keep Material `error` unless the brand requires otherwise. |
| Background color | Let the scheme generate surfaces. Do not hard-code a background. |

When a brand color must appear exactly (a logo tile, for example), Material Theme Builder's color
fidelity option or a fixed custom color keeps it recognizable. Check contrast after any adjustment.

### Custom roles

Define extra roles the way Material does: a source palette, a reference tone for light and dark, and
the contrast requirement against its pair. Roles defined this way keep working with contrast levels.
See `documentation/styles/color/advanced/define-new-colors.md`.

## Contrast levels

Material schemes support standard, medium, and high contrast. On Android 14 and later the user picks
the level in system settings and dynamic schemes follow it. For static schemes, generate a scheme per
level (Material Theme Builder exports them, Material Color Utilities takes a contrast parameter) and
select one from the platform's contrast setting where available. Values are in `tokens.md`.

## Scoped themes

A subtree can carry its own theme. In Compose, wrap it in another `MaterialTheme` with a different
`colorScheme`. On the web, override role properties on a container class:

```css
.promo-section {
  --md-sys-color-primary: /* generated value */;
  --md-sys-color-on-primary: /* generated value */;
  --md-sys-color-primary-container: /* generated value */;
  --md-sys-color-on-primary-container: /* generated value */;
}
```

Override complete pairs from one generated scheme, never a single role, or the pairing contract
breaks.

## Baseline scheme

The baseline static scheme uses source color `#6750A4`. The AndroidX `lightColorScheme()` and
`darkColorScheme()` defaults and Material Web's defaults ship its values, for example light
`primary #6750A4`, `surface #FEF7FF`, `on surface #1D1B20`, `outline #79747E`, and dark
`primary #D0BCFF`, `surface #141218`, `on surface #E6E0E9`, `outline #938F99`. Generate the full set
rather than copying partial values, and note that later Material Color Utilities versions shift some
tones.

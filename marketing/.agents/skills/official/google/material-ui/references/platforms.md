# Platform status

Check this before telling a user that a Material feature is available on their platform.
Availability is uneven, and this is where confident answers go wrong. Status recorded on 2026-11-20;
versions and links are in `sources.md`.

## Library status

| Platform | Status | Material updates |
|---|---|---|
| Jetpack Compose (Compose Material 3) | Recommended for Android. Built by the Material team and receives updates first. | First |
| Android Views (MDC-Android) | Maintenance mode. 1.14.0 is the final feature release; critical fixes only. | No new features |
| Web (`@material/web`) | Maintenance mode pending new maintainers. Usable, no feature updates. | No new features |
| Wear OS (Wear Compose Material 3) | Separate Wear implementation of Material 3 Expressive. | Own release line |

The Figma Design Kit and the design guidelines track the Compose library.

## M3 Expressive support

### Jetpack Compose

- 21 Material components use the motion physics system by default.
- `MotionScheme`: `MotionScheme.expressive()`, `MotionScheme.standard()`, custom schemes, and
  per-element override through a `CompositionLocal`.
- `MaterialShapes`: the 35-shape library and shape morphing.
- Emphasized typography through the emphasized type tokens.
- Compose Material 3 1.5.0 (stable, 2026-11-18) graduated most Expressive components and the
  Expressive theming APIs to stable. Still experimental in 1.5.0: loading indicators
  (`LoadingIndicator`, `ContainedLoadingIndicator`) and `MaterialShapes` with its `toShape` and
  `toPath` extensions.
- Announced and ongoing: a Styles API for customization, and Material Adaptive with Navigation 3
  integration.

### Android Views (MDC-Android 1.14.0)

M3 Expressive landed in Views before the freeze: Expressive themes, the Expressive list, the
emphasized type scale, Expressive styles for existing components, and spring tokens that are not
wired into components. The full list and its upgrade cost are in `android.md`.

Treat new Views work as migration debt and move to Compose screen by screen when the project plans
it.

### Web

- Motion: compatible with Compose springs. Use real springs where possible; otherwise the spring to
  cubic-bezier table in `tokens.md`. Curves cannot handle interruptions or gestures, so reserve them
  for animations that have neither.
- Emphasized typography: not available as a library feature. Build it from the values in `tokens.md`.
- Shape library and shape morph: not available.
- New Expressive components (button groups, split button, FAB menu, toolbars, loading indicator): not
  available. See `web.md` for what `@material/web` does provide.

### Wear OS

Wear Compose Material 3 implements Expressive for watches: physics-based motion, edge-hugging
containers, arc text, and Wear-specific components. Design guidance is in
`documentation/foundations/watches/`. Implementation guidance is in the `wear-compose-m3` skill.

### Android XR

Material has XR design and component guidance (`documentation/foundations/xr/`): spatial panels,
depth-based elevation, and XR variants of app bars, dialogs, navigation bar and rail, and toolbars.
Check the Compose for XR libraries in the project before promising a component.

## Feature summary

| Feature | Compose | Views (MDC) | Web | Wear |
|---|---|---|---|---|
| Motion physics (springs) | Yes, in components | Tokens only | Compatible; convert or use a spring library | Yes |
| Shape library (35 shapes) | Yes, `MaterialShapes` (experimental) | Shape theming | No | Not stated |
| Shape morph | Yes | Not stated | No | Not stated |
| Emphasized type scale | Yes | Yes | Build from tokens | Not stated |
| Dynamic color | Yes (Android 12+) | Yes (Android 12+) | Content-based only | Yes (Wear OS 6+); check the `wear-compose-m3` skill |
| Expressive components | Yes | Styles for existing components | No | Wear set |

"Not stated" means the guidelines say nothing about it. Do not upgrade it to "yes".

## Tools

- Figma Design Kit (includes the shape library): https://www.figma.com/community/file/1035203688168086460
- Material Theme Builder: https://material-foundation.github.io/material-theme-builder/
- Material Color Utilities: https://github.com/material-foundation/material-color-utilities
- Compose `MotionScheme`: https://developer.android.com/reference/kotlin/androidx/compose/material3/MotionScheme
- Compose `MaterialShapes`: https://developer.android.com/reference/kotlin/androidx/compose/material3/MaterialShapes
- MDC-Android theming docs: https://github.com/material-components/material-components-android/tree/master/docs/theming

Custom components get Expressive motion through the spring tokens; see `motion.md`.

# `:library:core:designsystem` Logic Graph

## Purpose

Defines the AppToolkit Compose theme, typography, color palettes, dynamic wallpaper colors,
theme-selection visuals, and the shared icon slot used by navigation items and buttons.

## Owns

- `AppTheme`, theme configuration, typography, and palette selection.
- Static, seasonal, monochrome, rose, and Material You color definitions.
- `ColorPalette`, `ThemeSettingOption`, and wallpaper swatch models.
- Theme option/swatch composables.
- `ToolkitIcon` and its renderers, the icon slot shared by navigation items and buttons.

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
- `:library:apptoolkit` as part of the public toolkit API.
- `:library:core:ui` for themed reusable components.

## Flow chart

```mermaid
flowchart TD
    Store[ThemePreferencesRepository] --> State[ThemePreferencesState Flow]
    State --> Root[AppTheme]
    Host[Host default ColorPalette] --> Palette[Palette selection]
    Seasonal[SeasonalPaletteFilter] --> Palette
    Wallpaper[Android wallpaper colors] --> Dynamic{Dynamic colors enabled and supported?}
    Dynamic -->|yes| Scheme[Dynamic Material color scheme]
    Dynamic -->|no| Palette
    Palette --> Scheme
    Root --> Scheme
    Root --> Typography[Toolkit typography]
    Root --> Locals[Global bounce / labels / ad-slot CompositionLocals]
    Scheme --> Content[Themed Compose content]
    Typography --> Content
    Locals --> Content
```

## Architectural decisions

- `AppTheme` is the single Compose collection boundary for global appearance preferences, avoiding
  independent DataStore collectors in every reusable component.
- Dynamic wallpaper colors are presentation inputs, not persisted palette data; unsupported or
  disabled devices fall back to the selected static/seasonal palette.
- Bounce behavior is owned here through a CompositionLocal and modifier so `core:ui` can consume it
  without creating a design-system-to-UI dependency cycle.
- The icon slot is owned here for the same reason: `:library:navigation` and `:library:core:ui` both
  render icons, and this is the lowest module both already depend on.
- Bundled Lottie icon loading and finite playback are presentation implementation details here.
  The module uses the existing Lottie Compose dependency internally; public icon models expose
  resource IDs and replay options, not Lottie SDK types. Compositions survive click replays and
  progress is consumed while drawing to avoid recomposing host surfaces every frame.

## Public contracts

- `AppTheme`, `AppThemeConfig`, `ColorPalette`, palette providers/values, theme models, and
  selection composables.
- `ToolkitIcon`, `ToolkitIconReplayMode`, `resolveToolkitIcon`, `ToolkitIconContent`, and
  `AnimatedToolkitIcon`. See [the Toolkit Icon API](#toolkit-icon-api) below.

## Toolkit Icon API

`ToolkitIcon` is the icon slot used by every toolkit component that draws an icon: navigation drawer
items, bottom bar and rail items, and the `General*Button` family. It lives in
`:library:core:designsystem`, package
`com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons`.

### The four sources

| Source                              | Declares                                                   | Animates                                |
|-------------------------------------|------------------------------------------------------------|-----------------------------------------|
| `ToolkitIcon.Vector(imageVector)`   | A Compose `ImageVector`, for example `Icons.Rounded.Share` | No                                      |
| `ToolkitIcon.Resource(resId)`       | A drawable or vector XML resource, drawn through a painter | No                                      |
| `ToolkitIcon.AnimatedVector(resId)` | An `animated-vector` resource                              | Yes, on click and on selection          |
| `ToolkitIcon.Lottie(resId)`         | Lottie JSON in `res/raw`                                   | Yes, once per click or selection change |

`ToolkitIcon.of(...)` and `ToolkitIcon.animated(...)` are shorthand factories for the same
static/AVD types.

`AnimatedVector` requires a real `animated-vector` drawable, the kind
`androidx.compose.animation.graphics` can read: one `vector` plus the `target` animators that move
it from the first frame to the last one. A plain `vector` resource passed as `AnimatedVector` fails to
inflate at runtime. Declare it as `Resource` instead.

Remote Lottie URLs and asset paths are not accepted by this icon API. Anything else, such as a Coil
`Painter` or a bitmap, is not accepted. Draw those with a plain
`Image` next to the component instead of through the icon slot.

### Components with a selected state

Navigation items own two slots, `icon` for the unselected state and `selectedIcon` for the selected
one. `selectedIcon` defaults to `icon`, so a single icon covers both. Any source is allowed in
either slot, which gives four combinations:

| `icon`   | `selectedIcon`             | Result                                                                                                                          |
|----------|----------------------------|---------------------------------------------------------------------------------------------------------------------------------|
| static   | static                     | Plain swap when selection changes. Nothing animates.                                                                            |
| static   | animated                   | The static icon is drawn at rest. The first click swaps in the animated one and plays it, and every later click plays it again. |
| animated | animated (same or another) | The drawable rests on its first frame while unselected and on its last frame while selected, and replays on every click.        |
| animated | static                     | The animated icon plays while the item is unselected; the static one takes over once it is selected.                            |

The static plus animated combination is the one to use for an action such as Share, which is clicked
repeatedly and never becomes a selected destination. Draw the first frame of the animated vector
like the static icon, otherwise the swap on the first click is visible. When the first frame already
matches, passing the animated vector alone as `icon` is simpler and behaves the same.

### Components without a selected state

Buttons take a single `icon`. An animated icon plays every time the button is clicked; a static one
just draws. Nothing else about the button changes.

```kotlin
GeneralButton(
    onClick = ::share,
    label = stringResource(id = R.string.share),
    icon = ToolkitIcon.AnimatedVector(R.drawable.anim_share),
)
```

### Replaying an animation

A click that happens while the drawable already rests on its last frame is a replay, and
`ToolkitIconReplayMode` decides what it looks like:

- `Restart`, the default, plays the animation forward from its first frame again. It suits the usual
  icon whose last frame is drawn like its first one, so the reset is invisible and every click looks
  the same.
- `Reverse` plays the animation backwards, from the last frame to the first one. Use it for a
  drawable that morphs between two distinct shapes and should visibly travel back, such as a
  play/pause or a menu/close toggle.

```kotlin
ToolkitIcon.AnimatedVector(
    resId = R.drawable.anim_menu,
    replayMode = ToolkitIconReplayMode.Reverse,
)
```

`atEnd` sets the frame the drawable rests on before anything happens, and is `false`, the first
frame, by default.

### Rendering an icon outside a toolkit component

Two composables are public:

- `ToolkitIconContent(icon, contentDescription, modifier, atEnd, tint)` draws exactly one icon and
  owns no state. `atEnd` picks the frame of an animated vector, and changing it animates between the
  two frames.
- `AnimatedToolkitIcon(icon, clickCount, contentDescription, modifier, selectedIcon, selected, tint)`
  is the stateful one the toolkit components use. The owner keeps a click counter and increments it
  on every click; each increment replays the animation. Owners that never animate pass `0`.

`resolveToolkitIcon(icon, selectedIcon, selected, interacted)` returns the icon a component should
be drawing, and is useful when a host renders its own navigation surface from toolkit item models.

### Accessibility

The content description belongs to the component, not to the icon. Navigation items reuse their
title, and buttons take `contentDescription`, which is required for an icon-only button because
nothing else describes it.

### Lottie icons and performance

```kotlin
ToolkitIcon.Lottie(R.raw.add_icon, tintable = true)
ToolkitIcon.Lottie(R.raw.toggle_icon, replayMode = ToolkitIconReplayMode.Reverse)
```

Use bundled JSON from `res/raw`. Lottie compositions load asynchronously and remain remembered
across clicks; playback does not reparse the JSON. The progress provider is read while drawing,
so animation frames do not recompose the parent button or navigation surface. Animations run
once per interaction, cancel when removed from composition, and use Compose duration scaling.
Rapid clicks restart from zero by default. Reverse is opt-in. Icons do not loop while idle.

Authored colors are preserved by default. Set `tintable = true` for monochrome artwork that should
follow the component's content color, including its disabled state. `Color.Unspecified` preserves
artwork colors even when tintable. Size defaults to 24 dp and caller constraints take precedence.
Loading or invalid JSON renders an empty icon slot without starting an animation; provide valid,
small vector-only artwork and a meaningful component label. Avoid embedded images, expensive
masks, and large compositions in lists. No network requests are made by this icon API.

The FAB wrappers also accept `ToolkitIcon`: `AnimatedFloatingActionButton`,
`SmallFloatingActionButton`, and `AnimatedExtendedFloatingActionButton`. Existing ImageVector
and custom-content overloads remain available. The sample FAB showcase uses a bundled Lottie
plus icon to demonstrate replay. No repository, domain model, or persisted preference owns
playback; it remains local presentation state in `core:designsystem`.

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

## GeneralButton (3.0)

Import `GeneralButton`, `GeneralButtonStyle`, and `ButtonIconPosition` from
`com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons`. The component lives in
`core:ui`, which owns feedback and analytics; this module owns its theme and ToolkitIcon rendering.

One button API handles a nonblank label, an icon, or both. A null, empty, or whitespace-only label
selects the icon-only form. Missing content fails fast. Icon-only actions require a nonblank,
localized `contentDescription`. Labelled icons are decorative; the label names the action by default.
An explicit description replaces the visible label's accessibility text without announcing both.

| Style | Label / icon + label | Icon only |
|---|---|---|
| `Filled` (default) | Material Button | FilledIconButton |
| `Tonal` | FilledTonalButton | FilledTonalIconButton |
| `Outlined` | OutlinedButton | OutlinedIconButton |
| `Elevated` | ElevatedButton | Compact 40 dp ElevatedButton with zero content padding |
| `Text` | TextButton | IconButton |

The elevated icon form retains Material elevation, disabled behavior, shape, and minimum interactive
target; it does not fall back to a filled style. Caller size constraints take precedence.
`iconPosition = ButtonIconPosition.End` places the icon after the label in logical reading order;
`Start` is the default and both follow RTL. `iconSize` applies to either form.

`containerColor` and `contentColor` are optional enabled-state overrides shared by both forms;
null retains style defaults. Disabled colors remain Material defaults. `iconTint` overrides icon
color through the shared renderer; null follows the button content color. Lottie retains authored
colors unless `tintable = true`; `Color.Unspecified` preserves artwork even for tintable Lottie.
Vectors, resources, AVD and bundled Lottie all use the same `ToolkitIcon` slot and existing
`Restart` / `Reverse` replay contract described above.

Every enabled click replays the icon, performs `ButtonFeedback`, logs the optional `ga4Event`
through `firebaseController`, then invokes `onClick`. Disabled buttons do none of these.
Do not add `bounceClick()`, manual sounds, haptics, or duplicate analytics at call sites.

```kotlin
GeneralButton(onClick = ::save, label = stringResource(R.string.save))
GeneralButton(
    onClick = ::showMenu,
    style = GeneralButtonStyle.Text,
    icon = ToolkitIcon.Vector(Icons.Rounded.MoreVert),
    contentDescription = stringResource(R.string.more_options),
)
GeneralButton(
    onClick = ::continueFlow,
    style = GeneralButtonStyle.Elevated,
    label = stringResource(R.string.continue_label),
    icon = ToolkitIcon.Vector(Icons.AutoMirrored.Rounded.ArrowForward),
    iconPosition = ButtonIconPosition.End,
)
```

### Breaking migration

`GeneralTextButton`, `GeneralTonalButton`, and `GeneralOutlinedButton` have been removed, with no
deprecated aliases. Replace them with `GeneralButton` and the corresponding `Text`, `Tonal`, or
`Outlined` style. Rename `iconContentDescription` to `contentDescription`. Replace `colors =
ButtonDefaults.buttonColors(...)` with `containerColor` / `contentColor` overrides. `onClick` is now
the first parameter; prefer named arguments when migrating positional calls.

`AnimatedIconButtonDirection` delegates its action to GeneralButton and only owns visibility
transitions. FABs remain specialized floating action components.

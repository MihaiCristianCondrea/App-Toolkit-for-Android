# Color

M3 has 26 standard color roles in six groups (primary, secondary, tertiary, error, surface, outline).
With inverse, scrim, shadow, and the add-on roles there are 45 roles in total. Scheme generation,
HCT, and contrast-level values are in `tokens.md`. The long extract of the official color pages is
`color-schemes.md`. Theme setup per platform is in `theming.md`.

Official pages: `documentation/styles/color/roles.md`, `documentation/styles/color/system/`,
`documentation/styles/color/choosing-a-scheme.md`.

## Why roles, not colors

Material describes color roles as "the numbers in a paint-by-number canvas". A role connects a UI
element to the color that goes there. Three consequences follow:

- Roles are mapped to Material components. Map custom components to the same roles.
- Roles carry accessibility. The system is built on accessible pairings with at least 3:1 contrast.
- Roles are tokenized, so contrast levels and dynamic color work without extra code.

Apply colors only in their intended pairs and layering order. Improper combinations break contrast,
and they break visibly when the user changes the contrast level or dynamic color shifts. A button
that uses `primary` on `primary container` becomes illegible at some contrast levels.
`primary` with `on primary` stays legible at every level.

## Naming grammar

| Term | Meaning |
|---|---|
| Surface | Backgrounds and large, low-emphasis areas. |
| Primary, secondary, tertiary | Accent roles that emphasize or de-emphasize foreground elements. |
| Container | Fill color for foreground elements such as buttons. Never use it for text or icons. |
| On | Text and icons placed on the paired parent color. |
| Variant | A lower-emphasis alternative to its non-variant pair. |

Platform names for the same role:

| Spec | Compose | Views theme attribute | Web custom property |
|---|---|---|---|
| `primary container` | `MaterialTheme.colorScheme.primaryContainer` | `?attr/colorPrimaryContainer` | `--md-sys-color-primary-container` |
| `on surface variant` | `colorScheme.onSurfaceVariant` | `?attr/colorOnSurfaceVariant` | `--md-sys-color-on-surface-variant` |
| `surface container high` | `colorScheme.surfaceContainerHigh` | `?attr/colorSurfaceContainerHigh` | `--md-sys-color-surface-container-high` |

## Choosing an accent group

Assign accents by importance and the emphasis the element needs.

- Primary: the most prominent components, such as the FAB, high-emphasis buttons, and active states.
- Secondary: elements that don't need immediate attention, such as filter chips, the selected state
  of a navigation icon, dismissive buttons, and the background of an inactive star.
- Tertiary: smaller elements that need special emphasis but not immediate attention, such as badges,
  notifications, and input fields. Tertiary balances primary and secondary and is applied at the
  designer's discretion.

Material advises caution when changing roles purely for visual effect.

### The four roles in each accent group

| Role | Use |
|---|---|
| `primary` | High-emphasis fills, text, and icons against surface. |
| `on primary` | Text and icons against primary. |
| `primary container` | Standout fill against surface for key components such as a FAB. |
| `on primary container` | Text and icons against primary container. |

Secondary follows the same pattern for less prominent fills; its container is used by recessive
components such as tonal buttons. Tertiary follows it for complementary fills; its container is used
by components such as input fields.

## Pairing table

Each fill has a defined foreground. Use only these pairs.

| Fill | Text and icons |
|---|---|
| `primary` | `on primary` |
| `primary container` | `on primary container` |
| `secondary` | `on secondary` |
| `secondary container` | `on secondary container` |
| `tertiary` | `on tertiary` |
| `tertiary container` | `on tertiary container` |
| `error` | `on error` |
| `error container` | `on error container` |
| `surface`, any `surface container` | `on surface` or `on surface variant` |
| `inverse surface` | `inverse on surface`, or `inverse primary` for actions |
| `primary fixed`, `primary fixed dim` | `on primary fixed`, `on primary fixed variant` |

## Error

`error`, `on error`, `error container`, and `on error container` indicate urgency, such as an
incorrect password in a text field.

Error is static by default in a dynamic color scheme. It does not shift with the source color, but it
still adapts to light and dark theme.

## Surface

| Role | Use |
|---|---|
| `surface` | Default background color. |
| `on surface` | Text and icons on any surface or surface container. |
| `on surface variant` | Lower-emphasis text and icons on any surface or surface container. |

There are five container levels by emphasis: `surface container lowest`, `low`, `surface container`
(the default), `high`, and `highest`. The non-default levels are most useful for hierarchy and
nested containers on expanded screens.

The most common combination is `surface` for the body area and `surface container` for the
navigation area. Text and icons use `on surface` or `on surface variant` on every surface type.

### Keep region colors constant across window sizes

All color mappings, especially surface, should stay the same for a layout region across window size
classes. The body uses `surface` and the navigation area uses `surface container` on both phone and
tablet. At larger sizes you may add other surface container colors for hierarchy if you apply them
consistently.

### Default component mappings

- `surface container low`: elevated button, card.
- `surface container`: top and bottom bars.
- `surface container high`: FAB, basic dialog.
- `surface container highest`: input label, off switch.

Neutral components such as navigation bars, menus, and dialogs default to specific surface container
roles and can be remapped. Exact per-component assignments are in `component-tokens.md`.

### Surface color is not elevation

Surface roles are tone-based (see the February 2023 update below). Do not derive a surface color from
an elevation level. Elevation is in `foundations.md`.

## Inverse

`inverse surface` is a background that contrasts with surface. `inverse on surface` is text and icons
on it, and `inverse primary` is for actionable elements such as a text button on inverse surface.

The canonical use is a snackbar: inverse surface background, inverse on surface text, and an inverse
primary text button.

## Outline

- `outline` marks important boundaries, such as a text field outline.
- `outline variant` is for decorative elements such as dividers, and for cases where other elements
  already provide 4.5:1 contrast.

Rules from the color roles page:

- Don't use `outline` for dividers. The contrast requirements differ. Use `outline variant`.
- Don't use `outline` for components that contain multiple elements, such as cards. Use
  `outline variant`.
- Don't use `outline variant` to create visual hierarchy or to define the boundary of a target. Use
  `outline`, or another color with 3:1 contrast against the surface.
- `outline variant` is acceptable on the border of targets such as chips and buttons when the
  content inside meets 4.5:1 contrast.

## Add-on roles

Material states that most products won't need these: "If you aren't sure whether your product should
use the add-on roles, it probably shouldn't."

Fixed accent colors (`primary fixed`, `secondary fixed`, `tertiary fixed`) are fills against surface
that keep the same tone in light and dark theme, unlike container roles. `*-fixed-dim` is a deeper
tone with the same fixed behavior. Text on them uses `on * fixed`, or `on * fixed variant` for lower
emphasis.

Fixed colors don't respond to theme, so they are likely to cause contrast issues. Avoid them where
contrast matters and use `primary`, `secondary`, or `tertiary` instead.

`surface dim` is the dimmest surface in both themes and `surface bright` the brightest. Unlike
`surface`, which inverts between themes, these keep their relative brightness. Example: a navigation
rail on `surface dim` and a chat window on `surface bright`.

## Tonal palettes and role tones

A source color produces five key colors (primary, secondary, tertiary, neutral, neutral variant). Each
key color becomes a tonal palette with tones 0 to 100 in steps of 10, plus 95, 98, and 99. Roles are
assigned tones in each palette. Common assignments in the standard-contrast scheme:

| Role | Palette | Light tone | Dark tone |
|---|---|---|---|
| `primary` | primary | 40 | 80 |
| `on primary` | primary | 100 | 20 |
| `primary container` | primary | 90 | 30 |
| `on primary container` | primary | 10 | 90 |
| `surface` | neutral | 98 | 6 |
| `on surface` | neutral | 10 | 90 |
| `surface container lowest` | neutral | 100 | 4 |
| `surface container low` | neutral | 96 | 10 |
| `surface container` | neutral | 94 | 12 |
| `surface container high` | neutral | 92 | 17 |
| `surface container highest` | neutral | 90 | 22 |
| `outline` | neutral variant | 50 | 60 |
| `outline variant` | neutral variant | 80 | 30 |

`primary40` mapping to `primary` and `primary100` mapping to `on primary` are stated on the official
"how the system works" page. The remaining rows reflect Material Color Utilities' standard scheme.
Generated schemes, the August 2024 color update, and contrast levels adjust some tones, so treat this
table as orientation and read real values from the generated scheme.

## Static and dynamic schemes

- Static: hand-picked or generated, for example with Material Theme Builder, and constant once
  assigned. The baseline scheme is the default static scheme and uses a hand-picked source color.
- Dynamic: one source color from the wallpaper or in-app content generates an accessible scheme that
  updates when the source changes. It gives personalized UI, accessible contrast, user-controlled
  contrast, and automatic dark theme.

Source options: the user's wallpaper (quantization), in-app content such as album art, a logo, or a
video preview, or a hand-picked color.

Algorithms: user-generated (maps tones from system design choices and user preferences),
content-based (adjusts tones to match the source image while keeping accessible contrast), and custom
colors (closely match input colors, for brand or semantic use).

When migrating from M2, map the baseline scheme onto the product first, then switch to dynamic color.
Specific colors, semantic colors in particular, can be set not to change dynamically.

### Harmonizing extra brand colors

A brand or semantic color that does not come from the source can be harmonized: its hue shifts
slightly toward the scheme's primary so it fits the palette without losing its identity. Material
Color Utilities provides `Blend.harmonize(designColor, sourceColor)`. Android Views exposes the same
operation through `MaterialColors.harmonizeWithPrimary`. Harmonize decorative or brand accents; keep
status colors recognizable and check their contrast after harmonizing.

## The expressive color tactic

Expressive color mixes primary, secondary, and tertiary on key elements and builds hierarchy with
surface tones. The tactic is described in `expressive.md` (tactic 2).

## Contrast levels (May 2025)

Standard, medium, and high contrast are tokenized and apply to light and dark theme automatically.
The guarantees are in `tokens.md`. Custom components support all three only when they use proper role
pairs, such as `primary container` with `on primary container`.

## August 2024 update: more colorful text and icons

In light theme these roles became more colorful while keeping accessible contrast: `on primary
container`, `on secondary container`, `on tertiary container`, and `on error container`.

Affected components: badges, bottom app bar, buttons, extended FAB, FAB, icon buttons, segmented
buttons, chips, lists, menus, navigation bar, navigation drawer, navigation rail, and switches.

## February 2023 update: tone-based surfaces

Tone-based surface roles replaced surfaces at +1 to +5 elevation, so surface color is no longer tied
to elevation. Alignment changes with Android SysUI moved the light theme default surface from tone 99
to 98, raised neutral palette chroma from 4 to 6, and slightly darkened dark theme surface roles.

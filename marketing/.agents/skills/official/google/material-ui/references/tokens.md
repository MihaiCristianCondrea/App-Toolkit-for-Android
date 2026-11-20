# M3 Expressive token values

Exact values. Copy them; do not recompute or round.

Sources: m3.material.io (shape scale, type roles, contrast levels) and the generated token files in
`androidx.compose.material3.tokens` (`TypeScaleTokens.kt` v0_103, `ShapeTokens.kt` 14_1_0,
`ExpressiveMotionTokens.kt` and `StandardMotionTokens.kt` v0_14_0).

## Corner radius scale: 10 steps

M3 uses a size-based scale of ten styles, assigned by desired roundedness. It replaced M2's
three-level scale, which was based on container size.

| Style | Value | Token (Compose) |
|---|---|---|
| None | 0dp | `CornerNone` (RectangleShape) |
| Extra small | 4dp | `CornerExtraSmall` |
| Small | 8dp | `CornerSmall` |
| Medium | 12dp | `CornerMedium` |
| Large | 16dp | `CornerLarge` |
| **Large increased** | **20dp** | `CornerLargeIncreased` |
| Extra large | 28dp | `CornerExtraLarge` |
| **Extra large increased** | **32dp** | `CornerExtraLargeIncreased` |
| **Extra extra large** | **48dp** | `CornerExtraExtraLarge` |
| Full | fully rounded | `CornerFull` (CircleShape) |

Bold rows were added by the M3 Expressive update (May 2025). The same update redefined fully rounded
corners to use `full`; previously it was 50% of component size.

Partial-corner variants exist for sheets, menus, and drawers: `CornerExtraLargeTop` (28dp, top only),
`CornerExtraSmallTop`, `CornerLargeTop`, `CornerLargeStart`, `CornerLargeEnd`. Individual corners use
corner-value tokens (`CornerValueMedium` and so on), which are what inner-corner component tokens map
to.

### Optical roundness when nesting

Never use the same radius for a container and its nested child; the corners look unbalanced.

```
inner radius = outer radius - padding      e.g. 48dp - 14dp = 34dp
```

## Type scale: 30 styles (15 baseline and 15 emphasized)

Five roles (display, headline, title, body, label), three sizes (large, medium, small), and two sets.
Both sets follow the same scale. Scale ratio: Major Second (1.125), base size 14.

Token form: `md.sys.typescale.display-large`; the emphasized form is
`md.sys.typescale.emphasized.display-large`. Components do not use emphasized styles by default; you
swap the token.

### Baseline

| Style | Size | Line height | Tracking | Weight | Typeface |
|---|---|---|---|---|---|
| Display large | 57sp | 64sp | -0.2sp | Regular | Brand |
| Display medium | 45sp | 52sp | 0sp | Regular | Brand |
| Display small | 36sp | 44sp | 0sp | Regular | Brand |
| Headline large | 32sp | 40sp | 0sp | Regular | Brand |
| Headline medium | 28sp | 36sp | 0sp | Regular | Brand |
| Headline small | 24sp | 32sp | 0sp | Regular | Brand |
| Title large | 22sp | 28sp | 0sp | Regular | Brand |
| Title medium | 16sp | 24sp | 0.2sp | Medium | Plain |
| Title small | 14sp | 20sp | 0.1sp | Medium | Plain |
| Body large | 16sp | 24sp | 0.5sp | Regular | Plain |
| Body medium | 14sp | 20sp | 0.2sp | Regular | Plain |
| Body small | 12sp | 16sp | 0.4sp | Regular | Plain |
| Label large | 14sp | 20sp | 0.1sp | Medium | Plain |
| Label medium | 12sp | 16sp | 0.5sp | Medium | Plain |
| Label small | 11sp | 16sp | 0.5sp | Medium | Plain |

Older references list display large tracking as -0.25 and title medium as 0.15, and body medium as
0.25. The values above come from the current AndroidX generated tokens. If a project pins an older
library, its tokens are what ships.

### Emphasized (M3 Expressive, May 2025)

Size and line height match the baseline in every case. Only weight and tracking change.

| Style | Size | Line height | Tracking | Weight |
|---|---|---|---|---|
| Display large emph. | 57sp | 64sp | 0sp | Medium |
| Display medium emph. | 45sp | 52sp | 0sp | Medium |
| Display small emph. | 36sp | 44sp | 0sp | Medium |
| Headline large emph. | 32sp | 40sp | 0sp | Medium |
| Headline medium emph. | 28sp | 36sp | 0sp | Medium |
| Headline small emph. | 24sp | 32sp | 0sp | Medium |
| Title large emph. | 22sp | 28sp | 0sp | Medium |
| Title medium emph. | 16sp | 24sp | 0.15sp | **Bold** |
| Title small emph. | 14sp | 20sp | 0.1sp | **Bold** |
| Body large emph. | 16sp | 24sp | 0.15sp | Medium |
| Body medium emph. | 14sp | 20sp | 0.25sp | Medium |
| Body small emph. | 12sp | 16sp | 0.4sp | Medium |
| Label large emph. | 14sp | 20sp | 0.1sp | **Bold** |
| Label medium emph. | 12sp | 16sp | 0.5sp | **Bold** |
| Label small emph. | 11sp | 16sp | 0.5sp | **Bold** |

The pattern: display, headline, title large, and body styles go from Regular to Medium. Title medium,
title small, and all label sizes go from Medium to Bold. Display, headline, and title large tracking
becomes exactly 0.

Typeface slots (brand and plain) and sp to rem conversion are in `typography.md`.

## Motion springs

A spring is stiffness, damping, and initial velocity. There are two schemes, three speeds, and two
styles. Token form: `md.sys.motion.spring.fast.spatial`. The scheme (`expressive` or `standard`) is
not part of the token name. It is applied at product level, so schemes swap without retokenizing.

### Expressive scheme (Material's recommended default)

| Token | Damping | Stiffness |
|---|---|---|
| Default spatial | 0.8 | 380 |
| Fast spatial | 0.6 | 800 |
| Slow spatial | 0.8 | 200 |
| Default effects | 1.0 | 1600 |
| Fast effects | 1.0 | 3800 |
| Slow effects | 1.0 | 800 |

### Standard scheme (utilitarian products)

| Token | Damping | Stiffness |
|---|---|---|
| Default spatial | 0.9 | 700 |
| Fast spatial | 0.9 | 1400 |
| Slow spatial | 0.9 | 300 |
| Default effects | 1.0 | 1600 |
| Fast effects | 1.0 | 3800 |
| Slow effects | 1.0 | 800 |

Effects springs are identical in both schemes and always use damping 1.0, so color and opacity never
overshoot. All the expression is in the spatial damping: 0.6 to 0.8 in expressive (visible bounce)
and 0.9 in standard (barely any).

Spring values differ per device class (wearable, phone, tablet) so "fast" feels fast in context. The
relative order is guaranteed; the absolute values above are the Compose phone set.

### Web: spring to cubic-bezier and duration

Use real springs where possible. These curves are the sanctioned fallback for animations without
interruption or gestures.

| Spring | cubic-bezier | Duration |
|---|---|---|
| Expressive fast spatial | 0.42, 1.67, 0.21, 0.90 | 350ms |
| Expressive default spatial | 0.38, 1.21, 0.22, 1.00 | 500ms |
| Expressive slow spatial | 0.39, 1.29, 0.35, 0.98 | 650ms |
| Expressive fast effects | 0.31, 0.94, 0.34, 1.00 | 150ms |
| Expressive default effects | 0.34, 0.80, 0.34, 1.00 | 200ms |
| Expressive slow effects | 0.34, 0.88, 0.34, 1.00 | 300ms |
| Standard fast spatial | 0.27, 1.06, 0.18, 1.00 | 350ms |
| Standard default spatial | 0.27, 1.06, 0.18, 1.00 | 500ms |
| Standard slow spatial | 0.27, 1.06, 0.18, 1.00 | 750ms |
| Standard fast effects | 0.31, 0.94, 0.34, 1.00 | 150ms |
| Standard default effects | 0.34, 0.80, 0.34, 1.00 | 200ms |
| Standard slow effects | 0.34, 0.88, 0.34, 1.00 | 300ms |

The y1 values above 1 (1.67, 1.21, 1.29, 1.06) are the overshoot, which is the bounce. Effects curves
never exceed 1.

### Legacy easing and duration (still used by transitions)

Durations: short 1 to 4 = 50, 100, 150, 200ms; medium 1 to 4 = 250, 300, 350, 400ms; long 1 to 4 =
450, 500, 550, 600ms; extra-long 1 to 4 = 700, 800, 900, 1000ms.

Easing:

| Token | CSS |
|---|---|
| Emphasized | No CSS curve is published; the official spec says to use standard as the CSS fallback. |
| Emphasized decelerate | `cubic-bezier(0.05, 0.7, 0.1, 1)` |
| Emphasized accelerate | `cubic-bezier(0.3, 0, 0.8, 0.15)` |
| Standard | `cubic-bezier(0.2, 0, 0, 1)` |
| Standard decelerate | `cubic-bezier(0, 0, 0, 1)` |
| Standard accelerate | `cubic-bezier(0.3, 0, 1, 1)` |
| Legacy | `cubic-bezier(0.4, 0, 0.2, 1)` |
| Linear | `cubic-bezier(0, 0, 1, 1)` |

Source: `documentation/styles/motion/easing-and-duration/tokens-specs.md`. The physics system replaces
easing and duration for components. Use springs for new component motion.

## Contrast levels

Three tokenized levels, applied automatically across light and dark theme:

| Level | Guarantee | Note |
|---|---|---|
| Standard (default) | Mixed high and low contrast. | Reduces cognitive load through hierarchy. |
| Medium | At least 3:1. | For users who need more contrast but get halation discomfort at high. |
| High | 7:1. | Applied to card content, not the card container. |

Text minimums: 3:1 for large text and 4.5:1 for small text.

## Color scheme generation

26 standard color roles (45 including add-on roles). Source color, then the algorithm, then five key
colors (primary, secondary, tertiary, neutral, neutral variant), then tonal palettes, roles, and UI.

Tonal palettes are numbered 0 to 100 in steps of 10, plus 95, 98, and 99. Colors are defined in HCT:
hue 0 to 360 (circular), chroma 0 to about 120, tone 0 (black) to 100 (white).

Accessible pairing is by tone distance: tones 50 and 98 give about 3:1; tones 30 and 98 give about
7:1.

Generated by [Material Color Utilities](https://github.com/material-foundation/material-color-utilities).

## Elevation

Six levels: level 0 = 0dp, level 1 = 1dp, level 2 = 3dp, level 3 = 6dp, level 4 = 8dp, level 5 =
12dp.

Surface color in M3 is tone-based, not derived from elevation. The old "+1 to +5 elevation overlay"
model was replaced by `surface container` roles. Do not tie surface color to elevation.

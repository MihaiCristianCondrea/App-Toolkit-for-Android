---
title: "Extended FABs"
source_url: "https://m3.material.io/components/extended-fab/specs"
lastmod: "2026-05-18"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "components"
assets:
  - "assets/images/0a7a08bc5326-ma3rfc55-01-3P.png"
  - "assets/images/c08cbeb513b6-ma3rfwo5-02-3P.png"
  - "assets/images/6312ddb56a64-ma3rgmo9-03-3P.png"
  - "assets/images/ce1f182a9511-ma3rha4a-04-3P.png"
  - "assets/images/ac6f8cdc99e9-ma3ri2r1-05-3P.png"
  - "assets/images/725545795226-maljqck5-06-3P.png"
  - "assets/images/a6868cb7ae56-ma3rj563-07-3P.png"
  - "assets/images/8e4790c6a96e-ma3rjn8g-08-3P.png"
  - "assets/images/0ffeeca0fbb4-ma3rm9q3-10-3P.png"
  - "assets/images/952983238f04-ml9o6q6p-15-3P.png"
  - "assets/images/ad2847b8dbde-ml9o6v12-16-3P.png"
  - "assets/images/d1f6d27d7e5b-ma3rmvmo-11-3P.png"
  - "assets/images/6e1db152da5c-ma3rnofz-12-3P.png"
  - "assets/images/ea5474e7eeaf-maljrky7-13-3P.png"
  - "assets/images/ffab5c35f570-maebvm3w-14-3P.png"
  - "assets/images/3dfffa03a2ce-maebvv3o-15-3P.png"
---

# Extended FABs

## Specs

## Variants

![3 variants of extended FABs.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fma3rfc55-01-3P.png?alt=media&token=6a57bbbf-b6f3-4f09-8343-d327e4581fbc>)

_Small extended FAB; Medium extended FAB; Large extended FAB_

### Baseline variants

The baseline extended FAB is no longer recommended in the M3 expressive update. Use a small extended FAB; the type style was updated from **label large** to **title medium**, and the inner padding was reduced. [View baseline extended FAB specs](specs.md#01e114e6-8c3d-4d39-9376-65aa5c10e01b)

![1 baseline extended FAB.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fma3rfwo5-02-3P.png?alt=media&token=eb54fbf9-bad6-4aa9-8407-0ebd348d1787>)

_Extended FAB_

| Variant | M3 | M3 Expressive |
| --- | --- | --- |
| Small extended FAB | \-- | Available |
| Medium extended FAB | \-- | Available |
| Large extended FAB | \-- | Available |
| Extended FAB (baseline) | Available | Not recommended. Use **small extended FAB.** |

## Tokens & specs

Use the table's menu to select a token set. Extended FAB tokens are organized by size and color.

### TOKEN_TABLE

Component: Extended FAB

Configured context tags: None, Medium contrast, Default, High contrast, 3P, Android, Dark, Light

#### Token sets

| Token set | Name | Type | Tokens | Description |
| --- | --- | --- | --- | --- |
| Extended FAB - Size - Small | md.comp.extended-fab.small | COMPONENT | 7 | For expressive small extended FABs. |
| Extended FAB - Size - Medium | md.comp.extended-fab.medium | COMPONENT | 7 | For medium extended FABs. |
| Extended FAB - Size - Large | md.comp.extended-fab.large | COMPONENT | 7 | For large extended FABs. |
| Extended FAB - Color - Tonal primary | md.comp.extended-fab.primary-container | COMPONENT | 20 | For tonal primary container extended FABs. |
| Extended FAB - Color - Tonal secondary | md.comp.extended-fab.secondary-container | COMPONENT | 20 | For tonal secondary container extended FABs. |
| Extended FAB - Color - Tonal tertiary | md.comp.extended-fab.tertiary-container | COMPONENT | 20 | For tonal tertiary container extended FABs. |
| Extended FAB - Color - Primary | md.comp.extended-fab.primary | COMPONENT | 46 | For primary color extended FABs. |
| Extended FAB - Color - Secondary | md.comp.extended-fab.secondary | COMPONENT | 46 | For secondary color extended FABs. |
| Extended FAB - Color - Tertiary | md.comp.extended-fab.tertiary | COMPONENT | 46 | For tertiary color extended FABs. |

#### Extended FAB - Size - Small (md.comp.extended-fab.small)

For expressive small extended FABs.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.extended-fab.small.container.height | LENGTH |  | 56dp |  |  |
| md.comp.extended-fab.small.label-text | TYPOGRAPHY |  | `md.sys.typescale.title-medium` |  |  |
| md.comp.extended-fab.small.icon.size | LENGTH |  | 24dp |  |  |
| md.comp.extended-fab.small.container.shape | SHAPE |  | `md.sys.shape.corner.large` |  |  |
| md.comp.extended-fab.small.leading-space | LENGTH |  | 16dp |  |  |
| md.comp.extended-fab.small.icon-label-space | LENGTH |  | 8dp |  |  |
| md.comp.extended-fab.small.trailing-space | LENGTH |  | 16dp |  |  |

#### Extended FAB - Size - Medium (md.comp.extended-fab.medium)

For medium extended FABs.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.extended-fab.medium.container.height | LENGTH |  | 80dp |  |  |
| md.comp.extended-fab.medium.label-text | TYPOGRAPHY |  | `md.sys.typescale.title-large` |  |  |
| md.comp.extended-fab.medium.icon.size | LENGTH |  | 28dp |  |  |
| md.comp.extended-fab.medium.container.shape | SHAPE |  | `md.sys.shape.corner.large-increased` |  |  |
| md.comp.extended-fab.medium.leading-space | LENGTH |  | 26dp |  |  |
| md.comp.extended-fab.medium.icon-label-space | LENGTH |  | 12dp |  |  |
| md.comp.extended-fab.medium.trailing-space | LENGTH |  | 26dp |  |  |

#### Extended FAB - Size - Large (md.comp.extended-fab.large)

For large extended FABs.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.extended-fab.large.container.height | LENGTH |  | 96dp |  |  |
| md.comp.extended-fab.large.label-text | TYPOGRAPHY |  | `md.sys.typescale.headline-small` |  |  |
| md.comp.extended-fab.large.icon.size | LENGTH |  | 36dp |  |  |
| md.comp.extended-fab.large.container.shape | SHAPE |  | `md.sys.shape.corner.extra-large` |  |  |
| md.comp.extended-fab.large.leading-space | LENGTH |  | 28dp |  |  |
| md.comp.extended-fab.large.icon-label-space | LENGTH |  | 16dp |  |  |
| md.comp.extended-fab.large.trailing-space | LENGTH |  | 28dp |  |  |

#### Extended FAB - Color - Tonal primary (md.comp.extended-fab.primary-container)

For tonal primary container extended FABs.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.extended-fab.primary-container.container.color | COLOR | Enabled | `md.sys.color.primary-container` |  |  |
| md.comp.extended-fab.primary-container.container.elevation | ELEVATION | Enabled | `md.sys.elevation.level3` |  |  |
| md.comp.extended-fab.primary-container.container.shadow-color | COLOR | Enabled | `md.sys.color.shadow` |  |  |
| md.comp.extended-fab.primary-container.label-text.color | COLOR | Enabled | `md.sys.color.on-primary-container` |  |  |
| md.comp.extended-fab.primary-container.icon.color | COLOR | Enabled | `md.sys.color.on-primary-container` |  |  |
| md.comp.extended-fab.primary-container.hovered.container.elevation | ELEVATION | Hovered | `md.sys.elevation.level4` |  |  |
| md.comp.extended-fab.primary-container.hovered.state-layer.color | COLOR | Hovered | `md.sys.color.on-primary-container` |  |  |
| md.comp.extended-fab.primary-container.hovered.state-layer.opacity | OPACITY | Hovered | `md.sys.state.hover.state-layer-opacity` |  |  |
| md.comp.extended-fab.primary-container.hovered.label-text.color | COLOR | Hovered | `md.sys.color.on-primary-container` |  |  |
| md.comp.extended-fab.primary-container.hovered.icon.color | COLOR | Hovered | `md.sys.color.on-primary-container` |  |  |
| md.comp.extended-fab.primary-container.focused.container.elevation | ELEVATION | Focused | `md.sys.elevation.level3` |  |  |
| md.comp.extended-fab.primary-container.focused.state-layer.color | COLOR | Focused | `md.sys.color.on-primary-container` |  |  |
| md.comp.extended-fab.primary-container.focused.state-layer.opacity | OPACITY | Focused | `md.sys.state.focus.state-layer-opacity` |  |  |
| md.comp.extended-fab.primary-container.focused.label-text.color | COLOR | Focused | `md.sys.color.on-primary-container` |  |  |
| md.comp.extended-fab.primary-container.focused.icon.color | COLOR | Focused | `md.sys.color.on-primary-container` |  |  |
| md.comp.extended-fab.primary-container.pressed.container.elevation | ELEVATION | Pressed | `md.sys.elevation.level3` |  |  |
| md.comp.extended-fab.primary-container.pressed.state-layer.color | COLOR | Pressed | `md.sys.color.on-primary-container` |  |  |
| md.comp.extended-fab.primary-container.pressed.state-layer.opacity | OPACITY | Pressed | `md.sys.state.pressed.state-layer-opacity` |  |  |
| md.comp.extended-fab.primary-container.pressed.label-text.color | COLOR | Pressed | `md.sys.color.on-primary-container` |  |  |
| md.comp.extended-fab.primary-container.pressed.icon.color | COLOR | Pressed | `md.sys.color.on-primary-container` |  |  |

#### Extended FAB - Color - Tonal secondary (md.comp.extended-fab.secondary-container)

For tonal secondary container extended FABs.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.extended-fab.secondary-container.container.color | COLOR | Enabled | `md.sys.color.secondary-container` |  |  |
| md.comp.extended-fab.secondary-container.container.elevation | ELEVATION | Enabled | `md.sys.elevation.level3` |  |  |
| md.comp.extended-fab.secondary-container.container.shadow-color | COLOR | Enabled | `md.sys.color.shadow` |  |  |
| md.comp.extended-fab.secondary-container.label-text.color | COLOR | Enabled | `md.sys.color.on-secondary-container` |  |  |
| md.comp.extended-fab.secondary-container.icon.color | COLOR | Enabled | `md.sys.color.on-secondary-container` |  |  |
| md.comp.extended-fab.secondary-container.hovered.container.elevation | ELEVATION | Hovered | `md.sys.elevation.level4` |  |  |
| md.comp.extended-fab.secondary-container.hovered.state-layer.color | COLOR | Hovered | `md.sys.color.on-secondary-container` |  |  |
| md.comp.extended-fab.secondary-container.hovered.state-layer.opacity | OPACITY | Hovered | `md.sys.state.hover.state-layer-opacity` |  |  |
| md.comp.extended-fab.secondary-container.hovered.label-text.color | COLOR | Hovered | `md.sys.color.on-secondary-container` |  |  |
| md.comp.extended-fab.secondary-container.hovered.icon.color | COLOR | Hovered | `md.sys.color.on-secondary-container` |  |  |
| md.comp.extended-fab.secondary-container.focused.container.elevation | ELEVATION | Focused | `md.sys.elevation.level3` |  |  |
| md.comp.extended-fab.secondary-container.focused.state-layer.color | COLOR | Focused | `md.sys.color.on-secondary-container` |  |  |
| md.comp.extended-fab.secondary-container.focused.state-layer.opacity | OPACITY | Focused | `md.sys.state.focus.state-layer-opacity` |  |  |
| md.comp.extended-fab.secondary-container.focused.label-text.color | COLOR | Focused | `md.sys.color.on-secondary-container` |  |  |
| md.comp.extended-fab.secondary-container.focused.icon.color | COLOR | Focused | `md.sys.color.on-secondary-container` |  |  |
| md.comp.extended-fab.secondary-container.pressed.container.elevation | ELEVATION | Pressed | `md.sys.elevation.level3` |  |  |
| md.comp.extended-fab.secondary-container.pressed.state-layer.color | COLOR | Pressed | `md.sys.color.on-secondary-container` |  |  |
| md.comp.extended-fab.secondary-container.pressed.state-layer.opacity | OPACITY | Pressed | `md.sys.state.pressed.state-layer-opacity` |  |  |
| md.comp.extended-fab.secondary-container.pressed.label-text.color | COLOR | Pressed | `md.sys.color.on-secondary-container` |  |  |
| md.comp.extended-fab.secondary-container.pressed.icon.color | COLOR | Pressed | `md.sys.color.on-secondary-container` |  |  |

#### Extended FAB - Color - Tonal tertiary (md.comp.extended-fab.tertiary-container)

For tonal tertiary container extended FABs.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.extended-fab.tertiary-container.container.color | COLOR | Enabled | `md.sys.color.tertiary-container` |  |  |
| md.comp.extended-fab.tertiary-container.container.elevation | ELEVATION | Enabled | `md.sys.elevation.level3` |  |  |
| md.comp.extended-fab.tertiary-container.container.shadow-color | COLOR | Enabled | `md.sys.color.shadow` |  |  |
| md.comp.extended-fab.tertiary-container.label-text.color | COLOR | Enabled | `md.sys.color.on-tertiary-container` |  |  |
| md.comp.extended-fab.tertiary-container.icon.color | COLOR | Enabled | `md.sys.color.on-tertiary-container` |  |  |
| md.comp.extended-fab.tertiary-container.hovered.container.elevation | ELEVATION | Hovered | `md.sys.elevation.level4` |  |  |
| md.comp.extended-fab.tertiary-container.hovered.state-layer.color | COLOR | Hovered | `md.sys.color.on-tertiary-container` |  |  |
| md.comp.extended-fab.tertiary-container.hovered.state-layer.opacity | OPACITY | Hovered | `md.sys.state.hover.state-layer-opacity` |  |  |
| md.comp.extended-fab.tertiary-container.hovered.label-text.color | COLOR | Hovered | `md.sys.color.on-tertiary-container` |  |  |
| md.comp.extended-fab.tertiary-container.hovered.icon.color | COLOR | Hovered | `md.sys.color.on-tertiary-container` |  |  |
| md.comp.extended-fab.tertiary-container.focused.container.elevation | ELEVATION | Focused | `md.sys.elevation.level3` |  |  |
| md.comp.extended-fab.tertiary-container.focused.state-layer.color | COLOR | Focused | `md.sys.color.on-tertiary-container` |  |  |
| md.comp.extended-fab.tertiary-container.focused.state-layer.opacity | OPACITY | Focused | `md.sys.state.focus.state-layer-opacity` |  |  |
| md.comp.extended-fab.tertiary-container.focused.label-text.color | COLOR | Focused | `md.sys.color.on-tertiary-container` |  |  |
| md.comp.extended-fab.tertiary-container.focused.icon.color | COLOR | Focused | `md.sys.color.on-tertiary-container` |  |  |
| md.comp.extended-fab.tertiary-container.pressed.container.elevation | ELEVATION | Pressed | `md.sys.elevation.level3` |  |  |
| md.comp.extended-fab.tertiary-container.pressed.state-layer.color | COLOR | Pressed | `md.sys.color.on-tertiary-container` |  |  |
| md.comp.extended-fab.tertiary-container.pressed.state-layer.opacity | OPACITY | Pressed | `md.sys.state.pressed.state-layer-opacity` |  |  |
| md.comp.extended-fab.tertiary-container.pressed.label-text.color | COLOR | Pressed | `md.sys.color.on-tertiary-container` |  |  |
| md.comp.extended-fab.tertiary-container.pressed.icon.color | COLOR | Pressed | `md.sys.color.on-tertiary-container` |  |  |

#### Extended FAB - Color - Primary (md.comp.extended-fab.primary)

For primary color extended FABs.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.extended-fab.primary.container.color | COLOR | Enabled | `md.sys.color.primary` |  |  |
| md.comp.extended-fab.primary.container.elevation | ELEVATION | Enabled | `md.sys.elevation.level3` |  |  |
| md.comp.extended-fab.primary.container.shadow-color | COLOR | Enabled | `md.sys.color.shadow` |  |  |
| md.comp.extended-fab.primary.label-text.color | COLOR | Enabled | `md.sys.color.on-primary` |  |  |
| md.comp.extended-fab.primary.icon.color | COLOR | Enabled | `md.sys.color.on-primary` |  |  |
| md.comp.extended-fab.primary.hovered.container.elevation | ELEVATION | Hovered | `md.sys.elevation.level4` |  |  |
| md.comp.extended-fab.primary.hovered.state-layer.color | COLOR | Hovered | `md.sys.color.on-primary` |  |  |
| md.comp.extended-fab.primary.hovered.state-layer.opacity | OPACITY | Hovered | `md.sys.state.hover.state-layer-opacity` |  |  |
| md.comp.extended-fab.primary.hovered.label-text.color | COLOR | Hovered | `md.sys.color.on-primary` |  |  |
| md.comp.extended-fab.primary.hovered.icon.color | COLOR | Hovered | `md.sys.color.on-primary` |  |  |
| md.comp.extended-fab.primary.focused.container.elevation | ELEVATION | Focused | `md.sys.elevation.level3` |  |  |
| md.comp.extended-fab.primary.focused.state-layer.color | COLOR | Focused | `md.sys.color.on-primary` |  |  |
| md.comp.extended-fab.primary.focused.state-layer.opacity | OPACITY | Focused | `md.sys.state.focus.state-layer-opacity` |  |  |
| md.comp.extended-fab.primary.focused.label-text.color | COLOR | Focused | `md.sys.color.on-primary` |  |  |
| md.comp.extended-fab.primary.focused.icon.color | COLOR | Focused | `md.sys.color.on-primary` |  |  |
| md.comp.extended-fab.primary.pressed.container.elevation | ELEVATION | Pressed | `md.sys.elevation.level3` |  |  |
| md.comp.extended-fab.primary.pressed.state-layer.color | COLOR | Pressed | `md.sys.color.on-primary` |  |  |
| md.comp.extended-fab.primary.pressed.state-layer.opacity | OPACITY | Pressed | `md.sys.state.pressed.state-layer-opacity` |  |  |
| md.comp.extended-fab.primary.pressed.label-text.color | COLOR | Pressed | `md.sys.color.on-primary` |  |  |
| md.comp.extended-fab.primary.pressed.icon.color | COLOR | Pressed | `md.sys.color.on-primary` |  |  |
| md.comp.extended-fab.primary.container.shape | SHAPE | [Deprecated] Enabled / [Deprecated] Container | `md.sys.shape.corner.large` |  | Token is deprecated. |
| md.comp.extended-fab.primary.container.height | LENGTH | [Deprecated] Enabled / [Deprecated] Container | 56dp |  | Token is deprecated. |
| md.comp.extended-fab.primary.lowered.container.elevation | ELEVATION | [Deprecated] Enabled / [Deprecated] Container | `md.sys.elevation.level1` |  | Token is deprecated. |
| md.comp.extended-fab.primary.label-text.font | FONT_NAMES | [Deprecated] Enabled / [Deprecated] Label text | `md.sys.typescale.label-large.font` |  | Token is deprecated. |
| md.comp.extended-fab.primary.label-text.line-height | LINE_HEIGHT | [Deprecated] Enabled / [Deprecated] Label text | `md.sys.typescale.label-large.line-height` |  | Token is deprecated. |
| md.comp.extended-fab.primary.label-text.size | FONT_SIZE | [Deprecated] Enabled / [Deprecated] Label text | `md.sys.typescale.label-large.size` |  | Token is deprecated. |
| md.comp.extended-fab.primary.label-text.weight | FONT_WEIGHT | [Deprecated] Enabled / [Deprecated] Label text | `md.sys.typescale.label-large.weight` |  | Token is deprecated. |
| md.comp.extended-fab.primary.label-text.tracking | FONT_TRACKING | [Deprecated] Enabled / [Deprecated] Label text | `md.sys.typescale.label-large.tracking` |  | Token is deprecated. |
| md.comp.extended-fab.primary.label-text.type | TYPOGRAPHY | [Deprecated] Enabled / [Deprecated] Label text | font name: `md.comp.extended-fab.primary.label-text.font`, font weight: `md.comp.extended-fab.primary.label-text.weight`, font size: `md.comp.extended-fab.primary.label-text.size`, font tracking: `md.comp.extended-fab.primary.label-text.tracking`, line height: `md.comp.extended-fab.primary.label-text.line-height` |  | Token is deprecated. |
| md.comp.extended-fab.primary.icon.size | LENGTH | [Deprecated] Enabled / [Deprecated] Icon | 24dp |  | Token is deprecated. |
| md.comp.extended-fab.primary.hover.container.elevation | ELEVATION | [Deprecated] Hovered / [Deprecated] Container | `md.sys.elevation.level4` |  | Token is deprecated. |
| md.comp.extended-fab.primary.lowered.hover.container.elevation | ELEVATION | [Deprecated] Hovered / [Deprecated] Container | `md.sys.elevation.level2` |  | Token is deprecated. |
| md.comp.extended-fab.primary.hover.label-text.color | COLOR | [Deprecated] Hovered / [Deprecated] Label text | `md.sys.color.on-primary-container` |  | Token is deprecated. |
| md.comp.extended-fab.primary.hover.state-layer.color | COLOR | [Deprecated] Hovered / [Deprecated] State layer | `md.sys.color.on-primary-container` |  | Token is deprecated. |
| md.comp.extended-fab.primary.hover.state-layer.opacity | OPACITY | [Deprecated] Hovered / [Deprecated] State layer | `md.sys.state.hover.state-layer-opacity` |  | Token is deprecated. |
| md.comp.extended-fab.primary.hover.icon.color | COLOR | [Deprecated] Hovered / [Deprecated] Icon | `md.sys.color.on-primary-container` |  | Token is deprecated. |
| md.comp.extended-fab.primary.focus.indicator.color | COLOR | [Deprecated] Focused / [Deprecated] Focus indicator | `md.sys.color.secondary` |  | Token is deprecated. |
| md.comp.extended-fab.primary.focus.indicator.thickness | LENGTH | [Deprecated] Focused / [Deprecated] Focus indicator | `md.sys.state.focus-indicator.thickness` |  | Token is deprecated. |
| md.comp.extended-fab.primary.focus.indicator.outline.offset | LENGTH | [Deprecated] Focused / [Deprecated] Focus indicator | `md.sys.state.focus-indicator.outer-offset` |  | Token is deprecated. |
| md.comp.extended-fab.primary.focus.container.elevation | ELEVATION | [Deprecated] Focused / [Deprecated] Container | `md.sys.elevation.level3` |  | Token is deprecated. |
| md.comp.extended-fab.primary.lowered.focus.container.elevation | ELEVATION | [Deprecated] Focused / [Deprecated] Container | `md.sys.elevation.level1` |  | Token is deprecated. |
| md.comp.extended-fab.primary.focus.label-text.color | COLOR | [Deprecated] Focused / [Deprecated] Label text | `md.sys.color.on-primary-container` |  | Token is deprecated. |
| md.comp.extended-fab.primary.focus.state-layer.color | COLOR | [Deprecated] Focused / [Deprecated] State layer | `md.sys.color.on-primary-container` |  | Token is deprecated. |
| md.comp.extended-fab.primary.focus.state-layer.opacity | OPACITY | [Deprecated] Focused / [Deprecated] State layer | `md.sys.state.focus.state-layer-opacity` |  | Token is deprecated. |
| md.comp.extended-fab.primary.focus.icon.color | COLOR | [Deprecated] Focused / [Deprecated] Icon | `md.sys.color.on-primary-container` |  | Token is deprecated. |
| md.comp.extended-fab.primary.lowered.pressed.container.elevation | ELEVATION | [Deprecated] Pressed (ripple) / [Deprecated] Container | `md.sys.elevation.level1` |  | Token is deprecated. |

#### Extended FAB - Color - Secondary (md.comp.extended-fab.secondary)

For secondary color extended FABs.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.extended-fab.secondary.container.color | COLOR | Enabled | `md.sys.color.secondary` |  |  |
| md.comp.extended-fab.secondary.container.elevation | ELEVATION | Enabled | `md.sys.elevation.level3` |  |  |
| md.comp.extended-fab.secondary.container.shadow-color | COLOR | Enabled | `md.sys.color.shadow` |  |  |
| md.comp.extended-fab.secondary.label-text.color | COLOR | Enabled | `md.sys.color.on-secondary` |  |  |
| md.comp.extended-fab.secondary.icon.color | COLOR | Enabled | `md.sys.color.on-secondary` |  |  |
| md.comp.extended-fab.secondary.hovered.container.elevation | ELEVATION | Hovered | `md.sys.elevation.level4` |  |  |
| md.comp.extended-fab.secondary.hovered.state-layer.color | COLOR | Hovered | `md.sys.color.on-secondary` |  |  |
| md.comp.extended-fab.secondary.hovered.state-layer.opacity | OPACITY | Hovered | `md.sys.state.hover.state-layer-opacity` |  |  |
| md.comp.extended-fab.secondary.hovered.label-text.color | COLOR | Hovered | `md.sys.color.on-secondary` |  |  |
| md.comp.extended-fab.secondary.hovered.icon.color | COLOR | Hovered | `md.sys.color.on-secondary` |  |  |
| md.comp.extended-fab.secondary.focused.container.elevation | ELEVATION | Focused | `md.sys.elevation.level3` |  |  |
| md.comp.extended-fab.secondary.focused.state-layer.color | COLOR | Focused | `md.sys.color.on-secondary` |  |  |
| md.comp.extended-fab.secondary.focused.state-layer.opacity | OPACITY | Focused | `md.sys.state.focus.state-layer-opacity` |  |  |
| md.comp.extended-fab.secondary.focused.label-text.color | COLOR | Focused | `md.sys.color.on-secondary` |  |  |
| md.comp.extended-fab.secondary.focused.icon.color | COLOR | Focused | `md.sys.color.on-secondary` |  |  |
| md.comp.extended-fab.secondary.pressed.container.elevation | ELEVATION | Pressed | `md.sys.elevation.level3` |  |  |
| md.comp.extended-fab.secondary.pressed.state-layer.color | COLOR | Pressed | `md.sys.color.on-secondary` |  |  |
| md.comp.extended-fab.secondary.pressed.state-layer.opacity | OPACITY | Pressed | `md.sys.state.pressed.state-layer-opacity` |  |  |
| md.comp.extended-fab.secondary.pressed.label-text.color | COLOR | Pressed | `md.sys.color.on-secondary` |  |  |
| md.comp.extended-fab.secondary.pressed.icon.color | COLOR | Pressed | `md.sys.color.on-secondary` |  |  |
| md.comp.extended-fab.secondary.container.shape | SHAPE | [Deprecated] Enabled / [Deprecated] Container | `md.sys.shape.corner.large` |  | Token is deprecated. |
| md.comp.extended-fab.secondary.container.height | LENGTH | [Deprecated] Enabled / [Deprecated] Container | 56dp |  | Token is deprecated. |
| md.comp.extended-fab.secondary.lowered.container.elevation | ELEVATION | [Deprecated] Enabled / [Deprecated] Container | `md.sys.elevation.level1` |  | Token is deprecated. |
| md.comp.extended-fab.secondary.label-text.font | FONT_NAMES | [Deprecated] Enabled / [Deprecated] Label text | `md.sys.typescale.label-large.font` |  | Token is deprecated. |
| md.comp.extended-fab.secondary.label-text.line-height | LINE_HEIGHT | [Deprecated] Enabled / [Deprecated] Label text | `md.sys.typescale.label-large.line-height` |  | Token is deprecated. |
| md.comp.extended-fab.secondary.label-text.size | FONT_SIZE | [Deprecated] Enabled / [Deprecated] Label text | `md.sys.typescale.label-large.size` |  | Token is deprecated. |
| md.comp.extended-fab.secondary.label-text.weight | FONT_WEIGHT | [Deprecated] Enabled / [Deprecated] Label text | `md.sys.typescale.label-large.weight` |  | Token is deprecated. |
| md.comp.extended-fab.secondary.label-text.tracking | FONT_TRACKING | [Deprecated] Enabled / [Deprecated] Label text | `md.sys.typescale.label-large.tracking` |  | Token is deprecated. |
| md.comp.extended-fab.secondary.label-text.type | TYPOGRAPHY | [Deprecated] Enabled / [Deprecated] Label text | font name: `md.comp.extended-fab.secondary.label-text.font`, font weight: `md.comp.extended-fab.secondary.label-text.weight`, font size: `md.comp.extended-fab.secondary.label-text.size`, font tracking: `md.comp.extended-fab.secondary.label-text.tracking`, line height: `md.comp.extended-fab.secondary.label-text.line-height` |  | Token is deprecated. |
| md.comp.extended-fab.secondary.icon.size | LENGTH | [Deprecated] Enabled / [Deprecated] Icon | 24dp |  | Token is deprecated. |
| md.comp.extended-fab.secondary.hover.container.elevation | ELEVATION | [Deprecated] Hovered / [Deprecated] Container | `md.sys.elevation.level4` |  | Token is deprecated. |
| md.comp.extended-fab.secondary.lowered.hover.container.elevation | ELEVATION | [Deprecated] Hovered / [Deprecated] Container | `md.sys.elevation.level2` |  | Token is deprecated. |
| md.comp.extended-fab.secondary.hover.label-text.color | COLOR | [Deprecated] Hovered / [Deprecated] Label text | `md.sys.color.on-secondary-container` |  | Token is deprecated. |
| md.comp.extended-fab.secondary.hover.state-layer.color | COLOR | [Deprecated] Hovered / [Deprecated] State layer | `md.sys.color.on-secondary-container` |  | Token is deprecated. |
| md.comp.extended-fab.secondary.hover.state-layer.opacity | OPACITY | [Deprecated] Hovered / [Deprecated] State layer | `md.sys.state.hover.state-layer-opacity` |  | Token is deprecated. |
| md.comp.extended-fab.secondary.hover.icon.color | COLOR | [Deprecated] Hovered / [Deprecated] Icon | `md.sys.color.on-secondary-container` |  | Token is deprecated. |
| md.comp.extended-fab.secondary.focus.indicator.color | COLOR | [Deprecated] Focused / [Deprecated] Focus indicator | `md.sys.color.secondary` |  | Token is deprecated. |
| md.comp.extended-fab.secondary.focus.indicator.thickness | LENGTH | [Deprecated] Focused / [Deprecated] Focus indicator | `md.sys.state.focus-indicator.thickness` |  | Token is deprecated. |
| md.comp.extended-fab.secondary.focus.indicator.outline.offset | LENGTH | [Deprecated] Focused / [Deprecated] Focus indicator | `md.sys.state.focus-indicator.outer-offset` |  | Token is deprecated. |
| md.comp.extended-fab.secondary.focus.container.elevation | ELEVATION | [Deprecated] Focused / [Deprecated] Container | `md.sys.elevation.level3` |  | Token is deprecated. |
| md.comp.extended-fab.secondary.lowered.focus.container.elevation | ELEVATION | [Deprecated] Focused / [Deprecated] Container | `md.sys.elevation.level1` |  | Token is deprecated. |
| md.comp.extended-fab.secondary.focus.label-text.color | COLOR | [Deprecated] Focused / [Deprecated] Label text | `md.sys.color.on-secondary-container` |  | Token is deprecated. |
| md.comp.extended-fab.secondary.focus.state-layer.color | COLOR | [Deprecated] Focused / [Deprecated] State layer | `md.sys.color.on-secondary-container` |  | Token is deprecated. |
| md.comp.extended-fab.secondary.focus.state-layer.opacity | OPACITY | [Deprecated] Focused / [Deprecated] State layer | `md.sys.state.focus.state-layer-opacity` |  | Token is deprecated. |
| md.comp.extended-fab.secondary.focus.icon.color | COLOR | [Deprecated] Focused / [Deprecated] Icon | `md.sys.color.on-secondary-container` |  | Token is deprecated. |
| md.comp.extended-fab.secondary.lowered.pressed.container.elevation | ELEVATION | [Deprecated] Pressed (ripple) / [Deprecated] Container | `md.sys.elevation.level1` |  | Token is deprecated. |

#### Extended FAB - Color - Tertiary (md.comp.extended-fab.tertiary)

For tertiary color extended FABs.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.extended-fab.tertiary.container.color | COLOR | Enabled | `md.sys.color.tertiary` |  |  |
| md.comp.extended-fab.tertiary.container.elevation | ELEVATION | Enabled | `md.sys.elevation.level3` |  |  |
| md.comp.extended-fab.tertiary.container.shadow-color | COLOR | Enabled | `md.sys.color.shadow` |  |  |
| md.comp.extended-fab.tertiary.label-text.color | COLOR | Enabled | `md.sys.color.on-tertiary` |  |  |
| md.comp.extended-fab.tertiary.icon.color | COLOR | Enabled | `md.sys.color.on-tertiary` |  |  |
| md.comp.extended-fab.tertiary.hovered.container.elevation | ELEVATION | Hovered | `md.sys.elevation.level4` |  |  |
| md.comp.extended-fab.tertiary.hovered.state-layer.color | COLOR | Hovered | `md.sys.color.on-tertiary` |  |  |
| md.comp.extended-fab.tertiary.hovered.state-layer.opacity | OPACITY | Hovered | `md.sys.state.hover.state-layer-opacity` |  |  |
| md.comp.extended-fab.tertiary.hovered.label-text.color | COLOR | Hovered | `md.sys.color.on-tertiary` |  |  |
| md.comp.extended-fab.tertiary.hovered.icon.color | COLOR | Hovered | `md.sys.color.on-tertiary` |  |  |
| md.comp.extended-fab.tertiary.focused.container.elevation | ELEVATION | Focused | `md.sys.elevation.level3` |  |  |
| md.comp.extended-fab.tertiary.focused.state-layer.color | COLOR | Focused | `md.sys.color.on-tertiary` |  |  |
| md.comp.extended-fab.tertiary.focused.state-layer.opacity | OPACITY | Focused | `md.sys.state.focus.state-layer-opacity` |  |  |
| md.comp.extended-fab.tertiary.focused.label-text.color | COLOR | Focused | `md.sys.color.on-tertiary` |  |  |
| md.comp.extended-fab.tertiary.focused.icon.color | COLOR | Focused | `md.sys.color.on-tertiary` |  |  |
| md.comp.extended-fab.tertiary.pressed.container.elevation | ELEVATION | Pressed | `md.sys.elevation.level3` |  |  |
| md.comp.extended-fab.tertiary.pressed.state-layer.color | COLOR | Pressed | `md.sys.color.on-tertiary` |  |  |
| md.comp.extended-fab.tertiary.pressed.state-layer.opacity | OPACITY | Pressed | `md.sys.state.pressed.state-layer-opacity` |  |  |
| md.comp.extended-fab.tertiary.pressed.label-text.color | COLOR | Pressed | `md.sys.color.on-tertiary` |  |  |
| md.comp.extended-fab.tertiary.pressed.icon.color | COLOR | Pressed | `md.sys.color.on-tertiary` |  |  |
| md.comp.extended-fab.tertiary.lowered.container.elevation | ELEVATION | [Deprecated] Enabled / [Deprecated] Container | `md.sys.elevation.level1` |  | Token is deprecated. |
| md.comp.extended-fab.tertiary.container.height | LENGTH | [Deprecated] Enabled / [Deprecated] Container | 56dp |  | Token is deprecated. |
| md.comp.extended-fab.tertiary.container.shape | SHAPE | [Deprecated] Enabled / [Deprecated] Container | `md.sys.shape.corner.large` |  | Token is deprecated. |
| md.comp.extended-fab.tertiary.label-text.font | FONT_NAMES | [Deprecated] Enabled / [Deprecated] Label text | `md.sys.typescale.label-large.font` |  | Token is deprecated. |
| md.comp.extended-fab.tertiary.label-text.line-height | LINE_HEIGHT | [Deprecated] Enabled / [Deprecated] Label text | `md.sys.typescale.label-large.line-height` |  | Token is deprecated. |
| md.comp.extended-fab.tertiary.label-text.size | FONT_SIZE | [Deprecated] Enabled / [Deprecated] Label text | `md.sys.typescale.label-large.size` |  | Token is deprecated. |
| md.comp.extended-fab.tertiary.label-text.tracking | FONT_TRACKING | [Deprecated] Enabled / [Deprecated] Label text | `md.sys.typescale.label-large.tracking` |  | Token is deprecated. |
| md.comp.extended-fab.tertiary.label-text.weight | FONT_WEIGHT | [Deprecated] Enabled / [Deprecated] Label text | `md.sys.typescale.label-large.weight` |  | Token is deprecated. |
| md.comp.extended-fab.tertiary.label-text.type | TYPOGRAPHY | [Deprecated] Enabled / [Deprecated] Label text | font name: `md.comp.extended-fab.tertiary.label-text.font`, font weight: `md.comp.extended-fab.tertiary.label-text.weight`, font size: `md.comp.extended-fab.tertiary.label-text.size`, font tracking: `md.comp.extended-fab.tertiary.label-text.tracking`, line height: `md.comp.extended-fab.tertiary.label-text.line-height` |  | Token is deprecated. |
| md.comp.extended-fab.tertiary.icon.size | LENGTH | [Deprecated] Enabled / [Deprecated] Icon | 24dp |  | Token is deprecated. |
| md.comp.extended-fab.tertiary.hover.container.elevation | ELEVATION | [Deprecated] Hovered / [Deprecated] Container | `md.sys.elevation.level4` |  | Token is deprecated. |
| md.comp.extended-fab.tertiary.lowered.hover.container.elevation | ELEVATION | [Deprecated] Hovered / [Deprecated] Container | `md.sys.elevation.level2` |  | Token is deprecated. |
| md.comp.extended-fab.tertiary.hover.label-text.color | COLOR | [Deprecated] Hovered / [Deprecated] Label text | `md.sys.color.on-tertiary-container` |  | Token is deprecated. |
| md.comp.extended-fab.tertiary.hover.state-layer.color | COLOR | [Deprecated] Hovered / [Deprecated] State layer | `md.sys.color.on-tertiary-container` |  | Token is deprecated. |
| md.comp.extended-fab.tertiary.hover.state-layer.opacity | OPACITY | [Deprecated] Hovered / [Deprecated] State layer | `md.sys.state.hover.state-layer-opacity` |  | Token is deprecated. |
| md.comp.extended-fab.tertiary.hover.icon.color | COLOR | [Deprecated] Hovered / [Deprecated] Icon | `md.sys.color.on-tertiary-container` |  | Token is deprecated. |
| md.comp.extended-fab.tertiary.focus.indicator.color | COLOR | [Deprecated] Focused / [Deprecated] Focus indicator | `md.sys.color.secondary` |  | Token is deprecated. |
| md.comp.extended-fab.tertiary.focus.indicator.thickness | LENGTH | [Deprecated] Focused / [Deprecated] Focus indicator | `md.sys.state.focus-indicator.thickness` |  | Token is deprecated. |
| md.comp.extended-fab.tertiary.focus.indicator.outline.offset | LENGTH | [Deprecated] Focused / [Deprecated] Focus indicator | `md.sys.state.focus-indicator.outer-offset` |  | Token is deprecated. |
| md.comp.extended-fab.tertiary.focus.container.elevation | ELEVATION | [Deprecated] Focused / [Deprecated] Container | `md.sys.elevation.level3` |  | Token is deprecated. |
| md.comp.extended-fab.tertiary.lowered.focus.container.elevation | ELEVATION | [Deprecated] Focused / [Deprecated] Container | `md.sys.elevation.level1` |  | Token is deprecated. |
| md.comp.extended-fab.tertiary.focus.label-text.color | COLOR | [Deprecated] Focused / [Deprecated] Label text | `md.sys.color.on-tertiary-container` |  | Token is deprecated. |
| md.comp.extended-fab.tertiary.focus.state-layer.color | COLOR | [Deprecated] Focused / [Deprecated] State layer | `md.sys.color.on-tertiary-container` |  | Token is deprecated. |
| md.comp.extended-fab.tertiary.focus.state-layer.opacity | OPACITY | [Deprecated] Focused / [Deprecated] State layer | `md.sys.state.focus.state-layer-opacity` |  | Token is deprecated. |
| md.comp.extended-fab.tertiary.focus.icon.color | COLOR | [Deprecated] Focused / [Deprecated] Icon | `md.sys.color.on-tertiary-container` |  | Token is deprecated. |
| md.comp.extended-fab.tertiary.lowered.pressed.container.elevation | ELEVATION | [Deprecated] Pressed (ripple) / [Deprecated] Container | `md.sys.elevation.level1` |  | Token is deprecated. |

## Anatomy

![3 elements of extended FABs.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fma3rgmo9-03-3P.png?alt=media&token=79794fe0-1648-40c3-8ecf-5e5592d12d16>)

_Container; Label text; Icon_

## Color

Color values are implemented through design tokens. For design, this means working with color values that correspond with tokens. For implementation, a color value will be a token that references a value. [Learn more about design tokens](https://m3.material.io/m3/pages/design-tokens/overview/)

### Color styles

Extended FABs can use several combinations of **color** and **on color** styles, such as **primary** and **on primary**. The following color mappings provide the same level of contrast and functionality, so choose a color mapping based on visual preference.

![6 extended FAB color styles.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fma3rha4a-04-3P.png?alt=media&token=e58d72e9-c13e-4a24-946b-f34ef588516a>)

_Extended FAB color roles used for light and dark schemes: Primary container & on primary container (default); Secondary container & on secondary container; Tertiary container & on tertiary container; Primary & on primary; Secondary & on secondary; Tertiary & on tertiary_

### Baseline color styles

Extended FABs should no longer use surface color styles. They’re still available, but not recommended.

![1 baseline extended FAB color style.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fma3ri2r1-05-3P.png?alt=media&token=262c3137-4f81-4e54-a410-502c63d52210>)

_Surface container FAB_

## States

States are visual representations used to communicate the status of a component or interactive element. [Learn more about interaction states](https://m3.material.io/m3/pages/interaction-states/overview)

When using a non-default color mapping for extended FABs, make sure the state layer color is the same as the icon color. For example, the state layer color for primary mapping should be md.sys.color.primary.

![4 states of extended FABs.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmaljqck5-06-3P.png?alt=media&token=fd04f135-2a40-4ee3-bb14-9673911382d7>)

_Enabled; Hovered - elevation 4; Focused; Pressed_

## Measurements

![Extended FAB padding and size measurements.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fma3rj563-07-3P.png?alt=media&token=a20a7b7a-2fb6-4c4d-829c-a97dc72e8f26>)

_Size and padding measurements of the small, medium, and large extended FABs_

![Extended FAB margin measurements.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fma3rjn8g-08-3P.png?alt=media&token=22bc3f2b-72fe-4857-8ebe-662c70054166>)

_Extended FABs should have margins of 16dp_

## Baseline extended FAB

![3 elements of baseline extended FAB.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fma3rm9q3-10-3P.png?alt=media&token=621815a8-8ecf-4aef-accc-2997113f330e>)

_Container; Label text; Icon_

### Baseline configurations

![Baseline extended FAB with icon.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fml9o6q6p-15-3P.png?alt=media&token=f58280d6-56a7-4c66-815c-4325264115e5>)

_With icon_

![Baseline extended FAB without icon.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fml9o6v12-16-3P.png?alt=media&token=42cd1ef1-6133-4d16-bfbb-5fcc172a5834>)

_Without icon_

### Baseline tokens

Use the table's menu to select a token set. The baseline extended FAB token sets are organized by common tokens, then by surface and branded color styles. Other color styles like primary, secondary, and tertiary are still used by the latest extended FABs.

### TOKEN_TABLE

Component: Extended FAB

#### Token sets

| Token set | Name | Type | Tokens | Description |
| --- | --- | --- | --- | --- |
| Extended FAB - Color - Tonal primary | md.comp.extended-fab.primary-container | COMPONENT | 20 | For tonal primary container extended FABs. |
| Extended FAB - Color - Primary | md.comp.extended-fab.primary | COMPONENT | 46 | For primary color extended FABs. |
| Extended FAB - Size - Large | md.comp.extended-fab.large | COMPONENT | 7 | For large extended FABs. |
| Extended FAB - Size - Medium | md.comp.extended-fab.medium | COMPONENT | 7 | For medium extended FABs. |
| Extended FAB - Color - Tonal secondary | md.comp.extended-fab.secondary-container | COMPONENT | 20 | For tonal secondary container extended FABs. |
| Extended FAB - Color - Tonal tertiary | md.comp.extended-fab.tertiary-container | COMPONENT | 20 | For tonal tertiary container extended FABs. |
| Extended FAB - Size - Small | md.comp.extended-fab.small | COMPONENT | 7 | For expressive small extended FABs. |
| Extended FAB - Size - Baseline | md.comp.extended-fab | COMPONENT | 7 | For baseline extended FABs. For expressive, use the small extended FAB token set `md.comp.extended-fab.small` instead. |
| Extended FAB - Color - Tertiary | md.comp.extended-fab.tertiary | COMPONENT | 46 | For tertiary color extended FABs. |
| Extended FAB - Color - Surface | md.comp.extended-fab.surface | COMPONENT | 38 | For surface color extended FABs. Not recommended with expressive extended FABs. |
| Extended FAB - Color - Secondary | md.comp.extended-fab.secondary | COMPONENT | 46 | For secondary color extended FABs. |
| Extended FAB - Color - Branded | md.comp.extended-fab.branded | COMPONENT | 34 | For branded color extended FABs. Not recommended with expressive extended FABs. |

#### Extended FAB - Color - Tonal primary (md.comp.extended-fab.primary-container)

For tonal primary container extended FABs.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.extended-fab.primary-container.container.color | COLOR | Enabled | `md.sys.color.primary-container` |  |  |
| md.comp.extended-fab.primary-container.container.elevation | ELEVATION | Enabled | `md.sys.elevation.level3` |  |  |
| md.comp.extended-fab.primary-container.container.shadow-color | COLOR | Enabled | `md.sys.color.shadow` |  |  |
| md.comp.extended-fab.primary-container.label-text.color | COLOR | Enabled | `md.sys.color.on-primary-container` |  |  |
| md.comp.extended-fab.primary-container.icon.color | COLOR | Enabled | `md.sys.color.on-primary-container` |  |  |
| md.comp.extended-fab.primary-container.hovered.container.elevation | ELEVATION | Hovered | `md.sys.elevation.level4` |  |  |
| md.comp.extended-fab.primary-container.hovered.state-layer.color | COLOR | Hovered | `md.sys.color.on-primary-container` |  |  |
| md.comp.extended-fab.primary-container.hovered.state-layer.opacity | OPACITY | Hovered | `md.sys.state.hover.state-layer-opacity` |  |  |
| md.comp.extended-fab.primary-container.hovered.label-text.color | COLOR | Hovered | `md.sys.color.on-primary-container` |  |  |
| md.comp.extended-fab.primary-container.hovered.icon.color | COLOR | Hovered | `md.sys.color.on-primary-container` |  |  |
| md.comp.extended-fab.primary-container.focused.container.elevation | ELEVATION | Focused | `md.sys.elevation.level3` |  |  |
| md.comp.extended-fab.primary-container.focused.state-layer.color | COLOR | Focused | `md.sys.color.on-primary-container` |  |  |
| md.comp.extended-fab.primary-container.focused.state-layer.opacity | OPACITY | Focused | `md.sys.state.focus.state-layer-opacity` |  |  |
| md.comp.extended-fab.primary-container.focused.label-text.color | COLOR | Focused | `md.sys.color.on-primary-container` |  |  |
| md.comp.extended-fab.primary-container.focused.icon.color | COLOR | Focused | `md.sys.color.on-primary-container` |  |  |
| md.comp.extended-fab.primary-container.pressed.container.elevation | ELEVATION | Pressed | `md.sys.elevation.level3` |  |  |
| md.comp.extended-fab.primary-container.pressed.state-layer.color | COLOR | Pressed | `md.sys.color.on-primary-container` |  |  |
| md.comp.extended-fab.primary-container.pressed.state-layer.opacity | OPACITY | Pressed | `md.sys.state.pressed.state-layer-opacity` |  |  |
| md.comp.extended-fab.primary-container.pressed.label-text.color | COLOR | Pressed | `md.sys.color.on-primary-container` |  |  |
| md.comp.extended-fab.primary-container.pressed.icon.color | COLOR | Pressed | `md.sys.color.on-primary-container` |  |  |

#### Extended FAB - Color - Primary (md.comp.extended-fab.primary)

For primary color extended FABs.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.extended-fab.primary.container.color | COLOR | Enabled | `md.sys.color.primary` |  |  |
| md.comp.extended-fab.primary.container.elevation | ELEVATION | Enabled | `md.sys.elevation.level3` |  |  |
| md.comp.extended-fab.primary.container.shadow-color | COLOR | Enabled | `md.sys.color.shadow` |  |  |
| md.comp.extended-fab.primary.label-text.color | COLOR | Enabled | `md.sys.color.on-primary` |  |  |
| md.comp.extended-fab.primary.icon.color | COLOR | Enabled | `md.sys.color.on-primary` |  |  |
| md.comp.extended-fab.primary.hovered.container.elevation | ELEVATION | Hovered | `md.sys.elevation.level4` |  |  |
| md.comp.extended-fab.primary.hovered.state-layer.color | COLOR | Hovered | `md.sys.color.on-primary` |  |  |
| md.comp.extended-fab.primary.hovered.state-layer.opacity | OPACITY | Hovered | `md.sys.state.hover.state-layer-opacity` |  |  |
| md.comp.extended-fab.primary.hovered.label-text.color | COLOR | Hovered | `md.sys.color.on-primary` |  |  |
| md.comp.extended-fab.primary.hovered.icon.color | COLOR | Hovered | `md.sys.color.on-primary` |  |  |
| md.comp.extended-fab.primary.focused.container.elevation | ELEVATION | Focused | `md.sys.elevation.level3` |  |  |
| md.comp.extended-fab.primary.focused.state-layer.color | COLOR | Focused | `md.sys.color.on-primary` |  |  |
| md.comp.extended-fab.primary.focused.state-layer.opacity | OPACITY | Focused | `md.sys.state.focus.state-layer-opacity` |  |  |
| md.comp.extended-fab.primary.focused.label-text.color | COLOR | Focused | `md.sys.color.on-primary` |  |  |
| md.comp.extended-fab.primary.focused.icon.color | COLOR | Focused | `md.sys.color.on-primary` |  |  |
| md.comp.extended-fab.primary.pressed.container.elevation | ELEVATION | Pressed | `md.sys.elevation.level3` |  |  |
| md.comp.extended-fab.primary.pressed.state-layer.color | COLOR | Pressed | `md.sys.color.on-primary` |  |  |
| md.comp.extended-fab.primary.pressed.state-layer.opacity | OPACITY | Pressed | `md.sys.state.pressed.state-layer-opacity` |  |  |
| md.comp.extended-fab.primary.pressed.label-text.color | COLOR | Pressed | `md.sys.color.on-primary` |  |  |
| md.comp.extended-fab.primary.pressed.icon.color | COLOR | Pressed | `md.sys.color.on-primary` |  |  |
| md.comp.extended-fab.primary.container.shape | SHAPE | [Deprecated] Enabled / [Deprecated] Container | `md.sys.shape.corner.large` |  | Token is deprecated. |
| md.comp.extended-fab.primary.container.height | LENGTH | [Deprecated] Enabled / [Deprecated] Container | 56dp |  | Token is deprecated. |
| md.comp.extended-fab.primary.lowered.container.elevation | ELEVATION | [Deprecated] Enabled / [Deprecated] Container | `md.sys.elevation.level1` |  | Token is deprecated. |
| md.comp.extended-fab.primary.label-text.font | FONT_NAMES | [Deprecated] Enabled / [Deprecated] Label text | `md.sys.typescale.label-large.font` |  | Token is deprecated. |
| md.comp.extended-fab.primary.label-text.line-height | LINE_HEIGHT | [Deprecated] Enabled / [Deprecated] Label text | `md.sys.typescale.label-large.line-height` |  | Token is deprecated. |
| md.comp.extended-fab.primary.label-text.size | FONT_SIZE | [Deprecated] Enabled / [Deprecated] Label text | `md.sys.typescale.label-large.size` |  | Token is deprecated. |
| md.comp.extended-fab.primary.label-text.weight | FONT_WEIGHT | [Deprecated] Enabled / [Deprecated] Label text | `md.sys.typescale.label-large.weight` |  | Token is deprecated. |
| md.comp.extended-fab.primary.label-text.tracking | FONT_TRACKING | [Deprecated] Enabled / [Deprecated] Label text | `md.sys.typescale.label-large.tracking` |  | Token is deprecated. |
| md.comp.extended-fab.primary.label-text.type | TYPOGRAPHY | [Deprecated] Enabled / [Deprecated] Label text | font name: `md.comp.extended-fab.primary.label-text.font`, font weight: `md.comp.extended-fab.primary.label-text.weight`, font size: `md.comp.extended-fab.primary.label-text.size`, font tracking: `md.comp.extended-fab.primary.label-text.tracking`, line height: `md.comp.extended-fab.primary.label-text.line-height` |  | Token is deprecated. |
| md.comp.extended-fab.primary.icon.size | LENGTH | [Deprecated] Enabled / [Deprecated] Icon | 24dp |  | Token is deprecated. |
| md.comp.extended-fab.primary.hover.container.elevation | ELEVATION | [Deprecated] Hovered / [Deprecated] Container | `md.sys.elevation.level4` |  | Token is deprecated. |
| md.comp.extended-fab.primary.lowered.hover.container.elevation | ELEVATION | [Deprecated] Hovered / [Deprecated] Container | `md.sys.elevation.level2` |  | Token is deprecated. |
| md.comp.extended-fab.primary.hover.label-text.color | COLOR | [Deprecated] Hovered / [Deprecated] Label text | `md.sys.color.on-primary-container` |  | Token is deprecated. |
| md.comp.extended-fab.primary.hover.state-layer.color | COLOR | [Deprecated] Hovered / [Deprecated] State layer | `md.sys.color.on-primary-container` |  | Token is deprecated. |
| md.comp.extended-fab.primary.hover.state-layer.opacity | OPACITY | [Deprecated] Hovered / [Deprecated] State layer | `md.sys.state.hover.state-layer-opacity` |  | Token is deprecated. |
| md.comp.extended-fab.primary.hover.icon.color | COLOR | [Deprecated] Hovered / [Deprecated] Icon | `md.sys.color.on-primary-container` |  | Token is deprecated. |
| md.comp.extended-fab.primary.focus.indicator.color | COLOR | [Deprecated] Focused / [Deprecated] Focus indicator | `md.sys.color.secondary` |  | Token is deprecated. |
| md.comp.extended-fab.primary.focus.indicator.thickness | LENGTH | [Deprecated] Focused / [Deprecated] Focus indicator | `md.sys.state.focus-indicator.thickness` |  | Token is deprecated. |
| md.comp.extended-fab.primary.focus.indicator.outline.offset | LENGTH | [Deprecated] Focused / [Deprecated] Focus indicator | `md.sys.state.focus-indicator.outer-offset` |  | Token is deprecated. |
| md.comp.extended-fab.primary.focus.container.elevation | ELEVATION | [Deprecated] Focused / [Deprecated] Container | `md.sys.elevation.level3` |  | Token is deprecated. |
| md.comp.extended-fab.primary.lowered.focus.container.elevation | ELEVATION | [Deprecated] Focused / [Deprecated] Container | `md.sys.elevation.level1` |  | Token is deprecated. |
| md.comp.extended-fab.primary.focus.label-text.color | COLOR | [Deprecated] Focused / [Deprecated] Label text | `md.sys.color.on-primary-container` |  | Token is deprecated. |
| md.comp.extended-fab.primary.focus.state-layer.color | COLOR | [Deprecated] Focused / [Deprecated] State layer | `md.sys.color.on-primary-container` |  | Token is deprecated. |
| md.comp.extended-fab.primary.focus.state-layer.opacity | OPACITY | [Deprecated] Focused / [Deprecated] State layer | `md.sys.state.focus.state-layer-opacity` |  | Token is deprecated. |
| md.comp.extended-fab.primary.focus.icon.color | COLOR | [Deprecated] Focused / [Deprecated] Icon | `md.sys.color.on-primary-container` |  | Token is deprecated. |
| md.comp.extended-fab.primary.lowered.pressed.container.elevation | ELEVATION | [Deprecated] Pressed (ripple) / [Deprecated] Container | `md.sys.elevation.level1` |  | Token is deprecated. |

#### Extended FAB - Size - Large (md.comp.extended-fab.large)

For large extended FABs.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.extended-fab.large.container.height | LENGTH |  | 96dp |  |  |
| md.comp.extended-fab.large.label-text | TYPOGRAPHY |  | `md.sys.typescale.headline-small` |  |  |
| md.comp.extended-fab.large.icon.size | LENGTH |  | 36dp |  |  |
| md.comp.extended-fab.large.container.shape | SHAPE |  | `md.sys.shape.corner.extra-large` |  |  |
| md.comp.extended-fab.large.leading-space | LENGTH |  | 28dp |  |  |
| md.comp.extended-fab.large.icon-label-space | LENGTH |  | 16dp |  |  |
| md.comp.extended-fab.large.trailing-space | LENGTH |  | 28dp |  |  |

#### Extended FAB - Size - Medium (md.comp.extended-fab.medium)

For medium extended FABs.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.extended-fab.medium.container.height | LENGTH |  | 80dp |  |  |
| md.comp.extended-fab.medium.label-text | TYPOGRAPHY |  | `md.sys.typescale.title-large` |  |  |
| md.comp.extended-fab.medium.icon.size | LENGTH |  | 28dp |  |  |
| md.comp.extended-fab.medium.container.shape | SHAPE |  | `md.sys.shape.corner.large-increased` |  |  |
| md.comp.extended-fab.medium.leading-space | LENGTH |  | 26dp |  |  |
| md.comp.extended-fab.medium.icon-label-space | LENGTH |  | 12dp |  |  |
| md.comp.extended-fab.medium.trailing-space | LENGTH |  | 26dp |  |  |

#### Extended FAB - Color - Tonal secondary (md.comp.extended-fab.secondary-container)

For tonal secondary container extended FABs.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.extended-fab.secondary-container.container.color | COLOR | Enabled | `md.sys.color.secondary-container` |  |  |
| md.comp.extended-fab.secondary-container.container.elevation | ELEVATION | Enabled | `md.sys.elevation.level3` |  |  |
| md.comp.extended-fab.secondary-container.container.shadow-color | COLOR | Enabled | `md.sys.color.shadow` |  |  |
| md.comp.extended-fab.secondary-container.label-text.color | COLOR | Enabled | `md.sys.color.on-secondary-container` |  |  |
| md.comp.extended-fab.secondary-container.icon.color | COLOR | Enabled | `md.sys.color.on-secondary-container` |  |  |
| md.comp.extended-fab.secondary-container.hovered.container.elevation | ELEVATION | Hovered | `md.sys.elevation.level4` |  |  |
| md.comp.extended-fab.secondary-container.hovered.state-layer.color | COLOR | Hovered | `md.sys.color.on-secondary-container` |  |  |
| md.comp.extended-fab.secondary-container.hovered.state-layer.opacity | OPACITY | Hovered | `md.sys.state.hover.state-layer-opacity` |  |  |
| md.comp.extended-fab.secondary-container.hovered.label-text.color | COLOR | Hovered | `md.sys.color.on-secondary-container` |  |  |
| md.comp.extended-fab.secondary-container.hovered.icon.color | COLOR | Hovered | `md.sys.color.on-secondary-container` |  |  |
| md.comp.extended-fab.secondary-container.focused.container.elevation | ELEVATION | Focused | `md.sys.elevation.level3` |  |  |
| md.comp.extended-fab.secondary-container.focused.state-layer.color | COLOR | Focused | `md.sys.color.on-secondary-container` |  |  |
| md.comp.extended-fab.secondary-container.focused.state-layer.opacity | OPACITY | Focused | `md.sys.state.focus.state-layer-opacity` |  |  |
| md.comp.extended-fab.secondary-container.focused.label-text.color | COLOR | Focused | `md.sys.color.on-secondary-container` |  |  |
| md.comp.extended-fab.secondary-container.focused.icon.color | COLOR | Focused | `md.sys.color.on-secondary-container` |  |  |
| md.comp.extended-fab.secondary-container.pressed.container.elevation | ELEVATION | Pressed | `md.sys.elevation.level3` |  |  |
| md.comp.extended-fab.secondary-container.pressed.state-layer.color | COLOR | Pressed | `md.sys.color.on-secondary-container` |  |  |
| md.comp.extended-fab.secondary-container.pressed.state-layer.opacity | OPACITY | Pressed | `md.sys.state.pressed.state-layer-opacity` |  |  |
| md.comp.extended-fab.secondary-container.pressed.label-text.color | COLOR | Pressed | `md.sys.color.on-secondary-container` |  |  |
| md.comp.extended-fab.secondary-container.pressed.icon.color | COLOR | Pressed | `md.sys.color.on-secondary-container` |  |  |

#### Extended FAB - Color - Tonal tertiary (md.comp.extended-fab.tertiary-container)

For tonal tertiary container extended FABs.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.extended-fab.tertiary-container.container.color | COLOR | Enabled | `md.sys.color.tertiary-container` |  |  |
| md.comp.extended-fab.tertiary-container.container.elevation | ELEVATION | Enabled | `md.sys.elevation.level3` |  |  |
| md.comp.extended-fab.tertiary-container.container.shadow-color | COLOR | Enabled | `md.sys.color.shadow` |  |  |
| md.comp.extended-fab.tertiary-container.label-text.color | COLOR | Enabled | `md.sys.color.on-tertiary-container` |  |  |
| md.comp.extended-fab.tertiary-container.icon.color | COLOR | Enabled | `md.sys.color.on-tertiary-container` |  |  |
| md.comp.extended-fab.tertiary-container.hovered.container.elevation | ELEVATION | Hovered | `md.sys.elevation.level4` |  |  |
| md.comp.extended-fab.tertiary-container.hovered.state-layer.color | COLOR | Hovered | `md.sys.color.on-tertiary-container` |  |  |
| md.comp.extended-fab.tertiary-container.hovered.state-layer.opacity | OPACITY | Hovered | `md.sys.state.hover.state-layer-opacity` |  |  |
| md.comp.extended-fab.tertiary-container.hovered.label-text.color | COLOR | Hovered | `md.sys.color.on-tertiary-container` |  |  |
| md.comp.extended-fab.tertiary-container.hovered.icon.color | COLOR | Hovered | `md.sys.color.on-tertiary-container` |  |  |
| md.comp.extended-fab.tertiary-container.focused.container.elevation | ELEVATION | Focused | `md.sys.elevation.level3` |  |  |
| md.comp.extended-fab.tertiary-container.focused.state-layer.color | COLOR | Focused | `md.sys.color.on-tertiary-container` |  |  |
| md.comp.extended-fab.tertiary-container.focused.state-layer.opacity | OPACITY | Focused | `md.sys.state.focus.state-layer-opacity` |  |  |
| md.comp.extended-fab.tertiary-container.focused.label-text.color | COLOR | Focused | `md.sys.color.on-tertiary-container` |  |  |
| md.comp.extended-fab.tertiary-container.focused.icon.color | COLOR | Focused | `md.sys.color.on-tertiary-container` |  |  |
| md.comp.extended-fab.tertiary-container.pressed.container.elevation | ELEVATION | Pressed | `md.sys.elevation.level3` |  |  |
| md.comp.extended-fab.tertiary-container.pressed.state-layer.color | COLOR | Pressed | `md.sys.color.on-tertiary-container` |  |  |
| md.comp.extended-fab.tertiary-container.pressed.state-layer.opacity | OPACITY | Pressed | `md.sys.state.pressed.state-layer-opacity` |  |  |
| md.comp.extended-fab.tertiary-container.pressed.label-text.color | COLOR | Pressed | `md.sys.color.on-tertiary-container` |  |  |
| md.comp.extended-fab.tertiary-container.pressed.icon.color | COLOR | Pressed | `md.sys.color.on-tertiary-container` |  |  |

#### Extended FAB - Size - Small (md.comp.extended-fab.small)

For expressive small extended FABs.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.extended-fab.small.container.height | LENGTH |  | 56dp |  |  |
| md.comp.extended-fab.small.label-text | TYPOGRAPHY |  | `md.sys.typescale.title-medium` |  |  |
| md.comp.extended-fab.small.icon.size | LENGTH |  | 24dp |  |  |
| md.comp.extended-fab.small.container.shape | SHAPE |  | `md.sys.shape.corner.large` |  |  |
| md.comp.extended-fab.small.leading-space | LENGTH |  | 16dp |  |  |
| md.comp.extended-fab.small.icon-label-space | LENGTH |  | 8dp |  |  |
| md.comp.extended-fab.small.trailing-space | LENGTH |  | 16dp |  |  |

#### Extended FAB - Size - Baseline (md.comp.extended-fab)

For baseline extended FABs. For expressive, use the small extended FAB token set `md.comp.extended-fab.small` instead.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.extended-fab.container.height | LENGTH |  | 56dp |  |  |
| md.comp.extended-fab.label-text | TYPOGRAPHY |  | `md.sys.typescale.label-large` |  |  |
| md.comp.extended-fab.icon.size | LENGTH |  | 24dp |  |  |
| md.comp.extended-fab.container.shape | SHAPE |  | `md.sys.shape.corner.large` |  |  |
| md.comp.extended-fab.leading-space | LENGTH |  | 16dp |  |  |
| md.comp.extended-fab.icon-label-space | LENGTH |  | 12dp |  |  |
| md.comp.extended-fab.trailing-space | LENGTH |  | 20dp |  |  |

#### Extended FAB - Color - Tertiary (md.comp.extended-fab.tertiary)

For tertiary color extended FABs.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.extended-fab.tertiary.container.color | COLOR | Enabled | `md.sys.color.tertiary` |  |  |
| md.comp.extended-fab.tertiary.container.elevation | ELEVATION | Enabled | `md.sys.elevation.level3` |  |  |
| md.comp.extended-fab.tertiary.container.shadow-color | COLOR | Enabled | `md.sys.color.shadow` |  |  |
| md.comp.extended-fab.tertiary.label-text.color | COLOR | Enabled | `md.sys.color.on-tertiary` |  |  |
| md.comp.extended-fab.tertiary.icon.color | COLOR | Enabled | `md.sys.color.on-tertiary` |  |  |
| md.comp.extended-fab.tertiary.hovered.container.elevation | ELEVATION | Hovered | `md.sys.elevation.level4` |  |  |
| md.comp.extended-fab.tertiary.hovered.state-layer.color | COLOR | Hovered | `md.sys.color.on-tertiary` |  |  |
| md.comp.extended-fab.tertiary.hovered.state-layer.opacity | OPACITY | Hovered | `md.sys.state.hover.state-layer-opacity` |  |  |
| md.comp.extended-fab.tertiary.hovered.label-text.color | COLOR | Hovered | `md.sys.color.on-tertiary` |  |  |
| md.comp.extended-fab.tertiary.hovered.icon.color | COLOR | Hovered | `md.sys.color.on-tertiary` |  |  |
| md.comp.extended-fab.tertiary.focused.container.elevation | ELEVATION | Focused | `md.sys.elevation.level3` |  |  |
| md.comp.extended-fab.tertiary.focused.state-layer.color | COLOR | Focused | `md.sys.color.on-tertiary` |  |  |
| md.comp.extended-fab.tertiary.focused.state-layer.opacity | OPACITY | Focused | `md.sys.state.focus.state-layer-opacity` |  |  |
| md.comp.extended-fab.tertiary.focused.label-text.color | COLOR | Focused | `md.sys.color.on-tertiary` |  |  |
| md.comp.extended-fab.tertiary.focused.icon.color | COLOR | Focused | `md.sys.color.on-tertiary` |  |  |
| md.comp.extended-fab.tertiary.pressed.container.elevation | ELEVATION | Pressed | `md.sys.elevation.level3` |  |  |
| md.comp.extended-fab.tertiary.pressed.state-layer.color | COLOR | Pressed | `md.sys.color.on-tertiary` |  |  |
| md.comp.extended-fab.tertiary.pressed.state-layer.opacity | OPACITY | Pressed | `md.sys.state.pressed.state-layer-opacity` |  |  |
| md.comp.extended-fab.tertiary.pressed.label-text.color | COLOR | Pressed | `md.sys.color.on-tertiary` |  |  |
| md.comp.extended-fab.tertiary.pressed.icon.color | COLOR | Pressed | `md.sys.color.on-tertiary` |  |  |
| md.comp.extended-fab.tertiary.lowered.container.elevation | ELEVATION | [Deprecated] Enabled / [Deprecated] Container | `md.sys.elevation.level1` |  | Token is deprecated. |
| md.comp.extended-fab.tertiary.container.height | LENGTH | [Deprecated] Enabled / [Deprecated] Container | 56dp |  | Token is deprecated. |
| md.comp.extended-fab.tertiary.container.shape | SHAPE | [Deprecated] Enabled / [Deprecated] Container | `md.sys.shape.corner.large` |  | Token is deprecated. |
| md.comp.extended-fab.tertiary.label-text.font | FONT_NAMES | [Deprecated] Enabled / [Deprecated] Label text | `md.sys.typescale.label-large.font` |  | Token is deprecated. |
| md.comp.extended-fab.tertiary.label-text.line-height | LINE_HEIGHT | [Deprecated] Enabled / [Deprecated] Label text | `md.sys.typescale.label-large.line-height` |  | Token is deprecated. |
| md.comp.extended-fab.tertiary.label-text.size | FONT_SIZE | [Deprecated] Enabled / [Deprecated] Label text | `md.sys.typescale.label-large.size` |  | Token is deprecated. |
| md.comp.extended-fab.tertiary.label-text.tracking | FONT_TRACKING | [Deprecated] Enabled / [Deprecated] Label text | `md.sys.typescale.label-large.tracking` |  | Token is deprecated. |
| md.comp.extended-fab.tertiary.label-text.weight | FONT_WEIGHT | [Deprecated] Enabled / [Deprecated] Label text | `md.sys.typescale.label-large.weight` |  | Token is deprecated. |
| md.comp.extended-fab.tertiary.label-text.type | TYPOGRAPHY | [Deprecated] Enabled / [Deprecated] Label text | font name: `md.comp.extended-fab.tertiary.label-text.font`, font weight: `md.comp.extended-fab.tertiary.label-text.weight`, font size: `md.comp.extended-fab.tertiary.label-text.size`, font tracking: `md.comp.extended-fab.tertiary.label-text.tracking`, line height: `md.comp.extended-fab.tertiary.label-text.line-height` |  | Token is deprecated. |
| md.comp.extended-fab.tertiary.icon.size | LENGTH | [Deprecated] Enabled / [Deprecated] Icon | 24dp |  | Token is deprecated. |
| md.comp.extended-fab.tertiary.hover.container.elevation | ELEVATION | [Deprecated] Hovered / [Deprecated] Container | `md.sys.elevation.level4` |  | Token is deprecated. |
| md.comp.extended-fab.tertiary.lowered.hover.container.elevation | ELEVATION | [Deprecated] Hovered / [Deprecated] Container | `md.sys.elevation.level2` |  | Token is deprecated. |
| md.comp.extended-fab.tertiary.hover.label-text.color | COLOR | [Deprecated] Hovered / [Deprecated] Label text | `md.sys.color.on-tertiary-container` |  | Token is deprecated. |
| md.comp.extended-fab.tertiary.hover.state-layer.color | COLOR | [Deprecated] Hovered / [Deprecated] State layer | `md.sys.color.on-tertiary-container` |  | Token is deprecated. |
| md.comp.extended-fab.tertiary.hover.state-layer.opacity | OPACITY | [Deprecated] Hovered / [Deprecated] State layer | `md.sys.state.hover.state-layer-opacity` |  | Token is deprecated. |
| md.comp.extended-fab.tertiary.hover.icon.color | COLOR | [Deprecated] Hovered / [Deprecated] Icon | `md.sys.color.on-tertiary-container` |  | Token is deprecated. |
| md.comp.extended-fab.tertiary.focus.indicator.color | COLOR | [Deprecated] Focused / [Deprecated] Focus indicator | `md.sys.color.secondary` |  | Token is deprecated. |
| md.comp.extended-fab.tertiary.focus.indicator.thickness | LENGTH | [Deprecated] Focused / [Deprecated] Focus indicator | `md.sys.state.focus-indicator.thickness` |  | Token is deprecated. |
| md.comp.extended-fab.tertiary.focus.indicator.outline.offset | LENGTH | [Deprecated] Focused / [Deprecated] Focus indicator | `md.sys.state.focus-indicator.outer-offset` |  | Token is deprecated. |
| md.comp.extended-fab.tertiary.focus.container.elevation | ELEVATION | [Deprecated] Focused / [Deprecated] Container | `md.sys.elevation.level3` |  | Token is deprecated. |
| md.comp.extended-fab.tertiary.lowered.focus.container.elevation | ELEVATION | [Deprecated] Focused / [Deprecated] Container | `md.sys.elevation.level1` |  | Token is deprecated. |
| md.comp.extended-fab.tertiary.focus.label-text.color | COLOR | [Deprecated] Focused / [Deprecated] Label text | `md.sys.color.on-tertiary-container` |  | Token is deprecated. |
| md.comp.extended-fab.tertiary.focus.state-layer.color | COLOR | [Deprecated] Focused / [Deprecated] State layer | `md.sys.color.on-tertiary-container` |  | Token is deprecated. |
| md.comp.extended-fab.tertiary.focus.state-layer.opacity | OPACITY | [Deprecated] Focused / [Deprecated] State layer | `md.sys.state.focus.state-layer-opacity` |  | Token is deprecated. |
| md.comp.extended-fab.tertiary.focus.icon.color | COLOR | [Deprecated] Focused / [Deprecated] Icon | `md.sys.color.on-tertiary-container` |  | Token is deprecated. |
| md.comp.extended-fab.tertiary.lowered.pressed.container.elevation | ELEVATION | [Deprecated] Pressed (ripple) / [Deprecated] Container | `md.sys.elevation.level1` |  | Token is deprecated. |

#### Extended FAB - Color - Surface (md.comp.extended-fab.surface)

For surface color extended FABs. Not recommended with expressive extended FABs.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.extended-fab.surface.container.color | COLOR | Enabled / Container | `md.sys.color.surface-container-high` |  |  |
| md.comp.extended-fab.surface.lowered.container.color | COLOR | Enabled / Container | `md.sys.color.surface-container-low` |  |  |
| md.comp.extended-fab.surface.container.shape | SHAPE | Enabled / Container | `md.sys.shape.corner.large` |  |  |
| md.comp.extended-fab.surface.container.height | LENGTH | Enabled / Container | 56dp |  |  |
| md.comp.extended-fab.surface.container.elevation | ELEVATION | Enabled / Container | `md.sys.elevation.level3` |  |  |
| md.comp.extended-fab.surface.container.surface-tint-layer.color | COLOR | Enabled / Container | `md.sys.color.surface-tint` |  | Deprecated as part of the update from opacity based surfaces to tonal surfaces. Surfaces no longer use surface-tint layers for tinting, please use the desired surface role directly as the container color. |
| md.comp.extended-fab.surface.container.shadow-color | COLOR | Enabled / Container | `md.sys.color.shadow` |  |  |
| md.comp.extended-fab.surface.lowered.container.elevation | ELEVATION | Enabled / Container | `md.sys.elevation.level1` |  |  |
| md.comp.extended-fab.surface.label-text.font | FONT_NAMES | Enabled / Label text | `md.sys.typescale.label-large.font` |  |  |
| md.comp.extended-fab.surface.label-text.line-height | LINE_HEIGHT | Enabled / Label text | `md.sys.typescale.label-large.line-height` |  |  |
| md.comp.extended-fab.surface.label-text.size | FONT_SIZE | Enabled / Label text | `md.sys.typescale.label-large.size` |  |  |
| md.comp.extended-fab.surface.label-text.weight | FONT_WEIGHT | Enabled / Label text | `md.sys.typescale.label-large.weight` |  |  |
| md.comp.extended-fab.surface.label-text.tracking | FONT_TRACKING | Enabled / Label text | `md.sys.typescale.label-large.tracking` |  |  |
| md.comp.extended-fab.surface.label-text.type | TYPOGRAPHY | Enabled / Label text | font name: `md.comp.extended-fab.surface.label-text.font`, font weight: `md.comp.extended-fab.surface.label-text.weight`, font size: `md.comp.extended-fab.surface.label-text.size`, font tracking: `md.comp.extended-fab.surface.label-text.tracking`, line height: `md.comp.extended-fab.surface.label-text.line-height` |  |  |
| md.comp.extended-fab.surface.label-text.color | COLOR | Enabled / Label text | `md.sys.color.primary` |  |  |
| md.comp.extended-fab.surface.icon.size | LENGTH | Enabled / Icon | 24dp |  |  |
| md.comp.extended-fab.surface.icon.color | COLOR | Enabled / Icon | `md.sys.color.primary` |  |  |
| md.comp.extended-fab.surface.hover.container.elevation | ELEVATION | Hovered / Container | `md.sys.elevation.level4` |  |  |
| md.comp.extended-fab.surface.lowered.hover.container.elevation | ELEVATION | Hovered / Container | `md.sys.elevation.level2` |  |  |
| md.comp.extended-fab.surface.hover.label-text.color | COLOR | Hovered / Label text | `md.sys.color.primary` |  |  |
| md.comp.extended-fab.surface.hover.state-layer.color | COLOR | Hovered / State layer | `md.sys.color.primary` |  |  |
| md.comp.extended-fab.surface.hover.state-layer.opacity | OPACITY | Hovered / State layer | `md.sys.state.hover.state-layer-opacity` |  |  |
| md.comp.extended-fab.surface.hover.icon.color | COLOR | Hovered / Icon | `md.sys.color.primary` |  |  |
| md.comp.extended-fab.surface.focus.indicator.color | COLOR | Focused / Focus indicator | `md.sys.color.secondary` |  |  |
| md.comp.extended-fab.surface.focus.indicator.thickness | LENGTH | Focused / Focus indicator | `md.sys.state.focus-indicator.thickness` |  |  |
| md.comp.extended-fab.surface.focus.indicator.outline.offset | LENGTH | Focused / Focus indicator | `md.sys.state.focus-indicator.outer-offset` |  |  |
| md.comp.extended-fab.surface.focus.container.elevation | ELEVATION | Focused / Container | `md.sys.elevation.level3` |  |  |
| md.comp.extended-fab.surface.lowered.focus.container.elevation | ELEVATION | Focused / Container | `md.sys.elevation.level1` |  |  |
| md.comp.extended-fab.surface.focus.label-text.color | COLOR | Focused / Label text | `md.sys.color.primary` |  |  |
| md.comp.extended-fab.surface.focus.state-layer.color | COLOR | Focused / State layer | `md.sys.color.primary` |  |  |
| md.comp.extended-fab.surface.focus.state-layer.opacity | OPACITY | Focused / State layer | `md.sys.state.focus.state-layer-opacity` |  |  |
| md.comp.extended-fab.surface.focus.icon.color | COLOR | Focused / Icon | `md.sys.color.primary` |  |  |
| md.comp.extended-fab.surface.pressed.container.elevation | ELEVATION | Pressed (ripple) / Container | `md.sys.elevation.level3` |  |  |
| md.comp.extended-fab.surface.lowered.pressed.container.elevation | ELEVATION | Pressed (ripple) / Container | `md.sys.elevation.level1` |  |  |
| md.comp.extended-fab.surface.pressed.label-text.color | COLOR | Pressed (ripple) / Label text | `md.sys.color.primary` |  |  |
| md.comp.extended-fab.surface.pressed.state-layer.color | COLOR | Pressed (ripple) / State layer | `md.sys.color.primary` |  |  |
| md.comp.extended-fab.surface.pressed.state-layer.opacity | OPACITY | Pressed (ripple) / State layer | `md.sys.state.pressed.state-layer-opacity` |  |  |
| md.comp.extended-fab.surface.pressed.icon.color | COLOR | Pressed (ripple) / Icon | `md.sys.color.primary` |  |  |

#### Extended FAB - Color - Secondary (md.comp.extended-fab.secondary)

For secondary color extended FABs.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.extended-fab.secondary.container.color | COLOR | Enabled | `md.sys.color.secondary` |  |  |
| md.comp.extended-fab.secondary.container.elevation | ELEVATION | Enabled | `md.sys.elevation.level3` |  |  |
| md.comp.extended-fab.secondary.container.shadow-color | COLOR | Enabled | `md.sys.color.shadow` |  |  |
| md.comp.extended-fab.secondary.label-text.color | COLOR | Enabled | `md.sys.color.on-secondary` |  |  |
| md.comp.extended-fab.secondary.icon.color | COLOR | Enabled | `md.sys.color.on-secondary` |  |  |
| md.comp.extended-fab.secondary.hovered.container.elevation | ELEVATION | Hovered | `md.sys.elevation.level4` |  |  |
| md.comp.extended-fab.secondary.hovered.state-layer.color | COLOR | Hovered | `md.sys.color.on-secondary` |  |  |
| md.comp.extended-fab.secondary.hovered.state-layer.opacity | OPACITY | Hovered | `md.sys.state.hover.state-layer-opacity` |  |  |
| md.comp.extended-fab.secondary.hovered.label-text.color | COLOR | Hovered | `md.sys.color.on-secondary` |  |  |
| md.comp.extended-fab.secondary.hovered.icon.color | COLOR | Hovered | `md.sys.color.on-secondary` |  |  |
| md.comp.extended-fab.secondary.focused.container.elevation | ELEVATION | Focused | `md.sys.elevation.level3` |  |  |
| md.comp.extended-fab.secondary.focused.state-layer.color | COLOR | Focused | `md.sys.color.on-secondary` |  |  |
| md.comp.extended-fab.secondary.focused.state-layer.opacity | OPACITY | Focused | `md.sys.state.focus.state-layer-opacity` |  |  |
| md.comp.extended-fab.secondary.focused.label-text.color | COLOR | Focused | `md.sys.color.on-secondary` |  |  |
| md.comp.extended-fab.secondary.focused.icon.color | COLOR | Focused | `md.sys.color.on-secondary` |  |  |
| md.comp.extended-fab.secondary.pressed.container.elevation | ELEVATION | Pressed | `md.sys.elevation.level3` |  |  |
| md.comp.extended-fab.secondary.pressed.state-layer.color | COLOR | Pressed | `md.sys.color.on-secondary` |  |  |
| md.comp.extended-fab.secondary.pressed.state-layer.opacity | OPACITY | Pressed | `md.sys.state.pressed.state-layer-opacity` |  |  |
| md.comp.extended-fab.secondary.pressed.label-text.color | COLOR | Pressed | `md.sys.color.on-secondary` |  |  |
| md.comp.extended-fab.secondary.pressed.icon.color | COLOR | Pressed | `md.sys.color.on-secondary` |  |  |
| md.comp.extended-fab.secondary.container.shape | SHAPE | [Deprecated] Enabled / [Deprecated] Container | `md.sys.shape.corner.large` |  | Token is deprecated. |
| md.comp.extended-fab.secondary.container.height | LENGTH | [Deprecated] Enabled / [Deprecated] Container | 56dp |  | Token is deprecated. |
| md.comp.extended-fab.secondary.lowered.container.elevation | ELEVATION | [Deprecated] Enabled / [Deprecated] Container | `md.sys.elevation.level1` |  | Token is deprecated. |
| md.comp.extended-fab.secondary.label-text.font | FONT_NAMES | [Deprecated] Enabled / [Deprecated] Label text | `md.sys.typescale.label-large.font` |  | Token is deprecated. |
| md.comp.extended-fab.secondary.label-text.line-height | LINE_HEIGHT | [Deprecated] Enabled / [Deprecated] Label text | `md.sys.typescale.label-large.line-height` |  | Token is deprecated. |
| md.comp.extended-fab.secondary.label-text.size | FONT_SIZE | [Deprecated] Enabled / [Deprecated] Label text | `md.sys.typescale.label-large.size` |  | Token is deprecated. |
| md.comp.extended-fab.secondary.label-text.weight | FONT_WEIGHT | [Deprecated] Enabled / [Deprecated] Label text | `md.sys.typescale.label-large.weight` |  | Token is deprecated. |
| md.comp.extended-fab.secondary.label-text.tracking | FONT_TRACKING | [Deprecated] Enabled / [Deprecated] Label text | `md.sys.typescale.label-large.tracking` |  | Token is deprecated. |
| md.comp.extended-fab.secondary.label-text.type | TYPOGRAPHY | [Deprecated] Enabled / [Deprecated] Label text | font name: `md.comp.extended-fab.secondary.label-text.font`, font weight: `md.comp.extended-fab.secondary.label-text.weight`, font size: `md.comp.extended-fab.secondary.label-text.size`, font tracking: `md.comp.extended-fab.secondary.label-text.tracking`, line height: `md.comp.extended-fab.secondary.label-text.line-height` |  | Token is deprecated. |
| md.comp.extended-fab.secondary.icon.size | LENGTH | [Deprecated] Enabled / [Deprecated] Icon | 24dp |  | Token is deprecated. |
| md.comp.extended-fab.secondary.hover.container.elevation | ELEVATION | [Deprecated] Hovered / [Deprecated] Container | `md.sys.elevation.level4` |  | Token is deprecated. |
| md.comp.extended-fab.secondary.lowered.hover.container.elevation | ELEVATION | [Deprecated] Hovered / [Deprecated] Container | `md.sys.elevation.level2` |  | Token is deprecated. |
| md.comp.extended-fab.secondary.hover.label-text.color | COLOR | [Deprecated] Hovered / [Deprecated] Label text | `md.sys.color.on-secondary-container` |  | Token is deprecated. |
| md.comp.extended-fab.secondary.hover.state-layer.color | COLOR | [Deprecated] Hovered / [Deprecated] State layer | `md.sys.color.on-secondary-container` |  | Token is deprecated. |
| md.comp.extended-fab.secondary.hover.state-layer.opacity | OPACITY | [Deprecated] Hovered / [Deprecated] State layer | `md.sys.state.hover.state-layer-opacity` |  | Token is deprecated. |
| md.comp.extended-fab.secondary.hover.icon.color | COLOR | [Deprecated] Hovered / [Deprecated] Icon | `md.sys.color.on-secondary-container` |  | Token is deprecated. |
| md.comp.extended-fab.secondary.focus.indicator.color | COLOR | [Deprecated] Focused / [Deprecated] Focus indicator | `md.sys.color.secondary` |  | Token is deprecated. |
| md.comp.extended-fab.secondary.focus.indicator.thickness | LENGTH | [Deprecated] Focused / [Deprecated] Focus indicator | `md.sys.state.focus-indicator.thickness` |  | Token is deprecated. |
| md.comp.extended-fab.secondary.focus.indicator.outline.offset | LENGTH | [Deprecated] Focused / [Deprecated] Focus indicator | `md.sys.state.focus-indicator.outer-offset` |  | Token is deprecated. |
| md.comp.extended-fab.secondary.focus.container.elevation | ELEVATION | [Deprecated] Focused / [Deprecated] Container | `md.sys.elevation.level3` |  | Token is deprecated. |
| md.comp.extended-fab.secondary.lowered.focus.container.elevation | ELEVATION | [Deprecated] Focused / [Deprecated] Container | `md.sys.elevation.level1` |  | Token is deprecated. |
| md.comp.extended-fab.secondary.focus.label-text.color | COLOR | [Deprecated] Focused / [Deprecated] Label text | `md.sys.color.on-secondary-container` |  | Token is deprecated. |
| md.comp.extended-fab.secondary.focus.state-layer.color | COLOR | [Deprecated] Focused / [Deprecated] State layer | `md.sys.color.on-secondary-container` |  | Token is deprecated. |
| md.comp.extended-fab.secondary.focus.state-layer.opacity | OPACITY | [Deprecated] Focused / [Deprecated] State layer | `md.sys.state.focus.state-layer-opacity` |  | Token is deprecated. |
| md.comp.extended-fab.secondary.focus.icon.color | COLOR | [Deprecated] Focused / [Deprecated] Icon | `md.sys.color.on-secondary-container` |  | Token is deprecated. |
| md.comp.extended-fab.secondary.lowered.pressed.container.elevation | ELEVATION | [Deprecated] Pressed (ripple) / [Deprecated] Container | `md.sys.elevation.level1` |  | Token is deprecated. |

#### Extended FAB - Color - Branded (md.comp.extended-fab.branded)

For branded color extended FABs. Not recommended with expressive extended FABs.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.extended-fab.branded.container.color | COLOR | Enabled / Container | `md.sys.color.surface-container-high` |  |  |
| md.comp.extended-fab.branded.lowered.container.color | COLOR | Enabled / Container | `md.sys.color.surface-container-low` |  |  |
| md.comp.extended-fab.branded.container.shape | SHAPE | Enabled / Container | `md.sys.shape.corner.large` |  |  |
| md.comp.extended-fab.branded.container.height | LENGTH | Enabled / Container | 56dp |  |  |
| md.comp.extended-fab.branded.container.elevation | ELEVATION | Enabled / Container | `md.sys.elevation.level3` |  |  |
| md.comp.extended-fab.branded.container.surface-tint-layer.color | COLOR | Enabled / Container | `md.sys.color.surface-tint` |  | Deprecated as part of the update from opacity based surfaces to tonal surfaces. Surfaces no longer use surface-tint layers for tinting, please use the desired surface role directly as the container color. |
| md.comp.extended-fab.branded.container.shadow-color | COLOR | Enabled / Container | `md.sys.color.shadow` |  |  |
| md.comp.extended-fab.branded.lowered.container.elevation | ELEVATION | Enabled / Container | `md.sys.elevation.level1` |  |  |
| md.comp.extended-fab.branded.label-text.font | FONT_NAMES | Enabled / Label text | `md.sys.typescale.label-large.font` |  |  |
| md.comp.extended-fab.branded.label-text.line-height | LINE_HEIGHT | Enabled / Label text | `md.sys.typescale.label-large.line-height` |  |  |
| md.comp.extended-fab.branded.label-text.size | FONT_SIZE | Enabled / Label text | `md.sys.typescale.label-large.size` |  |  |
| md.comp.extended-fab.branded.label-text.weight | FONT_WEIGHT | Enabled / Label text | `md.sys.typescale.label-large.weight` |  |  |
| md.comp.extended-fab.branded.label-text.tracking | FONT_TRACKING | Enabled / Label text | `md.sys.typescale.label-large.tracking` |  |  |
| md.comp.extended-fab.branded.label-text.type | TYPOGRAPHY | Enabled / Label text | font name: `md.comp.extended-fab.branded.label-text.font`, font weight: `md.comp.extended-fab.branded.label-text.weight`, font size: `md.comp.extended-fab.branded.label-text.size`, font tracking: `md.comp.extended-fab.branded.label-text.tracking`, line height: `md.comp.extended-fab.branded.label-text.line-height` |  |  |
| md.comp.extended-fab.branded.label-text.color | COLOR | Enabled / Label text | `md.sys.color.on-surface` |  |  |
| md.comp.extended-fab.branded.icon.size | LENGTH | Enabled / Icon | 36dp |  |  |
| md.comp.extended-fab.branded.hover.container.elevation | ELEVATION | Hovered / Container | `md.sys.elevation.level4` |  |  |
| md.comp.extended-fab.branded.lowered.hover.container.elevation | ELEVATION | Hovered / Container | `md.sys.elevation.level2` |  |  |
| md.comp.extended-fab.branded.hover.label-text.color | COLOR | Hovered / Label text | `md.sys.color.primary` |  |  |
| md.comp.extended-fab.branded.hover.state-layer.color | COLOR | Hovered / State layer | `md.sys.color.primary` |  |  |
| md.comp.extended-fab.branded.hover.state-layer.opacity | OPACITY | Hovered / State layer | `md.sys.state.hover.state-layer-opacity` |  |  |
| md.comp.extended-fab.branded.focus.indicator.color | COLOR | Focused / Focus indicator | `md.sys.color.secondary` |  |  |
| md.comp.extended-fab.branded.focus.indicator.thickness | LENGTH | Focused / Focus indicator | `md.sys.state.focus-indicator.thickness` |  |  |
| md.comp.extended-fab.branded.focus.indicator.outline.offset | LENGTH | Focused / Focus indicator | `md.sys.state.focus-indicator.outer-offset` |  |  |
| md.comp.extended-fab.branded.focus.container.elevation | ELEVATION | Focused / Container | `md.sys.elevation.level3` |  |  |
| md.comp.extended-fab.branded.lowered.focus.container.elevation | ELEVATION | Focused / Container | `md.sys.elevation.level1` |  |  |
| md.comp.extended-fab.branded.focus.label-text.color | COLOR | Focused / Label text | `md.sys.color.primary` |  |  |
| md.comp.extended-fab.branded.focus.state-layer.color | COLOR | Focused / State layer | `md.sys.color.primary` |  |  |
| md.comp.extended-fab.branded.focus.state-layer.opacity | OPACITY | Focused / State layer | `md.sys.state.focus.state-layer-opacity` |  |  |
| md.comp.extended-fab.branded.pressed.container.elevation | ELEVATION | Pressed (ripple) / Container | `md.sys.elevation.level3` |  |  |
| md.comp.extended-fab.branded.lowered.pressed.container.elevation | ELEVATION | Pressed (ripple) / Container | `md.sys.elevation.level1` |  |  |
| md.comp.extended-fab.branded.pressed.label-text.color | COLOR | Pressed (ripple) / Label text | `md.sys.color.primary` |  |  |
| md.comp.extended-fab.branded.pressed.state-layer.color | COLOR | Pressed (ripple) / State layer | `md.sys.color.primary` |  |  |
| md.comp.extended-fab.branded.pressed.state-layer.opacity | OPACITY | Pressed (ripple) / State layer | `md.sys.state.pressed.state-layer-opacity` |  |  |

### Baseline colors

Color values are implemented through design tokens. For design, this means working with color values that correspond with tokens. For implementation, a color value will be a token that references a value. [Learn more about design tokens](https://m3.material.io/m3/pages/design-tokens/overview/)

![3 baseline extended FAB color roles.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fma3rmvmo-11-3P.png?alt=media&token=d7936f8b-e675-46ad-96c2-5ad14fd729cb>)

_Extended FAB color roles used for light and dark schemes: Primary container + shadow; On primary container; On primary container_

#### Additional color mappings

Extended FABs can use other combinations of container and icon colors. The color mappings below provide the same legibility and functionality as the default, so the color mapping you use depends on style alone.

![3 deprecated extended FABs with different container and icon colors.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fma3rnofz-12-3P.png?alt=media&token=89b9a842-01e1-4dad-a525-edee128e9051>)

_Extended FABs can use different combinations of container and icon colors_

### Baseline states

States are visual representations used to communicate the status of a component or interactive element. [Learn more about interaction states](https://m3.material.io/m3/pages/interaction-states)

![4 states of baseline extended FAB.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmaljrky7-13-3P.png?alt=media&token=b2fa64b8-d9c7-42df-a65a-91b7b880aed6>)

_Enabled; Hovered; Focused; Pressed_

### Baseline measurements

![Margins of baseline extended FAB.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmaebvm3w-14-3P.png?alt=media&token=f68926a0-c6a7-44c6-9c70-5c603af855ac>)

_Extended FABs have a padding of 16dp_

![Size of baseline extended FAB while on screen.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmaebvv3o-15-3P.png?alt=media&token=c125c6fa-e299-426a-98d4-19e12ef62f0d>)

_Extended FAB height, width, and icon size_

| Attribute | Value |
| --- | --- |
| Container height | 56dp |
| Container width | Dynamic, 80dp min |
| Container shape | 16dp corner radius |
| Icon size | 24dp |
| Padding | 16dp |

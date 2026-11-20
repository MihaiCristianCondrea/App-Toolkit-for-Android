---
title: "FABs"
source_url: "https://m3.material.io/components/floating-action-button/specs"
lastmod: "2026-05-18"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "components"
assets:
  - "assets/images/decf24b70bcc-mkc6atia-01-3P.png"
  - "assets/images/49dcc6263da3-mkc6bkq8-02-3P.png"
  - "assets/images/5458bfd670a3-mkc6cml8-03-3P.png"
  - "assets/images/4806b1dfdb15-mkc6diiz-04-3P.png"
  - "assets/images/4f98d554e048-mkc6empp-05-3P.png"
  - "assets/images/2729476e3f5e-mkc6fdu1-06-3P.png"
  - "assets/images/882daea4d3ad-mkc6fz4n-07-3P.png"
  - "assets/images/22ca7c028dfd-mkc6gdhg-08-3P.png"
  - "assets/images/42928beea31d-mkc6gu0p-09-3P.png"
  - "assets/images/aa316fc00983-mkc6ha1z-10-3P.png"
  - "assets/images/5784a40b9f16-mkc6hrgc-11-3P.png"
  - "assets/images/7f09b3257bd7-mkc6i508-12-3P.png"
---

# FABs

## Specs

## Variants

![An icon on the container of a FAB, medium FAB, and large FAB.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmkc6atia-01-3P.png?alt=media&token=2f73681d-69d8-4486-a35b-e3e28c1eaa41>)

_FAB; Medium FAB; Large FAB_

### Baseline variants

The small FAB is still available, but no longer recommended. [Jump to baseline specs](specs.md#cd336045-e97d-4a6d-ac23-f778fa695e3c)

![An icon on the container of a small FAB.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmkc6bkq8-02-3P.png?alt=media&token=00146424-866a-4818-b7a1-f65a6214d47e>)

_1. Small FAB_

| Variant | M3 | M3 Expressive |
| --- | --- | --- |
| FAB | Available | Available |
| Medium FAB | \-- | Available |
| Large FAB | Available | Available |
| Small FAB | Available | Not recommended. Use a larger size. |

## Configurations

In the expressive update, the **primary**, **secondary**, and **tertiary** set colors were renamed to **primary container**, **secondary container**, and **tertiary container** to match the actual color roles used. New primary, secondary, and tertiary color styles were created to match the corresponding color roles. [View details in the color styles section](specs.md#67e71ec7-b520-405a-aa06-2decfa0b92a3)

| Category | Configuration | M3 | M3 Expressive |
| --- | --- | --- | --- |
| Color | Primary container, secondary container, tertiary container | Available as primary, secondary, tertiary | Available |
| Primary. secondary, tertiary | \-- | Available |  |

## Tokens & specs

Use the table's menu to select a token set. FAB tokens are organized by size and color. [Learn more about design tokens](https://m3.material.io/m3/pages/design-tokens/overview/)

### TOKEN_TABLE

Component: Floating action buttons (FABs)

Configured context tags: None, Medium contrast, Default, High contrast, 3P, Android, Dark, Light

#### Token sets

| Token set | Name | Type | Tokens | Description |
| --- | --- | --- | --- | --- |
| FAB - Size - Regular | md.comp.fab | COMPONENT | 4 | For the default, regular FAB size. |
| FAB - Size - Medium | md.comp.fab.medium | COMPONENT | 4 | For medium FABs. |
| FAB - Size - Large | md.comp.fab.large | COMPONENT | 4 | For large FABs. |
| FAB - Color - Tonal primary | md.comp.fab.primary-container | COMPONENT | 16 | For tonal primary container FABs. |
| FAB - Color - Tonal secondary | md.comp.fab.secondary-container | COMPONENT | 16 | For tonal secondary container FABs. |
| FAB - Color - Tonal tertiary | md.comp.fab.tertiary-container | COMPONENT | 16 | For tonal tertiary container FABs. |
| FAB - Color - Primary | md.comp.fab.primary | COMPONENT | 35 | For primary color FABs. |
| FAB - Color - Secondary | md.comp.fab.secondary | COMPONENT | 35 | For secondary color FABs. |
| FAB - Color - Tertiary | md.comp.fab.tertiary | COMPONENT | 35 | For the tertiary style fab. |

#### FAB - Size - Regular (md.comp.fab)

For the default, regular FAB size.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.fab.container.height | LENGTH |  | 56dp |  |  |
| md.comp.fab.container.width | LENGTH |  | 56dp |  |  |
| md.comp.fab.icon.size | LENGTH |  | 24dp |  |  |
| md.comp.fab.container.shape | SHAPE |  | `md.sys.shape.corner.large` |  |  |

#### FAB - Size - Medium (md.comp.fab.medium)

For medium FABs.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.fab.medium.container.height | LENGTH |  | 80dp |  |  |
| md.comp.fab.medium.container.width | LENGTH |  | 80dp |  |  |
| md.comp.fab.medium.icon.size | LENGTH |  | 28dp |  |  |
| md.comp.fab.medium.container.shape | SHAPE |  | `md.sys.shape.corner.large-increased` |  |  |

#### FAB - Size - Large (md.comp.fab.large)

For large FABs.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.fab.large.container.height | LENGTH |  | 96dp |  |  |
| md.comp.fab.large.container.width | LENGTH |  | 96dp |  |  |
| md.comp.fab.large.icon.size | LENGTH |  | 36dp |  |  |
| md.comp.fab.large.container.shape | SHAPE |  | `md.sys.shape.corner.extra-large` |  |  |

#### FAB - Color - Tonal primary (md.comp.fab.primary-container)

For tonal primary container FABs.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.fab.primary-container.container.color | COLOR | Enabled | `md.sys.color.primary-container` |  |  |
| md.comp.fab.primary-container.container.shadow-color | COLOR | Enabled | `md.sys.color.shadow` |  |  |
| md.comp.fab.primary-container.container.elevation | ELEVATION | Enabled | `md.sys.elevation.level3` |  |  |
| md.comp.fab.primary-container.icon.color | COLOR | Enabled | `md.sys.color.on-primary-container` |  |  |
| md.comp.fab.primary-container.hovered.container.elevation | ELEVATION | Hovered | `md.sys.elevation.level4` |  |  |
| md.comp.fab.primary-container.hovered.state-layer.color | COLOR | Hovered | `md.sys.color.on-primary-container` |  |  |
| md.comp.fab.primary-container.hovered.state-layer.opacity | OPACITY | Hovered | `md.sys.state.hover.state-layer-opacity` |  |  |
| md.comp.fab.primary-container.hovered.icon.color | COLOR | Hovered | `md.sys.color.on-primary-container` |  |  |
| md.comp.fab.primary-container.focused.container.elevation | ELEVATION | Focused | `md.sys.elevation.level3` |  |  |
| md.comp.fab.primary-container.focused.state-layer.color | COLOR | Focused | `md.sys.color.on-primary-container` |  |  |
| md.comp.fab.primary-container.focused.state-layer.opacity | OPACITY | Focused | `md.sys.state.focus.state-layer-opacity` |  |  |
| md.comp.fab.primary-container.focused.icon.color | COLOR | Focused | `md.sys.color.on-primary-container` |  |  |
| md.comp.fab.primary-container.pressed.container.elevation | ELEVATION | Pressed | `md.sys.elevation.level3` |  |  |
| md.comp.fab.primary-container.pressed.state-layer.color | COLOR | Pressed | `md.sys.color.on-primary-container` |  |  |
| md.comp.fab.primary-container.pressed.state-layer.opacity | OPACITY | Pressed | `md.sys.state.pressed.state-layer-opacity` |  |  |
| md.comp.fab.primary-container.pressed.icon.color | COLOR | Pressed | `md.sys.color.on-primary-container` |  |  |

#### FAB - Color - Tonal secondary (md.comp.fab.secondary-container)

For tonal secondary container FABs.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.fab.secondary-container.container.color | COLOR | Enabled | `md.sys.color.secondary-container` |  |  |
| md.comp.fab.secondary-container.container.shadow-color | COLOR | Enabled | `md.sys.color.shadow` |  |  |
| md.comp.fab.secondary-container.container.elevation | ELEVATION | Enabled | `md.sys.elevation.level3` |  |  |
| md.comp.fab.secondary-container.icon.color | COLOR | Enabled | `md.sys.color.on-secondary-container` |  |  |
| md.comp.fab.secondary-container.hovered.container.elevation | ELEVATION | Hovered | `md.sys.elevation.level4` |  |  |
| md.comp.fab.secondary-container.hovered.state-layer.color | COLOR | Hovered | `md.sys.color.on-secondary-container` |  |  |
| md.comp.fab.secondary-container.hovered.state-layer.opacity | OPACITY | Hovered | `md.sys.state.hover.state-layer-opacity` |  |  |
| md.comp.fab.secondary-container.hovered.icon.color | COLOR | Hovered | `md.sys.color.on-secondary-container` |  |  |
| md.comp.fab.secondary-container.focused.container.elevation | ELEVATION | Focused | `md.sys.elevation.level3` |  |  |
| md.comp.fab.secondary-container.focused.state-layer.color | COLOR | Focused | `md.sys.color.on-secondary-container` |  |  |
| md.comp.fab.secondary-container.focused.state-layer.opacity | OPACITY | Focused | `md.sys.state.focus.state-layer-opacity` |  |  |
| md.comp.fab.secondary-container.focused.icon.color | COLOR | Focused | `md.sys.color.on-secondary-container` |  |  |
| md.comp.fab.secondary-container.pressed.container.elevation | ELEVATION | Pressed | `md.sys.elevation.level3` |  |  |
| md.comp.fab.secondary-container.pressed.state-layer.color | COLOR | Pressed | `md.sys.color.on-secondary-container` |  |  |
| md.comp.fab.secondary-container.pressed.state-layer.opacity | OPACITY | Pressed | `md.sys.state.pressed.state-layer-opacity` |  |  |
| md.comp.fab.secondary-container.pressed.icon.color | COLOR | Pressed | `md.sys.color.on-secondary-container` |  |  |

#### FAB - Color - Tonal tertiary (md.comp.fab.tertiary-container)

For tonal tertiary container FABs.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.fab.tertiary-container.container.color | COLOR | Enabled | `md.sys.color.tertiary-container` |  |  |
| md.comp.fab.tertiary-container.container.shadow-color | COLOR | Enabled | `md.sys.color.shadow` |  |  |
| md.comp.fab.tertiary-container.container.elevation | ELEVATION | Enabled | `md.sys.elevation.level3` |  |  |
| md.comp.fab.tertiary-container.icon.color | COLOR | Enabled | `md.sys.color.on-tertiary-container` |  |  |
| md.comp.fab.tertiary-container.hovered.container.elevation | ELEVATION | Hovered | `md.sys.elevation.level4` |  |  |
| md.comp.fab.tertiary-container.hovered.state-layer.color | COLOR | Hovered | `md.sys.color.on-tertiary-container` |  |  |
| md.comp.fab.tertiary-container.hovered.state-layer.opacity | OPACITY | Hovered | `md.sys.state.hover.state-layer-opacity` |  |  |
| md.comp.fab.tertiary-container.hovered.icon.color | COLOR | Hovered | `md.sys.color.on-tertiary-container` |  |  |
| md.comp.fab.tertiary-container.focused.container.elevation | ELEVATION | Focused | `md.sys.elevation.level3` |  |  |
| md.comp.fab.tertiary-container.focused.state-layer.color | COLOR | Focused | `md.sys.color.on-tertiary-container` |  |  |
| md.comp.fab.tertiary-container.focused.state-layer.opacity | OPACITY | Focused | `md.sys.state.focus.state-layer-opacity` |  |  |
| md.comp.fab.tertiary-container.focused.icon.color | COLOR | Focused | `md.sys.color.on-tertiary-container` |  |  |
| md.comp.fab.tertiary-container.pressed.container.elevation | ELEVATION | Pressed | `md.sys.elevation.level3` |  |  |
| md.comp.fab.tertiary-container.pressed.state-layer.color | COLOR | Pressed | `md.sys.color.on-tertiary-container` |  |  |
| md.comp.fab.tertiary-container.pressed.state-layer.opacity | OPACITY | Pressed | `md.sys.state.pressed.state-layer-opacity` |  |  |
| md.comp.fab.tertiary-container.pressed.icon.color | COLOR | Pressed | `md.sys.color.on-tertiary-container` |  |  |

#### FAB - Color - Primary (md.comp.fab.primary)

For primary color FABs.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.fab.primary.container.color | COLOR | Enabled | `md.sys.color.primary` |  |  |
| md.comp.fab.primary.container.shadow-color | COLOR | Enabled | `md.sys.color.shadow` |  |  |
| md.comp.fab.primary.container.elevation | ELEVATION | Enabled | `md.sys.elevation.level3` |  |  |
| md.comp.fab.primary.icon.color | COLOR | Enabled | `md.sys.color.on-primary` |  |  |
| md.comp.fab.primary.hovered.container.elevation | ELEVATION | Hovered | `md.sys.elevation.level4` |  |  |
| md.comp.fab.primary.hovered.state-layer.color | COLOR | Hovered | `md.sys.color.on-primary` |  |  |
| md.comp.fab.primary.hovered.state-layer.opacity | OPACITY | Hovered | `md.sys.state.hover.state-layer-opacity` |  |  |
| md.comp.fab.primary.hovered.icon.color | COLOR | Hovered | `md.sys.color.on-primary` |  |  |
| md.comp.fab.primary.focused.container.elevation | ELEVATION | Focused | `md.sys.elevation.level3` |  |  |
| md.comp.fab.primary.focused.state-layer.color | COLOR | Focused | `md.sys.color.on-primary` |  |  |
| md.comp.fab.primary.focused.state-layer.opacity | OPACITY | Focused | `md.sys.state.focus.state-layer-opacity` |  |  |
| md.comp.fab.primary.focused.icon.color | COLOR | Focused | `md.sys.color.on-primary` |  |  |
| md.comp.fab.primary.pressed.container.elevation | ELEVATION | Pressed | `md.sys.elevation.level3` |  |  |
| md.comp.fab.primary.pressed.state-layer.color | COLOR | Pressed | `md.sys.color.on-primary` |  |  |
| md.comp.fab.primary.pressed.state-layer.opacity | OPACITY | Pressed | `md.sys.state.pressed.state-layer-opacity` |  |  |
| md.comp.fab.primary.pressed.icon.color | COLOR | Pressed | `md.sys.color.on-primary` |  |  |
| md.comp.fab.primary.container.shape | SHAPE | [Deprecated] Enabled / [Deprecated] Container | `md.sys.shape.corner.large` |  | Token is deprecated. |
| md.comp.fab.primary.container.width | LENGTH | [Deprecated] Enabled / [Deprecated] Container | 56dp |  | Token is deprecated. |
| md.comp.fab.primary.container.height | LENGTH | [Deprecated] Enabled / [Deprecated] Container | 56dp |  | Token is deprecated. |
| md.comp.fab.primary.lowered.container.elevation | ELEVATION | [Deprecated] Enabled / [Deprecated] Container | `md.sys.elevation.level1` |  | Token is deprecated. |
| md.comp.fab.primary.icon.size | LENGTH | [Deprecated] Enabled / [Deprecated] Icon | 24dp |  | Token is deprecated. |
| md.comp.fab.primary.hover.container.elevation | ELEVATION | [Deprecated] Hovered / [Deprecated] Container | `md.sys.elevation.level4` |  | Token is deprecated. |
| md.comp.fab.primary.lowered.hover.container.elevation | ELEVATION | [Deprecated] Hovered / [Deprecated] Container | `md.sys.elevation.level2` |  | Token is deprecated. |
| md.comp.fab.primary.hover.state-layer.color | COLOR | [Deprecated] Hovered / [Deprecated] State layer | `md.sys.color.on-primary-container` |  | Token is deprecated. |
| md.comp.fab.primary.hover.state-layer.opacity | OPACITY | [Deprecated] Hovered / [Deprecated] State layer | `md.sys.state.hover.state-layer-opacity` |  | Token is deprecated. |
| md.comp.fab.primary.hover.icon.color | COLOR | [Deprecated] Hovered / [Deprecated] Icon | `md.sys.color.on-primary-container` |  | Token is deprecated. |
| md.comp.fab.primary.focus.indicator.color | COLOR | [Deprecated] Focused / [Deprecated] Focus indicator | `md.sys.color.secondary` |  | Token is deprecated. |
| md.comp.fab.primary.focus.indicator.thickness | LENGTH | [Deprecated] Focused / [Deprecated] Focus indicator | `md.sys.state.focus-indicator.thickness` |  | Token is deprecated. |
| md.comp.fab.primary.focus.indicator.outline.offset | LENGTH | [Deprecated] Focused / [Deprecated] Focus indicator | `md.sys.state.focus-indicator.outer-offset` |  | Token is deprecated. |
| md.comp.fab.primary.focus.container.elevation | ELEVATION | [Deprecated] Focused / [Deprecated] Container | `md.sys.elevation.level3` |  | Token is deprecated. |
| md.comp.fab.primary.lowered.focus.container.elevation | ELEVATION | [Deprecated] Focused / [Deprecated] Container | `md.sys.elevation.level1` |  | Token is deprecated. |
| md.comp.fab.primary.focus.state-layer.color | COLOR | [Deprecated] Focused / [Deprecated] State layer | `md.sys.color.on-primary-container` |  | Token is deprecated. |
| md.comp.fab.primary.focus.state-layer.opacity | OPACITY | [Deprecated] Focused / [Deprecated] State layer | `md.sys.state.focus.state-layer-opacity` |  | Token is deprecated. |
| md.comp.fab.primary.focus.icon.color | COLOR | [Deprecated] Focused / [Deprecated] Icon | `md.sys.color.on-primary-container` |  | Token is deprecated. |
| md.comp.fab.primary.lowered.pressed.container.elevation | ELEVATION | [Deprecated] Pressed (ripple) / [Deprecated] Container | `md.sys.elevation.level1` |  | Token is deprecated. |

#### FAB - Color - Secondary (md.comp.fab.secondary)

For secondary color FABs.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.fab.secondary.container.color | COLOR | Enabled | `md.sys.color.secondary` |  |  |
| md.comp.fab.secondary.container.shadow-color | COLOR | Enabled | `md.sys.color.shadow` |  |  |
| md.comp.fab.secondary.container.elevation | ELEVATION | Enabled | `md.sys.elevation.level3` |  |  |
| md.comp.fab.secondary.icon.color | COLOR | Enabled | `md.sys.color.on-secondary` |  |  |
| md.comp.fab.secondary.hovered.container.elevation | ELEVATION | Hovered | `md.sys.elevation.level4` |  |  |
| md.comp.fab.secondary.hovered.state-layer.color | COLOR | Hovered | `md.sys.color.on-secondary` |  |  |
| md.comp.fab.secondary.hovered.state-layer.opacity | OPACITY | Hovered | `md.sys.state.hover.state-layer-opacity` |  |  |
| md.comp.fab.secondary.hovered.icon.color | COLOR | Hovered | `md.sys.color.on-secondary` |  |  |
| md.comp.fab.secondary.focused.container.elevation | ELEVATION | Focused | `md.sys.elevation.level3` |  |  |
| md.comp.fab.secondary.focused.state-layer.color | COLOR | Focused | `md.sys.color.on-secondary` |  |  |
| md.comp.fab.secondary.focused.state-layer.opacity | OPACITY | Focused | `md.sys.state.focus.state-layer-opacity` |  |  |
| md.comp.fab.secondary.focused.icon.color | COLOR | Focused | `md.sys.color.on-secondary` |  |  |
| md.comp.fab.secondary.pressed.container.elevation | ELEVATION | Pressed | `md.sys.elevation.level3` |  |  |
| md.comp.fab.secondary.pressed.state-layer.color | COLOR | Pressed | `md.sys.color.on-secondary` |  |  |
| md.comp.fab.secondary.pressed.state-layer.opacity | OPACITY | Pressed | `md.sys.state.pressed.state-layer-opacity` |  |  |
| md.comp.fab.secondary.pressed.icon.color | COLOR | Pressed | `md.sys.color.on-secondary` |  |  |
| md.comp.fab.secondary.container.shape | SHAPE | [Deprecated] Enabled / [Deprecated] Container | `md.sys.shape.corner.large` |  | Token is deprecated. |
| md.comp.fab.secondary.container.width | LENGTH | [Deprecated] Enabled / [Deprecated] Container | 56dp |  | Token is deprecated. |
| md.comp.fab.secondary.container.height | LENGTH | [Deprecated] Enabled / [Deprecated] Container | 56dp |  | Token is deprecated. |
| md.comp.fab.secondary.lowered.container.elevation | ELEVATION | [Deprecated] Enabled / [Deprecated] Container | `md.sys.elevation.level1` |  | Token is deprecated. |
| md.comp.fab.secondary.icon.size | LENGTH | [Deprecated] Enabled / [Deprecated] Icon | 24dp |  | Token is deprecated. |
| md.comp.fab.secondary.hover.container.elevation | ELEVATION | [Deprecated] Hovered / [Deprecated] Container | `md.sys.elevation.level4` |  | Token is deprecated. |
| md.comp.fab.secondary.lowered.hover.container.elevation | ELEVATION | [Deprecated] Hovered / [Deprecated] Container | `md.sys.elevation.level2` |  | Token is deprecated. |
| md.comp.fab.secondary.hover.state-layer.color | COLOR | [Deprecated] Hovered / [Deprecated] State layer | `md.sys.color.on-secondary-container` |  | Token is deprecated. |
| md.comp.fab.secondary.hover.state-layer.opacity | OPACITY | [Deprecated] Hovered / [Deprecated] State layer | `md.sys.state.hover.state-layer-opacity` |  | Token is deprecated. |
| md.comp.fab.secondary.hover.icon.color | COLOR | [Deprecated] Hovered / [Deprecated] Icon | `md.sys.color.on-secondary-container` |  | Token is deprecated. |
| md.comp.fab.secondary.focus.container.elevation | ELEVATION | [Deprecated] Focused / [Deprecated] Container | `md.sys.elevation.level3` |  | Token is deprecated. |
| md.comp.fab.secondary.lowered.focus.container.elevation | ELEVATION | [Deprecated] Focused / [Deprecated] Container | `md.sys.elevation.level1` |  | Token is deprecated. |
| md.comp.fab.secondary.focus.state-layer.color | COLOR | [Deprecated] Focused / [Deprecated] State layer | `md.sys.color.on-secondary-container` |  | Token is deprecated. |
| md.comp.fab.secondary.focus.state-layer.opacity | OPACITY | [Deprecated] Focused / [Deprecated] State layer | `md.sys.state.focus.state-layer-opacity` |  | Token is deprecated. |
| md.comp.fab.secondary.focus.icon.color | COLOR | [Deprecated] Focused / [Deprecated] Icon | `md.sys.color.on-secondary-container` |  | Token is deprecated. |
| md.comp.fab.secondary.focus.indicator.color | COLOR | [Deprecated] Focused / [Deprecated] Focus indicator | `md.sys.color.secondary` |  | Token is deprecated. |
| md.comp.fab.secondary.focus.indicator.thickness | LENGTH | [Deprecated] Focused / [Deprecated] Focus indicator | `md.sys.state.focus-indicator.thickness` |  | Token is deprecated. |
| md.comp.fab.secondary.focus.indicator.outline.offset | LENGTH | [Deprecated] Focused / [Deprecated] Focus indicator | `md.sys.state.focus-indicator.outer-offset` |  | Token is deprecated. |
| md.comp.fab.secondary.lowered.pressed.container.elevation | ELEVATION | [Deprecated] Pressed (ripple) / [Deprecated] Container | `md.sys.elevation.level1` |  | Token is deprecated. |

#### FAB - Color - Tertiary (md.comp.fab.tertiary)

For the tertiary style fab.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.fab.tertiary.container.color | COLOR | Enabled | `md.sys.color.tertiary` |  |  |
| md.comp.fab.tertiary.container.shadow-color | COLOR | Enabled | `md.sys.color.shadow` |  |  |
| md.comp.fab.tertiary.container.elevation | ELEVATION | Enabled | `md.sys.elevation.level3` |  |  |
| md.comp.fab.tertiary.icon.color | COLOR | Enabled | `md.sys.color.on-tertiary` |  |  |
| md.comp.fab.tertiary.hovered.container.elevation | ELEVATION | Hovered | `md.sys.elevation.level4` |  |  |
| md.comp.fab.tertiary.hovered.state-layer.color | COLOR | Hovered | `md.sys.color.on-tertiary` |  |  |
| md.comp.fab.tertiary.hovered.state-layer.opacity | OPACITY | Hovered | `md.sys.state.hover.state-layer-opacity` |  |  |
| md.comp.fab.tertiary.hovered.icon.color | COLOR | Hovered | `md.sys.color.on-tertiary` |  |  |
| md.comp.fab.tertiary.focused.container.elevation | ELEVATION | Focused | `md.sys.elevation.level3` |  |  |
| md.comp.fab.tertiary.focused.state-layer.color | COLOR | Focused | `md.sys.color.on-tertiary` |  |  |
| md.comp.fab.tertiary.focused.state-layer.opacity | OPACITY | Focused | `md.sys.state.focus.state-layer-opacity` |  |  |
| md.comp.fab.tertiary.focused.icon.color | COLOR | Focused | `md.sys.color.on-tertiary` |  |  |
| md.comp.fab.tertiary.pressed.container.elevation | ELEVATION | Pressed | `md.sys.elevation.level3` |  |  |
| md.comp.fab.tertiary.pressed.state-layer.color | COLOR | Pressed | `md.sys.color.on-tertiary` |  |  |
| md.comp.fab.tertiary.pressed.state-layer.opacity | OPACITY | Pressed | `md.sys.state.pressed.state-layer-opacity` |  |  |
| md.comp.fab.tertiary.pressed.icon.color | COLOR | Pressed | `md.sys.color.on-tertiary` |  |  |
| md.comp.fab.tertiary.lowered.container.elevation | ELEVATION | [Deprecated] Enabled / [Deprecated] Container | `md.sys.elevation.level1` |  | Token is deprecated. |
| md.comp.fab.tertiary.container.height | LENGTH | [Deprecated] Enabled / [Deprecated] Container | 56dp |  | Token is deprecated. |
| md.comp.fab.tertiary.container.shape | SHAPE | [Deprecated] Enabled / [Deprecated] Container | `md.sys.shape.corner.large` |  | Token is deprecated. |
| md.comp.fab.tertiary.container.width | LENGTH | [Deprecated] Enabled / [Deprecated] Container | 56dp |  | Token is deprecated. |
| md.comp.fab.tertiary.icon.size | LENGTH | [Deprecated] Enabled / [Deprecated] Icon | 24dp |  | Token is deprecated. |
| md.comp.fab.tertiary.hover.container.elevation | ELEVATION | [Deprecated] Hovered / [Deprecated] Container | `md.sys.elevation.level4` |  | Token is deprecated. |
| md.comp.fab.tertiary.lowered.hover.container.elevation | ELEVATION | [Deprecated] Hovered / [Deprecated] Container | `md.sys.elevation.level2` |  | Token is deprecated. |
| md.comp.fab.tertiary.hover.state-layer.color | COLOR | [Deprecated] Hovered / [Deprecated] State layer | `md.sys.color.on-tertiary-container` |  | Token is deprecated. |
| md.comp.fab.tertiary.hover.state-layer.opacity | OPACITY | [Deprecated] Hovered / [Deprecated] State layer | `md.sys.state.hover.state-layer-opacity` |  | Token is deprecated. |
| md.comp.fab.tertiary.hover.icon.color | COLOR | [Deprecated] Hovered / [Deprecated] Icon | `md.sys.color.on-tertiary-container` |  | Token is deprecated. |
| md.comp.fab.tertiary.focus.indicator.color | COLOR | [Deprecated] Focused / [Deprecated] Focus indicator | `md.sys.color.secondary` |  | Token is deprecated. |
| md.comp.fab.tertiary.focus.indicator.thickness | LENGTH | [Deprecated] Focused / [Deprecated] Focus indicator | `md.sys.state.focus-indicator.thickness` |  | Token is deprecated. |
| md.comp.fab.tertiary.focus.indicator.outline.offset | LENGTH | [Deprecated] Focused / [Deprecated] Focus indicator | `md.sys.state.focus-indicator.outer-offset` |  | Token is deprecated. |
| md.comp.fab.tertiary.focus.container.elevation | ELEVATION | [Deprecated] Focused / [Deprecated] Container | `md.sys.elevation.level3` |  | Token is deprecated. |
| md.comp.fab.tertiary.lowered.focus.container.elevation | ELEVATION | [Deprecated] Focused / [Deprecated] Container | `md.sys.elevation.level1` |  | Token is deprecated. |
| md.comp.fab.tertiary.focus.state-layer.color | COLOR | [Deprecated] Focused / [Deprecated] State layer | `md.sys.color.on-tertiary-container` |  | Token is deprecated. |
| md.comp.fab.tertiary.focus.state-layer.opacity | OPACITY | [Deprecated] Focused / [Deprecated] State layer | `md.sys.state.focus.state-layer-opacity` |  | Token is deprecated. |
| md.comp.fab.tertiary.focus.icon.color | COLOR | [Deprecated] Focused / [Deprecated] Icon | `md.sys.color.on-tertiary-container` |  | Token is deprecated. |
| md.comp.fab.tertiary.lowered.pressed.container.elevation | ELEVATION | [Deprecated] Pressed (ripple) / [Deprecated] Container | `md.sys.elevation.level1` |  | Token is deprecated. |

## Anatomy

![2 elements of the FAB.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmkc6cml8-03-3P.png?alt=media&token=b76c428a-d4ed-4ec1-b6ab-cdec99f0afff>)

_1. Container 2. Icon_

## Color

Color values are implemented through design tokens. For design, this means working with color values that correspond with tokens. In implementation, a color value will be a token that references a value. [Learn more about design tokens](../../foundations/design-tokens/how-to-use-tokens.md)

### Color styles

FABs can use several combinations of **color** and **on-color** styles, such as **primary** and **on-primary**. The following color mappings provide the same legibility and functionality, so the color mapping you use depends on style alone.

![6 FAB color styles in light and dark themes. Each style has 2 color roles, 1 for the container and icon.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmkc6diiz-04-3P.png?alt=media&token=d50706b5-2254-4cd5-afb1-852c728e47b2>)

_Primary container & On primary container (default); Secondary container & On secondary container; Tertiary container & On tertiary container; Primary & On primary; Secondary & On secondary; Tertiary & On tertiary_

### Baseline color styles

Surface FAB color styles are still available, but no longer recommended.

![Baseline FAB style in all 3 sizes.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmkc6empp-05-3P.png?alt=media&token=63065aac-7292-44cb-be02-7632eba1af38>)

_Surface FABs_

## States

States are visual representations used to communicate the status of a component or interactive element.

When using a non-default color mapping for FABs, make sure the state layer color is the same as the icon color. For example, the state layer color for the **primary** color style should be md.sys.color.primary.

![4 states of a FAB shown in light and dark themes.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmkc6fdu1-06-3P.png?alt=media&token=ef3a63fe-b331-4553-a51a-f1e0ca0c3b9b>)

_Enabled; Hovered (8% state layer) - elevation 4; Focused (10% state layer); Pressed (10% state layer)_

## Measurements

### FAB

![FAB size measurements.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmkc6fz4n-07-3P.png?alt=media&token=de1a1525-dae3-446e-8546-21b8320985ce>)

_FAB size measurements_

![FAB padding measurements.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmkc6gdhg-08-3P.png?alt=media&token=816f05ba-1ad7-4d96-a205-d86e3be8772e>)

_FAB padding measurements_

### Medium FAB

![Medium FAB size measurements.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmkc6gu0p-09-3P.png?alt=media&token=26b5fc9c-0d30-48cb-abc9-724ec0b42f3e>)

_Medium FAB size measurements_

![Medium FAB padding measurements.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmkc6ha1z-10-3P.png?alt=media&token=07048e86-e793-4f9e-98d7-816d1c2292dd>)

_Medium FAB padding measurements_

### Large FAB

![Large FAB size measurements.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmkc6hrgc-11-3P.png?alt=media&token=bb7d4869-9309-4cde-b2f7-824004d2f429>)

_Large FAB size measurements_

![Large FAB padding measurements.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmkc6i508-12-3P.png?alt=media&token=d1de97fa-e91b-40c9-aff1-63fcd3535ebb>)

_Large FAB padding measurements_

## Baseline tokens & specs

Use the table's menu to select a token set. This only includes tokens for small and surface FABs, which are both no longer recommended. It doesn't include other colors, or large or regular FABs, since those are still currently used.

### TOKEN_TABLE

Component: Floating action buttons (FABs)

Configured context tags: None, Medium contrast, Default, High contrast, 3P, Android, Dark, Light

#### Token sets

| Token set | Name | Type | Tokens | Description |
| --- | --- | --- | --- | --- |
| [Deprecated] FAB - Size - Small | md.comp.fab.small | COMPONENT | 4 | For small FABs. Not used in expressive FABs. |
| FAB - Color - Surface | md.comp.fab.surface | COMPONENT | 29 | For surface color FABs. Not recommended with expressive FABs. |

#### [Deprecated] FAB - Size - Small (md.comp.fab.small)

For small FABs. Not used in expressive FABs.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.fab.small.container.height | LENGTH |  | 40dp |  |  |
| md.comp.fab.small.container.width | LENGTH |  | 40dp |  |  |
| md.comp.fab.small.icon.size | LENGTH |  | 24dp |  |  |
| md.comp.fab.small.container.shape | SHAPE |  | `md.sys.shape.corner.medium` |  |  |

#### FAB - Color - Surface (md.comp.fab.surface)

For surface color FABs. Not recommended with expressive FABs.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.fab.surface.container.color | COLOR | Enabled / Container | `md.sys.color.surface-container-high` |  |  |
| md.comp.fab.surface.lowered.container.color | COLOR | Enabled / Container | `md.sys.color.surface-container-low` |  |  |
| md.comp.fab.surface.container.shape | SHAPE | Enabled / Container | `md.sys.shape.corner.large` |  |  |
| md.comp.fab.surface.container.width | LENGTH | Enabled / Container | 56dp |  |  |
| md.comp.fab.surface.container.height | LENGTH | Enabled / Container | 56dp |  |  |
| md.comp.fab.surface.container.elevation | ELEVATION | Enabled / Container | `md.sys.elevation.level3` |  |  |
| md.comp.fab.surface.container.surface-tint-layer.color | COLOR | Enabled / Container | `md.sys.color.surface-tint` |  | Deprecated as part of the update from opacity based surfaces to tonal surfaces. Surfaces no longer use surface-tint layers for tinting, please use the desired surface role directly as the container color. |
| md.comp.fab.surface.container.shadow-color | COLOR | Enabled / Container | `md.sys.color.shadow` |  |  |
| md.comp.fab.surface.lowered.container.elevation | ELEVATION | Enabled / Container | `md.sys.elevation.level1` |  |  |
| md.comp.fab.surface.icon.size | LENGTH | Enabled / Icon | 24dp |  |  |
| md.comp.fab.surface.icon.color | COLOR | Enabled / Icon | `md.sys.color.primary` |  |  |
| md.comp.fab.surface.hover.container.elevation | ELEVATION | Hovered / Container | `md.sys.elevation.level4` |  |  |
| md.comp.fab.surface.lowered.hover.container.elevation | ELEVATION | Hovered / Container | `md.sys.elevation.level2` |  |  |
| md.comp.fab.surface.hover.state-layer.color | COLOR | Hovered / State layer | `md.sys.color.primary` |  |  |
| md.comp.fab.surface.hover.state-layer.opacity | OPACITY | Hovered / State layer | `md.sys.state.hover.state-layer-opacity` |  |  |
| md.comp.fab.surface.hover.icon.color | COLOR | Hovered / Icon | `md.sys.color.primary` |  |  |
| md.comp.fab.surface.focus.indicator.color | COLOR | Focused / Focus indicator | `md.sys.color.secondary` |  |  |
| md.comp.fab.surface.focus.indicator.thickness | LENGTH | Focused / Focus indicator | `md.sys.state.focus-indicator.thickness` |  |  |
| md.comp.fab.surface.focus.indicator.outline.offset | LENGTH | Focused / Focus indicator | `md.sys.state.focus-indicator.outer-offset` |  |  |
| md.comp.fab.surface.focus.container.elevation | ELEVATION | Focused / Container | `md.sys.elevation.level3` |  |  |
| md.comp.fab.surface.lowered.focus.container.elevation | ELEVATION | Focused / Container | `md.sys.elevation.level1` |  |  |
| md.comp.fab.surface.focus.state-layer.color | COLOR | Focused / State layer | `md.sys.color.primary` |  |  |
| md.comp.fab.surface.focus.state-layer.opacity | OPACITY | Focused / State layer | `md.sys.state.focus.state-layer-opacity` |  |  |
| md.comp.fab.surface.focus.icon.color | COLOR | Focused / Icon | `md.sys.color.primary` |  |  |
| md.comp.fab.surface.pressed.container.elevation | ELEVATION | Pressed (ripple) / Container | `md.sys.elevation.level3` |  |  |
| md.comp.fab.surface.lowered.pressed.container.elevation | ELEVATION | Pressed (ripple) / Container | `md.sys.elevation.level1` |  |  |
| md.comp.fab.surface.pressed.state-layer.color | COLOR | Pressed (ripple) / State layer | `md.sys.color.primary` |  |  |
| md.comp.fab.surface.pressed.state-layer.opacity | OPACITY | Pressed (ripple) / State layer | `md.sys.state.pressed.state-layer-opacity` |  |  |
| md.comp.fab.surface.pressed.icon.color | COLOR | Pressed (ripple) / Icon | `md.sys.color.primary` |  |  |

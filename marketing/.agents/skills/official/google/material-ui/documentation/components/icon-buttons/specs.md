---
title: "Icon buttons"
source_url: "https://m3.material.io/components/icon-buttons/specs"
lastmod: "2026-02-11"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "components"
assets:
  - "assets/images/1314d57a6ba7-macn9b93-1.png"
  - "assets/images/b7960af7d3c5-macn9wle-2.png"
  - "assets/images/1625e25895c3-macngrp0-3.png"
  - "assets/images/ae63520871c5-macnmn5n-4.png"
  - "assets/images/d80347bb7b72-macny1bg-7.png"
  - "assets/images/4798047f809b-macnyuzs-8.png"
  - "assets/images/99bb22175870-maco0ivh-9.png"
  - "assets/images/54a5599e55d9-maco1xul-10.png"
  - "assets/images/e008d13a13ea-maco30ip-11.png"
  - "assets/images/dc78afc26f10-maco3sgl-12.png"
  - "assets/images/6dd52a77f008-maco50jx-13.png"
  - "assets/images/d17d6226e6a9-maco5p31-14.png"
  - "assets/images/36f9a63a8fe0-mal45bxj-15.png"
  - "assets/images/bed05e041ede-mafeboyy-16.png"
  - "assets/images/e8bb16886226-macoq4wo-17.png"
  - "assets/images/0589307ae8e2-macosw5f-18.png"
  - "assets/images/25e4f0b935bb-mafelwts-19.png"
---

# Icon buttons

Icon buttons help people take minor actions with one tap

## Specs

## Variants

![Side by side view of default and toggle icon buttons.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmacn9b93-1.png?alt=media&token=eaccf9c1-f0f1-4ca1-bdd0-1bd695733cf8>)

_Default icon button; Toggle icon button_

| Variant | M3 | M3 Expressive |
| --- | --- | --- |
| Default | Available | Available |
| Toggle (selection) | Available | Available |

## Configurations

![Side by side view of size, shape, color, and width variations.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmacn9wle-2.png?alt=media&token=d72ef6d8-9fd8-465e-858b-fbae2febebb5>)

_Five sizes; Two shapes; Four color styles; Three widths_

| Category | Options | M3 | M3 Expressive |
| --- | --- | --- | --- |
| Size | Small (default) | Available | Available |
| XS, M, L, XL | \-- | Available |  |
| Shape | Round (default) | Available | Available |
| Square | \-- | Available |  |
| Color | Filled (default), tonal, outlined, standard | Available | Available |
| Width | Default | Available | Available |
| Narrow, wide | \-- | Available |  |

## Tokens & specs

Icon button token sets are organized by common tokens, color, and size. Select the token set from the table’s menu. [Learn about design tokens](https://m3.material.io/m3/pages/design-tokens/overview/)

### TOKEN_TABLE

Component: Icon buttons

Configured context tags: None, Medium contrast, Default, High contrast, Standard, Expressive, 3P, Android, Dark, Light

#### Token sets

| Token set | Name | Type | Tokens | Description |
| --- | --- | --- | --- | --- |
| Icon button - Color - Filled | md.comp.icon-button.filled | COMPONENT | 31 | For filled icon buttons. |
| Icon button - Color - Tonal | md.comp.icon-button.tonal | COMPONENT | 31 | For tonal icon buttons. |
| Icon button - Color - Outlined | md.comp.icon-button.outlined | COMPONENT | 33 | For outlined icon buttons. |
| Icon button - Color - Standard | md.comp.icon-button.standard | COMPONENT | 26 | For standard icon buttons. |
| Icon button - Size - Xsmall | md.comp.icon-button.xsmall | COMPONENT | 16 | For XS icon buttons. |
| Icon button - Size - Small | md.comp.icon-button.small | COMPONENT | 16 | For small icon buttons. |
| Icon button - Size - Medium | md.comp.icon-button.medium | COMPONENT | 16 | For medium icon buttons. |
| Icon button - Size - Large | md.comp.icon-button.large | COMPONENT | 16 | For large icon buttons. |
| Icon button - Size - Xlarge | md.comp.icon-button.xlarge | COMPONENT | 16 | For XL icon buttons. |

#### Icon button - Color - Filled (md.comp.icon-button.filled)

For filled icon buttons.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.icon-button.filled.container.color | COLOR | Enabled | `md.sys.color.primary` |  |  |
| md.comp.icon-button.filled.unselected.container.color | COLOR | Enabled | `md.sys.color.surface-container` |  |  |
| md.comp.icon-button.filled.selected.container.color | COLOR | Enabled | `md.sys.color.primary` |  |  |
| md.comp.icon-button.filled.icon.color | COLOR | Enabled | `md.sys.color.on-primary` |  |  |
| md.comp.icon-button.filled.unselected.icon.color | COLOR | Enabled | `md.sys.color.on-surface-variant` |  |  |
| md.comp.icon-button.filled.selected.icon.color | COLOR | Enabled | `md.sys.color.on-primary` |  |  |
| md.comp.icon-button.filled.disabled.container.color | COLOR | Disabled | `md.sys.color.on-surface` |  |  |
| md.comp.icon-button.filled.disabled.container.opacity | OPACITY | Disabled | 0.1 |  |  |
| md.comp.icon-button.filled.disabled.icon.color | COLOR | Disabled | `md.sys.color.on-surface` |  |  |
| md.comp.icon-button.filled.disabled.icon.opacity | OPACITY | Disabled | 0.38 |  |  |
| md.comp.icon-button.filled.hovered.state-layer.color | COLOR | Hovered | `md.sys.color.on-primary` |  |  |
| md.comp.icon-button.filled.unselected.hovered.state-layer.color | COLOR | Hovered | `md.sys.color.on-surface-variant` |  |  |
| md.comp.icon-button.filled.selected.hovered.state-layer.color | COLOR | Hovered | `md.sys.color.on-primary` |  |  |
| md.comp.icon-button.filled.hovered.state-layer.opacity | OPACITY | Hovered | `md.sys.state.hover.state-layer-opacity` |  |  |
| md.comp.icon-button.filled.hovered.icon.color | COLOR | Hovered | `md.sys.color.on-primary` |  |  |
| md.comp.icon-button.filled.unselected.hovered.icon.color | COLOR | Hovered | `md.sys.color.on-surface-variant` |  |  |
| md.comp.icon-button.filled.selected.hovered.icon.color | COLOR | Hovered | `md.sys.color.on-primary` |  |  |
| md.comp.icon-button.filled.focused.state-layer.color | COLOR | Focused | `md.sys.color.on-primary` |  |  |
| md.comp.icon-button.filled.unselected.focused.state-layer.color | COLOR | Focused | `md.sys.color.on-surface-variant` |  |  |
| md.comp.icon-button.filled.selected.focused.state-layer.color | COLOR | Focused | `md.sys.color.on-primary` |  |  |
| md.comp.icon-button.filled.focused.state-layer.opacity | OPACITY | Focused | `md.sys.state.focus.state-layer-opacity` |  |  |
| md.comp.icon-button.filled.focused.icon.color | COLOR | Focused | `md.sys.color.on-primary` |  |  |
| md.comp.icon-button.filled.unselected.focused.icon.color | COLOR | Focused | `md.sys.color.on-surface-variant` |  |  |
| md.comp.icon-button.filled.selected.focused.icon.color | COLOR | Focused | `md.sys.color.on-primary` |  |  |
| md.comp.icon-button.filled.pressed.state-layer.color | COLOR | Pressed | `md.sys.color.on-primary` |  |  |
| md.comp.icon-button.filled.unselected.pressed.state-layer.color | COLOR | Pressed | `md.sys.color.on-surface-variant` |  |  |
| md.comp.icon-button.filled.selected.pressed.state-layer.color | COLOR | Pressed | `md.sys.color.on-primary` |  |  |
| md.comp.icon-button.filled.pressed.state-layer.opacity | OPACITY | Pressed | `md.sys.state.pressed.state-layer-opacity` |  |  |
| md.comp.icon-button.filled.pressed.icon.color | COLOR | Pressed | `md.sys.color.on-primary` |  |  |
| md.comp.icon-button.filled.unselected.pressed.icon.color | COLOR | Pressed | `md.sys.color.on-surface-variant` |  |  |
| md.comp.icon-button.filled.selected.pressed.icon.color | COLOR | Pressed | `md.sys.color.on-primary` |  |  |

#### Icon button - Color - Tonal (md.comp.icon-button.tonal)

For tonal icon buttons.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.icon-button.tonal.container.color | COLOR | Enabled | `md.sys.color.secondary-container` |  |  |
| md.comp.icon-button.tonal.unselected.container.color | COLOR | Enabled | `md.sys.color.secondary-container` |  |  |
| md.comp.icon-button.tonal.selected.container.color | COLOR | Enabled | `md.sys.color.secondary` |  |  |
| md.comp.icon-button.tonal.icon.color | COLOR | Enabled | `md.sys.color.on-secondary-container` |  |  |
| md.comp.icon-button.tonal.unselected.icon.color | COLOR | Enabled | `md.sys.color.on-secondary-container` |  |  |
| md.comp.icon-button.tonal.selected.icon.color | COLOR | Enabled | `md.sys.color.on-secondary` |  |  |
| md.comp.icon-button.tonal.disabled.container.color | COLOR | Disabled | `md.sys.color.on-surface` |  |  |
| md.comp.icon-button.tonal.disabled.container.opacity | OPACITY | Disabled | 0.1 |  |  |
| md.comp.icon-button.tonal.disabled.icon.color | COLOR | Disabled | `md.sys.color.on-surface` |  |  |
| md.comp.icon-button.tonal.disabled.icon.opacity | OPACITY | Disabled | 0.38 |  |  |
| md.comp.icon-button.tonal.hovered.state-layer.color | COLOR | Hovered | `md.sys.color.on-secondary-container` |  |  |
| md.comp.icon-button.tonal.unselected.hovered.state-layer.color | COLOR | Hovered | `md.sys.color.on-secondary-container` |  |  |
| md.comp.icon-button.tonal.selected.hovered.state-layer.color | COLOR | Hovered | `md.sys.color.on-secondary` |  |  |
| md.comp.icon-button.tonal.hovered.state-layer.opacity | OPACITY | Hovered | `md.sys.state.hover.state-layer-opacity` |  |  |
| md.comp.icon-button.tonal.hovered.icon.color | COLOR | Hovered | `md.sys.color.on-secondary-container` |  |  |
| md.comp.icon-button.tonal.unselected.hovered.icon.color | COLOR | Hovered | `md.sys.color.on-secondary-container` |  |  |
| md.comp.icon-button.tonal.selected.hovered.icon.color | COLOR | Hovered | `md.sys.color.on-secondary` |  |  |
| md.comp.icon-button.tonal.focused.state-layer.color | COLOR | Focused | `md.sys.color.on-secondary-container` |  |  |
| md.comp.icon-button.tonal.unselected.focused.state-layer.color | COLOR | Focused | `md.sys.color.on-secondary-container` |  |  |
| md.comp.icon-button.tonal.selected.focused.state-layer.color | COLOR | Focused | `md.sys.color.on-secondary` |  |  |
| md.comp.icon-button.tonal.focused.state-layer.opacity | OPACITY | Focused | `md.sys.state.focus.state-layer-opacity` |  |  |
| md.comp.icon-button.tonal.focused.icon.color | COLOR | Focused | `md.sys.color.on-secondary-container` |  |  |
| md.comp.icon-button.tonal.unselected.focused.icon.color | COLOR | Focused | `md.sys.color.on-secondary-container` |  |  |
| md.comp.icon-button.tonal.selected.focused.icon.color | COLOR | Focused | `md.sys.color.on-secondary` |  |  |
| md.comp.icon-button.tonal.pressed.state-layer.color | COLOR | Pressed | `md.sys.color.on-secondary-container` |  |  |
| md.comp.icon-button.tonal.unselected.pressed.state-layer.color | COLOR | Pressed | `md.sys.color.on-secondary-container` |  |  |
| md.comp.icon-button.tonal.selected.pressed.state-layer.color | COLOR | Pressed | `md.sys.color.on-secondary` |  |  |
| md.comp.icon-button.tonal.pressed.state-layer.opacity | OPACITY | Pressed | `md.sys.state.pressed.state-layer-opacity` |  |  |
| md.comp.icon-button.tonal.pressed.icon.color | COLOR | Pressed | `md.sys.color.on-secondary-container` |  |  |
| md.comp.icon-button.tonal.unselected.pressed.icon.color | COLOR | Pressed | `md.sys.color.on-secondary-container` |  |  |
| md.comp.icon-button.tonal.selected.pressed.icon.color | COLOR | Pressed | `md.sys.color.on-secondary` |  |  |

#### Icon button - Color - Outlined (md.comp.icon-button.outlined)

For outlined icon buttons.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.icon-button.outlined.outline.color | COLOR | Enabled | `md.sys.color.outline-variant` |  |  |
| md.comp.icon-button.outlined.unselected.outline.color | COLOR | Enabled | `md.sys.color.outline-variant` |  |  |
| md.comp.icon-button.outlined.selected.container.color | COLOR | Enabled | `md.sys.color.inverse-surface` |  |  |
| md.comp.icon-button.outlined.icon.color | COLOR | Enabled | `md.sys.color.on-surface-variant` |  |  |
| md.comp.icon-button.outlined.unselected.icon.color | COLOR | Enabled | `md.sys.color.on-surface-variant` |  |  |
| md.comp.icon-button.outlined.selected.icon.color | COLOR | Enabled | `md.sys.color.inverse-on-surface` |  |  |
| md.comp.icon-button.outlined.disabled.outline.color | COLOR | Disabled | `md.sys.color.outline-variant` |  |  |
| md.comp.icon-button.outlined.unselected.disabled.outline.color | COLOR | Disabled | `md.sys.color.outline-variant` |  |  |
| md.comp.icon-button.outlined.selected.disabled.container.color | COLOR | Disabled | `md.sys.color.on-surface` |  |  |
| md.comp.icon-button.outlined.selected.disabled.container.opacity | OPACITY | Disabled | 0.1 |  |  |
| md.comp.icon-button.outlined.disabled.icon.color | COLOR | Disabled | `md.sys.color.on-surface` |  |  |
| md.comp.icon-button.outlined.disabled.icon.opacity | OPACITY | Disabled | 0.38 |  |  |
| md.comp.icon-button.outlined.hovered.state-layer.color | COLOR | Hovered | `md.sys.color.on-surface-variant` |  |  |
| md.comp.icon-button.outlined.unselected.hovered.state-layer.color | COLOR | Hovered | `md.sys.color.on-surface-variant` |  |  |
| md.comp.icon-button.outlined.selected.hovered.state-layer.color | COLOR | Hovered | `md.sys.color.inverse-on-surface` |  |  |
| md.comp.icon-button.outlined.hovered.state-layer.opacity | OPACITY | Hovered | `md.sys.state.hover.state-layer-opacity` |  |  |
| md.comp.icon-button.outlined.hovered.icon.color | COLOR | Hovered | `md.sys.color.on-surface-variant` |  |  |
| md.comp.icon-button.outlined.unselected.hovered.icon.color | COLOR | Hovered | `md.sys.color.on-surface-variant` |  |  |
| md.comp.icon-button.outlined.selected.hovered.icon.color | COLOR | Hovered | `md.sys.color.inverse-on-surface` |  |  |
| md.comp.icon-button.outlined.focused.state-layer.color | COLOR | Focused | `md.sys.color.on-surface-variant` |  |  |
| md.comp.icon-button.outlined.unselected.focused.state-layer.color | COLOR | Focused | `md.sys.color.on-surface-variant` |  |  |
| md.comp.icon-button.outlined.selected.focused.state-layer.color | COLOR | Focused | `md.sys.color.inverse-on-surface` |  |  |
| md.comp.icon-button.outlined.focused.state-layer.opacity | OPACITY | Focused | `md.sys.state.focus.state-layer-opacity` |  |  |
| md.comp.icon-button.outlined.focused.icon.color | COLOR | Focused | `md.sys.color.on-surface-variant` |  |  |
| md.comp.icon-button.outlined.unselected.focused.icon.color | COLOR | Focused | `md.sys.color.on-surface-variant` |  |  |
| md.comp.icon-button.outlined.selected.focused.icon.color | COLOR | Focused | `md.sys.color.inverse-on-surface` |  |  |
| md.comp.icon-button.outlined.pressed.state-layer.color | COLOR | Pressed | `md.sys.color.on-surface-variant` |  |  |
| md.comp.icon-button.outlined.unselected.pressed.state-layer.color | COLOR | Pressed | `md.sys.color.on-surface-variant` |  |  |
| md.comp.icon-button.outlined.selected.pressed.state-layer.color | COLOR | Pressed | `md.sys.color.inverse-on-surface` |  |  |
| md.comp.icon-button.outlined.pressed.state-layer.opacity | OPACITY | Pressed | `md.sys.state.pressed.state-layer-opacity` |  |  |
| md.comp.icon-button.outlined.pressed.icon.color | COLOR | Pressed | `md.sys.color.on-surface-variant` |  |  |
| md.comp.icon-button.outlined.unselected.pressed.icon.color | COLOR | Pressed | `md.sys.color.on-surface-variant` |  |  |
| md.comp.icon-button.outlined.selected.pressed.icon.color | COLOR | Pressed | `md.sys.color.inverse-on-surface` |  |  |

#### Icon button - Color - Standard (md.comp.icon-button.standard)

For standard icon buttons.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.icon-button.standard.icon.color | COLOR | Enabled | `md.sys.color.on-surface-variant` |  |  |
| md.comp.icon-button.standard.unselected.icon.color | COLOR | Enabled | `md.sys.color.on-surface-variant` |  |  |
| md.comp.icon-button.standard.selected.icon.color | COLOR | Enabled | `md.sys.color.primary` |  |  |
| md.comp.icon-button.standard.disabled.icon.color | COLOR | Disabled | `md.sys.color.on-surface` |  |  |
| md.comp.icon-button.standard.disabled.icon.opacity | OPACITY | Disabled | 0.38 |  |  |
| md.comp.icon-button.standard.hovered.state-layer.color | COLOR | Hovered | `md.sys.color.on-surface-variant` |  |  |
| md.comp.icon-button.standard.unselected.hovered.state-layer.color | COLOR | Hovered | `md.sys.color.on-surface-variant` |  |  |
| md.comp.icon-button.standard.selected.hovered.state-layer.color | COLOR | Hovered | `md.sys.color.primary` |  |  |
| md.comp.icon-button.standard.hovered.state-layer.opacity | OPACITY | Hovered | `md.sys.state.hover.state-layer-opacity` |  |  |
| md.comp.icon-button.standard.hovered.icon.color | COLOR | Hovered | `md.sys.color.on-surface-variant` |  |  |
| md.comp.icon-button.standard.unselected.hovered.icon.color | COLOR | Hovered | `md.sys.color.on-surface-variant` |  |  |
| md.comp.icon-button.standard.selected.hovered.icon.color | COLOR | Hovered | `md.sys.color.primary` |  |  |
| md.comp.icon-button.standard.focused.state-layer.color | COLOR | Focused | `md.sys.color.on-surface-variant` |  |  |
| md.comp.icon-button.standard.unselected.focused.state-layer.color | COLOR | Focused | `md.sys.color.on-surface-variant` |  |  |
| md.comp.icon-button.standard.selected.focused.state-layer.color | COLOR | Focused | `md.sys.color.primary` |  |  |
| md.comp.icon-button.standard.focused.state-layer.opacity | OPACITY | Focused | `md.sys.state.focus.state-layer-opacity` |  |  |
| md.comp.icon-button.standard.focused.icon.color | COLOR | Focused | `md.sys.color.on-surface-variant` |  |  |
| md.comp.icon-button.standard.unselected.focused.icon.color | COLOR | Focused | `md.sys.color.on-surface-variant` |  |  |
| md.comp.icon-button.standard.selected.focused.icon.color | COLOR | Focused | `md.sys.color.primary` |  |  |
| md.comp.icon-button.standard.pressed.state-layer.color | COLOR | Pressed | `md.sys.color.on-surface-variant` |  |  |
| md.comp.icon-button.standard.unselected.pressed.state-layer.color | COLOR | Pressed | `md.sys.color.on-surface-variant` |  |  |
| md.comp.icon-button.standard.selected.pressed.state-layer.color | COLOR | Pressed | `md.sys.color.primary` |  |  |
| md.comp.icon-button.standard.pressed.state-layer.opacity | OPACITY | Pressed | `md.sys.state.pressed.state-layer-opacity` |  |  |
| md.comp.icon-button.standard.pressed.icon.color | COLOR | Pressed | `md.sys.color.on-surface-variant` |  |  |
| md.comp.icon-button.standard.unselected.pressed.icon.color | COLOR | Pressed | `md.sys.color.on-surface-variant` |  |  |
| md.comp.icon-button.standard.selected.pressed.icon.color | COLOR | Pressed | `md.sys.color.primary` |  |  |

#### Icon button - Size - Xsmall (md.comp.icon-button.xsmall)

For XS icon buttons.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.icon-button.xsmall.container.height | LENGTH |  | 32dp |  |  |
| md.comp.icon-button.xsmall.icon.size | LENGTH |  | 20dp |  |  |
| md.comp.icon-button.xsmall.narrow.leading-space | LENGTH |  | 4dp |  |  |
| md.comp.icon-button.xsmall.narrow.trailing-space | LENGTH |  | 4dp |  |  |
| md.comp.icon-button.xsmall.default.leading-space | LENGTH |  | 6dp |  |  |
| md.comp.icon-button.xsmall.default.trailing-space | LENGTH |  | 6dp |  |  |
| md.comp.icon-button.xsmall.wide.leading-space | LENGTH |  | 10dp |  |  |
| md.comp.icon-button.xsmall.wide.trailing-space | LENGTH |  | 10dp |  |  |
| md.comp.icon-button.xsmall.container.shape.round | SHAPE |  | `md.sys.shape.corner.full` |  |  |
| md.comp.icon-button.xsmall.container.shape.square | SHAPE |  | `md.sys.shape.corner.medium` |  |  |
| md.comp.icon-button.xsmall.outlined.outline.width | LENGTH |  | 1dp |  |  |
| md.comp.icon-button.xsmall.pressed.container.shape | SHAPE |  | `md.sys.shape.corner.small` |  |  |
| md.comp.icon-button.xsmall.pressed.container.corner-size.motion.spring.damping | DURATION |  | `md.sys.motion.spring.fast.spatial.damping` |  |  |
| md.comp.icon-button.xsmall.pressed.container.corner-size.motion.spring.stiffness | DURATION |  | `md.sys.motion.spring.fast.spatial.stiffness` |  |  |
| md.comp.icon-button.xsmall.selected.container.shape.round | SHAPE |  | `md.sys.shape.corner.medium` |  |  |
| md.comp.icon-button.xsmall.selected.container.shape.square | SHAPE |  | `md.sys.shape.corner.full` |  |  |

#### Icon button - Size - Small (md.comp.icon-button.small)

For small icon buttons.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.icon-button.small.container.height | LENGTH |  | 40dp |  |  |
| md.comp.icon-button.small.icon.size | LENGTH |  | 24dp |  |  |
| md.comp.icon-button.small.narrow.leading-space | LENGTH |  | 4dp |  |  |
| md.comp.icon-button.small.narrow.trailing-space | LENGTH |  | 4dp |  |  |
| md.comp.icon-button.small.default.leading-space | LENGTH |  | 8dp |  |  |
| md.comp.icon-button.small.default.trailing-space | LENGTH |  | 8dp |  |  |
| md.comp.icon-button.small.wide.leading-space | LENGTH |  | 14dp |  |  |
| md.comp.icon-button.small.wide.trailing-space | LENGTH |  | 14dp |  |  |
| md.comp.icon-button.small.container.shape.round | SHAPE |  | `md.sys.shape.corner.full` |  |  |
| md.comp.icon-button.small.container.shape.square | SHAPE |  | `md.sys.shape.corner.medium` |  |  |
| md.comp.icon-button.small.outlined.outline.width | LENGTH |  | 1dp |  |  |
| md.comp.icon-button.small.pressed.container.shape | SHAPE |  | `md.sys.shape.corner.small` |  |  |
| md.comp.icon-button.small.pressed.container.corner-size.motion.spring.damping | DURATION |  | `md.sys.motion.spring.fast.spatial.damping` |  |  |
| md.comp.icon-button.small.pressed.container.corner-size.motion.spring.stiffness | DURATION |  | `md.sys.motion.spring.fast.spatial.stiffness` |  |  |
| md.comp.icon-button.small.selected.container.shape.round | SHAPE |  | `md.sys.shape.corner.medium` |  |  |
| md.comp.icon-button.small.selected.container.shape.square | SHAPE |  | `md.sys.shape.corner.full` |  |  |

#### Icon button - Size - Medium (md.comp.icon-button.medium)

For medium icon buttons.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.icon-button.medium.container.height | LENGTH |  | 56dp |  |  |
| md.comp.icon-button.medium.icon.size | LENGTH |  | 24dp |  |  |
| md.comp.icon-button.medium.narrow.leading-space | LENGTH |  | 12dp |  |  |
| md.comp.icon-button.medium.narrow.trailing-space | LENGTH |  | 12dp |  |  |
| md.comp.icon-button.medium.default.leading-space | LENGTH |  | 16dp |  |  |
| md.comp.icon-button.medium.default.trailing-space | LENGTH |  | 16dp |  |  |
| md.comp.icon-button.medium.wide.leading-space | LENGTH |  | 24dp |  |  |
| md.comp.icon-button.medium.wide.trailing-space | LENGTH |  | 24dp |  |  |
| md.comp.icon-button.medium.container.shape.round | SHAPE |  | `md.sys.shape.corner.full` |  |  |
| md.comp.icon-button.medium.container.shape.square | SHAPE |  | `md.sys.shape.corner.large` |  |  |
| md.comp.icon-button.medium.outlined.outline.width | LENGTH |  | 1dp |  |  |
| md.comp.icon-button.medium.pressed.container.shape | SHAPE |  | `md.sys.shape.corner.medium` |  |  |
| md.comp.icon-button.medium.pressed.container.corner-size.motion.spring.damping | DURATION |  | `md.sys.motion.spring.fast.spatial.damping` |  |  |
| md.comp.icon-button.medium.pressed.container.corner-size.motion.spring.stiffness | DURATION |  | `md.sys.motion.spring.fast.spatial.stiffness` |  |  |
| md.comp.icon-button.medium.selected.container.shape.round | SHAPE |  | `md.sys.shape.corner.large` |  |  |
| md.comp.icon-button.medium.selected.container.shape.square | SHAPE |  | `md.sys.shape.corner.full` |  |  |

#### Icon button - Size - Large (md.comp.icon-button.large)

For large icon buttons.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.icon-button.large.container.height | LENGTH |  | 96dp |  |  |
| md.comp.icon-button.large.icon.size | LENGTH |  | 32dp |  |  |
| md.comp.icon-button.large.narrow.leading-space | LENGTH |  | 16dp |  |  |
| md.comp.icon-button.large.narrow.trailing-space | LENGTH |  | 16dp |  |  |
| md.comp.icon-button.large.default.leading-space | LENGTH |  | 32dp |  |  |
| md.comp.icon-button.large.default.trailing-space | LENGTH |  | 32dp |  |  |
| md.comp.icon-button.large.wide.leading-space | LENGTH |  | 48dp |  |  |
| md.comp.icon-button.large.wide.trailing-space | LENGTH |  | 48dp |  |  |
| md.comp.icon-button.large.container.shape.round | SHAPE |  | `md.sys.shape.corner.full` |  |  |
| md.comp.icon-button.large.container.shape.square | SHAPE |  | `md.sys.shape.corner.extra-large` |  |  |
| md.comp.icon-button.large.outlined.outline.width | LENGTH |  | 2dp |  |  |
| md.comp.icon-button.large.pressed.container.shape | SHAPE |  | `md.sys.shape.corner.large` |  |  |
| md.comp.icon-button.large.pressed.container.corner-size.motion.spring.damping | DURATION |  | `md.sys.motion.spring.fast.spatial.damping` |  |  |
| md.comp.icon-button.large.pressed.container.corner-size.motion.spring.stiffness | DURATION |  | `md.sys.motion.spring.fast.spatial.stiffness` |  |  |
| md.comp.icon-button.large.selected.container.shape.round | SHAPE |  | `md.sys.shape.corner.extra-large` |  |  |
| md.comp.icon-button.large.selected.container.shape.square | SHAPE |  | `md.sys.shape.corner.full` |  |  |

#### Icon button - Size - Xlarge (md.comp.icon-button.xlarge)

For XL icon buttons.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.icon-button.xlarge.container.height | LENGTH |  | 136dp |  |  |
| md.comp.icon-button.xlarge.icon.size | LENGTH |  | 40dp |  |  |
| md.comp.icon-button.xlarge.narrow.leading-space | LENGTH |  | 32dp |  |  |
| md.comp.icon-button.xlarge.narrow.trailing-space | LENGTH |  | 32dp |  |  |
| md.comp.icon-button.xlarge.default.leading-space | LENGTH |  | 48dp |  |  |
| md.comp.icon-button.xlarge.default.trailing-space | LENGTH |  | 48dp |  |  |
| md.comp.icon-button.xlarge.wide.leading-space | LENGTH |  | 72dp |  |  |
| md.comp.icon-button.xlarge.wide.trailing-space | LENGTH |  | 72dp |  |  |
| md.comp.icon-button.xlarge.container.shape.round | SHAPE |  | `md.sys.shape.corner.full` |  |  |
| md.comp.icon-button.xlarge.container.shape.square | SHAPE |  | `md.sys.shape.corner.extra-large` |  |  |
| md.comp.icon-button.xlarge.outlined.outline.width | LENGTH |  | 3dp |  |  |
| md.comp.icon-button.xlarge.pressed.container.shape | SHAPE |  | `md.sys.shape.corner.large` |  |  |
| md.comp.icon-button.xlarge.pressed.container.corner-size.motion.spring.damping | DURATION |  | `md.sys.motion.spring.fast.spatial.damping` |  |  |
| md.comp.icon-button.xlarge.pressed.container.corner-size.motion.spring.stiffness | DURATION |  | `md.sys.motion.spring.fast.spatial.stiffness` |  |  |
| md.comp.icon-button.xlarge.selected.container.shape.round | SHAPE |  | `md.sys.shape.corner.extra-large` |  |  |
| md.comp.icon-button.xlarge.selected.container.shape.square | SHAPE |  | `md.sys.shape.corner.full` |  |  |

## Anatomy

![Diagram indicating anatomy of filled icon button.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmacngrp0-3.png?alt=media&token=6f49a2d7-daed-42bb-8a28-29f66f842591>)

_Icon; Container_

## Color

Color values are implemented through design tokens. For designers, this means working with color values that correspond with tokens; in implementation, a color value will be a token that references a value.

There are four built-in color styles: filled, tonal, outlined, and standard. Default and toggle buttons use different color roles per style.

star

Note:

These color roles were chosen to create design coherence and familiarity. Other color roles can be used as long as the container and text have a 3:1 contrast ratio. For example, tertiary and on tertiary.

![Color roles of default and toggle buttons in 4 visual styles.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmacnmn5n-4.png?alt=media&token=a93643fc-01ad-4c96-b27f-6fc44e197485>)

_A: Filled, B: Tonal, C: Outlined, D: Standard / Default; Toggle, unselected; Toggle, selected_

|  | 1\. Default | 2\. Toggle, unselected | 3\. Toggle, selected |
| --- | --- | --- | --- |
| Filled container Filled icon | Primary On primary | Surface container On surface variant | Primary On primary |
| Tonal container Tonal icon | Secondary container On secondary container | Secondary container On secondary container | Secondary On secondary |
| Outlined container Outlined icon | Outline variant (outline) On surface variant | Outline variant (outline) On surface variant | Inverse surface Inverse on surface |
| Standard icon | On surface variant | On surface variant | Primary |

## States

States are visual representations used to communicate the status of a component or interactive element. State layers slightly change button color. Disabled states have different base colors. [View tokens for details](../../foundations/design-tokens/overview.md)

### Filled button states

#### Default

![5 states of filled icon button.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmacny1bg-7.png?alt=media&token=d9a12639-84db-4ce0-a2ab-cb94ea1fd49f>)

_Enabled; Disabled (10% state layer); Hovered (8% state layer); Focused (10% state layer); Pressed (10% state layer)_

#### Toggle

![5 states of filled toggle icon button.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmacnyuzs-8.png?alt=media&token=a503edb9-66a1-467f-9012-2a0d190db399>)

_A: Unselected. B: Selected Enabled; Disabled (10% state layer); Hovered (8% state layer); Focused (10% state layer); Pressed (10% state layer)_

### Tonal button states

#### Default

![5 states of tonal icon button.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmaco0ivh-9.png?alt=media&token=608bddef-1cc2-456c-8fa7-7b483a1f3bfb>)

_Enabled; Disabled (10% state layer); Hovered (8% state layer); Focused (10% state layer); Pressed (10% state layer)_

#### Toggle

![5 states of tonal toggle icon button.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmaco1xul-10.png?alt=media&token=b3419078-228a-4fe2-b312-3c4cf1ffd2c5>)

_A: Unselected. B: Selected Enabled; Disabled (10% state layer); Hovered (8% state layer); Focused (10% state layer); Pressed (10% state layer)_

### Outlined button states

#### Default

![5 states of outlined icon button.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmaco30ip-11.png?alt=media&token=46194872-3ef7-4efe-9f06-2b4ad86432ea>)

_Enabled; Disabled (10% state layer); Hovered (8% state layer); Focused (10% state layer); Pressed (10% state layer)_

#### Toggle

![5 states of outlined toggle icon button.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmaco3sgl-12.png?alt=media&token=f038a5f8-56b3-4d57-9642-60dd620a55fc>)

_A: Unselected. B: Selected Enabled; Disabled (10% state layer); Hovered (8% state layer); Focused (10% state layer); Pressed (10% state layer)_

### Standard icon button states

The standard icon button’s container is invisible at rest, but visible when the state layer is applied.

#### Default

![5 states of standard icon button.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmaco50jx-13.png?alt=media&token=e436ef4e-3ce4-4ab4-8823-46785a508376>)

_Enabled; Disabled (10% state layer); Hovered (8% state layer); Focused (10% state layer); Pressed (10% state layer)_

#### Toggle

![5 states of standard toggle icon button.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmaco5p31-14.png?alt=media&token=6dd172e4-9231-405e-9d91-885cfcb639ed>)

_A: Unselected. B: Selected Enabled; Disabled (10% state layer); Hovered (8% state layer); Focused (10% state layer); Pressed (10% state layer)_

## Shape morph

### Pressed state

While pressed, icon buttons can morph to become more square. 

Both round and square icon buttons should have the same pressed shape radius.

The corner radius value differs for each button size. [See full icon button corner measurements](specs.md#b3df1f02-d313-44e9-9542-37f7e0e24dc7)

![Shape changes for round and square icon buttons in 3 states.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmal45bxj-15.png?alt=media&token=8748a957-b64c-4f67-82ee-a19fddf12a3b>)

_A. Round, B. Square Enabled; Hovered; Pressed_

### When selected

In addition to changing shape when pressed, toggle icon buttons also change the resting shape from round (unselected) to square (selected) by default.

If the resting shape is square, the selected shape should be round.

![Shape changes for round and square toggle icon buttons in 3 states.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmafeboyy-16.png?alt=media&token=d78d1634-d6a5-4e93-b964-74dd5828ea0a>)

_A. Round, B. Square Enabled; Hovered; Pressed; Selected_

## Measurements

![Diagram of 5 sizes of icon buttons in 4 widths.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmacoq4wo-17.png?alt=media&token=6ba00bd8-8461-4d22-a064-b844353a3598>)

_A. Extra small B. Small C. Medium D. Large E. Extra large Icon size; Default width size; Narrow width size; Wide width size_

### Target sizes

Extra small and small icon buttons must have a target size of 48x48dp or larger to be accessible.

![Diagram of target sizes.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmacosw5f-18.png?alt=media&token=25d269f8-b0c4-4510-be2c-e503f0a9c48a>)

_A. Extra small icon button size B. Small icon button size Narrow width; Default width; Wide width_

### Button corner radius

![Diagram of icon button corner radius.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmafelwts-19.png?alt=media&token=e4d6f03c-43da-4306-bdd0-eabc58424088>)

_/_

|  | XS | S | M | L | XL |
| --- | --- | --- | --- | --- | --- |
| A. Round button | Full | Full | Full | Full | Full |
| B. Square button | 12dp | 12dp | 16dp | 28dp | 28dp |
| C. Pressed state | 8dp | 8dp | 12dp | 16dp | 16dp |

## Baseline tokens

Use the table's menu to select a token set. Filled, tonal, and outlined icon button tokens are no longer recommended. Standard tokens are still available in the module at the top of the page.

### TOKEN_TABLE

Component: Icon buttons

Configured context tags: None, Medium contrast, Default, High contrast, 3P, Android, Dark, Light

#### Token sets

| Token set | Name | Type | Tokens | Description |
| --- | --- | --- | --- | --- |
| [Deprecated] Icon button - Filled | md.comp.filled-icon-button | COMPONENT | 39 | This token set is deprecated. Use `md.comp.icon-button.filled` instead. |
| [Deprecated] Icon button - Tonal | md.comp.filled-tonal-icon-button | COMPONENT | 39 | This token set is deprecated. Use `md.comp.icon-button.tonal` instead. |
| [Deprecated] Icon button - Outlined | md.outlined-icon-button | COMPONENT | 36 | This token set is deprecated. Use `md.comp.icon-button.outlined` instead. |

#### [Deprecated] Icon button - Filled (md.comp.filled-icon-button)

This token set is deprecated. Use `md.comp.icon-button.filled` instead.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.filled-icon-button.container.size | LENGTH | [Deprecated] Enabled / [Deprecated] Container | 40dp |  | Deprecating in place of width and height tokens |
| md.comp.filled-icon-button.container.width | LENGTH | [Deprecated] Enabled / [Deprecated] Container | 40dp |  | This token is deprecated. Use the corresponding token from either the md.comp.icon-button.filled or md.comp.icon-button.(size) set instead. |
| md.comp.filled-icon-button.container.height | LENGTH | [Deprecated] Enabled / [Deprecated] Container | 40dp |  | This token is deprecated. Use the corresponding token from either the md.comp.icon-button.filled or md.comp.icon-button.(size) set instead. |
| md.comp.filled-icon-button.container.shape | SHAPE | [Deprecated] Enabled / [Deprecated] Container | `md.sys.shape.corner.full` |  | This token is deprecated. Use the corresponding token from either the md.comp.icon-button.filled or md.comp.icon-button.(size) set instead. |
| md.comp.filled-icon-button.container.color | COLOR | [Deprecated] Enabled / [Deprecated] Container | `md.sys.color.primary` |  | This token is deprecated. Use the corresponding token from either the md.comp.icon-button.filled or md.comp.icon-button.(size) set instead. |
| md.comp.filled-icon-button.unselected.container.color | COLOR | [Deprecated] Enabled / [Deprecated] Container | `md.sys.color.surface-container-highest` |  | This token is deprecated. Use the corresponding token from either the md.comp.icon-button.filled or md.comp.icon-button.(size) set instead. |
| md.comp.filled-icon-button.selected.container.color | COLOR | [Deprecated] Enabled / [Deprecated] Container | `md.sys.color.primary` |  | This token is deprecated. Use the corresponding token from either the md.comp.icon-button.filled or md.comp.icon-button.(size) set instead. |
| md.comp.filled-icon-button.icon.size | LENGTH | [Deprecated] Enabled / [Deprecated] Icon | 24dp |  | This token is deprecated. Use the corresponding token from either the md.comp.icon-button.filled or md.comp.icon-button.(size) set instead. |
| md.comp.filled-icon-button.icon.color | COLOR | [Deprecated] Enabled / [Deprecated] Icon | `md.sys.color.on-primary` |  | This token is deprecated. Use the corresponding token from either the md.comp.icon-button.filled or md.comp.icon-button.(size) set instead. |
| md.comp.filled-icon-button.toggle.unselected.icon.color | COLOR | [Deprecated] Enabled / [Deprecated] Icon | `md.sys.color.primary` |  | This token is deprecated. Use the corresponding token from either the md.comp.icon-button.filled or md.comp.icon-button.(size) set instead. |
| md.comp.filled-icon-button.toggle.selected.icon.color | COLOR | [Deprecated] Enabled / [Deprecated] Icon | `md.sys.color.on-primary` |  | This token is deprecated. Use the corresponding token from either the md.comp.icon-button.filled or md.comp.icon-button.(size) set instead. |
| md.comp.filled-icon-button.disabled.container.color | COLOR | [Deprecated] Disabled / [Deprecated] Container | `md.sys.color.on-surface` |  | This token is deprecated. Use the corresponding token from either the md.comp.icon-button.filled or md.comp.icon-button.(size) set instead. |
| md.comp.filled-icon-button.disabled.container.opacity | OPACITY | [Deprecated] Disabled / [Deprecated] Container | 0.12 |  | This token is deprecated. Use the corresponding token from either the md.comp.icon-button.filled or md.comp.icon-button.(size) set instead. |
| md.comp.filled-icon-button.disabled.icon.color | COLOR | [Deprecated] Disabled / [Deprecated] Icon | `md.sys.color.on-surface` |  | This token is deprecated. Use the corresponding token from either the md.comp.icon-button.filled or md.comp.icon-button.(size) set instead. |
| md.comp.filled-icon-button.disabled.icon.opacity | OPACITY | [Deprecated] Disabled / [Deprecated] Icon | 0.38 |  | This token is deprecated. Use the corresponding token from either the md.comp.icon-button.filled or md.comp.icon-button.(size) set instead. |
| md.comp.filled-icon-button.hover.state-layer.opacity | OPACITY | [Deprecated] Hovered / [Deprecated] State layer | `md.sys.state.hover.state-layer-opacity` |  | This token is deprecated. Use the corresponding token from either the md.comp.icon-button.filled or md.comp.icon-button.(size) set instead. |
| md.comp.filled-icon-button.hover.state-layer.color | COLOR | [Deprecated] Hovered / [Deprecated] State layer | `md.sys.color.on-primary` |  | This token is deprecated. Use the corresponding token from either the md.comp.icon-button.filled or md.comp.icon-button.(size) set instead. |
| md.comp.filled-icon-button.toggle.unselected.hover.state-layer.color | COLOR | [Deprecated] Hovered / [Deprecated] State layer | `md.sys.color.primary` |  | This token is deprecated. Use the corresponding token from either the md.comp.icon-button.filled or md.comp.icon-button.(size) set instead. |
| md.comp.filled-icon-button.toggle.selected.hover.state-layer.color | COLOR | [Deprecated] Hovered / [Deprecated] State layer | `md.sys.color.on-primary` |  | This token is deprecated. Use the corresponding token from either the md.comp.icon-button.filled or md.comp.icon-button.(size) set instead. |
| md.comp.filled-icon-button.hover.icon.color | COLOR | [Deprecated] Hovered / [Deprecated] Icon | `md.sys.color.on-primary` |  | This token is deprecated. Use the corresponding token from either the md.comp.icon-button.filled or md.comp.icon-button.(size) set instead. |
| md.comp.filled-icon-button.toggle.unselected.hover.icon.color | COLOR | [Deprecated] Hovered / [Deprecated] Icon | `md.sys.color.primary` |  | This token is deprecated. Use the corresponding token from either the md.comp.icon-button.filled or md.comp.icon-button.(size) set instead. |
| md.comp.filled-icon-button.toggle.selected.hover.icon.color | COLOR | [Deprecated] Hovered / [Deprecated] Icon | `md.sys.color.on-primary` |  | This token is deprecated. Use the corresponding token from either the md.comp.icon-button.filled or md.comp.icon-button.(size) set instead. |
| md.comp.filled-icon-button.focus.indicator.color | COLOR | [Deprecated] Focused / [Deprecated] Focus indicator | `md.sys.color.secondary` |  | This token is deprecated. Use the corresponding token from either the md.comp.icon-button.filled or md.comp.icon-button.(size) set instead. |
| md.comp.filled-icon-button.focus.indicator.thickness | LENGTH | [Deprecated] Focused / [Deprecated] Focus indicator | `md.sys.state.focus-indicator.thickness` |  | This token is deprecated. Use the corresponding token from either the md.comp.icon-button.filled or md.comp.icon-button.(size) set instead. |
| md.comp.filled-icon-button.focus.indicator.outline.offset | LENGTH | [Deprecated] Focused / [Deprecated] Focus indicator | `md.sys.state.focus-indicator.outer-offset` |  | This token is deprecated. Use the corresponding token from either the md.comp.icon-button.filled or md.comp.icon-button.(size) set instead. |
| md.comp.filled-icon-button.focus.state-layer.opacity | OPACITY | [Deprecated] Focused / [Deprecated] State layer | `md.sys.state.focus.state-layer-opacity` |  | This token is deprecated. Use the corresponding token from either the md.comp.icon-button.filled or md.comp.icon-button.(size) set instead. |
| md.comp.filled-icon-button.focus.state-layer.color | COLOR | [Deprecated] Focused / [Deprecated] State layer | `md.sys.color.on-primary` |  | This token is deprecated. Use the corresponding token from either the md.comp.icon-button.filled or md.comp.icon-button.(size) set instead. |
| md.comp.filled-icon-button.toggle.unselected.focus.state-layer.color | COLOR | [Deprecated] Focused / [Deprecated] State layer | `md.sys.color.primary` |  | This token is deprecated. Use the corresponding token from either the md.comp.icon-button.filled or md.comp.icon-button.(size) set instead. |
| md.comp.filled-icon-button.toggle.selected.focus.state-layer.color | COLOR | [Deprecated] Focused / [Deprecated] State layer | `md.sys.color.on-primary` |  | This token is deprecated. Use the corresponding token from either the md.comp.icon-button.filled or md.comp.icon-button.(size) set instead. |
| md.comp.filled-icon-button.focus.icon.color | COLOR | [Deprecated] Focused / [Deprecated] Icon | `md.sys.color.on-primary` |  | This token is deprecated. Use the corresponding token from either the md.comp.icon-button.filled or md.comp.icon-button.(size) set instead. |
| md.comp.filled-icon-button.toggle.unselected.focus.icon.color | COLOR | [Deprecated] Focused / [Deprecated] Icon | `md.sys.color.primary` |  | This token is deprecated. Use the corresponding token from either the md.comp.icon-button.filled or md.comp.icon-button.(size) set instead. |
| md.comp.filled-icon-button.toggle.selected.focus.icon.color | COLOR | [Deprecated] Focused / [Deprecated] Icon | `md.sys.color.on-primary` |  | This token is deprecated. Use the corresponding token from either the md.comp.icon-button.filled or md.comp.icon-button.(size) set instead. |
| md.comp.filled-icon-button.pressed.state-layer.opacity | OPACITY | [Deprecated] Pressed (ripple) / [Deprecated] State layer | `md.sys.state.pressed.state-layer-opacity` |  | This token is deprecated. Use the corresponding token from either the md.comp.icon-button.filled or md.comp.icon-button.(size) set instead. |
| md.comp.filled-icon-button.pressed.state-layer.color | COLOR | [Deprecated] Pressed (ripple) / [Deprecated] State layer | `md.sys.color.on-primary` |  | This token is deprecated. Use the corresponding token from either the md.comp.icon-button.filled or md.comp.icon-button.(size) set instead. |
| md.comp.filled-icon-button.toggle.unselected.pressed.state-layer.color | COLOR | [Deprecated] Pressed (ripple) / [Deprecated] State layer | `md.sys.color.primary` |  | This token is deprecated. Use the corresponding token from either the md.comp.icon-button.filled or md.comp.icon-button.(size) set instead. |
| md.comp.filled-icon-button.toggle.selected.pressed.state-layer.color | COLOR | [Deprecated] Pressed (ripple) / [Deprecated] State layer | `md.sys.color.on-primary` |  | This token is deprecated. Use the corresponding token from either the md.comp.icon-button.filled or md.comp.icon-button.(size) set instead. |
| md.comp.filled-icon-button.pressed.icon.color | COLOR | [Deprecated] Pressed (ripple) / [Deprecated] Icon | `md.sys.color.on-primary` |  | This token is deprecated. Use the corresponding token from either the md.comp.icon-button.filled or md.comp.icon-button.(size) set instead. |
| md.comp.filled-icon-button.toggle.unselected.pressed.icon.color | COLOR | [Deprecated] Pressed (ripple) / [Deprecated] Icon | `md.sys.color.primary` |  | This token is deprecated. Use the corresponding token from either the md.comp.icon-button.filled or md.comp.icon-button.(size) set instead. |
| md.comp.filled-icon-button.toggle.selected.pressed.icon.color | COLOR | [Deprecated] Pressed (ripple) / [Deprecated] Icon | `md.sys.color.on-primary` |  | This token is deprecated. Use the corresponding token from either the md.comp.icon-button.filled or md.comp.icon-button.(size) set instead. |

#### [Deprecated] Icon button - Tonal (md.comp.filled-tonal-icon-button)

This token set is deprecated. Use `md.comp.icon-button.tonal` instead.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.filled-tonal-icon-button.container.size | LENGTH | [Deprecated] Enabled / [Deprecated] Container | 40dp |  | Deprecating in place of width and height tokens |
| md.comp.filled-tonal-icon-button.container.width | LENGTH | [Deprecated] Enabled / [Deprecated] Container | 40dp |  | This token is deprecated. Use the corresponding token from either the md.comp.icon-button.tonal or md.comp.icon-button.(size) set instead. |
| md.comp.filled-tonal-icon-button.container.height | LENGTH | [Deprecated] Enabled / [Deprecated] Container | 40dp |  | This token is deprecated. Use the corresponding token from either the md.comp.icon-button.tonal or md.comp.icon-button.(size) set instead. |
| md.comp.filled-tonal-icon-button.container.shape | SHAPE | [Deprecated] Enabled / [Deprecated] Container | `md.sys.shape.corner.full` |  | This token is deprecated. Use the corresponding token from either the md.comp.icon-button.tonal or md.comp.icon-button.(size) set instead. |
| md.comp.filled-tonal-icon-button.container.color | COLOR | [Deprecated] Enabled / [Deprecated] Container | `md.sys.color.secondary-container` |  | This token is deprecated. Use the corresponding token from either the md.comp.icon-button.tonal or md.comp.icon-button.(size) set instead. |
| md.comp.filled-tonal-icon-button.unselected.container.color | COLOR | [Deprecated] Enabled / [Deprecated] Container | `md.sys.color.surface-container-highest` |  | This token is deprecated. Use the corresponding token from either the md.comp.icon-button.tonal or md.comp.icon-button.(size) set instead. |
| md.comp.filled-tonal-icon-button.selected.container.color | COLOR | [Deprecated] Enabled / [Deprecated] Container | `md.sys.color.secondary-container` |  | This token is deprecated. Use the corresponding token from either the md.comp.icon-button.tonal or md.comp.icon-button.(size) set instead. |
| md.comp.filled-tonal-icon-button.icon.size | LENGTH | [Deprecated] Enabled / [Deprecated] Icon | 24dp |  | This token is deprecated. Use the corresponding token from either the md.comp.icon-button.tonal or md.comp.icon-button.(size) set instead. |
| md.comp.filled-tonal-icon-button.icon.color | COLOR | [Deprecated] Enabled / [Deprecated] Icon | `md.sys.color.on-secondary-container` |  | This token is deprecated. Use the corresponding token from either the md.comp.icon-button.tonal or md.comp.icon-button.(size) set instead. |
| md.comp.filled-tonal-icon-button.toggle.unselected.icon.color | COLOR | [Deprecated] Enabled / [Deprecated] Icon | `md.sys.color.on-surface-variant` |  | This token is deprecated. Use the corresponding token from either the md.comp.icon-button.tonal or md.comp.icon-button.(size) set instead. |
| md.comp.filled-tonal-icon-button.toggle.selected.icon.color | COLOR | [Deprecated] Enabled / [Deprecated] Icon | `md.sys.color.on-secondary-container` |  | This token is deprecated. Use the corresponding token from either the md.comp.icon-button.tonal or md.comp.icon-button.(size) set instead. |
| md.comp.filled-tonal-icon-button.disabled.container.color | COLOR | [Deprecated] Disabled / [Deprecated] Container | `md.sys.color.on-surface` |  | This token is deprecated. Use the corresponding token from either the md.comp.icon-button.tonal or md.comp.icon-button.(size) set instead. |
| md.comp.filled-tonal-icon-button.disabled.container.opacity | OPACITY | [Deprecated] Disabled / [Deprecated] Container | 0.12 |  | This token is deprecated. Use the corresponding token from either the md.comp.icon-button.tonal or md.comp.icon-button.(size) set instead. |
| md.comp.filled-tonal-icon-button.disabled.icon.color | COLOR | [Deprecated] Disabled / [Deprecated] Icon | `md.sys.color.on-surface` |  | This token is deprecated. Use the corresponding token from either the md.comp.icon-button.tonal or md.comp.icon-button.(size) set instead. |
| md.comp.filled-tonal-icon-button.disabled.icon.opacity | OPACITY | [Deprecated] Disabled / [Deprecated] Icon | 0.38 |  | This token is deprecated. Use the corresponding token from either the md.comp.icon-button.tonal or md.comp.icon-button.(size) set instead. |
| md.comp.filled-tonal-icon-button.hover.state-layer.color | COLOR | [Deprecated] Hovered / [Deprecated] State layer | `md.sys.color.on-secondary-container` |  | This token is deprecated. Use the corresponding token from either the md.comp.icon-button.tonal or md.comp.icon-button.(size) set instead. |
| md.comp.filled-tonal-icon-button.toggle.unselected.hover.state-layer.color | COLOR | [Deprecated] Hovered / [Deprecated] State layer | `md.sys.color.on-surface-variant` |  | This token is deprecated. Use the corresponding token from either the md.comp.icon-button.tonal or md.comp.icon-button.(size) set instead. |
| md.comp.filled-tonal-icon-button.toggle.selected.hover.state-layer.color | COLOR | [Deprecated] Hovered / [Deprecated] State layer | `md.sys.color.on-secondary-container` |  | This token is deprecated. Use the corresponding token from either the md.comp.icon-button.tonal or md.comp.icon-button.(size) set instead. |
| md.comp.filled-tonal-icon-button.hover.state-layer.opacity | OPACITY | [Deprecated] Hovered / [Deprecated] State layer | `md.sys.state.hover.state-layer-opacity` |  | This token is deprecated. Use the corresponding token from either the md.comp.icon-button.tonal or md.comp.icon-button.(size) set instead. |
| md.comp.filled-tonal-icon-button.hover.icon.color | COLOR | [Deprecated] Hovered / [Deprecated] Icon | `md.sys.color.on-secondary-container` |  | This token is deprecated. Use the corresponding token from either the md.comp.icon-button.tonal or md.comp.icon-button.(size) set instead. |
| md.comp.filled-tonal-icon-button.toggle.unselected.hover.icon.color | COLOR | [Deprecated] Hovered / [Deprecated] Icon | `md.sys.color.on-surface-variant` |  | This token is deprecated. Use the corresponding token from either the md.comp.icon-button.tonal or md.comp.icon-button.(size) set instead. |
| md.comp.filled-tonal-icon-button.toggle.selected.hover.icon.color | COLOR | [Deprecated] Hovered / [Deprecated] Icon | `md.sys.color.on-secondary-container` |  | This token is deprecated. Use the corresponding token from either the md.comp.icon-button.tonal or md.comp.icon-button.(size) set instead. |
| md.comp.filled-tonal-icon-button.focus.indicator.color | COLOR | [Deprecated] Focused / [Deprecated] Focus indicator | `md.sys.color.secondary` |  | This token is deprecated. Use the corresponding token from either the md.comp.icon-button.tonal or md.comp.icon-button.(size) set instead. |
| md.comp.filled-tonal-icon-button.focus.indicator.thickness | LENGTH | [Deprecated] Focused / [Deprecated] Focus indicator | `md.sys.state.focus-indicator.thickness` |  | This token is deprecated. Use the corresponding token from either the md.comp.icon-button.tonal or md.comp.icon-button.(size) set instead. |
| md.comp.filled-tonal-icon-button.focus.indicator.outline.offset | LENGTH | [Deprecated] Focused / [Deprecated] Focus indicator | `md.sys.state.focus-indicator.outer-offset` |  | This token is deprecated. Use the corresponding token from either the md.comp.icon-button.tonal or md.comp.icon-button.(size) set instead. |
| md.comp.filled-tonal-icon-button.focus.state-layer.opacity | OPACITY | [Deprecated] Focused / [Deprecated] State layer | `md.sys.state.focus.state-layer-opacity` |  | This token is deprecated. Use the corresponding token from either the md.comp.icon-button.tonal or md.comp.icon-button.(size) set instead. |
| md.comp.filled-tonal-icon-button.focus.state-layer.color | COLOR | [Deprecated] Focused / [Deprecated] State layer | `md.sys.color.on-secondary-container` |  | This token is deprecated. Use the corresponding token from either the md.comp.icon-button.tonal or md.comp.icon-button.(size) set instead. |
| md.comp.filled-tonal-icon-button.toggle.unselected.focus.state-layer.color | COLOR | [Deprecated] Focused / [Deprecated] State layer | `md.sys.color.on-surface-variant` |  | This token is deprecated. Use the corresponding token from either the md.comp.icon-button.tonal or md.comp.icon-button.(size) set instead. |
| md.comp.filled-tonal-icon-button.toggle.selected.focus.state-layer.color | COLOR | [Deprecated] Focused / [Deprecated] State layer | `md.sys.color.on-secondary-container` |  | This token is deprecated. Use the corresponding token from either the md.comp.icon-button.tonal or md.comp.icon-button.(size) set instead. |
| md.comp.filled-tonal-icon-button.focus.icon.color | COLOR | [Deprecated] Focused / [Deprecated] Icon | `md.sys.color.on-secondary-container` |  | This token is deprecated. Use the corresponding token from either the md.comp.icon-button.tonal or md.comp.icon-button.(size) set instead. |
| md.comp.filled-tonal-icon-button.toggle.unselected.focus.icon.color | COLOR | [Deprecated] Focused / [Deprecated] Icon | `md.sys.color.on-surface-variant` |  | This token is deprecated. Use the corresponding token from either the md.comp.icon-button.tonal or md.comp.icon-button.(size) set instead. |
| md.comp.filled-tonal-icon-button.toggle.selected.focus.icon.color | COLOR | [Deprecated] Focused / [Deprecated] Icon | `md.sys.color.on-secondary-container` |  | This token is deprecated. Use the corresponding token from either the md.comp.icon-button.tonal or md.comp.icon-button.(size) set instead. |
| md.comp.filled-tonal-icon-button.pressed.state-layer.opacity | OPACITY | [Deprecated] Pressed (ripple) / [Deprecated] State layer | `md.sys.state.pressed.state-layer-opacity` |  | This token is deprecated. Use the corresponding token from either the md.comp.icon-button.tonal or md.comp.icon-button.(size) set instead. |
| md.comp.filled-tonal-icon-button.pressed.state-layer.color | COLOR | [Deprecated] Pressed (ripple) / [Deprecated] State layer | `md.sys.color.on-secondary-container` |  | This token is deprecated. Use the corresponding token from either the md.comp.icon-button.tonal or md.comp.icon-button.(size) set instead. |
| md.comp.filled-tonal-icon-button.toggle.unselected.pressed.state-layer.color | COLOR | [Deprecated] Pressed (ripple) / [Deprecated] State layer | `md.sys.color.on-surface-variant` |  | This token is deprecated. Use the corresponding token from either the md.comp.icon-button.tonal or md.comp.icon-button.(size) set instead. |
| md.comp.filled-tonal-icon-button.toggle.selected.pressed.state-layer.color | COLOR | [Deprecated] Pressed (ripple) / [Deprecated] State layer | `md.sys.color.on-secondary-container` |  | This token is deprecated. Use the corresponding token from either the md.comp.icon-button.tonal or md.comp.icon-button.(size) set instead. |
| md.comp.filled-tonal-icon-button.pressed.icon.color | COLOR | [Deprecated] Pressed (ripple) / Icon | `md.sys.color.on-secondary-container` |  | This token is deprecated. Use the corresponding token from either the md.comp.icon-button.tonal or md.comp.icon-button.(size) set instead. |
| md.comp.filled-tonal-icon-button.toggle.unselected.pressed.icon.color | COLOR | [Deprecated] Pressed (ripple) / Icon | `md.sys.color.on-surface-variant` |  | This token is deprecated. Use the corresponding token from either the md.comp.icon-button.tonal or md.comp.icon-button.(size) set instead. |
| md.comp.filled-tonal-icon-button.toggle.selected.pressed.icon.color | COLOR | [Deprecated] Pressed (ripple) / Icon | `md.sys.color.on-secondary-container` |  | This token is deprecated. Use the corresponding token from either the md.comp.icon-button.tonal or md.comp.icon-button.(size) set instead. |

#### [Deprecated] Icon button - Outlined (md.outlined-icon-button)

This token set is deprecated. Use `md.comp.icon-button.outlined` instead.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.outlined-icon-button.container.size | LENGTH | [Deprecated] Enabled / [Deprecated] Container | 40dp |  | Deprecating in place of width and height tokens |
| md.outlined-icon-button.container.width | LENGTH | [Deprecated] Enabled / [Deprecated] Container | 40dp |  | This token is deprecated. Use the corresponding token from either the md.comp.icon-button.outlined or md.comp.icon-button.(size) set instead. |
| md.outlined-icon-button.container.height | LENGTH | [Deprecated] Enabled / [Deprecated] Container | 40dp |  | This token is deprecated. Use the corresponding token from either the md.comp.icon-button.outlined or md.comp.icon-button.(size) set instead. |
| md.outlined-icon-button.container.shape | SHAPE | [Deprecated] Enabled / [Deprecated] Container | `md.sys.shape.corner.full` |  | This token is deprecated. Use the corresponding token from either the md.comp.icon-button.outlined or md.comp.icon-button.(size) set instead. |
| md.outlined-icon-button.selected.container.color | COLOR | [Deprecated] Enabled / [Deprecated] Container | `md.sys.color.inverse-surface` |  | This token is deprecated. Use the corresponding token from either the md.comp.icon-button.outlined or md.comp.icon-button.(size) set instead. |
| md.outlined-icon-button.unselected.outline.color | COLOR | [Deprecated] Enabled / [Deprecated] Outline | `md.sys.color.outline` |  | This token is deprecated. Use the corresponding token from either the md.comp.icon-button.outlined or md.comp.icon-button.(size) set instead. |
| md.outlined-icon-button.unselected.outline.width | LENGTH | [Deprecated] Enabled / [Deprecated] Outline | 1dp |  | This token is deprecated. Use the corresponding token from either the md.comp.icon-button.outlined or md.comp.icon-button.(size) set instead. |
| md.outlined-icon-button.icon.size | LENGTH | [Deprecated] Enabled / [Deprecated] Icon | 24dp |  | This token is deprecated. Use the corresponding token from either the md.comp.icon-button.outlined or md.comp.icon-button.(size) set instead. |
| md.outlined-icon-button.unselected.icon.color | COLOR | [Deprecated] Enabled / [Deprecated] Icon | `md.sys.color.on-surface-variant` |  | This token is deprecated. Use the corresponding token from either the md.comp.icon-button.outlined or md.comp.icon-button.(size) set instead. |
| md.outlined-icon-button.selected.icon.color | COLOR | [Deprecated] Enabled / [Deprecated] Icon | `md.sys.color.inverse-on-surface` |  | This token is deprecated. Use the corresponding token from either the md.comp.icon-button.outlined or md.comp.icon-button.(size) set instead. |
| md.outlined-icon-button.disabled.selected.container.color | COLOR | [Deprecated] Disabled / [Deprecated] Container | `md.sys.color.on-surface` |  | This token is deprecated. Use the corresponding token from either the md.comp.icon-button.outlined or md.comp.icon-button.(size) set instead. |
| md.outlined-icon-button.disabled.selected.container.opacity | OPACITY | [Deprecated] Disabled / [Deprecated] Container | 0.12 |  | This token is deprecated. Use the corresponding token from either the md.comp.icon-button.outlined or md.comp.icon-button.(size) set instead. |
| md.outlined-icon-button.disabled.icon.color | COLOR | [Deprecated] Disabled / [Deprecated] Icon | `md.sys.color.on-surface` |  | This token is deprecated. Use the corresponding token from either the md.comp.icon-button.outlined or md.comp.icon-button.(size) set instead. |
| md.outlined-icon-button.disabled.icon.opacity | OPACITY | [Deprecated] Disabled / [Deprecated] Icon | 0.38 |  | This token is deprecated. Use the corresponding token from either the md.comp.icon-button.outlined or md.comp.icon-button.(size) set instead. |
| md.outlined-icon-button.disabled.unselected.outline.color | COLOR | [Deprecated] Disabled / [Deprecated] Outline | `md.sys.color.on-surface` |  | This token is deprecated. Use the corresponding token from either the md.comp.icon-button.outlined or md.comp.icon-button.(size) set instead. |
| md.outlined-icon-button.disabled.unselected.outline.opacity | OPACITY | [Deprecated] Disabled / [Deprecated] Outline | 0.12 |  | This token is deprecated. Use the corresponding token from either the md.comp.icon-button.outlined or md.comp.icon-button.(size) set instead. |
| md.outlined-icon-button.disabled.outline.color | COLOR | [Deprecated] Disabled / [Deprecated] Outline | `md.sys.color.on-surface` |  | As per spec the outlined icon buttons do use a different disabled style between selected and unselected. Use newly added "selected." and "unselected." tokens instead. |
| md.outlined-icon-button.disabled.outline.opacity | OPACITY | [Deprecated] Disabled / [Deprecated] Outline | 0.12 |  | As per spec the outlined icon buttons do use a different disabled style between selected and unselected. Use newly added "selected." and "unselected." tokens instead. |
| md.outlined-icon-button.hover.state-layer.opacity | OPACITY | [Deprecated] Hovered / [Deprecated] State layer | `md.sys.state.hover.state-layer-opacity` |  | This token is deprecated. Use the corresponding token from either the md.comp.icon-button.outlined or md.comp.icon-button.(size) set instead. |
| md.outlined-icon-button.unselected.hover.state-layer.color | COLOR | [Deprecated] Hovered / [Deprecated] State layer | `md.sys.color.on-surface-variant` |  | This token is deprecated. Use the corresponding token from either the md.comp.icon-button.outlined or md.comp.icon-button.(size) set instead. |
| md.outlined-icon-button.selected.hover.state-layer.color | COLOR | [Deprecated] Hovered / [Deprecated] State layer | `md.sys.color.inverse-on-surface` |  | This token is deprecated. Use the corresponding token from either the md.comp.icon-button.outlined or md.comp.icon-button.(size) set instead. |
| md.outlined-icon-button.unselected.hover.icon.color | COLOR | [Deprecated] Hovered / [Deprecated] Icon | `md.sys.color.on-surface-variant` |  | This token is deprecated. Use the corresponding token from either the md.comp.icon-button.outlined or md.comp.icon-button.(size) set instead. |
| md.outlined-icon-button.selected.hover.icon.color | COLOR | [Deprecated] Hovered / [Deprecated] Icon | `md.sys.color.inverse-on-surface` |  | This token is deprecated. Use the corresponding token from either the md.comp.icon-button.outlined or md.comp.icon-button.(size) set instead. |
| md.outlined-icon-button.focus.indicator.color | COLOR | [Deprecated] Focused / [Deprecated] Focus indicator | `md.sys.color.secondary` |  | This token is deprecated. Use the corresponding token from either the md.comp.icon-button.outlined or md.comp.icon-button.(size) set instead. |
| md.outlined-icon-button.focus.indicator.thickness | LENGTH | [Deprecated] Focused / [Deprecated] Focus indicator | `md.sys.state.focus-indicator.thickness` |  | This token is deprecated. Use the corresponding token from either the md.comp.icon-button.outlined or md.comp.icon-button.(size) set instead. |
| md.outlined-icon-button.focus.indicator.outline.offset | LENGTH | [Deprecated] Focused / [Deprecated] Focus indicator | `md.sys.state.focus-indicator.outer-offset` |  | This token is deprecated. Use the corresponding token from either the md.comp.icon-button.outlined or md.comp.icon-button.(size) set instead. |
| md.outlined-icon-button.focus.state-layer.opacity | OPACITY | [Deprecated] Focused / [Deprecated] State layer | `md.sys.state.hover.state-layer-opacity` |  | This token is deprecated. Use the corresponding token from either the md.comp.icon-button.outlined or md.comp.icon-button.(size) set instead. |
| md.outlined-icon-button.unselected.focus.state-layer.color | COLOR | [Deprecated] Focused / [Deprecated] State layer | `md.sys.color.on-surface-variant` |  | This token is deprecated. Use the corresponding token from either the md.comp.icon-button.outlined or md.comp.icon-button.(size) set instead. |
| md.outlined-icon-button.selected.focus.state-layer.color | COLOR | [Deprecated] Focused / [Deprecated] State layer | `md.sys.color.inverse-on-surface` |  | This token is deprecated. Use the corresponding token from either the md.comp.icon-button.outlined or md.comp.icon-button.(size) set instead. |
| md.outlined-icon-button.unselected.focus.icon.color | COLOR | [Deprecated] Focused / [Deprecated] Icon | `md.sys.color.on-surface-variant` |  | This token is deprecated. Use the corresponding token from either the md.comp.icon-button.outlined or md.comp.icon-button.(size) set instead. |
| md.outlined-icon-button.selected.focus.icon.color | COLOR | [Deprecated] Focused / [Deprecated] Icon | `md.sys.color.inverse-on-surface` |  | This token is deprecated. Use the corresponding token from either the md.comp.icon-button.outlined or md.comp.icon-button.(size) set instead. |
| md.outlined-icon-button.pressed.state-layer.opacity | OPACITY | [Deprecated] Pressed (ripple) / [Deprecated] State layer | `md.sys.state.pressed.state-layer-opacity` |  | This token is deprecated. Use the corresponding token from either the md.comp.icon-button.outlined or md.comp.icon-button.(size) set instead. |
| md.outlined-icon-button.unselected.pressed.state-layer.color | COLOR | [Deprecated] Pressed (ripple) / [Deprecated] State layer | `md.sys.color.on-surface` |  | This token is deprecated. Use the corresponding token from either the md.comp.icon-button.outlined or md.comp.icon-button.(size) set instead. |
| md.outlined-icon-button.selected.pressed.state-layer.color | COLOR | [Deprecated] Pressed (ripple) / [Deprecated] State layer | `md.sys.color.inverse-on-surface` |  | This token is deprecated. Use the corresponding token from either the md.comp.icon-button.outlined or md.comp.icon-button.(size) set instead. |
| md.outlined-icon-button.unselected.pressed.icon.color | COLOR | [Deprecated] Pressed (ripple) / Icon | `md.sys.color.on-surface` |  | This token is deprecated. Use the corresponding token from either the md.comp.icon-button.outlined or md.comp.icon-button.(size) set instead. |
| md.outlined-icon-button.selected.pressed.icon.color | COLOR | [Deprecated] Pressed (ripple) / Icon | `md.sys.color.inverse-on-surface` |  | This token is deprecated. Use the corresponding token from either the md.comp.icon-button.outlined or md.comp.icon-button.(size) set instead. |

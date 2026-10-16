---
title: "Buttons"
source_url: "https://m3.material.io/components/buttons/specs"
lastmod: "2026-05-18"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "components"
assets:
  - "assets/images/69237f19b6c4-mnhhn4lk-01-3P.png"
  - "assets/images/8b8c280373e7-mmneq41m-02-3P.png"
  - "assets/images/cef051a7a748-mmneqix9-03-3P.png"
  - "assets/images/541937492a23-mmner6e7-04-3P.png"
  - "assets/images/3ce412947ae2-mmnesmjm-06-3P.png"
  - "assets/images/fcbd5765614b-mmnet1qn-07-3P.png"
  - "assets/images/4ccc8ece1219-mmneu2zl-08-3P.png"
  - "assets/images/f37fd3b5febd-mnhkhbya-09-3P.png"
  - "assets/images/667a6d930f94-mmneuv7c-10-3P.png"
  - "assets/images/39c5568fe0b1-mmnevsol-11-3P.png"
  - "assets/images/db8ffb04e721-mmnewg2s-12-3P.png"
  - "assets/images/4c1d06a35bc0-mmnexggd-13-3P.png"
  - "assets/images/7449244fafbc-mmneyf25-14-3P.png"
  - "assets/images/66b7aed7ffc0-mmneyrwe-15-3P.png"
  - "assets/images/6183600d7529-mmnezbyl-16-3P.png"
  - "assets/images/020667773421-mmnf0bsj-17-3P.png"
  - "assets/images/4d1260e9c76b-mmnf0o8e-18-3P.png"
  - "assets/images/6897638d4a1f-mmnf14u5-19-3P.png"
---

# Buttons

Buttons prompt most actions in a UI

## Specs

## Variants

![Diagram comparing buttons with toggle buttons.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmnhhn4lk-01-3P.png?alt=media&token=f2e9d292-4cc2-48bc-89c5-91fedff0ecf1>)

_Default button; Toggle button_

| Variant | M3 | M3 Expressive |
| --- | --- | --- |
| Default | Available | Available |
| Toggle (selection) | \-- | Available |

## Configurations

![Diagram showing configurations of buttons.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmmneq41m-02-3P.png?alt=media&token=505244df-55c9-4406-83ef-9af3c370fb89>)

_Size; Shape; Color; Small button padding_

| Category | Configuration | M3 | M3 Expressive |
| --- | --- | --- | --- |
| Size | Small (default) | Available | Available |
| XS, M, L, XL | \-- | Available |  |
| Shape | Round (default) | Available | Available |
| Square | \-- | Available |  |
| Color | Elevated, filled (default), tonal, outlined, text | Available | Available |
| Small button padding | 24dp | Available | Not recommended. Use 16dp |
| 16dp | \-- | Available |  |

## Tokens & specs

Use the table's menu to select a token set. Button token sets are separated into common tokens, color, and size. [View baseline tokens](specs.md#c305d304-a6c0-466a-a48c-8d0718a29ae2)

### TOKEN_TABLE

Component: Buttons

Configured context tags: None, Medium contrast, Default, High contrast, 3P, Android, Dark, Light

#### Token sets

| Token set | Name | Type | Tokens | Description |
| --- | --- | --- | --- | --- |
| Button - Color - Elevated | md.comp.button.elevated | COMPONENT | 51 | For elevated buttons. |
| Button - Color - Filled | md.comp.button.filled | COMPONENT | 51 | For filled buttons. |
| Button - Color - Tonal | md.comp.button.tonal | COMPONENT | 51 | For tonal buttons. |
| Button - Color - Outlined | md.comp.button.outlined | COMPONENT | 52 | For outlined buttons. |
| Button - Color - Text | md.comp.button.text | COMPONENT | 20 | For text buttons. |
| Button - Size - Xsmall | md.comp.button.xsmall | COMPONENT | 14 | For XS buttons. |
| Button - Size - Small | md.comp.button.small | COMPONENT | 14 | For small buttons. |
| Button - Size - Medium | md.comp.button.medium | COMPONENT | 14 | For medium buttons. |
| Button - Size - Large | md.comp.button.large | COMPONENT | 14 | For large buttons. |
| Button - Size - Xlarge | md.comp.button.xlarge | COMPONENT | 14 | For XL buttons. |

#### Button - Color - Elevated (md.comp.button.elevated)

For elevated buttons.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.button.elevated.container.color | COLOR | Enabled | `md.sys.color.surface-container-low` |  |  |
| md.comp.button.elevated.unselected.container.color | COLOR | Enabled | `md.sys.color.surface-container-low` |  |  |
| md.comp.button.elevated.selected.container.color | COLOR | Enabled | `md.sys.color.primary` |  |  |
| md.comp.button.elevated.container.shadow-color | COLOR | Enabled | `md.sys.color.shadow` |  |  |
| md.comp.button.elevated.container.elevation | ELEVATION | Enabled | `md.sys.elevation.level1` |  |  |
| md.comp.button.elevated.label-text.color | COLOR | Enabled | `md.sys.color.primary` |  |  |
| md.comp.button.elevated.unselected.label-text.color | COLOR | Enabled | `md.sys.color.primary` |  |  |
| md.comp.button.elevated.selected.label-text.color | COLOR | Enabled | `md.sys.color.on-primary` |  |  |
| md.comp.button.elevated.icon.color | COLOR | Enabled | `md.sys.color.primary` |  |  |
| md.comp.button.elevated.unselected.icon.color | COLOR | Enabled | `md.sys.color.primary` |  |  |
| md.comp.button.elevated.selected.icon.color | COLOR | Enabled | `md.sys.color.on-primary` |  |  |
| md.comp.button.elevated.disabled.container.color | COLOR | Disabled | `md.sys.color.on-surface` |  |  |
| md.comp.button.elevated.disabled.container.opacity | OPACITY | Disabled | 0.1 |  |  |
| md.comp.button.elevated.disabled.container.elevation | ELEVATION | Disabled | `md.sys.elevation.level0` |  |  |
| md.comp.button.elevated.disabled.label-text.color | COLOR | Disabled | `md.sys.color.on-surface` |  |  |
| md.comp.button.elevated.disabled.label-text.opacity | OPACITY | Disabled | 0.38 |  |  |
| md.comp.button.elevated.disabled.icon.color | COLOR | Disabled | `md.sys.color.on-surface` |  |  |
| md.comp.button.elevated.disabled.icon.opacity | OPACITY | Disabled | 0.38 |  |  |
| md.comp.button.elevated.hovered.state-layer.color | COLOR | Hovered | `md.sys.color.primary` |  |  |
| md.comp.button.elevated.unselected.hovered.state-layer.color | COLOR | Hovered | `md.sys.color.primary` |  |  |
| md.comp.button.elevated.selected.hovered.state-layer.color | COLOR | Hovered | `md.sys.color.on-primary` |  |  |
| md.comp.button.elevated.hovered.state-layer.opacity | OPACITY | Hovered | `md.sys.state.hover.state-layer-opacity` |  |  |
| md.comp.button.elevated.hovered.container.elevation | ELEVATION | Hovered | `md.sys.elevation.level2` |  | No longer part of the design spec |
| md.comp.button.elevated.hovered.label-text.color | COLOR | Hovered | `md.sys.color.primary` |  |  |
| md.comp.button.elevated.unselected.hovered.label-text.color | COLOR | Hovered | `md.sys.color.primary` |  |  |
| md.comp.button.elevated.selected.hovered.label-text.color | COLOR | Hovered | `md.sys.color.on-primary` |  |  |
| md.comp.button.elevated.hovered.icon.color | COLOR | Hovered | `md.sys.color.primary` |  |  |
| md.comp.button.elevated.unselected.hovered.icon.color | COLOR | Hovered | `md.sys.color.primary` |  |  |
| md.comp.button.elevated.selected.hovered.icon.color | COLOR | Hovered | `md.sys.color.on-primary` |  |  |
| md.comp.button.elevated.focused.state-layer.color | COLOR | Focused | `md.sys.color.primary` |  |  |
| md.comp.button.elevated.unselected.focused.state-layer.color | COLOR | Focused | `md.sys.color.primary` |  |  |
| md.comp.button.elevated.selected.focused.state-layer.color | COLOR | Focused | `md.sys.color.on-primary` |  |  |
| md.comp.button.elevated.focused.state-layer.opacity | OPACITY | Focused | `md.sys.state.focus.state-layer-opacity` |  |  |
| md.comp.button.elevated.focused.container.elevation | ELEVATION | Focused | `md.sys.elevation.level1` |  |  |
| md.comp.button.elevated.focused.label-text.color | COLOR | Focused | `md.sys.color.primary` |  |  |
| md.comp.button.elevated.unselected.focused.label-text.color | COLOR | Focused | `md.sys.color.primary` |  |  |
| md.comp.button.elevated.selected.focused.label-text.color | COLOR | Focused | `md.sys.color.on-primary` |  |  |
| md.comp.button.elevated.focused.icon.color | COLOR | Focused | `md.sys.color.primary` |  |  |
| md.comp.button.elevated.unselected.focused.icon.color | COLOR | Focused | `md.sys.color.primary` |  |  |
| md.comp.button.elevated.selected.focused.icon.color | COLOR | Focused | `md.sys.color.on-primary` |  |  |
| md.comp.button.elevated.pressed.state-layer.color | COLOR | Pressed | `md.sys.color.primary` |  |  |
| md.comp.button.elevated.unselected.pressed.state-layer.color | COLOR | Pressed | `md.sys.color.primary` |  |  |
| md.comp.button.elevated.selected.pressed.state-layer.color | COLOR | Pressed | `md.sys.color.on-primary` |  |  |
| md.comp.button.elevated.pressed.state-layer.opacity | OPACITY | Pressed | `md.sys.state.pressed.state-layer-opacity` |  |  |
| md.comp.button.elevated.pressed.container.elevation | ELEVATION | Pressed | `md.sys.elevation.level1` |  |  |
| md.comp.button.elevated.pressed.label-text.color | COLOR | Pressed | `md.sys.color.primary` |  |  |
| md.comp.button.elevated.unselected.pressed.label-text.color | COLOR | Pressed | `md.sys.color.primary` |  |  |
| md.comp.button.elevated.selected.pressed.label-text.color | COLOR | Pressed | `md.sys.color.on-primary` |  |  |
| md.comp.button.elevated.pressed.icon.color | COLOR | Pressed | `md.sys.color.primary` |  |  |
| md.comp.button.elevated.unselected.pressed.icon.color | COLOR | Pressed | `md.sys.color.primary` |  |  |
| md.comp.button.elevated.selected.pressed.icon.color | COLOR | Pressed | `md.sys.color.on-primary` |  |  |

#### Button - Color - Filled (md.comp.button.filled)

For filled buttons.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.button.filled.container.color | COLOR | Enabled | `md.sys.color.primary` |  |  |
| md.comp.button.filled.unselected.container.color | COLOR | Enabled | `md.sys.color.surface-container` |  |  |
| md.comp.button.filled.selected.container.color | COLOR | Enabled | `md.sys.color.primary` |  |  |
| md.comp.button.filled.container.shadow-color | COLOR | Enabled | `md.sys.color.shadow` |  |  |
| md.comp.button.filled.container.elevation | ELEVATION | Enabled | `md.sys.elevation.level0` |  |  |
| md.comp.button.filled.label-text.color | COLOR | Enabled | `md.sys.color.on-primary` |  |  |
| md.comp.button.filled.unselected.label-text.color | COLOR | Enabled | `md.sys.color.on-surface-variant` |  |  |
| md.comp.button.filled.selected.label-text.color | COLOR | Enabled | `md.sys.color.on-primary` |  |  |
| md.comp.button.filled.icon.color | COLOR | Enabled | `md.sys.color.on-primary` |  |  |
| md.comp.button.filled.unselected.icon.color | COLOR | Enabled | `md.sys.color.on-surface-variant` |  |  |
| md.comp.button.filled.selected.icon.color | COLOR | Enabled | `md.sys.color.on-primary` |  |  |
| md.comp.button.filled.disabled.container.color | COLOR | Disabled | `md.sys.color.on-surface` |  |  |
| md.comp.button.filled.disabled.container.opacity | OPACITY | Disabled | 0.1 |  |  |
| md.comp.button.filled.disabled.container.elevation | ELEVATION | Disabled | `md.sys.elevation.level0` |  |  |
| md.comp.button.filled.disabled.label-text.color | COLOR | Disabled | `md.sys.color.on-surface` |  |  |
| md.comp.button.filled.disabled.label-text.opacity | OPACITY | Disabled | 0.38 |  |  |
| md.comp.button.filled.disabled.icon.color | COLOR | Disabled | `md.sys.color.on-surface` |  |  |
| md.comp.button.filled.disabled.icon.opacity | OPACITY | Disabled | 0.38 |  |  |
| md.comp.button.filled.hovered.state-layer.color | COLOR | Hovered | `md.sys.color.on-primary` |  |  |
| md.comp.button.filled.unselected.hovered.state-layer.color | COLOR | Hovered | `md.sys.color.on-surface-variant` |  |  |
| md.comp.button.filled.selected.hovered.state-layer.color | COLOR | Hovered | `md.sys.color.on-primary` |  |  |
| md.comp.button.filled.hovered.state-layer.opacity | OPACITY | Hovered | `md.sys.state.hover.state-layer-opacity` |  |  |
| md.comp.button.filled.hovered.container.elevation | ELEVATION | Hovered | `md.sys.elevation.level1` |  | No longer part of the design spec |
| md.comp.button.filled.hovered.label-text.color | COLOR | Hovered | `md.sys.color.on-primary` |  |  |
| md.comp.button.filled.unselected.hovered.label-text.color | COLOR | Hovered | `md.sys.color.on-surface-variant` |  |  |
| md.comp.button.filled.selected.hovered.label-text.color | COLOR | Hovered | `md.sys.color.on-primary` |  |  |
| md.comp.button.filled.hovered.icon.color | COLOR | Hovered | `md.sys.color.on-primary` |  |  |
| md.comp.button.filled.unselected.hovered.icon.color | COLOR | Hovered | `md.sys.color.on-surface-variant` |  |  |
| md.comp.button.filled.selected.hovered.icon.color | COLOR | Hovered | `md.sys.color.on-primary` |  |  |
| md.comp.button.filled.focused.state-layer.color | COLOR | Focused | `md.sys.color.on-primary` |  |  |
| md.comp.button.filled.unselected.focused.state-layer.color | COLOR | Focused | `md.sys.color.on-surface-variant` |  |  |
| md.comp.button.filled.selected.focused.state-layer.color | COLOR | Focused | `md.sys.color.on-primary` |  |  |
| md.comp.button.filled.focused.state-layer.opacity | OPACITY | Focused | `md.sys.state.focus.state-layer-opacity` |  |  |
| md.comp.button.filled.focused.container.elevation | ELEVATION | Focused | `md.sys.elevation.level0` |  |  |
| md.comp.button.filled.focused.label-text.color | COLOR | Focused | `md.sys.color.on-primary` |  |  |
| md.comp.button.filled.unselected.focused.label-text.color | COLOR | Focused | `md.sys.color.on-surface-variant` |  |  |
| md.comp.button.filled.selected.focused.label-text.color | COLOR | Focused | `md.sys.color.on-primary` |  |  |
| md.comp.button.filled.focused.icon.color | COLOR | Focused | `md.sys.color.on-primary` |  |  |
| md.comp.button.filled.unselected.focused.icon.color | COLOR | Focused | `md.sys.color.on-surface-variant` |  |  |
| md.comp.button.filled.selected.focused.icon.color | COLOR | Focused | `md.sys.color.on-primary` |  |  |
| md.comp.button.filled.pressed.state-layer.color | COLOR | Pressed | `md.sys.color.on-primary` |  |  |
| md.comp.button.filled.unselected.pressed.state-layer.color | COLOR | Pressed | `md.sys.color.on-surface-variant` |  |  |
| md.comp.button.filled.selected.pressed.state-layer.color | COLOR | Pressed | `md.sys.color.on-primary` |  |  |
| md.comp.button.filled.pressed.state-layer.opacity | OPACITY | Pressed | `md.sys.state.pressed.state-layer-opacity` |  |  |
| md.comp.button.filled.pressed.container.elevation | ELEVATION | Pressed | `md.sys.elevation.level0` |  |  |
| md.comp.button.filled.pressed.label-text.color | COLOR | Pressed | `md.sys.color.on-primary` |  |  |
| md.comp.button.filled.unselected.pressed.label-text.color | COLOR | Pressed | `md.sys.color.on-surface-variant` |  |  |
| md.comp.button.filled.selected.pressed.label-text.color | COLOR | Pressed | `md.sys.color.on-primary` |  |  |
| md.comp.button.filled.pressed.icon.color | COLOR | Pressed | `md.sys.color.on-primary` |  |  |
| md.comp.button.filled.unselected.pressed.icon.color | COLOR | Pressed | `md.sys.color.on-surface-variant` |  |  |
| md.comp.button.filled.selected.pressed.icon.color | COLOR | Pressed | `md.sys.color.on-primary` |  |  |

#### Button - Color - Tonal (md.comp.button.tonal)

For tonal buttons.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.button.tonal.container.color | COLOR | Enabled | `md.sys.color.secondary-container` |  |  |
| md.comp.button.tonal.unselected.container.color | COLOR | Enabled | `md.sys.color.secondary-container` |  |  |
| md.comp.button.tonal.selected.container.color | COLOR | Enabled | `md.sys.color.secondary` |  |  |
| md.comp.button.tonal.container.shadow-color | COLOR | Enabled | `md.sys.color.shadow` |  |  |
| md.comp.button.tonal.container.elevation | ELEVATION | Enabled | `md.sys.elevation.level0` |  |  |
| md.comp.button.tonal.label-text.color | COLOR | Enabled | `md.sys.color.on-secondary-container` |  |  |
| md.comp.button.tonal.unselected.label-text.color | COLOR | Enabled | `md.sys.color.on-secondary-container` |  |  |
| md.comp.button.tonal.selected.label-text.color | COLOR | Enabled | `md.sys.color.on-secondary` |  |  |
| md.comp.button.tonal.icon.color | COLOR | Enabled | `md.sys.color.on-secondary-container` |  |  |
| md.comp.button.tonal.unselected.icon.color | COLOR | Enabled | `md.sys.color.on-secondary-container` |  |  |
| md.comp.button.tonal.selected.icon.color | COLOR | Enabled | `md.sys.color.on-secondary` |  |  |
| md.comp.button.tonal.disabled.container.color | COLOR | Disabled | `md.sys.color.on-surface` |  |  |
| md.comp.button.tonal.disabled.container.opacity | OPACITY | Disabled | 0.1 |  |  |
| md.comp.button.tonal.disabled.container.elevation | ELEVATION | Disabled | `md.sys.elevation.level0` |  |  |
| md.comp.button.tonal.disabled.label-text.color | COLOR | Disabled | `md.sys.color.on-surface` |  |  |
| md.comp.button.tonal.disabled.label-text.opacity | OPACITY | Disabled | 0.38 |  |  |
| md.comp.button.tonal.disabled.icon.color | COLOR | Disabled | `md.sys.color.on-surface` |  |  |
| md.comp.button.tonal.disabled.icon.opacity | OPACITY | Disabled | 0.38 |  |  |
| md.comp.button.tonal.hovered.state-layer.color | COLOR | Hovered | `md.sys.color.on-secondary-container` |  |  |
| md.comp.button.tonal.unselected.hovered.state-layer.color | COLOR | Hovered | `md.sys.color.on-secondary-container` |  |  |
| md.comp.button.tonal.selected.hovered.state-layer.color | COLOR | Hovered | `md.sys.color.on-secondary` |  |  |
| md.comp.button.tonal.hovered.state-layer.opacity | OPACITY | Hovered | `md.sys.state.hover.state-layer-opacity` |  |  |
| md.comp.button.tonal.hovered.container.elevation | ELEVATION | Hovered | `md.sys.elevation.level1` |  | No longer part of the design spec |
| md.comp.button.tonal.hovered.label-text.color | COLOR | Hovered | `md.sys.color.on-secondary-container` |  |  |
| md.comp.button.tonal.unselected.hovered.label-text.color | COLOR | Hovered | `md.sys.color.on-secondary-container` |  |  |
| md.comp.button.tonal.selected.hovered.label-text.color | COLOR | Hovered | `md.sys.color.on-secondary` |  |  |
| md.comp.button.tonal.hovered.icon.color | COLOR | Hovered | `md.sys.color.on-secondary-container` |  |  |
| md.comp.button.tonal.unselected.hovered.icon.color | COLOR | Hovered | `md.sys.color.on-secondary-container` |  |  |
| md.comp.button.tonal.selected.hovered.icon.color | COLOR | Hovered | `md.sys.color.on-secondary` |  |  |
| md.comp.button.tonal.focused.state-layer.color | COLOR | Focused | `md.sys.color.on-secondary-container` |  |  |
| md.comp.button.tonal.unselected.focused.state-layer.color | COLOR | Focused | `md.sys.color.on-secondary-container` |  |  |
| md.comp.button.tonal.selected.focused.state-layer.color | COLOR | Focused | `md.sys.color.on-secondary` |  |  |
| md.comp.button.tonal.focused.state-layer.opacity | OPACITY | Focused | `md.sys.state.focus.state-layer-opacity` |  |  |
| md.comp.button.tonal.focused.container.elevation | ELEVATION | Focused | `md.sys.elevation.level0` |  |  |
| md.comp.button.tonal.focused.label-text.color | COLOR | Focused | `md.sys.color.on-secondary-container` |  |  |
| md.comp.button.tonal.unselected.focused.label-text.color | COLOR | Focused | `md.sys.color.on-secondary-container` |  |  |
| md.comp.button.tonal.selected.focused.label-text.color | COLOR | Focused | `md.sys.color.on-secondary` |  |  |
| md.comp.button.tonal.focused.icon.color | COLOR | Focused | `md.sys.color.on-secondary-container` |  |  |
| md.comp.button.tonal.unselected.focused.icon.color | COLOR | Focused | `md.sys.color.on-secondary-container` |  |  |
| md.comp.button.tonal.selected.focused.icon.color | COLOR | Focused | `md.sys.color.on-secondary` |  |  |
| md.comp.button.tonal.pressed.state-layer.color | COLOR | Pressed | `md.sys.color.on-secondary-container` |  |  |
| md.comp.button.tonal.unselected.pressed.state-layer.color | COLOR | Pressed | `md.sys.color.on-secondary-container` |  |  |
| md.comp.button.tonal.selected.pressed.state-layer.color | COLOR | Pressed | `md.sys.color.on-secondary` |  |  |
| md.comp.button.tonal.pressed.state-layer.opacity | OPACITY | Pressed | `md.sys.state.pressed.state-layer-opacity` |  |  |
| md.comp.button.tonal.pressed.container.elevation | ELEVATION | Pressed | `md.sys.elevation.level0` |  |  |
| md.comp.button.tonal.pressed.label-text.color | COLOR | Pressed | `md.sys.color.on-secondary-container` |  |  |
| md.comp.button.tonal.unselected.pressed.label-text.color | COLOR | Pressed | `md.sys.color.on-secondary-container` |  |  |
| md.comp.button.tonal.selected.pressed.label-text.color | COLOR | Pressed | `md.sys.color.on-secondary` |  |  |
| md.comp.button.tonal.pressed.icon.color | COLOR | Pressed | `md.sys.color.on-secondary-container` |  |  |
| md.comp.button.tonal.unselected.pressed.icon.color | COLOR | Pressed | `md.sys.color.on-secondary-container` |  |  |
| md.comp.button.tonal.selected.pressed.icon.color | COLOR | Pressed | `md.sys.color.on-secondary` |  |  |

#### Button - Color - Outlined (md.comp.button.outlined)

For outlined buttons.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.button.outlined.outline.color | COLOR | Enabled | `md.sys.color.outline-variant` |  |  |
| md.comp.button.outlined.selected.container.color | COLOR | Enabled | `md.sys.color.inverse-surface` |  |  |
| md.comp.button.outlined.label-text.color | COLOR | Enabled | `md.sys.color.on-surface-variant` |  |  |
| md.comp.button.outlined.unselected.label-text.color | COLOR | Enabled | `md.sys.color.on-surface-variant` |  |  |
| md.comp.button.outlined.selected.label-text.color | COLOR | Enabled | `md.sys.color.inverse-on-surface` |  |  |
| md.comp.button.outlined.icon.color | COLOR | Enabled | `md.sys.color.on-surface-variant` |  |  |
| md.comp.button.outlined.unselected.icon.color | COLOR | Enabled | `md.sys.color.on-surface-variant` |  |  |
| md.comp.button.outlined.selected.icon.color | COLOR | Enabled | `md.sys.color.inverse-on-surface` |  |  |
| md.comp.button.outlined.disabled.outline.color | COLOR | Disabled | `md.sys.color.outline-variant` |  |  |
| md.comp.button.outlined.unselected.disabled.outline.color | COLOR | Disabled | `md.sys.color.outline-variant` |  |  |
| md.comp.button.outlined.selected.disabled.container.color | COLOR | Disabled | `md.sys.color.on-surface` |  |  |
| md.comp.button.outlined.disabled.container.opacity | OPACITY | Disabled | 0.1 |  |  |
| md.comp.button.outlined.disabled.label-text.color | COLOR | Disabled | `md.sys.color.on-surface` |  |  |
| md.comp.button.outlined.disabled.label-text.opacity | OPACITY | Disabled | 0.38 |  |  |
| md.comp.button.outlined.disabled.icon.color | COLOR | Disabled | `md.sys.color.on-surface` |  |  |
| md.comp.button.outlined.disabled.icon.opacity | OPACITY | Disabled | 0.38 |  |  |
| md.comp.button.outlined.hovered.state-layer.color | COLOR | Hovered | `md.sys.color.on-surface-variant` |  |  |
| md.comp.button.outlined.unselected.hovered.state-layer.color | COLOR | Hovered | `md.sys.color.on-surface-variant` |  |  |
| md.comp.button.outlined.selected.hovered.state-layer.color | COLOR | Hovered | `md.sys.color.inverse-on-surface` |  |  |
| md.comp.button.outlined.hovered.state-layer.opacity | OPACITY | Hovered | `md.sys.state.hover.state-layer-opacity` |  |  |
| md.comp.button.outlined.hovered.outline.color | COLOR | Hovered | `md.sys.color.outline-variant` |  |  |
| md.comp.button.outlined.unselected.hovered.outline.color | COLOR | Hovered | `md.sys.color.outline-variant` |  |  |
| md.comp.button.outlined.hovered.label-text.color | COLOR | Hovered | `md.sys.color.on-surface-variant` |  |  |
| md.comp.button.outlined.unselected.hovered.label-text.color | COLOR | Hovered | `md.sys.color.on-surface-variant` |  |  |
| md.comp.button.outlined.selected.hovered.label-text.color | COLOR | Hovered | `md.sys.color.inverse-on-surface` |  |  |
| md.comp.button.outlined.hovered.icon.color | COLOR | Hovered | `md.sys.color.on-surface-variant` |  |  |
| md.comp.button.outlined.unselected.hovered.icon.color | COLOR | Hovered | `md.sys.color.on-surface-variant` |  |  |
| md.comp.button.outlined.selected.hovered.icon.color | COLOR | Hovered | `md.sys.color.inverse-on-surface` |  |  |
| md.comp.button.outlined.focused.state-layer.color | COLOR | Focused | `md.sys.color.on-surface-variant` |  |  |
| md.comp.button.outlined.unselected.focused.state-layer.color | COLOR | Focused | `md.sys.color.on-surface-variant` |  |  |
| md.comp.button.outlined.selected.focused.state-layer.color | COLOR | Focused | `md.sys.color.inverse-on-surface` |  |  |
| md.comp.button.outlined.focused.state-layer.opacity | OPACITY | Focused | `md.sys.state.focus.state-layer-opacity` |  |  |
| md.comp.button.outlined.focused.outline.color | COLOR | Focused | `md.sys.color.outline-variant` |  |  |
| md.comp.button.outlined.unselected.focused.outline.color | COLOR | Focused | `md.sys.color.outline-variant` |  |  |
| md.comp.button.outlined.focused.label-text.color | COLOR | Focused | `md.sys.color.on-surface-variant` |  |  |
| md.comp.button.outlined.unselected.focused.label-text.color | COLOR | Focused | `md.sys.color.on-surface-variant` |  |  |
| md.comp.button.outlined.selected.focused.label-text.color | COLOR | Focused | `md.sys.color.inverse-on-surface` |  |  |
| md.comp.button.outlined.focused.icon.color | COLOR | Focused | `md.sys.color.on-surface-variant` |  |  |
| md.comp.button.outlined.unselected.focused.icon.color | COLOR | Focused | `md.sys.color.on-surface-variant` |  |  |
| md.comp.button.outlined.selected.focused.icon.color | COLOR | Focused | `md.sys.color.inverse-on-surface` |  |  |
| md.comp.button.outlined.pressed.state-layer.color | COLOR | Pressed | `md.sys.color.on-surface-variant` |  |  |
| md.comp.button.outlined.unselected.pressed.state-layer.color | COLOR | Pressed | `md.sys.color.on-surface-variant` |  |  |
| md.comp.button.outlined.selected.pressed.state-layer.color | COLOR | Pressed | `md.sys.color.inverse-on-surface` |  |  |
| md.comp.button.outlined.pressed.state-layer.opacity | OPACITY | Pressed | `md.sys.state.pressed.state-layer-opacity` |  |  |
| md.comp.button.outlined.pressed.outline.color | COLOR | Pressed | `md.sys.color.outline-variant` |  |  |
| md.comp.button.outlined.unselected.pressed.outline.color | COLOR | Pressed | `md.sys.color.outline-variant` |  |  |
| md.comp.button.outlined.pressed.label-text.color | COLOR | Pressed | `md.sys.color.on-surface-variant` |  |  |
| md.comp.button.outlined.unselected.pressed.label-text.color | COLOR | Pressed | `md.sys.color.on-surface-variant` |  |  |
| md.comp.button.outlined.selected.pressed.label-text.color | COLOR | Pressed | `md.sys.color.inverse-on-surface` |  |  |
| md.comp.button.outlined.pressed.icon.color | COLOR | Pressed | `md.sys.color.on-surface-variant` |  |  |
| md.comp.button.outlined.unselected.pressed.icon.color | COLOR | Pressed | `md.sys.color.on-surface-variant` |  |  |
| md.comp.button.outlined.selected.pressed.icon.color | COLOR | Pressed | `md.sys.color.inverse-on-surface` |  |  |

#### Button - Color - Text (md.comp.button.text)

For text buttons.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.button.text.label-text.color | COLOR | Enabled | `md.sys.color.primary` |  |  |
| md.comp.button.text.icon.color | COLOR | Enabled | `md.sys.color.primary` |  |  |
| md.comp.button.text.disabled.container.color | COLOR | Disabled | `md.sys.color.on-surface` |  |  |
| md.comp.button.text.disabled.container.opacity | OPACITY | Disabled | 0.1 |  |  |
| md.comp.button.text.disabled.label-text.color | COLOR | Disabled | `md.sys.color.on-surface` |  |  |
| md.comp.button.text.disabled.label-text.opacity | OPACITY | Disabled | 0.38 |  |  |
| md.comp.button.text.disabled.icon.color | COLOR | Disabled | `md.sys.color.on-surface` |  |  |
| md.comp.button.text.disabled.icon.opacity | OPACITY | Disabled | 0.38 |  |  |
| md.comp.button.text.hovered.state-layer.color | COLOR | Hovered | `md.sys.color.primary` |  |  |
| md.comp.button.text.hovered.state-layer.opacity | OPACITY | Hovered | `md.sys.state.hover.state-layer-opacity` |  |  |
| md.comp.button.text.hovered.label-text.color | COLOR | Hovered | `md.sys.color.primary` |  |  |
| md.comp.button.text.hovered.icon.color | COLOR | Hovered | `md.sys.color.primary` |  |  |
| md.comp.button.text.focused.state-layer.color | COLOR | Focused | `md.sys.color.primary` |  |  |
| md.comp.button.text.focused.state-layer.opacity | OPACITY | Focused | `md.sys.state.focus.state-layer-opacity` |  |  |
| md.comp.button.text.focused.label-text.color | COLOR | Focused | `md.sys.color.primary` |  |  |
| md.comp.button.text.focused.icon.color | COLOR | Focused | `md.sys.color.primary` |  |  |
| md.comp.button.text.pressed.state-layer.color | COLOR | Pressed | `md.sys.color.primary` |  |  |
| md.comp.button.text.pressed.state-layer.opacity | OPACITY | Pressed | `md.sys.state.pressed.state-layer-opacity` |  |  |
| md.comp.button.text.pressed.label-text.color | COLOR | Pressed | `md.sys.color.primary` |  |  |
| md.comp.button.text.pressed.icon.color | COLOR | Pressed | `md.sys.color.primary` |  |  |

#### Button - Size - Xsmall (md.comp.button.xsmall)

For XS buttons.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.button.xsmall.container.height | LENGTH |  | 32dp |  |  |
| md.comp.button.xsmall.outlined.outline.width | LENGTH |  | 1dp |  |  |
| md.comp.button.xsmall.label-text | TYPOGRAPHY |  | `md.sys.typescale.label-large` |  |  |
| md.comp.button.xsmall.icon.size | LENGTH |  | 20dp |  |  |
| md.comp.button.xsmall.container.shape.round | SHAPE |  | `md.sys.shape.corner.full` |  |  |
| md.comp.button.xsmall.container.shape.square | SHAPE |  | `md.sys.shape.corner.medium` |  |  |
| md.comp.button.xsmall.leading-space | LENGTH |  | 12dp |  |  |
| md.comp.button.xsmall.icon-label-space | LENGTH |  | 8dp |  |  |
| md.comp.button.xsmall.trailing-space | LENGTH |  | 12dp |  |  |
| md.comp.button.xsmall.pressed.container.shape | SHAPE |  | `md.sys.shape.corner.small` |  |  |
| md.comp.button.xsmall.pressed.container.corner-size.motion.spring.damping | DURATION |  | `md.sys.motion.spring.fast.spatial.damping` |  |  |
| md.comp.button.xsmall.pressed.container.corner-size.motion.spring.stiffness | DURATION |  | `md.sys.motion.spring.fast.spatial.stiffness` |  |  |
| md.comp.button.xsmall.selected.container.shape.round | SHAPE |  | `md.sys.shape.corner.medium` |  |  |
| md.comp.button.xsmall.selected.container.shape.square | SHAPE |  | `md.sys.shape.corner.full` |  |  |

#### Button - Size - Small (md.comp.button.small)

For small buttons.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.button.small.container.height | LENGTH |  | 40dp |  |  |
| md.comp.button.small.outlined.outline.width | LENGTH |  | 1dp |  |  |
| md.comp.button.small.label-text | TYPOGRAPHY |  | `md.sys.typescale.label-large` |  |  |
| md.comp.button.small.icon.size | LENGTH |  | 20dp |  |  |
| md.comp.button.small.container.shape.round | SHAPE |  | `md.sys.shape.corner.full` |  |  |
| md.comp.button.small.container.shape.square | SHAPE |  | `md.sys.shape.corner.medium` |  |  |
| md.comp.button.small.leading-space | LENGTH |  | 16dp |  |  |
| md.comp.button.small.icon-label-space | LENGTH |  | 8dp |  |  |
| md.comp.button.small.trailing-space | LENGTH |  | 16dp |  |  |
| md.comp.button.small.pressed.container.shape | SHAPE |  | `md.sys.shape.corner.small` |  |  |
| md.comp.button.small.pressed.container.corner-size.motion.spring.damping | DURATION |  | `md.sys.motion.spring.fast.spatial.damping` |  |  |
| md.comp.button.small.pressed.container.corner-size.motion.spring.stiffness | DURATION |  | `md.sys.motion.spring.fast.spatial.stiffness` |  |  |
| md.comp.button.small.selected.container.shape.round | SHAPE |  | `md.sys.shape.corner.medium` |  |  |
| md.comp.button.small.selected.container.shape.square | SHAPE |  | `md.sys.shape.corner.full` |  |  |

#### Button - Size - Medium (md.comp.button.medium)

For medium buttons.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.button.medium.container.height | LENGTH |  | 56dp |  |  |
| md.comp.button.medium.outlined.outline.width | LENGTH |  | 1dp |  |  |
| md.comp.button.medium.label-text | TYPOGRAPHY |  | `md.sys.typescale.title-medium` |  |  |
| md.comp.button.medium.icon.size | LENGTH |  | 24dp |  |  |
| md.comp.button.medium.container.shape.round | SHAPE |  | `md.sys.shape.corner.full` |  |  |
| md.comp.button.medium.container.shape.square | SHAPE |  | `md.sys.shape.corner.large` |  |  |
| md.comp.button.medium.leading-space | LENGTH |  | 24dp |  |  |
| md.comp.button.medium.icon-label-space | LENGTH |  | 8dp |  |  |
| md.comp.button.medium.trailing-space | LENGTH |  | 24dp |  |  |
| md.comp.button.medium.pressed.container.shape | SHAPE |  | `md.sys.shape.corner.medium` |  |  |
| md.comp.button.medium.pressed.container.corner-size.motion.spring.damping | DURATION |  | `md.sys.motion.spring.fast.spatial.damping` |  |  |
| md.comp.button.medium.pressed.container.corner-size.motion.spring.stiffness | DURATION |  | `md.sys.motion.spring.fast.spatial.stiffness` |  |  |
| md.comp.button.medium.selected.container.shape.round | SHAPE |  | `md.sys.shape.corner.large` |  |  |
| md.comp.button.medium.selected.container.shape.square | SHAPE |  | `md.sys.shape.corner.full` |  |  |

#### Button - Size - Large (md.comp.button.large)

For large buttons.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.button.large.container.height | LENGTH |  | 96dp |  |  |
| md.comp.button.large.outlined.outline.width | LENGTH |  | 2dp |  |  |
| md.comp.button.large.label-text | TYPOGRAPHY |  | `md.sys.typescale.headline-small` |  |  |
| md.comp.button.large.icon.size | LENGTH |  | 32dp |  |  |
| md.comp.button.large.container.shape.round | SHAPE |  | `md.sys.shape.corner.full` |  |  |
| md.comp.button.large.container.shape.square | SHAPE |  | `md.sys.shape.corner.extra-large` |  |  |
| md.comp.button.large.leading-space | LENGTH |  | 48dp |  |  |
| md.comp.button.large.icon-label-space | LENGTH |  | 12dp |  |  |
| md.comp.button.large.trailing-space | LENGTH |  | 48dp |  |  |
| md.comp.button.large.pressed.container.shape | SHAPE |  | `md.sys.shape.corner.large` |  |  |
| md.comp.button.large.pressed.container.corner-size.motion.spring.damping | DURATION |  | `md.sys.motion.spring.fast.spatial.damping` |  |  |
| md.comp.button.large.pressed.container.corner-size.motion.spring.stiffness | DURATION |  | `md.sys.motion.spring.fast.spatial.stiffness` |  |  |
| md.comp.button.large.selected.container.shape.round | SHAPE |  | `md.sys.shape.corner.extra-large` |  |  |
| md.comp.button.large.selected.container.shape.square | SHAPE |  | `md.sys.shape.corner.full` |  |  |

#### Button - Size - Xlarge (md.comp.button.xlarge)

For XL buttons.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.button.xlarge.container.height | LENGTH |  | 136dp |  |  |
| md.comp.button.xlarge.outlined.outline.width | LENGTH |  | 3dp |  |  |
| md.comp.button.xlarge.label-text | TYPOGRAPHY |  | `md.sys.typescale.headline-large` |  |  |
| md.comp.button.xlarge.icon.size | LENGTH |  | 40dp |  |  |
| md.comp.button.xlarge.container.shape.round | SHAPE |  | `md.sys.shape.corner.full` |  |  |
| md.comp.button.xlarge.container.shape.square | SHAPE |  | `md.sys.shape.corner.extra-large` |  |  |
| md.comp.button.xlarge.leading-space | LENGTH |  | 64dp |  |  |
| md.comp.button.xlarge.icon-label-space | LENGTH |  | 16dp |  |  |
| md.comp.button.xlarge.trailing-space | LENGTH |  | 64dp |  |  |
| md.comp.button.xlarge.pressed.container.shape | SHAPE |  | `md.sys.shape.corner.large` |  |  |
| md.comp.button.xlarge.pressed.container.corner-size.motion.spring.damping | DURATION |  | `md.sys.motion.spring.fast.spatial.damping` |  |  |
| md.comp.button.xlarge.pressed.container.corner-size.motion.spring.stiffness | DURATION |  | `md.sys.motion.spring.fast.spatial.stiffness` |  |  |
| md.comp.button.xlarge.selected.container.shape.round | SHAPE |  | `md.sys.shape.corner.extra-large` |  |  |
| md.comp.button.xlarge.selected.container.shape.square | SHAPE |  | `md.sys.shape.corner.full` |  |  |

## Anatomy

![Diagram labeling 3 parts of a button.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmmneqix9-03-3P.png?alt=media&token=05fc9372-a4d6-4e91-9f86-8d2959be78b3>)

_Container; Label text; Icon (optional)_

## Color

Color values are implemented through design tokens. For designers, this means working with color values that correspond with tokens. In implementation, a color value will be a token that references a value.

-   There are five built-in button color styles: elevated, filled, tonal, outlined, and text

-   The default and toggle buttons use different colors

-   Toggle buttons don’t use the text style

star

Note:

These color roles were chosen to create design coherence and familiarity. Other color roles can be used as long as the container and text have a 3:1 contrast ratio. For example, tertiary and on tertiary.

![Diagram shows dark and light color schemes for buttons.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmmner6e7-04-3P.png?alt=media&token=ea59a3bb-f3fc-43ca-af7d-6cc7898c378d>)

_A. Elevated, B. Filled, C. Tonal, D. Outlined, E. Text Default; Toggle: unselected; Toggle: selected_

|  | 1\. Default | 2\. Toggle unselected | 3\. Toggle selected |
| --- | --- | --- | --- |
| Elevated container Elevated icon & label | Surface container low Primary | Surface container low Primary | Primary On primary |
| Filled container Filled icon & label | Primary On primary | Surface container On surface variant | Primary On primary |
| Tonal container Tonal icon & label | Secondary container On secondary container | Secondary container On secondary container | Secondary On secondary |
| Outlined container Outlined icon & label | Outline variant (outline) On surface variant | Outline variant (outline) On surface variant | Inverse surface Inverse on surface |
| Text icon & label | Primary | \-- | \-- |

## States

States are visual representations used to communicate the status of a component or interactive element.

### Elevated button states

The elevated button style has an elevation of 1 by default and 0 when disabled.

#### Default

![Elevated button states.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmmnesmjm-06-3P.png?alt=media&token=96e8c029-e1e8-472e-b4a1-767a8d9b9e1b>)

_Enabled; Disabled; Hovered; Focused; Pressed_

#### Toggle

![Toggle elevated button states.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmmnet1qn-07-3P.png?alt=media&token=d0320826-723d-4e6b-bce8-36f026c997d1>)

_A. Unselected, B. Selected Enabled; Disabled; Hovered; Focused; Pressed_

### Filled button states

#### Default

![Filled button states.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmmneu2zl-08-3P.png?alt=media&token=85a9b3f6-81d7-45b9-8355-882a776b1004>)

_Enabled; Disabled; Hovered; Focused; Pressed_

#### Toggle

![Toggle filled button states.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmnhkhbya-09-3P.png?alt=media&token=4d91d61b-742a-4cb2-b1c3-bb91c30d7c37>)

_A. Unselected, B. Selected Enabled; Disabled; Hovered; Focused; Pressed_

### Tonal button states

#### Default

![Tonal button states.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmmneuv7c-10-3P.png?alt=media&token=72439eb0-b173-49bb-9e6f-05bbc05fcce6>)

_Enabled; Disabled; Hovered; Focused; Pressed_

#### Toggle

![Toggle tonal button states.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmmnevsol-11-3P.png?alt=media&token=98ef1d74-1ea3-4dfa-8eb8-423776f5a91f>)

_A. Unselected, B. Selected Enabled; Disabled; Hovered; Focused; Pressed_

### Outlined button states

The outlined button’s container fill is invisible at rest, but the opacity and state layers behave the same as other button styles when disabled, hovered, focused, or pressed.

#### Default

![Outlined button states.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmmnewg2s-12-3P.png?alt=media&token=704b2a67-f91b-4d45-83b5-518b79385ac6>)

_Enabled; Disabled; Hovered; Focused; Pressed_

#### Toggle

![Outlined button states.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmmnexggd-13-3P.png?alt=media&token=105359d5-1b67-45b7-a585-a901da09aeef>)

_Enabled; Disabled; Hovered; Focused; Pressed_

### Text button style states

The text button’s container is invisible at rest, but the opacity and state layers behave the same as other button styles when disabled, hovered, focused, or pressed. There is no toggle text button.

![Default text button style states.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmmneyf25-14-3P.png?alt=media&token=02800d8d-417f-4612-8e0e-3f9edf30f1a6>)

_Enabled; Disabled; Hovered; Focused; Pressed_

## Shape morph

### Pressed state

When pressed, buttons can morph to become more square. Both round and square buttons should have the same pressed shape.

The corner radius value differs for each button size. [See full button corner measurements](specs.md#b1f39738-6f3a-409b-8f08-4cab6d78d756)

![Shape changes of a button.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmmneyrwe-15-3P.png?alt=media&token=32fe38c8-aa3d-4e9a-b907-b24fa1862a48>)

_A. Round button, B. Square button Enabled; Hovered; Pressed_

### When selected

In addition to changing shape when pressed, toggle buttons also change the resting shape from round (unselected) to square (selected). 

If the resting unselected shape is square, the selected shape should be round.

![Shape changes of a toggle button.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmmnezbyl-16-3P.png?alt=media&token=c47d2dc2-5f00-4ff1-b616-6617d140d79f>)

_A. Round button, B. Square button Enabled; Hovered; Pressed; Selected_

## Measurements

![Diagram of measurements of all button sizes.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmmnf0bsj-17-3P.png?alt=media&token=4c91ef4a-b41f-4b3a-8797-04d9a810c2ef>)

_Padding and size measurements of each button size Extra small; Small; Medium; Large; Extra large_

### Target areas

Extra small and small icon buttons must have a target size of 48x48dp or larger to be accessible.

![Diagram of small button target areas.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmmnf0o8e-18-3P.png?alt=media&token=7107e16a-9ec0-4cd3-ad00-ba2e2bb75514>)

_A. Extra small B. Small Round button; Button with icon; Square button_

### Corner sizes

![Diagram of corner radii of buttons.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmmnf14u5-19-3P.png?alt=media&token=8caa9040-a710-4f1f-a6a4-d0bfd85fe7b5>)

_/_

|  | XS | S | M | L | XL |
| --- | --- | --- | --- | --- | --- |
| A. Round button | Full | Full | Full | Full | Full |
| B. Square button | 12dp | 12dp | 16dp | 28dp | 28dp |
| C. Pressed state | 8dp | 8dp | 12dp | 16dp | 16dp |

## Baseline tokens

Use the table's menu to switch token sets. The baseline button token sets are organized by color.

### TOKEN_TABLE

Component: Buttons

Configured context tags: None, Medium contrast, Default, High contrast, Static, 3P, Android, Dark, Light

#### Token sets

| Token set | Name | Type | Tokens | Description |
| --- | --- | --- | --- | --- |
| [Deprecated] Button - Elevated | md.comp.elevated-button | COMPONENT | 40 | This token set is deprecated. Use `md.comp.button.elevated` instead. |
| [Deprecated] Button - Filled | md.comp.filled-button | COMPONENT | 44 | This token set is deprecated. Use `md.comp.button.filled` instead. |
| [Deprecated] Button - Tonal | md.comp.filled-tonal-button | COMPONENT | 39 | This token set is deprecated. Use `md.comp.button.tonal` instead. |
| [Deprecated] Button – Outlined | md.comp.outlined-button | COMPONENT | 38 | This token set is deprecated. Use `md.comp.button.outlined` instead. |
| [Deprecated] Button - Text | md.comp.text-button | COMPONENT | 31 | This token set is deprecated. Use `md.comp.button.text` instead. |

#### [Deprecated] Button - Elevated (md.comp.elevated-button)

This token set is deprecated. Use `md.comp.button.elevated` instead.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.elevated-button.container.shape | SHAPE | [Deprecated] Enabled / [Deprecated] Container | `md.sys.shape.corner.full` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.elevated or md.comp.button.(size) set instead. |
| md.comp.elevated-button.container.height | LENGTH | [Deprecated] Enabled / [Deprecated] Container | 40dp |  | This token is deprecated. Use the corresponding token from either the md.comp.button.elevated or md.comp.button.(size) set instead. |
| md.comp.elevated-button.container.elevation | ELEVATION | [Deprecated] Enabled / [Deprecated] Container | `md.sys.elevation.level1` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.elevated or md.comp.button.(size) set instead. |
| md.comp.elevated-button.container.shadow-color | COLOR | [Deprecated] Enabled / [Deprecated] Container | `md.sys.color.shadow` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.elevated or md.comp.button.(size) set instead. |
| md.comp.elevated-button.container.color | COLOR | [Deprecated] Enabled / [Deprecated] Container | `md.sys.color.surface-container-low` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.elevated or md.comp.button.(size) set instead. |
| md.comp.elevated-button.container.surface-tint-layer.color | COLOR | [Deprecated] Enabled / [Deprecated] Container | `md.sys.color.surface-tint` |  | Deprecated as part of the update from opacity based surfaces to tonal surfaces. Surfaces no longer use surface-tint layers for tinting, please use the desired surface role directly as the container color. |
| md.comp.elevated-button.label-text.font | FONT_NAMES | [Deprecated] Enabled / [Deprecated] Label text | `md.sys.typescale.label-large.font` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.elevated or md.comp.button.(size) set instead. |
| md.comp.elevated-button.label-text.line-height | LINE_HEIGHT | [Deprecated] Enabled / [Deprecated] Label text | `md.sys.typescale.label-large.line-height` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.elevated or md.comp.button.(size) set instead. |
| md.comp.elevated-button.label-text.size | FONT_SIZE | [Deprecated] Enabled / [Deprecated] Label text | `md.sys.typescale.label-large.size` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.elevated or md.comp.button.(size) set instead. |
| md.comp.elevated-button.label-text.weight | FONT_WEIGHT | [Deprecated] Enabled / [Deprecated] Label text | `md.sys.typescale.label-large.weight` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.elevated or md.comp.button.(size) set instead. |
| md.comp.elevated-button.label-text.tracking | FONT_TRACKING | [Deprecated] Enabled / [Deprecated] Label text | `md.sys.typescale.label-large.tracking` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.elevated or md.comp.button.(size) set instead. |
| md.comp.elevated-button.label-text.type | TYPOGRAPHY | [Deprecated] Enabled / [Deprecated] Label text | font name: `md.comp.elevated-button.label-text.font`, font weight: `md.comp.elevated-button.label-text.weight`, font size: `md.comp.elevated-button.label-text.size`, font tracking: `md.comp.elevated-button.label-text.tracking`, line height: `md.comp.elevated-button.label-text.line-height` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.elevated or md.comp.button.(size) set instead. |
| md.comp.elevated-button.label-text.color | COLOR | [Deprecated] Enabled / [Deprecated] Label text | `md.sys.color.primary` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.elevated or md.comp.button.(size) set instead. |
| md.comp.elevated-button.with-icon.icon.size | LENGTH | [Deprecated] Enabled / [Deprecated] Icon | 18dp |  | This token is deprecated. Use the corresponding token from either the md.comp.button.elevated or md.comp.button.(size) set instead. |
| md.comp.elevated-button.with-icon.icon.color | COLOR | [Deprecated] Enabled / [Deprecated] Icon | `md.sys.color.primary` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.elevated or md.comp.button.(size) set instead. |
| md.comp.elevated-button.disabled.container.color | COLOR | [Deprecated] Disabled / [Deprecated] Container | `md.sys.color.on-surface` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.elevated or md.comp.button.(size) set instead. |
| md.comp.elevated-button.disabled.container.opacity | OPACITY | [Deprecated] Disabled / [Deprecated] Container | 0.12 |  | This token is deprecated. Use the corresponding token from either the md.comp.button.elevated or md.comp.button.(size) set instead. |
| md.comp.elevated-button.disabled.container.elevation | ELEVATION | [Deprecated] Disabled / [Deprecated] Container | `md.sys.elevation.level0` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.elevated or md.comp.button.(size) set instead. |
| md.comp.elevated-button.disabled.label-text.color | COLOR | [Deprecated] Disabled / [Deprecated] Label text | `md.sys.color.on-surface` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.elevated or md.comp.button.(size) set instead. |
| md.comp.elevated-button.disabled.label-text.opacity | OPACITY | [Deprecated] Disabled / [Deprecated] Label text | 0.38 |  | This token is deprecated. Use the corresponding token from either the md.comp.button.elevated or md.comp.button.(size) set instead. |
| md.comp.elevated-button.with-icon.disabled.icon.color | COLOR | [Deprecated] Disabled / [Deprecated] Icon | `md.sys.color.on-surface` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.elevated or md.comp.button.(size) set instead. |
| md.comp.elevated-button.with-icon.disabled.icon.opacity | OPACITY | [Deprecated] Disabled / [Deprecated] Icon | 0.38 |  | This token is deprecated. Use the corresponding token from either the md.comp.button.elevated or md.comp.button.(size) set instead. |
| md.comp.elevated-button.hover.container.elevation | ELEVATION | [Deprecated] Hovered / [Deprecated] Container | `md.sys.elevation.level2` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.elevated or md.comp.button.(size) set instead. |
| md.comp.elevated-button.hover.label-text.color | COLOR | [Deprecated] Hovered / [Deprecated] Label text | `md.sys.color.primary` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.elevated or md.comp.button.(size) set instead. |
| md.comp.elevated-button.hover.state-layer.color | COLOR | [Deprecated] Hovered / [Deprecated] State layer | `md.sys.color.primary` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.elevated or md.comp.button.(size) set instead. |
| md.comp.elevated-button.hover.state-layer.opacity | OPACITY | [Deprecated] Hovered / [Deprecated] State layer | `md.sys.state.hover.state-layer-opacity` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.elevated or md.comp.button.(size) set instead. |
| md.comp.elevated-button.with-icon.hover.icon.color | COLOR | [Deprecated] Hovered / [Deprecated] Icon | `md.sys.color.primary` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.elevated or md.comp.button.(size) set instead. |
| md.comp.elevated-button.focus.indicator.color | COLOR | [Deprecated] Focused / [Deprecated] Focus indicator | `md.sys.color.secondary` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.elevated or md.comp.button.(size) set instead. |
| md.comp.elevated-button.focus.indicator.thickness | LENGTH | [Deprecated] Focused / [Deprecated] Focus indicator | `md.sys.state.focus-indicator.thickness` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.elevated or md.comp.button.(size) set instead. |
| md.comp.elevated-button.focus.indicator.outline.offset | LENGTH | [Deprecated] Focused / [Deprecated] Focus indicator | `md.sys.state.focus-indicator.outer-offset` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.elevated or md.comp.button.(size) set instead. |
| md.comp.elevated-button.focus.container.elevation | ELEVATION | [Deprecated] Focused / [Deprecated] Container | `md.sys.elevation.level1` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.elevated or md.comp.button.(size) set instead. |
| md.comp.elevated-button.focus.label-text.color | COLOR | [Deprecated] Focused / [Deprecated] Label text | `md.sys.color.primary` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.elevated or md.comp.button.(size) set instead. |
| md.comp.elevated-button.focus.state-layer.color | COLOR | [Deprecated] Focused / [Deprecated] State layer | `md.sys.color.primary` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.elevated or md.comp.button.(size) set instead. |
| md.comp.elevated-button.focus.state-layer.opacity | OPACITY | [Deprecated] Focused / [Deprecated] State layer | `md.sys.state.focus.state-layer-opacity` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.elevated or md.comp.button.(size) set instead. |
| md.comp.elevated-button.with-icon.focus.icon.color | COLOR | [Deprecated] Focused / [Deprecated] Icon | `md.sys.color.primary` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.elevated or md.comp.button.(size) set instead. |
| md.comp.elevated-button.pressed.container.elevation | ELEVATION | [Deprecated] Pressed (ripple) / [Deprecated] Container | `md.sys.elevation.level1` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.elevated or md.comp.button.(size) set instead. |
| md.comp.elevated-button.pressed.label-text.color | COLOR | [Deprecated] Pressed (ripple) / [Deprecated] Label text | `md.sys.color.primary` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.elevated or md.comp.button.(size) set instead. |
| md.comp.elevated-button.pressed.state-layer.color | COLOR | [Deprecated] Pressed (ripple) / [Deprecated] State layer | `md.sys.color.primary` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.elevated or md.comp.button.(size) set instead. |
| md.comp.elevated-button.pressed.state-layer.opacity | OPACITY | [Deprecated] Pressed (ripple) / [Deprecated] State layer | `md.sys.state.pressed.state-layer-opacity` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.elevated or md.comp.button.(size) set instead. |
| md.comp.elevated-button.with-icon.pressed.icon.color | COLOR | [Deprecated] Pressed (ripple) / [Deprecated] Icon | `md.sys.color.primary` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.elevated or md.comp.button.(size) set instead. |

#### [Deprecated] Button - Filled (md.comp.filled-button)

This token set is deprecated. Use `md.comp.button.filled` instead.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.filled-button.container.shape | SHAPE | [Deprecated] Enabled / [Deprecated] Container | `md.sys.shape.corner.full` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.filled or md.comp.button.(size) set instead. |
| md.comp.filled-button.container.height | LENGTH | [Deprecated] Enabled / [Deprecated] Container | 40dp |  | This token is deprecated. Use the corresponding token from either the md.comp.button.filled or md.comp.button.(size) set instead. |
| md.comp.filled-button.container.elevation | ELEVATION | [Deprecated] Enabled / [Deprecated] Container | `md.sys.elevation.level0` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.filled or md.comp.button.(size) set instead. |
| md.comp.filled-button.container.shadow-color | COLOR | [Deprecated] Enabled / [Deprecated] Container | `md.sys.color.shadow` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.filled or md.comp.button.(size) set instead. |
| md.comp.filled-button.container.color | COLOR | [Deprecated] Enabled / [Deprecated] Container | `md.sys.color.primary` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.filled or md.comp.button.(size) set instead. |
| md.comp.filled-button.label-text.font | FONT_NAMES | [Deprecated] Enabled / [Deprecated] Label text | `md.sys.typescale.label-large.font` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.filled or md.comp.button.(size) set instead. |
| md.comp.filled-button.label-text.line-height | LINE_HEIGHT | [Deprecated] Enabled / [Deprecated] Label text | `md.sys.typescale.label-large.line-height` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.filled or md.comp.button.(size) set instead. |
| md.comp.filled-button.label-text.size | FONT_SIZE | [Deprecated] Enabled / [Deprecated] Label text | `md.sys.typescale.label-large.size` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.filled or md.comp.button.(size) set instead. |
| md.comp.filled-button.label-text.weight | FONT_WEIGHT | [Deprecated] Enabled / [Deprecated] Label text | `md.sys.typescale.label-large.weight` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.filled or md.comp.button.(size) set instead. |
| md.comp.filled-button.label-text.tracking | FONT_TRACKING | [Deprecated] Enabled / [Deprecated] Label text | `md.sys.typescale.label-large.tracking` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.filled or md.comp.button.(size) set instead. |
| md.comp.filled-button.label-text.type | TYPOGRAPHY | [Deprecated] Enabled / [Deprecated] Label text | font name: `md.comp.filled-button.label-text.font`, font weight: `md.comp.filled-button.label-text.weight`, font size: `md.comp.filled-button.label-text.size`, font tracking: `md.comp.filled-button.label-text.tracking`, line height: `md.comp.filled-button.label-text.line-height` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.filled or md.comp.button.(size) set instead. |
| md.comp.filled-button.label-text.color | COLOR | [Deprecated] Enabled / [Deprecated] Label text | `md.sys.color.on-primary` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.filled or md.comp.button.(size) set instead. |
| md.comp.filled-button.with-icon.icon.size | LENGTH | [Deprecated] Enabled / [Deprecated] Icon | 18dp |  | This token is deprecated. Use the corresponding token from either the md.comp.button.filled or md.comp.button.(size) set instead. |
| md.comp.filled-button.with-icon.icon.color | COLOR | [Deprecated] Enabled / [Deprecated] Icon | `md.sys.color.on-primary` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.filled or md.comp.button.(size) set instead. |
| md.comp.filled-button.disabled.container.elevation | ELEVATION | [Deprecated] Disabled / [Deprecated] Container | `md.sys.elevation.level0` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.filled or md.comp.button.(size) set instead. |
| md.comp.filled-button.disabled.container.color | COLOR | [Deprecated] Disabled / [Deprecated] Container | `md.sys.color.on-surface` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.filled or md.comp.button.(size) set instead. |
| md.comp.filled-button.disabled.container.opacity | OPACITY | [Deprecated] Disabled / [Deprecated] Container | 0.12 |  | This token is deprecated. Use the corresponding token from either the md.comp.button.filled or md.comp.button.(size) set instead. |
| md.comp.filled-button.disabled.label-text.color | COLOR | [Deprecated] Disabled / [Deprecated] Label text | `md.sys.color.on-surface` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.filled or md.comp.button.(size) set instead. |
| md.comp.filled-button.disabled.label-text.opacity | OPACITY | [Deprecated] Disabled / [Deprecated] Label text | 0.38 |  | This token is deprecated. Use the corresponding token from either the md.comp.button.filled or md.comp.button.(size) set instead. |
| md.comp.filled-button.with-icon.disabled.icon.color | COLOR | [Deprecated] Disabled / [Deprecated] Icon | `md.sys.color.on-surface` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.filled or md.comp.button.(size) set instead. |
| md.comp.filled-button.with-icon.disabled.icon.opacity | OPACITY | [Deprecated] Disabled / [Deprecated] Icon | 0.38 |  | This token is deprecated. Use the corresponding token from either the md.comp.button.filled or md.comp.button.(size) set instead. |
| md.comp.filled-button.hover.container.elevation | ELEVATION | [Deprecated] Hovered / [Deprecated] Container | `md.sys.elevation.level1` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.filled or md.comp.button.(size) set instead. |
| md.comp.filled-button.hover.label-text.color | COLOR | [Deprecated] Hovered / [Deprecated] Label text | `md.sys.color.on-primary` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.filled or md.comp.button.(size) set instead. |
| md.comp.filled-button.hover.state-layer.color | COLOR | [Deprecated] Hovered / [Deprecated] State layer | `md.sys.color.on-primary` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.filled or md.comp.button.(size) set instead. |
| md.comp.filled-button.hover.state-layer.opacity | OPACITY | [Deprecated] Hovered / [Deprecated] State layer | `md.sys.state.hover.state-layer-opacity` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.filled or md.comp.button.(size) set instead. |
| md.comp.filled-button.with-icon.hover.icon.color | COLOR | [Deprecated] Hovered / [Deprecated] Icon | `md.sys.color.on-primary` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.filled or md.comp.button.(size) set instead. |
| md.comp.filled-button.focus.indicator.color | COLOR | [Deprecated] Focused / [Deprecated] Focus indicator | `md.sys.color.secondary` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.filled or md.comp.button.(size) set instead. |
| md.comp.filled-button.focus.indicator.thickness | LENGTH | [Deprecated] Focused / [Deprecated] Focus indicator | `md.sys.state.focus-indicator.thickness` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.filled or md.comp.button.(size) set instead. |
| md.comp.filled-button.focus.indicator.outline.offset | LENGTH | [Deprecated] Focused / [Deprecated] Focus indicator | `md.sys.state.focus-indicator.outer-offset` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.filled or md.comp.button.(size) set instead. |
| md.comp.filled-button.focus.container.elevation | ELEVATION | [Deprecated] Focused / [Deprecated] Container | `md.sys.elevation.level0` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.filled or md.comp.button.(size) set instead. |
| md.comp.filled-button.focus.label-text.color | COLOR | [Deprecated] Focused / [Deprecated] Label text | `md.sys.color.on-primary` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.filled or md.comp.button.(size) set instead. |
| md.comp.filled-button.focus.state-layer.color | COLOR | [Deprecated] Focused / [Deprecated] State layer | `md.sys.color.on-primary` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.filled or md.comp.button.(size) set instead. |
| md.comp.filled-button.focus.state-layer.opacity | OPACITY | [Deprecated] Focused / [Deprecated] State layer | `md.sys.state.focus.state-layer-opacity` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.filled or md.comp.button.(size) set instead. |
| md.comp.filled-button.with-icon.focus.icon.color | COLOR | [Deprecated] Focused / [Deprecated] Icon | `md.sys.color.on-primary` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.filled or md.comp.button.(size) set instead. |
| md.comp.filled-button.pressed.container.elevation | ELEVATION | [Deprecated] Pressed (ripple) / [Deprecated] Container | `md.sys.elevation.level0` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.filled or md.comp.button.(size) set instead. |
| md.comp.filled-button.pressed.label-text.color | COLOR | [Deprecated] Pressed (ripple) / [Deprecated] Label text | `md.sys.color.on-primary` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.filled or md.comp.button.(size) set instead. |
| md.comp.filled-button.pressed.state-layer.color | COLOR | [Deprecated] Pressed (ripple) / [Deprecated] State layer | `md.sys.color.on-primary` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.filled or md.comp.button.(size) set instead. |
| md.comp.filled-button.pressed.state-layer.opacity | OPACITY | [Deprecated] Pressed (ripple) / [Deprecated] State layer | `md.sys.state.pressed.state-layer-opacity` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.filled or md.comp.button.(size) set instead. |
| md.comp.filled-button.with-icon.pressed.icon.color | COLOR | [Deprecated] Pressed (ripple) / [Deprecated] Icon | `md.sys.color.on-primary` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.filled or md.comp.button.(size) set instead. |
| md.comp.filled-button.dragged.container.elevation | ELEVATION | [Deprecated] Dragged / [Deprecated] Container | `md.sys.elevation.level3` |  | Bug fix. Filled buttons do not provide a dragged state. |
| md.comp.filled-button.dragged.label-text.color | COLOR | [Deprecated] Dragged / [Deprecated] Label text | `md.sys.color.on-primary` |  | Bug fix. Filled buttons do not provide a dragged state. |
| md.comp.filled-button.dragged.state-layer.color | COLOR | [Deprecated] Dragged / [Deprecated] State layer | `md.sys.color.on-primary` |  | Bug fix. Filled buttons do not provide a dragged state. |
| md.comp.filled-button.dragged.state-layer.opacity | OPACITY | [Deprecated] Dragged / [Deprecated] State layer | `md.sys.state.dragged.state-layer-opacity` |  | Bug fix. Filled buttons do not provide a dragged state. |
| md.comp.filled-button.with-icon.dragged.icon.color | COLOR | [Deprecated] Dragged / [Deprecated] Icon | `md.sys.color.on-primary` |  | Bug fix. Filled buttons do not provide a dragged state. |

#### [Deprecated] Button - Tonal (md.comp.filled-tonal-button)

This token set is deprecated. Use `md.comp.button.tonal` instead.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.filled-tonal-button.container.color | COLOR | [Deprecated] Enabled / [Deprecated] Container | `md.sys.color.secondary-container` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.tonal or md.comp.button.(size) set instead. |
| md.comp.filled-tonal-button.container.shadow-color | COLOR | [Deprecated] Enabled / [Deprecated] Container | `md.sys.color.shadow` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.tonal or md.comp.button.(size) set instead. |
| md.comp.filled-tonal-button.container.elevation | ELEVATION | [Deprecated] Enabled / [Deprecated] Container | `md.sys.elevation.level0` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.tonal or md.comp.button.(size) set instead. |
| md.comp.filled-tonal-button.container.height | LENGTH | [Deprecated] Enabled / [Deprecated] Container | 40dp |  | This token is deprecated. Use the corresponding token from either the md.comp.button.tonal or md.comp.button.(size) set instead. |
| md.comp.filled-tonal-button.container.shape | SHAPE | [Deprecated] Enabled / [Deprecated] Container | `md.sys.shape.corner.full` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.tonal or md.comp.button.(size) set instead. |
| md.comp.filled-tonal-button.label-text.color | COLOR | [Deprecated] Enabled / [Deprecated] Label text | `md.sys.color.on-secondary-container` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.tonal or md.comp.button.(size) set instead. |
| md.comp.filled-tonal-button.label-text.font | FONT_NAMES | [Deprecated] Enabled / [Deprecated] Label text | `md.sys.typescale.label-large.font` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.tonal or md.comp.button.(size) set instead. |
| md.comp.filled-tonal-button.label-text.line-height | LINE_HEIGHT | [Deprecated] Enabled / [Deprecated] Label text | `md.sys.typescale.label-large.line-height` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.tonal or md.comp.button.(size) set instead. |
| md.comp.filled-tonal-button.label-text.size | FONT_SIZE | [Deprecated] Enabled / [Deprecated] Label text | `md.sys.typescale.label-large.size` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.tonal or md.comp.button.(size) set instead. |
| md.comp.filled-tonal-button.label-text.tracking | FONT_TRACKING | [Deprecated] Enabled / [Deprecated] Label text | `md.sys.typescale.label-large.tracking` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.tonal or md.comp.button.(size) set instead. |
| md.comp.filled-tonal-button.label-text.weight | FONT_WEIGHT | [Deprecated] Enabled / [Deprecated] Label text | `md.sys.typescale.label-large.weight` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.tonal or md.comp.button.(size) set instead. |
| md.comp.filled-tonal-button.label-text.type | TYPOGRAPHY | [Deprecated] Enabled / [Deprecated] Label text | font name: `md.comp.filled-tonal-button.label-text.font`, font weight: `md.comp.filled-tonal-button.label-text.weight`, font size: `md.comp.filled-tonal-button.label-text.size`, font tracking: `md.comp.filled-tonal-button.label-text.tracking`, line height: `md.comp.filled-tonal-button.label-text.line-height` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.tonal or md.comp.button.(size) set instead. |
| md.comp.filled-tonal-button.with-icon.icon.color | COLOR | [Deprecated] Enabled / [Deprecated] Icon | `md.sys.color.on-secondary-container` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.tonal or md.comp.button.(size) set instead. |
| md.comp.filled-tonal-button.with-icon.icon.size | LENGTH | [Deprecated] Enabled / [Deprecated] Icon | 18dp |  | This token is deprecated. Use the corresponding token from either the md.comp.button.tonal or md.comp.button.(size) set instead. |
| md.comp.filled-tonal-button.disabled.container.color | COLOR | [Deprecated] Disabled / [Deprecated] Container | `md.sys.color.on-surface` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.tonal or md.comp.button.(size) set instead. |
| md.comp.filled-tonal-button.disabled.container.elevation | ELEVATION | [Deprecated] Disabled / [Deprecated] Container | `md.sys.elevation.level0` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.tonal or md.comp.button.(size) set instead. |
| md.comp.filled-tonal-button.disabled.container.opacity | OPACITY | [Deprecated] Disabled / [Deprecated] Container | 0.12 |  | This token is deprecated. Use the corresponding token from either the md.comp.button.tonal or md.comp.button.(size) set instead. |
| md.comp.filled-tonal-button.disabled.label-text.color | COLOR | [Deprecated] Disabled / [Deprecated] Label text | `md.sys.color.on-surface` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.tonal or md.comp.button.(size) set instead. |
| md.comp.filled-tonal-button.disabled.label-text.opacity | OPACITY | [Deprecated] Disabled / [Deprecated] Label text | 0.38 |  | This token is deprecated. Use the corresponding token from either the md.comp.button.tonal or md.comp.button.(size) set instead. |
| md.comp.filled-tonal-button.with-icon.disabled.icon.color | COLOR | [Deprecated] Disabled / [Deprecated] Icon | `md.sys.color.on-surface` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.tonal or md.comp.button.(size) set instead. |
| md.comp.filled-tonal-button.with-icon.disabled.icon.opacity | OPACITY | [Deprecated] Disabled / [Deprecated] Icon | 0.38 |  | This token is deprecated. Use the corresponding token from either the md.comp.button.tonal or md.comp.button.(size) set instead. |
| md.comp.filled-tonal-button.hover.container.elevation | ELEVATION | [Deprecated] Hovered / [Deprecated] Container | `md.sys.elevation.level1` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.tonal or md.comp.button.(size) set instead. |
| md.comp.filled-tonal-button.hover.label-text.color | COLOR | [Deprecated] Hovered / [Deprecated] Label text | `md.sys.color.on-secondary-container` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.tonal or md.comp.button.(size) set instead. |
| md.comp.filled-tonal-button.hover.state-layer.color | COLOR | [Deprecated] Hovered / [Deprecated] State layer | `md.sys.color.on-secondary-container` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.tonal or md.comp.button.(size) set instead. |
| md.comp.filled-tonal-button.hover.state-layer.opacity | OPACITY | [Deprecated] Hovered / [Deprecated] State layer | `md.sys.state.hover.state-layer-opacity` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.tonal or md.comp.button.(size) set instead. |
| md.comp.filled-tonal-button.with-icon.hover.icon.color | COLOR | [Deprecated] Hovered / [Deprecated] Icon | `md.sys.color.on-secondary-container` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.tonal or md.comp.button.(size) set instead. |
| md.comp.filled-tonal-button.focus.indicator.color | COLOR | [Deprecated] Focused / [Deprecated] Focus indicator | `md.sys.color.secondary` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.tonal or md.comp.button.(size) set instead. |
| md.comp.filled-tonal-button.focus.indicator.thickness | LENGTH | [Deprecated] Focused / [Deprecated] Focus indicator | `md.sys.state.focus-indicator.thickness` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.tonal or md.comp.button.(size) set instead. |
| md.comp.filled-tonal-button.focus.indicator.outline.offset | LENGTH | [Deprecated] Focused / [Deprecated] Focus indicator | `md.sys.state.focus-indicator.outer-offset` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.tonal or md.comp.button.(size) set instead. |
| md.comp.filled-tonal-button.focus.container.elevation | ELEVATION | [Deprecated] Focused / [Deprecated] Container | `md.sys.elevation.level0` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.tonal or md.comp.button.(size) set instead. |
| md.comp.filled-tonal-button.focus.label-text.color | COLOR | [Deprecated] Focused / [Deprecated] Label text | `md.sys.color.on-secondary-container` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.tonal or md.comp.button.(size) set instead. |
| md.comp.filled-tonal-button.focus.state-layer.color | COLOR | [Deprecated] Focused / [Deprecated] State layer | `md.sys.color.on-secondary-container` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.tonal or md.comp.button.(size) set instead. |
| md.comp.filled-tonal-button.focus.state-layer.opacity | OPACITY | [Deprecated] Focused / [Deprecated] State layer | `md.sys.state.focus.state-layer-opacity` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.tonal or md.comp.button.(size) set instead. |
| md.comp.filled-tonal-button.with-icon.focus.icon.color | COLOR | [Deprecated] Focused / [Deprecated] Icon | `md.sys.color.on-secondary-container` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.tonal or md.comp.button.(size) set instead. |
| md.comp.filled-tonal-button.pressed.container.elevation | ELEVATION | [Deprecated] Pressed (ripple) / [Deprecated] Container | `md.sys.elevation.level0` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.tonal or md.comp.button.(size) set instead. |
| md.comp.filled-tonal-button.pressed.label-text.color | COLOR | [Deprecated] Pressed (ripple) / [Deprecated] Label text | `md.sys.color.on-secondary-container` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.tonal or md.comp.button.(size) set instead. |
| md.comp.filled-tonal-button.pressed.state-layer.color | COLOR | [Deprecated] Pressed (ripple) / [Deprecated] State layer | `md.sys.color.on-secondary-container` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.tonal or md.comp.button.(size) set instead. |
| md.comp.filled-tonal-button.pressed.state-layer.opacity | OPACITY | [Deprecated] Pressed (ripple) / [Deprecated] State layer | `md.sys.state.pressed.state-layer-opacity` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.tonal or md.comp.button.(size) set instead. |
| md.comp.filled-tonal-button.with-icon.pressed.icon.color | COLOR | [Deprecated] Pressed (ripple) / [Deprecated] Icon | `md.sys.color.on-secondary-container` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.tonal or md.comp.button.(size) set instead. |

#### [Deprecated] Button – Outlined (md.comp.outlined-button)

This token set is deprecated. Use `md.comp.button.outlined` instead.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.outlined-button.container.shape | SHAPE | [Deprecated] Enabled / [Deprecated] Container | `md.sys.shape.corner.full` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.outlined or md.comp.button.(size) set instead. |
| md.comp.outlined-button.container.height | LENGTH | [Deprecated] Enabled / [Deprecated] Container | 40dp |  | This token is deprecated. Use the corresponding token from either the md.comp.button.outlined or md.comp.button.(size) set instead. |
| md.comp.outlined-button.container.elevation | ELEVATION | [Deprecated] Enabled / [Deprecated] Container | `md.sys.elevation.level0` |  | Reduced redundency in the system by eliminating tokens that duplicate the default value of zero. Replace usage with the default value of zero. |
| md.comp.outlined-button.outline.color | COLOR | [Deprecated] Enabled / [Deprecated] Outline | `md.sys.color.outline` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.outlined or md.comp.button.(size) set instead. |
| md.comp.outlined-button.outline.width | LENGTH | [Deprecated] Enabled / [Deprecated] Outline | 1dp |  | This token is deprecated. Use the corresponding token from either the md.comp.button.outlined or md.comp.button.(size) set instead. |
| md.comp.outlined-button.label-text.font | FONT_NAMES | [Deprecated] Enabled / [Deprecated] Label text | `md.sys.typescale.label-large.font` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.outlined or md.comp.button.(size) set instead. |
| md.comp.outlined-button.label-text.line-height | LINE_HEIGHT | [Deprecated] Enabled / [Deprecated] Label text | `md.sys.typescale.label-large.line-height` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.outlined or md.comp.button.(size) set instead. |
| md.comp.outlined-button.label-text.size | FONT_SIZE | [Deprecated] Enabled / [Deprecated] Label text | `md.sys.typescale.label-large.size` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.outlined or md.comp.button.(size) set instead. |
| md.comp.outlined-button.label-text.weight | FONT_WEIGHT | [Deprecated] Enabled / [Deprecated] Label text | `md.sys.typescale.label-large.weight` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.outlined or md.comp.button.(size) set instead. |
| md.comp.outlined-button.label-text.tracking | FONT_TRACKING | [Deprecated] Enabled / [Deprecated] Label text | `md.sys.typescale.label-large.tracking` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.outlined or md.comp.button.(size) set instead. |
| md.comp.outlined-button.label-text.type | TYPOGRAPHY | [Deprecated] Enabled / [Deprecated] Label text | font name: `md.comp.outlined-button.label-text.font`, font weight: `md.comp.outlined-button.label-text.weight`, font size: `md.comp.outlined-button.label-text.size`, font tracking: `md.comp.outlined-button.label-text.tracking`, line height: `md.comp.outlined-button.label-text.line-height` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.outlined or md.comp.button.(size) set instead. |
| md.comp.outlined-button.label-text.color | COLOR | [Deprecated] Enabled / [Deprecated] Label text | `md.sys.color.primary` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.outlined or md.comp.button.(size) set instead. |
| md.comp.outlined-button.with-icon.icon.size | LENGTH | [Deprecated] Enabled / [Deprecated] Icon | 18dp |  | This token is deprecated. Use the corresponding token from either the md.comp.button.outlined or md.comp.button.(size) set instead. |
| md.comp.outlined-button.with-icon.icon.color | COLOR | [Deprecated] Enabled / [Deprecated] Icon | `md.sys.color.primary` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.outlined or md.comp.button.(size) set instead. |
| md.comp.outlined-button.disabled.label-text.color | COLOR | [Deprecated] Disabled / [Deprecated] Label text | `md.sys.color.on-surface` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.outlined or md.comp.button.(size) set instead. |
| md.comp.outlined-button.disabled.label-text.opacity | OPACITY | [Deprecated] Disabled / [Deprecated] Label text | 0.38 |  | This token is deprecated. Use the corresponding token from either the md.comp.button.outlined or md.comp.button.(size) set instead. |
| md.comp.outlined-button.with-icon.disabled.icon.color | COLOR | [Deprecated] Disabled / [Deprecated] Icon | `md.sys.color.on-surface` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.outlined or md.comp.button.(size) set instead. |
| md.comp.outlined-button.with-icon.disabled.icon.opacity | OPACITY | [Deprecated] Disabled / [Deprecated] Icon | 0.38 |  | This token is deprecated. Use the corresponding token from either the md.comp.button.outlined or md.comp.button.(size) set instead. |
| md.comp.outlined-button.disabled.outline.color | COLOR | [Deprecated] Disabled / [Deprecated] Outline | `md.sys.color.on-surface` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.outlined or md.comp.button.(size) set instead. |
| md.comp.outlined-button.disabled.outline.opacity | OPACITY | [Deprecated] Disabled / [Deprecated] Outline | 0.12 |  | This token is deprecated. Use the corresponding token from either the md.comp.button.outlined or md.comp.button.(size) set instead. |
| md.comp.outlined-button.hover.label-text.color | COLOR | [Deprecated] Hovered / [Deprecated] Label text | `md.sys.color.primary` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.outlined or md.comp.button.(size) set instead. |
| md.comp.outlined-button.hover.state-layer.color | COLOR | [Deprecated] Hovered / [Deprecated] State layer | `md.sys.color.primary` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.outlined or md.comp.button.(size) set instead. |
| md.comp.outlined-button.hover.state-layer.opacity | OPACITY | [Deprecated] Hovered / [Deprecated] State layer | `md.sys.state.hover.state-layer-opacity` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.outlined or md.comp.button.(size) set instead. |
| md.comp.outlined-button.with-icon.hover.icon.color | COLOR | [Deprecated] Hovered / [Deprecated] Icon | `md.sys.color.primary` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.outlined or md.comp.button.(size) set instead. |
| md.comp.outlined-button.hover.outline.color | COLOR | [Deprecated] Hovered / [Deprecated] Outline | `md.sys.color.outline` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.outlined or md.comp.button.(size) set instead. |
| md.comp.outlined-button.focus.indicator.color | COLOR | [Deprecated] Focused / [Deprecated] Focus indicator | `md.sys.color.secondary` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.outlined or md.comp.button.(size) set instead. |
| md.comp.outlined-button.focus.indicator.thickness | LENGTH | [Deprecated] Focused / [Deprecated] Focus indicator | `md.sys.state.focus-indicator.thickness` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.outlined or md.comp.button.(size) set instead. |
| md.comp.outlined-button.focus.indicator.outline.offset | LENGTH | [Deprecated] Focused / [Deprecated] Focus indicator | `md.sys.state.focus-indicator.outer-offset` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.outlined or md.comp.button.(size) set instead. |
| md.comp.outlined-button.focus.label-text.color | COLOR | [Deprecated] Focused / [Deprecated] Label text | `md.sys.color.primary` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.outlined or md.comp.button.(size) set instead. |
| md.comp.outlined-button.focus.state-layer.color | COLOR | [Deprecated] Focused / [Deprecated] State layer | `md.sys.color.primary` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.outlined or md.comp.button.(size) set instead. |
| md.comp.outlined-button.focus.state-layer.opacity | OPACITY | [Deprecated] Focused / [Deprecated] State layer | `md.sys.state.focus.state-layer-opacity` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.outlined or md.comp.button.(size) set instead. |
| md.comp.outlined-button.with-icon.focus.icon.color | COLOR | [Deprecated] Focused / [Deprecated] Icon | `md.sys.color.primary` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.outlined or md.comp.button.(size) set instead. |
| md.comp.outlined-button.focus.outline.color | COLOR | [Deprecated] Focused / [Deprecated] Outline | `md.sys.color.primary` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.outlined or md.comp.button.(size) set instead. |
| md.comp.outlined-button.pressed.label-text.color | COLOR | [Deprecated] Pressed (ripple) / [Deprecated] Label text | `md.sys.color.primary` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.outlined or md.comp.button.(size) set instead. |
| md.comp.outlined-button.pressed.state-layer.color | COLOR | [Deprecated] Pressed (ripple) / [Deprecated] State layer | `md.sys.color.primary` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.outlined or md.comp.button.(size) set instead. |
| md.comp.outlined-button.pressed.state-layer.opacity | OPACITY | [Deprecated] Pressed (ripple) / [Deprecated] State layer | `md.sys.state.pressed.state-layer-opacity` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.outlined or md.comp.button.(size) set instead. |
| md.comp.outlined-button.with-icon.pressed.icon.color | COLOR | [Deprecated] Pressed (ripple) / [Deprecated] Icon | `md.sys.color.primary` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.outlined or md.comp.button.(size) set instead. |
| md.comp.outlined-button.pressed.outline.color | COLOR | [Deprecated] Pressed (ripple) / [Deprecated] Outline | `md.sys.color.outline` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.outlined or md.comp.button.(size) set instead. |

#### [Deprecated] Button - Text (md.comp.text-button)

This token set is deprecated. Use `md.comp.button.text` instead.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.text-button.container.shape | SHAPE | [Deprecated] Enabled / [Deprecated] Container | `md.sys.shape.corner.full` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.text or md.comp.button.(size) set instead. |
| md.comp.text-button.container.height | LENGTH | [Deprecated] Enabled / [Deprecated] Container | 40dp |  | This token is deprecated. Use the corresponding token from either the md.comp.button.text or md.comp.button.(size) set instead. |
| md.comp.text-button.container.elevation | ELEVATION | [Deprecated] Enabled / [Deprecated] Container | `md.sys.elevation.level0` |  | Bug: should not have been created. Remove any shadows on navigation bars. |
| md.comp.text-button.label-text.font | FONT_NAMES | [Deprecated] Enabled / [Deprecated] Label text | `md.sys.typescale.label-large.font` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.text or md.comp.button.(size) set instead. |
| md.comp.text-button.label-text.line-height | LINE_HEIGHT | [Deprecated] Enabled / [Deprecated] Label text | `md.sys.typescale.label-large.line-height` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.text or md.comp.button.(size) set instead. |
| md.comp.text-button.label-text.size | FONT_SIZE | [Deprecated] Enabled / [Deprecated] Label text | `md.sys.typescale.label-large.size` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.text or md.comp.button.(size) set instead. |
| md.comp.text-button.label-text.weight | FONT_WEIGHT | [Deprecated] Enabled / [Deprecated] Label text | `md.sys.typescale.label-large.weight` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.text or md.comp.button.(size) set instead. |
| md.comp.text-button.label-text.tracking | FONT_TRACKING | [Deprecated] Enabled / [Deprecated] Label text | `md.sys.typescale.label-large.tracking` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.text or md.comp.button.(size) set instead. |
| md.comp.text-button.label-text.type | TYPOGRAPHY | [Deprecated] Enabled / [Deprecated] Label text | font name: `md.comp.text-button.label-text.font`, font weight: `md.comp.text-button.label-text.weight`, font size: `md.comp.text-button.label-text.size`, font tracking: `md.comp.text-button.label-text.tracking`, line height: `md.comp.text-button.label-text.line-height` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.text or md.comp.button.(size) set instead. |
| md.comp.text-button.label-text.color | COLOR | [Deprecated] Enabled / [Deprecated] Label text | `md.sys.color.primary` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.text or md.comp.button.(size) set instead. |
| md.comp.text-button.with-icon.icon.size | LENGTH | [Deprecated] Enabled / [Deprecated] Icon | 18dp |  | This token is deprecated. Use the corresponding token from either the md.comp.button.text or md.comp.button.(size) set instead. |
| md.comp.text-button.with-icon.icon.color | COLOR | [Deprecated] Enabled / [Deprecated] Icon | `md.sys.color.primary` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.text or md.comp.button.(size) set instead. |
| md.comp.text-button.disabled.label-text.color | COLOR | [Deprecated] Disabled / [Deprecated] Label text | `md.sys.color.on-surface` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.text or md.comp.button.(size) set instead. |
| md.comp.text-button.disabled.label-text.opacity | OPACITY | [Deprecated] Disabled / [Deprecated] Label text | 0.38 |  | This token is deprecated. Use the corresponding token from either the md.comp.button.text or md.comp.button.(size) set instead. |
| md.comp.text-button.with-icon.disabled.icon.color | COLOR | [Deprecated] Disabled / [Deprecated] Icon | `md.sys.color.on-surface` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.text or md.comp.button.(size) set instead. |
| md.comp.text-button.with-icon.disabled.icon.opacity | OPACITY | [Deprecated] Disabled / [Deprecated] Icon | 0.38 |  | This token is deprecated. Use the corresponding token from either the md.comp.button.text or md.comp.button.(size) set instead. |
| md.comp.text-button.hover.label-text.color | COLOR | [Deprecated] Hovered / [Deprecated] Label text | `md.sys.color.primary` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.text or md.comp.button.(size) set instead. |
| md.comp.text-button.hover.state-layer.color | COLOR | [Deprecated] Hovered / [Deprecated] State layer | `md.sys.color.primary` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.text or md.comp.button.(size) set instead. |
| md.comp.text-button.hover.state-layer.opacity | OPACITY | [Deprecated] Hovered / [Deprecated] State layer | `md.sys.state.hover.state-layer-opacity` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.text or md.comp.button.(size) set instead. |
| md.comp.text-button.with-icon.hover.icon.color | COLOR | [Deprecated] Hovered / [Deprecated] Icon | `md.sys.color.primary` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.text or md.comp.button.(size) set instead. |
| md.comp.text-button.focus.indicator.color | COLOR | [Deprecated] Focused / [Deprecated] Focus indicator | `md.sys.color.secondary` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.text or md.comp.button.(size) set instead. |
| md.comp.text-button.focus.indicator.thickness | LENGTH | [Deprecated] Focused / [Deprecated] Focus indicator | `md.sys.state.focus-indicator.thickness` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.text or md.comp.button.(size) set instead. |
| md.comp.text-button.focus.indicator.outline.offset | LENGTH | [Deprecated] Focused / [Deprecated] Focus indicator | `md.sys.state.focus-indicator.outer-offset` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.text or md.comp.button.(size) set instead. |
| md.comp.text-button.focus.label-text.color | COLOR | [Deprecated] Focused / [Deprecated] Label text | `md.sys.color.primary` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.text or md.comp.button.(size) set instead. |
| md.comp.text-button.focus.state-layer.color | COLOR | [Deprecated] Focused / [Deprecated] State layer | `md.sys.color.primary` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.text or md.comp.button.(size) set instead. |
| md.comp.text-button.focus.state-layer.opacity | OPACITY | [Deprecated] Focused / [Deprecated] State layer | `md.sys.state.focus.state-layer-opacity` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.text or md.comp.button.(size) set instead. |
| md.comp.text-button.with-icon.focus.icon.color | COLOR | [Deprecated] Focused / [Deprecated] Icon | `md.sys.color.primary` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.text or md.comp.button.(size) set instead. |
| md.comp.text-button.pressed.label-text.color | COLOR | [Deprecated] Pressed (ripple) / [Deprecated] Label text | `md.sys.color.primary` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.text or md.comp.button.(size) set instead. |
| md.comp.text-button.pressed.state-layer.color | COLOR | [Deprecated] Pressed (ripple) / [Deprecated] State layer | `md.sys.color.primary` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.text or md.comp.button.(size) set instead. |
| md.comp.text-button.pressed.state-layer.opacity | OPACITY | [Deprecated] Pressed (ripple) / [Deprecated] State layer | `md.sys.state.pressed.state-layer-opacity` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.text or md.comp.button.(size) set instead. |
| md.comp.text-button.with-icon.pressed.icon.color | COLOR | [Deprecated] Pressed (ripple) / [Deprecated] Icon | `md.sys.color.primary` |  | This token is deprecated. Use the corresponding token from either the md.comp.button.text or md.comp.button.(size) set instead. |

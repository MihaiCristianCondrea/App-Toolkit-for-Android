---
title: "Chips"
source_url: "https://m3.material.io/components/chips/specs"
lastmod: "2026-05-18"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "components"
assets:
  - "assets/images/54bfc07dcff0-m01dv1nz-1.png"
  - "assets/images/f4bb9f661b0e-m01dv6ou-2.png"
  - "assets/images/ba865ad1bc7f-m8sg1d2r-03-3P.png"
  - "assets/images/a88000314f8d-m01dvj22-4.png"
  - "assets/images/1479dde443b5-m01dvpzm-5.png"
  - "assets/images/847ee1a5d814-mmp8q5ab-06-3P.png"
  - "assets/images/35b44fea02b4-m8sg2d2t-07-3P.png"
  - "assets/images/e46181d9bf46-m01dwqif-8.png"
  - "assets/images/c62eef564280-m01dwxct-9.png"
  - "assets/images/ad2251e2f83a-m02q3wit-10-3p.png"
  - "assets/images/c63dff074446-m8sg4m8e-11-3P.png"
  - "assets/images/dc6a00912874-m34ufke8-12-3p.png"
  - "assets/images/fe132d2470ef-m01dxq8p-13.png"
  - "assets/images/d296853652eb-m01dxxwr-14.png"
  - "assets/images/e9892a50b458-m8sg5r6a-15-3P.png"
  - "assets/images/e07890296d55-m01dybi0-16.png"
---

# Chips

Chips help people enter information, make selections, filter content, or trigger actions

## Specs

## Tokens & specs

Select a component variant below to see its elements, attributes, tokens, and values.

### TOKEN_TABLE

Component: Chips

Configured context tags: None, Medium contrast, Default, High contrast, Static, 3P, Android, Dark, Light

#### Token sets

| Token set | Name | Type | Tokens | Description |
| --- | --- | --- | --- | --- |
| Chip - Assist | md.comp.assist-chip | COMPONENT | 51 | For assist chips. |
| Chip - Filter | md.comp.filter-chip | COMPONENT | 107 | For filter chips. |
| Chip - Input | md.comp.input-chip | COMPONENT | 107 | For input chips. |
| Chip - Suggestion | md.comp.suggestion-chip | COMPONENT | 51 | For suggestion chips. |

#### Chip - Assist (md.comp.assist-chip)

For assist chips.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.assist-chip.container.shape | SHAPE | Enabled / Container | `md.sys.shape.corner.small` |  |  |
| md.comp.assist-chip.container.height | LENGTH | Enabled / Container | 32dp |  |  |
| md.comp.assist-chip.flat.container.elevation | ELEVATION | Enabled / Container | `md.sys.elevation.level0` |  |  |
| md.comp.assist-chip.flat.outline.color | COLOR | Enabled / Container | `md.sys.color.outline-variant` |  |  |
| md.comp.assist-chip.flat.outline.width | LENGTH | Enabled / Container | 1dp |  |  |
| md.comp.assist-chip.elevated.container.elevation | ELEVATION | Enabled / Container | `md.sys.elevation.level1` |  |  |
| md.comp.assist-chip.elevated.container.color | COLOR | Enabled / Container | `md.sys.color.surface-container-low` |  |  |
| md.comp.assist-chip.elevated.container.shadow-color | COLOR | Enabled / Container | `md.sys.color.shadow` |  |  |
| md.comp.assist-chip.container.surface-tint-layer.color | COLOR | Enabled / Container | `md.sys.color.surface-tint` |  | Deprecated as part of the update from opacity based surfaces to tonal surfaces. Surfaces no longer use surface-tint layers for tinting, please use the desired surface role directly as the container color. |
| md.comp.assist-chip.label-text.font | FONT_NAMES | Enabled / Label text | `md.sys.typescale.label-large.font` |  |  |
| md.comp.assist-chip.label-text.line-height | LINE_HEIGHT | Enabled / Label text | `md.sys.typescale.label-large.line-height` |  |  |
| md.comp.assist-chip.label-text.size | FONT_SIZE | Enabled / Label text | `md.sys.typescale.label-large.size` |  |  |
| md.comp.assist-chip.label-text.weight | FONT_WEIGHT | Enabled / Label text | `md.sys.typescale.label-large.weight` |  |  |
| md.comp.assist-chip.label-text.tracking | FONT_TRACKING | Enabled / Label text | `md.sys.typescale.label-large.tracking` |  |  |
| md.comp.assist-chip.label-text.type | TYPOGRAPHY | Enabled / Label text | font name: `md.comp.assist-chip.label-text.font`, font weight: `md.comp.assist-chip.label-text.weight`, font size: `md.comp.assist-chip.label-text.size`, font tracking: `md.comp.assist-chip.label-text.tracking`, line height: `md.comp.assist-chip.label-text.line-height` |  |  |
| md.comp.assist-chip.label-text.color | COLOR | Enabled / Label text | `md.sys.color.on-surface` |  |  |
| md.comp.assist-chip.with-icon.icon.size | LENGTH | Enabled / Icon | 18dp |  |  |
| md.comp.assist-chip.with-icon.icon.color | COLOR | Enabled / Icon | `md.sys.color.primary` |  |  |
| md.comp.assist-chip.flat.disabled.outline.color | COLOR | Disabled / Container | `md.sys.color.on-surface` |  |  |
| md.comp.assist-chip.flat.disabled.outline.opacity | OPACITY | Disabled / Container | 0.12 |  |  |
| md.comp.assist-chip.elevated.disabled.container.elevation | ELEVATION | Disabled / Container | `md.sys.elevation.level0` |  |  |
| md.comp.assist-chip.elevated.disabled.container.color | COLOR | Disabled / Container | `md.sys.color.on-surface` |  |  |
| md.comp.assist-chip.elevated.disabled.container.opacity | OPACITY | Disabled / Container | 0.12 |  |  |
| md.comp.assist-chip.disabled.label-text.color | COLOR | Disabled / Label text | `md.sys.color.on-surface` |  |  |
| md.comp.assist-chip.disabled.label-text.opacity | OPACITY | Disabled / Label text | 0.38 |  |  |
| md.comp.assist-chip.with-icon.disabled.icon.color | COLOR | Disabled / Icon | `md.sys.color.on-surface` |  |  |
| md.comp.assist-chip.with-icon.disabled.icon.opacity | OPACITY | Disabled / Icon | 0.38 |  |  |
| md.comp.assist-chip.elevated.hover.container.elevation | ELEVATION | Hovered / Container | `md.sys.elevation.level2` |  |  |
| md.comp.assist-chip.hover.label-text.color | COLOR | Hovered / Label text | `md.sys.color.on-surface` |  |  |
| md.comp.assist-chip.hover.state-layer.color | COLOR | Hovered / State layer | `md.sys.color.on-surface` |  |  |
| md.comp.assist-chip.hover.state-layer.opacity | OPACITY | Hovered / State layer | `md.sys.state.hover.state-layer-opacity` |  |  |
| md.comp.assist-chip.with-icon.hover.icon.color | COLOR | Hovered / Icon | `md.sys.color.primary` |  |  |
| md.comp.assist-chip.focus.indicator.color | COLOR | Focused / Focus indicator | `md.sys.color.secondary` |  |  |
| md.comp.assist-chip.focus.indicator.thickness | LENGTH | Focused / Focus indicator | `md.sys.state.focus-indicator.thickness` |  |  |
| md.comp.assist-chip.focus.indicator.outline.offset | LENGTH | Focused / Focus indicator | `md.sys.state.focus-indicator.outer-offset` |  |  |
| md.comp.assist-chip.flat.focus.outline.color | COLOR | Focused / Container | `md.sys.color.on-surface` |  |  |
| md.comp.assist-chip.elevated.focus.container.elevation | ELEVATION | Focused / Container | `md.sys.elevation.level1` |  |  |
| md.comp.assist-chip.focus.label-text.color | COLOR | Focused / Label text | `md.sys.color.on-surface` |  |  |
| md.comp.assist-chip.focus.state-layer.color | COLOR | Focused / State layer | `md.sys.color.on-surface` |  |  |
| md.comp.assist-chip.focus.state-layer.opacity | OPACITY | Focused / State layer | `md.sys.state.focus.state-layer-opacity` |  |  |
| md.comp.assist-chip.with-icon.focus.icon.color | COLOR | Focused / Icon | `md.sys.color.primary` |  |  |
| md.comp.assist-chip.elevated.pressed.container.elevation | ELEVATION | Pressed (ripple) / Container | `md.sys.elevation.level1` |  |  |
| md.comp.assist-chip.pressed.label-text.color | COLOR | Pressed (ripple) / Label text | `md.sys.color.on-surface` |  |  |
| md.comp.assist-chip.pressed.state-layer.color | COLOR | Pressed (ripple) / State layer | `md.sys.color.on-surface` |  |  |
| md.comp.assist-chip.pressed.state-layer.opacity | OPACITY | Pressed (ripple) / State layer | `md.sys.state.pressed.state-layer-opacity` |  |  |
| md.comp.assist-chip.with-icon.pressed.icon.color | COLOR | Pressed (ripple) / Icon | `md.sys.color.primary` |  |  |
| md.comp.assist-chip.dragged.container.elevation | ELEVATION | Dragged / Container | `md.sys.elevation.level4` |  |  |
| md.comp.assist-chip.dragged.label-text.color | COLOR | Dragged / Label text | `md.sys.color.on-surface` |  |  |
| md.comp.assist-chip.dragged.state-layer.color | COLOR | Dragged / State layer | `md.sys.color.on-surface` |  |  |
| md.comp.assist-chip.dragged.state-layer.opacity | OPACITY | Dragged / State layer | `md.sys.state.dragged.state-layer-opacity` |  |  |
| md.comp.assist-chip.with-icon.dragged.icon.color | COLOR | Dragged / Icon | `md.sys.color.primary` |  |  |

#### Chip - Filter (md.comp.filter-chip)

For filter chips.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.filter-chip.container.shape | SHAPE | Enabled / Container | `md.sys.shape.corner.small` |  |  |
| md.comp.filter-chip.container.height | LENGTH | Enabled / Container | 32dp |  |  |
| md.comp.filter-chip.container.shadow-color | COLOR | Enabled / Container | `md.sys.color.shadow` |  | Replaced with new token md.comp.filter-chip.elevated.container.shadow-color to include the "elevated" designation in the token name. |
| md.comp.filter-chip.container.surface-tint-layer.color | COLOR | Enabled / Container | `md.sys.color.surface-tint` |  | Deprecated as part of the update from opacity based surfaces to tonal surfaces. Surfaces no longer use surface-tint layers for tinting, please use the desired surface role directly as the container color. |
| md.comp.filter-chip.flat.container.elevation | ELEVATION | Enabled / Container | `md.sys.elevation.level0` |  |  |
| md.comp.filter-chip.flat.unselected.outline.color | COLOR | Enabled / Container | `md.sys.color.outline-variant` |  |  |
| md.comp.filter-chip.flat.unselected.outline.width | LENGTH | Enabled / Container | 1dp |  |  |
| md.comp.filter-chip.flat.selected.container.color | COLOR | Enabled / Container | `md.sys.color.secondary-container` |  |  |
| md.comp.filter-chip.flat.selected.outline.width | LENGTH | Enabled / Container | 0dp |  |  |
| md.comp.filter-chip.elevated.container.elevation | ELEVATION | Enabled / Container | `md.sys.elevation.level1` |  |  |
| md.comp.filter-chip.elevated.container.shadow-color | COLOR | Enabled / Container | `md.sys.color.shadow` |  |  |
| md.comp.filter-chip.elevated.unselected.container.color | COLOR | Enabled / Container | `md.sys.color.surface-container-low` |  |  |
| md.comp.filter-chip.elevated.selected.container.color | COLOR | Enabled / Container | `md.sys.color.secondary-container` |  |  |
| md.comp.filter-chip.label-text.font | FONT_NAMES | Enabled / Label text | `md.sys.typescale.label-large.font` |  |  |
| md.comp.filter-chip.label-text.line-height | LINE_HEIGHT | Enabled / Label text | `md.sys.typescale.label-large.line-height` |  |  |
| md.comp.filter-chip.label-text.size | FONT_SIZE | Enabled / Label text | `md.sys.typescale.label-large.size` |  |  |
| md.comp.filter-chip.label-text.weight | FONT_WEIGHT | Enabled / Label text | `md.sys.typescale.label-large.weight` |  |  |
| md.comp.filter-chip.label-text.tracking | FONT_TRACKING | Enabled / Label text | `md.sys.typescale.label-large.tracking` |  |  |
| md.comp.filter-chip.label-text.type | TYPOGRAPHY | Enabled / Label text | font name: `md.comp.filter-chip.label-text.font`, font weight: `md.comp.filter-chip.label-text.weight`, font size: `md.comp.filter-chip.label-text.size`, font tracking: `md.comp.filter-chip.label-text.tracking`, line height: `md.comp.filter-chip.label-text.line-height` |  |  |
| md.comp.filter-chip.unselected.label-text.color | COLOR | Enabled / Label text | `md.sys.color.on-surface-variant` |  |  |
| md.comp.filter-chip.selected.label-text.color | COLOR | Enabled / Label text | `md.sys.color.on-secondary-container` |  |  |
| md.comp.filter-chip.with-icon.icon.size | LENGTH | Enabled / Icon | 18dp |  |  |
| md.comp.filter-chip.with-leading-icon.selected.leading-icon.color | COLOR | Enabled / Icon | `md.sys.color.on-secondary-container` |  |  |
| md.comp.filter-chip.with-leading-icon.unselected.leading-icon.color | COLOR | Enabled / Icon | `md.sys.color.primary` |  |  |
| md.comp.filter-chip.with-trailing-icon.selected.trailing-icon.color | COLOR | Enabled / Icon | `md.sys.color.on-secondary-container` |  |  |
| md.comp.filter-chip.with-trailing-icon.unselected.trailing-icon.color | COLOR | Enabled / Icon | `md.sys.color.on-surface-variant` |  |  |
| md.comp.filter-chip.with-icon.unselected.icon.color | COLOR | Enabled / Icon | `md.sys.color.on-surface-variant` |  | Established distinct leading and trailing icon tokens for filter chips. Please use corresponding token added for leading and trailing instead. |
| md.comp.filter-chip.with-icon.selected.icon.color | COLOR | Enabled / Icon | `md.sys.color.on-secondary-container` |  | Established distinct leading and trailing icon tokens for filter chips. Please use corresponding token added for leading and trailing instead. |
| md.comp.filter-chip.with-trailing-icon.trailing-icon.unselected.color | COLOR | Enabled / Icon | `md.sys.color.on-surface-variant` |  | Wrongly named token. Please use this token instead: md.comp.filter-chip.with-trailing-icon.unselected.trailing-icon.color |
| md.comp.filter-chip.with-leading-icon.leading-icon.unselected.color | COLOR | Enabled / Icon | `md.sys.color.primary` |  | Wrongly named token. Please use this token instead: md.comp.filter-chip.with-leading-icon.selected.leading-icon.color |
| md.comp.filter-chip.flat.disabled.unselected.outline.color | COLOR | Disabled / Container | `md.sys.color.on-surface` |  |  |
| md.comp.filter-chip.flat.disabled.unselected.outline.opacity | OPACITY | Disabled / Container | 0.12 |  |  |
| md.comp.filter-chip.flat.disabled.selected.container.color | COLOR | Disabled / Container | `md.sys.color.on-surface` |  |  |
| md.comp.filter-chip.flat.disabled.selected.container.opacity | OPACITY | Disabled / Container | 0.12 |  |  |
| md.comp.filter-chip.elevated.disabled.container.elevation | ELEVATION | Disabled / Container | `md.sys.elevation.level0` |  |  |
| md.comp.filter-chip.elevated.disabled.container.color | COLOR | Disabled / Container | `md.sys.color.on-surface` |  |  |
| md.comp.filter-chip.elevated.disabled.container.opacity | OPACITY | Disabled / Container | 0.12 |  |  |
| md.comp.filter-chip.disabled.label-text.color | COLOR | Disabled / Label text | `md.sys.color.on-surface` |  |  |
| md.comp.filter-chip.disabled.label-text.opacity | OPACITY | Disabled / Label text | 0.38 |  |  |
| md.comp.filter-chip.with-leading-icon.disabled.leading-icon.color | COLOR | Disabled / Icon | `md.sys.color.on-surface` |  |  |
| md.comp.filter-chip.with-leading-icon.disabled.leading-icon.opacity | OPACITY | Disabled / Icon | 0.38 |  |  |
| md.comp.filter-chip.with-trailing-icon.disabled.trailing-icon.color | COLOR | Disabled / Icon | `md.sys.color.on-surface` |  |  |
| md.comp.filter-chip.with-trailing-icon.disabled.trailing-icon.opacity | OPACITY | Disabled / Icon | 0.38 |  |  |
| md.comp.filter-chip.with-icon.disabled.icon.color | COLOR | Disabled / Icon | `md.sys.color.on-surface` |  | Established distinct leading and trailing icon tokens for filter chips. Please use corresponding token added for leading and trailing instead. |
| md.comp.filter-chip.with-icon.disabled.icon.opacity | OPACITY | Disabled / Icon | 0.38 |  | Established distinct leading and trailing icon tokens for filter chips. Please use corresponding token added for leading and trailing instead. |
| md.comp.filter-chip.focus.indicator.color | COLOR | Focused / Focus indicator | `md.sys.color.secondary` |  |  |
| md.comp.filter-chip.focus.indicator.thickness | LENGTH | Focused / Focus indicator | `md.sys.state.focus-indicator.thickness` |  |  |
| md.comp.filter-chip.focus.indicator.outline.offset | LENGTH | Focused / Focus indicator | `md.sys.state.focus-indicator.outer-offset` |  |  |
| md.comp.filter-chip.flat.unselected.focus.outline.color | COLOR | Focused / Container | `md.sys.color.on-surface-variant` |  |  |
| md.comp.filter-chip.elevated.focus.container.elevation | ELEVATION | Focused / Container | `md.sys.elevation.level1` |  |  |
| md.comp.filter-chip.flat.selected.focus.container.elevation | ELEVATION | Focused / Container | `md.sys.elevation.level0` |  |  |
| md.comp.filter-chip.flat.unselected.focus.container.elevation | ELEVATION | Focused / Container | `md.sys.elevation.level0` |  |  |
| md.comp.filter-chip.unselected.focus.label-text.color | COLOR | Focused / Label text | `md.sys.color.on-surface-variant` |  |  |
| md.comp.filter-chip.selected.focus.label-text.color | COLOR | Focused / Label text | `md.sys.color.on-secondary-container` |  |  |
| md.comp.filter-chip.unselected.focus.state-layer.color | COLOR | Focused / State layer | `md.sys.color.on-surface-variant` |  |  |
| md.comp.filter-chip.unselected.focus.state-layer.opacity | OPACITY | Focused / State layer | `md.sys.state.focus.state-layer-opacity` |  |  |
| md.comp.filter-chip.selected.focus.state-layer.color | COLOR | Focused / State layer | `md.sys.color.on-secondary-container` |  |  |
| md.comp.filter-chip.selected.focus.state-layer.opacity | OPACITY | Focused / State layer | `md.sys.state.focus.state-layer-opacity` |  |  |
| md.comp.filter-chip.with-leading-icon.selected.focus.leading-icon.color | COLOR | Focused / Icon | `md.sys.color.on-secondary-container` |  |  |
| md.comp.filter-chip.with-leading-icon.unselected.focus.leading-icon.color | COLOR | Focused / Icon | `md.sys.color.primary` |  |  |
| md.comp.filter-chip.with-trailing-icon.selected.focus.trailing-icon.color | COLOR | Focused / Icon | `md.sys.color.on-secondary-container` |  |  |
| md.comp.filter-chip.with-trailing-icon.unselected.focus.trailing-icon.color | COLOR | Focused / Icon | `md.sys.color.on-surface-variant` |  |  |
| md.comp.filter-chip.with-icon.unselected.focus.icon.color | COLOR | Focused / Icon | `md.sys.color.on-surface-variant` |  | Established distinct leading and trailing icon tokens for filter chips. Please use corresponding token added for leading and trailing instead. |
| md.comp.filter-chip.with-icon.selected.focus.icon.color | COLOR | Focused / Icon | `md.sys.color.on-secondary-container` |  | Established distinct leading and trailing icon tokens for filter chips. Please use corresponding token added for leading and trailing instead. |
| md.comp.filter-chip.elevated.hover.container.elevation | ELEVATION | Hovered / Container | `md.sys.elevation.level2` |  |  |
| md.comp.filter-chip.flat.selected.hover.container.elevation | ELEVATION | Hovered / Container | `md.sys.elevation.level1` |  |  |
| md.comp.filter-chip.flat.unselected.hover.container.elevation | ELEVATION | Hovered / Container | `md.sys.elevation.level0` |  |  |
| md.comp.filter-chip.unselected.hover.label-text.color | COLOR | Hovered / Label text | `md.sys.color.on-surface-variant` |  |  |
| md.comp.filter-chip.selected.hover.label-text.color | COLOR | Hovered / Label text | `md.sys.color.on-secondary-container` |  |  |
| md.comp.filter-chip.unselected.hover.state-layer.color | COLOR | Hovered / State layer | `md.sys.color.on-surface-variant` |  |  |
| md.comp.filter-chip.unselected.hover.state-layer.opacity | OPACITY | Hovered / State layer | `md.sys.state.hover.state-layer-opacity` |  |  |
| md.comp.filter-chip.selected.hover.state-layer.color | COLOR | Hovered / State layer | `md.sys.color.on-secondary-container` |  |  |
| md.comp.filter-chip.selected.hover.state-layer.opacity | OPACITY | Hovered / State layer | `md.sys.state.hover.state-layer-opacity` |  |  |
| md.comp.filter-chip.with-leading-icon.selected.hover.leading-icon.color | COLOR | Hovered / Icon | `md.sys.color.on-secondary-container` |  |  |
| md.comp.filter-chip.with-leading-icon.unselected.hover.leading-icon.color | COLOR | Hovered / Icon | `md.sys.color.primary` |  |  |
| md.comp.filter-chip.with-trailing-icon.selected.hover.trailing-icon.color | COLOR | Hovered / Icon | `md.sys.color.on-secondary-container` |  |  |
| md.comp.filter-chip.with-trailing-icon.unselected.hover.trailing-icon.color | COLOR | Hovered / Icon | `md.sys.color.on-surface-variant` |  |  |
| md.comp.filter-chip.with-icon.unselected.hover.icon.color | COLOR | Hovered / Icon | `md.sys.color.on-surface-variant` |  | Established distinct leading and trailing icon tokens for filter chips. Please use corresponding token added for leading and trailing instead. |
| md.comp.filter-chip.with-icon.selected.hover.icon.color | COLOR | Hovered / Icon | `md.sys.color.on-secondary-container` |  | Established distinct leading and trailing icon tokens for filter chips. Please use corresponding token added for leading and trailing instead. |
| md.comp.filter-chip.elevated.pressed.container.elevation | ELEVATION | Pressed (ripple) / Container | `md.sys.elevation.level1` |  |  |
| md.comp.filter-chip.flat.selected.pressed.container.elevation | ELEVATION | Pressed (ripple) / Container | `md.sys.elevation.level0` |  |  |
| md.comp.filter-chip.flat.unselected.pressed.container.elevation | ELEVATION | Pressed (ripple) / Container | `md.sys.elevation.level0` |  |  |
| md.comp.filter-chip.unselected.pressed.label-text.color | COLOR | Pressed (ripple) / Label text | `md.sys.color.on-surface-variant` |  |  |
| md.comp.filter-chip.selected.pressed.label-text.color | COLOR | Pressed (ripple) / Label text | `md.sys.color.on-secondary-container` |  |  |
| md.comp.filter-chip.unselected.pressed.state-layer.color | COLOR | Pressed (ripple) / State layer | `md.sys.color.on-secondary-container` |  |  |
| md.comp.filter-chip.unselected.pressed.state-layer.opacity | OPACITY | Pressed (ripple) / State layer | `md.sys.state.pressed.state-layer-opacity` |  |  |
| md.comp.filter-chip.selected.pressed.state-layer.color | COLOR | Pressed (ripple) / State layer | `md.sys.color.on-surface-variant` |  |  |
| md.comp.filter-chip.selected.pressed.state-layer.opacity | OPACITY | Pressed (ripple) / State layer | `md.sys.state.pressed.state-layer-opacity` |  |  |
| md.comp.filter-chip.with-leading-icon.selected.pressed.leading-icon.color | COLOR | Pressed (ripple) / Icon | `md.sys.color.on-secondary-container` |  |  |
| md.comp.filter-chip.with-leading-icon.unselected.pressed.leading-icon.color | COLOR | Pressed (ripple) / Icon | `md.sys.color.primary` |  |  |
| md.comp.filter-chip.with-trailing-icon.selected.pressed.trailing-icon.color | COLOR | Pressed (ripple) / Icon | `md.sys.color.on-secondary-container` |  |  |
| md.comp.filter-chip.with-trailing-icon.unselected.pressed.trailing-icon.color | COLOR | Pressed (ripple) / Icon | `md.sys.color.on-surface-variant` |  |  |
| md.comp.filter-chip.with-icon.unselected.pressed.icon.color | COLOR | Pressed (ripple) / Icon | `md.sys.color.on-surface-variant` |  | Established distinct leading and trailing icon tokens for filter chips. Please use corresponding token added for leading and trailing instead. |
| md.comp.filter-chip.with-icon.selected.pressed.icon.color | COLOR | Pressed (ripple) / Icon | `md.sys.color.on-secondary-container` |  | Established distinct leading and trailing icon tokens for filter chips. Please use corresponding token added for leading and trailing instead. |
| md.comp.filter-chip.dragged.container.elevation | ELEVATION | Dragged / Container | `md.sys.elevation.level4` |  |  |
| md.comp.filter-chip.unselected.dragged.label-text.color | COLOR | Dragged / Label text | `md.sys.color.on-surface-variant` |  |  |
| md.comp.filter-chip.selected.dragged.label-text.color | COLOR | Dragged / Label text | `md.sys.color.on-secondary-container` |  |  |
| md.comp.filter-chip.unselected.dragged.state-layer.color | COLOR | Dragged / State layer | `md.sys.color.on-surface-variant` |  |  |
| md.comp.filter-chip.unselected.dragged.state-layer.opacity | OPACITY | Dragged / State layer | `md.sys.state.dragged.state-layer-opacity` |  |  |
| md.comp.filter-chip.selected.dragged.state-layer.color | COLOR | Dragged / State layer | `md.sys.color.on-secondary-container` |  |  |
| md.comp.filter-chip.selected.dragged.state-layer.opacity | OPACITY | Dragged / State layer | `md.sys.state.dragged.state-layer-opacity` |  |  |
| md.comp.filter-chip.with-leading-icon.selected.dragged.leading-icon.color | COLOR | Dragged / Icon | `md.sys.color.on-secondary-container` |  |  |
| md.comp.filter-chip.with-leading-icon.unselected.dragged.leading-icon.color | COLOR | Dragged / Icon | `md.sys.color.primary` |  |  |
| md.comp.filter-chip.with-trailing-icon.selected.dragged.trailing-icon.color | COLOR | Dragged / Icon | `md.sys.color.on-secondary-container` |  |  |
| md.comp.filter-chip.with-trailing-icon.unselected.dragged.trailing-icon.color | COLOR | Dragged / Icon | `md.sys.color.on-surface-variant` |  |  |
| md.comp.filter-chip.with-icon.unselected.dragged.icon.color | COLOR | Dragged / Icon | `md.sys.color.on-surface-variant` |  | Established distinct leading and trailing icon tokens for filter chips. Please use corresponding token added for leading and trailing instead. |
| md.comp.filter-chip.with-icon.selected.dragged.icon.color | COLOR | Dragged / Icon | `md.sys.color.on-secondary-container` |  | Established distinct leading and trailing icon tokens for filter chips. Please use corresponding token added for leading and trailing instead. |

#### Chip - Input (md.comp.input-chip)

For input chips.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.input-chip.with-avatar.avatar.size | LENGTH | Enabled / Avatar | 24dp |  |  |
| md.comp.input-chip.with-avatar.avatar.shape | SHAPE | Enabled / Avatar | `md.sys.shape.corner.full` |  |  |
| md.comp.input-chip.container.shape | SHAPE | Enabled / Container | `md.sys.shape.corner.small` |  |  |
| md.comp.input-chip.container.height | LENGTH | Enabled / Container | 32dp |  |  |
| md.comp.input-chip.container.elevation | ELEVATION | Enabled / Container | `md.sys.elevation.level0` |  |  |
| md.comp.input-chip.unselected.outline.color | COLOR | Enabled / Container | `md.sys.color.outline-variant` |  |  |
| md.comp.input-chip.unselected.outline.width | LENGTH | Enabled / Container | 1dp |  |  |
| md.comp.input-chip.selected.container.color | COLOR | Enabled / Container | `md.sys.color.secondary-container` |  |  |
| md.comp.input-chip.selected.outline.width | LENGTH | Enabled / Container | 0dp |  |  |
| md.comp.input-chip.outline.color | COLOR | Enabled / Container | `md.sys.color.outline` |  | With this update we are introducing selected and unselected state tokens for input chips. Please replace these tokens with the newly added and equivalent "md.comp.input-chip.selected.…" or "md.comp.input-chip.unselected.…" tokens depending on component status. |
| md.comp.input-chip.outline.width | LENGTH | Enabled / Container | 1dp |  | With this update we are introducing selected and unselected state tokens for input chips. Please replace these tokens with the newly added and equivalent "md.comp.input-chip.selected.…" or "md.comp.input-chip.unselected.…" tokens depending on component status. |
| md.comp.input-chip.label-text.font | FONT_NAMES | Enabled / Label text | `md.sys.typescale.label-large.font` |  |  |
| md.comp.input-chip.label-text.line-height | LINE_HEIGHT | Enabled / Label text | `md.sys.typescale.label-large.line-height` |  |  |
| md.comp.input-chip.label-text.size | FONT_SIZE | Enabled / Label text | `md.sys.typescale.label-large.size` |  |  |
| md.comp.input-chip.label-text.weight | FONT_WEIGHT | Enabled / Label text | `md.sys.typescale.label-large.weight` |  |  |
| md.comp.input-chip.label-text.tracking | FONT_TRACKING | Enabled / Label text | `md.sys.typescale.label-large.tracking` |  |  |
| md.comp.input-chip.label-text.type | TYPOGRAPHY | Enabled / Label text | font name: `md.comp.input-chip.label-text.font`, font weight: `md.comp.input-chip.label-text.weight`, font size: `md.comp.input-chip.label-text.size`, font tracking: `md.comp.input-chip.label-text.tracking`, line height: `md.comp.input-chip.label-text.line-height` |  |  |
| md.comp.input-chip.selected.label-text.color | COLOR | Enabled / Label text | `md.sys.color.on-secondary-container` |  |  |
| md.comp.input-chip.unselected.label-text.color | COLOR | Enabled / Label text | `md.sys.color.on-surface-variant` |  |  |
| md.comp.input-chip.label-text.color | COLOR | Enabled / Label text | `md.sys.color.on-surface-variant` |  | With this update we are introducing selected and unselected state tokens for input chips. Please replace these tokens with the newly added and equivalent "md.comp.input-chip.selected.…" or "md.comp.input-chip.unselected.…" tokens depending on component status. |
| md.comp.input-chip.with-leading-icon.leading-icon.size | LENGTH | Enabled / Leading icon | 18dp |  |  |
| md.comp.input-chip.with-leading-icon.selected.leading-icon.color | COLOR | Enabled / Leading icon | `md.sys.color.primary` |  |  |
| md.comp.input-chip.with-leading-icon.unselected.leading-icon.color | COLOR | Enabled / Leading icon | `md.sys.color.on-surface-variant` |  |  |
| md.comp.input-chip.with-leading-icon.leading-icon.color | COLOR | Enabled / Leading icon | `md.sys.color.on-surface-variant` |  | With this update we are introducing selected and unselected state tokens for input chips. Please replace these tokens with the newly added and equivalent "md.comp.input-chip.with-leading-icon.selected.…" or "md.comp.input-chip.with-leading-icon.unselected.…" tokens depending on component status. |
| md.comp.input-chip.with-trailing-icon.trailing-icon.size | LENGTH | Enabled / Trailing icon | 18dp |  |  |
| md.comp.input-chip.with-trailing-icon.selected.trailing-icon.color | COLOR | Enabled / Trailing icon | `md.sys.color.on-secondary-container` |  |  |
| md.comp.input-chip.with-trailing-icon.unselected.trailing-icon.color | COLOR | Enabled / Trailing icon | `md.sys.color.on-surface-variant` |  |  |
| md.comp.input-chip.with-trailing-icon.trailing-icon.color | COLOR | Enabled / Trailing icon | `md.sys.color.on-surface-variant` |  | With this update we are introducing selected and unselected state tokens for input chips. Please replace these tokens with the newly added and equivalent "md.comp.input-chip.with-trailing-icon.selected.…" or "md.comp.input-chip.with-trailing-icon.unselected.…" tokens depending on component status. |
| md.comp.input-chip.disabled.unselected.outline.color | COLOR | Disabled / Container | `md.sys.color.on-surface` |  |  |
| md.comp.input-chip.disabled.unselected.outline.opacity | OPACITY | Disabled / Container | 0.12 |  |  |
| md.comp.input-chip.disabled.selected.container.color | COLOR | Disabled / Container | `md.sys.color.on-surface` |  |  |
| md.comp.input-chip.disabled.selected.container.opacity | OPACITY | Disabled / Container | 0.12 |  |  |
| md.comp.input-chip.disabled.outline.color | COLOR | Disabled / Container | `md.sys.color.on-surface` |  | With this update we are introducing selected and unselected state tokens for input chips. Please replace these tokens with the newly added and equivalent "md.comp.input-chip.selected.…" or "md.comp.input-chip.unselected.…" tokens depending on component status. |
| md.comp.input-chip.disabled.outline.opacity | OPACITY | Disabled / Container | 0.12 |  | With this update we are introducing selected and unselected state tokens for input chips. Please replace these tokens with the newly added and equivalent "md.comp.input-chip.selected.…" or "md.comp.input-chip.unselected.…" tokens depending on component status. |
| md.comp.input-chip.disabled.label-text.color | COLOR | Disabled / Label text | `md.sys.color.on-surface` |  |  |
| md.comp.input-chip.disabled.label-text.opacity | OPACITY | Disabled / Label text | 0.38 |  |  |
| md.comp.input-chip.with-leading-icon.disabled.leading-icon.color | COLOR | Disabled / Leading icon | `md.sys.color.on-surface` |  |  |
| md.comp.input-chip.with-leading-icon.disabled.leading-icon.opacity | OPACITY | Disabled / Leading icon | 0.38 |  |  |
| md.comp.input-chip.with-trailing-icon.disabled.trailing-icon.color | COLOR | Disabled / Trailing icon | `md.sys.color.on-surface` |  |  |
| md.comp.input-chip.with-trailing-icon.disabled.trailing-icon.opacity | OPACITY | Disabled / Trailing icon | 0.38 |  |  |
| md.comp.input-chip.with-avatar.disabled.avatar.opacity | OPACITY | Disabled / Avatar | 0.38 |  |  |
| md.comp.input-chip.unselected.hover.label-text.color | COLOR | Hovered / Label text | `md.sys.color.on-surface-variant` |  |  |
| md.comp.input-chip.selected.hover.label-text.color | COLOR | Hovered / Label text | `md.sys.color.on-secondary-container` |  |  |
| md.comp.input-chip.hover.label-text.color | COLOR | Hovered / Label text | `md.sys.color.on-surface-variant` |  | With this update we are introducing selected and unselected state tokens for input chips. Please replace these tokens with the newly added and equivalent "md.comp.input-chip.selected.…" or "md.comp.input-chip.unselected.…" tokens depending on component status. |
| md.comp.input-chip.unselected.hover.state-layer.color | COLOR | Hovered / State layer | `md.sys.color.on-surface-variant` |  |  |
| md.comp.input-chip.unselected.hover.state-layer.opacity | OPACITY | Hovered / State layer | `md.sys.state.hover.state-layer-opacity` |  |  |
| md.comp.input-chip.selected.hover.state-layer.color | COLOR | Hovered / State layer | `md.sys.color.on-secondary-container` |  |  |
| md.comp.input-chip.selected.hover.state-layer.opacity | OPACITY | Hovered / State layer | `md.sys.state.hover.state-layer-opacity` |  |  |
| md.comp.input-chip.hover.state-layer.color | COLOR | Hovered / State layer | `md.sys.color.on-surface-variant` |  | With this update we are introducing selected and unselected state tokens for input chips. Please replace these tokens with the newly added and equivalent "md.comp.input-chip.selected.…" or "md.comp.input-chip.unselected.…" tokens depending on component status. |
| md.comp.input-chip.hover.state-layer.opacity | OPACITY | Hovered / State layer | `md.sys.state.hover.state-layer-opacity` |  | With this update we are introducing selected and unselected state tokens for input chips. Please replace these tokens with the newly added and equivalent "md.comp.input-chip.selected.…" or "md.comp.input-chip.unselected.…" tokens depending on component status. |
| md.comp.input-chip.with-leading-icon.unselected.hover.leading-icon.color | COLOR | Hovered / Leading icon | `md.sys.color.primary` |  |  |
| md.comp.input-chip.with-leading-icon.selected.hover.leading-icon.color | COLOR | Hovered / Leading icon | `md.sys.color.primary` |  |  |
| md.comp.input-chip.with-leading-icon.hover.leading-icon.color | COLOR | Hovered / Leading icon | `md.sys.color.on-surface-variant` |  | With this update we are introducing selected and unselected state tokens for input chips. Please replace these tokens with the newly added and equivalent "md.comp.input-chip.with-leading-icon.selected.…" or "md.comp.input-chip.with-leading-icon.unselected.…" tokens depending on component status. |
| md.comp.input-chip.with-trailing-icon.unselected.hover.trailing-icon.color | COLOR | Hovered / Trailing icon | `md.sys.color.on-surface-variant` |  |  |
| md.comp.input-chip.with-trailing-icon.selected.hover.trailing-icon.color | COLOR | Hovered / Trailing icon | `md.sys.color.on-secondary-container` |  |  |
| md.comp.input-chip.with-trailing-icon.hover.trailing-icon.color | COLOR | Hovered / Trailing icon | `md.sys.color.on-surface-variant` |  | With this update we are introducing selected and unselected state tokens for input chips. Please replace these tokens with the newly added and equivalent "md.comp.input-chip.with-trailing-icon.selected.…" or "md.comp.input-chip.with-trailing-icon.unselected.…" tokens depending on component status. |
| md.comp.input-chip.focus.indicator.color | COLOR | Focused / Focus indicator | `md.sys.color.secondary` |  |  |
| md.comp.input-chip.focus.indicator.thickness | LENGTH | Focused / Focus indicator | `md.sys.state.focus-indicator.thickness` |  |  |
| md.comp.input-chip.focus.indicator.outline.offset | LENGTH | Focused / Focus indicator | `md.sys.state.focus-indicator.outer-offset` |  |  |
| md.comp.input-chip.unselected.focus.outline.color | COLOR | Focused / Container | `md.sys.color.on-surface-variant` |  |  |
| md.comp.input-chip.focus.outline.color | COLOR | Focused / Container | `md.sys.color.on-surface-variant` |  | With this update we are introducing selected and unselected state tokens for input chips. Please replace these tokens with the newly added and equivalent "md.comp.input-chip.selected.…" or "md.comp.input-chip.unselected.…" tokens depending on component status. |
| md.comp.input-chip.unselected.focus.label-text.color | COLOR | Focused / Label text | `md.sys.color.on-surface-variant` |  |  |
| md.comp.input-chip.selected.focus.label-text.color | COLOR | Focused / Label text | `md.sys.color.on-secondary-container` |  |  |
| md.comp.input-chip.focus.label-text.color | COLOR | Focused / Label text | `md.sys.color.on-surface-variant` |  | With this update we are introducing selected and unselected state tokens for input chips. Please replace these tokens with the newly added and equivalent "md.comp.input-chip.selected.…" or "md.comp.input-chip.unselected.…" tokens depending on component status. |
| md.comp.input-chip.unselected.focus.state-layer.color | COLOR | Focused / State layer | `md.sys.color.on-surface-variant` |  |  |
| md.comp.input-chip.unselected.focus.state-layer.opacity | OPACITY | Focused / State layer | `md.sys.state.focus.state-layer-opacity` |  |  |
| md.comp.input-chip.selected.focus.state-layer.color | COLOR | Focused / State layer | `md.sys.color.on-secondary-container` |  |  |
| md.comp.input-chip.selected.focus.state-layer.opacity | OPACITY | Focused / State layer | `md.sys.state.focus.state-layer-opacity` |  |  |
| md.comp.input-chip.focus.state-layer.color | COLOR | Focused / State layer | `md.sys.color.on-surface-variant` |  | With this update we are introducing selected and unselected state tokens for input chips. Please replace these tokens with the newly added and equivalent "md.comp.input-chip.selected.…" or "md.comp.input-chip.unselected.…" tokens depending on component status. |
| md.comp.input-chip.focus.state-layer.opacity | OPACITY | Focused / State layer | `md.sys.state.focus.state-layer-opacity` |  | With this update we are introducing selected and unselected state tokens for input chips. Please replace these tokens with the newly added and equivalent "md.comp.input-chip.selected.…" or "md.comp.input-chip.unselected.…" tokens depending on component status. |
| md.comp.input-chip.with-leading-icon.unselected.focus.leading-icon.color | COLOR | Focused / Leading icon | `md.sys.color.primary` |  |  |
| md.comp.input-chip.with-leading-icon.selected.focus.leading-icon.color | COLOR | Focused / Leading icon | `md.sys.color.primary` |  |  |
| md.comp.input-chip.with-leading-icon.focus.leading-icon.color | COLOR | Focused / Leading icon | `md.sys.color.on-surface-variant` |  | With this update we are introducing selected and unselected state tokens for input chips. Please replace these tokens with the newly added and equivalent "md.comp.input-chip.with-leading-icon.selected.…" or "md.comp.input-chip.with-leading-icon.unselected.…" tokens depending on component status. |
| md.comp.input-chip.with-trailing-icon.unselected.focus.trailing-icon.color | COLOR | Focused / Trailing icon | `md.sys.color.on-surface-variant` |  |  |
| md.comp.input-chip.with-trailing-icon.selected.focus.trailing-icon.color | COLOR | Focused / Trailing icon | `md.sys.color.on-secondary-container` |  |  |
| md.comp.input-chip.with-trailing-icon.focus.trailing-icon.color | COLOR | Focused / Trailing icon | `md.sys.color.on-surface-variant` |  | With this update we are introducing selected and unselected state tokens for input chips. Please replace these tokens with the newly added and equivalent "md.comp.input-chip.with-trailing-icon.selected.…" or "md.comp.input-chip.with-trailing-icon.unselected.…" tokens depending on component status. |
| md.comp.input-chip.unselected.pressed.label-text.color | COLOR | Pressed (ripple) / Label text | `md.sys.color.on-surface-variant` |  |  |
| md.comp.input-chip.selected.pressed.label-text.color | COLOR | Pressed (ripple) / Label text | `md.sys.color.on-secondary-container` |  |  |
| md.comp.input-chip.pressed.label-text.color | COLOR | Pressed (ripple) / Label text | `md.sys.color.on-surface-variant` |  | With this update we are introducing selected and unselected state tokens for input chips. Please replace these tokens with the newly added and equivalent "md.comp.input-chip.selected.…" or "md.comp.input-chip.unselected.…" tokens depending on component status. |
| md.comp.input-chip.unselected.pressed.state-layer.color | COLOR | Pressed (ripple) / State layer | `md.sys.color.on-surface-variant` |  |  |
| md.comp.input-chip.unselected.pressed.state-layer.opacity | OPACITY | Pressed (ripple) / State layer | `md.sys.state.pressed.state-layer-opacity` |  |  |
| md.comp.input-chip.selected.pressed.state-layer.color | COLOR | Pressed (ripple) / State layer | `md.sys.color.on-secondary-container` |  |  |
| md.comp.input-chip.selected.pressed.state-layer.opacity | OPACITY | Pressed (ripple) / State layer | `md.sys.state.pressed.state-layer-opacity` |  |  |
| md.comp.input-chip.pressed.state-layer.color | COLOR | Pressed (ripple) / State layer | `md.sys.color.on-surface-variant` |  | With this update we are introducing selected and unselected state tokens for input chips. Please replace these tokens with the newly added and equivalent "md.comp.input-chip.selected.…" or "md.comp.input-chip.unselected.…" tokens depending on component status. |
| md.comp.input-chip.pressed.state-layer.opacity | OPACITY | Pressed (ripple) / State layer | `md.sys.state.pressed.state-layer-opacity` |  | With this update we are introducing selected and unselected state tokens for input chips. Please replace these tokens with the newly added and equivalent "md.comp.input-chip.selected.…" or "md.comp.input-chip.unselected.…" tokens depending on component status. |
| md.comp.input-chip.with-leading-icon.unselected.pressed.leading-icon.color | COLOR | Pressed (ripple) / Leading icon | `md.sys.color.primary` |  |  |
| md.comp.input-chip.with-leading-icon.selected.pressed.leading-icon.color | COLOR | Pressed (ripple) / Leading icon | `md.sys.color.primary` |  |  |
| md.comp.input-chip.with-leading-icon.pressed.leading-icon.color | COLOR | Pressed (ripple) / Leading icon | `md.sys.color.on-surface-variant` |  | With this update we are introducing selected and unselected state tokens for input chips. Please replace these tokens with the newly added and equivalent "md.comp.input-chip.with-leading-icon.selected.…" or "md.comp.input-chip.with-leading-icon.unselected.…" tokens depending on component status. |
| md.comp.input-chip.with-trailing-icon.unselected.pressed.trailing-icon.color | COLOR | Pressed (ripple) / Trailing icon | `md.sys.color.on-surface-variant` |  |  |
| md.comp.input-chip.with-trailing-icon.selected.pressed.trailing-icon.color | COLOR | Pressed (ripple) / Trailing icon | `md.sys.color.on-secondary-container` |  |  |
| md.comp.input-chip.with-trailing-icon.pressed.trailing-icon.color | COLOR | Pressed (ripple) / Trailing icon | `md.sys.color.on-surface-variant` |  | With this update we are introducing selected and unselected state tokens for input chips. Please replace these tokens with the newly added and equivalent "md.comp.input-chip.with-trailing-icon.selected.…" or "md.comp.input-chip.with-trailing-icon.unselected.…" tokens depending on component status. |
| md.comp.input-chip.dragged.container.elevation | ELEVATION | Dragged / Container | `md.sys.elevation.level4` |  |  |
| md.comp.input-chip.unselected.dragged.label-text.color | COLOR | Dragged / Label text | `md.sys.color.on-surface-variant` |  |  |
| md.comp.input-chip.selected.dragged.label-text.color | COLOR | Dragged / Label text | `md.sys.color.on-secondary-container` |  |  |
| md.comp.input-chip.dragged.label-text.color | COLOR | Dragged / Label text | `md.sys.color.on-surface-variant` |  | With this update we are introducing selected and unselected state tokens for input chips. Please replace these tokens with the newly added and equivalent "md.comp.input-chip.selected.…" or "md.comp.input-chip.unselected.…" tokens depending on component status. |
| md.comp.input-chip.unselected.dragged.state-layer.color | COLOR | Dragged / State layer | `md.sys.color.on-surface-variant` |  |  |
| md.comp.input-chip.unselected.dragged.state-layer.opacity | OPACITY | Dragged / State layer | `md.sys.state.dragged.state-layer-opacity` |  |  |
| md.comp.input-chip.selected.dragged.state-layer.color | COLOR | Dragged / State layer | `md.sys.color.on-secondary-container` |  |  |
| md.comp.input-chip.selected.dragged.state-layer.opacity | OPACITY | Dragged / State layer | `md.sys.state.dragged.state-layer-opacity` |  |  |
| md.comp.input-chip.dragged.state-layer.color | COLOR | Dragged / State layer | `md.sys.color.on-surface-variant` |  | With this update we are introducing selected and unselected state tokens for input chips. Please replace these tokens with the newly added and equivalent "md.comp.input-chip.selected.…" or "md.comp.input-chip.unselected.…" tokens depending on component status. |
| md.comp.input-chip.dragged.state-layer.opacity | OPACITY | Dragged / State layer | `md.sys.state.dragged.state-layer-opacity` |  | With this update we are introducing selected and unselected state tokens for input chips. Please replace these tokens with the newly added and equivalent "md.comp.input-chip.selected.…" or "md.comp.input-chip.unselected.…" tokens depending on component status. |
| md.comp.input-chip.with-trailing-icon.unselected.dragged.trailing-icon.color | COLOR | Dragged / Trailing icon | `md.sys.color.primary` |  |  |
| md.comp.input-chip.with-trailing-icon.selected.dragged.trailing-icon.color | COLOR | Dragged / Trailing icon | `md.sys.color.primary` |  |  |
| md.comp.input-chip.with-trailing-icon.dragged.trailing-icon.color | COLOR | Dragged / Trailing icon | `md.sys.color.on-surface-variant` |  | With this update we are introducing selected and unselected state tokens for input chips. Please replace these tokens with the newly added and equivalent "md.comp.input-chip.with-trailing-icon.selected.…" or "md.comp.input-chip.with-trailing-icon.unselected.…" tokens depending on component status. |
| md.comp.input-chip.with-leading-icon.unselected.dragged.leading-icon.color | COLOR | Dragged / Leading icon | `md.sys.color.on-surface-variant` |  |  |
| md.comp.input-chip.with-leading-icon.selected.dragged.leading-icon.color | COLOR | Dragged / Leading icon | `md.sys.color.on-secondary-container` |  |  |
| md.comp.input-chip.with-leading-icon.dragged.leading-icon.color | COLOR | Dragged / Leading icon | `md.sys.color.on-surface-variant` |  | With this update we are introducing selected and unselected state tokens for input chips. Please replace these tokens with the newly added and equivalent "md.comp.input-chip.with-leading-icon.selected.…" or "md.comp.input-chip.with-leading-icon.unselected.…" tokens depending on component status. |

#### Chip - Suggestion (md.comp.suggestion-chip)

For suggestion chips.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.suggestion-chip.container.shape | SHAPE | Enabled / Container | `md.sys.shape.corner.small` |  |  |
| md.comp.suggestion-chip.container.height | LENGTH | Enabled / Container | 32dp |  |  |
| md.comp.suggestion-chip.flat.container.elevation | ELEVATION | Enabled / Container | `md.sys.elevation.level0` |  |  |
| md.comp.suggestion-chip.flat.outline.color | COLOR | Enabled / Container | `md.sys.color.outline-variant` |  |  |
| md.comp.suggestion-chip.flat.outline.width | LENGTH | Enabled / Container | 1dp |  |  |
| md.comp.suggestion-chip.elevated.container.elevation | ELEVATION | Enabled / Container | `md.sys.elevation.level1` |  |  |
| md.comp.suggestion-chip.elevated.container.color | COLOR | Enabled / Container | `md.sys.color.surface-container-low` |  |  |
| md.comp.suggestion-chip.elevated.container.shadow-color | COLOR | Enabled / Container | `md.sys.color.shadow` |  |  |
| md.comp.suggestion-chip.container.surface-tint-layer.color | COLOR | Enabled / Container | `md.sys.color.surface-tint` |  | Deprecated as part of the update from opacity based surfaces to tonal surfaces. Surfaces no longer use surface-tint layers for tinting, please use the desired surface role directly as the container color. |
| md.comp.suggestion-chip.with-leading-icon.leading-icon.color | COLOR | Enabled / Icon | `md.sys.color.primary` |  |  |
| md.comp.suggestion-chip.with-leading-icon.leading-icon.size | LENGTH | Enabled / Icon | 18dp |  |  |
| md.comp.suggestion-chip.label-text.font | FONT_NAMES | Enabled / Label text | `md.sys.typescale.label-large.font` |  |  |
| md.comp.suggestion-chip.label-text.line-height | LINE_HEIGHT | Enabled / Label text | `md.sys.typescale.label-large.line-height` |  |  |
| md.comp.suggestion-chip.label-text.size | FONT_SIZE | Enabled / Label text | `md.sys.typescale.label-large.size` |  |  |
| md.comp.suggestion-chip.label-text.weight | FONT_WEIGHT | Enabled / Label text | `md.sys.typescale.label-large.weight` |  |  |
| md.comp.suggestion-chip.label-text.tracking | FONT_TRACKING | Enabled / Label text | `md.sys.typescale.label-large.tracking` |  |  |
| md.comp.suggestion-chip.label-text.type | TYPOGRAPHY | Enabled / Label text | font name: `md.comp.suggestion-chip.label-text.font`, font weight: `md.comp.suggestion-chip.label-text.weight`, font size: `md.comp.suggestion-chip.label-text.size`, font tracking: `md.comp.suggestion-chip.label-text.tracking`, line height: `md.comp.suggestion-chip.label-text.line-height` |  |  |
| md.comp.suggestion-chip.label-text.color | COLOR | Enabled / Label text | `md.sys.color.on-surface-variant` |  |  |
| md.comp.suggestion-chip.flat.disabled.outline.color | COLOR | Disabled / Container | `md.sys.color.on-surface` |  |  |
| md.comp.suggestion-chip.flat.disabled.outline.opacity | OPACITY | Disabled / Container | 0.12 |  |  |
| md.comp.suggestion-chip.elevated.disabled.container.elevation | ELEVATION | Disabled / Container | `md.sys.elevation.level0` |  |  |
| md.comp.suggestion-chip.elevated.disabled.container.color | COLOR | Disabled / Container | `md.sys.color.on-surface` |  |  |
| md.comp.suggestion-chip.elevated.disabled.container.opacity | OPACITY | Disabled / Container | 0.12 |  |  |
| md.comp.suggestion-chip.with-leading-icon.disabled.leading-icon.color | COLOR | Disabled / Icon | `md.sys.color.on-surface` |  |  |
| md.comp.suggestion-chip.with-leading-icon.disabled.leading-icon.opacity | OPACITY | Disabled / Icon | 0.38 |  |  |
| md.comp.suggestion-chip.disabled.label-text.color | COLOR | Disabled / Label text | `md.sys.color.on-surface` |  |  |
| md.comp.suggestion-chip.disabled.label-text.opacity | OPACITY | Disabled / Label text | 0.38 |  |  |
| md.comp.suggestion-chip.elevated.hover.container.elevation | ELEVATION | Hovered / Container | `md.sys.elevation.level2` |  |  |
| md.comp.suggestion-chip.with-leading-icon.hover.leading-icon.color | COLOR | Hovered / Icon | `md.sys.color.primary` |  |  |
| md.comp.suggestion-chip.hover.label-text.color | COLOR | Hovered / Label text | `md.sys.color.on-surface-variant` |  |  |
| md.comp.suggestion-chip.hover.state-layer.color | COLOR | Hovered / State layer | `md.sys.color.on-surface-variant` |  |  |
| md.comp.suggestion-chip.hover.state-layer.opacity | OPACITY | Hovered / State layer | `md.sys.state.hover.state-layer-opacity` |  |  |
| md.comp.suggestion-chip.focus.indicator.color | COLOR | Focused / Focus indicator | `md.sys.color.secondary` |  |  |
| md.comp.suggestion-chip.focus.indicator.thickness | LENGTH | Focused / Focus indicator | `md.sys.state.focus-indicator.thickness` |  |  |
| md.comp.suggestion-chip.focus.indicator.outline.offset | LENGTH | Focused / Focus indicator | `md.sys.state.focus-indicator.outer-offset` |  |  |
| md.comp.suggestion-chip.flat.focus.outline.color | COLOR | Focused / Container | `md.sys.color.on-surface-variant` |  |  |
| md.comp.suggestion-chip.elevated.focus.container.elevation | ELEVATION | Focused / Container | `md.sys.elevation.level1` |  |  |
| md.comp.suggestion-chip.with-leading-icon.focus.leading-icon.color | COLOR | Focused / Icon | `md.sys.color.primary` |  |  |
| md.comp.suggestion-chip.focus.label-text.color | COLOR | Focused / Label text | `md.sys.color.on-surface-variant` |  |  |
| md.comp.suggestion-chip.focus.state-layer.color | COLOR | Focused / State layer | `md.sys.color.on-surface-variant` |  |  |
| md.comp.suggestion-chip.focus.state-layer.opacity | OPACITY | Focused / State layer | `md.sys.state.focus.state-layer-opacity` |  |  |
| md.comp.suggestion-chip.elevated.pressed.container.elevation | ELEVATION | Pressed (ripple) / Container | `md.sys.elevation.level1` |  |  |
| md.comp.suggestion-chip.with-leading-icon.pressed.leading-icon.color | COLOR | Pressed (ripple) / Icon | `md.sys.color.primary` |  |  |
| md.comp.suggestion-chip.pressed.label-text.color | COLOR | Pressed (ripple) / Label text | `md.sys.color.on-surface-variant` |  |  |
| md.comp.suggestion-chip.pressed.state-layer.color | COLOR | Pressed (ripple) / State layer | `md.sys.color.on-surface-variant` |  |  |
| md.comp.suggestion-chip.pressed.state-layer.opacity | OPACITY | Pressed (ripple) / State layer | `md.sys.state.pressed.state-layer-opacity` |  |  |
| md.comp.suggestion-chip.dragged.container.elevation | ELEVATION | Dragged / Container | `md.sys.elevation.level4` |  |  |
| md.comp.suggestion-chip.with-leading-icon.dragged.leading-icon.color | COLOR | Dragged / Icon | `md.sys.color.primary` |  |  |
| md.comp.suggestion-chip.dragged.label-text.color | COLOR | Dragged / Label text | `md.sys.color.on-surface-variant` |  |  |
| md.comp.suggestion-chip.dragged.state-layer.color | COLOR | Dragged / State layer | `md.sys.color.on-surface-variant` |  |  |
| md.comp.suggestion-chip.dragged.state-layer.opacity | OPACITY | Dragged / State layer | `md.sys.state.dragged.state-layer-opacity` |  |  |

## Assist chip

![Assist chip diagram numbering 3 elements.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fm01dv1nz-1.png?alt=media&token=8b66182d-b209-4110-a780-7c2fea9f89f8>)

_Container; Label text; Leading icon_

### Assist chip color

Color values are implemented through design tokens. For design, this means working with color values that correspond with tokens. For implementation, a color value will be a token that references a value. [Learn more about design tokens](../../foundations/design-tokens/overview.md)

![Assist chip diagram numbering 4 color elements.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fm01dv6ou-2.png?alt=media&token=71af17c7-755b-4e82-8783-3d14e68afb74>)

_Assist chip color roles used for light and dark themes: Surface container low (optional); On surface; Outline; Primary_

### Assist chip states

States are visual representations used to communicate the status of a component or interactive element. [Learn more about interaction states](https://m3.material.io/m3/pages/interaction-states/overview)

![36 assist chips illustrating combinations of styles, selection and non-selection, and 6 interaction states.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fm8sg1d2r-03-3P.png?alt=media&token=603c0499-acc9-4157-bebc-d11d56548e5e>)

_Selected and unselected assist chip states: Enabled; Disabled; Hovered; Focused; Pressed; Dragged_

### Assist chip measurements

![3 assist chips with measurements shown for variants with and without a leading icon.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fm01dvj22-4.png?alt=media&token=6d55f422-809c-4299-b51f-a92f45987516>)

_Assist chip padding and size measurements_

| Attribute | Value |
| --- | --- |
| Height | 32dp |
| Shape | 8dp corner radius |
| Icon size | 18dp |
| Vertical label text alignment | Center-aligned |
| Horizontal label text alignment | Start-aligned |
| Left/right padding | 16dp |
| Left/right padding with icon | 8dp |
| Padding between elements | 8dp |

## Filter chip

![Filter chip diagram numbering 4 elements.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fm01dvpzm-5.png?alt=media&token=6302536e-9843-48e9-acf8-8f25e4616e14>)

_Container; Label text; Leading icon; Trailing icon_

### Filter chip color

Color values are implemented through design tokens. For design, this means working with color values that correspond with tokens. For implementation, a color value will be a token that references a value. [Learn more about design tokens](../../foundations/design-tokens/overview.md)

![Filter chip diagram numbering 4 color elements.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmmp8q5ab-06-3P.png?alt=media&token=cb486b8e-952e-4d83-b7b7-f549bc909c33>)

_Filter chip color roles used for light and dark themes: On surface variant; On secondary container; Secondary container; Outline variant; Surface container low (optional)_

### Filter chip states

States are visual representations used to communicate the status of a component or interactive element. [Learn more about interaction states](https://m3.material.io/m3/pages/interaction-states/overview)

![24 filter chips showing combinations of elevated, non-elevated, selected, and non-selected styles, and 6 interaction states.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fm8sg2d2t-07-3P.png?alt=media&token=33f4b292-c821-4621-8d79-e208cf3d41ca>)

_Selected and unselected filter chip states: Enabled; Disabled; Hovered; Focused; Pressed; Dragged_

### Filter chip measurements

![3 filter chips with measurements shown for types with and without a leading icon and trailing icon.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fm01dwqif-8.png?alt=media&token=4fcc217b-122a-442a-b135-a812be0e2752>)

_Filter chip padding and size measurements_

| Attribute | Value |
| --- | --- |
| Container height | 32dp |
| Container shape | 8dp corner radius |
| Icon size | 18dp |
| Vertical label text alignment | Center-aligned |
| Horizontal label text alignment | Start-aligned |
| Left/right padding | 16dp |
| Left/right padding with icon | 8dp |
| Padding between elements | 8dp |

## Input chip

![Input chip diagram numbering 4 elements.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fm01dwxct-9.png?alt=media&token=f502e45b-c29b-4e36-b02a-19f61bf2cf1d>)

_Container; Label text; Trailing icon; Leading icon_

### Input chip color

Color values are implemented through design tokens. For design, this means working with color values that correspond with tokens. For implementation, a color value will be a token that references a value. [Learn more about design tokens](../../foundations/design-tokens/overview.md)

![Input chip diagram numbering 5 color elements.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fm02q3wit-10-3p.png?alt=media&token=2930e234-d1bc-45d6-a0b7-5030aa65e856>)

_Input chip color roles used for light and dark themes: On surface variant; Surface container low (optional); On surface variant; On surface variant; Outline variant; Primary; Secondary container; On secondary container; On secondary container_

### Input chip states

States are visual representations used to communicate the status of a component or interactive element. [Learn more about interaction states](https://m3.material.io/m3/pages/interaction-states/overview)

![33 input chips illustrating combinations of styles, selection and non-selection, and 6 interaction states.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fm8sg4m8e-11-3P.png?alt=media&token=f0519dc3-5dd1-4c3d-af7d-941f77fa286e>)

_Selected and unselected input chip states: Enabled; Disabled; Hovered; Focused; Pressed; Dragged_

### Input chip measurements

![2 input chips with measurements: 1 with a trailing icon only; 1 with an avatar as a leading icon and a trailing icon.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fm34ufke8-12-3p.png?alt=media&token=accd17a3-9af8-478e-b57b-7ba29d34a792>)

_Input chip padding and size measurements_

| Attribute | Value |
| --- | --- |
| Container height | 32dp |
| Container shape | 8dp corner radius |
| Icon size | 18dp |
| Avatar shape | 12dp corner radius |
| Avatar size | 24dp |
| Vertical label text alignment | Center-aligned |
| Horizontal label text alignment | Start-aligned |
| Left padding for avatar | 4dp |
| Right padding for avatar | 8dp |
| Left/right padding for icon | 8dp |
| Padding between elements | 8dp |
| Target size for close icon | Min 48dp |

## Suggestion chip

![Suggestion chip diagram numbering 2 elements.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fm01dxq8p-13.png?alt=media&token=5f745c90-2770-48d3-a0bb-ccd1215ec1eb>)

_Container; Label text_

### Suggestion chip color

Color values are implemented through design tokens. For design, this means working with color values that correspond with tokens. For implementation, a color value will be a token that references a value. [Learn more about design tokens](../../foundations/design-tokens/overview.md)

![Suggestion chip diagram numbering 3 color elements.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fm01dxxwr-14.png?alt=media&token=8f51719e-0531-4d6b-80a5-f7d44e6a84ae>)

_Suggestion chip color roles used for light and dark themes: Outline; Surface container low (optional); On surface variant_

### Suggestion chip states

States are visual representations used to communicate the status of a component or interactive element. [Learn more about interaction states](https://m3.material.io/m3/pages/interaction-states/overview)

![24 suggestion chips illustrating combinations of styles across 6 interaction states.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fm8sg5r6a-15-3P.png?alt=media&token=990c5a71-7228-4c18-a655-2d23d4528be2>)

_Selected and unselected suggestion chip states: Enabled; Disabled; Hovered; Focused; Pressed; Dragged_

### Suggestion chip measurements

![2 suggestion chips with measurements shown for variants with and without a leading icon.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fm01dybi0-16.png?alt=media&token=ed6c212f-362c-48c5-808e-8959ad55a5e3>)

_Suggestion chip padding and size measurements_

| Attribute | Value |
| --- | --- |
| Container height | 32dp |
| Container shape | 8dp corner radius |
| Icon size | 18dp |
| Vertical label text alignment | Center-aligned |
| Horizontal label text alignment | Start-aligned |
| Left/right padding without icon | 16dp |
| Left/right padding with icon | 8dp |
| Padding between elements | 8dp |

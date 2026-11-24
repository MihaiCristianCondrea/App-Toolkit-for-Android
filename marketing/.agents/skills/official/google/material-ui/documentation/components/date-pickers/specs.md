---
title: "Date pickers"
source_url: "https://m3.material.io/components/date-pickers/specs"
lastmod: "2026-05-18"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "components"
assets:
  - "assets/images/2106679f8d6e-me6vnbm5-01-3P.png"
  - "assets/images/fb4f93d04219-me6vo1av-02-3P.png"
  - "assets/images/a2750f27872f-me6vofa8-03-3P.png"
  - "assets/images/909b114ba230-me6vovcl-04-3P.png"
  - "assets/images/103eaca22971-mel11ou5-05-3P.png"
  - "assets/images/6ec427cb9670-mel12gcl-06-3P.png"
  - "assets/images/5a0ff6eb3236-me6vrc28-07-3P.png"
  - "assets/images/3c606d84cf7a-me6vsstf-08-3P.png"
  - "assets/images/dceaff480596-me6vtc2f-09-3P.png"
  - "assets/images/9202925dd008-me6vtxo6-10-3P.png"
  - "assets/images/38096f358f14-me6vuima-11-3P.png"
  - "assets/images/6ceec6c9c11b-me6vuwh7-12-3P.png"
  - "assets/images/3b4101cf282b-me6vv9ft-13-3P.png"
  - "assets/images/bc8368f1bd1e-mel16bx5-14-3P.png"
  - "assets/images/d991e038f36b-mel16puv-15-3P.png"
  - "assets/images/09f6a1e53604-mel175wp-16-3P.png"
  - "assets/images/c7d97a96bba0-me6vxg0z-17-3P.png"
  - "assets/images/0d599cb7db1a-me6vxz7r-18-3P.png"
  - "assets/images/f2bef903d3e5-me6vyfrl-19-3P.png"
  - "assets/images/f53597821e6c-me6vyzk0-20-3P.png"
  - "assets/images/49a20db468af-me6vzozd-21-3P.png"
  - "assets/images/b6a8383312e6-me6w02sb-22b-3P.png"
---

# Date pickers

Date pickers let people select a date, or a range of dates

## Specs

## Tokens & specs

Select a component variant below to see its elements, attributes, tokens, and their values.

### TOKEN_TABLE

Component: Date pickers

Configured context tags: None, Medium contrast, Default, High contrast, Static, 3P, Android, Dark, Light

#### Token sets

| Token set | Name | Type | Tokens | Description |
| --- | --- | --- | --- | --- |
| Date picker - Docked | md.comp.date-picker.docked | COMPONENT | 96 | For docked date pickers. |
| Date picker - Modal | md.comp.date-picker.modal | COMPONENT | 111 | For modal calendar date pickers. |
| Date picker - Modal input | md.comp.date-input.modal | COMPONENT | 22 | For modal input date pickers. |

#### Date picker - Docked (md.comp.date-picker.docked)

For docked date pickers.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.date-picker.docked.container.color | COLOR | Enabled / Container | `md.sys.color.surface-container-high` |  |  |
| md.comp.date-picker.docked.container.elevation | ELEVATION | Enabled / Container | `md.sys.elevation.level3` |  |  |
| md.comp.date-picker.docked.container.surface-tint-layer.color | COLOR | Enabled / Container | `md.sys.color.surface-tint` |  | Deprecated as part of the update from opacity based surfaces to tonal surfaces. Surfaces no longer use surface-tint layers for tinting, please use the desired surface role directly as the container color. |
| md.comp.date-picker.docked.container.shape | SHAPE | Enabled / Container | `md.sys.shape.corner.large` |  |  |
| md.comp.date-picker.docked.date.container.shape | SHAPE | Enabled / Container | `md.sys.shape.corner.full` |  |  |
| md.comp.date-picker.docked.date.today.container.outline.color | COLOR | Enabled / Container | `md.sys.color.primary` |  |  |
| md.comp.date-picker.docked.date.today.container.outline.width | LENGTH | Enabled / Container | 1dp |  |  |
| md.comp.date-picker.docked.date.selected.container.color | COLOR | Enabled / Container | `md.sys.color.primary` |  |  |
| md.comp.date-picker.docked.menu-button.container.shape | SHAPE | Enabled / Container | `md.sys.shape.corner.full` |  |  |
| md.comp.date-picker.docked.menu.list-item.selected.container.color | COLOR | Enabled / Container | `md.sys.color.surface-variant` |  |  |
| md.comp.date-picker.docked.container.width | LENGTH | Enabled / Container | 360dp |  |  |
| md.comp.date-picker.docked.container.height | LENGTH | Enabled / Container | 456dp |  |  |
| md.comp.date-picker.docked.date.container.width | LENGTH | Enabled / Container | 48dp |  |  |
| md.comp.date-picker.docked.date.container.height | LENGTH | Enabled / Container | 48dp |  |  |
| md.comp.date-picker.docked.menu-button.container.height | LENGTH | Enabled / Container | 40dp |  |  |
| md.comp.date-picker.docked.menu.list-item.container.height | LENGTH | Enabled / Container | 48dp |  |  |
| md.comp.date-picker.docked.weekdays.label-text.color | COLOR | Enabled / Label text | `md.sys.color.on-surface` |  |  |
| md.comp.date-picker.docked.weekdays.label-text.font | FONT_NAMES | Enabled / Label text | `md.sys.typescale.body-large.font` |  |  |
| md.comp.date-picker.docked.weekdays.label-text.line-height | LINE_HEIGHT | Enabled / Label text | `md.sys.typescale.body-large.line-height` |  |  |
| md.comp.date-picker.docked.weekdays.label-text.size | FONT_SIZE | Enabled / Label text | `md.sys.typescale.body-large.size` |  |  |
| md.comp.date-picker.docked.weekdays.label-text.weight | FONT_WEIGHT | Enabled / Label text | `md.sys.typescale.body-large.weight` |  |  |
| md.comp.date-picker.docked.weekdays.label-text.tracking | FONT_TRACKING | Enabled / Label text | `md.sys.typescale.body-large.tracking` |  |  |
| md.comp.date-picker.docked.weekdays.label-text.type | TYPOGRAPHY | Enabled / Label text | font name: `md.comp.date-picker.docked.weekdays.label-text.font`, font weight: `md.comp.date-picker.docked.weekdays.label-text.weight`, font size: `md.comp.date-picker.docked.weekdays.label-text.size`, font tracking: `md.comp.date-picker.docked.weekdays.label-text.tracking`, line height: `md.comp.date-picker.docked.weekdays.label-text.line-height` |  |  |
| md.comp.date-picker.docked.date.label-text.font | FONT_NAMES | Enabled / Label text | `md.sys.typescale.body-large.font` |  |  |
| md.comp.date-picker.docked.date.label-text.line-height | LINE_HEIGHT | Enabled / Label text | `md.sys.typescale.body-large.line-height` |  |  |
| md.comp.date-picker.docked.date.label-text.size | FONT_SIZE | Enabled / Label text | `md.sys.typescale.body-large.size` |  |  |
| md.comp.date-picker.docked.date.label-text.weight | FONT_WEIGHT | Enabled / Label text | `md.sys.typescale.body-large.weight` |  |  |
| md.comp.date-picker.docked.date.label-text.tracking | FONT_TRACKING | Enabled / Label text | `md.sys.typescale.body-large.tracking` |  |  |
| md.comp.date-picker.docked.date.label-text.type | TYPOGRAPHY | Enabled / Label text | font name: `md.comp.date-picker.docked.date.label-text.font`, font weight: `md.comp.date-picker.docked.date.label-text.weight`, font size: `md.comp.date-picker.docked.date.label-text.size`, font tracking: `md.comp.date-picker.docked.date.label-text.tracking`, line height: `md.comp.date-picker.docked.date.label-text.line-height` |  |  |
| md.comp.date-picker.docked.date.unselected.label-text.color | COLOR | Enabled / Label text | `md.sys.color.on-surface` |  |  |
| md.comp.date-picker.docked.date.unselected.outside-month.label-text.color | COLOR | Enabled / Label text | `md.sys.color.on-surface` |  |  |
| md.comp.date-picker.docked.date.unselected.outside-month.label-text.opacity | OPACITY | Enabled / Label text | 0.38 |  |  |
| md.comp.date-picker.docked.date.today.label-text.color | COLOR | Enabled / Label text | `md.sys.color.primary` |  |  |
| md.comp.date-picker.docked.date.selected.label-text.color | COLOR | Enabled / Label text | `md.sys.color.on-primary` |  |  |
| md.comp.date-picker.docked.menu-button.label-text.color | COLOR | Enabled / Label text | `md.sys.color.on-surface-variant` |  |  |
| md.comp.date-picker.docked.menu-button.label-text.font | FONT_NAMES | Enabled / Label text | `md.sys.typescale.label-large.font` |  |  |
| md.comp.date-picker.docked.menu-button.label-text.line-height | LINE_HEIGHT | Enabled / Label text | `md.sys.typescale.label-large.line-height` |  |  |
| md.comp.date-picker.docked.menu-button.label-text.size | FONT_SIZE | Enabled / Label text | `md.sys.typescale.label-large.size` |  |  |
| md.comp.date-picker.docked.menu-button.label-text.weight | FONT_WEIGHT | Enabled / Label text | `md.sys.typescale.label-large.weight` |  |  |
| md.comp.date-picker.docked.menu-button.label-text.tracking | FONT_TRACKING | Enabled / Label text | `md.sys.typescale.label-large.tracking` |  |  |
| md.comp.date-picker.docked.menu-button.label-text.type | TYPOGRAPHY | Enabled / Label text | font name: `md.comp.date-picker.docked.menu-button.label-text.font`, font weight: `md.comp.date-picker.docked.menu-button.label-text.weight`, font size: `md.comp.date-picker.docked.menu-button.label-text.size`, font tracking: `md.comp.date-picker.docked.menu-button.label-text.tracking`, line height: `md.comp.date-picker.docked.menu-button.label-text.line-height` |  |  |
| md.comp.date-picker.docked.menu.list-item.label-text.color | COLOR | Enabled / Label text | `md.sys.color.on-surface` |  |  |
| md.comp.date-picker.docked.menu.list-item.label-text.font | FONT_NAMES | Enabled / Label text | `md.sys.typescale.body-large.font` |  |  |
| md.comp.date-picker.docked.menu.list-item.label-text.line-height | LINE_HEIGHT | Enabled / Label text | `md.sys.typescale.body-large.line-height` |  |  |
| md.comp.date-picker.docked.menu.list-item.label-text.size | FONT_SIZE | Enabled / Label text | `md.sys.typescale.body-large.size` |  |  |
| md.comp.date-picker.docked.menu.list-item.label-text.tracking | FONT_TRACKING | Enabled / Label text | `md.sys.typescale.body-large.tracking` |  |  |
| md.comp.date-picker.docked.menu.list-item.label-text.weight | FONT_WEIGHT | Enabled / Label text | `md.sys.typescale.body-large.weight` |  |  |
| md.comp.date-picker.docked.menu.list-item.label-text.type | TYPOGRAPHY | Enabled / Label text | font name: `md.comp.date-picker.docked.menu.list-item.label-text.font`, font weight: `md.comp.date-picker.docked.menu.list-item.label-text.weight`, font size: `md.comp.date-picker.docked.menu.list-item.label-text.size`, font tracking: `md.comp.date-picker.docked.menu.list-item.label-text.tracking`, line height: `md.comp.date-picker.docked.menu.list-item.label-text.line-height` |  |  |
| md.comp.date-picker.docked.menu-button.icon.color | COLOR | Enabled / Icon | `md.sys.color.on-surface-variant` |  |  |
| md.comp.date-picker.docked.menu-button.icon.size | LENGTH | Enabled / Icon | 18dp |  |  |
| md.comp.date-picker.docked.menu.list-item.selected.leading-icon.color | COLOR | Enabled / Leading icon | `md.sys.color.on-surface` |  |  |
| md.comp.date-picker.docked.menu.list-item.selected.leading-icon.size | LENGTH | Enabled / Leading icon | 24dp |  |  |
| md.comp.date-picker.docked.date.state-layer.shape | SHAPE | Enabled / State layer | `md.sys.shape.corner.full` |  |  |
| md.comp.date-picker.docked.date.state-layer.width | LENGTH | Enabled / State layer | 40dp |  |  |
| md.comp.date-picker.docked.date.state-layer.height | LENGTH | Enabled / State layer | 40dp |  |  |
| md.comp.date-picker.docked.header.height | LENGTH | Enabled / Header | 64dp |  |  |
| md.comp.date-picker.docked.menu-button.disabled.label-text.color | COLOR | Disabled / Label text | `md.sys.color.on-surface` |  |  |
| md.comp.date-picker.docked.menu-button.disabled.label-text.opacity | OPACITY | Disabled / Label text | 0.38 |  |  |
| md.comp.date-picker.docked.menu-button.disabled.icon.color | COLOR | Disabled / Icon | `md.sys.color.on-surface` |  |  |
| md.comp.date-picker.docked.menu-button.disabled.icon.opacity | OPACITY | Disabled / Icon | 0.38 |  |  |
| md.comp.date-picker.docked.menu-button.hover.label-text.color | COLOR | Hovered / Label text | `md.sys.color.on-surface-variant` |  |  |
| md.comp.date-picker.docked.menu.list-item.hover.label-text.color | COLOR | Hovered / Label text | `md.sys.color.on-surface` |  |  |
| md.comp.date-picker.docked.date.unselected.hover.state-layer.color | COLOR | Hovered / State layer | `md.sys.color.on-surface-variant` |  |  |
| md.comp.date-picker.docked.date.selected.hover.state-layer.color | COLOR | Hovered / State layer | `md.sys.color.on-primary` |  |  |
| md.comp.date-picker.docked.date.today.hover.state-layer.color | COLOR | Hovered / State layer | `md.sys.color.primary` |  |  |
| md.comp.date-picker.docked.date.hover.state-layer.opacity | OPACITY | Hovered / State layer | `md.sys.state.hover.state-layer-opacity` |  |  |
| md.comp.date-picker.docked.menu-button.hover.state-layer.color | COLOR | Hovered / State layer | `md.sys.color.on-surface-variant` |  |  |
| md.comp.date-picker.docked.menu-button.hover.state-layer.opacity | OPACITY | Hovered / State layer | `md.sys.state.hover.state-layer-opacity` |  |  |
| md.comp.date-picker.docked.menu.list-item.hover.state-layer.color | COLOR | Hovered / State layer | `md.sys.color.on-surface` |  |  |
| md.comp.date-picker.docked.menu.list-item.hover.state-layer.opacity | OPACITY | Hovered / State layer | `md.sys.state.hover.state-layer-opacity` |  |  |
| md.comp.date-picker.docked.menu-button.hover.icon.color | COLOR | Hovered / Icon | `md.sys.color.on-surface-variant` |  |  |
| md.comp.date-picker.docked.menu.list-item.selected.hover.leading-icon.color | COLOR | Hovered / Leading icon | `md.sys.color.on-surface-variant` |  |  |
| md.comp.date-picker.docked.menu-button.focus.label-text.color | COLOR | Focused / Label text | `md.sys.color.on-surface-variant` |  |  |
| md.comp.date-picker.docked.menu.list-item.focus.label-text.color | COLOR | Focused / Label text | `md.sys.color.on-surface` |  |  |
| md.comp.date-picker.docked.date.unselected.focus.state-layer.color | COLOR | Focused / State layer | `md.sys.color.on-surface-variant` |  |  |
| md.comp.date-picker.docked.date.selected.focus.state-layer.color | COLOR | Focused / State layer | `md.sys.color.on-primary` |  |  |
| md.comp.date-picker.docked.date.today.focus.state-layer.color | COLOR | Focused / State layer | `md.sys.color.primary` |  |  |
| md.comp.date-picker.docked.date.focus.state-layer.opacity | OPACITY | Focused / State layer | `md.sys.state.focus.state-layer-opacity` |  |  |
| md.comp.date-picker.docked.menu-button.focus.state-layer.color | COLOR | Focused / State layer | `md.sys.color.on-surface-variant` |  |  |
| md.comp.date-picker.docked.menu-button.focus.state-layer.opacity | OPACITY | Focused / State layer | `md.sys.state.focus.state-layer-opacity` |  |  |
| md.comp.date-picker.docked.menu.list-item.focus.state-layer.color | COLOR | Focused / State layer | `md.sys.color.on-surface` |  |  |
| md.comp.date-picker.docked.menu.list-item.focus.state-layer.opacity | OPACITY | Focused / State layer | `md.sys.state.focus.state-layer-opacity` |  |  |
| md.comp.date-picker.docked.menu-button.focus.icon.color | COLOR | Focused / Icon | `md.sys.color.on-surface-variant` |  |  |
| md.comp.date-picker.docked.menu.list-item.selected.focus.leading-icon.color | COLOR | Focused / Leading icon | `md.sys.color.on-surface-variant` |  |  |
| md.comp.date-picker.docked.menu-button.pressed.label-text.color | COLOR | Pressed (ripple) / Label text | `md.sys.color.on-surface-variant` |  |  |
| md.comp.date-picker.docked.menu.list-item.pressed.label-text.color | COLOR | Pressed (ripple) / Label text | `md.sys.color.on-surface` |  |  |
| md.comp.date-picker.docked.date.unselected.pressed.state-layer.color | COLOR | Pressed (ripple) / State layer | `md.sys.color.on-surface-variant` |  |  |
| md.comp.date-picker.docked.date.selected.pressed.state-layer.color | COLOR | Pressed (ripple) / State layer | `md.sys.color.on-primary` |  |  |
| md.comp.date-picker.docked.date.today.pressed.state-layer.color | COLOR | Pressed (ripple) / State layer | `md.sys.color.primary` |  |  |
| md.comp.date-picker.docked.date.pressed.state-layer.opacity | OPACITY | Pressed (ripple) / State layer | `md.sys.state.pressed.state-layer-opacity` |  |  |
| md.comp.date-picker.docked.menu-button.pressed.state-layer.color | COLOR | Pressed (ripple) / State layer | `md.sys.color.on-surface-variant` |  |  |
| md.comp.date-picker.docked.menu-button.pressed.state-layer.opacity | OPACITY | Pressed (ripple) / State layer | `md.sys.state.pressed.state-layer-opacity` |  |  |
| md.comp.date-picker.docked.menu.list-item.pressed.state-layer.color | COLOR | Pressed (ripple) / State layer | `md.sys.color.on-surface` |  |  |
| md.comp.date-picker.docked.menu.list-item.pressed.state-layer.opacity | OPACITY | Pressed (ripple) / State layer | `md.sys.state.pressed.state-layer-opacity` |  |  |
| md.comp.date-picker.docked.menu-button.pressed.icon.color | COLOR | Pressed (ripple) / Icon | `md.sys.color.on-surface-variant` |  |  |
| md.comp.date-picker.docked.menu.list-item.selected.pressed.leading-icon.color | COLOR | Pressed (ripple) / Leading icon | `md.sys.color.on-surface-variant` |  |  |

#### Date picker - Modal (md.comp.date-picker.modal)

For modal calendar date pickers.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.date-picker.modal.container.color | COLOR | Enabled / Container | `md.sys.color.surface-container-high` |  |  |
| md.comp.date-picker.modal.container.elevation | ELEVATION | Enabled / Container | `md.sys.elevation.level3` |  |  |
| md.comp.date-picker.modal.container.surface-tint-layer.color | COLOR | Enabled / Container | `md.sys.color.surface-tint` |  | Deprecated as part of the update from opacity based surfaces to tonal surfaces. Surfaces no longer use surface-tint layers for tinting, please use the desired surface role directly as the container color. |
| md.comp.date-picker.modal.container.shape | SHAPE | Enabled / Container | `md.sys.shape.corner.extra-large` |  |  |
| md.comp.date-picker.modal.range-selection.container.elevation | ELEVATION | Enabled / Container | `md.sys.elevation.level0` |  |  |
| md.comp.date-picker.modal.range-selection.container.shape | SHAPE | Enabled / Container | `md.sys.shape.corner.none` |  |  |
| md.comp.date-picker.modal.date.container.shape | SHAPE | Enabled / Container | `md.sys.shape.corner.full` |  |  |
| md.comp.date-picker.modal.date.today.container.outline.color | COLOR | Enabled / Container | `md.sys.color.primary` |  |  |
| md.comp.date-picker.modal.date.today.container.outline.width | LENGTH | Enabled / Container | 1dp |  |  |
| md.comp.date-picker.modal.date.selected.container.color | COLOR | Enabled / Container | `md.sys.color.primary` |  |  |
| md.comp.date-picker.modal.range-selection.active-indicator.container.color | COLOR | Enabled / Container | `md.sys.color.secondary-container` |  |  |
| md.comp.date-picker.modal.range-selection.active-indicator.container.shape | SHAPE | Enabled / Container | `md.sys.shape.corner.full` |  |  |
| md.comp.date-picker.modal.year-selection.year.selected.container.color | COLOR | Enabled / Container | `md.sys.color.primary` |  |  |
| md.comp.date-picker.modal.container.width | LENGTH | Enabled / Container | 360dp |  |  |
| md.comp.date-picker.modal.container.height | LENGTH | Enabled / Container | 524dp |  |  |
| md.comp.date-picker.modal.header.container.width | LENGTH | Enabled / Container | 360dp |  |  |
| md.comp.date-picker.modal.header.container.height | LENGTH | Enabled / Container | 120dp |  |  |
| md.comp.date-picker.modal.range-selection.header.container.height | LENGTH | Enabled / Container | 128dp |  |  |
| md.comp.date-picker.modal.date.container.width | LENGTH | Enabled / Container | 40dp |  |  |
| md.comp.date-picker.modal.date.container.height | LENGTH | Enabled / Container | 40dp |  |  |
| md.comp.date-picker.modal.year-selection.year.container.width | LENGTH | Enabled / Container | 72dp |  |  |
| md.comp.date-picker.modal.year-selection.year.container.height | LENGTH | Enabled / Container | 36dp |  |  |
| md.comp.date-picker.modal.range-selection.active-indicator.container.height | LENGTH | Enabled / Container | 40dp |  |  |
| md.comp.date-picker.modal.weekdays.label-text.color | COLOR | Enabled / Label text | `md.sys.color.on-surface` |  |  |
| md.comp.date-picker.modal.weekdays.label-text.font | FONT_NAMES | Enabled / Label text | `md.sys.typescale.body-large.font` |  |  |
| md.comp.date-picker.modal.weekdays.label-text.line-height | LINE_HEIGHT | Enabled / Label text | `md.sys.typescale.body-large.line-height` |  |  |
| md.comp.date-picker.modal.weekdays.label-text.size | FONT_SIZE | Enabled / Label text | `md.sys.typescale.body-large.size` |  |  |
| md.comp.date-picker.modal.weekdays.label-text.weight | FONT_WEIGHT | Enabled / Label text | `md.sys.typescale.body-large.weight` |  |  |
| md.comp.date-picker.modal.weekdays.label-text.tracking | FONT_TRACKING | Enabled / Label text | `md.sys.typescale.body-large.tracking` |  |  |
| md.comp.date-picker.modal.weekdays.label-text.type | TYPOGRAPHY | Enabled / Label text | font name: `md.comp.date-picker.modal.weekdays.label-text.font`, font weight: `md.comp.date-picker.modal.weekdays.label-text.weight`, font size: `md.comp.date-picker.modal.weekdays.label-text.size`, font tracking: `md.comp.date-picker.modal.weekdays.label-text.tracking`, line height: `md.comp.date-picker.modal.weekdays.label-text.line-height` |  |  |
| md.comp.date-picker.modal.date.label-text.font | FONT_NAMES | Enabled / Label text | `md.sys.typescale.body-large.font` |  |  |
| md.comp.date-picker.modal.date.label-text.line-height | LINE_HEIGHT | Enabled / Label text | `md.sys.typescale.body-large.line-height` |  |  |
| md.comp.date-picker.modal.date.label-text.size | FONT_SIZE | Enabled / Label text | `md.sys.typescale.body-large.size` |  |  |
| md.comp.date-picker.modal.date.label-text.weight | FONT_WEIGHT | Enabled / Label text | `md.sys.typescale.body-large.weight` |  |  |
| md.comp.date-picker.modal.date.label-text.tracking | FONT_TRACKING | Enabled / Label text | `md.sys.typescale.body-large.tracking` |  |  |
| md.comp.date-picker.modal.date.label-text.type | TYPOGRAPHY | Enabled / Label text | font name: `md.comp.date-picker.modal.date.label-text.font`, font weight: `md.comp.date-picker.modal.date.label-text.weight`, font size: `md.comp.date-picker.modal.date.label-text.size`, font tracking: `md.comp.date-picker.modal.date.label-text.tracking`, line height: `md.comp.date-picker.modal.date.label-text.line-height` |  |  |
| md.comp.date-picker.modal.date.unselected.label-text.color | COLOR | Enabled / Label text | `md.sys.color.on-surface` |  |  |
| md.comp.date-picker.modal.date.today.label-text.color | COLOR | Enabled / Label text | `md.sys.color.primary` |  |  |
| md.comp.date-picker.modal.date.selected.label-text.color | COLOR | Enabled / Label text | `md.sys.color.on-primary` |  |  |
| md.comp.date-picker.modal.range-selection.date.in-range.label-text.color | COLOR | Enabled / Label text | `md.sys.color.on-secondary-container` |  |  |
| md.comp.date-picker.modal.year-selection.year.label-text.font | FONT_NAMES | Enabled / Label text | `md.sys.typescale.body-large.font` |  |  |
| md.comp.date-picker.modal.year-selection.year.label-text.line-height | LINE_HEIGHT | Enabled / Label text | `md.sys.typescale.body-large.line-height` |  |  |
| md.comp.date-picker.modal.year-selection.year.label-text.size | FONT_SIZE | Enabled / Label text | `md.sys.typescale.body-large.size` |  |  |
| md.comp.date-picker.modal.year-selection.year.label-text.weight | FONT_WEIGHT | Enabled / Label text | `md.sys.typescale.body-large.weight` |  |  |
| md.comp.date-picker.modal.year-selection.year.label-text.tracking | FONT_TRACKING | Enabled / Label text | `md.sys.typescale.body-large.tracking` |  |  |
| md.comp.date-picker.modal.year-selection.year.label-text.type | TYPOGRAPHY | Enabled / Label text | font name: `md.comp.date-picker.modal.year-selection.year.label-text.font`, font weight: `md.comp.date-picker.modal.year-selection.year.label-text.weight`, font size: `md.comp.date-picker.modal.year-selection.year.label-text.size`, font tracking: `md.comp.date-picker.modal.year-selection.year.label-text.tracking`, line height: `md.comp.date-picker.modal.year-selection.year.label-text.line-height` |  |  |
| md.comp.date-picker.modal.year-selection.year.selected.label-text.color | COLOR | Enabled / Label text | `md.sys.color.on-primary` |  |  |
| md.comp.date-picker.modal.year-selection.year.unselected.label-text.color | COLOR | Enabled / Label text | `md.sys.color.on-surface-variant` |  |  |
| md.comp.date-picker.modal.range-selection.month.subhead.color | COLOR | Enabled / Subhead | `md.sys.color.on-surface-variant` |  |  |
| md.comp.date-picker.modal.range-selection.month.subhead.font | FONT_NAMES | Enabled / Subhead | `md.sys.typescale.title-small.font` |  |  |
| md.comp.date-picker.modal.range-selection.month.subhead.line-height | LINE_HEIGHT | Enabled / Subhead | `md.sys.typescale.title-small.line-height` |  |  |
| md.comp.date-picker.modal.range-selection.month.subhead.size | FONT_SIZE | Enabled / Subhead | `md.sys.typescale.title-small.size` |  |  |
| md.comp.date-picker.modal.range-selection.month.subhead.weight | FONT_WEIGHT | Enabled / Subhead | `md.sys.typescale.title-small.weight` |  |  |
| md.comp.date-picker.modal.range-selection.month.subhead.tracking | FONT_TRACKING | Enabled / Subhead | `md.sys.typescale.title-small.tracking` |  |  |
| md.comp.date-picker.modal.range-selection.month.subhead.type | TYPOGRAPHY | Enabled / Subhead | font name: `md.comp.date-picker.modal.range-selection.month.subhead.font`, font weight: `md.comp.date-picker.modal.range-selection.month.subhead.weight`, font size: `md.comp.date-picker.modal.range-selection.month.subhead.size`, font tracking: `md.comp.date-picker.modal.range-selection.month.subhead.tracking`, line height: `md.comp.date-picker.modal.range-selection.month.subhead.line-height` |  |  |
| md.comp.date-picker.modal.header.headline.color | COLOR | Enabled / Headline | `md.sys.color.on-surface-variant` |  |  |
| md.comp.date-picker.modal.header.headline.font | FONT_NAMES | Enabled / Headline | `md.sys.typescale.headline-large.font` |  |  |
| md.comp.date-picker.modal.header.headline.line-height | LINE_HEIGHT | Enabled / Headline | `md.sys.typescale.headline-large.line-height` |  |  |
| md.comp.date-picker.modal.header.headline.size | FONT_SIZE | Enabled / Headline | `md.sys.typescale.headline-large.size` |  |  |
| md.comp.date-picker.modal.header.headline.weight | FONT_WEIGHT | Enabled / Headline | `md.sys.typescale.headline-large.weight` |  |  |
| md.comp.date-picker.modal.header.headline.tracking | FONT_TRACKING | Enabled / Headline | `md.sys.typescale.headline-large.tracking` |  |  |
| md.comp.date-picker.modal.header.headline.type | TYPOGRAPHY | Enabled / Headline | font name: `md.comp.date-picker.modal.header.headline.font`, font weight: `md.comp.date-picker.modal.header.headline.weight`, font size: `md.comp.date-picker.modal.header.headline.size`, font tracking: `md.comp.date-picker.modal.header.headline.tracking`, line height: `md.comp.date-picker.modal.header.headline.line-height` |  |  |
| md.comp.date-picker.modal.range-selection.header.headline.font | FONT_NAMES | Enabled / Headline | `md.sys.typescale.title-large.font` |  |  |
| md.comp.date-picker.modal.range-selection.header.headline.line-height | LINE_HEIGHT | Enabled / Headline | `md.sys.typescale.title-large.line-height` |  |  |
| md.comp.date-picker.modal.range-selection.header.headline.size | FONT_SIZE | Enabled / Headline | `md.sys.typescale.title-large.size` |  |  |
| md.comp.date-picker.modal.range-selection.header.headline.weight | FONT_WEIGHT | Enabled / Headline | `md.sys.typescale.title-large.weight` |  |  |
| md.comp.date-picker.modal.range-selection.header.headline.tracking | FONT_TRACKING | Enabled / Headline | `md.sys.typescale.title-large.tracking` |  |  |
| md.comp.date-picker.modal.range-selection.header.headline.type | TYPOGRAPHY | Enabled / Headline | font name: `md.comp.date-picker.modal.range-selection.header.headline.font`, font weight: `md.comp.date-picker.modal.range-selection.header.headline.weight`, font size: `md.comp.date-picker.modal.range-selection.header.headline.size`, font tracking: `md.comp.date-picker.modal.range-selection.header.headline.tracking`, line height: `md.comp.date-picker.modal.range-selection.header.headline.line-height` |  |  |
| md.comp.date-picker.modal.header.supporting-text.type | TYPOGRAPHY | Enabled / Supporting text | font name: `md.comp.date-picker.modal.header.supporting-text.font`, font weight: `md.comp.date-picker.modal.header.supporting-text.weight`, font size: `md.comp.date-picker.modal.header.supporting-text.size`, font tracking: `md.comp.date-picker.modal.header.supporting-text.tracking`, line height: `md.comp.date-picker.modal.header.supporting-text.line-height` |  |  |
| md.comp.date-picker.modal.date.state-layer.shape | SHAPE | Enabled / State layer | `md.sys.shape.corner.full` |  |  |
| md.comp.date-picker.modal.year-selection.year.state-layer.shape | SHAPE | Enabled / State layer | `md.sys.shape.corner.full` |  |  |
| md.comp.date-picker.modal.date.state-layer.width | LENGTH | Enabled / State layer | 40dp |  |  |
| md.comp.date-picker.modal.date.state-layer.height | LENGTH | Enabled / State layer | 40dp |  |  |
| md.comp.date-picker.modal.year-selection.year.state-layer.width | LENGTH | Enabled / State layer | 72dp |  |  |
| md.comp.date-picker.modal.year-selection.year.state-layer.height | LENGTH | Enabled / State layer | 36dp |  |  |
| md.comp.date-picker.modal.header.supporting-text.color | COLOR | Enabled / Header | `md.sys.color.on-surface-variant` |  |  |
| md.comp.date-picker.modal.header.supporting-text.font | FONT_NAMES | Enabled / Header | `md.sys.typescale.label-large.font` |  |  |
| md.comp.date-picker.modal.header.supporting-text.line-height | LINE_HEIGHT | Enabled / Header | `md.sys.typescale.label-large.line-height` |  |  |
| md.comp.date-picker.modal.header.supporting-text.size | FONT_SIZE | Enabled / Header | `md.sys.typescale.label-large.size` |  |  |
| md.comp.date-picker.modal.header.supporting-text.weight | FONT_WEIGHT | Enabled / Header | `md.sys.typescale.label-large.weight` |  |  |
| md.comp.date-picker.modal.header.supporting-text.tracking | FONT_TRACKING | Enabled / Header | `md.sys.typescale.label-large.tracking` |  |  |
| md.comp.date-picker.modal.date.unselected.hover.state-layer.color | COLOR | Hovered / State layer | `md.sys.color.on-surface-variant` |  |  |
| md.comp.date-picker.modal.date.selected.hover.state-layer.color | COLOR | Hovered / State layer | `md.sys.color.on-primary` |  |  |
| md.comp.date-picker.modal.date.today.hover.state-layer.color | COLOR | Hovered / State layer | `md.sys.color.primary` |  |  |
| md.comp.date-picker.modal.date.hover.state-layer.opacity | OPACITY | Hovered / State layer | `md.sys.state.hover.state-layer-opacity` |  |  |
| md.comp.date-picker.modal.range-selection.date.in-range.hover.state-layer.color | COLOR | Hovered / State layer | `md.sys.color.on-primary-container` |  |  |
| md.comp.date-picker.modal.range-selection.date.in-range.hover.state-layer.opacity | OPACITY | Hovered / State layer | `md.sys.state.hover.state-layer-opacity` |  |  |
| md.comp.date-picker.modal.range-selection.date.in-range.hover.state-layer.opcaity | OPACITY | Hovered / State layer | `md.sys.state.hover.state-layer-opacity` |  | Fixed spelling (was ".opcaity" should be ".opacity") |
| md.comp.date-picker.modal.year-selection.year.unselected.hover.state-layer.color | COLOR | Hovered / State layer | `md.sys.color.on-surface-variant` |  |  |
| md.comp.date-picker.modal.year-selection.year.selected.hover.state-layer.color | COLOR | Hovered / State layer | `md.sys.color.on-primary` |  |  |
| md.comp.date-picker.modal.year-selection.year.hover.state-layer.opacity | OPACITY | Hovered / State layer | `md.sys.state.hover.state-layer-opacity` |  |  |
| md.comp.date-picker.modal.range-selection.date.in-range.focus.state-layer.opacity | OPACITY | Focused / State layer | `md.sys.state.focus.state-layer-opacity` |  |  |
| md.comp.date-picker.modal.date.unselected.focus.state-layer.color | COLOR | Focused / State layer | `md.sys.color.on-surface-variant` |  |  |
| md.comp.date-picker.modal.date.selected.focus.state-layer.color | COLOR | Focused / State layer | `md.sys.color.on-primary` |  |  |
| md.comp.date-picker.modal.date.today.focus.state-layer.color | COLOR | Focused / State layer | `md.sys.color.primary` |  |  |
| md.comp.date-picker.modal.date.focus.state-layer.opacity | OPACITY | Focused / State layer | `md.sys.state.focus.state-layer-opacity` |  |  |
| md.comp.date-picker.modal.range-selection.date.in-range.focus.state-layer.color | COLOR | Focused / State layer | `md.sys.color.on-primary-container` |  |  |
| md.comp.date-picker.modal.range-selection.date.in-range.focus.state-layer.opcaity | OPACITY | Focused / State layer | `md.sys.state.focus.state-layer-opacity` |  | Fixed spelling (was ".opcaity" should be ".opacity") |
| md.comp.date-picker.modal.year-selection.year.unselected.focus.state-layer.color | COLOR | Focused / State layer | `md.sys.color.on-surface-variant` |  |  |
| md.comp.date-picker.modal.year-selection.year.selected.focus.state-layer.color | COLOR | Focused / State layer | `md.sys.color.on-primary` |  |  |
| md.comp.date-picker.modal.year-selection.year.focus.state-layer.opacity | OPACITY | Focused / State layer | `md.sys.state.focus.state-layer-opacity` |  |  |
| md.comp.date-picker.modal.range-selection.date.in-range.pressed.state-layer.opacity | OPACITY | Pressed (ripple) / State layer | `md.sys.state.pressed.state-layer-opacity` |  |  |
| md.comp.date-picker.modal.date.unselected.pressed.state-layer.color | COLOR | Pressed (ripple) / State layer | `md.sys.color.on-surface-variant` |  |  |
| md.comp.date-picker.modal.date.selected.pressed.state-layer.color | COLOR | Pressed (ripple) / State layer | `md.sys.color.on-primary` |  |  |
| md.comp.date-picker.modal.date.today.pressed.state-layer.color | COLOR | Pressed (ripple) / State layer | `md.sys.color.primary` |  |  |
| md.comp.date-picker.modal.date.pressed.state-layer.opacity | OPACITY | Pressed (ripple) / State layer | `md.sys.state.pressed.state-layer-opacity` |  |  |
| md.comp.date-picker.modal.range-selection.date.in-range.pressed.state-layer.color | COLOR | Pressed (ripple) / State layer | `md.sys.color.on-primary-container` |  |  |
| md.comp.date-picker.modal.range-selection.date.in-range.pressed.state-layer.opcaity | OPACITY | Pressed (ripple) / State layer | `md.sys.state.pressed.state-layer-opacity` |  | Fixed spelling (was ".opcaity" should be ".opacity") |
| md.comp.date-picker.modal.year-selection.year.unselected.pressed.state-layer.color | COLOR | Pressed (ripple) / State layer | `md.sys.color.on-surface-variant` |  |  |
| md.comp.date-picker.modal.year-selection.year.selected.pressed.state-layer.color | COLOR | Pressed (ripple) / State layer | `md.sys.color.on-primary` |  |  |
| md.comp.date-picker.modal.year-selection.year.pressed.state-layer.opacity | OPACITY | Pressed (ripple) / State layer | `md.sys.state.pressed.state-layer-opacity` |  |  |

#### Date picker - Modal input (md.comp.date-input.modal)

For modal input date pickers.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.date-input.modal.container.color | COLOR | Enabled / Container | `md.sys.color.surface-container-high` |  |  |
| md.comp.date-input.modal.container.elevation | ELEVATION | Enabled / Container | `md.sys.elevation.level3` |  |  |
| md.comp.date-input.modal.container.surface-tint-layer.color | COLOR | Enabled / Container | `md.sys.color.surface-tint` |  | Deprecated as part of the update from opacity based surfaces to tonal surfaces. Surfaces no longer use surface-tint layers for tinting, please use the desired surface role directly as the container color. |
| md.comp.date-input.modal.container.shape | SHAPE | Enabled / Container | `md.sys.shape.corner.extra-large` |  |  |
| md.comp.date-input.modal.container.width | LENGTH | Enabled / Container | 328dp |  |  |
| md.comp.date-input.modal.container.height | LENGTH | Enabled / Container | 512dp |  |  |
| md.comp.date-input.modal.header.container.width | LENGTH | Enabled / Container | 328dp |  |  |
| md.comp.date-input.modal.header.container.height | LENGTH | Enabled / Container | 120dp |  |  |
| md.comp.date-input.modal.header.headline.color | COLOR | Enabled / Headline | `md.sys.color.on-surface-variant` |  |  |
| md.comp.date-input.modal.header.headline.font | FONT_NAMES | Enabled / Headline | `md.sys.typescale.headline-large.font` |  |  |
| md.comp.date-input.modal.header.headline.line-height | LINE_HEIGHT | Enabled / Headline | `md.sys.typescale.headline-large.line-height` |  |  |
| md.comp.date-input.modal.header.headline.size | FONT_SIZE | Enabled / Headline | `md.sys.typescale.headline-large.size` |  |  |
| md.comp.date-input.modal.header.headline.weight | FONT_WEIGHT | Enabled / Headline | `md.sys.typescale.headline-large.weight` |  |  |
| md.comp.date-input.modal.header.headline.tracking | FONT_TRACKING | Enabled / Headline | `md.sys.typescale.headline-large.tracking` |  |  |
| md.comp.date-input.modal.header.headline.type | TYPOGRAPHY | Enabled / Headline | font name: `md.comp.date-input.modal.header.headline.font`, font weight: `md.comp.date-input.modal.header.headline.weight`, font size: `md.comp.date-input.modal.header.headline.size`, font tracking: `md.comp.date-input.modal.header.headline.tracking`, line height: `md.comp.date-input.modal.header.headline.line-height` |  |  |
| md.comp.date-input.modal.header.supporting-text.color | COLOR | Enabled / Supporting text | `md.sys.color.on-surface-variant` |  |  |
| md.comp.date-input.modal.header.supporting-text.font | FONT_NAMES | Enabled / Supporting text | `md.sys.typescale.label-large.font` |  |  |
| md.comp.date-input.modal.header.supporting-text.line-height | LINE_HEIGHT | Enabled / Supporting text | `md.sys.typescale.label-large.line-height` |  |  |
| md.comp.date-input.modal.header.supporting-text.size | FONT_SIZE | Enabled / Supporting text | `md.sys.typescale.label-large.size` |  |  |
| md.comp.date-input.modal.header.supporting-text.weight | FONT_WEIGHT | Enabled / Supporting text | `md.sys.typescale.label-large.weight` |  |  |
| md.comp.date-input.modal.header.supporting-text.tracking | FONT_TRACKING | Enabled / Supporting text | `md.sys.typescale.label-large.tracking` |  |  |
| md.comp.date-input.modal.header.supporting-text.type | TYPOGRAPHY | Enabled / Supporting text | font name: `md.comp.date-input.modal.header.supporting-text.font`, font weight: `md.comp.date-input.modal.header.supporting-text.weight`, font size: `md.comp.date-input.modal.header.supporting-text.size`, font tracking: `md.comp.date-input.modal.header.supporting-text.tracking`, line height: `md.comp.date-input.modal.header.supporting-text.line-height` |  |  |

## Docked date picker

![Diagram indicating the 11 elements of a docked date picker.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fme6vnbm5-01-3P.png?alt=media&token=f8cfe189-33cd-4fc4-a6dc-bd6450d2e6a1>)

_Outlined text field; Menu button: Month selection; Menu button: Year selection; Icon button; Weekdays label text; Unselected date; Today’s date; Outside month date; Text buttons; Selected date; Container_

![Diagram indicating 8 elements of a docked date picker with an open dropdown menu showing the months May to November.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fme6vo1av-02-3P.png?alt=media&token=cba8add3-87cc-4939-add2-d986a5c24e70>)

_Outlined text field; Menu button: Month selection (pressed); Menu button: Year selection (disabled); Header; Menu; Selected list item; Unselected menu list item; Container_

### Docked date picker color

Color values are implemented through design tokens. For design, this means working with color values that correspond with tokens. For implementation, a color value will be a token that references a value. [Learn more about design tokens](https://m3.material.io/m3/pages/design-tokens/overview/)

![11 color roles of a docked date picker in light and dark themes.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fme6vofa8-03-3P.png?alt=media&token=3bd8bafd-1d95-4cf2-af69-9023191c7f36>)

_Docked date picker color roles used for light and dark themes: Primary; On surface variant; On surface variant; On surface; On surface; Primary; On surface variant; Primary; Surface container high; Primary; On primary_

![7 color roles of a docked date picker menu in light and dark themes.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fme6vovcl-04-3P.png?alt=media&token=1a14476e-327e-4930-8f5e-21b31ebe0633>)

_Docked date picker menu color roles used for light and dark themes: Primary; On surface variant; On surface; Outline variant; Surface container high; Surface variant; On surface_

### Docked date picker measurements

![Diagram of padding, size, and layout measurements.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmel11ou5-05-3P.png?alt=media&token=02f8923f-d188-460f-af59-6fe54d826576>)

_Docked date picker padding and size measurements_

![Diagram of padding, size, and layout measurements.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmel12gcl-06-3P.png?alt=media&token=9fa12372-b9a0-418d-9723-c45792f3ed8d>)

_Docked date picker month menu padding and size measurements_

### Docked date picker configurations

![3 configurations of docked date picker.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fme6vrc28-07-3P.png?alt=media&token=85602f38-74d4-4677-9fab-3d0c70e3e2ab>)

_Day selection; Month selection; Year selection_

## Modal date picker

![Diagram indicating the 13 elements of a modal date picker in the day selection view.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fme6vsstf-08-3P.png?alt=media&token=f8917782-dc39-459c-82c1-25a58cc38522>)

_Headline; Supporting text; Header; Container; Icon button; Icon buttons; Weekdays; Today’s date; Unselected date; Text buttons; Selected date; Menu button; Divider_

![10 elements of a modal date picker menu.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fme6vtc2f-09-3P.png?alt=media&token=83ed57ad-19ce-49fd-ae71-8abbffaa04c0>)

_Headline; Supporting text; Header; Container; Icon button; Unselected year; Selected year; Text buttons; Divider; Menu button_

![Diagram indicating the 15 elements of a modal date picker when selecting a range of dates.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fme6vtxo6-10-3P.png?alt=media&token=f05f53b5-59da-4e5f-9ec9-7203ad1f9580>)

_Headline; Supporting text; Icon button; Header; Text button; Icon button; Weekdays label text; Container; Today’s date; Unselected date; In-range active indicator; In-range date; Month subhead; Selected date; Divider_

### Modal date picker color

Color values are implemented through design tokens. For design, this means working with color values that correspond with tokens. For implementation, a color value will be a token that references a value. [Learn more about design tokens](https://m3.material.io/m3/pages/design-tokens/overview/)

![12 color roles of a modal date picker day selection view.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fme6vuima-11-3P.png?alt=media&token=1b73b296-3144-48af-8be5-d480a52c7c25>)

_Modal date picker color roles used for light and dark themes in a day selection menu: On surface; On surface variant; Surface container high; On surface variant; On surface variant; On surface; Primary; On surface; Primary; Primary; On surface variant; Outline variant_

![Diagram of 9 color roles of a modal date picker year selection view.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fme6vuwh7-12-3P.png?alt=media&token=fb15589b-0448-409d-86f3-8abe2ea61b51>)

_Modal date picker color roles used for light and dark themes in a year selection menu: On surface; On surface variant; Surface container high; On surface variant; On surface variant; Primary; Primary; Outline variant; On surface variant_

![Diagram of 14 color roles of a modal date picker when selecting a range of dates.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fme6vv9ft-13-3P.png?alt=media&token=2d71a3a0-7075-4060-b90d-8e7b4e923f91>)

_Modal date picker range selector color roles used for light and dark themes: / On surface; On surface variant; On surface variant; Surface container high; Primary; On surface variant; On surface; Primary; On surface; Secondary container; On secondary container; Outline variant; On surface variant; Primary_

### Modal date picker measurements

![Diagram of size and padding measurements in day selection view.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmel16bx5-14-3P.png?alt=media&token=32b56f0d-5b77-413c-9223-f2ae6d5cc275>)

_Modal date picker padding and size measurements_

![Diagram of size and padding measurements in year selection view.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmel16puv-15-3P.png?alt=media&token=dec9ac5f-f517-4f99-b9e8-4f0d672a76e0>)

_Modal date picker year selector padding and size measurements_

![Diagram of size and padding measurements when selecting a range of dates.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmel175wp-16-3P.png?alt=media&token=4cd021d6-ccee-4180-94d2-4cdc83d3b60f>)

_Modal date picker date range selector padding and size measurements_

### Modal date picker configurations

![3 configurations of a modal date picker shown in dark mode.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fme6vxg0z-17-3P.png?alt=media&token=0c687b22-f9e0-400f-b021-a647102e0d5b>)

_Single date selection; Date range selection; Year selection_

## Modal date input

![Diagram indicating the 8 elements of a modal date input.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fme6vxz7r-18-3P.png?alt=media&token=bd73746c-8b00-49a3-9e3c-e5424fedbc59>)

_Headline; Supporting text; Header; Container; Icon button; Outlined text field; Text buttons; Divider_

### Modal date input color

Color values are implemented through design tokens. For design, this means working with color values that correspond with tokens. For implementation, a color value will be a token that references a value. [Learn more about design tokens](https://m3.material.io/m3/pages/design-tokens/overview/)

![Diagram indicating the 7 color roles of a modal date input.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fme6vyfrl-19-3P.png?alt=media&token=78be5f98-a2b6-4572-85c3-b4f56cc6fecb>)

_Modal date input color roles used for light and dark themes: On surface; On surface variant; Surface container high; On surface variant; Primary; Primary; Outline variant_

### Modal date input measurements

![Diagram of the padding and size measurements of a modal date input.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fme6vyzk0-20-3P.png?alt=media&token=48c3f31a-3c58-4f58-9d95-2ffbbec4ea12>)

_Modal date input padding and size measurements_

### Modal date input configurations

![2 configurations of modal date input.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fme6vzozd-21-3P.png?alt=media&token=9efa75cb-9f0e-4244-855d-97c76e55e80e>)

_Single date input; Date range input_

## Element states

![Diagram of 5 various states for date and year elements within date pickers.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fme6w02sb-22b-3P.png?alt=media&token=39741611-fe07-4d4f-a3ab-91c2ae665a5d>)

_States for date and year selection: Default (enabled); Disabled; Hovered; Focused; Pressed (ripple)_

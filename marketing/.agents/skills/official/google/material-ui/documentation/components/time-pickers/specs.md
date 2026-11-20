---
title: "Time pickers"
source_url: "https://m3.material.io/components/time-pickers/specs"
lastmod: "2026-02-05"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "components"
assets:
  - "assets/images/225b19b4773b-mdcxf11n-01-3P.png"
  - "assets/images/d7a465e55694-mdcxfokb-02-3P.png"
  - "assets/images/9ed1cbeaf64b-l6nxwe4v-[1P]-color-time-pickers-(1).jpg"
  - "assets/images/4fa47e75ed79-l6nxy34v-[1P]-color-time-input-(1).jpg"
  - "assets/images/34ee69fc5e4a-m8t5j9hb-5b.png"
  - "assets/images/20ab4eef09ad-l6p241d2-[1P]-layout-time-pickers-vertical-(1).png"
  - "assets/images/3e022b55a3c8-l6p279e1-[1P]-layout-time-pickers-horizontal-(3).png"
  - "assets/images/d87e3dce624e-l6p28zo3-[1P]-layout-time-input-(1).png"
  - "assets/images/9a589b1f1767-l7ao2hx2-[1P]-guideline-time-pickers-configurations.png"
  - "assets/images/c73f655b3a55-l7ao3ny7-[1P]-guideline-time-pickers-configurations-24h.png"
  - "assets/images/2832c3e3089c-l7ao513q-[1P]-guideline-time-input-configurations.png"
---

# Time pickers

## Specs

## Tokens & specs

Select a component variant below to see its elements, attributes, tokens, and their values. [Learn more about design tokens](../../foundations/design-tokens/overview.md)

### TOKEN_TABLE

Component: Time pickers

Configured context tags: None, Medium contrast, Default, High contrast, Static, 3P, Android, Dark, Light

#### Token sets

| Token set | Name | Type | Tokens | Description |
| --- | --- | --- | --- | --- |
| Time picker - Dial | md.comp.time-picker | COMPONENT | 99 | For dial time pickers. |
| Time picker - Input | md.comp.time-input | COMPONENT | 75 | For input time pickers. |

#### Time picker - Dial (md.comp.time-picker)

For dial time pickers.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.time-picker.clock-dial.color | COLOR | Enabled / Container | `md.sys.color.surface-container-highest` |  |  |
| md.comp.time-picker.clock-dial.shape | SHAPE | Enabled / Container | `md.sys.shape.corner.full` |  |  |
| md.comp.time-picker.container.color | COLOR | Enabled / Container | `md.sys.color.surface-container-high` |  |  |
| md.comp.time-picker.container.elevation | ELEVATION | Enabled / Container | `md.sys.elevation.level3` |  |  |
| md.comp.time-picker.surface-tint-layer.color | COLOR | Enabled / Container | `md.sys.color.surface-tint` |  | Deprecated as part of the update from opacity based surfaces to tonal surfaces. Surfaces no longer use surface-tint layers for tinting, please use the desired surface role directly as the container color. |
| md.comp.time-picker.container.shape | SHAPE | Enabled / Container | `md.sys.shape.corner.extra-large` |  |  |
| md.comp.time-picker.time-selector.selected.container.color | COLOR | Enabled / Container | `md.sys.color.primary-container` |  |  |
| md.comp.time-picker.time-selector.unselected.container.color | COLOR | Enabled / Container | `md.sys.color.surface-container-highest` |  |  |
| md.comp.time-picker.time-selector.container.shape | SHAPE | Enabled / Container | `md.sys.shape.corner.small` |  |  |
| md.comp.time-picker.period-selector.selected.container.color | COLOR | Enabled / Container | `md.sys.color.tertiary-container` |  |  |
| md.comp.time-picker.period-selector.container.shape | SHAPE | Enabled / Container | `md.sys.shape.corner.small` |  |  |
| md.comp.time-picker.period-selector.outline.color | COLOR | Enabled / Container | `md.sys.color.outline` |  |  |
| md.comp.time-picker.period-selector.outline.width | LENGTH | Enabled / Container | 1dp |  |  |
| md.comp.time-picker.clock-dial.color.ignore | COLOR | Enabled / Container | `md.sys.color.on-surface-variant` |  | Deprecating token due to typo in name. Please use md.comp.time-picker.clock-dial.color instead" |
| md.comp.time-picker.clock-dial.shape.ignore | SHAPE | Enabled / Container | `md.sys.shape.corner.full` |  | Deprecating token due to typo in name. Please use md.comp.time-picker.clock-dial.shape instead |
| md.comp.time-picker.clock-dial.selector.handle.container.color | COLOR | Enabled / Container | `md.sys.color.primary` |  |  |
| md.comp.time-picker.clock-dial.selector.handle.container.shape | SHAPE | Enabled / Container | `md.sys.shape.corner.full` |  |  |
| md.comp.time-picker.clock-dial.selector.center.container.color | COLOR | Enabled / Container | `md.sys.color.primary` |  |  |
| md.comp.time-picker.clock-dial.selector.center.container.shape | SHAPE | Enabled / Container | `md.sys.shape.corner.full` |  |  |
| md.comp.time-picker.clock-dial.selector.track.container.color | COLOR | Enabled / Container | `md.sys.color.primary` |  |  |
| md.comp.time-picker.time-selector.container.width | LENGTH | Enabled / Container | 96dp |  |  |
| md.comp.time-picker.time-selector.24h-vertical.container.width | LENGTH | Enabled / Container | 114dp |  |  |
| md.comp.time-picker.time-selector.container.height | LENGTH | Enabled / Container | 80dp |  |  |
| md.comp.time-picker.period-selector.vertical.container.width | LENGTH | Enabled / Container | 52dp |  |  |
| md.comp.time-picker.period-selector.vertical.container.height | LENGTH | Enabled / Container | 80dp |  |  |
| md.comp.time-picker.period-selector.horizontal.container.width | LENGTH | Enabled / Container | 216dp |  |  |
| md.comp.time-picker.period-selector.horizontal.container.height | LENGTH | Enabled / Container | 38dp |  |  |
| md.comp.time-picker.clock-dial.container.size | LENGTH | Enabled / Container | 256dp |  |  |
| md.comp.time-picker.clock-dial.selector.handle.container.size | LENGTH | Enabled / Container | 48dp |  |  |
| md.comp.time-picker.clock-dial.selector.center.container.size | LENGTH | Enabled / Container | 8dp |  |  |
| md.comp.time-picker.clock-dial.selector.track.container.width | LENGTH | Enabled / Container | 2dp |  |  |
| md.comp.time-picker.time-selector.selected.label-text.color | COLOR | Enabled / Label text | `md.sys.color.on-primary-container` |  |  |
| md.comp.time-picker.time-selector.unselected.label-text.color | COLOR | Enabled / Label text | `md.sys.color.on-surface` |  |  |
| md.comp.time-picker.time-selector.label-text.font | FONT_NAMES | Enabled / Label text | `md.sys.typescale.display-large.font` |  |  |
| md.comp.time-picker.time-selector.label-text.line-height | LINE_HEIGHT | Enabled / Label text | `md.sys.typescale.display-large.line-height` |  |  |
| md.comp.time-picker.time-selector.label-text.size | FONT_SIZE | Enabled / Label text | `md.sys.typescale.display-large.size` |  |  |
| md.comp.time-picker.time-selector.label-text.weight | FONT_WEIGHT | Enabled / Label text | `md.sys.typescale.display-large.weight` |  |  |
| md.comp.time-picker.time-selector.label-text.tracking | FONT_TRACKING | Enabled / Label text | `md.sys.typescale.display-large.tracking` |  |  |
| md.comp.time-picker.time-selector.label-text.type | TYPOGRAPHY | Enabled / Label text | font name: `md.comp.time-picker.time-selector.label-text.font`, font weight: `md.comp.time-picker.time-selector.label-text.weight`, font size: `md.comp.time-picker.time-selector.label-text.size`, font tracking: `md.comp.time-picker.time-selector.label-text.tracking`, line height: `md.comp.time-picker.time-selector.label-text.line-height` |  |  |
| md.comp.time-picker.period-selector.selected.label-text.color | COLOR | Enabled / Label text | `md.sys.color.on-tertiary-container` |  |  |
| md.comp.time-picker.period-selector.unselected.label-text.color | COLOR | Enabled / Label text | `md.sys.color.on-surface-variant` |  |  |
| md.comp.time-picker.period-selector.label-text.font | FONT_NAMES | Enabled / Label text | `md.sys.typescale.title-medium.font` |  |  |
| md.comp.time-picker.period-selector.label-text.line-height | LINE_HEIGHT | Enabled / Label text | `md.sys.typescale.title-medium.line-height` |  |  |
| md.comp.time-picker.period-selector.label-text.size | FONT_SIZE | Enabled / Label text | `md.sys.typescale.title-medium.size` |  |  |
| md.comp.time-picker.period-selector.label-text.weight | FONT_WEIGHT | Enabled / Label text | `md.sys.typescale.title-medium.weight` |  |  |
| md.comp.time-picker.period-selector.label-text.tracking | FONT_TRACKING | Enabled / Label text | `md.sys.typescale.title-medium.tracking` |  |  |
| md.comp.time-picker.period-selector.label-text.type | TYPOGRAPHY | Enabled / Label text | font name: `md.comp.time-picker.period-selector.label-text.font`, font weight: `md.comp.time-picker.period-selector.label-text.weight`, font size: `md.comp.time-picker.period-selector.label-text.size`, font tracking: `md.comp.time-picker.period-selector.label-text.tracking`, line height: `md.comp.time-picker.period-selector.label-text.line-height` |  |  |
| md.comp.time-picker.clock-dial.selected.label-text.color | COLOR | Enabled / Label text | `md.sys.color.on-primary` |  |  |
| md.comp.time-picker.clock-dial.unselected.label-text.color | COLOR | Enabled / Label text | `md.sys.color.on-surface` |  |  |
| md.comp.time-picker.clock-dial.label-text.font | FONT_NAMES | Enabled / Label text | `md.sys.typescale.body-large.font` |  |  |
| md.comp.time-picker.clock-dial.label-text.line-height | LINE_HEIGHT | Enabled / Label text | `md.sys.typescale.body-large.line-height` |  |  |
| md.comp.time-picker.clock-dial.label-text.size | FONT_SIZE | Enabled / Label text | `md.sys.typescale.body-large.size` |  |  |
| md.comp.time-picker.clock-dial.label-text.weight | FONT_WEIGHT | Enabled / Label text | `md.sys.typescale.body-large.weight` |  |  |
| md.comp.time-picker.clock-dial.label-text.tracking | FONT_TRACKING | Enabled / Label text | `md.sys.typescale.body-large.tracking` |  |  |
| md.comp.time-picker.clock-dial.label-text.type | TYPOGRAPHY | Enabled / Label text | font name: `md.comp.time-picker.clock-dial.label-text.font`, font weight: `md.comp.time-picker.clock-dial.label-text.weight`, font size: `md.comp.time-picker.clock-dial.label-text.size`, font tracking: `md.comp.time-picker.clock-dial.label-text.tracking`, line height: `md.comp.time-picker.clock-dial.label-text.line-height` |  |  |
| md.comp.time-picker.headline.color | COLOR | Enabled / Headline | `md.sys.color.on-surface-variant` |  |  |
| md.comp.time-picker.headline.font | FONT_NAMES | Enabled / Headline | `md.sys.typescale.label-medium.font` |  |  |
| md.comp.time-picker.headline.line-height | LINE_HEIGHT | Enabled / Headline | `md.sys.typescale.label-medium.line-height` |  |  |
| md.comp.time-picker.headline.size | FONT_SIZE | Enabled / Headline | `md.sys.typescale.label-medium.size` |  |  |
| md.comp.time-picker.headline.weight | FONT_WEIGHT | Enabled / Headline | `md.sys.typescale.label-medium.weight` |  |  |
| md.comp.time-picker.headline.tracking | FONT_TRACKING | Enabled / Headline | `md.sys.typescale.label-medium.tracking` |  |  |
| md.comp.time-picker.headline.type | TYPOGRAPHY | Enabled / Headline | font name: `md.comp.time-picker.headline.font`, font weight: `md.comp.time-picker.headline.weight`, font size: `md.comp.time-picker.headline.size`, font tracking: `md.comp.time-picker.headline.tracking`, line height: `md.comp.time-picker.headline.line-height` |  |  |
| md.comp.time-picker.time-selector.separator.color | COLOR | Enabled / Separator | `md.sys.color.on-surface` |  |  |
| md.comp.time-picker.time-selector.separator.font | FONT_NAMES | Enabled / Separator | `md.sys.typescale.display-large.font` |  |  |
| md.comp.time-picker.time-selector.separator.line-height | LINE_HEIGHT | Enabled / Separator | `md.sys.typescale.display-large.line-height` |  |  |
| md.comp.time-picker.time-selector.separator.size | FONT_SIZE | Enabled / Separator | `md.sys.typescale.display-large.size` |  |  |
| md.comp.time-picker.time-selector.separator.weight | FONT_WEIGHT | Enabled / Separator | `md.sys.typescale.display-large.weight` |  |  |
| md.comp.time-picker.time-selector.separator.tracking | FONT_TRACKING | Enabled / Separator | `md.sys.typescale.display-large.tracking` |  |  |
| md.comp.time-picker.time-selector.separator.type | TYPOGRAPHY | Enabled / Separator | font name: `md.comp.time-picker.time-selector.separator.font`, font weight: `md.comp.time-picker.time-selector.separator.weight`, font size: `md.comp.time-picker.time-selector.separator.size`, font tracking: `md.comp.time-picker.time-selector.separator.tracking`, line height: `md.comp.time-picker.time-selector.separator.line-height` |  |  |
| md.comp.time-picker.time-selector.selected.hover.label-text.color | COLOR | Hovered / Label text | `md.sys.color.on-primary-container` |  |  |
| md.comp.time-picker.time-selector.unselected.hover.label-text.color | COLOR | Hovered / Label text | `md.sys.color.on-surface` |  |  |
| md.comp.time-picker.period-selector.selected.hover.label-text.color | COLOR | Hovered / Label text | `md.sys.color.on-tertiary-container` |  |  |
| md.comp.time-picker.period-selector.unselected.hover.label-text.color | COLOR | Hovered / Label text | `md.sys.color.on-surface-variant` |  |  |
| md.comp.time-picker.time-selector.selected.hover.state-layer.color | COLOR | Hovered / State layer | `md.sys.color.on-primary-container` |  |  |
| md.comp.time-picker.time-selector.unselected.hover.state-layer.color | COLOR | Hovered / State layer | `md.sys.color.on-surface` |  |  |
| md.comp.time-picker.time-selector.hover.state-layer.opacity | OPACITY | Hovered / State layer | `md.sys.state.hover.state-layer-opacity` |  |  |
| md.comp.time-picker.period-selector.selected.hover.state-layer.color | COLOR | Hovered / State layer | `md.sys.color.on-tertiary-container` |  |  |
| md.comp.time-picker.period-selector.unselected.hover.state-layer.color | COLOR | Hovered / State layer | `md.sys.color.on-surface-variant` |  |  |
| md.comp.time-picker.period-selector.hover.state-layer.opacity | OPACITY | Hovered / State layer | `md.sys.state.hover.state-layer-opacity` |  |  |
| md.comp.time-picker.time-selector.selected.focus.label-text.color | COLOR | Focused / Label text | `md.sys.color.on-primary-container` |  |  |
| md.comp.time-picker.time-selector.unselected.focus.label-text.color | COLOR | Focused / Label text | `md.sys.color.on-surface` |  |  |
| md.comp.time-picker.period-selector.selected.focus.label-text.color | COLOR | Focused / Label text | `md.sys.color.on-tertiary-container` |  |  |
| md.comp.time-picker.period-selector.unselected.focus.label-text.color | COLOR | Focused / Label text | `md.sys.color.on-surface-variant` |  |  |
| md.comp.time-picker.time-selector.selected.focus.state-layer.color | COLOR | Focused / State layer | `md.sys.color.on-primary-container` |  |  |
| md.comp.time-picker.time-selector.unselected.focus.state-layer.color | COLOR | Focused / State layer | `md.sys.color.on-surface` |  |  |
| md.comp.time-picker.time-selector.focus.state-layer.opacity | OPACITY | Focused / State layer | `md.sys.state.focus.state-layer-opacity` |  |  |
| md.comp.time-picker.period-selector.selected.focus.state-layer.color | COLOR | Focused / State layer | `md.sys.color.on-tertiary-container` |  |  |
| md.comp.time-picker.period-selector.unselected.focus.state-layer.color | COLOR | Focused / State layer | `md.sys.color.on-surface-variant` |  |  |
| md.comp.time-picker.period-selector.focus.state-layer.opacity | OPACITY | Focused / State layer | `md.sys.state.focus.state-layer-opacity` |  |  |
| md.comp.time-picker.time-selector.selected.pressed.label-text.color | COLOR | Pressed (ripple) / Label text | `md.sys.color.on-primary-container` |  |  |
| md.comp.time-picker.time-selector.unselected.pressed.label-text.color | COLOR | Pressed (ripple) / Label text | `md.sys.color.on-surface` |  |  |
| md.comp.time-picker.period-selector.selected.pressed.label-text.color | COLOR | Pressed (ripple) / Label text | `md.sys.color.on-tertiary-container` |  |  |
| md.comp.time-picker.period-selector.unselected.pressed.label-text.color | COLOR | Pressed (ripple) / Label text | `md.sys.color.on-surface-variant` |  |  |
| md.comp.time-picker.time-selector.selected.pressed.state-layer.color | COLOR | Pressed (ripple) / State layer | `md.sys.color.on-primary-container` |  |  |
| md.comp.time-picker.time-selector.unselected.pressed.state-layer.color | COLOR | Pressed (ripple) / State layer | `md.sys.color.on-surface` |  |  |
| md.comp.time-picker.time-selector.pressed.state-layer.opacity | OPACITY | Pressed (ripple) / State layer | `md.sys.state.pressed.state-layer-opacity` |  |  |
| md.comp.time-picker.period-selector.selected.pressed.state-layer.color | COLOR | Pressed (ripple) / State layer | `md.sys.color.on-tertiary-container` |  |  |
| md.comp.time-picker.period-selector.unselected.pressed.state-layer.color | COLOR | Pressed (ripple) / State layer | `md.sys.color.on-surface-variant` |  |  |
| md.comp.time-picker.period-selector.pressed.state-layer.opacity | OPACITY | Pressed (ripple) / State layer | `md.sys.state.pressed.state-layer-opacity` |  |  |

#### Time picker - Input (md.comp.time-input)

For input time pickers.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.time-input.container.color | COLOR | Enabled / Container | `md.sys.color.surface-container-high` |  |  |
| md.comp.time-input.container.elevation | ELEVATION | Enabled / Container | `md.sys.elevation.level3` |  |  |
| md.comp.time-input.surface-tint-layer.color | COLOR | Enabled / Container | `md.sys.color.surface-tint` |  | Deprecated as part of the update from opacity based surfaces to tonal surfaces. Surfaces no longer use surface-tint layers for tinting, please use the desired surface role directly as the container color. |
| md.comp.time-input.container.shape | SHAPE | Enabled / Container | `md.sys.shape.corner.extra-large` |  |  |
| md.comp.time-input.time-input-field.container.color | COLOR | Enabled / Container | `md.sys.color.surface-container-highest` |  |  |
| md.comp.time-input.time-input-field.container.shape | SHAPE | Enabled / Container | `md.sys.shape.corner.small` |  |  |
| md.comp.time-input.period-selector.selected.container.color | COLOR | Enabled / Container | `md.sys.color.tertiary-container` |  |  |
| md.comp.time-input.period-selector.container.shape | SHAPE | Enabled / Container | `md.sys.shape.corner.small` |  |  |
| md.comp.time-input.period-selector.outline.color | COLOR | Enabled / Container | `md.sys.color.outline` |  |  |
| md.comp.time-input.period-selector.outline.width | LENGTH | Enabled / Container | 1dp |  |  |
| md.comp.time-input.time-input-field.container.width | LENGTH | Enabled / Container | 96dp |  |  |
| md.comp.time-input.time-input-field.container.height | LENGTH | Enabled / Container | 72dp |  |  |
| md.comp.time-input.period-selector.container.width | LENGTH | Enabled / Container | 52dp |  |  |
| md.comp.time-input.period-selector.container.height | LENGTH | Enabled / Container | 72dp |  |  |
| md.comp.time-input.time-input-field.label-text.color | COLOR | Enabled / Label text | `md.sys.color.on-surface` |  |  |
| md.comp.time-input.time-input-field.label-text.font | FONT_NAMES | Enabled / Label text | `md.sys.typescale.display-medium.font` |  |  |
| md.comp.time-input.time-input-field.label-text.line-height | LINE_HEIGHT | Enabled / Label text | `md.sys.typescale.display-medium.line-height` |  |  |
| md.comp.time-input.time-input-field.label-text.size | FONT_SIZE | Enabled / Label text | `md.sys.typescale.display-medium.size` |  |  |
| md.comp.time-input.time-input-field.label-text.weight | FONT_WEIGHT | Enabled / Label text | `md.sys.typescale.display-medium.weight` |  |  |
| md.comp.time-input.time-input-field.label-text.tracking | FONT_TRACKING | Enabled / Label text | `md.sys.typescale.display-medium.tracking` |  |  |
| md.comp.time-input.time-input-field.label-text.type | TYPOGRAPHY | Enabled / Label text | font name: `md.comp.time-input.time-input-field.label-text.font`, font weight: `md.comp.time-input.time-input-field.label-text.weight`, font size: `md.comp.time-input.time-input-field.label-text.size`, font tracking: `md.comp.time-input.time-input-field.label-text.tracking`, line height: `md.comp.time-input.time-input-field.label-text.line-height` |  |  |
| md.comp.time-input.period-selector.selected.label-text.color | COLOR | Enabled / Label text | `md.sys.color.on-tertiary-container` |  |  |
| md.comp.time-input.period-selector.unselected.label-text.color | COLOR | Enabled / Label text | `md.sys.color.on-surface-variant` |  |  |
| md.comp.time-input.period-selector.label-text.font | FONT_NAMES | Enabled / Label text | `md.sys.typescale.title-medium.font` |  |  |
| md.comp.time-input.period-selector.label-text.line-height | LINE_HEIGHT | Enabled / Label text | `md.sys.typescale.title-medium.line-height` |  |  |
| md.comp.time-input.period-selector.label-text.size | FONT_SIZE | Enabled / Label text | `md.sys.typescale.title-medium.size` |  |  |
| md.comp.time-input.period-selector.label-text.weight | FONT_WEIGHT | Enabled / Label text | `md.sys.typescale.title-medium.weight` |  |  |
| md.comp.time-input.period-selector.label-text.tracking | FONT_TRACKING | Enabled / Label text | `md.sys.typescale.title-medium.tracking` |  |  |
| md.comp.time-input.period-selector.label-text.type | TYPOGRAPHY | Enabled / Label text | font name: `md.comp.time-input.period-selector.label-text.font`, font weight: `md.comp.time-input.period-selector.label-text.weight`, font size: `md.comp.time-input.period-selector.label-text.size`, font tracking: `md.comp.time-input.period-selector.label-text.tracking`, line height: `md.comp.time-input.period-selector.label-text.line-height` |  |  |
| md.comp.time-input.headline.color | COLOR | Enabled / Headline | `md.sys.color.on-surface-variant` |  |  |
| md.comp.time-input.headline.font | FONT_NAMES | Enabled / Headline | `md.sys.typescale.label-medium.font` |  |  |
| md.comp.time-input.headline.line-height | LINE_HEIGHT | Enabled / Headline | `md.sys.typescale.label-medium.line-height` |  |  |
| md.comp.time-input.headline.size | FONT_SIZE | Enabled / Headline | `md.sys.typescale.label-medium.size` |  |  |
| md.comp.time-input.headline.weight | FONT_WEIGHT | Enabled / Headline | `md.sys.typescale.label-medium.weight` |  |  |
| md.comp.time-input.headline.tracking | FONT_TRACKING | Enabled / Headline | `md.sys.typescale.label-medium.tracking` |  |  |
| md.comp.time-input.headline.type | TYPOGRAPHY | Enabled / Headline | font name: `md.comp.time-input.headline.font`, font weight: `md.comp.time-input.headline.weight`, font size: `md.comp.time-input.headline.size`, font tracking: `md.comp.time-input.headline.tracking`, line height: `md.comp.time-input.headline.line-height` |  |  |
| md.comp.time-input.time-input-field.supporting-text.color | COLOR | Enabled / Supporting text | `md.sys.color.on-surface-variant` |  |  |
| md.comp.time-input.time-input-field.supporting-text.font | FONT_NAMES | Enabled / Supporting text | `md.sys.typescale.body-small.font` |  |  |
| md.comp.time-input.time-input-field.supporting-text.line-height | LINE_HEIGHT | Enabled / Supporting text | `md.sys.typescale.body-small.line-height` |  |  |
| md.comp.time-input.time-input-field.supporting-text.size | FONT_SIZE | Enabled / Supporting text | `md.sys.typescale.body-small.size` |  |  |
| md.comp.time-input.time-input-field.supporting-text.weight | FONT_WEIGHT | Enabled / Supporting text | `md.sys.typescale.body-small.weight` |  |  |
| md.comp.time-input.time-input-field.supporting-text.tracking | FONT_TRACKING | Enabled / Supporting text | `md.sys.typescale.body-small.tracking` |  |  |
| md.comp.time-input.time-input-field.supporting-text.type | TYPOGRAPHY | Enabled / Supporting text | font name: `md.comp.time-input.time-input-field.supporting-text.font`, font weight: `md.comp.time-input.time-input-field.supporting-text.weight`, font size: `md.comp.time-input.time-input-field.supporting-text.size`, font tracking: `md.comp.time-input.time-input-field.supporting-text.tracking`, line height: `md.comp.time-input.time-input-field.supporting-text.line-height` |  |  |
| md.comp.time-input.time-input-field.separator.color | COLOR | Enabled / Separator | `md.sys.color.on-surface` |  |  |
| md.comp.time-input.time-input-field.separator.font | FONT_NAMES | Enabled / Separator | `md.sys.typescale.display-large.font` |  |  |
| md.comp.time-input.time-input-field.separator.line-height | LINE_HEIGHT | Enabled / Separator | `md.sys.typescale.display-large.line-height` |  |  |
| md.comp.time-input.time-input-field.separator.size | FONT_SIZE | Enabled / Separator | `md.sys.typescale.display-large.size` |  |  |
| md.comp.time-input.time-input-field.separator.weight | FONT_WEIGHT | Enabled / Separator | `md.sys.typescale.display-large.weight` |  |  |
| md.comp.time-input.time-input-field.separator.tracking | FONT_TRACKING | Enabled / Separator | `md.sys.typescale.display-large.tracking` |  |  |
| md.comp.time-input.time-input-field.separator.type | TYPOGRAPHY | Enabled / Separator | font name: `md.comp.time-input.time-input-field.separator.font`, font weight: `md.comp.time-input.time-input-field.separator.weight`, font size: `md.comp.time-input.time-input-field.separator.size`, font tracking: `md.comp.time-input.time-input-field.separator.tracking`, line height: `md.comp.time-input.time-input-field.separator.line-height` |  |  |
| md.comp.time-input.time-input-field.hover.label-text.color | COLOR | Hovered / Label text | `md.sys.color.on-surface` |  |  |
| md.comp.time-input.period-selector.selected.hover.label-text.color | COLOR | Hovered / Label text | `md.sys.color.on-tertiary-container` |  |  |
| md.comp.time-input.period-selector.unselected.hover.label-text.color | COLOR | Hovered / Label text | `md.sys.color.on-surface-variant` |  |  |
| md.comp.time-input.time-input-field.hover.state-layer.color | COLOR | Hovered / State layer | `md.sys.color.on-surface` |  |  |
| md.comp.time-input.time-input-field.hover.state-layer.opacity | OPACITY | Hovered / State layer | `md.sys.state.hover.state-layer-opacity` |  |  |
| md.comp.time-input.period-selector.selected.hover.state-layer.color | COLOR | Hovered / State layer | `md.sys.color.on-tertiary-container` |  |  |
| md.comp.time-input.period-selector.unselected.hover.state-layer.color | COLOR | Hovered / State layer | `md.sys.color.on-surface-variant` |  |  |
| md.comp.time-input.period-selector.hover.state-layer.opacity | OPACITY | Hovered / State layer | `md.sys.state.hover.state-layer-opacity` |  |  |
| md.comp.time-input.focus.indicator.color | COLOR | Focused / Focus indicator | `md.sys.color.secondary` |  |  |
| md.comp.time-input.focus.indicator.thickness | LENGTH | Focused / Focus indicator | `md.sys.state.focus-indicator.thickness` |  |  |
| md.comp.time-input.focus.indicator.outline.offset | LENGTH | Focused / Focus indicator | `md.sys.state.focus-indicator.outer-offset` |  |  |
| md.comp.time-input.time-input-field.focus.container.color | COLOR | Focused / Container | `md.sys.color.primary-container` |  |  |
| md.comp.time-input.time-input-field.focus.outline.color | COLOR | Focused / Container | `md.sys.color.primary` |  |  |
| md.comp.time-input.time-input-field.focus.outline.width | LENGTH | Focused / Container | 2dp |  |  |
| md.comp.time-input.time-input-field.focus.label-text.color | COLOR | Focused / Label text | `md.sys.color.on-primary-container` |  |  |
| md.comp.time-input.period-selector.selected.focus.label-text.color | COLOR | Focused / Label text | `md.sys.color.on-tertiary-container` |  |  |
| md.comp.time-input.period-selector.unselected.focus.label-text.color | COLOR | Focused / Label text | `md.sys.color.on-surface-variant` |  |  |
| md.comp.time-input.period-selector.selected.focus.state-layer.color | COLOR | Focused / State layer | `md.sys.color.on-tertiary-container` |  |  |
| md.comp.time-input.period-selector.unselected.focus.state-layer.color | COLOR | Focused / State layer | `md.sys.color.on-surface-variant` |  |  |
| md.comp.time-input.period-selector.focus.state-layer.opacity | OPACITY | Focused / State layer | `md.sys.state.focus.state-layer-opacity` |  |  |
| md.comp.time-input.period-selector.selected.pressed.label-text.color | COLOR | Pressed (ripple) / Label text | `md.sys.color.on-tertiary-container` |  |  |
| md.comp.time-input.period-selector.unselected.pressed.label-text.color | COLOR | Pressed (ripple) / Label text | `md.sys.color.on-surface-variant` |  |  |
| md.comp.time-input.period-selector.selected.pressed.state-layer.color | COLOR | Pressed (ripple) / State layer | `md.sys.color.on-tertiary-container` |  |  |
| md.comp.time-input.period-selector.unselected.pressed.state-layer.color | COLOR | Pressed (ripple) / State layer | `md.sys.color.on-surface-variant` |  |  |
| md.comp.time-input.period-selector.pressed.state-layer.opacity | OPACITY | Pressed (ripple) / State layer | `md.sys.state.pressed.state-layer-opacity` |  |  |

## Anatomy

### Time picker dial

![Diagram indicating the 14 elements of a time picker dial.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmdcxf11n-01-3P.png?alt=media&token=3ecd0554-c896-4456-819b-c56c8313bbf1>)

_Headline; Time selector separator; Container; Period selector container; Period selector label text; Clock dial selector center; Clock dial selector track; Text button; Icon button; Clock dial selector container; Clock dial label text; Clock dial container; Time selector label text; Time selector container_

### Time picker input

![Diagram indicating the 10 elements of a time picker input.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmdcxfokb-02-3P.png?alt=media&token=490ea902-f5a1-434a-bf2c-9013a3b71b07>)

_Headline; Time input field seperator; Container; Period selector container; Period selector label text; Text button; Icon button; Time input field supporting text; Time input field label text; Time input field container_

## Color

Color values are implemented through design tokens. For design, this means working with color values that correspond with tokens. For implementation, a color value will be a token that references a value. [Learn more about design tokens](../../foundations/design-tokens/overview.md)

### Time picker dial color

![Side-by-side diagram indicating the 17 different color elements of a time picker dial.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fl6nxwe4v-%5B1P%5D%20color-time-pickers%20(1).jpg?alt=media&token=f0bbaf37-d293-45c1-91e2-2d9fe41ace48>)

_Time picker dial color roles used for light and dark themes: On surface variant; On surface; Surface container highest; On surface; Tertiary container; On tertiary container; Surface container high; Outline; On surface; Primary; On primary; Primary; On surface variant; On surface; Surface container highest; On primary container; Primary container_

### Time picker input color

![Side-by-side diagram indicating the 13 different color elements of a time picker input.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fl6nxy34v-%5B1P%5D%20color-time-input%20(1).jpg?alt=media&token=463baa36-094b-470a-995d-2db6970ab755>)

_Time picker input color roles used for light and dark themes: On surface variant; On surface; Surface container highest; On surface; Tertiary container; On tertiary container; Surface container high; Outline; On surface; Primary; On surface variant; On primary container; Primary container_

## States

![Diagram showing the 4 interactive states of a time picker, in both light theme and dark theme.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fm8t5j9hb-5b.png?alt=media&token=841395db-38a8-47d7-86af-8716459b6032>)

_Enabled; Hover; Focus; Pressed_

[States specs can be found in the token module above](specs.md#2ccd9809-9246-4667-85fa-7747f4ac7349)

## Measurements

### Time picker dial - vertical

![Diagram of vertical time picker dial measurements.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fl6p241d2-%5B1P%5D%20layout-time-pickers-vertical%20(1).png?alt=media&token=cf0e2679-196d-4b0f-8b1e-1fb589e239f4>)

_Vertical time picker dial padding and size measurements_

| Element | Attribute | Value |
| --- | --- | --- |
| Container | Width | Dynamic |
| Height | Dynamic |  |
| Headline alignment | Left |  |
| Top/bottom padding | 24dp |  |
| Left/right padding | 24dp |  |
| Time selector container | Width | 96dp |
| Width (24h vertical) | 114dp |  |
| Height | 80dp |  |
| Period selector container | Width (vertical layout) | 52dp |
| Height (vertical layout) | 80dp |  |
| Width (horizontal layout) | 216dp |  |
| Height (horizontal layout) | 38dp |  |
| Clock dial container | Size | 256dp |
| Clock dial selector handle | Size | 48dp |
| Clock dial selector center | Size | 8dp |
| Clock dial selector track | Width | 2dp |

### Time picker dial - horizontal

![Diagram of horizontal time picker dial measurements.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fl6p279e1-%5B1P%5D%20layout-time-pickers-horizontal%20(3).png?alt=media&token=ffeb93b2-0e9b-48d5-9350-36ab567fce0f>)

_Horizontal time picker dial padding and size measurements_

| Element | Attribute | Value |
| --- | --- | --- |
| Container | Width | Dynamic |
| Height | Dynamic |  |
| Headline alignment | Left |  |
| Top/bottom padding | 24dp |  |
| Left/right padding | 24dp |  |
| Time selector container | Width | 96dp |
| Width (24h vertical) | 114dp |  |
| Height | 80dp |  |
| Period selector container | Width (vertical layout) | 52dp |
| Height (vertical layout) | 80dp |  |
| Width (horizontal layout) | 216dp |  |
| Height (horizontal layout) | 38dp |  |
| Clock dial container | Size | 256dp |
| Clock dial selector handle | Size | 48dp |
| Clock dial selector center | Size | 8dp |
| Clock dial selector track | Width | 2dp |

### Time picker input

![Diagram of time picker input measurements.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fl6p28zo3-%5B1P%5D%20layout-time-input%20(1).png?alt=media&token=c1fe856d-0deb-4101-98cf-da76f23414cd>)

_Time picker input padding and size measurements_

| Element | Attribute | Value |
| --- | --- | --- |
| Container | Width | Dynamic |
| Height | Dynamic |  |
| Headline alignment | Left |  |
| Top/bottom padding | 24dp |  |
| Left/right padding | 24dp |  |
| Time input field container | Width | 96dp |
| Height | 72dp |  |
| Period selector container | Width | 52dp |
| Height | 72dp |  |

## Configurations

### Vertical orientation and horizontal orientation

![Comparing vertical and horizontal time picker dials.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fl7ao2hx2-%5B1P%5D%20guideline-time-pickers-configurations.png?alt=media&token=e43accb7-46d8-4afe-9258-ceffc4f81158>)

_Vertical layout (default on mobile); Horizontal layout_

### 24-hour time picker dial

![2 24-hour time picker dials with vertical and horizontal layouts.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fl7ao3ny7-%5B1P%5D%20guideline-time-pickers-configurations-24h.png?alt=media&token=68160ebd-03b8-44d2-8eff-b21bacb9d372>)

_24h dial in vertical layout (default on mobile); 24h dial in horizontal layout_

### 12-hour and 24-hour time picker inputs

![Compare 12-hour and 24-hour time picker inputs.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fl7ao513q-%5B1P%5D%20guideline-time-input-configurations.png?alt=media&token=161a6bfb-8df3-4318-bc4c-2af54a37e7ce>)

_12h input; 24h input_

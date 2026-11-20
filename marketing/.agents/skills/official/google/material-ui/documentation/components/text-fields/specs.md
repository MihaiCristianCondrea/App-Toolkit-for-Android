---
title: "Text fields"
source_url: "https://m3.material.io/components/text-fields/specs"
lastmod: "2026-05-18"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "components"
assets:
  - "assets/images/4a67db93abb5-m2j3wjd6-1.png"
  - "assets/images/1e7b23e6ae40-m2j3x0d6-2-3P.png"
  - "assets/images/6902c75bf75b-lyqwy2pr-3.png"
  - "assets/images/a6fc462532ae-lyqx5pjx-4.png"
  - "assets/images/240c4da25c72-lyqxam89-5.png"
  - "assets/images/fa25e532c808-lyqxhgvg-6.png"
  - "assets/images/6920bff8b7bc-m4adcnvc-7-3P.png"
  - "assets/images/c2563cb5fcec-m84goiff-8-3P.png"
  - "assets/images/a516b444f343-mhk4c6jn-09-3P.png"
  - "assets/images/ca994a0b6710-m2j3xtlg-10-3P.png"
  - "assets/images/9e46ee37dd16-lyqy69rp-11.png"
  - "assets/images/61ba0085222b-lyqya4uw-12.png"
  - "assets/images/2ba658ab29ab-lyqys303-13.png"
  - "assets/images/a204788e1163-lyqyuu9g-14.png"
  - "assets/images/c72343eb4dd4-lyqywif5-15.png"
  - "assets/images/7b5fd2232386-m84gq17k-16-3P.png"
---

# Text fields

Text fields let users enter text into a UI

## Specs

## Tokens & specs

Browse the component elements, attributes, tokens, and their values. [Learn about design tokens](../../foundations/design-tokens/overview.md)

### TOKEN_TABLE

Component: Text fields

Configured context tags: None, Medium contrast, Default, High contrast, Static, 3P, Android, Dark, Light

#### Token sets

| Token set | Name | Type | Tokens | Description |
| --- | --- | --- | --- | --- |
| Text field - Filled | md.comp.filled-text-field | COMPONENT | 89 | For filled text fields. |
| Text field - Outlined | md.comp.outlined-text-field | COMPONENT | 84 | For outlined text fields. |

#### Text field - Filled (md.comp.filled-text-field)

For filled text fields.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.filled-text-field.container.color | COLOR | Enabled / Container | `md.sys.color.surface-container-highest` |  |  |
| md.comp.filled-text-field.container.height | LENGTH | Enabled / Container | 56dp |  | Removing fixed height token due to conflicts with text fields variants requiring dynamic height (multi-line, text area). Use height of internal elements and paddings to control component height. |
| md.comp.filled-text-field.container.shape | SHAPE | Enabled / Container | `md.sys.shape.corner.extra-small.top` |  |  |
| md.comp.filled-text-field.label-text.color | COLOR | Enabled / Label text | `md.sys.color.on-surface-variant` |  |  |
| md.comp.filled-text-field.label-text.font | FONT_NAMES | Enabled / Label text | `md.sys.typescale.body-large.font` |  |  |
| md.comp.filled-text-field.label-text.line-height | LINE_HEIGHT | Enabled / Label text | `md.sys.typescale.body-large.line-height` |  |  |
| md.comp.filled-text-field.label-text.size | FONT_SIZE | Enabled / Label text | `md.sys.typescale.body-large.size` |  |  |
| md.comp.filled-text-field.label-text.weight | FONT_WEIGHT | Enabled / Label text | `md.sys.typescale.body-large.weight` |  |  |
| md.comp.filled-text-field.label-text.tracking | FONT_TRACKING | Enabled / Label text | `md.sys.typescale.body-large.tracking` |  |  |
| md.comp.filled-text-field.label-text.type | TYPOGRAPHY | Enabled / Label text | font name: `md.comp.filled-text-field.label-text.font`, font weight: `md.comp.filled-text-field.label-text.weight`, font size: `md.comp.filled-text-field.label-text.size`, font tracking: `md.comp.filled-text-field.label-text.tracking`, line height: `md.comp.filled-text-field.label-text.line-height` |  |  |
| md.comp.filled-text-field.label-text.populated.line-height | LINE_HEIGHT | Enabled / Label text | `md.sys.typescale.body-small.line-height` |  |  |
| md.comp.filled-text-field.label-text.populated.size | FONT_SIZE | Enabled / Label text | `md.sys.typescale.body-small.size` |  |  |
| md.comp.filled-text-field.leading-icon.color | COLOR | Enabled / Leading icon | `md.sys.color.on-surface-variant` |  |  |
| md.comp.filled-text-field.leading-icon.size | LENGTH | Enabled / Leading icon | 24dp |  |  |
| md.comp.filled-text-field.trailing-icon.color | COLOR | Enabled / Trailing icon | `md.sys.color.on-surface-variant` |  |  |
| md.comp.filled-text-field.trailing-icon.size | LENGTH | Enabled / Trailing icon | 24dp |  |  |
| md.comp.filled-text-field.active-indicator.height | LENGTH | Enabled / Active indicator | 1dp |  |  |
| md.comp.filled-text-field.active-indicator.color | COLOR | Enabled / Active indicator | `md.sys.color.on-surface-variant` |  |  |
| md.comp.filled-text-field.supporting-text.type | TYPOGRAPHY | Enabled / Supporting text | font name: `md.comp.filled-text-field.supporting-text.font`, font weight: `md.comp.filled-text-field.supporting-text.weight`, font size: `md.comp.filled-text-field.supporting-text.size`, font tracking: `md.comp.filled-text-field.supporting-text.tracking`, line height: `md.comp.filled-text-field.supporting-text.line-height` |  |  |
| md.comp.filled-text-field.supporting-text.color | COLOR | Enabled / Supporting text | `md.sys.color.on-surface-variant` |  |  |
| md.comp.filled-text-field.supporting-text.font | FONT_NAMES | Enabled / Supporting text | `md.sys.typescale.body-small.font` |  |  |
| md.comp.filled-text-field.supporting-text.line-height | LINE_HEIGHT | Enabled / Supporting text | `md.sys.typescale.body-small.line-height` |  |  |
| md.comp.filled-text-field.supporting-text.size | FONT_SIZE | Enabled / Supporting text | `md.sys.typescale.body-small.size` |  |  |
| md.comp.filled-text-field.supporting-text.weight | FONT_WEIGHT | Enabled / Supporting text | `md.sys.typescale.body-small.weight` |  |  |
| md.comp.filled-text-field.supporting-text.tracking | FONT_TRACKING | Enabled / Supporting text | `md.sys.typescale.body-small.tracking` |  |  |
| md.comp.filled-text-field.input-text.color | COLOR | Enabled / Input text | `md.sys.color.on-surface` |  |  |
| md.comp.filled-text-field.input-text.font | FONT_NAMES | Enabled / Input text | `md.sys.typescale.body-large.font` |  |  |
| md.comp.filled-text-field.input-text.line-height | LINE_HEIGHT | Enabled / Input text | `md.sys.typescale.body-large.line-height` |  |  |
| md.comp.filled-text-field.input-text.size | FONT_SIZE | Enabled / Input text | `md.sys.typescale.body-large.size` |  |  |
| md.comp.filled-text-field.input-text.weight | FONT_WEIGHT | Enabled / Input text | `md.sys.typescale.body-large.weight` |  |  |
| md.comp.filled-text-field.input-text.tracking | FONT_TRACKING | Enabled / Input text | `md.sys.typescale.body-large.tracking` |  |  |
| md.comp.filled-text-field.input-text.type | TYPOGRAPHY | Enabled / Input text | font name: `md.comp.filled-text-field.input-text.font`, font weight: `md.comp.filled-text-field.input-text.weight`, font size: `md.comp.filled-text-field.input-text.size`, font tracking: `md.comp.filled-text-field.input-text.tracking`, line height: `md.comp.filled-text-field.input-text.line-height` |  |  |
| md.comp.filled-text-field.input-text.prefix.color | COLOR | Enabled / Input text | `md.sys.color.on-surface-variant` |  |  |
| md.comp.filled-text-field.input-text.suffix.color | COLOR | Enabled / Input text | `md.sys.color.on-surface-variant` |  |  |
| md.comp.filled-text-field.input-text.placeholder.color | COLOR | Enabled / Input text | `md.sys.color.on-surface-variant` |  |  |
| md.comp.filled-text-field.caret.color | COLOR | Enabled / Caret | `md.sys.color.primary` |  |  |
| md.comp.filled-text-field.disabled.container.color | COLOR | Disabled / Container | `md.sys.color.on-surface` |  |  |
| md.comp.filled-text-field.disabled.container.opacity | OPACITY | Disabled / Container | 0.04 |  |  |
| md.comp.filled-text-field.disabled.label-text.color | COLOR | Disabled / Label text | `md.sys.color.on-surface` |  |  |
| md.comp.filled-text-field.disabled.label-text.opacity | OPACITY | Disabled / Label text | 0.38 |  |  |
| md.comp.filled-text-field.disabled.leading-icon.color | COLOR | Disabled / Leading icon | `md.sys.color.on-surface` |  |  |
| md.comp.filled-text-field.disabled.leading-icon.opacity | OPACITY | Disabled / Leading icon | 0.38 |  |  |
| md.comp.filled-text-field.disabled.trailing-icon.color | COLOR | Disabled / Trailing icon | `md.sys.color.on-surface` |  |  |
| md.comp.filled-text-field.disabled.trailing-icon.opacity | OPACITY | Disabled / Trailing icon | 0.38 |  |  |
| md.comp.filled-text-field.disabled.supporting-text.color | COLOR | Disabled / Supporting text | `md.sys.color.on-surface` |  |  |
| md.comp.filled-text-field.disabled.supporting-text.opacity | OPACITY | Disabled / Supporting text | 0.38 |  |  |
| md.comp.filled-text-field.disabled.input-text.color | COLOR | Disabled / Input text | `md.sys.color.on-surface` |  |  |
| md.comp.filled-text-field.disabled.input-text.opacity | OPACITY | Disabled / Input text | 0.38 |  |  |
| md.comp.filled-text-field.disabled.active-indicator.height | LENGTH | Disabled / Active indicator | 1dp |  |  |
| md.comp.filled-text-field.disabled.active-indicator.color | COLOR | Disabled / Active indicator | `md.sys.color.on-surface` |  |  |
| md.comp.filled-text-field.disabled.active-indicator.opacity | OPACITY | Disabled / Active indicator | 0.38 |  |  |
| md.comp.filled-text-field.hover.label-text.color | COLOR | Hovered / Label text | `md.sys.color.on-surface-variant` |  |  |
| md.comp.filled-text-field.hover.state-layer.color | COLOR | Hovered / State layer | `md.sys.color.on-surface` |  |  |
| md.comp.filled-text-field.hover.state-layer.opacity | OPACITY | Hovered / State layer | `md.sys.state.hover.state-layer-opacity` |  |  |
| md.comp.filled-text-field.hover.leading-icon.color | COLOR | Hovered / Leading icon | `md.sys.color.on-surface-variant` |  |  |
| md.comp.filled-text-field.hover.trailing-icon.color | COLOR | Hovered / Trailing icon | `md.sys.color.on-surface-variant` |  |  |
| md.comp.filled-text-field.hover.input-text.color | COLOR | Hovered / Input text | `md.sys.color.on-surface` |  |  |
| md.comp.filled-text-field.hover.supporting-text.color | COLOR | Hovered / Supporting text | `md.sys.color.on-surface-variant` |  |  |
| md.comp.filled-text-field.hover.active-indicator.height | LENGTH | Hovered / Active indicator | 1dp |  |  |
| md.comp.filled-text-field.hover.active-indicator.color | COLOR | Hovered / Active indicator | `md.sys.color.on-surface` |  |  |
| md.comp.filled-text-field.focus.label-text.color | COLOR | Focused / Label text | `md.sys.color.primary` |  |  |
| md.comp.filled-text-field.focus.leading-icon.color | COLOR | Focused / Leading icon | `md.sys.color.on-surface-variant` |  |  |
| md.comp.filled-text-field.focus.trailing-icon.color | COLOR | Focused / Trailing icon | `md.sys.color.on-surface-variant` |  |  |
| md.comp.filled-text-field.focus.input-text.color | COLOR | Focused / Input text | `md.sys.color.on-surface` |  |  |
| md.comp.filled-text-field.focus.supporting-text.color | COLOR | Focused / Supporting text | `md.sys.color.on-surface-variant` |  |  |
| md.comp.filled-text-field.focus.active-indicator.height | LENGTH | Focused / Active indicator | 2dp |  |  |
| md.comp.filled-text-field.focus.active-indicator.color | COLOR | Focused / Active indicator | `md.sys.color.primary` |  |  |
| md.comp.filled-text-field.focus.active-indicator.thickness | LENGTH | Focused / Active indicator | `md.sys.state.focus-indicator.thickness` |  |  |
| md.comp.filled-text-field.error.active-indicator.color | COLOR | Error | `md.sys.color.error` |  |  |
| md.comp.filled-text-field.error.label-text.color | COLOR | Error | `md.sys.color.error` |  |  |
| md.comp.filled-text-field.error.input-text.color | COLOR | Error | `md.sys.color.on-surface` |  |  |
| md.comp.filled-text-field.error.supporting-text.color | COLOR | Error | `md.sys.color.error` |  |  |
| md.comp.filled-text-field.error.leading-icon.color | COLOR | Error | `md.sys.color.on-surface-variant` |  |  |
| md.comp.filled-text-field.error.trailing-icon.color | COLOR | Error | `md.sys.color.error` |  |  |
| md.comp.filled-text-field.error.focus.active-indicator.color | COLOR | Error / Focus | `md.sys.color.error` |  |  |
| md.comp.filled-text-field.error.focus.label-text.color | COLOR | Error / Focus | `md.sys.color.error` |  |  |
| md.comp.filled-text-field.error.focus.input-text.color | COLOR | Error / Focus | `md.sys.color.on-surface` |  |  |
| md.comp.filled-text-field.error.focus.supporting-text.color | COLOR | Error / Focus | `md.sys.color.error` |  |  |
| md.comp.filled-text-field.error.focus.leading-icon.color | COLOR | Error / Focus | `md.sys.color.on-surface-variant` |  |  |
| md.comp.filled-text-field.error.focus.trailing-icon.color | COLOR | Error / Focus | `md.sys.color.error` |  |  |
| md.comp.filled-text-field.error.focus.caret.color | COLOR | Error / Focus | `md.sys.color.error` |  |  |
| md.comp.filled-text-field.error.hover.active-indicator.color | COLOR | Error / Hover | `md.sys.color.on-error-container` |  |  |
| md.comp.filled-text-field.error.hover.label-text.color | COLOR | Error / Hover | `md.sys.color.on-error-container` |  |  |
| md.comp.filled-text-field.error.hover.input-text.color | COLOR | Error / Hover | `md.sys.color.on-surface` |  |  |
| md.comp.filled-text-field.error.hover.supporting-text.color | COLOR | Error / Hover | `md.sys.color.error` |  |  |
| md.comp.filled-text-field.error.hover.leading-icon.color | COLOR | Error / Hover | `md.sys.color.on-surface-variant` |  |  |
| md.comp.filled-text-field.error.hover.trailing-icon.color | COLOR | Error / Hover | `md.sys.color.on-error-container` |  |  |
| md.comp.filled-text-field.error.hover.state-layer.color | COLOR | Error / Hover | `md.sys.color.on-surface` |  |  |
| md.comp.filled-text-field.error.hover.state-layer.opacity | OPACITY | Error / Hover | `md.sys.state.hover.state-layer-opacity` |  |  |

#### Text field - Outlined (md.comp.outlined-text-field)

For outlined text fields.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.outlined-text-field.container.height | LENGTH | Enabled / Container | 56dp |  | Removing fixed height token due to conflicts with text fields variants requiring dynamic height (multi-line, text area). Use height of internal elements and paddings to control component height. |
| md.comp.outlined-text-field.container.shape | SHAPE | Enabled / Container | `md.sys.shape.corner.extra-small` |  |  |
| md.comp.outlined-text-field.outline.width | LENGTH | Enabled / Outline | 1dp |  |  |
| md.comp.outlined-text-field.outline.color | COLOR | Enabled / Outline | `md.sys.color.outline` |  |  |
| md.comp.outlined-text-field.label-text.color | COLOR | Enabled / Label text | `md.sys.color.on-surface-variant` |  |  |
| md.comp.outlined-text-field.label-text.font | FONT_NAMES | Enabled / Label text | `md.sys.typescale.body-large.font` |  |  |
| md.comp.outlined-text-field.label-text.line-height | LINE_HEIGHT | Enabled / Label text | `md.sys.typescale.body-large.line-height` |  |  |
| md.comp.outlined-text-field.label-text.size | FONT_SIZE | Enabled / Label text | `md.sys.typescale.body-large.size` |  |  |
| md.comp.outlined-text-field.label-text.weight | FONT_WEIGHT | Enabled / Label text | `md.sys.typescale.body-large.weight` |  |  |
| md.comp.outlined-text-field.label-text.tracking | FONT_TRACKING | Enabled / Label text | `md.sys.typescale.body-large.tracking` |  |  |
| md.comp.outlined-text-field.label-text.type | TYPOGRAPHY | Enabled / Label text | font name: `md.comp.outlined-text-field.label-text.font`, font weight: `md.comp.outlined-text-field.label-text.weight`, font size: `md.comp.outlined-text-field.label-text.size`, font tracking: `md.comp.outlined-text-field.label-text.tracking`, line height: `md.comp.outlined-text-field.label-text.line-height` |  |  |
| md.comp.outlined-text-field.label-text.populated.line-height | LINE_HEIGHT | Enabled / Label text | `md.sys.typescale.body-small.line-height` |  |  |
| md.comp.outlined-text-field.label-text.populated.size | FONT_SIZE | Enabled / Label text | `md.sys.typescale.body-small.size` |  |  |
| md.comp.outlined-text-field.leading-icon.color | COLOR | Enabled / Leading icon | `md.sys.color.on-surface-variant` |  |  |
| md.comp.outlined-text-field.leading-icon.size | LENGTH | Enabled / Leading icon | 24dp |  |  |
| md.comp.outlined-text-field.trailing-icon.color | COLOR | Enabled / Trailing icon | `md.sys.color.on-surface-variant` |  |  |
| md.comp.outlined-text-field.trailing-icon.size | LENGTH | Enabled / Trailing icon | 24dp |  |  |
| md.comp.outlined-text-field.supporting-text.color | COLOR | Enabled / Supporting text | `md.sys.color.on-surface-variant` |  |  |
| md.comp.outlined-text-field.supporting-text.font | FONT_NAMES | Enabled / Supporting text | `md.sys.typescale.body-small.font` |  |  |
| md.comp.outlined-text-field.supporting-text.line-height | LINE_HEIGHT | Enabled / Supporting text | `md.sys.typescale.body-small.line-height` |  |  |
| md.comp.outlined-text-field.supporting-text.size | FONT_SIZE | Enabled / Supporting text | `md.sys.typescale.body-small.size` |  |  |
| md.comp.outlined-text-field.supporting-text.weight | FONT_WEIGHT | Enabled / Supporting text | `md.sys.typescale.body-small.weight` |  |  |
| md.comp.outlined-text-field.supporting-text.tracking | FONT_TRACKING | Enabled / Supporting text | `md.sys.typescale.body-small.tracking` |  |  |
| md.comp.outlined-text-field.supporting-text.type | TYPOGRAPHY | Enabled / Supporting text | font name: `md.comp.outlined-text-field.supporting-text.font`, font weight: `md.comp.outlined-text-field.supporting-text.weight`, font size: `md.comp.outlined-text-field.supporting-text.size`, font tracking: `md.comp.outlined-text-field.supporting-text.tracking`, line height: `md.comp.outlined-text-field.supporting-text.line-height` |  |  |
| md.comp.outlined-text-field.input-text.color | COLOR | Enabled / Input text | `md.sys.color.on-surface` |  |  |
| md.comp.outlined-text-field.input-text.font | FONT_NAMES | Enabled / Input text | `md.sys.typescale.body-large.font` |  |  |
| md.comp.outlined-text-field.input-text.line-height | LINE_HEIGHT | Enabled / Input text | `md.sys.typescale.body-large.line-height` |  |  |
| md.comp.outlined-text-field.input-text.size | FONT_SIZE | Enabled / Input text | `md.sys.typescale.body-large.size` |  |  |
| md.comp.outlined-text-field.input-text.weight | FONT_WEIGHT | Enabled / Input text | `md.sys.typescale.body-large.weight` |  |  |
| md.comp.outlined-text-field.input-text.tracking | FONT_TRACKING | Enabled / Input text | `md.sys.typescale.body-large.tracking` |  |  |
| md.comp.outlined-text-field.input-text.type | TYPOGRAPHY | Enabled / Input text | font name: `md.comp.outlined-text-field.input-text.font`, font weight: `md.comp.outlined-text-field.input-text.weight`, font size: `md.comp.outlined-text-field.input-text.size`, font tracking: `md.comp.outlined-text-field.input-text.tracking`, line height: `md.comp.outlined-text-field.input-text.line-height` |  |  |
| md.comp.outlined-text-field.input-text.suffix.color | COLOR | Enabled / Input text | `md.sys.color.on-surface-variant` |  |  |
| md.comp.outlined-text-field.input-text.prefix.color | COLOR | Enabled / Input text | `md.sys.color.on-surface-variant` |  |  |
| md.comp.outlined-text-field.input-text.placeholder.color | COLOR | Enabled / Input text | `md.sys.color.on-surface-variant` |  |  |
| md.comp.outlined-text-field.caret.color | COLOR | Enabled / Caret | `md.sys.color.primary` |  |  |
| md.comp.outlined-text-field.error.focus.caret.color | COLOR | Enabled / Caret | `md.sys.color.error` |  |  |
| md.comp.outlined-text-field.disabled.label-text.color | COLOR | Disabled / Label text | `md.sys.color.on-surface` |  |  |
| md.comp.outlined-text-field.disabled.label-text.opacity | OPACITY | Disabled / Label text | 0.38 |  |  |
| md.comp.outlined-text-field.disabled.leading-icon.color | COLOR | Disabled / Leading icon | `md.sys.color.on-surface` |  |  |
| md.comp.outlined-text-field.disabled.leading-icon.opacity | OPACITY | Disabled / Leading icon | 0.38 |  |  |
| md.comp.outlined-text-field.disabled.trailing-icon.color | COLOR | Disabled / Trailing icon | `md.sys.color.on-surface` |  |  |
| md.comp.outlined-text-field.disabled.trailing-icon.opacity | OPACITY | Disabled / Trailing icon | 0.38 |  |  |
| md.comp.outlined-text-field.disabled.outline.width | LENGTH | Disabled / Outline | 1dp |  |  |
| md.comp.outlined-text-field.disabled.outline.color | COLOR | Disabled / Outline | `md.sys.color.on-surface` |  |  |
| md.comp.outlined-text-field.disabled.outline.opacity | OPACITY | Disabled / Outline | 0.12 |  |  |
| md.comp.outlined-text-field.disabled.supporting-text.color | COLOR | Disabled / Supporting text | `md.sys.color.on-surface` |  |  |
| md.comp.outlined-text-field.disabled.supporting-text.opacity | OPACITY | Disabled / Supporting text | 0.38 |  |  |
| md.comp.outlined-text-field.disabled.input-text.color | COLOR | Disabled / Input text | `md.sys.color.on-surface` |  |  |
| md.comp.outlined-text-field.disabled.input-text.opacity | OPACITY | Disabled / Input text | 0.38 |  |  |
| md.comp.outlined-text-field.hover.label-text.color | COLOR | Hovered / Label text | `md.sys.color.on-surface` |  |  |
| md.comp.outlined-text-field.hover.leading-icon.color | COLOR | Hovered / Leading icon | `md.sys.color.on-surface-variant` |  |  |
| md.comp.outlined-text-field.hover.trailing-icon.color | COLOR | Hovered / Trailing icon | `md.sys.color.on-surface-variant` |  |  |
| md.comp.outlined-text-field.hover.outline.width | LENGTH | Hovered / Outline | 1dp |  |  |
| md.comp.outlined-text-field.hover.outline.color | COLOR | Hovered / Outline | `md.sys.color.on-surface` |  |  |
| md.comp.outlined-text-field.hover.input-text.color | COLOR | Hovered / Input text | `md.sys.color.on-surface` |  |  |
| md.comp.outlined-text-field.hover.supporting-text.color | COLOR | Hovered / Supporting text | `md.sys.color.on-surface-variant` |  |  |
| md.comp.outlined-text-field.focus.label-text.color | COLOR | Focused / Label text | `md.sys.color.primary` |  |  |
| md.comp.outlined-text-field.focus.leading-icon.color | COLOR | Focused / Leading icon | `md.sys.color.on-surface-variant` |  |  |
| md.comp.outlined-text-field.focus.trailing-icon.color | COLOR | Focused / Trailing icon | `md.sys.color.on-surface-variant` |  |  |
| md.comp.outlined-text-field.focus.outline.width | LENGTH | Focused / Outline | 3dp |  |  |
| md.comp.outlined-text-field.focus.outline.color | COLOR | Focused / Outline | `md.sys.color.primary` |  |  |
| md.comp.outlined-text-field.focus.input-text.color | COLOR | Focused / Input text | `md.sys.color.on-surface` |  |  |
| md.comp.outlined-text-field.focus.supporting-text.color | COLOR | Focused / Supporting text | `md.sys.color.on-surface-variant` |  |  |
| md.comp.outlined-text-field.focus.indicator.outline.color | COLOR | Focused / Focus indicator | `md.sys.color.secondary` |  | Deprecating - instead use focus outline tokens |
| md.comp.outlined-text-field.error.focus.indicator.outline.color | COLOR | Focused / Focus indicator | `md.sys.color.error` |  | Deprecating - instead use focus outline tokens |
| md.comp.outlined-text-field.focus.indicator.outline.thickness | LENGTH | Focused / Focus indicator | `md.sys.state.focus-indicator.thickness` |  | Deprecating - instead use focus outline tokens |
| md.comp.outlined-text-field.error.outline.color | COLOR | Error | `md.sys.color.error` |  |  |
| md.comp.outlined-text-field.error.label-text.color | COLOR | Error | `md.sys.color.error` |  |  |
| md.comp.outlined-text-field.error.input-text.color | COLOR | Error | `md.sys.color.on-surface` |  |  |
| md.comp.outlined-text-field.error.supporting-text.color | COLOR | Error | `md.sys.color.error` |  |  |
| md.comp.outlined-text-field.error.leading-icon.color | COLOR | Error | `md.sys.color.on-surface-variant` |  |  |
| md.comp.outlined-text-field.error.trailing-icon.color | COLOR | Error | `md.sys.color.error` |  |  |
| md.comp.outlined-text-field.error.focus.outline.color | COLOR | Error / Focus | `md.sys.color.error` |  |  |
| md.comp.outlined-text-field.error.focus.label-text.color | COLOR | Error / Focus | `md.sys.color.error` |  |  |
| md.comp.outlined-text-field.error.focus.input-text.color | COLOR | Error / Focus | `md.sys.color.on-surface` |  |  |
| md.comp.outlined-text-field.error.focus.supporting-text.color | COLOR | Error / Focus | `md.sys.color.error` |  |  |
| md.comp.outlined-text-field.error.focus.leading-icon.color | COLOR | Error / Focus | `md.sys.color.on-surface-variant` |  |  |
| md.comp.outlined-text-field.error.focus.trailing-icon.color | COLOR | Error / Focus | `md.sys.color.error` |  |  |
| md.comp.outlined-text-field.error.hover.outline.color | COLOR | Error / Hover | `md.sys.color.on-error-container` |  |  |
| md.comp.outlined-text-field.error.hover.label-text.color | COLOR | Error / Hover | `md.sys.color.on-error-container` |  |  |
| md.comp.outlined-text-field.error.hover.input-text.color | COLOR | Error / Hover | `md.sys.color.on-surface` |  |  |
| md.comp.outlined-text-field.error.hover.supporting-text.color | COLOR | Error / Hover | `md.sys.color.error` |  |  |
| md.comp.outlined-text-field.error.hover.leading-icon.color | COLOR | Error / Hover | `md.sys.color.on-surface-variant` |  |  |
| md.comp.outlined-text-field.error.hover.trailing-icon.color | COLOR | Error / Hover | `md.sys.color.on-error-container` |  |  |

## Filled text field

![Diagram of a filled text field indicating the 10 parts of its anatomy.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fm2j3wjd6-1.png?alt=media&token=03c85687-5f77-4b5c-a7b0-a38799a7b42b>)

_Container; Leading icon (optional); Label text in empty field; Label text in populated field; Trailing icon (optional); Focused active Indicator; Caret; Input text; Supporting text (optional); Enabled active indicator_

### Filled text field color

Color values are implemented through design tokens. For design, this means working with color values that correspond with tokens. For implementation, a color value will be a token that references a value. [Learn more about design tokens](../../foundations/design-tokens/overview.md)

![Diagram of a filled text field indicating its color mappings. ](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fm2j3x0d6-2-3P.png?alt=media&token=f9e0224e-9c4d-4c73-b313-88a8d4c63bd3>)

_Filled text field color roles used for light and dark schemes: Surface container highest; On surface variant; On surface variant; Primary; On surface variant; Primary; Primary; On surface; On surface variant; On surface_

### Filled text field states

States are visual representations used to communicate the status of a component or interactive element. [Learn more about interaction states](https://m3.material.io/m3/pages/interaction-states/overview)

![Side by side view of empty and populated filled text fields across different states, showing the differences between enabled, focused, hovered, and disabled. ](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Flyqwy2pr-3.png?alt=media&token=b8865528-a6d3-496c-a968-29b0b626d31e>)

_Enabled (empty); Focused (empty); Hovered (empty); Disabled (empty); Enabled (populated); Focused (populated); Hovered (populated); Disabled (populated)_

### Filled text field error states

Error states are visual representations used to communicate the status of a component or interactive element. An error message can display instructions on how to fix it. Error messages are displayed below the text field as supporting text until fixed.

![Side by side view of empty and populated filled text fields across different error states, showing the differences between enabled, focused, hovered. ](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Flyqx5pjx-4.png?alt=media&token=ea761b5e-fc6e-4979-acd8-69d42c2737f2>)

_Enabled (empty); Focused (empty); Hovered (empty); Enabled (populated); Focused (populated); Hovered (populated)_

### Filled text field measurements

![Diagram showing layout values and paddings for filled text fields without icons. ](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Flyqxam89-5.png?alt=media&token=d571e1d6-c212-4d3e-bb44-522da83b6fb1>)

_Padding and size measurements without icons_

![Diagram showing layout values and paddings for outlined text fields with leading and trailing icons. ](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Flyqxhgvg-6.png?alt=media&token=3a26e5e8-ac4b-438d-b4c8-b56375deae62>)

_Padding and size measurements with icons_

![A diagram showing layout values and paddings for supporting text, and supporting text in combination with a character count. ](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fm4adcnvc-7-3P.png?alt=media&token=0ceb08d6-a607-492b-a62f-a8b29e2db948>)

_Padding and size measurements with supporting text and character count_

| Attribute | Value |
| --- | --- |
| Default container height | 56dp |
| Label alignment (unpopulated) | Vertically centered |
| Top/bottom padding | 8dp |
| Left/right padding without icons | 16dp |
| Left/right padding with icons | 12dp |
| Icon alignment | Vertically centered |
| Padding between icons and text | 16dp |
| Supporting text and character counter top padding | 4dp |
| Padding between supporting text and character counter | 16dp |
| Target size | 56dp |

### Filled text field configurations

![Side by side view of filled text fields in different configurations.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fm84goiff-8%20-%203P.png?alt=media&token=17771e17-b866-4a9b-800b-ad5beed90873>)

_Empty and populated filled text fields with: Supporting text; Trailing icon; Leading icon; Leading and trailing icons; Prefix; Suffix; Multi-line text field_

## Outlined text field

![Diagram of an outlined text field indicating the 9 parts of its anatomy](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmhk4c6jn-09-3P.png?alt=media&token=f55d7652-a5bb-4963-8395-791263662261>)

_Enabled container outline; Leading icon (optional); Label text in empty field; Label text in populated field; Trailing icon (optional); Focused container outline; Caret; Input text; Supporting text (optional)_

### Outlined text field color

Color values are implemented through design tokens. For design, this means working with color values that correspond with tokens. For implementation, a color value will be a token that references a value. [Learn more about design tokens](../../foundations/design-tokens/overview.md)

![Diagram of an outlined text field indicating its color mappings ](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fm2j3xtlg-10-3P.png?alt=media&token=e7f9bb14-13e1-42e5-b79f-1ddd3178f252>)

_Outlined text field color roles used for light and dark schemes: Outline; On surface variant; On surface variant; Primary; On surface variant; Primary; Primary; On surface; On surface variant_

### Outlined text field states

States are visual representations used to communicate the status of a component or interactive element. [Learn more about interaction states](https://m3.material.io/m3/pages/interaction-states/overview)

![Side by side view of empty and populated outlined text fields across different states, showing the differences between enabled, focused, hovered, and disabled. ](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Flyqy69rp-11.png?alt=media&token=ff991447-ecb4-414e-9fa4-9b72b3537e1d>)

_Enabled (empty); Focused (empty); Hovered (empty); Disabled (empty); Enabled (populated); Focused (populated); Hovered (populated); Disabled (populated)_

### Outlined text field error states

Error states are visual representations used to communicate the status of a component or interactive element. An error message can display instructions on how to fix it. Error messages are displayed below the text field as supporting text until fixed.

![Side by side view of empty and populated filled text fields across different error states, showing the differences between enabled, focused, hovered. ](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Flyqya4uw-12.png?alt=media&token=4bd381a5-2489-4bdb-9d62-a5d37ae4fa2a>)

_Enabled (empty); Focused (empty); Hovered (empty); Enabled (populated); Focused (populated); Hovered (populated)_

### Outlined text field measurements

![A diagram showing layout values and paddings for outlined text fields without icons. ](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Flyqys303-13.png?alt=media&token=76e0ea27-3e53-4a3e-bf9d-50d6b1f67a2d>)

_Padding and size measurements without icons_

![A diagram showing layout values and paddings for outlined text fields with leading and trailing icons. ](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Flyqyuu9g-14.png?alt=media&token=84d6a030-ea94-4790-b658-cc477664a70c>)

_Padding and size measurements with icons_

![A diagram showing layout values and paddings for supporting text, and supporting text in combination with a character count. ](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Flyqywif5-15.png?alt=media&token=91409e82-1dbb-45a6-869d-f024ee50f193>)

_Padding and size measurements with supporting text and character count_

| Attribute | Value |
| --- | --- |
| Container height | 56dp |
| Left/right padding without icons | 16dp |
| Left/right padding with icons | 12dp |
| Padding between icons and text | 16dp |
| Icon alignment | Vertically centered |
| Supporting text and character counter top padding | 4dp |
| Padding between supporting text and character counter | 16dp |
| Label alignment | Vertically centered |
| Left/right padding populated label text | 4dp |
| Target size | 56dp |

### Outlined text field configurations

![A side by side view of outlined text fields in different configurations.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fm84gq17k-16%20-%203P.png?alt=media&token=eefd975a-84d8-4f4b-9cc8-9707e31f383f>)

_Empty and populated outlined text fields with: Supporting text; Trailing icon; Leading icon; Leading and trailing icons; Prefix; Suffix; Multi-line text field_

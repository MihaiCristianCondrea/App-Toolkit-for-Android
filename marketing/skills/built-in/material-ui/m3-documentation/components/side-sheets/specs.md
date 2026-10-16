---
title: "Side sheets"
source_url: "https://m3.material.io/components/side-sheets/specs"
lastmod: "2026-05-18"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "components"
assets:
  - "assets/images/865a0fa70296-mgam1q2e-01-3P.png"
  - "assets/images/d6430f52d294-mgam3xno-02-3P.png"
  - "assets/images/5acea70465c3-mgam4wao-03-3P.png"
  - "assets/images/18f79793e9d2-mgam6adp-04-3P.png"
  - "assets/images/f90400885ae6-mgam8seo-05-3P.png"
  - "assets/images/941cfbc27c94-mgamb5rx-06-3P.png"
---

# Side sheets

Side sheets show secondary content anchored to the side of the screen

## Specs

## Tokens & specs

Browse the component elements, attributes, tokens, and their values. [Learn more about design tokens](../../foundations/design-tokens/overview.md)

### TOKEN_TABLE

Component: Side sheets

Configured context tags: None, Medium contrast, Default, High contrast, Static, 3P, Android, Dark, Light

#### Token sets

| Token set | Name | Type | Tokens | Description |
| --- | --- | --- | --- | --- |
| Sheets - Side | md.comp.sheet.side | COMPONENT | 31 | For side sheets. |

#### Sheets - Side (md.comp.sheet.side)

For side sheets.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.sheet.side.docked.container.color | COLOR | Enabled / Container | `md.sys.color.surface` |  | Deprecated as part of the update from opacity based surfaces to tonal surfaces. Use “md.comp.sheet.side.docked.modal.container.color” or “md.comp.sheet.side.docked.standard.container.color” depending on intended configuration. |
| md.comp.sheet.side.docked.modal.container.color | COLOR | Enabled / Container | `md.sys.color.surface-container-low` |  |  |
| md.comp.sheet.side.docked.standard.container.color | COLOR | Enabled / Container | `md.sys.color.surface` |  |  |
| md.comp.sheet.side.docked.container.surface-tint-layer.color | COLOR | Enabled / Container | `md.sys.color.surface-tint` |  | Deprecated as part of the update from opacity based surfaces to tonal surfaces. Surfaces no longer use surface-tint layers for tinting, please use the desired surface role directly as the container color. |
| md.comp.sheet.side.docked.modal.container.elevation | ELEVATION | Enabled / Container | `md.sys.elevation.level1` |  |  |
| md.comp.sheet.side.docked.standard.container.elevation | ELEVATION | Enabled / Container | `md.sys.elevation.level0` |  |  |
| md.comp.sheet.side.docked.container.height | LENGTH | Enabled / Container | 100 percent |  |  |
| md.comp.sheet.side.docked.container.shape | SHAPE | Enabled / Container | `md.sys.shape.corner.none` |  |  |
| md.comp.sheet.side.detached.container.shape | SHAPE | Enabled / Container | `md.sys.shape.corner.large` |  |  |
| md.comp.sheet.side.docked.container.width | LENGTH | Enabled / Container | 256dp |  |  |
| md.comp.sheet.side.docked.modal.container.shape | SHAPE | Enabled / Container | `md.sys.shape.corner.large.start` |  |  |
| md.comp.sheet.side.docked.headline.color | COLOR | Enabled / Headline | `md.sys.color.on-surface-variant` |  |  |
| md.comp.sheet.side.docked.headline.font | FONT_NAMES | Enabled / Headline | `md.sys.typescale.title-large.font` |  |  |
| md.comp.sheet.side.docked.headline.line-height | LINE_HEIGHT | Enabled / Headline | `md.sys.typescale.title-large.line-height` |  |  |
| md.comp.sheet.side.docked.headline.size | FONT_SIZE | Enabled / Headline | `md.sys.typescale.title-large.size` |  |  |
| md.comp.sheet.side.docked.headline.tracking | FONT_TRACKING | Enabled / Headline | `md.sys.typescale.title-large.tracking` |  |  |
| md.comp.sheet.side.docked.headline.weight | FONT_WEIGHT | Enabled / Headline | `md.sys.typescale.title-large.weight` |  |  |
| md.comp.sheet.side.docked.headline.type | TYPOGRAPHY | Enabled / Headline | font name: `md.comp.sheet.side.docked.headline.font`, font weight: `md.comp.sheet.side.docked.headline.weight`, font size: `md.comp.sheet.side.docked.headline.size`, font tracking: `md.comp.sheet.side.docked.headline.tracking`, line height: `md.comp.sheet.side.docked.headline.line-height` |  |  |
| md.comp.sheet.side.docked.divider.color | COLOR | Enabled / Divider | `md.sys.color.outline` |  |  |
| md.comp.sheet.side.docked.action.hover.label-text.color | COLOR | Hovered / Label text | `md.sys.color.primary` |  |  |
| md.comp.sheet.side.docked.action.hover.state-layer.color | COLOR | Hovered / State layer | `md.sys.color.primary` |  |  |
| md.comp.sheet.side.docked.action.hover.state-layer.opacity | OPACITY | Hovered / State layer | `md.sys.state.hover.state-layer-opacity` |  |  |
| md.comp.sheet.side.docked.focus.indicator.color | COLOR | Focused / Focus indicator | `md.sys.color.secondary` |  |  |
| md.comp.sheet.side.docked.focus.indicator.thickness | LENGTH | Focused / Focus indicator | `md.sys.state.focus-indicator.thickness` |  |  |
| md.comp.sheet.side.docked.focus.indicator.outline.offset | LENGTH | Focused / Focus indicator | `md.sys.state.focus-indicator.outer-offset` |  |  |
| md.comp.sheet.side.docked.action.focus.label-text.color | COLOR | Focused / Label text | `md.sys.color.primary` |  |  |
| md.comp.sheet.side.docked.action.focus.state-layer.color | COLOR | Focused / State layer | `md.sys.color.primary` |  |  |
| md.comp.sheet.side.docked.action.focus.state-layer.opacity | OPACITY | Focused / State layer | `md.sys.state.focus.state-layer-opacity` |  |  |
| md.comp.sheet.side.docked.action.pressed.label-text.color | COLOR | Pressed (ripple) / Label text | `md.sys.color.primary` |  |  |
| md.comp.sheet.side.docked.action.pressed.state-layer.color | COLOR | Pressed (ripple) / State layer | `md.sys.color.primary` |  |  |
| md.comp.sheet.side.docked.action.pressed.state-layer.opacity | OPACITY | Pressed (ripple) / State layer | `md.sys.state.pressed.state-layer-opacity` |  |  |

## Standard side sheet

![4 elements of a standard side sheet.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmgam1q2e-01-3P.png?alt=media&token=c481dd60-266a-4f45-b3e7-c5148f52d7a4>)

_Divider (optional); Headline; Container; Close icon button_

### Standard side sheet color

Color values are implemented through design tokens. For design, this means working with color values that correspond with tokens. For implementation, a color value will be a token that references a value. [Learn more about design tokens](https://m3.material.io/m3/pages/design-tokens/overview/)

![4 color roles applied to a side sheet in light and dark themes.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmgam3xno-02-3P.png?alt=media&token=a014f6d6-2b5c-4b1b-a365-35f44ff94849>)

_Side sheet color roles used for light and dark themes: Outline variant; On surface variant; Surface; On surface variant_

### Standard side sheet measurements

![Standard side sheet padding and size measurements.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmgam4wao-03-3P.png?alt=media&token=9ad396a2-516e-4f81-8ebd-00351e542220>)

_Side sheet padding and size measurements_

| Attribute | Value |
| --- | --- |
| Start/end padding | 24dp |
| Padding between top elements | 12dp |
| Bottom actions height | 72dp |
| Bottom actions top padding | 16dp |
| Bottom actions bottom padding | 24dp |
| Bottom actions alignment (horizontal) | Left |
| Max-width | 400dp |
| Margins (when detached) | 16dp |

## Modal side sheet

![7 elements of a modal side sheet.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmgam6adp-04-3P.png?alt=media&token=9b5b4904-7ee2-4e28-b834-2d91602c1217>)

_Back icon button (optional); Headline; Container; Close icon button; Divider (optional); Action buttons (optional); Scrim_

### Modal side sheet color

Color values are implemented through design tokens. For design, this means working with color values that correspond with tokens. For implementation, a color value will be a token that references a value. [Learn more about design tokens](https://m3.material.io/m3/pages/design-tokens/overview/).

![4 color roles applied to a modal side sheet in light and dark themes.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmgam8seo-05-3P.png?alt=media&token=f1de3f6f-2bb5-4788-9b42-7cce55f744b7>)

_Side sheet color roles used for light and dark themes: On surface variant; On surface variant; Surface container low; On surface variant_

### Modal side sheet measurements

![Modal side sheet padding and size measurements](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmgamb5rx-06-3P.png?alt=media&token=882e119b-eae4-437b-9cdb-5fe69888fa1a>)

_Modal side sheet padding and size measurements_

| Attribute | Value |
| --- | --- |
| Start/end padding | 24dp |
| Start padding with icon | 16dp |
| Padding between top elements | 12dp |
| Bottom actions height | 72dp |
| Bottom actions top padding | 16dp |
| Bottom actions bottom padding | 24dp |
| Bottom actions alignment (horizontal) | Left |
| Max-width | 400dp |
| Margins (when detached) | 16dp |

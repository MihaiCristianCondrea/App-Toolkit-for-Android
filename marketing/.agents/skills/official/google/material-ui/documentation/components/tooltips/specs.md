---
title: "Tooltips"
source_url: "https://m3.material.io/components/tooltips/specs"
lastmod: "2026-01-30"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "components"
assets:
  - "assets/images/6317ea202a25-mg6beegk-01-3P.png"
  - "assets/images/91ab4f2fd776-mg6bfggt-02-3P.png"
  - "assets/images/4de82dc58f1d-mg6bgjww-03-3P.png"
  - "assets/images/730d115a0d28-mg6bgxay-04-3P.png"
  - "assets/images/51b8923b7d3f-mg6bi2oc-05-3P.png"
  - "assets/images/60ec4c031fed-mg6big7s-06-3P.png"
  - "assets/images/70add244d28c-mg6biuoq-07-3P.png"
---

# Tooltips

Tooltips display brief labels or messages

## Specs

## Tokens & specs

Select a component variant below to see its attributes, tokens, and values.

### TOKEN_TABLE

Component: Tooltips

Configured context tags: None, Medium contrast, Default, High contrast, Static, 3P, Android, Dark, Light

#### Token sets

| Token set | Name | Type | Tokens | Description |
| --- | --- | --- | --- | --- |
| Tooltip - Plain | md.comp.plain-tooltip | COMPONENT | 9 | For plain tooltips. |
| Tooltip - Rich | md.comp.rich-tooltip | COMPONENT | 35 | For rich tooltips. |

#### Tooltip - Plain (md.comp.plain-tooltip)

For plain tooltips.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.plain-tooltip.container.color | COLOR | Enabled / Container | `md.sys.color.inverse-surface` |  |  |
| md.comp.plain-tooltip.container.shape | SHAPE | Enabled / Container | `md.sys.shape.corner.extra-small` |  |  |
| md.comp.plain-tooltip.supporting-text.font | FONT_NAMES | Enabled / Supporting text | `md.sys.typescale.body-small.font` |  |  |
| md.comp.plain-tooltip.supporting-text.line-height | LINE_HEIGHT | Enabled / Supporting text | `md.sys.typescale.body-small.line-height` |  |  |
| md.comp.plain-tooltip.supporting-text.size | FONT_SIZE | Enabled / Supporting text | `md.sys.typescale.body-small.size` |  |  |
| md.comp.plain-tooltip.supporting-text.weight | FONT_WEIGHT | Enabled / Supporting text | `md.sys.typescale.body-small.weight` |  |  |
| md.comp.plain-tooltip.supporting-text.tracking | FONT_TRACKING | Enabled / Supporting text | `md.sys.typescale.body-small.tracking` |  |  |
| md.comp.plain-tooltip.supporting-text.type | TYPOGRAPHY | Enabled / Supporting text | font name: `md.comp.plain-tooltip.supporting-text.font`, font weight: `md.comp.plain-tooltip.supporting-text.weight`, font size: `md.comp.plain-tooltip.supporting-text.size`, font tracking: `md.comp.plain-tooltip.supporting-text.tracking`, line height: `md.comp.plain-tooltip.supporting-text.line-height` |  |  |
| md.comp.plain-tooltip.supporting-text.color | COLOR | Enabled / Supporting text | `md.sys.color.inverse-on-surface` |  |  |

#### Tooltip - Rich (md.comp.rich-tooltip)

For rich tooltips.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.rich-tooltip.container.color | COLOR | Enabled / Container | `md.sys.color.surface-container` |  |  |
| md.comp.rich-tooltip.container.elevation | ELEVATION | Enabled / Container | `md.sys.elevation.level2` |  |  |
| md.comp.rich-tooltip.container.surface-tint-layer.color | COLOR | Enabled / Container | `md.sys.color.surface-tint` |  | Deprecated as part of the update from opacity based surfaces to tonal surfaces. Surfaces no longer use surface-tint layers for tinting, please use the desired surface role directly as the container color. |
| md.comp.rich-tooltip.container.shadow-color | COLOR | Enabled / Container | `md.sys.color.shadow` |  |  |
| md.comp.rich-tooltip.container.shape | SHAPE | Enabled / Container | `md.sys.shape.corner.medium` |  |  |
| md.comp.rich-tooltip.action.label-text.font | FONT_NAMES | Enabled / Label text | `md.sys.typescale.label-large.font` |  |  |
| md.comp.rich-tooltip.action.label-text.line-height | LINE_HEIGHT | Enabled / Label text | `md.sys.typescale.label-large.line-height` |  |  |
| md.comp.rich-tooltip.action.label-text.size | FONT_SIZE | Enabled / Label text | `md.sys.typescale.label-large.size` |  |  |
| md.comp.rich-tooltip.action.label-text.weight | FONT_WEIGHT | Enabled / Label text | `md.sys.typescale.label-large.weight` |  |  |
| md.comp.rich-tooltip.action.label-text.tracking | FONT_TRACKING | Enabled / Label text | `md.sys.typescale.label-large.tracking` |  |  |
| md.comp.rich-tooltip.action.label-text.type | TYPOGRAPHY | Enabled / Label text | font name: `md.comp.rich-tooltip.action.label-text.font`, font weight: `md.comp.rich-tooltip.action.label-text.weight`, font size: `md.comp.rich-tooltip.action.label-text.size`, font tracking: `md.comp.rich-tooltip.action.label-text.tracking`, line height: `md.comp.rich-tooltip.action.label-text.line-height` |  |  |
| md.comp.rich-tooltip.action.label-text.color | COLOR | Enabled / Label text | `md.sys.color.primary` |  |  |
| md.comp.rich-tooltip.subhead.font | FONT_NAMES | Enabled / Subhead | `md.sys.typescale.title-small.font` |  |  |
| md.comp.rich-tooltip.subhead.line-height | LINE_HEIGHT | Enabled / Subhead | `md.sys.typescale.title-small.line-height` |  |  |
| md.comp.rich-tooltip.subhead.size | FONT_SIZE | Enabled / Subhead | `md.sys.typescale.title-small.size` |  |  |
| md.comp.rich-tooltip.subhead.weight | FONT_WEIGHT | Enabled / Subhead | `md.sys.typescale.title-small.weight` |  |  |
| md.comp.rich-tooltip.subhead.tracking | FONT_TRACKING | Enabled / Subhead | `md.sys.typescale.title-small.tracking` |  |  |
| md.comp.rich-tooltip.subhead.type | TYPOGRAPHY | Enabled / Subhead | font name: `md.comp.rich-tooltip.subhead.font`, font weight: `md.comp.rich-tooltip.subhead.weight`, font size: `md.comp.rich-tooltip.subhead.size`, font tracking: `md.comp.rich-tooltip.subhead.tracking`, line height: `md.comp.rich-tooltip.subhead.line-height` |  |  |
| md.comp.rich-tooltip.subhead.color | COLOR | Enabled / Subhead | `md.sys.color.on-surface-variant` |  |  |
| md.comp.rich-tooltip.supporting-text.font | FONT_NAMES | Enabled / Supporting text | `md.sys.typescale.body-medium.font` |  |  |
| md.comp.rich-tooltip.supporting-text.line-height | LINE_HEIGHT | Enabled / Supporting text | `md.sys.typescale.body-medium.line-height` |  |  |
| md.comp.rich-tooltip.supporting-text.size | FONT_SIZE | Enabled / Supporting text | `md.sys.typescale.body-medium.size` |  |  |
| md.comp.rich-tooltip.supporting-text.weight | FONT_WEIGHT | Enabled / Supporting text | `md.sys.typescale.body-medium.weight` |  |  |
| md.comp.rich-tooltip.supporting-text.tracking | FONT_TRACKING | Enabled / Supporting text | `md.sys.typescale.body-medium.tracking` |  |  |
| md.comp.rich-tooltip.supporting-text.type | TYPOGRAPHY | Enabled / Supporting text | font name: `md.comp.rich-tooltip.supporting-text.font`, font weight: `md.comp.rich-tooltip.supporting-text.weight`, font size: `md.comp.rich-tooltip.supporting-text.size`, font tracking: `md.comp.rich-tooltip.supporting-text.tracking`, line height: `md.comp.rich-tooltip.supporting-text.line-height` |  |  |
| md.comp.rich-tooltip.supporting-text.color | COLOR | Enabled / Supporting text | `md.sys.color.on-surface-variant` |  |  |
| md.comp.rich-tooltip.action.hover.label-text.color | COLOR | Hovered / Label text | `md.sys.color.primary` |  |  |
| md.comp.rich-tooltip.action.hover.state-layer.color | COLOR | Hovered / State layer | `md.sys.color.primary` |  |  |
| md.comp.rich-tooltip.action.hover.state-layer.opacity | OPACITY | Hovered / State layer | `md.sys.state.hover.state-layer-opacity` |  |  |
| md.comp.rich-tooltip.action.focus.label-text.color | COLOR | Focused / Label text | `md.sys.color.primary` |  |  |
| md.comp.rich-tooltip.action.focus.state-layer.color | COLOR | Focused / State layer | `md.sys.color.primary` |  |  |
| md.comp.rich-tooltip.action.focus.state-layer.opacity | OPACITY | Focused / State layer | `md.sys.state.focus.state-layer-opacity` |  |  |
| md.comp.rich-tooltip.action.pressed.label-text.color | COLOR | Pressed (ripple) / Label text | `md.sys.color.primary` |  |  |
| md.comp.rich-tooltip.action.pressed.state-layer.color | COLOR | Pressed (ripple) / State layer | `md.sys.color.primary` |  |  |
| md.comp.rich-tooltip.action.pressed.state-layer.opacity | OPACITY | Pressed (ripple) / State layer | `md.sys.state.pressed.state-layer-opacity` |  |  |

## Plain tooltip

![2 elements of a plain tooltip.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmg6beegk-01-3P.png?alt=media&token=bc9cfc37-50ed-4dc0-a792-31616c3b822e>)

_Supporting text; Container_

### Plain tooltip colors

Color values are implemented through design tokens. For design, this means working with color values that correspond with tokens. For implementation, a color value will be a token that references a value. [Learn more about design tokens](../../foundations/design-tokens/overview.md)

![2 color roles of a plain tooltip.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmg6bfggt-02-3P.png?alt=media&token=77ecaad6-24fd-4b5b-83c6-31d409422518>)

_Plain tooltip color roles used for light and dark themes: Inverse on surface; Inverse surface_

### Plain tooltip measurements

![Measurements of a plain tooltip.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmg6bgjww-03-3P.png?alt=media&token=9c29e9c5-7681-4a6a-b926-d0120e71350a>)

_Plain tooltip padding and size measurements_

| Attribute | Value |
| --- | --- |
| Container height | 24dp |
| Padding | 8dp |

## Rich tooltip

![4 elements of a rich tooltip.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmg6bgxay-04-3P.png?alt=media&token=46a4b568-3a02-4814-8411-95ef51193943>)

_Subhead; Container; Supporting text; Text button_

### Rich tooltip colors

Color values are implemented through design tokens. For design, this means working with color values that correspond with tokens. For implementation, a color value will be a token that references a value. [Learn more about design tokens](../../foundations/design-tokens/overview.md)

![4 color roles of a rich tooltip.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmg6bi2oc-05-3P.png?alt=media&token=dbe6be0c-aca7-490d-a39f-cca377a2fbd2>)

_Rich tooltip color roles used for light and dark themes: On surface variant; Surface container; On surface variant; Primary_

### Rich tooltip measurements

![Measurements of a rich tooltip.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmg6big7s-06-3P.png?alt=media&token=2a5f5dbd-2798-4a2f-9d93-f1a7f23575fa>)

_Rich tooltip padding and size measurements_

| Attribute | Value |
| --- | --- |
| Top padding | 12dp |
| Bottom padding | 8dp |
| Left and right padding | 16dp |

### Rich tooltip configurations

Rich tooltips can have a headline, body, and up to two buttons. The headline and number of buttons can be configured.

![5 common configurations of a rich tooltip.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmg6biuoq-07-3P.png?alt=media&token=0c5bd1c9-b9d9-4ee7-b940-80f5ed6189cc>)

_Subhead, supporting text, and two buttons; Subhead, supporting text, and one button; Subhead and supporting text; Supporting text and one button; Supporting text and two buttons_

---
title: "Snackbar"
source_url: "https://m3.material.io/components/snackbar/specs"
lastmod: "2026-05-18"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "components"
assets:
  - "assets/images/39ac29782370-mkvpjhzt-01-3P.png"
  - "assets/images/8290b59d221d-lxlhysf9-2.png"
  - "assets/images/6f71be86335a-m8otc9es-3-3P.png"
  - "assets/images/b601f855e3f9-lxli48dd-5.png"
---

# Snackbar

Snackbars show short updates about app processes at the bottom of the screen

## Specs

![Diagram of snackbar indicating the four parts of its anatomy](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmkvpjhzt-01-3P.png?alt=media&token=6b576463-6ebb-4dce-8b7b-ec988f189091>)

_Container; Icon (optional close affordance); Action (optional); Supporting text_

## Tokens and specs

Browse the component elements, attributes, tokens, and their values. [Learn more about design tokens](../../foundations/design-tokens/overview.md)

### TOKEN_TABLE

Component: Snackbar

Configured context tags: None, Medium contrast, Default, High contrast, Static, 3P, Android, Dark, Light

#### Token sets

| Token set | Name | Type | Tokens | Description |
| --- | --- | --- | --- | --- |
| Snackbars | md.comp.snackbar | COMPONENT | 40 | For snackbars. |

#### Snackbars (md.comp.snackbar)

For snackbars.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.snackbar.container.color | COLOR | Enabled / Container | `md.sys.color.inverse-surface` |  |  |
| md.comp.snackbar.container.shadow-color | COLOR | Enabled / Container | `md.sys.color.shadow` |  |  |
| md.comp.snackbar.container.elevation | ELEVATION | Enabled / Container | `md.sys.elevation.level3` |  |  |
| md.comp.snackbar.container.shape | SHAPE | Enabled / Container | `md.sys.shape.corner.extra-small` |  |  |
| md.comp.snackbar.with-single-line.container.height | LENGTH | Enabled / Container | 48dp |  |  |
| md.comp.snackbar.with-two-lines.container.height | LENGTH | Enabled / Container | 68dp |  |  |
| md.comp.snackbar.action.label-text.type | TYPOGRAPHY | Enabled / Label text | font name: `md.comp.snackbar.action.label-text.font`, font weight: `md.comp.snackbar.action.label-text.weight`, font size: `md.comp.snackbar.action.label-text.size`, font tracking: `md.comp.snackbar.action.label-text.tracking`, line height: `md.comp.snackbar.action.label-text.line-height` |  |  |
| md.comp.snackbar.action.label-text.color | COLOR | Enabled / Label text | `md.sys.color.inverse-primary` |  |  |
| md.comp.snackbar.action.label-text.font | FONT_NAMES | Enabled / Label text | `md.sys.typescale.label-large.font` |  |  |
| md.comp.snackbar.action.label-text.line-height | LINE_HEIGHT | Enabled / Label text | `md.sys.typescale.label-large.line-height` |  |  |
| md.comp.snackbar.action.label-text.size | FONT_SIZE | Enabled / Label text | `md.sys.typescale.label-large.size` |  |  |
| md.comp.snackbar.action.label-text.tracking | FONT_TRACKING | Enabled / Label text | `md.sys.typescale.label-large.tracking` |  |  |
| md.comp.snackbar.action.label-text.weight | FONT_WEIGHT | Enabled / Label text | `md.sys.typescale.label-large.weight` |  |  |
| md.comp.snackbar.icon.color | COLOR | Enabled / Icon | `md.sys.color.inverse-on-surface` |  |  |
| md.comp.snackbar.icon.size | LENGTH | Enabled / Icon | 24dp |  |  |
| md.comp.snackbar.supporting-text.type | TYPOGRAPHY | Enabled / Supporting text | font name: `md.comp.snackbar.supporting-text.font`, font weight: `md.comp.snackbar.supporting-text.weight`, font size: `md.comp.snackbar.supporting-text.size`, font tracking: `md.comp.snackbar.supporting-text.tracking`, line height: `md.comp.snackbar.supporting-text.line-height` |  |  |
| md.comp.snackbar.supporting-text.color | COLOR | Enabled / Supporting text | `md.sys.color.inverse-on-surface` |  |  |
| md.comp.snackbar.supporting-text.font | FONT_NAMES | Enabled / Supporting text | `md.sys.typescale.body-medium.font` |  |  |
| md.comp.snackbar.supporting-text.line-height | LINE_HEIGHT | Enabled / Supporting text | `md.sys.typescale.body-medium.line-height` |  |  |
| md.comp.snackbar.supporting-text.size | FONT_SIZE | Enabled / Supporting text | `md.sys.typescale.body-medium.size` |  |  |
| md.comp.snackbar.supporting-text.tracking | FONT_TRACKING | Enabled / Supporting text | `md.sys.typescale.body-medium.tracking` |  |  |
| md.comp.snackbar.supporting-text.weight | FONT_WEIGHT | Enabled / Supporting text | `md.sys.typescale.body-medium.weight` |  |  |
| md.comp.snackbar.action.hover.label-text.color | COLOR | Hovered / Label text | `md.sys.color.inverse-primary` |  |  |
| md.comp.snackbar.action.hover.state-layer.color | COLOR | Hovered / State layer | `md.sys.color.inverse-primary` |  |  |
| md.comp.snackbar.action.hover.state-layer.opacity | OPACITY | Hovered / State layer | `md.sys.state.hover.state-layer-opacity` |  |  |
| md.comp.snackbar.icon.hover.state-layer.color | COLOR | Hovered / State layer | `md.sys.color.inverse-on-surface` |  |  |
| md.comp.snackbar.icon.hover.state-layer.opacity | OPACITY | Hovered / State layer | `md.sys.state.hover.state-layer-opacity` |  |  |
| md.comp.snackbar.icon.hover.icon.color | COLOR | Hovered / Icon | `md.sys.color.inverse-on-surface` |  |  |
| md.comp.snackbar.action.focus.label-text.color | COLOR | Focused / Label text | `md.sys.color.inverse-primary` |  |  |
| md.comp.snackbar.action.focus.state-layer.color | COLOR | Focused / State layer | `md.sys.color.inverse-primary` |  |  |
| md.comp.snackbar.action.focus.state-layer.opacity | OPACITY | Focused / State layer | `md.sys.state.focus.state-layer-opacity` |  |  |
| md.comp.snackbar.icon.focus.state-layer.color | COLOR | Focused / State layer | `md.sys.color.inverse-on-surface` |  |  |
| md.comp.snackbar.icon.focus.state-layer.opacity | OPACITY | Focused / State layer | `md.sys.state.focus.state-layer-opacity` |  |  |
| md.comp.snackbar.icon.focus.icon.color | COLOR | Focused / Icon | `md.sys.color.inverse-on-surface` |  |  |
| md.comp.snackbar.action.pressed.label-text.color | COLOR | Pressed (ripple) / Label text | `md.sys.color.inverse-primary` |  |  |
| md.comp.snackbar.action.pressed.state-layer.color | COLOR | Pressed (ripple) / State layer | `md.sys.color.inverse-primary` |  |  |
| md.comp.snackbar.action.pressed.state-layer.opacity | OPACITY | Pressed (ripple) / State layer | `md.sys.state.pressed.state-layer-opacity` |  |  |
| md.comp.snackbar.icon.pressed.state-layer.color | COLOR | Pressed (ripple) / State layer | `md.sys.color.inverse-on-surface` |  |  |
| md.comp.snackbar.icon.pressed.state-layer.opacity | OPACITY | Pressed (ripple) / State layer | `md.sys.state.pressed.state-layer-opacity` |  |  |
| md.comp.snackbar.icon.pressed.icon.color | COLOR | Pressed (ripple) / Icon | `md.sys.color.inverse-on-surface` |  |  |

## Color

Color values are implemented through design tokens. For design, this means working with color values that correspond with tokens. For implementation, a color value will be a token that references a value. [Learn more about design tokens](../../foundations/design-tokens/overview.md)

![Diagram of snackbar indicating color and inverse text and labels](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Flxlhysf9-2.png?alt=media&token=bdd60717-341a-4897-92a8-f69e82d01678>)

_Snackbar color roles used for light and dark schemes: Inverse surface; Inverse on surface; Inverse primary; Inverse on surface_

## Measurements

![Diagram of snackbar with action](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fm8otc9es-3-3P.png?alt=media&token=4f3390bd-7907-45b6-b9ac-638802fca181>)

_Snackbar padding and size measurements_

## Configurations

![5 snackbar configurations.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Flxli48dd-5.png?alt=media&token=f82b1a9e-b006-4266-983f-78f956d8b70a>)

_Single line; Single line with action; Two lines; Two lines with action; Two lines with longer action_

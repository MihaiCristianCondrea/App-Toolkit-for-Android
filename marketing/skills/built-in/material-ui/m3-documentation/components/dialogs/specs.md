---
title: "Dialogs"
source_url: "https://m3.material.io/components/dialogs/specs"
lastmod: "2026-05-18"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "components"
assets:
  - "assets/images/254aa0bf2934-mk29t71i-01-3P.png"
  - "assets/images/3fc776dec363-ml9yledn-02-3P.png"
  - "assets/images/a24212459066-ml9ynrkv-03-3P.png"
  - "assets/images/00b0a551f2f7-m9h7yor0-04-3P.png"
  - "assets/images/aadd1307d8b4-m9h7z23b-05-3P.png"
  - "assets/images/dac332b3a714-mafq2x4i-06-3P.png"
---

# Dialogs

Dialogs provide important prompts in a user flow

## Specs

## Tokens & specs

Select a component variant below to see its elements, attributes, tokens, and their values.

### TOKEN_TABLE

Component: Dialogs

Configured context tags: None, Medium contrast, Default, High contrast, Static, 3P, Android, Dark, Light

#### Token sets

| Token set | Name | Type | Tokens | Description |
| --- | --- | --- | --- | --- |
| Dialog - Full screen | md.comp.full-screen-dialog | COMPONENT | 56 | For full-screen dialogs. |
| Dialog - Basic | md.comp.dialog | COMPONENT | 45 | For basic dialogs. |

#### Dialog - Full screen (md.comp.full-screen-dialog)

For full-screen dialogs.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.full-screen-dialog.header.on-scroll.container.color | COLOR | Enabled / Container | `md.sys.color.surface-container` |  |  |
| md.comp.full-screen-dialog.container.color | COLOR | Enabled / Container | `md.sys.color.surface` |  |  |
| md.comp.full-screen-dialog.container.elevation | ELEVATION | Enabled / Container | `md.sys.elevation.level0` |  |  |
| md.comp.full-screen-dialog.container.shape | SHAPE | Enabled / Container | `md.sys.shape.corner.none` |  |  |
| md.comp.full-screen-dialog.header.container.height | LENGTH | Enabled / Container | 56dp |  |  |
| md.comp.full-screen-dialog.header.container.color | COLOR | Enabled / Container | `md.sys.color.surface` |  |  |
| md.comp.full-screen-dialog.header.container.elevation | ELEVATION | Enabled / Container | `md.sys.elevation.level0` |  |  |
| md.comp.full-screen-dialog.header.container.surface-tint-layer.color | COLOR | Enabled / Container | `md.sys.color.surface-tint` |  | Deprecated as part of the update from opacity based surfaces to tonal surfaces. Surfaces no longer use surface-tint layers for tinting, please use the desired surface role directly as the container color. |
| md.comp.full-screen-dialog.header.on-scroll.container.elevation | ELEVATION | Enabled / Container | `md.sys.elevation.level2` |  |  |
| md.comp.full-screen-dialog.action-bar.container.height | LENGTH | Enabled / Container | 56dp |  | Bug: should not have been created. If needed, copy to your own tokens. |
| md.comp.full-screen-dialog.action-bar.container.color | COLOR | Enabled / Container | `md.sys.color.surface` |  | Bug: should not have been created. If needed, copy to your own tokens. |
| md.comp.full-screen-dialog.action-bar.container.elevation | ELEVATION | Enabled / Container | `md.sys.elevation.level0` |  | Bug: should not have been created. If needed, copy to your own tokens. |
| md.comp.full-screen-dialog.action-bar.on-scroll.container.elevation | ELEVATION | Enabled / Container | `md.sys.elevation.level2` |  | Bug: should not have been created. If needed, copy to your own tokens. |
| md.comp.full-screen-dialog.header.action.label-text.font | FONT_NAMES | Enabled / Label text | `md.sys.typescale.label-large.font` |  |  |
| md.comp.full-screen-dialog.header.action.label-text.line-height | LINE_HEIGHT | Enabled / Label text | `md.sys.typescale.label-large.line-height` |  |  |
| md.comp.full-screen-dialog.header.action.label-text.size | FONT_SIZE | Enabled / Label text | `md.sys.typescale.label-large.size` |  |  |
| md.comp.full-screen-dialog.header.action.label-text.weight | FONT_WEIGHT | Enabled / Label text | `md.sys.typescale.label-large.weight` |  |  |
| md.comp.full-screen-dialog.header.action.label-text.tracking | FONT_TRACKING | Enabled / Label text | `md.sys.typescale.label-large.tracking` |  |  |
| md.comp.full-screen-dialog.header.action.label-text.type | TYPOGRAPHY | Enabled / Label text | font name: `md.comp.full-screen-dialog.header.action.label-text.font`, font weight: `md.comp.full-screen-dialog.header.action.label-text.weight`, font size: `md.comp.full-screen-dialog.header.action.label-text.size`, font tracking: `md.comp.full-screen-dialog.header.action.label-text.tracking`, line height: `md.comp.full-screen-dialog.header.action.label-text.line-height` |  |  |
| md.comp.full-screen-dialog.header.action.label-text.color | COLOR | Enabled / Label text | `md.sys.color.primary` |  |  |
| md.comp.full-screen-dialog.action-bar.label-text.font | FONT_NAMES | Enabled / Label text | `md.sys.typescale.label-large.font` |  | Bug: should not have been created. If needed, copy to your own tokens. |
| md.comp.full-screen-dialog.action-bar.label-text.line-height | LINE_HEIGHT | Enabled / Label text | `md.sys.typescale.label-large.line-height` |  | Bug: should not have been created. If needed, copy to your own tokens. |
| md.comp.full-screen-dialog.action-bar.label-text.size | FONT_SIZE | Enabled / Label text | `md.sys.typescale.label-large.size` |  | Bug: should not have been created. If needed, copy to your own tokens. |
| md.comp.full-screen-dialog.action-bar.label-text.weight | FONT_WEIGHT | Enabled / Label text | `md.sys.typescale.label-large.weight` |  | Bug: should not have been created. If needed, copy to your own tokens. |
| md.comp.full-screen-dialog.action-bar.label-text.tracking | FONT_TRACKING | Enabled / Label text | `md.sys.typescale.label-large.tracking` |  | Bug: should not have been created. If needed, copy to your own tokens. |
| md.comp.full-screen-dialog.action-bar.label-text.type | TYPOGRAPHY | Enabled / Label text | font name: `md.comp.full-screen-dialog.action-bar.label-text.font`, font weight: `md.comp.full-screen-dialog.action-bar.label-text.weight`, font size: `md.comp.full-screen-dialog.action-bar.label-text.size`, font tracking: `md.comp.full-screen-dialog.action-bar.label-text.tracking`, line height: `md.comp.full-screen-dialog.action-bar.label-text.line-height` |  | Bug: should not have been created. If needed, copy to your own tokens. |
| md.comp.full-screen-dialog.action-bar.label-text.color | COLOR | Enabled / Label text | `md.sys.color.primary` |  | Bug: should not have been created. If needed, copy to your own tokens. |
| md.comp.full-screen-dialog.header.icon.color | COLOR | Enabled / Icon | `md.sys.color.on-surface` |  |  |
| md.comp.full-screen-dialog.header.icon.size | LENGTH | Enabled / Icon | 24dp |  |  |
| md.comp.full-screen-dialog.header.headline.color | COLOR | Enabled / Headline | `md.sys.color.on-surface` |  |  |
| md.comp.full-screen-dialog.header.headline.font | FONT_NAMES | Enabled / Headline | `md.sys.typescale.title-large.font` |  |  |
| md.comp.full-screen-dialog.header.headline.line-height | LINE_HEIGHT | Enabled / Headline | `md.sys.typescale.title-large.line-height` |  |  |
| md.comp.full-screen-dialog.header.headline.size | FONT_SIZE | Enabled / Headline | `md.sys.typescale.title-large.size` |  |  |
| md.comp.full-screen-dialog.header.headline.weight | FONT_WEIGHT | Enabled / Headline | `md.sys.typescale.title-large.weight` |  |  |
| md.comp.full-screen-dialog.header.headline.tracking | FONT_TRACKING | Enabled / Headline | `md.sys.typescale.title-large.tracking` |  |  |
| md.comp.full-screen-dialog.header.headline.type | TYPOGRAPHY | Enabled / Headline | font name: `md.comp.full-screen-dialog.header.headline.font`, font weight: `md.comp.full-screen-dialog.header.headline.weight`, font size: `md.comp.full-screen-dialog.header.headline.size`, font tracking: `md.comp.full-screen-dialog.header.headline.tracking`, line height: `md.comp.full-screen-dialog.header.headline.line-height` |  |  |
| md.comp.full-screen-dialog.with-divider.divider.color | COLOR | Enabled / Divider | `md.sys.color.surface-variant` |  | Depcrecating all divider tokens nested across components. Please use the standalone divider component token md.comp.divider.color as a replacement |
| md.comp.full-screen-dialog.with-divider.divider.height | LENGTH | Enabled / Divider | 1dp |  | Depcrecating all divider tokens nested across components. Please use the standalone divider component token md.comp.divider.thickness as a replacement |
| md.comp.full-screen-dialog.header.action.hover.label-text.color | COLOR | Hovered / Label text | `md.sys.color.primary` |  |  |
| md.comp.full-screen-dialog.action-bar.hover.label-text.color | COLOR | Hovered / Label text | `md.sys.color.primary` |  | Bug: should not have been created. If needed, copy to your own tokens. |
| md.comp.full-screen-dialog.header.action.hover.state-layer.color | COLOR | Hovered / State layer | `md.sys.color.primary` |  |  |
| md.comp.full-screen-dialog.header.action.hover.state-layer.opacity | OPACITY | Hovered / State layer | `md.sys.state.hover.state-layer-opacity` |  |  |
| md.comp.full-screen-dialog.action-bar.hover.state-layer.color | COLOR | Hovered / State layer | `md.sys.color.primary` |  | Bug: should not have been created. If needed, copy to your own tokens. |
| md.comp.full-screen-dialog.action-bar.hover.state-layer.opacity | OPACITY | Hovered / State layer | `md.sys.state.hover.state-layer-opacity` |  | Bug: should not have been created. If needed, copy to your own tokens. |
| md.comp.full-screen-dialog.header.action.focus.label-text.color | COLOR | Focused / Label text | `md.sys.color.primary` |  |  |
| md.comp.full-screen-dialog.action-bar.focus.label-text.color | COLOR | Focused / Label text | `md.sys.color.primary` |  | Bug: should not have been created. If needed, copy to your own tokens. |
| md.comp.full-screen-dialog.header.action.focus.state-layer.color | COLOR | Focused / State layer | `md.sys.color.primary` |  |  |
| md.comp.full-screen-dialog.header.action.focus.state-layer.opacity | OPACITY | Focused / State layer | `md.sys.state.focus.state-layer-opacity` |  |  |
| md.comp.full-screen-dialog.action-bar.focus.state-layer.color | COLOR | Focused / State layer | `md.sys.color.primary` |  | Bug: should not have been created. If needed, copy to your own tokens. |
| md.comp.full-screen-dialog.action-bar.focus.state-layer.opacity | OPACITY | Focused / State layer | `md.sys.state.focus.state-layer-opacity` |  | Bug: should not have been created. If needed, copy to your own tokens. |
| md.comp.full-screen-dialog.header.action.pressed.label-text.color | COLOR | Pressed (ripple) / Label text | `md.sys.color.primary` |  |  |
| md.comp.full-screen-dialog.action-bar.pressed.label-text.color | COLOR | Pressed (ripple) / Label text | `md.sys.color.primary` |  | Bug: should not have been created. If needed, copy to your own tokens. |
| md.comp.full-screen-dialog.header.action.pressed.state-layer.color | COLOR | Pressed (ripple) / State layer | `md.sys.color.primary` |  |  |
| md.comp.full-screen-dialog.header.action.pressed.state-layer.opacity | OPACITY | Pressed (ripple) / State layer | `md.sys.state.pressed.state-layer-opacity` |  |  |
| md.comp.full-screen-dialog.action-bar.pressed.state-layer.color | COLOR | Pressed (ripple) / State layer | `md.sys.color.primary` |  | Bug: should not have been created. If needed, copy to your own tokens. |
| md.comp.full-screen-dialog.action-bar.pressed.state-layer.opacity | OPACITY | Pressed (ripple) / State layer | `md.sys.state.pressed.state-layer-opacity` |  | Bug: should not have been created. If needed, copy to your own tokens. |

#### Dialog - Basic (md.comp.dialog)

For basic dialogs.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.dialog.container.color | COLOR | Enabled / Container | `md.sys.color.surface-container-high` |  |  |
| md.comp.dialog.container.elevation | ELEVATION | Enabled / Container | `md.sys.elevation.level3` |  |  |
| md.comp.dialog.container.surface-tint-layer.color | COLOR | Enabled / Container | `md.sys.color.surface-tint` |  | Deprecated as part of the update from opacity based surfaces to tonal surfaces. Surfaces no longer use surface-tint layers for tinting, please use the desired surface role directly as the container color. |
| md.comp.dialog.container.shape | SHAPE | Enabled / Container | `md.sys.shape.corner.extra-large` |  |  |
| md.comp.dialog.action.label-text.font | FONT_NAMES | Enabled / Label text | `md.sys.typescale.label-large.font` |  |  |
| md.comp.dialog.action.label-text.line-height | LINE_HEIGHT | Enabled / Label text | `md.sys.typescale.label-large.line-height` |  |  |
| md.comp.dialog.action.label-text.size | FONT_SIZE | Enabled / Label text | `md.sys.typescale.label-large.size` |  |  |
| md.comp.dialog.action.label-text.weight | FONT_WEIGHT | Enabled / Label text | `md.sys.typescale.label-large.weight` |  |  |
| md.comp.dialog.action.label-text.tracking | FONT_TRACKING | Enabled / Label text | `md.sys.typescale.label-large.tracking` |  |  |
| md.comp.dialog.action.label-text.type | TYPOGRAPHY | Enabled / Label text | font name: `md.comp.dialog.action.label-text.font`, font weight: `md.comp.dialog.action.label-text.weight`, font size: `md.comp.dialog.action.label-text.size`, font tracking: `md.comp.dialog.action.label-text.tracking`, line height: `md.comp.dialog.action.label-text.line-height` |  |  |
| md.comp.dialog.action.label-text.color | COLOR | Enabled / Label text | `md.sys.color.primary` |  |  |
| md.comp.dialog.with-icon.icon.size | LENGTH | Enabled / Icon | 24dp |  |  |
| md.comp.dialog.with-icon.icon.color | COLOR | Enabled / Icon | `md.sys.color.secondary` |  |  |
| md.comp.dialog.subhead.font | FONT_NAMES | Enabled / Subhead | `md.sys.typescale.headline-small.font` |  | Tokens deprecated to align taxonomy with full-screen dialogs. Please use md.comp.dialog.headline.… instead |
| md.comp.dialog.subhead.line-height | LINE_HEIGHT | Enabled / Subhead | `md.sys.typescale.headline-small.line-height` |  | Tokens deprecated to align taxonomy with full-screen dialogs. Please use md.comp.dialog.headline.… instead |
| md.comp.dialog.subhead.size | FONT_SIZE | Enabled / Subhead | `md.sys.typescale.headline-small.size` |  | Tokens deprecated to align taxonomy with full-screen dialogs. Please use md.comp.dialog.headline.… instead |
| md.comp.dialog.subhead.weight | FONT_WEIGHT | Enabled / Subhead | `md.sys.typescale.headline-small.weight` |  | Tokens deprecated to align taxonomy with full-screen dialogs. Please use md.comp.dialog.headline.… instead |
| md.comp.dialog.subhead.tracking | FONT_TRACKING | Enabled / Subhead | `md.sys.typescale.headline-small.tracking` |  | Tokens deprecated to align taxonomy with full-screen dialogs. Please use md.comp.dialog.headline.… instead |
| md.comp.dialog.subhead.type | TYPOGRAPHY | Enabled / Subhead | font name: `md.comp.dialog.subhead.font`, font weight: `md.comp.dialog.subhead.weight`, font size: `md.comp.dialog.subhead.size`, font tracking: `md.comp.dialog.subhead.tracking`, line height: `md.comp.dialog.subhead.line-height` |  | Tokens deprecated to align taxonomy with full-screen dialogs. Please use md.comp.dialog.headline.… instead |
| md.comp.dialog.subhead.color | COLOR | Enabled / Subhead | `md.sys.color.on-surface` |  | Tokens deprecated to align taxonomy with full-screen dialogs. Please use md.comp.dialog.headline.… instead |
| md.comp.dialog.headline.font | FONT_NAMES | Enabled / Headline | `md.sys.typescale.headline-small.font` |  |  |
| md.comp.dialog.headline.line-height | LINE_HEIGHT | Enabled / Headline | `md.sys.typescale.headline-small.line-height` |  |  |
| md.comp.dialog.headline.size | FONT_SIZE | Enabled / Headline | `md.sys.typescale.headline-small.size` |  |  |
| md.comp.dialog.headline.weight | FONT_WEIGHT | Enabled / Headline | `md.sys.typescale.headline-small.weight` |  |  |
| md.comp.dialog.headline.tracking | FONT_TRACKING | Enabled / Headline | `md.sys.typescale.headline-small.tracking` |  |  |
| md.comp.dialog.headline.type | TYPOGRAPHY | Enabled / Headline | font name: `md.comp.dialog.headline.font`, font weight: `md.comp.dialog.headline.weight`, font size: `md.comp.dialog.headline.size`, font tracking: `md.comp.dialog.headline.tracking`, line height: `md.comp.dialog.headline.line-height` |  |  |
| md.comp.dialog.headline.color | COLOR | Enabled / Headline | `md.sys.color.on-surface` |  |  |
| md.comp.dialog.with-divider.divider.color | COLOR | Enabled / Divider | `md.sys.color.outline` |  | Depcrecating all divider tokens nested across components. Please use the standalone divider component token md.comp.divider.color as a replacement |
| md.comp.dialog.with-divider.divider.height | LENGTH | Enabled / Divider | 1dp |  | Depcrecating all divider tokens nested across components. Please use the standalone divider component token md.comp.divider.thickness as a replacement |
| md.comp.dialog.supporting-text.font | FONT_NAMES | Enabled / Supporting text | `md.sys.typescale.body-medium.font` |  |  |
| md.comp.dialog.supporting-text.line-height | LINE_HEIGHT | Enabled / Supporting text | `md.sys.typescale.body-medium.line-height` |  |  |
| md.comp.dialog.supporting-text.size | FONT_SIZE | Enabled / Supporting text | `md.sys.typescale.body-medium.size` |  |  |
| md.comp.dialog.supporting-text.weight | FONT_WEIGHT | Enabled / Supporting text | `md.sys.typescale.body-medium.weight` |  |  |
| md.comp.dialog.supporting-text.tracking | FONT_TRACKING | Enabled / Supporting text | `md.sys.typescale.body-medium.tracking` |  |  |
| md.comp.dialog.supporting-text.type | TYPOGRAPHY | Enabled / Supporting text | font name: `md.comp.dialog.supporting-text.font`, font weight: `md.comp.dialog.supporting-text.weight`, font size: `md.comp.dialog.supporting-text.size`, font tracking: `md.comp.dialog.supporting-text.tracking`, line height: `md.comp.dialog.supporting-text.line-height` |  |  |
| md.comp.dialog.supporting-text.color | COLOR | Enabled / Supporting text | `md.sys.color.on-surface-variant` |  |  |
| md.comp.dialog.action.hover.label-text.color | COLOR | Hovered / Label text | `md.sys.color.primary` |  |  |
| md.comp.dialog.action.hover.state-layer.color | COLOR | Hovered / State layer | `md.sys.color.primary` |  |  |
| md.comp.dialog.action.hover.state-layer.opacity | OPACITY | Hovered / State layer | `md.sys.state.hover.state-layer-opacity` |  |  |
| md.comp.dialog.action.focus.label-text.color | COLOR | Focused / Label text | `md.sys.color.primary` |  |  |
| md.comp.dialog.action.focus.state-layer.color | COLOR | Focused / State layer | `md.sys.color.primary` |  |  |
| md.comp.dialog.action.focus.state-layer.opacity | OPACITY | Focused / State layer | `md.sys.state.focus.state-layer-opacity` |  |  |
| md.comp.dialog.action.pressed.label-text.color | COLOR | Pressed (ripple) / Label text | `md.sys.color.primary` |  |  |
| md.comp.dialog.action.pressed.state-layer.color | COLOR | Pressed (ripple) / State layer | `md.sys.color.primary` |  |  |
| md.comp.dialog.action.pressed.state-layer.opacity | OPACITY | Pressed (ripple) / State layer | `md.sys.state.pressed.state-layer-opacity` |  |  |

## Basic dialogs

![Anatomy diagram numbering dialog elements.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmk29t71i-01-3P.png?alt=media&token=107d201b-e3d7-4dcc-aa58-71f053c9364c>)

_Container; Icon (optional); Headline (optional); Supporting text; Divider (optional); Button label text; Scrim_

### Basic dialog color

Color values are implemented through design tokens. For design, this means working with color values that correspond with tokens. For implementation, a color value will be a token that references a value. [Learn more about design tokens](../../foundations/design-tokens/overview.md)

![Color mapping diagram labeling 6 color roles across the dialog and scrim.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fml9yledn-02-3P.png?alt=media&token=d1f0e8c8-54da-41e3-81a8-ad46dcb5d3b7>)

_Basic dialog color roles used for light and dark themes: Surface container high; Secondary; On surface; On surface variant; Primary; Scrim_

### Basic dialog measurements

![Annotated diagram showing padding values.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fml9ynrkv-03-3P.png?alt=media&token=8e554b7e-0509-4f02-8563-7453e0bb40e9>)

_Basic dialog padding and size measurements_

| Attribute | Value |
| --- | --- |
| Container shape | 28dp corner radius |
| Container height | Dynamic |
| Container width | Min 280dp; Max 560dp |
| Divider height | 1dp |
| Icon size | 24dp |
| Minimum width | 280dp |
| Maximum width | 560dp |
| Alignment with icon | Center-aligned |
| Alignment without icon | Start-aligned |
| Top/Left/right/bottom padding | 24dp |
| Padding between buttons | 8dp |
| Padding between title and body | 16dp |
| Padding between icon and title | 16dp |
| Padding between body and actions | 24dp |

## Full-screen dialogs

![Diagram numbering 6 full-screen dialog elements.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fm9h7yor0-04-3P.png?alt=media&token=26b84960-cb4d-4ddc-b996-2fdc771c1432>)

_Container; Header; Icon (close affordance); Headline (optional); Text button; Divider (optional)_

### Full-screen dialog color

Color values are implemented through design tokens. For design, this means working with color values that correspond with tokens. For implementation, a color value will be a token that references a value.

![Color mapping diagram shows 5 callout markers across the dialog.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fm9h7z23b-05-3P.png?alt=media&token=0db0c4c5-bdf2-4697-964e-4d38cf508d2b>)

_Full-screen dialog color roles used for light and dark themes: Surface container high; On surface; On surface; Primary; On surface variant_

### Full-screen dialog measurements

![Diagram noting layout measurements for padding values, title, height, and action regions.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmafq2x4i-06-3P.png?alt=media&token=4cc6deb6-b269-43d4-b21a-7234ae0d2d49>)

_Full-screen dialog padding and size measurements_

| Attribute | Value |
| --- | --- |
| Container shape | 0dp corner radius |
| Container height | Dynamic |
| Container width | Container width; Max 560dp |
| Header height | 56dp |
| Header width | Container width |
| Headline text alignment | Start-aligned |
| Divider height | 1dp |
| Icon (close affordance) size | 24dp |
| Bottom action bar height | 56dp |
| Bottom action bar width | Container width |
| Top/left/right padding | 24dp |
| Padding between elements | 8dp |

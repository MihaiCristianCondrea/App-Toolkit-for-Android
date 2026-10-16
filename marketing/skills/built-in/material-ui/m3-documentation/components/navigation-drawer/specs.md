---
title: "Navigation drawer"
source_url: "https://m3.material.io/components/navigation-drawer/specs"
lastmod: "2026-05-18"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "components"
assets:
  - "assets/images/39e28301e737-mobm461s-1-3P-3p.png"
  - "assets/images/b7067955cc8a-ma6b7pyr-2-3p.png"
  - "assets/images/93478c23447b-ma77ejxi-3b-3p.png"
  - "assets/images/c9dcad271b84-mobmv0i5-4-3p.png"
  - "assets/images/6ebb2d94cd70-ma6bb7nw-5-3p.png"
  - "assets/images/5fb358b10364-ma775z3o-6-3p-3p.png"
  - "assets/images/c6f33b4d74ba-ma7769l7-7-3p-3p.png"
---

# Navigation drawer

Navigation drawers let people switch between UI views on larger devices

## Specs

star

Note:

The navigation drawer is no longer recommended in the Material 3 Expressive update. For those who have updated, use an [expanded navigation rail](https://m3.material.io/m3/pages/navigation-rail/overview/), which has mostly the same functionality of the navigation drawer and adapts better across window size classes.

![Navigation drawer diagram numbering 7 elements](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmobm461s-1-3P-3p.png?alt=media&token=c824150d-669e-4277-a2a3-4fabbfe78c87>)

_Container; Headline; Label text; Active indicator; Badge label text; Scrim; Icon_

## Tokens & specs

The navigation drawer has one token set. [Learn about design tokens](https://m3.material.io/m3/pages/design-tokens/overview/)

### TOKEN_TABLE

Component: Navigation drawer

Configured context tags: None, Medium contrast, Default, High contrast, Static, 3P, Android, Dark, Light

#### Token sets

| Token set | Name | Type | Tokens | Description |
| --- | --- | --- | --- | --- |
| Navigation drawers (baseline) | md.comp.navigation-drawer | COMPONENT | 67 | For the baseline navigation drawer. For expressive, use the expanded nav rail instead `md.comp.nav-rail.expanded`. |

#### Navigation drawers (baseline) (md.comp.navigation-drawer)

For the baseline navigation drawer. For expressive, use the expanded nav rail instead `md.comp.nav-rail.expanded`.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.navigation-drawer.container.color | COLOR | Enabled / Container | `md.sys.color.surface` |  | Deprecated as part of the update from opacity based surfaces to tonal surfaces. Use "md.comp.navigation-drawer.modal.container.color" or "md.comp.navigation-drawer.standard.container.color" instead depending on desired configuration. |
| md.comp.navigation-drawer.modal.container.color | COLOR | Enabled / Container | `md.sys.color.surface-container-low` |  |  |
| md.comp.navigation-drawer.standard.container.color | COLOR | Enabled / Container | `md.sys.color.surface` |  |  |
| md.comp.navigation-drawer.container.surface-tint-layer.color | COLOR | Enabled / Container | `md.sys.color.surface-tint` |  | Deprecated as part of the update from opacity based surfaces to tonal surfaces. Surfaces no longer use surface-tint layers for tinting, please use the desired surface role directly as the container color. |
| md.comp.navigation-drawer.modal.container.elevation | ELEVATION | Enabled / Container | `md.sys.elevation.level1` |  |  |
| md.comp.navigation-drawer.standard.container.elevation | ELEVATION | Enabled / Container | `md.sys.elevation.level0` |  |  |
| md.comp.navigation-drawer.container.height | LENGTH | Enabled / Container | 100 percent |  |  |
| md.comp.navigation-drawer.container.shape | SHAPE | Enabled / Container | `md.sys.shape.corner.large.end` |  |  |
| md.comp.navigation-drawer.bottom.container.shape | SHAPE | Enabled / Container | `md.sys.shape.corner.large.top` |  |  |
| md.comp.navigation-drawer.container.width | LENGTH | Enabled / Container | 360dp |  |  |
| md.comp.navigation-drawer.active.label-text.color | COLOR | Enabled / Label text | `md.sys.color.on-secondary-container` |  |  |
| md.comp.navigation-drawer.inactive.label-text.color | COLOR | Enabled / Label text | `md.sys.color.on-surface-variant` |  |  |
| md.comp.navigation-drawer.label-text.font | FONT_NAMES | Enabled / Label text | `md.sys.typescale.label-large.font` |  |  |
| md.comp.navigation-drawer.label-text.line-height | LINE_HEIGHT | Enabled / Label text | `md.sys.typescale.label-large.line-height` |  |  |
| md.comp.navigation-drawer.label-text.size | FONT_SIZE | Enabled / Label text | `md.sys.typescale.label-large.size` |  |  |
| md.comp.navigation-drawer.label-text.tracking | FONT_TRACKING | Enabled / Label text | `md.sys.typescale.label-large.tracking` |  |  |
| md.comp.navigation-drawer.label-text.weight | FONT_WEIGHT | Enabled / Label text | `md.sys.typescale.label-large.weight` |  |  |
| md.comp.navigation-drawer.active.label-text.weight | FONT_WEIGHT | Enabled / Label text | `md.sys.typescale.label-large.weight.prominent` |  |  |
| md.comp.navigation-drawer.label-text.type | TYPOGRAPHY | Enabled / Label text | font name: `md.comp.navigation-drawer.label-text.font`, font weight: `md.comp.navigation-drawer.label-text.weight`, font size: `md.comp.navigation-drawer.label-text.size`, font tracking: `md.comp.navigation-drawer.label-text.tracking`, line height: `md.comp.navigation-drawer.label-text.line-height` |  |  |
| md.comp.navigation-drawer.active.icon.color | COLOR | Enabled / Icon | `md.sys.color.on-secondary-container` |  |  |
| md.comp.navigation-drawer.inactive.icon.color | COLOR | Enabled / Icon | `md.sys.color.on-surface-variant` |  |  |
| md.comp.navigation-drawer.icon.size | LENGTH | Enabled / Icon | 24dp |  |  |
| md.comp.navigation-drawer.headline.color | COLOR | Enabled / Headline | `md.sys.color.on-surface-variant` |  |  |
| md.comp.navigation-drawer.headline.font | FONT_NAMES | Enabled / Headline | `md.sys.typescale.title-small.font` |  |  |
| md.comp.navigation-drawer.headline.line-height | LINE_HEIGHT | Enabled / Headline | `md.sys.typescale.title-small.line-height` |  |  |
| md.comp.navigation-drawer.headline.size | FONT_SIZE | Enabled / Headline | `md.sys.typescale.title-small.size` |  |  |
| md.comp.navigation-drawer.headline.tracking | FONT_TRACKING | Enabled / Headline | `md.sys.typescale.title-small.tracking` |  |  |
| md.comp.navigation-drawer.headline.weight | FONT_WEIGHT | Enabled / Headline | `md.sys.typescale.title-small.weight` |  |  |
| md.comp.navigation-drawer.headline.type | TYPOGRAPHY | Enabled / Headline | font name: `md.comp.navigation-drawer.headline.font`, font weight: `md.comp.navigation-drawer.headline.weight`, font size: `md.comp.navigation-drawer.headline.size`, font tracking: `md.comp.navigation-drawer.headline.tracking`, line height: `md.comp.navigation-drawer.headline.line-height` |  |  |
| md.comp.navigation-drawer.divider.color | COLOR | Enabled / Divider | `md.sys.color.outline` |  | Depcrecating all divider tokens nested across components. Please use the standalone divider component token md.comp.divider.color as a replacement |
| md.comp.navigation-drawer.active-indicator.color | COLOR | Enabled / Active indicator | `md.sys.color.secondary-container` |  |  |
| md.comp.navigation-drawer.active-indicator.height | LENGTH | Enabled / Active indicator | 56dp |  |  |
| md.comp.navigation-drawer.active-indicator.shape | SHAPE | Enabled / Active indicator | `md.sys.shape.corner.full` |  |  |
| md.comp.navigation-drawer.active-indicator.width | LENGTH | Enabled / Active indicator | 336dp |  |  |
| md.comp.navigation-drawer.large-badge-label.color | COLOR | Enabled / Large badge label | `md.sys.color.on-surface-variant` |  |  |
| md.comp.navigation-drawer.large-badge-label.font | FONT_NAMES | Enabled / Large badge label | `md.sys.typescale.label-large.font` |  |  |
| md.comp.navigation-drawer.large-badge-label.line-height | LINE_HEIGHT | Enabled / Large badge label | `md.sys.typescale.label-large.line-height` |  |  |
| md.comp.navigation-drawer.large-badge-label.size | FONT_SIZE | Enabled / Large badge label | `md.sys.typescale.label-large.size` |  |  |
| md.comp.navigation-drawer.large-badge-label.tracking | FONT_TRACKING | Enabled / Large badge label | `md.sys.typescale.label-large.tracking` |  |  |
| md.comp.navigation-drawer.large-badge-label.weight | FONT_WEIGHT | Enabled / Large badge label | `md.sys.typescale.label-large.weight` |  |  |
| md.comp.navigation-drawer.large-badge-label.type | TYPOGRAPHY | Enabled / Large badge label | font name: `md.comp.navigation-drawer.large-badge-label.font`, font weight: `md.comp.navigation-drawer.large-badge-label.weight`, font size: `md.comp.navigation-drawer.large-badge-label.size`, font tracking: `md.comp.navigation-drawer.large-badge-label.tracking`, line height: `md.comp.navigation-drawer.large-badge-label.line-height` |  |  |
| md.comp.navigation-drawer.scrim.color | COLOR | Enabled / [Deprecated] Scrim | `md.ref.palette.neutral-variant20` |  | Bug fix. Token does not represent M3 design intent. Use Neutral-Variant10 at 50% for scrims instead. |
| md.comp.navigation-drawer.scrim.opacity | OPACITY | Enabled / [Deprecated] Scrim | 0.4 |  | Bug fix. Token does not represent M3 design intent. Use Neutral-Variant10 at 50% for scrims instead. |
| md.comp.navigation-drawer.active.hover.label-text.color | COLOR | Hovered / Label text | `md.sys.color.on-secondary-container` |  |  |
| md.comp.navigation-drawer.inactive.hover.label-text.color | COLOR | Hovered / Label text | `md.sys.color.on-surface` |  |  |
| md.comp.navigation-drawer.active.hover.state-layer.color | COLOR | Hovered / State layer | `md.sys.color.on-secondary-container` |  |  |
| md.comp.navigation-drawer.inactive.hover.state-layer.color | COLOR | Hovered / State layer | `md.sys.color.on-surface` |  |  |
| md.comp.navigation-drawer.hover.state-layer.opacity | OPACITY | Hovered / State layer | `md.sys.state.hover.state-layer-opacity` |  |  |
| md.comp.navigation-drawer.active.hover.icon.color | COLOR | Hovered / Icon | `md.sys.color.on-secondary-container` |  |  |
| md.comp.navigation-drawer.inactive.hover.icon.color | COLOR | Hovered / Icon | `md.sys.color.on-surface` |  |  |
| md.comp.navigation-drawer.focus.indicator.color | COLOR | Focused / Focus indicator | `md.sys.color.secondary` |  |  |
| md.comp.navigation-drawer.focus.indicator.thickness | LENGTH | Focused / Focus indicator | `md.sys.state.focus-indicator.thickness` |  |  |
| md.comp.navigation-drawer.focus.indicator.outline.offset | LENGTH | Focused / Focus indicator | `md.sys.state.focus-indicator.inner-offset` |  |  |
| md.comp.navigation-drawer.active.focus.label-text.color | COLOR | Focused / Label text | `md.sys.color.on-secondary-container` |  |  |
| md.comp.navigation-drawer.inactive.focus.label-text.color | COLOR | Focused / Label text | `md.sys.color.on-surface` |  |  |
| md.comp.navigation-drawer.active.focus.state-layer.color | COLOR | Focused / State layer | `md.sys.color.on-secondary-container` |  |  |
| md.comp.navigation-drawer.inactive.focus.state-layer.color | COLOR | Focused / State layer | `md.sys.color.on-surface` |  |  |
| md.comp.navigation-drawer.focus.state-layer.opacity | OPACITY | Focused / State layer | `md.sys.state.focus.state-layer-opacity` |  |  |
| md.comp.navigation-drawer.active.focus.icon.color | COLOR | Focused / Icon | `md.sys.color.on-secondary-container` |  |  |
| md.comp.navigation-drawer.inactive.focus.icon.color | COLOR | Focused / Icon | `md.sys.color.on-surface` |  |  |
| md.comp.navigation-drawer.active.pressed.label-text.color | COLOR | Pressed (ripple) / Label text | `md.sys.color.on-secondary-container` |  |  |
| md.comp.navigation-drawer.inactive.pressed.label-text.color | COLOR | Pressed (ripple) / Label text | `md.sys.color.on-surface` |  |  |
| md.comp.navigation-drawer.active.pressed.state-layer.color | COLOR | Pressed (ripple) / State layer | `md.sys.color.on-secondary-container` |  |  |
| md.comp.navigation-drawer.inactive.pressed.state-layer.color | COLOR | Pressed (ripple) / State layer | `md.sys.color.on-secondary-container` |  |  |
| md.comp.navigation-drawer.pressed.state-layer.opacity | OPACITY | Pressed (ripple) / State layer | `md.sys.state.pressed.state-layer-opacity` |  |  |
| md.comp.navigation-drawer.active.pressed.icon.color | COLOR | Pressed (ripple) / Icon | `md.sys.color.on-secondary-container` |  |  |
| md.comp.navigation-drawer.inactive.pressed.icon.color | COLOR | Pressed (ripple) / Icon | `md.sys.color.on-surface` |  |  |

## Color

Color values are implemented through design tokens. For design, this means working with color values that correspond with tokens. For implementation, a color value will be a token that references a value. [Learn more about design tokens](../../foundations/design-tokens/overview.md)

![Navigation drawer diagram numbering 9 color roles.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fma6b7pyr-2-3p.png?alt=media&token=9b66c873-2f9c-49a4-bddc-a1ea7b223280>)

_Navigation drawer color roles used for light and dark schemes: Surface container low; On surface variant; On secondary container; On secondary container; Secondary container; On secondary container; On surface variant; On surface variant; Scrim_

For divider color roles, go to [divider specs](../divider/specs.md).

## States

States are visual representations used to communicate the status of a component or interactive element. [Learn more about interaction states](https://m3.material.io/m3/pages/interaction-states/overview)

![4 navigation drawers illustrating enabled, hovered, focused, and pressed states.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fma77ejxi-3b-3p.png?alt=media&token=3f75133e-ade4-4a4b-b6f0-bc72a90a7b3b>)

_Navigation drawer states: Enabled; Hovered; Focused; Pressed_

[State specs are in the tokens module above](specs.md#6207b00f-a259-41d2-8146-b6efc6380976)

## Measurements

### Standard navigation drawer

![Standard navigation drawer with measurements shown for various elements.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmobmv0i5-4-3p.png?alt=media&token=6731ae6e-d242-4f1b-9b2a-414bc4948d94>)

_Element size measurements_

![Standard navigation drawer with measurements shown for padding and margins.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fma6bb7nw-5-3p.png?alt=media&token=109e0a51-8d58-487a-b1f1-07ceb2e97b6b>)

_Padding and margins_

| Attribute | Value |
| --- | --- |
| Container height | 100% |
| Container width | 360dp |
| Container shape | 0,16,16,0dp corner radii |
| Icon size | 24dp |
| Active indicator height | 56dp |
| Active indicator shape | 28dp |
| Active indicator width | 336dp |
| Horizontal label alignment | Start-aligned |
| Left padding | 28dp |
| Right padding | 28dp |
| Active indicator padding | 12dp |
| Padding between elements | 0dp |

### Modal navigation drawer

![Modal navigation drawer with measurements shown for various elements.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fma775z3o-6-3p-3p.png?alt=media&token=554989e5-00c1-4338-8ab8-df73927626e4>)

_Element size measurements_

![Modal navigation drawer with measurements shown for padding and margins.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fma7769l7-7-3p-3p.png?alt=media&token=caaccf88-701f-44bf-bcb9-2569e95d000c>)

_Padding and margins_

| Attribute | Value |
| --- | --- |
| Container height | 100% |
| Container width | 360dp |
| Icon size | 24dp |
| Active indicator height | 56dp |
| Active indicator shape | 28dp |
| Active indicator width | 336dp |
| Horizontal label alignment | Start-aligned |
| Left padding | 28dp |
| Right padding | 28dp |
| Active indicator padding | 12dp |
| Padding between elements | 0dp |

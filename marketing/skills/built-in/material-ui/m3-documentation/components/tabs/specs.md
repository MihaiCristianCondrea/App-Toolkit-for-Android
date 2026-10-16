---
title: "Tabs"
source_url: "https://m3.material.io/components/tabs/specs"
lastmod: "2026-05-18"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "components"
assets:
  - "assets/images/660c6e1f3847-m2lequcy-1.png"
  - "assets/images/9a7b1a149e8a-m2lerpx7-2.png"
  - "assets/images/23fa45ced386-m8t9w5tr-03-3P.png"
  - "assets/images/5622d7a070ee-m2letsf5-4.png"
  - "assets/images/758f921c41f0-m2leuvo3-5.png"
  - "assets/images/7907c57683c1-m8t9wue2-06-3P.png"
  - "assets/images/c343ce328a1c-m92tisv3-07-3P.png"
  - "assets/images/e1c641009a9d-m92thj1h-08-3P.png"
---

# Tabs

Tabs organize content across different screens and views

## Specs

## Tokens and specs

Select a component variant below to see its elements, attributes, tokens, and their values.

### TOKEN_TABLE

Component: Tabs

Configured context tags: None, Medium contrast, Default, High contrast, Static, 3P, Android, Dark, Light

#### Token sets

| Token set | Name | Type | Tokens | Description |
| --- | --- | --- | --- | --- |
| Tabs - Primary navigation | md.comp.primary-navigation-tab | COMPONENT | 48 | For primary tabs. |
| Tabs - Secondary navigation | md.comp.secondary-navigation-tab | COMPONENT | 35 | For secondary tabs. |

#### Tabs - Primary navigation (md.comp.primary-navigation-tab)

For primary tabs.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.primary-navigation-tab.container.color | COLOR | Enabled / Container | `md.sys.color.surface` |  |  |
| md.comp.primary-navigation-tab.container.shape | SHAPE | Enabled / Container | `md.sys.shape.corner.none` |  |  |
| md.comp.primary-navigation-tab.container.height | LENGTH | Enabled / Container | 48dp |  |  |
| md.comp.primary-navigation-tab.with-icon-and-label-text.container.height | LENGTH | Enabled / Container | 64dp |  |  |
| md.comp.primary-navigation-tab.container.elevation | ELEVATION | Enabled / Container | `md.sys.elevation.level0` |  |  |
| md.comp.primary-navigation-tab.with-label-text.label-text.font | FONT_NAMES | Enabled / Label text | `md.sys.typescale.title-small.font` |  |  |
| md.comp.primary-navigation-tab.with-label-text.label-text.line-height | LINE_HEIGHT | Enabled / Label text | `md.sys.typescale.title-small.line-height` |  |  |
| md.comp.primary-navigation-tab.with-label-text.label-text.size | FONT_SIZE | Enabled / Label text | `md.sys.typescale.title-small.size` |  |  |
| md.comp.primary-navigation-tab.with-label-text.label-text.weight | FONT_WEIGHT | Enabled / Label text | `md.sys.typescale.title-small.weight` |  |  |
| md.comp.primary-navigation-tab.with-label-text.label-text.tracking | FONT_TRACKING | Enabled / Label text | `md.sys.typescale.title-small.tracking` |  |  |
| md.comp.primary-navigation-tab.with-label-text.label-text.type | TYPOGRAPHY | Enabled / Label text | font name: `md.comp.primary-navigation-tab.with-label-text.label-text.font`, font weight: `md.comp.primary-navigation-tab.with-label-text.label-text.weight`, font size: `md.comp.primary-navigation-tab.with-label-text.label-text.size`, font tracking: `md.comp.primary-navigation-tab.with-label-text.label-text.tracking`, line height: `md.comp.primary-navigation-tab.with-label-text.label-text.line-height` |  |  |
| md.comp.primary-navigation-tab.with-label-text.active.label-text.color | COLOR | Enabled / Label text | `md.sys.color.primary` |  |  |
| md.comp.primary-navigation-tab.with-label-text.inactive.label-text.color | COLOR | Enabled / Label text | `md.sys.color.on-surface-variant` |  |  |
| md.comp.primary-navigation-tab.with-icon.icon.size | LENGTH | Enabled / Icon | 24dp |  |  |
| md.comp.primary-navigation-tab.with-icon.active.icon.color | COLOR | Enabled / Icon | `md.sys.color.primary` |  |  |
| md.comp.primary-navigation-tab.with-icon.inactive.icon.color | COLOR | Enabled / Icon | `md.sys.color.on-surface-variant` |  |  |
| md.comp.primary-navigation-tab.divider.color | COLOR | Enabled / Divider | `md.sys.color.surface-variant` |  | Use standalone divider component instead of nested tokens. |
| md.comp.primary-navigation-tab.divider.height | LENGTH | Enabled / Divider | 1dp |  | Use standalone divider component instead of nested tokens. |
| md.comp.primary-navigation-tab.active-indicator.color | COLOR | Enabled / Active indicator | `md.sys.color.primary` |  |  |
| md.comp.primary-navigation-tab.active-indicator.height | LENGTH | Enabled / Active indicator | 3dp |  |  |
| md.comp.primary-navigation-tab.active-indicator.shape | SHAPE | Enabled / Active indicator | rounded corners 3dp |  |  |
| md.comp.primary-navigation-tab.with-label-text.active.hover.label-text.color | COLOR | Hovered / Label text | `md.sys.color.primary` |  |  |
| md.comp.primary-navigation-tab.with-label-text.inactive.hover.label-text.color | COLOR | Hovered / Label text | `md.sys.color.on-surface` |  |  |
| md.comp.primary-navigation-tab.active.hover.state-layer.color | COLOR | Hovered / State layer | `md.sys.color.primary` |  |  |
| md.comp.primary-navigation-tab.active.hover.state-layer.opacity | OPACITY | Hovered / State layer | `md.sys.state.hover.state-layer-opacity` |  |  |
| md.comp.primary-navigation-tab.inactive.hover.state-layer.color | COLOR | Hovered / State layer | `md.sys.color.on-surface` |  |  |
| md.comp.primary-navigation-tab.inactive.hover.state-layer.opacity | OPACITY | Hovered / State layer | `md.sys.state.hover.state-layer-opacity` |  |  |
| md.comp.primary-navigation-tab.with-icon.active.hover.icon.color | COLOR | Hovered / Icon | `md.sys.color.primary` |  |  |
| md.comp.primary-navigation-tab.with-icon.inactive.hover.icon.color | COLOR | Hovered / Icon | `md.sys.color.on-surface` |  |  |
| md.comp.primary-navigation-tab.focus.indicator.color | COLOR | Focused / Focus indicator | `md.sys.color.secondary` |  |  |
| md.comp.primary-navigation-tab.focus.indicator.thickness | LENGTH | Focused / Focus indicator | `md.sys.state.focus-indicator.thickness` |  |  |
| md.comp.primary-navigation-tab.focus.indicator.outline.offset | LENGTH | Focused / Focus indicator | `md.sys.state.focus-indicator.inner-offset` |  |  |
| md.comp.primary-navigation-tab.with-label-text.active.focus.label-text.color | COLOR | Focused / Label text | `md.sys.color.primary` |  |  |
| md.comp.primary-navigation-tab.with-label-text.inactive.focus.label-text.color | COLOR | Focused / Label text | `md.sys.color.on-surface` |  |  |
| md.comp.primary-navigation-tab.active.focus.state-layer.color | COLOR | Focused / State layer | `md.sys.color.primary` |  |  |
| md.comp.primary-navigation-tab.active.focus.state-layer.opacity | OPACITY | Focused / State layer | `md.sys.state.focus.state-layer-opacity` |  |  |
| md.comp.primary-navigation-tab.inactive.focus.state-layer.color | COLOR | Focused / State layer | `md.sys.color.on-surface` |  |  |
| md.comp.primary-navigation-tab.inactive.focus.state-layer.opacity | OPACITY | Focused / State layer | `md.sys.state.focus.state-layer-opacity` |  |  |
| md.comp.primary-navigation-tab.with-icon.active.focus.icon.color | COLOR | Focused / Icon | `md.sys.color.primary` |  |  |
| md.comp.primary-navigation-tab.with-icon.inactive.focus.icon.color | COLOR | Focused / Icon | `md.sys.color.on-surface` |  |  |
| md.comp.primary-navigation-tab.with-label-text.active.pressed.label-text.color | COLOR | Pressed (ripple) / Label text | `md.sys.color.primary` |  |  |
| md.comp.primary-navigation-tab.with-label-text.inactive.pressed.label-text.color | COLOR | Pressed (ripple) / Label text | `md.sys.color.on-surface` |  |  |
| md.comp.primary-navigation-tab.active.pressed.state-layer.color | COLOR | Pressed (ripple) / State layer | `md.sys.color.primary` |  |  |
| md.comp.primary-navigation-tab.active.pressed.state-layer.opacity | OPACITY | Pressed (ripple) / State layer | `md.sys.state.pressed.state-layer-opacity` |  |  |
| md.comp.primary-navigation-tab.inactive.pressed.state-layer.color | COLOR | Pressed (ripple) / State layer | `md.sys.color.primary` |  |  |
| md.comp.primary-navigation-tab.inactive.pressed.state-layer.opacity | OPACITY | Pressed (ripple) / State layer | `md.sys.state.pressed.state-layer-opacity` |  |  |
| md.comp.primary-navigation-tab.with-icon.active.pressed.icon.color | COLOR | Pressed (ripple) / Icon | `md.sys.color.primary` |  |  |
| md.comp.primary-navigation-tab.with-icon.inactive.pressed.icon.color | COLOR | Pressed (ripple) / Icon | `md.sys.color.on-surface` |  |  |

#### Tabs - Secondary navigation (md.comp.secondary-navigation-tab)

For secondary tabs.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.secondary-navigation-tab.container.color | COLOR | Enabled / Container | `md.sys.color.surface` |  |  |
| md.comp.secondary-navigation-tab.container.shape | SHAPE | Enabled / Container | `md.sys.shape.corner.none` |  |  |
| md.comp.secondary-navigation-tab.container.height | LENGTH | Enabled / Container | 48dp |  |  |
| md.comp.secondary-navigation-tab.container.elevation | ELEVATION | Enabled / Container | `md.sys.elevation.level0` |  |  |
| md.comp.secondary-navigation-tab.container.shadow-color | COLOR | Enabled / Container | `md.sys.color.shadow` |  |  |
| md.comp.secondary-navigation-tab.label-text.font | FONT_NAMES | Enabled / Label text | `md.sys.typescale.title-small.font` |  |  |
| md.comp.secondary-navigation-tab.label-text.line-height | LINE_HEIGHT | Enabled / Label text | `md.sys.typescale.title-small.line-height` |  |  |
| md.comp.secondary-navigation-tab.label-text.size | FONT_SIZE | Enabled / Label text | `md.sys.typescale.title-small.size` |  |  |
| md.comp.secondary-navigation-tab.label-text.weight | FONT_WEIGHT | Enabled / Label text | `md.sys.typescale.title-small.weight` |  |  |
| md.comp.secondary-navigation-tab.label-text.tracking | FONT_TRACKING | Enabled / Label text | `md.sys.typescale.title-small.tracking` |  |  |
| md.comp.secondary-navigation-tab.label-text.type | TYPOGRAPHY | Enabled / Label text | font name: `md.comp.secondary-navigation-tab.label-text.font`, font weight: `md.comp.secondary-navigation-tab.label-text.weight`, font size: `md.comp.secondary-navigation-tab.label-text.size`, font tracking: `md.comp.secondary-navigation-tab.label-text.tracking`, line height: `md.comp.secondary-navigation-tab.label-text.line-height` |  |  |
| md.comp.secondary-navigation-tab.active.label-text.color | COLOR | Enabled / Label text | `md.sys.color.on-surface` |  |  |
| md.comp.secondary-navigation-tab.inactive.label-text.color | COLOR | Enabled / Label text | `md.sys.color.on-surface-variant` |  |  |
| md.comp.secondary-navigation-tab.with-icon.icon.size | LENGTH | Enabled / Icon | 24dp |  |  |
| md.comp.secondary-navigation-tab.with-icon.active.icon.color | COLOR | Enabled / Icon | `md.sys.color.on-surface` |  |  |
| md.comp.secondary-navigation-tab.with-icon.inactive.icon.color | COLOR | Enabled / Icon | `md.sys.color.on-surface-variant` |  |  |
| md.comp.secondary-navigation-tab.divider.color | COLOR | Enabled / Divider | `md.sys.color.surface-variant` |  | Please use standalone divider component tokens instead |
| md.comp.secondary-navigation-tab.divider.height | LENGTH | Enabled / Divider | 1dp |  | Please use standalone divider component tokens instead |
| md.comp.secondary-navigation-tab.active-indicator.color | COLOR | Enabled / Active indicator | `md.sys.color.primary` |  |  |
| md.comp.secondary-navigation-tab.active-indicator.height | LENGTH | Enabled / Active indicator | 2dp |  |  |
| md.comp.secondary-navigation-tab.hover.label-text.color | COLOR | Hovered / Label text | `md.sys.color.on-surface` |  |  |
| md.comp.secondary-navigation-tab.hover.state-layer.color | COLOR | Hovered / State layer | `md.sys.color.on-surface` |  |  |
| md.comp.secondary-navigation-tab.hover.state-layer.opacity | OPACITY | Hovered / State layer | `md.sys.state.hover.state-layer-opacity` |  |  |
| md.comp.secondary-navigation-tab.with-icon.hover.icon.color | COLOR | Hovered / Icon | `md.sys.color.on-surface` |  |  |
| md.comp.secondary-navigation-tab.focus.indicator.color | COLOR | Focused / Focus indicator | `md.sys.color.secondary` |  |  |
| md.comp.secondary-navigation-tab.focus.indicator.thickness | LENGTH | Focused / Focus indicator | `md.sys.state.focus-indicator.thickness` |  |  |
| md.comp.secondary-navigation-tab.focus.indicator.outline.offset | LENGTH | Focused / Focus indicator | `md.sys.state.focus-indicator.inner-offset` |  |  |
| md.comp.secondary-navigation-tab.focus.label-text.color | COLOR | Focused / Label text | `md.sys.color.on-surface` |  |  |
| md.comp.secondary-navigation-tab.focus.state-layer.color | COLOR | Focused / State layer | `md.sys.color.on-surface` |  |  |
| md.comp.secondary-navigation-tab.focus.state-layer.opacity | OPACITY | Focused / State layer | `md.sys.state.focus.state-layer-opacity` |  |  |
| md.comp.secondary-navigation-tab.with-icon.focus.icon.color | COLOR | Focused / Icon | `md.sys.color.on-surface` |  |  |
| md.comp.secondary-navigation-tab.pressed.label-text.color | COLOR | Pressed (ripple) / Label text | `md.sys.color.on-surface` |  |  |
| md.comp.secondary-navigation-tab.pressed.state-layer.color | COLOR | Pressed (ripple) / State layer | `md.sys.color.on-surface` |  |  |
| md.comp.secondary-navigation-tab.pressed.state-layer.opacity | OPACITY | Pressed (ripple) / State layer | `md.sys.state.pressed.state-layer-opacity` |  |  |
| md.comp.secondary-navigation-tab.with-icon.pressed.icon.color | COLOR | Pressed (ripple) / Icon | `md.sys.color.on-surface` |  |  |

## Primary tabs

![6 elements of primary tabs.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fm2lequcy-1.png?alt=media&token=e30b24d1-ba44-45b8-a4e3-8d260ba61783>)

_Container; Badge (optional); Icon (optional); Label; Divider; Active indicator_

### Primary tabs color

Color values are implemented through design tokens. For design, this means working with color values that correspond with tokens. For implementation, a color value will be a token that references a value. [Learn more about design tokens](../../foundations/design-tokens/overview.md)

![7 color roles applied to primary tabs in light and dark themes.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fm2lerpx7-2.png?alt=media&token=61392b0c-376b-4e53-bec0-592dc867150d>)

_Primary tab color roles used for light and dark schemes: Surface; Primary; Primary; On surface variant; On surface variant; Outline variant; Primary_

### Primary tabs states

![Diagram of all primary tab states in both light and dark mode](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fm8t9w5tr-03-3P.png?alt=media&token=ab51d1db-3cf4-4f3c-95ee-72ffb1f6dd3d>)

_Enabled (active destination); Hover (active destination); Focused (active destination); Pressed (active destination); Enabled (inactive destination); Hover (inactive destination); Focused (inactive destination); Pressed (inactive destination)_

## Secondary tabs

![5 elements of secondary tabs.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fm2letsf5-4.png?alt=media&token=53f548f7-a618-4720-ba08-563be29dce2b>)

_Container; Badge (optional); Label; Divider; Active indicator_

### Secondary tabs color

Color values are implemented through design tokens. For design, this means working with color values that correspond with tokens. For implementation, a color value will be a token that references a value. [Learn more about design tokens](../../foundations/design-tokens/overview.md)

![5 color roles applied to secondary tabs in light and dark themes.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fm2leuvo3-5.png?alt=media&token=ad58a754-c7b2-4ae3-9eaa-b24f4f04e6ae>)

_Secondary tab color roles used for light and dark schemes: Surface; On surface; On surface variant; Outline variant; Primary_

### Secondary tabs states

![Diagram of all secondary tab states in both light and dark mode](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fm8t9wue2-06-3P.png?alt=media&token=c4d4a82a-f2b2-484e-8eb3-0511a6a4aa81>)

_Enabled (active destination); Hover (active destination); Focused (active destination); Pressed (active destination); Enabled (inactive destination); Hover (inactive destination); Focused (inactive destination); Pressed (inactive destination)_

## Measurements

![Diagram of measurements for four and two tabs per container, including icon and label placement.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fm92tisv3-07-3P.png?alt=media&token=3f3fca3c-6b4d-4034-9c84-94f9bf7c785d>)

_Tabs are divided into equal sections, with labels and icons positioned vertically centered. The divider is included in the height, placed inside the container._

![Diagram of Primary tab active indicator measurements.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fm92thj1h-08-3P.png?alt=media&token=76d7e053-1d60-462d-900c-9e65d537a4d7>)

_Primary tab active indicators are inset 2dp on each side, have a fully rounded corner radius, and a minimum length of 24dp._

| Attribute | Value |
| --- | --- |
| Container height (label text only) | 48dp |
| Container height (icon and label text) | 64dp |
| Icon size | 24dp |
| Divider height | 1dp |
| Primary active indicator height | 3dp |
| Secondary active indicator height | 2dp |
| Active indicator shape | 3, 3, 0, 0 |
| Active indicator minimum length | 24dp |
| Padding between inline icon and text | 8dp |
| Padding between inline text and badge | 4dp |
| Overlap of badge on stacked icon | 6dp |

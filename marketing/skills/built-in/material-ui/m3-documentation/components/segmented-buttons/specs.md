---
title: "Segmented buttons"
source_url: "https://m3.material.io/components/segmented-buttons/specs"
lastmod: "2026-05-18"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "components"
assets:
  - "assets/images/a5c87cac171f-ma6l9syf-1.png"
  - "assets/images/53be60785054-ma6lafzx-2.png"
  - "assets/images/970c0380fb29-ma6law70-3b.png"
  - "assets/images/8582229b5f74-ma6lbdzb-4b.png"
  - "assets/images/266b6c355664-mmp8rn0c-5-3P.png"
  - "assets/images/e8a3a3d38ec4-ma6lc5hl-6.png"
---

# Segmented buttons

Segmented buttons help people select options, switch views, or sort elements

## Specs

star

Note:

Segmented buttons are no longer recommended in the Material 3 expressive update. For those who have updated, use the [connected button group](https://m3.material.io/m3/pages/button-groups/overview/) instead, which has mostly the same functionality but with an updated visual design.

![Diagram of segmented button indicating 3 parts of its anatomy.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fma6l9syf-1.png?alt=media&token=6f061b57-7ba3-4b4a-8d05-48c819cb5aa8>)

_Container; Icon (optional for unselected state); Label text_

## Tokens and specs

Browse the component elements, attributes, tokens, and their values. [Learn more about design tokens](../../foundations/design-tokens/overview.md)

### TOKEN_TABLE

Component: Segmented buttons

Configured context tags: None, Medium contrast, Default, High contrast, Static, 3P, Android, Dark, Light

#### Token sets

| Token set | Name | Type | Tokens | Description |
| --- | --- | --- | --- | --- |
| Segmented button - Outlined | md.comp.outlined-segmented-button | COMPONENT | 46 | This component is not recommended. Use connected button groups instead. For outlined segmented buttons. |

#### Segmented button - Outlined (md.comp.outlined-segmented-button)

This component is not recommended. Use connected button groups instead. For outlined segmented buttons.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.outlined-segmented-button.outline.color | COLOR | Enabled / Container | `md.sys.color.outline` |  |  |
| md.comp.outlined-segmented-button.outline.width | LENGTH | Enabled / Container | 1dp |  |  |
| md.comp.outlined-segmented-button.container.height | LENGTH | Enabled / Container | 40dp |  |  |
| md.comp.outlined-segmented-button.selected.container.color | COLOR | Enabled / Container | `md.sys.color.secondary-container` |  |  |
| md.comp.outlined-segmented-button.shape | SHAPE | Enabled / Container | `md.sys.shape.corner.full` |  |  |
| md.comp.outlined-segmented-button.unselected.label-text.color | COLOR | Enabled / Label text | `md.sys.color.on-surface` |  |  |
| md.comp.outlined-segmented-button.selected.label-text.color | COLOR | Enabled / Label text | `md.sys.color.on-secondary-container` |  |  |
| md.comp.outlined-segmented-button.label-text.font | FONT_NAMES | Enabled / Label text | `md.sys.typescale.label-large.font` |  |  |
| md.comp.outlined-segmented-button.label-text.line-height | LINE_HEIGHT | Enabled / Label text | `md.sys.typescale.label-large.line-height` |  |  |
| md.comp.outlined-segmented-button.label-text.size | FONT_SIZE | Enabled / Label text | `md.sys.typescale.label-large.size` |  |  |
| md.comp.outlined-segmented-button.label-text.tracking | FONT_TRACKING | Enabled / Label text | `md.sys.typescale.label-large.tracking` |  |  |
| md.comp.outlined-segmented-button.label-text.weight | FONT_WEIGHT | Enabled / Label text | `md.sys.typescale.label-large.weight` |  |  |
| md.comp.outlined-segmented-button.label-text.type | TYPOGRAPHY | Enabled / Label text | font name: `md.comp.outlined-segmented-button.label-text.font`, font weight: `md.comp.outlined-segmented-button.label-text.weight`, font size: `md.comp.outlined-segmented-button.label-text.size`, font tracking: `md.comp.outlined-segmented-button.label-text.tracking`, line height: `md.comp.outlined-segmented-button.label-text.line-height` |  |  |
| md.comp.outlined-segmented-button.unselected.with-icon.icon.color | COLOR | Enabled / Icon | `md.sys.color.on-surface` |  |  |
| md.comp.outlined-segmented-button.selected.with-icon.icon.color | COLOR | Enabled / Icon | `md.sys.color.on-secondary-container` |  |  |
| md.comp.outlined-segmented-button.with-icon.icon.size | LENGTH | Enabled / Icon | 18dp |  |  |
| md.comp.outlined-segmented-button.disabled.outline.color | COLOR | Disabled / Container | `md.sys.color.on-surface` |  |  |
| md.comp.outlined-segmented-button.disabled.outline.opacity | OPACITY | Disabled / Container | 0.12 |  |  |
| md.comp.outlined-segmented-button.disabled.label-text.color | COLOR | Disabled / Label text | `md.sys.color.on-surface` |  |  |
| md.comp.outlined-segmented-button.disabled.label-text.opacity | OPACITY | Disabled / Label text | 0.38 |  |  |
| md.comp.outlined-segmented-button.disabled.icon.color | COLOR | Disabled / Icon | `md.sys.color.on-surface` |  |  |
| md.comp.outlined-segmented-button.disabled.icon.opacity | OPACITY | Disabled / Icon | 0.38 |  |  |
| md.comp.outlined-segmented-button.unselected.hover.label-text.color | COLOR | Hovered / Label text | `md.sys.color.on-surface` |  |  |
| md.comp.outlined-segmented-button.selected.hover.label-text.color | COLOR | Hovered / Label text | `md.sys.color.on-secondary-container` |  |  |
| md.comp.outlined-segmented-button.unselected.hover.state-layer.color | COLOR | Hovered / State layer | `md.sys.color.on-surface` |  |  |
| md.comp.outlined-segmented-button.selected.hover.state-layer.color | COLOR | Hovered / State layer | `md.sys.color.on-secondary-container` |  |  |
| md.comp.outlined-segmented-button.hover.state-layer.opacity | OPACITY | Hovered / State layer | `md.sys.state.hover.state-layer-opacity` |  |  |
| md.comp.outlined-segmented-button.unselected.hover.icon.color | COLOR | Hovered / Icon | `md.sys.color.on-surface` |  |  |
| md.comp.outlined-segmented-button.selected.hover.icon.color | COLOR | Hovered / Icon | `md.sys.color.on-secondary-container` |  |  |
| md.comp.outlined-segmented-button.focus.indicator.color | COLOR | Focused / Focus indicator | `md.sys.color.secondary` |  |  |
| md.comp.outlined-segmented-button.focus.indicator.thickness | LENGTH | Focused / Focus indicator | `md.sys.state.focus-indicator.thickness` |  |  |
| md.comp.outlined-segmented-button.focus.indicator.outline.offset | LENGTH | Focused / Focus indicator | `md.sys.state.focus-indicator.outer-offset` |  |  |
| md.comp.outlined-segmented-button.unselected.focus.label-text.color | COLOR | Focused / Label text | `md.sys.color.on-surface` |  |  |
| md.comp.outlined-segmented-button.selected.focus.label-text.color | COLOR | Focused / Label text | `md.sys.color.on-secondary-container` |  |  |
| md.comp.outlined-segmented-button.unselected.focus.state-layer.color | COLOR | Focused / State layer | `md.sys.color.on-surface` |  |  |
| md.comp.outlined-segmented-button.selected.focus.state-layer.color | COLOR | Focused / State layer | `md.sys.color.on-secondary-container` |  |  |
| md.comp.outlined-segmented-button.focus.state-layer.opacity | OPACITY | Focused / State layer | `md.sys.state.focus.state-layer-opacity` |  |  |
| md.comp.outlined-segmented-button.unselected.focus.icon.color | COLOR | Focused / Icon | `md.sys.color.on-surface` |  |  |
| md.comp.outlined-segmented-button.selected.focus.icon.color | COLOR | Focused / Icon | `md.sys.color.on-secondary-container` |  |  |
| md.comp.outlined-segmented-button.unselected.pressed.label-text.color | COLOR | Pressed (ripple) / Label text | `md.sys.color.on-surface` |  |  |
| md.comp.outlined-segmented-button.selected.pressed.label-text.color | COLOR | Pressed (ripple) / Label text | `md.sys.color.on-secondary-container` |  |  |
| md.comp.outlined-segmented-button.unselected.pressed.state-layer.color | COLOR | Pressed (ripple) / State layer | `md.sys.color.on-surface` |  |  |
| md.comp.outlined-segmented-button.selected.pressed.state-layer.color | COLOR | Pressed (ripple) / State layer | `md.sys.color.on-secondary-container` |  |  |
| md.comp.outlined-segmented-button.pressed.state-layer.opacity | OPACITY | Pressed (ripple) / State layer | `md.sys.state.focus.state-layer-opacity` |  |  |
| md.comp.outlined-segmented-button.unselected.pressed.icon.color | COLOR | Pressed (ripple) / Icon | `md.sys.color.on-surface` |  |  |
| md.comp.outlined-segmented-button.selected.pressed.icon.color | COLOR | Pressed (ripple) / Icon | `md.sys.color.on-secondary-container` |  |  |

## Color

Color values are implemented through design tokens. For design, this means working with color values that correspond with tokens. For implementation, a color value will be a token that references a value. [Learn more about design tokens](../../foundations/design-tokens/overview.md)

![Diagram of segmented button indicating its color mappings](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fma6lafzx-2.png?alt=media&token=5e27c91f-def3-4bd7-b695-1ab003bcbf6e>)

_Segmented button color roles used for light and dark schemes: On surface; Outline; Secondary container; On secondary container_

## States

States are visual representations used to communicate the status of a component or interactive element.  [Learn more about interaction states](https://m3.material.io/m3/pages/interaction-states/overview)

### Unselected

![Side by side view of segmented buttons with 5 unselected states.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fma6law70-3b.png?alt=media&token=c4bc48ed-8fdf-487b-8523-05454e22d786>)

_Unselected button states: Enabled; Disabled; Hovered; Focused; Pressed; /_

### Selected

![Side by side view of segmented buttons with 4 selected states.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fma6lbdzb-4b.png?alt=media&token=83d2c823-d412-4284-80e0-6d5f27141c63>)

_Selected button states: Selected; Hovered on selected; Focused on selected; Pressed on selected_

## Measurements

![Diagram indicating layout values, paddings, and target size for segmented buttons](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmmp8rn0c-5-3P.png?alt=media&token=ecf7d32e-1b88-45e0-a33f-0cb132870814>)

_Padding and container size; Target size_

| Attribute | Value |
| --- | --- |
| Container width | Dynamic based on labels |
| Segment width | Container width / total segments (Example: 1/3) |
| Height | 40dp |
| Outline width | 1dp |
| Label alignment | Center |
| Left/right padding | Min 12dp |
| Padding between elements | 8dp |
| Target size | 48dp |

### Density

Density can be used in denser UIs where space is limited. Density is only applied to the height.

![Side by side view of segmented buttons with 4 different density heights](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fma6lc5hl-6.png?alt=media&token=f45257f0-41dc-4045-b92b-22ea302cc4c7>)

_Each step down in density removes 4dp from the height_

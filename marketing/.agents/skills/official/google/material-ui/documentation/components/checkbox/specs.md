---
title: "Checkbox"
source_url: "https://m3.material.io/components/checkbox/specs"
lastmod: "2026-05-18"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "components"
assets:
  - "assets/images/adec38612545-memmstmc-1-3P.png"
  - "assets/images/397c3a43a343-memmtsu5-2-3P.png"
  - "assets/images/1631d3d23279-memmvnkr-3-3P.png"
  - "assets/images/3e93318de949-memmxoan-4-3P.png"
  - "assets/images/0d6c7bb91103-memn204f-5-3P.png"
---

# Checkbox

Checkboxes let users select one or more items from a list, or turn an item on or off

## Specs

## Tokens & specs

Browse the component elements, attributes, tokens, and their values.

### TOKEN_TABLE

Component: Checkbox

Configured context tags: None, Medium contrast, Default, High contrast, 3P, Android, Dark, Light

#### Token sets

| Token set | Name | Type | Tokens | Description |
| --- | --- | --- | --- | --- |
| Checkbox | md.comp.checkbox | COMPONENT | 81 | For checkboxes. |

#### Checkbox (md.comp.checkbox)

For checkboxes.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.checkbox.container.width | LENGTH | Enabled / Container | 18dp |  | Combining height and width into a single size token |
| md.comp.checkbox.container.height | LENGTH | Enabled / Container | 18dp |  | Combining height and width into a single size token |
| md.comp.checkbox.container.size | LENGTH | Enabled / Container | 18dp |  |  |
| md.comp.checkbox.container.shape | SHAPE | Enabled / Container | rounded corners 2dp |  |  |
| md.comp.checkbox.unselected.outline.color | COLOR | Enabled / Container | `md.sys.color.on-surface-variant` |  |  |
| md.comp.checkbox.unselected.outline.width | LENGTH | Enabled / Container | 2dp |  |  |
| md.comp.checkbox.selected.container.color | COLOR | Enabled / Container | `md.sys.color.primary` |  |  |
| md.comp.checkbox.selected.outline.width | LENGTH | Enabled / Container | 0dp |  |  |
| md.comp.checkbox.unselected.error.outline.color | COLOR | Enabled / Container | `md.sys.color.error` |  |  |
| md.comp.checkbox.selected.error.container.color | COLOR | Enabled / Container | `md.sys.color.error` |  |  |
| md.comp.checkbox.icon.size | LENGTH | Enabled / Icon | 18dp |  |  |
| md.comp.checkbox.selected.icon.color | COLOR | Enabled / Icon | `md.sys.color.on-primary` |  |  |
| md.comp.checkbox.selected.error.icon.color | COLOR | Enabled / Icon | `md.sys.color.on-error` |  |  |
| md.comp.checkbox.unselected.icon.color | COLOR | Enabled / Icon | `md.sys.color.on-surface` |  | Checkbox changed how rendering was specified, if these are needed, copy to your own tokens. |
| md.comp.checkbox.state-layer.size | LENGTH | Enabled / State layer | 40dp |  |  |
| md.comp.checkbox.state-layer.shape | SHAPE | Enabled / State layer | `md.sys.shape.corner.full` |  |  |
| md.comp.checkbox.unselected.disabled.outline.color | COLOR | Disabled / Container | `md.sys.color.on-surface` |  |  |
| md.comp.checkbox.unselected.disabled.outline.width | LENGTH | Disabled / Container | 2dp |  |  |
| md.comp.checkbox.unselected.disabled.container.opacity | OPACITY | Disabled / Container | 0.38 |  |  |
| md.comp.checkbox.selected.disabled.container.color | COLOR | Disabled / Container | `md.sys.color.on-surface` |  |  |
| md.comp.checkbox.selected.disabled.container.opacity | OPACITY | Disabled / Container | 0.38 |  |  |
| md.comp.checkbox.selected.disabled.container.outline.width | LENGTH | Disabled / Container | 0dp |  |  |
| md.comp.checkbox.selected.disabled.icon.color | COLOR | Disabled / Icon | `md.sys.color.surface` |  |  |
| md.comp.checkbox.disabled.selected.icon.color | COLOR | Disabled / Icon | `md.sys.color.primary` |  | Checkbox changed how rendering was specified, if these are needed, copy to your own tokens. |
| md.comp.checkbox.disabled.selected.icon.opacity | OPACITY | Disabled / Icon | 0.38 |  | Checkbox changed how rendering was specified, if these are needed, copy to your own tokens. |
| md.comp.checkbox.disabled.unselected.icon.color | COLOR | Disabled / Icon | `md.sys.color.primary` |  | Checkbox changed how rendering was specified, if these are needed, copy to your own tokens. |
| md.comp.checkbox.disabled.unselected.icon.opacity | OPACITY | Disabled / Icon | 0.38 |  | Checkbox changed how rendering was specified, if these are needed, copy to your own tokens. |
| md.comp.checkbox.unselected.hover.outline.color | COLOR | Hovered / Container | `md.sys.color.on-surface` |  |  |
| md.comp.checkbox.unselected.hover.outline.width | LENGTH | Hovered / Container | 2dp |  |  |
| md.comp.checkbox.selected.hover.container.color | COLOR | Hovered / Container | `md.sys.color.primary` |  |  |
| md.comp.checkbox.selected.hover.outline.width | LENGTH | Hovered / Container | 0dp |  |  |
| md.comp.checkbox.unselected.error.hover.outline.color | COLOR | Hovered / Container | `md.sys.color.error` |  |  |
| md.comp.checkbox.unselected.error.hover.outline.width | LENGTH | Hovered / Container | 2dp |  | Redundant values. Please use md.comp.checkbox.unselected.outline.width or md.comp.checkbox.selected.outline.width tokens instead. |
| md.comp.checkbox.selected.error.hover.container.color | COLOR | Hovered / Container | `md.sys.color.error` |  |  |
| md.comp.checkbox.selected.error.hover.outline.width | LENGTH | Hovered / Container | 0dp |  | Redundant values. Please use md.comp.checkbox.unselected.outline.width or md.comp.checkbox.selected.outline.width tokens instead. |
| md.comp.checkbox.selected.hover.state-layer.color | COLOR | Hovered / State layer | `md.sys.color.primary` |  |  |
| md.comp.checkbox.selected.hover.state-layer.opacity | OPACITY | Hovered / State layer | `md.sys.state.hover.state-layer-opacity` |  |  |
| md.comp.checkbox.unselected.hover.state-layer.color | COLOR | Hovered / State layer | `md.sys.color.on-surface` |  |  |
| md.comp.checkbox.unselected.hover.state-layer.opacity | OPACITY | Hovered / State layer | `md.sys.state.hover.state-layer-opacity` |  |  |
| md.comp.checkbox.error.hover.state-layer.color | COLOR | Hovered / State layer | `md.sys.color.error` |  |  |
| md.comp.checkbox.error.hover.state-layer.opacity | OPACITY | Hovered / State layer | `md.sys.state.hover.state-layer-opacity` |  |  |
| md.comp.checkbox.selected.hover.icon.color | COLOR | Hovered / Icon | `md.sys.color.on-primary` |  |  |
| md.comp.checkbox.selected.error.hover.icon.color | COLOR | Hovered / Icon | `md.sys.color.on-error` |  |  |
| md.comp.checkbox.unselected.hover.icon.color | COLOR | Hovered / Icon | `md.sys.color.on-surface` |  | Checkbox changed how rendering was specified, if these are needed, copy to your own tokens. |
| md.comp.checkbox.focus.indicator.color | COLOR | Focused / Focus indicator | `md.sys.color.secondary` |  |  |
| md.comp.checkbox.focus.indicator.thickness | LENGTH | Focused / Focus indicator | `md.sys.state.focus-indicator.thickness` |  |  |
| md.comp.checkbox.focus.indicator.outline.offset | LENGTH | Focused / Focus indicator | `md.sys.state.focus-indicator.outer-offset` |  |  |
| md.comp.checkbox.unselected.focus.outline.color | COLOR | Focused / Container | `md.sys.color.on-surface` |  |  |
| md.comp.checkbox.unselected.focus.outline.width | LENGTH | Focused / Container | 2dp |  |  |
| md.comp.checkbox.selected.focus.container.color | COLOR | Focused / Container | `md.sys.color.primary` |  |  |
| md.comp.checkbox.selected.focus.outline.width | LENGTH | Focused / Container | 0dp |  |  |
| md.comp.checkbox.unselected.error.focus.outline.color | COLOR | Focused / Container | `md.sys.color.error` |  |  |
| md.comp.checkbox.unselected.error.focus.outline.width | LENGTH | Focused / Container | 2dp |  | Redundant values. Please use md.comp.checkbox.unselected.outline.width or md.comp.checkbox.selected.outline.width tokens instead. |
| md.comp.checkbox.selected.error.focus.container.color | COLOR | Focused / Container | `md.sys.color.error` |  |  |
| md.comp.checkbox.selected.error.focus.outline.width | LENGTH | Focused / Container | 0dp |  | Redundant values. Please use md.comp.checkbox.unselected.outline.width or md.comp.checkbox.selected.outline.width tokens instead. |
| md.comp.checkbox.error.focus.state-layer.opacity | OPACITY | Focused / State layer | `md.sys.state.focus.state-layer-opacity` |  |  |
| md.comp.checkbox.selected.focus.state-layer.color | COLOR | Focused / State layer | `md.sys.color.primary` |  |  |
| md.comp.checkbox.selected.focus.state-layer.opacity | OPACITY | Focused / State layer | `md.sys.state.focus.state-layer-opacity` |  |  |
| md.comp.checkbox.unselected.focus.state-layer.color | COLOR | Focused / State layer | `md.sys.color.on-surface` |  |  |
| md.comp.checkbox.unselected.focus.state-layer.opacity | OPACITY | Focused / State layer | `md.sys.state.focus.state-layer-opacity` |  |  |
| md.comp.checkbox.error.focus.state-layer.color | COLOR | Focused / State layer | `md.sys.color.error` |  |  |
| md.comp.checkbox.selected.focus.icon.color | COLOR | Focused / Icon | `md.sys.color.on-primary` |  |  |
| md.comp.checkbox.selected.error.focus.icon.color | COLOR | Focused / Icon | `md.sys.color.on-error` |  |  |
| md.comp.checkbox.unselected.focus.icon.color | COLOR | Focused / Icon | `md.sys.color.on-surface` |  | Checkbox changed how rendering was specified, if these are needed, copy to your own tokens. |
| md.comp.checkbox.unselected.pressed.outline.color | COLOR | Pressed (ripple) / Container | `md.sys.color.on-surface` |  |  |
| md.comp.checkbox.unselected.pressed.outline.width | LENGTH | Pressed (ripple) / Container | 2dp |  |  |
| md.comp.checkbox.selected.pressed.container.color | COLOR | Pressed (ripple) / Container | `md.sys.color.primary` |  |  |
| md.comp.checkbox.selected.pressed.outline.width | LENGTH | Pressed (ripple) / Container | 0dp |  |  |
| md.comp.checkbox.unselected.error.pressed.outline.color | COLOR | Pressed (ripple) / Container | `md.sys.color.error` |  |  |
| md.comp.checkbox.unselected.error.pressed.outline.width | LENGTH | Pressed (ripple) / Container | 2dp |  | Redundant values. Please use md.comp.checkbox.unselected.outline.width or md.comp.checkbox.selected.outline.width tokens instead. |
| md.comp.checkbox.selected.error.pressed.container.color | COLOR | Pressed (ripple) / Container | `md.sys.color.error` |  |  |
| md.comp.checkbox.selected.error.pressed.outline.width | LENGTH | Pressed (ripple) / Container | 0dp |  | Redundant values. Please use md.comp.checkbox.unselected.outline.width or md.comp.checkbox.selected.outline.width tokens instead. |
| md.comp.checkbox.unselected.pressed.state-layer.color | COLOR | Pressed (ripple) / State layer | `md.sys.color.primary` |  |  |
| md.comp.checkbox.unselected.pressed.state-layer.opacity | OPACITY | Pressed (ripple) / State layer | `md.sys.state.pressed.state-layer-opacity` |  |  |
| md.comp.checkbox.selected.pressed.state-layer.color | COLOR | Pressed (ripple) / State layer | `md.sys.color.on-surface` |  |  |
| md.comp.checkbox.selected.pressed.state-layer.opacity | OPACITY | Pressed (ripple) / State layer | `md.sys.state.pressed.state-layer-opacity` |  |  |
| md.comp.checkbox.error.pressed.state-layer.color | COLOR | Pressed (ripple) / State layer | `md.sys.color.error` |  |  |
| md.comp.checkbox.error.pressed.state-layer.opacity | OPACITY | Pressed (ripple) / State layer | `md.sys.state.pressed.state-layer-opacity` |  |  |
| md.comp.checkbox.selected.pressed.icon.color | COLOR | Pressed (ripple) / Icon | `md.sys.color.on-primary` |  |  |
| md.comp.checkbox.selected.error.pressed.icon.color | COLOR | Pressed (ripple) / Icon | `md.sys.color.on-error` |  |  |
| md.comp.checkbox.unselected.pressed.icon.color | COLOR | Pressed (ripple) / Icon | `md.sys.color.on-surface` |  | Checkbox changed how rendering was specified, if these are needed, copy to your own tokens. |

## Checkbox

![Diagram of checkbox indicating the 2 parts of its anatomy.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmemmstmc-1-3P.png?alt=media&token=924643e8-47c7-4874-8271-434f69003145>)

_Container; Icon_

## Color

Color values are implemented through design tokens. For design, this means working with color values that correspond with tokens. For implementation, a color value will be a token that references a value. [Learn more about design tokens](../../foundations/design-tokens/overview.md)

![Checkbox color roles in light and dark themes.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmemmtsu5-2-3P.png?alt=media&token=ffcff7c9-bb3d-4de7-be55-117c9cf5fe0b>)

_Checkbox; State-layer; Icon; /_

### Adjacent text label color

Use the color role **on surface** for adjacent text labels. This remains the same even if interacting with the label or component.

![Checkboxes with text labels. The text color is the same for checked and unchecked checkboxes.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmemmvnkr-3-3P.png?alt=media&token=8ead9094-8d12-42db-9365-a68124755b51>)

_The text color remains the same regardless if the checkbox is selected or not_

## States

States are visual representations used to communicate the status of a component or interactive element. [Learn more about interaction states](https://m3.material.io/m3/pages/interaction-states/overview)

![Side by side view of states in light and dark themes.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmemmxoan-4-3P.png?alt=media&token=9dc838c3-495c-4055-a7e4-aca61fe84f4e>)

_Enabled; Disabled; Hovered; Focused; Pressed_

## Measurements

![Diagram of a selected checkbox with a container width and height of 18dp and a state-layer width and height of 40dp.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmemn204f-5-3P.png?alt=media&token=f0da40bb-17ba-4681-9e12-b76629ef8f5e>)

| Attribute | Value |
| --- | --- |
| Container size | 18dp |
| Container corner shape | 2dp |
| Icon size | 18dp |
| Icon alignment | Center-aligned |
| Target size | 48dp |
| State-layer size | 40dp |

---
title: "Radio button"
source_url: "https://m3.material.io/components/radio-button/specs"
lastmod: "2026-02-16"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "components"
assets:
  - "assets/images/be3b2c0d46fc-md8jp5v6-01-3P.png"
  - "assets/images/7ad7a0a2f710-md8jpn14-02-3P.png"
  - "assets/images/ed806f993259-md8jq2c4-03-3P.png"
  - "assets/images/6a5dc5a66fbf-md8jqfvs-04-3P.png"
  - "assets/images/6b77551cb8ae-md8juo16-05-3P.png"
---

# Radio button

Radio buttons let people select one option from a set of options

## Specs

![Diagram of enabled radio button.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmd8jp5v6-01-3P.png?alt=media&token=be9d2b02-1a2a-447a-99ad-3374eda8cc04>)

_Radio button icon_

## Tokens & specs

[Learn more about design tokens](../../foundations/design-tokens/overview.md)

### TOKEN_TABLE

Component: Radio button

Configured context tags: None, Medium contrast, Default, High contrast, 3P, Android, Dark, Light

#### Token sets

| Token set | Name | Type | Tokens | Description |
| --- | --- | --- | --- | --- |
| Radio Button | md.comp.radio-button | COMPONENT | 26 | For radio buttons. |

#### Radio Button (md.comp.radio-button)

For radio buttons.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.radio-button.selected.icon.color | COLOR | Enabled / Icon | `md.sys.color.primary` |  |  |
| md.comp.radio-button.unselected.icon.color | COLOR | Enabled / Icon | `md.sys.color.on-surface-variant` |  |  |
| md.comp.radio-button.icon.size | LENGTH | Enabled / Icon | 20dp |  |  |
| md.comp.radio-button.state-layer.size | LENGTH | Enabled / State layer | 40dp |  |  |
| md.comp.radio-button.disabled.selected.icon.color | COLOR | Disabled / Icon | `md.sys.color.on-surface` |  |  |
| md.comp.radio-button.disabled.selected.icon.opacity | OPACITY | Disabled / Icon | 0.38 |  |  |
| md.comp.radio-button.disabled.unselected.icon.color | COLOR | Disabled / Icon | `md.sys.color.on-surface` |  |  |
| md.comp.radio-button.disabled.unselected.icon.opacity | OPACITY | Disabled / Icon | 0.38 |  |  |
| md.comp.radio-button.selected.hover.state-layer.color | COLOR | Hovered / State layer | `md.sys.color.primary` |  |  |
| md.comp.radio-button.selected.hover.state-layer.opacity | OPACITY | Hovered / State layer | `md.sys.state.hover.state-layer-opacity` |  |  |
| md.comp.radio-button.unselected.hover.state-layer.color | COLOR | Hovered / State layer | `md.sys.color.on-surface` |  |  |
| md.comp.radio-button.unselected.hover.state-layer.opacity | OPACITY | Hovered / State layer | `md.sys.state.hover.state-layer-opacity` |  |  |
| md.comp.radio-button.selected.hover.icon.color | COLOR | Hovered / Icon | `md.sys.color.primary` |  |  |
| md.comp.radio-button.unselected.hover.icon.color | COLOR | Hovered / Icon | `md.sys.color.on-surface` |  |  |
| md.comp.radio-button.selected.focus.state-layer.color | COLOR | Focused / State layer | `md.sys.color.primary` |  |  |
| md.comp.radio-button.selected.focus.state-layer.opacity | OPACITY | Focused / State layer | `md.sys.state.focus.state-layer-opacity` |  |  |
| md.comp.radio-button.unselected.focus.state-layer.color | COLOR | Focused / State layer | `md.sys.color.on-surface` |  |  |
| md.comp.radio-button.unselected.focus.state-layer.opacity | OPACITY | Focused / State layer | `md.sys.state.focus.state-layer-opacity` |  |  |
| md.comp.radio-button.selected.focus.icon.color | COLOR | Focused / Icon | `md.sys.color.primary` |  |  |
| md.comp.radio-button.unselected.focus.icon.color | COLOR | Focused / Icon | `md.sys.color.on-surface` |  |  |
| md.comp.radio-button.selected.pressed.state-layer.color | COLOR | Pressed (ripple) / State layer | `md.sys.color.on-surface` |  |  |
| md.comp.radio-button.selected.pressed.state-layer.opacity | OPACITY | Pressed (ripple) / State layer | `md.sys.state.pressed.state-layer-opacity` |  |  |
| md.comp.radio-button.unselected.pressed.state-layer.color | COLOR | Pressed (ripple) / State layer | `md.sys.color.primary` |  |  |
| md.comp.radio-button.unselected.pressed.state-layer.opacity | OPACITY | Pressed (ripple) / State layer | `md.sys.state.pressed.state-layer-opacity` |  |  |
| md.comp.radio-button.selected.pressed.icon.color | COLOR | Pressed (ripple) / Icon | `md.sys.color.primary` |  |  |
| md.comp.radio-button.unselected.pressed.icon.color | COLOR | Pressed (ripple) / Icon | `md.sys.color.on-surface` |  |  |

## Color

Color values are implemented through design tokens. For design, this means working with color values that correspond with tokens. For implementation, a color value will be a token that references a value. [Learn more about design tokens](../../foundations/design-tokens/overview.md)

![Diagram of selected and unselected radio button colors.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmd8jpn14-02-3P.png?alt=media&token=07b7a3f4-c11b-4a24-b356-9b7665723560>)

_Radio button color roles used for light and dark themes: Primary; On surface variant; /_

### Adjacent text label color

Use the color role **on surface** for adjacent text labels. This remains the same even if interacting with the label or component.

![Radio buttons with labels. The labels are the same color for both selected and unselected radio buttons.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmd8jq2c4-03-3P.png?alt=media&token=5139c7bd-b6b7-482a-8ec7-6f0ab70be705>)

_The text color remains the same regardless if the button is selected or not_

## States

States are visual representations used to communicate the status of a component or interactive element. [Learn more about interaction states](https://m3.material.io/m3/pages/interaction-states/overview)

![Diagram of radio button states including enabled, hover, focus, pressed, and disabled.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmd8jqfvs-04-3P.png?alt=media&token=1b0973c2-4f09-49a9-8d23-17c22fa18ead>)

_Enabled; Hover; Focus; Pressed; Disabled_

[State specs are in the token module above](specs.md#3eef19a6-cdcb-4ecf-b1af-2b8095d485ac)

## Measurements

![Diagram of radio button layout values.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmd8juo16-05-3P.png?alt=media&token=9241a630-7ef4-4e04-8741-21d6ea679187>)

_Radio button size measurements_

| Attribute | Value |
| --- | --- |
| Icon size | 20dp |
| State layer size | 40dp |
| Target size | 48dp |

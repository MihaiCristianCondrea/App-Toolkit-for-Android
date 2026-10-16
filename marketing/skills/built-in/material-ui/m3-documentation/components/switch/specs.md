---
title: "Switch"
source_url: "https://m3.material.io/components/switch/specs"
lastmod: "2026-01-06"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "components"
assets:
  - "assets/images/6f5d06fe436f-mg66415n-01-3P.png"
  - "assets/images/91d6f68e8e51-mg66ckmz-02-3P.png"
  - "assets/images/ed601f47c795-mg66fz7u-03-3P.png"
  - "assets/images/0b2419a2bd7c-mg66iwsh-04-3P.png"
  - "assets/images/e7651cbc1b3e-mg66jzjg-05-3P.png"
  - "assets/images/b6027c0c94c7-mg66kbil-06-3P.png"
  - "assets/images/50964c685b37-mg66mxl5-07-3P.png"
  - "assets/images/a4a09dc75860-mg66nbzr-08-3P.png"
  - "assets/images/240ac99d8660-mg66of2m-09-3P.png"
---

# Switch

Switches toggle the selection of an item on or off

## Specs

![3 elements of a switch.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmg66415n-01-3P.png?alt=media&token=27830702-c236-42e4-9f51-90de8d2b302c>)

_Track; Handle (formerly "thumb"); Icon_

## Tokens & specs

Browse the component elements, attributes, tokens, and their values. [Learn more about design tokens](../../foundations/design-tokens/overview.md)

### TOKEN_TABLE

Component: Switch

Configured context tags: None, Medium contrast, Default, High contrast, 3P, Android, Dark, Light

#### Token sets

| Token set | Name | Type | Tokens | Description |
| --- | --- | --- | --- | --- |
| Switch | md.comp.switch | COMPONENT | 78 | For switches. |

#### Switch (md.comp.switch)

For switches.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.switch.selected.icon.color | COLOR | Enabled / Icon | `md.sys.color.primary` |  |  |
| md.comp.switch.selected.icon.size | LENGTH | Enabled / Icon | 16dp |  |  |
| md.comp.switch.unselected.icon.color | COLOR | Enabled / Icon | `md.sys.color.surface-container-highest` |  |  |
| md.comp.switch.unselected.icon.size | LENGTH | Enabled / Icon | 16dp |  |  |
| md.comp.switch.track.height | LENGTH | Enabled / Track | 32dp |  |  |
| md.comp.switch.track.width | LENGTH | Enabled / Track | 52dp |  |  |
| md.comp.switch.unselected.track.outline.color | COLOR | Enabled / Track | `md.sys.color.outline` |  |  |
| md.comp.switch.track.outline.width | LENGTH | Enabled / Track | 2dp |  |  |
| md.comp.switch.track.shape | SHAPE | Enabled / Track | `md.sys.shape.corner.full` |  |  |
| md.comp.switch.selected.track.color | COLOR | Enabled / Track | `md.sys.color.primary` | Theme: Dark, Platform: iOS, Contrast: Medium contrast: `md.ref.palette.primary60`<br>Theme: Dark, Platform: iOS, Contrast: Default: `md.ref.palette.primary50` |  |
| md.comp.switch.unselected.track.color | COLOR | Enabled / Track | `md.sys.color.surface-container-highest` |  |  |
| md.comp.switch.unselected.handle.height | LENGTH | Enabled / Handle | 16dp |  |  |
| md.comp.switch.with-icon.handle.height | LENGTH | Enabled / Handle | 24dp |  |  |
| md.comp.switch.selected.handle.height | LENGTH | Enabled / Handle | 24dp |  |  |
| md.comp.switch.pressed.handle.height | LENGTH | Enabled / Handle | 28dp |  |  |
| md.comp.switch.unselected.handle.width | LENGTH | Enabled / Handle | 16dp |  |  |
| md.comp.switch.with-icon.handle.width | LENGTH | Enabled / Handle | 24dp |  |  |
| md.comp.switch.selected.handle.width | LENGTH | Enabled / Handle | 24dp |  |  |
| md.comp.switch.pressed.handle.width | LENGTH | Enabled / Handle | 28dp |  |  |
| md.comp.switch.handle.shape | SHAPE | Enabled / Handle | `md.sys.shape.corner.full` |  |  |
| md.comp.switch.selected.handle.color | COLOR | Enabled / Handle | `md.sys.color.on-primary` |  |  |
| md.comp.switch.unselected.handle.color | COLOR | Enabled / Handle | `md.sys.color.outline` |  |  |
| md.comp.switch.handle.shadow-color | COLOR | Enabled / Handle | `md.sys.color.shadow` |  | The Material Switch component has been updated to a new design. Deprecated tokens are no longer required, new tokens have been added and token values updated. The new tokens only correspond to the new design |
| md.comp.switch.handle.elevation | ELEVATION | Enabled / Handle | `md.sys.elevation.level1` |  | The Material Switch component has been updated to a new design. Deprecated tokens are no longer required, new tokens have been added and token values updated. The new tokens only correspond to the new design |
| md.comp.switch.handle.height | LENGTH | Enabled / Handle | 20dp |  | The Material Switch component has been updated to a new design. Deprecated tokens are no longer required, new tokens have been added and token values updated. The new tokens only correspond to the new design |
| md.comp.switch.handle.width | LENGTH | Enabled / Handle | 20dp |  | The Material Switch component has been updated to a new design. Deprecated tokens are no longer required, new tokens have been added and token values updated. The new tokens only correspond to the new design |
| md.comp.switch.state-layer.size | LENGTH | Enabled / State layer | 40dp |  |  |
| md.comp.switch.state-layer.shape | SHAPE | Enabled / State layer | `md.sys.shape.corner.full` |  |  |
| md.comp.switch.disabled.selected.icon.color | COLOR | Disabled / Icon | `md.sys.color.on-surface` |  |  |
| md.comp.switch.disabled.selected.icon.opacity | OPACITY | Disabled / Icon | 0.38 |  |  |
| md.comp.switch.disabled.unselected.icon.color | COLOR | Disabled / Icon | `md.sys.color.surface-container-highest` |  |  |
| md.comp.switch.disabled.unselected.icon.opacity | OPACITY | Disabled / Icon | 0.38 |  |  |
| md.comp.switch.disabled.track.opacity | OPACITY | Disabled / Track | 0.12 |  |  |
| md.comp.switch.disabled.selected.track.color | COLOR | Disabled / Track | `md.sys.color.on-surface` |  |  |
| md.comp.switch.disabled.unselected.track.color | COLOR | Disabled / Track | `md.sys.color.surface-container-highest` |  |  |
| md.comp.switch.disabled.unselected.track.outline.color | COLOR | Disabled / Track | `md.sys.color.on-surface` |  |  |
| md.comp.switch.disabled.unselected.handle.opacity | OPACITY | Disabled / Handle | 0.38 |  |  |
| md.comp.switch.disabled.selected.handle.opacity | OPACITY | Disabled / Handle | 1 |  |  |
| md.comp.switch.disabled.selected.handle.color | COLOR | Disabled / Handle | `md.sys.color.surface` |  |  |
| md.comp.switch.disabled.unselected.handle.color | COLOR | Disabled / Handle | `md.sys.color.on-surface` |  |  |
| md.comp.switch.disabled.handle.opacity | OPACITY | Disabled / Handle | 0.38 |  | The Material Switch component has been updated to a new design. Deprecated tokens are no longer required, new tokens have been added and token values updated. The new tokens only correspond to the new design |
| md.comp.switch.disabled.handle.elevation | ELEVATION | Disabled / Handle | `md.sys.elevation.level0` |  | The Material Switch component has been updated to a new design. Deprecated tokens are no longer required, new tokens have been added and token values updated. The new tokens only correspond to the new design |
| md.comp.switch.selected.hover.icon.color | COLOR | Hovered / Icon | `md.sys.color.primary` |  |  |
| md.comp.switch.unselected.hover.icon.color | COLOR | Hovered / Icon | `md.sys.color.surface-container-highest` |  |  |
| md.comp.switch.selected.hover.track.color | COLOR | Hovered / Track | `md.sys.color.primary` |  |  |
| md.comp.switch.selected.hover.state-layer.color | COLOR | Hovered / Track | `md.sys.color.primary` |  |  |
| md.comp.switch.selected.hover.state-layer.opacity | OPACITY | Hovered / Track | `md.sys.state.hover.state-layer-opacity` |  |  |
| md.comp.switch.unselected.hover.track.color | COLOR | Hovered / Track | `md.sys.color.surface-container-highest` |  |  |
| md.comp.switch.unselected.hover.track.outline.color | COLOR | Hovered / Track | `md.sys.color.outline` |  |  |
| md.comp.switch.unselected.hover.state-layer.color | COLOR | Hovered / Track | `md.sys.color.on-surface` |  |  |
| md.comp.switch.unselected.hover.state-layer.opacity | OPACITY | Hovered / Track | `md.sys.state.hover.state-layer-opacity` |  |  |
| md.comp.switch.selected.hover.handle.color | COLOR | Hovered / Handle | `md.sys.color.primary-container` |  |  |
| md.comp.switch.unselected.hover.handle.color | COLOR | Hovered / Handle | `md.sys.color.on-surface-variant` |  |  |
| md.comp.switch.focus.indicator.color | COLOR | Focused / Focus indicator | `md.sys.color.secondary` |  |  |
| md.comp.switch.focus.indicator.thickness | LENGTH | Focused / Focus indicator | `md.sys.state.focus-indicator.thickness` |  |  |
| md.comp.switch.focus.indicator.offset | LENGTH | Focused / Focus indicator | `md.sys.state.focus-indicator.outer-offset` |  |  |
| md.comp.switch.selected.focus.icon.color | COLOR | Focused / Icon | `md.sys.color.primary` |  |  |
| md.comp.switch.unselected.focus.icon.color | COLOR | Focused / Icon | `md.sys.color.surface-container-highest` |  |  |
| md.comp.switch.selected.focus.track.color | COLOR | Focused / Track | `md.sys.color.primary` |  |  |
| md.comp.switch.selected.focus.state-layer.color | COLOR | Focused / Track | `md.sys.color.primary` |  |  |
| md.comp.switch.selected.focus.state-layer.opacity | OPACITY | Focused / Track | `md.sys.state.focus.state-layer-opacity` |  |  |
| md.comp.switch.unselected.focus.track.color | COLOR | Focused / Track | `md.sys.color.surface-container-highest` |  |  |
| md.comp.switch.unselected.focus.track.outline.color | COLOR | Focused / Track | `md.sys.color.outline` |  |  |
| md.comp.switch.unselected.focus.state-layer.color | COLOR | Focused / Track | `md.sys.color.on-surface` |  |  |
| md.comp.switch.unselected.focus.state-layer.opacity | OPACITY | Focused / Track | `md.sys.state.focus.state-layer-opacity` |  |  |
| md.comp.switch.selected.focus.handle.color | COLOR | Focused / Handle | `md.sys.color.primary-container` |  |  |
| md.comp.switch.unselected.focus.handle.color | COLOR | Focused / Handle | `md.sys.color.on-surface-variant` |  |  |
| md.comp.switch.selected.pressed.icon.color | COLOR | Pressed (ripple) / Icon | `md.sys.color.primary` |  |  |
| md.comp.switch.unselected.pressed.icon.color | COLOR | Pressed (ripple) / Icon | `md.sys.color.surface-container-highest` |  |  |
| md.comp.switch.selected.pressed.track.color | COLOR | Pressed (ripple) / Track | `md.sys.color.primary` |  |  |
| md.comp.switch.selected.pressed.state-layer.color | COLOR | Pressed (ripple) / Track | `md.sys.color.primary` |  |  |
| md.comp.switch.selected.pressed.state-layer.opacity | OPACITY | Pressed (ripple) / Track | `md.sys.state.pressed.state-layer-opacity` |  |  |
| md.comp.switch.unselected.pressed.track.color | COLOR | Pressed (ripple) / Track | `md.sys.color.surface-container-highest` |  |  |
| md.comp.switch.unselected.pressed.track.outline.color | COLOR | Pressed (ripple) / Track | `md.sys.color.outline` |  |  |
| md.comp.switch.unselected.pressed.state-layer.color | COLOR | Pressed (ripple) / Track | `md.sys.color.on-surface` |  |  |
| md.comp.switch.unselected.pressed.state-layer.opacity | OPACITY | Pressed (ripple) / Track | `md.sys.state.pressed.state-layer-opacity` |  |  |
| md.comp.switch.selected.pressed.handle.color | COLOR | Pressed (ripple) / Handle | `md.sys.color.primary-container` |  |  |
| md.comp.switch.unselected.pressed.handle.color | COLOR | Pressed (ripple) / Handle | `md.sys.color.on-surface-variant` |  |  |

## Color

Color values are implemented through design tokens. For design, this means working with color values that correspond with tokens. For implementation, a color value will be a token that references a value. [Learn more about design tokens](https://m3.material.io/m3/pages/design-tokens/overview/)

![6 color roles of a switch in light and dark themes.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmg66ckmz-02-3P.png?alt=media&token=a6c6fc8c-15bd-4364-bd5e-f9ba0422a8a4>)

_Switch color roles used for light and dark themes: Surface container highest; Outline; Outline; Primary; On primary; On primary container_

### Adjacent text label color

Use the color role **on surface** for adjacent text labels. This remains the same even if interacting with the label or component.

![The large body text adjacent to switches uses "on surface" color and the body text uses "on surface variant."](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmg66fz7u-03-3P.png?alt=media&token=2c476a53-996e-45b0-b337-e2b0cd25e166>)

_The text label uses on surface. Supporting text may use on surface variant._

## States

States are visual representations used to communicate the status of a component or interactive element. [Learn more about interaction states](https://m3.material.io/m3/pages/interaction-states)

![5 states of a switch shown in light and dark themes.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmg66iwsh-04-3P.png?alt=media&token=45f45be7-4760-4f45-b752-da18d5f646e4>)

_Enabled; Hovered; Focused; Pressed; Disabled_

[State specs are in the token module above](specs.md#3708644e-b4d7-4237-bb0a-7afeeae4a9b0)

## Measurements

![Measurements of switches without icons.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmg66jzjg-05-3P.png?alt=media&token=03d410e2-0e07-4deb-b2d1-bffaf49c4e13>)

_Switches without icons_

![Measurements of pressed switches without icons.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmg66kbil-06-3P.png?alt=media&token=0a28f42d-8777-4c2f-87c8-eccad6e33f66>)

_Pressed switches without icons_

![Measurements of switches with icons.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmg66mxl5-07-3P.png?alt=media&token=5d9528c6-a0a0-4a9a-bad7-6247ad057d37>)

_Switches with icons_

![Measurements of pressed switches with icons.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmg66nbzr-08-3P.png?alt=media&token=aabf8a3a-33eb-4ed4-8992-3de1ad2e7e57>)

_Pressed switches with icons_

| Element | Attribute | Value |
| --- | --- | --- |
| Track | Height | 32dp |
| Width | 52dp |  |
| Outline width | 2dp |  |
| Shape | [md.sys.shape.corner.full](../../styles/shape/corner-radius-scale.md#56e2bfb5-4bec-49bd-b3a3-bd822c8ab88e) |  |
| Handle | Height (unselected) | 16dp |
| Height - with icon | 24dp |  |
| Height (selected) | 24dp |  |
| Height (pressed) | 28dp |  |
| Width (unselected) | 16dp |  |
| Width - with icon | 24dp |  |
| Width (selected) | 24dp |  |
| Width (pressed) | 28dp |  |
| Shape | [md.sys.shape.corner.full](../../styles/shape/corner-radius-scale.md#56e2bfb5-4bec-49bd-b3a3-bd822c8ab88e) |  |
| State layer | Size | 40dp |
| Shape | [md.sys.shape.corner.full](../../styles/shape/corner-radius-scale.md#56e2bfb5-4bec-49bd-b3a3-bd822c8ab88e) |  |
| Target | Size | 48dp |
| Icon | Size (selected) | 16dp |
| Icon | Size (unselected) | 16dp |

## Configurations

1.  Without icons
2.  Icon on selected switch
3.  Icon on selected and unselected switch

![3 example switches with and without icons in on and off states. ](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmg66of2m-09-3P.png?alt=media&token=299c633b-e204-4717-bc64-c54b7a5100e6>)

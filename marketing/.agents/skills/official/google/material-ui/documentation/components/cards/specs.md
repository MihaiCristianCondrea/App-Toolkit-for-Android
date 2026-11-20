---
title: "Cards"
source_url: "https://m3.material.io/components/cards/specs"
lastmod: "2026-05-18"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "components"
assets:
  - "assets/images/d3b65a742fa5-lx8i6cjo-1.png"
  - "assets/images/4acfd35b59f2-lx8i798w-2.png"
  - "assets/images/e2439e0cb022-m8tathzk-3.png"
  - "assets/images/06e1e704e999-lx8i88xc-4.png"
  - "assets/images/9035fedcb9ce-lx8i8kna-5.png"
  - "assets/images/0d00c3df1353-m8taswqd-7.png"
  - "assets/images/bc320c9ff678-lx8ia1sc-8.png"
  - "assets/images/68c8d097f3d2-lx8iah38-9.png"
  - "assets/images/8fa0f53ed2e4-m8taue2l-10.png"
  - "assets/images/8be3c740b1f0-lx8ibavc-11.png"
---

# Cards

Cards display content and actions about a single subject

## Specs

## Tokens & specs

Select a component variant below to see its elements, attributes, tokens, and their values.

### TOKEN_TABLE

Component: Cards

Configured context tags: None, Medium contrast, Default, High contrast, 3P, Android, Dark, Light

#### Token sets

| Token set | Name | Type | Tokens | Description |
| --- | --- | --- | --- | --- |
| Card - Filled | md.comp.filled-card | COMPONENT | 25 | For filled cards. |
| Card - Elevated | md.comp.elevated-card | COMPONENT | 25 | For elevated cards. |
| Card - Outlined | md.comp.outlined-card | COMPONENT | 31 | For outlined cards. |

#### Card - Filled (md.comp.filled-card)

For filled cards.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.filled-card.container.color | COLOR | Enabled / Container | `md.sys.color.surface-container-highest` |  |  |
| md.comp.filled-card.container.elevation | ELEVATION | Enabled / Container | `md.sys.elevation.level0` |  |  |
| md.comp.filled-card.container.shape | SHAPE | Enabled / Container | `md.sys.shape.corner.medium` |  |  |
| md.comp.filled-card.container.shadow-color | COLOR | Enabled / Container | `md.sys.color.shadow` |  |  |
| md.comp.filled-card.container.surface-tint-layer.color | COLOR | Enabled / Container | `md.sys.color.surface-tint` |  | Bug fix. Removing surface-tint token, since the tint does not apply to components using surface-variant as container color. |
| md.comp.filled-card.icon.color | COLOR | Enabled / Icon | `md.sys.color.primary` |  |  |
| md.comp.filled-card.icon.size | LENGTH | Enabled / Icon | 24dp |  |  |
| md.comp.filled-card.disabled.container.elevation | ELEVATION | Disabled / Container | `md.sys.elevation.level0` |  |  |
| md.comp.filled-card.disabled.container.opacity | OPACITY | Disabled / Container | 0.38 |  |  |
| md.comp.filled-card.disabled.container.color | COLOR | Disabled / Container | `md.sys.color.surface-variant` |  |  |
| md.comp.filled-card.hover.container.elevation | ELEVATION | Hovered / Container | `md.sys.elevation.level1` |  |  |
| md.comp.filled-card.hover.state-layer.color | COLOR | Hovered / State layer | `md.sys.color.on-surface` |  |  |
| md.comp.filled-card.hover.state-layer.opacity | OPACITY | Hovered / State layer | `md.sys.state.hover.state-layer-opacity` |  |  |
| md.comp.filled-card.focus.indicator.color | COLOR | Focused / Focus indicator | `md.sys.color.secondary` |  |  |
| md.comp.filled-card.focus.indicator.thickness | LENGTH | Focused / Focus indicator | `md.sys.state.focus-indicator.thickness` |  |  |
| md.comp.filled-card.focus.indicator.outline.offset | LENGTH | Focused / Focus indicator | `md.sys.state.focus-indicator.outer-offset` |  |  |
| md.comp.filled-card.focus.container.elevation | ELEVATION | Focused / Container | `md.sys.elevation.level0` |  |  |
| md.comp.filled-card.focus.state-layer.color | COLOR | Focused / State layer | `md.sys.color.on-surface` |  |  |
| md.comp.filled-card.focus.state-layer.opacity | OPACITY | Focused / State layer | `md.sys.state.focus.state-layer-opacity` |  |  |
| md.comp.filled-card.pressed.container.elevation | ELEVATION | Pressed (ripple) / Container | `md.sys.elevation.level0` |  |  |
| md.comp.filled-card.pressed.state-layer.color | COLOR | Pressed (ripple) / State layer | `md.sys.color.on-surface` |  |  |
| md.comp.filled-card.pressed.state-layer.opacity | OPACITY | Pressed (ripple) / State layer | `md.sys.state.pressed.state-layer-opacity` |  |  |
| md.comp.filled-card.dragged.container.elevation | ELEVATION | Dragged / Container | `md.sys.elevation.level3` |  |  |
| md.comp.filled-card.dragged.state-layer.color | COLOR | Dragged / State layer | `md.sys.color.on-surface` |  |  |
| md.comp.filled-card.dragged.state-layer.opacity | OPACITY | Dragged / State layer | `md.sys.state.dragged.state-layer-opacity` |  |  |

#### Card - Elevated (md.comp.elevated-card)

For elevated cards.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.elevated-card.container.color | COLOR | Enabled / Container | `md.sys.color.surface-container-low` |  |  |
| md.comp.elevated-card.container.elevation | ELEVATION | Enabled / Container | `md.sys.elevation.level1` |  |  |
| md.comp.elevated-card.container.surface-tint-layer.color | COLOR | Enabled / Container | `md.sys.color.surface-tint` |  | Deprecated as part of the update from opacity based surfaces to tonal surfaces. Surfaces no longer use surface-tint layers for tinting, please use the desired surface role directly as the container color. |
| md.comp.elevated-card.container.shape | SHAPE | Enabled / Container | `md.sys.shape.corner.medium` |  |  |
| md.comp.elevated-card.container.shadow-color | COLOR | Enabled / Container | `md.sys.color.shadow` |  |  |
| md.comp.elevated-card.icon.color | COLOR | Enabled / Icon | `md.sys.color.primary` |  |  |
| md.comp.elevated-card.icon.size | LENGTH | Enabled / Icon | 24dp |  |  |
| md.comp.elevated-card.disabled.container.elevation | ELEVATION | Disabled / Container | `md.sys.elevation.level1` |  |  |
| md.comp.elevated-card.disabled.container.opacity | OPACITY | Disabled / Container | 0.38 |  |  |
| md.comp.elevated-card.disabled.container.color | COLOR | Disabled / Container | `md.sys.color.surface` |  |  |
| md.comp.elevated-card.hover.container.elevation | ELEVATION | Hovered / Container | `md.sys.elevation.level2` |  |  |
| md.comp.elevated-card.hover.state-layer.color | COLOR | Hovered / State layer | `md.sys.color.on-surface` |  |  |
| md.comp.elevated-card.hover.state-layer.opacity | OPACITY | Hovered / State layer | `md.sys.state.hover.state-layer-opacity` |  |  |
| md.comp.elevated-card.focus.indicator.color | COLOR | Focused / Focus indicator | `md.sys.color.secondary` |  |  |
| md.comp.elevated-card.focus.indicator.thickness | LENGTH | Focused / Focus indicator | `md.sys.state.focus-indicator.thickness` |  |  |
| md.comp.elevated-card.focus.indicator.outline.offset | LENGTH | Focused / Focus indicator | `md.sys.state.focus-indicator.outer-offset` |  |  |
| md.comp.elevated-card.focus.container.elevation | ELEVATION | Focused / Container | `md.sys.elevation.level1` |  |  |
| md.comp.elevated-card.focus.state-layer.color | COLOR | Focused / State layer | `md.sys.color.on-surface` |  |  |
| md.comp.elevated-card.focus.state-layer.opacity | OPACITY | Focused / State layer | `md.sys.state.focus.state-layer-opacity` |  |  |
| md.comp.elevated-card.pressed.container.elevation | ELEVATION | Pressed (ripple) / Container | `md.sys.elevation.level1` |  |  |
| md.comp.elevated-card.pressed.state-layer.color | COLOR | Pressed (ripple) / State layer | `md.sys.color.on-surface` |  |  |
| md.comp.elevated-card.pressed.state-layer.opacity | OPACITY | Pressed (ripple) / State layer | `md.sys.state.pressed.state-layer-opacity` |  |  |
| md.comp.elevated-card.dragged.container.elevation | ELEVATION | Dragged / Container | `md.sys.elevation.level4` |  |  |
| md.comp.elevated-card.dragged.state-layer.color | COLOR | Dragged / State layer | `md.sys.color.on-surface` |  |  |
| md.comp.elevated-card.dragged.state-layer.opacity | OPACITY | Dragged / State layer | `md.sys.state.dragged.state-layer-opacity` |  |  |

#### Card - Outlined (md.comp.outlined-card)

For outlined cards.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.outlined-card.container.color | COLOR | Enabled / Container | `md.sys.color.surface` |  |  |
| md.comp.outlined-card.container.elevation | ELEVATION | Enabled / Container | `md.sys.elevation.level0` |  |  |
| md.comp.outlined-card.container.surface-tint-layer.color | COLOR | Enabled / Container | `md.sys.color.surface-tint` |  | Deprecated as part of the update from opacity based surfaces to tonal surfaces. Surfaces no longer use surface-tint layers for tinting, please use the desired surface role directly as the container color. |
| md.comp.outlined-card.container.shape | SHAPE | Enabled / Container | `md.sys.shape.corner.medium` |  |  |
| md.comp.outlined-card.container.shadow-color | COLOR | Enabled / Outline | `md.sys.color.shadow` |  |  |
| md.comp.outlined-card.outline.width | LENGTH | Enabled / Outline | 1dp |  |  |
| md.comp.outlined-card.outline.color | COLOR | Enabled / Outline | `md.sys.color.outline-variant` |  |  |
| md.comp.outlined-card.icon.color | COLOR | Enabled / Icon | `md.sys.color.primary` |  |  |
| md.comp.outlined-card.icon.size | LENGTH | Enabled / Icon | 24dp |  |  |
| md.comp.outlined-card.disabled.container.elevation | ELEVATION | Disabled / Container | `md.sys.elevation.level0` |  |  |
| md.comp.outlined-card.disabled.outline.color | COLOR | Disabled / Outline | `md.sys.color.outline` |  |  |
| md.comp.outlined-card.disabled.outline.opacity | OPACITY | Disabled / Outline | 0.12 |  |  |
| md.comp.outlined-card.hover.container.elevation | ELEVATION | Hovered / Container | `md.sys.elevation.level1` |  |  |
| md.comp.outlined-card.hover.state-layer.color | COLOR | Hovered / State layer | `md.sys.color.on-surface` |  |  |
| md.comp.outlined-card.hover.state-layer.opacity | OPACITY | Hovered / State layer | `md.sys.state.hover.state-layer-opacity` |  |  |
| md.comp.outlined-card.hover.outline.color | COLOR | Hovered / Outline | `md.sys.color.outline-variant` |  |  |
| md.comp.outlined-card.focus.indicator.thickness | LENGTH | Focused / Focus indicator | `md.sys.state.focus-indicator.thickness` |  |  |
| md.comp.outlined-card.focus.indicator.outline.offset | LENGTH | Focused / Focus indicator | `md.sys.state.focus-indicator.outer-offset` |  |  |
| md.comp.outlined-card.focus.indicator.color | COLOR | Focused / Focus indicator | `md.sys.color.secondary` |  |  |
| md.comp.outlined-card.focus.container.elevation | ELEVATION | Focused / Container | `md.sys.elevation.level0` |  |  |
| md.comp.outlined-card.focus.state-layer.color | COLOR | Focused / State layer | `md.sys.color.on-surface` |  |  |
| md.comp.outlined-card.focus.state-layer.opacity | OPACITY | Focused / State layer | `md.sys.state.focus.state-layer-opacity` |  |  |
| md.comp.outlined-card.focus.outline.color | COLOR | Focused / Outline | `md.sys.color.on-surface` |  |  |
| md.comp.outlined-card.pressed.container.elevation | ELEVATION | Pressed (ripple) / Container | `md.sys.elevation.level0` |  |  |
| md.comp.outlined-card.pressed.state-layer.color | COLOR | Pressed (ripple) / State layer | `md.sys.color.on-surface` |  |  |
| md.comp.outlined-card.pressed.state-layer.opacity | OPACITY | Pressed (ripple) / State layer | `md.sys.state.pressed.state-layer-opacity` |  |  |
| md.comp.outlined-card.pressed.outline.color | COLOR | Pressed (ripple) / Outline | `md.sys.color.outline-variant` |  |  |
| md.comp.outlined-card.dragged.container.elevation | ELEVATION | Dragged / Container | `md.sys.elevation.level3` |  |  |
| md.comp.outlined-card.dragged.state-layer.color | COLOR | Dragged / State layer | `md.sys.color.on-surface` |  |  |
| md.comp.outlined-card.dragged.state-layer.opacity | OPACITY | Dragged / State layer | `md.sys.state.dragged.state-layer-opacity` |  |  |
| md.comp.outlined-card.dragged.outline.color | COLOR | Dragged / Outline | `md.sys.color.outline-variant` |  |  |

## Elevated card

![Diagram indicating elevated card container.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Flx8i6cjo-1.png?alt=media&token=37a9669d-e573-49fc-ae35-4ccabecb1102>)

_Container_

### Elevated card color

Color values are implemented through design tokens. For design, this means working with color values that correspond with tokens. For implementation, a color value will be a token that references a value. [Learn more about design tokens](https://m3.material.io/m3/pages/design-tokens/overview/)

![Color diagram indicating elevated card surface color.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Flx8i798w-2.png?alt=media&token=30b428b3-43f3-4dd9-8048-fea7716c1c65>)

_Elevated card color roles used for light and dark themes: Surface container low_

### Elevated card states

States are visual representations used to communicate the status of a component or interactive element. [Learn more about interaction states](https://m3.material.io/m3/pages/interaction-states)

![Diagram of 5 elevated card states.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fm8tathzk-3.png?alt=media&token=41f8d8f7-c77f-4911-b8d5-a736289fe745>)

_Elevated card states: Hovered; Focused; Pressed; Dragged; Disabled_

## Filled card

![Diagram indicating filled card container.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Flx8i88xc-4.png?alt=media&token=3e4703b7-1619-4058-9bd1-45ac27602cbb>)

_Container_

### Filled card color

Color values are implemented through design tokens. For design, this means working with color values that correspond with tokens. For implementation, a color value will be a token that references a value. [Learn more about design tokens](https://m3.material.io/m3/pages/design-tokens/overview/)

![Color diagram indicating filled card surface color.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Flx8i8kna-5.png?alt=media&token=fc1a890b-971a-424b-a558-b089f8937345>)

_Filled card color roles used for light and dark themes: Surface container highest_

### Filled card states

States are visual representations used to communicate the status of a component or interactive element. [Learn more about interaction states](https://m3.material.io/m3/pages/interaction-states)

![Diagram of 5 filled card states.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fm8taswqd-7.png?alt=media&token=0b047a36-4652-47b9-b0ce-e637b869edc2>)

_Filled card states: Hovered; Focused; Pressed; Dragged; Disabled_

## Outlined card

![Diagram indicating outlined card container and outline.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Flx8ia1sc-8.png?alt=media&token=95f75fa6-624a-4f6c-a9a3-c6116871c37c>)

_Container; Outline_

### Outlined card color

Color values are implemented through design tokens. For design, this means working with color values that correspond with tokens. For implementation, a color value will be a token that references a value. [Learn more about design tokens](https://m3.material.io/m3/pages/design-tokens/overview/)

![Outlined card color roles in light and dark themes.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Flx8iah38-9.png?alt=media&token=77dadefc-e058-4948-a4cd-5f7beff55ed5>)

_Outlined card color roles used for light and dark themes: Surface; Outline variant_

### Outlined card states

States are visual representations used to communicate the status of a component or interactive element. [Learn more about interaction states](https://m3.material.io/m3/pages/interaction-states)

![Diagram of 5 outlined card states.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fm8taue2l-10.png?alt=media&token=ff253d81-6cfb-468f-9a62-ab1e33fa7afa>)

_Outlined card states: Hovered; Focused; Pressed; Dragged; Disabled_

## Measurements

![Diagram with card layout measurements.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Flx8ibavc-11.png?alt=media&token=bcefa149-71c2-41fd-bf75-618453a2c32f>)

_Card padding and size measurements_

| Attribute | Value |
| --- | --- |
| Shape | 12dp corner radius |
| Left/right padding | 16dp |
| Padding between cards | 8dp max |
| Label text alignment | Start-aligned |

---
title: "Navigation bar"
source_url: "https://m3.material.io/components/navigation-bar/specs"
lastmod: "2026-05-18"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "components"
assets:
  - "assets/images/7a35a73c3a4d-ma3onczd-01.png"
  - "assets/images/2310714e9427-mkq5ef63-02-3P.png"
  - "assets/images/e976fc013ec4-ma3oovxg-7.png"
  - "assets/images/c36815af3da3-ma3opmda-1.png"
  - "assets/images/277ec09ee153-ma3oqa88-05.png"
  - "assets/images/b6044cc29e18-mah8b097-3.png"
  - "assets/images/58d1c3864eb2-ma3ovtkk-5.png"
  - "assets/images/ac57511e4374-ma3owffv-6.png"
  - "assets/images/0a65c2e4cd56-ma3ox4jl-3p_nav-bar_anatomy.png"
  - "assets/images/1fdd16920edc-ma3oxof4-3p_nav-bar_color.png"
  - "assets/images/57392fa2a5ea-mamkex4k-11-3P.png"
  - "assets/images/96f11f617ffa-mamkf93e-12-3P.png"
  - "assets/images/f681c2e46356-mamkhglx-13-3P.png"
  - "assets/images/c53ec582ca92-ma3ozytu-3p_nav-bar_config.png"
---

# Navigation bar

Navigation bars let people switch between UI views on smaller devices

## Specs

## Variants

![The recommended flexible navigation bar.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fma3onczd-01.png?alt=media&token=c6b6040c-4cb3-43bd-a7ae-7ed99ed6ae5f>)

_Flexible navigation bar_

### Baseline variants

The baseline nav bar is no longer recommended, and should be replaced by the flexible nav bar, which is shorter and supports horizontal navigation items in medium windows. [View baseline nav bar specs](specs.md#46dc2521-acf0-44e3-bbc0-78dc225b9749)

![1 baseline navigation bar.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmkq5ef63-02-3P.png?alt=media&token=fa54cab6-245c-4733-acaa-c347531501cf>)

_Navigation bar (not recommended)_

| Variant | M3 | M3 Expressive |
| --- | --- | --- |
| Flexible navigation bar | \-- | Available |
| Navigation bar | Available | Not recommended. Use **flexible navigation bar**. |

## Configurations

In compact windows, navigation bars use vertical items. In medium windows, navigation bars should use horizontal items.

![Two size configurations for navigation bar and items.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fma3oovxg-7.png?alt=media&token=b9d08a75-97c1-4081-801e-182ef6eff927>)

_Vertical navigation items; Horizontal navigation items_

| Category | Configuration | M3 | M3 Expressive |
| --- | --- | --- | --- |
| Navigation item layout | Vertical (default) | Available | Available |
| Horizontal | \-- | Available |  |

## Tokens & specs

Use the table's menu to switch between token sets for the navigation bar and the nav items. [](specs.md#3425f33a-0b11-492a-ae5a-40d63f939384)[Learn about design tokens](https://m3.material.io/m3/pages/design-tokens/overview/)

### TOKEN_TABLE

Component: Navigation bar

Configured context tags: None, Medium contrast, Default, High contrast, 3P, Android, Dark, Light

#### Token sets

| Token set | Name | Type | Tokens | Description |
| --- | --- | --- | --- | --- |
| Nav bar - Common | md.comp.nav-bar | COMPONENT | 23 | Common tokens for navigation bars. |
| Nav bar - Item - Horizontal | md.comp.nav-bar.item.horizontal | COMPONENT | 5 | For horizontal nav items, where elements are side-by-side. |
| Nav bar - Item - Vertical | md.comp.nav-bar.item.vertical | COMPONENT | 5 | For vertical nav items, where elements are stacked. |

#### Nav bar - Common (md.comp.nav-bar)

Common tokens for navigation bars.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.nav-bar.item.active.hovered.state-layer.color | COLOR | Color / Hovered | `md.sys.color.on-secondary-container` |  |  |
| md.comp.nav-bar.item.active.hovered.state-layer.opacity | OPACITY | Color / Hovered | `md.sys.state.hover.state-layer-opacity` |  |  |
| md.comp.nav-bar.item.inactive.hovered.state-layer.color | COLOR | Color / Hovered | `md.sys.color.on-secondary-container` |  |  |
| md.comp.nav-bar.item.active.focused.state-layer.color | COLOR | Color / Focused | `md.sys.color.on-secondary-container` |  |  |
| md.comp.nav-bar.item.active.focused.state-layer.opacity | OPACITY | Color / Focused | `md.sys.state.focus.state-layer-opacity` |  |  |
| md.comp.nav-bar.item.inactive.focused.state-layer.color | COLOR | Color / Focused | `md.sys.color.on-secondary-container` |  |  |
| md.comp.nav-bar.item.active.pressed.state-layer.color | COLOR | Color / Pressed | `md.sys.color.on-secondary-container` |  |  |
| md.comp.nav-bar.item.active.pressed.state-layer.opacity | OPACITY | Color / Pressed | `md.sys.state.pressed.state-layer-opacity` |  |  |
| md.comp.nav-bar.item.inactive.pressed.state-layer.color | COLOR | Color / Pressed | `md.sys.color.on-secondary-container` |  |  |
| md.comp.nav-bar.container.elevation | ELEVATION | Color / Enabled | `md.sys.elevation.level2` |  |  |
| md.comp.nav-bar.container.shadow-color | COLOR | Color / Enabled | `md.sys.color.shadow` |  |  |
| md.comp.nav-bar.container.color | COLOR | Color / Enabled | `md.sys.color.surface-container` |  |  |
| md.comp.nav-bar.item.active.indicator.color | COLOR | Color / Enabled | `md.sys.color.secondary-container` |  |  |
| md.comp.nav-bar.item.active.label-text.color | COLOR | Color / Enabled | `md.sys.color.secondary` |  |  |
| md.comp.nav-bar.item.inactive.label-text.color | COLOR | Color / Enabled | `md.sys.color.on-surface-variant` |  |  |
| md.comp.nav-bar.item.active.icon.color | COLOR | Color / Enabled | `md.sys.color.on-secondary-container` |  |  |
| md.comp.nav-bar.item.inactive.icon.color | COLOR | Color / Enabled | `md.sys.color.on-surface-variant` |  |  |
| md.comp.nav-bar.item.between-space | LENGTH | Nav item | 0dp |  |  |
| md.comp.nav-bar.item.active-indicator.shape | SHAPE | Nav item | `md.sys.shape.corner.full` |  |  |
| md.comp.nav-bar.item.active-indicator.icon-label-space | LENGTH | Nav item | 4dp |  |  |
| md.comp.nav-bar.item.icon.size | LENGTH | Nav item | 24dp |  |  |
| md.comp.nav-bar.container.height | LENGTH | Container | 64dp |  |  |
| md.comp.nav-bar.container.shape | SHAPE | Container | `md.sys.shape.corner.none` |  |  |

#### Nav bar - Item - Horizontal (md.comp.nav-bar.item.horizontal)

For horizontal nav items, where elements are side-by-side.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.nav-bar.item.horizontal.label-text.font | TYPOGRAPHY |  | `md.sys.typescale.label-medium` |  |  |
| md.comp.nav-bar.item.horizontal.active-indicator.height | LENGTH |  | 40dp |  |  |
| md.comp.nav-bar.item.horizontal.active-indicator.leading-space | LENGTH |  | 16dp |  |  |
| md.comp.nav-bar.item.horizontal.active-indicator.trailing-space | LENGTH |  | 16dp |  |  |
| md.comp.nav-bar.item.horizontal.active-indicator.icon-label-space | LENGTH |  | 4dp |  |  |

#### Nav bar - Item - Vertical (md.comp.nav-bar.item.vertical)

For vertical nav items, where elements are stacked.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.nav-bar.item.vertical.label-text.font | TYPOGRAPHY |  | `md.sys.typescale.label-medium` |  |  |
| md.comp.nav-bar.item.vertical.active-indicator.height | LENGTH |  | 32dp |  |  |
| md.comp.nav-bar.item.vertical.active-indicator.width | LENGTH |  | 56dp |  |  |
| md.comp.nav-bar.item.vertical.container.between-space | LENGTH |  | 6dp |  |  |
| md.comp.nav-bar.item.vertical.active-indicator.icon-label-space | LENGTH |  | 4dp |  |  |

## Anatomy

![Seven elements of the navigation bar.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fma3opmda-1.png?alt=media&token=b21e5714-91c3-48e2-8160-989f3cd62304>)

_Container; Icon; Label text; Active indicator; Small badge (optional); Large badge (optional); Large badge label_

## Color

Color values are implemented through design tokens. For designers, this means working with color values that correspond with tokens; in implementation, a color value will be a token that references a value. [Learn more about design tokens](../../foundations/design-tokens/overview.md)

![Six color roles of the navigation bar.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fma3oqa88-05.png?alt=media&token=c1c5220f-00fe-4af9-84a0-4be55a1696a0>)

_Navigation bar color roles used for light and dark schemes: / Surface container; On-secondary container; Secondary; Secondary container; On-surface variant; On-surface variant_

For badge color roles, go to [badge specs](../badges/specs.md).

## States

States are visual representations used to communicate the status of a component or an interactive element.

![Four states of the navigation bar items.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmah8b097-3.png?alt=media&token=a4fbdf79-02ec-429c-8fe2-bbbbb6060b53>)

_Enabled; Hovered (8% state layer); Focused (10% state layer); Pressed (10% state layer)_

## Measurements

The navigation bar stretches the full window width.

![Navigation bar padding and size measurements.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fma3ovtkk-5.png?alt=media&token=0728e092-f6bd-4ebd-b9d0-00eb4d9e6e4e>)

_Navigation bar padding and size measurements_

Vertical navigation items dynamically change width to equally fit the container. Horizontal navigation items have a fixed width, so extra space is added to the ends of the navigation bar instead.

![Navigation bar and item widths.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fma3owffv-6.png?alt=media&token=1d3f41df-5b3e-43e1-b300-55ca57878008>)

_Navigation bar width and margins for compact and medium windows. / Vertical navigation item; Margin from window edge; Horizontal navigation item_

* * *

## Baseline navigation bar

![7 elements of baseline navigation bar.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fma3ox4jl-3p_nav-bar_anatomy.png?alt=media&token=3331837b-38d7-4348-8309-dd351c92e315>)

_Container; Icon; Label text; Active indicator; Small badge; Large badge; Large badge label_

### Tokens & specs

These tokens are for the baseline navigation bar.

### TOKEN_TABLE

Component: Navigation bar

Configured context tags: None, Medium contrast, Default, High contrast, Static, 3P, Android, Dark, Light

#### Token sets

| Token set | Name | Type | Tokens | Description |
| --- | --- | --- | --- | --- |
| Navigation bar (baseline) | md.comp.navigation-bar | COMPONENT | 52 | For baseline navigation bar. For expressive, use `md.comp.nav-bar` and corresponding `.item` token sets instead. |
| Nav bar - Common | md.comp.nav-bar | COMPONENT | 23 | Common tokens for navigation bars. |
| Nav bar - Item - Vertical | md.comp.nav-bar.item.vertical | COMPONENT | 5 | For vertical nav items, where elements are stacked. |
| Nav bar - Item - Horizontal | md.comp.nav-bar.item.horizontal | COMPONENT | 5 | For horizontal nav items, where elements are side-by-side. |

#### Navigation bar (baseline) (md.comp.navigation-bar)

For baseline navigation bar. For expressive, use `md.comp.nav-bar` and corresponding `.item` token sets instead.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.navigation-bar.focus.indicator.color | COLOR | Enabled / Focus indicator | `md.sys.color.secondary` |  |  |
| md.comp.navigation-bar.focus.indicator.thickness | LENGTH | Enabled / Focus indicator | `md.sys.state.focus-indicator.thickness` |  |  |
| md.comp.navigation-bar.focus.indicator.outline.offset | LENGTH | Enabled / Focus indicator | `md.sys.state.focus-indicator.inner-offset` |  |  |
| md.comp.navigation-bar.container.color | COLOR | Enabled / Container | `md.sys.color.surface-container` |  |  |
| md.comp.navigation-bar.container.shape | SHAPE | Enabled / Container | `md.sys.shape.corner.none` |  |  |
| md.comp.navigation-bar.container.height | LENGTH | Enabled / Container | 80dp |  |  |
| md.comp.navigation-bar.container.elevation | ELEVATION | Enabled / Container | `md.sys.elevation.level2` |  |  |
| md.comp.navigation-bar.container.surface-tint-layer.color | COLOR | Enabled / Container | `md.sys.color.surface-tint` |  | Deprecated as part of the update from opacity based surfaces to tonal surfaces. Surfaces no longer use surface-tint layers for tinting, please use the desired surface role directly as the container color. |
| md.comp.navigation-bar.container.shadow-color | COLOR | Enabled / Container | `md.sys.color.shadow` |  | Bug: should not have been created. Remove any shadows on navigation bars. |
| md.comp.navigation-bar.label-text.font | FONT_NAMES | Enabled / Label text | `md.sys.typescale.label-medium.font` |  |  |
| md.comp.navigation-bar.label-text.line-height | LINE_HEIGHT | Enabled / Label text | `md.sys.typescale.label-medium.line-height` |  |  |
| md.comp.navigation-bar.label-text.size | FONT_SIZE | Enabled / Label text | `md.sys.typescale.label-medium.size` |  |  |
| md.comp.navigation-bar.label-text.weight | FONT_WEIGHT | Enabled / Label text | `md.sys.typescale.label-medium.weight` |  |  |
| md.comp.navigation-bar.active.label-text.weight | FONT_WEIGHT | Enabled / Label text | `md.sys.typescale.label-medium.weight.prominent` |  |  |
| md.comp.navigation-bar.label-text.tracking | FONT_TRACKING | Enabled / Label text | `md.sys.typescale.label-medium.tracking` |  |  |
| md.comp.navigation-bar.label-text.type | TYPOGRAPHY | Enabled / Label text | font name: `md.comp.navigation-bar.label-text.font`, font weight: `md.comp.navigation-bar.label-text.weight`, font size: `md.comp.navigation-bar.label-text.size`, font tracking: `md.comp.navigation-bar.label-text.tracking`, line height: `md.comp.navigation-bar.label-text.line-height` |  |  |
| md.comp.navigation-bar.active.label-text.color | COLOR | Enabled / Label text | `md.sys.color.on-surface` |  |  |
| md.comp.navigation-bar.inactive.label-text.color | COLOR | Enabled / Label text | `md.sys.color.on-surface-variant` |  |  |
| md.comp.navigation-bar.active.hover.label-text.color | COLOR | Enabled / Label text | `md.sys.color.on-surface` |  |  |
| md.comp.navigation-bar.inactive.hover.label-text.color | COLOR | Enabled / Label text | `md.sys.color.on-surface` |  |  |
| md.comp.navigation-bar.active.focus.label-text.color | COLOR | Enabled / Label text | `md.sys.color.on-surface` |  |  |
| md.comp.navigation-bar.inactive.focus.label-text.color | COLOR | Enabled / Label text | `md.sys.color.on-surface` |  |  |
| md.comp.navigation-bar.active.pressed.label-text.color | COLOR | Enabled / Label text | `md.sys.color.on-surface` |  |  |
| md.comp.navigation-bar.inactive.pressed.label-text.color | COLOR | Enabled / Label text | `md.sys.color.on-surface` |  |  |
| md.comp.navigation-bar.icon.size | LENGTH | Enabled / Icon | 24dp |  |  |
| md.comp.navigation-bar.active.icon.color | COLOR | Enabled / Icon | `md.sys.color.on-secondary-container` |  |  |
| md.comp.navigation-bar.inactive.icon.color | COLOR | Enabled / Icon | `md.sys.color.on-surface-variant` |  |  |
| md.comp.navigation-bar.active.hover.icon.color | COLOR | Enabled / Icon | `md.sys.color.on-secondary-container` |  |  |
| md.comp.navigation-bar.inactive.hover.icon.color | COLOR | Enabled / Icon | `md.sys.color.on-surface` |  |  |
| md.comp.navigation-bar.active.focus.icon.color | COLOR | Enabled / Icon | `md.sys.color.on-secondary-container` |  |  |
| md.comp.navigation-bar.inactive.focus.icon.color | COLOR | Enabled / Icon | `md.sys.color.on-surface` |  |  |
| md.comp.navigation-bar.active.pressed.icon.color | COLOR | Enabled / Icon | `md.sys.color.on-secondary-container` |  |  |
| md.comp.navigation-bar.inactive.pressed.icon.color | COLOR | Enabled / Icon | `md.sys.color.on-surface` |  |  |
| md.comp.navigation-bar.badge.color | COLOR | Enabled / [Deprecated] Badge | `md.sys.color.error` |  | Badge values were refactored out into their own token set. Replace usage with the equivalent `md.comp.badge.*` tokens. |
| md.comp.navigation-bar.badge.size | LENGTH | Enabled / [Deprecated] Badge | 6dp |  | Badge values were refactored out into their own token set. Replace usage with the equivalent `md.comp.badge.*` tokens. |
| md.comp.navigation-bar.badge.shape | SHAPE | Enabled / [Deprecated] Badge | rounded corners 3dp |  | Badge values were refactored out into their own token set. Replace usage with the equivalent `md.comp.badge.*` tokens. |
| md.comp.navigation-bar.active-indicator.color | COLOR | Enabled / Active indicator | `md.sys.color.secondary-container` |  |  |
| md.comp.navigation-bar.active-indicator.height | LENGTH | Enabled / Active indicator | 32dp |  |  |
| md.comp.navigation-bar.active-indicator.width | LENGTH | Enabled / Active indicator | 64dp |  |  |
| md.comp.navigation-bar.active-indicator.shape | SHAPE | Enabled / Active indicator | `md.sys.shape.corner.full` |  |  |
| md.comp.navigation-bar.active.hover.state-layer.color | COLOR | Enabled / State layer | `md.sys.color.on-surface` |  |  |
| md.comp.navigation-bar.inactive.hover.state-layer.color | COLOR | Enabled / State layer | `md.sys.color.on-surface` |  |  |
| md.comp.navigation-bar.hover.state-layer.opacity | OPACITY | Enabled / State layer | `md.sys.state.hover.state-layer-opacity` |  |  |
| md.comp.navigation-bar.active.focus.state-layer.color | COLOR | Enabled / State layer | `md.sys.color.on-surface` |  |  |
| md.comp.navigation-bar.inactive.focus.state-layer.color | COLOR | Enabled / State layer | `md.sys.color.on-surface` |  |  |
| md.comp.navigation-bar.focus.state-layer.opacity | OPACITY | Enabled / State layer | `md.sys.state.focus.state-layer-opacity` |  |  |
| md.comp.navigation-bar.active.pressed.state-layer.color | COLOR | Enabled / State layer | `md.sys.color.on-surface` |  |  |
| md.comp.navigation-bar.inactive.pressed.state-layer.color | COLOR | Enabled / State layer | `md.sys.color.on-surface` |  |  |
| md.comp.navigation-bar.pressed.state-layer.opacity | OPACITY | Enabled / State layer | `md.sys.state.pressed.state-layer-opacity` |  |  |
| md.comp.navigation-bar.large-badge.size | LENGTH | Enabled / [Deprecated] Large badge | 16dp |  | Badge values were refactored out into their own token set. Replace usage with the equivalent `md.comp.badge.large.*` tokens. |
| md.comp.navigation-bar.large-badge.shape | SHAPE | Enabled / [Deprecated] Large badge | rounded corners 8dp |  | Badge values were refactored out into their own token set. Replace usage with the equivalent `md.comp.badge.large.*` tokens. |
| md.comp.navigation-bar.large-badge.color | COLOR | Enabled / [Deprecated] Large badge | `md.sys.color.error` |  | Badge values were refactored out into their own token set. Replace usage with the equivalent `md.comp.badge.large.*` tokens. |

#### Nav bar - Common (md.comp.nav-bar)

Common tokens for navigation bars.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.nav-bar.item.active.hovered.state-layer.color | COLOR | Color / Hovered | `md.sys.color.on-secondary-container` |  |  |
| md.comp.nav-bar.item.active.hovered.state-layer.opacity | OPACITY | Color / Hovered | `md.sys.state.hover.state-layer-opacity` |  |  |
| md.comp.nav-bar.item.inactive.hovered.state-layer.color | COLOR | Color / Hovered | `md.sys.color.on-secondary-container` |  |  |
| md.comp.nav-bar.item.active.focused.state-layer.color | COLOR | Color / Focused | `md.sys.color.on-secondary-container` |  |  |
| md.comp.nav-bar.item.active.focused.state-layer.opacity | OPACITY | Color / Focused | `md.sys.state.focus.state-layer-opacity` |  |  |
| md.comp.nav-bar.item.inactive.focused.state-layer.color | COLOR | Color / Focused | `md.sys.color.on-secondary-container` |  |  |
| md.comp.nav-bar.item.active.pressed.state-layer.color | COLOR | Color / Pressed | `md.sys.color.on-secondary-container` |  |  |
| md.comp.nav-bar.item.active.pressed.state-layer.opacity | OPACITY | Color / Pressed | `md.sys.state.pressed.state-layer-opacity` |  |  |
| md.comp.nav-bar.item.inactive.pressed.state-layer.color | COLOR | Color / Pressed | `md.sys.color.on-secondary-container` |  |  |
| md.comp.nav-bar.container.elevation | ELEVATION | Color / Enabled | `md.sys.elevation.level2` |  |  |
| md.comp.nav-bar.container.shadow-color | COLOR | Color / Enabled | `md.sys.color.shadow` |  |  |
| md.comp.nav-bar.container.color | COLOR | Color / Enabled | `md.sys.color.surface-container` |  |  |
| md.comp.nav-bar.item.active.indicator.color | COLOR | Color / Enabled | `md.sys.color.secondary-container` |  |  |
| md.comp.nav-bar.item.active.label-text.color | COLOR | Color / Enabled | `md.sys.color.secondary` |  |  |
| md.comp.nav-bar.item.inactive.label-text.color | COLOR | Color / Enabled | `md.sys.color.on-surface-variant` |  |  |
| md.comp.nav-bar.item.active.icon.color | COLOR | Color / Enabled | `md.sys.color.on-secondary-container` |  |  |
| md.comp.nav-bar.item.inactive.icon.color | COLOR | Color / Enabled | `md.sys.color.on-surface-variant` |  |  |
| md.comp.nav-bar.item.between-space | LENGTH | Nav item | 0dp |  |  |
| md.comp.nav-bar.item.active-indicator.shape | SHAPE | Nav item | `md.sys.shape.corner.full` |  |  |
| md.comp.nav-bar.item.active-indicator.icon-label-space | LENGTH | Nav item | 4dp |  |  |
| md.comp.nav-bar.item.icon.size | LENGTH | Nav item | 24dp |  |  |
| md.comp.nav-bar.container.height | LENGTH | Container | 64dp |  |  |
| md.comp.nav-bar.container.shape | SHAPE | Container | `md.sys.shape.corner.none` |  |  |

#### Nav bar - Item - Vertical (md.comp.nav-bar.item.vertical)

For vertical nav items, where elements are stacked.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.nav-bar.item.vertical.label-text.font | TYPOGRAPHY |  | `md.sys.typescale.label-medium` |  |  |
| md.comp.nav-bar.item.vertical.active-indicator.height | LENGTH |  | 32dp |  |  |
| md.comp.nav-bar.item.vertical.active-indicator.width | LENGTH |  | 56dp |  |  |
| md.comp.nav-bar.item.vertical.container.between-space | LENGTH |  | 6dp |  |  |
| md.comp.nav-bar.item.vertical.active-indicator.icon-label-space | LENGTH |  | 4dp |  |  |

#### Nav bar - Item - Horizontal (md.comp.nav-bar.item.horizontal)

For horizontal nav items, where elements are side-by-side.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.nav-bar.item.horizontal.label-text.font | TYPOGRAPHY |  | `md.sys.typescale.label-medium` |  |  |
| md.comp.nav-bar.item.horizontal.active-indicator.height | LENGTH |  | 40dp |  |  |
| md.comp.nav-bar.item.horizontal.active-indicator.leading-space | LENGTH |  | 16dp |  |  |
| md.comp.nav-bar.item.horizontal.active-indicator.trailing-space | LENGTH |  | 16dp |  |  |
| md.comp.nav-bar.item.horizontal.active-indicator.icon-label-space | LENGTH |  | 4dp |  |  |

### Color

Color values are implemented through design tokens. For designers, this means working with color values that correspond with tokens; in implementation, a color value will be a token that references a value. [Learn more about design tokens](../../foundations/design-tokens/overview.md)

![6 color roles of baseline navigation bar.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fma3oxof4-3p_nav-bar_color.png?alt=media&token=83b54af5-d6c4-4d49-9767-378ca90f188f>)

_Navigation bar color roles used for light and dark schemes: / Surface; On secondary container; On surface; Secondary container; On surface variant; On surface variant_

For badge color roles, go to [badge specs](../badges/specs.md).

### States

States are visual representations used to communicate the status of a component or an interactive element.

![4 states of baseline navigation bar.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmamkex4k-11-3P.png?alt=media&token=4a126b4d-9b65-4afe-9166-580a25fbdde6>)

_Navigation bar states: / Enabled; Hovered; Focused; Pressed_

## Measurements

![Baseline navigation bar padding and size measurements.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmamkf93e-12-3P.png?alt=media&token=f86a7f01-46be-421b-93f1-751cf528b763>)

_Navigation bar padding and size measurements_

![Baseline navigation bar target size and margins.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmamkhglx-13-3P.png?alt=media&token=f19f2b64-055b-4663-8243-f0e0fff8887c>)

_Navigation bar target size and margins_

## Configurations

![3 configurations of the baseline navigation bar.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fma3ozytu-3p_nav-bar_config.png?alt=media&token=81af67b1-6ff4-4eeb-8ae5-7cc46db34645>)

_3 destinations; 4 destinations; 5 destinations_

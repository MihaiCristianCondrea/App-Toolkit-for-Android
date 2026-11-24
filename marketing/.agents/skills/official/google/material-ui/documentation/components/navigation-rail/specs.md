---
title: "Navigation rail"
source_url: "https://m3.material.io/components/navigation-rail/specs"
lastmod: "2026-05-18"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "components"
assets:
  - "assets/images/53fe40f8defa-mal9f2lt-01-3P.png"
  - "assets/images/afa1b241b275-mkvenn9g-02-3P.png"
  - "assets/images/5d576a0c2aef-mal9fbzs-03-3P.png"
  - "assets/images/90c385738ea6-mg0uznol-04-3P.png"
  - "assets/images/5dc4b25d7fee-mmyzgq71-05-3P.png"
  - "assets/images/d153df0cdd67-mal9s5f0-06-3P.png"
  - "assets/images/cf3c6f95c227-mal9sbiq-07-3P.png"
  - "assets/images/332fb514003c-mal9t1mb-08-3P.png"
  - "assets/images/84c1b24eb1cf-mal9twwq-09-3P.png"
  - "assets/images/53268226c34c-mal9u2w3-10-3P.png"
  - "assets/images/6cbbb9848915-mal9uv1q-11-3P.png"
  - "assets/images/cf91569e8a84-mal9vzs8-12-3P.png"
  - "assets/images/2d86b7af4cbb-mal9x9ol-13-3P.png"
  - "assets/images/f99ca485ece5-mal9xiu7-14-3P.png"
  - "assets/images/dcb83794ef4c-mal9xyxt-15-3P.png"
  - "assets/images/4e222d13a79d-mal9yote-16-3P.png"
---

# Navigation rail

Navigation rails let people switch between UI views on mid-sized devices

## Specs

## Variants

![2 variants of navigation rails.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmal9f2lt-01-3P.png?alt=media&token=e3d39033-8b0a-4cfb-834c-708b8a39d735>)

_Collapsed navigation rail; Expanded navigation rail_

### Baseline variants

The baseline navigation rail is no longer recommended, and should be replaced by the collapsed navigation rail. [View baseline tokens](specs.md#d4d97764-20ec-496f-a6f3-0d423940ec5a)

![Baseline navigation rail.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmkvenn9g-02-3P.png?alt=media&token=ef44fc56-2069-4191-8b8e-fdd672e4f136>)

_The baseline navigation rail is no longer recommended_

| Variant | M3 | M3 Expressive |
| --- | --- | --- |
| Collapsed navigation rail | \-- | Available |
| Expanded navigation rail | \-- | Available |
| Navigation rail (baseline) | Available | Not recommended. Use **collapsed navigation rail**. |

## Configurations

![Standard and modal layouts of navigation rail.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmal9fbzs-03-3P.png?alt=media&token=86384321-4a5b-4da1-8b96-269c108187c6>)

_Expanded layout: standard; Expanded layout: modal_

| Category | Configuration | M3 | M3 Expressive |
| --- | --- | --- | --- |
| Expanded layout | Standard (default) | Available as navigation drawer | Available |
| Modal | Available as navigation drawer | Available |  |
| Expanded behavior | Hide when collapsed | \-- | Available |

## Tokens & specs

Browse the component elements, attributes, tokens, and their values. [Learn about design tokens](https://m3.material.io/m3/pages/design-tokens/overview/)

### TOKEN_TABLE

Component: Navigation rail

Configured context tags: None, Medium contrast, Default, High contrast, 3P, Android, Dark, Light

#### Token sets

| Token set | Name | Type | Tokens | Description |
| --- | --- | --- | --- | --- |
| Nav rail - Common | md.comp.nav-rail | COMPONENT | 14 | Common tokens for nav rail. |
| Nav rail - Collapsed | md.comp.nav-rail.collapsed | COMPONENT | 7 | For collapsed nav rail. |
| Nav rail - Expanded | md.comp.nav-rail.expanded | COMPONENT | 11 | For expanded nav rail. |
| Nav rail item - Common | md.comp.nav-rail.item | COMPONENT | 10 | Common tokens for all nav items. |
| Nav rail item - Vertical | md.comp.nav-rail.item.vertical | COMPONENT | 6 | For vertical nav rail items, where elements are stacked. |
| Nav rail item - Horizontal | md.comp.nav-rail.item.horizontal | COMPONENT | 5 | For horizontal nav rail items, where elements are side-by-side. |

#### Nav rail - Common (md.comp.nav-rail)

Common tokens for nav rail.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.nav-rail.item.active.indicator.color | COLOR | Enabled | `md.sys.color.secondary-container` |  |  |
| md.comp.nav-rail.item.active.label-text.color | COLOR | Enabled | `md.sys.color.secondary` |  |  |
| md.comp.nav-rail.item.inactive.label-text.color | COLOR | Enabled | `md.sys.color.on-surface-variant` |  |  |
| md.comp.nav-rail.item.active.icon.color | COLOR | Enabled | `md.sys.color.on-secondary-container` |  |  |
| md.comp.nav-rail.item.inactive.icon.color | COLOR | Enabled | `md.sys.color.on-surface-variant` |  |  |
| md.comp.nav-rail.item.active.hovered.state-layer.color | COLOR | Hovered | `md.sys.color.on-secondary-container` |  |  |
| md.comp.nav-rail.item.active.hovered.state-layer.opacity | OPACITY | Hovered | `md.sys.state.hover.state-layer-opacity` |  |  |
| md.comp.nav-rail.item.inactive.hovered.state-layer.color | COLOR | Hovered | `md.sys.color.on-secondary-container` |  |  |
| md.comp.nav-rail.item.active.focused.state-layer.color | COLOR | Focused | `md.sys.color.on-secondary-container` |  |  |
| md.comp.nav-rail.item.active.focused.state-layer.opacity | OPACITY | Focused | `md.sys.state.focus.state-layer-opacity` |  |  |
| md.comp.nav-rail.item.inactive.focused.state-layer.color | COLOR | Focused | `md.sys.color.on-secondary-container` |  |  |
| md.comp.nav-rail.item.active.pressed.state-layer.color | COLOR | Pressed | `md.sys.color.on-secondary-container` |  |  |
| md.comp.nav-rail.item.active.pressed.state-layer.opacity | OPACITY | Pressed | `md.sys.state.pressed.state-layer-opacity` |  |  |
| md.comp.nav-rail.item.inactive.pressed.state-layer.color | COLOR | Pressed | `md.sys.color.on-secondary-container` |  |  |

#### Nav rail - Collapsed (md.comp.nav-rail.collapsed)

For collapsed nav rail.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.nav-rail.collapsed.container.width | LENGTH |  | 96dp |  |  |
| md.comp.nav-rail.collapsed.narrow.container.width | LENGTH |  | 80dp |  |  |
| md.comp.nav-rail.collapsed.container.elevation | ELEVATION |  | `md.sys.elevation.level0` |  |  |
| md.comp.nav-rail.collapsed.container.shape | SHAPE |  | `md.sys.shape.corner.none` |  |  |
| md.comp.nav-rail.collapsed.container.color | COLOR |  | `md.sys.color.surface` |  |  |
| md.comp.nav-rail.collapsed.item.vertical-space | LENGTH |  | 4dp |  |  |
| md.comp.nav-rail.collapsed.top-space | LENGTH |  | 44dp |  |  |

#### Nav rail - Expanded (md.comp.nav-rail.expanded)

For expanded nav rail.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.nav-rail.expanded.container.width.minimum | LENGTH |  | 220dp |  |  |
| md.comp.nav-rail.expanded.container.width.maximum | LENGTH |  | 360dp |  |  |
| md.comp.nav-rail.expanded.top-space | LENGTH |  | 44dp |  |  |
| md.comp.nav-rail.expanded.container.elevation | ELEVATION |  | `md.sys.elevation.level0` |  |  |
| md.comp.nav-rail.expanded.modal.container.elevation | ELEVATION |  | `md.sys.elevation.level2` |  |  |
| md.comp.nav-rail.expanded.container.color | COLOR |  | `md.sys.color.surface` |  |  |
| md.comp.nav-rail.expanded.modal.container.color | COLOR |  | `md.sys.color.surface-container` |  |  |
| md.comp.nav-rail.expanded.container.shape | SHAPE |  | `md.sys.shape.corner.none` |  |  |
| md.comp.nav-rail.expanded.modal.container.shape | SHAPE |  | `md.sys.shape.corner.large` |  |  |
| md.comp.nav-rail.expanded.between-item-space | LENGTH |  | 0dp |  | Token not needed as value is "0dp" |
| md.comp.nav-rail.expanded.vertical.trailing-space | LENGTH |  | 20dp |  | Token moved to nav rail item horizontal set |

#### Nav rail item - Common (md.comp.nav-rail.item)

Common tokens for all nav items.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.nav-rail.item.icon.size | LENGTH |  | 24dp |  |  |
| md.comp.nav-rail.item.active-indicator.shape | SHAPE |  | `md.sys.shape.corner.full` |  |  |
| md.comp.nav-rail.item.active-indicator.leading-space | LENGTH |  | 16dp |  |  |
| md.comp.nav-rail.item.active-indicator.icon-label-space | LENGTH |  | 8dp |  |  |
| md.comp.nav-rail.item.active-indicator.trailing-space | LENGTH |  | 16dp |  |  |
| md.comp.nav-rail.item.container.height | LENGTH |  | 64dp |  |  |
| md.comp.nav-rail.item.short.container.height | LENGTH |  | 56dp |  |  |
| md.comp.nav-rail.item.container.shape | SHAPE |  | `md.sys.shape.corner.none` |  |  |
| md.comp.nav-rail.item.container.vertical-space | LENGTH |  | 6dp |  |  |
| md.comp.nav-rail.item.header-space-minimum | LENGTH |  | 40dp |  |  |

#### Nav rail item - Vertical (md.comp.nav-rail.item.vertical)

For vertical nav rail items, where elements are stacked.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.nav-rail.item.vertical.active-indicator.height | LENGTH |  | 32dp |  |  |
| md.comp.nav-rail.item.vertical.active-indicator.width | LENGTH |  | 56dp |  |  |
| md.comp.nav-rail.item.vertical.label-text.font | TYPOGRAPHY |  | `md.sys.typescale.label-medium` |  |  |
| md.comp.nav-rail.item.vertical.icon-label-space | LENGTH |  | 4dp |  |  |
| md.comp.nav-rail.item.vertical.leading-space | LENGTH |  | 16dp |  |  |
| md.comp.nav-rail.item.vertical.trailing-space | LENGTH |  | 16dp |  |  |

#### Nav rail item - Horizontal (md.comp.nav-rail.item.horizontal)

For horizontal nav rail items, where elements are side-by-side.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.nav-rail.item.horizontal.label-text.font | TYPOGRAPHY |  | `md.sys.typescale.label-large` |  |  |
| md.comp.nav-rail.item.horizontal.active-indicator.height | LENGTH |  | 56dp |  |  |
| md.comp.nav-rail.item.horizontal.full-width.leading-space | LENGTH |  | 16dp |  |  |
| md.comp.nav-rail.item.horizontal.full-width.trailing-space | LENGTH |  | 16dp |  |  |
| md.comp.nav-rail.item.horizontal.icon-label-space | LENGTH |  | 8dp |  |  |

## Anatomy

![9 elements of collapsed and expanded navigation rails.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmg0uznol-04-3P.png?alt=media&token=bb0f488f-96f8-4128-9266-c8ff9bb70ce2>)

_Collapsed and expanded navigation rail elements: Container; Menu (optional); FAB or Extended FAB (optional); Icon; Active indicator; Label text; Large badge (optional); Large badge label (optional); Small badge (optional)_

## Color

Color values are implemented through design tokens. For designers, this means working with color values that correspond with tokens; in implementation, a color value will be a token that references a value. [Learn more about design tokens](../../foundations/design-tokens/overview.md)

![Color roles of 9 elements of collapsed and expanded navigation rails in light and dark color schemes.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmmyzgq71-05-3P.png?alt=media&token=ac37fe4a-fbe3-4d2d-abf9-f753d00fc5f7>)

_Navigation rail color roles used for light and dark schemes: Surface container (optional); On secondary container; Secondary container; Secondary (vertical), On secondary container (horizontal); On surface variant; On surface variant; Error; On error; Error_

## States

States are visual representations used to communicate the status of a component or an interactive element.

The navigation item’s target area always spans the full width of the nav rail, even if the item container hugs its contents.

![4 states of collapsed navigation rails.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmal9s5f0-06-3P.png?alt=media&token=3900b54e-403f-4138-952a-22d9c5d47a68>)

__

![4 states of expanded navigation rails.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmal9sbiq-07-3P.png?alt=media&token=e367b797-932d-49fa-a72a-3ab34851205a>)

_Enabled; Hovered; Focused; Pressed_

## Measurements

![Padding and measurements for expanded and collapsed navigation rails.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmal9t1mb-08-3P.png?alt=media&token=8e32163c-6c4e-4d28-bf1b-5da584c4b46a>)

_Navigation rail padding and size measurements_

## Common layouts

![4 common layouts of collapsed navigation rail.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmal9twwq-09-3P.png?alt=media&token=9065bce5-6d1f-4c23-9090-27dc31f73d63>)

![4 common layouts of expanded navigation rail.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmal9u2w3-10-3P.png?alt=media&token=5b5ada66-3e11-40a7-a7f2-8559cb16e2ad>)

_Three navigation items; Three navigation items with a menu; Three navigation items with a FAB; Three navigation items with a menu and FAB_

* * *

## Baseline navigation rail

![8 elements of baseline navigation rail.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmal9uv1q-11-3P.png?alt=media&token=15c53656-c999-4f96-9e1d-77a16ca00b3a>)

_Container; Menu icon (optional); Icon; Active indicator; Label text; Large badge label (optional); Large badge (optional); Badge (optional)_

### Tokens & specs

### TOKEN_TABLE

Component: Navigation rail

#### Token sets

| Token set | Name | Type | Tokens | Description |
| --- | --- | --- | --- | --- |
| Navigation rail (baseline) | md.comp.navigation-rail | COMPONENT | 71 | For baseline navigation rails. For expressive, use all nav rail token sets`md.comp.nav-rail` and corresponding `.collapsed` and `.item` token sets. |
| Nav rail item - Horizontal | md.comp.nav-rail.item.horizontal | COMPONENT | 5 | For horizontal nav rail items, where elements are side-by-side. |
| Nav rail item - Common | md.comp.nav-rail.item | COMPONENT | 10 | Common tokens for all nav items. |
| Nav rail - Common | md.comp.nav-rail | COMPONENT | 14 | Common tokens for nav rail. |
| Nav rail - Collapsed | md.comp.nav-rail.collapsed | COMPONENT | 7 | For collapsed nav rail. |
| Nav rail - Expanded | md.comp.nav-rail.expanded | COMPONENT | 11 | For expanded nav rail. |
| Nav rail item - Vertical | md.comp.nav-rail.item.vertical | COMPONENT | 6 | For vertical nav rail items, where elements are stacked. |

#### Navigation rail (baseline) (md.comp.navigation-rail)

For baseline navigation rails. For expressive, use all nav rail token sets`md.comp.nav-rail` and corresponding `.collapsed` and `.item` token sets.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.navigation-rail.container.color | COLOR | Enabled / Container | `md.sys.color.surface` |  |  |
| md.comp.navigation-rail.container.shape | SHAPE | Enabled / Container | `md.sys.shape.corner.none` |  |  |
| md.comp.navigation-rail.container.width | LENGTH | Enabled / Container | 80dp |  |  |
| md.comp.navigation-rail.container.elevation | ELEVATION | Enabled / Container | `md.sys.elevation.level0` |  |  |
| md.comp.navigation-rail.label-text.font | FONT_NAMES | Enabled / Label text | `md.sys.typescale.label-medium.font` |  |  |
| md.comp.navigation-rail.label-text.line-height | LINE_HEIGHT | Enabled / Label text | `md.sys.typescale.label-medium.line-height` |  |  |
| md.comp.navigation-rail.label-text.size | FONT_SIZE | Enabled / Label text | `md.sys.typescale.label-medium.size` |  |  |
| md.comp.navigation-rail.label-text.weight | FONT_WEIGHT | Enabled / Label text | `md.sys.typescale.label-medium.weight` |  |  |
| md.comp.navigation-rail.active.label-text.weight | FONT_WEIGHT | Enabled / Label text | `md.sys.typescale.label-medium.weight.prominent` |  |  |
| md.comp.navigation-rail.label-text.tracking | FONT_TRACKING | Enabled / Label text | `md.sys.typescale.label-medium.tracking` |  |  |
| md.comp.navigation-rail.label-text.type | TYPOGRAPHY | Enabled / Label text | font name: `md.comp.navigation-rail.label-text.font`, font weight: `md.comp.navigation-rail.label-text.weight`, font size: `md.comp.navigation-rail.label-text.size`, font tracking: `md.comp.navigation-rail.label-text.tracking`, line height: `md.comp.navigation-rail.label-text.line-height` |  |  |
| md.comp.navigation-rail.active.label-text.color | COLOR | Enabled / Label text | `md.sys.color.on-surface` |  |  |
| md.comp.navigation-rail.inactive.label-text.color | COLOR | Enabled / Label text | `md.sys.color.on-surface-variant` |  |  |
| md.comp.navigation-rail.label-text.font-family | FONT_NAMES | Enabled / Label text | `md.sys.typescale.label-medium.font` |  | For consistency, we have standardized typography naming. Replace usage with the suggested token. |
| md.comp.navigation-rail.label-text.font-size | FONT_SIZE | Enabled / Label text | `md.sys.typescale.label-medium.size` |  | For consistency, we have standardized typography naming. Replace usage with the suggested token. |
| md.comp.navigation-rail.label-text.letter-spacing | FONT_TRACKING | Enabled / Label text | `md.sys.typescale.label-medium.tracking` |  | For consistency, we have standardized typography naming. Replace usage with the suggested token. |
| md.comp.navigation-rail.icon.size | LENGTH | Enabled / Icon | 24dp |  |  |
| md.comp.navigation-rail.active.icon.color | COLOR | Enabled / Icon | `md.sys.color.on-secondary-container` |  |  |
| md.comp.navigation-rail.inactive.icon.color | COLOR | Enabled / Icon | `md.sys.color.on-surface-variant` |  |  |
| md.comp.navigation-rail.menu.icon.color | COLOR | Enabled / Icon | `md.sys.color.on-surface-variant` |  | Using menu button token instead, this token no longer needed. |
| md.comp.navigation-rail.menu.icon.size | LENGTH | Enabled / Icon | 24dp |  | Using menu button token instead, this token no longer needed. |
| md.comp.navigation-rail.badge.color | COLOR | Enabled / [Deprecated] Badge | `md.sys.color.error` |  | Badge values were refactored out into their own token set. Replace usage with the equivalent `md.comp.badge.*` tokens. |
| md.comp.navigation-rail.badge.size | LENGTH | Enabled / [Deprecated] Badge | 6dp |  | Badge values were refactored out into their own token set. Replace usage with the equivalent `md.comp.badge.*` tokens. |
| md.comp.navigation-rail.badge.shape | SHAPE | Enabled / [Deprecated] Badge | rounded corners 3dp |  | Badge values were refactored out into their own token set. Replace usage with the equivalent `md.comp.badge.*` tokens. |
| md.comp.navigation-rail.active-indicator.color | COLOR | Enabled / Active indicator | `md.sys.color.secondary-container` |  |  |
| md.comp.navigation-rail.active-indicator.height | LENGTH | Enabled / Active indicator | 32dp |  |  |
| md.comp.navigation-rail.no-label.active-indicator.height | LENGTH | Enabled / Active indicator | 56dp |  |  |
| md.comp.navigation-rail.active-indicator.shape | SHAPE | Enabled / Active indicator | `md.sys.shape.corner.full` |  |  |
| md.comp.navigation-rail.no-label.active-indicator.shape | SHAPE | Enabled / Active indicator | `md.sys.shape.corner.full` |  |  |
| md.comp.navigation-rail.active-indicator.width | LENGTH | Enabled / Active indicator | 56dp |  |  |
| md.comp.navigation-rail.large-badge.size | LENGTH | Enabled / [Deprecated] Large badge | 16dp |  | Badge values were refactored out into their own token set. Replace usage with the equivalent `md.comp.badge.large.*` tokens. |
| md.comp.navigation-rail.large-badge.shape | SHAPE | Enabled / [Deprecated] Large badge | rounded corners 8dp |  | Badge values were refactored out into their own token set. Replace usage with the equivalent `md.comp.badge.large.*` tokens. |
| md.comp.navigation-rail.large-badge.color | COLOR | Enabled / [Deprecated] Large badge | `md.sys.color.error` |  | Badge values were refactored out into their own token set. Replace usage with the equivalent `md.comp.badge.large.*` tokens. |
| md.comp.navigation-rail.large-badge-label.font-family | FONT_NAMES | Enabled / [Deprecated] Large badge label | `md.sys.typescale.label-small.font` |  | For consistency, we have standardized typography naming. Replace usage with the suggested token. |
| md.comp.navigation-rail.large-badge-label.font | FONT_NAMES | Enabled / [Deprecated] Large badge label | `md.sys.typescale.label-small.font` |  | Badge values were refactored out into their own token set. Replace usage with the equivalent `md.comp.badge.large.*` tokens. |
| md.comp.navigation-rail.large-badge-label.line-height | LINE_HEIGHT | Enabled / [Deprecated] Large badge label | `md.sys.typescale.label-small.line-height` |  | Badge values were refactored out into their own token set. Replace usage with the equivalent `md.comp.badge.large.*` tokens. |
| md.comp.navigation-rail.large-badge-label.size | FONT_SIZE | Enabled / [Deprecated] Large badge label | `md.sys.typescale.label-small.size` |  | Badge values were refactored out into their own token set. Replace usage with the equivalent `md.comp.badge.large.*` tokens. |
| md.comp.navigation-rail.large-badge-label.weight | FONT_WEIGHT | Enabled / [Deprecated] Large badge label | `md.sys.typescale.label-small.weight` |  | Badge values were refactored out into their own token set. Replace usage with the equivalent `md.comp.badge.large.*` tokens. |
| md.comp.navigation-rail.large-badge-label.tracking | FONT_TRACKING | Enabled / [Deprecated] Large badge label | `md.sys.typescale.label-small.tracking` |  | Badge values were refactored out into their own token set. Replace usage with the equivalent `md.comp.badge.large.*` tokens. |
| md.comp.navigation-rail.large-badge-label.type | TYPOGRAPHY | Enabled / [Deprecated] Large badge label | font name: `md.comp.navigation-rail.large-badge-label.font`, font weight: `md.comp.navigation-rail.large-badge-label.weight`, font size: `md.comp.navigation-rail.large-badge-label.size`, font tracking: `md.comp.navigation-rail.large-badge-label.tracking`, line height: `md.comp.navigation-rail.large-badge-label.line-height` |  | Badge values were refactored out into their own token set. Replace usage with the equivalent `md.comp.badge.large.*` tokens. |
| md.comp.navigation-rail.large-badge-label.color | COLOR | Enabled / [Deprecated] Large badge label | `md.sys.color.on-error` |  | Badge values were refactored out into their own token set. Replace usage with the equivalent `md.comp.badge.large.*` tokens. |
| md.comp.navigation-rail.active.hover.label-text.color | COLOR | Hovered / Label text | `md.sys.color.on-surface` |  |  |
| md.comp.navigation-rail.inactive.hover.label-text.color | COLOR | Hovered / Label text | `md.sys.color.on-surface` |  |  |
| md.comp.navigation-rail.active.hover.state-layer.color | COLOR | Hovered / State layer | `md.sys.color.on-surface` |  |  |
| md.comp.navigation-rail.inactive.hover.state-layer.color | COLOR | Hovered / State layer | `md.sys.color.on-surface` |  |  |
| md.comp.navigation-rail.hover.state-layer.opacity | OPACITY | Hovered / State layer | `md.sys.state.hover.state-layer-opacity` |  |  |
| md.comp.navigation-rail.menu.hover.state-layer.color | COLOR | Hovered / State layer | `md.sys.color.on-surface` |  | Using menu button token instead, this token no longer needed. |
| md.comp.navigation-rail.menu.hover.state-layer.opacity | OPACITY | Hovered / State layer | `md.sys.state.hover.state-layer-opacity` |  | Using menu button token instead, this token no longer needed. |
| md.comp.navigation-rail.active.hover.icon.color | COLOR | Hovered / Icon | `md.sys.color.on-secondary-container` |  |  |
| md.comp.navigation-rail.inactive.hover.icon.color | COLOR | Hovered / Icon | `md.sys.color.on-surface` |  |  |
| md.comp.navigation-rail.menu.hover.icon.color | COLOR | Hovered / Icon | `md.sys.color.on-surface` |  | Using menu button token instead, this token no longer needed. |
| md.comp.navigation-rail.active.focus.label-text.color | COLOR | Focused / Label text | `md.sys.color.on-surface` |  |  |
| md.comp.navigation-rail.inactive.focus.label-text.color | COLOR | Focused / Label text | `md.sys.color.on-surface` |  |  |
| md.comp.navigation-rail.active.focus.state-layer.color | COLOR | Focused / State layer | `md.sys.color.on-surface` |  |  |
| md.comp.navigation-rail.inactive.focus.state-layer.color | COLOR | Focused / State layer | `md.sys.color.on-surface` |  |  |
| md.comp.navigation-rail.focus.state-layer.opacity | OPACITY | Focused / State layer | `md.sys.state.focus.state-layer-opacity` |  |  |
| md.comp.navigation-rail.menu.focus.state-layer.color | COLOR | Focused / State layer | `md.sys.color.on-surface` |  | Using menu button token instead, this token no longer needed. |
| md.comp.navigation-rail.menu.focus.state-layer.opacity | OPACITY | Focused / State layer | `md.sys.state.focus.state-layer-opacity` |  | Using menu button token instead, this token no longer needed. |
| md.comp.navigation-rail.active.focus.icon.color | COLOR | Focused / Icon | `md.sys.color.on-secondary-container` |  |  |
| md.comp.navigation-rail.inactive.focus.icon.color | COLOR | Focused / Icon | `md.sys.color.on-surface` |  |  |
| md.comp.navigation-rail.menu.focus.icon.color | COLOR | Focused / Icon | `md.sys.color.on-surface` |  | Using menu button token instead, this token no longer needed. |
| md.comp.navigation-rail.active.pressed.label-text.color | COLOR | Pressed (ripple) / Label text | `md.sys.color.on-surface` |  |  |
| md.comp.navigation-rail.inactive.pressed.label-text.color | COLOR | Pressed (ripple) / Label text | `md.sys.color.on-surface` |  |  |
| md.comp.navigation-rail.active.pressed.state-layer.color | COLOR | Pressed (ripple) / State layer | `md.sys.color.on-surface` |  |  |
| md.comp.navigation-rail.inactive.pressed.state-layer.color | COLOR | Pressed (ripple) / State layer | `md.sys.color.on-surface` |  |  |
| md.comp.navigation-rail.pressed.state-layer.opacity | OPACITY | Pressed (ripple) / State layer | `md.sys.state.pressed.state-layer-opacity` |  |  |
| md.comp.navigation-rail.menu.pressed.state-layer.color | COLOR | Pressed (ripple) / State layer | `md.sys.color.on-surface` |  | Using menu button token instead, this token no longer needed. |
| md.comp.navigation-rail.menu.pressed.state-layer.opacity | OPACITY | Pressed (ripple) / State layer | `md.sys.state.pressed.state-layer-opacity` |  | Using menu button token instead, this token no longer needed. |
| md.comp.navigation-rail.active.pressed.icon.color | COLOR | Pressed (ripple) / Icon | `md.sys.color.on-secondary-container` |  |  |
| md.comp.navigation-rail.inactive.pressed.icon.color | COLOR | Pressed (ripple) / Icon | `md.sys.color.on-surface` |  |  |
| md.comp.navigation-rail.menu.pressed.icon.color | COLOR | Pressed (ripple) / Icon | `md.sys.color.on-surface` |  | Using menu button token instead, this token no longer needed. |

#### Nav rail item - Horizontal (md.comp.nav-rail.item.horizontal)

For horizontal nav rail items, where elements are side-by-side.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.nav-rail.item.horizontal.label-text.font | TYPOGRAPHY |  | `md.sys.typescale.label-large` |  |  |
| md.comp.nav-rail.item.horizontal.active-indicator.height | LENGTH |  | 56dp |  |  |
| md.comp.nav-rail.item.horizontal.full-width.leading-space | LENGTH |  | 16dp |  |  |
| md.comp.nav-rail.item.horizontal.full-width.trailing-space | LENGTH |  | 16dp |  |  |
| md.comp.nav-rail.item.horizontal.icon-label-space | LENGTH |  | 8dp |  |  |

#### Nav rail item - Common (md.comp.nav-rail.item)

Common tokens for all nav items.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.nav-rail.item.icon.size | LENGTH |  | 24dp |  |  |
| md.comp.nav-rail.item.active-indicator.shape | SHAPE |  | `md.sys.shape.corner.full` |  |  |
| md.comp.nav-rail.item.active-indicator.leading-space | LENGTH |  | 16dp |  |  |
| md.comp.nav-rail.item.active-indicator.icon-label-space | LENGTH |  | 8dp |  |  |
| md.comp.nav-rail.item.active-indicator.trailing-space | LENGTH |  | 16dp |  |  |
| md.comp.nav-rail.item.container.height | LENGTH |  | 64dp |  |  |
| md.comp.nav-rail.item.short.container.height | LENGTH |  | 56dp |  |  |
| md.comp.nav-rail.item.container.shape | SHAPE |  | `md.sys.shape.corner.none` |  |  |
| md.comp.nav-rail.item.container.vertical-space | LENGTH |  | 6dp |  |  |
| md.comp.nav-rail.item.header-space-minimum | LENGTH |  | 40dp |  |  |

#### Nav rail - Common (md.comp.nav-rail)

Common tokens for nav rail.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.nav-rail.item.active.indicator.color | COLOR | Enabled | `md.sys.color.secondary-container` |  |  |
| md.comp.nav-rail.item.active.label-text.color | COLOR | Enabled | `md.sys.color.secondary` |  |  |
| md.comp.nav-rail.item.inactive.label-text.color | COLOR | Enabled | `md.sys.color.on-surface-variant` |  |  |
| md.comp.nav-rail.item.active.icon.color | COLOR | Enabled | `md.sys.color.on-secondary-container` |  |  |
| md.comp.nav-rail.item.inactive.icon.color | COLOR | Enabled | `md.sys.color.on-surface-variant` |  |  |
| md.comp.nav-rail.item.active.hovered.state-layer.color | COLOR | Hovered | `md.sys.color.on-secondary-container` |  |  |
| md.comp.nav-rail.item.active.hovered.state-layer.opacity | OPACITY | Hovered | `md.sys.state.hover.state-layer-opacity` |  |  |
| md.comp.nav-rail.item.inactive.hovered.state-layer.color | COLOR | Hovered | `md.sys.color.on-secondary-container` |  |  |
| md.comp.nav-rail.item.active.focused.state-layer.color | COLOR | Focused | `md.sys.color.on-secondary-container` |  |  |
| md.comp.nav-rail.item.active.focused.state-layer.opacity | OPACITY | Focused | `md.sys.state.focus.state-layer-opacity` |  |  |
| md.comp.nav-rail.item.inactive.focused.state-layer.color | COLOR | Focused | `md.sys.color.on-secondary-container` |  |  |
| md.comp.nav-rail.item.active.pressed.state-layer.color | COLOR | Pressed | `md.sys.color.on-secondary-container` |  |  |
| md.comp.nav-rail.item.active.pressed.state-layer.opacity | OPACITY | Pressed | `md.sys.state.pressed.state-layer-opacity` |  |  |
| md.comp.nav-rail.item.inactive.pressed.state-layer.color | COLOR | Pressed | `md.sys.color.on-secondary-container` |  |  |

#### Nav rail - Collapsed (md.comp.nav-rail.collapsed)

For collapsed nav rail.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.nav-rail.collapsed.container.width | LENGTH |  | 96dp |  |  |
| md.comp.nav-rail.collapsed.narrow.container.width | LENGTH |  | 80dp |  |  |
| md.comp.nav-rail.collapsed.container.elevation | ELEVATION |  | `md.sys.elevation.level0` |  |  |
| md.comp.nav-rail.collapsed.container.shape | SHAPE |  | `md.sys.shape.corner.none` |  |  |
| md.comp.nav-rail.collapsed.container.color | COLOR |  | `md.sys.color.surface` |  |  |
| md.comp.nav-rail.collapsed.item.vertical-space | LENGTH |  | 4dp |  |  |
| md.comp.nav-rail.collapsed.top-space | LENGTH |  | 44dp |  |  |

#### Nav rail - Expanded (md.comp.nav-rail.expanded)

For expanded nav rail.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.nav-rail.expanded.container.width.minimum | LENGTH |  | 220dp |  |  |
| md.comp.nav-rail.expanded.container.width.maximum | LENGTH |  | 360dp |  |  |
| md.comp.nav-rail.expanded.top-space | LENGTH |  | 44dp |  |  |
| md.comp.nav-rail.expanded.container.elevation | ELEVATION |  | `md.sys.elevation.level0` |  |  |
| md.comp.nav-rail.expanded.modal.container.elevation | ELEVATION |  | `md.sys.elevation.level2` |  |  |
| md.comp.nav-rail.expanded.container.color | COLOR |  | `md.sys.color.surface` |  |  |
| md.comp.nav-rail.expanded.modal.container.color | COLOR |  | `md.sys.color.surface-container` |  |  |
| md.comp.nav-rail.expanded.container.shape | SHAPE |  | `md.sys.shape.corner.none` |  |  |
| md.comp.nav-rail.expanded.modal.container.shape | SHAPE |  | `md.sys.shape.corner.large` |  |  |
| md.comp.nav-rail.expanded.between-item-space | LENGTH |  | 0dp |  | Token not needed as value is "0dp" |
| md.comp.nav-rail.expanded.vertical.trailing-space | LENGTH |  | 20dp |  | Token moved to nav rail item horizontal set |

#### Nav rail item - Vertical (md.comp.nav-rail.item.vertical)

For vertical nav rail items, where elements are stacked.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.nav-rail.item.vertical.active-indicator.height | LENGTH |  | 32dp |  |  |
| md.comp.nav-rail.item.vertical.active-indicator.width | LENGTH |  | 56dp |  |  |
| md.comp.nav-rail.item.vertical.label-text.font | TYPOGRAPHY |  | `md.sys.typescale.label-medium` |  |  |
| md.comp.nav-rail.item.vertical.icon-label-space | LENGTH |  | 4dp |  |  |
| md.comp.nav-rail.item.vertical.leading-space | LENGTH |  | 16dp |  |  |
| md.comp.nav-rail.item.vertical.trailing-space | LENGTH |  | 16dp |  |  |

### Color

Color values are implemented through design tokens. For design, this means working with color values that correspond with tokens. For implementation, a color value will be a token that references a value. [Learn more about design tokens](../../foundations/design-tokens/overview.md)

![8 color roles of baseline navigation rail.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmal9vzs8-12-3P.png?alt=media&token=4d01ee27-7870-4c37-8095-067a9d64d37e>)

_Navigation rail color roles used for light and dark themes: On secondary container; Secondary container; On surface; On surface variant; On surface variant; Error; On error; Error_

### States

States are visual representations used to communicate the status of a component or interactive element.

![8 states of baseline navigation rail.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmal9x9ol-13-3P.png?alt=media&token=69b0b5ae-6f96-4d67-8a45-e77efecd6b17>)

_Navigation rail states: Enabled (on active destination); Hovered (on active destination); Focused (on active destination); Pressed (on active destination); Enabled (on inactive destination); Hovered (on inactive destination); Focused (on inactive destination); Pressed (on inactive destination)_

### Measurements

![Baseline nav rail size measurements.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmal9xiu7-14-3P.png?alt=media&token=5970c991-6782-4784-bcbb-4fc5ed1dd423>)

_Navigation rail size measurements_

![Baseline nav rail padding and margin measurements.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmal9xyxt-15-3P.png?alt=media&token=3229f7c0-780c-4bbc-9e2f-aa8a5d83e591>)

_Navigation rail padding and margin measurements_

### Configurations

Common arrangements of elements within a navigation rail.

![5 configurations of the baseline navigation rail.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmal9yote-16-3P.png?alt=media&token=551b3b7f-1fd2-4e64-a69f-a57c4a7fe835>)

_With a menu; With a FAB; With menu and FAB, without labels; All destinations with text labels; With menu, FAB, and label text for all destinations_

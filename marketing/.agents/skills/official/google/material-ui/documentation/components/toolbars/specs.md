---
title: "Toolbars"
source_url: "https://m3.material.io/components/toolbars/specs"
lastmod: "2026-05-18"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "components"
assets:
  - "assets/images/63c55e59d127-ma23ultc-01-3P.png"
  - "assets/images/d25d5d91212a-ma24egim-1-deprecated.png"
  - "assets/images/09397b7ff3a1-ma24i061-02-3P.png"
  - "assets/images/2ef9fa86515b-ma24poz2-03-3P.png"
  - "assets/images/1a7f351b4113-ma25cmf1-04-3P.png"
  - "assets/images/e21e92c78fed-ma25fg6c-05-3P.png"
  - "assets/images/4a6336729d96-ma25hban-06-3P.png"
  - "assets/images/4076d0948ab3-ma25j41t-07-3P.png"
  - "assets/images/1ef0485d0e12-ma25jqfl-08-3P.png"
  - "assets/images/5627c1ae3114-ma25l6kh-09-3P.png"
  - "assets/images/fae72374dd60-ma25numd-10-3P.png"
  - "assets/images/0bb44c8b0c52-ma25or07-11-3P.png"
  - "assets/images/b3d21cc09a05-ma25qmnu-12-3P.png"
  - "assets/images/c2f04d3299d1-ma25vwtc-13-3P.png"
  - "assets/images/ca0944f9b9c2-ma25wn0p-14-3P.png"
  - "assets/images/3ccd13ffd70d-ma25xkg5-15-3P.png"
---

# Toolbars

Toolbars display frequently used actions relevant to the current page

## Specs

## Variants

![2 variants of toolbars.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fma23ultc-01-3P.png?alt=media&token=f06dc965-479c-428e-b1b5-3c549f018f8e>)

_Docked toolbar; Floating toolbar_

### Baseline variant

The baseline bottom app bar is no longer recommended. It should be replaced with the docked toolbar, which is very similar and more flexible.

![Baseline bottom app bar, which looks like the docked toolbar, but is not recommended.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fma24egim-1-deprecated.png?alt=media&token=1081969c-6b00-4ff2-832a-6c81df9e30eb>)

_Bottom app bar (not recommended)_

| Variant | M3 | M3 Expressive |
| --- | --- | --- |
| Docked toolbar | \-- | Available |
| Floating toolbar | \-- | Available |
| Bottom app bar | Available | Not recommended. Use **docked toolbar**. |

star

Note:

Implementation differs per platform. On Jetpack Compose, the floating toolbar is a separate component from the docked toolbar and bottom app bar.

## Configurations

![Color configuration of toolbars.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fma24i061-02-3P.png?alt=media&token=ea746f3b-39a6-4cb4-bc49-0b8c2d935f3f>)

_Standard and vibrant toolbars; Vertical floating toolbar; Floating toolbar with FAB_

| Category | Configuration | M3 | M3 Expressive |
| --- | --- | --- | --- |
| Color | Standard (default) | Available as bottom app bar | Available |
| Vibrant | \-- | Available |  |
| Floating toolbar layout | Horizontal (default) | \-- | Available |
| Vertical | \-- | Available |  |
| Other elements | With FAB | Available as bottom app bar | Available\* |

star

Note:

\*Implementation differs per platform. On Jetpack Compose, floating toolbar with FAB is [fully supported](https://developer.android.com/reference/kotlin/androidx/compose/material3/package-summary#HorizontalFloatingToolbar\(kotlin.Boolean,androidx.compose.ui.Modifier,androidx.compose.material3.FloatingToolbarColors,androidx.compose.foundation.layout.PaddingValues,androidx.compose.material3.FloatingToolbarScrollBehavior,androidx.compose.ui.graphics.Shape,kotlin.Function1,kotlin.Function1,androidx.compose.ui.unit.Dp,androidx.compose.ui.unit.Dp,kotlin.Function1\)). On other platforms, each component needs to be added separately.

## Tokens & specs

Browse the component elements, attributes, tokens, and their values. [Jump to baseline bottom app bar specs](specs.md#ad142675-3e3b-43b8-ba53-12c1f0b7138d)

### TOKEN_TABLE

Component: Toolbars

Configured context tags: None, Medium contrast, Default, High contrast, 3P, Android, Dark, Light

#### Token sets

| Token set | Name | Type | Tokens | Description |
| --- | --- | --- | --- | --- |
| Toolbar - Color - Standard | md.comp.toolbar.standard | COMPONENT | 33 | For standard docked and floating toolbars. |
| Toolbar - Color - Vibrant | md.comp.toolbar.vibrant | COMPONENT | 33 | For vibrant docked and floating toolbars. |
| Toolbar - Docked | md.comp.toolbar.docked | COMPONENT | 7 | For docked toolbars. |
| Toolbar - Floating | md.comp.toolbar.floating | COMPONENT | 18 | For floating toolbars. |
| Toolbar - Floating - FAB | md.comp.toolbar.floating.fab | COMPONENT | 15 | For floating toolbars with a FAB in them. |

#### Toolbar - Color - Standard (md.comp.toolbar.standard)

For standard docked and floating toolbars.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.toolbar.standard.container.color | COLOR | Enabled | `md.sys.color.surface-container` |  |  |
| md.comp.toolbar.standard.button.container.color | COLOR | Enabled | `md.sys.color.surface-container` |  |  |
| md.comp.toolbar.standard.selected.button.container.color | COLOR | Enabled | `md.sys.color.secondary-container` |  |  |
| md.comp.toolbar.standard.icon.color | COLOR | Enabled | `md.sys.color.on-surface-variant` |  |  |
| md.comp.toolbar.standard.selected.icon.color | COLOR | Enabled | `md.sys.color.on-secondary-container` |  |  |
| md.comp.toolbar.standard.label-text.color | COLOR | Enabled | `md.sys.color.on-surface-variant` |  |  |
| md.comp.toolbar.standard.selected.label-text.color | COLOR | Enabled | `md.sys.color.on-secondary-container` |  |  |
| md.comp.toolbar.standard.container.shape | SHAPE | Enabled | `md.sys.shape.corner.full` |  |  |
| md.comp.toolbar.standard.disabled.icon.color | COLOR | Disabled | `md.sys.color.on-surface` |  |  |
| md.comp.toolbar.standard.disabled.icon.opacity | OPACITY | Disabled | 0.38 |  |  |
| md.comp.toolbar.standard.disabled.label-text.color | COLOR | Disabled | `md.sys.color.on-surface` |  |  |
| md.comp.toolbar.standard.disabled.label-text.opacity | OPACITY | Disabled | 0.38 |  |  |
| md.comp.toolbar.standard.hovered.state-layer.color | COLOR | Hovered | `md.sys.color.on-surface-variant` |  |  |
| md.comp.toolbar.standard.selected.hovered.state-layer.color | COLOR | Hovered | `md.sys.color.on-secondary-container` |  |  |
| md.comp.toolbar.standard.hovered.state-layer.opacity | OPACITY | Hovered | `md.sys.state.hover.state-layer-opacity` |  |  |
| md.comp.toolbar.standard.hovered.icon.color | COLOR | Hovered | `md.sys.color.on-surface-variant` |  |  |
| md.comp.toolbar.standard.selected.hovered.icon.color | COLOR | Hovered | `md.sys.color.on-secondary-container` |  |  |
| md.comp.toolbar.standard.hovered.label-text.color | COLOR | Hovered | `md.sys.color.on-surface-variant` |  |  |
| md.comp.toolbar.standard.selected.hovered.label-text.color | COLOR | Hovered | `md.sys.color.on-secondary-container` |  |  |
| md.comp.toolbar.standard.focused.state-layer.color | COLOR | Focused | `md.sys.color.on-surface-variant` |  |  |
| md.comp.toolbar.standard.selected.focused.state-layer.color | COLOR | Focused | `md.sys.color.on-secondary-container` |  |  |
| md.comp.toolbar.standard.focused.state-layer.opacity | OPACITY | Focused | `md.sys.state.focus.state-layer-opacity` |  |  |
| md.comp.toolbar.standard.focused.icon.color | COLOR | Focused | `md.sys.color.on-surface-variant` |  |  |
| md.comp.toolbar.standard.selected.focused.icon.color | COLOR | Focused | `md.sys.color.on-secondary-container` |  |  |
| md.comp.toolbar.standard.focused.label-text.color | COLOR | Focused | `md.sys.color.on-surface-variant` |  |  |
| md.comp.toolbar.standard.selected.focused.label-text.color | COLOR | Focused | `md.sys.color.on-secondary-container` |  |  |
| md.comp.toolbar.standard.pressed.state-layer.color | COLOR | Pressed | `md.sys.color.on-surface-variant` |  |  |
| md.comp.toolbar.standard.selected.pressed.state-layer.color | COLOR | Pressed | `md.sys.color.on-secondary-container` |  |  |
| md.comp.toolbar.standard.pressed.state-layer.opacity | OPACITY | Pressed | `md.sys.state.pressed.state-layer-opacity` |  |  |
| md.comp.toolbar.standard.pressed.icon.color | COLOR | Pressed | `md.sys.color.on-surface-variant` |  |  |
| md.comp.toolbar.standard.selected.pressed.icon.color | COLOR | Pressed | `md.sys.color.on-secondary-container` |  |  |
| md.comp.toolbar.standard.pressed.label-text.color | COLOR | Pressed | `md.sys.color.on-surface-variant` |  |  |
| md.comp.toolbar.standard.selected.pressed.label-text.color | COLOR | Pressed | `md.sys.color.on-secondary-container` |  |  |

#### Toolbar - Color - Vibrant (md.comp.toolbar.vibrant)

For vibrant docked and floating toolbars.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.toolbar.vibrant.container.color | COLOR | Enabled | `md.sys.color.primary-container` |  |  |
| md.comp.toolbar.vibrant.button.container.color | COLOR | Enabled | `md.sys.color.primary-container` |  |  |
| md.comp.toolbar.vibrant.selected.button.container.color | COLOR | Enabled | `md.sys.color.surface-container` |  |  |
| md.comp.toolbar.vibrant.icon.color | COLOR | Enabled | `md.sys.color.on-primary-container` |  |  |
| md.comp.toolbar.vibrant.selected.icon.color | COLOR | Enabled | `md.sys.color.on-surface` |  |  |
| md.comp.toolbar.vibrant.label-text.color | COLOR | Enabled | `md.sys.color.on-primary-container` |  |  |
| md.comp.toolbar.vibrant.selected.label-text.color | COLOR | Enabled | `md.sys.color.on-surface` |  |  |
| md.comp.toolbar.vibrant.container.shape | SHAPE | Enabled | `md.sys.shape.corner.full` |  |  |
| md.comp.toolbar.vibrant.disabled.icon.color | COLOR | Disabled | `md.sys.color.on-surface` |  |  |
| md.comp.toolbar.vibrant.disabled.icon.opacity | OPACITY | Disabled | 0.38 |  |  |
| md.comp.toolbar.vibrant.disabled.label-text.color | COLOR | Disabled | `md.sys.color.on-surface` |  |  |
| md.comp.toolbar.vibrant.disabled.label-text.opacity | OPACITY | Disabled | 0.38 |  |  |
| md.comp.toolbar.vibrant.hovered.state-layer.color | COLOR | Hovered | `md.sys.color.on-primary-container` |  |  |
| md.comp.toolbar.vibrant.selected.hovered.state-layer.color | COLOR | Hovered | `md.sys.color.on-surface` |  |  |
| md.comp.toolbar.vibrant.hovered.state-layer.opacity | OPACITY | Hovered | `md.sys.state.hover.state-layer-opacity` |  |  |
| md.comp.toolbar.vibrant.hovered.icon.color | COLOR | Hovered | `md.sys.color.on-primary-container` |  |  |
| md.comp.toolbar.vibrant.selected.hovered.icon.color | COLOR | Hovered | `md.sys.color.on-surface` |  |  |
| md.comp.toolbar.vibrant.hovered.label-text.color | COLOR | Hovered | `md.sys.color.on-primary-container` |  |  |
| md.comp.toolbar.vibrant.selected.hovered.label-text.color | COLOR | Hovered | `md.sys.color.on-surface` |  |  |
| md.comp.toolbar.vibrant.focused.state-layer.color | COLOR | Focused | `md.sys.color.on-primary-container` |  |  |
| md.comp.toolbar.vibrant.selected.focused.state-layer.color | COLOR | Focused | `md.sys.color.on-surface` |  |  |
| md.comp.toolbar.vibrant.focused.state-layer.opacity | OPACITY | Focused | `md.sys.state.focus.state-layer-opacity` |  |  |
| md.comp.toolbar.vibrant.focused.icon.color | COLOR | Focused | `md.sys.color.on-primary-container` |  |  |
| md.comp.toolbar.vibrant.selected.focused.icon.color | COLOR | Focused | `md.sys.color.on-surface` |  |  |
| md.comp.toolbar.vibrant.focused.label-text.color | COLOR | Focused | `md.sys.color.on-primary-container` |  |  |
| md.comp.toolbar.vibrant.selected.focused.label-text.color | COLOR | Focused | `md.sys.color.on-surface` |  |  |
| md.comp.toolbar.vibrant.pressed.state-layer.color | COLOR | Pressed | `md.sys.color.on-primary-container` |  |  |
| md.comp.toolbar.vibrant.selected.pressed.state-layer.color | COLOR | Pressed | `md.sys.color.on-surface` |  |  |
| md.comp.toolbar.vibrant.pressed.state-layer.opacity | OPACITY | Pressed | `md.sys.state.pressed.state-layer-opacity` |  |  |
| md.comp.toolbar.vibrant.pressed.icon.color | COLOR | Pressed | `md.sys.color.on-primary-container` |  |  |
| md.comp.toolbar.vibrant.selected.pressed.icon.color | COLOR | Pressed | `md.sys.color.on-surface` |  |  |
| md.comp.toolbar.vibrant.pressed.label-text.color | COLOR | Pressed | `md.sys.color.on-primary-container` |  |  |
| md.comp.toolbar.vibrant.selected.pressed.label-text.color | COLOR | Pressed | `md.sys.color.on-surface` |  |  |

#### Toolbar - Docked (md.comp.toolbar.docked)

For docked toolbars.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.toolbar.docked.container.color | COLOR | Color | `md.sys.color.surface-container` |  | Moving this token to the color token set |
| md.comp.toolbar.docked.container.height | LENGTH | Size | 64dp |  |  |
| md.comp.toolbar.docked.container.leading-space | LENGTH | Size | 16dp |  |  |
| md.comp.toolbar.docked.container.trailing-space | LENGTH | Size | 16dp |  |  |
| md.comp.toolbar.docked.container.max-spacing | LENGTH | Size | 32dp |  |  |
| md.comp.toolbar.docked.container.min-spacing | LENGTH | Size | 4dp |  |  |
| md.comp.toolbar.docked.container.shape | SHAPE | Shape | `md.sys.shape.corner.none` |  |  |

#### Toolbar - Floating (md.comp.toolbar.floating)

For floating toolbars.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.toolbar.floating.standard.container.color | COLOR | Color | `md.sys.color.surface-container` |  | Deprecating this token here and putting it into the color token set |
| md.comp.toolbar.floating.vibrant.container.color | COLOR | Color | `md.sys.color.primary-container` |  | Deprecating this token here and putting it into the color token set |
| md.comp.toolbar.floating.vibrant.button.unselected.icon.color | COLOR | Color | `md.sys.color.on-primary-container` |  | Deprecating this token here and putting it into the color token set |
| md.comp.toolbar.floating.vibrant.button.unselected.text.color | COLOR | Color | `md.sys.color.on-primary-container` |  | Deprecating this token here and putting it into the color token set |
| md.comp.toolbar.floating.vibrant.button.selected.icon.color | COLOR | Color | `md.sys.color.on-surface` |  | Deprecating this token here and putting it into the color token set |
| md.comp.toolbar.floating.vibrant.button.selected.text.color | COLOR | Color | `md.sys.color.on-surface` |  | Deprecating this token here and putting it into the color token set |
| md.comp.toolbar.floating.vibrant.button.selected.container.color | COLOR | Color | `md.sys.color.surface-container` |  | Deprecating this token here and putting it into the color token set |
| md.comp.toolbar.floating.container.height | LENGTH | Size | 64dp |  | Deprecating this for a vertical and horizontal variant |
| md.comp.toolbar.floating.horizontal.container.height | LENGTH | Size | 64dp |  |  |
| md.comp.toolbar.floating.vertical.container.width | LENGTH | Size | 64dp |  |  |
| md.comp.toolbar.floating.container.leading-space | LENGTH | Size | 8dp |  |  |
| md.comp.toolbar.floating.container.trailing-space | LENGTH | Size | 8dp |  |  |
| md.comp.toolbar.floating.container.external-padding | LENGTH | Size | 16dp |  | Deprecating for a vertical and horizontal option |
| md.comp.toolbar.floating.horizontal.container.external-space | LENGTH | Size | 16dp |  |  |
| md.comp.toolbar.floating.vertical.container.external-space | LENGTH | Size | 24dp |  |  |
| md.comp.toolbar.floating.container.between-space | LENGTH | Size | 4dp |  |  |
| md.comp.toolbar.floating.container.shape | SHAPE | Shape | `md.sys.shape.corner.full` |  |  |
| md.comp.toolbar.floating.container.elevation | ELEVATION | Elevation | `md.sys.elevation.level3` |  |  |

#### Toolbar - Floating - FAB (md.comp.toolbar.floating.fab)

For floating toolbars with a FAB in them.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.toolbar.floating.fab.between-space | LENGTH |  | 8dp |  |  |
| md.comp.toolbar.floating.fab.standard.container.color | COLOR |  | `md.sys.color.secondary-container` |  |  |
| md.comp.toolbar.floating.fab.standard.icon.color | COLOR |  | `md.sys.color.on-secondary-container` |  |  |
| md.comp.toolbar.floating.fab.vibrant.container.color | COLOR |  | `md.sys.color.tertiary-container` |  |  |
| md.comp.toolbar.floating.fab.vibrant.icon.color | COLOR |  | `md.sys.color.on-tertiary-container` |  |  |
| md.comp.toolbar.floating.fab.container.height | LENGTH | Expanded | 56dp |  |  |
| md.comp.toolbar.floating.fab.container.width | LENGTH | Expanded | 56dp |  |  |
| md.comp.toolbar.floating.fab.icon.size | LENGTH | Expanded | 24dp |  |  |
| md.comp.toolbar.floating.fab.container.shape | SHAPE | Expanded | `md.sys.shape.corner.large` |  |  |
| md.comp.toolbar.floating.fab.container.elevation | ELEVATION | Expanded | `md.sys.elevation.level1` |  |  |
| md.comp.toolbar.floating.fab.medium.container.height | LENGTH | Collapsed | 80dp |  |  |
| md.comp.toolbar.floating.fab.medium.container.width | LENGTH | Collapsed | 80dp |  |  |
| md.comp.toolbar.floating.fab.medium.icon.size | LENGTH | Collapsed | 28dp |  |  |
| md.comp.toolbar.floating.fab.medium.container.shape | SHAPE | Collapsed | `md.sys.shape.corner.large-increased` |  |  |
| md.comp.toolbar.floating.fab.medium.container.elevation | ELEVATION | Collapsed | `md.sys.elevation.level2` |  |  |

## Anatomy

![2 elements of a toolbar.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fma24poz2-03-3P.png?alt=media&token=01853411-fd2a-46df-a634-5ba6f213ddaf>)

_Container; Placed components_

### Flexibility & slots

When configuring a toolbar, think of it as a container with several slots. 

Each slot can be a different element. The most common elements are icon buttons, buttons, and text fields.

![A toolbar with 5 slots, conceptual spaces for UI elements, next to each other.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fma25cmf1-04-3P.png?alt=media&token=aab2da97-79dc-4533-a948-f51575cd6e31>)

_A toolbar is essentially a container with configurable slots_

## Color

Color values are implemented through design tokens. For design, this means working with color values that correspond with tokens. For implementation, a color value will be a token that references a value. [Learn more about design tokens](../../foundations/design-tokens/overview.md)

### Standard

![4 color roles in the standard color scheme  of the floating toolbar in light and dark scheme.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fma25fg6c-05-3P.png?alt=media&token=1575d435-2053-4e77-aef6-f5ec25af2580>)

_Standard color schemes and icon button types: Surface container; Filled button (Primary, On primary); Toggle tonal button (Secondary container, On secondary container); Standard button (Primary)_

### Vibrant

![4 color roles in the vibrant color scheme of the floating toolbar in light and dark scheme.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fma25hban-06-3P.png?alt=media&token=59ff67bd-7807-47ec-93de-beab552a6360>)

_Vibrant color scheme and icon button types: Primary container; Filled button (Primary, On primary); Toggle tonal button: (Surface container, On surface); Standard button (On primary container)_

## Measurements

By default all toolbars are 64dp high, center-aligned, have equal padding between items, and have a minimum outside padding of 16dp.

### Docked toolbar

![Default internal padding of a docked toolbar.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fma25j41t-07-3P.png?alt=media&token=64118047-e221-4b16-b995-7dd09d62df2c>)

_Default margins and padding; Margins and padding with leading, middle, and trailing content_

![2 docked toolbars with different margins and alignment.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fma25jqfl-08-3P.png?alt=media&token=69b2890a-a4ee-4057-875c-6ac032fa1fe1>)

_Alignment and padding can be configured to create unique layouts: Left and right alignment; Center-aligned, 8dp padding between items_

### Floating toolbar

![Diagram noting margin around edge of floating toolbar.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fma25l6kh-09-3P.png?alt=media&token=88fb2262-a466-463c-839f-c5a397cbea75>)

_Default padding of floating toolbar_

![Diagram noting layout measurements.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fma25numd-10-3P.png?alt=media&token=65655b16-bb45-4154-904e-98dfa7e77b77>)

_Floating toolbar size and padding measurements_

![Diagram noting layout margins.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fma25or07-11-3P.png?alt=media&token=d5e97936-d6ec-4c04-8fb9-7dbfab981bde>)

_Floating toolbar margins_

* * *

## Bottom app bar (baseline)

![Diagram of bottom app bar indicating the container.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fma25qmnu-12-3P.png?alt=media&token=526e6241-99db-49b5-b795-5a06b9127bfa>)

_Container_

### Tokens & specs

Bottom app bar tokens are in one token set.

### TOKEN_TABLE

Component: Toolbars

#### Token sets

| Token set | Name | Type | Tokens | Description |
| --- | --- | --- | --- | --- |
| Bottom app bar (baseline) | md.comp.bottom-app-bar | COMPONENT | 6 | Baseline bottom app bar container tokens. For expressive, use `md.comp.toolbar.docked` and `md.comp.toolbar.standard` instead. |
| Toolbar - Docked | md.comp.toolbar.docked | COMPONENT | 7 | For docked toolbars. |
| Toolbar - Floating | md.comp.toolbar.floating | COMPONENT | 18 | For floating toolbars. |
| Toolbar - Color - Standard | md.comp.toolbar.standard | COMPONENT | 33 | For standard docked and floating toolbars. |
| Toolbar - Color - Vibrant | md.comp.toolbar.vibrant | COMPONENT | 33 | For vibrant docked and floating toolbars. |
| Toolbar - Floating - FAB | md.comp.toolbar.floating.fab | COMPONENT | 15 | For floating toolbars with a FAB in them. |

#### Bottom app bar (baseline) (md.comp.bottom-app-bar)

Baseline bottom app bar container tokens. For expressive, use `md.comp.toolbar.docked` and `md.comp.toolbar.standard` instead.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.bottom-app-bar.container.color | COLOR | Enabled / Container | `md.sys.color.surface-container` |  |  |
| md.comp.bottom-app-bar.container.shape | SHAPE | Enabled / Container | `md.sys.shape.corner.none` |  |  |
| md.comp.bottom-app-bar.container.elevation | ELEVATION | Enabled / Container | `md.sys.elevation.level2` |  |  |
| md.comp.bottom-app-bar.container.surface-tint-layer.color | COLOR | Enabled / Container | `md.sys.color.surface-tint` |  | Deprecated as part of the update from opacity based surfaces to tonal surfaces. Surfaces no longer use surface-tint layers for tinting, please use the desired surface role directly as the container color. |
| md.comp.bottom-app-bar.container.height | LENGTH | Enabled / Container | 80dp |  |  |
| md.comp.bottom-app-bar.with-fab.container.height | LENGTH | Enabled / Container | 72dp |  | Bottom app bar design updated to use a single height for all configurations, with vertically centered content. Please use md.comp.bottom-app-bar.container.height instead |

#### Toolbar - Docked (md.comp.toolbar.docked)

For docked toolbars.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.toolbar.docked.container.color | COLOR | Color | `md.sys.color.surface-container` |  | Moving this token to the color token set |
| md.comp.toolbar.docked.container.height | LENGTH | Size | 64dp |  |  |
| md.comp.toolbar.docked.container.leading-space | LENGTH | Size | 16dp |  |  |
| md.comp.toolbar.docked.container.trailing-space | LENGTH | Size | 16dp |  |  |
| md.comp.toolbar.docked.container.max-spacing | LENGTH | Size | 32dp |  |  |
| md.comp.toolbar.docked.container.min-spacing | LENGTH | Size | 4dp |  |  |
| md.comp.toolbar.docked.container.shape | SHAPE | Shape | `md.sys.shape.corner.none` |  |  |

#### Toolbar - Floating (md.comp.toolbar.floating)

For floating toolbars.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.toolbar.floating.standard.container.color | COLOR | Color | `md.sys.color.surface-container` |  | Deprecating this token here and putting it into the color token set |
| md.comp.toolbar.floating.vibrant.container.color | COLOR | Color | `md.sys.color.primary-container` |  | Deprecating this token here and putting it into the color token set |
| md.comp.toolbar.floating.vibrant.button.unselected.icon.color | COLOR | Color | `md.sys.color.on-primary-container` |  | Deprecating this token here and putting it into the color token set |
| md.comp.toolbar.floating.vibrant.button.unselected.text.color | COLOR | Color | `md.sys.color.on-primary-container` |  | Deprecating this token here and putting it into the color token set |
| md.comp.toolbar.floating.vibrant.button.selected.icon.color | COLOR | Color | `md.sys.color.on-surface` |  | Deprecating this token here and putting it into the color token set |
| md.comp.toolbar.floating.vibrant.button.selected.text.color | COLOR | Color | `md.sys.color.on-surface` |  | Deprecating this token here and putting it into the color token set |
| md.comp.toolbar.floating.vibrant.button.selected.container.color | COLOR | Color | `md.sys.color.surface-container` |  | Deprecating this token here and putting it into the color token set |
| md.comp.toolbar.floating.container.height | LENGTH | Size | 64dp |  | Deprecating this for a vertical and horizontal variant |
| md.comp.toolbar.floating.horizontal.container.height | LENGTH | Size | 64dp |  |  |
| md.comp.toolbar.floating.vertical.container.width | LENGTH | Size | 64dp |  |  |
| md.comp.toolbar.floating.container.leading-space | LENGTH | Size | 8dp |  |  |
| md.comp.toolbar.floating.container.trailing-space | LENGTH | Size | 8dp |  |  |
| md.comp.toolbar.floating.container.external-padding | LENGTH | Size | 16dp |  | Deprecating for a vertical and horizontal option |
| md.comp.toolbar.floating.horizontal.container.external-space | LENGTH | Size | 16dp |  |  |
| md.comp.toolbar.floating.vertical.container.external-space | LENGTH | Size | 24dp |  |  |
| md.comp.toolbar.floating.container.between-space | LENGTH | Size | 4dp |  |  |
| md.comp.toolbar.floating.container.shape | SHAPE | Shape | `md.sys.shape.corner.full` |  |  |
| md.comp.toolbar.floating.container.elevation | ELEVATION | Elevation | `md.sys.elevation.level3` |  |  |

#### Toolbar - Color - Standard (md.comp.toolbar.standard)

For standard docked and floating toolbars.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.toolbar.standard.container.color | COLOR | Enabled | `md.sys.color.surface-container` |  |  |
| md.comp.toolbar.standard.button.container.color | COLOR | Enabled | `md.sys.color.surface-container` |  |  |
| md.comp.toolbar.standard.selected.button.container.color | COLOR | Enabled | `md.sys.color.secondary-container` |  |  |
| md.comp.toolbar.standard.icon.color | COLOR | Enabled | `md.sys.color.on-surface-variant` |  |  |
| md.comp.toolbar.standard.selected.icon.color | COLOR | Enabled | `md.sys.color.on-secondary-container` |  |  |
| md.comp.toolbar.standard.label-text.color | COLOR | Enabled | `md.sys.color.on-surface-variant` |  |  |
| md.comp.toolbar.standard.selected.label-text.color | COLOR | Enabled | `md.sys.color.on-secondary-container` |  |  |
| md.comp.toolbar.standard.container.shape | SHAPE | Enabled | `md.sys.shape.corner.full` |  |  |
| md.comp.toolbar.standard.disabled.icon.color | COLOR | Disabled | `md.sys.color.on-surface` |  |  |
| md.comp.toolbar.standard.disabled.icon.opacity | OPACITY | Disabled | 0.38 |  |  |
| md.comp.toolbar.standard.disabled.label-text.color | COLOR | Disabled | `md.sys.color.on-surface` |  |  |
| md.comp.toolbar.standard.disabled.label-text.opacity | OPACITY | Disabled | 0.38 |  |  |
| md.comp.toolbar.standard.hovered.state-layer.color | COLOR | Hovered | `md.sys.color.on-surface-variant` |  |  |
| md.comp.toolbar.standard.selected.hovered.state-layer.color | COLOR | Hovered | `md.sys.color.on-secondary-container` |  |  |
| md.comp.toolbar.standard.hovered.state-layer.opacity | OPACITY | Hovered | `md.sys.state.hover.state-layer-opacity` |  |  |
| md.comp.toolbar.standard.hovered.icon.color | COLOR | Hovered | `md.sys.color.on-surface-variant` |  |  |
| md.comp.toolbar.standard.selected.hovered.icon.color | COLOR | Hovered | `md.sys.color.on-secondary-container` |  |  |
| md.comp.toolbar.standard.hovered.label-text.color | COLOR | Hovered | `md.sys.color.on-surface-variant` |  |  |
| md.comp.toolbar.standard.selected.hovered.label-text.color | COLOR | Hovered | `md.sys.color.on-secondary-container` |  |  |
| md.comp.toolbar.standard.focused.state-layer.color | COLOR | Focused | `md.sys.color.on-surface-variant` |  |  |
| md.comp.toolbar.standard.selected.focused.state-layer.color | COLOR | Focused | `md.sys.color.on-secondary-container` |  |  |
| md.comp.toolbar.standard.focused.state-layer.opacity | OPACITY | Focused | `md.sys.state.focus.state-layer-opacity` |  |  |
| md.comp.toolbar.standard.focused.icon.color | COLOR | Focused | `md.sys.color.on-surface-variant` |  |  |
| md.comp.toolbar.standard.selected.focused.icon.color | COLOR | Focused | `md.sys.color.on-secondary-container` |  |  |
| md.comp.toolbar.standard.focused.label-text.color | COLOR | Focused | `md.sys.color.on-surface-variant` |  |  |
| md.comp.toolbar.standard.selected.focused.label-text.color | COLOR | Focused | `md.sys.color.on-secondary-container` |  |  |
| md.comp.toolbar.standard.pressed.state-layer.color | COLOR | Pressed | `md.sys.color.on-surface-variant` |  |  |
| md.comp.toolbar.standard.selected.pressed.state-layer.color | COLOR | Pressed | `md.sys.color.on-secondary-container` |  |  |
| md.comp.toolbar.standard.pressed.state-layer.opacity | OPACITY | Pressed | `md.sys.state.pressed.state-layer-opacity` |  |  |
| md.comp.toolbar.standard.pressed.icon.color | COLOR | Pressed | `md.sys.color.on-surface-variant` |  |  |
| md.comp.toolbar.standard.selected.pressed.icon.color | COLOR | Pressed | `md.sys.color.on-secondary-container` |  |  |
| md.comp.toolbar.standard.pressed.label-text.color | COLOR | Pressed | `md.sys.color.on-surface-variant` |  |  |
| md.comp.toolbar.standard.selected.pressed.label-text.color | COLOR | Pressed | `md.sys.color.on-secondary-container` |  |  |

#### Toolbar - Color - Vibrant (md.comp.toolbar.vibrant)

For vibrant docked and floating toolbars.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.toolbar.vibrant.container.color | COLOR | Enabled | `md.sys.color.primary-container` |  |  |
| md.comp.toolbar.vibrant.button.container.color | COLOR | Enabled | `md.sys.color.primary-container` |  |  |
| md.comp.toolbar.vibrant.selected.button.container.color | COLOR | Enabled | `md.sys.color.surface-container` |  |  |
| md.comp.toolbar.vibrant.icon.color | COLOR | Enabled | `md.sys.color.on-primary-container` |  |  |
| md.comp.toolbar.vibrant.selected.icon.color | COLOR | Enabled | `md.sys.color.on-surface` |  |  |
| md.comp.toolbar.vibrant.label-text.color | COLOR | Enabled | `md.sys.color.on-primary-container` |  |  |
| md.comp.toolbar.vibrant.selected.label-text.color | COLOR | Enabled | `md.sys.color.on-surface` |  |  |
| md.comp.toolbar.vibrant.container.shape | SHAPE | Enabled | `md.sys.shape.corner.full` |  |  |
| md.comp.toolbar.vibrant.disabled.icon.color | COLOR | Disabled | `md.sys.color.on-surface` |  |  |
| md.comp.toolbar.vibrant.disabled.icon.opacity | OPACITY | Disabled | 0.38 |  |  |
| md.comp.toolbar.vibrant.disabled.label-text.color | COLOR | Disabled | `md.sys.color.on-surface` |  |  |
| md.comp.toolbar.vibrant.disabled.label-text.opacity | OPACITY | Disabled | 0.38 |  |  |
| md.comp.toolbar.vibrant.hovered.state-layer.color | COLOR | Hovered | `md.sys.color.on-primary-container` |  |  |
| md.comp.toolbar.vibrant.selected.hovered.state-layer.color | COLOR | Hovered | `md.sys.color.on-surface` |  |  |
| md.comp.toolbar.vibrant.hovered.state-layer.opacity | OPACITY | Hovered | `md.sys.state.hover.state-layer-opacity` |  |  |
| md.comp.toolbar.vibrant.hovered.icon.color | COLOR | Hovered | `md.sys.color.on-primary-container` |  |  |
| md.comp.toolbar.vibrant.selected.hovered.icon.color | COLOR | Hovered | `md.sys.color.on-surface` |  |  |
| md.comp.toolbar.vibrant.hovered.label-text.color | COLOR | Hovered | `md.sys.color.on-primary-container` |  |  |
| md.comp.toolbar.vibrant.selected.hovered.label-text.color | COLOR | Hovered | `md.sys.color.on-surface` |  |  |
| md.comp.toolbar.vibrant.focused.state-layer.color | COLOR | Focused | `md.sys.color.on-primary-container` |  |  |
| md.comp.toolbar.vibrant.selected.focused.state-layer.color | COLOR | Focused | `md.sys.color.on-surface` |  |  |
| md.comp.toolbar.vibrant.focused.state-layer.opacity | OPACITY | Focused | `md.sys.state.focus.state-layer-opacity` |  |  |
| md.comp.toolbar.vibrant.focused.icon.color | COLOR | Focused | `md.sys.color.on-primary-container` |  |  |
| md.comp.toolbar.vibrant.selected.focused.icon.color | COLOR | Focused | `md.sys.color.on-surface` |  |  |
| md.comp.toolbar.vibrant.focused.label-text.color | COLOR | Focused | `md.sys.color.on-primary-container` |  |  |
| md.comp.toolbar.vibrant.selected.focused.label-text.color | COLOR | Focused | `md.sys.color.on-surface` |  |  |
| md.comp.toolbar.vibrant.pressed.state-layer.color | COLOR | Pressed | `md.sys.color.on-primary-container` |  |  |
| md.comp.toolbar.vibrant.selected.pressed.state-layer.color | COLOR | Pressed | `md.sys.color.on-surface` |  |  |
| md.comp.toolbar.vibrant.pressed.state-layer.opacity | OPACITY | Pressed | `md.sys.state.pressed.state-layer-opacity` |  |  |
| md.comp.toolbar.vibrant.pressed.icon.color | COLOR | Pressed | `md.sys.color.on-primary-container` |  |  |
| md.comp.toolbar.vibrant.selected.pressed.icon.color | COLOR | Pressed | `md.sys.color.on-surface` |  |  |
| md.comp.toolbar.vibrant.pressed.label-text.color | COLOR | Pressed | `md.sys.color.on-primary-container` |  |  |
| md.comp.toolbar.vibrant.selected.pressed.label-text.color | COLOR | Pressed | `md.sys.color.on-surface` |  |  |

#### Toolbar - Floating - FAB (md.comp.toolbar.floating.fab)

For floating toolbars with a FAB in them.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.toolbar.floating.fab.between-space | LENGTH |  | 8dp |  |  |
| md.comp.toolbar.floating.fab.standard.container.color | COLOR |  | `md.sys.color.secondary-container` |  |  |
| md.comp.toolbar.floating.fab.standard.icon.color | COLOR |  | `md.sys.color.on-secondary-container` |  |  |
| md.comp.toolbar.floating.fab.vibrant.container.color | COLOR |  | `md.sys.color.tertiary-container` |  |  |
| md.comp.toolbar.floating.fab.vibrant.icon.color | COLOR |  | `md.sys.color.on-tertiary-container` |  |  |
| md.comp.toolbar.floating.fab.container.height | LENGTH | Expanded | 56dp |  |  |
| md.comp.toolbar.floating.fab.container.width | LENGTH | Expanded | 56dp |  |  |
| md.comp.toolbar.floating.fab.icon.size | LENGTH | Expanded | 24dp |  |  |
| md.comp.toolbar.floating.fab.container.shape | SHAPE | Expanded | `md.sys.shape.corner.large` |  |  |
| md.comp.toolbar.floating.fab.container.elevation | ELEVATION | Expanded | `md.sys.elevation.level1` |  |  |
| md.comp.toolbar.floating.fab.medium.container.height | LENGTH | Collapsed | 80dp |  |  |
| md.comp.toolbar.floating.fab.medium.container.width | LENGTH | Collapsed | 80dp |  |  |
| md.comp.toolbar.floating.fab.medium.icon.size | LENGTH | Collapsed | 28dp |  |  |
| md.comp.toolbar.floating.fab.medium.container.shape | SHAPE | Collapsed | `md.sys.shape.corner.large-increased` |  |  |
| md.comp.toolbar.floating.fab.medium.container.elevation | ELEVATION | Collapsed | `md.sys.elevation.level2` |  |  |

### Color

Color values are implemented through design tokens. For designers, this means working with color values that correspond with tokens. In implementation, a color value will be a token that references a value. [Learn more about design tokens](../../foundations/design-tokens/overview.md)

![Diagram of bottom app bar indicating its color mappings.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fma25vwtc-13-3P.png?alt=media&token=f5f362b3-e75c-46cd-b4b5-a38075e10434>)

_Bottom app bar color role used for light and dark themes: Surface container_

### Measurements

![Diagram showing layout values and paddings for bottom app bar.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fma25wn0p-14-3P.png?alt=media&token=6aeb9904-fefe-4dda-919b-3e0a6a46aa0b>)

_Bottom app bar padding and size measurements_

### Common layouts

![Side by side view of bottom app bars in different configurations.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fma25xkg5-15-3P.png?alt=media&token=2b89e735-e35e-408d-8c57-263f9a37339a>)

_Icon buttons and FAB; Icon buttons and no FAB_

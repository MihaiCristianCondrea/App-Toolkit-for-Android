---
title: "Menus"
source_url: "https://m3.material.io/components/menus/specs"
lastmod: "2026-05-18"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "components"
assets:
  - "assets/images/545fcc0e9973-mhp3tuy7-01-3P.png"
  - "assets/images/ac47a512f481-mhp47zoj-02-3P.png"
  - "assets/images/f0cf9e8fa83b-mhp4bydj-03-3P.png"
  - "assets/images/73d30fc27a4f-mhp4i2in-05-3P.png"
  - "assets/images/2dd13ba0baee-mhp4jv7k-07-3P.png"
  - "assets/images/47b2b0fa21d6-mhp4r65m-08-3P.png"
  - "assets/images/40f062cfb9f1-mhp4scno-10-3P.png"
  - "assets/images/e7162c531f34-mhp4upew-14-3P.png"
  - "assets/images/2f5c6a18bddc-mi5nwcqt-17-3P.png"
  - "assets/images/9a6efb43efd7-mhp5x7zn-19-3P.png"
  - "assets/images/dcc62b78f411-mhp5xzj4-20-3P.png"
  - "assets/images/cc760ae85cc1-mhp6035c-21-3P.png"
  - "assets/images/8fe1055974fd-mhp61n8r-22-3P.png"
  - "assets/images/a03caf2318fb-mhp6b6dr-23-3P.png"
  - "assets/images/fdc182de8e53-mhp6c83m-24-3P.png"
---

# Menus

## Specs

## Variants

### Vertical menus

Use vertical menus for a more expressive look and feel, including rounded corners, standard and vibrant color styles, more selection states, and submenu motion.

![2 vertical menus use shape and color to indicate selected state.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmhp3tuy7-01-3P.png?alt=media&token=53c53a22-2391-40c9-bb50-0a1bae72569e>)

_Vertical menu with gap; Vertical menu with divider_

### Baseline variant

In M3 Expressive, baseline menu is still available to use, but doesn’t have the latest shapes, color styles, selection states, and motion. [See baseline menu specs](specs.md#a80df2f9-8610-4ce0-b3a3-b9ee749d5c98)

![A baseline menu variant with square corners and standard colors.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmhp47zoj-02-3P.png?alt=media&token=b7fa2ee6-ebb2-4d3c-a6e1-19e4a8edc452>)

_A baseline menu has square corners, as compared to a vertical menu’s round corners and expressive styling_

| **Variant** | **M3** | **M3 Expressive** |
| --- | --- | --- |
| Vertical menus | \-- | Available |
| Menu (baseline) | Available | Available |

## Configurations

### Vertical menus layout

![2 menus: 1 standard, and 1 with a gap, creating groups.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmhp4bydj-03-3P.png?alt=media&token=2eb83f0c-2aa9-4736-9acc-8f10b6052dbf>)

_Standard; Grouped_

| **Category ** | **Configuration ** | **M3** | **M3 Expressiv****e** |
| --- | --- | --- | --- |
| Color | Standard | Available | Available |
| Vibrant | \-- | Available |  |
| Layout | Standard | Available | Available |
| Grouped | \-- | Available |  |

## Tokens & specs

Browse the component elements, attributes, tokens, and their values. [Learn about design tokens](../../foundations/design-tokens/overview.md)

### TOKEN_TABLE

Component: Menus

Configured context tags: None, Medium contrast, Default, High contrast, Static, 3P, Android, Dark, Light

#### Token sets

| Token set | Name | Type | Tokens | Description |
| --- | --- | --- | --- | --- |
| Menus - Common | md.comp.menus | COMPONENT | 59 | Common tokens for expressive menus. For baseline menus, use "Menu (baseline)" tokens `md.comp.menu`. |
| Menus - Color - Vibrant | md.comp.menus.vibrant | COMPONENT | 70 | Color tokens for the vibrant expressive menu. |
| Menus - Color - Standard | md.comp.menus.standard | COMPONENT | 84 | Color tokens for the standard expressive menu. |

#### Menus - Common (md.comp.menus)

Common tokens for expressive menus. For baseline menus, use "Menu (baseline)" tokens `md.comp.menu`.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.menus.menu-item.label-text.font | FONT_NAMES | Typography | `md.sys.typescale.label-large.font` |  |  |
| md.comp.menus.menu-item.label-text.line-height | LINE_HEIGHT | Typography | `md.sys.typescale.label-large.line-height` |  |  |
| md.comp.menus.menu-item.label-text.size | FONT_SIZE | Typography | `md.sys.typescale.label-large.size` |  |  |
| md.comp.menus.menu-item.label-text.tracking | FONT_TRACKING | Typography | `md.sys.typescale.label-large.tracking` |  |  |
| md.comp.menus.menu-item.label-text.weight | FONT_WEIGHT | Typography | `md.sys.typescale.label-large.weight` |  |  |
| md.comp.menus.menu-item.supporting-text.font | FONT_NAMES | Typography | `md.sys.typescale.body-small.font` |  |  |
| md.comp.menus.menu-item.supporting-text.line-height | LINE_HEIGHT | Typography | `md.sys.typescale.body-small.line-height` |  |  |
| md.comp.menus.menu-item.supporting-text.size | FONT_SIZE | Typography | `md.sys.typescale.body-small.size` |  |  |
| md.comp.menus.menu-item.supporting-text.tracking | FONT_TRACKING | Typography | `md.sys.typescale.body-small.tracking` |  |  |
| md.comp.menus.menu-item.supporting-text.weight | FONT_WEIGHT | Typography | `md.sys.typescale.body-small.weight` |  |  |
| md.comp.menus.menu-item.trailing-supporting-text.font | FONT_NAMES | Typography | `md.sys.typescale.label-large.font` |  |  |
| md.comp.menus.menu-item.trailing-supporting-text.line-height | LINE_HEIGHT | Typography | `md.sys.typescale.label-large.line-height` |  |  |
| md.comp.menus.menu-item.trailing-supporting-text.size | FONT_SIZE | Typography | `md.sys.typescale.label-large.size` |  |  |
| md.comp.menus.menu-item.trailing-supporting-text.tracking | FONT_TRACKING | Typography | `md.sys.typescale.label-large.tracking` |  |  |
| md.comp.menus.menu-item.trailing-supporting-text.weight | FONT_WEIGHT | Typography | `md.sys.typescale.label-large.weight` |  |  |
| md.comp.menus.menu-item.selected.shape | SHAPE | Shape / Selected | `md.sys.shape.corner.medium` |  |  |
| md.comp.menus.horizontal.container.shape | SHAPE | Shape / Horizontal, Default | `md.sys.shape.corner.full` |  |  |
| md.comp.menus.horizontal.menu-item.hovered.shape | SHAPE | Shape / Horizontal, Default | `md.sys.shape.corner.medium` |  |  |
| md.comp.menus.horizontal.menu-item.focused.shape | SHAPE | Shape / Horizontal, Default | `md.sys.shape.corner.medium` |  |  |
| md.comp.menus.horizontal.menu-item.pressed.shape | SHAPE | Shape / Horizontal, Default | `md.sys.shape.corner.medium` |  |  |
| md.comp.menus.horizontal.menu-item.selected.hovered.shape | SHAPE | Shape / Horizontal, Selected | `md.sys.shape.corner.full` |  |  |
| md.comp.menus.horizontal.menu-item.selected.focused.shape | SHAPE | Shape / Horizontal, Selected | `md.sys.shape.corner.full` |  |  |
| md.comp.menus.horizontal.menu-item.selected.pressed.shape | SHAPE | Shape / Horizontal, Selected | `md.sys.shape.corner.full` |  |  |
| md.comp.menus.horizontal.icon-only.menu-item.selected.shape | SHAPE | Shape / Horizontal icon-only, selected | `md.sys.shape.corner.full` |  |  |
| md.comp.menus.container.shape | SHAPE | Shape / Default | `md.sys.shape.corner.large` |  |  |
| md.comp.menus.active.container.shape | LENGTH | Shape / Default | `md.sys.shape.corner.large` |  |  |
| md.comp.menus.inactive.container.shape | SHAPE | Shape / Default | `md.sys.shape.corner.small` |  |  |
| md.comp.menus.group.shape | SHAPE | Shape / Default | `md.sys.shape.corner.small` |  |  |
| md.comp.menus.menu-item.shape | SHAPE | Shape / Default | `md.sys.shape.corner.extra-small` |  |  |
| md.comp.menus.menu-item.first-child.shape | SHAPE | Shape / Default | `md.sys.shape.corner.medium` |  |  |
| md.comp.menus.menu-item.first-child.inner-corner.corner-size | SHAPE | Shape / Default | `md.sys.shape.corner.extra-small` |  |  |
| md.comp.menus.menu-item.last-child.shape | SHAPE | Shape / Default | `md.sys.shape.corner.medium` |  |  |
| md.comp.menus.menu-item.last-child.inner-corner.corner-size | SHAPE | Shape / Default | `md.sys.shape.corner.extra-small` |  |  |
| md.comp.menus.horizontal.container.top-space | LENGTH | Layout / Horizontal | 8dp |  |  |
| md.comp.menus.horizontal.container.bottom-space | LENGTH | Layout / Horizontal | 8dp |  |  |
| md.comp.menus.horizontal.menu-item.leading-space | LENGTH | Layout / Horizontal | 12dp |  |  |
| md.comp.menus.horizontal.menu-item.trailing-space | LENGTH | Layout / Horizontal | 12dp |  |  |
| md.comp.menus.horizontal.menu-item.between-space | LENGTH | Layout / Horizontal | 12dp |  |  |
| md.comp.menus.horizontal.menu-item.top-space | LENGTH | Layout / Horizontal | 6dp |  |  |
| md.comp.menus.horizontal.menu-item.bottom-space | LENGTH | Layout / Horizontal | 6dp |  |  |
| md.comp.menus.horizontal.icon-only.menu-item.leading-space | LENGTH | Layout / Horizontal icon-only | 16dp |  |  |
| md.comp.menus.horizontal.icon-only.menu-item.trailing-space | LENGTH | Layout / Horizontal icon-only | 16dp |  |  |
| md.comp.menus.horizontal.icon-only.menu-item.top-space | LENGTH | Layout / Horizontal icon-only | 16dp |  |  |
| md.comp.menus.horizontal.icon-only.menu-item.bottom-space | LENGTH | Layout / Horizontal icon-only | 16dp |  |  |
| md.comp.menus.horizontal.icon-only.gap | LENGTH | Layout / Horizontal icon-only | 4dp |  |  |
| md.comp.menus.gap | LENGTH | Layout / Default | 2dp |  |  |
| md.comp.menus.group.padding | LENGTH | Layout / Default | 2dp |  |  |
| md.comp.menus.container.elevation | ELEVATION | Layout / Default | `md.sys.elevation.level2` |  |  |
| md.comp.menus.menu-item.height | LENGTH | Layout / Default | 44dp |  |  |
| md.comp.menus.menu-item.top-space | LENGTH | Layout / Default | 8dp |  |  |
| md.comp.menus.menu-item.bottom-space | LENGTH | Layout / Default | 8dp |  |  |
| md.comp.menus.menu-item.leading-space | LENGTH | Layout / Default | 16dp |  |  |
| md.comp.menus.menu-item.trailing-space | LENGTH | Layout / Default | 16dp |  |  |
| md.comp.menus.menu-item.between-space | LENGTH | Layout / Default | 12dp |  |  |
| md.comp.menus.menu-item.leading-icon.size | LENGTH | Layout / Default | 20dp |  |  |
| md.comp.menus.menu-item.trailing-icon.size | LENGTH | Layout / Default | 20dp |  |  |
| md.comp.menus.menu-item.focus.indicator.color | COLOR | Focus ring | `md.sys.color.secondary` |  |  |
| md.comp.menus.menu-item.focus.indicator.thickness | LENGTH | Focus ring | `md.sys.state.focus-indicator.thickness` |  |  |
| md.comp.menus.menu-item.focus.indicator.outline.offset | LENGTH | Focus ring | `md.sys.state.focus-indicator.inner-offset` |  |  |

#### Menus - Color - Vibrant (md.comp.menus.vibrant)

Color tokens for the vibrant expressive menu.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.menus.vibrant.container.color | COLOR | Enabled | `md.sys.color.tertiary-container` |  |  |
| md.comp.menus.vibrant.menu-item.color | COLOR | Enabled | `md.sys.color.tertiary-container` |  |  |
| md.comp.menus.vibrant.section-label-text.color | COLOR | Enabled | `md.sys.color.on-tertiary-container` |  |  |
| md.comp.menus.vibrant.menu-item.label-text.color | COLOR | Enabled | `md.sys.color.on-tertiary-container` |  |  |
| md.comp.menus.vibrant.menu-item.supporting-text.color | COLOR | Enabled | `md.sys.color.on-tertiary-container` |  |  |
| md.comp.menus.vibrant.menu-item.trailing-supporting-text.color | COLOR | Enabled | `md.sys.color.on-tertiary-container` |  |  |
| md.comp.menus.vibrant.menu-item.leading-icon.color | COLOR | Enabled | `md.sys.color.on-tertiary-container` |  |  |
| md.comp.menus.vibrant.menu-item.trailing-icon.color | COLOR | Enabled | `md.sys.color.on-tertiary-container` |  |  |
| md.comp.menus.vibrant.icon-button.container.color | COLOR | Enabled | `md.sys.color.tertiary-container` |  |  |
| md.comp.menus.vibrant.icon-button.icon.color | COLOR | Enabled | `md.sys.color.on-tertiary-container` |  |  |
| md.comp.menus.vibrant.menu-item.disabled.label-text.color | COLOR | Disabled | `md.sys.color.on-tertiary-container` |  |  |
| md.comp.menus.vibrant.menu-item.disabled.label-text.opacity | OPACITY | Disabled | 0.38 |  |  |
| md.comp.menus.vibrant.menu-item.disabled.supporting-text.color | COLOR | Disabled | `md.sys.color.on-tertiary-container` |  |  |
| md.comp.menus.vibrant.menu-item.disabled.supporting-text.opacity | OPACITY | Disabled | 0.38 |  |  |
| md.comp.menus.vibrant.menu-item.disabled.trailing-supporting-text.color | COLOR | Disabled | `md.sys.color.on-tertiary-container` |  |  |
| md.comp.menus.vibrant.menu-item.disabled.trailing-supporting-text.opacity | OPACITY | Disabled | 0.38 |  |  |
| md.comp.menus.vibrant.menu-item.disabled.leading-icon.color | COLOR | Disabled | `md.sys.color.on-tertiary-container` |  |  |
| md.comp.menus.vibrant.menu-item.disabled.leading-icon.opacity | OPACITY | Disabled | 0.38 |  |  |
| md.comp.menus.vibrant.menu-item.disabled.trailing-icon.color | COLOR | Disabled | `md.sys.color.on-tertiary-container` |  |  |
| md.comp.menus.vibrant.menu-item.disabled.trailing-icon.opacity | OPACITY | Disabled | 0.38 |  |  |
| md.comp.menus.vibrant.icon-button.disabled.icon.color | COLOR | Disabled | `md.sys.color.on-tertiary-container` |  |  |
| md.comp.menus.vibrant.menu-item.hovered.state-layer.color | COLOR | Hovered | `md.sys.color.on-tertiary-container` |  |  |
| md.comp.menus.vibrant.menu-item.hovered.state-layer.opacity | OPACITY | Hovered | `md.sys.state.hover.state-layer-opacity` |  |  |
| md.comp.menus.vibrant.menu-item.hovered.label-text.color | COLOR | Hovered | `md.sys.color.on-tertiary-container` |  |  |
| md.comp.menus.vibrant.menu-item.hovered.supporting-text.color | COLOR | Hovered | `md.sys.color.on-tertiary-container` |  |  |
| md.comp.menus.vibrant.menu-item.hovered.trailing-supporting-text.color | COLOR | Hovered | `md.sys.color.on-tertiary-container` |  |  |
| md.comp.menus.vibrant.menu-item.hovered.leading-icon.color | COLOR | Hovered | `md.sys.color.tertiary` |  |  |
| md.comp.menus.vibrant.menu-item.hovered.trailing-icon.color | COLOR | Hovered | `md.sys.color.tertiary` |  |  |
| md.comp.menus.vibrant.icon-button.hovered.state-layer.color | COLOR | Hovered | `md.sys.color.on-tertiary-container` |  |  |
| md.comp.menus.vibrant.menu-item.focused.state-layer.color | COLOR | Focused | `md.sys.color.on-tertiary-container` |  |  |
| md.comp.menus.vibrant.menu-item.focused.state-layer.opacity | OPACITY | Focused | `md.sys.state.focus.state-layer-opacity` |  |  |
| md.comp.menus.vibrant.menu-item.focused.label-text.color | COLOR | Focused | `md.sys.color.on-tertiary-container` |  |  |
| md.comp.menus.vibrant.menu-item.focused.supporting-text.color | COLOR | Focused | `md.sys.color.on-tertiary-container` |  |  |
| md.comp.menus.vibrant.menu-item.focused.trailing-supporting-text.color | COLOR | Focused | `md.sys.color.on-tertiary-container` |  |  |
| md.comp.menus.vibrant.menu-item.focused.leading-icon.color | COLOR | Focused | `md.sys.color.tertiary` |  |  |
| md.comp.menus.vibrant.menu-item.focused.trailing-icon.color | COLOR | Focused | `md.sys.color.tertiary` |  |  |
| md.comp.menus.vibrant.icon-button.focused.state-layer.color | COLOR | Focused | `md.sys.color.on-tertiary-container` |  |  |
| md.comp.menus.vibrant.menu-item.pressed.state-layer.color | COLOR | Pressed | `md.sys.color.on-tertiary-container` |  |  |
| md.comp.menus.vibrant.menu-item.pressed.state-layer.opacity | OPACITY | Pressed | `md.sys.state.pressed.state-layer-opacity` |  |  |
| md.comp.menus.vibrant.menu-item.pressed.label-text.color | COLOR | Pressed | `md.sys.color.on-tertiary-container` |  |  |
| md.comp.menus.vibrant.menu-item.pressed.supporting-text.color | COLOR | Pressed | `md.sys.color.on-tertiary-container` |  |  |
| md.comp.menus.vibrant.menu-item.pressed.trailing-supporting-text.color | COLOR | Pressed | `md.sys.color.on-tertiary-container` |  |  |
| md.comp.menus.vibrant.menu-item.pressed.leading-icon.color | COLOR | Pressed | `md.sys.color.tertiary` |  |  |
| md.comp.menus.vibrant.menu-item.pressed.trailing-icon.color | COLOR | Pressed | `md.sys.color.tertiary` |  |  |
| md.comp.menus.vibrant.icon-button.pressed.state-layer.color | COLOR | Pressed | `md.sys.color.on-tertiary-container` |  |  |
| md.comp.menus.vibrant.menu-item.active.state-layer.color | COLOR | Enabled, Active | `md.sys.color.on-tertiary-container` |  |  |
| md.comp.menus.vibrant.menu-item.active.state-layer.opacity | OPACITY | Enabled, Active | `md.sys.state.hover.state-layer-opacity` |  |  |
| md.comp.menus.vibrant.menu-item.selected.container.color | COLOR | Enabled, Selected | `md.sys.color.tertiary` |  |  |
| md.comp.menus.vibrant.menu-item.selected.supporting-text.color | COLOR | Enabled, Selected | `md.sys.color.on-tertiary` |  |  |
| md.comp.menus.vibrant.menu-item.selected.trailing-supporting-text.color | COLOR | Enabled, Selected | `md.sys.color.on-tertiary` |  |  |
| md.comp.menus.vibrant.menu-item.selected.label-text.color | COLOR | Enabled, Selected | `md.sys.color.on-tertiary` |  |  |
| md.comp.menus.vibrant.menu-item.selected.leading-icon.color | COLOR | Enabled, Selected | `md.sys.color.on-tertiary` |  |  |
| md.comp.menus.vibrant.menu-item.selected.trailing-icon.color | COLOR | Enabled, Selected | `md.sys.color.on-tertiary` |  |  |
| md.comp.menus.vibrant.icon-button.selected.container.color | COLOR | Enabled, Selected | `md.sys.color.tertiary` |  |  |
| md.comp.menus.vibrant.icon-button.selected.icon.color | COLOR | Enabled, Selected | `md.sys.color.on-tertiary` |  |  |
| md.comp.menus.vibrant.menu-item.selected.disabled.label-text.opacity | OPACITY | Disabled, Selected | 0.38 |  |  |
| md.comp.menus.vibrant.menu-item.selected.disabled.supporting-text.opacity | OPACITY | Disabled, Selected | 0.38 |  |  |
| md.comp.menus.vibrant.menu-item.selected.disabled.trailing-supporting-text.opacity | OPACITY | Disabled, Selected | 0.38 |  |  |
| md.comp.menus.vibrant.menu-item.selected.disabled.leading-icon.opacity | OPACITY | Disabled, Selected | 0.38 |  |  |
| md.comp.menus.vibrant.menu-item.selected.disabled.trailing-icon.opacity | OPACITY | Disabled, Selected | 0.38 |  |  |
| md.comp.menus.vibrant.icon-button.selected.disabled.icon.color | COLOR | Disabled, Selected | `md.sys.color.on-tertiary` |  |  |
| md.comp.menus.vibrant.menu-item.selected.hovered.state-layer.color | COLOR | Hovered, Selected | `md.sys.color.on-tertiary` |  |  |
| md.comp.menus.vibrant.menu-item.selected.hovered.state-layer.opacity | OPACITY | Hovered, Selected | `md.sys.state.hover.state-layer-opacity` |  |  |
| md.comp.menus.vibrant.menu-item.selected.hovered.label-text.color | COLOR | Hovered, Selected | `md.sys.color.on-tertiary` |  |  |
| md.comp.menus.vibrant.menu-item.selected.focused.state-layer.color | COLOR | Focused, Selected | `md.sys.color.on-tertiary` |  |  |
| md.comp.menus.vibrant.menu-item.selected.focused.state-layer.opacity | OPACITY | Focused, Selected | `md.sys.state.focus.state-layer-opacity` |  |  |
| md.comp.menus.vibrant.menu-item.selected.focused.label-text.color | COLOR | Focused, Selected | `md.sys.color.on-tertiary` |  |  |
| md.comp.menus.vibrant.menu-item.selected.pressed.state-layer.color | COLOR | Pressed, Selected | `md.sys.color.on-tertiary` |  |  |
| md.comp.menus.vibrant.menu-item.selected.pressed.state-layer.opacity | OPACITY | Pressed, Selected | `md.sys.state.pressed.state-layer-opacity` |  |  |
| md.comp.menus.vibrant.menu-item.selected.pressed.label-text.color | COLOR | Pressed, Selected | `md.sys.color.on-tertiary` |  |  |

#### Menus - Color - Standard (md.comp.menus.standard)

Color tokens for the standard expressive menu.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.menus.standard.container.color | COLOR | Enabled | `md.sys.color.surface-container-low` |  |  |
| md.comp.menus.standard.section-label-text.color | COLOR | Enabled | `md.sys.color.on-surface-variant` |  |  |
| md.comp.menus.standard.menu-item.container.color | COLOR | Enabled | `md.sys.color.surface-container-low` |  |  |
| md.comp.menus.standard.menu-item.label-text.color | COLOR | Enabled | `md.sys.color.on-surface` |  |  |
| md.comp.menus.standard.menu-item.supporting-text.color | COLOR | Enabled | `md.sys.color.on-surface-variant` |  |  |
| md.comp.menus.standard.container.shadow-color | COLOR | Enabled | `md.sys.color.shadow` |  |  |
| md.comp.menus.standard.menu-item.leading-icon.color | COLOR | Enabled | `md.sys.color.on-surface-variant` |  |  |
| md.comp.menus.standard.menu-item.trailing-icon.color | COLOR | Enabled | `md.sys.color.on-surface-variant` |  |  |
| md.comp.menus.standard.menu-item.trailing-supporting-text.color | COLOR | Enabled | `md.sys.color.on-surface-variant` |  |  |
| md.comp.menus.standard.icon-button.container.color | COLOR | Enabled | `md.sys.color.surface-container-low` |  |  |
| md.comp.menus.standard.icon-button.icon.color | COLOR | Enabled | `md.sys.color.on-surface-variant` |  |  |
| md.comp.menus.standard.menu-item.disabled.label-text.color | COLOR | Disabled | `md.sys.color.on-surface` |  |  |
| md.comp.menus.standard.menu-item.disabled.label-text.opacity | OPACITY | Disabled | 0.38 |  |  |
| md.comp.menus.standard.menu-item.disabled.supporting-text.color | COLOR | Disabled | `md.sys.color.on-surface` |  |  |
| md.comp.menus.standard.menu-item.disabled.supporting-text.opacity | OPACITY | Disabled | 0.38 |  |  |
| md.comp.menus.standard.menu-item.disabled.trailing-supporting-text.color | COLOR | Disabled | `md.sys.color.on-surface` |  |  |
| md.comp.menus.standard.menu-item.disabled.trailing-supporting-text.opacity | OPACITY | Disabled | 0.38 |  |  |
| md.comp.menus.standard.menu-item.disabled.leading-icon.color | COLOR | Disabled | `md.sys.color.on-surface` |  |  |
| md.comp.menus.standard.menu-item.disabled.leading-icon.opacity | OPACITY | Disabled | 0.38 |  |  |
| md.comp.menus.standard.menu-item.disabled.trailing-icon.color | COLOR | Disabled | `md.sys.color.on-surface` |  |  |
| md.comp.menus.standard.menu-item.disabled.trailing-icon.opacity | OPACITY | Disabled | 0.38 |  |  |
| md.comp.menus.standard.disabled.icon-button.icon.color | COLOR | Disabled | `md.sys.color.on-surface` |  |  |
| md.comp.menus.standard.menu-item.hovered.state-layer.color | COLOR | Hovered | `md.sys.color.on-surface` |  |  |
| md.comp.menus.standard.menu-item.hovered.state-layer.opacity | OPACITY | Hovered | `md.sys.state.hover.state-layer-opacity` |  |  |
| md.comp.menus.standard.menu-item.hovered.label-text.color | COLOR | Hovered | `md.sys.color.on-surface` |  |  |
| md.comp.menus.standard.menu-item.hovered.leading-icon.color | COLOR | Hovered | `md.sys.color.on-surface-variant` |  |  |
| md.comp.menus.standard.menu-item.hovered.trailing-icon.color | COLOR | Hovered | `md.sys.color.on-surface-variant` |  |  |
| md.comp.menus.standard.icon-button.hovered.state-layer.color | COLOR | Hovered | `md.sys.color.on-surface` |  |  |
| md.comp.menus.standard.menu-item.focused.state-layer.color | COLOR | Focused | `md.sys.color.on-surface` |  |  |
| md.comp.menus.standard.menu-item.focused.state-layer.opacity | OPACITY | Focused | `md.sys.state.focus.state-layer-opacity` |  |  |
| md.comp.menus.standard.menu-item.focused.label-text.color | COLOR | Focused | `md.sys.color.on-surface` |  |  |
| md.comp.menus.standard.menu-item.focused.leading-icon.color | COLOR | Focused | `md.sys.color.on-surface-variant` |  |  |
| md.comp.menus.standard.menu-item.focused.trailing-icon.color | COLOR | Focused | `md.sys.color.on-surface-variant` |  |  |
| md.comp.menus.standard.icon-button.focused.state-layer.color | COLOR | Focused | `md.sys.color.on-surface` |  |  |
| md.comp.menus.standard.menu-item.pressed.state-layer.color | COLOR | Pressed | `md.sys.color.on-surface` |  |  |
| md.comp.menus.standard.menu-item.pressed.state-layer.opacity | OPACITY | Pressed | `md.sys.state.pressed.state-layer-opacity` |  |  |
| md.comp.menus.standard.menu-item.pressed.label-text.color | COLOR | Pressed | `md.sys.color.on-surface` |  |  |
| md.comp.menus.standard.menu-item.pressed.leading-icon.color | COLOR | Pressed | `md.sys.color.on-surface-variant` |  |  |
| md.comp.menus.standard.menu-item.pressed.trailing-icon.color | COLOR | Pressed | `md.sys.color.on-surface-variant` |  |  |
| md.comp.menus.standard.icon-button.pressed.state-layer.color | COLOR | Pressed | `md.sys.color.on-surface` |  |  |
| md.comp.menus.standard.menu-item.active.state-layer.color | COLOR | Enabled, Active | `md.sys.color.on-surface` |  |  |
| md.comp.menus.standard.menu-item.active.state-layer.opacity | OPACITY | Enabled, Active | `md.sys.state.hover.state-layer-opacity` |  |  |
| md.comp.menus.standard.menu-item.selected.container.color | COLOR | Enabled, Selected | `md.sys.color.tertiary-container` |  |  |
| md.comp.menus.standard.menu-item.selected.label-text.color | COLOR | Enabled, Selected | `md.sys.color.on-tertiary-container` |  |  |
| md.comp.menus.standard.menu-item.selected.supporting-text.color | COLOR | Enabled, Selected | `md.sys.color.on-tertiary-container` |  |  |
| md.comp.menus.standard.menu-item.selected.leading-icon.color | COLOR | Enabled, Selected | `md.sys.color.on-tertiary-container` |  |  |
| md.comp.menus.standard.menu-item.selected.trailing-icon.color | COLOR | Enabled, Selected | `md.sys.color.on-tertiary-container` |  |  |
| md.comp.menus.standard.menu-item.selected.trailing-supporting-text.color | COLOR | Enabled, Selected | `md.sys.color.on-tertiary-container` |  |  |
| md.comp.menus.standard.icon-button.selected.container.color | COLOR | Enabled, Selected | `md.sys.color.tertiary-container` |  |  |
| md.comp.menus.standard.icon-button.selected.icon.color | COLOR | Enabled, Selected | `md.sys.color.on-tertiary-container` |  |  |
| md.comp.menus.standard.menu-item.selected.disabled.container.color | COLOR | Disabled, Selected | `md.sys.color.tertiary-container` |  |  |
| md.comp.menus.standard.menu-item.selected.disabled.container.opacity | OPACITY | Disabled, Selected | 0.38 |  |  |
| md.comp.menus.standard.menu-item.selected.disabled.label-text.color | COLOR | Disabled, Selected | `md.sys.color.on-tertiary-container` |  |  |
| md.comp.menus.standard.menu-item.selected.disabled.label-text.opacity | OPACITY | Disabled, Selected | 0.38 |  |  |
| md.comp.menus.standard.menu-item.selected.disabled.leading-icon.color | COLOR | Disabled, Selected | `md.sys.color.on-tertiary-container` |  |  |
| md.comp.menus.standard.menu-item.selected.disabled.leading-icon.opacity | OPACITY | Disabled, Selected | 0.38 |  |  |
| md.comp.menus.standard.menu-item.selected.disabled.trailing-icon.color | COLOR | Disabled, Selected | `md.sys.color.on-tertiary-container` |  |  |
| md.comp.menus.standard.menu-item.selected.disabled.trailing-icon.opacity | OPACITY | Disabled, Selected | 0.38 |  |  |
| md.comp.menus.standard.menu-item.selected.disabled.trailing-supporting-text.color | COLOR | Disabled, Selected | `md.sys.color.on-tertiary-container` |  |  |
| md.comp.menus.standard.icon-button.disabled.icon.color | COLOR | Disabled, Selected | `md.sys.color.on-tertiary-container` |  |  |
| md.comp.menus.standard.menu-item.selected.hovered.state-layer.color | COLOR | Hovered, Selected | `md.sys.color.on-tertiary-container` |  |  |
| md.comp.menus.standard.menu-item.selected.hovered.state-layer.opacity | OPACITY | Hovered, Selected | `md.sys.state.hover.state-layer-opacity` |  |  |
| md.comp.menus.standard.menu-item.selected.hovered.label-text.color | COLOR | Hovered, Selected | `md.sys.color.on-tertiary-container` |  |  |
| md.comp.menus.standard.menu-item.selected.hovered.supporting-text.color | COLOR | Hovered, Selected | `md.sys.color.on-tertiary-container` |  |  |
| md.comp.menus.standard.menu-item.selected.hovered.trailing-supporting-text.color | COLOR | Hovered, Selected | `md.sys.color.on-tertiary-container` |  |  |
| md.comp.menus.standard.menu-item.selected.hovered.leading-icon.color | COLOR | Hovered, Selected | `md.sys.color.on-tertiary-container` |  |  |
| md.comp.menus.standard.menu-item.selected.hovered.trailing-icon.color | COLOR | Hovered, Selected | `md.sys.color.on-tertiary-container` |  |  |
| md.comp.menus.standard.icon-button.selected.hovered.state-layer.color | COLOR | Hovered, Selected | `md.sys.color.on-tertiary-container` |  |  |
| md.comp.menus.standard.menu-item.selected.focused.state-layer.color | COLOR | Focused, Selected | `md.sys.color.on-tertiary-container` |  |  |
| md.comp.menus.standard.menu-item.selected.focused.state-layer.opacity | OPACITY | Focused, Selected | `md.sys.state.focus.state-layer-opacity` |  |  |
| md.comp.menus.standard.menu-item.selected.focused.label-text.color | COLOR | Focused, Selected | `md.sys.color.on-tertiary-container` |  |  |
| md.comp.menus.standard.menu-item.selected.focused.supporting-text.color | COLOR | Focused, Selected | `md.sys.color.on-tertiary-container` |  |  |
| md.comp.menus.standard.menu-item.selected.focused.trailing-supporting-text.color | COLOR | Focused, Selected | `md.sys.color.on-tertiary-container` |  |  |
| md.comp.menus.standard.menu-item.selected.focused.leading-icon.color | COLOR | Focused, Selected | `md.sys.color.on-tertiary-container` |  |  |
| md.comp.menus.standard.menu-item.selected.focused.trailing-icon.color | COLOR | Focused, Selected | `md.sys.color.on-tertiary-container` |  |  |
| md.comp.menus.standard.icon-button.selected.focused.state-layer.color | COLOR | Focused, Selected | `md.sys.color.on-tertiary-container` |  |  |
| md.comp.menus.standard.menu-item.selected.pressed.state-layer.color | COLOR | Pressed, Selected | `md.sys.color.on-tertiary-container` |  |  |
| md.comp.menus.standard.menu-item.selected.pressed.state-layer.opacity | OPACITY | Pressed, Selected | `md.sys.state.pressed.state-layer-opacity` |  |  |
| md.comp.menus.standard.menu-item.selected.pressed.label-text.color | COLOR | Pressed, Selected | `md.sys.color.on-tertiary-container` |  |  |
| md.comp.menus.standard.menu-item.selected.pressed.supporting-text.color | COLOR | Pressed, Selected | `md.sys.color.on-tertiary-container` |  |  |
| md.comp.menus.standard.menu-item.selected.pressed.trailing-supporting-text.color | COLOR | Pressed, Selected | `md.sys.color.on-tertiary-container` |  |  |
| md.comp.menus.standard.menu-item.selected.pressed.leading-icon.color | COLOR | Pressed, Selected | `md.sys.color.on-tertiary-container` |  |  |
| md.comp.menus.standard.menu-item.selected.pressed.trailing-icon.color | COLOR | Pressed, Selected | `md.sys.color.on-tertiary-container` |  |  |
| md.comp.menus.standard.icon-button.selected.pressed.state-layer.color | COLOR | Pressed, Selected | `md.sys.color.on-tertiary-container` |  |  |

## Anatomy

### Vertical menus

![A diagram of a vertical menu.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmhp4i2in-05-3P.png?alt=media&token=8b39eb86-be0a-42d3-80cd-60b703d6180b>)

_Menu item; Leading icon (optional); Menu item text; Trailing icon (optional); Badge (optional); Trailing text (optional); Container; Supporting text (optional); Label text (optional); Gap (optional); Divider (optional); /_

## Color

Color values are implemented through design tokens. For designers, this means working with color values that correspond with tokens. In implementation, a color value will be a token that references a value. [Learn more about design tokens](../../foundations/design-tokens/overview.md)

Menus have two color mappings:

-   Standard: Surface-based
-   Vibrant: Tertiary-based

These mappings provide options for lower or higher visual emphasis. Vibrant menus are more prominent so should be used sparingly.

![2 vertical menus: 1 with lower visual emphasis, and 1 vibrant menu with bold shades.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmhp4jv7k-07-3P.png?alt=media&token=f38379bf-4e9c-46ae-9d1f-83b879e3f729>)

_Standard color scheme; Vibrant color scheme_

### Standard colors

![2 vertical menus with standard color roles mapped to 11 elements.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmhp4r65m-08-3P.png?alt=media&token=e7a0a642-b2a5-4124-8710-f9fc5e508f25>)

_Vertical menus color roles used for light and dark themes: On surface variant; On surface; On surface (state layer); Surface container low; On surface variant; On surface variant; Tertiary container (selected); On tertiary container (selected); On surface variant; On surface variant; On tertiary container (selected); /_

### Vibrant colors

![2 vertical menus with vibrant color roles mapped to 11 elements.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmhp4scno-10-3P.png?alt=media&token=3e3303c5-3778-46df-929d-8433ac2a6d90>)

_Vertical menus color roles used for light and dark themes: On tertiary container; On tertiary container; On tertiary container (state layer); Tertiary container; On tertiary container; On tertiary container; Tertiary (selected); On tertiary (selected); On tertiary container; On tertiary container; On tertiary (selected)_

## States

States are visual representations used to communicate the status of a component or an interactive element. [More on interaction states](https://m3.material.io/m3/pages/interaction-states/overview)

Shape morphing in vertical menus creates an expressive active state. As focus moves between submenus, the corner shape changes to highlight the active menu. [More on menu focus](guidelines.md#7cc1d01b-a454-48c7-8306-e60347ffd17f)

![6 vertical menu states in light and dark themes.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmhp4upew-14-3P.png?alt=media&token=9a23182f-ffb3-4156-ae3c-9e6daf5e22f0>)

_Enabled; Disabled; Hovered; Focused; Pressed; Active (main menu reveals submenu); /_

## Measurements

![Vertical menu marked with spacing and padding measurements.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmi5nwcqt-17-3P.png?alt=media&token=0bbb9bfb-50f9-405c-af84-504554eebb6f>)

_Vertical menu padding and size measurements_

## Menu (baseline)

The baseline menu variant is available and continues to work in existing products. However, M3 expressive vertical menus are recommended for new designs.

### Baseline tokens & specs

Browse the component elements, attributes, tokens, and their values. [Learn about design tokens](../../foundations/design-tokens/overview.md)

### TOKEN_TABLE

Component: Menus

Configured context tags: None, Medium contrast, Default, High contrast, Static, 3P, Android, Dark, Light

#### Token sets

| Token set | Name | Type | Tokens | Description |
| --- | --- | --- | --- | --- |
| Menu (baseline) | md.comp.menu | COMPONENT | 49 | Tokens for baseline GM3 menus. For expressive menus, use `md.comp.menus` instead. |

#### Menu (baseline) (md.comp.menu)

Tokens for baseline GM3 menus. For expressive menus, use `md.comp.menus` instead.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.menu.container.color | COLOR | Enabled | `md.sys.color.surface-container` |  |  |
| md.comp.menu.container.shape | SHAPE | Enabled | `md.sys.shape.corner.extra-small` |  |  |
| md.comp.menu.container.shadow-color | COLOR | Enabled | `md.sys.color.shadow` |  |  |
| md.comp.menu.container.elevation | ELEVATION | Enabled | `md.sys.elevation.level2` |  |  |
| md.comp.menu.list-item.selected.label-text.color | COLOR | Enabled / Container | `md.sys.color.on-secondary-container` |  | Deprecated to remove nested tokens |
| md.comp.menu.list-item.selected.with-leading-icon.leading-icon.color | COLOR | Enabled / Container | `md.sys.color.on-secondary-container` |  | Deprecated to remove nested tokens |
| md.comp.menu.list-item.selected.with-leading-icon.trailing-icon.color | COLOR | Enabled / Container | `md.sys.color.on-secondary-container` |  | Deprecated to remove nested tokens |
| md.comp.menu.container.surface-tint-layer.color | COLOR | Enabled / Container | `md.sys.color.surface-tint` |  | Deprecated as part of the update from opacity based surfaces to tonal surfaces. Surfaces no longer use surface-tint layers for tinting, please use the desired surface role directly as the container color. |
| md.comp.menu.list-item.container.height | LENGTH | Enabled / Container | 48dp |  | Deprecated to reduce reliance on embedded component tokens. Please use standalone list component tokens instead. |
| md.comp.menu.list-item.selected.container.color | COLOR | Enabled / Container | `md.sys.color.secondary-container` |  | Deprecated to remove nested tokens |
| md.comp.menu.list-item.label-text.color | COLOR | Enabled / Label text | `md.sys.color.on-surface` |  | Deprecated to reduce reliance on embedded component tokens. Please use standalone list component tokens instead. |
| md.comp.menu.list-item.label-text.font | FONT_NAMES | Enabled / Label text | `md.sys.typescale.label-large.font` |  | Deprecated to reduce reliance on embedded component tokens. Please use standalone list component tokens instead. |
| md.comp.menu.list-item.label-text.line-height | LINE_HEIGHT | Enabled / Label text | `md.sys.typescale.label-large.line-height` |  | Deprecated to reduce reliance on embedded component tokens. Please use standalone list component tokens instead. |
| md.comp.menu.list-item.label-text.size | FONT_SIZE | Enabled / Label text | `md.sys.typescale.label-large.size` |  | Deprecated to reduce reliance on embedded component tokens. Please use standalone list component tokens instead. |
| md.comp.menu.list-item.label-text.tracking | FONT_TRACKING | Enabled / Label text | `md.sys.typescale.label-large.tracking` |  | Deprecated to reduce reliance on embedded component tokens. Please use standalone list component tokens instead. |
| md.comp.menu.list-item.label-text.weight | FONT_WEIGHT | Enabled / Label text | `md.sys.typescale.label-large.weight` |  | Deprecated to reduce reliance on embedded component tokens. Please use standalone list component tokens instead. |
| md.comp.menu.list-item.label-text.type | TYPOGRAPHY | Enabled / Label text | font name: `md.comp.menu.list-item.label-text.font`, font weight: `md.comp.menu.list-item.label-text.weight`, font size: `md.comp.menu.list-item.label-text.size`, font tracking: `md.comp.menu.list-item.label-text.tracking`, line height: `md.comp.menu.list-item.label-text.line-height` |  | Deprecated to reduce reliance on embedded component tokens. Please use standalone list component tokens instead. |
| md.comp.menu.cascading-menu-indicator.icon.color | COLOR | Enabled / Icon | `md.sys.color.on-surface-variant` |  | Deprecated to reduce reliance on embedded component tokens. Please use standalone list component tokens instead. |
| md.comp.menu.cascading-menu-indicator.icon.size | LENGTH | Enabled / Icon | 24dp |  | Deprecated to reduce reliance on embedded component tokens. Please use standalone list component tokens instead. |
| md.comp.menu.list-item.with-leading-icon.leading-icon.size | LENGTH | Enabled / Leading icon | 24dp |  | Deprecated to reduce reliance on embedded component tokens. Please use standalone list component tokens instead. |
| md.comp.menu.list-item.with-leading-icon.leading-icon.color | COLOR | Enabled / Leading icon | `md.sys.color.on-surface-variant` |  | Deprecated to reduce reliance on embedded component tokens. Please use standalone list component tokens instead. |
| md.comp.menu.list-item.with-trailing-icon.trailing-icon.size | LENGTH | Enabled / Trailing icon | 24dp |  | Deprecated to reduce reliance on embedded component tokens. Please use standalone list component tokens instead. |
| md.comp.menu.list-item.with-trailing-icon.trailing-icon.color | COLOR | Enabled / Trailing icon | `md.sys.color.on-surface-variant` |  | Deprecated to reduce reliance on embedded component tokens. Please use standalone list component tokens instead. |
| md.comp.menu.divider.color | COLOR | Enabled / Divider | `md.sys.color.surface-variant` |  | Depcrecating all divider tokens nested across components. Please use the standalone divider component token md.comp.divider.color as a replacement |
| md.comp.menu.divider.height | LENGTH | Enabled / Divider | 1dp |  | Depcrecating all divider tokens nested across components. Please use the standalone divider component token md.comp.divider.thickness as a replacement |
| md.comp.menu.list-item.with-leading-icon.disabled.leading-icon.color | COLOR | Disabled / Leading icon | `md.sys.color.on-surface` |  | Deprecated to reduce reliance on embedded component tokens. Please use standalone list component tokens instead. |
| md.comp.menu.list-item.with-leading-icon.disabled.leading-icon.opacity | OPACITY | Disabled / Leading icon | 0.38 |  | Deprecated to reduce reliance on embedded component tokens. Please use standalone list component tokens instead. |
| md.comp.menu.list-item.with-trailing-icon.disabled.trailing-icon.color | COLOR | Disabled / Trailing icon | `md.sys.color.on-surface` |  | Deprecated to reduce reliance on embedded component tokens. Please use standalone list component tokens instead. |
| md.comp.menu.list-item.with-trailing-icon.disabled.trailing-icon.opacity | OPACITY | Disabled / Trailing icon | 0.38 |  | Deprecated to reduce reliance on embedded component tokens. Please use standalone list component tokens instead. |
| md.comp.menu.list-item.disabled.label-text.color | COLOR | Disabled / Label text | `md.sys.color.on-surface` |  | Deprecated to reduce reliance on embedded component tokens. Please use standalone list component tokens instead. |
| md.comp.menu.list-item.disabled.label-text.opacity | OPACITY | Disabled / Label text | 0.38 |  | Deprecated to reduce reliance on embedded component tokens. Please use standalone list component tokens instead. |
| md.comp.menu.list-item.with-leading-icon.hover.icon.color | COLOR | Hover / Leading icon | `md.sys.color.on-surface-variant` |  | Deprecated to reduce reliance on embedded component tokens. Please use standalone list component tokens instead. |
| md.comp.menu.list-item.with-trailing-icon.hover.icon.color | COLOR | Hover / Trailing icon | `md.sys.color.on-surface-variant` |  | Deprecated to reduce reliance on embedded component tokens. Please use standalone list component tokens instead. |
| md.comp.menu.list-item.hover.state-layer.color | COLOR | Hover / State layer | `md.sys.color.on-surface` |  | Deprecated to reduce reliance on embedded component tokens. Please use standalone list component tokens instead. |
| md.comp.menu.list-item.hover.state-layer.opacity | OPACITY | Hover / State layer | `md.sys.state.hover.state-layer-opacity` |  | Deprecated to reduce reliance on embedded component tokens. Please use standalone list component tokens instead. |
| md.comp.menu.list-item.hover.label-text.color | COLOR | Hover / Label text | `md.sys.color.on-surface` |  | Deprecated to reduce reliance on embedded component tokens. Please use standalone list component tokens instead. |
| md.comp.menu.list-item.with-leading-icon.focus.icon.color | COLOR | Focus / Leading icon | `md.sys.color.on-surface-variant` |  | Deprecated to reduce reliance on embedded component tokens. Please use standalone list component tokens instead |
| md.comp.menu.list-item.with-trailing-icon.focus.icon.color | COLOR | Focus / Trailing icon | `md.sys.color.on-surface-variant` |  | Deprecated to reduce reliance on embedded component tokens. Please use standalone list component tokens instead |
| md.comp.menu.list-item.focus.label-text.color | COLOR | Focus / Label text | `md.sys.color.on-surface` |  | Deprecated to reduce reliance on embedded component tokens. Please use standalone list component tokens instead |
| md.comp.menu.list-item.focus.state-layer.color | COLOR | Focus / State layer | `md.sys.color.on-surface` |  | Deprecated to reduce reliance on embedded component tokens. Please use standalone list component tokens instead. |
| md.comp.menu.list-item.focus.state-layer.opacity | OPACITY | Focus / State layer | `md.sys.state.focus.state-layer-opacity` |  | Deprecated to reduce reliance on embedded component tokens. Please use standalone list component tokens instead. |
| md.comp.menu.list-item.with-leading-icon.pressed.icon.color | COLOR | Pressed / Leading icon | `md.sys.color.on-surface-variant` |  | Deprecated to reduce reliance on embedded component tokens. Please use standalone list component tokens instead |
| md.comp.menu.list-item.pressed.label-text.color | COLOR | Pressed / Label text | `md.sys.color.on-surface` |  | Deprecated to reduce reliance on embedded component tokens. Please use standalone list component tokens instead |
| md.comp.menu.list-item.with-trailing-icon.pressed.icon.color | COLOR | Pressed / Trailing icon | `md.sys.color.on-surface-variant` |  | Deprecated to reduce reliance on embedded component tokens. Please use standalone list component tokens instead |
| md.comp.menu.list-item.pressed.state-layer.color | COLOR | Pressed / State layer | `md.sys.color.on-surface` |  | Deprecated to reduce reliance on embedded component tokens. Please use standalone list component tokens instead |
| md.comp.menu.list-item.pressed.state-layer.opacity | OPACITY | Pressed / State layer | `md.sys.state.pressed.state-layer-opacity` |  | Deprecated to reduce reliance on embedded component tokens. Please use standalone list component tokens instead |
| md.comp.menu.focus.indicator.color | COLOR | Focus indicator | `md.sys.color.secondary` |  | Updating to include menu item |
| md.comp.menu.focus.indicator.thickness | LENGTH | Focus indicator | `md.sys.state.focus-indicator.thickness` |  | Updating to include menu item |
| md.comp.menu.focus.indicator.outline.offset | LENGTH | Focus indicator | `md.sys.state.focus-indicator.inner-offset` |  | Updating to include menu item |

### Anatomy

![Diagram of 6 elements of a baseline menu.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmhp5x7zn-19-3P.png?alt=media&token=bb80c591-2185-4774-938d-ade6c787d2cb>)

_List item; List item leading icon; List item trailing icon; Container; List item trailing text; Divider; /_

### Color

![9 color roles of a baseline menu in light and dark themes.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmhp5xzj4-20-3P.png?alt=media&token=608252b6-49b6-4fbb-950e-e34bb3b23874>)

_Baseline menu color roles used for light and dark themes: On surface variant; On surface; On surface - opacity: 0.08; Surface container; On surface variant; On surface variant; On surface variant; Surface container highest; Outline variant_

### States

#### Default menu items

![Diagram numbering the 5 default states of a baseline menu.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmhp6035c-21-3P.png?alt=media&token=0a96dac7-f21a-4e63-aab3-d77b4e2687d2>)

_Enabled; Disabled; Hovered; Focused; Pressed_

#### Selected menu items

![5 states of a selected baseline menu item.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmhp61n8r-22-3P.png?alt=media&token=c0c51609-bc02-4275-8017-41bedcf4de55>)

_Enabled; Disabled; Hovered; Focused; Pressed; /_

[State specs are in the token module above](specs.md#c811d2fa-469a-4e4e-9d9f-0f535c5c9b4c)

### Measurements

![Diagram of a baseline menu’s padding, text alignment, height, and width.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmhp6b6dr-23-3P.png?alt=media&token=be7b69e3-6291-4725-ab2c-ec443cfe07f0>)

_Baseline menu padding and size measurements_

| Attribute | Value |
| --- | --- |
| Container width | 112dp min, 280dp max |
| Corner radius | 4dp |
| Vertical label text alignment | Center-aligned |
| Horizontal label text alignment | Start-aligned |
| Left/right padding | 12dp |
| Left/right padding with-icon | 12dp |
| List item height | 48dp |
| Padding between elements within a list item | 12dp |
| Divider top/bottom padding | 8dp |
| Divider height | 1dp |
| Divider width | Dynamic |
| Leading/trailing icon size | 24dp |

### Configurations

A baseline menu appears when a person interacts with a button, action, or other control. 

A few examples:

1.  Button
2.  Text field
3.  Icon button
4.  Selected text

![Examples of 4 baseline menu inputs.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmhp6c83m-24-3P.png?alt=media&token=98b74598-7650-44d3-9fa5-ec73ed8b6564>)

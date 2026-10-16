---
title: "Carousel"
source_url: "https://m3.material.io/components/carousel/specs"
lastmod: "2026-05-18"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "components"
assets:
  - "assets/images/f0e518717353-lyr7cydt-1.png"
  - "assets/images/2be75efbfb97-lyr7dtgs-2.png"
  - "assets/images/3695dcfc51fd-m8tafq6c-3.png"
  - "assets/images/0c336d4ca8f5-mi9mff4k-04-3P.png"
  - "assets/images/000adff82c57-lyr7hu8z-5.png"
  - "assets/images/846a186503f7-mi9meptr-06-3P.png"
  - "assets/images/4a744f97bb28-lyr7jh4c-7.png"
  - "assets/images/0a3859d1e761-mi9mdco2-08-3P.png"
  - "assets/images/c2fe72213218-mi9lyvnk-09-3P.png"
  - "assets/images/4ee2a855ee11-mi9m4p2v-10-3P.png"
  - "assets/images/3eeefcd335c2-lyr7l425-9.png"
  - "assets/images/62f9588f1cd5-mi9macpj-12-3P.png"
  - "assets/images/53b0df6870a5-lyr7mksj-11.png"
  - "assets/images/0d0e2776f890-mi9mb8nd-14-3P.png"
  - "assets/images/c7f20d827210-lyr7o850-13.png"
  - "assets/images/9295a1d247b7-mi9mbqt5-16-3P.png"
---

# Carousel

Carousels show a collection of items that can be scrolled on and off the screen

## Specs

![4 elements of a carousel.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Flyr7cydt-1.png?alt=media&token=457c501d-9caa-4940-8089-c857014a3246>)

_Container; Large carousel item; Medium carousel item; Small carousel item_

## Tokens & specs

Browse the component elements, attributes, tokens, and their values.

### TOKEN_TABLE

Component: Carousel

Configured context tags: None, Medium contrast, Default, High contrast, 3P, Android, Dark, Light

#### Token sets

| Token set | Name | Type | Tokens | Description |
| --- | --- | --- | --- | --- |
| Carousel item | md.comp.carousel-item | COMPONENT | 27 | For carousel items. |

#### Carousel item (md.comp.carousel-item)

For carousel items.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.carousel-item.with-outline.outline.color | COLOR | Enabled / Outline | `md.sys.color.outline` |  |  |
| md.comp.carousel-item.with-outline.outline.width | ELEVATION | Enabled / Outline | 1dp |  |  |
| md.comp.carousel-item.container.color | COLOR | Enabled / Container | `md.sys.color.surface` |  |  |
| md.comp.carousel-item.container.elevation | ELEVATION | Enabled / Container | `md.sys.elevation.level0` |  |  |
| md.comp.carousel-item.container.shadow-color | COLOR | Enabled / Container | `md.sys.color.shadow` |  |  |
| md.comp.carousel-item.container.surface-tint-layer.color | COLOR | Enabled / Container | `md.sys.color.surface-tint` |  | Deprecated as part of the update from opacity based surfaces to tonal surfaces. Surfaces no longer use surface-tint layers for tinting, please use the desired surface role directly as the container color. |
| md.comp.carousel-item.container.shape | SHAPE | Enabled / Container | `md.sys.shape.corner.extra-large` |  |  |
| md.comp.carousel-item.hover.container.elevation | ELEVATION | Hover / Container | `md.sys.elevation.level1` |  |  |
| md.comp.carousel-item.hover.state-layer.color | COLOR | Hover / State layer | `md.sys.color.on-surface` |  |  |
| md.comp.carousel-item.hover.state-layer.opacity | OPACITY | Hover / State layer | `md.sys.state.hover.state-layer-opacity` |  |  |
| md.comp.carousel-item.with-outline.hover.outline.color | COLOR | Hover / Outline | `md.sys.color.outline` |  |  |
| md.comp.carousel-item.with-outline.focus.outline.color | COLOR | Focus / Outline | `md.sys.color.on-surface` |  |  |
| md.comp.carousel-item.focus.indicator.color | COLOR | Focus / Focus indicator | `md.sys.color.secondary` |  |  |
| md.comp.carousel-item.focus.indicator.thickness | LENGTH | Focus / Focus indicator | `md.sys.state.focus-indicator.thickness` |  |  |
| md.comp.carousel-item.focus.indicator.outline.offset | LENGTH | Focus / Focus indicator | `md.sys.state.focus-indicator.outer-offset` |  |  |
| md.comp.carousel-item.focus.container.elevation | ELEVATION | Focus / Container | `md.sys.elevation.level0` |  |  |
| md.comp.carousel-item.focus.state-layer.color | COLOR | Focus / State layer | `md.sys.color.on-surface` |  |  |
| md.comp.carousel-item.focus.state-layer.opacity | OPACITY | Focus / State layer | `md.sys.state.focus.state-layer-opacity` |  |  |
| md.comp.carousel-item.with-outline.pressed.outline.color | COLOR | Pressed (ripple) / Outline | `md.sys.color.outline` |  |  |
| md.comp.carousel-item.pressed.container.elevation | ELEVATION | Pressed (ripple) / Container | `md.sys.elevation.level0` |  |  |
| md.comp.carousel-item.pressed.state-layer.color | COLOR | Pressed (ripple) / State layer | `md.sys.color.on-surface` |  |  |
| md.comp.carousel-item.pressed.state-layer.opacity | OPACITY | Pressed (ripple) / State layer | `md.sys.state.pressed.state-layer-opacity` |  |  |
| md.comp.carousel-item.disabled.container.elevation | ELEVATION | Disabled / Container | `md.sys.elevation.level0` |  |  |
| md.comp.carousel-item.disabled.container.opacity | OPACITY | Disabled / Container | 0.38 |  |  |
| md.comp.carousel-item.disabled.container.color | COLOR | Disabled / Container | `md.sys.color.surface` |  |  |
| md.comp.carousel-item.with-outline.disabled.outline.color | COLOR | Disabled / Outline | `md.sys.color.outline` |  |  |
| md.comp.carousel-item.with-outline.disabled.outline.opacity | OPACITY | Disabled / Outline | 0.12 |  |  |

## Color

Color values are implemented through design tokens. For design, this means working with color values that correspond with tokens. For implementation, a color value will be a token that references a value. [Learn more about design tokens](https://m3.material.io/m3/pages/design-tokens/overview/)

![2 color roles of a carousel.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Flyr7dtgs-2.png?alt=media&token=88ace94e-9b83-4829-af0d-580102726097>)

_Carousel color roles used for light and dark schemes: Container; Surface_

## States

States are visual representations used to communicate the status of a component or interactive element. [Learn more about interaction states](https://m3.material.io/m3/pages/interaction-states/overview)

![5 states of a carousel in light and dark schemes.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fm8tafq6c-3.png?alt=media&token=89db79c9-95d1-48ac-b027-f80fce7811b0>)

_Enabled; Hovered; Focused; Pressed; Disabled_

## Carousel item dynamic widths

All kinds of carousel items dynamically adapt to the width of the container.

Large items have a customizable maximum width that's used to optimally fit carousel items into the available space.

Small carousel items have a minimum width of 40dp and a maximum width of 56dp.

Items change size as they move through the carousel layout.

![Measurements for a small carousel item.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmi9mff4k-04-3P.png?alt=media&token=07ae8548-be08-4e25-baf1-c4b314c683d5>)

_Small carousel items have a minimum and maximum width_

## Multi-browse

The multi-browse layout shows at least one large, medium, and small carousel item.

![4 elements of a multi-browse carousel layout.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Flyr7hu8z-5.png?alt=media&token=0fbbafde-d1ed-4c82-b8e8-c792c2f39794>)

_Container; Large carousel item; Medium carousel item; Small carousel item_

### Measurements

![Measurements of a multi-browse carousel layout.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmi9meptr-06-3P.png?alt=media&token=0caf8655-c1bd-40af-a64f-7dc9606544c0>)

_Multi-browse carousels have padding on both sides of the container_

| Attribute | Value |
| --- | --- |
| Alignment | Vertically centered |
| Leading/trailing padding | 16dp |
| Top/bottom padding | 8dp |
| Padding between elements | 8dp |
| Large item width | Dynamic, or user-set |
| Medium item width | Dynamic |
| Small item width | 40–56dp, dynamic |
| Item corner radius | 28dp |

## Uncontained

The uncontained layout shows items that scroll to the edge of the container.

![4 elements of an uncontained carousel layout.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Flyr7jh4c-7.png?alt=media&token=295f37e4-7b0d-4bea-a256-b2a38e9d0d3f>)

_Container; Large carousel item_

### Measurements

![Measurements of an uncontained carousel layout.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmi9mdco2-08-3P.png?alt=media&token=0878f292-7ad5-4738-9d2a-67b39cd69670>)

_Uncontained carousel items bleed over the padding on each side when scrolling_

| Attribute | Value |
| --- | --- |
| Alignment | Vertically centered |
| Leading padding | 16dp |
| Top/bottom padding | 8dp |
| Padding between elements | 8dp |
| Item corner radius | 28dp |

## Uncontained mutli-aspect ratio

The uncontained multi-aspect ratio layout shows carousel items of various widths.

![4 elements of an uncontained carousel layout](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmi9lyvnk-09-3P.png?alt=media&token=8fcbc1ce-ab70-44b7-8d93-62b4920f74b6>)

_Container; Carousel item (16:9); Carousel item (9:16); Carousel item (1:1); Carousel item (3:4)_

### Measurements

![Image](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmi9m4p2v-10-3P.png?alt=media&token=cbaae1be-c4fd-47b6-9ccd-c3ad2ed7d9a6>)

_Uncontained multi-aspect ratio carousels only have leading padding, with 8dp of padding between items._

| Attribute | Value |
| --- | --- |
| Alignment | Vertically centered |
| Leading padding | 16dp |
| Top/bottom padding | 8dp |
| Padding between elements | 8dp |
| Item corner radius | 28dp |

## Hero

The hero layout shows at least one large item and one small item.

![3 elements of a hero carousel layout.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Flyr7l425-9.png?alt=media&token=a5d4ade3-7977-496d-9605-174848266c7b>)

_Container; Large carousel item; Small carousel item_

### Measurements

![Measurements of a hero carousel layout.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmi9macpj-12-3P.png?alt=media&token=78cc4e71-d6af-4b8b-80c6-798e5845d1d0>)

_Hero carousels have padding on both sides of the container_

| Attribute | Value |
| --- | --- |
| Alignment | Vertically centered |
| Leading/Trailing padding | 16dp |
| Top/bottom padding | 8dp |
| Padding between elements | 8dp |
| Large item width | Dynamic |
| Small item width | 40-56dp, dynamic |
| Item corner radius | 28dp |

## Center-aligned hero

The center-aligned hero layout shows at least one large item and two small items.

![3 elements of a center-aligned hero carousel layout.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Flyr7mksj-11.png?alt=media&token=5530baf8-e9d0-44a1-9327-dbe0594233b8>)

_Container; Large carousel item; Small carousel item_

### Measurements

![Measurements of a center-aligned hero carousel layout.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmi9mb8nd-14-3P.png?alt=media&token=781e5673-c04c-4e31-b767-9235ccf87af7>)

_Center-aligned hero carousels have padding on both sides of the container_

| Attribute | Value |
| --- | --- |
| Alignment | Vertically centered |
| Leading/Trailing padding | 16dp |
| Top/bottom padding | 8dp |
| Padding between elements | 8dp |
| Large item width | Dynamic |
| Small item width | 40-56dp, dynamic |
| Item corner radius | 28dp |

## Full-screen

The full-screen layout shows one edge-to-edge large item.

![2 elements of a full-screen carousel layout.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Flyr7o850-13.png?alt=media&token=2075e453-f5f1-4372-8305-b05c2d7e0f3c>)

_Container; Large carousel item_

### Measurements

![Measurements of a full-screen carousel layout.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmi9mbqt5-16-3P.png?alt=media&token=5bc05830-cde0-428a-a596-1cc30c659e33>)

_Full-screen carousels fill the window edge-to-edge_

| Attribute | Value |
| --- | --- |
| Alignment | Centered |
| Leading/Trailing padding | 0dp |
| Top/bottom padding | 0dp |
| Padding between elements | 16dp |

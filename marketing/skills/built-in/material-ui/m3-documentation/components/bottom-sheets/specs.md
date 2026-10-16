---
title: "Bottom sheets"
source_url: "https://m3.material.io/components/bottom-sheets/specs"
lastmod: "2026-05-18"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "components"
assets:
  - "assets/images/df25caa58914-lvutved1-1.png"
  - "assets/images/7e92f1dbcbe8-lvutweax-2.png"
  - "assets/images/b54e6b3827c5-lvutx4kk-3.png"
---

# Bottom sheets

Bottom sheets show secondary content anchored to the bottom of the screen

## Specs

Modal bottom sheets are above a scrim while standard bottom sheets don't have a scrim. Besides this, both variants of bottom sheets have the same specs.

![Diagram of container, drag handle, scrim](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Flvutved1-1.png?alt=media&token=255fee5a-992a-4df3-bae9-ea3fde9c08a7>)

_Container; Drag handle (optional); Scrim_

## Tokens and specs

Browse the component elements, attributes, tokens, and their values. [Learn more about design tokens](../../foundations/design-tokens/overview.md)

### TOKEN_TABLE

Component: Bottom sheets

Configured context tags: None, Medium contrast, Default, High contrast, 3P, Android, Dark, Light

#### Token sets

| Token set | Name | Type | Tokens | Description |
| --- | --- | --- | --- | --- |
| Sheets - Bottom | md.comp.sheet.bottom | COMPONENT | 13 | For bottom sheets. |

#### Sheets - Bottom (md.comp.sheet.bottom)

For bottom sheets.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.sheet.bottom.docked.container.color | COLOR | Enabled / Container | `md.sys.color.surface-container-low` |  |  |
| md.comp.sheet.bottom.docked.container.surface-tint-layer.color | COLOR | Enabled / Container | `md.sys.color.surface-tint` |  | Deprecated as part of the update from opacity based surfaces to tonal surfaces. Surfaces no longer use surface-tint layers for tinting, please use the desired surface role directly as the container color. |
| md.comp.sheet.bottom.docked.modal.container.elevation | ELEVATION | Enabled / Container | `md.sys.elevation.level1` |  |  |
| md.comp.sheet.bottom.docked.standard.container.elevation | ELEVATION | Enabled / Container | `md.sys.elevation.level1` |  |  |
| md.comp.sheet.bottom.docked.container.shape | SHAPE | Enabled / Container | `md.sys.shape.corner.extra-large.top` |  |  |
| md.comp.sheet.bottom.docked.minimized.container.shape | SHAPE | Enabled / Container | `md.sys.shape.corner.none` |  |  |
| md.comp.sheet.bottom.docked.drag-handle.color | COLOR | Enabled / Drag handle | `md.sys.color.on-surface-variant` |  |  |
| md.comp.sheet.bottom.docked.drag-handle.opacity | OPACITY | Enabled / Drag handle | 0.4 |  | Deprecated per b/278783477 |
| md.comp.sheet.bottom.docked.drag-handle.width | LENGTH | Enabled / Drag handle | 32dp |  |  |
| md.comp.sheet.bottom.docked.drag-handle.height | LENGTH | Enabled / Drag handle | 4dp |  |  |
| md.comp.sheet.bottom.focus.indicator.color | COLOR | Enabled / Focus indicator | `md.sys.color.secondary` |  |  |
| md.comp.sheet.bottom.focus.indicator.thickness | LENGTH | Enabled / Focus indicator | `md.sys.state.focus-indicator.thickness` |  |  |
| md.comp.sheet.bottom.focus.indicator.outline.offset | LENGTH | Enabled / Focus indicator | `md.sys.state.focus-indicator.outer-offset` |  |  |

## Color

Color values are implemented through design tokens. For design, this means working with color values that correspond with tokens. For implementation, a color value will be a token that references a value. [Learn more about design tokens](../../foundations/design-tokens/overview.md)

![Two diagrams featuring color opposites of scrim, container, drag handle](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Flvutweax-2.png?alt=media&token=b648cac3-38a8-4ca1-8897-a573240be757>)

_Bottom sheet color roles used for both light and dark schemes: Scrim*; On surface variant; Surface container low; *On Android platforms, the scrim color and opacity is automatically handled by the system UI._

## Measurements

![Bottom sheet on larger device with 56dp top and 56dp side margins](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Flvutx4kk-3.png?alt=media&token=dd4a1d99-3cfc-444a-ae94-1c577604b3dc>)

_Bottom sheet padding and size measurements_

Bottom sheets span the full window width up to 640dp. When the window width exceeds 640dp, bottom sheets adjust to have a top margin of 56dp and side margins of 56dp. 

| Attribute | Value |
| --- | --- |
| Drag handle alignment (horizontal) | Center |
| Drag handle padding top/bottom | 22dp |
| Top margin | 72dp |
| Top margin (window width > 640dp) | 56dp |
| Start/end margin (window width > 640dp) | 56dp |
| Width | Full width, up to max-width 640dp |
| Height | Variable |

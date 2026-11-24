---
title: "Lists"
source_url: "https://m3.material.io/components/lists/specs"
lastmod: "2026-05-18"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "components"
assets:
  - "assets/images/74fff6d874e2-miq1b43h-frame.png"
  - "assets/images/3b456ca935e2-mio97lfj-01-3P.png"
  - "assets/images/8df4fd50f3b7-mio98r05-02-3P-VQA.png"
  - "assets/images/5d9f6ffce6ef-mio99zjp-03-3P.png"
  - "assets/images/721caf98b739-mio9bca0-04-3P-singleaction.png"
  - "assets/images/7ac3e4ab8082-mio9ecsb-05-3P-multiaction.png"
  - "assets/images/8ccd3c969c13-mio9x63z-06-3P-singleselect.png"
  - "assets/images/923759cc421f-mio9xiam-07-3P-multiselect.png"
  - "assets/videos/d882401ef931-miqm268j-GM3-Expressive-Specs-08-3P-v01-(1).mp4"
  - "assets/images/3826f2ff6539-mioasn71-11-3P-VQA.png"
  - "assets/images/2111c46ec444-mioecnyy-12-3P.png"
  - "assets/images/f40db4fff3bf-mioefhsu-13-3P-VQA.png"
  - "assets/images/d66a2e490fbc-mioehno9-14-do-3P.png"
  - "assets/images/4956ccec91eb-mioek6y5-15-dont-3P.png"
  - "assets/images/85549be8a1ab-mion9qya-16-3P.png"
  - "assets/images/6218de9f92c6-mionai1i-17-3P.png"
  - "assets/images/f254a2d7d520-mioncezo-18-3P.png"
  - "assets/images/f4d212c91b55-miondj5y-19-3P.png"
  - "assets/images/d387ffffe098-miq3n8p4-20-3P.png"
  - "assets/images/df279fe63fbb-miq3rr3p-21-3P.png"
  - "assets/images/f6a6166a3e80-miofu0i1-22-3P.png"
  - "assets/images/abd718195815-miogaa9o-23-3P.png"
  - "assets/images/10d6930cda55-miogazt3-24-3P.png"
  - "assets/images/6a10f7a62b4b-miogcld0-25-3P.png"
  - "assets/images/4158b3632bff-miogdayj-26-3P.png"
  - "assets/images/66b7a47d4f12-miogduex-27-3P.png"
  - "assets/images/c7f68d2e6d5c-mioh5e7e-28-3P.png"
  - "assets/images/c4120dba3e34-mioh5246-29-3P.png"
  - "assets/images/f8c8fd9257e0-mioh4nys-30-3P.png"
  - "assets/images/4e1e18904259-mioh3zha-31-3P.png"
  - "assets/images/2df5141fe05f-mioh3lil-32-3P.png"
  - "assets/images/bf8ea0c73a06-mioh2vwf-33-3P.png"
  - "assets/images/a47fa496da60-mioh2gxb-34-3P.png"
  - "assets/images/9bca09bd4728-mioh0mgk-35-3P.png"
---

# Lists

Lists are continuous, vertical indexes of text or images.

![Lists](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmiq1b43h-frame.png?alt=media&token=fc752db1-dd0a-4ab3-95fa-46598de9cffe>)

## Specs

## Variants

### Expressive lists

Use the expressive list variant for more flexible styling, highlighted selection states, and customizable slots.

![2 expressive lists: a photos list on a tablet, and a song list on mobile.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmio97lfj-01-3P.png?alt=media&token=65cdf253-7e5c-4de4-9cb2-b13c5f066963>)

_An expressive list has a segmented style and round corners_

### Baseline lists

In M3 Expressive, baseline lists are still available to use, but don’t have the latest visual style, selection treatment, and slot functionality.

[See baseline list specs](specs.md#94cf7f4d-fe29-4fab-9aae-a99e9b754329)

![3 baseline list items with square corners.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmio98r05-02-3P-VQA.png?alt=media&token=e21c7359-73e5-4cbb-8e8a-83b6fb3f46c9>)

_Baseline list items have square corners and standard colors_

| **Variants** | **M3** | **M3 Expressive** |
| --- | --- | --- |
| List (expressive) | \-- | Available |
| List (baseline) | Available | Available |

## Configurations

### Styles

The standard and segmented styles are a visual choice, and don’t affect a list’s behavior.

![A standard list and segmented list in dark mode.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmio99zjp-03-3P.png?alt=media&token=3a22c3a3-4de8-4def-94c7-ec35a39129b5>)

_Standard; Segmented_

### List selection modes

A list can have only one selection mode at a time. For example, a single-action list can change to a multi-select list, but can’t be both at once.

![A single-action list with 4 items and no additional actions.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmio9bca0-04-3P-singleaction.png?alt=media&token=8619eeab-8329-4101-a6a3-38f86e8596ed>)

_In a single-action list, each item is a single tappable area_

![A list with 4 items. Each item has 2 trailing icons for additional actions.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmio9ecsb-05-3P-multiaction.png?alt=media&token=233ffebc-aaa4-4ea0-8785-3f9254c3d90f>)

_Multi-action list items include a primary action and one or more secondary actions_

![A list with 1 item selected.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmio9x63z-06-3P-singleselect.png?alt=media&token=bf84fd5b-35c2-44a4-b3bc-a84a7345cbad>)

_A single-select list_

![A list with 2 items selected.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmio9xiam-07-3P-multiselect.png?alt=media&token=3348363c-b410-4789-bf1d-5927b98f0b1f>)

_A multi-select list_

### List interactions

#### Expand

On Android, lists can [expand and collapse](guidelines.md#90a236ee-b587-4361-8911-34006f25a6f1).

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmiqm268j-GM3%20-%20Expressive%20-%20Specs%2008%20-%203P%20-%20v01%20(1).mp4?alt=media&amp;token=3ecacbac-4e83-4a82-bfab-107d95b063b9" title="A dropdown list item expands into a list with 6 items, then collapses."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmiqm268j-GM3%20-%20Expressive%20-%20Specs%2008%20-%203P%20-%20v01%20(1).mp4?alt=media&token=3ecacbac-4e83-4a82-bfab-107d95b063b9>)

| **Category** | **Configuration** | **M3** | **M3 Expressive** |
| --- | --- | --- | --- |
| Styles | Standard | Available | Available |
| Segmented | \-- | Available |  |
| Selection modes | Single-action, multi-action, single-select, multi-select | Available | Available |
| Interactions | Expand | Available | Available |

## Tokens & specs

Use the table's menu to select a token set. The **common** set combines baseline tokens with new expressive shapes and sizes. The **expand** set has tokens for the expand interaction. [Learn about design tokens](../../foundations/design-tokens/overview.md)

### TOKEN_TABLE

Component: Lists

Configured context tags: None, Medium contrast, Default, High contrast, Static, 3P, Android, Dark, Light

#### Token sets

| Token set | Name | Type | Tokens | Description |
| --- | --- | --- | --- | --- |
| List - Common | md.comp.list | COMPONENT | 171 | Common baseline and expressive list tokens. |
| List - Expand | md.comp.list.expand | COMPONENT | 8 | For list items that can expand and collapse. |

#### List - Common (md.comp.list)

Common baseline and expressive list tokens.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.list.list-item.container.color | COLOR | Color / Enabled | `md.sys.color.surface` |  |  |
| md.comp.list.list-item.segmented.container.color | COLOR | Color / Enabled | `md.sys.color.surface` |  |  |
| md.comp.list.list-item.label-text.color | COLOR | Color / Enabled | `md.sys.color.on-surface` |  |  |
| md.comp.list.list-item.supporting-text.color | COLOR | Color / Enabled | `md.sys.color.on-surface-variant` |  |  |
| md.comp.list.list-item.overline.color | COLOR | Color / Enabled | `md.sys.color.on-surface-variant` |  |  |
| md.comp.list.divider.color | COLOR | Color / Enabled | `md.sys.color.outline` |  | Depcrecating all divider tokens nested across components. Please use the standalone divider component token md.comp.divider.color as a replacement |
| md.comp.list.divider.height | LENGTH | Color / Enabled | 1dp |  | Depcrecating all divider tokens nested across components. Please use the standalone divider component token md.comp.divider.thickness as a replacement |
| md.comp.list.list-item.container.height | LENGTH | Color / Enabled | 56dp |  | Removing single height value in favor of distinct height values for 1, 2, or 3 line list items. |
| md.comp.list.md.comp.list.list-item.one-line.container.height | LENGTH | Color / Enabled | 56dp |  | Typo in token name. Please use `md.comp.list.list-item.one-line.container.height `. |
| md.comp.list.md.comp.list.list-item.three-line.container.height | LENGTH | Color / Enabled | 88dp |  | Typo in token name. Please use `md.comp.list.list-item.three-line.container.height`. |
| md.comp.list.md.comp.list.list-item.two-line.container.height | LENGTH | Color / Enabled | 72dp |  | Typo in token name. Please use `md.comp.list.md.comp.list.list-item.two-line.container.height`. |
| md.comp.list.list-item.leading-icon.color | COLOR | Color / Enabled | `md.sys.color.on-surface-variant` |  |  |
| md.comp.list.list-item.trailing-icon.color | COLOR | Color / Enabled | `md.sys.color.on-surface-variant` |  |  |
| md.comp.list.list-item.unselected.trailing-icon.color | COLOR | Color / Enabled | `md.sys.color.on-surface` |  |  |
| md.comp.list.list-item.trailing-supporting-text.color | COLOR | Color / Enabled | `md.sys.color.on-surface-variant` |  |  |
| md.comp.list.list-item.leading-avatar.color | COLOR | Color / Enabled | `md.sys.color.primary-container` |  |  |
| md.comp.list.list-item.leading-avatar-label.color | COLOR | Color / Enabled | `md.sys.color.on-primary-container` |  |  |
| md.comp.list.list-item.container.elevation | ELEVATION | Color / Enabled | `md.sys.elevation.level0` |  |  |
| md.comp.list.list-item.selected.container.color | COLOR | Color / Enabled - Selected | `md.sys.color.secondary-container` |  |  |
| md.comp.list.list-item.selected.label-text.color | COLOR | Color / Enabled - Selected | `md.sys.color.on-secondary-container` |  |  |
| md.comp.list.list-item.selected.supporting-text.color | COLOR | Color / Enabled - Selected | `md.sys.color.on-secondary-container` |  |  |
| md.comp.list.list-item.selected.trailing-supporting-text.color | COLOR | Color / Enabled - Selected | `md.sys.color.on-secondary-container` |  |  |
| md.comp.list.list-item.selected.leading-icon.color | COLOR | Color / Enabled - Selected | `md.sys.color.on-secondary-container` |  |  |
| md.comp.list.list-item.selected.trailing-icon.color | COLOR | Color / Enabled - Selected | `md.sys.color.on-secondary-container` |  |  |
| md.comp.list.list-item.selected.overline.color | COLOR | Color / Enabled - Selected | `md.sys.color.on-secondary-container` |  |  |
| md.comp.list.list-item.disabled.state-layer.color | COLOR | Color / Disabled | `md.sys.color.on-surface` |  |  |
| md.comp.list.list-item.disabled.state-layer.opacity | OPACITY | Color / Disabled | 0.1 |  |  |
| md.comp.list.list-item.disabled.label-text.color | COLOR | Color / Disabled | `md.sys.color.on-surface` |  |  |
| md.comp.list.list-item.disabled.label-text.opacity | OPACITY | Color / Disabled | 0.38 |  |  |
| md.comp.list.list-item.disabled.supporting-text.color | COLOR | Color / Disabled | `md.sys.color.on-surface` |  |  |
| md.comp.list.list-item.disabled.supporting-text.opacity | OPACITY | Color / Disabled | 0.38 |  |  |
| md.comp.list.list-item.disabled.overline.color | COLOR | Color / Disabled | `md.sys.color.on-surface` |  |  |
| md.comp.list.list-item.disabled.overline.opacity | OPACITY | Color / Disabled | 0.38 |  |  |
| md.comp.list.list-item.disabled.leading-icon.color | COLOR | Color / Disabled | `md.sys.color.on-surface` |  |  |
| md.comp.list.list-item.disabled.leading-icon.opacity | OPACITY | Color / Disabled | 0.38 |  |  |
| md.comp.list.list-item.disabled.trailing-icon.color | COLOR | Color / Disabled | `md.sys.color.on-surface` |  |  |
| md.comp.list.list-item.disabled.trailing-icon.opacity | OPACITY | Color / Disabled | 0.38 |  |  |
| md.comp.list.list-item.selected.disabled.container.color | COLOR | Color / Disabled - Selected | `md.sys.color.on-surface` |  |  |
| md.comp.list.list-item.selected.disabled.container.opacity | OPACITY | Color / Disabled - Selected | 0.38 |  |  |
| md.comp.list.list-item.selected.disabled.label-text.color | COLOR | Color / Disabled - Selected | `md.sys.color.on-surface` |  |  |
| md.comp.list.list-item.selected.disabled.label-text.opacity | OPACITY | Color / Disabled - Selected | 0.38 |  |  |
| md.comp.list.list-item.selected.disabled.supporting-text.color | COLOR | Color / Disabled - Selected | `md.sys.color.on-surface` |  |  |
| md.comp.list.list-item.selected.disabled.supporting-text.opacity | OPACITY | Color / Disabled - Selected | 0.38 |  |  |
| md.comp.list.list-item.selected.disabled.trailing-supporting-text.color | COLOR | Color / Disabled - Selected | `md.sys.color.on-surface` |  |  |
| md.comp.list.list-item.selected.disabled.trailing-supporting-text.opacity | OPACITY | Color / Disabled - Selected | 0.38 |  |  |
| md.comp.list.list-item.selected.disabled.overline.color | COLOR | Color / Disabled - Selected | `md.sys.color.on-surface` |  |  |
| md.comp.list.list-item.selected.disabled.overline.opacity | OPACITY | Color / Disabled - Selected | 0.38 |  |  |
| md.comp.list.list-item.selected.disabled.state-layer.color | COLOR | Color / Disabled - Selected | `md.sys.color.on-surface` |  |  |
| md.comp.list.list-item.selected.disabled.state-layer.opacity | OPACITY | Color / Disabled - Selected | 0.1 |  |  |
| md.comp.list.list-item.selected.disabled.leading-icon.color | COLOR | Color / Disabled - Selected | `md.sys.color.on-surface` |  |  |
| md.comp.list.list-item.selected.disabled.leading-icon.opacity | OPACITY | Color / Disabled - Selected | 0.38 |  |  |
| md.comp.list.list-item.selected.disabled.trailing-icon.color | COLOR | Color / Disabled - Selected | `md.sys.color.on-surface` |  |  |
| md.comp.list.list-item.selected.disabled.trailing-icon.opacity | OPACITY | Color / Disabled - Selected | 0.38 |  |  |
| md.comp.list.list-item.hover.state-layer.color | COLOR | Color / Hovered | `md.sys.color.on-surface` |  |  |
| md.comp.list.list-item.hover.state-layer.opacity | OPACITY | Color / Hovered | `md.sys.state.hover.state-layer-opacity` |  |  |
| md.comp.list.list-item.hover.label-text.color | COLOR | Color / Hovered | `md.sys.color.on-surface` |  |  |
| md.comp.list.list-item.hover.leading-icon.icon.color | COLOR | Color / Hovered | `md.sys.color.on-surface-variant` |  |  |
| md.comp.list.list-item.hover.trailing-icon.icon.color | COLOR | Color / Hovered | `md.sys.color.on-surface-variant` |  |  |
| md.comp.list.list-item.selected.hover.state-layer.color | COLOR | Color / Hovered - Selected | `md.sys.color.on-surface` |  |  |
| md.comp.list.list-item.selected.hover.state-layer.opacity | OPACITY | Color / Hovered - Selected | `md.sys.state.hover.state-layer-opacity` |  |  |
| md.comp.list.list-item.selected.hover.label-text.color | COLOR | Color / Hovered - Selected | `md.sys.color.on-secondary-container` |  |  |
| md.comp.list.list-item.selected.hover.leading-icon.color | COLOR | Color / Hovered - Selected | `md.sys.color.on-surface` |  |  |
| md.comp.list.list-item.selected.hover.trailing-icon.color | COLOR | Color / Hovered - Selected | `md.sys.color.on-surface` |  |  |
| md.comp.list.list-item.focus.state-layer.color | COLOR | Color / Focused | `md.sys.color.on-surface` |  |  |
| md.comp.list.list-item.focus.state-layer.opacity | OPACITY | Color / Focused | `md.sys.state.focus.state-layer-opacity` |  |  |
| md.comp.list.list-item.focus.label-text.color | COLOR | Color / Focused | `md.sys.color.on-surface` |  |  |
| md.comp.list.list-item.focus.leading-icon.icon.color | COLOR | Color / Focused | `md.sys.color.on-surface-variant` |  |  |
| md.comp.list.list-item.focus.trailing-icon.icon.color | COLOR | Color / Focused | `md.sys.color.on-surface-variant` |  |  |
| md.comp.list.focus.indicator.color | COLOR | Color / Focused / Focus indicator | `md.sys.color.secondary` |  |  |
| md.comp.list.focus.indicator.thickness | LENGTH | Color / Focused / Focus indicator | `md.sys.state.focus-indicator.thickness` |  |  |
| md.comp.list.focus.indicator.outline.offset | LENGTH | Color / Focused / Focus indicator | `md.sys.state.focus-indicator.inner-offset` |  |  |
| md.comp.list.list-item.selected.focus.state-layer.color | COLOR | Color / Focused - Selected | `md.sys.color.on-surface` |  |  |
| md.comp.list.list-item.selected.focus.state-layer.opacity | OPACITY | Color / Focused - Selected | `md.sys.state.focus.state-layer-opacity` |  |  |
| md.comp.list.list-item.selected.focus.label-text.color | COLOR | Color / Focused - Selected | `md.sys.color.on-secondary-container` |  |  |
| md.comp.list.list-item.selected.focus.leading-icon.color | COLOR | Color / Focused - Selected | `md.sys.color.on-surface` |  |  |
| md.comp.list.list-item.selected.focus.trailing-icon.color | COLOR | Color / Focused - Selected | `md.sys.color.on-surface` |  |  |
| md.comp.list.list-item.pressed.state-layer.color | COLOR | Color / Pressed (ripple) | `md.sys.color.on-surface` |  |  |
| md.comp.list.list-item.pressed.state-layer.opacity | OPACITY | Color / Pressed (ripple) | `md.sys.state.pressed.state-layer-opacity` |  |  |
| md.comp.list.list-item.pressed.label-text.color | COLOR | Color / Pressed (ripple) | `md.sys.color.on-surface` |  |  |
| md.comp.list.list-item.pressed.leading-icon.icon.color | COLOR | Color / Pressed (ripple) | `md.sys.color.on-surface-variant` |  |  |
| md.comp.list.list-item.pressed.trailing-icon.icon.color | COLOR | Color / Pressed (ripple) | `md.sys.color.on-surface-variant` |  |  |
| md.comp.list.list-item.selected.pressed.state-layer.color | COLOR | Color / Pressed - Selected | `md.sys.color.on-surface` |  |  |
| md.comp.list.list-item.selected.pressed.state-layer.opacity | OPACITY | Color / Pressed - Selected | `md.sys.state.pressed.state-layer-opacity` |  |  |
| md.comp.list.list-item.selected.pressed.label-text.color | COLOR | Color / Pressed - Selected | `md.sys.color.on-secondary-container` |  |  |
| md.comp.list.list-item.selected.pressed.trailing-icon.color | COLOR | Color / Pressed - Selected | `md.sys.color.on-surface` |  |  |
| md.comp.list.list-item.selected.pressed.leading-icon.color | COLOR | Color / Pressed - Selected | `md.sys.color.on-surface` |  |  |
| md.comp.list.list-item.dragged.container.elevation | ELEVATION | Color / Dragged (baseline only) | `md.sys.elevation.level4` |  |  |
| md.comp.list.list-item.dragged.label-text.color | COLOR | Color / Dragged (baseline only) | `md.sys.color.on-surface` |  |  |
| md.comp.list.list-item.selected.dragged.label-text.color | COLOR | Color / Dragged (baseline only) | `md.sys.color.on-secondary-container` |  |  |
| md.comp.list.list-item.dragged.state-layer.color | COLOR | Color / Dragged (baseline only) | `md.sys.color.on-surface` |  |  |
| md.comp.list.list-item.dragged.state-layer.opacity | OPACITY | Color / Dragged (baseline only) | `md.sys.state.dragged.state-layer-opacity` |  |  |
| md.comp.list.list-item.selected.dragged.state-layer.color | COLOR | Color / Dragged (baseline only) | `md.sys.color.on-surface` |  |  |
| md.comp.list.list-item.selected.dragged.state-layer.opacity | OPACITY | Color / Dragged (baseline only) | `md.sys.state.dragged.state-layer-opacity` |  |  |
| md.comp.list.list-item.dragged.leading-icon.icon.color | COLOR | Color / Dragged (baseline only) | `md.sys.color.on-surface-variant` |  |  |
| md.comp.list.list-item.selected.dragged.leading-icon.color | COLOR | Color / Dragged (baseline only) | `md.sys.color.on-surface` |  |  |
| md.comp.list.list-item.dragged.trailing-icon.icon.color | COLOR | Color / Dragged (baseline only) | `md.sys.color.on-surface-variant` |  |  |
| md.comp.list.list-item.selected.dragged.trailing-icon.color | COLOR | Color / Dragged (baseline only) | `md.sys.color.on-surface` |  |  |
| md.comp.list.list-item.leading-space | LENGTH | Spacing | 16dp |  |  |
| md.comp.list.list-item.trailing-space | LENGTH | Spacing | 16dp |  |  |
| md.comp.list.list-item.top-space | LENGTH | Spacing | 10dp |  |  |
| md.comp.list.list-item.bottom-space | LENGTH | Spacing | 10dp |  |  |
| md.comp.list.list-item.between-space | LENGTH | Spacing | 12dp |  |  |
| md.comp.list.divider.leading-space | LENGTH | Spacing | 16dp |  |  |
| md.comp.list.divider.trailing-space | LENGTH | Spacing | 16dp |  |  |
| md.comp.list.divider.top-space | LENGTH | Spacing | 0dp |  |  |
| md.comp.list.divider.bottom-space | LENGTH | Spacing | 0dp |  |  |
| md.comp.list.segmented.gap | LENGTH | Spacing | 2dp |  |  |
| md.comp.list.container.shape | SHAPE | Shape | `md.sys.shape.corner.large` |  |  |
| md.comp.list.list-item.container.shape | SHAPE | Shape | `md.sys.shape.corner.none` |  |  |
| md.comp.list.list-item.container.expressive.shape | SHAPE | Shape | `md.sys.shape.corner.extra-small` |  |  |
| md.comp.list.list-item.disabled.container.expressive.shape | SHAPE | Shape | `md.sys.shape.corner.extra-small` |  |  |
| md.comp.list.list-item.hovered.container.expressive.shape | SHAPE | Shape | `md.sys.shape.corner.medium` |  |  |
| md.comp.list.list-item.focused.container.expressive.shape | SHAPE | Shape | `md.sys.shape.corner.large` |  |  |
| md.comp.list.list-item.pressed.container.expressive.shape | SHAPE | Shape | `md.sys.shape.corner.large` |  |  |
| md.comp.list.list-item.dragged.container.expressive.shape | SHAPE | Shape | `md.sys.shape.corner.large` |  |  |
| md.comp.list.list-item.selected.container.expressive.shape | SHAPE | Shape | `md.sys.shape.corner.large` |  |  |
| md.comp.list.list-item.selected.disabled.container.expressive.shape | SHAPE | Shape | `md.sys.shape.corner.large` |  |  |
| md.comp.list.list-item.selected.hovered.container.expressive.shape | SHAPE | Shape | `md.sys.shape.corner.large` |  |  |
| md.comp.list.list-item.selected.focused.container.expressive.shape | SHAPE | Shape | `md.sys.shape.corner.large` |  |  |
| md.comp.list.list-item.selected.pressed.container.expressive.shape | SHAPE | Shape | `md.sys.shape.corner.large` |  |  |
| md.comp.list.list-item.selected.dragged.container.expressive.shape | SHAPE | Shape | `md.sys.shape.corner.large` |  |  |
| md.comp.list.list-item.leading-avatar.shape | SHAPE | Shape | `md.sys.shape.corner.full` |  |  |
| md.comp.list.list-item.leading-video.shape | SHAPE | Shape | `md.sys.shape.corner.small` |  |  |
| md.comp.list.list-item.leading-image.shape | SHAPE | Shape | `md.sys.shape.corner.none` |  |  |
| md.comp.list.list-item.leading-image.expressive.shape | SHAPE | Shape | `md.sys.shape.corner.small` |  |  |
| md.comp.list.list-item.selected.container.shape | SHAPE | Shape | `md.sys.shape.corner.large` |  |  |
| md.comp.list.list-item.leading-avatar-label.type | TYPOGRAPHY | Size and typography | font name: `md.comp.list.list-item.leading-avatar-label.font`, font weight: `md.comp.list.list-item.leading-avatar-label.weight`, font size: `md.comp.list.list-item.leading-avatar-label.size`, font tracking: `md.comp.list.list-item.leading-avatar-label.tracking`, line height: `md.comp.list.list-item.leading-avatar-label.line-height` |  |  |
| md.comp.list.list-item.leading-avatar.size | LENGTH | Size and typography | 40dp |  |  |
| md.comp.list.list-item.leading-avatar-label.font | FONT_NAMES | Size and typography | `md.sys.typescale.title-medium.font` |  |  |
| md.comp.list.list-item.leading-avatar-label.line-height | LINE_HEIGHT | Size and typography | `md.sys.typescale.title-medium.line-height` |  |  |
| md.comp.list.list-item.leading-avatar-label.size | FONT_SIZE | Size and typography | `md.sys.typescale.title-medium.size` |  |  |
| md.comp.list.list-item.leading-avatar-label.tracking | FONT_TRACKING | Size and typography | `md.sys.typescale.title-medium.tracking` |  |  |
| md.comp.list.list-item.leading-avatar-label.weight | FONT_WEIGHT | Size and typography | `md.sys.typescale.title-medium.weight` |  |  |
| md.comp.list.list-item.leading-icon.size | LENGTH | Size and typography | 24dp |  |  |
| md.comp.list.list-item.leading-icon.expressive.size | LENGTH | Size and typography | 20dp |  |  |
| md.comp.list.list-item.leading-image.width | LENGTH | Size and typography | 56dp |  |  |
| md.comp.list.list-item.leading-image.height | LENGTH | Size and typography | 56dp |  |  |
| md.comp.list.list-item.leading-video.width | LENGTH | Size and typography | 100dp |  |  |
| md.comp.list.list-item.small.leading-video.width | LENGTH | Size and typography | 100dp |  |  |
| md.comp.list.list-item.small.leading-video.height | LENGTH | Size and typography | 56dp |  |  |
| md.comp.list.list-item.large.leading-video.width | LENGTH | Size and typography | 114dp |  |  |
| md.comp.list.list-item.large.leading-video.height | LENGTH | Size and typography | 64dp |  |  |
| md.comp.list.list-item.one-line.container.height | LENGTH | Size and typography | 56dp |  |  |
| md.comp.list.list-item.two-line.container.height | LENGTH | Size and typography | 72dp |  |  |
| md.comp.list.list-item.three-line.container.height | LENGTH | Size and typography | 88dp |  |  |
| md.comp.list.list-item.trailing-icon.size | LENGTH | Size and typography | 24dp |  |  |
| md.comp.list.list-item.trailing-icon.expressive.size | LENGTH | Size and typography | 20dp |  |  |
| md.comp.list.list-item.label-text.type | TYPOGRAPHY | Size and typography | font name: `md.comp.list.list-item.label-text.font`, font weight: `md.comp.list.list-item.label-text.weight`, font size: `md.comp.list.list-item.label-text.size`, font tracking: `md.comp.list.list-item.label-text.tracking`, line height: `md.comp.list.list-item.label-text.line-height` |  |  |
| md.comp.list.list-item.label-text.font | FONT_NAMES | Size and typography | `md.sys.typescale.body-large.font` |  |  |
| md.comp.list.list-item.label-text.line-height | LINE_HEIGHT | Size and typography | `md.sys.typescale.body-large.line-height` |  |  |
| md.comp.list.list-item.label-text.size | FONT_SIZE | Size and typography | `md.sys.typescale.body-large.size` |  |  |
| md.comp.list.list-item.label-text.tracking | FONT_TRACKING | Size and typography | `md.sys.typescale.body-large.tracking` |  |  |
| md.comp.list.list-item.label-text.weight | FONT_WEIGHT | Size and typography | `md.sys.typescale.body-large.weight` |  |  |
| md.comp.list.list-item.trailing-supporting-text.type | TYPOGRAPHY | Size and typography | font name: `md.comp.list.list-item.trailing-supporting-text.font`, font weight: `md.comp.list.list-item.trailing-supporting-text.weight`, font size: `md.comp.list.list-item.trailing-supporting-text.size`, font tracking: `md.comp.list.list-item.trailing-supporting-text.tracking`, line height: `md.comp.list.list-item.trailing-supporting-text.line-height` |  |  |
| md.comp.list.list-item.trailing-supporting-text.font | FONT_NAMES | Size and typography | `md.sys.typescale.label-small.font` |  |  |
| md.comp.list.list-item.trailing-supporting-text.line-height | LINE_HEIGHT | Size and typography | `md.sys.typescale.label-small.line-height` |  |  |
| md.comp.list.list-item.trailing-supporting-text.size | FONT_SIZE | Size and typography | `md.sys.typescale.label-small.size` |  |  |
| md.comp.list.list-item.trailing-supporting-text.tracking | FONT_TRACKING | Size and typography | `md.sys.typescale.label-small.tracking` |  |  |
| md.comp.list.list-item.trailing-supporting-text.weight | FONT_WEIGHT | Size and typography | `md.sys.typescale.label-small.weight` |  |  |
| md.comp.list.list-item.supporting-text.type | TYPOGRAPHY | Size and typography | font name: `md.comp.list.list-item.supporting-text.font`, font weight: `md.comp.list.list-item.supporting-text.weight`, font size: `md.comp.list.list-item.supporting-text.size`, font tracking: `md.comp.list.list-item.supporting-text.tracking`, line height: `md.comp.list.list-item.supporting-text.line-height` |  |  |
| md.comp.list.list-item.supporting-text.font | FONT_NAMES | Size and typography | `md.sys.typescale.body-medium.font` |  |  |
| md.comp.list.list-item.supporting-text.line-height | LINE_HEIGHT | Size and typography | `md.sys.typescale.body-medium.line-height` |  |  |
| md.comp.list.list-item.supporting-text.size | FONT_SIZE | Size and typography | `md.sys.typescale.body-medium.size` |  |  |
| md.comp.list.list-item.supporting-text.tracking | FONT_TRACKING | Size and typography | `md.sys.typescale.body-medium.tracking` |  |  |
| md.comp.list.list-item.supporting-text.weight | FONT_WEIGHT | Size and typography | `md.sys.typescale.body-medium.weight` |  |  |
| md.comp.list.list-item.overline.type | TYPOGRAPHY | Size and typography | font name: `md.comp.list.list-item.overline.font`, font weight: `md.comp.list.list-item.overline.weight`, font size: `md.comp.list.list-item.overline.size`, font tracking: `md.comp.list.list-item.overline.tracking`, line height: `md.comp.list.list-item.overline.line-height` |  |  |
| md.comp.list.list-item.overline.font | FONT_NAMES | Size and typography | `md.sys.typescale.label-small.font` |  |  |
| md.comp.list.list-item.overline.line-height | LINE_HEIGHT | Size and typography | `md.sys.typescale.label-small.line-height` |  |  |
| md.comp.list.list-item.overline.size | FONT_SIZE | Size and typography | `md.sys.typescale.label-small.size` |  |  |
| md.comp.list.list-item.overline.tracking | FONT_TRACKING | Size and typography | `md.sys.typescale.label-small.tracking` |  |  |
| md.comp.list.list-item.overline.weight | FONT_WEIGHT | Size and typography | `md.sys.typescale.label-small.weight` |  |  |

#### List - Expand (md.comp.list.expand)

For list items that can expand and collapse.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.list.expand.expanded.list-item.container.color | COLOR | Color | `md.sys.color.surface` |  |  |
| md.comp.list.expand.expanded.list-item.segmented.container.color | COLOR | Color | `md.sys.color.surface` |  |  |
| md.comp.list.expand.collapsed.list-item.trailing-icon.container.color | COLOR | Color | `md.sys.color.surface` |  |  |
| md.comp.list.expand.collapsed.list-item.trailing-icon.icon.color | COLOR | Color | `md.sys.color.on-surface` |  |  |
| md.comp.list.expand.expanded.list-item.trailing-icon.container.color | COLOR | Color | `md.sys.color.surface-container` |  |  |
| md.comp.list.expand.expanded.list-item.trailing-icon.icon.color | COLOR | Color | `md.sys.color.on-surface` |  |  |
| md.comp.list.expand.container.shape | SHAPE | Shape | `md.sys.shape.corner.large` |  |  |
| md.comp.list.expand.trailing-icon.shape | SHAPE | Shape | `md.sys.shape.corner.full` |  |  |

## Anatomy

![Diagram with 10 elements that can be included in lists.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmioasn71-11-3P-VQA.png?alt=media&token=7838dcd3-7b19-4506-b422-3c251e4f1238>)

_Container and label text are required. All other elements are optional: Container; Overline; Label text; Trailing text; Supporting text; Trailing icon; Divider; Leading avatar; Leading icon; Leading media - image or video_

### Flexibility & slots

The [M3 Design Kit](https://www.figma.com/community/file/1035203688168086460) includes lists with custom slots for designing flexible item layouts. Think of a custom list as a container with three different slots: leading, content, and trailing. Each slot can hold a different element.

#### **Slot accessibility**

Slots are not accessible by default. Consider the following:

-   Elements must follow the rules, structure, and interaction patterns for lists

-   Use standard list item padding

-   Target size must be at least 48x48dp

-   Don't add interactive elements that make the list item difficult to navigate, especially for people using screen readers


[More on required accessibility guidelines](accessibility.md#538f23f7-689c-4516-bfc8-5f6933a43f5e)

![A diagram with leading, content, and trailing slots.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmioecnyy-12-3P.png?alt=media&token=33b605a8-0730-4453-81ca-71c3f2db29f1>)

_Reserve the use of slots for use cases that maintain the list’s accessibility and functionality Leading slot; Content slot; Trailing slot_

warning

Caution:

Slots require custom code implementation that you must create and maintain

The **leading** and **trailing** slot positions must be a smaller width than the **content** section.

1\. **Leading slots** can contain:

-   Visual elements: Avatar, icon, image, or video thumbnail

-   Selection controls: Checkbox, radio button, or switch

-   Customizations: Badge or larger image


2\. **Content slots** must be the largest-width slot and can contain:

-   Default content: Label text, supporting text

-   Optional add-ons: Badge, icon, in-line label, or more text elements

-   Avoid long lines of text to preserve readability


3. **Trailing slots** can contain:

-   Action elements or text: Icon, icon button, or trailing text

-   Selection controls: Checkbox, radio button, or switch

![Slot diagram showing slot placement in the middle of the list.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmioefhsu-13-3P-VQA.png?alt=media&token=5f703576-a349-48ad-a14c-55b7f9db9742>)

_The content slot must be the largest section, placed in the middle of the list item_

#### Selection lists

For selection lists, use only one selection interaction per list item.

![A selected list item with a checkmark in the leading slot.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmioehno9-14-do-3P.png?alt=media&token=19978fb7-5f58-4f78-a098-9f6c73e8f1a2>)

_Use only one selection interaction per list item_

![A selected list item with both a checkmark in the leading slot and a bookmark in the trailing slot.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmioek6y5-15-dont-3P.png?alt=media&token=cd9cd890-328c-4c60-a4da-a1e4f684643a>)

_Don't use multiple selection interactions in one item_

## Color

Color values are implemented through design tokens. For designers, this means working with color values that correspond with tokens. In implementation, a color value will be a token that references a value. [Learn more about design tokens](../../foundations/design-tokens/overview.md)

![10 list element color roles in light mode and dark mode.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmion9qya-16-3P.png?alt=media&token=81e39e2f-a93f-484c-b6b0-1356790b08d1>)

_List color roles used for light and dark themes: Surface; On surface variant; On surface; On surface variant; On surface variant; On surface variant; Outline variant; Primary container; On primary container; On surface variant_

## States

States are visual representations used to communicate the status of a component or an interactive element. [Learn more about interaction states](https://m3.material.io/m3/pages/interaction-states/overview)

### Default list items

![6 default list states in light and dark mode.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmionai1i-17-3P.png?alt=media&token=e0b89f3c-2d85-4d4e-ba61-9e7bb8a80829>)

_Enabled; Disabled; Hovered; Focused; Pressed; Dragged_

### Selected list items

![6 selected list states in light and dark mode.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmioncezo-18-3P.png?alt=media&token=5d3479f7-4cc1-4324-b589-b8912b226880>)

_Enabled; Disabled; Hovered; Focused; Pressed; Dragged_

## Measurements

![Size and padding measurements for list items.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmiondj5y-19-3P.png?alt=media&token=0f6e18cf-137f-444f-ab70-c7bfa50ef6ec>)

_List item alignment, padding, and size measurements. The icon button height is dynamic, and automatically adjusts to fill the list item height._

## List (baseline)

The baseline list variant is available and continues to work in existing products. However, the [expressive list](specs.md#ebf87f58-d5bf-4cb5-a856-d2bb104eec4d) variant is recommended for new designs.

### Tokens & specs

Baseline list tokens are in the **common** token set. Note: This set also includes several expressive tokens.

### TOKEN_TABLE

Component: Lists

Configured context tags: None, Medium contrast, Default, High contrast, Static, 3P, Android, Dark, Light

#### Token sets

| Token set | Name | Type | Tokens | Description |
| --- | --- | --- | --- | --- |
| List - Common | md.comp.list | COMPONENT | 171 | Common baseline and expressive list tokens. |

#### List - Common (md.comp.list)

Common baseline and expressive list tokens.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.list.list-item.container.color | COLOR | Color / Enabled | `md.sys.color.surface` |  |  |
| md.comp.list.list-item.segmented.container.color | COLOR | Color / Enabled | `md.sys.color.surface` |  |  |
| md.comp.list.list-item.label-text.color | COLOR | Color / Enabled | `md.sys.color.on-surface` |  |  |
| md.comp.list.list-item.supporting-text.color | COLOR | Color / Enabled | `md.sys.color.on-surface-variant` |  |  |
| md.comp.list.list-item.overline.color | COLOR | Color / Enabled | `md.sys.color.on-surface-variant` |  |  |
| md.comp.list.divider.color | COLOR | Color / Enabled | `md.sys.color.outline` |  | Depcrecating all divider tokens nested across components. Please use the standalone divider component token md.comp.divider.color as a replacement |
| md.comp.list.divider.height | LENGTH | Color / Enabled | 1dp |  | Depcrecating all divider tokens nested across components. Please use the standalone divider component token md.comp.divider.thickness as a replacement |
| md.comp.list.list-item.container.height | LENGTH | Color / Enabled | 56dp |  | Removing single height value in favor of distinct height values for 1, 2, or 3 line list items. |
| md.comp.list.md.comp.list.list-item.one-line.container.height | LENGTH | Color / Enabled | 56dp |  | Typo in token name. Please use `md.comp.list.list-item.one-line.container.height `. |
| md.comp.list.md.comp.list.list-item.three-line.container.height | LENGTH | Color / Enabled | 88dp |  | Typo in token name. Please use `md.comp.list.list-item.three-line.container.height`. |
| md.comp.list.md.comp.list.list-item.two-line.container.height | LENGTH | Color / Enabled | 72dp |  | Typo in token name. Please use `md.comp.list.md.comp.list.list-item.two-line.container.height`. |
| md.comp.list.list-item.leading-icon.color | COLOR | Color / Enabled | `md.sys.color.on-surface-variant` |  |  |
| md.comp.list.list-item.trailing-icon.color | COLOR | Color / Enabled | `md.sys.color.on-surface-variant` |  |  |
| md.comp.list.list-item.unselected.trailing-icon.color | COLOR | Color / Enabled | `md.sys.color.on-surface` |  |  |
| md.comp.list.list-item.trailing-supporting-text.color | COLOR | Color / Enabled | `md.sys.color.on-surface-variant` |  |  |
| md.comp.list.list-item.leading-avatar.color | COLOR | Color / Enabled | `md.sys.color.primary-container` |  |  |
| md.comp.list.list-item.leading-avatar-label.color | COLOR | Color / Enabled | `md.sys.color.on-primary-container` |  |  |
| md.comp.list.list-item.container.elevation | ELEVATION | Color / Enabled | `md.sys.elevation.level0` |  |  |
| md.comp.list.list-item.selected.container.color | COLOR | Color / Enabled - Selected | `md.sys.color.secondary-container` |  |  |
| md.comp.list.list-item.selected.label-text.color | COLOR | Color / Enabled - Selected | `md.sys.color.on-secondary-container` |  |  |
| md.comp.list.list-item.selected.supporting-text.color | COLOR | Color / Enabled - Selected | `md.sys.color.on-secondary-container` |  |  |
| md.comp.list.list-item.selected.trailing-supporting-text.color | COLOR | Color / Enabled - Selected | `md.sys.color.on-secondary-container` |  |  |
| md.comp.list.list-item.selected.leading-icon.color | COLOR | Color / Enabled - Selected | `md.sys.color.on-secondary-container` |  |  |
| md.comp.list.list-item.selected.trailing-icon.color | COLOR | Color / Enabled - Selected | `md.sys.color.on-secondary-container` |  |  |
| md.comp.list.list-item.selected.overline.color | COLOR | Color / Enabled - Selected | `md.sys.color.on-secondary-container` |  |  |
| md.comp.list.list-item.disabled.state-layer.color | COLOR | Color / Disabled | `md.sys.color.on-surface` |  |  |
| md.comp.list.list-item.disabled.state-layer.opacity | OPACITY | Color / Disabled | 0.1 |  |  |
| md.comp.list.list-item.disabled.label-text.color | COLOR | Color / Disabled | `md.sys.color.on-surface` |  |  |
| md.comp.list.list-item.disabled.label-text.opacity | OPACITY | Color / Disabled | 0.38 |  |  |
| md.comp.list.list-item.disabled.supporting-text.color | COLOR | Color / Disabled | `md.sys.color.on-surface` |  |  |
| md.comp.list.list-item.disabled.supporting-text.opacity | OPACITY | Color / Disabled | 0.38 |  |  |
| md.comp.list.list-item.disabled.overline.color | COLOR | Color / Disabled | `md.sys.color.on-surface` |  |  |
| md.comp.list.list-item.disabled.overline.opacity | OPACITY | Color / Disabled | 0.38 |  |  |
| md.comp.list.list-item.disabled.leading-icon.color | COLOR | Color / Disabled | `md.sys.color.on-surface` |  |  |
| md.comp.list.list-item.disabled.leading-icon.opacity | OPACITY | Color / Disabled | 0.38 |  |  |
| md.comp.list.list-item.disabled.trailing-icon.color | COLOR | Color / Disabled | `md.sys.color.on-surface` |  |  |
| md.comp.list.list-item.disabled.trailing-icon.opacity | OPACITY | Color / Disabled | 0.38 |  |  |
| md.comp.list.list-item.selected.disabled.container.color | COLOR | Color / Disabled - Selected | `md.sys.color.on-surface` |  |  |
| md.comp.list.list-item.selected.disabled.container.opacity | OPACITY | Color / Disabled - Selected | 0.38 |  |  |
| md.comp.list.list-item.selected.disabled.label-text.color | COLOR | Color / Disabled - Selected | `md.sys.color.on-surface` |  |  |
| md.comp.list.list-item.selected.disabled.label-text.opacity | OPACITY | Color / Disabled - Selected | 0.38 |  |  |
| md.comp.list.list-item.selected.disabled.supporting-text.color | COLOR | Color / Disabled - Selected | `md.sys.color.on-surface` |  |  |
| md.comp.list.list-item.selected.disabled.supporting-text.opacity | OPACITY | Color / Disabled - Selected | 0.38 |  |  |
| md.comp.list.list-item.selected.disabled.trailing-supporting-text.color | COLOR | Color / Disabled - Selected | `md.sys.color.on-surface` |  |  |
| md.comp.list.list-item.selected.disabled.trailing-supporting-text.opacity | OPACITY | Color / Disabled - Selected | 0.38 |  |  |
| md.comp.list.list-item.selected.disabled.overline.color | COLOR | Color / Disabled - Selected | `md.sys.color.on-surface` |  |  |
| md.comp.list.list-item.selected.disabled.overline.opacity | OPACITY | Color / Disabled - Selected | 0.38 |  |  |
| md.comp.list.list-item.selected.disabled.state-layer.color | COLOR | Color / Disabled - Selected | `md.sys.color.on-surface` |  |  |
| md.comp.list.list-item.selected.disabled.state-layer.opacity | OPACITY | Color / Disabled - Selected | 0.1 |  |  |
| md.comp.list.list-item.selected.disabled.leading-icon.color | COLOR | Color / Disabled - Selected | `md.sys.color.on-surface` |  |  |
| md.comp.list.list-item.selected.disabled.leading-icon.opacity | OPACITY | Color / Disabled - Selected | 0.38 |  |  |
| md.comp.list.list-item.selected.disabled.trailing-icon.color | COLOR | Color / Disabled - Selected | `md.sys.color.on-surface` |  |  |
| md.comp.list.list-item.selected.disabled.trailing-icon.opacity | OPACITY | Color / Disabled - Selected | 0.38 |  |  |
| md.comp.list.list-item.hover.state-layer.color | COLOR | Color / Hovered | `md.sys.color.on-surface` |  |  |
| md.comp.list.list-item.hover.state-layer.opacity | OPACITY | Color / Hovered | `md.sys.state.hover.state-layer-opacity` |  |  |
| md.comp.list.list-item.hover.label-text.color | COLOR | Color / Hovered | `md.sys.color.on-surface` |  |  |
| md.comp.list.list-item.hover.leading-icon.icon.color | COLOR | Color / Hovered | `md.sys.color.on-surface-variant` |  |  |
| md.comp.list.list-item.hover.trailing-icon.icon.color | COLOR | Color / Hovered | `md.sys.color.on-surface-variant` |  |  |
| md.comp.list.list-item.selected.hover.state-layer.color | COLOR | Color / Hovered - Selected | `md.sys.color.on-surface` |  |  |
| md.comp.list.list-item.selected.hover.state-layer.opacity | OPACITY | Color / Hovered - Selected | `md.sys.state.hover.state-layer-opacity` |  |  |
| md.comp.list.list-item.selected.hover.label-text.color | COLOR | Color / Hovered - Selected | `md.sys.color.on-secondary-container` |  |  |
| md.comp.list.list-item.selected.hover.leading-icon.color | COLOR | Color / Hovered - Selected | `md.sys.color.on-surface` |  |  |
| md.comp.list.list-item.selected.hover.trailing-icon.color | COLOR | Color / Hovered - Selected | `md.sys.color.on-surface` |  |  |
| md.comp.list.list-item.focus.state-layer.color | COLOR | Color / Focused | `md.sys.color.on-surface` |  |  |
| md.comp.list.list-item.focus.state-layer.opacity | OPACITY | Color / Focused | `md.sys.state.focus.state-layer-opacity` |  |  |
| md.comp.list.list-item.focus.label-text.color | COLOR | Color / Focused | `md.sys.color.on-surface` |  |  |
| md.comp.list.list-item.focus.leading-icon.icon.color | COLOR | Color / Focused | `md.sys.color.on-surface-variant` |  |  |
| md.comp.list.list-item.focus.trailing-icon.icon.color | COLOR | Color / Focused | `md.sys.color.on-surface-variant` |  |  |
| md.comp.list.focus.indicator.color | COLOR | Color / Focused / Focus indicator | `md.sys.color.secondary` |  |  |
| md.comp.list.focus.indicator.thickness | LENGTH | Color / Focused / Focus indicator | `md.sys.state.focus-indicator.thickness` |  |  |
| md.comp.list.focus.indicator.outline.offset | LENGTH | Color / Focused / Focus indicator | `md.sys.state.focus-indicator.inner-offset` |  |  |
| md.comp.list.list-item.selected.focus.state-layer.color | COLOR | Color / Focused - Selected | `md.sys.color.on-surface` |  |  |
| md.comp.list.list-item.selected.focus.state-layer.opacity | OPACITY | Color / Focused - Selected | `md.sys.state.focus.state-layer-opacity` |  |  |
| md.comp.list.list-item.selected.focus.label-text.color | COLOR | Color / Focused - Selected | `md.sys.color.on-secondary-container` |  |  |
| md.comp.list.list-item.selected.focus.leading-icon.color | COLOR | Color / Focused - Selected | `md.sys.color.on-surface` |  |  |
| md.comp.list.list-item.selected.focus.trailing-icon.color | COLOR | Color / Focused - Selected | `md.sys.color.on-surface` |  |  |
| md.comp.list.list-item.pressed.state-layer.color | COLOR | Color / Pressed (ripple) | `md.sys.color.on-surface` |  |  |
| md.comp.list.list-item.pressed.state-layer.opacity | OPACITY | Color / Pressed (ripple) | `md.sys.state.pressed.state-layer-opacity` |  |  |
| md.comp.list.list-item.pressed.label-text.color | COLOR | Color / Pressed (ripple) | `md.sys.color.on-surface` |  |  |
| md.comp.list.list-item.pressed.leading-icon.icon.color | COLOR | Color / Pressed (ripple) | `md.sys.color.on-surface-variant` |  |  |
| md.comp.list.list-item.pressed.trailing-icon.icon.color | COLOR | Color / Pressed (ripple) | `md.sys.color.on-surface-variant` |  |  |
| md.comp.list.list-item.selected.pressed.state-layer.color | COLOR | Color / Pressed - Selected | `md.sys.color.on-surface` |  |  |
| md.comp.list.list-item.selected.pressed.state-layer.opacity | OPACITY | Color / Pressed - Selected | `md.sys.state.pressed.state-layer-opacity` |  |  |
| md.comp.list.list-item.selected.pressed.label-text.color | COLOR | Color / Pressed - Selected | `md.sys.color.on-secondary-container` |  |  |
| md.comp.list.list-item.selected.pressed.trailing-icon.color | COLOR | Color / Pressed - Selected | `md.sys.color.on-surface` |  |  |
| md.comp.list.list-item.selected.pressed.leading-icon.color | COLOR | Color / Pressed - Selected | `md.sys.color.on-surface` |  |  |
| md.comp.list.list-item.dragged.container.elevation | ELEVATION | Color / Dragged (baseline only) | `md.sys.elevation.level4` |  |  |
| md.comp.list.list-item.dragged.label-text.color | COLOR | Color / Dragged (baseline only) | `md.sys.color.on-surface` |  |  |
| md.comp.list.list-item.selected.dragged.label-text.color | COLOR | Color / Dragged (baseline only) | `md.sys.color.on-secondary-container` |  |  |
| md.comp.list.list-item.dragged.state-layer.color | COLOR | Color / Dragged (baseline only) | `md.sys.color.on-surface` |  |  |
| md.comp.list.list-item.dragged.state-layer.opacity | OPACITY | Color / Dragged (baseline only) | `md.sys.state.dragged.state-layer-opacity` |  |  |
| md.comp.list.list-item.selected.dragged.state-layer.color | COLOR | Color / Dragged (baseline only) | `md.sys.color.on-surface` |  |  |
| md.comp.list.list-item.selected.dragged.state-layer.opacity | OPACITY | Color / Dragged (baseline only) | `md.sys.state.dragged.state-layer-opacity` |  |  |
| md.comp.list.list-item.dragged.leading-icon.icon.color | COLOR | Color / Dragged (baseline only) | `md.sys.color.on-surface-variant` |  |  |
| md.comp.list.list-item.selected.dragged.leading-icon.color | COLOR | Color / Dragged (baseline only) | `md.sys.color.on-surface` |  |  |
| md.comp.list.list-item.dragged.trailing-icon.icon.color | COLOR | Color / Dragged (baseline only) | `md.sys.color.on-surface-variant` |  |  |
| md.comp.list.list-item.selected.dragged.trailing-icon.color | COLOR | Color / Dragged (baseline only) | `md.sys.color.on-surface` |  |  |
| md.comp.list.list-item.leading-space | LENGTH | Spacing | 16dp |  |  |
| md.comp.list.list-item.trailing-space | LENGTH | Spacing | 16dp |  |  |
| md.comp.list.list-item.top-space | LENGTH | Spacing | 10dp |  |  |
| md.comp.list.list-item.bottom-space | LENGTH | Spacing | 10dp |  |  |
| md.comp.list.list-item.between-space | LENGTH | Spacing | 12dp |  |  |
| md.comp.list.divider.leading-space | LENGTH | Spacing | 16dp |  |  |
| md.comp.list.divider.trailing-space | LENGTH | Spacing | 16dp |  |  |
| md.comp.list.divider.top-space | LENGTH | Spacing | 0dp |  |  |
| md.comp.list.divider.bottom-space | LENGTH | Spacing | 0dp |  |  |
| md.comp.list.segmented.gap | LENGTH | Spacing | 2dp |  |  |
| md.comp.list.container.shape | SHAPE | Shape | `md.sys.shape.corner.large` |  |  |
| md.comp.list.list-item.container.shape | SHAPE | Shape | `md.sys.shape.corner.none` |  |  |
| md.comp.list.list-item.container.expressive.shape | SHAPE | Shape | `md.sys.shape.corner.extra-small` |  |  |
| md.comp.list.list-item.disabled.container.expressive.shape | SHAPE | Shape | `md.sys.shape.corner.extra-small` |  |  |
| md.comp.list.list-item.hovered.container.expressive.shape | SHAPE | Shape | `md.sys.shape.corner.medium` |  |  |
| md.comp.list.list-item.focused.container.expressive.shape | SHAPE | Shape | `md.sys.shape.corner.large` |  |  |
| md.comp.list.list-item.pressed.container.expressive.shape | SHAPE | Shape | `md.sys.shape.corner.large` |  |  |
| md.comp.list.list-item.dragged.container.expressive.shape | SHAPE | Shape | `md.sys.shape.corner.large` |  |  |
| md.comp.list.list-item.selected.container.expressive.shape | SHAPE | Shape | `md.sys.shape.corner.large` |  |  |
| md.comp.list.list-item.selected.disabled.container.expressive.shape | SHAPE | Shape | `md.sys.shape.corner.large` |  |  |
| md.comp.list.list-item.selected.hovered.container.expressive.shape | SHAPE | Shape | `md.sys.shape.corner.large` |  |  |
| md.comp.list.list-item.selected.focused.container.expressive.shape | SHAPE | Shape | `md.sys.shape.corner.large` |  |  |
| md.comp.list.list-item.selected.pressed.container.expressive.shape | SHAPE | Shape | `md.sys.shape.corner.large` |  |  |
| md.comp.list.list-item.selected.dragged.container.expressive.shape | SHAPE | Shape | `md.sys.shape.corner.large` |  |  |
| md.comp.list.list-item.leading-avatar.shape | SHAPE | Shape | `md.sys.shape.corner.full` |  |  |
| md.comp.list.list-item.leading-video.shape | SHAPE | Shape | `md.sys.shape.corner.small` |  |  |
| md.comp.list.list-item.leading-image.shape | SHAPE | Shape | `md.sys.shape.corner.none` |  |  |
| md.comp.list.list-item.leading-image.expressive.shape | SHAPE | Shape | `md.sys.shape.corner.small` |  |  |
| md.comp.list.list-item.selected.container.shape | SHAPE | Shape | `md.sys.shape.corner.large` |  |  |
| md.comp.list.list-item.leading-avatar-label.type | TYPOGRAPHY | Size and typography | font name: `md.comp.list.list-item.leading-avatar-label.font`, font weight: `md.comp.list.list-item.leading-avatar-label.weight`, font size: `md.comp.list.list-item.leading-avatar-label.size`, font tracking: `md.comp.list.list-item.leading-avatar-label.tracking`, line height: `md.comp.list.list-item.leading-avatar-label.line-height` |  |  |
| md.comp.list.list-item.leading-avatar.size | LENGTH | Size and typography | 40dp |  |  |
| md.comp.list.list-item.leading-avatar-label.font | FONT_NAMES | Size and typography | `md.sys.typescale.title-medium.font` |  |  |
| md.comp.list.list-item.leading-avatar-label.line-height | LINE_HEIGHT | Size and typography | `md.sys.typescale.title-medium.line-height` |  |  |
| md.comp.list.list-item.leading-avatar-label.size | FONT_SIZE | Size and typography | `md.sys.typescale.title-medium.size` |  |  |
| md.comp.list.list-item.leading-avatar-label.tracking | FONT_TRACKING | Size and typography | `md.sys.typescale.title-medium.tracking` |  |  |
| md.comp.list.list-item.leading-avatar-label.weight | FONT_WEIGHT | Size and typography | `md.sys.typescale.title-medium.weight` |  |  |
| md.comp.list.list-item.leading-icon.size | LENGTH | Size and typography | 24dp |  |  |
| md.comp.list.list-item.leading-icon.expressive.size | LENGTH | Size and typography | 20dp |  |  |
| md.comp.list.list-item.leading-image.width | LENGTH | Size and typography | 56dp |  |  |
| md.comp.list.list-item.leading-image.height | LENGTH | Size and typography | 56dp |  |  |
| md.comp.list.list-item.leading-video.width | LENGTH | Size and typography | 100dp |  |  |
| md.comp.list.list-item.small.leading-video.width | LENGTH | Size and typography | 100dp |  |  |
| md.comp.list.list-item.small.leading-video.height | LENGTH | Size and typography | 56dp |  |  |
| md.comp.list.list-item.large.leading-video.width | LENGTH | Size and typography | 114dp |  |  |
| md.comp.list.list-item.large.leading-video.height | LENGTH | Size and typography | 64dp |  |  |
| md.comp.list.list-item.one-line.container.height | LENGTH | Size and typography | 56dp |  |  |
| md.comp.list.list-item.two-line.container.height | LENGTH | Size and typography | 72dp |  |  |
| md.comp.list.list-item.three-line.container.height | LENGTH | Size and typography | 88dp |  |  |
| md.comp.list.list-item.trailing-icon.size | LENGTH | Size and typography | 24dp |  |  |
| md.comp.list.list-item.trailing-icon.expressive.size | LENGTH | Size and typography | 20dp |  |  |
| md.comp.list.list-item.label-text.type | TYPOGRAPHY | Size and typography | font name: `md.comp.list.list-item.label-text.font`, font weight: `md.comp.list.list-item.label-text.weight`, font size: `md.comp.list.list-item.label-text.size`, font tracking: `md.comp.list.list-item.label-text.tracking`, line height: `md.comp.list.list-item.label-text.line-height` |  |  |
| md.comp.list.list-item.label-text.font | FONT_NAMES | Size and typography | `md.sys.typescale.body-large.font` |  |  |
| md.comp.list.list-item.label-text.line-height | LINE_HEIGHT | Size and typography | `md.sys.typescale.body-large.line-height` |  |  |
| md.comp.list.list-item.label-text.size | FONT_SIZE | Size and typography | `md.sys.typescale.body-large.size` |  |  |
| md.comp.list.list-item.label-text.tracking | FONT_TRACKING | Size and typography | `md.sys.typescale.body-large.tracking` |  |  |
| md.comp.list.list-item.label-text.weight | FONT_WEIGHT | Size and typography | `md.sys.typescale.body-large.weight` |  |  |
| md.comp.list.list-item.trailing-supporting-text.type | TYPOGRAPHY | Size and typography | font name: `md.comp.list.list-item.trailing-supporting-text.font`, font weight: `md.comp.list.list-item.trailing-supporting-text.weight`, font size: `md.comp.list.list-item.trailing-supporting-text.size`, font tracking: `md.comp.list.list-item.trailing-supporting-text.tracking`, line height: `md.comp.list.list-item.trailing-supporting-text.line-height` |  |  |
| md.comp.list.list-item.trailing-supporting-text.font | FONT_NAMES | Size and typography | `md.sys.typescale.label-small.font` |  |  |
| md.comp.list.list-item.trailing-supporting-text.line-height | LINE_HEIGHT | Size and typography | `md.sys.typescale.label-small.line-height` |  |  |
| md.comp.list.list-item.trailing-supporting-text.size | FONT_SIZE | Size and typography | `md.sys.typescale.label-small.size` |  |  |
| md.comp.list.list-item.trailing-supporting-text.tracking | FONT_TRACKING | Size and typography | `md.sys.typescale.label-small.tracking` |  |  |
| md.comp.list.list-item.trailing-supporting-text.weight | FONT_WEIGHT | Size and typography | `md.sys.typescale.label-small.weight` |  |  |
| md.comp.list.list-item.supporting-text.type | TYPOGRAPHY | Size and typography | font name: `md.comp.list.list-item.supporting-text.font`, font weight: `md.comp.list.list-item.supporting-text.weight`, font size: `md.comp.list.list-item.supporting-text.size`, font tracking: `md.comp.list.list-item.supporting-text.tracking`, line height: `md.comp.list.list-item.supporting-text.line-height` |  |  |
| md.comp.list.list-item.supporting-text.font | FONT_NAMES | Size and typography | `md.sys.typescale.body-medium.font` |  |  |
| md.comp.list.list-item.supporting-text.line-height | LINE_HEIGHT | Size and typography | `md.sys.typescale.body-medium.line-height` |  |  |
| md.comp.list.list-item.supporting-text.size | FONT_SIZE | Size and typography | `md.sys.typescale.body-medium.size` |  |  |
| md.comp.list.list-item.supporting-text.tracking | FONT_TRACKING | Size and typography | `md.sys.typescale.body-medium.tracking` |  |  |
| md.comp.list.list-item.supporting-text.weight | FONT_WEIGHT | Size and typography | `md.sys.typescale.body-medium.weight` |  |  |
| md.comp.list.list-item.overline.type | TYPOGRAPHY | Size and typography | font name: `md.comp.list.list-item.overline.font`, font weight: `md.comp.list.list-item.overline.weight`, font size: `md.comp.list.list-item.overline.size`, font tracking: `md.comp.list.list-item.overline.tracking`, line height: `md.comp.list.list-item.overline.line-height` |  |  |
| md.comp.list.list-item.overline.font | FONT_NAMES | Size and typography | `md.sys.typescale.label-small.font` |  |  |
| md.comp.list.list-item.overline.line-height | LINE_HEIGHT | Size and typography | `md.sys.typescale.label-small.line-height` |  |  |
| md.comp.list.list-item.overline.size | FONT_SIZE | Size and typography | `md.sys.typescale.label-small.size` |  |  |
| md.comp.list.list-item.overline.tracking | FONT_TRACKING | Size and typography | `md.sys.typescale.label-small.tracking` |  |  |
| md.comp.list.list-item.overline.weight | FONT_WEIGHT | Size and typography | `md.sys.typescale.label-small.weight` |  |  |

### Color

Color values are implemented through design tokens. For designers, this means working with color values that correspond with tokens. In implementation, a color value will be a token that references a value. [Learn more about design tokens](../../foundations/design-tokens/overview.md)

![9 baseline list element color roles in light and dark mode.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmiq3n8p4-20-3P.png?alt=media&token=b48ec623-56e1-4fb8-af5d-cb492be8701a>)

_List color roles used for light and dark themes: Surface; On surface; On surface variant; On surface variant; On surface variant; Outline variant; Primary container; On primary container; On surface variant_

### States

States are visual representations used to communicate the status of a component or interactive element.

![6 baseline list states in light and dark mode.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmiq3rr3p-21-3P.png?alt=media&token=1cad62d6-6b18-4aff-a5a3-95081a37bea6>)

_1. Enabled 2. Disabled 3. Hovered 4. Focused 5. Pressed 6. Dragged_

### Layout

#### One-line lists

![Alignment, padding, and size specifications for baseline list items with 1 line of text.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmiofu0i1-22-3P.png?alt=media&token=6d6baf81-2dcf-44c7-a698-12dc4df7b893>)

_Baseline one-line list alignment, padding, and size measurements_

![Measurements for a 3-item list with 1 line each.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmiogaa9o-23-3P.png?alt=media&token=e5b30379-40b7-4f40-8b8c-ab3de728b304>)

_Baseline list item measurements and padding_

#### Two-line lists

![Alignment, padding, and size specifications for baseline list items with 2 lines of text.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmiogazt3-24-3P.png?alt=media&token=555ab1aa-0068-411e-aa41-3c58358396ba>)

_Baseline two-line list alignment, padding, and size measurements_

![Measurements for a 3-item list with 2 lines each.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmiogcld0-25-3P.png?alt=media&token=8fff9b9c-745e-4132-bd10-cecb13ce4172>)

_Baseline list item measurements and padding_

#### Three-line lists

![Alignment, padding, and size specifications for baseline list items with 3 lines of text.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmiogdayj-26-3P.png?alt=media&token=f40d0151-71ac-4ae1-bb39-ac88dacb0a91>)

_Baseline three-line list alignment, padding, and size measurements_

![Measurements for a 3-item list with 3 lines each.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmiogduex-27-3P.png?alt=media&token=54e24855-b3e4-44d3-af16-3cf06223505d>)

_Baseline list item measurements and padding_

| Attribute | Value |
| --- | --- |
| Label alignment | Center |
| Label alignment when height is 88dp or taller | Top |
| Label left padding | 16dp |
| Leading element alignment (vertical) | Center |
| Leading element alignment (vertical) when height is 88dp or taller | Top |
| Leading element left padding | 16dp |
| Leading icon alignment (vertical) | Top |
| Leading icon top padding | 8dp |
| Leading icon top padding when height is 88dp or taller | 12dp |
| Trailing element alignment (vertical) | Center |
| Trailing element alignment (vertical) when height is 88dp or taller | Top |
| Trailing element left padding | 16dp |
| Trailing element right padding | 24dp |
| Padding above/below divider | 0dp |
| Targets | 48dp |
| Divider full-width | 100% |
| Divider inset left padding | 16dp |
| Divider inset right padding | 24dp |

### Configurations

#### Leading avatar

![1, 2, and 3-line list items with and without a leading avatar and trailing checkbox, in dark mode.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmioh5e7e-28-3P.png?alt=media&token=b57fa62b-3d95-4dff-9eab-552c7d172234>)

_With leading avatar; With leading avatar and trailing checkbox_

#### Leading image or thumbnail

![1, 2, and 3-line list items with and without a leading image and trailing checkbox, in dark mode.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmioh5246-29-3P.png?alt=media&token=16932198-c722-4db9-b051-747ca911531d>)

_With leading image; With leading image and trailing checkbox_

#### Leading video

![1, 2, and 3-line list items with and without a leading video and trailing checkbox, in dark mode.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmioh4nys-30-3P.png?alt=media&token=fb71bee0-4810-4219-8433-757fdd7c5bec>)

_With leading video; With leading video and trailing checkbox_

#### Leading icon

![1, 2, and 3-line list items with and without a leading icon and trailing checkbox, in dark mode.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmioh3zha-31-3P.png?alt=media&token=ef144cf2-fd45-48fb-9a7b-496903e6a9ab>)

_With leading icon; With leading icon and trailing checkbox_

#### Text-only

![1, 2, and 3-line text only list items with and without a trailing checkbox, in dark mode.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmioh3lil-32-3P.png?alt=media&token=6053c26e-0987-47a4-af39-7f0371f591a4>)

_With text only; With text and trailing checkbox_

#### Leading checkbox

![1, 2, and 3-line list items with and without a leading checkbox and trailing text, in dark mode.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmioh2vwf-33-3P.png?alt=media&token=0cb7fa46-e3c2-4c30-be0a-6b0faa1043e2>)

_With leading checkbox; With leading checkbox and trailing text_

#### Leading radio button

![1, 2, and 3-line list items with and without a leading radio button and trailing text, in dark mode.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmioh2gxb-34-3P.png?alt=media&token=62ff10e1-d363-42da-b92c-10b308604018>)

_With leading radio button; With leading radio button and trailing text_

#### Trailing switch

![1, 2, and 3-line list items with and without a leading icon and trailing switch, in dark mode.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmioh0mgk-35-3P.png?alt=media&token=da8d3853-6828-43dd-ad1a-d98b1a8e7b94>)

_With trailing switch; With leading icon and trailing switch_

---
title: "App bars"
source_url: "https://m3.material.io/components/app-bars/specs"
lastmod: "2026-05-18"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "components"
assets:
  - "assets/images/6343539e762e-mmagk85x-01-3P.png"
  - "assets/images/d717360d52dd-mmar0379-01-deprecated-3P.png"
  - "assets/images/d52474fcf262-mmah1clb-02-3P.png"
  - "assets/images/7f0e55e42469-mmahcxrn-03-3P.png"
  - "assets/images/720625330525-mmahfvsg-04-3P.png"
  - "assets/images/94073505e16e-mmahnmz2-05-3P.png"
  - "assets/images/85fea7ec36ba-mmahv36y-06-3P.png"
  - "assets/images/56d3a803f47a-mmaiawq3-07-3P.png"
  - "assets/images/2af0cde47c10-mmaiezk5-08-3P.png"
  - "assets/images/83e6d853fb23-mmao48ka-09-3P.png"
  - "assets/images/190f1544e289-mmaoaorq-10-3P.png"
  - "assets/images/f1b469aecfe9-mmaod25k-11-3P.png"
  - "assets/images/e3514b62891d-mmaof4ew-12-3P.png"
  - "assets/images/00d15e4aadc3-mmaonw2d-13-3P.png"
  - "assets/images/6b5c4686eab0-mmaoprdb-14-3P.png"
  - "assets/images/50ac0565f56d-mmap4u9c-15-3P.png"
  - "assets/images/01c5eab435d1-mmapnkl4-16-3P.png"
  - "assets/images/6ab8e28aa8ed-mmaqk9ki-17-deprecated-3P.png"
  - "assets/images/109ad9f572b0-mmaqrta6-18-deprecated-3P.png"
  - "assets/images/0e84b2436bce-mmaquor6-19-deprecated-3P.png"
  - "assets/images/f9eed1682f54-mmaqwh2h-20-deprecated-3P.png"
---

# App bars

App bars are placed at the top of the screen to help people navigate through a product

## Specs

## Variants

![4 variants of app bars.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmmagk85x-01-3P.png?alt=media&token=f4b3768f-c723-4ad0-b874-8add82998732>)

_Search app bar; Small; Medium flexible; Large flexible_

### Baseline variants

The baseline M3 **medium** and **large** app bars are no longer recommended in M3 Expressive, and should be replaced with **medium flexible** and **large flexible** app bars, which are similar visually, but have multi-line support, a shorter height, and can contain a wide variety of elements, like images. [Jump to baseline app bar specs](specs.md#faec9baf-140f-41dc-8b88-2792e90d9d5d)

![2 baseline app bar variants, medium and large.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmmar0379-01-deprecated-3P.png?alt=media&token=5bc2c581-dfea-4b04-af42-2eee017bc73a>)

_Baseline variants Medium; Large_

| Variant | M3 | M3 Expressive |
| --- | --- | --- |
| Search app bar | \-- | Available |
| Small | Available | Available |
| Center-aligned | Available | Merged into **small**. Use centered-text configuration. |
| Medium (baseline) | Available | Not recommended. Use **medium flexible** |
| Medium flexible | \-- | Available |
| Large (baseline) | Available | Not recommended. Use **large flexible** |
| Large flexible | \-- | Available |

## Configurations

### Text alignment

![4 variants of app bars with different left and center aligned text headlines.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmmah1clb-02-3P.png?alt=media&token=8bb55fe8-792f-4e9d-bbe4-b8087281207e>)

_Text labels, including supporting text, can be aligned to the leading edge or centered_

| Category | Configuration | M3 | M3 Expressive |
| --- | --- | --- | --- |
| Text alignment | Leading edge (default) | Available | Available |
| Centered | \-- | Available |  |

## Tokens & specs

Select a token set to view in the table's menu. App bar token sets are organized into a common token set, and size-specific tokens. [Learn about design tokens](../../foundations/design-tokens/overview.md)

### TOKEN_TABLE

Component: App bars

Configured context tags: Static, 3P, Android

#### Token sets

| Token set | Name | Type | Tokens | Description |
| --- | --- | --- | --- | --- |
| App bar - Common | md.comp.app-bar | COMPONENT | 19 | Common tokens for all app bars. |
| App bar - Size - Small | md.comp.app-bar.small | COMPONENT | 7 | For the expressive small app bar and search app bar. |
| App bar - Size - Medium Flexible | md.comp.app-bar.medium-flexible | COMPONENT | 4 | For the medium flexible app bar. |
| App bar - Size - Large Flexible | md.comp.app-bar.large-flexible | COMPONENT | 4 | For the large flexible app bar. |

#### App bar - Common (md.comp.app-bar)

Common tokens for all app bars.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.app-bar.container.color | COLOR | Color | `md.sys.color.surface` |  |  |
| md.comp.app-bar.search.container.color | COLOR | Color | `md.sys.color.surface-container` |  |  |
| md.comp.app-bar.search.label.color | COLOR | Color | `md.sys.color.on-surface-variant` |  |  |
| md.comp.app-bar.on-scroll.container.color | COLOR | Color | `md.sys.color.surface-container` |  |  |
| md.comp.app-bar.search.on-scroll.container.color | COLOR | Color | `md.sys.color.surface-container-highest` |  |  |
| md.comp.app-bar.container.elevation | ELEVATION | Color | `md.sys.elevation.level0` |  |  |
| md.comp.app-bar.on-scroll.container.elevation | ELEVATION | Color | `md.sys.elevation.level2` |  |  |
| md.comp.app-bar.title.color | COLOR | Color | `md.sys.color.on-surface` |  |  |
| md.comp.app-bar.subtitle.color | COLOR | Color | `md.sys.color.on-surface-variant` |  |  |
| md.comp.app-bar.leading-icon.color | COLOR | Color | `md.sys.color.on-surface` |  |  |
| md.comp.app-bar.trailing-icon.color | COLOR | Color | `md.sys.color.on-surface-variant` |  |  |
| md.comp.app-bar.leading-space | LENGTH | Spacing | 4dp |  |  |
| md.comp.app-bar.trailing-space | LENGTH | Spacing | 4dp |  |  |
| md.comp.app-bar.icon-button-space | LENGTH | Spacing | 0dp |  |  |
| md.comp.app-bar.search.leading-space | LENGTH | Spacing | 8dp |  |  |
| md.comp.app-bar.search.trailing-space | LENGTH | Spacing | 8dp |  |  |
| md.comp.app-bar.container.shape | SHAPE | Shape | `md.sys.shape.corner.none` |  |  |
| md.comp.app-bar.avatar.size | LENGTH | Size | 32dp |  |  |
| md.comp.app-bar.icon.size | LENGTH | Size | 24dp |  |  |

#### App bar - Size - Small (md.comp.app-bar.small)

For the expressive small app bar and search app bar.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.app-bar.small.container.height | LENGTH |  | 64dp |  |  |
| md.comp.app-bar.small.title.font | TYPOGRAPHY |  | `md.sys.typescale.title-large` |  |  |
| md.comp.app-bar.small.subtitle.font | TYPOGRAPHY |  | `md.sys.typescale.label-medium` |  |  |
| md.comp.app-bar.small.icon.size | LENGTH |  | 24dp |  | Please use icon size token in the common app bar set |
| md.comp.app-bar.small.search.container.height | LENGTH |  | 56dp |  |  |
| md.comp.app-bar.small.search.container.shape | SHAPE |  | `md.sys.shape.corner.full` |  |  |
| md.comp.app-bar.small.search.label-text.font | FONT_SIZE |  | `md.sys.typescale.body-large` |  |  |

#### App bar - Size - Medium Flexible (md.comp.app-bar.medium-flexible)

For the medium flexible app bar.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.app-bar.medium-flexible.container.height | LENGTH |  | 112dp |  |  |
| md.comp.app-bar.medium-flexible.with-subtitle.container.height | LENGTH |  | 136dp |  |  |
| md.comp.app-bar.medium-flexible.title.font | TYPOGRAPHY |  | `md.sys.typescale.headline-medium` |  |  |
| md.comp.app-bar.medium-flexible.subtitle.font | TYPOGRAPHY |  | `md.sys.typescale.label-large` |  |  |

#### App bar - Size - Large Flexible (md.comp.app-bar.large-flexible)

For the large flexible app bar.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.app-bar.large-flexible.container.height | LENGTH |  | 120dp |  |  |
| md.comp.app-bar.large-flexible.with-subtitle.container.height | LENGTH |  | 152dp |  |  |
| md.comp.app-bar.large-flexible.title.font | TYPOGRAPHY |  | `md.sys.typescale.display-small` |  |  |
| md.comp.app-bar.large-flexible.subtitle.font | TYPOGRAPHY |  | `md.sys.typescale.title-medium` |  |  |

### Search component tokens & specs

The default search component tokens are used in the search app bar.

### TOKEN_TABLE

Component: Search

Configured context tags: None, Medium contrast, Default, High contrast, Static, 3P, Android, Dark, Light

#### Token sets

| Token set | Name | Type | Tokens | Description |
| --- | --- | --- | --- | --- |
| Search - Bar | md.comp.search-bar | COMPONENT | 49 | For baseline search bar and expressive search. |
| Search - View | md.comp.search-view | COMPONENT | 36 | For baseline search view and expressive search when activated. |

#### Search - Bar (md.comp.search-bar)

For baseline search bar and expressive search.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.search-bar.hover.state-layer.color | COLOR | Color / Hovered | `md.sys.color.on-surface` |  |  |
| md.comp.search-bar.hover.state-layer.opacity | OPACITY | Color / Hovered | `md.sys.state.hover.state-layer-opacity` |  |  |
| md.comp.search-bar.hover.supporting-text.color | COLOR | Color / Hovered | `md.sys.color.on-surface-variant` |  |  |
| md.comp.search-bar.pressed.state-layer.color | COLOR | Color / Pressed | `md.sys.color.on-surface` |  |  |
| md.comp.search-bar.container.color | COLOR | Color / Enabled | `md.sys.color.surface-container-high` |  |  |
| md.comp.search-bar.pressed.state-layer.opacity | OPACITY | Color / Pressed | `md.sys.state.pressed.state-layer-opacity` |  |  |
| md.comp.search-bar.container.elevation | ELEVATION | Color / Enabled | `md.sys.elevation.level3` |  |  |
| md.comp.search-bar.pressed.supporting-text.color | COLOR | Color / Pressed | `md.sys.color.on-surface-variant` |  |  |
| md.comp.search-bar.leading-icon.color | COLOR | Color / Enabled | `md.sys.color.on-surface` |  |  |
| md.comp.search-bar.trailing-icon.color | COLOR | Color / Enabled | `md.sys.color.on-surface-variant` |  |  |
| md.comp.search-bar.supporting-text.color | COLOR | Color / Enabled | `md.sys.color.on-surface-variant` |  |  |
| md.comp.search-bar.input-text.color | COLOR | Color / Enabled | `md.sys.color.on-surface` |  |  |
| md.comp.search-bar.focus.indicator.color | COLOR | Color / Focused | `md.sys.color.secondary` |  |  |
| md.comp.search-bar.focus.indicator.thickness | LENGTH | Color / Focused | `md.sys.state.focus-indicator.thickness` |  |  |
| md.comp.search-bar.focus.indicator.outline.offset | LENGTH | Color / Focused | `md.sys.state.focus-indicator.outer-offset` |  |  |
| md.comp.search-bar.leading-space | LENGTH | Layout and Text / Baseline | 16dp |  |  |
| md.comp.search-bar.trailing-space | LENGTH | Layout and Text / Baseline | 16dp |  |  |
| md.comp.search-bar.leading-icon.leading-icon-label-space | LENGTH | Layout and Text / Baseline | 16dp |  |  |
| md.comp.search-bar.trailing-icon.label-trailing-icon-space | LENGTH | Layout and Text / Baseline | 16dp |  |  |
| md.comp.search-bar.contained.leading-margin | LENGTH | Layout and Text / Contained (expressive) | 24dp |  |  |
| md.comp.search-bar.contained.trailing-margin | LENGTH | Layout and Text / Contained (expressive) | 24dp |  |  |
| md.comp.search-bar.container.height | LENGTH | Layout and Text / Common tokens | 56dp |  |  |
| md.comp.search-bar.contained.motion.spring | CUSTOM_COMPOSITE | Layout and Text / Contained (expressive) | `md.sys.motion.spring.fast.spatial` |  |  |
| md.comp.search-bar.container.shape | SHAPE | Layout and Text / Common tokens | `md.sys.shape.corner.full` |  |  |
| md.comp.search-bar.contained.leading-space | LENGTH | Layout and Text / Contained (expressive) | 4dp |  |  |
| md.comp.search-bar.avatar.size | LENGTH | Layout and Text / Common tokens | 30dp |  |  |
| md.comp.search-bar.contained.trailing-space | LENGTH | Layout and Text / Contained (expressive) | 4dp |  |  |
| md.comp.search-bar.avatar.shape | SHAPE | Layout and Text / Common tokens | `md.sys.shape.corner.full` |  |  |
| md.comp.search-bar.contained.no-actions.leading-space | LENGTH | Layout and Text / Contained (expressive) | 16dp |  |  |
| md.comp.search-bar.contained.no-actions.trailing-space | LENGTH | Layout and Text / Contained (expressive) | 16dp |  |  |
| md.comp.search-bar.icon.size | LENGTH | Layout and Text / Common tokens | 24dp |  |  |
| md.comp.search-bar.contained.icon-label.gap | LENGTH | Layout and Text / Contained (expressive) | 4dp |  |  |
| md.comp.search-bar.supporting-text.type | TYPOGRAPHY | Layout and Text / Common tokens | font name: `md.comp.search-bar.supporting-text.font`, font weight: `md.comp.search-bar.supporting-text.weight`, font size: `md.comp.search-bar.supporting-text.size`, font tracking: `md.comp.search-bar.supporting-text.tracking`, line height: `md.comp.search-bar.supporting-text.line-height` |  |  |
| md.comp.search-bar.contained.avatar.target-size | LENGTH | Layout and Text / Contained (expressive) | 48dp |  |  |
| md.comp.search-bar.supporting-text.font | FONT_NAMES | Layout and Text / Common tokens | `md.sys.typescale.body-large.font` |  |  |
| md.comp.search-bar.contained.trailing-actions.gap | LENGTH | Layout and Text / Contained (expressive) | 0dp |  |  |
| md.comp.search-bar.supporting-text.line-height | LINE_HEIGHT | Layout and Text / Common tokens | `md.sys.typescale.body-large.line-height` |  |  |
| md.comp.search-bar.contained.trailing-actions.leading-space | LENGTH | Layout and Text / Contained (expressive) | 4dp |  |  |
| md.comp.search-bar.supporting-text.size | FONT_SIZE | Layout and Text / Common tokens | `md.sys.typescale.body-large.size` |  |  |
| md.comp.search-bar.contained.trailing-actions.trailing-space | LENGTH | Layout and Text / Contained (expressive) | 4dp |  |  |
| md.comp.search-bar.supporting-text.weight | FONT_WEIGHT | Layout and Text / Common tokens | `md.sys.typescale.body-large.weight` |  |  |
| md.comp.search-bar.supporting-text.tracking | FONT_TRACKING | Layout and Text / Common tokens | `md.sys.typescale.body-large.tracking` |  |  |
| md.comp.search-bar.input-text.type | TYPOGRAPHY | Layout and Text / Common tokens | font name: `md.comp.search-bar.input-text.font`, font weight: `md.comp.search-bar.input-text.weight`, font size: `md.comp.search-bar.input-text.size`, font tracking: `md.comp.search-bar.input-text.tracking`, line height: `md.comp.search-bar.input-text.line-height` |  |  |
| md.comp.search-bar.input-text.font | FONT_NAMES | Layout and Text / Common tokens | `md.sys.typescale.body-large.font` |  |  |
| md.comp.search-bar.input-text.line-height | LINE_HEIGHT | Layout and Text / Common tokens | `md.sys.typescale.body-large.line-height` |  |  |
| md.comp.search-bar.input-text.size | FONT_SIZE | Layout and Text / Common tokens | `md.sys.typescale.body-large.size` |  |  |
| md.comp.search-bar.input-text.weight | FONT_WEIGHT | Layout and Text / Common tokens | `md.sys.typescale.body-large.weight` |  |  |
| md.comp.search-bar.input-text.tracking | FONT_TRACKING | Layout and Text / Common tokens | `md.sys.typescale.body-large.tracking` |  |  |
| md.comp.search-bar.container.surface-tint-layer.color | COLOR | Layout and Text / Common tokens | `md.sys.color.surface-tint` |  | Deprecated as part of the update from opacity based surfaces to tonal surfaces. Surfaces no longer use surface-tint layers for tinting, please use the desired surface role directly as the container color. |

#### Search - View (md.comp.search-view)

For baseline search view and expressive search when activated.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.search-view.container.surface-tint-layer.color | COLOR |  | `md.sys.color.surface-tint` |  | Deprecated as part of the update from opacity based surfaces to tonal surfaces. Surfaces no longer use surface-tint layers for tinting, please use the desired surface role directly as the container color. |
| md.comp.search-view.container.color | COLOR | Color | `md.sys.color.surface-container-high` |  |  |
| md.comp.search-view.contained.background.color | COLOR | Color | `md.sys.color.surface-container-low` |  |  |
| md.comp.search-view.container.elevation | ELEVATION | Color | `md.sys.elevation.level3` |  |  |
| md.comp.search-view.header.supporting-text.color | COLOR | Color | `md.sys.color.on-surface-variant` |  |  |
| md.comp.search-view.header.input-text.color | COLOR | Color | `md.sys.color.on-surface` |  |  |
| md.comp.search-view.header.leading-icon.color | COLOR | Color | `md.sys.color.on-surface` |  |  |
| md.comp.search-view.header.trailing-icon.color | COLOR | Color | `md.sys.color.on-surface-variant` |  |  |
| md.comp.search-view.divider.color | COLOR | Color | `md.sys.color.outline` |  |  |
| md.comp.search-view.docked.container.shape | SHAPE | Layout and Text / Divided (baseline) | `md.sys.shape.corner.extra-large` |  |  |
| md.comp.search-view.full-screen.header.container.height | LENGTH | Layout and Text / Divided (baseline) | 72dp |  |  |
| md.comp.search-view.docked.header.container.height | LENGTH | Layout and Text / Divided (baseline) | 56dp |  |  |
| md.comp.search-view.leading-space | LENGTH | Layout and Text / Divided (baseline) | 16dp |  |  |
| md.comp.search-view.trailing-space | LENGTH | Layout and Text / Divided (baseline) | 16dp |  |  |
| md.comp.search-view.leading-icon.leading-icon-label-space | LENGTH | Layout and Text / Divided (baseline) | 16dp |  |  |
| md.comp.search-view.trailing-icon.label-trailing-icon-space | LENGTH | Layout and Text / Divided (baseline) | 16dp |  |  |
| md.comp.search-view.full-screen.container.shape | SHAPE | Layout and Text / Common tokens | `md.sys.shape.corner.none` |  |  |
| md.comp.search-view.header.supporting-text.type | TYPOGRAPHY | Layout and Text / Common tokens | font name: `md.comp.search-view.header.supporting-text.font`, font weight: `md.comp.search-view.header.supporting-text.weight`, font size: `md.comp.search-view.header.supporting-text.size`, font tracking: `md.comp.search-view.header.supporting-text.tracking`, line height: `md.comp.search-view.header.supporting-text.line-height` |  |  |
| md.comp.search-view.header.supporting-text.font | FONT_NAMES | Layout and Text / Common tokens | `md.sys.typescale.body-large.font` |  |  |
| md.comp.search-view.header.supporting-text.line-height | LINE_HEIGHT | Layout and Text / Common tokens | `md.sys.typescale.body-large.line-height` |  |  |
| md.comp.search-view.header.supporting-text.size | FONT_SIZE | Layout and Text / Common tokens | `md.sys.typescale.body-large.size` |  |  |
| md.comp.search-view.header.supporting-text.weight | FONT_WEIGHT | Layout and Text / Common tokens | `md.sys.typescale.body-large.weight` |  |  |
| md.comp.search-view.header.supporting-text.tracking | FONT_TRACKING | Layout and Text / Common tokens | `md.sys.typescale.body-large.tracking` |  |  |
| md.comp.search-view.header.input-text.type | TYPOGRAPHY | Layout and Text / Common tokens | font name: `md.comp.search-view.header.input-text.font`, font weight: `md.comp.search-view.header.input-text.weight`, font size: `md.comp.search-view.header.input-text.size`, font tracking: `md.comp.search-view.header.input-text.tracking`, line height: `md.comp.search-view.header.input-text.line-height` |  |  |
| md.comp.search-view.header.input-text.font | FONT_NAMES | Layout and Text / Common tokens | `md.sys.typescale.body-large.font` |  |  |
| md.comp.search-view.header.input-text.line-height | LINE_HEIGHT | Layout and Text / Common tokens | `md.sys.typescale.body-large.line-height` |  |  |
| md.comp.search-view.header.input-text.size | FONT_SIZE | Layout and Text / Common tokens | `md.sys.typescale.body-large.size` |  |  |
| md.comp.search-view.header.input-text.weight | FONT_WEIGHT | Layout and Text / Common tokens | `md.sys.typescale.body-large.weight` |  |  |
| md.comp.search-view.header.input-text.tracking | FONT_TRACKING | Layout and Text / Common tokens | `md.sys.typescale.body-large.tracking` |  |  |
| md.comp.search-view.contained.leading-margin | LENGTH | Layout and Text / Contained (expressive) | 12dp |  |  |
| md.comp.search-view.contained.trailing-margin | LENGTH | Layout and Text / Contained (expressive) | 12dp |  |  |
| md.comp.search-view.contained.docked.bar-results.gap | LENGTH | Layout and Text / Contained (expressive) | 2dp |  |  |
| md.comp.search-view.contained.docked.results.shape | SHAPE | Layout and Text / Contained (expressive) | `md.sys.shape.corner.medium` |  |  |
| md.comp.search-view.contained.docked.bar.shape | SHAPE | Layout and Text / Contained (expressive) | `md.sys.shape.corner.full` |  |  |
| md.comp.search-view.contained.full-screen.bar.container.height | LENGTH | Layout and Text / Contained (expressive) | 56dp |  |  |
| md.comp.search-view.contained.icon-label.gap | LENGTH | Layout and Text / Contained (expressive) | 4dp |  |  |

## Anatomy

![5 elements of the component.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmmahcxrn-03-3P.png?alt=media&token=dee6a9ac-4550-46f7-afed-1503ba60e005>)

_Container; Leading button; Trailing elements; Headline; Subtitle_

App bars can be customized to include:

-   An image or logo
-   A subtitle
-   A filled icon button

Avoid customizing the size of the heading and subtitle, or adding too many actions.

![3 app bars: 1 with a newspaper logo, 1 with a subtitle, and 1 with a filled icon button.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmmahfvsg-04-3P.png?alt=media&token=3c3da553-0d20-4215-a307-7355f3e97a41>)

_The app bar can have different layouts depending on which elements are shown_

### Search

The search app bar can include trailing actions inside and outside the search bar. When the search bar is selected, it should open the search view component.

![5 elements of the search app bar.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmmahnmz2-05-3P.png?alt=media&token=22ee7ea7-1df4-4a7d-95ce-ff42f83ec483>)

_Container; Leading icon button; Hinted search text; Trailing icon or avatar; Search container_

![3 layouts of icons in the search app bar.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmmahv36y-06-3P.png?alt=media&token=12c2ad1e-b608-4977-9dc6-b56085f99efd>)

_A leading element and a trailing element outside search; A leading element, a trailing element inside search, and a trailing element outside search; A leading element and two trailing elements outside search_

### Image 

An image can be placed in the app bar. In small app bars, this can replace the label text.

![Graphic replacing text headline content.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmmaiawq3-07-3P.png?alt=media&token=7973518d-24eb-47d4-bf91-29091cde0f79>)

_Images can be added to app bars and can replace label text on small app bars_

### Filled trailing icon button

The app bar's trailing icon buttons can be replaced with a single, primary, or tonal filled icon button in default or wide sizes.

![App bars configured with filled trailing icons.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmmaiezk5-08-3P.png?alt=media&token=35d786d8-d9a1-4c18-9e4b-db1606b4ac43>)

_The trailing icons can be configured to be a single filled icon button_

### Subtitle

![App bars configured with subtitles below their headlines.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmmao48ka-09-3P.png?alt=media&token=c99d158e-422b-4454-b89c-359a5c99e7af>)

_The medium flexible and large flexible app bars hug the text contents, so they are taller when a subtitle is visible Small; Small with subtitle; Medium flexible; Medium flexible with subtitle; Large flexible; Large flexible with subtitle_

## Color

Color values are implemented through design tokens. For design, this means working with color values that correspond with tokens. For implementation, a color value will be a token that references a value. [Learn more about design tokens](../../foundations/design-tokens/overview.md)

All app bars share the same color roles. On scroll, the container changes color to **surface container**.

![4 color roles of the leading edge app bar in light and dark scheme.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmmaoaorq-10-3P.png?alt=media&token=81cfa880-1bee-497e-8e12-dd4fcd127a37>)

_App bar color roles used for light and dark themes: Surface; On surface; On surface variant; On surface; On surface variant; Surface container (on scroll)_

![4 color roles of the center-aligned app bar in light and dark scheme.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmmaod25k-11-3P.png?alt=media&token=6c3264aa-5fc5-473a-9be6-7df09e087e18>)

_Search app bar color roles used for light and dark themes: Surface; On surface variant; On surface variant; On surface variant; Surface container; Surface container; Surface container highest_

### Scroll states

![Color roles for app bars when flat and on scroll.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmmaof4ew-12-3P.png?alt=media&token=d415b2d4-018a-427e-8dd5-c04161f65b1f>)

_The app bar changes color when flat or on scroll. The search bar can also change color on scroll. Flat; On scroll_

## Measurements

### Search app bar

![Search app bar size and padding measurements.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmmaonw2d-13-3P.png?alt=media&token=d231e0ce-4b66-4e1e-bdf4-3d0c05add1eb>)

_Search app bar padding and size measurements_

### Small app bar

![Small app bar size and padding measurements.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmmaoprdb-14-3P.png?alt=media&token=eb64f015-3fda-4a6f-a1ac-851b7e2b622b>)

_Small app bar padding and size measurements_

### Medium flexible app bar

![Medium flexible app bar padding and size measurements.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmmap4u9c-15-3P.png?alt=media&token=2aba72f0-d565-4ee3-aeb9-e4583c00069e>)

_Medium flexible app bar padding and size measurements_

### Large flexible app bar

![Large flexible app bar padding and size measurements](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmmapnkl4-16-3P.png?alt=media&token=98a8959d-34a9-40d4-9a2f-28f066ffdc74>)

_Large flexible app bar padding and size measurements_

* * *

## Baseline app bars

The **medium** and **large** app bars are no longer recommended in M3 Expressive. Use the **medium flexible** and **large flexible** app bars in their place.

![4 elements of medium and large app bars.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmmaqk9ki-17-deprecated-3P.png?alt=media&token=b1be4d49-8395-4deb-9fb3-4cbb0c03fdb7>)

_Medium and large app bars have the same elements: Container; Leading button; Trailing icons; Headline_

### Tokens & specs

Select a token set to view in the table's menu. Baseline app bar token sets are organized into medium, large, and older baseline token sets. [Learn about design tokens](../../foundations/design-tokens/overview.md)

### TOKEN_TABLE

Component: App bars

Configured context tags: None, Medium contrast, Default, High contrast, Static, 3P, Android, Dark, Light

#### Token sets

| Token set | Name | Type | Tokens | Description |
| --- | --- | --- | --- | --- |
| App bar - Size - Large (baseline) | md.comp.app-bar.large | COMPONENT | 4 | For the baseline large app bar. In expressive, consider using large flexible instead `md.comp.app-bar.large-flexible`. |
| App bar - Size - Medium (baseline) | md.comp.app-bar.medium | COMPONENT | 4 | For the baseline medium app bar. In expressive, consider using medium flexible instead `md.comp.app-bar.medium-flexible`. |
| [Deprecated] Top app bar - Large | md.comp.top-app-bar.large | COMPONENT | 16 | This token set is deprecated. Use `md.comp.app-bar` and `md.comp.app-bar.large` tokens instead. Original tokens for the large top app bar. |
| [Deprecated] Top app bar - Medium | md.comp.top-app-bar.medium | COMPONENT | 16 | This token set is deprecated. Use `md.comp.app-bar` and `md.comp.app-bar.medium` tokens instead. Original tokens for the medium top app bar. |
| [Deprecated] Top app bar - Small, Center-aligned | md.comp.top-app-bar.small.centered | COMPONENT | 21 | This token set is deprecated. Use `md.comp.app-bar` and `md.comp.app-bar.small` tokens instead. Original component tokens for the center-aligned top app bar. |
| [Deprecated] Top app bar - Small | md.comp.top-app-bar.small | COMPONENT | 19 | This token set is deprecated. Use `md.comp.app-bar` and `md.comp.app-bar.small` tokens instead. Original tokens for the small top app bar. |

#### App bar - Size - Large (baseline) (md.comp.app-bar.large)

For the baseline large app bar. In expressive, consider using large flexible instead `md.comp.app-bar.large-flexible`.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.app-bar.large.container.height | LENGTH |  | 152dp |  |  |
| md.comp.app-bar.large.title.font | TYPOGRAPHY |  | `md.sys.typescale.headline-medium` |  |  |
| md.comp.app-bar.large.icon.size | LENGTH |  | 24dp |  | Please use icon size token in the common app bar set |
| md.comp.app-bar.large.subtitle.font | TYPOGRAPHY |  | `md.sys.typescale.title-medium` |  | No subtitle support on the legacy app bar. Please use the new, flexible variant instead. |

#### App bar - Size - Medium (baseline) (md.comp.app-bar.medium)

For the baseline medium app bar. In expressive, consider using medium flexible instead `md.comp.app-bar.medium-flexible`.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.app-bar.medium.container.height | LENGTH |  | 112dp |  |  |
| md.comp.app-bar.medium.title.font | TYPOGRAPHY |  | `md.sys.typescale.headline-small` |  |  |
| md.comp.app-bar.medium.icon.size | LENGTH |  | 24dp |  | Please use icon size token in the common app bar set |
| md.comp.app-bar.medium.subtitle.font | TYPOGRAPHY |  | `md.sys.typescale.label-large` |  | No subtitle support on the legacy app bar. Please use the new, flexible variant instead. |

#### [Deprecated] Top app bar -  Large (md.comp.top-app-bar.large)

This token set is deprecated. Use `md.comp.app-bar` and `md.comp.app-bar.large` tokens instead. Original tokens for the large top app bar.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.top-app-bar.large.container.color | COLOR | [Deprecated] Enabled / [Deprecated] Container | `md.sys.color.surface` |  | This token is deprecated. Use the corresponding token from the 'md.comp.app-bar' and 'md.comp.app-bar.large' tokens instead. |
| md.comp.top-app-bar.large.container.shape | SHAPE | [Deprecated] Enabled / [Deprecated] Container | `md.sys.shape.corner.none` |  | This token is deprecated. Use the corresponding token from the 'md.comp.app-bar' and 'md.comp.app-bar.large' tokens instead. |
| md.comp.top-app-bar.large.container.height | LENGTH | [Deprecated] Enabled / [Deprecated] Container | 152dp |  | This token is deprecated. Use the corresponding token from the 'md.comp.app-bar' and 'md.comp.app-bar.large' tokens instead. |
| md.comp.top-app-bar.large.container.elevation | ELEVATION | [Deprecated] Enabled / [Deprecated] Container | `md.sys.elevation.level0` |  | This token is deprecated. Use the corresponding token from the 'md.comp.app-bar' and 'md.comp.app-bar.large' tokens instead. |
| md.comp.top-app-bar.large.container.surface-tint-layer.color | COLOR | [Deprecated] Enabled / [Deprecated] Container | `md.sys.color.surface-tint` |  | Deprecated as part of the update from opacity based surfaces to tonal surfaces. Surfaces no longer use surface-tint layers for tinting, please use the desired surface role directly as the container color. |
| md.comp.top-app-bar.large.leading-icon.size | LENGTH | [Deprecated] Enabled / [Deprecated] Leading icon | 24dp |  | This token is deprecated. Use the corresponding token from the 'md.comp.app-bar' and 'md.comp.app-bar.large' tokens instead. |
| md.comp.top-app-bar.large.leading-icon.color | COLOR | [Deprecated] Enabled / [Deprecated] Leading icon | `md.sys.color.on-surface` |  | This token is deprecated. Use the corresponding token from the 'md.comp.app-bar' and 'md.comp.app-bar.large' tokens instead. |
| md.comp.top-app-bar.large.trailing-icon.size | LENGTH | [Deprecated] Enabled / [Deprecated] Trailing icon | 24dp |  | This token is deprecated. Use the corresponding token from the 'md.comp.app-bar' and 'md.comp.app-bar.large' tokens instead. |
| md.comp.top-app-bar.large.trailing-icon.color | COLOR | [Deprecated] Enabled / [Deprecated] Trailing icon | `md.sys.color.on-surface-variant` |  | This token is deprecated. Use the corresponding token from the 'md.comp.app-bar' and 'md.comp.app-bar.large' tokens instead. |
| md.comp.top-app-bar.large.headline.font | FONT_NAMES | [Deprecated] Enabled / [Deprecated] Headline | `md.sys.typescale.headline-medium.font` |  | This token is deprecated. Use the corresponding token from the 'md.comp.app-bar' and 'md.comp.app-bar.large' tokens instead. |
| md.comp.top-app-bar.large.headline.line-height | LINE_HEIGHT | [Deprecated] Enabled / [Deprecated] Headline | `md.sys.typescale.headline-medium.line-height` |  | This token is deprecated. Use the corresponding token from the 'md.comp.app-bar' and 'md.comp.app-bar.large' tokens instead. |
| md.comp.top-app-bar.large.headline.size | FONT_SIZE | [Deprecated] Enabled / [Deprecated] Headline | `md.sys.typescale.headline-medium.size` |  | This token is deprecated. Use the corresponding token from the 'md.comp.app-bar' and 'md.comp.app-bar.large' tokens instead. |
| md.comp.top-app-bar.large.headline.tracking | FONT_TRACKING | [Deprecated] Enabled / [Deprecated] Headline | `md.sys.typescale.headline-medium.tracking` |  | This token is deprecated. Use the corresponding token from the 'md.comp.app-bar' and 'md.comp.app-bar.large' tokens instead. |
| md.comp.top-app-bar.large.headline.weight | FONT_WEIGHT | [Deprecated] Enabled / [Deprecated] Headline | `md.sys.typescale.headline-medium.weight` |  | This token is deprecated. Use the corresponding token from the 'md.comp.app-bar' and 'md.comp.app-bar.large' tokens instead. |
| md.comp.top-app-bar.large.headline.type | TYPOGRAPHY | [Deprecated] Enabled / [Deprecated] Headline | font name: `md.comp.top-app-bar.large.headline.font`, font weight: `md.comp.top-app-bar.large.headline.weight`, font size: `md.comp.top-app-bar.large.headline.size`, font tracking: `md.comp.top-app-bar.large.headline.tracking`, line height: `md.comp.top-app-bar.large.headline.line-height` |  | This token is deprecated. Use the corresponding token from the 'md.comp.app-bar' and 'md.comp.app-bar.large' tokens instead. |
| md.comp.top-app-bar.large.headline.color | COLOR | [Deprecated] Enabled / [Deprecated] Headline | `md.sys.color.on-surface` |  | This token is deprecated. Use the corresponding token from the 'md.comp.app-bar' and 'md.comp.app-bar.large' tokens instead. |

#### [Deprecated] Top app bar - Medium (md.comp.top-app-bar.medium)

This token set is deprecated. Use `md.comp.app-bar` and `md.comp.app-bar.medium` tokens instead. Original tokens for the medium top app bar.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.top-app-bar.medium.container.color | COLOR | [Deprecated] Enabled / [Deprecated] Container | `md.sys.color.surface` |  | This token is deprecated. Use the corresponding token from the 'md.comp.app-bar' and 'md.comp.app-bar.medium' tokens instead. |
| md.comp.top-app-bar.medium.container.shape | SHAPE | [Deprecated] Enabled / [Deprecated] Container | `md.sys.shape.corner.none` |  | This token is deprecated. Use the corresponding token from the 'md.comp.app-bar' and 'md.comp.app-bar.medium' tokens instead. |
| md.comp.top-app-bar.medium.container.height | LENGTH | [Deprecated] Enabled / [Deprecated] Container | 112dp |  | This token is deprecated. Use the corresponding token from the 'md.comp.app-bar' and 'md.comp.app-bar.medium' tokens instead. |
| md.comp.top-app-bar.medium.container.elevation | ELEVATION | [Deprecated] Enabled / [Deprecated] Container | `md.sys.elevation.level0` |  | This token is deprecated. Use the corresponding token from the 'md.comp.app-bar' and 'md.comp.app-bar.medium' tokens instead. |
| md.comp.top-app-bar.medium.container.surface-tint-layer.color | COLOR | [Deprecated] Enabled / [Deprecated] Container | `md.sys.color.surface-tint` |  | Deprecated as part of the update from opacity based surfaces to tonal surfaces. Surfaces no longer use surface-tint layers for tinting, please use the desired surface role directly as the container color. |
| md.comp.top-app-bar.medium.leading-icon.size | LENGTH | [Deprecated] Enabled / [Deprecated] Leading icon | 24dp |  | This token is deprecated. Use the corresponding token from the 'md.comp.app-bar' and 'md.comp.app-bar.medium' tokens instead. |
| md.comp.top-app-bar.medium.leading-icon.color | COLOR | [Deprecated] Enabled / [Deprecated] Leading icon | `md.sys.color.on-surface` |  | This token is deprecated. Use the corresponding token from the 'md.comp.app-bar' and 'md.comp.app-bar.medium' tokens instead. |
| md.comp.top-app-bar.medium.trailing-icon.size | LENGTH | [Deprecated] Enabled / [Deprecated] Trailing icon | 24dp |  | This token is deprecated. Use the corresponding token from the 'md.comp.app-bar' and 'md.comp.app-bar.medium' tokens instead. |
| md.comp.top-app-bar.medium.trailing-icon.color | COLOR | [Deprecated] Enabled / [Deprecated] Trailing icon | `md.sys.color.on-surface-variant` |  | This token is deprecated. Use the corresponding token from the 'md.comp.app-bar' and 'md.comp.app-bar.medium' tokens instead. |
| md.comp.top-app-bar.medium.headline.font | FONT_NAMES | [Deprecated] Enabled / [Deprecated] Headline | `md.sys.typescale.headline-small.font` |  | This token is deprecated. Use the corresponding token from the 'md.comp.app-bar' and 'md.comp.app-bar.medium' tokens instead. |
| md.comp.top-app-bar.medium.headline.line-height | LINE_HEIGHT | [Deprecated] Enabled / [Deprecated] Headline | `md.sys.typescale.headline-small.line-height` |  | This token is deprecated. Use the corresponding token from the 'md.comp.app-bar' and 'md.comp.app-bar.medium' tokens instead. |
| md.comp.top-app-bar.medium.headline.size | FONT_SIZE | [Deprecated] Enabled / [Deprecated] Headline | `md.sys.typescale.headline-small.size` |  | This token is deprecated. Use the corresponding token from the 'md.comp.app-bar' and 'md.comp.app-bar.medium' tokens instead. |
| md.comp.top-app-bar.medium.headline.tracking | FONT_TRACKING | [Deprecated] Enabled / [Deprecated] Headline | `md.sys.typescale.headline-small.tracking` |  | This token is deprecated. Use the corresponding token from the 'md.comp.app-bar' and 'md.comp.app-bar.medium' tokens instead. |
| md.comp.top-app-bar.medium.headline.weight | FONT_WEIGHT | [Deprecated] Enabled / [Deprecated] Headline | `md.sys.typescale.headline-small.weight` |  | This token is deprecated. Use the corresponding token from the 'md.comp.app-bar' and 'md.comp.app-bar.medium' tokens instead. |
| md.comp.top-app-bar.medium.headline.type | TYPOGRAPHY | [Deprecated] Enabled / [Deprecated] Headline | font name: `md.comp.top-app-bar.medium.headline.font`, font weight: `md.comp.top-app-bar.medium.headline.weight`, font size: `md.comp.top-app-bar.medium.headline.size`, font tracking: `md.comp.top-app-bar.medium.headline.tracking`, line height: `md.comp.top-app-bar.medium.headline.line-height` |  | This token is deprecated. Use the corresponding token from the 'md.comp.app-bar' and 'md.comp.app-bar.medium' tokens instead. |
| md.comp.top-app-bar.medium.headline.color | COLOR | [Deprecated] Enabled / [Deprecated] Headline | `md.sys.color.on-surface` |  | This token is deprecated. Use the corresponding token from the 'md.comp.app-bar' and 'md.comp.app-bar.medium' tokens instead. |

#### [Deprecated] Top app bar - Small, Center-aligned (md.comp.top-app-bar.small.centered)

This token set is deprecated. Use `md.comp.app-bar` and `md.comp.app-bar.small` tokens instead. Original component tokens for the center-aligned top app bar.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.top-app-bar.small.centered.avatar.size | LENGTH | Enabled / Avatar | 30dp |  | This token is deprecated. Use the corresponding token from the 'md.comp.app-bar' and 'md.comp.app-bar.small' tokens instead. |
| md.comp.top-app-bar.small.centered.avatar.shape | SHAPE | Enabled / Avatar | `md.sys.shape.corner.full` |  | This token is deprecated. Use the corresponding token from the 'md.comp.app-bar' and 'md.comp.app-bar.small' tokens instead. |
| md.comp.top-app-bar.small.centered.on-scroll.container.color | COLOR | Enabled / Container | `md.sys.color.surface-container` |  | This token is deprecated. Use the corresponding token from the 'md.comp.app-bar' and 'md.comp.app-bar.small' tokens instead. |
| md.comp.top-app-bar.small.centered.container.color | COLOR | Enabled / Container | `md.sys.color.surface` |  | This token is deprecated. Use the corresponding token from the 'md.comp.app-bar' and 'md.comp.app-bar.small' tokens instead. |
| md.comp.top-app-bar.small.centered.container.shape | SHAPE | Enabled / Container | `md.sys.shape.corner.none` |  | This token is deprecated. Use the corresponding token from the 'md.comp.app-bar' and 'md.comp.app-bar.small' tokens instead. |
| md.comp.top-app-bar.small.centered.container.height | LENGTH | Enabled / Container | 64dp |  | This token is deprecated. Use the corresponding token from the 'md.comp.app-bar' and 'md.comp.app-bar.small' tokens instead. |
| md.comp.top-app-bar.small.centered.container.elevation | ELEVATION | Enabled / Container | `md.sys.elevation.level0` |  | This token is deprecated. Use the corresponding token from the 'md.comp.app-bar' and 'md.comp.app-bar.small' tokens instead. |
| md.comp.top-app-bar.small.centered.container.surface-tint-layer.color | COLOR | Enabled / Container | `md.sys.color.surface-tint` |  | This token is deprecated. Use the corresponding token from the 'md.comp.app-bar' and 'md.comp.app-bar.small' tokens instead. |
| md.comp.top-app-bar.small.centered.on-scroll.container.shadow-color | COLOR | Enabled / Container | `md.sys.color.shadow` |  | Bug: should not have been created. Remove any shadows on navigation bars. |
| md.comp.top-app-bar.small.centered.leading-icon.size | LENGTH | Enabled / Leading icon | 24dp |  | This token is deprecated. Use the corresponding token from the 'md.comp.app-bar' and 'md.comp.app-bar.small' tokens instead. |
| md.comp.top-app-bar.small.centered.leading-icon.color | COLOR | Enabled / Leading icon | `md.sys.color.on-surface` |  | This token is deprecated. Use the corresponding token from the 'md.comp.app-bar' and 'md.comp.app-bar.small' tokens instead. |
| md.comp.top-app-bar.small.centered.trailing-icon.size | LENGTH | Enabled / Trailing icon | 24dp |  | This token is deprecated. Use the corresponding token from the 'md.comp.app-bar' and 'md.comp.app-bar.small' tokens instead. |
| md.comp.top-app-bar.small.centered.trailing-icon.color | COLOR | Enabled / Trailing icon | `md.sys.color.on-surface-variant` |  | This token is deprecated. Use the corresponding token from the 'md.comp.app-bar' and 'md.comp.app-bar.small' tokens instead. |
| md.comp.top-app-bar.small.centered.headline.font | FONT_NAMES | Enabled / Headline | `md.sys.typescale.title-large.font` |  | This token is deprecated. Use the corresponding token from the 'md.comp.app-bar' and 'md.comp.app-bar.small' tokens instead. |
| md.comp.top-app-bar.small.centered.headline.line-height | LINE_HEIGHT | Enabled / Headline | `md.sys.typescale.title-large.line-height` |  | This token is deprecated. Use the corresponding token from the 'md.comp.app-bar' and 'md.comp.app-bar.small' tokens instead. |
| md.comp.top-app-bar.small.centered.headline.size | FONT_SIZE | Enabled / Headline | `md.sys.typescale.title-large.size` |  | This token is deprecated. Use the corresponding token from the 'md.comp.app-bar' and 'md.comp.app-bar.small' tokens instead. |
| md.comp.top-app-bar.small.centered.headline.tracking | FONT_TRACKING | Enabled / Headline | `md.sys.typescale.title-large.tracking` |  | This token is deprecated. Use the corresponding token from the 'md.comp.app-bar' and 'md.comp.app-bar.small' tokens instead. |
| md.comp.top-app-bar.small.centered.headline.weight | FONT_WEIGHT | Enabled / Headline | `md.sys.typescale.title-large.weight` |  | This token is deprecated. Use the corresponding token from the 'md.comp.app-bar' and 'md.comp.app-bar.small' tokens instead. |
| md.comp.top-app-bar.small.centered.headline.type | TYPOGRAPHY | Enabled / Headline | font name: `md.comp.top-app-bar.small.centered.headline.font`, font weight: `md.comp.top-app-bar.small.centered.headline.weight`, font size: `md.comp.top-app-bar.small.centered.headline.size`, font tracking: `md.comp.top-app-bar.small.centered.headline.tracking`, line height: `md.comp.top-app-bar.small.centered.headline.line-height` |  | This token is deprecated. Use the corresponding token from the 'md.comp.app-bar' and 'md.comp.app-bar.small' tokens instead. |
| md.comp.top-app-bar.small.centered.headline.color | COLOR | Enabled / Headline | `md.sys.color.on-surface` |  | This token is deprecated. Use the corresponding token from the 'md.comp.app-bar' and 'md.comp.app-bar.small' tokens instead. |
| md.comp.top-app-bar.small.centered.on-scroll.container.elevation | ELEVATION | Enabled / On scroll | `md.sys.elevation.level2` |  | This token is deprecated. Use the corresponding token from the 'md.comp.app-bar' and 'md.comp.app-bar.small' tokens instead. |

#### [Deprecated] Top app bar - Small (md.comp.top-app-bar.small)

This token set is deprecated. Use `md.comp.app-bar` and `md.comp.app-bar.small` tokens instead. Original tokens for the small top app bar.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.top-app-bar.small.container.color | COLOR | [Deprecated] Enabled / [Deprecated] Container | `md.sys.color.surface` |  | This token is deprecated. Use the corresponding token from the 'md.comp.app-bar' and 'md.comp.app-bar.small' tokens instead. |
| md.comp.top-app-bar.small.on-scroll.container.color | COLOR | [Deprecated] Enabled / [Deprecated] Container | `md.sys.color.surface-container` |  | This token is deprecated. Use the corresponding token from the 'md.comp.app-bar' and 'md.comp.app-bar.small' tokens instead. |
| md.comp.top-app-bar.small.container.shape | SHAPE | [Deprecated] Enabled / [Deprecated] Container | `md.sys.shape.corner.none` |  | This token is deprecated. Use the corresponding token from the 'md.comp.app-bar' and 'md.comp.app-bar.small' tokens instead. |
| md.comp.top-app-bar.small.container.height | LENGTH | [Deprecated] Enabled / [Deprecated] Container | 64dp |  | This token is deprecated. Use the corresponding token from the 'md.comp.app-bar' and 'md.comp.app-bar.small' tokens instead. |
| md.comp.top-app-bar.small.container.elevation | ELEVATION | [Deprecated] Enabled / [Deprecated] Container | `md.sys.elevation.level0` |  | This token is deprecated. Use the corresponding token from the 'md.comp.app-bar' and 'md.comp.app-bar.small' tokens instead. |
| md.comp.top-app-bar.small.container.surface-tint-layer.color | COLOR | [Deprecated] Enabled / [Deprecated] Container | `md.sys.color.surface-tint` |  | Deprecated as part of the update from opacity based surfaces to tonal surfaces. Surfaces no longer use surface-tint layers for tinting, please use the desired surface role directly as the container color. |
| md.comp.top-app-bar.small.on-scroll.container.shadow-color | COLOR | [Deprecated] Enabled / [Deprecated] Container | `md.sys.color.shadow` |  | Bug: should not have been created. Remove any shadows on navigation bars. |
| md.comp.top-app-bar.small.leading-icon.size | LENGTH | [Deprecated] Enabled / [Deprecated] Leading icon | 24dp |  | This token is deprecated. Use the corresponding token from the 'md.comp.app-bar' and 'md.comp.app-bar.small' tokens instead. |
| md.comp.top-app-bar.small.leading-icon.color | COLOR | [Deprecated] Enabled / [Deprecated] Leading icon | `md.sys.color.on-surface` |  | This token is deprecated. Use the corresponding token from the 'md.comp.app-bar' and 'md.comp.app-bar.small' tokens instead. |
| md.comp.top-app-bar.small.trailing-icon.size | LENGTH | [Deprecated] Enabled / [Deprecated] Trailing icon | 24dp |  | This token is deprecated. Use the corresponding token from the 'md.comp.app-bar' and 'md.comp.app-bar.small' tokens instead. |
| md.comp.top-app-bar.small.trailing-icon.color | COLOR | [Deprecated] Enabled / [Deprecated] Trailing icon | `md.sys.color.on-surface-variant` |  | This token is deprecated. Use the corresponding token from the 'md.comp.app-bar' and 'md.comp.app-bar.small' tokens instead. |
| md.comp.top-app-bar.small.headline.font | FONT_NAMES | [Deprecated] Enabled / [Deprecated] Headline | `md.sys.typescale.title-large.font` |  | This token is deprecated. Use the corresponding token from the 'md.comp.app-bar' and 'md.comp.app-bar.small' tokens instead. |
| md.comp.top-app-bar.small.headline.line-height | LINE_HEIGHT | [Deprecated] Enabled / [Deprecated] Headline | `md.sys.typescale.title-large.line-height` |  | This token is deprecated. Use the corresponding token from the 'md.comp.app-bar' and 'md.comp.app-bar.small' tokens instead. |
| md.comp.top-app-bar.small.headline.size | FONT_SIZE | [Deprecated] Enabled / [Deprecated] Headline | `md.sys.typescale.title-large.size` |  | This token is deprecated. Use the corresponding token from the 'md.comp.app-bar' and 'md.comp.app-bar.small' tokens instead. |
| md.comp.top-app-bar.small.headline.tracking | FONT_TRACKING | [Deprecated] Enabled / [Deprecated] Headline | `md.sys.typescale.title-large.tracking` |  | This token is deprecated. Use the corresponding token from the 'md.comp.app-bar' and 'md.comp.app-bar.small' tokens instead. |
| md.comp.top-app-bar.small.headline.weight | FONT_WEIGHT | [Deprecated] Enabled / [Deprecated] Headline | `md.sys.typescale.title-large.weight` |  | This token is deprecated. Use the corresponding token from the 'md.comp.app-bar' and 'md.comp.app-bar.small' tokens instead. |
| md.comp.top-app-bar.small.headline.type | TYPOGRAPHY | [Deprecated] Enabled / [Deprecated] Headline | font name: `md.comp.top-app-bar.small.headline.font`, font weight: `md.comp.top-app-bar.small.headline.weight`, font size: `md.comp.top-app-bar.small.headline.size`, font tracking: `md.comp.top-app-bar.small.headline.tracking`, line height: `md.comp.top-app-bar.small.headline.line-height` |  | This token is deprecated. Use the corresponding token from the 'md.comp.app-bar' and 'md.comp.app-bar.small' tokens instead. |
| md.comp.top-app-bar.small.headline.color | COLOR | [Deprecated] Enabled / [Deprecated] Headline | `md.sys.color.on-surface` |  | This token is deprecated. Use the corresponding token from the 'md.comp.app-bar' and 'md.comp.app-bar.small' tokens instead. |
| md.comp.top-app-bar.small.on-scroll.container.elevation | ELEVATION | [Deprecated] Enabled / [Deprecated] On scroll | `md.sys.elevation.level2` |  | This token is deprecated. Use the corresponding token from the 'md.comp.app-bar' and 'md.comp.app-bar.small' tokens instead. |

### Color

Color values are implemented through design tokens. For designers, this means working with color values that correspond with tokens. In implementation, a color value will be a token that references a value. [Learn more about design tokens](../../foundations/design-tokens/overview.md)

![4 color roles of the medium top app bar in light and dark scheme.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmmaqrta6-18-deprecated-3P.png?alt=media&token=a54e3621-ef99-4168-b3a9-9cdc9606eb71>)

_Medium top app bar color roles used for light and dark schemes: Surface; On surface; On surface; On surface variant_

### Measurements

#### Medium app bar

![Diagram of medium app bar padding and size measurements.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmmaquor6-19-deprecated-3P.png?alt=media&token=f4fa6be1-bfde-4707-8d64-1d868073e86e>)

_Medium app bar padding and size measurements_

#### Large app bar

![Diagram of large app bar padding and size measurements.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmmaqwh2h-20-deprecated-3P.png?alt=media&token=57be919c-7a77-45d1-880e-9afae140b506>)

_Large app bar padding and size measurements_

---
title: "Sliders"
source_url: "https://m3.material.io/components/sliders/specs"
lastmod: "2026-05-08"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "components"
assets:
  - "assets/images/e6ea7ef4d189-ma6a7kht-01-3P.png"
  - "assets/images/661083c87bc1-ma6a86b2-02-3P.png"
  - "assets/images/a4a38e048b81-ma6a8q7u-03-3P.png"
  - "assets/images/fd29369eb6b2-ma6a99lg-04-3p.png"
  - "assets/images/55944bc73cc2-ma6aa21o-05-3P.png"
  - "assets/images/ced01732b171-ma6aaode-06-3P.png"
  - "assets/images/ff884732fc93-ma6abaty-07-3P.png"
  - "assets/images/20eb937ccd2b-ma6absmo-07-3P.png"
---

# Sliders

Sliders let users make selections from a range of values

## Specs

## Variants

![3 variants of sliders.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fma6a7kht-01-3P.png?alt=media&token=1f16d3a6-fc72-447e-83d7-ef38b37ff8fd>)

_Standard; Centered; Range_

| Variant | M3 | M3 Expressive |
| --- | --- | --- |
| Standard | Available as “continuous” slider | Available |
| Centered | Available (web only) | Available |
| Range | Available | Available |
| Discrete | Available | Available as “stops” configuration |

## Configurations

![Orientation and size configurations of sliders.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fma6a86b2-02-3P.png?alt=media&token=fbacd23b-0918-427c-9803-cd4c7050ed9f>)

_Orientation: Horizontal, vertical; Size: XS, S, M, L, XL_

![Optional anatomy configurations of sliders.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fma6a8q7u-03-3P.png?alt=media&token=799e249c-9c5f-496b-b4ce-ef9b61ae2466>)

_Inset icon; Stops; Value indicator_

| Category | Configuration | M3 | M3 Expressive |
| --- | --- | --- | --- |
| Inset icon | No (default) | Available | Available |
| Yes | \-- | Available |  |
| Orientation | Horizontal (default) | Available | Available |
| Vertical | \-- | Available |  |
| Size | XS (default) | Available | Available |
| S, M, L, XL | \-- | Available on Android Views (MDC-Android). Available as tokens on other platforms.\* |  |
| Stop indicators | No (default), Yes | Available as “discrete” slider | Available |
| Value Indicator | No (default), Yes | Available | Available |

> \*Configurations only available using tokens don’t have implemented presets in code. To change the size, swap the default size tokens md.comp.slider.**xsmall**.\[...\] with those of the desired size.

## Tokens & specs

Slider tokens are organized into a common token set, and token sets for each size. Switch token sets from the table’s menu. [Learn more about design tokens](../../foundations/design-tokens/overview.md)

### TOKEN_TABLE

Component: Sliders

Configured context tags: None, Medium contrast, Default, High contrast, Static, 3P, Android, Dark, Light

#### Token sets

| Token set | Name | Type | Tokens | Description |
| --- | --- | --- | --- | --- |
| Slider - Size - Large | md.comp.slider.large | COMPONENT | 7 | For expressive large sliders. |
| Slider - Size - Medium | md.comp.slider.medium | COMPONENT | 7 | For expressive medium sliders. |
| Slider - Size - Small | md.comp.slider.small | COMPONENT | 5 | For expressive small sliders. |
| Slider - Size - Xlarge | md.comp.slider.xlarge | COMPONENT | 7 | For expressive XL sliders. |
| Slider - Size - Xsmall | md.comp.slider.xsmall | COMPONENT | 5 | For expressive XS sliders. |
| Slider | md.comp.slider | COMPONENT | 96 | Common tokens for all expressive slider sizes, and baseline sliders. |

#### Slider - Size - Large (md.comp.slider.large)

For expressive large sliders.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.slider.large.active.track.height | LENGTH | Active track | 56dp |  |  |
| md.comp.slider.large.active.track.shape.leading | LENGTH | Active track | 16dp |  |  |
| md.comp.slider.large.inactive.track.height | LENGTH | Inactive track | 56dp |  |  |
| md.comp.slider.large.inactive.track.shape.trailing | LENGTH | Inactive track | 16dp |  |  |
| md.comp.slider.large.active.handle.height | LENGTH | Handle | 68dp |  |  |
| md.comp.slider.large.icon.padding | LENGTH | Icon | 6dp |  |  |
| md.comp.slider.large.icon.size | LENGTH | Icon | 24dp |  |  |

#### Slider - Size - Medium (md.comp.slider.medium)

For expressive medium sliders.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.slider.medium.active.track.height | LENGTH | Active track | 40dp |  |  |
| md.comp.slider.medium.active.track.shape.leading | LENGTH | Active track | 12dp |  |  |
| md.comp.slider.medium.inactive.track.height | LENGTH | Inactive track | 40dp |  |  |
| md.comp.slider.medium.inactive.track.shape.trailing | LENGTH | Inactive track | 12dp |  |  |
| md.comp.slider.medium.active.handle.height | LENGTH | Handle | 44dp |  |  |
| md.comp.slider.medium.icon.padding | LENGTH | Icon | 6dp |  |  |
| md.comp.slider.medium.icon.size | LENGTH | Icon | 24dp |  |  |

#### Slider - Size - Small (md.comp.slider.small)

For expressive small sliders.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.slider.small.active.track.height | LENGTH | Active track | 24dp |  |  |
| md.comp.slider.small.active.track.shape.leading | LENGTH | Active track | 8dp |  |  |
| md.comp.slider.small.inactive.track.height | LENGTH | Inactive track | 24dp |  |  |
| md.comp.slider.small.inactive.track.shape.trailing | LENGTH | Inactive track | 8dp |  |  |
| md.comp.slider.small.active.handle.height | LENGTH | Handle | 44dp |  |  |

#### Slider - Size - Xlarge (md.comp.slider.xlarge)

For expressive XL sliders.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.slider.xlarge.active.track.height | LENGTH | Active track | 96dp |  |  |
| md.comp.slider.xlarge.active.track.shape.leading | LENGTH | Active track | 28dp |  |  |
| md.comp.slider.xlarge.inactive.track.height | LENGTH | Inactive track | 96dp |  |  |
| md.comp.slider.xlarge.inactive.track.shape.trailing | LENGTH | Inactive track | 28dp |  |  |
| md.comp.slider.xlarge.active.handle.height | LENGTH | Handle | 108dp |  |  |
| md.comp.slider.xlarge.icon.padding | LENGTH | Icon | 8dp |  |  |
| md.comp.slider.xlarge.icon.size | LENGTH | Icon | 32dp |  |  |

#### Slider - Size - Xsmall (md.comp.slider.xsmall)

For expressive XS sliders.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.slider.xsmall.active.track.height | LENGTH | Active track | 16dp |  |  |
| md.comp.slider.xsmall.active.track.shape.leading | LENGTH | Active track | 8dp |  |  |
| md.comp.slider.xsmall.inactive.track.height | LENGTH | Inactive track | 16dp |  |  |
| md.comp.slider.xsmall.inactive.track.shape.trailing | LENGTH | Inactive track | 8dp |  |  |
| md.comp.slider.xsmall.active.handle.height | LENGTH | Handle | 44dp |  |  |

#### Slider (md.comp.slider)

Common tokens for all expressive slider sizes, and baseline sliders.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.slider.stop-indicator.size | LENGTH | Enabled / Stop indicator | 4dp |  |  |
| md.comp.slider.stop-indicator.shape | SHAPE | Enabled / Stop indicator | `md.sys.shape.corner.full` |  |  |
| md.comp.slider.stop-indicator.trailing-space | LENGTH | Enabled / Stop indicator | 4dp |  |  |
| md.comp.slider.stop-indicator.color | COLOR | Enabled / Stop indicator | `md.sys.color.on-secondary-container` |  |  |
| md.comp.slider.stop-indicator.color-selected | COLOR | Enabled / Stop indicator | `md.sys.color.on-primary` |  |  |
| md.comp.slider.with-tick-marks.container.size | LENGTH | Enabled / Container | 2dp |  | Deprecating per b/323475024 |
| md.comp.slider.with-tick-marks.container.shape | SHAPE | Enabled / Container | `md.sys.shape.corner.full` |  | Deprecating per b/323475024 |
| md.comp.slider.with-tick-marks.active.container.color | COLOR | Enabled / Container | `md.sys.color.on-primary` |  | Deprecated for less confusing token name |
| md.comp.slider.active.stop-indicator.container.color | COLOR | Enabled / Container | `md.sys.color.on-primary` |  |  |
| md.comp.slider.with-tick-marks.active.container.opacity | OPACITY | Enabled / Container | 0.38 |  | Deprecated for less confusing token name |
| md.comp.slider.active.stop-indicator.container.opacity | OPACITY | Enabled / Container | 1 |  |  |
| md.comp.slider.with-tick-marks.inactive.container.color | COLOR | Enabled / Container | `md.sys.color.on-surface-variant` |  | Deprecated for less confusing token name |
| md.comp.slider.inactive.stop-indicator.container.color | COLOR | Enabled / Container | `md.sys.color.on-secondary-container` |  |  |
| md.comp.slider.with-tick-marks.inactive.container.opacity | OPACITY | Enabled / Container | 0.38 |  | Deprecated for less confusing token name |
| md.comp.slider.inactive.stop-indicator.container.opacity | OPACITY | Enabled / Container | 1 |  |  |
| md.comp.slider.label.container.height | LENGTH | Enabled / Container | 28dp |  | Deprecating per b/323475024 |
| md.comp.slider.label.container.color | COLOR | Enabled / Container | `md.sys.color.primary` |  | Deprecate per NTC update |
| md.comp.slider.label.container.elevation | ELEVATION | Enabled / Container | `md.sys.elevation.level0` |  | Deprecating per b/323475024 |
| md.comp.slider.active.container.opacity | OPACITY | Enabled / Container | 1 |  | Deprecate per NTC update |
| md.comp.slider.inactive.container.opacity | OPACITY | Enabled / Container | 1 |  | Deprecate per NTC update |
| md.comp.slider.label.label-text.font | FONT_NAMES | Enabled / [Deprecated] Label text | `md.sys.typescale.label-medium.font` |  | Deprecating per b/323475024 |
| md.comp.slider.label.label-text.size | FONT_SIZE | Enabled / [Deprecated] Label text | `md.sys.typescale.label-medium.size` |  | Deprecating per b/323475024 |
| md.comp.slider.label.label-text.line-height | LINE_HEIGHT | Enabled / [Deprecated] Label text | `md.sys.typescale.label-medium.line-height` |  | Deprecating per b/323475024 |
| md.comp.slider.label.label-text.weight | FONT_WEIGHT | Enabled / [Deprecated] Label text | `md.sys.typescale.label-medium.weight` |  | Deprecating per b/323475024 |
| md.comp.slider.label.label-text.tracking | FONT_TRACKING | Enabled / [Deprecated] Label text | `md.sys.typescale.label-medium.tracking` |  | Deprecating per b/323475024 |
| md.comp.slider.label.label-text.color | COLOR | Enabled / [Deprecated] Label text | `md.sys.color.inverse-on-surface` |  | No longer needed |
| md.comp.slider.label.label-text.font-size | FONT_SIZE | Enabled / [Deprecated] Label text | `md.sys.typescale.label-medium.size` |  | For consistency, we have standardized on `size` instead of `font-size`. Replace usage with the suggested token. |
| md.comp.slider.active.track.height | LENGTH | Enabled / Track | 16dp |  |  |
| md.comp.slider.inactive.track.height | LENGTH | Enabled / Track | 16dp |  |  |
| md.comp.slider.active.track.shape | SHAPE | Enabled / Track | `md.sys.shape.corner.full` |  |  |
| md.comp.slider.active.track.outer-corner.corner-size | SHAPE | Enabled / Track | `md.sys.shape.corner.full` |  |  |
| md.comp.slider.active.track.inner-corner.corner-size | SHAPE | Enabled / Track | rounded corners 0 |  |  |
| md.comp.slider.inactive.track.shape | SHAPE | Enabled / Track | `md.sys.shape.corner.full` |  |  |
| md.comp.slider.track.elevation | ELEVATION | Enabled / Track | `md.sys.elevation.level0` |  | Deprecating per b/323475024 |
| md.comp.slider.active.track.color | COLOR | Enabled / Track | `md.sys.color.primary` |  |  |
| md.comp.slider.inactive.track.color | COLOR | Enabled / Track | `md.sys.color.secondary-container` |  |  |
| md.comp.slider.handle.height | LENGTH | Enabled / Handle | 44dp |  |  |
| md.comp.slider.handle.width | LENGTH | Enabled / Handle | 4dp |  |  |
| md.comp.slider.handle.shape | SHAPE | Enabled / Handle | `md.sys.shape.corner.full` |  |  |
| md.comp.slider.handle.elevation | ELEVATION | Enabled / Handle | `md.sys.elevation.level1` |  | Deprecating per b/323475024 |
| md.comp.slider.handle.shadow-color | COLOR | Enabled / Handle | `md.sys.color.shadow` |  | Deprecating per b/323475024 |
| md.comp.slider.handle.color | COLOR | Enabled / Handle | `md.sys.color.primary` |  |  |
| md.comp.slider.slider-active-handle-color | COLOR | Enabled / Handle | `md.sys.color.primary` |  |  |
| md.comp.slider.active.handle.height | LENGTH | Enabled / Handle | 44dp |  |  |
| md.comp.slider.active.handle.width | LENGTH | Enabled / Handle | 4dp |  |  |
| md.comp.slider.with-overlap.handle.outline.color | COLOR | Enabled / Handle | `md.sys.color.on-primary` |  | Deprecating per b/323475024 |
| md.comp.slider.with-overlap.handle.outline.width | LENGTH | Enabled / Handle | 1dp |  | Deprecating per b/323475024 |
| md.comp.slider.active.handle.shape | SHAPE | Enabled / Handle | `md.sys.shape.corner.full` |  |  |
| md.comp.slider.active.handle.leading-space | LENGTH | Enabled / Handle | 6dp |  |  |
| md.comp.slider.active.handle.trailing-space | LENGTH | Enabled / Handle | 6dp |  |  |
| md.comp.slider.active.handle.padding | LENGTH | Enabled / Handle | 6dp |  |  |
| md.comp.slider.label.label-text.type | TYPOGRAPHY | Enabled / [Deprecated] Subhead | font name: `md.comp.slider.label.label-text.font`, font weight: `md.comp.slider.label.label-text.weight`, font size: `md.comp.slider.label.label-text.size`, font tracking: `md.comp.slider.label.label-text.tracking`, line height: `md.comp.slider.label.label-text.line-height` |  | Deprecating per b/298282669 |
| md.comp.slider.state-layer.size | LENGTH | Enabled / [Deprecated] State layer | 40dp |  | Deprecating per b/323475024 |
| md.comp.slider.disabled.stop-indicator.color | COLOR | Disabled / Stop | `md.sys.color.on-surface` |  | Replacing with: Slider disabled inactive stop indicator container color |
| md.comp.slider.disabled.stop-indicator.color-selected | COLOR | Disabled / Stop | `md.sys.color.inverse-on-surface` |  | Replacing with: Slider disabled active stop indicator container color |
| md.comp.slider.disabled.active.stop-indicator.container.color | COLOR | Disabled / Stop | `md.sys.color.inverse-on-surface` |  |  |
| md.comp.slider.disabled.inactive.stop-indicator.container.color | COLOR | Disabled / Stop | `md.sys.color.on-surface` |  |  |
| md.comp.slider.with-tick-marks.disabled.container.color | COLOR | Disabled / [Deprecated] Container | `md.sys.color.on-surface` |  | Deprecated for new token with less confusion |
| md.comp.slider.disabled.stop-indicator.container.color | COLOR | Disabled / [Deprecated] Container | `md.sys.color.on-surface` |  | Replacing with 2 tokens active and inactive stop indicator color |
| md.comp.slider.with-tick-marks.disabled.container.opacity | OPACITY | Disabled / [Deprecated] Container | 0.38 |  | Deprecated for new token with less confusion |
| md.comp.slider.disabled.stop-indicator.container.opacity | OPACITY | Disabled / [Deprecated] Container | 0.38 |  | Replacing with 2 tokens active and inactive stop indicator color |
| md.comp.slider.disabled.active.track.color | COLOR | Disabled / Track | `md.sys.color.on-surface` |  |  |
| md.comp.slider.disabled.active.track.opacity | OPACITY | Disabled / Track | 0.38 |  |  |
| md.comp.slider.disabled.inactive.track.color | COLOR | Disabled / Track | `md.sys.color.on-surface` |  |  |
| md.comp.slider.disabled.inactive.track.opacity | OPACITY | Disabled / Track | 0.12 |  |  |
| md.comp.slider.disabled.handle.color | COLOR | Disabled / Handle | `md.sys.color.on-surface` |  |  |
| md.comp.slider.disabled.handle.elevation | ELEVATION | Disabled / Handle | `md.sys.elevation.level0` |  | Deprecating per b/323475024 |
| md.comp.slider.disabled.handle.opacity | OPACITY | Disabled / Handle | 0.38 |  |  |
| md.comp.slider.disabled.handle.width | LENGTH | Disabled / Handle | 4dp |  |  |
| md.comp.slider.hover.stop.color | COLOR | Hovered / [Deprecated] Stop | `md.sys.color.primary` |  | Deprecate per NTC update |
| md.comp.slider.hover.state-layer.color | COLOR | Hovered / [Deprecated] State layer | `md.sys.color.primary` |  | Deprecating per b/323475024 |
| md.comp.slider.hover.state-layer.opacity | OPACITY | Hovered / [Deprecated] State layer | `md.sys.state.hover.state-layer-opacity` |  | Deprecating per b/323475024 |
| md.comp.slider.hover.handle.color | COLOR | Hovered / Handle | `md.sys.color.primary` |  | Deprecate per NTC update |
| md.comp.slider.hover.handle.width | LENGTH | Hovered / Handle | 4dp |  |  |
| md.comp.slider.focus.active.track.color | COLOR | Focused / Track | `md.sys.color.primary` |  |  |
| md.comp.slider.focus.stop.color | COLOR | Focused / [Deprecated] Stop | `md.sys.color.primary` |  | No longer needed |
| md.comp.slider.focus.inactive.track.color | COLOR | Focused / Track | `md.sys.color.secondary-container` |  |  |
| md.comp.slider.focus.state-layer.color | COLOR | Focused / [Deprecated] State layer | `md.sys.color.primary` |  | Deprecating per b/323475024 |
| md.comp.slider.focus.state-layer.opacity | OPACITY | Focused / [Deprecated] State layer | `md.sys.state.focus.state-layer-opacity` |  | Deprecating per b/323475024 |
| md.comp.slider.focus.handle.color | COLOR | Focused / Handle | `md.sys.color.primary` |  | Deprecating per b/323475024 |
| md.comp.slider.focus.handle.width | LENGTH | Focused / Handle | 2dp |  |  |
| md.comp.slider.pressed.state-layer.color | COLOR | Pressed (ripple) / [Deprecated] State layer | `md.sys.color.primary` |  | Deprecating per b/323475024 |
| md.comp.slider.pressed.state-layer.opacity | OPACITY | Pressed (ripple) / [Deprecated] State layer | `md.sys.state.pressed.state-layer-opacity` |  | Deprecating per b/323475024 |
| md.comp.slider.pressed.handle.color | COLOR | Pressed (ripple) / Handle | `md.sys.color.primary` |  |  |
| md.comp.slider.pressed.handle.width | LENGTH | Pressed (ripple) / Handle | 2dp |  |  |
| md.comp.slider.pressed.active.track.color | COLOR | Pressed (ripple) / Track | `md.sys.color.primary` |  |  |
| md.comp.slider.pressed.inactive.track.color | COLOR | Pressed (ripple) / Track | `md.sys.color.secondary-container` |  |  |
| md.comp.slider.pressed.stop.color | COLOR | Pressed (ripple) / [Deprecated] Stop | `md.sys.color.primary` |  | Deprecating per NTC update |
| md.comp.slider.value-indicator.container.color | COLOR | Pressed (ripple) / Value indicator | `md.sys.color.inverse-surface` |  |  |
| md.comp.slider.value-indicator.label.label-text.font | FONT_NAMES | Pressed (ripple) / Value indicator | `md.sys.typescale.label-large.font` |  |  |
| md.comp.slider.value-indicator.label.label-text.color | COLOR | Pressed (ripple) / Value indicator | `md.sys.color.inverse-on-surface` |  |  |
| md.comp.slider.value-indicator.label.label-text.line-height | LINE_HEIGHT | Pressed (ripple) / Value indicator | `md.sys.typescale.label-large.line-height` |  |  |
| md.comp.slider.value-indicator.label.label-text.size | FONT_SIZE | Pressed (ripple) / Value indicator | `md.sys.typescale.label-large.size` |  |  |
| md.comp.slider.value-indicator.label.label-text.tracking | FONT_TRACKING | Pressed (ripple) / Value indicator | `md.sys.typescale.body-large.tracking` |  |  |
| md.comp.slider.value-indicator.label.label-text.weight | FONT_WEIGHT | Pressed (ripple) / Value indicator | `md.sys.typescale.body-large.weight` |  |  |
| md.comp.slider.value-indicator.active.bottom-space | LENGTH | Pressed (ripple) / Value indicator | 12dp |  |  |

## Anatomy

![6 elements of a slider.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fma6a99lg-04-3p.png?alt=media&token=c1f10fb4-9cb1-4093-8305-6d3df903a22d>)

_Value indicator (optional); Stop indicators (optional); Active track; Handle; Inactive track; Inset icon (optional)_

## Color

![9 color roles of a slider.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fma6aa21o-05-3P.png?alt=media&token=d3fe982f-6f76-4973-aaf6-aa65a59956d3>)

_Slider color roles used for light and dark schemes: Inverse surface; Inverse on surface; Primary; On primary; Primary; Secondary container; On secondary container; On secondary container; On primary_

## States

![5 states of sliders in light and dark schemes.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fma6aaode-06-3P.png?alt=media&token=5b594eb5-38dd-45e4-a53e-2a976e720a09>)

_Enabled; Disabled; Hovered; Focused; Pressed_

## Measurements

![Common slider padding and size measurements.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fma6abaty-07-3P.png?alt=media&token=ecddf256-7f72-45c7-a4b2-64a91b405704>)

_Padding and size measurements for common sliders_

![Slider padding and size measurements at each size configuration, XS to XL.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fma6absmo-07-3P.png?alt=media&token=e564d492-18b4-45d1-8efe-a62a5278e577>)

_Padding and size measurements for XS, S, M, L, and XL sliders_

| Attribute | XS | S | M | L | XL |
| --- | --- | --- | --- | --- | --- |
| Track height | 16dp | 24dp | 40dp | 56dp | 96dp |
| Label container height | 44dp |  |  |  |  |
| Label container width | 48dp |  |  |  |  |
| Handle height | 44dp | 44dp | 52dp | 68dp | 108dp |
| Handle width | 4dp |  |  |  |  |
| Track shape | 8dp | 8dp | 12dp | 16dp | 28dp |
| Inset icon size | \-- | \-- | 24dp | 24dp | 32dp |

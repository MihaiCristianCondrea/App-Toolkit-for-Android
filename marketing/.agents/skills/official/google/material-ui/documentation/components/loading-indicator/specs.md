---
title: "Loading indicator"
source_url: "https://m3.material.io/components/loading-indicator/specs"
lastmod: "2026-02-05"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "components"
assets:
  - "assets/images/afa30094eb0f-ma6gd1jj-1-3P.png"
  - "assets/images/73c9d0b2e263-ma6gdew8-2-3P.png"
  - "assets/images/6c5fc4984e8c-ma6ge8q8-3-3P.png"
  - "assets/images/c16d1b03a350-ma6geonn-4-3P.png"
  - "assets/images/333485562d9c-ma6gf51h-5-3P.png"
  - "assets/images/68b5ade3d1c7-ma6gfgvp-6-3P.png"
---

# Loading indicator

Loading indicators show the progress for a short wait time

## Specs

## Variants

![2 elements of a loading indicator.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fma6gd1jj-1-3P.png?alt=media&token=562f343f-e70c-45a8-bb18-0c86c1f8f300>)

_Loading indicator_

| Variant | M3 | M3 Expressive |
| --- | --- | --- |
| Loading indicator | \-- | Available |

## Configurations

![2 configurations of loading indicators.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fma6gdew8-2-3P.png?alt=media&token=99bc7884-a3c5-4ad5-aca7-dd2513b1aff2>)

_Default; Contained_

| Category | Configuration | M3 | M3 Expressive |
| --- | --- | --- | --- |
| Containment | Default | \-- | Available |
| Contained | \-- | Available |  |

## Tokens & specs

Loading indicators have a single token set.

### TOKEN_TABLE

Component: Loading indicator

Configured context tags: None, Medium contrast, Default, High contrast, 3P, Android, Dark, Light

#### Token sets

| Token set | Name | Type | Tokens | Description |
| --- | --- | --- | --- | --- |
| Loading indicator | md.comp.loading-indicator | COMPONENT | 8 | For loading indicators. |

#### Loading indicator (md.comp.loading-indicator)

For loading indicators.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.loading-indicator.active-indicator.color | COLOR | Color | `md.sys.color.primary` |  |  |
| md.comp.loading-indicator.container.color | COLOR | Color | `md.sys.color.secondary-container` |  | Deprecated in favor of a distinct variant which uses a different color mapping with, and without container. |
| md.comp.loading-indicator.contained.container.color | COLOR | Color | `md.sys.color.primary-container` |  |  |
| md.comp.loading-indicator.contained.active-indicator.color | COLOR | Color | `md.sys.color.on-primary-container` |  |  |
| md.comp.loading-indicator.active-indicator.size | LENGTH | Size | 38dp |  |  |
| md.comp.loading-indicator.container.height | LENGTH | Size | 48dp |  |  |
| md.comp.loading-indicator.container.width | LENGTH | Size | 48dp |  |  |
| md.comp.loading-indicator.container.shape | SHAPE | Shape | `md.sys.shape.corner.full` |  |  |

## Anatomy

![2 elements of a loading indicator.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fma6ge8q8-3-3P.png?alt=media&token=73e297a0-6023-4493-a402-55ed8be14d94>)

_Active indicator; Container_

## Color

### Default

Color values are implemented through design tokens. For designers, this means working with color values that correspond with tokens; in implementation, a color value will be a token that references a value.

![2 color roles of a loading progress indicator in light and dark themes.: the active indicator is primary and the container is secondary container.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fma6geonn-4-3P.png?alt=media&token=20bf0764-7c45-481a-80fc-4b3e07bc0414>)

_Loading indicator color roles used for light and dark schemes: / Primary_

### Contained

![2 color roles of a loading progress indicator in light and dark themes. The active indicator is primary and the container is secondary container.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fma6gf51h-5-3P.png?alt=media&token=7883ee05-b32e-4a72-952e-39a6ed705318>)

_Contained loading indicator color roles used for light and dark schemes: / On primary container; Primary container_

## Measurements

![Loading progress indicator measurements.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fma6gfgvp-6-3P.png?alt=media&token=a0a3c6be-ecd7-4240-ab28-436996abdc95>)

_To ensure sufficient margins, the size is 48dp while the shape container is 38dp_

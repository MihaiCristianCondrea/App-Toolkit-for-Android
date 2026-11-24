---
title: "Badges"
source_url: "https://m3.material.io/components/badges/specs"
lastmod: "2026-02-05"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "components"
assets:
  - "assets/images/827879794a3d-mamlcpos-1-3P.png"
  - "assets/images/e0ba99ad622c-mamlee0o-2-3P.png"
  - "assets/images/7298af6b76bd-mg0yc68n-03-3P.png"
  - "assets/images/eeb1cf35bfd7-lz9ep1tz-4.png"
  - "assets/images/4bae765a51e1-lz9eqf8f-badge_layout-correct.png"
  - "assets/images/3a82d4920907-lz9eqsps-6.png"
---

# Badges

Badges show notifications, counts, or status information on navigation items and icons

## Specs

![5 aspects of badge anatomy on a navigation bar.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmamlcpos-1-3P.png?alt=media&token=915611ea-55ac-48cb-83bf-417ef338e02d>)

_Navigation bar Small badge; Large badge container; Large badge label; Large badge maximum character count container; Large badge maximum character count label_

![5 aspects of badge anatomy on a navigation rail.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmamlee0o-2-3P.png?alt=media&token=456c8e6e-e485-4c1f-87bc-f4185cabe437>)

_Navigation rail Small badge; Large badge container; Large badge label; Large badge maximum character count container; Large badge maximum character count label; /_

## Tokens & specs

Browse the component elements, attributes, tokens, and their values.

### TOKEN_TABLE

Component: Badges

Configured context tags: None, Medium contrast, Default, High contrast, Static, 3P, Android, Dark, Light

#### Token sets

| Token set | Name | Type | Tokens | Description |
| --- | --- | --- | --- | --- |
| Badges | md.comp.badge | COMPONENT | 13 | For badges. |

#### Badges (md.comp.badge)

For badges.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.badge.color | COLOR | Enabled / Container | `md.sys.color.error` |  |  |
| md.comp.badge.shape | SHAPE | Enabled / Container | `md.sys.shape.corner.full` |  |  |
| md.comp.badge.size | LENGTH | Enabled / Container | 6dp |  |  |
| md.comp.badge.large.color | COLOR | Enabled / Container | `md.sys.color.error` |  |  |
| md.comp.badge.large.shape | SHAPE | Enabled / Container | `md.sys.shape.corner.full` |  |  |
| md.comp.badge.large.size | LENGTH | Enabled / Container | 16dp |  |  |
| md.comp.badge.large.label-text.color | COLOR | Enabled / Label text | `md.sys.color.on-error` |  |  |
| md.comp.badge.large.label-text.font | FONT_NAMES | Enabled / Label text | `md.sys.typescale.label-small.font` |  |  |
| md.comp.badge.large.label-text.line-height | LINE_HEIGHT | Enabled / Label text | `md.sys.typescale.label-small.line-height` |  |  |
| md.comp.badge.large.label-text.size | FONT_SIZE | Enabled / Label text | `md.sys.typescale.label-small.size` |  |  |
| md.comp.badge.large.label-text.tracking | FONT_TRACKING | Enabled / Label text | `md.sys.typescale.label-small.tracking` |  |  |
| md.comp.badge.large.label-text.weight | FONT_WEIGHT | Enabled / Label text | `md.sys.typescale.label-small.weight` |  |  |
| md.comp.badge.large.label-text.type | TYPOGRAPHY | Enabled / Label text | font name: `md.comp.badge.large.label-text.font`, font weight: `md.comp.badge.large.label-text.weight`, font size: `md.comp.badge.large.label-text.size`, font tracking: `md.comp.badge.large.label-text.tracking`, line height: `md.comp.badge.large.label-text.line-height` |  |  |

## Color

Color values are implemented through design tokens. For design, this means working with color values that correspond with tokens. For implementation, a color value will be a token that references a value. [Learn more about design tokens](../../foundations/design-tokens/overview.md)

![5 applications of badge color on light and dark theme navigation bars.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmg0yc68n-03-3P.png?alt=media&token=b0199799-0f54-4989-9eb5-9ab7aac1a3d0>)

_Badge color roles used for light and dark schemes in navigation bar: Error; Error; On error; On error; Error_

![5 applications of badge color on light and dark theme navigation rails.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Flz9ep1tz-4.png?alt=media&token=1fe78232-1ccb-4c41-92a7-c1b9ee36df28>)

_Badge color roles used for light and dark schemes in navigation rail: Error; On error; Error; On error; Error; /_

## Measurements

![Annotation of badge sizes, padding, and measurements from the corner of the icon to the badge opposite corner.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Flz9eqf8f-badge_layout-correct.png?alt=media&token=5eef9643-a8a4-4adf-8cb5-1aead3ce6d05>)

_Badge padding and size measurements_

| Attribute | Value |
| --- | --- |
| Small badge shape | 3dp corner radius |
| Small badge size (HxW) | 6dp |
| Large badge shape | 8dp corner radius |
| Large badge one digit size (HxW) | 16dp |
| Large badge max character count size (HxW) | 16x34dp |
| Small badge: distance from top trailing icon corner to bottom leading badge corner (HxW) | 6x6dp |
| Large badge: distance from top trailing icon corner to bottom leading badge corner (HxW) | 14x12dp |
| Large badge padding between badge and text container | 4dp |

## Configuration

Different badges are shown on navigation destinations in various states.

![Diagram of 3 badge variations shown on navigation destinations in various states.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Flz9eqsps-6.png?alt=media&token=d12e4a0d-b307-4c34-8df1-27d87e4a5a4a>)

1.  Inactive with label - small badge
2.  Inactive with label - large badge
3.  Inactive with label - large badge max character count
4.  Inactive - small badge
5.  Inactive - large badge
6.  Inactive - large badge max character count
7.  Active with label - small badge
8.  Active with label - large badge
9.  Active with label - large badge max character count
10.  Active nav bar no label - small badge
11.  Active nav bar no label - large badge
12.  Active nav bar no label - large badge max character count
13.  Active nav rail no label - small badge
14.  Active nav rail no label - large badge
15.  Active nav rail no label - large badge max character count

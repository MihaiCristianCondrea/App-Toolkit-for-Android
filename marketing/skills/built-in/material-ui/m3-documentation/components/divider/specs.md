---
title: "Divider"
source_url: "https://m3.material.io/components/divider/specs"
lastmod: "2026-02-11"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "components"
assets:
  - "assets/images/816bad015123-lvdgvb10-1.png"
  - "assets/images/5b99431ce070-lvdgvshh-2.png"
  - "assets/images/4adc1228b5a3-lvdgwerz-3.png"
---

# Divider

Dividers are thin lines that group content in lists or other containers

## Specs

![Diagram of divider set on horizontal line](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Flvdgvb10-1.png?alt=media&token=33e2a703-65c2-43ed-9d28-0debf4306507>)

_Divider_

## Tokens and specs

Browse the component elements, attributes, tokens, and their values.

### TOKEN_TABLE

Component: Divider

Configured context tags: None, Medium contrast, Default, High contrast, 3P, Android, Dark, Light

#### Token sets

| Token set | Name | Type | Tokens | Description |
| --- | --- | --- | --- | --- |
| Divider | md.comp.divider | COMPONENT | 2 | For dividers. |

#### Divider (md.comp.divider)

For dividers.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.divider.thickness | LENGTH | Enabled / Container | 1dp |  |  |
| md.comp.divider.color | COLOR | Enabled / Container | `md.sys.color.outline-variant` |  |  |

## Color

Color values are implemented through design tokens. For design, this means working with color values that correspond with tokens. For implementation, a color value will be a token that references a value. [Learn more about design tokens](https://m3.material.io/m3/pages/design-tokens/overview/825906c9-6eed-47d1-8812-450910c1356e)

![Divider on light background and dark background.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Flvdgvshh-2.png?alt=media&token=12a3db31-a65a-4668-94bf-7be61e0eeb33>)

_Divider color roles used for light and dark schemes: Outline variant_

## Measurements

![Divider's measurement.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Flvdgwerz-3.png?alt=media&token=3897448c-4415-4e21-985e-3fbe9b50f072>)

_Measurements_

| Attribute | Value |
| --- | --- |
| Divider full-width | 100% |
| Divider inset left margin | 16dp |
| Divider inset right margin | 0dp |
| Divider middle-inset left margin | 16dp |
| Divider middle-inset right margin | 16dp |
| Space between divider & supporting-text | 4dp |
| Divider right margin | 8dp |
| Divider bottom margin | 8dp |

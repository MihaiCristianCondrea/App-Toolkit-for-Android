---
title: "Typography"
source_url: "https://m3.material.io/styles/typography/overview"
lastmod: "2026-05-06"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "styles"
assets:
  - "assets/images/a6467e3e3c53-mac4t99d-1.png"
  - "assets/images/4dc5a9d2ece8-mac5g88o-2.png"
  - "assets/images/d600af88506b-mac5las9-3.png"
  - "assets/images/0696fc10d416-mac5s4nr-4.png"
  - "assets/images/57035cbf0519-mac5vn8t-5.png"
---

# Typography

Use typography to make content readable and beautiful

## Overview

-   M3 type scale has 30 type styles: 15 baseline and 15 emphasized
-   Use variable fonts for more control over expression in editorial treatments
-   Use Material tokens to easily define font, line height, size, tracking, weight, and more

![Type at different scales.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmac4t99d-1.png?alt=media&token=5c81069b-a395-423a-8628-e21378271a22>)

## Availability & resources

This shows where the type scale is available and implemented into Material components.

| Type | Link | Status |
| --- | --- | --- |
| Design | [Design Kit](http://goo.gle/m3-design-kit) | Available |
| [Google Fonts](https://fonts.google.com/) | Available |  |
| Implementation | [Flutter](https://api.flutter.dev/flutter/material/Typography/Typography.material2021.html) | Available |
| [Jetpack Compose](https://developer.android.com/develop/ui/compose/designsystems/material3#typography) | Available |  |
| [Jetpack Compose: Expressive](https://developer.android.com/reference/kotlin/androidx/compose/material3/Typography) | Available |  |
| [Android Views (MDC-Android)](https://github.com/material-components/material-components-android/blob/master/docs/theming/Typography.md) | Available |  |
| [Android Views (MDC-Android): Expressive](https://github.com/material-components/material-components-android/blob/master/docs/theming/Typography.md) | Available |  |
| [Web](https://github.com/material-components/material-web/blob/main/docs/theming/typography.md) | Available |  |
| Web: Expressive | Unavailable |  |

## M3 Expressive update

**May 2025**

### Updated M3 type scale with emphasized styles

Material’s type scale includes fifteen **baseline** type styles, the same as before, and fifteen new **emphasized** type styles. 

The emphasized type styles add more expression to highlighted moments.

Roboto Flex can be used on its own to show a range of emotional states, but is not yet part of the M3 typescale.

[More on how to use emphasized styles](type-scale-tokens.md#0020d4d9-4f5b-4666-b3ce-c26db849bd73)

[More on M3 Expressive](../../blog/building-with-m3-expressive.md)

![Type scale showing roles for “display”, “headline,” “title,” “label,” and “body.”](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmac5g88o-2.png?alt=media&token=e6f292d1-1724-4a58-bd97-4a880f6ee0b2>)

_The expressive type scale includes fifteen baseline type styles and fifteen emphasized type styles_

### Emphasized type style tokens

Design tokens offer an improved way to define typography in products by assigning an element's type style by a configurable value, rather than a set value.

Emphasized tokens allow for clearer hierarchies and prioritized components within a layout.

Type roles describe size—such as small, medium, and large—enabling them to adapt and respond to the device or context.

![Close crop of markdown text showing type scales referenced as “display-large” and “display-large-emphasized.”](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmac5las9-3.png?alt=media&token=dc037127-f767-45bd-b54f-8e0c21af01da>)

_Typography tokens describe scalable size that adapts to devices or settings, including updating the style on boldness_

## Previous updates

### Variable fonts

**Roboto Flex, Roboto Serif, & Roboto Mono**

Updated considerations for using variable fonts and different combinations of their customizable axes, including grade, width, weight, slant, and optical size.

![Roboto Flex, Roboto Mono, and Roboto Serif type rendered in different weights.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmac5s4nr-4.png?alt=media&token=c35d6d6c-ff83-494a-8ec5-addb16c6f331>)

_Roboto Flex, Roboto Serif, and Roboto Mono have a fluid range of axes, like weight, across all optical sizes_

### Style roles

Type styles are defined by five roles: display, headline, title, body, and label.

These names are more descriptive, allowing for easier matching of type style to use case.

![Different examples of Roboto scaled to their respective roles: display, headline, title, body, and label.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmac5vn8t-5.png?alt=media&token=51e80a07-4893-4d93-8b06-b402e90e1164>)

_M3 has five distinct type styles: display, headline, title, body, and label_

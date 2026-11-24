---
title: "FABs"
source_url: "https://m3.material.io/components/floating-action-button/overview"
lastmod: "2026-05-18"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "components"
assets:
  - "assets/images/e1f1bece813d-mkap9gay-01.png"
  - "assets/images/34408369174c-mkc68z96-02.png"
  - "assets/images/2985725d7e6e-mkc69mae-03-3P.png"
  - "assets/images/1782506e2b87-mkc6a5yd-04.png"
---

# FABs

## Overview

-   Use a FAB for the most common or important action on a screen

-   Make sure the icon in a FAB is clear and understandable

-   FABs persist on the screen when content is scrolling

-   Three variants: FAB, medium FAB, large FAB

![The 3 sizes of floating action buttons.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmkap9gay-01.png?alt=media&token=e8e925f7-45d2-4db6-8bfb-31ea3248ae1e>)

_FAB; Medium FAB; Large FAB_

## Availability & resources

### STATUS_TABLE

| Type | Resource | Status | URL |
| --- | --- | --- | --- |
| ANDROID_IMPLEMENTATION | Android Views (MDC-Android): Expressive | AVAILABLE | https://github.com/material-components/material-components-android/blob/master/docs/components/FloatingActionButton.md |
| ANDROID_IMPLEMENTATION | Android Views (MDC-Android) | AVAILABLE | https://github.com/material-components/material-components-android/blob/master/docs/components/FloatingActionButton.md |
| FLUTTER_IMPLEMENTATION | Flutter | AVAILABLE | https://api.flutter.dev/flutter/material/FloatingActionButton-class.html |
| LIT_IMPLEMENTATION | Web: Expressive | UNAVAILABLE |  |
| JETPACK_IMPLEMENTATION | Jetpack Compose: Expressive | AVAILABLE | https://developer.android.com/reference/kotlin/androidx/compose/material3/package-summary#FloatingActionButton(kotlin.Function0,androidx.compose.ui.Modifier,androidx.compose.ui.graphics.Shape,androidx.compose.ui.graphics.Color,androidx.compose.ui.graphics.Color,androidx.compose.material3.FloatingActionButtonElevation,androidx.compose.foundation.interaction.MutableInteractionSource,kotlin.Function0) |
| LIT_IMPLEMENTATION | Web | AVAILABLE | https://github.com/material-components/material-web/blob/main/docs/components/fab.md |
| JETPACK_IMPLEMENTATION | Jetpack Compose | AVAILABLE | https://developer.android.com/develop/ui/compose/components/fab |
| FIGMA | Design Kit (Figma) | AVAILABLE | https://www.figma.com/community/file/1035203688168086460 |

## M3 Expressive update

**May 2025**

The FAB has new sizes to match the extended FAB and more color options. The small FAB is no longer recommended. [More on M3 Expressive](../../blog/building-with-m3-expressive.md)

Variants and naming:

-   Added **medium** FAB size

-   **Small** FAB size is no longer recommended

-   FAB and large FAB sizes are unchanged

-   FAB variants are based on size, not color


Color:

-   Added tone color styles: 

    -   Primary

    -   Secondary

    -   Tertiary

-   Renamed existing tonal color styles to match their token names: 

    -   **Primary** to **Primary container**

    -   **Secondary** to **Secondary container**

    -   **Tertiary** to **Tertiary container**

    -   The values haven't changed

-   Surface color FABs are no longer recommended

![4 FABs showing the colors available after the expressive update.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmkc68z96-02.png?alt=media&token=7cf55ab6-acc3-44b1-8c1d-1291e0357739>)

_FABs have updated colors and sizes_

## Differences from M2

![M2 circular FAB with a plus icon.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmkc69mae-03-3P.png?alt=media&token=afcabba4-1475-4c47-9bbe-1270d37b9a5b>)

_M2: FABs are circles and always have a drop shadow_

![M3 rounded corner square FAB with an artist’s palette icon.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmkc6a5yd-04.png?alt=media&token=c710942d-d229-4b16-8eb1-f72cd22c8753>)

_M3: FABs have a boxier shape, can use dynamic color, and include a new large FAB variation_

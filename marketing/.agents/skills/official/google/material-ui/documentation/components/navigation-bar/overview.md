---
title: "Navigation bar"
source_url: "https://m3.material.io/components/navigation-bar/overview"
lastmod: "2026-05-18"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "components"
assets:
  - "assets/images/d5c1b4b9c501-m0fodjau-01.png"
  - "assets/images/5088cb9ce16a-mae7qe43-02.png"
  - "assets/images/71a3d955e187-m0ddmjvm-03.png"
  - "assets/images/3c2061c63604-mae7pvz8-04.png"
---

# Navigation bar

Navigation bars let people switch between UI views on smaller devices

## Overview

-   Use navigation bars in compact or medium window sizes

-   Can contain 3-5 destinations of equal importance

-   Destinations don't change. They should be consistent across app screens.

![Two navigation bars of different widths with 4 destinations.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0fodjau-01.png?alt=media&token=2a83f14e-464f-4f96-9c01-eb770d98010e>)

_Navigation bar for compact and medium window sizes_

## Availability & resources

### STATUS_TABLE

| Type | Resource | Status | URL |
| --- | --- | --- | --- |
| ANDROID_IMPLEMENTATION | Android Views (MDC-Android): Expressive | AVAILABLE | https://github.com/material-components/material-components-android/blob/master/docs/components/BottomNavigation.md |
| ANDROID_IMPLEMENTATION | Android Views (MDC-Android) | AVAILABLE | https://github.com/material-components/material-components-android/blob/master/docs/components/BottomNavigation.md |
| JETPACK_IMPLEMENTATION | Jetpack Compose | AVAILABLE | https://developer.android.com/develop/ui/compose/components/navigation-bar |
| LIT_IMPLEMENTATION | Web: Expressive | UNAVAILABLE |  |
| LIT_IMPLEMENTATION | Web | UNAVAILABLE |  |
| JETPACK_IMPLEMENTATION | Jetpack Compose: Expressive | AVAILABLE | https://developer.android.com/reference/kotlin/androidx/compose/material3/package-summary#NavigationBar(androidx.compose.ui.Modifier,androidx.compose.ui.graphics.Color,androidx.compose.ui.graphics.Color,androidx.compose.ui.unit.Dp,androidx.compose.foundation.layout.WindowInsets,kotlin.Function1) |
| FLUTTER_IMPLEMENTATION | Flutter | AVAILABLE | https://api.flutter.dev/flutter/material/NavigationBar-class.html |
| FIGMA | Design Kit (Figma) | AVAILABLE | https://www.figma.com/community/file/1035203688168086460 |

## M3 Expressive update

**May 2025**

A new flexible navigation bar was introduced to replace the baseline navigation bar. It’s shorter and supports horizontal navigation items in medium windows. [More on M3 Expressive](../../blog/building-with-m3-expressive.md)

Variants and naming:

-   Baseline navigation bar is no longer recommended

-   Added **flexible** navigation bar

    -   Shorter height

    -   Can be used in medium window sizes with horizontal navigation items


Color:

-   Active label changed from **on-surface-variant** to **secondary**

![Navigation bar in M3 Expressive. It’s shorter than the baseline nav bar.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmae7qe43-02.png?alt=media&token=52cdfa5b-8a10-45d5-af7c-0a92cc899672>)

_The flexible navigation bar is shorter and can be used in medium windows with horizontal nav items_

## Differences from M2

-   Color: New color mappings and compatibility with dynamic color
-   Elevation: No shadow
-   Layout: Container height is taller 
-   States: The active destination can be indicated with a pill shape in a contrasting color
-   Name: Bottom navigation has been renamed **navigation bar**

![M2 nav bar with a drop shadow and no active indicator.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0ddmjvm-03.png?alt=media&token=9e50be86-5460-46f5-90a7-0805ec3c9127>)

_M2: A drop shadow indicates placement on top of content. Filled and regular weight icons indicate active states._

![M3 nav bar with a surface color and active indicator.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmae7pvz8-04.png?alt=media&token=98531a7d-df77-4d8e-a56b-7164db497bd9>)

_M3: Taller and no drop shadow. Filled icons and an active indicator indicate active state._

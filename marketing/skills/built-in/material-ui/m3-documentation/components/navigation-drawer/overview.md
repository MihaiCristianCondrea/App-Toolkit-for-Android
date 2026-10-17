---
title: "Navigation drawer"
source_url: "https://m3.material.io/components/navigation-drawer/overview"
lastmod: "2026-05-18"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "components"
assets:
  - "assets/images/86197053537a-lwoorr6v-1.png"
  - "assets/images/0c4f8699d0d7-ldox2g48-navdrawer_OLD_M2.png"
  - "assets/images/4bef1d4e4ad2-lzdbjtd0-4.png"
---

# Navigation drawer

Navigation drawers let people switch between UI views on larger devices

## Overview

star

Note:

The navigation drawer is no longer recommended in the Material 3 Expressive update. For those who have updated, use an [expanded navigation rail](https://m3.material.io/m3/pages/navigation-rail/overview/), which has mostly the same functionality of the navigation drawer and adapts better across window size classes.

-   Use standard navigation drawers in expanded, large, and extra-large window sizes

-   Use modal navigation drawers in compact and medium window sizes

-   Can be open or closed by default

-   Two variants: standard and modal

-   Put the most frequent destinations at the top and group related destinations together

![2 variants of navigation drawers: standard and modal.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwoorr6v-1.png?alt=media&token=884dcde5-fdd3-438c-9825-1f6668eef908>)

_Standard navigation drawer; Modal navigation drawer_

## Availability & resources

### STATUS_TABLE

| Type | Resource | Status | URL |
| --- | --- | --- | --- |
| ANDROID_IMPLEMENTATION | Android Views (MDC-Android) | AVAILABLE | https://github.com/material-components/material-components-android/blob/master/docs/components/NavigationDrawer.md |
| LIT_IMPLEMENTATION | Web | UNAVAILABLE |  |
| JETPACK_IMPLEMENTATION | Jetpack Compose | AVAILABLE | https://developer.android.com/develop/ui/compose/components/drawer |
| FLUTTER_IMPLEMENTATION | Flutter | AVAILABLE | https://api.flutter.dev/flutter/material/NavigationDrawer-class.html |
| FIGMA | Design Kit (Figma) | AVAILABLE | https://www.figma.com/community/file/1035203688168086460 |

## M3 Expressive update

**May 2025**

The navigation drawer is no longer recommended. Use the expanded navigation rail instead. [More on M3 Expressive](../../blog/building-with-m3-expressive.md)

## Differences from M2

-   Color: New color mappings and compatibility with dynamic color

-   Variants: Distinguishes two separate variants of navigation drawer: Standard and modal

-   Shape: Rounded corners at the ending edge of the drawer

-   States: Updated color and shape for indicating selected state

![M2 navigation drawer with 4 destinations in a mail app. The active destination “Inbox” is rectangular.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fldox2g48-navdrawer_OLD_M2.png?alt=media&token=192ac522-fc4c-4fd2-8fd2-ef6d0f6662e7>)

_M2: Navigation drawer had square corners and a rectangular shape indicating the active destination_

![M3 navigation drawer with 4 destinations in a mail app. The active destination “Inbox” has rounded corners.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flzdbjtd0-4.png?alt=media&token=8621f0a5-d2d3-41b1-bed8-3d5d6c5fdf34>)

_M3: Navigation drawer has rounded corners, new color mappings, and an updated style for indicating the active destination_

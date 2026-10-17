---
title: "FAB menu"
source_url: "https://m3.material.io/components/fab-menu/overview"
lastmod: "2026-05-18"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "components"
assets:
  - "assets/images/eec0e1c35fb3-m0aj37mw-01.png"
  - "assets/images/29fdfb99679e-m0aj3ip6-02.png"
  - "assets/images/ef19a0eadc98-m0aj3w24-Diff-GM2.png"
  - "assets/images/e72b1f989f4e-m0aj42vs-Diff-GM3-Expressive.png"
---

# FAB menu

The floating action button (FAB) menu opens from a FAB to display multiple related actions

## Overview

-   Opens from a FAB to show 2–6 related actions floating on screen
-   One FAB menu size for all sizes of FABs
-   Not used with extended FABs
-   Available in primary, secondary, and tertiary color sets

![3 FAB menus in different color schemes.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0aj37mw-01.png?alt=media&token=1ac9e775-2541-4a63-8818-d76cf8570699>)

_The FAB menu comes in three color sets: primary, secondary, tertiary_

## Availability & resources

### STATUS_TABLE

| Type | Resource | Status | URL |
| --- | --- | --- | --- |
| ANDROID_IMPLEMENTATION | Android Views (MDC-Android): Expressive | UNAVAILABLE |  |
| LIT_IMPLEMENTATION | Web: Expressive | UNAVAILABLE |  |
| JETPACK_IMPLEMENTATION | Jetpack Compose: Expressive | AVAILABLE | https://developer.android.com/reference/kotlin/androidx/compose/material3/package-summary#FloatingActionButtonMenu(kotlin.Boolean,kotlin.Function0,androidx.compose.ui.Modifier,androidx.compose.ui.Alignment.Horizontal,kotlin.Function1) |
| FIGMA | Design Kit (Figma) | AVAILABLE | https://www.figma.com/community/file/1035203688168086460 |

## M3 Expressive update

**May 2025**

The FAB menu adds more options to the FAB. It should replace the speed dial and any usage of stacked small FABs. [More on M3 Expressive](../../blog/building-with-m3-expressive.md)

New component added to catalog:

-   One menu size that pairs with any FAB
-   Replaces any usage of stacked small FABs

Color:

-   Contrasting close button and item colors
-   Supports dynamic color
-   Compatible with any FAB color style

![4 screens. The FAB menu is on the first, and 3 FABs of different sizes are on the others.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0aj3ip6-02.png?alt=media&token=fde56cc4-c285-45ef-9019-faa06da95454>)

_The FAB menu uses contrasting color and large items to focus attention. It can open from any size FAB._

## Differences from M2

![M2 speed dial.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0aj3w24-Diff%20GM2.png?alt=media&token=e358569f-0a63-4ead-a844-ad98804cee2d>)

_M2: The speed dial used small round FABs_

![GM3 FAB menu.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0aj42vs-Diff%20GM3%20Expressive.png?alt=media&token=b0d9f87d-66c0-48f4-9a11-e312b5b207ef>)

_M3: The FAB menu uses dynamic color and a larger item size_

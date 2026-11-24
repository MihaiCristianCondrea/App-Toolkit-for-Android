---
title: "Extended FABs"
source_url: "https://m3.material.io/components/extended-fab/overview"
lastmod: "2026-05-18"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "components"
assets:
  - "assets/images/184b55314ff0-m0df17xu-01.png"
  - "assets/images/6e5c0183c20e-ml9nentp-02.png"
  - "assets/images/cf759af9670c-mae6zhok-04-3P.png"
  - "assets/images/08c26e20fb2e-m0dff6p3-05.png"
---

# Extended FABs

## Overview

-   Use for the most common or important action on a screen

-   Three variants: small, medium, and large

-   Use instead of FAB when label text is needed to understand action

![3 extended fab sizes.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0df17xu-01.png?alt=media&token=06dfec2a-47be-4659-a524-86f136cec764>)

_Small extended FAB; Medium extended FAB; Large extended FAB_

## Availability & resources

### STATUS_TABLE

| Type | Resource | Status | URL |
| --- | --- | --- | --- |
| ANDROID_IMPLEMENTATION | Android Views (MDC-Android): Expressive | AVAILABLE | https://github.com/material-components/material-components-android/blob/master/docs/components/FloatingActionButton.md#extended-fabs |
| ANDROID_IMPLEMENTATION | Android Views (MDC-Android) | AVAILABLE | https://github.com/material-components/material-components-android/blob/master/docs/components/FloatingActionButton.md#extended-fabs |
| FLUTTER_IMPLEMENTATION | Flutter | AVAILABLE | https://api.flutter.dev/flutter/material/FloatingActionButton-class.html |
| JETPACK_IMPLEMENTATION | Jetpack Compose | AVAILABLE | https://developer.android.com/develop/ui/compose/components/fab?hl=en#extended |
| JETPACK_IMPLEMENTATION | Jetpack Compose: Expressive | AVAILABLE | https://developer.android.com/reference/kotlin/androidx/compose/material3/package-summary#ExtendedFloatingActionButton(kotlin.Function0,androidx.compose.ui.Modifier,androidx.compose.ui.graphics.Shape,androidx.compose.ui.graphics.Color,androidx.compose.ui.graphics.Color,androidx.compose.material3.FloatingActionButtonElevation,androidx.compose.foundation.interaction.MutableInteractionSource,kotlin.Function1) |
| LIT_IMPLEMENTATION | Web: Expressive | UNAVAILABLE |  |
| LIT_IMPLEMENTATION | Web | AVAILABLE | https://github.com/material-components/material-web/blob/main/docs/components/fab.md |
| FIGMA | Design Kit (Figma) | AVAILABLE | https://www.figma.com/community/file/1035203688168086460 |

## M3 Expressive update

**May 2025**

The extended FAB now has three sizes: small, medium, and large, each with updated type styles. These align with the FAB sizes for an easier transition between FABs. The baseline extended FAB is no longer recommended and should be replaced with the small extended FAB. Surface and FABs are also no longer recommended. [More on M3 Expressive](../../blog/building-with-m3-expressive.md)

Variants and naming:

-   Added new sizes

    -   Small: 56dp

    -   Medium: 80dp

    -   Large: 96dp

-   No longer recommended

    -   Baseline extended FAB (56dp)

    -   Surface extended FAB


Updates:

-   Adjusted typography to be larger

![The baseline extended FAB and the small, medium, and large extended FABs from the expressive update.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fml9nentp-02.png?alt=media&token=fb7094cc-1fac-45eb-a3f7-a5c683b28ba3>)

_The baseline extended FAB is replaced with a set of small, medium, and large extended FABs with new typography_

## Differences from M2

-   Color: New color mappings and compatibility with dynamic color
-   Layout: Extended FAB is the same height as the FAB
-   Shape: Boxier style with smaller corner radius

![Diagram comparing the M2 FAB and extended FAB.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmae6zhok-04-3P.png?alt=media&token=37880129-5f8d-4a33-a5b2-b8e74634c57b>)

_M2: Extended FABs are pill-shaped and have a different height and elevation_

![Diagram comparing the M3 FAB and extended FAB.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0dff6p3-05.png?alt=media&token=896b601f-21d9-4cae-854f-840ba268dd73>)

_M3: Extended FABs share the same height, boxier shape, and simpler elevation model as FABs_

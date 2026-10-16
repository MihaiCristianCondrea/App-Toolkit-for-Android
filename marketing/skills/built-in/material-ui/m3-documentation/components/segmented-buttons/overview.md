---
title: "Segmented buttons"
source_url: "https://m3.material.io/components/segmented-buttons/overview"
lastmod: "2026-05-18"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "components"
assets:
  - "assets/images/dfcf8e5e672c-lw7qvais-1.png"
  - "assets/images/3e75542db38c-lwhr0fyz-2.png"
  - "assets/images/4d97025b1709-l20oj7ju-example.png"
  - "assets/images/a937b9181611-lw7ruslm-4.png"
---

# Segmented buttons

Segmented buttons help people select options, switch views, or sort elements

## Overview

star

Note:

Segmented buttons are no longer recommended in the Material 3 expressive update. For those who have updated, use the [connected button group](https://m3.material.io/m3/pages/button-groups/overview/) instead, which has mostly the same functionality but with an updated visual design.

-   Segmented buttons can contain icons, label text, or both

-   Two variants: single-select and multi-select

-   Use for simple choices between two to five items (for more items or complex choices, use chips)

![Two variants of segmented buttons.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flw7qvais-1.png?alt=media&token=52a09b72-85ab-4f14-93a4-0738235fef83>)

_Single-select segmented button; Multi-select segmented button_

## Availability & resources

### STATUS_TABLE

| Type | Resource | Status | URL |
| --- | --- | --- | --- |
| ANDROID_IMPLEMENTATION | Android Views (MDC-Android) | AVAILABLE | https://github.com/material-components/material-components-android/blob/master/docs/components/Button.md#toggle-button |
| LIT_IMPLEMENTATION | Web | UNAVAILABLE |  |
| FLUTTER_IMPLEMENTATION | Flutter | AVAILABLE | https://api.flutter.dev/flutter/material/SegmentedButton-class.html |
| JETPACK_IMPLEMENTATION | Jetpack Compose | AVAILABLE | https://developer.android.com/develop/ui/compose/components/segmented-button |
| FIGMA | Design Kit (Figma) | AVAILABLE | https://www.figma.com/community/file/1035203688168086460 |

## M3 Expressive update

**May 2025**

The segmented button is no longer recommended. Use the [connected button group](https://m3.material.io/m3/pages/button-groups/overview/) instead. [More on M3 Expressive](../../blog/building-with-m3-expressive.md)

## Differences from M2

-   **Color:** New color mappings and compatibility with dynamic color

-   **Icons:** Optional check icon to indicate selected state

-   **Layout:** Taller container height of 40dp

-   **Name and variants:** Segmented buttons were previously known as toggle buttons. They now have two official variants: single-select and multi-select.

-   **Shape:** Fully rounded corners

-   **Typography:** Labels use sentence case instead of all caps

![Diagram indicating the fully rounded corner radius of a segmented button.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwhr0fyz-2.png?alt=media&token=246e78a8-1e67-45c6-a7b7-552681fce036>)

_Segmented buttons now have a container height of 40dp_

![Segmented buttons with M2 color mappings, all caps text labels, boxy shape, and shorter height.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fl20oj7ju-example.png?alt=media&token=9a695831-7fd8-4af8-ad34-b16a30ee5e32>)

_M2: Segmented buttons had a small corner radius and label text in all caps_

![Segmented buttons with M3 color mappings, sentence case text labels, fully round shape, and taller height.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flw7ruslm-4.png?alt=media&token=f5efc802-2c9c-4a7d-98f6-1e92ef9b4c76>)

_M3: Segmented buttons have fully rounded corners, sentence-case text, different height, and new color mappings_

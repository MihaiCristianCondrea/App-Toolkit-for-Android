---
title: "Sliders"
source_url: "https://m3.material.io/components/sliders/overview"
lastmod: "2026-05-08"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "components"
assets:
  - "assets/videos/b2681e7e2460-m7ll9aj1-GM3_Expressive_Slider_Overview_01_IA_V01.mp4"
  - "assets/images/34a17fb72cd2-m7lii3kv-02.png"
  - "assets/images/6604484a6dab-m7lirx37-03.png"
  - "assets/images/473523e04531-m7livqfr-04.png"
  - "assets/images/804b7e48b94f-m7liwct9-05.png"
---

# Sliders

Sliders let users make selections from a range of values

## Overview

-   Three variants: Standard, centered, range

-   Has five sizes, vertical and horizontal orientation, and an optional inset icon

-   Sliders should present the full range of available values

-   The slider value should take effect immediately

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm7ll9aj1-GM3_Expressive_Slider_Overview_01_IA_V01.mp4?alt=media&amp;token=e9b46e6a-d3c9-4b05-bcec-725447cda4e6" title="A vertical slider changes the brightness of bedroom lights."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm7ll9aj1-GM3_Expressive_Slider_Overview_01_IA_V01.mp4?alt=media&token=e9b46e6a-d3c9-4b05-bcec-725447cda4e6>)

## Availability & resources

### STATUS_TABLE

| Type | Resource | Status | URL |
| --- | --- | --- | --- |
| ANDROID_IMPLEMENTATION | Android Views (MDC-Android): Expressive | AVAILABLE | https://github.com/material-components/material-components-android/blob/master/docs/components/Slider.md |
| ANDROID_IMPLEMENTATION | Android Views (MDC-Android) | AVAILABLE | https://github.com/material-components/material-components-android/blob/master/docs/components/Slider.md |
| JETPACK_IMPLEMENTATION | Jetpack Compose: Expressive | AVAILABLE | https://developer.android.com/reference/kotlin/androidx/compose/material3/package-summary#Slider(androidx.compose.material3.SliderState,androidx.compose.ui.Modifier,kotlin.Boolean,androidx.compose.material3.SliderColors,androidx.compose.foundation.interaction.MutableInteractionSource,kotlin.Function1,kotlin.Function1) |
| LIT_IMPLEMENTATION | Web: Expressive | UNAVAILABLE |  |
| LIT_IMPLEMENTATION | Web | AVAILABLE | https://github.com/material-components/material-web/blob/main/docs/components/slider.md |
| JETPACK_IMPLEMENTATION | Jetpack Compose | AVAILABLE | https://developer.android.com/develop/ui/compose/components/slider |
| FLUTTER_IMPLEMENTATION | Flutter | AVAILABLE | https://api.flutter.dev/flutter/material/Slider-class.html |
| FIGMA | Design Kit (Figma) | AVAILABLE | https://www.figma.com/community/file/1035203688168086460 |

## M3 Expressive update

**May 2025**

The slider includes expressive configurations for orientation, shape sizes, and an inset icon. [More on M3 Expressive](../../blog/building-with-m3-expressive.md)

Updated on Android Views (MDC-Android) and Jetpack Compose.

Variants and naming: 

-   Changed **continuous** slider to **standard** slider

-   The **discrete** slider is now the **stops** configuration


New configurations: 

-   Orientation: Horizontal, vertical

-   Optional inset icon (standard slider only)

-   Sizes: XS (existing default), S, M, L, XL

![3 M3 Expressive sliders.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm7lii3kv-02.png?alt=media&token=315a4297-8491-4398-9ceb-ee02c42bce76>)

_Standard slider; Centered slider; Range slider_

## Previous updates

### Visual refresh to improve non-text contrast

**Dec 2023:** Updated on Android Views (MDC-Android) and Jetpack Compose.

-   **Configuration:** Added centered configuration and range selection

-   **Shape:** New shape for slider tracks and handles. Slider elements change shape when selected.

-   **Motion:** Slider handle adjusts width upon selection. Slider tracks adjust in shape when sliding to the edge.

-   **Color:** Refreshed color mappings

![M3 visually-refreshed slider.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm7lirx37-03.png?alt=media&token=938a41c7-a272-4f54-b226-a138dbc77dbd>)

_M3 visual refresh: Sliders have a stop indicator, larger label text, and a vertical handle that narrows when pressed. Centered sliders start from the middle instead of the leading edge._

## Differences from M2

-   **Color**: New color mappings and compatibility with dynamic color

![M2 slider.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm7livqfr-04.png?alt=media&token=04f22602-6e80-4772-84e6-e36473774b80>)

_M2: Sliders have a circular handle and a small label when pressed_

![Original M3 slider.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm7liwct9-05.png?alt=media&token=92017b6d-1e41-4639-9a31-6e78d744afe0>)

_M3: Sliders have new color mappings and support dynamic color_

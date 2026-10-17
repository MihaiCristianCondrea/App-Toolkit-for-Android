---
title: "Progress indicators"
source_url: "https://m3.material.io/components/progress-indicators/overview"
lastmod: "2026-02-27"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "components"
assets:
  - "assets/images/0586462c6a91-mleotj2q-01.png"
  - "assets/videos/899179fdc74b-m0qvxgu6-Progress-Indicator-Overview.mp4"
  - "assets/images/bf6aa274ce90-mleov4bo-03.png"
  - "assets/images/e27837adebbd-m0c2if7i-04.png"
  - "assets/images/a4d36b71d6be-m0c2jf26-05.png"
---

# Progress indicators

## Overview

-   Two variants: linear and circular

-   Use the same configuration for all instances of a process (like loading)

-   They capture attention through motion

-   Option to apply a wave to the active track for use cases that would benefit from increased expressiveness

![8 progress indicators configured to show different thickness and shape.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmleotj2q-01.png?alt=media&token=8d1f2405-86ed-4634-8847-af33a549410d>)

_Linear and circular progress indicators have visual configurations for shape and thickness_

## Availability & resources

### STATUS_TABLE

| Type | Resource | Status | URL |
| --- | --- | --- | --- |
| ANDROID_IMPLEMENTATION | Android Views (MDC-Android): Expressive | AVAILABLE | https://github.com/material-components/material-components-android/blob/master/docs/components/ProgressIndicator.md |
| ANDROID_IMPLEMENTATION | Android Views (MDC-Android) | AVAILABLE | https://github.com/material-components/material-components-android/blob/master/docs/components/ProgressIndicator.md |
| JETPACK_IMPLEMENTATION | Jetpack Compose: Expressive | AVAILABLE | https://developer.android.com/reference/kotlin/androidx/compose/material3/package-summary#LinearWavyProgressIndicator(androidx.compose.ui.Modifier,androidx.compose.ui.graphics.Color,androidx.compose.ui.graphics.Color,androidx.compose.ui.graphics.drawscope.Stroke,androidx.compose.ui.graphics.drawscope.Stroke,androidx.compose.ui.unit.Dp,kotlin.Float,androidx.compose.ui.unit.Dp,androidx.compose.ui.unit.Dp) |
| LIT_IMPLEMENTATION | Web: Expressive | UNAVAILABLE |  |
| LIT_IMPLEMENTATION | Web | AVAILABLE | https://github.com/material-components/material-web/blob/main/docs/components/progress.md |
| JETPACK_IMPLEMENTATION | Jetpack Compose | AVAILABLE | https://developer.android.com/develop/ui/compose/components/progress |
| FLUTTER_IMPLEMENTATION | Flutter | AVAILABLE | https://api.flutter.dev/flutter/material/ThemeData/useMaterial3.html |
| FIGMA | Design Kit (Figma) | AVAILABLE | https://www.figma.com/community/file/1035203688168086460 |

## M3 Expressive update

**Aug 2024**

The progress indicators have configurations for height and wavy shape. Choose the visual style that best fits your product. [More on M3 Expressive](../../blog/building-with-m3-expressive.md)

-   Track height: Configurable
-   Shape: Wavy

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0qvxgu6-Progress-Indicator-Overview.mp4?alt=media&amp;token=2131e37c-feab-4c1c-b117-16ad223b0078" title="Progress indicators used when loading a page and for processing a payment."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0qvxgu6-Progress-Indicator-Overview.mp4?alt=media&token=2131e37c-feab-4c1c-b117-16ad223b0078>)

## Previous updates

**Dec 2023: Non-text contrast (NTC)**

-   Anatomy: Added an end stop indicator to improve accessibility
-   Contrast: Higher contrast between track and active indicator to enhance the perception of progress
-   Motion: New motion behavior
-   Shape: Rounded corners

![GM3 linear and circular progress indicators](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmleov4bo-03.png?alt=media&token=ec54810c-b325-4dd1-a547-b66d15d3b260>)

_Progress indicators have a new rounded, colorful style_

## Differences from M2

**July 2022: Added to Material 3**

-   **Color:** New color mappings and compatibility with dynamic color

![M2 linear and circular progress indicators.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0c2if7i-04.png?alt=media&token=2cccabb4-c40c-4db4-9321-51c34cae3d25>)

_M2: Progress indicators have a boxier, neutral style_

![M3 linear and circular progress indicators.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0c2jf26-05.png?alt=media&token=2ee132f7-2c47-46e2-8826-910fbdfe4547>)

_M3: Progress indicators are compatible with dynamic color_

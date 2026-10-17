---
title: "Loading indicator"
source_url: "https://m3.material.io/components/loading-indicator/overview"
lastmod: "2026-02-05"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "components"
assets:
  - "assets/videos/929ee2bfca47-m0fcl84h-Loading-Indicator-Overview-A-Sync.mp4"
  - "assets/videos/e718edf163d9-m0fcq8u9-Loading-Indicator-Overview-B.mp4"
---

# Loading indicator

Loading indicators show the progress for a short wait time

## Overview

-   Recommended as a replacement for indeterminate circular progress indicators

-   Always reflect an ongoing process and are never simply decorative

-   Used for pull-to-refresh interactions

-   Not used for processes that transition from indeterminate to determinate  

-   Capture attention through motion

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0fcl84h-Loading-Indicator-Overview-A-Sync.mp4?alt=media&amp;token=d75aa987-4751-44cf-8013-2db500096066" title="A looping sequence of morphs between 7 different shapes in a darker color, sitting on a circular background container in lighter color."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0fcl84h-Loading-Indicator-Overview-A-Sync.mp4?alt=media&token=d75aa987-4751-44cf-8013-2db500096066>)

## Availability & resources

### STATUS_TABLE

| Type | Resource | Status | URL |
| --- | --- | --- | --- |
| ANDROID_IMPLEMENTATION | Android Views (MDC-Android): Expressive | AVAILABLE | https://github.com/material-components/material-components-android/blob/master/docs/components/LoadingIndicator.md |
| LIT_IMPLEMENTATION | Web: Expressive | UNAVAILABLE |  |
| JETPACK_IMPLEMENTATION | Jetpack Compose: Expressive | AVAILABLE | https://developer.android.com/reference/kotlin/androidx/compose/material3/package-summary#LoadingIndicator(androidx.compose.ui.Modifier,androidx.compose.ui.graphics.Color,kotlin.collections.List) |
| FIGMA | Design Kit (Figma) | AVAILABLE | https://www.figma.com/community/file/1035203688168086460 |

## M3 Expressive update

**May 2025**

The loading indicator is designed to show progress that loads in under five seconds. It should replace most uses of the indeterminate circular progress indicator. [More on M3 Expressive](../../blog/building-with-m3-expressive.md)

New component added to catalog.

Loading indicators:

-   Are used in pull-to-refresh functionality

-   Can be contained or uncontained

-   Use shape and motion to capture attention

-   Can scale in size

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0fcq8u9-Loading-Indicator-Overview-B.mp4?alt=media&amp;token=e6966a82-22f1-46ac-82c8-2597e2815244" title="Loading indicator in news app with pull-to-refresh."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0fcq8u9-Loading-Indicator-Overview-B.mp4?alt=media&token=e6966a82-22f1-46ac-82c8-2597e2815244>)

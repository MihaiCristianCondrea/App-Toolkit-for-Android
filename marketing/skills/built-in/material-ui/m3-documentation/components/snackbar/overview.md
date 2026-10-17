---
title: "Snackbar"
source_url: "https://m3.material.io/components/snackbar/overview"
lastmod: "2026-05-18"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "components"
assets:
  - "assets/images/65d60b9bf854-lwow6ukp-1.png"
  - "assets/images/11921ba0fcd8-lwow95bc-2.png"
---

# Snackbar

Snackbars show short updates about app processes at the bottom of the screen

## Overview

-   Snackbars shouldn’t interrupt the user’s experience 
-   Usually appear at the bottom of the UI
-   Can disappear on their own or remain on screen until the user takes action

![Diagram of snackbar placement](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwow6ukp-1.png?alt=media&token=24ee05cc-c58f-4ecb-8348-804d66e04ab4>)

## Availability & resources

### STATUS_TABLE

| Type | Resource | Status | URL |
| --- | --- | --- | --- |
| ANDROID_IMPLEMENTATION | Android Views (MDC-Android) | AVAILABLE | https://github.com/material-components/material-components-android/blob/master/docs/components/Snackbar.md |
| LIT_IMPLEMENTATION | Web | UNAVAILABLE |  |
| JETPACK_IMPLEMENTATION | Jetpack Compose | AVAILABLE | https://developer.android.com/develop/ui/compose/components/snackbar |
| FLUTTER_IMPLEMENTATION | Flutter | AVAILABLE | https://api.flutter.dev/flutter/material/SnackBar-class.html |
| FIGMA | Design Kit (Figma) | AVAILABLE | https://www.figma.com/community/file/1035203688168086460 |

## Differences from M2

-   Color: New color mappings and compatibility with dynamic color
-   Behavior: Clarified that snackbars can either appear temporarily (dismissive) or persist until the user takes an action (non-dismissive)

![Example of snackbar on screen bottom](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwow95bc-2.png?alt=media&token=6719d9ee-ca40-4021-8b1c-1e07c2a88258>)

_Snackbars have new color mappings_

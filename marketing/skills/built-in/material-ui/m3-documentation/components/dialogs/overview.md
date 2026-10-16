---
title: "Dialogs"
source_url: "https://m3.material.io/components/dialogs/overview"
lastmod: "2026-05-18"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "components"
assets:
  - "assets/images/ab5c82b2661e-ml9yk7iw-01.png"
  - "assets/images/1d5d6a44a88e-ml9ykeuk-02.png"
---

# Dialogs

Dialogs provide important prompts in a user flow

## Overview

-   Use dialogs to make sure users act on information

-   Two variants: basic and full-screen

-   Should be dedicated to completing a single task

-   Can also display information relevant to the task

-   Commonly used to confirm high-risk actions like deleting progress

![Basic and full-screen dialog.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fml9yk7iw-01.png?alt=media&token=50a39047-4d06-477e-a9d8-84dd6a5afae9>)

_Basic dialog; Full-screen dialog_

## Availability & resources

### STATUS_TABLE

| Type | Resource | Status | URL |
| --- | --- | --- | --- |
| ANDROID_IMPLEMENTATION | Android Views (MDC-Android) | AVAILABLE | https://github.com/material-components/material-components-android/blob/master/docs/components/Dialog.md |
| LIT_IMPLEMENTATION | Web | AVAILABLE | https://github.com/material-components/material-web/blob/main/docs/components/dialog.md |
| JETPACK_IMPLEMENTATION | Jetpack Compose | AVAILABLE | https://developer.android.com/develop/ui/compose/components/dialog |
| FIGMA | Design Kit (Figma) | AVAILABLE | https://www.figma.com/community/file/1035203688168086460 |
| FLUTTER_IMPLEMENTATION | Flutter | AVAILABLE | https://api.flutter.dev/flutter/material/ThemeData/useMaterial3.html |

## Differences from M2

-   Color: New color mappings and compatibility with dynamic color
-   Layout: Greater padding to account for the increased corner-radius and title size
-   Position: Option for custom basic dialog positioning
-   Shape: Increased corner-radius
-   Typography: Larger and darker headline

![Basic dialog with rounded corner, larger headline.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fml9ykeuk-02.png?alt=media&token=02244cb3-7889-4321-9a65-aa3040bbadc1>)

_New updates to color, layout, position, shape, and typography_

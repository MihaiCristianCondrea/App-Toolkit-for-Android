---
title: "Icon buttons"
source_url: "https://m3.material.io/components/icon-buttons/overview"
lastmod: "2026-02-11"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "components"
assets:
  - "assets/images/df11b411f4a6-m0by1ftf-1.png"
  - "assets/images/61f66c93a7bf-m0by3xdg-2.png"
  - "assets/images/0d9841ac47fc-m0by5hfz-3.png"
---

# Icon buttons

Icon buttons help people take minor actions with one tap

## Overview

-   Icon buttons must use a system icon with a clear meaning

-   Two variants: default and toggle

-   Many configurations: Color, size, width, and shape

-   On web, display a tooltip describing the action while hovering

-   In toggle buttons, use the outlined style of an icon for the unselected state, and the filled style for the selected state

![5 kinds of outline buttons.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0by1ftf-1.png?alt=media&token=2888d586-c4ee-456e-8444-805543dddbed>)

_Standard, filled unselected, filled selected, filled tonal, and outlined icon buttons_

## Availability & resources

### STATUS_TABLE

| Type | Resource | Status | URL |
| --- | --- | --- | --- |
| ANDROID_IMPLEMENTATION | Android Views (MDC-Android): Expressive | AVAILABLE | https://github.com/material-components/material-components-android/blob/master/docs/components/Button.md#icon-button |
| ANDROID_IMPLEMENTATION | Android Views (MDC-Android) | AVAILABLE | https://github.com/material-components/material-components-android/blob/master/docs/components/Button.md#icon-button |
| JETPACK_IMPLEMENTATION | Jetpack Compose | AVAILABLE | https://developer.android.com/develop/ui/compose/components/icon-button |
| LIT_IMPLEMENTATION | Web: Expressive | UNAVAILABLE |  |
| JETPACK_IMPLEMENTATION | Jetpack Compose: Expressive | AVAILABLE | https://developer.android.com/reference/kotlin/androidx/compose/material3/package-summary#IconButton(kotlin.Function0,androidx.compose.ui.Modifier,kotlin.Boolean,androidx.compose.material3.IconButtonColors,androidx.compose.foundation.interaction.MutableInteractionSource,androidx.compose.ui.graphics.Shape,kotlin.Function0) |
| LIT_IMPLEMENTATION | Web | AVAILABLE | https://github.com/material-components/material-web/blob/main/docs/components/icon-button.md |
| FLUTTER_IMPLEMENTATION | Flutter | AVAILABLE | https://api.flutter.dev/flutter/material/IconButton-class.html |
| FIGMA | Design Kit (Figma) | AVAILABLE | https://www.figma.com/community/file/1035203688168086460 |

## M3 Expressive update

**May 2025**

Icon buttons now have a wider variety of shapes and sizes, changing shape when selected. When placed in button groups, icon buttons interact with each other when pressed. [More on M3 Expressive](../../blog/building-with-m3-expressive.md)

Variants and naming:

-   Default and toggle (selection)

-   Color styles are now configurations. (filled, tonal, outlined, standard)


Shapes:

-   Round and square options

-   Shape morphs when pressed

-   Shape morphs when selected


Sizes:

-   Extra small

-   Small (default)

-   Medium

-   Large

-   Extra large


Widths: 

-   Narrow

-   Default

-   Wide

![Icon buttons can vary in size, shape, and width.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0by3xdg-2.png?alt=media&token=0e9b026c-d4c0-4e94-ad89-55a59618a51d>)

_Five sizes; Two shapes; Three widths_

## Differences from M2

-   **Color:** New color mappings and compatibility with dynamic color

-   **Variants and naming:** Icon buttons were called toggle buttons. There are now two variants of icon buttons: default and toggle.

![Icon buttons were known as toggle buttons in M2.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0by5hfz-3.png?alt=media&token=9b34e493-6c33-4b8e-aa41-93e8045c9952>)

_Default icon buttons; Toggle icon buttons_

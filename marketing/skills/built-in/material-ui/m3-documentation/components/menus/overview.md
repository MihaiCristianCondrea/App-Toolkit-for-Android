---
title: "Menus"
source_url: "https://m3.material.io/components/menus/overview"
lastmod: "2026-05-18"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "components"
assets:
  - "assets/images/2ab9b671715b-mhkajj6i-01.png"
  - "assets/images/52a9b7732473-mhk9jsmw-02.png"
  - "assets/images/aacd90920af6-mi5n1krh-4.png"
  - "assets/images/43cb3ceeeb2d-mhlt7xid-04.png"
---

# Menus

## Overview

-   Use a **menu** to show a temporary set of actions. To show actions on screen at all times, use a **toolbar** instead
-   Menus can open from many components, including icon buttons, split buttons, and text fields
-   **Context menus** provide actions for a specific element, like an image or highlighted text, and usually open with a secondary click

![1 vertical menu with vibrant colors opens from a split button, and 1 vertical menu with a submenu.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmhkajj6i-01.png?alt=media&token=b7e5017f-ef26-4526-8d0c-759c47445705>)

_Vertical menus can include vibrant colors, gaps, dividers, and submenus to organize a list of choices_

## Availability & resources

### STATUS_TABLE

| Type | Resource | Status | URL |
| --- | --- | --- | --- |
| ANDROID_IMPLEMENTATION | Android Views (MDC-Android): Expressive | UNAVAILABLE |  |
| ANDROID_IMPLEMENTATION | Android Views (MDC-Android) | AVAILABLE | https://github.com/material-components/material-components-android/blob/master/docs/components/Menu.md |
| JETPACK_IMPLEMENTATION | Jetpack Compose: Expressive | AVAILABLE | https://developer.android.com/reference/kotlin/androidx/compose/material3/package-summary#DropdownMenuGroup%28androidx.compose.material3.MenuGroupShapes,androidx.compose.ui.Modifier,androidx.compose.ui.graphics.Color,androidx.compose.ui.unit.Dp,androidx.compose.ui.unit.Dp,androidx.compose.foundation.BorderStroke,androidx.compose.foundation.layout.PaddingValues,androidx.compose.foundation.interaction.MutableInteractionSource,kotlin.Function1%29 |
| LIT_IMPLEMENTATION | Web | AVAILABLE | https://github.com/material-components/material-web/blob/main/docs/components/menu.md |
| WIZ_IMPLEMENTATION | Web: Expressive | UNAVAILABLE |  |
| FIGMA | Design Kit (Figma) | AVAILABLE | https://www.figma.com/community/file/1035203688168086460 |
| JETPACK_IMPLEMENTATION | Jetpack Compose | AVAILABLE | https://developer.android.com/develop/ui/compose/components/menu |
| FLUTTER_IMPLEMENTATION | Flutter | AVAILABLE | https://api.flutter.dev/flutter/material/ThemeData/useMaterial3.html |

## M3 Expressive update

**November 2025**

**Vertical menus** were introduced with new shapes, color styles, selection states, and refined submenu motion. Gaps can be used for a more flexible layout on Android. [More on M3 Expressive](../../blog/building-with-m3-expressive.md)

Variants:

-   Added **vertical menus**, recommended for new designs
-   Baseline **menu** is still available  

Color styles: 

-   Standard
-   Vibrant

![A vertical menu using shape and vibrant color to show a selected state.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmhk9jsmw-02.png?alt=media&token=3e48c83b-95fa-49ff-b870-925785fb4a04>)

_Vibrant colors help selected menu items stand out_

## Differences from M2

-   **Color**: New color mappings and compatibility with dynamic color
-   **Variants**: Dropdown menu and exposed dropdown menu are now both referred to as menu, since they differ only in the element which opens the menu surface

![Menu with gray color.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmi5n1krh-4.png?alt=media&token=64d1bd04-5d8f-4ac4-9ed8-83081bf03bc8>)

_M2: Former menu colors don’t contrast with the background_

![Menu with purple background and outline.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmhlt7xid-04.png?alt=media&token=10f6b199-9664-4a24-b811-2980270f499c>)

_M3: Menus feature new color mappings and dynamic color_

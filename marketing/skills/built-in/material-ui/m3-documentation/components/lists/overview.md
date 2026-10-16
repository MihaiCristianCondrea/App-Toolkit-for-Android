---
title: "Lists"
source_url: "https://m3.material.io/components/lists/overview"
lastmod: "2026-05-18"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "components"
assets:
  - "assets/images/74fff6d874e2-miq1b43h-frame.png"
  - "assets/images/63b9796f40c1-mi8nxjh0-01.png"
  - "assets/images/890a074f84e2-mi8pwbjv-02.png"
  - "assets/images/f13ab1b086a1-mi8q1tdh-03.png"
  - "assets/images/8027a26ba8ba-mi8q2xsp-04.png"
---

# Lists

Lists are continuous, vertical indexes of text or images.

![Lists](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmiq1b43h-frame.png?alt=media&token=fc752db1-dd0a-4ab3-95fa-46598de9cffe>)

## Overview

-   Use lists to help people find a specific item and act on it

-   Order list items in logical ways, like alphabetical or numerical

-   Keep items short and easy to scan

-   Show icons, text, and actions in a consistent format

-   Choose between standard and segmented styles

![1 list contains 3 items, each with a label text, supporting text, and trailing text. A music app shows list items with leading images.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmi8nxjh0-01.png?alt=media&token=c030c446-c1e4-46b4-b732-cf0f8bb19bd5>)

_A list item's label text, supporting text, image, and trailing icon can be customized to create a variety of lists_

## Availability & resources

### STATUS_TABLE

| Type | Resource | Status | URL |
| --- | --- | --- | --- |
| ANDROID_IMPLEMENTATION | Android Views (MDC-Android): Expressive | AVAILABLE | https://github.com/material-components/material-components-android/blob/master/docs/components/List.md#m3-expressive |
| ANDROID_IMPLEMENTATION | Android Views (MDC-Android) | AVAILABLE | https://developer.android.com/develop/ui/views/layout/recyclerview |
| JETPACK_IMPLEMENTATION | Jetpack Compose: Expressive | AVAILABLE | https://developer.android.com/reference/kotlin/androidx/compose/material3/package-summary#ListItem%28kotlin.Function0,androidx.compose.ui.Modifier,kotlin.Function0,kotlin.Function0,kotlin.Function0,kotlin.Function0,androidx.compose.material3.ListItemColors,androidx.compose.ui.unit.Dp,androidx.compose.ui.unit.Dp%29 |
| WIZ_IMPLEMENTATION | Web: Expressive | UNAVAILABLE |  |
| WIZ_IMPLEMENTATION | Web | AVAILABLE | https://github.com/material-components/material-web/blob/main/docs/components/list.md |
| JETPACK_IMPLEMENTATION | Jetpack Compose | AVAILABLE | https://developer.android.com/develop/ui/compose/lists |
| FLUTTER_IMPLEMENTATION | Flutter | AVAILABLE | https://api.flutter.dev/flutter/material/ListTile-class.html |
| FIGMA | Design Kit (Figma) | AVAILABLE | https://www.figma.com/community/file/1035203688168086460 |

## M3 Expressive update

Lists have a new segmented visual style, improved selection treatment, and support for slots. [More on M3 Expressive](../../blog/building-with-m3-expressive.md)

**December 2025** 

Variants:

-   Added **expressive** list

    -   Recommended for new designs


-   List (baseline) is still available


New visual styles:

-   Standard or segmented

-   Highlighted selection states

-   Flexible slots


Supported platforms:

-   [Android Views (MDC-Android)](https://github.com/material-components/material-components-android/blob/master/docs/components/List.md#m3-expressive)

-   [Jetpack Compose](https://developer.android.com/reference/kotlin/androidx/compose/material3/package-summary#ListItem%28kotlin.Function0,androidx.compose.ui.Modifier,kotlin.Function0,kotlin.Function0,kotlin.Function0,kotlin.Function0,androidx.compose.material3.ListItemColors,androidx.compose.ui.unit.Dp,androidx.compose.ui.unit.Dp%29)

![2 party planning lists with 2 completed list items each. In 1 list, the selected items are highlighted.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmi8pwbjv-02.png?alt=media&token=dc992ed3-06fd-4d06-aaab-249562097efc>)

_Expressive lists feature improved selection states_

## Differences from M2 to M3 baseline

-   **Color:** New color mappings and compatibility with dynamic color

-   **Layout:** Padding and spacing rules are updated to be more consistent

-   **Height:** The tallest element within a list item determines the list item’s height - either 56dp, 72dp, or 88dp

-   **Alignment:**

    -   In most cases, elements in a list item are middle-aligned

    -   If a list is 88dp or larger, or contains three or more lines of text, elements are top-aligned

![3 variants of lists in M2.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmi8q1tdh-03.png?alt=media&token=5845ac54-f92c-41e8-8290-fac24588b690>)

_M2: Non-standard heights and alignments_

![3 variants of lists in M3 baseline.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmi8q2xsp-04.png?alt=media&token=df0c89b9-3118-4fad-bd02-8b9bd51cc98f>)

_M3 (baseline): Standardized heights and alignments_

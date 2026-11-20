---
title: "Search"
source_url: "https://m3.material.io/components/search/overview"
lastmod: "2026-05-18"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "components"
assets:
  - "assets/videos/a07de0722807-mlb0x4f4-GM3-Search-Bar-Overview-01-v2.mp4"
  - "assets/videos/dbc37af4892d-mlb19j2i-GM3-Search-Bar-Overview-02-v2.mp4"
  - "assets/images/c48e5ac4dad1-mlb1gn7s-04.png"
  - "assets/images/3964f838fd78-mlb1hcrs-05.png"
---

# Search

Search lets people enter a keyword or phrase to get relevant information

## Overview

-   Use search for navigating a product with queries

-   A search bar can include a leading search icon, hinted search text, and optional trailing icons

-   Search can display suggested keywords or phrases as a person types

-   A search bar displays search suggestions or results in a list

-   Use a search app bar to provide an emphasized, global entry-point

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmlb0x4f4-GM3%20-%20Search%20Bar%20-%20Overview%2001%20-v2.mp4?alt=media&amp;token=4438710d-8472-4698-ab9d-183b972998ad" title="Mobile UI shows a person typing into an email search bar. It expands to show a list of results."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmlb0x4f4-GM3%20-%20Search%20Bar%20-%20Overview%2001%20-v2.mp4?alt=media&token=4438710d-8472-4698-ab9d-183b972998ad>)

## Availability & resources

### STATUS_TABLE

| Type | Resource | Status | URL |
| --- | --- | --- | --- |
| ANDROID_IMPLEMENTATION | Android Views (MDC-Android): Expressive | UNAVAILABLE |  |
| ANDROID_IMPLEMENTATION | Android Views (MDC-Android) | AVAILABLE | https://github.com/material-components/material-components-android/blob/master/docs/components/Search.md |
| FLUTTER_IMPLEMENTATION | Flutter | AVAILABLE | https://api.flutter.dev/flutter/material/SearchBar-class.html |
| JETPACK_IMPLEMENTATION | Jetpack Compose: Expressive | AVAILABLE | https://developer.android.com/reference/kotlin/androidx/compose/material3/package-summary#SearchBar(androidx.compose.material3.SearchBarState,kotlin.Function0,androidx.compose.ui.Modifier,androidx.compose.ui.graphics.Shape,androidx.compose.material3.SearchBarColors,androidx.compose.ui.unit.Dp,androidx.compose.ui.unit.Dp) |
| LIT_IMPLEMENTATION | Web: Expressive | UNAVAILABLE |  |
| JETPACK_IMPLEMENTATION | Jetpack Compose | AVAILABLE | https://developer.android.com/develop/ui/compose/components/search-bar |
| LIT_IMPLEMENTATION | Web | UNAVAILABLE |  |
| FIGMA | Design Kit (Figma) | AVAILABLE | https://www.figma.com/community/file/1035203688168086460 |

## M3 Expressive update

Search has a new visual style, motion, and more flexibility for trailing icons. [More on M3 Expressive](../../blog/building-with-m3-expressive.md)

**February 2025** 

Naming

-   Search bar and search view are now collectively named **search**


Configurations

-   Styles: Search can be contained (recommended) or divided

-   Gaps can separate results into groups


Motion

-   The search bar grows wider when focused


Supported platforms:

-   [Jetpack Compose](https://developer.android.com/reference/kotlin/androidx/compose/material3/package-summary#SearchBar\(androidx.compose.material3.SearchBarState,kotlin.Function0,androidx.compose.ui.Modifier,androidx.compose.ui.graphics.Shape,androidx.compose.material3.SearchBarColors,androidx.compose.ui.unit.Dp,androidx.compose.ui.unit.Dp\))

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmlb19j2i-GM3%20-%20Search%20Bar%20-%20Overview%2002%20-v2.mp4?alt=media&amp;token=99e6f5e3-f3d1-4d83-88c9-5e37c8a0437b" title="A recipe search with “Search recipes” hinted text, “Mexican dishes” is entered, then results appear in a list."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmlb19j2i-GM3%20-%20Search%20Bar%20-%20Overview%2002%20-v2.mp4?alt=media&token=99e6f5e3-f3d1-4d83-88c9-5e37c8a0437b>)

## Differences from M2 to M3 baseline

-   Color: New color mappings and compatibility with dynamic color

-   Elevation: Lower elevation and no shadow by default

-   Name: Search was formerly known as open search bar

-   Variants: Two official variants of search components: search bar and search view

![M2 open search bar.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmlb1gn7s-04.png?alt=media&token=dc445e20-469e-40b2-9175-8d198effc998>)

_M2 open search bars were square and elevated_

![M3 search bar.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmlb1hcrs-05.png?alt=media&token=ae55f13c-75fb-4b39-bc09-f503ea5b156a>)

_M3 search bars are rounded, use tonal surface, and support dynamic color_

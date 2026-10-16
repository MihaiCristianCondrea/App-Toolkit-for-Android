---
title: "Side sheets"
source_url: "https://m3.material.io/components/side-sheets/overview"
lastmod: "2026-05-18"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "components"
assets:
  - "assets/images/c024e5d82d66-mgalv851-01.png"
  - "assets/images/65668dd44202-mgalwhrt-02.png"
---

# Side sheets

Side sheets show secondary content anchored to the side of the screen

## Overview

-   Use side sheets to provide optional content and actions without interrupting the main content

-   Two variants: standard and modal

-   People can navigate to another region within the sheet

-   Side sheets can contain a back icon for navigation

![The 2 variants of side sheets.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmgalv851-01.png?alt=media&token=191d453b-5ede-4308-8bf3-fce576d974a9>)

_Standard side sheet; Modal side sheet_

## Availability & resources

### STATUS_TABLE

| Type | Resource | Status | URL |
| --- | --- | --- | --- |
| ANDROID_IMPLEMENTATION | Android Views (MDC-Android) | AVAILABLE | https://github.com/material-components/material-components-android/blob/master/docs/components/SideSheet.md |
| FIGMA | Design Kit (Figma) | AVAILABLE | http://goo.gle/m3-design-kit |
| LIT_IMPLEMENTATION | Web | UNAVAILABLE |  |
| JETPACK_IMPLEMENTATION | Jetpack Compose | UNAVAILABLE |  |
| FLUTTER_IMPLEMENTATION | Flutter | UNAVAILABLE |  |

## Differences from M2

-   Right-to-left (RTL) language support with left side sheet
-   Color: New color mappings and compatibility with dynamic color
-   Shape: Modal side sheets have a 16dp corner radius

![A modal side sheet showing the 16dp corner radius.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmgalwhrt-02.png?alt=media&token=054fa34c-239b-4ffb-99d0-fb5c0f751873>)

_Side sheets have new color mappings to support dynamic color_

---
title: "Carousel"
source_url: "https://m3.material.io/components/carousel/overview"
lastmod: "2026-05-18"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "components"
assets:
  - "assets/videos/322edda5b3f4-lwuthdnj-GM3-Components-Carousel-Overview-1-v01.mp4"
  - "assets/images/ad8f3620137a-mljue787-02.png"
  - "assets/images/b90f5744a72b-lwur4ddz-3.png"
---

# Carousel

Carousels show a collection of items that can be scrolled on and off the screen

## Overview

-   Contain visual items like images or video, along with optional label text

-   Six layouts: Multi-browse, uncontained, uncontained multi-aspect ratio, hero, center-aligned hero and full-screen

-   Layouts can be start-aligned or center-aligned

-   Item visuals have a parallax effect when scrolled

-   Items change size as they move through the carousel

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwuthdnj-GM3-Components-Carousel-Overview-1-v01.mp4?alt=media&amp;token=fdc84e41-6db8-4bbb-8474-60d754219b63" title="A carousel being scrolled horizontally. Each carousel item changes shape as it scrolls."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwuthdnj-GM3-Components-Carousel-Overview-1-v01.mp4?alt=media&token=fdc84e41-6db8-4bbb-8474-60d754219b63>)

## Availability & resources

### STATUS_TABLE

| Type | Resource | Status | URL |
| --- | --- | --- | --- |
| ANDROID_IMPLEMENTATION | Android Views (MDC-Android) | AVAILABLE | https://github.com/material-components/material-components-android/blob/master/docs/components/Carousel.md |
| FLUTTER_IMPLEMENTATION | Flutter | AVAILABLE | https://api.flutter.dev/flutter/material/CarouselView-class.html |
| FIGMA | Design Kit (Figma) | AVAILABLE | https://www.figma.com/community/file/1035203688168086460 |
| JETPACK_IMPLEMENTATION | Jetpack Compose | AVAILABLE | https://developer.android.com/develop/ui/compose/components/carousel |
| LIT_IMPLEMENTATION | Web | UNAVAILABLE |  |

## Updates

**November 2025**

New carousel layout:

-   Uncontained multi-aspect ratio


**2023** 

Additional layouts and configurations:

-   Uncontained

-   Full-screen

-   Centered carousels

-   Hero carousel layout

-   Multi-browse layout

![Items of different widths in an uncontained multi-aspect ratio layout.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmljue787-02.png?alt=media&token=995bfea7-ba9d-4d3a-b922-96ed26cf7de6>)

_New carousel layout: uncontained multi-aspect ratio_

## Differences from M2

This component is new in Material 3.

-   **Shape**: Dynamic carousel items change shape when scrolled
-   **Motion**: Carousel items move at a different speed than their content, creating a parallax effect
-   **Interaction**: When scrolled, carousel items snap into place to maintain the same layout. Hero carousels swipe through one item at a time. Multi-browse carousels scroll through many items at once.

![Carousel in a contained layout with a large item and a small item.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwur4ddz-3.png?alt=media&token=a42f76c5-c4fd-46fd-89a1-04b492638986>)

_Hero carousels scroll through one large item at a time_

## Research

The Material Research Team conducted two studies (quantitative and qualitative) with over 200 participants to understand their perspectives of five different carousel designs. The studies measured their understanding of how to interact with each carousel, their expectations of the number of items in each design, and how they expected carousels to be used.

**Summary of findings:**

-   Participants thought carousels were a good way to explore many different kinds of content

-   A previewed or squished item strongly indicated that there was more content to swipe through

-   Participants expected around 10 items in a carousel that scrolled multiple items at once

-   While some contexts were considered better for some carousel designs, all designs were considered similarly usable

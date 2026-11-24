---
title: "Layout overview"
source_url: "https://m3.material.io/foundations/layout/layout-overview/overview"
lastmod: "2026-05-19"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "foundations"
assets:
  - "assets/images/b340bf4b5a76-mp3ukjvm-01.png"
  - "assets/images/0294b76da611-mp3uxdvt-02.png"
---

# Layout overview

Layout is the visual and strategic arrangement of elements on a screen

## Overview

-   Use layout to organize all elements in a screen, signal hierarchy, and draw attention to key actions

-   Adapt layouts to compact, medium, expanded, large, and extra-large breakpoints (previously window size classes)

-   Build from an established [canonical layout example](../canonical-examples/feed.md)

-   Design for [bidirectionality](../bidirectionality-rtl.md) to support both left-to-right (LTR) and right-to-left (RTL) languages

-   Apply consistent arrangement, sizing, and spacing to create a functional layout structure

-   Material layout guidance is implemented on Android and applies to web

![7 elements of layout.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp3ukjvm-01.png?alt=media&token=6f4fdd25-559d-4ed3-964a-ffb0c5f7b5f7>)

_Column; Fold; Margin; Bar; Drag handle; Pane; Rail_

## Availability & resources

| **Type** | **Resource** | **Status** |
| --- | --- | --- |
| Design | [M3 Design Kit](https://www.figma.com/community/file/1035203688168086460) (Figma) | Available |
| [Spacing system & tokens](../../../styles/spacing/overview.md) | Available |  |
| Implementation | [Jetpack Compose: Canonical layouts](https://developer.android.com/develop/ui/compose/layouts/adaptive/canonical-layouts) | Available |
| [Jetpack Compose: Rulers](https://developer.android.com/reference/kotlin/androidx/compose/ui/layout/Ruler) | Available |  |
| [Android Views (MDC-Android): Canonical layouts](https://github.com/android/user-interface-samples/tree/main/CanonicalLayouts) | Available |  |

## What's new

When creating new layouts, use the layout scaffold, start from a canonical layout example, and ensure layouts scale across breakpoints.

##### **May 2026** 

Layout structure and design:

-   Introduced layout scaffold, to create adaptive layouts efficiently

-   New adaptive guidelines for mobile, desktop, and spatial devices

-   Updated canonical layout examples

-   [Spacing system](../../../styles/spacing/overview.md)


Naming:

-   Window size classes renamed to breakpoints

-   Responsive layout renamed to [adaptive design](adaptive-design.md)

![Compact and expanded layouts for differently sized screens.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp3uxdvt-02.png?alt=media&token=a4cf0742-9a81-4697-8963-46b936eeb5f3>)

_The Material layout scaffold enables layouts to adapt across different screen sizes_

## Layout terms

-   **Adaptive design**: Techniques that allow an interface to dynamically respond to contexts like user preferences, device type, state, and breakpoints

-   **Bars**: Can frame the page to help people navigate through a product, and typically house the app bar and bottom navigation bar

-   **Bidirectionality**: A writing system that displays text and content from right-to-left (RTL) 

-   **Breakpoints**: Opinionated window sizes where a layout changes to match available space, device conventions, and ergonomics (previously window size classes)

-   **Column**: One or more vertical blocks of content within a pane

-   **Drag handle**: The component that resizes panes

-   **Fold**: A flexible area of the screen or a hinge that separates two displays on foldable devices

-   **Gap**: The space between components or elements within a container

-   **Margin**: The space between the edge of the screen and any elements inside of it

-   **Multi-window mode**: Enables multiple apps to share the same screen simultaneously

-   **Pane**: A layout container that houses other components and elements within a single app. A pane can be fixed, flexible, floating, or semi-permanent.

-   **Rails**: The perimeter space surrounding panes that holds key elements such as navigation rails, toolbars, and pane control

-   **Right-to-left (RTL) language**: Languages written and read right-to-left, such as Arabic, Hebrew, and Farsi, used by [over 2 billion people](https://www.w3.org/International/questions/qa-scripts.en.html)

-   **Rulers**: An opinionated set of global alignment lines that help organize building blocks in a layout

-   **Safety region**: Zones reserved for system UI elements outside the application space, such as status bar or gesture bar

-   **Scaffold**: A fundamental UI design structure that provides a standard platform for assembling key screen components

-   **Spacer**: The space between two panes on a foldable device

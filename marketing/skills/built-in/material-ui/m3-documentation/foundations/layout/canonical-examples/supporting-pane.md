---
title: "Canonical layout examples"
source_url: "https://m3.material.io/foundations/layout/canonical-examples/supporting-pane"
lastmod: "2026-05-19"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "foundations"
assets:
  - "assets/images/dda7f94a0aa6-mp54jbnh-01.png"
  - "assets/images/ab3194028ef2-mp54n3m1-02.png"
  - "assets/images/f96344f8281d-mp54omgc-03.png"
  - "assets/images/86f894b90994-mp73c5cw-04.png"
  - "assets/images/a559435ee280-mp54tfu7-05.png"
  - "assets/images/fc5da298f7ad-mp73dr78-6.png"
---

# Canonical layout examples

Canonical layout examples are designs for common screen layouts across all breakpoints

## Supporting pane

The supporting pane layout organizes content into primary and secondary areas.   

The primary area contains the main content and occupies the majority of the space. The secondary area contains supporting content.  

Key use cases for supporting pane layouts include:

-   Productivity

-   Document editing and commenting

-   Content and media browsing

![A video app has the main content in the primary area and “up next” content is listed in the secondary area.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp54jbnh-01.png?alt=media&token=409de525-30e4-43b6-9864-a7e9d59c137e>)

_Supporting pane layouts organize content into primary and secondary areas_

## Usage

Use the supporting pane layout when the secondary content is only meaningful in relation to the primary content.   

For content with a parent-child relationship, use a [list-detail layout](https://m3.material.io/m3/pages/canonical-layouts/list-detail/) instead.

![The supporting pane has vertically stacked cards.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp54n3m1-02.png?alt=media&token=db669416-4a28-46b3-8f50-8ee0148c932c>)

_Supporting panes provide contextual info for the primary area_

## Dividing space

The window is divided between a focus pane and a supporting pane.  

Depending on the breakpoint, the supporting pane may appear below or beside the focus pane.

![The cards of a supporting pane scroll horizontally across the bottom of the screen.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp54omgc-03.png?alt=media&token=015715a5-701f-4812-b8b7-a943d11973d5>)

_Supporting panes can appear beside or below the primary area_

| Supporting pane placement | Pane width | Breakpoint |
| --- | --- | --- |
| Below | Flexible | Compact or Medium |
| Leading or trailing | Fixed (360 dp) | Expanded |

## Across breakpoints

### Compact

The supporting pane should appear below the focus pane. 

A bottom sheet can be useful for keeping focus on the primary pane while providing access to supporting information.

![2 layouts showing  bottom sheets at a compact breakpoint.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp73c5cw-04.png?alt=media&token=c27a6901-5f40-4e51-8c67-ea4eb3ebbfe6>)

_Bottom sheets can provide supporting information in compact windows_

### Medium

The supporting pane should appear below the focus pane.

![3 cards in a supporting pane are horizontal across the bottom of a tablet.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp54tfu7-05.png?alt=media&token=2bbdfa34-1ec6-45ab-83c1-ad65cc35e156>)

_Supporting panes appear below the focus pane in medium windows_

### Expanded

The supporting pane should appear on the leading or trailing side of the focus pane.

![The supporting pane is to the right of the primary focus pane on 2 screens.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp73dr78-6.png?alt=media&token=923d4433-2a50-4111-8de9-3e69135229cc>)

_Supporting panes appear beside the focus pane in expanded windows_

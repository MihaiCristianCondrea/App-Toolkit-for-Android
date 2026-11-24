---
title: "Breakpoints"
source_url: "https://m3.material.io/foundations/layout/breakpoints/medium"
lastmod: "2026-05-19"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "foundations"
assets:
  - "assets/images/f4f512d8e41d-mp413hn4-01.png"
  - "assets/images/0410ec91f903-mp414ag3-02.png"
  - "assets/images/3e4032943466-mp417y7j-03.png"
  - "assets/images/a0653b7a62ea-mp418umv-04.png"
  - "assets/images/4698c38e898e-mp419dq9-05.png"
  - "assets/images/e914da8027b7-mp41ahko-06.png"
  - "assets/images/f7b7bc5b3853-mp5kr0vt-07.png"
  - "assets/images/8dd6d599ee93-mp41cohr-08.png"
  - "assets/images/fbb506656f4e-mp41dv1l-09.png"
---

# Breakpoints

Breakpoints ensure layouts work across a wide range of devices

## Medium

Layouts for medium breakpoints are for **screen widths from 600dp to 839dp.**

![A medium breakpoint with a video call app in full-screen mode.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp413hn4-01.png?alt=media&token=2c0da3ff-7141-4ca8-af41-0346e2de7d60>)

_Single-pane layouts can focus attention on one action or view, such as a video call_

## Navigation

Place navigation components close to edges of the window where they’re easier to reach:

-   Single-pane layouts: Navigation rail

-   Two-pane layouts: Navigation bar


The navigation rail can be hidden in secondary destinations as long as the primary destination can still be accessed using a back button.

![The navigation area of a medium breakpoint is a vertical bar at the leading edge, beside a single pane.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp414ag3-02.png?alt=media&token=64a6283a-5109-4f5c-86ff-68ae06606b69>)

_Navigation area; Single pane_

## Panes

### Single-pane layout  

In a medium layout, a single pane is recommended because of limited screen width.

![A single pane uses most of the space in a medium window.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp417y7j-03.png?alt=media&token=327c1e0d-8a6b-4be9-aedf-6371f854b6be>)

_A single-pane layout is recommended for medium breakpoints_

### Two-pane layout

Limit use of two panes for content with lower information density, such as a settings screen.

Each pane in a two-pane layout should take up 50% of the window width. Avoid setting custom widths. A drag handle can be used to expand or collapse panes to be 100% of the window width.

![2-pane layout in a medium window. Both panes fit 50% of the window width by default.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp418umv-04.png?alt=media&token=9b34c412-bb58-42cd-8f44-7c395f8ef18c>)

_Two-pane layouts should use 50% widths for each pane by default_

When adding navigation to a two-pane layout, use a navigation bar. This allows the panes to fully use the available window width.

![A navigation bar extends over 2 panes at the bottom of a medium window.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp419dq9-05.png?alt=media&token=58daa7e8-0a14-43e3-90b4-ae2041b8e2ec>)

_Two-pane layout with: Navigation bar_

## Spacing

Medium layouts have margins of 24dp.

The spacer between panes is also 24dp.

![2 pane layout with 24dp margins and 24dp space between panes.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp41ahko-06.png?alt=media&token=9da78d9d-b883-4409-b40c-48158ed8d2e7>)

_Use 24dp for margins and spacer in a medium layout_

## Special considerations

A medium layout will need to transition dynamically to a compact or expanded layout when:

-   A foldable device is folded

-   A tablet is rotated from portrait to landscape

-   A product goes from full-screen to split-screen

-   Multi-window mode is initiated

-   A free-form window is resized

![Email app with 2 panes at  a medium breakpoint.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp5kr0vt-07.png?alt=media&token=9581e119-eca0-4d12-ab75-364cfe6c170e>)

_Think of how a medium layout should change to a compact or expanded layout_

### Reachability

For horizontal tablets and unfolded foldables, the top 25% of the screen is likely out of reach, unless the grip is adjusted. To accommodate device and hand sizes, limit the amount of interactions that are placed in the upper 25% of the screen.

![The hard-to-reach top quarter of a medium breakpoint in landscape mode.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp41cohr-08.png?alt=media&token=f2643638-a1cd-48f0-834c-4c801887ba91>)

_Limit interactions in the upper quarter of a screen, as they can be hard to reach_

Avoid placing essential interactive elements too close to the bottom edge of the screen. Some users, particularly those with larger hands, might struggle to reach this area.

Specify interactions in a layout with these ergonomic regions in mind:

1.  Users can reach this area by extending their fingers, which makes it inconvenient

2.  Users can reach this area comfortably

3.  Reaching this area is challenging when holding the device

![The 3 ergonomic regions of a medium breakpoint.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp41dv1l-09.png?alt=media&token=ae8d93b9-146c-457a-864e-a93d0bc727dc>)

_Medium breakpoint ergonomic regions: Inconvenient; Comfortable; Challenging_

---
title: "Breakpoints"
source_url: "https://m3.material.io/foundations/layout/breakpoints/expanded"
lastmod: "2026-05-19"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "foundations"
assets:
  - "assets/images/7ecd7c73eea6-mp41hvbo-01.png"
  - "assets/images/666ff5fdf6a7-mp41iro5-02.png"
  - "assets/images/17067ab3b08b-mp41jkb5-03.png"
  - "assets/videos/d928cdc6eeac-mp41knzh-GM3_Applaying-Layout_Understanding_Part_10_IA_v01.mp4"
  - "assets/videos/9e7a5f53353c-mp41lyj0-ly7433ph-GM3-Styles-Foundations-Layout-ApplyingLayout-2-v01.mp4"
  - "assets/images/3f6372275b53-mp5l3xgt-06.png"
  - "assets/videos/3f03ee019785-mp41ofdd-GM3_Applaying-Layout_Expanded_07_IA_v01.mp4"
---

# Breakpoints

Breakpoints ensure layouts work across a wide range of devices

## Expanded

Layouts for expanded breakpoints are for **screen widths from 840dp to 1199dp.**

![Supporting pane layout of a video app. The large, primary pane has the video, title, and actions. The secondary pane has queued videos.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp41hvbo-01.png?alt=media&token=a0987d8d-bee6-446b-9533-8bad300ec920>)

_Two-pane layouts are often best for expanded breakpoints_

## Navigation

Place navigation components close to edges of the window where they’re easier to reach. Use a navigation rail, either collapsed or expanded.

The navigation rail can be hidden in secondary destinations as long as the primary destination can still be accessed using a back button.

For sorting, filtering, or secondary navigation, use tabs or other components directly in the pane.

![The navigation area is a vertical bar at the left of the screen. To its right, the body pane fills the rest of the window.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp41iro5-02.png?alt=media&token=d61c1d3f-5835-4afa-8577-c5e0f27eedb8>)

_Navigation area; Single pane_

## Panes

Use a single-pane or two-pane layout.

A two-pane layout is often best for expanded breakpoints. However, a single-pane layout can work when displaying visually- or information-dense content, such as videos.

![A single pane covers most of the expanded screen except for the navigation area and margins.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp41jkb5-03.png?alt=media&token=6ba22cd5-a9b7-4c62-9c8a-90049d3742b3>)

_At an expanded breakpoint, reserve single-pane layouts for information-dense content_

When using a [fixed-and-flexible](../scaffold/panes.md#92371c3b-587d-4c6f-8105-05b69dcec81a) layout, the fixed pane should have a width of 360dp by default.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp41knzh-GM3_Applaying%20Layout_Understanding_Part_10_IA_v01.mp4?alt=media&amp;token=3e86bdd8-17eb-44b5-a519-a1706a4b2f83" title="A 2-pane layout at an expanded breakpoint. The fixed pane starts at 360dp, then expands to a single-pane layout."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp41knzh-GM3_Applaying%20Layout_Understanding_Part_10_IA_v01.mp4?alt=media&token=3e86bdd8-17eb-44b5-a519-a1706a4b2f83>)

A [split-pane layout](../scaffold/panes.md#dc7982b7-754c-410a-9e88-18a54557c87b) uses two flexible panes and visually centers the spacer by default.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp41lyj0-ly7433ph-GM3-Styles-Foundations-Layout-ApplyingLayout-2-v01.mp4?alt=media&amp;token=58a29eae-b5c1-4d65-afb7-33015910bed2" title="A nav rail and a pane fill 50% of the window. A second pane fills the remaining 50%."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp41lyj0-ly7433ph-GM3-Styles-Foundations-Layout-ApplyingLayout-2-v01.mp4?alt=media&token=58a29eae-b5c1-4d65-afb7-33015910bed2>)

## Spacing

Expanded layouts have a leading and trailing margin of 24dp.

The spacer between panes is 24dp.

![2 pane layout with 24dp margins and 24dp space between panes.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp5l3xgt-06.png?alt=media&token=7bf8133f-ef0c-4377-8cd4-abe7cb6c41d8>)

_Use 24dp for margins and spacer in an expanded layout_

## Special considerations

An expanded layout will need to transition dynamically to a compact or medium layout when:

-   A foldable device is folded

-   A tablet is rotated from landscape to portrait

-   The app goes from full-screen to split-screen

-   Multi-window mode is initiated

-   A free-form window is resized

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp41ofdd-GM3_Applaying%20Layout_Expanded_07_IA_v01.mp4?alt=media&amp;token=9329994a-953e-4e49-be8b-5088638a4070" title="Email app moves from 2-pane expanded layout to medium to 1-pane small layout."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp41ofdd-GM3_Applaying%20Layout_Expanded_07_IA_v01.mp4?alt=media&token=9329994a-953e-4e49-be8b-5088638a4070>)

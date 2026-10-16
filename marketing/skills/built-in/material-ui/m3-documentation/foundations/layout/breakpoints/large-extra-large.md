---
title: "Breakpoints"
source_url: "https://m3.material.io/foundations/layout/breakpoints/large-extra-large"
lastmod: "2026-05-19"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "foundations"
assets:
  - "assets/images/36eaa77af6ee-mp41v09e-01.png"
  - "assets/images/4afab7896a7a-mp41w5ix-02.png"
  - "assets/images/cc27d1688668-mp41xhwl-03.png"
  - "assets/images/b4d0673b5ff5-mp41y2za-04.png"
  - "assets/images/bbccaf2d2569-mp41yyza-05.png"
  - "assets/images/266dad975934-mp41zo3h-06.png"
  - "assets/images/db4f7e257d10-mp420iqc-07.png"
  - "assets/images/a812b2a49091-mp5lfdh0-08.png"
  - "assets/images/d0630620c84c-mp42206a-09.png"
---

# Breakpoints

Breakpoints ensure layouts work across a wide range of devices

## Large & extra-large

These breakpoints are most useful for creating web experiences tailored to laptop and desktop devices. Some products may not need large and extra-large breakpoints. Consider your platform’s conventions and users when making decisions on which breakpoints to design for.

-   Layouts for large breakpoints are for screen widths **from 1200dp to 1599dp**

-   Layouts for extra-large breakpoints are for screen widths of **1600dp and larger**

![Large window with a video app in 2 pane layout.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp41v09e-01.png?alt=media&token=7f094b0b-b03b-4951-a36d-4a8c6093e85e>)

_A two-pane layout is recommended for large and extra-large breakpoints_

## Navigation

Use a navigation rail, either collapsed or expanded, depending on the amount of content.  

For sorting, filtering, or secondary navigation, use tabs or other components directly in the pane.

![Large web browser with a small navigation area on the leading edge with 1 pane filling the rest of the window.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp41w5ix-02.png?alt=media&token=9614a072-ce72-4776-a47f-91ab5187e9be>)

_Collapsed navigation area; Single-pane layout_

An expanded navigation rail is best suited for extra-large windows, where there's still plenty of room for content. Consider collapsing the navigation rail when space is needed, or when on pages deeper in the page hierarchy.

![Extra-large web browser with an expanded navigation area and 1 pane filling the rest of the window.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp41xhwl-03.png?alt=media&token=3d8311b3-3c1e-4e15-a616-a3671838fc27>)

_Expanded navigation area; Single-pane layout_

## Panes

A two-pane layout is often best for large and extra-large breakpoints.   

However, a single-pane layout can work when displaying visually- or information-dense content, such as videos.

![The single pane covers most of a large screen except for the navigation area and margins.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp41y2za-04.png?alt=media&token=e358e573-2d73-467d-b691-018e78dcb632>)

_Only use a single-pane layout for dense content or media_

When using a [fixed-and-flexible](../scaffold/panes.md#92371c3b-587d-4c6f-8105-05b69dcec81a) layout, the fixed pane should have a width of 412dp by default.

![A 2-pane layout with 1 pane fixed at 412dp.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp41yyza-05.png?alt=media&token=b8a289e7-29b4-4203-aabe-0b1d74d8dfed>)

_Fixed panes should be 412dp in large and extra-large layouts_

When using a [split-pane layout](../scaffold/panes.md#dc7982b7-754c-410a-9e88-18a54557c87b), the spacer should be visually centered by default, even when using an expanded navigation rail.

![An expanded nav rail and the first pane take up 50% of the window, and the second pane takes up 50%. ](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp41zo3h-06.png?alt=media&token=c84fdbe6-ae28-4c1e-90f8-78b4febd3cf7>)

_In split-pane layouts, navigation components shrink the leading pane, so the spacer remains centered_

## Additional panes

The extra-large breakpoint supports using a standard side sheet as a third pane. When the side sheet is present, the navigation rail can remain visible, collapse, or hide completely. Don't use more than three panes.   

Note: Fixed panes in this window size are recommended to be 412dp, but side sheets have a default maximum width of 400dp.

![Extra-large window with 2 panes and a side sheet acting as a third pane.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp420iqc-07.png?alt=media&token=5b1ecbcf-5222-4480-95d6-cea9cbf11938>)

_Standard side sheet (third pane)_

## Spacing

Large and extra-large layouts have a leading and trailing margin of 24dp.

The spacer between panes is 24dp.

![2 pane layout with 24dp margins and 24dp space between panes.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp5lfdh0-08.png?alt=media&token=6cd05b0b-e27f-42eb-89f7-810209deab65>)

_Use 24dp for margins and spacers in large and extra-large layouts_

## Special considerations

Large and extra-large layouts will need to transition dynamically to a smaller layout when:

-   The app goes from full-screen to split-screen

-   Multi-window mode is initiated

-   A free-form window is resized


Pay attention to typographic elements such as line length to ensure readability on large and extra-large layouts.

![An email app with 2 panes in a large layout.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp42206a-09.png?alt=media&token=985c34f9-93f7-410a-8a2d-3b060180c293>)

_Consider how a large layout should change at smaller breakpoints_

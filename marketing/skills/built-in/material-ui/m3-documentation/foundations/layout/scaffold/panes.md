---
title: "Scaffold"
source_url: "https://m3.material.io/foundations/layout/scaffold/panes"
lastmod: "2026-05-19"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "foundations"
assets:
  - "assets/images/446eae9d32e5-mp3nyytv-13-key01.png"
  - "assets/images/2b134f435e40-mp3o5axo-01.png"
  - "assets/images/d273db4954cd-mp3o6q96-02.png"
  - "assets/videos/c5430c566fb3-mp3o8609-GM3_Applying-Layouts_Co-planar-semi-permanent-06_IA_v01.mp4"
  - "assets/images/1cf62d83906d-mp3oanue-04.png"
  - "assets/images/b695630d26a6-mp3ocqkb-05.png"
  - "assets/images/6ccb7c8fa115-mp3oeb3e-06.png"
  - "assets/images/f06d9bb8d11c-mp3ogbeu-07.png"
  - "assets/images/aa7d9059762d-mp3ojkaq-08.png"
  - "assets/videos/9abeb50c3638-mp3omlsl-GM3_Applaying-Layout_Understanding_Part_09_IA_v01.mp4"
  - "assets/videos/13cb2d3c5c62-mp3ooyrc-GM3_Applaying-Layout_Understanding_Part_10_IA_v01.mp4"
  - "assets/videos/6b74d1420c6b-mp3org16-GM3_Applaying-Layout_Understanding_Part_11_IA_v01.mp4"
  - "assets/videos/fc303d6438aa-mp3otau7-GM3_Applaying-Layout_Understanding_Part_12_IA_v02.mp4"
  - "assets/videos/0fa2c6d6e347-mp5bgps3-GM3_Applaying-Layout_Understanding_Part_13_IA_v01.mp4"
  - "assets/videos/221c2a1cf868-mp5bk7hu-lxx28ijn-GM3-Styles-Foundations-Layout-UnderstandingLayout-Parts-6-v02.mp4"
  - "assets/images/0f106a2b5f0e-mp3ow4kk-15.png"
  - "assets/images/d6be2dc9ef5c-mp3oxadg-16.png"
  - "assets/images/01484513af46-mp3oxtwa-17.png"
  - "assets/videos/5fe5c594527a-mp3p0wwo-GM3_Applying-Layouts_Understanding_Parts_Reflow_17_IA_V01.mp4"
  - "assets/videos/7bd04211fe03-mp3p3knn-GM3_Applying-Layouts_Understanding_Parts_Reflow_18_IA_v01.mp4"
  - "assets/images/172bfba9fe43-mp3p9uz9-20.png"
  - "assets/videos/6bc452f4c16b-mp3rcrfk-GM3_Applying-Layouts_Understanding_Parts_Bottom-Sheet-to-Floating-Pane_19_IA_v01.mp4"
  - "assets/videos/b514a49c0392-mp3rfkvn-GM3_Applying-Layouts_Understanding_Parts_Bottom-Sheet-to-Co-planar-Pane_20_IA_v01.mp4"
  - "assets/images/63052cc2edeb-mp3riptw-23.png"
  - "assets/videos/0bb611c61232-mp3skf28-GM3_Applying-Layouts_Understanding_Parts_Reflow_23_IA_v01.mp4"
  - "assets/videos/02b6ca9259fe-mp3smk3l-GM3_Applying-Layouts_Understanding_Parts_Reflow-wsc-Change-24_-IA_v01.mp4"
  - "assets/images/81024d7af27c-mp3sp9gc-07-key01.png"
---

# Scaffold

A fundamental UI design structure that provides a standard platform for assembling key components

## Panes

## Panes

All layouts are made up of 1–3 panes. The type of layout and amount of panes you choose should depend on the breakpoint (previously window size classes) and the type of product being built.

![A two-pane layout on desktop.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp3nyytv-13-key01.png?alt=media&token=db27e96e-eb52-49b9-bb50-345e485da060>)

_Layouts often include multiple panes that work together_

All layouts are made up of 1–3 visible panes. The type of layout and amount of panes you choose should depend on the breakpoint and the type of product you're building.

| Breakpoint | Recommended pane total | Other pane totals |
| --- | --- | --- |
| Compact | 1 | \-- |
| Medium | 1 | 2 |
| Expanded | 2 | 1 |
| Large | 2 | 1 |
| Extra-large | 2 | 1, 3 |

Panes can be:

-   Fixed: Width doesn’t change based on available space

-   Flexible: Width changes based on available space, and can grow and shrink


All layouts need at least one flexible pane.

![A device with 2 panes: 1 fixed and 1 flexible. ](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp3o5axo-01.png?alt=media&token=0cec90da-3b69-4d39-9d38-0ac403866224>)

_Fixed pane; Flexible pane_

Panes can be permanent or temporary. Temporary panes can appear and be dismissed when necessary, affecting the layout and size of other panes.

![2 permanent panes.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp3o6q96-02.png?alt=media&token=0a6d1b28-52cd-42ee-b280-e31226b49c42>)

_Panes can be displayed permanently side by side_

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp3o8609-GM3_Applying%20Layouts_Co-planar%20semi-permanent%2006_IA_v01.mp4?alt=media&amp;token=8b54a92e-2107-4890-a90f-6e3f11c3b858" title="Right pane with a close button being dismissed. The left pane fills the available space."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp3o8609-GM3_Applying%20Layouts_Co-planar%20semi-permanent%2006_IA_v01.mp4?alt=media&token=8b54a92e-2107-4890-a90f-6e3f11c3b858>)

### Single-pane layouts

Single-pane layouts use one flexible pane that extends to fit the available space in a layout’s width. They can be used at any breakpoint, but are recommended for compact and medium.

![A mobile screen with 1 flexible pane.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp3oanue-04.png?alt=media&token=51c37f6b-9879-4f51-b2b8-7692175c9963>)

_A single flexible pane adapts to fit any breakpoint_

### Two-pane layouts

**Split-pane layout**

A split-pane layout keeps the spacer visually centered. It’s best for foldable devices and dynamic layouts.

When a navigation rail or drawer is present, it only reduces the size of one pane. The other pane remains at 50% of the window width.

![2 flexible panes in a split-pane layout.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp3ocqkb-05.png?alt=media&token=11e0471f-55e9-4358-86e1-4d1b53a07e1a>)

_The navigation and first pane should be 50% of the window width to keep the spacer centered_

With a navigation bar, or no navigation, both panes span 50% of the window width by default.

![2 flexible panes at 50% width, with a navigation bar below them spanning the whole window.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp3oeb3e-06.png?alt=media&token=389164ff-277f-4ec5-8572-f480d2badcea>)

_With no navigation rail visible, split-pane layouts set each pane to 50% width by default_

**Fixed-and-flexible layout**

This layout is common for expanded, large, and extra-large breakpoints. The fixed-and-flexible panes can appear in whichever order is best for the content.

The fixed pane is often temporary, and used for side sheets or lists with light information density.

![Fixed and flexible panes arranged 2 different ways.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp3ogbeu-07.png?alt=media&token=d5605ccb-16b9-4a61-bd35-3b7a25e5a1cb>)

_Fixed pane; Flexible pane_

### Three-pane layouts

While less common, the extra-large breakpoint supports using a standard side sheet as a third pane. When the side sheet is present, the expanded navigation rail can remain visible, change into a collapsed navigation rail, or hide completely. Don't use more than three panes. 

Note: Fixed panes at this breakpoint are recommended to be 412dp, but side sheets have a default maximum width of 400dp.

![Extra large breakpoint with 2 panes and a side sheet acting as a third pane.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp3ojkaq-08.png?alt=media&token=b50cca85-884b-44aa-9e63-66adb80f855a>)

_A standard side sheet can be used as a third pane_

## Pane expansion & resizing

Panes can be resized, expanded, and collapsed using drag handles. 

-   In a split-pane layout, both flexible panes can be freely adjusted, or can snap to certain widths.

-   In a fixed-and-flexible layout, the drag handle can fully collapse and expand the fixed pane. This makes it easy to switch between a single-pane and two-pane layout.


The drag handle should also toggle between layout sizes when selected. This can be a tap, double tap, or long press.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp3omlsl-GM3_Applaying%20Layout_Understanding_Part_09_IA_v01.mp4?alt=media&amp;token=c9b3b79a-dfb6-43f3-8e6e-244b5901f36a" title="A drag handle is used to collapse a two-pane layout into a single-pane layout."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp3omlsl-GM3_Applaying%20Layout_Understanding_Part_09_IA_v01.mp4?alt=media&token=c9b3b79a-dfb6-43f3-8e6e-244b5901f36a>)

At expanded, large, and extra-large breakpoints, two-pane layouts can be customized to snap to set widths when resized.

The recommended custom widths are:

-   360dp

-   412dp

-   Split-pane with spacer centered visually

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp3ooyrc-GM3_Applaying%20Layout_Understanding_Part_10_IA_v01.mp4?alt=media&amp;token=2898a68a-5da8-4f68-963f-8be20759fba7" title="A drag handle adjusts the panes to recommended custom widths."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp3ooyrc-GM3_Applaying%20Layout_Understanding_Part_10_IA_v01.mp4?alt=media&token=2898a68a-5da8-4f68-963f-8be20759fba7>)

### Persistent pane resizing

The persistent resizing behavior remembers a person's pane width preference. Use this for most resizable layouts.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp3org16-GM3_Applaying%20Layout_Understanding_Part_11_IA_v01.mp4?alt=media&amp;token=bd9cc9d8-990c-4030-ad91-c25093fd808c" title="Resizing the panes and then resetting the app preserves the set width."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp3org16-GM3_Applaying%20Layout_Understanding_Part_11_IA_v01.mp4?alt=media&token=bd9cc9d8-990c-4030-ad91-c25093fd808c>)

The width persists even after a breakpoint change. This means that if a two-pane layout is collapsed to one pane at any size, it’ll remain collapsed even when changing breakpoints.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp3otau7-GM3_Applaying%20Layout_Understanding_Part_12_IA_v02.mp4?alt=media&amp;token=1aeccb08-d4c3-4cd8-90c2-b79246944eb4" title="Resizing the panes and then rotating a tablet back and forth preserves the set width."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp3otau7-GM3_Applaying%20Layout_Understanding_Part_12_IA_v02.mp4?alt=media&token=1aeccb08-d4c3-4cd8-90c2-b79246944eb4>)

### Temporary pane resizing

The temporary resizing behavior doesn't remember a person’s preferences for pane width. This is primarily used in supporting pane layouts where resizing is uncommon.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp5bgps3-GM3_Applaying%20Layout_Understanding_Part_13_IA_v01.mp4?alt=media&amp;token=47e02955-4e23-43b3-8da3-ac589c943814" title="Resizing the panes and then closing the second pane resets the set width when expanded again."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp5bgps3-GM3_Applaying%20Layout_Understanding_Part_13_IA_v01.mp4?alt=media&token=47e02955-4e23-43b3-8da3-ac589c943814>)

With temporary resizing, panes should always return to the default layout after the pane or product is closed and reopened. This ensures content is a suitable size for most interactions.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp5bk7hu-lxx28ijn-GM3-Styles-Foundations-Layout-UnderstandingLayout-Parts-6-v02.mp4?alt=media&amp;token=36004a49-bc33-4f4d-81d7-4cf7bd0a56c9" title="Layouts with temporary resizing reset any custom widths to the default."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp5bk7hu-lxx28ijn-GM3-Styles-Foundations-Layout-UnderstandingLayout-Parts-6-v02.mp4?alt=media&token=36004a49-bc33-4f4d-81d7-4cf7bd0a56c9>)

## Displaying multiple panes

Multiple panes can be displayed in three ways: co-planar, floating, or docked. The layout depends on breakpoint, what the pane does, and how people interact with it:

-   Co-planar: Two side-by-side panes. To stay accessible, persistent utilities like tool panels should be co-planar with primary content.

-   Floating: A small pane displays above larger panes. Temporary tasks should remain floating regardless of breakpoint, such as a dialog.

-   Docked: A small pane pinned to the edge of a window. For example, a bottom sheet can be docked to show additional actions.

![A foldable open screen with 2 co-planar panes displayed side by side.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp3ow4kk-15.png?alt=media&token=3d9d909d-60dd-45df-9a01-f46405e18a80>)

_Co-planar: Panes are displayed side by side_

![ A foldable open screen with a floating pane displayed above other elements. ](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp3oxadg-16.png?alt=media&token=be699075-a53f-4149-ba51-7f0d41116d9e>)

_Floating: A pane is displayed above other panes or content, like a dialog_

![A foldable open screen with a docked pane to the bottom of the screen displayed above other elements. ](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp3oxtwa-17.png?alt=media&token=8ec788eb-43eb-4a88-9ed9-12ae8a069de4>)

_Docked: A pane is displayed above other panes and one of its edges extends beyond one side of the screen, like a bottom sheet_

## How panes adapt

Pane layouts can adapt using three strategies: **show and hide, levitate,** or **reflow**. When a window is resized or changes orientation, these strategies allow panes to reorganize themselves to preserve context and meaning.

### Show and hide

As the breakpoint size or orientation changes, panes can enter and exit the screen or appear next to one another.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp3p0wwo-GM3_Applying%20Layouts_Understanding_Parts_Reflow_17_IA_V01.mp4?alt=media&amp;token=9983bea7-e42f-4bd4-bbd6-bbfb982759fb" title="1 pane animates off-screen when the device is vertical, then reappears when horizontal."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp3p0wwo-GM3_Applying%20Layouts_Understanding_Parts_Reflow_17_IA_V01.mp4?alt=media&token=9983bea7-e42f-4bd4-bbd6-bbfb982759fb>)

### Levitate

Panes can be elevated above other content as **floating** or **docked** panes. This strategy helps panes appear relative to their triggers.

Floating panes:

-   Appear in front of the body content

-   Can be customized to be dragged or resized


When adding controls that resize or move a floating pane, provide accessible controls.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp3p3knn-GM3_Applying%20Layouts_Understanding_Parts_Reflow_18_IA_v01.mp4?alt=media&amp;token=801d93cf-65bb-47b1-96dc-d92f5dcf370e" title="1 pane floats on top of the other when the device changes to a vertical orientation."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp3p3knn-GM3_Applying%20Layouts_Understanding_Parts_Reflow_18_IA_v01.mp4?alt=media&token=801d93cf-65bb-47b1-96dc-d92f5dcf370e>)

On large screens:

-   Floating panes are the default

-   The scrim behind a floating pane is optional

![2 ways of showing floating panes on large screens, with and without a scrim.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp3p9uz9-20.png?alt=media&token=7fab1453-4041-4c55-9e7b-e6390c603dcf>)

_Floating pane with a scrim; Floating pane without a scrim_

Docked panes are usually at the bottom of the window, like a bottom sheet.

At medium and expanded breakpoints, docked panes can adapt into floating panes.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp3rcrfk-GM3_Applying%20Layouts_Understanding_Parts_Bottom%20Sheet%20to%20Floating%20Pane_19_IA_v01.mp4?alt=media&amp;token=bceb6a76-5826-4102-abe8-39911dcf4fcd" title="A docked pane on mobile turning into a floating pane on desktop."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp3rcrfk-GM3_Applying%20Layouts_Understanding_Parts_Bottom%20Sheet%20to%20Floating%20Pane_19_IA_v01.mp4?alt=media&token=bceb6a76-5826-4102-abe8-39911dcf4fcd>)

Alternatively, at medium and expanded breakpoints, a docked pane can adapt into a co-planar pane.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp3rfkvn-GM3_Applying%20Layouts_Understanding_Parts_Bottom%20Sheet%20to%20Co-planar%20Pane_20_IA_v01.mp4?alt=media&amp;token=17ef48a8-0b49-4cd6-8582-61c35f24f80d" title="A docked pane on the lower half of a mobile screen changes to a co-planar pane on a larger screen."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp3rfkvn-GM3_Applying%20Layouts_Understanding_Parts_Bottom%20Sheet%20to%20Co-planar%20Pane_20_IA_v01.mp4?alt=media&token=17ef48a8-0b49-4cd6-8582-61c35f24f80d>)

On large screens, consider changing docked panes into co-planar panes.

![A docked pane on mobile and a co-planar pane on a tablet.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp3riptw-23.png?alt=media&token=292b9430-835a-4518-94f0-e1172ff96b6a>)

_A compact screen can have a docked pane; On a large screen, it should change to a co-planar pane_

### Reflow

Panes can be reorganized on screen as the breakpoint or orientation changes, also known as reflow.

For example, in a vertical orientation, the supporting pane can move underneath the primary pane.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp3skf28-GM3_Applying%20Layouts_Understanding_Parts_Reflow_23_IA_v01.mp4?alt=media&amp;token=7df7dd3b-c257-4705-9d84-dfa333cf7ee0" title="A supporting pane changes orientation and location as the screen changes orientation. "></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp3skf28-GM3_Applying%20Layouts_Understanding_Parts_Reflow_23_IA_v01.mp4?alt=media&token=7df7dd3b-c257-4705-9d84-dfa333cf7ee0>)

Reflow also applies to breakpoints. When there’s not enough horizontal space for panes, they can stack vertically instead.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp3smk3l-GM3_Applying%20Layouts_Understanding_Parts_Reflow%20wsc%20Change%2024_%20IA_v01.mp4?alt=media&amp;token=eae1b187-6655-48dc-9436-c8a9c1ae9fb2" title="A supporting pane on the right side of a large horizontal screen moves to the bottom of a vertical small screen."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp3smk3l-GM3_Applying%20Layouts_Understanding_Parts_Reflow%20wsc%20Change%2024_%20IA_v01.mp4?alt=media&token=eae1b187-6655-48dc-9436-c8a9c1ae9fb2>)

## Spatial panels

On XR devices, pane layouts can be presented in disconnected spatial panels. These panels must have clear containment to make them easy to see on any background.

The content in a spatial panel can use implicit grouping when the pane has an explicit container to distinguish it from the environment.

![2-pane layout in XR with implicit content grouping and a virtual reality background. ](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp3sp9gc-07-key01.png?alt=media&token=f06756e6-8045-4f55-8f3a-8b0874db7445>)

_When a pane uses explicit containment, content can use implicit grouping_

## Accessibility considerations

**Coplanar panes**

-   The focus order should match the visual arrangement of the panes on screen


**Floating panes**

Modal floating pane:

-   When active, the elements behind it can’t be interacted with

-   Focus moves automatically to the first element in the pane, and when the pane is closed, focus moves back to the element that triggered it, like a dialog

-   If triggered automatically, focus should still move to it, but when it’s closed, focus should go to the next most logical element on screen

-   It disappears when a person interacts with something behind it.


Non-modal floating pane:

-   When open, other parts of a product can be interacted with

-   Focus should be able to move to and from the pane

-   The pane should be available in a logical reading order of the screen


**Docked panes**

-   Have the same focus requirements as modal and non-modal panes

-   The focus order should match the visual arrangement of the panes on screen

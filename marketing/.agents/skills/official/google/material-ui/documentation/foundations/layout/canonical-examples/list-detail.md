---
title: "Canonical layout examples"
source_url: "https://m3.material.io/foundations/layout/canonical-examples/list-detail"
lastmod: "2026-05-19"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "foundations"
assets:
  - "assets/images/6bef93607929-mp7394ub-LD-adapt-C.png"
  - "assets/images/b5a2a03c075d-mp52v68p-02.png"
  - "assets/images/506748044188-mp52wfuu-03.png"
  - "assets/images/82b2d8142cf1-mp533vzf-04.png"
  - "assets/images/e4ea757937b9-mp535cbt-05.png"
  - "assets/images/32747cdc9058-mp537scf-06.png"
  - "assets/images/2e9606795ed9-mp538uu5-07.png"
  - "assets/images/168507122a3f-mp53b085-08.png"
  - "assets/videos/83b04d0e98bd-mp53duna-lxygxf8i-Canonical_List-Detail_1.mp4"
  - "assets/videos/e3892d9e2f94-lxyh497r-Canonical_List-Detail_2.mp4"
  - "assets/videos/2b42672508ac-mp54burn-lxyh80t8-Canonical_List-Detail_3.mp4"
  - "assets/videos/e348a540a984-lxyhcqmk-Canonical_List-Detail_4.mp4"
  - "assets/videos/98a2015878e0-mp54fy4m-lxyhgczm-Canonical_List-Detail_5.mp4"
---

# Canonical layout examples

Canonical layout examples are designs for common screen layouts across all breakpoints

## List-detail

Many layouts can be split into a list view and a detail view.

Key use cases for this layout include parent-child pairings of information like:

-   Text message + conversation

-   File browser + open folder

-   Musical artist + album detail

-   Settings + category detail

-   Email inbox + selected email

![An email app in a list-detail layout at a medium breakpoint.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp7394ub-LD%20adapt%20C.png?alt=media&token=d4ee0120-592a-47e3-b820-7180650f7da8>)

_List; Detail_

## Usage

Use the list-detail layout for quickly accessing details of an item from a long list of content.  

Examples include:

-   Showing a series of conversations and a text message

-   Browsing files and seeing their details

-   Browsing multiple albums and seeing individual track information

![Several stacked cards make up the list area on the left pane, while the detail area is a single section on the right pane.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp52v68p-02.png?alt=media&token=4173a4a5-6f51-4639-8404-0ef3711a22ec>)

_Simplified diagram of: List area; Detail area_

### Dividing space

![Compact windows have 1 pane, while medium and expanded windows can have 2 panes for list-detailed views.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp52wfuu-03.png?alt=media&token=9535e2cb-8fe9-4f55-87cc-c2f55b2ef300>)

_The most basic list-detail views for compact, medium, and expanded layouts_

A list-detail layout uses two panes. Depending on the breakpoint, the two panes may appear together in the same layout or across separate layouts.  

List-detail layouts use the same pane guidance as all single and two-pane layouts, including special behavior for foldables.

| **Breakpoint (dp)** | **Visible panes** |
| --- | --- |
| Compact (0-599) | 1 pane |
| Medium (600-839) | 1 (recommended) or 2 panes |
| Expanded (840+) | 2 panes |
| Large (1200-1599) | 2 panes |
| Extra-large (1600+) | 2 panes |

## Across breakpoints

### Compact

-   Use a single-pane layout

-   Only one view is visible at a time, either list or detail

![Single-pane layout on 3 devices at compact breakpoints.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp533vzf-04.png?alt=media&token=7241c2e9-126a-487b-b74d-e20d62d16fa6>)

_Phone in portrait orientation; Closed foldable; Tablet in split-screen mode_

### Medium

-   Use a single-pane layout for information-dense content or deep focus

![Single-pane layout on a foldable open flat and a tablet in portrait orientation.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp535cbt-05.png?alt=media&token=e6487d36-75c2-482d-bd8b-fec3cde0306d>)

_Foldable open flat; Tablet in portrait orientation_

-   Use a two-pane layout to browse collections and switch between items quickly

-   To maximize horizontal space for two-pane layouts, use a bottom navigation bar or modal navigation rail

![2-pane layout on a foldable open flat and a tablet in portrait orientation.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp537scf-06.png?alt=media&token=5cf94d50-364d-41d7-b069-67aec1ac338d>)

_Foldable open flat; Tablet in portrait orientation_

### Expanded, large, & extra-large

-   Use a two-pane layout

![2-pane layout on a phone and tablet, both in landscape orientation.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp538uu5-07.png?alt=media&token=3348b4ce-14f4-4bc3-ae8b-8bb5a4851f0c>)

_Phone in landscape orientation; Tablet in landscape orientation_

## Behavior

### Single vs two-pane

-   Back button: Appears in detail view only for single-pane layouts

-   Selected state: Appears only in list view for two-pane layouts

-   Visual focus: Use [explicit and implicit grouping](../grids-spacing/spacing.md#e7e6d1ac-031a-4757-afcf-b223f23654ea) to direct focus in two-pane layouts

![A 2-pane layout shows a selected list item. A single-pane layout uses a Back button to return to the list.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp53b085-08.png?alt=media&token=ee7424e2-9c1c-4669-93cf-2f7aae5b4409>)

_Use a selection state in two-pane layouts only; Use a back button in single-pane layouts only_

### Transitioning between layouts

The amount of available space is dynamic and changes based on how someone uses a device, such as rotating or unfolding it, or entering multi-window mode.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp53duna-lxygxf8i-Canonical_List-Detail_1.mp4?alt=media&amp;token=0f0919ed-8fc8-4fac-8c81-57a08a618eb4" title="Device rotating from landscape to portrait mode, reducing layout panes from 2 to 1."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp53duna-lxygxf8i-Canonical_List-Detail_1.mp4?alt=media&token=0f0919ed-8fc8-4fac-8c81-57a08a618eb4>)

#### No selected list item

A single-pane layout shows a list view, while a two-pane layout shows placeholder content in the detail pane.

In some cases, such as multi-select, the most recently used pane should stay visible when switching to single-pane layout.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flxyh497r-Canonical_List-Detail_2.mp4?alt=media&amp;token=94cbed39-a6cb-4c46-b544-12d7e209208b" title="No item selected on a folded device. When unfolded, the detail pane is empty."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flxyh497r-Canonical_List-Detail_2.mp4?alt=media&token=94cbed39-a6cb-4c46-b544-12d7e209208b>)

#### Selected list item

When switching from a single- to two-pane layout, both panes should be shown and the selected item’s details are visible.

When going from a two- to single-pane layout, the view depends on the product:

-   The detail pane should typically show in a single-pane layout, and an app bar appears

-   If the product supports selection without deep navigation, like multi-select, the list view can show with the item selected

-   Consistency is key: If a layout showed the list view previously, it should return to that view when returning to a single pane

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp54burn-lxyh80t8-Canonical_List-Detail_3.mp4?alt=media&amp;token=0b4b9578-2ef6-4360-ab63-5a050c98377c" title="Item selected on a folded device. When unfolded, the detail view shows the item."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp54burn-lxyh80t8-Canonical_List-Detail_3.mp4?alt=media&token=0b4b9578-2ef6-4360-ab63-5a050c98377c>)

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flxyhcqmk-Canonical_List-Detail_4.mp4?alt=media&amp;token=9349512d-8ecc-414e-a56d-f953992823b4" title="Item selected on an unfolded device. When folded, only the detail view shows."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flxyhcqmk-Canonical_List-Detail_4.mp4?alt=media&token=9349512d-8ecc-414e-a56d-f953992823b4>)

#### Persistent states

In most cases, a state should be saved when navigating between detail views. This includes read and unread content.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp54fy4m-lxyhgczm-Canonical_List-Detail_5.mp4?alt=media&amp;token=bf7772c3-a3e8-49b5-8652-a3e1914eaf41" title="Scroll position is kept after folding or unfolding the device."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp54fy4m-lxyhgczm-Canonical_List-Detail_5.mp4?alt=media&token=bf7772c3-a3e8-49b5-8652-a3e1914eaf41>)

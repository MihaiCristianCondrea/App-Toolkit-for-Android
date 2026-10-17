---
title: "FAB menu"
source_url: "https://m3.material.io/components/fab-menu/guidelines"
lastmod: "2026-05-18"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "components"
assets:
  - "assets/images/2eb10d966ff3-m0al2scq-01.png"
  - "assets/images/9433aaa1904c-m0al39xa-02.png"
  - "assets/images/f50661d3e109-m0al3tfx-03.png"
  - "assets/images/713619816c4e-m0al46l0-04.png"
  - "assets/images/1a6c163bdffc-m0al4jux-05.png"
  - "assets/images/58bb18f30f15-m0al73q3-06.png"
  - "assets/images/611705d18f5d-m0al7run-07.png"
  - "assets/images/b9a38401d6bf-m0al91ki-08.png"
  - "assets/images/71bd24d5beee-m0alljh0-09.png"
  - "assets/images/ca8583397b51-m0almfyg-10.png"
  - "assets/images/c496a8e96df6-m0alqq5m-11.png"
  - "assets/images/c494663cbbe8-m0alqzgn-12.png"
  - "assets/images/a70cb0759447-m0feodak-13.png"
  - "assets/images/394c35380e2b-m0alssof-14.png"
  - "assets/images/13dc75eaf5c0-m0alt3lp-15.png"
  - "assets/images/b4c974dd73fd-m0altd3s-16.png"
  - "assets/videos/149e8df4108f-mc0tzxol-GM3_Expressive_FAB_Guidelines_FAB-Menu-D_v01.mp4"
  - "assets/images/329cdcdeb0cc-m0alwg82-18.png"
  - "assets/images/491875f41ffa-mn7ozpxv-18-Web-vs.-Mobile-3P.png"
  - "assets/videos/db5421a4aa0c-m23g9t73-FAB-Menu-A-1.mp4"
  - "assets/images/95ea9718780b-m4hkgdmm-21.png"
  - "assets/images/22d9f207d48b-m4hkgqnr-22.png"
  - "assets/videos/af87fd2da5e1-m0fevuhp-FAB-Menu-B.mp4"
  - "assets/videos/fa6ab6e43055-m0few8ot-FAB-Menu-C.mp4"
---

# FAB menu

The floating action button (FAB) menu opens from a FAB to display multiple related actions

## Guidelines

![On a page of music albums, a FAB menu shows options to make a new playlist, collection, or station.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0al2scq-01.png?alt=media&token=933b5d53-2e43-42c9-8f5b-6dd54b507ed1>)

_Use the FAB menu to show multiple related actions in a prominent, expressive style_

## Usage

A FAB menu opens from a FAB to show multiple related actions. It should always appear in the same place as the FAB that opened it.

This makes actions immediately accessible, and keeps the UI clean by concealing actions when they’re not needed.

Don’t open a FAB menu from an extended FAB or any other component.

![1 mobile screen with a FAB, 1 with a FAB menu. Both are right aligned.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0al39xa-02.png?alt=media&token=8593c439-72f3-4e9a-bab4-31fb9b508f96>)

_The FAB menu should always open from a FAB_

The FAB menu should be aligned to the trailing edge of the window. 

In right-to-left (RTL) languages, this means the FAB and FAB menu should be aligned to the left edge, and the layout of elements should be mirrored.

![1 mobile screen with a FAB, 1 with a FAB menu. Both are left aligned and mirrored for a right-to-left language.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0al3tfx-03.png?alt=media&token=5f6b948a-7cfb-433e-b36c-e6039479f05f>)

_In RTL languages, the FAB menu should be left-aligned with the icon and text placement mirrored_

FAB menus can contain 2–6 items. These should be closely related under a single action, like **Share**. 

Avoid grouping unrelated actions in the same FAB menu.

![A FAB menu with 5 options on a photo gallery UI.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0al46l0-04.png?alt=media&token=99e07baf-b16a-429a-8bd2-f577e1f0ab03>)

_FAB menus can have 2-6 items_

![A FAB menu with 1 option on a photo gallery UI.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0al4jux-05.png?alt=media&token=4c31fc95-1876-44d9-bd9a-74dfa92d7e4e>)

_Don’t use a FAB menu with one item_

When a FAB is paired with other components, like the floating toolbar or navigation rail, don’t use the FAB menu.

![A toolbar with a FAB directly next to it.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0al73q3-06.png?alt=media&token=7530f997-8c27-4487-b57b-d0257454ef76>)

_FABs can be placed next to toolbars_

![A toolbar with a FAB menu next to it.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0al7run-07.png?alt=media&token=35de4220-c99b-4cdf-9903-cd4c4df92f87>)

_Don’t add a FAB menu to a FAB next to a toolbar_

### Color sets

FAB menus have three color sets: primary, secondary, and tertiary. Use the color set that best matches the FAB color style.   

Use the primary FAB menu color set with the **primary** or **primary container** FAB color styles.

![A FAB menu using the primary color set. ](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0al91ki-08.png?alt=media&token=ef35ad96-9369-44fa-8547-d3df63456e56>)

_A primary FAB is paired with a primary FAB menu_

Use the secondary FAB menu color set with the **secondary** or **secondary container** FAB color styles.

![A FAB menu using the secondary color set.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0alljh0-09.png?alt=media&token=ed402cb2-4a9b-4116-9562-9e3805d5d8f6>)

_A secondary FAB is paired with a secondary FAB menu_

Use the tertiary FAB menu color set with the **tertiary** or **tertiary container** FAB color styles.

![A FAB menu using the tertiary color set.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0almfyg-10.png?alt=media&token=67b04c16-7eeb-47fa-86c1-4ed567e43115>)

_A tertiary FAB is paired with a tertiary FAB menu_

## Anatomy

![2 elements of a FAB menu.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0alqq5m-11.png?alt=media&token=e0479ac2-8344-4f44-8bb0-c566b5231015>)

_Close button; List item_

FAB menu items should always have label text. The icons shouldn’t be removed since they make each item easy to identify.

![A FAB menu with 3 options for selecting Food, People, or Nature. There are no icons next to the text.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0alqzgn-12.png?alt=media&token=9f30f3f1-7e25-49f9-be70-457be0bed35f>)

_Only remove the icon if necessary. The icon provides a differentiation between items._

![A FAB menu with 3 options for selecting Food, People, or Nature. The options are only icons, no text.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0feodak-13.png?alt=media&token=eec6074a-71e9-47d1-96ad-fea96e8f63a7>)

_Don’t remove the label_

The list item should always hug its contents and look consistent. Avoid truncating text or setting fixed widths. All FAB menu elements should be rounded.

![A FAB menu used out of the box with no configurations.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0alssof-14.png?alt=media&token=83c41c88-207b-4692-b117-89a41eb96f38>)

_Keep the padding between the container and icon, icon and text, and text and container consistent_

![FAB menu items are equal width despite having different lengths of text.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0alt3lp-15.png?alt=media&token=f8796c60-e6ec-48f7-9ff9-255bb1a6fa66>)

_Don’t expand container sizes_

![FAB menu items are square instead of round.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0altd3s-16.png?alt=media&token=911d5b4e-aa6b-4f62-a85c-b64c402bb265>)

_Don’t change FAB menu shapes_

## Adaptive layout

The FAB menu can open from any sized FAB. Use with a FAB size suitable for the window size class. For example, larger FABs are recommended for larger windows.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmc0tzxol-GM3_Expressive_FAB_Guidelines_FAB-Menu%20D_v01.mp4?alt=media&amp;token=9f57a56c-c58c-4a79-920e-5b21485d655a" title="The same FAB menu used in medium and compact window sizes."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmc0tzxol-GM3_Expressive_FAB_Guidelines_FAB-Menu%20D_v01.mp4?alt=media&token=9f57a56c-c58c-4a79-920e-5b21485d655a>)

The FAB menu should remain anchored to the same corner or edge regardless of window size. 

In large and extra large windows, the FAB and FAB menu margins should increase from 16dp to 24dp.

![A FAB menu with 24dp margins from the edge of the window.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0alwg82-18.png?alt=media&token=4fbeae7c-3a0b-43ab-9dde-fa6fdaf5fa93>)

_On desktop, use larger FABs and margins /_

On web, the FAB menu uses a menu component for an experience that's consistent with other desktop apps.

![A FAB menu using menu component on web and traditional FAB menu on compact screen.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmn7ozpxv-18%20-%20Web%20vs.%20Mobile%20-%203P.png?alt=media&token=acc5a489-c566-4633-901b-093de2973a0c>)

_The same FAB menu options on both large window (left) and an Android compact window (right)_

## Behavior

### Appearing

The FAB should transform into the close button of the FAB menu. The menu items should appear using the [enter and exit](../../styles/motion/transitions/transition-patterns.md#e1c2a650-d7a4-4a6d-9025-e6b7845291ed) transition.

Originate the transition from one of the FAB's trailing corners, preferably the top-aligned corner.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm23g9t73-FAB-Menu-A-1.mp4?alt=media&amp;token=bdf9b02f-b3a7-403e-854b-e1866dc67724" title="A FAB transforms into a FAB menu while anchoring the animation to the top right corner of the FAB."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm23g9t73-FAB-Menu-A-1.mp4?alt=media&token=bdf9b02f-b3a7-403e-854b-e1866dc67724>)

To ensure accessibility for keyboard users on the web, avoid positioning the FAB menu to completely obscure the focus indicator of an actionable element. 

Partially covering the desired element is fine, as long as the focus indicator is visible.

![FAB menu doesn’t obscure actionable element and its focus indicator.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm4hkgdmm-21.png?alt=media&token=f82bf9f8-0945-44a8-b5b9-644fecd6c509>)

_Ensure the actionable element and its focus indicator are visible behind the FAB menu_

![FAB menu obscures both an actionable element and its focus indicator.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm4hkgqnr-22.png?alt=media&token=c61ae0da-15e0-4c67-8ed2-e27877fd51d5>)

_Don’t block an actionable element and its focus indicator completely with the FAB menu_

### Scrolling

When window height is limited, like when viewing phones in horizontal orientation, FAB menu items can scroll. 

The items should scroll behind the close button.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0fevuhp-FAB-Menu-B.mp4?alt=media&amp;token=30bc4800-73ce-4680-9067-a006d8713410" title="A FAB menu with 6 items scrolls off screen on a horizontal-oriented device. Scrolled items move behind the close button."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0fevuhp-FAB-Menu-B.mp4?alt=media&token=30bc4800-73ce-4680-9067-a006d8713410>)

### Expanding

Any FAB menu item can expand and adapt to any shape using a [container transform](../../styles/motion/transitions/transition-patterns.md#b67cba74-6240-4663-a423-d537b6d21187) transition pattern. This includes a surface that is part of the app structure, or a surface that spans the entire screen.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0few8ot-FAB-Menu-C.mp4?alt=media&amp;token=3017a1d5-9e41-47dc-b66a-982e4e1e35db" title="A FAB menu item expands and transforms into a full screen dialog."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0few8ot-FAB-Menu-C.mp4?alt=media&token=3017a1d5-9e41-47dc-b66a-982e4e1e35db>)

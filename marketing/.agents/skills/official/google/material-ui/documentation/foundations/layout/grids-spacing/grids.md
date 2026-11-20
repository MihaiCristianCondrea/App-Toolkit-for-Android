---
title: "Grids &amp; spacing"
source_url: "https://m3.material.io/foundations/layout/grids-spacing/grids"
lastmod: "2026-05-15"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "foundations"
assets:
  - "assets/images/bc2847d8da88-mp54500h-03.png"
  - "assets/images/f8fb942cbc96-mp54dby8-04.png"
  - "assets/images/186c1e2c9226-mp54ejyh-05.png"
  - "assets/images/a0bacf544f4a-mp55v0r6-06.png"
  - "assets/images/7b99d1c77ba7-mp55w2g9-07.png"
  - "assets/images/12e546757c11-mp55yqda-08.png"
  - "assets/images/d0b9b7553bdd-mp56n0pg-09.png"
  - "assets/images/327557c0ee27-mp573btr-10.png"
  - "assets/images/10e51b980f57-mp574ab3-11-key02-TO-PUBLISH.png"
  - "assets/images/55ce23cb0b81-mp575wo3-12-key02-TO-PUBLISH.png"
  - "assets/videos/9ecb64637d72-mp5778zb-GM3-Layout-Grids-Spacing-13-Ruler-Hero-IA-v02.mp4"
  - "assets/videos/1d7f045dc5d9-mp578joa-GM3-Layout-Grids-Spacing-14-Ruler-Options-IA-v02.mp4"
  - "assets/images/14d999b5ee88-mp579mwo-15.png"
---

# Grids &amp; spacing

Grids and spacing organize content and actions for any layout

## Grids

-   Layouts in Material are based on a grid that adapts across all breakpoints (previously window size classes)

-   Parts of the layout scaffold like rails and panes are positioned on this grid to create consistent adaptive layouts

-   The structure and spacing values used in a grid can add personality to a product’s layout

## How to use grids

### Start with placing grid columns

Grids adapt across breakpoints. As the size increases, column count, width, and spacing change as well.

![A mockup of grid columns, showing compact, medium/expanded, and large/extra-large breakpoints.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp54500h-03.png?alt=media&token=2dcb98ec-a57d-4d9b-a4f9-44227e456977>)

_The number and size of columns changes based on breakpoints_

When moving between sizes, column count may increase to show more content or controls.

![A course listing on a compact screen, with 4 columns.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp54dby8-04.png?alt=media&token=04b776f7-e5bc-4a68-9143-464df6e33251>)

_On compact screens, fewer columns are used to create a focused layout_

![A course listing on a foldable screen, with 8 columns.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp54ejyh-05.png?alt=media&token=acde1184-80ae-4066-853d-4e2e21443b45>)

_As screen size increases, for example when a foldable screen is unfolded, additional columns allow for a richer layout_

### Place bars & rails

Populate regions of the layout scaffold that are closest to the edges of the screen’s usable space first. This may include:

-   Bars like the navigation bar and rail

-   Components like toolbars and app bars

![A compact screen with a toolbar highlighted.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp55v0r6-06.png?alt=media&token=06b7ed36-86b6-4a5f-9e72-3d43ed36524f>)

_The bar region can contain a toolbar_

![A large screen with a leading navigation rail highlighted.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp55w2g9-07.png?alt=media&token=6bc41919-a8f2-481a-8bad-f463479cdfb8>)

_The rail region on larger screens usually contains a navigation rail_

### Place panes

Next, populate the main region of the screen with panes with content and components, based on available space and structure.

See the [canonical layout examples](../canonical-examples/feed.md) for ideas on which panes are appropriate for a product.

![Mobile UI with 1 pane. Foldable UI with 2 panes in a supporting pane layout.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp55yqda-08.png?alt=media&token=2b0b4c87-9448-4a1a-b576-55c2a08e20ba>)

_Primary pane; Supporting pane_

## Rulers & alignment

Rulers are a set of recommended global alignment lines that help create consistent focal points in a product, while keeping content and components consistently aligned.

[How to implement rulers in Compose](https://developer.android.com/reference/kotlin/androidx/compose/ui/layout/Ruler)

![1 compact and 1 desktop UI mapping rulers.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp56n0pg-09.png?alt=media&token=aaa37678-f50e-45b3-a64d-09d126bfb433>)

_Margin; Bar or safety region; Title; Content 1; Content 2; Content 3; Content 4; Bar or safety region; Rail_

### Bar & safety rulers

Bar and safety rulers reserve space for [system UI](https://developer.android.com/training/system-ui) elements like the status bar and gesture navigation.

They ensure actionable content like app bars aren’t covered by system UI.

![2 mobile UIs showing bar and safety rulers at the top and bottom.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp573btr-10.png?alt=media&token=8cb7f6fd-802a-4ac4-b0f8-e78c29f4acc1>)

_Bar and safety rulers align to the edges of a screen’s usable space, providing a reference for where system UI like the status bar or gesture navigation appear_

### Title rulers

The title ruler creates consistency for the screen’s title, aligning the text, icons, and other components in an app bar.

![1 mobile and 1 desktop UI showing title rulers.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp574ab3-11-key02%20-%20TO%20PUBLISH.png?alt=media&token=24881915-d4ed-4466-9fab-8a8462a27df3>)

_The title ruler aligns with the title in an app bar_

### Content rulers

Use content rulers to align and anchor key content, such as headlines and carousels.

-   First content ruler: Emphasizes major blocks like hero images, headlines, or primary components

-   Secondary rulers: Determine where supplementary text or actions begin

![1 mobile and 1 desktop UI showing content rulers.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp575wo3-12-key02%20-%20TO%20PUBLISH.png?alt=media&token=615dfb4c-55b3-48fb-aad7-218ee4af69d6>)

_Content rulers offer flexible alignment options to help create a consistent layout across a product_

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp5778zb-GM3%20-Layout%20-%20Grids%20%20-%20Spacing%20-13%20-%20Ruler%20Hero%20-%20IA%20-%20v02.mp4?alt=media&amp;token=ba498b49-3f64-467d-bc3a-852a122cdfb8" title="Carousel and text resizing to align with content rulers, creating a structured layout."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp5778zb-GM3%20-Layout%20-%20Grids%20%20-%20Spacing%20-13%20-%20Ruler%20Hero%20-%20IA%20-%20v02.mp4?alt=media&token=ba498b49-3f64-467d-bc3a-852a122cdfb8>)

### Ruler options

Margin rulers come with some wiggle room to determine how tight or loose a product’s content feels on-screen. The standard ruler can be adjusted to the left or right.

Choosing a narrower or wider margin can create or remove negative space, or create expressive moments in a content-forward product.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp578joa-GM3%20-Layout%20-%20Grids%20%20-%20Spacing%20-14%20-%20Ruler%20Options%20-%20IA%20-%20v02.mp4?alt=media&amp;token=88e11830-1df2-4dfd-89ad-c0a17dd90a99" title="Mobile UI showing a recipe layout where text margins narrow while the hero photo expands."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp578joa-GM3%20-Layout%20-%20Grids%20%20-%20Spacing%20-14%20-%20Ruler%20Options%20-%20IA%20-%20v02.mp4?alt=media&token=88e11830-1df2-4dfd-89ad-c0a17dd90a99>)

Rulers can also be used to create more immersive experiences. For example, a photo grid can take the full width of the screen, while components like search use wider margins.

![Mobile UI for a photo app showing a full-width image grid and a search bar with wide margins.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp579mwo-15.png?alt=media&token=38c06930-bb63-44ef-8ff7-ec7d3a3d49af>)

_Rulers allow components and media to use different margin widths_

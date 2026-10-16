---
title: "XR components"
source_url: "https://m3.material.io/foundations/xr/components/toolbars"
lastmod: "2026-05-18"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "foundations"
assets:
  - "assets/images/7cfdd50a69a7-mp6grqe5-hero-image.png"
  - "assets/images/bc90c052059c-mp5sscqe-01-1P.png"
  - "assets/images/fdd5cf39613e-mp5stimq-02-1P.png"
  - "assets/images/f36166785aec-mp5sum97-03-1P.png"
  - "assets/images/5886db3515f8-mp5svaqo-04-1P.png"
  - "assets/images/228a0853718e-mp5sw649-05-1P.png"
  - "assets/videos/e3b95b637861-moxe8vd0-toolbars-xr-06-(1).mp4"
  - "assets/videos/447016b954d7-mp0wl8ix-toolbars-xr-07.mp4"
  - "assets/videos/b249e5ea1ec0-mp0woole-toolbars-xr-08.mp4"
  - "assets/videos/3b02bd720ae7-mp0wrkvj-toolbars-xr-09.mp4"
  - "assets/videos/7b1b5b93053a-mp0wtnt4-toolbars-xr-10.mp4"
  - "assets/images/279e697bcf32-moxer5rt-11-do.png"
  - "assets/images/346a56ff90a0-moxetpx5-12-don't.png"
  - "assets/images/9d00652200bd-moxewjln-13-do.png"
  - "assets/images/4dda7b35f34d-moxeyben-14-don't.png"
  - "assets/images/5a26b390a5a7-moxf0mbw-15-do.png"
  - "assets/images/524eb42b00e8-memfyo8f-16-don't.png"
  - "assets/images/1945362a818c-memfvpq3-17.png"
  - "assets/images/e662900d9eb2-memfx943-18-don't.png"
---

# XR components

Learn how Material 3 Expressive components adapt to extended reality devices

![XR components](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmp6grqe5-hero%20image.png?alt=media&token=77848b44-f2d9-4c6e-a51a-d4807d7ea9e4>)

## Toolbars

star

Note:

XR guidelines are primarily intended for designers. Find what’s implemented in code in the [design kit](https://www.figma.com/community/file/1035203688168086460).

Extended reality (XR) interfaces have special design requirements, like showing apps in 3D space. Material has an XR toolbar with custom specs and guidance. Read [XR developer documentation](https://developer.android.com/design/ui/xr/guides/foundations) for more details.

## Variants

There is one toolbar orbiter. It closely aligns with the floating toolbar. It can be configured to be horizontal or vertical. [Go to XR toolbar API reference](https://developer.android.com/reference/kotlin/androidx/xr/compose/material3/package-summary#HorizontalFloatingToolbar\(kotlin.Boolean,androidx.compose.ui.Modifier,androidx.compose.material3.FloatingToolbarColors,androidx.compose.foundation.layout.PaddingValues,androidx.compose.material3.FloatingToolbarScrollBehavior,kotlin.Function1,kotlin.Function1,kotlin.Function1\))

![Horizontal and vertical toolbar orbiters.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmp5sscqe-01-1P.png?alt=media&token=bdd0c440-0cd1-4d30-bd53-b36dead8cbc0>)

_Horizontal floating toolbar; Vertical floating toolbar_

## Anatomy

![2 elements of a toolbar orbiter: container and placed components. ](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmp5stimq-02-1P.png?alt=media&token=faf847b2-0a96-4506-b5e5-755d7eecdf08>)

_Container; Placed components_

## Color & elevation

XR uses color to communicate the elevation of UI elements and orbiters. With [spatial elevation](https://developer.android.com/design/ui/xr/guides/spatial-ui#spatial-elevation), the toolbar displays above the spatial panel on the Z-axis. Elevated toolbars can use any of these color options:

![4 versions of toolbar elevation color strategy.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmp5sum97-03-1P.png?alt=media&token=f8d0d1a2-d67a-4f4a-88f8-a6ec5c184fa9>)

_Surface container; Surface container high; Surface container highest; Tertiary container_

## Measurements

![Diagram with measurements for toolbar orbiters.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmp5svaqo-04-1P.png?alt=media&token=92f20043-78db-4d1b-b604-d9e00a2bc9bc>)

_Measurements for toolbar orbiters_

![Diagram with 12dp padding for toolbar orbiters.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmp5sw649-05-1P.png?alt=media&token=ac0dda53-ac9a-4d64-b735-6d78144c7dbe>)

_Padding for toolbar orbiters_

## Usage

A toolbar can appear in an orbiter for a more immersive experience. Currently, this spatial capability is only available in full space. In home space, use a regular toolbar on the same plane as the body content to mimic a 2D experience.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmoxe8vd0-toolbars-xr-06%20(1).mp4?alt=media&amp;token=2824457a-7cee-4649-801e-79cffd355aae" title="Animation showing a toolbar changing from 2D to 3D."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmoxe8vd0-toolbars-xr-06%20(1).mp4?alt=media&token=2824457a-7cee-4649-801e-79cffd355aae>)

## Behavior

### Local context (recommended)

When placed in local context, the toolbar orbiter is centered at the bottom of the spatial panel it controls. 

It repositions in response to layout or content changes.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp0wl8ix-toolbars-xr-07.mp4?alt=media&amp;token=d27f646b-5bb6-4bfd-9e58-4b6cd3f6546f" title="A toolbar orbiter placed in local context."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp0wl8ix-toolbars-xr-07.mp4?alt=media&token=d27f646b-5bb6-4bfd-9e58-4b6cd3f6546f>)

### Global context

When placed in global context, the toolbar orbiter is centered at the bottom of the app. 

It stays anchored to the app during layout or content changes.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp0woole-toolbars-xr-08.mp4?alt=media&amp;token=15aa6f6b-377c-4aae-a7a2-a3af70f09055" title="A toolbar orbiter placed in global context."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp0woole-toolbars-xr-08.mp4?alt=media&token=15aa6f6b-377c-4aae-a7a2-a3af70f09055>)

### Expand & collapse

Toolbar orbiters with more than five items can expand and collapse to reveal or hide additional content.

When a toolbar orbiter expands, it stays within the bounds of the adjacent spatial panel.

Alternatively, more complex toolbars can be split into multiple toolbars.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp0wrkvj-toolbars-xr-09.mp4?alt=media&amp;token=6b1d51a9-3408-4ac9-bd5a-f204de662100" title="A spatial panel with a Google document has a toolbar orbiter that expands from 5 to 10 items."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp0wrkvj-toolbars-xr-09.mp4?alt=media&token=6b1d51a9-3408-4ac9-bd5a-f204de662100>)

### Additional toolbars

In some cases, full space apps can have more than one toolbar orbiter, placed in either global or local context.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp0wtnt4-toolbars-xr-10.mp4?alt=media&amp;token=fa37073a-17df-44b4-9443-913f6e4f2f09" title="An app switches between 1 and 2 toolbar orbiters."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp0wtnt4-toolbars-xr-10.mp4?alt=media&token=fa37073a-17df-44b4-9443-913f6e4f2f09>)

## Placement

### Offset & inset positioning

In full space, a toolbar orbiter can be positioned adjacent to or overlap a spatial panel.

![Toolbar orbiter with offset positioning.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmoxer5rt-11-do.png?alt=media&token=e4b3858b-33c8-4780-a480-bb736654fe44>)

_The recommended toolbar orbiter position from the spatial panel is: Offset by 20dp or; Inset by 12dp; / /_

![Toolbar orbiter with inset positioning above 12dp that obstructs content on the spatial panel. ](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmoxetpx5-12-don't.png?alt=media&token=9785f369-7394-4db6-9b9f-d5cc51db2087>)

_To prevent content obstruction, don’t overlap the toolbar orbiter and spatial panel above 12dp_

### Horizontal alignment

![A horizontal toolbar orbiter placed within the bounds of its spatial panel.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmoxewjln-13-do.png?alt=media&token=532608ce-b877-416f-b725-f6e3637560d5>)

_Always align the toolbar orbiter within the horizontal bounds of nearby spatial panels_

![A horizontal toolbar orbiter that extends beyond the width of its spatial panel.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmoxeyben-14-don't.png?alt=media&token=51df328d-28b3-4aa4-9025-47ea3f674d36>)

_The toolbar orbiter shouldn’t exceed the width of adjacent spatial panels_

### Vertical alignment

![A vertical toolbar orbiter placed within the bounds of its spatial panel.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmoxf0mbw-15-do.png?alt=media&token=29cbe69d-bd06-4ace-ba38-0e7e991cf121>)

_Always align the toolbar orbiter within the vertical bounds of nearby spatial panels_

![A vertical toolbar orbiter that extends beyond the height of its spatial panel.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmemfyo8f-16-don't.png?alt=media&token=d9e5cd69-d3b6-4238-94fa-5d7dbd359c2c>)

_The toolbar orbiter shouldn’t exceed the height of adjacent spatial panels_

### Spatial panel alignment

By default, toolbar orbiters are center-aligned to the spatial panel. Their placement can be adjusted to accommodate specific user needs, such as improved ergonomics or [right-to-left (RTL) languages](../../layout/bidirectionality-rtl.md).

![Toolbar orbiter alignment options in relation to spatial panels.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmemfvpq3-17.png?alt=media&token=06472c3c-8a05-4dab-9e63-b4dac374c622>)

_Depending on the configuration (horizontal or vertical) of the toolbar orbiter, it can align to the center, left, right, top, or bottom of a spatial panel_

Avoid placing a vertical toolbar orbiter between spatial panels. 

This negatively affects the interface structure and can make it difficult to find.

![A vertical toolbar orbiter is placed between 2 spatial panels.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmemfx943-18-don't.png?alt=media&token=ce865771-93db-4ded-ac2c-f0ca46254201>)

_Don't place a vertical toolbar orbiter between spatial panels_

## Accessibility considerations

[XR accessibility](../design/accessibility.md) guidelines are still evolving. XR toolbars should follow applicable Material [toolbar accessibility standards](../../../components/toolbars/accessibility.md).

---
title: "XR components"
source_url: "https://m3.material.io/foundations/xr/components/app-bars"
lastmod: "2026-05-18"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "foundations"
assets:
  - "assets/images/7cfdd50a69a7-mp6grqe5-hero-image.png"
  - "assets/images/a0ebba1fa85e-mp5ntwab-01-3P.png"
  - "assets/images/59e939ea92f0-mp5nuh2w-02-3P.png"
  - "assets/images/e8aa0cb3decc-mp5nuy7e-03-3P.png"
  - "assets/images/bcd27dcdc07f-mp5nvfsh-04-3P.png"
  - "assets/videos/64be6eb726b4-mowvrdts-XR_Top-App-Bar_Usage_01_IA_v03-(1).mp4"
  - "assets/videos/548fbd471cb3-mowvuyn4-XR_Top-App-Bar_Behavior_01_IA_v03-(1).mp4"
  - "assets/videos/88584281581e-mowvzhjn-XR_Top-App-Bar_Behavior_02_IA_v03-(1).mp4"
  - "assets/videos/8937d31b4c7b-moww3e8a-XR_Top-App-Bar_Behavior_03_IA_v03-(1).mp4"
  - "assets/images/c9bdff6bea91-mowwbs16-09.png"
  - "assets/images/1e08c6899724-mowwe02f-10.png"
  - "assets/images/68bf136938e3-mowwevxe-11.png"
  - "assets/images/3ba2ff466f48-mowwgpq6-12.png"
  - "assets/videos/e491b1f923f6-mowxpzvt-XR_Top-App-Bar_Spatial-pane-alignment_01_IA_v03-(1).mp4"
  - "assets/images/d7e1ab16bcad-mowy6885-14.png"
  - "assets/images/8be47781a17a-mowy73ja-15.png"
  - "assets/videos/ecb827f95491-mp1yzz8i-XR_Top-App-Bar_Adaptable-width_01_IA_v03.mp4"
---

# XR components

Learn how Material 3 Expressive components adapt to extended reality devices

![XR components](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmp6grqe5-hero%20image.png?alt=media&token=77848b44-f2d9-4c6e-a51a-d4807d7ea9e4>)

## App bars

star

Note:

XR guidelines are primarily intended for designers. Find what’s implemented in code in the [design kit](https://www.figma.com/community/file/1035203688168086460).

Extended reality (XR) interfaces have special design requirements, like showing apps in 3D space. Material has an XR app bar with custom specs and guidance. See [XR developer documentation](https://developer.android.com/design/ui/xr/guides/foundations) for more details.

## Variants & configurations

There is one app bar orbiter. It closely aligns with the small app bar. It can be configured to be center-aligned or left-aligned.

![Center and left-aligned app bar orbiters.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmp5ntwab-01-3P.png?alt=media&token=2957c931-0cbd-43e0-9817-180865d9d141>)

_Center-aligned app bar; Left-aligned app bar; /_

## Anatomy

![Diagrams of app bar orbiters identifying 4 internal elements.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmp5nuh2w-02-3P.png?alt=media&token=c1c9552e-52b3-4d01-bc82-bf895d38f2b8>)

_Container; Headline; Trailing icons; Leading icon; /_

## Color & elevation

XR uses color to communicate the elevation of UI elements and orbiters. With [spatial elevation](https://developer.android.com/design/ui/xr/guides/spatial-ui#spatial-elevation), the app bar displays above the spatial panel on the Z-axis. Elevated app bars can use any of these color options:

![3 versions of app bar elevation color strategy.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmp5nuy7e-03-3P.png?alt=media&token=386a2df0-b2db-448a-b116-e43036aa6d86>)

_Surface container; Surface container high; Surface container highest_

## Measurements

![Diagrams with measurements and padding for app bar orbiters.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmp5nvfsh-04-3P.png?alt=media&token=b5844799-c20a-4a08-b821-32a0c96b685d>)

_Measurements and padding for app bar orbiters_

## Usage

An app bar can appear in an orbiter for a more immersive experience. Currently, this spatial capability is only available in full space. In home space, use a regular app bar on the same plane as the body content to mimic a 2D experience.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmowvrdts-XR_Top-App-Bar_Usage_01_IA_v03%20(1).mp4?alt=media&amp;token=e1562dba-a3c5-47c5-8684-03296fa98418" title="Animation showing an app bar changing from 2D to 3D."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmowvrdts-XR_Top-App-Bar_Usage_01_IA_v03%20(1).mp4?alt=media&token=e1562dba-a3c5-47c5-8684-03296fa98418>)

## Behavior

### Global context

When placed in global context, the orbiter is centered at the top of the app it controls. 

It stays anchored to the app during layout or content changes. 

This ensures navigation elements are always easy to find and use.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmowvuyn4-XR_Top-App-Bar_Behavior_01_IA_v03%20(1).mp4?alt=media&amp;token=fd8f18d8-abfd-40af-b56b-8ba4fae68525" title="An app bar orbiter placed in global context."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmowvuyn4-XR_Top-App-Bar_Behavior_01_IA_v03%20(1).mp4?alt=media&token=fd8f18d8-abfd-40af-b56b-8ba4fae68525>)

### Local context

When placed in local context, the orbiter is centered at the top of the spatial panel it controls. 

It repositions in response to layout or content changes.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmowvzhjn-XR_Top-App-Bar_Behavior_02_IA_v03%20(1).mp4?alt=media&amp;token=b99a382c-3120-4735-9d93-65be00ff1c0c" title="An app bar orbiter placed in local context."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmowvzhjn-XR_Top-App-Bar_Behavior_02_IA_v03%20(1).mp4?alt=media&token=b99a382c-3120-4735-9d93-65be00ff1c0c>)

### Additional app bars

In most cases, apps should only have one app bar orbiter, placed in global context.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmoww3e8a-XR_Top-App-Bar_Behavior_03_IA_v03%20(1).mp4?alt=media&amp;token=b91ce31f-fba5-4d03-af5f-0e79b831fc68" title="An app switches between 1 and 2 app bar orbiters."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmoww3e8a-XR_Top-App-Bar_Behavior_03_IA_v03%20(1).mp4?alt=media&token=b91ce31f-fba5-4d03-af5f-0e79b831fc68>)

## Placement

### Offset & inset positioning

In full space, an app bar orbiter can be positioned:

-   Offset 20dp from the spatial panel

-   Inset 12dp, overlapping spatial panel without obstructing content

![App bar orbiter with offset positioning.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmowwbs16-09.png?alt=media&token=3fb5177d-cc1e-4a78-823a-ee721def6c50>)

_An app bar orbiter with a 20dp offset creates visual separation from the spatial panel_

![App bar orbiter with inset positioning that obstructs content..](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmowwe02f-10.png?alt=media&token=8a89faa8-6e01-4e00-93c9-740a22b9eedb>)

_Don’t overlap the app bar orbiter and spatial panel more than 12dp_

### Horizontal alignment

![An app bar orbiter placed within the bounds of its spatial panel.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmowwevxe-11.png?alt=media&token=bd0e4a8c-d279-4391-bc8e-d3487772377e>)

_Always align the app bar orbiter within the bounds of nearby spatial panels_

![An app bar orbiter that extends beyond the width of its spatial panel.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmowwgpq6-12.png?alt=media&token=aaa39a97-16d1-468c-8089-2cc25f6a0bab>)

_The app bar orbiter shouldn’t exceed the width of adjacent spatial panels_

### Spatial panel alignment

By default, app bar orbiters are center-aligned to the spatial panel. Their width and placement can be adjusted to accommodate specific user needs, such as improved ergonomics or [right-to-left (RTL) languages](../../layout/bidirectionality-rtl.md).

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmowxpzvt-XR_Top-App-Bar_Spatial-pane-alignment_01_IA_v03%20(1).mp4?alt=media&amp;token=f797df52-a9ab-45c9-84d2-e80f8a0e824c" title="App bar orbiter alignment options in relation to spatial panels: left, center, and right-aligned."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmowxpzvt-XR_Top-App-Bar_Spatial-pane-alignment_01_IA_v03%20(1).mp4?alt=media&token=f797df52-a9ab-45c9-84d2-e80f8a0e824c>)

### Width boundaries

An app bar orbiter’s width should adjust to stay in a person’s [field of view](https://developer.android.com/design/ui/xr/guides/spatial-ui#where-place). 

This makes crucial navigation elements easy to find.

![An app bar orbiter with a width that fits in a person’s field of view.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmowy6885-14.png?alt=media&token=f1863e50-7df0-4855-a040-b174905b4dda>)

_Adjust the width of the app bar orbiter to fit in a person’s field of view_

It’s not recommended to increase the width of an app bar orbiter beyond a person’s natural [field of view](https://developer.android.com/design/ui/xr/guides/spatial-ui#where-place). 

This creates a visual imbalance and makes it difficult to find navigation elements.

![An app bar orbiter that exceeds the panel’s width and a person’s field of view.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmowy73ja-15.png?alt=media&token=099daf73-2edb-44d7-8d32-6bc52a374044>)

_Avoid expanding the app bar orbiter beyond the adjacent panel’s width and a person’s field of view_

### Adaptable width

When placed in a local context, an app bar orbiter can expand to the width of its adjacent spatial panel.   

Be sure the orbiter stays in a person’s field of view, and test for usability.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp1yzz8i-XR_Top-App-Bar_Adaptable-width_01_IA_v03.mp4?alt=media&amp;token=a10d0813-23ba-40f8-931f-89492e8f9d4d" title="2 app bar orbiters with the same width as their adjacent spatial panels."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp1yzz8i-XR_Top-App-Bar_Adaptable-width_01_IA_v03.mp4?alt=media&token=a10d0813-23ba-40f8-931f-89492e8f9d4d>)

## Accessibility considerations

[XR accessibility](../design/accessibility.md) guidelines are still evolving. XR app bars should follow applicable Material [app bar accessibility standards](../../../components/app-bars/accessibility.md).

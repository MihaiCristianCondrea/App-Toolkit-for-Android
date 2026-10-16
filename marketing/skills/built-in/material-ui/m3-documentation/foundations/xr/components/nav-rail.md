---
title: "XR components"
source_url: "https://m3.material.io/foundations/xr/components/nav-rail"
lastmod: "2026-05-18"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "foundations"
assets:
  - "assets/images/7cfdd50a69a7-mp6grqe5-hero-image.png"
  - "assets/images/042d8ac85fee-mp5sirou-01-3P.png"
  - "assets/images/678e989df20a-mp5sjxq7-02-3P.png"
  - "assets/images/162f45d3c74e-mp5smtp2-03-3P.png"
  - "assets/images/80a8ce289daf-mp5sl333-04-3P.png"
  - "assets/images/93f54ff4fec0-mp5slo7l-05-3P.png"
  - "assets/videos/779d62767b2f-moxcb5no-navrail_01.mp4"
  - "assets/videos/032bccbb5214-ma4acdxe-XR_Nav-Rail_Behavior_01_V02.mp4"
  - "assets/videos/713c707e31c0-ma4acsak-XR_Nav-Rail_Behavior_02_V02.mp4"
  - "assets/videos/fd06369b6f5e-moxck8bj-navrail_02.mp4"
  - "assets/videos/36379e1e2edb-moxcmy1y-navrail_03.mp4"
  - "assets/videos/62ef1801f8f5-moxcqi3i-navrail_04.mp4"
  - "assets/videos/a4d917144d36-moxd1s5j-navrail_05.mp4"
  - "assets/videos/4ca05f58933d-moxdaai7-navrail_06.mp4"
  - "assets/videos/6464b841986a-moxd8z8l-navrail_07.mp4"
  - "assets/videos/d006188a4076-moxdgsuy-navrail_08.mp4"
  - "assets/videos/5236d8aca713-m4cn0kcj-navrail_09.mp4"
---

# XR components

Learn how Material 3 Expressive components adapt to extended reality devices

![XR components](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmp6grqe5-hero%20image.png?alt=media&token=77848b44-f2d9-4c6e-a51a-d4807d7ea9e4>)

## Nav rail

star

Note:

XR guidelines are primarily intended for designers. Find what’s implemented in code in the [design kit](https://www.figma.com/community/file/1035203688168086460).

Extended reality (XR) interfaces have special design requirements, like showing apps in 3D space. Material has an XR navigation rail with custom specs and guidance. See [XR developer documentation](http://developer.android.com/design/ui/xr/guides/foundations) for more details.

## Variants

There are two variants of navigation rail orbiters: the contained FAB and spatialized FAB navigation rails.

![Navigation bar orbiters with a contained FAB and a spatialized FAB.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmp5sirou-01-3P.png?alt=media&token=9e7d6650-d675-40ff-9598-318041a54cc4>)

_Contained FAB rail; Spatialized FAB rail_

## Anatomy

![Diagram of navigation rail orbiter identifying 9 internal elements of the component.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmp5sjxq7-02-3P.png?alt=media&token=fdcd5c66-8926-47f1-8b1b-713fea02f3e4>)

_Container; Active indicator; Large badge (optional); Badge (optional); Large badge label (optional); Label text; Icon; Embedded or spatialized FAB (optional); Menu icon (optional)_

## Color & elevation

On XR, color is used to highlight elevated UI elements and orbiters. With [spatial elevation](https://developer.android.com/design/ui/xr/guides/spatial-ui#spatial-elevation), the navigation bar displays above the [spatial panel](https://developer.android.com/design/ui/xr/guides/spatial-ui#spatial-panels), on the Z-axis. Color communicates elevation on UI elements and orbiters. Elevated nav rails can use any of these color options:

![4 versions of elevation color strategy.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmp5smtp2-03-3P.png?alt=media&token=d455db2e-29cb-48be-9f31-8e8f60bb8956>)

_Surface container with tertiary FAB; Surface container high with tertiary fixed dim FAB; Surface container highest with tertiary fixed dim FAB; Tertiary container with primary FAB_

## Measurements

![Measurements and padding for navigation rail orbiter with contained FAB.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmp5sl333-04-3P.png?alt=media&token=c4c50bc2-0f1f-4955-9b4d-1fdfb6eb3d9a>)

_Navigation rail orbiter padding and measurements with contained FAB_

![Measurements and padding for navigation rail orbiter with spatialized FAB.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmp5slo7l-05-3P.png?alt=media&token=431aeed3-0cb7-4811-b04f-c759f09bb4af>)

_Navigation rail orbiter padding and measurements with spatialized FAB_

## Usage

In full space, a navigation rail can appear in an orbiter for a more immersive experience. Currently, spatial capabilities, such as orbiters, are only available in full space. In home space, use a regular navigation rail on the same plane as the body content to mimic a 2D experience.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmoxcb5no-navrail_01.mp4?alt=media&amp;token=616861d0-6177-412c-b4f4-2b9ae67b09cb" title="Interface showcasing navigation rail behavior and placement when transitioning from a 2D to 3D experience."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmoxcb5no-navrail_01.mp4?alt=media&token=616861d0-6177-412c-b4f4-2b9ae67b09cb>)

## Behavior

### Global context

Intended for global navigation, a nav rail orbiter should be centered along the left or right edge of the app it controls. It stays anchored to the app during layout or content changes to ensure controls are easy to find.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fma4acdxe-XR_Nav-Rail_Behavior_01_V02.mp4?alt=media&amp;token=9414cfde-c786-4936-831e-4a0a3d3e89cb" title="A navigation rail orbiter placed in global context."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fma4acdxe-XR_Nav-Rail_Behavior_01_V02.mp4?alt=media&token=9414cfde-c786-4936-831e-4a0a3d3e89cb>)

### Local context

Don’t place a navigation rail orbiter in local context or [between spatial panels](nav-rail.md#d965ae72-bd1b-45a9-b4d3-d1d542e72087). Local placement can make controls hard to find. Nav rails are designed for app-level navigation, so should only use the global context.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fma4acsak-XR_Nav-Rail_Behavior_02_V02.mp4?alt=media&amp;token=8a2a2f30-3484-4757-94a1-6fa7d256e6b1" title="A navigation rail orbiter placed in local context."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fma4acsak-XR_Nav-Rail_Behavior_02_V02.mp4?alt=media&token=8a2a2f30-3484-4757-94a1-6fa7d256e6b1>)

## Placement

### Navigation context

The position of the navigation rail orbiter should communicate its navigational context:

-   Use **offset positioning** for global actions that affect the overall app experience

-   Use **inset positioning** for local actions that are specific to a spatial panel


A navigation rail orbiter can either overlap or be positioned adjacent to spatial panels with a 20dp margin for visual separation.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmoxck8bj-navrail_02.mp4?alt=media&amp;token=1abcf86d-a2ef-4121-9e76-ff9848bb9fdf" title="Navigation rail orbiter offset and inset positioning."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmoxck8bj-navrail_02.mp4?alt=media&token=1abcf86d-a2ef-4121-9e76-ff9848bb9fdf>)

### Inset positioning

Don’t obstruct content. To ensure a balanced and uncluttered layout, a navigation rail orbiter should overlap spatial panels by 12dp and no more than half their width.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmoxcmy1y-navrail_03.mp4?alt=media&amp;token=155ce8ef-0eb9-41c2-97c9-82da93c89626" title="Nav rail orbiter overlapping content by more than half its width."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmoxcmy1y-navrail_03.mp4?alt=media&token=155ce8ef-0eb9-41c2-97c9-82da93c89626>)

### Vertical alignment

A navigation rail orbiter can be aligned to the top, middle, or center of spatialized panels, providing different levels of visual prominence and accessibility.

Align the navigation rail orbiter based on the specific design and user experience goals for the application.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmoxcqi3i-navrail_04.mp4?alt=media&amp;token=36f84d94-add6-482b-9145-4be5935278ec" title="Nav rail orbiter positioning moving from the top, to middle, to center of spatialized panels."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmoxcqi3i-navrail_04.mp4?alt=media&token=36f84d94-add6-482b-9145-4be5935278ec>)

The navigation rail orbiter placement shouldn't exceed the height of adjacent spatial panels.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmoxd1s5j-navrail_05.mp4?alt=media&amp;token=dda5a4c9-6862-477f-9db6-6dd3f3a304de" title="A nav rail orbiter positioning above its spatial panel."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmoxd1s5j-navrail_05.mp4?alt=media&token=dda5a4c9-6862-477f-9db6-6dd3f3a304de>)

### Spatial panel alignment

Avoid placing a navigation rail orbiter between spatial panels. This negatively affects the interface structure.

Navigation rail placement can be adjusted to accommodate specific needs, such as improved ergonomics or [right-to-left (RTL) languages](../../layout/bidirectionality-rtl.md).

For layouts that span more than two spatial panels, consider using a [navigation bar orbiter](nav-bar.md).

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmoxdaai7-navrail_06.mp4?alt=media&amp;token=f31cafb8-b8a6-4b94-a85a-d85b0a5e98d8" title="A nav rail orbiter incorrectly positioned between spatial panels."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmoxdaai7-navrail_06.mp4?alt=media&token=f31cafb8-b8a6-4b94-a85a-d85b0a5e98d8>)

## Spatialized FAB

There are two variants of navigation rail orbiters with different FAB treatments:

-   **Contained FAB rail:** A contained FAB within the rail. This offers a compact and familiar layout.

-   **Spatialized FAB rail:** The FAB becomes an orbiter of its own and is placed outside the navigation rail orbiter. Use this for higher emphasis and a distinct spatial effect.


Use the spatialized FAB rail to emphasize key actions and leverage XR hierarchy. Use the contained FAB rail to be more subtle, and align the experience with the baseline navigation bar.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmoxd8z8l-navrail_07.mp4?alt=media&amp;token=de8986f3-4eed-430b-a165-7e4d8e9aa284" title="Interface showing the difference between a navigation rail orbiter with a contained FAB and spatialized FAB."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmoxd8z8l-navrail_07.mp4?alt=media&token=de8986f3-4eed-430b-a165-7e4d8e9aa284>)

To maintain visual association, place the spatialized FAB in close proximity to the navigation rail orbiter. Material recommends a 20dp margin.

The spatialized FAB can be placed above or below the navigation rail orbiter.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmoxdgsuy-navrail_08.mp4?alt=media&amp;token=d13e9164-41d5-44e4-8006-711220831ec7" title="Spatialized FAB positioned close to the navigation rail orbiter."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmoxdgsuy-navrail_08.mp4?alt=media&token=d13e9164-41d5-44e4-8006-711220831ec7>)

While the spatialized FAB and navigation rail orbiter are typically positioned together, their placement is adaptable.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm4cn0kcj-navrail_09.mp4?alt=media&amp;token=f9165d61-d8e1-4b44-ba35-f238087e45cf" title="Spatialized FAB positioned with excessive spacing between itself and the navigation rail orbiter."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm4cn0kcj-navrail_09.mp4?alt=media&token=f9165d61-d8e1-4b44-ba35-f238087e45cf>)

## Accessibility considerations

[XR accessibility](../design/accessibility.md) guidelines are still evolving. XR navigation rails should follow applicable Material [nav rail accessibility standards](../../../components/navigation-rail/accessibility.md).

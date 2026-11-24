---
title: "XR components"
source_url: "https://m3.material.io/foundations/xr/components/nav-bar"
lastmod: "2026-05-18"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "foundations"
assets:
  - "assets/images/7cfdd50a69a7-mp6grqe5-hero-image.png"
  - "assets/images/67e75b360d9e-mp5rw7th-01-3P.png"
  - "assets/images/bd87b5509390-mp5rwtar-02-3P.png"
  - "assets/images/952b1c0beedc-mp5rxeyw-03-3P.png"
  - "assets/videos/6bd31dee3904-m4cj01s5-navbar_01.mp4"
  - "assets/videos/bf26ceec7a10-mp0xfkl2-navbar_07.mp4"
  - "assets/videos/8161b687921f-ma4aas8k-XR_Nav-Bar_Behavior_02_V02.mp4"
  - "assets/videos/5be5d626a044-moxalxwd-navbar_02.mp4"
  - "assets/videos/c0ed48709f37-moxaokg7-navbar_04.mp4"
  - "assets/videos/24737cd373b3-moxaqxbc-navbar_05.mp4"
  - "assets/videos/13a4822d5db1-moxaupni-navbar_06.mp4"
---

# XR components

Learn how Material 3 Expressive components adapt to extended reality devices

![XR components](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmp6grqe5-hero%20image.png?alt=media&token=77848b44-f2d9-4c6e-a51a-d4807d7ea9e4>)

## Nav bar

star

Note:

XR guidelines are primarily intended for designers. Find what’s implemented in code in the [design kit](https://www.figma.com/community/file/1035203688168086460).

Extended reality (XR) interfaces have special design requirements, like showing apps in 3D space. Material has an XR navigation bar with custom specs and guidance. See [XR developer documentation](http://developer.android.com/design/ui/xr/guides/foundations) for more details.

## Anatomy

![Diagram of navigation bar orbiter identifying 7 internal elements of the component.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmp5rw7th-01-3P.png?alt=media&token=9f654553-9782-4442-80e6-c6f81a39479c>)

_Container; Icon; Active indicator; Small badge (optional); Large badge (optional); Large badge label (optional); Label text_

## Color & elevation

On XR, color is used to highlight elevated UI elements and orbiters. With [spatial elevation](https://developer.android.com/design/ui/xr/guides/spatial-ui#spatial-elevation), the navigation bar displays above the spatial panel, on the Z-axis. Color communicates elevation on UI elements and orbiters. Elevated nav bars can use any of these color options:

![4 versions of elevation color strategy.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmp5rwtar-02-3P.png?alt=media&token=949b1a5c-1fa5-4880-8a2c-28b4ccef1e93>)

_Surface container; Surface container high; Surface container highest; Tertiary container_

## Measurements

![Measurements and padding for navigation bar orbiter.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmp5rxeyw-03-3P.png?alt=media&token=7a9e8b61-55f8-4a7d-9a39-2da0d21c98fb>)

_Navigation bar orbiter padding and measurements_

## Usage

In full space, a navigation bar can appear in an orbiter for a more immersive experience. Currently, spatial capabilities, such as orbiters, are only available in full space. In home space, use a regular navigation bar on the same plane as the body content to mimic a 2D experience.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm4cj01s5-navbar_01.mp4?alt=media&amp;token=598ad15c-e79d-4684-bcda-a2eaf0f76071" title="Interface showcasing navigation bar behavior and placement when transitioning from a 2D to 3D experience."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm4cj01s5-navbar_01.mp4?alt=media&token=598ad15c-e79d-4684-bcda-a2eaf0f76071>)

## Behavior

### Global context

When placed in global context, the navigation bar orbiter is centered at the bottom of the app it controls. It stays anchored to the app during layout or content changes. This ensures navigation elements are easy to find and use.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp0xfkl2-navbar_07.mp4?alt=media&amp;token=204309ca-e287-47b0-b809-cf98d19d1881" title="A navigation bar orbiter placed in global context."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp0xfkl2-navbar_07.mp4?alt=media&token=204309ca-e287-47b0-b809-cf98d19d1881>)

### Local context

When placed in local context, the navigation bar orbiter is centered at the bottom of the spatial panel it controls. It repositions in response to layout or content changes.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fma4aas8k-XR_Nav-Bar_Behavior_02_V02.mp4?alt=media&amp;token=3fb3e553-5fc0-426a-8c0d-5c6275ab064f" title="A navigation bar orbiter placed in local context."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fma4aas8k-XR_Nav-Bar_Behavior_02_V02.mp4?alt=media&token=3fb3e553-5fc0-426a-8c0d-5c6275ab064f>)

## Placement

### Navigation context

The position of the navigation bar orbiter should communicate its navigational context:

-   Use **offset positioning** for global actions that affect the overall app experience

-   Use **inset positioning** for local actions that are specific to a spatial panel


A navigation bar orbiter can either overlap or be positioned adjacent to spatial panels with a 20dp margin for visual separation.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmoxalxwd-navbar_02.mp4?alt=media&amp;token=bf9cf606-d1f6-4bdc-b555-a6c1bfb5c10b" title="Navigation bar orbiter offset and inset positioning."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmoxalxwd-navbar_02.mp4?alt=media&token=bf9cf606-d1f6-4bdc-b555-a6c1bfb5c10b>)

### Inset positioning

Don’t obstruct content. To ensure a balanced and uncluttered layout, a navigation bar orbiter should overlap spatial panels by 12dp and no more than half their height.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmoxaokg7-navbar_04.mp4?alt=media&amp;token=b226855d-f99c-4e69-b64b-a394911579aa" title="Navigation bar orbiter inset positioning beyond the 12dp recommendation."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmoxaokg7-navbar_04.mp4?alt=media&token=b226855d-f99c-4e69-b64b-a394911579aa>)

### Horizontal alignment

The navigation bar orbiter placement shouldn't exceed the width of adjacent spatial panels.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmoxaqxbc-navbar_05.mp4?alt=media&amp;token=2dc24b69-cf71-45d0-afbf-ca41f39f2f1e" title="Nav bar orbiter placement that exceeds the width of its spatial panel."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmoxaqxbc-navbar_05.mp4?alt=media&token=2dc24b69-cf71-45d0-afbf-ca41f39f2f1e>)

### Spatial panel alignment

A navigation bar orbiter should always be placed at the bottom of a spatial panel and within the immediate field of view (FOV).

Their placement can be adjusted to accommodate specific needs, such as improved ergonomics or [right-to-left (RTL) languages](../../layout/bidirectionality-rtl.md).

Avoid placing the navigation bar orbiter at the top of a spatial panel, as this area is typically reserved for app bar orbiters or other critical UI elements.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmoxaupni-navbar_06.mp4?alt=media&amp;token=3c9ac7c4-18a7-43d4-9e1d-81457cbfd801" title="Nav bar orbiter incorrectly placed above a spatial panel."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmoxaupni-navbar_06.mp4?alt=media&token=3c9ac7c4-18a7-43d4-9e1d-81457cbfd801>)

## Accessibility considerations

XR navigation bars should follow applicable Material [nav bar accessibility standards](../../../components/navigation-bar/accessibility.md). [More on XR accessibility](../design/accessibility.md)

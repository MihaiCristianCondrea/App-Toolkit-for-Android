---
title: "Design for immersive XR"
source_url: "https://m3.material.io/foundations/xr/design/layout"
lastmod: "2026-05-19"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "foundations"
assets:
  - "assets/images/c96d3c3a1421-mp6oo483-hero-image.png"
  - "assets/videos/3faf1a81880d-mowquax6-XR-Headsets.mp4"
  - "assets/videos/2fbdc3186151-mowrcjbe-developer.android.com_hero-(2).mp4"
  - "assets/images/362e493e17b1-mowrftn5-03-option-b.png"
  - "assets/images/1eb7b9cc2a1a-mowrhano-04.png"
  - "assets/images/6d70a985218b-mowrjlzr-05.png"
  - "assets/videos/d6eeae191bc6-mowrrhdk-spatial-ui-scale.mp4"
  - "assets/images/fa3d48514cdb-mowrvz0k-07.png"
  - "assets/images/bd75e4e47211-mowrwm48-08.png"
  - "assets/images/02722cf478b0-mows5o73-09.png"
  - "assets/videos/8cfb13e75749-mows9uwa-navrail_proto_2x_01-(1).mp4"
  - "assets/videos/786cb21d39fd-mowuw75p-Spatialize_Components_8-(1).mp4"
  - "assets/videos/aaffe3917c60-mowv0qt4-spatial-ui-anchor.mp4"
  - "assets/videos/b0862c681417-mowv8jsw-Dimming-(1).mp4"
---

# Design for immersive XR

Resources and guidance for immersive extended reality (XR) devices

![Design for immersive XR](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmp6oo483-hero%20image.png?alt=media&token=e98832ea-27a5-45b3-8e22-8838005536d6>)

## Layout

In XR, a layout extends beyond the screen into the physical world. Spatial panels, 3D models, and an immersive environment can be arranged anywhere on an infinite canvas.

Unlike mobile layouts which are constrained by screen edges, XR layouts must account for:

-   Depth on the Z-axis

-   Viewing distance

-   A person's field of view


[More on Android XR layouts](https://developer.android.com/design/ui/xr/guides/spatial-ui)

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmowquax6-XR%20Headsets.mp4?alt=media&amp;token=f0c4ce44-de12-49af-bfc9-976a2b045251" title="A video of a family plays against an immersive, mountainous background."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmowquax6-XR%20Headsets.mp4?alt=media&token=f0c4ce44-de12-49af-bfc9-976a2b045251>)

## XR layout basics

The fundamental building block of an Android XR app is the spatial panel. Panels serve as containers for UI elements and can be spatially elevated alongside orbiters, 3D models, and environments.

star

Note:

Spatial panels are available in full space only. They aren't currently available in home space.

### Spatial panels

In full space, spatial panels are flexible canvases that can contain UI elements, media, and spatial video.

They often serve as the anchor for 3D models and orbiters.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmowrcjbe-developer.android.com_hero%20(2).mp4?alt=media&amp;token=980a9373-629e-4c1a-b7f7-e6c7ce55f9ac" title="An XR app with 1 large spatial panel, and 3 orbiters with UI elements and photos."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmowrcjbe-developer.android.com_hero%20(2).mp4?alt=media&token=980a9373-629e-4c1a-b7f7-e6c7ce55f9ac>)

#### Size & position

Full space supports panel placement in both passthrough and virtual environments.

By default, spatial panels launch:

-   Size: 1024x720dp

-   1.75 meters away from a person

-   With 32dp rounded corners


In full space, panels have no minimum size. The maximum panel size is 2560x1800dp.

When people switch from full space to home space, spatial panels usually stay in the same predictable position.

![A visualization of a spatial panel 1.75 meters from a person.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmowrftn5-03%20-%20option%20b.png?alt=media&token=1f3732d6-989b-4088-9455-f3efcc39daf3>)

_At a 1.75 meter launch distance, a spatial panel's size is 1024x720dp_

![A diagram of a person looking at a spatial panel 5 degrees below eye level.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmowrhano-04.png?alt=media&token=4b0bb7c9-da66-4534-aeef-a7c200c02007>)

_Place the panel's vertical center 5° below eye level to maximize comfort_

![An XR panel in the center 41 degrees of a person’s field of view.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmowrjlzr-05.png?alt=media&token=e68916cf-d679-4c5b-9e6d-3207c9df61a2>)

_Place primary content in the center 41° of a person’s field of view_

#### Adaptive design

Spatial panels dynamically scale based on their distance from a person.

Material 3 components use [adaptive design](../../layout/layout-overview/adaptive-design.md) to ensure content automatically scales and reflows to remain legible and comfortable at any distance or angle.

To avoid system UI conflicts, stay within default movement limits:

-   Minimum depth: 0.75 meter

-   Maximum depth: 5 meters

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmowrrhdk-spatial-ui-scale.mp4?alt=media&amp;token=776b7bf5-b144-4797-96c4-dbf2e5dc2a5b" title="A person uses a 1 arm gesture to make an XR UI larger. The size dynamically scales in relation to the person."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmowrrhdk-spatial-ui-scale.mp4?alt=media&token=776b7bf5-b144-4797-96c4-dbf2e5dc2a5b>)

#### Grouping panels

In full space, an app can be broken up into multiple spatial panels, arranged in a flat, curved, or arbitrary layout.

![3 spatial panels in a flat, straight line in front of a person.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmowrvz0k-07.png?alt=media&token=a61aac7d-85ca-4e64-a9f4-be1553a2c20b>)

_Flat layout: Panels are arranged in a straight line. Best for comparing information side by side._

![3 spatial panels curved around a person.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmowrwm48-08.png?alt=media&token=fd4b727a-4796-44cf-a2a8-6388301f3804>)

_Curved layout: Panels curve around the person. Best for immersive media or wide-format dashboards._

![3 spatial panels, each in a unique position, angle, and size.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmows5o73-09.png?alt=media&token=8c874ac9-1f81-4aed-87d6-ec9d0f035285>)

_Arbitrary layout: Panels are placed freely in space. Best for multi-tasking._

### Orbiters

An orbiter is a floating element that accompanies a spatial panel.

Use orbiters for navigation UI that needs to stay accessible without obscuring the main content.

Material [XR components](../components/overview.md) automatically adapt into orbiters.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmows9uwa-navrail_proto_2x_01%20(1).mp4?alt=media&amp;token=f89f3654-540e-4e32-92f2-38e7be47fdee" title="A nav rail appears as an orbiter to the left of an XR email app."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmows9uwa-navrail_proto_2x_01%20(1).mp4?alt=media&token=f89f3654-540e-4e32-92f2-38e7be47fdee>)

### Spatial elevation & depth

Use the Z-axis to create volumetric UI. Unlike 2D elevation which uses shadows, volumetric UI uses actual depth.

-   [Spatial elevation](https://developer.android.com/design/ui/xr/guides/spatial-ui#spatial-elevation) can create hierarchy, bring active elements forward, and push background elements back

-   Layering can separate UI layers physically. For example, a scrim can float several centimeters behind a dialog box.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmowuw75p-Spatialize_Components_8%20(1).mp4?alt=media&amp;token=09e9bad6-2ab4-4fb5-86f0-f69831e4c5a0" title="A dialog switches from 2D to 3D, using spatial elevation."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmowuw75p-Spatialize_Components_8%20(1).mp4?alt=media&token=09e9bad6-2ab4-4fb5-86f0-f69831e4c5a0>)

## Behavior

### Anchoring

In [passthrough](https://developer.android.com/design/ui/xr/guides/foundations#give-users), layouts can interact with the physical world: 

-   World-locked: Panels stay in a specific spot in the room, like a music player anchored to a table 

-   Head-locked: Avoid locking UI directly to the person's head view, as it can feel jarring. Instead, use a lazy follow behavior where the UI gently drifts to catch up with the person's movement.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmowv0qt4-spatial-ui-anchor.mp4?alt=media&amp;token=e0ed5e6b-6887-4a3d-a94c-2d33300a7be2" title="A person uses a 1 arm gesture to anchor a spatial panel to a physical desk."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmowv0qt4-spatial-ui-anchor.mp4?alt=media&token=e0ed5e6b-6887-4a3d-a94c-2d33300a7be2>)

### Color contrast & dimming

When using standard **surface** tokens, panels automatically handle contrast: 

-   Passthrough: If the physical room is bright, the system dims the background to ensure the UI remains legible 

-   Virtual environments: Panels adapt to the lighting of the virtual world


[More on XR colors](https://developer.android.com/design/ui/xr/guides/visual-design#colors)

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmowv8jsw-Dimming%20(1).mp4?alt=media&amp;token=484e9d13-ab47-404c-8ab6-6608426d6224" title="An XR video of a family at the dinner table. The dimming setting adjusts from an opaque virtual background to passthrough."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmowv8jsw-Dimming%20(1).mp4?alt=media&token=484e9d13-ab47-404c-8ab6-6608426d6224>)

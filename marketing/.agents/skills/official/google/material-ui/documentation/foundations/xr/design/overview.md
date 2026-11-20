---
title: "Design for immersive XR"
source_url: "https://m3.material.io/foundations/xr/design/overview"
lastmod: "2026-05-19"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "foundations"
assets:
  - "assets/images/c96d3c3a1421-mp6oo483-hero-image.png"
  - "assets/videos/122a73c9b76e-mowi4zcr-Full-Space.mp4"
  - "assets/images/2dda1a3ce9f9-moxbsbzn-Spatialize_Components_10.gif"
  - "assets/images/c131025bb83b-mowiikrm-03.png"
  - "assets/images/a145aa8f399c-mowilveb-04.png"
  - "assets/images/3170bdcccf4b-mowin62l-05.png"
  - "assets/videos/a8287ef6c08a-mowj1x1f-toolbars-xr-06.mp4"
---

# Design for immersive XR

Resources and guidance for immersive extended reality (XR) devices

![Design for immersive XR](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmp6oo483-hero%20image.png?alt=media&token=e98832ea-27a5-45b3-8e22-8838005536d6>)

## Overview

-   Use depth and expanded space to create believable environments

-   Map interactions, like gaze and gestures, to real-world expectations

-   Group UI elements on floating spatial panels

-   Design for comfort to minimize motion sickness and physical strain

-   Provide feedback through spatial audio, haptics, and visual cues

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmowi4zcr-Full-Space.mp4?alt=media&amp;token=2384b8fd-0a32-4930-8c5d-441b750dd6e9" title="3 XR apps float in a physical room. The view switches to 1 immersive app."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmowi4zcr-Full-Space.mp4?alt=media&token=2384b8fd-0a32-4930-8c5d-441b750dd6e9>)

## Resources & availability

| **Type** | **Resource** | **Status** |
| --- | --- | --- |
| Design | [M3 Design Kit](https://www.figma.com/community/file/1035203688168086460) (Figma) | Available |
|  | [Android XR immersive design guidelines](https://developer.android.com/design/ui/xr/guides/get-started) | Available |
|  | [Design for AI glasses](https://developer.android.com/design/ui/ai-glasses) | Available |
| Implementation | [Build for Android XR](https://developer.android.com/develop/xr/get-started) | Available |
|  | [Jetpack XR SDK](https://developer.android.com/develop/xr/jetpack-xr-sdk) | Available |
|  | [Material Design for XR API reference](https://developer.android.com/jetpack/androidx/releases/xr-compose-material3) | Available |

## Principles

### Use familiar patterns

Material components like buttons and menus help people navigate spatial apps with confidence.

![A button opens to show a 3D Material menu.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmoxbsbzn-Spatialize_Components_10.gif?alt=media&token=b6813038-ded9-4f90-ad94-d740c11fbb2f>)

_In XR, a Material menu uses elevation to appear in 3D_

### Prioritize comfort

Place content in the center of a person’s field of view, and design for different body positions, such as seated, standing, and reclined.

![A person sitting in a chair in an immersive XR environment, with lines marking their field of view.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmowiikrm-03.png?alt=media&token=f64a8f49-743a-439f-bf43-c5e577ba5c80>)

_Positioning content in a person’s field of view keeps the UI visible and minimizes the need for excessive head or body movement_

### Embrace depth

Use elevation and 3D models to add volume, create a sense of realism, and spatial understanding.

![A person stands in front of an XR app with 3 curved panels and a 3D shark.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmowilveb-04.png?alt=media&token=125e5211-61a5-46c7-9f4e-806274ec223a>)

_3D models can be viewed from all angles and moved with natural interactions_

### Design for accessibility

Design apps to work with system-level assistive technologies like screen readers, voice commands, and text resizing. Provide large target sizes, support multimodal inputs, and ensure text is legible against any background.

![1 XR settings icon button with labeled target size and offset. 1 microphone icon button with a visible cursor.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmowin62l-05.png?alt=media&token=1050432a-36bd-4218-ba7e-af41ccd0f12b>)

_In XR, icon buttons should have a 56dp target size and 4dp offset_

## Material XR components

The following Material components are adapted for XR:

-   [App bars](https://m3.material.io/m3/pages/xr-components/app-bars/)

-   [Dialogs](../components/dialogs.md)

-   [Navigation bar](../components/nav-bar.md)

-   [Navigation rail](../components/nav-rail.md)

-   [Toolbars](../components/toolbars.md)

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmowj1x1f-toolbars-xr-06.mp4?alt=media&amp;token=5f67401b-e96d-481c-8859-7b7734e9c577" title="Animation showing a toolbar changing from 2D to 3D."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmowj1x1f-toolbars-xr-06.mp4?alt=media&token=5f67401b-e96d-481c-8859-7b7734e9c577>)

## XR terms

-   [3D models](https://developer.android.com/design/ui/xr/guides/3d-content): Digital objects rendered with depth and volume

-   [Field of view](https://developer.android.com/design/ui/xr/guides/spatial-ui#where-place): The area a person can see without turning their head

-   [Full space](https://developer.android.com/design/ui/xr/guides/foundations#modes): Android XR’s immersive mode that supports spatial components

-   [Home space](https://developer.android.com/design/ui/xr/guides/foundations#modes): Compatible with mobile and large screen apps, but doesn’t support spatial components

-   [Orbiters](https://developer.android.com/design/ui/xr/guides/spatial-ui#orbiters): Floating elements that control the content within spatial panels, full space only

-   [Passthrough](https://developer.android.com/design/ui/xr/guides/foundations#give-users): A blended reality where an XR device displays multiple large apps and the user’s physical environment

-   [Spatial elevation](https://developer.android.com/design/ui/xr/guides/spatial-ui#spatial-elevation): Displays a component above an app on the Z-axis

-   [Spatial environments](https://developer.android.com/design/ui/xr/guides/environments): The 360° 3D virtual worlds people see in an immersive app

-   [Spatial panels](https://developer.android.com/design/ui/xr/guides/spatial-ui#spatial-panels): A container for UI elements, interactive components, and immersive content, full space only

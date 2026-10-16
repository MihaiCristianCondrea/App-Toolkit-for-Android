---
title: "Design for immersive XR"
source_url: "https://m3.material.io/foundations/xr/design/interaction"
lastmod: "2026-05-19"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "foundations"
assets:
  - "assets/images/c96d3c3a1421-mp6oo483-hero-image.png"
  - "assets/videos/fdd4d595aa56-mowkhgek-content-maps.mp4"
  - "assets/videos/3eedebf1d7da-mowkzvav-xr-interaction-02.mp4"
  - "assets/videos/c5ead70070c2-mowwbqda-xr-interaction-03.mp4"
  - "assets/videos/2812c51101ab-mowojjby-Spatialize_3D-Content_5-(2).mp4"
  - "assets/videos/ba6bb41a5513-mowqbjui-xr-accessibility-01.mp4"
  - "assets/videos/af7d46ea7a13-mowqhvfh-Spatialize_3D-Content_3-(1).mp4"
  - "assets/videos/70c4b7367337-mowqk798-spatial-audio.mp4"
---

# Design for immersive XR

Resources and guidance for immersive extended reality (XR) devices

![Design for immersive XR](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmp6oo483-hero%20image.png?alt=media&token=e98832ea-27a5-45b3-8e22-8838005536d6>)

## Interaction

XR interactions should be flexible, comfortable, and intuitive. People expect to interact with digital objects just like they do with physical ones.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmowkhgek-content-maps.mp4?alt=media&amp;token=a29bf114-19f0-497b-a293-6651bdc0d378" title="XR Google Maps switches from a floating 2D map to a 3D aerial city view, followed by a hand-gesture navigated restaurant tour."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmowkhgek-content-maps.mp4?alt=media&token=a29bf114-19f0-497b-a293-6651bdc0d378>)

## Natural interactions

Natural interactions like gestures allow people to navigate, select, and move content using their hands. Gestures should be:

-   Easy to learn

-   Follow familiar patterns, such as press, pinch, and swipe on mobile

-   Comfortable to use repeatedly 


Support one-handed interactions for essential actions. Don’t require large, repetitive arm movements, as they can cause fatigue.

 [More on Android XR gestures](https://developer.android.com/design/ui/xr/guides/foundations#understanding-system)

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmowkzvav-xr-interaction-02.mp4?alt=media&amp;token=2b67b2f3-e2e9-4b03-a792-49a9d1f8fb99" title="A person uses their hand to hover on and switch between 3 spatial panels."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmowkzvav-xr-interaction-02.mp4?alt=media&token=2b67b2f3-e2e9-4b03-a792-49a9d1f8fb99>)

### System navigation

People should be able to open a navigation menu anywhere, anytime.  

On Android XR, the system-level navigation menu includes:

-   Go back: Operates the same as the [back gesture](https://developer.android.com/guide/components/activities/tasks-and-back-stack) on Android phones

-   Launcher: Goes to the home screen

-   Recents: People can open, close, and switch apps

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmowwbqda-xr-interaction-03.mp4?alt=media&amp;token=81b539fb-1ddb-4060-bc67-80dd433d84f0" title="An animated hand moves its palm up, and pinches the index finger and thumb to open the navigation menu. "></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmowwbqda-xr-interaction-03.mp4?alt=media&token=81b539fb-1ddb-4060-bc67-80dd433d84f0>)

## Multimodal inputs

XR apps should support flexible, multimodal inputs, such as hand and eye tracking, voice, keyboard and mouse, and controllers.  

[More on Android XR multimodal inputs](https://developer.android.com/design/ui/xr/guides/foundations#design-multimodal)

### Hand & eye tracking

Tracking allows people to interact with the virtual world without a controller.

 Hand tracking

-   Direct: People can touch, grab, or push virtual objects in arm's reach

-   Ray-based: A ray, like a laser pointer, extends from the hand to target distant items, then a pinch selects it


Eye tracking

-   Enables gaze and dwell interactions

-   Looking at an object triggers a hover state

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmowojjby-Spatialize_3D-Content_5%20(2).mp4?alt=media&amp;token=2f861b7f-c40d-41b7-80c6-693545afbcd0" title="A person uses ray-based hand tracking to move a 3D globe from a table to a credenza. When selected, a highlighted box appears under the globe."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmowojjby-Spatialize_3D-Content_5%20(2).mp4?alt=media&token=2f861b7f-c40d-41b7-80c6-693545afbcd0>)

### Voice, keyboard, mouse, & controller inputs

XR apps should also support voice and physical inputs including:

-   Voice for hands-free text entry on virtual keyboards

-   Voice commands for common actions like **Open Settings** or **Go back**

-   Mouse & keyboard inputs for precision and text-heavy workflows

-   Six degrees of freedom (6DoF) controllers for gaming or complex 3D manipulation

## Motion

In XR, motion sickness can happen when visual cues disconnect from the inner ear's sense of balance.

To keep people comfortable:

-   Use [standard easing](../../../styles/motion/easing-and-duration/tokens-specs.md#601d5552-a6e6-4d74-9886-ff8f24b9ec35) and [long duration](../../../styles/motion/easing-and-duration/tokens-specs.md#48bf653e-46f9-48f5-87e0-eaf8ea3fe716) motion tokens

-   Maintain a stable horizon line

-   Limit continuous motion. To switch locations, use teleportation or instant jump instead.

-   Use tunnel vision or vignetting to reduce the field of view while in motion

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmowqbjui-xr-accessibility-01.mp4?alt=media&amp;token=31481d3d-1d3d-4d95-9e51-9ba2e1eb63ca" title="A person walking in a spatial environment, with a steady visual horizon."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmowqbjui-xr-accessibility-01.mp4?alt=media&token=31481d3d-1d3d-4d95-9e51-9ba2e1eb63ca>)

## Feedback

Since virtual objects lack physical resistance, use visual, audio, and sensory feedback to confirm interactions.

### Visual cues

Use hover icons, focus indicators, ripples, text labels, and elevation changes to show an object’s interaction state.  

To indicate an item is targeted, use [spatial elevation](https://developer.android.com/design/ui/xr/guides/spatial-ui#spatial-elevation) or a highlight state.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmowqhvfh-Spatialize_3D-Content_3%20(1).mp4?alt=media&amp;token=a19a311d-ed54-4448-81e0-b1526ce439e8" title="When a person uses hand tracking to target a 3D model, a focus indicator appears."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmowqhvfh-Spatialize_3D-Content_3%20(1).mp4?alt=media&token=a19a311d-ed54-4448-81e0-b1526ce439e8>)

### Spatial audio

Sound emitters can be positioned in 3D space and provide audio confirmation of an action. For example, add a **click** sound when a button is pressed.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmowqk798-spatial%20audio.mp4?alt=media&amp;token=3cc2c62a-45cd-44f4-b5f9-8ba993921acd" title="As sound emits from 1 of 4 spheres, a person looks toward the active sphere."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmowqk798-spatial%20audio.mp4?alt=media&token=3cc2c62a-45cd-44f4-b5f9-8ba993921acd>)

### Haptics

For controllers, allow people to turn vibration on or off to simulate the feel of touching or grabbing an object.

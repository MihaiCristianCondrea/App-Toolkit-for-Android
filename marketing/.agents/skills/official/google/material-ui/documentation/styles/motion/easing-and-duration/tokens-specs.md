---
title: "Easing and duration"
source_url: "https://m3.material.io/styles/motion/easing-and-duration/tokens-specs"
lastmod: "2025-09-26"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "styles"
assets:
  - "assets/videos/5c43e3ea7901-lxo7ug8i-GM3-Styles-Motion-Easing_Duration-Tokens-1-1-v01.mp4"
  - "assets/videos/c153c317240a-lxo7uv91-GM3-Styles-Motion-Easing_Duration-Tokens-1-2-v01.mp4"
  - "assets/videos/5d77b0339f64-lxo7v6ah-GM3-Styles-Motion-Easing_Duration-Tokens-1-3-v01.mp4"
  - "assets/videos/f34c03bd5016-lxo7vu4c-GM3-Styles-Motion-Easing_Duration-Tokens-2-1-v01.mp4"
  - "assets/videos/02f895e225ac-lxo7w7mt-GM3-Styles-Motion-Easing_Duration-Tokens-2-2-v01.mp4"
  - "assets/videos/f359215507d5-lxo7wjde-GM3-Styles-Motion-Easing_Duration-Tokens-2-3-v01.mp4"
  - "assets/videos/526401d689d3-lxo7xbhz-GM3-Styles-Motion-Easing_Duration-Tokens-3-v01.mp4"
  - "assets/videos/cffd887bce6b-lxo7xucu-GM3-Styles-Motion-Easing_Duration-Tokens-4-v01.mp4"
  - "assets/videos/06f4cb9c12d2-lxo7ysp0-GM3-Styles-Motion-Easing_Duration-Tokens-5-v01.mp4"
  - "assets/videos/42b850c565f7-lxo7z9jk-GM3-Styles-Motion-Easing_Duration-Tokens-6-v01.mp4"
---

# Easing and duration

Easing and duration create responsive and expressive motion

## Tokens & specs

star

Note:

In the expressive update, components and motion now use the [motion physics system](https://m3.material.io/m3/pages/motion-overview/), which uses springs. Products should migrate to the new system. The easing and duration system is still used for transitions and can be used by teams that haven't yet updated to GM3 Expressive, but is no longer maintained.

## Tokens

Motion easing and duration can be implemented using easing and duration tokens. [Learn more about design tokens](../../../foundations/design-tokens/overview.md)

### TOKEN_TYPE_UNSPECIFIED

Component: Sliders

Configured context tags: Standard, Expressive, Compose, Web, Android

## Easing

### Emphasized easing set

This set is the most common because it captures the expressive style of M3.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flxo7ug8i-GM3-Styles-Motion-Easing_Duration-Tokens-1-1-v01.mp4?alt=media&amp;token=879529b5-4b03-49b3-a203-46657b3148ac" title="A line graph illustrating an emphasized easing pattern."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flxo7ug8i-GM3-Styles-Motion-Easing_Duration-Tokens-1-1-v01.mp4?alt=media&token=879529b5-4b03-49b3-a203-46657b3148ac>)

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flxo7uv91-GM3-Styles-Motion-Easing_Duration-Tokens-1-2-v01.mp4?alt=media&amp;token=9f0d914d-4fd4-42ff-a924-54be8cf93e5a" title="A line graph illustrating an emphasized decelerate easing pattern."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flxo7uv91-GM3-Styles-Motion-Easing_Duration-Tokens-1-2-v01.mp4?alt=media&token=9f0d914d-4fd4-42ff-a924-54be8cf93e5a>)

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flxo7v6ah-GM3-Styles-Motion-Easing_Duration-Tokens-1-3-v01.mp4?alt=media&amp;token=6a20dec3-a717-4ac9-b39e-5bd93a90f222" title="A line graph illustrating an emphasized accelerate easing pattern."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flxo7v6ah-GM3-Styles-Motion-Easing_Duration-Tokens-1-3-v01.mp4?alt=media&token=6a20dec3-a717-4ac9-b39e-5bd93a90f222>)

| Info/Platform | Emphasized | Emphasized decelerate | Emphasized accelerate |
| --- | --- | --- | --- |
| Token | md.sys.motion.easing.emphasized | md.sys.motion.easing.emphasized.decelerate | md.sys.motion.easing.emphasized.accelerate |
| Android | [pathInterpolator(M 0,0 C 0.05, 0, 0.133333, 0.06, 0.166666, 0.4 C 0.208333, 0.82, 0.25, 1, 1, 1)](https://developer.android.com/reference/android/view/animation/PathInterpolator) | [PathInterpolator(0.05f, 0.7f, 0.1f, 1f)](https://developer.android.com/reference/android/view/animation/PathInterpolator) | [PathInterpolator(0.3f, 0f, 0.8f, 0.15f)](https://developer.android.com/reference/android/view/animation/PathInterpolator) |
| CSS | N/A (Use Standard as a fallback) | [cubic-bezier(0.05, 0.7, 0.1, 1.0)](https://www.w3schools.com/cssref/func_cubic-bezier.asp) | [cubic-bezier(0.3, 0.0, 0.8, 0.15)](https://www.w3schools.com/cssref/func_cubic-bezier.asp) |
| Flutter | [easeInOutCubicEmphasized](https://api.flutter.dev/flutter/animation/Curves/easeInOutCubicEmphasized-constant.html) | [Cubic(0.05, 0.7, 0.1, 1.0);](https://api.flutter.dev/flutter/animation/Cubic-class.html) | [Cubic(0.3, 0.0, 0.8, 0.15);](https://api.flutter.dev/flutter/animation/Cubic-class.html) |
| iOS | N/A (Use Standard as a fallback) | [ControlPoints:0.05f:0.7f:0.1f:1.0f\];](https://developer.apple.com/documentation/quartzcore/camediatimingfunction) | [ControlPoints:0.3f:0.0f:0.8f:0.15f\];](https://developer.apple.com/documentation/quartzcore/camediatimingfunction) |
| After Effects | Use [After Effects Easing Panel](https://storage.googleapis.com/material-io-static/resources/material-easing-1.1.3.zxp) (download) |  |  |

### Standard easing set

This set is used for simple, small, or utility-focused transitions.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flxo7vu4c-GM3-Styles-Motion-Easing_Duration-Tokens-2-1-v01.mp4?alt=media&amp;token=7a7c72cc-6c0f-4836-8319-96a7fa2dd0a0" title="A line graph illustrating a standard easing pattern."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flxo7vu4c-GM3-Styles-Motion-Easing_Duration-Tokens-2-1-v01.mp4?alt=media&token=7a7c72cc-6c0f-4836-8319-96a7fa2dd0a0>)

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flxo7w7mt-GM3-Styles-Motion-Easing_Duration-Tokens-2-2-v01.mp4?alt=media&amp;token=5dc6520f-ea02-4673-9527-51b9e214e1e4" title="A line graph illustrating a standard decelerate easing pattern."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flxo7w7mt-GM3-Styles-Motion-Easing_Duration-Tokens-2-2-v01.mp4?alt=media&token=5dc6520f-ea02-4673-9527-51b9e214e1e4>)

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flxo7wjde-GM3-Styles-Motion-Easing_Duration-Tokens-2-3-v01.mp4?alt=media&amp;token=d104f24b-757c-4172-8ff5-10beb509865d" title="A line graph illustrating a standard accelerate easing pattern."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flxo7wjde-GM3-Styles-Motion-Easing_Duration-Tokens-2-3-v01.mp4?alt=media&token=d104f24b-757c-4172-8ff5-10beb509865d>)

|  | Standard | Standard decelerate | Standard accelerate |
| --- | --- | --- | --- |
| Token | md.sys.motion.easing.standard | md.sys.motion.easing.standard.decelerate | md.sys.motion.easing.standard.accelerate |
| Android | [PathInterpolator(0.2f, 0f, 0f, 1f)](https://developer.android.com/reference/android/view/animation/PathInterpolator) | [PathInterpolator(0f, 0f, 0f, 1f)](https://developer.android.com/reference/android/view/animation/PathInterpolator) | [PathInterpolator(0.3f, 0f, 1f, 1f)](https://developer.android.com/reference/android/view/animation/PathInterpolator) |
| CSS | [cubic-bezier(0.2, 0.0, 0, 1.0);](https://www.w3schools.com/cssref/func_cubic-bezier.asp) | [cubic-bezier(0, 0, 0, 1);](https://www.w3schools.com/cssref/func_cubic-bezier.asp) | [cubic-bezier(0.3, 0, 1, 1);](https://www.w3schools.com/cssref/func_cubic-bezier.asp) |
| Flutter | [Cubic(0.2, 0.0, 0, 1.0);](https://api.flutter.dev/flutter/animation/Cubic-class.html) | [Cubic(0, 0, 0, 1);](https://api.flutter.dev/flutter/animation/Cubic-class.html) | [Cubic(0.3, 0, 1, 1);](https://api.flutter.dev/flutter/animation/Cubic-class.html) |
| iOS | [ControlPoints:0.2f:0.0f:0.0f:1.0f](https://developer.apple.com/documentation/quartzcore/camediatimingfunction) | [ControlPoints:0.0f:0.0f:0.0f:1.0f](https://developer.apple.com/documentation/quartzcore/camediatimingfunction) | [ControlPoints:0.3f:0.0f:1.0f:1.0f\];](https://developer.apple.com/documentation/quartzcore/camediatimingfunction) |
| After Effects | Use [After Effects Easing Panel](https://storage.googleapis.com/material-io-static/resources/material-easing-1.1.3.zxp) (download) |  |  |

## Duration

### Short durations

These are used for small utility-focused transitions.

| **Token** | **Value** |
| --- | --- |
| md.sys.motion.duration.short1 | 50ms |
| md.sys.motion.duration.short2 | 100ms |
| md.sys.motion.duration.short3 | 150ms |
| md.sys.motion.duration.short4 | 200ms |

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flxo7xbhz-GM3-Styles-Motion-Easing_Duration-Tokens-3-v01.mp4?alt=media&amp;token=1e5623d6-026a-425b-8c68-c8853a10882e" title="Animation showing a 200ms duration and standard easing curve applied to selection control interactions."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flxo7xbhz-GM3-Styles-Motion-Easing_Duration-Tokens-3-v01.mp4?alt=media&token=1e5623d6-026a-425b-8c68-c8853a10882e>)

### Medium durations

These are used for transitions that traverse a medium area of the screen.

| **Token** | **Value** |
| --- | --- |
| md.sys.motion.duration.medium1 | 250ms |
| md.sys.motion.duration.medium2 | 300ms |
| md.sys.motion.duration.medium3 | 350ms |
| md.sys.motion.duration.medium4 | 400ms |

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flxo7xucu-GM3-Styles-Motion-Easing_Duration-Tokens-4-v01.mp4?alt=media&amp;token=8e049eaf-5050-4c72-b3c7-fd5421f9d675" title="Animation showing a FAB expanding into a sheet with a 400ms duration and Emphasized easing."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flxo7xucu-GM3-Styles-Motion-Easing_Duration-Tokens-4-v01.mp4?alt=media&token=8e049eaf-5050-4c72-b3c7-fd5421f9d675>)

### Long durations

These durations are often paired with Emphasized easing. They're used for large expressive transitions.

| **Token** | **Value** |
| --- | --- |
| md.sys.motion.duration.long1 | 450ms |
| md.sys.motion.duration.long2 | 500ms |
| md.sys.motion.duration.long3 | 550ms |
| md.sys.motion.duration.long4 | 600ms |

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flxo7ysp0-GM3-Styles-Motion-Easing_Duration-Tokens-5-v01.mp4?alt=media&amp;token=8a2cb049-a5d4-40f8-b7b6-3303a5f9166c" title="Animation showing a card expanding into a full screen with a 500ms duration and emphasized easing."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flxo7ysp0-GM3-Styles-Motion-Easing_Duration-Tokens-5-v01.mp4?alt=media&token=8a2cb049-a5d4-40f8-b7b6-3303a5f9166c>)

### Extra long durations

Though rare, some transitions use durations above 600ms. These are usually used for ambient transitions that don't involve user input.

| **Token** | **Value** |
| --- | --- |
| md.sys.motion.duration.extra-long1 | 700ms |
| md.sys.motion.duration.extra-long2 | 800ms |
| md.sys.motion.duration.extra-long3 | 900ms |
| md.sys.motion.duration.extra-long4 | 1000ms |

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flxo7z9jk-GM3-Styles-Motion-Easing_Duration-Tokens-6-v01.mp4?alt=media&amp;token=2d3db148-918b-44e1-9368-780880ec63db" title="Animation showing the transition of an ambient carousel auto-advancing with a 1000ms duration and emphasized easing."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flxo7z9jk-GM3-Styles-Motion-Easing_Duration-Tokens-6-v01.mp4?alt=media&token=2d3db148-918b-44e1-9368-780880ec63db>)

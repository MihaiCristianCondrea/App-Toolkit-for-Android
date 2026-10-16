---
title: "Sliders"
source_url: "https://m3.material.io/components/sliders/accessibility"
lastmod: "2026-05-08"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "components"
assets:
  - "assets/videos/49270f5c3c90-lx2tjccg-GM3-Components-accessibility-Sliders-1-v01_1.mp4"
  - "assets/videos/0b27410d239d-lx2tk5qd-GM3-Components-accessibility-Sliders-2-v01.mp4"
  - "assets/images/d9f05f8d228f-lx1s6sg3-3.png"
  - "assets/images/2a6b00f6382e-lx1s73wl-4.png"
  - "assets/images/2b9d038dfe5b-m0o6wp1l-5.png"
  - "assets/images/b77f00b0dcfa-m3601h6w-6.png"
---

# Sliders

Sliders let users make selections from a range of values

## Accessibility

## Use cases

People should be able to do the following using assistive technology:

-   Navigate to a slider 
-   Select a range by controlling a handle along a track
-   Get appropriate feedback based on input type

## Interaction & style

The slider handle shrinks in width and the value appears to provide a visual cue to the user that the handle is being pressed.

**Touch**

When tapped or dragged, the handle width shrinks to provide interaction feedback, and the value appears.

**Cursor**

When hovered, the cursor changes. When clicked and dragged, the handle width shrinks, and the value appears.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flx2tjccg-GM3-Components-accessibility-Sliders-1-v01_1.mp4?alt=media&amp;token=1f2cb7c0-7e6c-4add-a536-e35a15ee94ce" title="Dragging a slider using a mouse cursor and by touch. Handle width shrinks when dragged."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flx2tjccg-GM3-Components-accessibility-Sliders-1-v01_1.mp4?alt=media&token=1f2cb7c0-7e6c-4add-a536-e35a15ee94ce>)

### Focus and navigation

Initial focus lands directly on the handle, since it’s the primary interactive element of the slider.

The slider value can then be adjusted using the arrow keys or other keyboard navigation options.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flx2tk5qd-GM3-Components-accessibility-Sliders-2-v01.mp4?alt=media&amp;token=7eb948e5-7fa9-47aa-b899-1b05f65b51db" title="Using keyboard navigation on a slider. Tab to the handle, and use arrow keys to change the value."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flx2tk5qd-GM3-Components-accessibility-Sliders-2-v01.mp4?alt=media&token=7eb948e5-7fa9-47aa-b899-1b05f65b51db>)

## Color contrast

Use visual anchors so the end of the slider’s inactive track has at least 3:1 contrast with the background. The stop indicator makes the end easily visible on most backgrounds.

![The inactive track has contrast below 3:1 with the background. The stop indicator is above 3:1. Slider is accessible.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flx1s6sg3-3.png?alt=media&token=fe50b51b-a15f-496e-b0ff-7b65ef7598f4>)

_A stop indicator on the inactive track makes it easier to identify the end of the slider on a low-contrast background_

Alternatively, icons or other elements that have a 3:1 contrast with the background can be used to indicate the ends of the slider’s inactive track.

![The inactive track has contrast ratio below 3:1 with the background. The icon is above 3:1. The slider is accessible.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flx1s73wl-4.png?alt=media&token=f00826a6-95c4-46d0-aa63-2da4e4b7759c>)

_Icons make it easier to identify the ends of the slider on a low-contrast background_

## Keyboard navigation

| Keys | Actions |
| --- | --- |
| Tab | Moves focus to the slider handle |
| Arrows | Increase and decrease the value by one value or one stop indicator |
| Space & Arrows | Increase and decrease the value by one interval or one stop indicator |
| Home or End | Set the slider to the first and last values on the slider |

## Labeling elements

The accessibility label for a slider is typically the same as the slider's adjacent text label. It should have the **slider** role.

![Annotated aria tags of a slider.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0o6wp1l-5.png?alt=media&token=99be26c8-7f4d-4336-8b83-f50fbf4f56b3>)

_A slider’s accessibility label should match the adjacent UI text_

If the UI text is correctly linked to the slider, assistive tech (such as a screenreader) will read the UI text followed by the component’s role.

![Annotated aria tags of a slider with stepper icons.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm3601h6w-6.png?alt=media&token=9855912f-4437-40d5-aa5a-6662501a6d06>)

_Icon buttons placed outside the slider should have the button role_

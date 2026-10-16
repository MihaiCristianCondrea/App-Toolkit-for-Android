---
title: "Bottom sheets"
source_url: "https://m3.material.io/components/bottom-sheets/accessibility"
lastmod: "2026-05-18"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "components"
assets:
  - "assets/images/dc6277cc0c8d-lvp8g5p9-1.png"
  - "assets/images/3f5a0c9c3d8d-lvp8gmd5-bottom-sheet-focus.png"
  - "assets/images/b1b4d16926ed-m33eulyj-20.png"
  - "assets/images/11145bcdbb46-m33euvu0-21.png"
  - "assets/images/d52bfd5d0501-m4bunmcg-2.png"
---

# Bottom sheets

Bottom sheets show secondary content anchored to the bottom of the screen

## Accessibility

## Use cases

Users should be able to:

-   Resize bottom sheets without having to rely on touch gestures

## Interaction & style

### Touch target area

The top 48dp portion of the bottom sheet is interactive when user-initiated resizing is available and the drag handle is present.

![Touch target area of a bottom sheet.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flvp8g5p9-1.png?alt=media&token=8e64004e-3857-4101-b39c-99d3a1202671>)

_To ensure touch target accessibility, the top portion of a bottom sheet can be reserved for resize interactions_

### Initial focus

The optional drag handle can be focused in the tab order and interacted with using non-touch inputs, such as keyboard or switch controls.

![Focus on the drag handle of a bottom sheet.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flvp8gmd5-bottom-sheet-focus.png?alt=media&token=cdb9816d-b6f1-4f2d-a6c9-57c7f19adf6a>)

_Visible focus shown on the drag handle affordance_

### Dragging

Include a single-pointer alternative for any action that can be completed by dragging.

Drag handles should cycle the bottom sheet through available heights when selected. If a drag handle can’t be used, add a button to do this action.

![Bottom sheet with focused drag handle at lower preset height.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fm33eulyj-20.png?alt=media&token=d5774ee7-5188-4627-bcb9-af10bdf399f7>)

_Interacting with the drag handle can quickly move a bottom sheet through preset heights_

![Bottom sheet with drag handle at higher preset height.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fm33euvu0-21.png?alt=media&token=0d41a86a-77e7-45d2-b8f8-711a008b7c01>)

_A bottom sheet can automatically resize to another height after interacting with the drag handle_

## Keyboard navigation

| Keys | Actions |
| --- | --- |
| Tab | Focus lands on drag handle |
| Space / Enter | Toggles between available heights |

## Labeling

Label only the drag handle. The accessibility role for the drag handle is “button.”

![Labeled drag handle with role of button.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fm4bunmcg-2.png?alt=media&token=52a1294c-a753-4e62-a1f3-3379fa80b31f>)

_Label the drag handle_

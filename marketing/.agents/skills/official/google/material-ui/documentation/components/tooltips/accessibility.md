---
title: "Tooltips"
source_url: "https://m3.material.io/components/tooltips/accessibility"
lastmod: "2026-01-30"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "components"
assets:
  - "assets/videos/07ad74c4efcc-m1i00uln-lwj9io0m-GM3-Components-ToolTips-Guidelines-1-v01.mp4"
  - "assets/images/8550fbbb913b-me6ru5uh-02.png"
  - "assets/images/fc554b59788e-me6ruf33-03.png"
  - "assets/images/8441b09b02ba-mjha0vdq-04.png"
  - "assets/images/e62799fa71ff-me6ry9ry-05.png"
---

# Tooltips

Tooltips display brief labels or messages

## Accessibility

## Use cases

People should be able to do the following using assistive technology:

-   Receive a tooltip message
-   Activate a tooltip with a keyboard or switch input

## Interaction & style

Plain and rich tooltips without required actions should remain on screen long enough for people to receive the information without disrupting their existing flow or task.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm1i00uln-lwj9io0m-GM3-Components-ToolTips-Guidelines-1-v01.mp4?alt=media&amp;token=fa4d2602-417f-40f2-b3b1-9979db691f9e" title="A cursor hovers over an icon that identifies it as a star, which remains after moving away."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm1i00uln-lwj9io0m-GM3-Components-ToolTips-Guidelines-1-v01.mp4?alt=media&token=fa4d2602-417f-40f2-b3b1-9979db691f9e>)

Tooltips can appear when an actionable element, like a button or navigation rail, is hovered or focused. However, this tooltip shouldn’t hide crucial information.

Rich tooltips can also appear by selecting an element instead of hovering or focusing on it.

![A cursor hovers over a favorite button producing text about finding this item later in favorites.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fme6ru5uh-02.png?alt=media&token=1a4085d9-9e8f-42d5-9aab-470093ca7dbf>)

_Tooltips can appear on hover or focus to explain actions_

![An information button in a selected state produces text about finding this item later in favorites.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fme6ruf33-03.png?alt=media&token=98c44750-5967-4b0e-8e26-933fd0cdcdbe>)

_Rich tooltips can appear when an element is selected_

## Focus order

Tooltip containers should not block important information or prevent a person from completing an action. 

Focus order within the rich tooltip moves top to bottom between interactive elements. 

Avoid trapping screen reader and keyboard focus on rich tooltips.

People should be able to move linearly through the rest of the page.

![Different elements of a rich tooltip are given a focus order, moving from parent element to inline link to text button.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmjha0vdq-04.png?alt=media&token=4f27369d-0b43-49ff-8a0e-3b074298eefc>)

_Parent element; Inline link; Text button_

## Keyboard navigation

| **Keys** | **Actions** |
| --- | --- |
| **Tab** | Focus lands on button, if available |
| **Space** or **Enter** | Activates the focused element |

## Labeling elements

Tooltips should have the **Tooltip** role, or similar. 

Label all elements in the tooltip according to their own accessibility guidance.

![A rich and plain tooltip with all elements matched to accessibility labels.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fme6ry9ry-05.png?alt=media&token=8d4b4d42-2cb4-4496-ac89-02fed5e0e0fb>)

_The tooltip container should have the Tooltip role_

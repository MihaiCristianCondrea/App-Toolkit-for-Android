---
title: "Toolbars"
source_url: "https://m3.material.io/components/toolbars/accessibility"
lastmod: "2026-05-18"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "components"
assets:
  - "assets/videos/818dbc1643f2-ma798ukw-GM3_Toolbar_Accessibility_1_IA_v02.mp4"
  - "assets/videos/83d3e5364742-ma7996zt-GM3_Toolbar_Accessibility_2_IA_v02.mp4"
  - "assets/images/a9bff947dfac-m0anu1gy-3.png"
  - "assets/images/1c8019ad1d98-m0anv4t7-4.png"
  - "assets/images/cfeff96f7181-m0anynmv-5.png"
---

# Toolbars

Toolbars display frequently used actions relevant to the current page

## Accessibility

## Use cases

People should be able to the following with assistive technology:

-   Navigate and activate any actions in the toolbar

-   Select a destination from a menu

-   Activate a back button

-   Maintain access to toolbar controls when the content is scrolled or collapsed

## Interaction & style

The toolbar has no interactions by default. All interactions are with the elements placed inside.

**Touch**

-   When tapping on an icon button in the toolbar, a touch ripple appears, indicating interaction feedback.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fma798ukw-GM3_Toolbar_Accessibility_1_IA_v02.mp4?alt=media&amp;token=936128aa-9795-48b3-8b10-5b0f5cbd1317" title="An animation of the user tapping on an action item and the ripple effect being shown."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fma798ukw-GM3_Toolbar_Accessibility_1_IA_v02.mp4?alt=media&token=936128aa-9795-48b3-8b10-5b0f5cbd1317>)

**Cursor**

-   When hovered, the hover state provides a visual cue to the user that the element is interactive. 
-   When clicked (in both active and inactive states), a ripple appears, showing the user feedback.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fma7996zt-GM3_Toolbar_Accessibility_2_IA_v02.mp4?alt=media&amp;token=2d872434-fcb6-426b-897e-73be777ad489" title="A mouse hovering over a button in the top app bar, then clicking."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fma7996zt-GM3_Toolbar_Accessibility_2_IA_v02.mp4?alt=media&token=2d872434-fcb6-426b-897e-73be777ad489>)

### Initial focus

Focus lands on the first interactive element. 

Use **Tab** to navigate through all other actions.

![Navigating the top app bar using arrow or tab on a keyboard.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0anu1gy-3.png?alt=media&token=6ce1a9cb-f8d9-4e2e-a0b5-5c3d56691c9a>)

_Use Tab to navigate through interactive elements_

![Activating actions in the top app bar using space or enter on a keyboard.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0anv4t7-4.png?alt=media&token=83f51ce3-ac26-4425-a6ff-a97026cb1e74>)

_Use Space or Enter to activate actions_

## Keyboard navigation

| Keys | Actions |
| --- | --- |
| Tab or Arrows | Navigate between interactive elements |
| Space or Enter | Activate the focused element |

### Labeling elements

On web, the toolbar container should have the **toolbar** role. 

On mobile, it can be a generic container. 

All actions inside the toolbar should follow their respective accessibility guidelines.

![A toolbar on web, with a “toolbar” role label.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0anynmv-5.png?alt=media&token=f53e2da8-9f81-47fa-9786-b445bc596dc8>)

_On web, use the toolbar role_

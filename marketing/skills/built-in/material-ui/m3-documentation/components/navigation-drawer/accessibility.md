---
title: "Navigation drawer"
source_url: "https://m3.material.io/components/navigation-drawer/accessibility"
lastmod: "2026-05-18"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "components"
assets:
  - "assets/videos/12dd17fec8ea-lwoqqvsf-GM3-Components-NavDrawer-Accessibility-1-v01.mp4"
  - "assets/videos/1d3987dc1ade-lwoqr8nd-GM3-Components-NavDrawer-Accessibility-2-v01.mp4"
  - "assets/images/036c25d75ab3-lworf9hl-3.png"
  - "assets/images/ccadf075cffe-m36co6ff-navdrawer-tablet-do-md.png"
  - "assets/images/c6b64cda6d32-lworgt4a-4.png"
  - "assets/images/79499e1dbde9-lworfw7l-5.png"
  - "assets/images/0afd65a4923e-lworg85u-6.png"
  - "assets/images/8c53ca57eabb-lworh5ui-7.png"
  - "assets/images/658fe6af9fab-lworho52-8.png"
  - "assets/images/2fb0d6c9f674-lwori08q-9.png"
---

# Navigation drawer

Navigation drawers let people switch between UI views on larger devices

## Accessibility

star

Note:

The navigation drawer is no longer recommended in the Material 3 Expressive update. For those who have updated, use an [expanded navigation rail](https://m3.material.io/m3/pages/navigation-rail/overview/), which has mostly the same functionality of the navigation drawer and adapts better across window size classes.

## Use cases

Users should be able to: 

-   Move between navigation destinations with assistive technology
-   Select a particular navigation destination from a set
-   Get appropriate feedback based on input type

## Interaction & style

**Touch**

-   When a navigation item is tapped, the active indicator appears in place, providing feedback to the user that it is selected
-   A touch ripple passes through the indicator
-   The icon switches from outlined to filled
-   The icon changes color, becoming darker

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwoqqvsf-GM3-Components-NavDrawer-Accessibility-1-v01.mp4?alt=media&amp;token=18c8c980-08e4-4182-86ec-63c02b1c4470" title="Active indicator and other visual cues appear when nav drawers are tapped."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwoqqvsf-GM3-Components-NavDrawer-Accessibility-1-v01.mp4?alt=media&token=18c8c980-08e4-4182-86ec-63c02b1c4470>)

**Cursor**

-   When hovered, the hover indicator appears providing a visual cue that the destination is interactive
-   When clicked, a ripple passes through the indicator
-   The icon switches from outlined to filled
-   The icon changes color, becoming darker in light theme and lighter in dark theme, to increase the contrast

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwoqr8nd-GM3-Components-NavDrawer-Accessibility-2-v01.mp4?alt=media&amp;token=97004b66-7c1e-4ff2-84a3-fd791bb5e348" title="When a cursor goes over a navigation drawer, a hover indicator appears. When tapped, the active indicator appears."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwoqr8nd-GM3-Components-NavDrawer-Accessibility-2-v01.mp4?alt=media&token=97004b66-7c1e-4ff2-84a3-fd791bb5e348>)

## Initial focus

Initial focus lands directly on the first navigation item, since that is the first interactive element of the component.

![1. Tab lands on the first navigation item, Inbox. 2. Down arrow to get to the second navigation item, Outbox.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flworf9hl-3.png?alt=media&token=1bb331b2-e914-4668-baec-8accae5b6fd6>)

_Focus lands on first navigation item_

## Closing

The modal navigation drawer can be dismissed by selecting the scrim that covers the rest of the screen.

![A navigation drawer with a scrim covering the body content. A touch target is selecting the scrim.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm36co6ff-navdrawer-tablet-do-md.png?alt=media&token=852f7a22-fb45-4eee-89e8-ab411fad1a65>)

_Select the scrim to close the navigation drawer_

## Visual indicators

Icons are the primary focus of the navigation and such give the dominant cue of its state. Use a filled icon for the selected destination to differentiate from the outlined icons of non-selected destinations.

![Space + enter is used to select the navigation item inbox.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flworgt4a-4.png?alt=media&token=6afa4fd0-0c68-4f18-9553-84c516c0c6b2>)

_The navigation item is selected via Space/Enter_

![A navigation drawer with the home destination using a filled icon.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flworfw7l-5.png?alt=media&token=d33e2758-6076-4012-933b-ce1dde9c539f>)

_Use a filled icon for the selected navigation destination to differentiate from the other destinations_

![A navigation drawer with the home destination using an outlined icon.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flworg85u-6.png?alt=media&token=6cd4cfa6-ec1a-40c4-95f3-ba4efc6de9e1>)

_Avoid keeping the icon style for the selected navigation destination the same as unselected destination's icons. This removes an important visual indicator of which destination is active._

![A selected home icon using a filled icon and active indicator and a unselected home icon using an outlined icon.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flworh5ui-7.png?alt=media&token=1b7a06eb-1f35-4b4f-b1d3-82500203e58b>)

_When selected, the icon fills, darkens in light theme (or lightens in dark theme), and is backed by an active indicator shape_

## Keyboard navigation

| **Keys** | **Actions** |
| --- | --- |
| Tab | Focus lands on the first navigation destination |
| Space or Enter | Selects the focused navigation destination, and focus moves to the newly opened section (if applicable) |
| Arrow | Navigate between destinations within the navigation drawer |

## Labeling elements

The accessibility label for a navigation item is typically the same as the destination name.

If the UI text is correctly linked, assistive tech (such as a screenreader) will read the UI text followed by the component’s role.

For Android Views (MDC-Android), a more descriptive accessibility label is not available to be set and the role is not announced.

![A navigation drawer item’s label text and accessibility label both read “photos.” The role is “tab.”](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flworho52-8.png?alt=media&token=7fb90305-fda9-43bf-a909-b1d9f9e64d05>)

_A navigation drawer’s accessibility label can incorporate its adjacent UI text_

When the visible UI text is ambiguous, accessibility labels need to be more descriptive. For example, a navigation destination visibly labeled **Recents** would benefit from additional information in its accessibility label to clarify the destination’s intent.

![A navigation drawer item’s label text is “recents”, the accessibility label is “recent images.” The role is “tab.”](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwori08q-9.png?alt=media&token=816c8f24-84d7-44bd-8578-eaf00de09d6d>)

_While the visible label text reads Recents, the accessibility label for this destination clarifies its function: Recent images_

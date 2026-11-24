---
title: "App bars"
source_url: "https://m3.material.io/components/app-bars/accessibility"
lastmod: "2026-05-18"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "components"
assets:
  - "assets/videos/c99ac57b9b45-mlosdik1-GM3-App-bar-Accessibility-01-IA-v01.mp4"
  - "assets/videos/12def69fdbeb-mlosc4c9-GM3-App-bar-Accessibility-02-IA-v01.mp4"
  - "assets/videos/ef6640706b8e-mlosad0z-GM3-App-bar-Accessibility-03-IA-v01.mp4"
  - "assets/images/aa6417c5da2c-mlos7r6g-04.png"
  - "assets/images/1373dbf8e98f-mlos8gq8-05.png"
  - "assets/images/6b0fd9d811fe-mlos5etv-06-do.png"
  - "assets/images/8953f21dd0c6-mlos65h6-07-dont.png"
  - "assets/images/6157248f9f75-mlos1na5-08.png"
  - "assets/images/2a8c28045e38-mlos0avb-09.png"
---

# App bars

App bars are placed at the top of the screen to help people navigate through a product

## Accessibility

## Use cases

People should be able to do the following using assistive technology:

-   Understand what page they’re currently visiting
-   Take actions or navigate to a new page destination
-   Maintain access to app bar actions when the content is scrolled

## Interaction & style

### Touch

When tapping on an icon button, a touch ripple appears, indicating interaction feedback.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmlosdik1-GM3%20-%20App-bar%20-%20Accessibility%2001%20-%20IA%20-%20v01.mp4?alt=media&amp;token=a64b98b3-db51-4137-bfa8-b481c5aa21ad" title="An animation of the user tapping on an action item and the ripple effect being shown."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmlosdik1-GM3%20-%20App-bar%20-%20Accessibility%2001%20-%20IA%20-%20v01.mp4?alt=media&token=a64b98b3-db51-4137-bfa8-b481c5aa21ad>)

### Cursor

When hovered, the hover state provides a visual cue to the user that the element is interactive.

When clicked (in both active and inactive states), a ripple appears to indicate feedback.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmlosc4c9-GM3%20-%20App-bar%20-%20Accessibility%20-%2002%20-%20IA%20-%20v01.mp4?alt=media&amp;token=bee4841d-d415-4dd5-b247-ad3df00c5cf7" title="A mouse hovering over a button in the app bar, then clicking."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmlosc4c9-GM3%20-%20App-bar%20-%20Accessibility%20-%2002%20-%20IA%20-%20v01.mp4?alt=media&token=bee4841d-d415-4dd5-b247-ad3df00c5cf7>)

### Keyboard or switch

When navigating to an interactive element, a focus indicator appears to show that action can be taken.

When the element is selected, an action is then performed.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmlosad0z-GM3%20-%20App-bar%20-%20Accessibility%20-%2003%20-%20IA%20-%20v01.mp4?alt=media&amp;token=c39b29d2-ff9a-4bd4-a6f6-94e717cb2253" title="An example of how to navigate the app bar using a keyboard."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmlosad0z-GM3%20-%20App-bar%20-%20Accessibility%20-%2003%20-%20IA%20-%20v01.mp4?alt=media&token=c39b29d2-ff9a-4bd4-a6f6-94e717cb2253>)

### Initial focus

Focus should initially land on the leading button, since it’s the first interactive element of the app bar.

![The tab button is used to navigate from the first button to the second button in the app bar.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmlos7r6g-04.png?alt=media&token=2389b948-f4b3-420d-9e6e-34f543525c2f>)

_Use Tabs to navigate through interactive items_

![The space or enter button activates the second action.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmlos8gq8-05.png?alt=media&token=bafd0e8c-7cfe-4ee8-844e-d45367dc8a61>)

_Use Space or Enter to activate actions_

## Color

On search app bars, use the default color roles when possible.

-   Search container: **surface container**
-   Search label: **on surface variant**

On darker backgrounds, search bar containers can use the **surface bright** role to maintain strong visual contrast. 

If mapping to other color roles, make sure the text and container have 3:1 contrast to ensure readability.

![A search app bar with default dark text on a light container background.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmlos5etv-06-do.png?alt=media&token=e55a083f-5a33-4ecc-9fef-d5946838d02d>)

_Make sure search bars and their labels have at least 3:1 contrast. Use the default colors when possible._

![A search app bar with custom light text on a light container background.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmlos65h6-07-dont.png?alt=media&token=0281bfc2-6d34-4c43-b2d3-e55f3b4e1e07>)

_Avoid using custom color roles for the search bar container and search label text. If custom roles are necessary, make sure they have contrast of at least 3:1._

## Keyboard navigation

| Keys | Actions |
| --- | --- |
| Tab | Move focus to the next interactive element |
| Space or Enter | Activate the focused element |

## Labeling elements

The accessibility label for a title should be the same as the content within the title. If needed, add additional context to the accessibility label to ensure users understand what page they’re on or what content is being shown.

Screen readers will read the UI text followed by the component’s role.

![The headline has accessibility role “Title”.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmlos1na5-08.png?alt=media&token=ad772624-922f-47f4-9bba-45c4e4129396>)

_An app bar’s accessibility label can incorporate its UI text as well as additional context_

Label icon buttons according to their [accessibility guidelines](../icon-buttons/accessibility.md).

![The app bar icon button has the accessibility role “Button”.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmlos0avb-09.png?alt=media&token=ea50a8f1-f6ec-4c0d-9693-c381e6b86ed0>)

_An icon button should be clearly labeled on the action it takes, like View on map_

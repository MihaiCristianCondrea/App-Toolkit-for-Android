---
title: "Tabs"
source_url: "https://m3.material.io/components/tabs/accessibility"
lastmod: "2026-05-18"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "components"
assets:
  - "assets/videos/e2af1b381076-m60bkpen-GM3-Components-Tabs-Accessibility-1-v01.mp4"
  - "assets/videos/a0bec8842e80-m60bllqp-GM3-Components-Tabs-Accessibility-2-v01.mp4"
  - "assets/videos/bc21485ef53a-m60bmbt7-GM3-Components-Tabs-Accessibility-3-v01.mp4"
  - "assets/videos/fd7e1e7a695c-m60bmwk8-GM3-Components-Tabs-Accessibility-4-v01.mp4"
  - "assets/videos/df0cd2f75370-m60bo0sa-GM3-Components-Tabs-Accessibility-5.1-v01.mp4"
  - "assets/videos/cf6a6cd4a99e-m60bot9i-GM3-Components-Tabs-Accessibility-5.2-v01.mp4"
  - "assets/images/bd9981a61d5f-m2k3h2mn-7.png"
---

# Tabs

Tabs organize content across different screens and views

## Accessibility

## Use cases

Users should be able to:

-   Undertake actions or invoke navigation to a new destination with assistive tech
-   Select an action or destination from an off screen tab with assistive tech
-   Maintain access of primary actions when the content is in a scrolled state

## Interaction & style

**Touch**

-   When a user taps on an icon button, a touch ripple appears, indicating interaction feedback
-   The selected indicator becomes active and shifts into position once the touch has been engaged

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fm60bkpen-GM3-Components-Tabs-Accessibility-1-v01.mp4?alt=media&amp;token=0e70cb1d-d5e6-4442-b894-95179d1da7f0" title="The ripple effect resulting from tapping on an action item."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fm60bkpen-GM3-Components-Tabs-Accessibility-1-v01.mp4?alt=media&token=0e70cb1d-d5e6-4442-b894-95179d1da7f0>)

**Scrollable**

-   When a set of tabs cannot fit on screen, scrollable tabs are used. They are best used for browsing on touch interfaces.
-   To navigate between scrollable tabs, users swipe the set left or right. Users can also use arrow/tab to navigate through.
-   It's **not recommended** to loop a tab set where it scrolls infinitely. This can trap users who are navigating linearly with a screen reader.
-   To select an individual tab, users tap or press space/enter.
-   Horizontal scrolling tabs meet accessibility requirements because they need to increase in width to respond to label text without affecting the layout, and horizontal scrolling is necessary to view those labels.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fm60bllqp-GM3-Components-Tabs-Accessibility-2-v01.mp4?alt=media&amp;token=c852313d-ba99-4db7-95b0-dd3736385156" title="Swiping right and left scrolls through tabs when scrolling is enabled."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fm60bllqp-GM3-Components-Tabs-Accessibility-2-v01.mp4?alt=media&token=c852313d-ba99-4db7-95b0-dd3736385156>)

**Cursor**

-   When hovered, the hover state appears, providing a visual cue that the icon button is interactive. When clicked (in both active and inactive states), a ripple appears and the indicator shifts into position, showing the user feedback.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fm60bmbt7-GM3-Components-Tabs-Accessibility-3-v01.mp4?alt=media&amp;token=e8ae2e95-3425-4f09-9b1c-0bb2249ab12f" title="Hover state appears when hovering over tabs, clicking on them changes the state to active. "></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fm60bmbt7-GM3-Components-Tabs-Accessibility-3-v01.mp4?alt=media&token=e8ae2e95-3425-4f09-9b1c-0bb2249ab12f>)

**Keyboard/Switch** 

-   When tabbed, a focus indicator appears, providing a visual cue to the user that the destination is now selected
-   When the user engages with the selected tab via Space/Enter in active states, the user is taken to a new destination
-   Within the tab menu, the user is able to arrow/tab through the menu items, Space/Enter to select an item, or tab to exit the active state

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fm60bmwk8-GM3-Components-Tabs-Accessibility-4-v01.mp4?alt=media&amp;token=313f83d3-6732-48c2-99c3-9d6e637c5bc4" title="Tabbing through navigation items. The focus indicator appears when an item is selected."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fm60bmwk8-GM3-Components-Tabs-Accessibility-4-v01.mp4?alt=media&token=313f83d3-6732-48c2-99c3-9d6e637c5bc4>)

### Avoid applying density by default

Don't apply density to tabs by default — this lowers their targets below our best practice of 48x48 CSS pixels. Instead, give people a way to choose a higher density, like selecting a denser layout or changing the theme.

To ensure that this density setting can be easily reverted when it's active, keep all the targets to change it at minimum 48x48 CSS pixels each.

## Initial focus

On arrow/tab in a tab menu, the active indicator appears on the first interactive element, providing feedback to the user that it is selected. The user is then able to tab to additional interactive elements until all available items are complete within the tab menu.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fm60bo0sa-GM3-Components-Tabs-Accessibility-5.1-v01.mp4?alt=media&amp;token=ef562741-1b5e-40e8-8107-22c2e664ca23" title="Arrow or Tab being used to navigate through a tab menu."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fm60bo0sa-GM3-Components-Tabs-Accessibility-5.1-v01.mp4?alt=media&token=ef562741-1b5e-40e8-8107-22c2e664ca23>)

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fm60bot9i-GM3-Components-Tabs-Accessibility-5.2-v01.mp4?alt=media&amp;token=13673350-f957-4807-94ec-446fa068e117" title="Space or Enter being used to navigate through a tab menu."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fm60bot9i-GM3-Components-Tabs-Accessibility-5.2-v01.mp4?alt=media&token=13673350-f957-4807-94ec-446fa068e117>)

## Keyboard navigation

| Keys | Actions |
| --- | --- |
| Arrow | Focus lands on the next available navigation destination |
| Space / Enter | Activates the focused navigation destination |
| Arrow | Allows navigation through menu items |

## Labeling elements

When the visible UI text is ambiguous, or there is no visible UI text, accessibility labels need to be more descriptive. For example, an icon button that visually represents a “video camera” requires additional information in its accessibility label to clarify the icon’s intent.

![Small device screen  with the tab highlighted and the label and role illustrated.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm2k3h2mn-7.png?alt=media&token=47b7122f-9754-488f-8436-39a6b015175a>)

_While the icon visually represents a “Video camera,” the accessibility label for this tab clarifies its function: “Video format media content”_

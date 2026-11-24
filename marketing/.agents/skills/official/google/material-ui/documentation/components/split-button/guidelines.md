---
title: "Split buttons"
source_url: "https://m3.material.io/components/split-button/guidelines"
lastmod: "2026-02-05"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "components"
assets:
  - "assets/images/99689c6aaafe-m36akp9w-1.png"
  - "assets/images/6899dbf657a2-maei547w-2-alt.png"
  - "assets/images/c0d849bdbe8b-m0dosrjf-3.png"
  - "assets/images/039cd9b61f45-maehs3k3-4_ALT.png"
  - "assets/images/ebc6eb630c60-maehumyk-5-alt.png"
  - "assets/images/36adc6f7ca72-m4il1tni-6-alt.png"
  - "assets/images/7c688dbe06af-m4il1xwy-7-alt.png"
  - "assets/images/b374d3d8f992-m0dp22eb-8.png"
  - "assets/images/41dafc5dbc70-m0dxuxjz-9.png"
  - "assets/images/8ebf0753e033-m0dpczv9-10.png"
  - "assets/videos/1dc5bd65c839-m4ilqjvz-Split-Button-B.mp4"
  - "assets/images/8b0ce96a1a14-m4il8isz-12.png"
  - "assets/images/568a1cce17b9-m4ilatqh-13.png"
  - "assets/images/ea54d7e3cfe7-m4iz4b9v-14.png"
---

# Split buttons

Split buttons open a menu to give people more options related to an action

## Guidelines

![Split buttons of many colors and sizes scattered. ](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm36akp9w-1.png?alt=media&token=954a37e5-dbe9-4715-8f37-fd34afd699e0>)

_Split buttons come in many sizes and colors_

## Usage

Split buttons are used to add a menu of actions alongside a main action. This reduces visual complexity by hiding extra options. Split buttons work well alone or alongside common buttons and icon buttons.

![A split button applied a filter of “Canada” to a list of activities. Three narrow buttons are next to it to share, favorite, and bookmark.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmaei547w-2-alt.png?alt=media&token=442ef5f8-434c-4615-a622-926623edc416>)

_Split buttons on their own can grab attention_

Split buttons have five recommended sizes. These sizes match the sizes offered on buttons and icon buttons:

-   Extra small
-   Small (default)
-   Medium
-   Large
-   Extra large

Scale up the split button in large window sizes, or to create more emphasis in smaller windows.

![A large split button in a compact window draws attention to buying an enamel mug in an online store.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0dosrjf-3.png?alt=media&token=1b565df6-4454-4460-9e64-f0dd199fe87c>)

_Using large split buttons on small screens can add extra emphasis for hero moments_

Split buttons can be used alongside other buttons and button groups.

![A vibrant split button for starting a car drive is next to 2 muted icon buttons for bookmarking and sharing the trip.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmaehs3k3-4_ALT.png?alt=media&token=be30a6a7-53a8-4e4d-a190-5293714dd79b>)

_Split buttons work harmoniously with regular buttons_

Split buttons can be of different sizes from other buttons on the page, especially since they take up more space.

![A media player has a split button for changing the speed quickly, or opening a menu of options.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmaehumyk-5-alt.png?alt=media&token=51014525-7e5f-43cb-8e6a-04ecd54f2ae8>)

_The most prominent controls can be larger while secondary controls in a split button can be smaller_

The split button typically opens a menu, but can be customized to open other components like cards.

![A split button opens a menu with a vibrant color scheme.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm4il1tni-6-alt.png?alt=media&token=bccedc39-a096-4dfa-b654-14d575be9f12>)

_Open a menu from a split button_

![A split button opens a menu with an irregular shape highlighting the selected item.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm4il1xwy-7-alt.png?alt=media&token=fb95faee-bb6d-447b-a38f-24f0ef87b0a4>)

_Avoid modifying the menu in unusual ways_

## Anatomy

![4 elements of a split button.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0dp22eb-8.png?alt=media&token=7aadf139-d85b-4439-8514-204c2d6c571d>)

_Leading button; Icon; Label text; Trailing button_

The leading button should be brief, just one or two words, with an icon that best matches the action.

The trailing button should always have the expand and collapse icon since it rotates when selected. Avoid modifying the icon.

![A split button for starting driving directions has a label “32 minutes away” and a refresh icon instead of a menu icon.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0dxuxjz-9.png?alt=media&token=6dbd3d91-8792-44cf-a5b2-f03e5621deb2>)

_Avoid using very long labels or changing the trailing icon_

In right-to-left languages, the component layout is mirrored.

![The split button elements are reversed in a right-to-left language.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0dpczv9-10.png?alt=media&token=9f93accf-e255-4421-8450-efd4294d1176>)

_Split buttons mirror the order of elements in right-to-left languages_

## Behavior

The split button uses the standard motion scheme (not the expressive motion scheme) when rotating the menu button.

The menu button rotates inwards 180° when opened and closed.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm4ilqjvz-Split-Button-B.mp4?alt=media&amp;token=0dbfe0fc-ca7b-4e12-afd7-60a6cceb140a" title="The trailing menu button spins 180° to point up, and applies a 10% state layer when selected."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm4ilqjvz-Split-Button-B.mp4?alt=media&token=0dbfe0fc-ca7b-4e12-afd7-60a6cceb140a>)

### Menu placement

When using the split button with a menu, align the menu with the trailing button when possible.

![A split button with an open menu. The leading edge of the menu is aligned to the leading edge of the menu button.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm4il8isz-12.png?alt=media&token=7dea4867-f593-4b35-a7dd-f8059c252ee5>)

_Align the menu with the trailing button_

If there’s not enough room, align the menu to one of the sides of the button.

![A split button with an open menu. The trailing edge of the menu is aligned to the trailing edge of the menu button.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm4ilatqh-13.png?alt=media&token=c60db39f-3af3-4fe8-92c7-09c26c255a71>)

_If not possible, align the menu to the side of the leading or trailing button_

Depending on window size, scroll position, and other factors, the menu may need to appear elsewhere around the button. Always try to align it with one of the edges of the button.

The menu should be 4dp from the split button.

![6 other ways the menu can align to the split button.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm4iz4b9v-14.png?alt=media&token=7596fabd-1a2c-4d83-a0d6-03963942cbf5>)

_Top aligned to trailing button; Bottom aligned to trailing button; Top right-aligned; Top left-aligned; Bottom right-aligned; Bottom left-aligned_

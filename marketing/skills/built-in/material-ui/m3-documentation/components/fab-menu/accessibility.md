---
title: "FAB menu"
source_url: "https://m3.material.io/components/fab-menu/accessibility"
lastmod: "2026-05-18"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "components"
assets:
  - "assets/images/ab4ca043c1ea-m0am5xpn-01.png"
  - "assets/images/327e5f61930c-m0am8l9a-02.png"
  - "assets/images/a2530ac1faff-m0am8ym2-03.png"
  - "assets/images/d4a711aa1169-maejeaf9-04.png"
  - "assets/images/0d1f9d5c1f48-mmmksaup-05.png"
  - "assets/images/1bcd0a52d929-mmmksh30-06.png"
---

# FAB menu

The floating action button (FAB) menu opens from a FAB to display multiple related actions

## Accessibility

## Use cases

People should be able to do the following using assistive technology:

-   Navigate and interact with the FAB menu
-   Ensure focus is correct when navigating through the menu

## Interaction & style

FAB menu elements meet the minimum target size of 48dp.

![FAB menu measurement annotations. All elements are larger than the minimum target size.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0am5xpn-01.png?alt=media&token=537719c2-9f5f-4d5d-ba85-2b59cdcd2c5b>)

_FAB menus have 48x48dp minimum width and sufficient spacing by default_

When the FAB menu can scroll, make sure the items scroll behind the close button. 

The close button should always be easy to access and unobstructed.

![FAB menu items are scrolling behind the close button.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0am8l9a-02.png?alt=media&token=989423d2-f7cd-4b11-8ac1-115565b2ccc2>)

_Allow the menu items to scroll behind the close button_

![FAB menu items are scrolling in front of the close button.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0am8ym2-03.png?alt=media&token=5fa58f68-9fae-4806-8754-4fe611b73f45>)

_Don’t obstruct the close button in short screens like horizontal orientation_

## Initial focus

When the FAB is selected, the FAB menu opens, and initial focus remains on the close button, which takes the place of the original FAB.

Then the focus moves from the top menu item to the bottom.

![4 FAB menus with the focus order labelled. Focus moves from the close button at the bottom to the topmost menu item next.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmaejeaf9-04.png?alt=media&token=d8a732b5-ad7b-4566-a92d-eccca8795f38>)

_Focus lands on the close button. People can then navigate through all the items. Close button; First menu item; Second menu item; Third menu item_

## Keyboard navigation

| **Keys** | **Actions** |
| --- | --- |
| **Tab** | Navigate to the next interactive element |
| **Space** or **Enter** | Activate the focused button or item |

## Labeling elements

### Android

On Android, a FAB menu’s close button should include a state to tell screen readers what action will occur when it's toggled. The close button should be labeled: 

-   Label: Toggle menu

-   Role: Button

-   State: Expanded or collapsed

![Accessibility labels for the close button on an Android device.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmmmksaup-05.png?alt=media&token=0aabb7b0-89f8-49e9-9564-e07ee8b0c517>)

_On Android, the close button accessibility labels should include a toggle menu label, button role, and an expanded or collapsed state_

FAB menu items should be labeled:

-   Label: Match the item’s UI text, such as **Reply all** 

-   Role: Button

![Accessibility labels for a FAB menu item on an Android device.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmmmksh30-06.png?alt=media&token=469b8f96-0db8-4a1a-b379-e77aeed01160>)

_Label FAB menu items to match their UI text, like Reply all, and use the button role_

### Web

On web, a FAB menu is a combination of a FAB and a menu component. The FAB opens the menu. Follow the [accessibility guidelines for FABs](../floating-action-button/accessibility.md) and [menus](../menus/accessibility.md).

The FAB's accessibility label should describe the menu that the FAB will open.

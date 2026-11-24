---
title: "Menus"
source_url: "https://m3.material.io/components/menus/accessibility"
lastmod: "2026-05-18"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "components"
assets:
  - "assets/images/4412148009be-mi5oa1gs-01.png"
  - "assets/images/a0977938df25-mhkskcq8-02-caution.png"
  - "assets/images/7924ccf0604d-mhkewf5t-03.png"
  - "assets/images/c8b5efb1e6f1-mhkkp9r3-05-do.png"
  - "assets/images/821eaab48537-mhkkqmt3-05-don't.png"
  - "assets/images/3b71b0d1673c-mhkl2sea-07.png"
  - "assets/images/488b64a13980-mhkkynea-08.png"
---

# Menus

## Accessibility

## Use cases

People should be able to do the following using assistive technology:

-   Navigate to, open, and close a menu
-   Navigate between and select menu items

## Interaction & style

Menu items need certain cues to clearly show when they're selected: 

-   By default, menu items change shape and color when selected
-   The default color contrast is 3:1 between selected and unselected menu items
-   It's recommended to include another visual cue, like a checkmark

![A state dropdown menu with the selected item Alaska highlighted in a vibrant color, with a checkmark icon.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmi5oa1gs-01.png?alt=media&token=3091ebcd-8922-4daf-87a4-802975dfd5e7>)

_Use multiple visual cues like color, shape, and icons to show that an item is selected_

## Flexibility & slots

Use caution when adding slots to menus:

-   Make sure the menu remains accessible
-   Elements must follow the rules and interaction patterns of the menu component
-   Keep the same menu item padding
-   Targets should be 48x48dp or larger

Don't add buttons, switches, or other direct actions into the menu item. Nested elements should only perform one action. Adding multiple actions can break keyboard navigation and screen reader functionality.

[More on slots in menus](guidelines.md#8a1684bb-99a5-4a73-91a0-068d0b406127)

![1 diagram and 1 menu showing icons in each item’s leading slot.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmhkskcq8-02-caution.png?alt=media&token=f56e6e61-6119-41d8-920c-26f6a7cdafff>)

_Reserve the use of slots for use cases that maintain the menu’s accessibility and functionality_

## Focus

**Initial focus**

When a menu opens, focus should be placed on the first menu item. This allows people using a keyboard or other assistive technologies to begin navigating the menu immediately.

**Exiting a menu**

People expect to exit a menu by:

-   Selecting an option
-   Tapping **Escape** or outside of the menu 
-   Using the system back button

Where focus is placed after closing the menu depends on the app.

![4 common keyboard navigation methods for menus on Android and web.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmhkewf5t-03.png?alt=media&token=374bb47a-9f22-409d-b8e5-f427a1f35c00>)

_Keyboard navigation on Android and web: Tab to select a menu item; Space or Enter to open a menu; Space or Enter to select a menu item; Escape to close a menu_

## Keyboard navigation

| **Keys** | **Actions** |
| --- | --- |
| **Tab** | Focus lands on menu |
| **Space** or **Enter** | For closed menus: Opens menu or submenu For open menus: Selects a menu item |
| **Up** and **Down** arrows | For closed menus: Opens menu For open menus: Moves focus to the next item |
| **Left** and **Right** arrows | Opens or closes a submenu |
| **Letters** | Focus moves to the next menu item starting with letter |
| **Escape** | Closes menu |

## Interactability

Disabled menu items can receive focus but aren't selectable.

Dividers and gaps can't receive focus.

![A disabled menu item “Share” is in focus.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmhkkp9r3-05-do.png?alt=media&token=4dda61ae-eb7f-45b1-b02f-d88aed80dd28>)

_Disabled menu items can receive focus_

![A divider with focus.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmhkkqmt3-05-don't.png?alt=media&token=f3178013-9a18-4ac1-9d98-c21a2876ea6d>)

_A divider or gap can’t receive focus_

## Labeling elements

Accessibility labels are used with assistive technology devices like screen readers. 

The accessibility label should be the same as the menu item text.

The role is [dependent on platform](accessibility.md#9c562e2c-da3a-4212-a2e3-ac91ba450b65).

![A “Preview” menu item has an accessibility label of ”preview”.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmhkl2sea-07.png?alt=media&token=9603d6f5-030f-41e2-84e6-2af9fd0deee5>)

_The menu item’s accessibility label aligns with the UI text_

| **Element** | **A11y label** | **Role (Web)** | **Role (Android Views) ** | **Role (Jetpack Compose)** |
| --- | --- | --- | --- | --- |
| Menu item text | Preview | Menu item | Generic actionable element | Generic actionable element |

For menu items with text and an icon, the icon’s accessibility label should be marked as **decorative** to avoid redundant verbalizations.

![A menu item icon of an eye next to the word “preview” has a note of “Decorative.”](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmhkkynea-08.png?alt=media&token=3adbfb1f-b0e8-4cd9-8d17-918d2830ddac>)

_For menu items with text and an icon, the icon’s accessibility label is decorative_

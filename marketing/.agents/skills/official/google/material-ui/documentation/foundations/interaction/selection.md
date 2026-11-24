---
title: "Selection"
source_url: "https://m3.material.io/foundations/interaction/selection"
lastmod: "2025-09-26"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "foundations"
assets:
  - "assets/images/70fe9c1e77cf-m1qhf1tb-1.png"
  - "assets/images/0d94b318f636-lwvowprh-2.png"
  - "assets/images/a71a80ac45fc-lfwnm6vf-1P-Touch-2x.png"
  - "assets/videos/459fc0d66720-lwvozn9v-GM3-Foundations-Interaction-Selection-Guidelines-2-v02.mp4"
  - "assets/images/13db9b9c2bc0-lfwnntlu-1P-Larger-selection-do.png"
  - "assets/images/3f4dc2a6f06f-lfwo38nu-1P-Larger-selection-dont.png"
  - "assets/images/95dc04e70036-lfwnp3as-1P-Click.png"
---

# Selection

Selection is how people interact with UI elements or choose which items to act on

-   Selection is shown through changes to surface color or other visible elements
-   An entire component can be selected, or just certain parts in a component
-   Selection can be performed via tap, cursor, keyboard, or voice

## Resources

| Type | Link | Status |
| --- | --- | --- |
| Design | [Design Kit (Figma)](http://goo.gle/m3-design-kit) | Available |

## Selection indicators

Selections are displayed using a check mark icon, a checkbox component, a change in surface color, or a combination.

Selections are inherited by the following components: 

-   Cards
-   Checkboxes
-   Chips
-   Data tables
-   Icon buttons
-   List items
-   Menu items
-   Pickers
-   Radio buttons
-   Segmented buttons
-   Sliders
-   Switch

![Seven types of selected  components.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm1qhf1tb-1.png?alt=media&token=f7a2e8d1-09f7-4b98-acfb-ee5b4536933b>)

_Selected components: Segmented buttons; Chips; List items; Checkboxes; Radio buttons; Switch; Slider_

The following components use an active indicator to represent which item is currently selected:

-   Navigation bar
-   Navigation drawer
-   Navigation rail
-   Tabs

The color and shape of the active indicator varies between components. In these components, only one item should be selected at a time.

![Tabs and navigation drawer with one destination item selected. Selection is identified with an active indicator.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwvowprh-2.png?alt=media&token=92cf2e82-6fc5-4fd0-a0a3-815191a386ca>)

_Selected components with active indicators: Tab; Navigation drawer_

## Types of selection

### Touch

On touch devices, select items using:

-   Long press touch or two-finger touch
-   Selection shortcut, if available, such as tapping an avatar

![Touch used to select 3 list items one after another.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flfwnm6vf-1P-Touch-2x.png?alt=media&token=63da0afb-4238-45e1-af6b-1a160d6be408>)

_Items in a list selected via touch_

### Entering and exiting selection mode

To select an item and enter selection mode, long press the item or use a shortcut, such as tapping the item’s avatar. To select additional items, tap each of them.

To exit a selection mode, tap each selected item until they’re unselected, or tap an action on the toolbar.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwvozn9v-GM3-Foundations-Interaction-Selection-Guidelines-2-v02.mp4?alt=media&amp;token=ab793705-f19c-433b-a4e7-4c704da83472" title="List items are tapped to select and unselect them."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwvozn9v-GM3-Foundations-Interaction-Selection-Guidelines-2-v02.mp4?alt=media&token=ab793705-f19c-433b-a4e7-4c704da83472>)

### Larger selections

To select multiple items simultaneously, long press and drag across items. Don’t use this gesture combination if it is already in use to pick up and move items, like cards.

![Long press and drag used to select multiple images in a photo feed.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flfwnntlu-1P%20-%20Larger%20selection-do.png?alt=media&token=974afcaf-586f-412f-8088-e1f203cdb62b>)

_Long press and drag can be used together to select items in batches_

![Long press and drag combination used to move cards, but can’t be used to batch select items while in use.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flfwo38nu-1P%20-%20Larger%20selection-dont.png?alt=media&token=7ee8631f-7607-49d0-b6d4-c4562c37bde8>)

_If the long press and drag combination is already in use to pick up and move components, like cards, then the combined gesture can’t also be used for selecting items in batches_

### Click

On desktop, checkboxes are always visible when selection is the primary activity. When selection is secondary, checkboxes (or other indicators) are displayed:

-   As a single checkbox for that item on hover
-   For all items after one item is selected

To make a selection, hover over an item to reveal a checkbox. The checkbox can then be clicked.

![Checkboxes being selected and unselected.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flfwnp3as-1P%20-%20Click.png?alt=media&token=781beb06-a277-4d39-9408-60a1ae32a1f3>)

_Checkboxes are visible by default in this table because selection is a primary activity_

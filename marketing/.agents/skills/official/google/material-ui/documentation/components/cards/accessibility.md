---
title: "Cards"
source_url: "https://m3.material.io/components/cards/accessibility"
lastmod: "2026-05-18"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "components"
assets:
  - "assets/images/05253f4916f8-lwuobzmz-1.png"
  - "assets/videos/5acaf6325378-lwuonrmp-GM3-Components-Cards-Accessibility-1-v01-3.21.14-PM.mp4"
  - "assets/videos/efc6bbe15771-m6gok77f-Components-Cards-Accessibility-1-v01-(1).mp4"
  - "assets/images/e0500b852552-m4ivybyo-3-a.png"
  - "assets/videos/20ab9fe510a6-lwuoolew-GM3-Components-Cards-Accessibility-2-v01-3.24.03-PM.mp4"
  - "assets/videos/fb65c366b40e-lwuop9fg-GM3-Components-Cards-Accessibility-3-v01.mp4"
  - "assets/images/b5d30b7b33e3-lwuodgu3-5.png"
  - "assets/images/59c82666be61-lwuoeob6-6.png"
  - "assets/images/7f33570dd561-mk6jw3io-09.png"
---

# Cards

Cards display content and actions about a single subject

## Accessibility

## Use cases

People should be able to do the following using assistive technology:

-   Navigate to a card and the elements within a card 
-   Get appropriate feedback based on input type documented under [Interaction & style](accessibility.md#ce764d54-6b59-42db-807f-b3cb370eecdb)

## Interaction & style

A card can be a non-actionable container that holds actions like buttons and links, or it can be directly actionable without any buttons or links. This is to avoid stacking actionable elements. An action shouldn’t be placed on an actionable surface.

![2 possible card interaction behaviors.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwuobzmz-1.png?alt=media&token=43522d6c-0a33-4b98-8cf8-3e7db99cd337>)

_Non-actionable card with buttons; Directly actionable card with no buttons_

### Touch

When a user taps on a directly actionable card, a touch ripple appears across the card, indicating feedback. 

Non-actionable cards don’t ripple.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwuonrmp-GM3-Components-Cards-Accessibility-1-v01%203.21.14%E2%80%AFPM.mp4?alt=media&amp;token=98280acb-35c0-466e-8b8c-a8301adbd956" title="Ripple effect created when tapping on a card."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwuonrmp-GM3-Components-Cards-Accessibility-1-v01%203.21.14%E2%80%AFPM.mp4?alt=media&token=98280acb-35c0-466e-8b8c-a8301adbd956>)

### Dragging and dismissing

To meet Material's accessibility standards, any dragging and swiping interactions need a single-pointer alternative, like selecting the same actions from a menu.

For example, tapping a card, or pressing and holding, should open a menu to change its position in a list. That menu could also contain an action to delete the card.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fm6gok77f-Components-Cards-Accessibility-1-v01%20(1).mp4?alt=media&amp;token=1d8244ec-d550-41e5-8cde-ef41ffb5d001" title="A list of cards has its order reshuffled using a single-pointer option."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fm6gok77f-Components-Cards-Accessibility-1-v01%20(1).mp4?alt=media&token=1d8244ec-d550-41e5-8cde-ef41ffb5d001>)

It isn’t recommended to place menus on top of the card on the draggable state. If doing so is necessary, ensure that the interaction can be completed.

![A menu over a card that doesn't totally obscure the card.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fm4ivybyo-3-a.png?alt=media&token=dd43ecb5-f57b-4079-bda5-e822258fb7c9>)

_Ensure that the menu doesn't cover the card_

### Cursor

When a directly actionable card is hovered, the hover state provides a visual cue to the person that the element is interactive. Non-actionable cards don’t have a hover state.

When a directly actionable card is clicked, a ripple appears, providing feedback.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwuoolew-GM3-Components-Cards-Accessibility-2-v01%203.24.03%E2%80%AFPM.mp4?alt=media&amp;token=2d157501-1a56-4c87-afa9-bfcc84685d50" title="Hover state and touch ripple effect of a card."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwuoolew-GM3-Components-Cards-Accessibility-2-v01%203.24.03%E2%80%AFPM.mp4?alt=media&token=2d157501-1a56-4c87-afa9-bfcc84685d50>)

### Keyboard

A focus indicator appears around actionable elements when tabbing through cards. This provides a visual cue to a person that the destination is now focused and an action can be taken.

A person can **Tab** to navigate between actionable elements of the cards. If the cards are non-actionable, **Tab** navigates directly to the actionable buttons or links within the cards.

When engaging with a focused actionable card or element using the **Space** or **Enter** key, an action is performed or a secondary action is available, such as a menu.

Within the menu, a person is able to **Arrow** through the menu items, **Space** or **Enter** to select an item, or **Tab** to exit.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwuop9fg-GM3-Components-Cards-Accessibility-3-v01.mp4?alt=media&amp;token=e9ed72c1-14d5-438a-9db4-7356c58e9d8b" title="Navigating cards with a keyboard."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwuop9fg-GM3-Components-Cards-Accessibility-3-v01.mp4?alt=media&token=e9ed72c1-14d5-438a-9db4-7356c58e9d8b>)

## Focus

All interactive elements of cards need a tab stop so they can be focused. Directly actionable cards are tab stops.

For non-actionable cards, the card itself is not a tab stop. However, every actionable element in the card is a tab stop so they’re all visited before focus navigates to the next card.

![The focus areas of a card with interactive elements.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwuodgu3-5.png?alt=media&token=3f2cf54f-c991-49bf-bf2a-447a25e2657a>)

_Use Tab to navigate through all buttons in a card_

![Cards forming a list on mobile and cards forming a gallery on tablet.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwuoeob6-6.png?alt=media&token=be7c42c2-1676-4bb7-a654-8bdfcc83691b>)

_Card layouts can change on different devices_

## Keyboard navigation

| Keys | Actions |
| --- | --- |
| **Tab** | Move to the next actionable element **Directly actionable cards:** Move to next card container **Non-actionable cards with actionable elements:** Move to next actionable element |
| **Space** or **Enter** | Confirm action |

## Labeling elements

The informative contents of a card are verbalized when navigating to them using a screen reader. If an image in a card is purely decorative, hide it from screen readers. All actionable elements must receive both screen reader and keyboard focus.

Directly actionable cards can have the **button** or **link** role, depending on how they’re used. 

Non-actionable cards are purely containers, so they don’t need a role.

![Card elements annotated in the order that a screen reader tabs through them.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmk6jw3io-09.png?alt=media&token=e51c3965-4c45-4a0d-bdc7-9874af242848>)

_Non-actionable card elements are navigable, focused in order, and verbalized when in focus. In this example, the order is: Heading; Image; Body text; Primary button; Secondary button_

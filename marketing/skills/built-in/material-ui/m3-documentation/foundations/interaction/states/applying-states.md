---
title: "States"
source_url: "https://m3.material.io/foundations/interaction/states/applying-states"
lastmod: "2025-08-05"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "foundations"
assets:
  - "assets/images/6cd1c2bf3bcf-ly2qpw4a-1.png"
  - "assets/images/36a36b70dd19-ly2qrzig-2.png"
  - "assets/images/370d0fee2cf4-ly2qtg0r-3.png"
  - "assets/images/aa9fd8cbba50-ly2quyuj-4.png"
  - "assets/videos/d15d6d433ed3-ly2vtf7p-GM3-Foundations-Interaction-States-DisabledBehavior-v01.mp4"
  - "assets/images/a88723f39ec0-lyml0ix7-6.png"
  - "assets/images/a61668434eb6-ly2vwc74-7.png"
  - "assets/images/e13366e50df5-ly2vxl8i-8.png"
  - "assets/images/5960b03e3b8b-ly2vymng-9.png"
  - "assets/videos/37d3204bd314-ly2w0utg-GM3-Foundations-Interaction-States-HoverBehavior-1-v01.mp4"
  - "assets/videos/38cd17fd63d5-ly2w4tl9-GM3-Foundations-Interaction-States-HoverBehavior-2-v01.mp4"
  - "assets/videos/532a6343562a-ly2w8zl5-GM3-Foundations-Interaction-States-HoverBehavior-3-v01.mp4"
  - "assets/images/ca670e39fd1b-ly2wa79s-13.png"
  - "assets/images/038aa3177299-ly2wb5y9-14.png"
  - "assets/images/94651d4040b6-ly2wctgk-15.png"
  - "assets/images/14407d225f89-ly2wdmfh-16.png"
  - "assets/images/79bc90873a46-ly2wf3qd-17.png"
  - "assets/videos/0ebe9f5d0ce6-ly2whda4-GM3-Foundations-Interaction-States-FocusedBehavior-v01.mp4"
  - "assets/images/614931561d9a-ly2wj03t-25.png"
  - "assets/images/be2bac79affa-ly2wk46q-26.png"
  - "assets/images/bba5efb6a591-ly2wl23l-27.png"
  - "assets/images/0b2d859dc84a-ly2wo5k7-28.png"
  - "assets/videos/fc1a20dab9f2-ly2wqo0t-GM3-Foundations-Interaction-States-PressedBehavior-1-v01.mp4"
  - "assets/videos/e215a8435977-ly2wrufa-GM3-Foundations-Interaction-States-PressedBehavior-2-v01.mp4"
  - "assets/videos/3bd025b052bd-ly2wt4k8-GM3-Foundations-Interaction-States-PressedBehavior-3-v01.mp4"
  - "assets/images/fa7acea7e79a-ly2wu37e-32.png"
  - "assets/images/1ec3ef560b9f-ly2wv77r-33.png"
  - "assets/images/077535ce7668-ly2ww2lq-34.png"
  - "assets/videos/ac5a9f7741f6-ly2wxglg-GM3-Foundations-Interaction-States-DraggedBehavior-1-v01.mp4"
  - "assets/videos/8bd4f296e173-ly2wyr5e-GM3-Foundations-Interaction-States-DraggedBehavior-2-v01.mp4"
---

# States

States show the interaction status of a component or UI element

## Applying states

## Enabled

An enabled state communicates an interactive component or element. Enabled states use the default styling for each interactive component.

![Enabled state of 4 components.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fly2qpw4a-1.png?alt=media&token=0bec13e6-8bd8-4dc3-9439-6e9d9ff68b4e>)

_Enabled states for: Button; FAB; Switch; Text field_

## Disabled

A disabled state communicates when a component or element isn’t interactive. This state is visually communicated through color changes and reduced elevation.

**Disabled states don't need to meet Material's contrast requirements.**

![Low opacity solitary button labeled disabled, indicates a  disabled/inoperable state.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fly2qrzig-2.png?alt=media&token=fc37d2ef-40ff-4d49-9c55-d1c580dae532>)

_Disabled button_

Disabled states **are** inherited by action, selection, and input components:

-   Buttons
-   Cards
-   Checkboxes
-   Chips
-   List items
-   Radio buttons
-   Switches
-   Text fields

![Inoperable state of 4 components.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fly2qtg0r-3.png?alt=media&token=469e37f6-f896-42c0-8d8a-2024ad74a6ee>)

_Disabled states for: Checkbox; Icon button; Radio button; Segmented button_

Disabled states **aren't** inherited by communication, containment, navigation, and some actions components: 

-   App bars
-   Badges
-   Dialogs
-   Floating action buttons (FABs)
-   Menus
-   Navigation bar, drawer, and rail
-   Sheets
-   Tabs
-   Tooltips

![Screen erroneously showing edit FAB in inoperable state, though the edit function is unavailable.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fly2quyuj-4.png?alt=media&token=3366711f-aafa-4b7e-ab99-1a0f6c67378e>)

_If the action represented in the FAB is unavailable, the FAB shouldn't appear_

### Behavior

Disabled components can’t be focused, dragged, or pressed, and they don’t change state when tapped or hovered over.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fly2vtf7p-GM3-Foundations-Interaction-States-DisabledBehavior-v01.mp4?alt=media&amp;token=929e0dbc-d1ff-4268-8c6a-d41e1a476f0e" title="A cursor moves over and clicks on an disabled/inoperable button and the button doesn’t change."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fly2vtf7p-GM3-Foundations-Interaction-States-DisabledBehavior-v01.mp4?alt=media&token=929e0dbc-d1ff-4268-8c6a-d41e1a476f0e>)

There can be any number of disabled states in a layout.

![Disabled components on a screen.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flyml0ix7-6.png?alt=media&token=c6c1365d-7e53-471d-a1e4-8babbf37b298>)

_Disabled redo icon button; Disabled checklist icon button_

## Hover

Hover states are initiated by the user pausing over an interactive element using a cursor.

The lower-emphasis surface overlay for hover states can be applied to the entire component, elements within a component, or as a circular shape over part of the component.

![Cursor moves toward button which reads “enabled” and when the cursor touches the button text changes to “hovered.”](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fly2vwc74-7.png?alt=media&token=728c0b8a-43b5-49a2-8640-a0c368d2e966>)

_Hovered button_

Hover states **are** inherited by action, selection, and input components:

-   Buttons
-   Cards
-   Checkbox
-   Chips
-   Date and time pickers
-   List items
-   Slider
-   Switch
-   Text fields

![Hover state of 4 components.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fly2vxl8i-8.png?alt=media&token=4f810fc7-a579-445c-a178-ebc7a19caeb2>)

_Hovered states for: FAB; Icon button; Chip; Segmented buttons_

Hover states **aren’t** inherited by communication, containment, or navigation components: 

-   App bars
-   Badges
-   Dialogs
-   Menus
-   Navigation bar, drawer, and rail
-   Sheets
-   Tabs

![Mobile screen with the whole  app bar wrongly in hover state.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fly2vymng-9.png?alt=media&token=5cf648b1-7239-43fd-a65f-9198fd11f289>)

_The individual components that are actionable within the app bar inherit hover states, not the whole app bar_

### Behavior

Hover states are initiated by the user pausing over an interactive element using a cursor.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fly2w0utg-GM3-Foundations-Interaction-States-HoverBehavior-1-v01.mp4?alt=media&amp;token=d62f89ca-33da-4b39-97a9-f766f45ba432" title="Button’s text“Enabled”  changes to“Hovered” when cursor moves over the button."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fly2w0utg-GM3-Foundations-Interaction-States-HoverBehavior-1-v01.mp4?alt=media&token=d62f89ca-33da-4b39-97a9-f766f45ba432>)

Hover states can be combined with focused, activated, selected, or pressed states.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fly2w4tl9-GM3-Foundations-Interaction-States-HoverBehavior-2-v01.mp4?alt=media&amp;token=e749787c-d5e7-4820-bab3-31b445b90302" title="Filter chip text matches state as it's unselected, hovered, and selected by a cursor."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fly2w4tl9-GM3-Foundations-Interaction-States-HoverBehavior-2-v01.mp4?alt=media&token=e749787c-d5e7-4820-bab3-31b445b90302>)

There can only be one hover state at a time in a layout.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fly2w8zl5-GM3-Foundations-Interaction-States-HoverBehavior-3-v01.mp4?alt=media&amp;token=e1229bd4-baf1-44c7-9f68-3f0f56c1afb7" title="Hover state moves from one card to another with cursor movement. "></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fly2w8zl5-GM3-Foundations-Interaction-States-HoverBehavior-3-v01.mp4?alt=media&token=e1229bd4-baf1-44c7-9f68-3f0f56c1afb7>)

## Focused

A focused state communicates when a user has highlighted an element using a keyboard or voice. Focus states apply to all interactive components.

The higher-emphasis surface overlay for focused states can be applied to the entire component, elements within a component, or as a circular shape over part of the component.

![A button in focused state.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fly2wa79s-13.png?alt=media&token=7bf4ddd9-516f-4ab7-b20e-e19ab3a26cdd>)

_Focused button_

Focus states **are** inherited by action, selection, and input components:

-   Buttons
-   Cards
-   Checkbox
-   Chips
-   Date and time pickers
-   List items
-   Selection controls
-   Text fields

![Focus state of 4 components.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fly2wb5y9-14.png?alt=media&token=292090a2-1e6f-4e36-84ba-5ebafeb0e3af>)

_Focused states for: FAB; Icon button; Chip; Segmented buttons_

Focus states **aren’t** inherited by most communication, containment, or navigation components:

-   App bars
-   Badges
-   Banner
-   Card
-   Dialogs
-   Navigation bar, drawer, and rail
-   Sheets

![Mobile screen showing  entire app bar in focus state, which is an error.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fly2wctgk-15.png?alt=media&token=871fc6c8-7cf7-48d9-aed1-4f876be97c67>)

_The individual components that are actionable within the app bar inherit focus states, not the whole app bar_

### Keyboard focus indicator

Many people use the **Tab** key or other shortcut to navigate the interactive elements of a web page, like links, buttons, and chips.

When an element is tabbed to, it appears in its focused state with a ring-like **keyboard focus indicator**. This indicator helps web users know where they are on the page.

While focused, an element can be acted on with the keyboard.

![A single filled button in focused state, displaying the keyboard focus indicator.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fly2wdmfh-16.png?alt=media&token=68221847-c082-4963-8058-4e75a9c38465>)

_Keyboard focus indicator for a filled button_

### Behavior

Focus states are initiated by the user by pressing the **Tab** key on the keyboard (or equivalent).

Focus states can be represented in combination with hover, activated, or selected states.

![A single filter chip simultaneously in selected state and focus state.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fly2wf3qd-17.png?alt=media&token=67048848-6bba-4299-9dfd-3b5fe3c753ec>)

_A selected filter chip in both selected and focused states_

There can only be one focus state at a time in a layout.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fly2whda4-GM3-Foundations-Interaction-States-FocusedBehavior-v01.mp4?alt=media&amp;token=7154f56e-a56d-423e-8de2-fea1290c9ef4" title="Cursor moving from one card in focus state to another card moves the focus state to the second card. "></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fly2whda4-GM3-Foundations-Interaction-States-FocusedBehavior-v01.mp4?alt=media&token=7154f56e-a56d-423e-8de2-fea1290c9ef4>)

## Pressed

A pressed state communicates a user-initiated tap or click via cursor, keyboard, or voice input. This state applies to all interactive components.

Pressed states trigger a change in composition and should be high-emphasis.

A ripple overlay signifies a pressed state. It can be applied to an entire component or elements within a component, or as a circular shape over part of the component.

![Button using a ripple overlay to signify it’s in a pressed state.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fly2wj03t-25.png?alt=media&token=776a2614-2e05-4545-96d3-a6bc53947375>)

_Pressed button_

Some components, such as buttons or cards, can inherit elevation to signify a pressed state.

![Button using elevation to signify it’s in a pressed state.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fly2wk46q-26.png?alt=media&token=b1e55cc3-9ee8-44b0-8722-0a4a23636069>)

_Pressed button with elevation_

Pressed states **are** inherited by action, selection, and some containment components: 

-   Buttons
-   Cards
-   Checkbox
-   Chips
-   List items
-   Text fields

![Four components shown in pressed state.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fly2wl23l-27.png?alt=media&token=b2dff0e0-1bf2-47fb-a689-040b778ffcf1>)

_Pressed states for: FAB; Icon button; Chip; Segmented button_

Pressed states **aren’t** inherited by communication, navigation, or some containment components: 

-   App bars
-   Badges
-   Bottom navigation
-   Dialogs
-   Menus
-   Sheets
-   Tabs

![Mobile screen showing  entire app bar in pressed state is an error.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fly2wo5k7-28.png?alt=media&token=93cc4266-18d2-497d-a0d9-55a73988142f>)

_The individual components that are actionable within the app bar inherit pressed states, not the whole app bar_

### Behavior

Pressed states are initiated by user keyboard or voice input on an interactive element.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fly2wqo0t-GM3-Foundations-Interaction-States-PressedBehavior-1-v01.mp4?alt=media&amp;token=878dc910-8f6c-4cb6-bfdb-3fb88a2ee40a" title="Enabled state activated to pressed state."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fly2wqo0t-GM3-Foundations-Interaction-States-PressedBehavior-1-v01.mp4?alt=media&token=878dc910-8f6c-4cb6-bfdb-3fb88a2ee40a>)

Pressed states can be combined with hovered, focused, activated, or selected states.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fly2wrufa-GM3-Foundations-Interaction-States-PressedBehavior-2-v01.mp4?alt=media&amp;token=37adbaa0-9c13-4684-90fe-ed200d7f9013" title="Hovered state activated to a pressed state."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fly2wrufa-GM3-Foundations-Interaction-States-PressedBehavior-2-v01.mp4?alt=media&token=37adbaa0-9c13-4684-90fe-ed200d7f9013>)

There may only be a single pressed state at a time in a layout.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fly2wt4k8-GM3-Foundations-Interaction-States-PressedBehavior-3-v01.mp4?alt=media&amp;token=6f49e3db-4639-4ed8-8556-54a86ee743c4" title="Pressed state on one card at a time."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fly2wt4k8-GM3-Foundations-Interaction-States-PressedBehavior-3-v01.mp4?alt=media&token=6f49e3db-4639-4ed8-8556-54a86ee743c4>)

## Dragged

A dragged state occurs when a user presses and moves an element or component. Dragged states should be low emphasis, to avoid distracting users from their task.

Dragged states use a lower emphasis overlay. It can be applied to the entire component or to elements within a component.

Some components, such as list items, chips, or cards, can inherit elevation to signify a dragged state.

![List item shown in dragged state.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fly2wu37e-32.png?alt=media&token=fefabf35-2cb5-4fdd-b0bb-4cee4442d148>)

_Dragged list item_

Dragged states **are** inherited by some containment and selection components: 

-   Cards
-   Chips
-   List items
-   Sliders

![A chip and a card both shown in dragged state.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fly2wv77r-33.png?alt=media&token=a3ace83a-7173-47cb-9fc2-60646fe0dd47>)

_Dragged states for: Chip; Card_

Dragged states **aren’t** inherited by action, communication, navigation, or some containment components: 

-   App bars
-   Badges
-   Buttons
-   Dialogs
-   Menus
-   Navigation bar, drawer, and rail

![Mobile screen with app bar in dragged state is an error.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fly2ww2lq-34.png?alt=media&token=33a7d3f2-493d-4137-a038-289ddac5488c>)

_Components like an app bar that require consistent placement should not inherit dragged states_

### Behavior

Dragged states are initiated when users touch and hold elements, using an input method such as a tap or click.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fly2wxglg-GM3-Foundations-Interaction-States-DraggedBehavior-1-v01.mp4?alt=media&amp;token=1a50ed48-7d69-4df1-b0d2-37034ad3431b" title="Going through the states of a draggable list item:  enabled, hovered, dragged."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fly2wxglg-GM3-Foundations-Interaction-States-DraggedBehavior-1-v01.mp4?alt=media&token=1a50ed48-7d69-4df1-b0d2-37034ad3431b>)

There may only be a single dragged state at a time within a layout.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fly2wyr5e-GM3-Foundations-Interaction-States-DraggedBehavior-2-v01.mp4?alt=media&amp;token=6ea2d536-318a-4749-9233-52eefb9baab5" title="Cursor dragging cards one at a time."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fly2wyr5e-GM3-Foundations-Interaction-States-DraggedBehavior-2-v01.mp4?alt=media&token=6ea2d536-318a-4749-9233-52eefb9baab5>)

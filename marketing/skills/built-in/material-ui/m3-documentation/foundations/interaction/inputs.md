---
title: "Inputs"
source_url: "https://m3.material.io/foundations/interaction/inputs"
lastmod: "2025-09-26"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "foundations"
assets:
  - "assets/images/3345346605b4-lwvoasbb-1.png"
  - "assets/images/febcb8e17f49-m4781mpt-2.png"
  - "assets/images/78ea7f703bf9-lwvoce8y-3.png"
  - "assets/images/41cd92893d95-lwvocxli-4.png"
  - "assets/images/56ae7b461cf6-m47884lw-Frame-1321316889.png"
  - "assets/images/61f197ddb72e-lwvoetuu-6.png"
  - "assets/images/63f2daa416e5-lwvofwmm-7.png"
  - "assets/images/0b64c41b9494-m478zll6-8.png"
  - "assets/images/a779442b80e3-lwvogkod-8.png"
  - "assets/images/4fea6b007841-m4793i0f-8-(1).png"
  - "assets/images/02883b1f4714-m4798kwo-9.png"
  - "assets/images/c05d376f9748-m479c91z-12-(1).png"
  - "assets/images/4499515e9453-m48p2zdz-13-(2).png"
  - "assets/images/77bb6be06597-m48p38qk-14-(2).png"
  - "assets/images/985943182f04-m48p68lr-12-(2).png"
  - "assets/videos/d0a75072d9c5-lwyimd28-GM3-Foundations-Interaction-Inputs-Guidelines-1-v01.mp4"
  - "assets/videos/d41aa379dabc-lwyinkvl-GM3-Foundations-Interaction-Inputs-Guidelines-2-1-v01.mp4"
  - "assets/videos/e3c0d03cb870-lwyioh3s-GM3-Foundations-Interaction-Inputs-Guidelines-2-2-v01.mp4"
  - "assets/videos/03bf17359603-lwyipb8r-GM3-Foundations-Interaction-Inputs-Guidelines-3-v01.mp4"
  - "assets/images/c8bf460dd741-m0b7vzhy-19.png"
  - "assets/images/1db502fdf72c-m0b7w8fa-20.png"
  - "assets/videos/50860ee9d6d7-lwyiq9rm-GM3-Foundations-Interaction-Inputs-Guidelines-4-v01.mp4"
  - "assets/videos/122d052eb30c-lwyirw27-GM3-Foundations-Interaction-Inputs-Guidelines-5-v01.mp4"
  - "assets/images/562471f398a8-m0b7yvt7-23.png"
  - "assets/images/fea484d653d9-m0b7zlqx-24.png"
  - "assets/videos/909eb10ff5b6-mb9r9yci-G_Foundations_Interaction+Inputs_1_IA_v04-(1).mp4"
  - "assets/videos/db8b14d17a52-mb9rakzb-G_Foundations_Interaction+Inputs_2_IA_v02-(1).mp4"
  - "assets/videos/8a8c006d81f1-mb9rbhsj-G_Foundations_Interaction+Inputs_3-_IA_v02-(1).mp4"
---

# Inputs

Inputs are devices that provide interactive control of an app. Common inputs include a mouse, keyboard, or touchpad.

-   Design for touch, keyboard, and mouse interactions
-   Embrace multiple input methods and gestures within your app

![Editing interface on a large screen device. The selected text is highlighted and the text cursor is visible.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwvoasbb-1.png?alt=media&token=2706b8e6-4f43-4a00-9f8c-313bf1ca33d1>)

_Designing for inputs allows people to use the inputs they prefer, like a mouse to highlight text on a tablet_

## External inputs for devices

People can use external inputs like a mouse, keyboard, or stylus with their phone, tablet, foldable, TV, laptop, or desktop computer. When someone connects an external input to their device, they expect it to behave in familiar and useful ways. Designing for different input methods can make a product more usable and accessible on all screen sizes.

### Common features of external inputs

#### **Mouse**

-   Left and right click
-   Mouse wheel
-   Extra buttons

#### **Trackpad**

-   Left and right click
-   Gestures
-   Haptics

#### **Physical keyboard**

-   Replaces virtual keyboard
-   Media keys
-   Modifier keys

![Image of a mouse.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fm4781mpt-2.png?alt=media&token=4b1a2542-90ca-4586-b809-6c20fa5aa034>)

![Image of a laptop keyboard and trackpad.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwvoce8y-3.png?alt=media&token=858a71ae-a1ab-403a-adb6-074566f3b0b0>)

![Image of a keyboard.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwvocxli-4.png?alt=media&token=fe5784ee-f092-40d6-9c96-4b3b6e1b4a10>)

### Input device behaviors

Depending on the input device, designers and developers can implement behaviors that meet standard conventions and user expectations.

| Input device action | Anticipated behavior |
| --- | --- |
| Mouse and trackpad movement | Show a mouse cursor on the screen |
| Primary click | Treat mouse clicks differently than touch events |
| Secondary click | Activate context menus |
| Hover | Change component states |
| Highlight | Allow text to be selected by the mouse cursor |
| Mouse wheel and trackpad two finger drag | Scroll list vertically and horizontally |
| Trackpad pinch | Zoom an element or page |
| Physical keyboard | Hide and show on screen keyboard |

## Mouse and cursor interactions

When an external mouse input device is used, a mouse cursor should be shown, regardless of the device type. 

A mouse may be connected to tablets, laptops, phones, foldables, and more. On some devices, it's possible to use an external input device simultaneously with touch input. 

On devices that don't specifically recognize mouse or stylus input, the mouse is treated as touch input.

### Primary click

A mouse click or stylus tap should demonstrate the same feedback as touch input. One example of this is showing the ripple for a pressed state.

![A view of a display with a visible mouse cursor.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fm47884lw-Frame%201321316889.png?alt=media&token=27bba76e-597c-41ac-9446-7e9bafa37f3a>)

_A visible mouse cursor is seen when the external input is connected_

### Secondary click

#### Context menus

A secondary click (whether using a single button or two fingers on a trackpad) should activate a context menu. The context menu shows additional options for the object that's clicked. See [menus](../../components/menus/overview.md) for more usage and guidelines.

![A context menu pop up from a link with the options: Open link in new window, Save link as, Copy link location, and Inspect.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwvoetuu-6.png?alt=media&token=a1d1943b-8906-4e9f-96bd-e23ba631d331>)

_The context menu should appear when right clicking with a mouse or trackpad_

### Hover

When using a mouse cursor, help users discover interactive objects by enabling visual changes. When the mouse rests on an interactive element, the hover state is a valuable cue for interaction. See [states](https://m3.material.io/m3/pages/interaction-states/applying-states#71c347c2-dd75-485b-892e-04d2900bd844) for styles and guidelines.

Hovering with a cursor (or stylus) should also invoke tooltips when applicable. See [tooltips](../../components/tooltips/overview.md) for guidance.

![Icon button, floating action button, and menu items in their hovered and not hovered states.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwvofwmm-7.png?alt=media&token=c01eebb6-93d7-4e40-8156-947c480b8840>)

_Components without a hover state; Components with a hover state change applied_

### Cursors

Cursors appear when using external input devices like a mouse or trackpad. The cursor can change to communicate more information about interactive elements.

#### Pointer

By default, external input control should be rendered as a pointer.

![A cursor rendered as a pointer.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fm478zll6-8.png?alt=media&token=985719f0-888f-41a6-a5c9-23b21091f793>)

_A pointer provides a visible indicator for input controls_

#### Hand

The cursor should appear as a hand to indicate links or linked images.

![A link cursor (hand) shown when hovering over a linked image.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwvogkod-8.png?alt=media&token=d6119250-af14-4742-b20d-8f562a50274d>)

_The hand cursor is used for links and clickable images_

#### Resize arrows

The cursor should change to resize arrows on the boundaries of resizable elements.

![A cursor at the edge of the screen as resize arrows.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fm4793i0f-8%20(1).png?alt=media&token=b94e8e6f-b790-4812-9d82-c66d70716ba5>)

_Resize arrows indicate an element can be resized_

#### I-beam

The cursor should appear as an I-beam when hovering on text. When manipulating editable text, the following interactions apply:

-   Single click places the cursor

-   Double click selects a word

-   Triple click selects a paragraph

-   Single click deselects text and repositions the cursor

![I-beam cursor hovering over selectable text.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fm4798kwo-9.png?alt=media&token=c20ead4b-ac39-45c4-bc73-552fdd5d165e>)

_An I-beam cursor indicates selectable text_

### Text selection

When selecting text using a mouse, trackpad, or stylus:

-   Highlight the selected area using a single color

-   Don’t show touch controls next to the highlighted area

![Highlighted text in a single color.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fm479c91z-12%20(1).png?alt=media&token=9f6f5be4-8571-4cab-a135-962387634d1d>)

_Selected text shows a visible highlight_

### Text selection with touch control

When interacting using touch, always show touch controls, even if other inputs are connected.

When using a mouse, trackpad, or stylus, show the I-beam and context menu, even if it's a touch device.

![Touch controls are produced on selected text, with mouse and trackpad detected.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fm48p2zdz-13%20(2).png?alt=media&token=6efd6004-66c4-4c60-995c-8dc26a01a4a8>)

_When using a touchscreen to select text, show touch controls_

![Selected text with a context menu, with mouse and trackpad detected.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fm48p38qk-14%20(2).png?alt=media&token=3b6e4428-9260-4953-8261-9136214a9809>)

_When using a mouse, trackpad, or stylus to select text, use the right-click context menu_

### Stylus input

When using a stylus, cursors are usually not necessary, unless they communicate tool properties such as brush size or shape.

![A cursor rendered as a circle.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fm48p68lr-12%20(2).png?alt=media&token=94399daa-4563-4402-9729-3fe0b3becaa0>)

_The circle cursor indicates the selected stylus tool and size_

## Mouse wheel and trackpad gestures

When an external mouse or touchpad is used, the mouse wheel and trackpad gesture allow more actions.

### Vertical scroll

When a cursor is positioned on a list, the mouse wheel and two-finger touchpad gesture should allow vertical scrolling of the list.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwyimd28-GM3-Foundations-Interaction-Inputs-Guidelines-1-v01.mp4?alt=media&amp;token=e453e95b-714b-40bf-a625-b56df2ddc7e8" title="Stationary cursor over a scrolling field controlled by a mouse wheel or trackpad."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwyimd28-GM3-Foundations-Interaction-Inputs-Guidelines-1-v01.mp4?alt=media&token=e453e95b-714b-40bf-a625-b56df2ddc7e8>)

### Touch scroll & mouse text selection

Upon touch and drag gesture, the text area will scroll. With a mouse interaction, dragging in a text area will select the text.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwyinkvl-GM3-Foundations-Interaction-Inputs-Guidelines-2-1-v01.mp4?alt=media&amp;token=89770d63-2edd-41d8-b685-6800b9b78242" title="Animation of a swipe gesture to scroll the screen."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwyinkvl-GM3-Foundations-Interaction-Inputs-Guidelines-2-1-v01.mp4?alt=media&token=89770d63-2edd-41d8-b685-6800b9b78242>)

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwyioh3s-GM3-Foundations-Interaction-Inputs-Guidelines-2-2-v01.mp4?alt=media&amp;token=4d672035-b6d3-4b9a-8b44-8bfce0704b5e" title="Animation showing a dragging gesture with a mouse to select text and image."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwyioh3s-GM3-Foundations-Interaction-Inputs-Guidelines-2-2-v01.mp4?alt=media&token=4d672035-b6d3-4b9a-8b44-8bfce0704b5e>)

### Horizontal scroll

Mouse users should be able to scroll with a mouse wheel to navigate horizontally scrolling fields. Trackpad users should be able to scroll using a two-finger horizontal gesture.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwyipb8r-GM3-Foundations-Interaction-Inputs-Guidelines-3-v01.mp4?alt=media&amp;token=a8764711-9075-4402-854c-846901d78f12" title="A news feed of horizontally scrolling cards."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwyipb8r-GM3-Foundations-Interaction-Inputs-Guidelines-3-v01.mp4?alt=media&token=a8764711-9075-4402-854c-846901d78f12>)

## Physical keyboard

When a physical keyboard is connected to a device, either externally or as a built-in laptop keyboard, users should be able to perform any actions that the virtual keyboard provides, and more.

### Show and hide virtual keyboard

A virtual keyboard should appear or hide in response to the presence of a physical keyboard.

![Text being entered into a field with no on-screen keyboard displayed.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0b7vzhy-19.png?alt=media&token=7ae7282f-1e8d-4ce9-9fd5-7423bcf4e498>)

_When a physical keyboard is attached, hide the virtual keyboard_

![Text being entered into a field with an on-screen keyboard.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0b7w8fa-20.png?alt=media&token=51ece2e1-122e-45bc-9c5d-7ddf5a72ba7d>)

_When a physical keyboard is removed, show the virtual keyboard_

### Common keyboard interactions

#### Enter key

People typically expect the **E****nter** key on a physical keyboard to be enabled by developers to allow a common function like sending a message.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwyiq9rm-GM3-Foundations-Interaction-Inputs-Guidelines-4-v01.mp4?alt=media&amp;token=75c7d071-fac7-4a86-a05c-94e4cc01b20c" title="A message being typed into a chat, and sent using the Enter key."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwyiq9rm-GM3-Foundations-Interaction-Inputs-Guidelines-4-v01.mp4?alt=media&token=75c7d071-fac7-4a86-a05c-94e4cc01b20c>)

#### Spacebar control

People typically expect the **Spacebar** (or available media keys) to be enabled to play and pause music or video.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwyirw27-GM3-Foundations-Interaction-Inputs-Guidelines-5-v01.mp4?alt=media&amp;token=2fffdde8-09e9-4295-8d58-c267d2dda7b2" title="A video being paused and resumed by pressing the Spacebar. "></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwyirw27-GM3-Foundations-Interaction-Inputs-Guidelines-5-v01.mp4?alt=media&token=2fffdde8-09e9-4295-8d58-c267d2dda7b2>)

#### Tab focus

When keyboard users navigate a page using **Tab**, the focus on interactive items must follow a logical order. On most pages, that means left to right, top to bottom.

When focused from a keyboard or other input device, the focus state includes a ring-like keyboard focus indicator.

![Tab focus is on “small,” which is one of four size options for sweatshirts at an online store.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0b7yvt7-23.png?alt=media&token=0461477f-c0fb-48d3-b29b-fd0a463524c3>)

_Tab focus includes a visible keyboard focus indicator_

![Tab focus is on “medium,” which is one of four size options for sweatshirts at an online store.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0b7zlqx-24.png?alt=media&token=ebdbbf64-30b6-48f0-9195-7278153eb547>)

_The focus state moves elements as the user presses Tab on their keyboard_

#### Escape key

People typically expect the **Escape** key on a physical keyboard to dismiss elements, remove focus, or clear selections.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmb9r9yci-G_Foundations_Interaction%2BInputs_1_IA_v04%20(1).mp4?alt=media&amp;token=e874ca18-ce94-4a97-9b69-40ebba6348fd" title="Escape key dismisses menu."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmb9r9yci-G_Foundations_Interaction%2BInputs_1_IA_v04%20(1).mp4?alt=media&token=e874ca18-ce94-4a97-9b69-40ebba6348fd>)

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmb9rakzb-G_Foundations_Interaction%2BInputs_2_IA_v02%20(1).mp4?alt=media&amp;token=3b6d50da-6e7b-4848-a09f-cd36e054f1bf" title="Escape key removes focus indicator from icon."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmb9rakzb-G_Foundations_Interaction%2BInputs_2_IA_v02%20(1).mp4?alt=media&token=3b6d50da-6e7b-4848-a09f-cd36e054f1bf>)

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmb9rbhsj-G_Foundations_Interaction%2BInputs_3%20_IA_v02%20(1).mp4?alt=media&amp;token=b625aabb-99e8-4ef2-ac78-684da460843f" title="Escape key dismisses text cursor and leaves text itself unchanged."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmb9rbhsj-G_Foundations_Interaction%2BInputs_3%20_IA_v02%20(1).mp4?alt=media&token=b625aabb-99e8-4ef2-ac78-684da460843f>)

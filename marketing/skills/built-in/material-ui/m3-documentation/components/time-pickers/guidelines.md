---
title: "Time pickers"
source_url: "https://m3.material.io/components/time-pickers/guidelines"
lastmod: "2026-02-05"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "components"
assets:
  - "assets/images/eab2ea48c98c-md2pwu0i-01.png"
  - "assets/images/f585103a6c56-md2q22nz-02.png"
  - "assets/images/830a7aade71b-md2q5unb-03.png"
  - "assets/images/0303816b3230-md2q7fdq-04.png"
  - "assets/images/8c4a9d5363d8-md2u47yh-05.png"
  - "assets/images/4401777cef53-md2u4xoe-06.png"
  - "assets/images/753f333a4cec-md2u8t61-07.png"
  - "assets/images/6942c5a00319-md2ugmkj-08.png"
  - "assets/images/8321caedcb7b-md2uji74-09.png"
  - "assets/images/e774439d0be2-md2uqf01-10.png"
  - "assets/images/479b1df75b94-md2wil5z-11.png"
  - "assets/images/202689aa08bf-lw7tgqq3-12.png"
  - "assets/images/d256ead36487-md2uzinf-13.png"
  - "assets/images/cde136cddf0c-md2v1wt2-14_dont.png"
  - "assets/videos/6a6f67768b99-lw8wa0wg-GM3-Components-TimePickers-Guidelines-1-v01.mp4"
  - "assets/videos/9dc80cc6f3cf-lw8waqdc-GM3-Components-TimePickers-Guidelines-2-v01.mp4"
  - "assets/videos/6b09269442a4-lw8wbaye-GM3-Components-TimePickers-Guidelines-3-v01.mp4"
  - "assets/images/f29d8b7b3fce-md2v8xeq-18.png"
---

# Time pickers

## Guidelines

![Time picker. It has a dial and keyboard input for hours and minutes, and a setting for AM or PM.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmd2pwu0i-01.png?alt=media&token=a9c2389d-af1b-43a9-9c96-59e999c31a6c>)

_Dial selector time picker for a 12-hour clock_

## Usage

Time pickers allow people to enter a specific time value. They’re displayed in dialogs and can be used to select hours, minutes, or periods of time.

They can be used for a wide range of scenarios. Common use cases include:

-   Setting an alarm
-   Scheduling a meeting

Time pickers are not ideal for nuanced or granular time selection, such as milliseconds for a stopwatch application.

![Time picker with dial input selecting hour 7.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmd2q22nz-02.png?alt=media&token=029a62a3-faff-4ea6-aaff-d5c7e3d0145c>)

_Hour selection in a mobile calendar picker_

### Time input picker

Time input pickers allow people to specify a time using keyboard numbers. This input option should be accessible from any other mobile time picker interface by tapping the keyboard icon.

![Input time picker with keyboard active for the hour.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmd2q5unb-03.png?alt=media&token=9cbe187b-c1c2-4581-a086-4e51f593861d>)

_Hour input with keyboard entry_

### 24-hour time selection

The dial view can be changed to reflect time selection across 24 hours. This option is set outside of the time picker component, typically through system settings.

![Time picker with dial input selecting hour 20. Hours 0–11 use an outer dial, hours 12–23 use an inner dial.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmd2q7fdq-04.png?alt=media&token=4d6c29d8-df4a-4cb8-98a9-994284bb9984>)

_24-hour dial view_

## Anatomy

![17 elements of a dial time picker.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmd2u47yh-05.png?alt=media&token=a671138b-6e5b-4d58-8dd2-84aeea14195d>)

_Label (headline); Time selector separator; Input field; Input text; Period selector (selected); Period selector text (selected); Container; Period selector outline; Period selector text; Dial selector track; Dial label (selected); Text buttons; Icon button; Dial label (unselected); Clock dial; Input text (selected); Input field (selected); /_

![13 elements of an input time picker.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmd2u4xoe-06.png?alt=media&token=f64fe914-9b52-445d-b071-2f5833ff0f83>)

_Label (headline); Time selector separator; Input field; Input text; Period selector (selected); Period selector text (selected); Container; Period selector outline; Period selector text (unselected); Text buttons; Icon button; Input text (selected); Input field (selected)_

### Container

Like dialogs, the container should appear above other screen elements. To focus attention, surfaces behind the container have a temporary scrim overlay to make them less prominent.

![Time picker container, all elements inside.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmd2u8t61-07.png?alt=media&token=824ffe77-2a95-46e0-a1ab-3ca315414499>)

_The container includes all time picker elements_

### Input selector

The input selector is a unique kind of text field input. It differs from typical text field inputs in that it has:

-   An added highlight to call attention to the selected field

-   A larger shape, size, and font

-   A label below the field


Hours and minutes should have separate inputs. For people using a 12-hour clock, an AM/PM selector appears to the right of minutes. For people using a 24-hour clock, the AM/PM selector shouldn’t appear.

![Input time picker with the hour field active, and so highlighted.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmd2ugmkj-08.png?alt=media&token=52517d18-15f3-48f7-b64b-d0297ae21e2e>)

_Input selector for a 12-hour clock_

### Dial selector

Dial selectors always mimic a round watch face. Hours and minutes can be selected by tapping a number or dragging the dial selector track.

When representing a 12-hour dial, all numbers appear in the outer ring. When representing a 24-hour dial, even numbers appear in an inner ring, and odd numbers appear in an outer ring.

![Dial time picker with hour 7 selected.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmd2uji74-09.png?alt=media&token=829a268c-cf9b-43e6-83be-5c388f6d1908>)

_Dial selector for a 12-hour clock_

### Text & icon buttons

Icon buttons are used to switch between the input selector, represented by a keyboard, and the dial selector, represented by a clock.

Text buttons are used to exit the dialog (**Cancel**) and save the selector input (**OK**).

![Time picker buttons.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmd2uqf01-10.png?alt=media&token=2af68f32-68f1-4c1f-86e5-9d3fada4e35f>)

_The keyboard icon allows people to switch between the dial selector (pictured) and the input selector_

### Landscape orientation

The clock dial interface adapts to a device’s orientation. In landscape mode, the stacked input and selection options are positioned side-by-side.

![Time picker in landscape orientation on mobile.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmd2wil5z-11.png?alt=media&token=a0b081bd-3ad5-48bf-b57c-4ef8e5f2b6d2>)

_On mobile, the time picker can adapt to landscape orientation_

## Placement

Time pickers shouldn’t be obscured by other elements.  

Time pickers should change orientation or variant to ensure they aren't cropped by the edge of the screen.

Time pickers are modal windows above a scrim. This puts the time pickers at the forefront of a person's view, calling attention to make a selection of time.

![Input time picker in landscape orientation on mobile. It's fully visible despite the limited device height.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flw7tgqq3-12.png?alt=media&token=c0a7ba7e-4362-49d9-bf35-3bc8bcde7c5f>)

_The time picker should change to fit the size of the screen so the time picker is always fully visible_

## Adaptive design

Time pickers can swap between orientation or variant depending on device orientation and viewport constraints. 

For example, the time picker can change to landscape orientation on larger breakpoints or when viewport height is limited, to avoid scrolling the dial presentation. 

Time pickers can fallback to the input time picker when there isn’t enough vertical real estate to present the landscape orientation without scrolling.

![Dial time picker in portrait orientation on mobile.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmd2uzinf-13.png?alt=media&token=bb6a904b-40c3-4353-9c57-7299f455b020>)

_High-density time picker displayed on mobile_

### Density

Don’t apply density to the time picker dial when the viewport is constrained. Instead, use an input picker.

![Dial time picker in portrait orientation, too tall to fully fit on a mobile device in landscape mode.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmd2v1wt2-14_dont.png?alt=media&token=776c2e3f-538d-4912-8815-ef79f77aba0c>)

_Don’t apply density to the time picker dial when the viewport is constrained. Instead, use an input picker._

## Behavior

There are two primary methods for selecting time with the mobile time picker. People can:

-   Type in a specific value in the hour and minute fields
-   Select the hour or minute field from the text input and adjust the clock dial to simultaneously change the corresponding time field above

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flw8wa0wg-GM3-Components-TimePickers-Guidelines-1-v01.mp4?alt=media&amp;token=303bca0c-902b-49c5-9fd3-8d14448f02fc" title="Dial time picker possible interactions. Dial selector adjusts to match manual input."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flw8wa0wg-GM3-Components-TimePickers-Guidelines-1-v01.mp4?alt=media&token=303bca0c-902b-49c5-9fd3-8d14448f02fc>)

### Appearing & disappearing

Like other kinds of dialogs, time pickers use an enter and exit transition pattern to appear on the screen.

To exit a time picker, the input can either be confirmed (**OK**) or dismissed (**Cancel**). Interacting outside of the dialog will also dismiss the time picker. Unless one of these actions is taken, a time picker will continue to retain focus.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flw8waqdc-GM3-Components-TimePickers-Guidelines-2-v01.mp4?alt=media&amp;token=7a222829-9aba-4cdc-be4b-97160cb6602c" title="Time picker enter and exit transitions."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flw8waqdc-GM3-Components-TimePickers-Guidelines-2-v01.mp4?alt=media&token=7a222829-9aba-4cdc-be4b-97160cb6602c>)

### Toggle between dial & input

Tapping the keyboard icon on a mobile time picker switches the view to the input picker.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flw8wbaye-GM3-Components-TimePickers-Guidelines-3-v01.mp4?alt=media&amp;token=507fb3f4-bfff-4e43-8221-3efa566af7ee" title="Time picker transitioning between dial and input selectors."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flw8wbaye-GM3-Components-TimePickers-Guidelines-3-v01.mp4?alt=media&token=507fb3f4-bfff-4e43-8221-3efa566af7ee>)

### Scrolling

Time pickers should avoid scrolling, and swap component orientation or variant based on device orientation or viewport size. 

Time pickers don’t scroll with elements outside of the modal window, such as the background.

![Input time picker in landscape orientation to fit a mobile device in landscape mode.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmd2v8xeq-18.png?alt=media&token=b4c07a4b-ff96-497f-88bf-e9eca05fc5f8>)

_Time pickers shouldn’t scroll_

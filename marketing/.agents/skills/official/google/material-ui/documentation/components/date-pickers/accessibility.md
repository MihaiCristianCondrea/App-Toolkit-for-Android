---
title: "Date pickers"
source_url: "https://m3.material.io/components/date-pickers/accessibility"
lastmod: "2026-05-18"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "components"
assets:
  - "assets/images/7fce4b897815-md5tcq2g-01.png"
  - "assets/images/75a291fbdac9-md5tdrn3-02.png"
  - "assets/images/e8780951f42a-lwhl9jct-3.png"
  - "assets/images/32b6cd04198d-md5tjcxl-04.png"
  - "assets/images/be6fff7475a3-md5tl1kf-05.png"
  - "assets/images/b6ead49c4126-md5tmhkf-06.png"
  - "assets/images/1df34a213252-md5tnt2t-07.png"
  - "assets/images/8a3ca6b52703-md5tp1fs-08.png"
  - "assets/images/d8bc1d7a894e-md5tqs6i-09.png"
  - "assets/images/2cc678db0f9c-md5tw17z-10.png"
---

# Date pickers

Date pickers let people select a date, or a range of dates

## Accessibility

## Use cases

People should be able to:

-   Enter dates manually by inputting text, without using the picker
-   Use multiple input methods, making it accessible to those using assistive technology

On the docked date picker, the text field can be used for input.
On the modal date picker, the date input option should be available using the edit icon.

## Interaction & style

The edit icon indicates the ability to switch to the modal date input.

Interactive targets for all elements meet Material's 48x48dp minimum touch target requirement. Increasing density would negatively impact accessibility by limiting tappable/clickable targets.

![Date picker with the edit icon focused.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmd5tcq2g-01.png?alt=media&token=c53d3a22-1ce5-4bfb-bf36-77548890bd4a>)

_The edit icon indicates the ability to switch to the modal date input_

![Touch target used to select September 17 to 23 on a date picker.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmd5tdrn3-02.png?alt=media&token=2770d845-5def-4bc1-a7a9-22b8d83f43ef>)

_Touch targets are 48x48dp_

## Date entry methods

The date entry component offers two ways to enter a date:

-   Direct text entry into a text field
-   Through the date picker

The calendar icon is the exclusive entry point for the date picker.

This improves efficiency for a screen reader and other keyboard users, as it makes interaction with the date picker optional and reduces the amount of key presses required to input a date.

Each input is a separate tab stop, which improves discoverability of the control.

![Text input field next to a date picker icon provides a choice of how to enter the date.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwhl9jct-3.png?alt=media&token=ec79be27-46b7-4560-8c67-2ff253e3b94d>)

_Entering a date either through direct text entry or the date picker_

## Accessible date input

Automatically format the date after the user hits “Enter“ or navigates out of the text field. Don't automatically format the date by adding slashes or other special characters while the user is typing (also known as input masks). This can cause confusion for people using screen readers because it changes what they typed.  

To reduce errors, accept a range of formats including dashes, spaces, slashes, dots, and 0 to the left of a single digit month/day. This is especially helpful for assistive technology users who might be more prone to errors when interacting with complex inputs.

![Numeric entry 08172323 automatically formatted to 08/17/2023.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmd5tjcxl-04.png?alt=media&token=5fa49d5e-5f45-4258-b90e-390d8e9aa4ed>)

_The text field's logic can adapt to the user's actual input format, applying the correct formatting after the user has completed their text entry_

## Optional **Clear** button

If it's not needed for your use case, remove the **Clear** button from the screen to reduce the number of tab stops for keyboard users.

![Optional clear button on lower left corner of a date picker.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmd5tl1kf-05.png?alt=media&token=2c6fd134-3e07-439b-a93f-1ae9cec2a1bf>)

_Remove non-critical actions to reduce the number of tab stops for keyboard users_

## Affordance for keyboard shortcuts

Ensure keyboard shortcuts are readily available for keyboard and screen reader users by providing the shortcut key in the tooltip. It should be included in the hint description to be read out by the screen reader.

As shown here, the previous year button is interactive and can therefore be focused via the keyboard. Upon focus, the tooltip explains the behavior of the button and shows the shortcut key.

![Shift + Page up is the keyboard shortcut to go to the previous year on a date picker.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmd5tmhkf-06.png?alt=media&token=78a780bd-c455-4e42-a20d-fcb0ef5b1e3f>)

_Keyboard tooltip example for date picker_

## Truncated labels & tooltips

Truncating labels isn't ideal, but tooltips allow the full text to be shown on hover or keyboard focus.

Days of the week are not interactive and are therefore not focusable via keyboard, yet the tooltip is available on hover. The date picker relies on the conventionality of these abbreviations for some assistive technology users.

![A pointer hover over the “T” day on a date picker produces the tooltip “Tuesday.”](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmd5tnt2t-07.png?alt=media&token=af48202f-5abc-49e2-8988-43052d0c84e0>)

_Days of the week are not navigable via keyboard, so the tooltip is shown only on pointer hover_

## Color contrast between dates

Dates should have contrast of at least 4.5:1 between the link text colors and the background.

![A date picker’s label text passes the color contrast minimum of 4.5:1.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmd5tp1fs-08.png?alt=media&token=f5cf6a07-9a3b-400a-9cc6-7183fdc69832>)

_Dates pass the 4.5:1 contrast minimum_

## Keyboard navigation

| **Keys** | **Actions** |
| --- | --- |
| Enter/return | Enter/return |
| Enter/return | Closes the calendar and saves the selected date |
| Page up/down | Move to the same date on next/previous month |
| Home/End | Move to the first day of the month |
| Shift + Page up/down | Moves to the same date in the next/previous year |
| Shift + M | Moves to the month list dropdown |
| Shift + Y | Moves to the year list dropdown |

## Labeling elements

The text field's accessibility label should clearly state the purpose of the input (for example, event date or reservation date) and should match the placeholder text when the field is empty.

The helper text (below the text field) should specify the date format (for example, MM/DD/YYYY or YYYY/MM/DD) and act as a description for the text field. The default helper text is "MM/DD/YYYY," but this can be customized.

![Text field accessibility labels.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmd5tqs6i-09.png?alt=media&token=b7d6086d-5892-421e-96ed-a8da8de62ec7>)

_The accessibility label clearly states the kind of input as an event date_

| **Element** | **A11y label** | **Role** |
| --- | --- | --- |
| Previous / next month and year | “{label}” | Button |
| Month and year dropdowns | “{label}” | Button |
| Days of the week | Column header |  |
| Month grid | Grid |  |

## Screen reader verbalizations

To support screen reader users, labels are used to enumerate the complete date. This allows screen reader users to hear the full context of "Monday, August 17” instead of just part of the date.

![Current date label providing day, month, and year for screen reader accessibility.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmd5tw17z-10.png?alt=media&token=43c1af13-8f5b-439f-b238-443d703699d5>)

_Screen readers will state the full day, month, date, and year instead of just the number 17_

---
title: "Switch"
source_url: "https://m3.material.io/components/switch/guidelines"
lastmod: "2026-01-06"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "components"
assets:
  - "assets/images/4aefc7eba60c-mg7sxhp8-01.png"
  - "assets/videos/06f256abdd2d-lwa9phy3-GM3-Components-Switch-1-v01.mp4"
  - "assets/images/71920a172821-mg7txet6-03.png"
  - "assets/images/a42a4184c323-mg7uio91-05-do.png"
  - "assets/images/a8aacfaeec0a-mg7ukia8-06-dont.png"
  - "assets/images/6bf26a16ee78-mg7ul4t0-07.png"
  - "assets/images/b46e98b7bca0-mg7ulhxp-08.png"
  - "assets/images/88ff3fe42453-mg7ustxv-09_Do.png"
  - "assets/images/f59618e66a1e-mg7ut60g-10_Don't.png"
  - "assets/images/93eab5c03ca8-mg7uvzmf-11_do.png"
  - "assets/images/42bec459bdd4-mg7vc3q1-12_don't.png"
  - "assets/images/12315816fa63-mg7vge0i-13.png"
  - "assets/videos/fadf6d96cdff-lwabkmev-GM3-Components-Switch-2-v01.mp4"
  - "assets/images/06fc31d0595f-mg7vjfr0-15_Do.png"
  - "assets/images/e178b0f06039-mg7vpyog-16_Don't.png"
  - "assets/images/6210ccb8918b-mg7vyj3y-17_do.png"
  - "assets/images/d2c776ded9a3-mg7vz4xm-18_don't.png"
  - "assets/images/210aa0ae31fb-mg7xvplw-19_placement.png"
  - "assets/videos/1fae96f42f6a-lwabqhfg-GM3-Components-Switch-3-v01.mp4"
  - "assets/videos/06f0465e3262-lwabqzdx-GM3-Components-Switch-4-v01.mp4"
---

# Switch

Switches toggle the selection of an item on or off

## Guidelines

![A switch in 2 states, off and on.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmg7sxhp8-01.png?alt=media&token=9cf20c7f-1383-4349-a75b-f6242b3a8aaf>)

_Switches change settings and other options immediately_

## Usage

Switches are best used to adjust settings and other standalone options. 

They make a binary selection:

-   On and off

-   True and false


The effects of a switch should start immediately, without needing to save.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwa9phy3-GM3-Components-Switch-1-v01.mp4?alt=media&amp;token=87034bac-0c9d-4b3f-91e4-29c02c97964d" title="Toggling a switch turns on dark mode in Settings on a mobile device."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwa9phy3-GM3-Components-Switch-1-v01.mp4?alt=media&token=87034bac-0c9d-4b3f-91e4-29c02c97964d>)

Use switches to:

-   Toggle a single item on or off
-   Immediately activate or deactivate something

![Switch used in notification settings to turn on and off the "play over notifications" function.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmg7txet6-03.png?alt=media&token=9dde367e-511f-44db-a9cd-6f3ea4c2630c>)

_Switches are commonly used on mobile to turn settings on or off_

Switches control binary options, not opposing ones. A binary option represents a single selection that's either on or off.

Opposing options are when only one option in a set can be selected at a time, like a list or map view. Use a connected button group instead.

![A connected button group with options of List View and Map View.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmg7uio91-05%20-%20do.png?alt=media&token=b2b0ba0d-c3d0-4239-8b0f-d96c53e9457c>)

_Use a connected button group to choose between opposing options_

![A switch with non-binary options of List View and Map View.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmg7ukia8-06%20-%20dont.png?alt=media&token=79ae5e92-2043-4265-b926-1965e7805d0b>)

_Avoid using switches to toggle between opposing options_

### Alternate selection controls

Checkboxes, radio buttons, and switches are the three main kinds of selection controls. They help people make choices, like selecting options or turning settings on and off.

Use checkboxes to select multiple related options in a list.

Use radio buttons to select a single option in a list.

Use switches to select standalone or more verbose options in a list, like settings.

![2 checkboxes, 1 unchecked and 1 checked.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmg7ul4t0-07.png?alt=media&token=95b2a20a-6385-4636-8418-d6322ab44f62>)

_Checkboxes_

![2 radio buttons, 1 in an enabled state, 1 in a disabled state.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmg7ulhxp-08.png?alt=media&token=3f1f093d-f109-41cc-94b9-8275abe131d8>)

_Radio buttons_

![Mobile screen with checkboxes to select list items and call to action button to update the list.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmg7ustxv-09_Do.png?alt=media&token=2f9d9c72-a27f-4bb4-a475-f1bdaa198acf>)

_Use checkboxes (not switches) to let people select one or more options from a list_

![Mobile screen with checkboxes to select list items and call to action switch to update the list.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmg7ut60g-10_Don't.png?alt=media&token=258cf8f8-e60a-4b0f-b0f0-9c08f5a70a8f>)

_A switch can't replace a button. People expect a call to action to be a button, not a switch._

![Radio buttons used to select a language for a mobile app.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmg7uvzmf-11_do.png?alt=media&token=07dcfa64-19b2-481b-b65d-4703baafa5e0>)

_Use radio buttons (not switches) when only one item can be selected from a list_

![Mobile screen with switches to select list items and call to action button to update the list.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmg7vc3q1-12_don't.png?alt=media&token=3176413a-9e5d-448f-b807-36ca1b72ac04>)

_Avoid using a switch to select multiple options that require people to save. Switches should be immediate. Use checkboxes instead._

## Anatomy

![3 elements of a switch. ](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmg7vge0i-13.png?alt=media&token=082b9467-6cbf-4fea-a904-be06d0b5b7ef>)

_Track; Handle; Icon (optional)_

### Icon (optional)

The switch handle can contain an optional icon.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwabkmev-GM3-Components-Switch-2-v01.mp4?alt=media&amp;token=c11da5d1-2565-4717-86b8-172964ae3708" title="A checkmark appears on the filled circle of a handle when the switch is toggled on."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwabkmev-GM3-Components-Switch-2-v01.mp4?alt=media&token=c11da5d1-2565-4717-86b8-172964ae3708>)

Icons can be used to visually emphasize the switch’s selection. The icon’s meaning should be clear and unambiguous to help the people understand whether switch is on or off.

![2 switches, the unselected state icon is an X, and the selected state is a checkmark.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmg7vjfr0-15_Do.png?alt=media&token=a17fe805-0167-4f6a-b155-f7c3f5aacb4b>)

_Use icons that clearly communicate whether the switch is on or off, such as an X and a checkmark_

![A switch’s unselected handle icon is a moon and the selected state icon is a pencil.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmg7vpyog-16_Don't.png?alt=media&token=70eb3f5d-9352-484d-8077-26b25f8c8339>)

_Avoid using more ambiguous or non-binary icons, such as a moon or edit icon_

### Label text

Switches should always be paired with an inline label describing what the switch controls when selected.

![Switch label text: 1. Permission manager, App has access to your data. 2. Camera access, app has access to your camera. 3. Show password.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmg7vyj3y-17_do.png?alt=media&token=dca3bc09-c973-4f83-8394-4012f61b958b>)

_Keep labels short and direct. A label should describe what the control does when the switch is on._

![On a mobile screen’s privacy settings, 2 switches have “On” label text and checkmark icons. The last switch has “Off” label text and no icon.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmg7vz4xm-18_don't.png?alt=media&token=e81167e0-82e8-487e-af0a-4b91d93360e9>)

_Don't add label text into the switch; the font size would be too small to be accessible. Use an appropriate icon instead._

## Placement

Switches are often arranged in stacked layouts.

![Screen showing labels and stacked switches in varying on/off modes.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmg7xvplw-19_placement.png?alt=media&token=73f5211c-64e5-4b82-b081-95ab81ae7b9d>)

_Settings screens are common places to use switches_

## Behavior

A switch is successfully toggled when the handle slides to the other side of the track after an interaction.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwabqhfg-GM3-Components-Switch-3-v01.mp4?alt=media&amp;token=b03a8301-b5e1-43b1-91d5-2fcb30f3ba71" title="A switch is toggled to turn on airplane mode and Wi-Fi switch automatically turns off."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwabqhfg-GM3-Components-Switch-3-v01.mp4?alt=media&token=b03a8301-b5e1-43b1-91d5-2fcb30f3ba71>)

When a person toggles a switch, its handle size changes and the corresponding action takes effect immediately.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwabqzdx-GM3-Components-Switch-4-v01.mp4?alt=media&amp;token=71a78019-9dac-4493-a0d1-ad40a4c505af" title="When selected the switch’s handle gets larger and a checkmark appears on the filled circle."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwabqzdx-GM3-Components-Switch-4-v01.mp4?alt=media&token=71a78019-9dac-4493-a0d1-ad40a4c505af>)

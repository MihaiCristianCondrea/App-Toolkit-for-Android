---
title: "Checkbox"
source_url: "https://m3.material.io/components/checkbox/guidelines"
lastmod: "2026-05-18"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "components"
assets:
  - "assets/images/1019b10763b6-mg0yt1n6-1.png"
  - "assets/images/6277a0e09161-mg0vnl39-2.png"
  - "assets/images/12583b42ad5c-mg0vok05-3_do.png"
  - "assets/images/ec9ee588ff43-mg0vwf9n-4_dont.png"
  - "assets/images/288f47105916-mcypcjyf-5.png"
  - "assets/images/d4c733cb2859-mg0vwj88-6.png"
  - "assets/images/8de2720d398b-mcypfx6t-7.png"
  - "assets/images/da5ec4e4893a-mg0vp83b-8.png"
  - "assets/videos/8b31ba9c4ecc-lwj3f7ot-GM3-Components-Checkbox-Guidelines-1-v01.mp4"
  - "assets/videos/bc5356abe343-lwj3h4ri-GM3-Components-Checkbox-Guidelines-2-v01.mp4"
  - "assets/videos/2ebf22e23559-lwj3iba1-GM3-Components-Checkbox-Guidelines-3-v01.mp4"
---

# Checkbox

Checkboxes let users select one or more items from a list, or turn an item on or off

## Guidelines

![A list of burger additions represented with checkboxes.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmg0yt1n6-1.png?alt=media&token=e687e377-f0e9-4a8c-8323-97b6f3b03b2d>)

_Checkboxes in a list of items_

## Usage

Use checkboxes to: 

-   Select one or more options from a list
-   Present a list containing sub-selections
-   Turn an item on or off in a desktop environment
-   Visually group similar options together

![List of 80's songs indicating choice through checkbox selection.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmg0vnl39-2.png?alt=media&token=a6b2f259-cb48-4e43-9699-e20e94c0d737>)

_Checkboxes select multiple, related options_

Checkboxes should be used instead of switches if multiple, related options can be selected from a list. Checkboxes visually group similar items effectively and take up less space than switches.

![List indicating choice with checkbox selection.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmg0vok05-3_do.png?alt=media&token=6dd5d04d-babd-45cf-b7f8-a5e95ab275ff>)

_Checkboxes let users select one or more options from a list. A parent checkbox allows for easy selection or deselection of all items._

![A list with multiple switches selected.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmg0vwf9n-4_dont.png?alt=media&token=049701a4-cc33-442f-8e4e-db9c79aa40fa>)

_If a list consists of multiple options, don't use switches. Instead, use checkboxes. Checkboxes imply the items are related, and take up less visual space._

### Alternate selection controls

Checkboxes, radio buttons, and switches are the three main selection controls. They all help people make choices, like selecting options or switching settings on or off.

-   Use checkboxes to select multiple related options in a list.

-   Use radio buttons to select a single option in a list.

-   Use switches to select standalone or more verbose options in a list, like settings.

![Diagram of 2 radio buttons, one selected and one unselected.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmcypcjyf-5.png?alt=media&token=e0bb850c-365e-424b-aaa7-703285e8a6ea>)

_Radio buttons_

![Diagram of 2 switches, one selected and one unselected.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmg0vwj88-6.png?alt=media&token=9323d5db-9e97-4df0-a4b0-2cf2136cf1d2>)

_Switches_

## Anatomy

![Diagram of checkbox indicating the 2 parts of its anatomy.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmcypfx6t-7.png?alt=media&token=5f2b1048-7e0f-45f5-8f9b-3f09fab4e910>)

_1. Container 2. Icon_

## Responsive layout

In expanded window sizes, placing checkboxes within a contained region such as a side sheet can help group related controls and available actions.

![Desktop screen showing music albums and a side sheet containing checkboxes for filtering music genres.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmg0vp83b-8.png?alt=media&token=ccabbf13-9b61-4bec-938e-708c502111b0>)

_A side sheet can group related controls on larger screens_

## Behavior

Multiple checkboxes in a list can be selected.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwj3f7ot-GM3-Components-Checkbox-Guidelines-1-v01.mp4?alt=media&amp;token=a07308bc-26e6-4d34-8765-cd9ad9e28b82" title="Using checkboxes to select a list of extra ingredients, like pickles and tomatoes, to add to a meal."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwj3f7ot-GM3-Components-Checkbox-Guidelines-1-v01.mp4?alt=media&token=a07308bc-26e6-4d34-8765-cd9ad9e28b82>)

Checkboxes can have a parent-child relationship with other checkboxes.

-   When the parent checkbox is checked, all child checkboxes are checked
-   If a parent checkbox is unchecked, all child checkboxes are unchecked
-   If some, but not all, child checkboxes are checked, the parent checkbox becomes an indeterminate checkbox. Checking an indeterminate checkbox checks all child items.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwj3h4ri-GM3-Components-Checkbox-Guidelines-2-v01.mp4?alt=media&amp;token=6272d373-67f5-4c84-83e3-b24489905343" title="Checking parent checkbox also checks child items. Unchecking one child item makes parent indeterminate."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwj3h4ri-GM3-Components-Checkbox-Guidelines-2-v01.mp4?alt=media&token=6272d373-67f5-4c84-83e3-b24489905343>)

When selected, a checkbox clearly and instantly communicates its selected state.

If used to turn something on or off, the action should be immediately executed.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwj3iba1-GM3-Components-Checkbox-Guidelines-3-v01.mp4?alt=media&amp;token=a72e8a98-afda-496e-b1ea-50647e6a9bca" title="Selecting a checkbox for turning on dark mode immediately changes the phone theme."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwj3iba1-GM3-Components-Checkbox-Guidelines-3-v01.mp4?alt=media&token=a72e8a98-afda-496e-b1ea-50647e6a9bca>)

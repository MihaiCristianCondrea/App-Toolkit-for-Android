---
title: "Split buttons"
source_url: "https://m3.material.io/components/split-button/accessibility"
lastmod: "2026-02-05"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "components"
assets:
  - "assets/images/da661acec8ef-m0dpv69h-1.png"
  - "assets/images/a1eaffb236db-m0dpwfdp-2.png"
  - "assets/images/ddf71ab8d06c-m0dq2sfs-3.png"
  - "assets/images/1da3a7a0d3b0-m0dq40tg-4.png"
---

# Split buttons

Split buttons open a menu to give people more options related to an action

## Accessibility

## Use cases

People should be able to do the following using assistive technology:

-   Navigate to each button and interact with them
-   Navigate to any element opened by the trailing button
-   Understand the current selection state of the button

## Interaction & style

Each button in the split button needs a minimum target area of 48x48dp. Extra small and small split buttons are shorter than 48dp, so the target areas around them need to be at least 48dp tall.

![Diagram showing extra small and small split buttons with visible 48x48dp target areas.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0dpv69h-1.png?alt=media&token=649cab35-4ea3-432c-b027-1dd04227d045>)

_Target areas should be at least 48x48dp / Extra small; Small_

## Initial focus

Focus should land on the leading button then move to the trailing button. This can depend on the operating system’s settings.

![Focus on the leading button and trailing button for both LTR and RTL languages.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0dpwfdp-2.png?alt=media&token=8f646a31-a6f5-4330-b17d-1d8c11d2132f>)

_Left to right; Right to left_

## Keyboard navigation

| Keys | Actions |
| --- | --- |
| Tab | Navigate between buttons |
| Space or enter | Activate focused button |

## Labeling elements

The accessibility label for the leading button is the same as buttons.

![“Watch later” is both the button label text and the accessibility label.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0dq2sfs-3.png?alt=media&token=85b75ca9-ea9b-4199-939d-9b42767d223b>)

_Leading buttons should have the same labels as common buttons_

The trailing icon button should have an extra state or similar label indicating that the menu is expanded or collapsed.

Label the button to clearly indicate that there are more options. The label of the secondary button should indicate that it provides additional choices related to the action of the main button. For instance, if the main button says "Watch later," the secondary button should be something like "More watch options."

Label the opened menu according to the [menu accessibility guidance](https://m3.material.io/m3/pages/menus/accessibility/).

![Collapsed state indicated for the trailing button.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0dq40tg-4.png?alt=media&token=e787ddba-953d-423a-bdee-55583e4500a7>)

_Trailing buttons should communicate the state of the menu and that more options are available_

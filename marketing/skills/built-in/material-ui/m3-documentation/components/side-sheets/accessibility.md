---
title: "Side sheets"
source_url: "https://m3.material.io/components/side-sheets/accessibility"
lastmod: "2026-05-18"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "components"
assets:
  - "assets/images/1cf90e501198-mgamgh0c-01-do.png"
  - "assets/images/44a9b13ebe4c-mgamhoet-02-don't.png"
  - "assets/images/7c917ca24323-mgamkh0k-03.png"
  - "assets/images/b16ebbd30e97-mgamoe2t-04.png"
---

# Side sheets

Side sheets show secondary content anchored to the side of the screen

## Accessibility

## Use cases

People should be able to dismiss the side sheet using assistive technology.

## Interaction & style

Material requires that a close affordance, such as a close icon button, is always present within a side sheet.

![Side sheet correctly designed with close icon in upper right corner.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmgamgh0c-01-do.png?alt=media&token=726a6d30-dab6-4f51-ad2e-015ac54dd3c3>)

_A close icon button makes the side sheet easy to dismiss_

![Side sheet incorrectly designed with no close icon button.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmgamhoet-02-don't.png?alt=media&token=39e0958b-98a1-439f-b7ba-a8a199550336>)

_Without a close icon button, people can’t predict the opening and closing flow of side sheets, or know if the sheet is transient or permanent_

## Initial focus

Actions within a side sheet can be focused by tab order using a keyboard or switch control.

![Side sheet diagram showing the focus order of headline, close, save, cancel.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmgamkh0k-03.png?alt=media&token=0ecb7ab4-bff0-48b1-84a2-0c39cc936114>)

_Visible focus shown on the available actions within a side sheet: Headline; Close; Cancel; Save; /_

## Keyboard navigation

| Keys | Actions |
| --- | --- |
| **Tab** | Focus lands on (non-disabled) icon button |
| **Space** or **Enter** | Activates the (non-disabled) icon button |

## Labeling

The accessibility role for a side sheet is **Dialog**.

![Side sheet showing the accessibility role as dialog.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmgamoe2t-04.png?alt=media&token=f7ce54e1-ca6e-486f-9776-2057064b9beb>)

_The role for side sheets is Dialog_

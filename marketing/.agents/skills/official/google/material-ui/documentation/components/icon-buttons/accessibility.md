---
title: "Icon buttons"
source_url: "https://m3.material.io/components/icon-buttons/accessibility"
lastmod: "2026-02-11"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "components"
assets:
  - "assets/images/5839acbccc38-mal4w7e9-1_Do.png"
  - "assets/images/2afd183d6459-mal4w3mg-2_Don't.png"
  - "assets/images/993afafc3799-mal4xuoj-3.png"
  - "assets/images/34d5520d50ad-m0c1h7ba-4.png"
  - "assets/images/506180e221e4-m0c1j2b9-5.png"
---

# Icon buttons

Icon buttons help people take minor actions with one tap

## Accessibility

## Use cases

People should be able to do the following using assistive technology:

-   Understand meaning of the icon
-   Navigate to and activate an icon button
-   When applicable, a tooltip should be available to help describe the icon button's purpose

## Interaction & style

Ensure the icon has contrast of at least 3:1 with the surface or background.

![Icon button with correct contrast ratio.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmal4w7e9-1_Do.png?alt=media&token=b59c5fba-011b-49af-8c0a-9f06a5d5f4b5>)

_Icon buttons should have a 3:1 contrast ratio with the surface or background_

![Icon button with insufficient contrast ratio.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmal4w3mg-2_Don't.png?alt=media&token=72bcee5d-ca2a-4f93-9c08-861003f0f4ff>)

_Avoid using colors with contrast below 3:1_

## Keyboard navigation

| **Keys** | **Actions** |
| --- | --- |
| Tab | Focus lands on (non-disabled) icon button |
| Space or Enter | Activates the (non-disabled) icon button |

## Labeling elements

The accessibility label for icon buttons describes the action the button is executing, such as **Add to favorites**, **Bookmark**, or **Send message**.

![Icon button label and role.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmal4xuoj-3.png?alt=media&token=f0908e1c-f8cf-467a-a4bc-359ab5bf80d0>)

_The icon button label describes the action, such as Add to favorites for the heart icon_

## Layout & density

Groups of similar components can be nested together inside a component, or they can stand alone.

The target size of each icon button should be at least 48dp, even when nested.

![Icon buttons with 48dp target sizes.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0c1h7ba-4.png?alt=media&token=79849c70-99e3-4433-8616-05d06808a57b>)

_Icon buttons can be used within other components, such as an app bar_

### Avoid applying density by default

Don't apply density to icon buttons by default. This lowers their targets below the required 48x48 CSS pixels minimum size. 

Provide density options that allow people to choose a higher density, such as selecting a denser layout or changing the theme. Controls for adjusting density must maintain a target size of at least 48x48 CSS pixels.

## Hover

On web, icon buttons should display a tooltip with an accessibility label.

![“Heart” icon with "Add to favorites" tooltip on hover.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0c1j2b9-5.png?alt=media&token=244efcd1-5b55-496d-a4bb-9bc97593fbd9>)

_The tooltip label text should be clear and concise_

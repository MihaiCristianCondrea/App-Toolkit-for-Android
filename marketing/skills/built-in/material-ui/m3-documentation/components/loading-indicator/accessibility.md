---
title: "Loading indicator"
source_url: "https://m3.material.io/components/loading-indicator/accessibility"
lastmod: "2026-02-05"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "components"
assets:
  - "assets/images/aa4cedab2b70-magv3srk-1.png"
  - "assets/images/877a99e6770b-maguuv6h-2_do.png"
  - "assets/images/d8923baca602-maguuziu-3_dont.png"
  - "assets/videos/1eca82867f73-mbf509tn-GM3_Expressive_Loading-Indicator_Accesibility_16_04_v01.mp4"
  - "assets/images/5c282179ce15-m0cb8rsa-5.png"
---

# Loading indicator

Loading indicators show the progress for a short wait time

## Accessibility

## Use cases

People should be able to do the following with assistive technology:

-   Navigate to the loading indicator
-   Understand what progress the indicator is communicating
-   Initiate a content refresh without relying on a gesture

## Interaction & style

The active indicator, which displays progress, provides visual contrast of at least 3:1 against most container and surface colors.

The indicator itself must have 3:1 contrast with the background, but the container does not.

![Loading indicator with 3:1 color contrast.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmagv3srk-1.png?alt=media&token=85fc9e3b-7664-442d-940c-8d7b51cabda4>)

_The loading indicator provides visual contrast of at least 3:1 against most background colors_

When integrated into another component, such as a button, make sure that the active indicator provides a visual contrast of at least 3:1 against the other component.

![Loading indicator with correct color contrast.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmaguuv6h-2_do.png?alt=media&token=8821caaf-6d1f-444d-8c96-12753282b334>)

_Ensure at least 3:1 contrast between the indicator and the surface it's on_

![Loading indicator with incorrect color contrast.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmaguuziu-3_dont.png?alt=media&token=e8157014-aaa8-47cf-ae70-c8c3b986b36e>)

_Avoid using when the contrast is under 3:1_

Pull-to-refresh interactions can’t be accessible by just swiping. Provide an alternate way to refresh the content with a single pointer, such as placing a refresh button in a menu or directly alongside the content.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmbf509tn-GM3_Expressive_Loading-Indicator_Accesibility_16_04_v01.mp4?alt=media&amp;token=2b731444-164e-4a5e-8dfe-b892b6b9dc6f" title="Refreshing content initiated by an app bar action."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmbf509tn-GM3_Expressive_Loading-Indicator_Accesibility_16_04_v01.mp4?alt=media&token=2b731444-164e-4a5e-8dfe-b892b6b9dc6f>)

## Labeling elements

Since the loading indicator is a visual cue, it needs an accessibility label to assist people who can't rely on visuals.

It should use the **progress bar** accessibility role. Write a label describing the purpose of the loading indicator, such as **loading news article** or **refreshing page**.

![Loading indicator accessibility label and role.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0cb8rsa-5.png?alt=media&token=a2cd5924-9d13-4657-a26f-b5c6130d0c67>)

_Loading indicator labels should explain which items are loading_

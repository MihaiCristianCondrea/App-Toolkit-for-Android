---
title: "Designing"
source_url: "https://m3.material.io/foundations/designing/color-contrast"
lastmod: "2025-10-08"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "foundations"
assets:
  - "assets/images/e2ee549640f9-m0quwwby-1.png"
  - "assets/images/ea61d075eb6b-m0qux0kv-2.png"
  - "assets/images/c4ee500e47ce-m0qux3pf-3.png"
  - "assets/images/c60a047e3afa-m0qux82l-4.png"
  - "assets/images/0ba33edba99b-lwj3vo5t-5.png"
  - "assets/images/074ce5c8932f-lwj3vxr1-6.png"
---

# Designing

Implement intuitive, accessible layouts, considering structure, color, and flow

## Color contrast

## Color & contrast

Color and contrast can be used to help users see and interpret your app’s content, interact with the right elements, and understand actions.

Color can help communicate mood, tone, and critical information. Primary, secondary, and accent colors can be selected to support usability. Sufficient color contrast between elements can help users with low vision see and use your app.

![two colors applied to button container and UI surface with contrast that passes 3:1 minimum](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0quwwby-1.png?alt=media&token=f5750025-ccb3-4916-8d4d-6136a6ced9e4>)

### Contrast ratios

Color contrast is important for users to distinguish various text and non-text elements. Higher contrast makes the imagery easier to see, while low-contrast images may be difficult for some users to differentiate in bright or low light conditions, such as on a very sunny day or at night.

Contrast ratios represent how different one color is from another color, commonly written as 1:1 or 21:1. The greater the difference is between the two numbers in the ratio, the greater the difference in relative luminance between the colors. The contrast ratio between a color and its background ranges from 1-21 based on its luminance (the intensity of light emitted) according to the World Wide Web Consortium (W3C).

**The W3C recommends the following contrasts for body text and image text**

| Text type | Color contrast ratio |
| --- | --- |
| Large text (at 14 pt bold/18 pt regular and up) and graphics | At least 3:1 against the background |
| Small text | At least 4.5:1 against the background |

Disabled states do not need to meet contrast requirements.

### Clustering elements

Some non-text elements, such as button containers, should meet a contrast ratio of 3:1 between their container color and the color of their background. Consider the following patterns for combining elements and tones, which are grounded in Material's research into contrast and functional changes when elements are combined.

[Learn more about color contrast for accessibility](../../styles/color/system/how-the-system-works.md#e1e92a3b-8702-46b6-8132-58321aa600bd)

Elements that are clustered with others, such as a group of buttons, require the user to distinguish each one from the group. 

These elements benefit from 3:1 contrast between themselves and the background.

![Two colors applied to button container and UI surface with contrast that fails 3:1 minimum.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0qux0kv-2.png?alt=media&token=6e7cd553-db88-44c9-8f4e-edf7e43c5328>)

_The contrast of the button container color against the background color is less than Material's required contrast of 3:1_

![Two colors applied to button container and UI surface with contrast that passes 3:1 minimum.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0qux3pf-3.png?alt=media&token=11307ebf-9f24-49b2-9667-b4db411ed819>)

_The container color exceeds Material's required minimum contrast of 3:1 against background color_

Elements that stand on their own and apart from other elements on the screen, such as a FAB, are already distinguishable to users because of their prominence. These elements don’t benefit from 3:1 contrast between themselves and the background.

![Two colors applied to the FAB container and UI surface that fail the minimum contrast of 3:1.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0qux82l-4.png?alt=media&token=582f5c7a-5e7f-4f79-9e8d-bc5f091be371>)

_Standalone components, such as FABs, don’t need to meet Material's minimum contrast of 3:1 between the container and background colors because of their prominence_

When placing components together in a cluster, use components or types of components that each achieve at least 3:1 contrast between themselves and the background.

![A cluster of three buttons with the same container color that fails the minimum contrast of 3:1 with the UI surface.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwj3vo5t-5.png?alt=media&token=6437d00b-724d-4823-9377-9421334c5d6e>)

_Each button's container color has less than Material's required minimum contrast of 3:1 against the UI background, leading to poor contrast support for users with low vision / /_

![A cluster of three buttons with the same container color that passes the minimum contrast of 3:1 with the UI surface.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwj3vxr1-6.png?alt=media&token=c6dff750-9b13-4117-bafb-59dfc3198736>)

_Each button's container color has contrast of at least 3:1 against the UI background, leading to better contrast support for users with low vision / /_

---
title: "Material Design Components for Android 1.11.0 and 1.12.0"
source_url: "https://m3.material.io/blog/android-stable-release-1-12-0"
lastmod: "2026-05-11"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "blog"
assets:
  - "assets/images/7241d20f31ce-fR3azAhK8xGmNL3cRg9f66GxSymGKh-WVmNQxzmoOvTa19vUrN2QNj8pjbw27466c4SIRGGZyTCYmTd8n7GnmbbJ75aw6n_z.png"
  - "assets/images/698e44ee10bb-4dfYjomm9jLyZrmrpQj6wbwMw5Zryb1iKscfrxOsijGLEkGJdIOqw2b_xFp-s9E3iwuwl9RY2CYQQSkwSgDb3eETUysLyFtE.png"
  - "assets/images/86a4f31874e3-Qzm10yEjrS44EMjoNNZq-RrcrICwVAqVCSI84yFnvaDOXwexiH2n-iUVXSBKcNGu2TVn0297L2m4NySjlFO1zUm0TMzKFyrO.png"
  - "assets/images/da4735566b7d-VyDL_gQtCboI-XpNHYJoVuaO4xQQNl0NraF8ar2aYy6CnN7u-vHj4beznfhRK0GrqOQTpjD8qKrEhcWWz1DqddJeqNj3XDnU.png"
  - "assets/images/4a13940618f0-wYC9nZ_piBDhi6_F2NsM0jZlb5od5BIUgMwDrrqs0Nu7Xw-SgmUF-acVqSv0YbuV6Xb1iLlT6ijcLPCyIrzENcSh0Ft3pmCO.png"
  - "assets/images/0a0aac98fd12-AE-jIS-uUK7UrS9_VI-2oofB11xaJINbpE9WfnpPFpZtMtkJHq5O_KvqEq8dtRuY7lKb1oUXQ3FV_6LCpIWpvl7CU3XHw3lm.png"
  - "assets/images/8dbe620c3ed1-zy97lo94vUh9MCZfE0XWNCxRSzUuWF5wZUNQ4eN6eQbwFjNLZkwwL4TV9WSonT4a2CVDdQC-svbuLd1P02NH_Zrc8497nFl4.png"
  - "assets/images/9c025cad9c81-ncTB3e-Q3A8plLzHS2sH4piRbW1w7OI8vLYZ9q34NESUKdFTm34QQ4odOVtVPLl6GqL6lasgNx-Avt6lzRub9yL2uWboV1X4.png"
  - "assets/images/d98094086fd1-Pq1pggutQySy_7ypKoBQBmPCHDj1-WJefUrXJXtaFp0QAGAx7VNUsjOK7litWYK7xHrOO9TINDMzoj-EDczkdA2hPDNiC8QC.png"
  - "assets/images/fad752b4d726-m6Pr8FsvUY5P2kRJjpDgVZFjILIAdAuTRVgfi6TcXrZ4KzBJWZy1SN14jI8TpGwtG3Xga2ub1ueG_L0hmlVY3MTZ4g6Lvuu3.png"
  - "assets/images/1d86f9b9da61-nHgpH1gWgauA9PxCVnLk0DNPIUlH1oXZM0m_vQFa_E7T7u2iwomZvilM4i7jrZsW40D-meLWsTtabwD5FlR34t1VdmzPDivg.png"
  - "assets/images/6e8999402baf-KN49PRsabGnC4SgVdwwtma86NiP2KZa8Hj3HPc-GgC2b_TBTH89TVtj8SaMOCv4kvPBT0fosafHdPcAJAl7ZZQn1XtpMOKjq.png"
  - "assets/images/4ddf832d976b-eDcSrLuTFtSdUxxzohbn16CKGeRF-htwKt_wS2KDld8nBL-C47GC1IobF6YCDcPzsxF-KlovQy0fg4DFPbz_LaIk3rb1Pip_.png"
  - "assets/images/d44b90990731-zTR9b-1YWFR6bRePHuGgBfpuzK0Bek93AKrWZsdHX6AeRE6S02U9V71WAap31gAvIxiUDt-Gxsgs3GGdRONr1vMWI5pOaOm9.png"
---

# Material Design Components for Android 1.11.0 and 1.12.0

New color options and carousel refinements

![carousel component](<https://lh3.googleusercontent.com/fR3azAhK8xGmNL3cRg9f66GxSymGKh-WVmNQxzmoOvTa19vUrN2QNj8pjbw27466c4SIRGGZyTCYmTd8n7GnmbbJ75aw6n_zC3cFfR2dvh4pvt8RVA>)

Published: 2024-05-08T13:00:00

Material Design Components versions 1.11 and 1.12 come on the eve of our yearly developer conference Google I/O and offer a number of new options for Carousel control, an expansion to color theming, and non-text contrast updates for progress indicators and sliders. The minimum SDK version for MDC Views raises to API 19 in version 1.12, similar to the rest of AndroidX. 1.12 also updates its dependency on androidx.transition (used in SharedElement transitions, Fade, and [Predictive Back animations](https://developer.android.com/guide/navigation/custom-back/support-animations)) to version 1.5.0 from 1.2.0.

## Carousel Updates

In the [last post](https://material.io/blog/android-stable-release-1-10-0) we announced a new Carousel strategy, HeroCarouselStrategy, that can be used to showcase large, more immersive content. In MDC 1.11, we’ve added two new strategies - UncontainedCarouselStrategy and FullscreenCarouselStrategy. Additionally, we’ve added the option to center-align any carousel so large, focal items can be placed in the center of the scroll container.

![carousel component](<https://lh3.googleusercontent.com/4dfYjomm9jLyZrmrpQj6wbwMw5Zryb1iKscfrxOsijGLEkGJdIOqw2b_xFp-s9E3iwuwl9RY2CYQQSkwSgDb3eETUysLyFtEi6YYjCc4TIaLlaCPppY>)

Start-aligned HeroCarouselStrategy

![Carousel component](<https://lh3.googleusercontent.com/Qzm10yEjrS44EMjoNNZq-RrcrICwVAqVCSI84yFnvaDOXwexiH2n-iUVXSBKcNGu2TVn0297L2m4NySjlFO1zUm0TMzKFyrO-BsnVnlsb7PHM7wVq7s>)

Center-aligned HeroCarouselStrategy

`UncontainedCarouselStrategy` doesn’t alter the aspect ratio of the carousel items. This is a useful strategy when altering an item’s size could detract from its meaning - for example, a carousel of album covers might benefit from retaining a recognizable 1:1 aspect ratio.

![uncontained carousel](<https://lh3.googleusercontent.com/VyDL_gQtCboI-XpNHYJoVuaO4xQQNl0NraF8ar2aYy6CnN7u-vHj4beznfhRK0GrqOQTpjD8qKrEhcWWz1DqddJeqNj3XDnU06NJ1q3J1GJOXrw_B6Xg>)

`FullScreenCarouselStrategy` allows a single item to take all the available space in a container. This is useful for fully immersive content like video or slideshows where items are interacted with, or viewed, one-at-a-time. We only recommend using this strategy in the vertical orientation.

**Item Snapping**

`CarouselSnapHelper` allows your carousels to give focus to the nearest item. They can be used on all strategies but are strongly encouraged on hero carousels and full-screen carousels.

## Color Changes

API 34 has an [expanded color set](https://github.com/material-components/material-components-android/blob/master/docs/theming/Color.md) giving you more ways to personalize your apps. Some of the additions like the new surface options have been incorporated in components. Fixed and Dim colors on the other hand are developer options but not used by the component set at time of writing.

![color diagram](<https://lh3.googleusercontent.com/wYC9nZ_piBDhi6_F2NsM0jZlb5od5BIUgMwDrrqs0Nu7Xw-SgmUF-acVqSv0YbuV6Xb1iLlT6ijcLPCyIrzENcSh0Ft3pmCOYoASvu-qLnZ3dsWfgg>)

**System UI access to named color roles**

Before API 34, when you used dynamic color, the color was stored as individual tones of the color palettes. MDC would map those colors to [dynamic color properties and then assign those properties to color roles](https://github.com/material-components/material-components-android/blob/master/lib/java/com/google/android/material/color/res/values-v31/tokens.xml).

As of API 34, the traversal is more direct and compact. Material color roles directly map to color resources from [`@android:color` namespace](https://github.com/material-components/material-components-android/blob/master/lib/java/com/google/android/material/color/res/values-v34/tokens.xml).

**Fixed and Fixed Dim**

Fixed and Fixed Dim color roles are new additions to the color spec. Fixed color roles do not change between light and dark themes. Dim color roles offer a less emphasized option to the associated color role. Neither of these categories of color roles are used in components but are available to further customize your UIs.

![section of color scheme](<https://lh3.googleusercontent.com/AE-jIS-uUK7UrS9_VI-2oofB11xaJINbpE9WfnpPFpZtMtkJHq5O_KvqEq8dtRuY7lKb1oUXQ3FV_6LCpIWpvl7CU3XHw3lmB6PtSNT-V14YWY2JLQ>)

**Surfaces**

Previously in Material themes, we provided a single surface color value and calculated interpolated variants of it at runtime by blending with the primary color (based on elevation). This was frustrating for developers and designers when it came to implementing UIs because they were unable to assign those colors directly.

Color roles simulating different elevation levels have been added to theming. There are now three surface color options and five surface container options.

![surface color scheme](<https://lh3.googleusercontent.com/zy97lo94vUh9MCZfE0XWNCxRSzUuWF5wZUNQ4eN6eQbwFjNLZkwwL4TV9WSonT4a2CVDdQC-svbuLd1P02NH_Zrc8497nFl4zqkALxKvKMnq2cHAJXXU>)

Surface Bright is guaranteed to be the lightest surface color.

Surface is largely unchanged from previous versions.

Surface Dim is equivalent or darker than its relative Surface color role.

Surface Container Lowest, Low, High and Highest are additional color roles that can provide more or less emphasis in contrast to Surface Container.

These components use some of the new surface container color roles:

[Elevated Button](../components/buttons/specs.md)

[Bottom App Bar](https://m3.material.io/components/bottom-app-bar/specs)

[Navigation Bar](../components/navigation-bar/specs.md)

[Bottom Sheet](../components/bottom-sheets/specs.md)

[Modal Side Sheet](../components/side-sheets/specs.md)

[Elevated Card](../components/cards/specs.md)

[Elevated Chips](../components/chips/specs.md)

[Datepicker](../components/date-pickers/specs.md)

[Dialogs](../components/dialogs/specs.md)

[Surface FAB](../components/floating-action-button/specs.md)/ [Surface exFAB](../components/extended-fab/specs.md)

[Menu](../components/menus/specs.md)

[Navigation Drawer](../components/navigation-drawer/specs.md)

[Search](../components/search/specs.md)

[Switch](../components/switch/specs.md)

[Text Fields](../components/text-fields/specs.md)

[Timepicker](../components/time-pickers/specs.md)

[Top App Bar](https://m3.material.io/components/top-app-bar/specs)

The new color roles are already available in [Material Theme Builder](https://material-foundation.github.io/material-theme-builder/).

**Non-Text Contrast Updates (NTC) for Progress Indicators and Sliders**

Non-text contrast standards involve first evaluating which parts of controls are essential to demark their state, interactivity options, and legibility and secondly making changes to improve the identified attributes and making sure those portions have a minimum contrast ratio (3:1 in standard contrast at time of writing). Progress Indicators and Sliders have been updated to implement these standards.

**Progress indicators** 

The NTC changes to progress indicators include a clearly identified gap between the active and inactive tracks for linear progress indicators. In circular progress indicators, the pre-1.12 version does not by default include a track making it more difficult to estimate its value. An inactive track is now drawn to complete the full circle with gaps on both ends where the track meets the active indicator.

A number of these options such as the size of the gap size and track attributes are addressable in XML code. You can see the full listing [here](https://github.com/material-components/material-components-android/blob/master/docs/components/ProgressIndicator.md).

![Progress indicator settings in the catalog app](<https://lh3.googleusercontent.com/ncTB3e-Q3A8plLzHS2sH4piRbW1w7OI8vLYZ9q34NESUKdFTm34QQ4odOVtVPLl6GqL6lasgNx-Avt6lzRub9yL2uWboV1X4RYn8iqNEfwevhzTUCpw>)

MDC 1.11 version of Progress Indicators

![Progress indicator settings in the catalog app](<https://lh3.googleusercontent.com/Pq1pggutQySy_7ypKoBQBmPCHDj1-WJefUrXJXtaFp0QAGAx7VNUsjOK7litWYK7xHrOO9TINDMzoj-EDczkdA2hPDNiC8QC-sNJa9eVn1x7_5s5ulmT>)

Basic Progress Indicator Options

![Progress indicator settings in the catalog app](<https://lh3.googleusercontent.com/m6Pr8FsvUY5P2kRJjpDgVZFjILIAdAuTRVgfi6TcXrZ4KzBJWZy1SN14jI8TpGwtG3Xga2ub1ueG_L0hmlVY3MTZ4g6Lvuu3ze8GRgbDhlL95-tFwkQ>)

Advanced Progress Indicator Options

![customized progress indicators in catalog app](<https://lh3.googleusercontent.com/nHgpH1gWgauA9PxCVnLk0DNPIUlH1oXZM0m_vQFa_E7T7u2iwomZvilM4i7jrZsW40D-meLWsTtabwD5FlR34t1VdmzPDivgi1crm8RLLGccpEtVlckq>)

**Sliders**

Sliders have undergone similar changes to progress indicators. There are now gaps between the active and inactive tracks.The default thumb for sliders has also changed from a connected filled circle to a vertical bar with configurable gaps on each side. The shape for formatted labels have also undergone a few tweaks to assist with NTC.

As is the case with progress indicators, you can configure these attributes inXML code. You can see the full listing [here](https://github.com/material-components/material-components-android/blob/master/docs/components/Slider.md).

![sliders in catalog app](<https://lh3.googleusercontent.com/KN49PRsabGnC4SgVdwwtma86NiP2KZa8Hj3HPc-GgC2b_TBTH89TVtj8SaMOCv4kvPBT0fosafHdPcAJAl7ZZQn1XtpMOKjqUc2OBz_XPRsEtfpXqwM>)

MDC 1.11 version of Sliders

![sliders in catalog app](<https://lh3.googleusercontent.com/eDcSrLuTFtSdUxxzohbn16CKGeRF-htwKt_wS2KDld8nBL-C47GC1IobF6YCDcPzsxF-KlovQy0fg4DFPbz_LaIk3rb1Pip_cYC0a3gNOymmg_rdwyZe>)

Basic Slider Options

![sliders in catalog app](<https://lh3.googleusercontent.com/zTR9b-1YWFR6bRePHuGgBfpuzK0Bek93AKrWZsdHX6AeRE6S02U9V71WAap31gAvIxiUDt-Gxsgs3GGdRONr1vMWI5pOaOm9fJRKjKAP3i70HAl-weYZ>)

Advanced Slider Options

## What’s next for MDC?

We’re hard at work on the 1.13.0 release. The components or features we highlight in these posts are only a fraction of the work that lands in each release. Check out the [release notes](https://github.com/material-components/material-components-android/releases/tag/1.12.0) for a full listing.

You can follow the progress of new versions, file [bug reports](https://github.com/material-components/material-components-android/issues/new?assignees=&labels=bug&template=bug_report.md&title=%5BComponent+name%5D+Short+description+of+issue) and submit [feature requests](https://github.com/material-components/material-components-android/issues/new?assignees=&labels=feature+request&template=feature_request.md&title=%5BComponent+name%5D+Short+description+of+request) on GitHub. Check out the [catalog app](https://github.com/material-components/material-components-android/releases/download/1.12.0/catalog-debug.apk) to see the components in action. Also feel free to reach out to us on Twitter [@materialdesign](https://twitter.com/materialdesign).

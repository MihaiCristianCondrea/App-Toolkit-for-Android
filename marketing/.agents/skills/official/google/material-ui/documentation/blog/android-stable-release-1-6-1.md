---
title: "Material Design Components for Android 1.6.1"
source_url: "https://m3.material.io/blog/android-stable-release-1-6-1"
lastmod: "2024-12-26"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "blog"
assets:
  - "assets/images/9f15cebd8981-C-K_3-tg7BllaegZIAESRLUWBTFk81T1ErVoVDVX7IP_c_BJxPFdUu8tz-SKCwBVySdNWVqhWUPxY1tSZd7mQRS_SOIZRigC.png"
  - "assets/images/1af656a9ce82-vGK0OPM_NKfC3c5wny9p5kkdUYysXlK8uWsXPpmcXZz0i6Ntq0H4Lnsc5Po6RhkMsTR4QjQQ0UljDNa5WWdeinnSUeuWlWkf.png"
  - "assets/images/e7ca55483e15-2njPukopZEBNPC7t3JgT26y4wJXjIWVqTXpbXeGNfNG6pZZ-ESwkL3Fn6_jGHkqazhqH9oLRgm6r15c0hO007-yo1Yj1O6in.png"
  - "assets/images/76e39d12a08d-EC9mZRSbwP_aAQTHL4z8Rn5Gaw7THGeSKGDcIkzPAXrPJLMWdwbfQ79Xc8xtpoY3xmWr7XKRWl5jZb_y93Mb4AMFIzuwaCdd.png"
  - "assets/images/144c973f6218-bCav1hV4fr9zldiZZJMkw_-zEp7JDwMPN8sHD2-Ib0tGlIDtPYu7bNDb6yKf9IvjEJUCUjcCpC59AKcqZMd1wpaPJreB2_l6.png"
  - "assets/images/942b1ab9d284-xgmGm0y5iMAixInKsXrdFo15UcR9ThHeM81xlRDVpXTYU3QDLJ6yyOfg3V2Y_icSD0tCQDjXhjrsSPI6KFhlbaAM5MICE0cZ.png"
---

# Material Design Components for Android 1.6.1

With color harmony and Material Design 3 refinements

![Abstract illustration](<https://lh3.googleusercontent.com/C-K_3-tg7BllaegZIAESRLUWBTFk81T1ErVoVDVX7IP_c_BJxPFdUu8tz-SKCwBVySdNWVqhWUPxY1tSZd7mQRS_SOIZRigCnyjvYRktzqdEPJRizQ>)

Published: 2022-07-07T12:59:00

After cooling down from Google I/O, we have a pair of Material Design Components(MDC) releases to share with you: [1.6.0](https://github.com/material-components/material-components-android/releases/tag/1.6.0) and [1.6.1](https://github.com/material-components/material-components-android/releases/tag/1.6.1). As was the case with MDC 1.5.0, color is again a major theme with new APIs targeting [dynamic color](https://m3.material.io/styles/color/dynamic-color/overview) and [color harmony](https://material.io/blog/dynamic-color-harmony).

Other notable highlights include Material3 style updates of various components and various bug fixes, like resolving issues on Android Oreo(8) and Pie(9), underscoring our commitment to more than just the most recent release.

Alongside the documentation we host on [Github](https://github.com/material-components/material-components-android/tree/master/docs), the [Material Design 3 site](../index.md) is an additional source for the styling guidance and availability of components. For components available on MDC Views, the implementation page will contain code snippets showing you how to implement that component and common use cases. Below is some of the code you can find on the [implementation page for Chips](https://m3.material.io/components/chips/implementation).

![Screenshot from material.io](<https://lh3.googleusercontent.com/vGK0OPM_NKfC3c5wny9p5kkdUYysXlK8uWsXPpmcXZz0i6Ntq0H4Lnsc5Po6RhkMsTR4QjQQ0UljDNa5WWdeinnSUeuWlWkfmrsYm1GyYbo4D4sJBls>)

![Screenshot from material.io](<https://lh3.googleusercontent.com/2njPukopZEBNPC7t3JgT26y4wJXjIWVqTXpbXeGNfNG6pZZ-ESwkL3Fn6_jGHkqazhqH9oLRgm6r15c0hO007-yo1Yj1O6insDRuAyG5kwMTo15UHJU>)

## Color Harmony

In the [blog post](https://material.io/blog/android-stable-release-1-5) announcing MDC 1.5.0, we discussed using tooling to select colors beyond the core colors that exist in every Material3 theme. Custom or extended color inside of a dynamic theme presents an interesting question: “How do we guarantee that a reserved color will look well alongside a dynamic theme AND still be legible?”

![Two swatches of yellow selected from a spectrum from yellow to green.](<https://lh3.googleusercontent.com/EC9mZRSbwP_aAQTHL4z8Rn5Gaw7THGeSKGDcIkzPAXrPJLMWdwbfQ79Xc8xtpoY3xmWr7XKRWl5jZb_y93Mb4AMFIzuwaCddZY5B_2z1u8kQCqKFY2E>)

The algorithm we use to create Material3 themes can handle this situation using a concept called color harmony. This harmonization works by shifting a target color towards a source color. Colors that are closer to each other appear more harmonious whereas colors that are further apart appear more discordant.

![Comparison of harmonious and less-harmonious color swatches.](<https://lh3.googleusercontent.com/bCav1hV4fr9zldiZZJMkw_-zEp7JDwMPN8sHD2-Ib0tGlIDtPYu7bNDb6yKf9IvjEJUCUjcCpC59AKcqZMd1wpaPJreB2_l62vi7x-hQ15A7RvAVjQPi>)

![Diagram of color swatches moving toward user-selected color.](<https://lh3.googleusercontent.com/xgmGm0y5iMAixInKsXrdFo15UcR9ThHeM81xlRDVpXTYU3QDLJ6yyOfg3V2Y_icSD0tCQDjXhjrsSPI6KFhlbaAM5MICE0cZlRUHu82NYqXCWhhKUE6N>)

To read more about color science and the whys and hows of color harmony, check out [The Science of Color & Design](https://material.io/blog/science-of-color-design) and [Designing Harmony into Dynamic Color](https://material.io/blog/dynamic-color-harmony).

### Applying Color Harmony On Demand

Using the functions [`harmonizeWithPrimary`](https://github.com/material-components/material-components-android/blob/948d5dac03a55971338d36bb52f1f49bd448ebd7/lib/java/com/google/android/material/color/MaterialColors.java#L185) and [`harmonize`](https://github.com/material-components/material-components-android/blob/948d5dac03a55971338d36bb52f1f49bd448ebd7/lib/java/com/google/android/material/color/MaterialColors.java#L199), you can harmonize a given color with the primary color for the theme or an arbitrary color.

### Applying Color Harmony Systematically

In most cases, instead of calculating harmonized colors on demand, you’ll want to leverage helper classes in MDC 1.6.0 that can take in a set of attributes or colors and return harmonized versions to your theme seamlessly. HarmonizedColorAttributes and HarmonizedColorOptions are two builder classes you can use to enable harmonization when you retrieve a dynamic color Context.

The snippet below shows how to build a dynamic context that automatically harmonizes to two specified colors.

For a more detailed discussion of how to apply color harmonization in code, check out the [Color Harmonization section](https://github.com/material-components/material-components-android/blob/master/docs/theming/Color.md#using-color-harmonization) of our developer documentation on color or the [codelab](https://codelabs.developers.google.com/harmonize-color-android-views#2) that demonstrates both methods.

## What’s next for MDC ?

We’re fast at work on the next major version of MDC. You can follow the progress, file [bug reports](https://github.com/material-components/material-components-android/issues/new?assignees=&labels=bug&template=bug_report.md&title=%5BComponent+name%5D+Short+description+of+issue) and [feature requests](https://github.com/material-components/material-components-android/issues/new?assignees=&labels=feature+request&template=feature_request.md&title=%5BComponent+name%5D+Short+description+of+request) on GitHub. Also feel free to reach out to us on Twitter [@materialdesign](https://twitter.com/materialdesign).

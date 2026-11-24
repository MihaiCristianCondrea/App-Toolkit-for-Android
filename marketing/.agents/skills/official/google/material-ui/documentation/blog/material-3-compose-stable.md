---
title: "Material Design 3 for Compose is now stable"
source_url: "https://m3.material.io/blog/material-3-compose-stable"
lastmod: "2025-03-11"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "blog"
assets:
  - "assets/images/69c1a8d9affd-yYfg__SGRj3ELVT3kcpto_hEBpyDEShR2HyKztmxiYxBIg2AKBokBC03XX8M72Zi2mX4bADa4pcg6PaWGJJ-qyDFAMK0rUJn.png"
  - "assets/images/09984d0ccf24-uqNajkKx7YKEhRZRWUvZGjuoziPV7zqyw5rBZW3OnpAynrqiWwTDEzrprVB9Ip1wktkBJ5BuQQ1Vcx9BsOL6Q3tIOsGFkPLW.png"
  - "assets/images/b4c583423094-BT8EcorV03uUW4SdPpdh3gw4d9qV1GTuL01gZglszBZoLDiO7DTy8XEPlguPXQ__zMm7qKrQmyCF4EM-6j9juIYbB5q66kSx.png"
  - "assets/images/59ebb3411d91-isYinQaWjIX0jX1aZY4i0QWOaKF-EQur83BI6AezKaQVSZ2poPCQi2BPQPvSeSQxmu7Sr_k4iubDvoKARRLSAhY1O0c0jB7K.png"
  - "assets/images/02e76f49ddd6-WxI5lRgubhlav9dG6WU6-yeSe3lAWuJQ0xuo7l2fMm1HWfcrGhzEbXlGngr99mWaLfvffiAIhhF45KkoopvokP9jOlP0ZVj8.png"
  - "assets/images/6622b9825d8b-9ZWLzSdlen3IzreM8RuT06sQZff-nbaR5vAzxZC8CwQgBit5eIxhGAVT66hmwGZFOn8lgPVx0xbpLJSvOl2-85iOOIHoro0j.png"
  - "assets/images/0ce3da010f66-7ZoGjkeViQgxDGkDzS0kzS5CofQ1RO1c0WL292JLfJE9Kk5Hqu_vCJ4tDGZ2W2opJj9CDJEqAV8kPVyfnlB_6n1l1NnIDnC3.png"
  - "assets/images/9cfcdc06119a-Z9-ggjETlvW8AR4YsYN8R0L3ooMkhl61Q0ytyUZYgEBj2K-jbl9wY0AIb-qfI98O_RPduFi6aI-ULCBBP0YMg7vXKR2cRvG1.png"
  - "assets/images/1aa683040f93-Gt8j2lkXa_bMtgZSvKh8GoC8iRjQ9uYlcHXe5NfvBxmXqxGgUFD9AsR9dubzaZKvUeB0SQieplUmpaqW_3fobMs3Dni6ZG4e.png"
  - "assets/images/f2a3f7cb69c0-v8BRFtSQBn-K4TTqtIh5rzjjj6hZSU0bx-2yaZEEMjjswx-IsswH7XIOeOVxfEYibE5GZXEdASkkGTKM86PJ-VE4KhX2B4wH.png"
---

# Material Design 3 for Compose is now stable

Exploring the first stable release of Material Design 3 for Compose

![Image](<https://lh3.googleusercontent.com/yYfg__SGRj3ELVT3kcpto_hEBpyDEShR2HyKztmxiYxBIg2AKBokBC03XX8M72Zi2mX4bADa4pcg6PaWGJJ-qyDFAMK0rUJn1eBzdZXilA7cw74K70c>)

Published: 2022-10-24T08:00:00

Today marks the first stable release of [Compose Material 3](https://developer.android.com/reference/kotlin/androidx/compose/material3/package-summary). The library allows you to build Jetpack Compose UIs with Material Design 3, the next evolution of Material Design. You can start using [Material Design 3](../index.md) in your apps today!

Note: The terms "Material Design 3," "Material 3," and "M3" are used interchangeably.

Material 3 includes updated theming and components, exclusive features like dynamic color, and is designed to be aligned with the latest Android visual style and system UI.

![Image](<https://lh3.googleusercontent.com/uqNajkKx7YKEhRZRWUvZGjuoziPV7zqyw5rBZW3OnpAynrqiWwTDEzrprVB9Ip1wktkBJ5BuQQ1Vcx9BsOL6Q3tIOsGFkPLWI1pznkAm6TWZ4DJUvQ>)

Multiple apps using Material Design 3 theming

You can start using Material Design 3 in your apps by adding the Compose Material 3 dependency to your `build.gradle` files:

Note: See the latest M3 versions on the [Compose Material 3 releases page](https://developer.android.com/jetpack/androidx/releases/compose-material3).

## Color schemes

Material 3 brings extensive, finer grained color customisation, and comes with both light and dark color scheme support out of the box. The [Material Theme Builder](https://m3.material.io/theme-builder#/dynamic) allows you to generate a custom color scheme using core colors, and optionally export Compose theming code. You can read more about [color schemes and color roles](https://m3.material.io/styles/color/the-color-system/key-colors-tones).

![Image](<https://lh3.googleusercontent.com/BT8EcorV03uUW4SdPpdh3gw4d9qV1GTuL01gZglszBZoLDiO7DTy8XEPlguPXQ__zMm7qKrQmyCF4EM-6j9juIYbB5q66kSxdRObF_x7Xa7NG1ZI6A>)

Material Theme Builder to export Material 3 color schemes

## Dynamic color

Dynamic color derives from the user’s wallpaper. The colors can be applied to apps and the system UI.

Dynamic color is available on Android 12 (API level 31) and above. If dynamic color is available, you can set up a dynamic `ColorScheme`. If not, you should fall back to using a custom light or dark `ColorScheme`.

| ![Image](<https://lh3.googleusercontent.com/isYinQaWjIX0jX1aZY4i0QWOaKF-EQur83BI6AezKaQVSZ2poPCQi2BPQPvSeSQxmu7Sr_k4iubDvoKARRLSAhY1O0c0jB7K-v2VgefJEyPYqucK1r0>) Reply Dynamic theming from wallpaper | ![Image](<https://lh3.googleusercontent.com/WxI5lRgubhlav9dG6WU6-yeSe3lAWuJQ0xuo7l2fMm1HWfcrGhzEbXlGngr99mWaLfvffiAIhhF45KkoopvokP9jOlP0ZVj809u6ZHwuigmlO4rd0i13>) Default Reply app theming |
| --- | --- |

The `ColorScheme` class provides builder functions to create both dynamic and custom [light](https://developer.android.com/reference/kotlin/androidx/compose/material3/package-summary#dynamiclightcolorscheme) and [dark](https://developer.android.com/reference/kotlin/androidx/compose/material3/package-summary#dynamicdarkcolorscheme) color schemes:

Theme.kt

## Material components

The Compose Material 3 APIs contain a wide range of both new and evolved Material components, with more planned for future versions. Many of the Material components, like `Card`, `RadioButton` and `CheckBox`, are no longer considered experimental; their APIs are stable and they can be used without the `ExperimentalMaterial3Api`annotation.

The M3 `Switch` component has a brand new UI refresh with accessibility-compliant minimum touch target size support, color mappings, and optional  icon support in the switch thumb. The touch target is bigger, and the thumb size increases on user interaction, providing feedback to the user that the thumb is being interacted with.

![Image](<https://lh3.googleusercontent.com/9ZWLzSdlen3IzreM8RuT06sQZff--nbaR5vAzxZC8CwQgBit5eIxhGAVT66hmwGZFOn8lgPVx0xbpLJSvOl2-85iOOIHoro0jq1G5MsLK8VYwYJNp2A>)

Material 3 Switch thumb interaction

Navigation drawer components now provide wrapper sheets for content to change colors, shapes, and elevation independently.

| **Navigation drawer component** | **Content** |
| --- | --- |
| ModalNavigationDrawer | ModalDrawerSheet |
| PermanentNavigationDrawer | PermanentDrawerSheet |
| DismissableNavigationDrawer | DismissableDrawerSheet |

![Image](<https://lh3.googleusercontent.com/7ZoGjkeViQgxDGkDzS0kzS5CofQ1RO1c0WL292JLfJE9Kk5Hqu_vCJ4tDGZ2W2opJj9CDJEqAV8kPVyfnlB_6n1l1NnIDnC3tQ0qx4jBmMUwu4QQw5A>)

ModalNavigationDrawer with content wrapped in ModalDrawerSheet

We have a brand new `CenterAlignedTopAppBar` in addition to already existing app bars. This can be used for the main root page in an app: you can display the app name or page headline with home and action icons.

![Image](<https://lh3.googleusercontent.com/Z9-ggjETlvW8AR4YsYN8R0L3ooMkhl61Q0ytyUZYgEBj2K-jbl9wY0AIb-qfI98O_RPduFi6aI-ULCBBP0YMg7vXKR2cRvG1tW8oip-NSEoDey0UvecE>)

Material CenterAlignedTopAppBar with home and action items

See the latest M3 components and layouts on the [Compose Material 3 API reference overview](https://developer.android.com/reference/kotlin/androidx/compose/material3/package-summary#overview). Keep an eye on the [releases page](https://developer.android.com/jetpack/androidx/releases/compose-material3) for new and updated APIs.

## Typography

Material 3 simplified the naming and grouping of typography to:

-   Display
-   Headline
-   Title
-   Body
-   Label

There are large, medium, and small sizes for each, providing a total of 15 text style variations.

The `Typography` constructor offers defaults for each style, so you can omit any parameters that you don’t want to customize:

You can customize your typography by changing default values of [`TextStyle`](https://developer.android.com/reference/kotlin/androidx/compose/ui/text/TextStyle) and [font-related properties](https://developer.android.com/reference/kotlin/androidx/compose/ui/text/font/package-summary) like `fontFamily` and `letterSpacing`.

## Shapes

The Material 3 shape scale defines the style of container corners, offering a range of roundedness from square to fully circular.

There are different sizes of shapes:

-   Extra small
-   Small
-   Medium
-   Large
-   Extra large

![Image](<https://lh3.googleusercontent.com/Gt8j2lkXa_bMtgZSvKh8GoC8iRjQ9uYlcHXe5NfvBxmXqxGgUFD9AsR9dubzaZKvUeB0SQieplUmpaqW_3fobMs3Dni6ZG4em6_kssnjKoR2sHQHnw>)

Material Design 3 shapes used in various components as default value

Each shape has a default value but you can override it:

You can [read more about applying shape](https://m3.material.io/styles/shape/overview).

## Window size classes

Jetpack Compose and Material 3 provide window size artifacts that can help make your apps adaptive. You can start by adding the Compose Material 3 window size class dependency to your `build.gradle` files:

Window size classes group sizes into standard size buckets, which are breakpoints that are designed to optimize your app for most unique cases.

![Image](<https://lh3.googleusercontent.com/v8BRFtSQBn-K4TTqtIh5rzjjj6hZSU0bx-2yaZEEMjjswx-IsswH7XIOeOVxfEYibE5GZXEdASkkGTKM86PJ-VE4KhX2B4wHskearQUaOZuJNkKyJVc>)

WindowWidthSize Class for grouping devices in different size buckets

See the [Reply Compose sample](https://github.com/android/compose-samples/tree/main/Reply) to learn more about adaptive apps and the window size classes implementation.

## Window insets support

M3 components, like top app bars, navigation drawers, bar, and rail, include built-in support for window insets. These components, when used independently or with `Scaffold`, will automatically handle insets determined by the status bar, navigation bar, and other parts of the system UI.

`Scaffold` now  supports the `contentWindowInsets` parameter which can help to specify insets for the scaffold content.

Scaffold insets are only taken into consideration when a `topBar` or `bottomBar` is not present in `Scaffold`, as these components handle insets at the component level.

## Resources

With Compose Material 3 reaching stable, it’s a great time to start learning all about it and get ready to adopt it in your apps. Check out the resources below to get started.

-   Fully Material 3 and Compose sample [Reply](https://github.com/android/compose-samples/tree/main/Reply)
-   The [Material 3 guidance](https://developer.android.com/jetpack/compose/themes/material3) to start adding Material 3 to your apps
-   The Material 2 to Material 3 [migration guide](https://developer.android.com/jetpack/compose/themes/material2-material3)
-   The [Jetpack Compose Samples GitHub repository](https://github.com/android/compose-samples), where you’ll find a variety of up-to-date samples using Material 3
-   The Compose community on [StackOverflow](https://stackoverflow.com/questions/tagged/material-design) and the [Kotlin Slack group](http://slack.kotlinlang.org/)
-   The [bug tracker](https://issuetracker.google.com/issues/new?component=742043), where you can report an issue and track feature requests

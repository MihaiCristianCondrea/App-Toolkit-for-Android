---
title: "Introducing the Compose Material Catalog"
source_url: "https://m3.material.io/blog/jetpack-compose-catalog"
lastmod: "2024-12-23"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "blog"
assets:
  - "assets/images/e71a853a36ff-EQuO-xaJrUVqix8E0Va2vWxmUWzEUU4rK7uQT_zfmdJaftbcM2g_kDDrJQavRjWpG1emn4uHlaA9AwptudrM_vSXzIbrUOz6.png"
  - "assets/images/726a9b6b1acb-cu90fkIIhQt6dUFBynmoXpBiC_EQDSDmocEd8oFHPtBlWeagHL9m9wbDHUdgD371oR_nrxs3wcik020Vn5bJCXzR7XYr_hnY.png"
  - "assets/images/246f1cb0e05d-bgVADi_We_Ncs0GcPReNEgA8K1yKHoytRbMwMZQ0kPATeadb1WZM8LJwRZs-N_SSPq6XlFBO91rN6Tzf_V-4j4T1DILxTmZj.png"
  - "assets/images/b50b55b6a9d7-xyweyVkypn20erzyYn9GN2T-t4sv1SLDi3zBS_3u0x9XD92HZap__ktET11kcA0IzZxuZ3Q8R52nMP7p6Mm1oN15_atYt-eY.png"
---

# Introducing the Compose Material Catalog

An overview of the new Material Design catalog for Jetpack Compose

![Image](<https://lh3.googleusercontent.com/EQuO-xaJrUVqix8E0Va2vWxmUWzEUU4rK7uQT_zfmdJaftbcM2g_kDDrJQavRjWpG1emn4uHlaA9AwptudrM_vSXzIbrUOz6NyBixnjGEF_rRTMISlk3>)

Published: 2021-05-19T10:30:00

We’re excited to announce a new canonical reference of Material Design components, theming, dark theme, and how to implement these features in Jetpack Compose: the Compose Material Catalog.

If you’d like to learn more about the Material Design features of Compose, check out our [blog post](https://material.io/blog/jetpack-compose-beta).

## Catalog features

The catalog consists of three main screens: the home screen, the component screen, and the example screen. At any point you can launch the theme picker or “more” menu from the top app bar. The app also supports dark theme.

![Screens in the Compose Material Catalog](<https://lh3.googleusercontent.com/cu90fkIIhQt6dUFBynmoXpBiC_EQDSDmocEd8oFHPtBlWeagHL9m9wbDHUdgD371oR_nrxs3wcik020Vn5bJCXzR7XYr_hnYxCaVGk51XImCg_ftgGg>)

Screens in the Compose Material Catalog

#### Home screen

The home screen consists of a responsive grid of Material Components available in Compose—from floating action buttons to navigation drawers. Tapping on a component launches the component screen.

#### Component screen

The component screen consists of a description of the Material Component and a list of relevant examples implemented in Compose. Tapping on an example launches the example screen.

#### Example screen

The example screen contains an interactive example of the Material Component implemented in Compose.

#### Theme picker

The theme picker and “more” menu are accessible via the top app bar from any screen.

![Theme picker in the Compose Material Catalog](<https://lh3.googleusercontent.com/bgVADi_We_Ncs0GcPReNEgA8K1yKHoytRbMwMZQ0kPATeadb1WZM8LJwRZs-N_SSPq6XlFBO91rN6Tzf_V-4j4T1DILxTmZjIktl_06LCAYU24eWU0E>)

Theme picker in the Compose Material Catalog

The theme picker allows you to pick Material Theming values—including color, typography, and shape. These values are applied (by animating between the current theme and the new theme) to examples and the app itself, so you can see how components respond to the changes.

#### “More” menu

The “more” menu contains links to design guidelines, developer docs, source code, and the issue tracker. These links are specific to the screen you’re on—eg. The source code link shows the source of a composable on the component screen, and the source of a sample on the example screen.

#### Dark theme

The app responds to the system dark theme. You can see Material Dark Theme features in action, like elevation overlays and limited color accents.

![Dark theme in the Compose Material Catalog](<https://lh3.googleusercontent.com/xyweyVkypn20erzyYn9GN2T-t4sv1SLDi3zBS_3u0x9XD92HZap__ktET11kcA0IzZxuZ3Q8R52nMP7p6Mm1oN15_atYt-eYQHASDEJJln1RQaGM17A>)

Dark theme in the Compose Material Catalog

## In sync and up-to-date

The app is available alongside the source code of Compose libraries, ensuring you’ll have the latest Compose API changes. Built directly from the code snippets in the API reference docs, you’ll receive the latest samples directly in the catalog.

Let’s take a look at an example:

-   [The compilable `ButtonSample` stored in AOSP](https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:compose/material/material/samples/src/main/java/androidx/compose/material/samples/ButtonSamples.kt)
-   [The `@sample` tag reference to `ButtonSample` in the `Button` KDoc](https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:compose/material/material/src/commonMain/kotlin/androidx/compose/material/Button.kt)
-   [The `Button` API reference doc showing the `ButtonSample` code snippet](https://developer.android.com/reference/kotlin/androidx/compose/material/package-summary#button)
-   [The app using the `ButtonSample` as an example for buttons](https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:compose/material/material/integration-tests/material-catalog/src/main/java/androidx/compose/material/catalog/library/model/Examples.kt)

## Get the catalog

You can get access to the app in a variety of formats:

-   Get the app on [Google Play](https://play.google.com/store/apps/details?id=androidx.compose.material.catalog)
-   Download APKs from the [Jetpack Compose samples GitHub repository releases page](https://github.com/android/compose-samples/releases)
-   View the source code on AOSP using [Android Code Search](https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:compose/integration-tests/material-catalog/) or the [synchronized Android Jetpack repository on GitHub](https://github.com/androidx/androidx/tree/androidx-main/compose/integration-tests/material-catalog). You can also run the app if you’ve checked out the code and opened it in Android Studio.

Found the catalog helpful? Let us know on [Twitter](https://twitter.com/MaterialDesign)!

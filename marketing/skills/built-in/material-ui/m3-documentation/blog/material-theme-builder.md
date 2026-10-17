---
title: "Introducing Material Theme Builder"
source_url: "https://m3.material.io/blog/material-theme-builder"
lastmod: "2024-12-18"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "blog"
assets:
  - "assets/images/2f5c0016628b-rKjjYo5u2SmXb8kfJvGrN8OAlwbBBZ24hyMWY5PCvt6V4iVhs-R9GvQjpgGm7-P8uAcC3BcmyIBmPgFtASL2nr5HO1H4r0Lc.png"
  - "assets/images/7abbabf9a11d-ru5tKBEJW4eDiLx4Lj8Vh67Lxw0cBm1nmThqeJXoVlhpQrG0R-ub5B0FVnb1hSMvmhrMPfDTVX6Ge8iA1MmH2j1E-5l2m772.png"
  - "assets/images/162261f5684e-pi5v57OE5qRvS3OAdk4Ifiug9_I0U8OUjRSgSkhQuTQJBcuoAN0AUl3oSsQhd-xKR_th4wO6MSorSnxaVnCBDk52zu-NEA70.png"
  - "assets/images/fe316110bb2e-in9HDVwfSDGN3EwonNk3vHZahIEikmKSyTJW80iHb-2zoARNNsguhUU06h1rwtqBRiIQpmyYelrE8-mUb47to4cThKHoBfqp.png"
  - "assets/images/0267930ac8d9-4x0iIUIImSgACQgMshakt63XQLnH_7hpaPVXNvvWKli6_5Mjl0Xmoc_A214aAaInDoYnHd2buW-Vqko1Iv5UtR5rOxJT7kFJ.png"
  - "assets/images/32b4385d748e-C0fyWFF3nfOOXyGYS_sOHo_KVmFuvI-aJaVYDeQkHnXhjVBdJTuwEnGbAAcHRAyN3CLtEJFbVQ29fPX-DweQe1jtaizAql4W.png"
  - "assets/images/15727fe81aad-ZNy-DSNdWoXNwerefZa-QImSGcy87htA1ytLMIa6ASFda1xcm_17eCtru7bsmv-px4rgcn1XuzXtAKGdDgAWC1sAuYrbhtzC.png"
  - "assets/images/562c1b3b417a-BJ1UTCG9NFC_XI05sa3_Z2CsTRjhkorR_7cKoo0zVspdXb8pz7qrB7VdyKzH6WhbVU0ptnH2rsESeDg37DBj-d2eytpOdjGY.png"
  - "assets/images/008b234e1e5b-enEHy4KTgyzmvVDNy5XgOBNAMVaefY9Kz_chuXG-2JIxA7oCEKLqy6GsMH2Hgnajar9xQa2wQN0huSi-4Fd0KhfGdH9EXSiO.png"
---

# Introducing Material Theme Builder

Visualize dynamic color, build a custom theme, and export to code

![Image](<https://lh3.googleusercontent.com/rKjjYo5u2SmXb8kfJvGrN8OAlwbBBZ24hyMWY5PCvt6V4iVhs-R9GvQjpgGm7-P8uAcC3BcmyIBmPgFtASL2nr5HO1H4r0LcL3EOwCyeoVM3X54-aJs>)

Published: 2021-10-27T09:15:00

Material Theme Builder is a new tool that helps you visualize Material You’s dynamic color and create a custom Material Design 3 theme. With built-in code export, it’s easy to migrate to Material's new color system and take advantage of dynamic color.

## Quick links

[Install Material Theme Builder as a Figma Plugin](https://goo.gle/material-theme-builder-figma) or try [Material Theme Builder on the Web](https://goo.gle/material-theme-builder-web), and grab the new [Material 3 Design Kit](http://goo.gle/m3-design-kit).

## Themes powered by Design Tokens

Material Design can be customized to represent a variety of stylistic choices. A theme is a set of style choices that define the visual appearance of a product. With Material Design 3, we are introducing design tokens—small, reusable design decisions that reflect the system’s visual style. By building with tokens instead of static values, design and code share a source of truth.

![Image](<https://lh3.googleusercontent.com/ru5tKBEJW4eDiLx4Lj8Vh67Lxw0cBm1nmThqeJXoVlhpQrG0R-ub5B0FVnb1hSMvmhrMPfDTVX6Ge8iA1MmH2j1E-5l2m772k524oZUDUX0EgRJOffs>)

Our [Material 3 Design Kit](http://goo.gle/m3-design-kit) and [Material 3 Component libraries](http://m3.material.io) have been updated to reflect the new design tokens and their assigned roles. In Figma, Material Theme Builder generates these tokens as Figma styles. Material Theme Builder exports design tokens directly as a [Design Systems Package (DSP)](https://github.com/material-foundation/material-tokens), and theming code for our Material 3 libraries on Android Views and Jetpack Compose.

## Visualize dynamic color

![Image](<https://lh3.googleusercontent.com/pi5v57OE5qRvS3OAdk4Ifiug9_I0U8OUjRSgSkhQuTQJBcuoAN0AUl3oSsQhd-xKR_th4wO6MSorSnxaVnCBDk52zu-NEA70MeV_hKMPyNuRIPL0aZM>)

Dynamic color plays a key role in Material You, creating individualized and expressive experiences for your users. Design tokens and their assigned color roles allow apps to seamlessly accept any dynamic color scheme in place of pre-set color values.

Material Theme Builder on the web gives a glimpse into how dynamic color from a wallpaper may represent itself in UI. With the Figma plugin, you’ll be able to see how dynamic color represents itself across your design. First, you’ll need to manually map the design token Figma styles to your custom components or use components from the Material 3 design kit to build your UI.

![Image](<https://lh3.googleusercontent.com/in9HDVwfSDGN3EwonNk3vHZahIEikmKSyTJW80iHb-2zoARNNsguhUU06h1rwtqBRiIQpmyYelrE8-mUb47to4cThKHoBfqpZNcPK2zia58y0oBv>)

Once you have a UI built with Material Design tokens, select an element or frame of the design and click “swap” to apply the Material Theme styles, now any changes you make in the Material Theme Builder will be reflected in your UI. While selected, you can switch between light and dark themes, by clicking the moon and sun icon buttons.

![Image](<https://lh3.googleusercontent.com/4x0iIUIImSgACQgMshakt63XQLnH_7hpaPVXNvvWKli6_5Mjl0Xmoc_A214aAaInDoYnHd2buW-Vqko1Iv5UtR5rOxJT7kFJYtbGnS6qMMLNyJk0VQ>)

## Build a custom theme

![Image](<https://lh3.googleusercontent.com/C0fyWFF3nfOOXyGYS_sOHo_KVmFuvI-aJaVYDeQkHnXhjVBdJTuwEnGbAAcHRAyN3CLtEJFbVQ29fPX-DweQe1jtaizAql4WKoHBlP6da3VAZvQVmtU>)

In order to take advantage of the latest features introduced with Material You, you’ll need to migrate to Material Design 3. Material 3 theme expands on Material 2’s color space by introducing new color roles and deprecating others. With Material Theme Builder on the web and Figma, you can easily create a custom color scheme based on your brand or style. Once you have a custom theme, you can export your code to be used with our Material Design 3 libraries for Android Views or Jetpack Compose.

![Image](<https://lh3.googleusercontent.com/ZNy-DSNdWoXNwerefZa-QImSGcy87htA1ytLMIa6ASFda1xcm_17eCtru7bsmv-px4rgcn1XuzXtAKGdDgAWC1sAuYrbhtzCidOl06fCWjpmc6d733Ey>)

For an in depth guide of the new Material 3 theme updates, check out our [migration guide](http://material.io/blog/migrating-material-3).

## Export to code

Material Theme Builder allows you to export the theming code to multiple formats: Android Views (XML), Jetpack Compose (Kotlin) and Design System Package (DSP).

Note that the surface colors (Surface 1 - 5) are shown within Material Theme Builder to help visualize surface tonal colors based on elevation. However, these surface tonal colors are not exported in the code. Their actual color values are calculated by setting elevation within Android. For details on how to set elevation in Android see documentation for [MDC-Android](https://m3.material.io/libraries/mdc-android/color-theming) and [Compose Material 3](https://developer.android.com/jetpack/compose/themes/material#elevation).

#### Compose Material 3 (Kotlin)

![App directory within Android Studio, showing ui.theme and Color.kt, Theme.kt](<https://lh3.googleusercontent.com/BJ1UTCG9NFC_XI05sa3_Z2CsTRjhkorR_7cKoo0zVspdXb8pz7qrB7VdyKzH6WhbVU0ptnH2rsESeDg37DBj-d2eytpOdjGYYWnNxbIi9bRZ_YdK3w>)

Within Compose Material 3, ui/theme directory hosts your theming files

Material Theme Builder exports `Theme.kt` which sets up your `MaterialTheme` with the proper design token mapping. `Color.kt` stores the color values of your custom theme. We strongly recommend referencing the Colors provided to the `MaterialTheme` composable via `MaterialTheme.colors` and not the values defined in `Colors.kt` in your UI. This will allow you to accept dynamic colors and easily switch between light and dark themes.

#### MDC-Android (XML)

![App directory within Android Studio, showing values and values-night with colors.xml and themes.xml](<https://lh3.googleusercontent.com/enEHy4KTgyzmvVDNy5XgOBNAMVaefY9Kz_chuXG-2JIxA7oCEKLqy6GsMH2Hgnajar9xQa2wQN0huSi-4Fd0KhfGdH9EXSiOG1pguVET3_Kt0LeYQNk>)

Within MDC-Android, res/values and res/values-night directories host your theming files

Material Theme Builder exports colors.xml, and both themes.xml files for light and dark themes. We strongly recommend referencing the Android color attributes, such as `?attr/colorPrimary` and not the hard coded `@color` values in your UI. This will allow you to accept dynamic colors and easily switch between light and dark themes.

## Start building

We are so excited to be bringing you these updates with Material Design 3 and the updated tooling to support designers and developers in the process.

-   [Install Material Theme Builder as Figma Plugin](https://goo.gle/material-theme-builder-figma)
-   [Material Theme Builder on the Web](https://goo.gle/material-theme-builder-web)
-   [Design lab: Visualizing dynamic color in your app](https://goo.gle/visualize-dynamic-color)
-   [Codelab: Applying dynamic color](https://goo.gle/apply-dynamic-color)

Watch our [talk at Schema 2021](https://youtu.be/clDuqcKgNBQ) where Ivy Knight and Rody Davis talk about bringing the Material Theme Builder to life. Subscribe to the [Material Design YouTube Channel](https://www.youtube.com/MaterialDesign) and follow [@MaterialDesign on Twitter](https://twitter.com/materialdesign).

---
title: "Why We Recommend Material Components for Android"
source_url: "https://m3.material.io/blog/why-we-recommend-material-design-components-android"
lastmod: "2024-12-23"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "blog"
assets:
  - "assets/images/2120829d4f6a-m79E6A7AVxB-mJ5yzx1aPqxbo2QyiEoAzMJhpbmyIGU9brgB0d4hZUDdXIt6suQFDOB6g7RcBcXAq8H_jfwTOhbTd3-yZ2MC.png"
  - "assets/images/ab34da16a17f-Y0bNV0tJr9AbuQGzOHwb4dJcesUxg0YY0ZL21f4gcVi09N1ikkUUsRBe6ZdLeoqQHj_Na0RKC27q_4-5EATlG2DmdpMPrHz4.png"
  - "assets/images/06fc23a32333-X_GpLf-IrLScJgm3zTMe_KyFHPyZklfGXZ0vMVxojNV0tUHYD1e8HHS-E7ow0iyeuDrvE-1XLqVKf_0CdERMNmHxM3ZWm-nc.png"
  - "assets/images/04b99dd165c6-ckPw0BqCjpOW5-YqRDf2WVP9twTlL82mMCOQ1Xu7JYQyvao1WDCsTp8xW-mEhYOiYSNHIf68vKhNSSja8ANmcYLxRxQuw-LO.png"
  - "assets/images/6133cd85f49a-JFBeD_3tKAJescoKiWBDW3SS2ZBIY7UKEMVknQWCLSswP9iXbTiU78PdJMz1wIC5giWqT0OkX9FTp8WxCQHJA_rnqPcDfxeC.png"
  - "assets/images/a076375af1c4-9Y1NM2YQyI9p_tx4bzGuQr-bAGxfGbQJM7vzV3Ah2C82si22V8BisTafAXkex6hKxzPMQZorwJ207dxeny6MyWNkgDlebH1X.png"
  - "assets/images/34a959555efa-HcUay0xLeUoYBEDScQ61lMuC9bTlWCPWagTZHqtH_0vyobpouF_yZAgb-T_LAwMpNir1oSmIjMXQI4ChGfEM6eNdyFCvmY9T.png"
---

# Why We Recommend Material Components for Android

Ensuring that patterns learned in one app can be used in another

![Image](<https://lh3.googleusercontent.com/m79E6A7AVxB-mJ5yzx1aPqxbo2QyiEoAzMJhpbmyIGU9brgB0d4hZUDdXIt6suQFDOB6g7RcBcXAq8H_jfwTOhbTd3-yZ2MCcyuvC5k>)

Published: 2020-08-25T13:00:00

##### _Android users expect your app to look and behave in a way that’s consistent with the platform. You should follow material design guidelines for visual and navigation patterns…
— [d.android.com/design](https://developer.android.com/design)_

Since your app exists alongside other apps installed on a user’s phone, it’s recommended that applications follow the [Material Design guidelines](https://material.io/design/introduction) to ensure that apps operate consistently, and that patterns learned in one app can be used in another.

To implement this, you can use the [Material Components for Android](https://github.com/material-components/material-components-android) (MDC) library. This post outlines the many benefits of using MDC, explaining why it’s our recommended solution.

## Widgets

![Grid of baseline purple Material Components including banners, bottom navigation, buttons, floating action buttons, card, and chips.](<https://lh3.googleusercontent.com/Y0bNV0tJr9AbuQGzOHwb4dJcesUxg0YY0ZL21f4gcVi09N1ikkUUsRBe6ZdLeoqQHj_Na0RKC27q_4-5EATlG2DmdpMPrHz4gs-UHA>)

MDC provides styled versions of standard widgets making it easy to implement Material styling. For example Material offers styled versions of `[Button](https://material.io/develop/android/components/buttons)`s, `[Toolbar](https://material.io/develop/android/components/app-bars-top)`s, `[CheckBox](https://material.io/develop/android/components/checkboxes)`es and more. If you use a `MaterialComponents` theme, then Material widgets will be instantiated instead of the stock components when you inflate a layout (by MDC’s [View Inflater](https://developer.android.com/reference/com/google/android/material/theme/MaterialComponentsViewInflater)) so it’s easy to achieve Material styling without having to make large updates to your layouts:

All Material widgets extend their relevant `AppCompat` counterparts so they benefit from the same backported functionality or bug fixes.

Material widgets offer extra styling and functionality over their platform or `AppCompat` counterparts, for example [`MaterialButton`](https://material.io/develop/android/components/buttons) offers a number of different display styles:

![Three baseline purple Material buttons with a filled style, text style and outlined style.](<https://lh3.googleusercontent.com/X_GpLf-IrLScJgm3zTMe_KyFHPyZklfGXZ0vMVxojNV0tUHYD1e8HHS-E7ow0iyeuDrvE-1XLqVKf_0CdERMNmHxM3ZWm-nc-B86>)

One of my favorite additions is `TextView`s being replaced with [`MaterialTextView`](https://material.io/develop/android/components/material-text-view)s which add support for specifying line heights in `TextAppearance`s. Handy.

As well as adding to existing widgets, MDC offers a number of new widgets, not available in the platform or `AppCompat`. You’ve likely seen [Bottom Navigation](https://material.io/develop/android/components/bottom-navigation-view/), [Bottom Sheet](https://material.io/develop/android/components/bottom-sheet-behavior/) and [Floating Action Buttons](https://material.io/develop/android/components/floating-action-button/) but might not have come across [Chips](https://material.io/develop/android/components/chip/), [Date Picker](https://material.io/develop/android/components/picker) or [Time Picker](https://github.com/material-components/material-components-android/blob/master/docs/components/TimePicker.md).

![Material Time Picker with a digital clock showing 07:00, an AM/PM toggle, and a circular time picker.](<https://lh3.googleusercontent.com/ckPw0BqCjpOW5-YqRDf2WVP9twTlL82mMCOQ1Xu7JYQyvao1WDCsTp8xW-mEhYOiYSNHIf68vKhNSSja8ANmcYLxRxQuw-LOxjogpYw>)

For a complete list of components offered by the library, see the [components section](https://material.io/components).

## Material Theming

![Various apps demonstrating Material Theming, including a purple baseline app, a pink “Shrine” shopping app, a dark purple and red “Crane” travel app, and a grey and orange “Reply” inbox app.](<https://lh3.googleusercontent.com/JFBeD_3tKAJescoKiWBDW3SS2ZBIY7UKEMVknQWCLSswP9iXbTiU78PdJMz1wIC5giWqT0OkX9FTp8WxCQHJA_rnqPcDfxeCEEAH>)

[Material Theming](https://material.io/design/material-theming/) is a systematic way to customize Material Design to reflect your product’s brand. A Material Theme comprises [color](https://material.io/design/color/), [typography](https://material.io/design/typography/) and [shape](https://material.io/design/shape/) attributes. Customizing these will be automatically reflected in the components you use to build your app.

You can think of Material Theming as a design system for creating design systems 🤯. You configure the color, type and shape inputs and get out a complete design system for your brand.

Nick Rout goes in depth into each of the three sub-systems in the following posts:

[Building a Material Theme on Android: Color](https://material.io/blog/android-material-theme-color)
[Building a Material Theme on Android: Typography](https://material.io/blog/android-material-theme-type)
[Building a Material Theme on Android: Shape](https://material.io/blog/android-material-theme-shape)

## Dark Theme

![Light and dark versions of a stats app using baseline Material purple.](<https://lh3.googleusercontent.com/9Y1NM2YQyI9p_tx4bzGuQr-bAGxfGbQJM7vzV3Ah2C82si22V8BisTafAXkex6hKxzPMQZorwJ207dxeny6MyWNkgDlebH1XvjwO>)

MDC widgets implement Material’s [dark theme guidance](https://material.io/design/color/dark-theme.html), with many widgets adapting their colors to dark themes and offering elevation overlays to communicate elevation when shadows don’t read.

Chris Banes goes in depth about MDC's dark theme support in [another post](https://material.io/blog/android-dark-theme-tutorial).

## Material Motion

![Various screens showing grids, lists and details, each animating to demonstrate container transform, shared axis, fade through, and fade transitions.](<https://lh3.googleusercontent.com/HcUay0xLeUoYBEDScQ61lMuC9bTlWCPWagTZHqtH_0vyobpouF_yZAgb-T_LAwMpNir1oSmIjMXQI4ChGfEM6eNdyFCvmY9ThZSA>)

Material Design provides [guidance](https://material.io/design/motion/the-motion-system.html) for screen transitions. Better than guidance alone, MDC now implements these patterns as Transitions ready to be dropped into your app. Hunter Stich introduces you to the material motion library in [this post](https://material.io/blog/android-material-motion).

## Get Composed

[Jetpack Compose](https://developer.android.com/jetpack/compose) is Android’s next-generation UI toolkit, currently in alpha. While it is not yet stable, it will offer an implementation of material components and material theming. Adopting MDC now will prepare your codebase for later adopting Jetpack Compose — it uses the same concepts, design vocabulary and components. It even becomes possible to ease your migration with libraries like [MDC-Android Compose Theme Adapter](https://github.com/material-components/material-components-android-compose-theme-adapter) which converts an MDC XML theme into a Compose [MaterialTheme](https://developer.android.com/reference/kotlin/androidx/compose/material/package-summary#materialtheme).

## Material Recommendation

Hopefully it’s clear to see why we recommend building Android UI with [Material Components for Android](https://github.com/material-components/material-components-android). We’ve recently updated the `File > New Project` template in Android Studio to use MDC and set up a material theme for you, making it even easier to get started. If you haven’t already migrated to MDC, then check out [our migration guide](https://material.io/blog/migrate-android-material-components).

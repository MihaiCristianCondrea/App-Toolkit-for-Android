---
title: "Material Components for Android 1.2.0"
source_url: "https://m3.material.io/blog/android-stable-release-1-2"
lastmod: "2024-04-16"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "blog"
assets:
  - "assets/images/e0e7d39cfd3f-kMVUmItZ96jyShlKNdRVPf3Xpt9BKDRMxc919C2imRjLozwk8fWO4jaAoe4_fAGzDZ0gyP5tZaNSqr-IKQbBwzShHyKR2BD2.png"
  - "assets/images/24a0cd0cf2d2-vN3rn-qxpd5mZAsxqq43fC088mMCtYFjOiCo1Ig-LFR6hEWlD44pna0YaOG2LIMZ9Fkf5xRQ1D5iN9p8c-d-9rdCH_QjKChd.png"
  - "assets/images/98ec941ad92e-fI5NTbHaTtm72CwrxbkMBgfUqiHNm9D7K-SsUSsrvP4sTVCuIv7stIn-yrruWnIeOKM0ClAJqp6wjxmE9GTGTqHa2Jk1a3dh.png"
  - "assets/images/972b98de4b82-VeQa8SnPQaKm_VvdkajEdiv4coIUGnhW-YKILlGRXnTyhaIbdTAcKT2fG7VOO3uwAe4MSOz8BDCxS7b-JFDA4qHwfOUrh7dU.png"
---

# Material Components for Android 1.2.0

Material motion system, Sliders, ShapeableImageView, and more

![Illustrated UI frames with Android bots](<https://lh3.googleusercontent.com/kMVUmItZ96jyShlKNdRVPf3Xpt9BKDRMxc919C2imRjLozwk8fWO4jaAoe4_fAGzDZ0gyP5tZaNSqr-IKQbBwzShHyKR2BD2Etru>)

Published: 2020-08-04T13:00:00

We’re excited to announce the release of Material Components for Android (MDC-Android) `1.2.0`! A host of exciting new features have been added along with many bug fixes and accessibility improvements. Get the rundown below.

Be sure to check out the [release notes](https://github.com/material-components/material-components-android/releases/tag/1.2.0). If you’re using MDC for the first time, also take a look at our [getting started guide](https://material.io/develop/android/docs/getting-started).

## What’s new in 1.2.0?

A fair bit has changed since we launched `[1.1.0](https://medium.com/google-design/material-design-components-for-android-1-1-0-are-now-available-45e1d576037c)` in February — we added the motion system, slider component, a widget for image shape theming, and more. The things you loved from the alpha, beta, and RC releases of `1.2.0` are now officially stable. If you haven’t yet started using version `1.2.0` of MDC, there’s never been a better time to update.

![Image](<https://lh3.googleusercontent.com/vN3rn-qxpd5mZAsxqq43fC088mMCtYFjOiCo1Ig-LFR6hEWlD44pna0YaOG2LIMZ9Fkf5xRQ1D5iN9p8c-d-9rdCH_QjKChdIe1M>)

## Material motion

[Material’s motion system](https://material.io/design/motion/the-motion-system.html) includes a set of four transition patterns. They can help users understand and navigate an app, and reinforce relationships between components or full-screen views. The transition patterns are:

-   [Container transform](https://material.io/design/motion/the-motion-system.html#container-transform)
-   [Shared axis](https://material.io/design/motion/the-motion-system.html#shared-axis)
-   [Fade through](https://material.io/design/motion/the-motion-system.html#fade-through)
-   [Fade](https://material.io/design/motion/the-motion-system.html#fade)

MDC `1.2.0` enables Material motion in your Android app. The four transition patterns are implemented as classes built on top of both the AndroidX Transition library (`androidx.transition`) (`androidx.transition`), available in the `com.google.android.material.transition package`, and the Android Framework Transition library (`android.transition`), available in the `com.google.android.material.transition.platform` package. They can be used to transition between Fragments (including [Jetpack Navigation](https://developer.android.com/guide/navigation)), Activities, and Views.

Container transform between Fragments (using Jetpack Navigation)

Shared Z-axis between Fragments

Fade through between Fragments

Fade a target View (using `TransitionManager`)

Learn more about [how to implement motion for Android](https://material.io/develop/android/theming/motion).

![Baseline slider component with indicator moving from 37 to 89](<https://lh3.googleusercontent.com/fI5NTbHaTtm72CwrxbkMBgfUqiHNm9D7K-SsUSsrvP4sTVCuIv7stIn-yrruWnIeOKM0ClAJqp6wjxmE9GTGTqHa2Jk1a3dh6kBd8g>)

## Slider

[Sliders](https://material.io/components/sliders) allow users to make selections from a range of values. They are ideal for adjusting settings such as volume, brightness, or applying image filters.

MDC `1.2.0` allows you to use sliders in your Android app with the `[Slider](https://github.com/material-components/material-components-android/blob/master/lib/java/com/google/android/material/slider/Slider.java)` and `[RangeSlider](https://github.com/material-components/material-components-android/blob/master/lib/java/com/google/android/material/slider/RangeSlider.java)` widgets. They’re similar to `SeekBar` but have additional features and support [Material Theming](https://material.io/design/material-theming/overview.html).

Learn more about [how to implement sliders](https://material.io/develop/android/components/slider).

![9 shapes illustrating the range of options from very rounded to slightly rounded, sharp, and cut](<https://lh3.googleusercontent.com/VeQa8SnPQaKm_VvdkajEdiv4coIUGnhW-YKILlGRXnTyhaIbdTAcKT2fG7VOO3uwAe4MSOz8BDCxS7b-JFDA4qHwfOUrh7dUepfgb3g>)

## ShapeableImageView

The new `[ShapeableImageView](https://github.com/material-components/material-components-android/blob/master/lib/java/com/google/android/material/imageview/ShapeableImageView.java)` widget is an extension of `AppCompatImageView` which understands shape theming. A common use case is to apply circular clips to rectangular source images. However, it also supports varying corner sizes, cut corners, as well as different stroke widths and colors.

## MaterialColors

The `[MaterialColors](https://github.com/material-components/material-components-android/blob/master/lib/java/com/google/android/material/color/MaterialColors.java)` utility class was made public in MDC `1.2.0`. It gives you a variety of useful static methods to use when dealing with colors programmatically in your app.

## Support for materialThemeOverlay in all components

The `materialThemeOverlay` attribute allows you to apply [theme overlays](https://medium.com/androiddevelopers/android-styling-themes-overlay-1ffd57745207). Unlike `android:theme`, it can be used in default component styles (e.g., `materialButtonStyle`). By default it can only be used with MDC; full support for all components was added in `1.2.0`. You can use `[MaterialThemeOverlay#wrap](https://github.com/material-components/material-components-android/blob/master/lib/java/com/google/android/material/theme/overlay/MaterialThemeOverlay.java#L61)` to add this functionality to custom views.

## MaterialButton respects android:background

`[MaterialButton](https://github.com/material-components/material-components-android/blob/master/lib/java/com/google/android/material/button/MaterialButton.java)` previously ignored custom background drawables applied with `android:background`. This has been fixed in MDC `1.2.0`. A `[MaterialShapeDrawable](https://github.com/material-components/material-components-android/blob/master/lib/java/com/google/android/material/shape/MaterialShapeDrawable.java)` will still be used as the default background if no custom background is set.

_Note: The default style for `MaterialButton` includes a `backgroundTint` which will also be applied to a custom background drawable. You may need to change this to a different color or set it as `@empty` to remove it._

## What’s next for MDC?

The next feature release of MDC— `1.3.0` — is well underway with multiple [alpha releases](https://github.com/material-components/material-components-android/releases/) out at the time of writing. Exciting new updates include `[ProgressIndicator](https://github.com/material-components/material-components-android/blob/master/docs/components/ProgressIndicator.md)`, `[MaterialTimePicker](https://github.com/material-components/material-components-android/blob/master/lib/java/com/google/android/material/timepicker/MaterialTimePicker.java)`, and various improvements to existing components. As always, we encourage you to file [bug reports](https://github.com/material-components/material-components-android/issues/new?assignees=&labels=bug&template=bug_report.md&title=%5BComponent+name%5D+Short+description+of+issue) and [feature requests](https://github.com/material-components/material-components-android/issues/new?assignees=&labels=feature+request&template=feature_request.md&title=%5BComponent+name%5D+Short+description+of+request) on GitHub. Also be sure to check out our Android [example apps](https://github.com/material-components/material-components-android-examples) and how to [Build a Material Theme](https://material.io/resources/build-a-material-theme/).

We highly encourage trying out MDC `1.2.0`. If you’re using its features in your Android app, reach out to us on Twitter [@materialdesign](https://twitter.com/materialdesign). We’d love to see it.

---
title: "Material Design Components for Android 1.3.0"
source_url: "https://m3.material.io/blog/android-stable-release-1-3-0"
lastmod: "2024-12-26"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "blog"
assets:
  - "assets/images/453f0804fe69-jYzl7MY0Jk0KTZuzu9TCyH7c_5exgEQhOSUqDtfy2jiwHgDoR3oL1BuBOxeoGlblrsRdWnEnZexFyxeVOzD4eeKxMoi-zCLg.png"
  - "assets/images/0e3ceea80a98-Nvg43GoQdU1BBQh9PPTDaCqb3-XzbzCqvYvVDekL_d3NekjQLvQXZhqavPKqRZoEbyAr7GZt5qPZ33VS9kd_z33OLgl7v6L1.png"
  - "assets/images/06e545d75ba1-D18QEw8kvf3p30wikbp6QbOFAUH7_Evlf-fa1YdutNgNRUfFveQEr9lIIcaKX-rWdUnl2PL_p8tygMlXXL7yJSHDK-fg9oIf.png"
---

# Material Design Components for Android 1.3.0

ProgressIndicator, MaterialTimePicker, i18n / l10n, and more

![illustration showing Material time picker and app bar](<https://lh3.googleusercontent.com/jYzl7MY0Jk0KTZuzu9TCyH7c_5exgEQhOSUqDtfy2jiwHgDoR3oL1BuBOxeoGlblrsRdWnEnZexFyxeVOzD4eeKxMoi-zCLgFUN2QQ>)

Published: 2021-02-18T13:00:00

We’re excited to announce the release of Material Design Components (MDC) 1.3.0! A host of exciting new features have been added along with many bug fixes and accessibility improvements. Get the rundown below.

Be sure to check out the [release notes](https://github.com/material-components/material-components-android/releases/tag/1.3.0). If you’re using MDC for the first time, also take a look at our [getting started guide](https://material.io/develop/android/docs/getting-started).

## What’s new in 1.3.0?

A fair bit has changed since we launched [1.2.1](https://material.io/blog/android-stable-release-1-2) in September last year – we added the progress indicator and time picker components, made extensive internationalization and localization string updates, and more. The things you loved from the alpha, beta, and RC releases of 1.3.0 are now officially stable. If you haven’t yet started using 1.3.0 of MDC, there’s never been a better time to update.

![GIF of linear and circular progress indicators](<https://lh3.googleusercontent.com/Nvg43GoQdU1BBQh9PPTDaCqb3-XzbzCqvYvVDekL_d3NekjQLvQXZhqavPKqRZoEbyAr7GZt5qPZ33VS9kd_z33OLgl7v6L1qfHG-g>)

## ProgressIndicator

[Progress indicators](https://material.io/components/progress-indicators) inform users about the status of ongoing processes, such as loading an app, submitting a form, or saving updates. They communicate an app’s state and indicate available actions, such as whether users can navigate away from the current screen.

MDC 1.3.0 allows you to use progress indicators in your Android app with the `[LinearProgressIndicator](https://github.com/material-components/material-components-android/blob/master/lib/java/com/google/android/material/progressindicator/LinearProgressIndicator.java)`and [`CircularProgressIndicator`](https://github.com/material-components/material-components-android/blob/master/lib/java/com/google/android/material/progressindicator/CircularProgressIndicator.java) widgets. They’re similar to `ProgressBar` but have additional features and support [Material Theming](https://material.io/design/material-theming/overview.html).

Learn more about how to implement progress indicators in [our documentation](https://material.io/components/progress-indicators/android).

![Illustration of a Material time picker](<https://lh3.googleusercontent.com/D18QEw8kvf3p30wikbp6QbOFAUH7_Evlf-fa1YdutNgNRUfFveQEr9lIIcaKX-rWdUnl2PL_p8tygMlXXL7yJSHDK-fg9oIfGPI0OXU>)

## MaterialTimePicker

Time pickers allow users to enter a specific time value. They can be used for a wide range of scenarios such as setting an alarm or scheduling a meeting. Mobile time pickers are displayed in dialogs and can be used to select hours, minutes, and a period of time.

MDC 1.3.0 allows you to use time pickers in your Android app with the `MaterialTimePicker` class, which extends `DialogFragment`. It’s similar to `TimePicker` but has additional features and input methods, and supports [Material Theming](https://material.io/design/material-theming/overview.html).

_**Note:** MaterialTimePicker can be used with the Navigation component using the_ _`&lt;dialog&gt;` tag. Learn more about navigating to dialog destinations here._

Learn more about how to implement time pickers in [our documentation](https://material.io/components/time-pickers/android).

## Internationalization and localization

Certain MDC components include strings as part of their anatomy—think of the “Choose a date” title and “OK” button in date pickers. Additionally there might be built-in strings for accessibility so that screen readers can announce actions and content descriptions.

As part of MDC 1.3.0 these strings have been translated for all languages supported by Android! The affected components include [date picker](https://github.com/material-components/material-components-android/commit/af375eb31a0037a6b30bc3e7ba43f2b5eee83de8), [time picker](https://github.com/material-components/material-components-android/commit/c1a290edb97ae9fc6c47e99df05e39bbe3099df8), [bottom sheet](https://github.com/material-components/material-components-android/commit/9d1d9773cd9418bdef740fa67ae4a2da4436f7bc), [text field](https://github.com/material-components/material-components-android/commit/467bb88650ab9c54463c58f4eb7ece75fe60d4b4), [chips](https://github.com/material-components/material-components-android/commit/0845dfe0d143f77be75b67e09b22950546b17bcf), [badges](https://github.com/material-components/material-components-android/commit/23018e1fbfa4587a23a8465d1b6b0dd4941bcd54), and [dialogs](https://github.com/material-components/material-components-android/commit/cf3c091c7a9bac003650ad12c419682b1e9f0f9e).

## BadgeUtils

The [`BadgeUtils`](https://github.com/material-components/material-components-android/blob/master/lib/java/com/google/android/material/badge/BadgeUtils.java) utility class was made public in MDC 1.3.0. It gives you a variety of useful static methods to use when dealing with badges in custom `View`s. New methods were also added for adding badges to `Toolbar` menu items.

_**Note:** `BadgeUtils` is considered experimental and the APIs are subject to change. You’ll need to use the `@ExperimentalBadgeUtils` annotation when accessing this class._

Learn more about how to implement badges in [our documentation](https://material.io/develop/android/supporting/badge).

## Material Motion updates

MDC transitions received a number of updates to improve stability as well as compatibility with all Android API levels. In particular there are a couple of notable changes for `MaterialContainerTransform`.

We now recommend specifying a target for `View`\-to-`View` container transform transitions to ensure they only run on a single `View`. We’ve added this to [our documentation](https://github.com/material-components/material-components-android/commit/76cba45d62f38947f59914cab7ef070660624ada).

An exciting addition is that `MaterialContainerTransform` [now supports additional interpolators](https://github.com/material-components/material-components-android/commit/ad1d1730180c850330949a7187a14ef402378e88)—`OvershootInterpolator`, `AnticipateOvershootInterpolator`, `BounceInterpolator`, and any subclasses.

Learn more about how to implement motion in [our documentation](https://material.io/develop/android/theming/motion).

## … and more

A variety of smaller features and bug fixes made it into this release. Check out the [releases page](https://github.com/material-components/material-components-android/releases) as well as the [diff between 1.2.1 and 1.3.0](https://github.com/material-components/material-components-android/compare/1.2.1...1.3.0) for a full list.

Here are some of the highlights:

-   [Show/hide `Slider` tick marks in discrete mode](https://github.com/material-components/material-components-android/commit/5fb796437291ce6b7d43b3beba13a42256db6eac)
-   [Shape theming in `NavigationView`](https://github.com/material-components/material-components-android/commit/e6f05bca7e32751e3d480427912bfd9895e40514)
-   [A getter method to check if an `AppBarLayout` is lifted](https://github.com/material-components/material-components-android/commit/4a0f60c62d5acb29841540d16ab24c313df7c55b)
-   [Elevation overlay support in `Chip`s](https://github.com/material-components/material-components-android/commit/6951bd517940dd3d2a2f9d15370f3a046443d260)
-   [Initial show animation in `ExtendedFloatingActionButton`](https://github.com/material-components/material-components-android/commit/5c83026c176e291157c2cf3ca4cfc9cb76f12820)
-   [Flipped password icon behavior in `TextInputLayout`](https://github.com/material-components/material-components-android/commit/99c6a28b104b804694a9b3225208cac2bc326c40)
-   [Updated `BottomNavigationView` menu item selection animation to match spec](https://github.com/material-components/material-components-android/commit/8ec11a1460f59cb4e6e60f67af76d0d1c90f812a)
-   [Updated `Snackbar` dimensions to match spec](https://github.com/material-components/material-components-android/commit/391d1f8ac5d560b1e97c67e129967bbe88cc6163)
-   [Support for content padding in `ShapeableImageView`](https://github.com/material-components/material-components-android/commit/c4f7de167f21d92265a39a21368beeda45c02e05)
-   [Better support for system gesture nav insets in `BottomSheetBehavior`](https://github.com/material-components/material-components-android/commit/0df77248d508ee59d0f2d9966e88f08f73a89610)

## What’s next for MDC?

The next feature release of MDC—1.4.0—is expected to land in alpha soon! Exciting new updates include [`NavigationRailView`](https://github.com/material-components/material-components-android/blob/master/lib/java/com/google/android/material/navigationrail/NavigationRailView.java), [motion theming](https://github.com/material-components/material-components-android/commit/186d5ed023a69176af3bcd0f69c635cf60285bba), and various improvements to existing components. As always, we encourage you to file [bug reports](https://github.com/material-components/material-components-android/issues/new?assignees=&labels=bug&template=bug_report.md&title=%5BComponent+name%5D+Short+description+of+issue) and [feature requests](https://github.com/material-components/material-components-android/issues/new?assignees=&labels=feature+request&template=feature_request.md&title=%5BComponent+name%5D+Short+description+of+request) on GitHub. Also be sure to check out our Android [example apps](https://github.com/material-components/material-components-android-examples) and [Build a Material Theme](https://material.io/resources/build-a-material-theme/).

We highly encourage trying out MDC 1.3.0. If you’re using its features in your Android app, reach out to us on Twitter [@materialdesign](https://twitter.com/materialdesign). We’d love to see it.

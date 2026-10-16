---
title: "Jetpack Compose: Now in Beta"
source_url: "https://m3.material.io/blog/jetpack-compose-beta"
lastmod: "2024-04-15"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "blog"
assets:
  - "assets/images/8c410e1abc33-IGVvFnjHRRbphKVi5QL8VrdI1ou4bgUIdWpt0CCHXweVBIwydrpun88kL73-kFg0UeGL2mxwxv6K1EIVIBn4IYiV_2bWT-85.png"
  - "assets/images/5fad811c2fe5-vLxDu0mrTDm234ff2xoxEKAG2xSDBTYByQ9TF8LyulfKL5nGB7z7PHVrKu3kRoJHj0OPGQHMTYT4_B0ck3XbuCcYETBS1waV.png"
  - "assets/images/56ddde525b18-x4KQQg07DrfuqIaz3E2XwAROxv8uIl202z1IXXV0o0ew5lFwpgZiBAQW_WxQwFELX3DSj8DANWGQHDGyFW_P9mWfk05u1JSi.png"
  - "assets/images/e843d1dce263-hbc3QhQITj-iic8Ju7WYvvWGIY7XSuM5SU0ipdy2xtlypGna4sYYMKTpXAPElpMH8Ho-6hWUmxbolCpWAGlxELNQ3CRS6mB1.png"
---

# Jetpack Compose: Now in Beta

Exploring the first beta release of Android’s modern, declarative toolkit for UI development

![abstract illustration](<https://lh3.googleusercontent.com/IGVvFnjHRRbphKVi5QL8VrdI1ou4bgUIdWpt0CCHXweVBIwydrpun88kL73-kFg0UeGL2mxwxv6K1EIVIBn4IYiV_2bWT-853MCB>)

Published: 2021-02-24T09:30:00

Today marks the first [beta release](http://goo.gle/Compose-Beta-Blog) of [Jetpack Compose](https://developer.android.com/jetpack/compose), Android’s modern, declarative toolkit designed to simplify and accelerate UI development. With Jetpack Compose, you can quickly bring your apps to life with less code, powerful tools, intuitive Kotlin APIs, and built-in support for Material Design, dark theme, animations, and more.

## Compose Material

Jetpack Compose offers an implementation of Material Design and [provides](https://developer.android.com/jetpack/androidx/releases/compose-material) all the components you need to create beautiful apps, following the guidance described at [material.io.](https://material.io)

## Material Theming

Jetpack Compose implements [Material Theming](https://material.io/design/material-theming/overview.html) and supports [dark theme](https://material.io/design/color/dark-theme.html) by default. You can customize color, typography, and shape theming values to fit your product's brand, and get access to convenient functions for working with [system dark theme](https://developer.android.com/guide/topics/ui/look-and-feel/darktheme) (like [`isSystemInDarkTheme`](https://developer.android.com/reference/kotlin/androidx/compose/foundation/package-summary#issystemindarktheme), [`lightColors`](https://developer.android.com/reference/kotlin/androidx/compose/material/package-summary#lightcolors), and [`darkColors`](https://developer.android.com/reference/kotlin/androidx/compose/material/package-summary#darkcolors)).

![GIF showing a Material Design button changing color and shape](<https://lh3.googleusercontent.com/vLxDu0mrTDm234ff2xoxEKAG2xSDBTYByQ9TF8LyulfKL5nGB7z7PHVrKu3kRoJHj0OPGQHMTYT4_B0ck3XbuCcYETBS1waVNWVuUA>)

When creating new screens in Compose, you should ensure that you apply your custom `MaterialTheme` _before_ any UI-emitting Material composables. The Material components (`Button`, `Checkbox`, `BottomNavigation`, etc.) depend on a `MaterialTheme` being in place and their behavior is undefined without it.

Check out the [Theming in Compose guide](https://developer.android.com/jetpack/compose/themes) for more information, and try the [Jetpack Compose Theming codelab](https://developer.android.com/codelabs/jetpack-compose-theming).

## Material Components

Jetpack Compose offers implementations of [Material Components](https://material.io/components). See the table below for composables available in the beta release, or check out the full list in the [API reference](https://developer.android.com/reference/kotlin/androidx/compose/material/package-summary).

| [App bars: bottom](https://material.io/components/app-bars-bottom) | [`BottomAppBar`](https://developer.android.com/reference/kotlin/androidx/compose/material/package-summary#bottomappbar) |
| --- | --- |
| [App bars: top](https://material.io/components/app-bars-top) | [`TopAppBar`](https://developer.android.com/reference/kotlin/androidx/compose/material/package-summary#topappbar) |
| [Backdrop](https://material.io/components/backdrop) | [`Backdrop`](https://developer.android.com/reference/kotlin/androidx/compose/material/package-summary#backdropscaffold) |
| [Banners](https://material.io/components/banners) | _Not available yet_ |
| [Bottom navigation](https://material.io/components/bottom-navigation) | [`BottomNavigation`](https://developer.android.com/reference/kotlin/androidx/compose/material/package-summary#bottomnavigation) |
| [Buttons](https://material.io/components/buttons) | [`Button`](https://developer.android.com/reference/kotlin/androidx/compose/material/package-summary#button) [`OutlinedButton`](https://developer.android.com/reference/kotlin/androidx/compose/material/package-summary#outlinedbutton) [`TextButton`](https://developer.android.com/reference/kotlin/androidx/compose/material/package-summary#textbutton) |
| [Buttons: floating action button](https://material.io/components/buttons-floating-action-button) | [`FloatingActionButton`](https://developer.android.com/reference/kotlin/androidx/compose/material/package-summary#floatingactionbutton) [`ExtendedFloatingActionButton`](https://developer.android.com/reference/kotlin/androidx/compose/material/package-summary#extendedfloatingactionbutton) |
| [Cards](https://material.io/components/cards) | [`Card`](https://developer.android.com/reference/kotlin/androidx/compose/material/package-summary#card) |
| [Checkboxes](https://material.io/components/checkboxes) | [`Checkbox`](https://developer.android.com/reference/kotlin/androidx/compose/material/package-summary#checkbox) [`TriStateCheckbox`](https://developer.android.com/reference/kotlin/androidx/compose/material/package-summary#tristatecheckbox) |
| [Chips](https://material.io/components/chips) | _Not available yet_ |
| [Data tables](https://material.io/components/data-tables) | _Not available yet_ |
| [Date pickers](https://material.io/components/date-pickers) | _Not available yet_ |
| [Dialogs](https://material.io/components/dialogs) | [`AlertDialog`](https://developer.android.com/reference/kotlin/androidx/compose/material/package-summary#alertdialog) |
| [Dividers](https://material.io/components/dividers) | [`Divider`](https://developer.android.com/reference/kotlin/androidx/compose/material/package-summary#divider) |
| [Image lists](https://material.io/components/image-lists) | _Not available yet_ |
| [Lists](https://material.io/components/lists) | [`ListItem`](https://developer.android.com/reference/kotlin/androidx/compose/material/package-summary#listitem) |
| [Menus](https://material.io/components/menus) | [`DropdownMenu`](https://developer.android.com/reference/kotlin/androidx/compose/material/package-summary#dropdownmenu) [`DropdownMenuItem`](https://developer.android.com/reference/kotlin/androidx/compose/material/package-summary#dropdownmenuitem) |
| [Navigation drawer](https://material.io/components/navigation-drawer) | [`ModalDrawerLayout`](https://developer.android.com/reference/kotlin/androidx/compose/material/package-summary#modaldrawerlayout) [`BottomDrawerLayout`](https://developer.android.com/reference/kotlin/androidx/compose/material/package-summary#bottomdrawerlayout) |
| [Navigation rail](https://material.io/components/navigation-rail) | _Not available yet_ |
| [Progress indicators](https://material.io/components/progress-indicators) | [`CircularProgressIndicator`](https://developer.android.com/reference/kotlin/androidx/compose/material/package-summary#circularprogressindicator) [`LinearProgressIndicator`](https://developer.android.com/reference/kotlin/androidx/compose/material/package-summary#linearprogressindicator) |
| [Radio buttons](https://material.io/components/radio-buttons) | [`RadioButton`](https://developer.android.com/reference/kotlin/androidx/compose/material/package-summary#radiobutton) |
| [Sheets: bottom](https://material.io/components/sheets-bottom) | [`BottomSheetScaffold`](https://developer.android.com/reference/kotlin/androidx/compose/material/package-summary#bottomsheetscaffold) [`ModalBottomSheetLayout`](https://developer.android.com/reference/kotlin/androidx/compose/material/package-summary#modalbottomsheetlayout) |
| [Sheets: side](https://material.io/components/sheets-side) | _Not available yet_ |
| [Sliders](https://material.io/components/sliders) | [`Slider`](https://developer.android.com/reference/kotlin/androidx/compose/material/package-summary#slider) |
| [Snackbars](https://material.io/components/snackbars) | [`Snackbar`](https://developer.android.com/reference/kotlin/androidx/compose/material/package-summary#snackbar) [`Scaffold`](https://developer.android.com/reference/kotlin/androidx/compose/material/package-summary#scaffold) |
| [Switches](https://material.io/components/switches) | [`Switch`](https://developer.android.com/reference/kotlin/androidx/compose/material/package-summary#switch) |
| [Tabs](https://material.io/components/tabs) | `[Tab](https://developer.android.com/reference/kotlin/androidx/compose/material/package-summary#tab)` [`TabRow`](https://developer.android.com/reference/kotlin/androidx/compose/material/package-summary#tabrow) [`ScrollableTabRow`](https://developer.android.com/reference/kotlin/androidx/compose/material/package-summary#scrollabletabrow) |
| [Text fields](https://material.io/components/text-fields) | [`TextField`](https://developer.android.com/reference/kotlin/androidx/compose/material/package-summary#textfield) [`OutlinedTextField`](https://developer.android.com/reference/kotlin/androidx/compose/material/package-summary#outlinedtextfield) |
| [Time pickers](https://material.io/components/time-pickers) | _Not available yet_ |
| [Tooltips](https://material.io/components/tooltips) | _Not available yet_ |

These components can be combined—using layouts and composables like `Scaffold`—as the building blocks for beautiful UIs.

Check out the [Layouts in Compose guide](https://developer.android.com/jetpack/compose/layout) for more information.

## Dark theme

Material composables that make use of a `Surface` (like `Card`, `TopAppBar`, etc.) automatically include dark theme properties like desaturated colors for accessibility, elevation overlays, and limited color accents. You can also incorporate these in custom scenarios.

![Illustration of a UI showing light theme and dark theme](<https://lh3.googleusercontent.com/x4KQQg07DrfuqIaz3E2XwAROxv8uIl202z1IXXV0o0ew5lFwpgZiBAQW_WxQwFELX3DSj8DANWGQHDGyFW_P9mWfk05u1JSismQdmQ>)

## Material Icons

Jetpack Compose also provides a convenient means of using icons listed in the [Material Icons tool](https://material.io/resources/icons), including all icon styles—filled, outlined, rounded, two-tone, and sharp. With this artifact, you can use icons directly, without the need to download SVGs and convert them to `VectorDrawable`s in Android Studio.

## Interoperability

Jetpack Compose is designed to work with Android Views. If you're building a new app, the best option might be to implement your entire UI with Compose. But if you're modifying an existing app, you might not want to migrate your app straight away. Instead, you can combine Compose with your existing UI design and adopt it at your own pace.

![Diagram describing the MDC-Android Compose Theme Adapter](<https://lh3.googleusercontent.com/hbc3QhQITj-iic8Ju7WYvvWGIY7XSuM5SU0ipdy2xtlypGna4sYYMKTpXAPElpMH8Ho-6hWUmxbolCpWAGlxELNQ3CRS6mB1UNTvH44>)

If you’re using [Material Components for Android](https://github.com/material-components/material-components-android) in your app—in particular [Material Theming](https://material.io/develop/android/theming/theming-overview)—the [MDC-Android Compose Theme Adapter library](https://github.com/material-components/material-components-android-compose-theme-adapter) allows you to easily re-use the color, typography and shape definitions from your existing XML themes, from within your composables, so you don’t need to declare them again and have a single source of truth.

If you’re using [AppCompat](https://developer.android.com/jetpack/androidx/releases/appcompat), you can access similar functionality by using the `AppCompatTheme` composable from the [AppCompat Compose Theme Adapter library](https://github.com/chrisbanes/accompanist/tree/main/appcompat-theme).

Check out the [Compose interoperability guide](https://developer.android.com/jetpack/compose/interop) for more information.

## Resources

With Jetpack Compose reaching beta, it’s a great time to start learning all about it and get ready to adopt it in your apps. Check out the resources below to get started.

-   Develop knowledge and skills at your own pace with the [Jetpack Compose Pathway](https://developer.android.com/courses/pathways/compose), through sequential learning experiences that include articles, codelabs, quizzes, and videos.
-   Visit the [Jetpack Compose Samples GitHub repository](https://github.com/android/compose-samples), where you’ll find a variety of up-to-date samples including Material Studies like [Owl](https://github.com/android/compose-samples/blob/main/Owl), [Crane](https://github.com/android/compose-samples/blob/main/Crane), and [Rally](https://github.com/android/compose-samples/blob/main/Rally).
-   Get setup using the [Android Studio with Jetpack Compose guide](https://developer.android.com/jetpack/compose/setup).
-   View the [Converting an existing app screen to Jetpack Compose video](https://www.youtube.com/watch?v=7alO6TQDWQQ) on the Material Design YouTube channel.
-   Join the Compose community on [StackOverflow](https://stackoverflow.com/questions/tagged/android-jetpack-compose) and the [Kotlin Slack group](http://slack.kotlinlang.org/).
-   Report an issue and track bugs on the [bug tracker](https://issuetracker.google.com/issues/new?component=612128).

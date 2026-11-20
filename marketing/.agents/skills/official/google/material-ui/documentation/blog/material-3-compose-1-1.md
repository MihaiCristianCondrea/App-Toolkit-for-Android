---
title: "Material Design 3 for Compose gets new components and features"
source_url: "https://m3.material.io/blog/material-3-compose-1-1"
lastmod: "2024-12-26"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "blog"
assets:
  - "assets/images/b19f5cf4e139-luOy6tCtv9Q6bJsVMD4jbQYdk_vBOTeemCcI5Zp2m88X_Z4FtrobQ7prtNkSevt1O-zZwg-vO5VAo-MGwzCG0IOqf-AkGuqr.png"
  - "assets/images/17adfc1e8237-rihApNZWg_567r2XkibXaCbPMUoi4ya6gUUkeFBrLAqERVnfh16hc5DtOWrLsBebFqh6hFGX4farNBP02XhaVV19AR5XfKnb.png"
  - "assets/images/1fec94a8c645-4tEM-mM93iOqZ-4O1JBTarOaF4SiF3nb1nK9hA9mKY33DgLw6RK9t3YRaWhfUUUA9VRdEELyhycc89mqiphANGJdcPyoHhK7.png"
  - "assets/images/aba8e0a40fde-t_lB-9MPlXlLlkTtQG9fKLeqDvBikAK8E8s50o0qt5BWcv42k-qiem7Ga-LqqF-nutSMg0YqXriP3b0QVKp2xJq0UyHk9iFd.png"
  - "assets/images/dfd6becf28a7-qkNHOHz6ZFWossHsS4STzxlmihni9sqWO6M8nzHfrEYu7yeLSM1C2whRQ8W_q4nJj7f7aTQ02wgquCOIQZqVB2xA1woAKmXe.png"
  - "assets/images/81fe10cd7b48-r9uHWAqLRgHwQOQMjSqZlldGdpM82FfenTiPv2f-ZonlSDyY4HSUcA5H79kL74MugaokXB3AvDQv1R-VedaSKozAJMfQ3fZr.png"
  - "assets/images/a20d191bf75f-AAQu2xACSSDqMw8zPEiQhvT9KGCOvAr47gpB8ujE02E_CxyrUojzbX22y5eibklRQbT7inNqtV9ReCo75BMZwrhSFa94Ds9V.png"
  - "assets/images/64f22e22080c-A5BFskNiJtnpu2xJYw9cevEukwyTCVQ1ntR42B7GMBn1r8-lxsMA1CtNlxOrD7-NXTsn9LA28zpsGcyuiiS1O-5gBkie550N.png"
  - "assets/images/7866780518e3-0be1_Ojz3qPiI2xZJj3oirD5dTbt_FMnXqop8u1LvHwTylXo0OIMed-KI076eJCGfjdoj7P84DdM8z9dbQVwBqhMBu-6nsgG.png"
  - "assets/images/184ccfec2707-oaCfOW1cPFvGlLFvokdfBtDlxbx5jkQCJqUZfurMh0so6n6Mg-bGWjgr0uYKKm-vQUmsWzJxMVwqD2pxXYeFLHc7Ql-D9Rwt.png"
  - "assets/images/a5fd87c3f321-Hm0xbN7qc4H49z88n1yv4foM80eieMA5SRQCV88E3gdwjEF6dIUGqdj4mBK7TcaCdNAdTe2eOUALWPBKKY_56DwWrV0ay_Ja.png"
  - "assets/images/3f9d03820b9e-U1gUUb8937W5VphSnJ3N3zp8UWxcHifADhVNdUwm5slE9SCz0B7lP4AuHvR2KmkUyH-TqcJDhtEOlDiyFQtuVpSF_p8VevQo.png"
  - "assets/images/d26dfe2dd539-rb0EIvJyXg44CDwYkDuRtGQ2aHpa8jAkiSbf0ybtFxRW8ufCPSkIyk67sMko4HX1nwzJ8XlsegNeDIQUW-aLUWWy8unmDAQJ.png"
---

# Material Design 3 for Compose gets new components and features

Exploring the 1.1 release of Material Design 3 for Compose

![Animated top app bar](<https://lh3.googleusercontent.com/luOy6tCtv9Q6bJsVMD4jbQYdk_vBOTeemCcI5Zp2m88X_Z4FtrobQ7prtNkSevt1O-zZwg-vO5VAo-MGwzCG0IOqf-AkGuqrSmyOHFTtaiAPBvWkViKN>)

Published: 2023-05-10T13:00:00

The 1.1 release of [Compose Material 3](https://developer.android.com/reference/kotlin/androidx/compose/material3/package-summary) is here, bringing new components, improved APIs, and many other updates and enhancements you’ve been asking us for. Material Design 3 is the next evolution of Material Design, enabling you to build expressive, spirited, and personal apps. Start using [Material Design 3](../index.md) in your production apps today!

_Note: The terms "Material Design 3", "Material 3", and "M3" are used interchangeably._

## New components

With the 1.1 release, Material 3 Compose provides all types of components needed to make your production-ready apps, with additions like bottom sheet, date and time pickers, and many others. Let’s dive into some of these additions.

**Bottom sheets**

There are two variations of bottom sheet available: standard and modal bottom sheet.

The [standard bottom sheet](https://developer.android.com/reference/kotlin/androidx/compose/material3/package-summary#BottomSheetScaffold\(kotlin.Function1,androidx.compose.ui.Modifier,androidx.compose.material3.BottomSheetScaffoldState,androidx.compose.ui.unit.Dp,androidx.compose.ui.graphics.Shape,androidx.compose.ui.graphics.Color,androidx.compose.ui.graphics.Color,androidx.compose.ui.unit.Dp,androidx.compose.ui.unit.Dp,kotlin.Function0,kotlin.Boolean,kotlin.Function0,kotlin.Function1,androidx.compose.ui.graphics.Color,androidx.compose.ui.graphics.Color,kotlin.Function1\)) is great for use cases where you need to interact with both the bottom sheet and main UI region. You can use the standard bottom sheet with BottomSheetScaffold by providing sheet UI in the sheetContent parameter. You can also provide an optional drag handle to easily interact with the sheet.

![Standard bottom sheet](<https://lh3.googleusercontent.com/rihApNZWg_567r2XkibXaCbPMUoi4ya6gUUkeFBrLAqERVnfh16hc5DtOWrLsBebFqh6hFGX4farNBP02XhaVV19AR5XfKnbu3_MkvnLEM0zTSolZj8>)

Standard bottom sheet

The [modal bottom sheet](https://developer.android.com/reference/kotlin/androidx/compose/material3/package-summary#ModalBottomSheet\(kotlin.Function0,androidx.compose.ui.Modifier,androidx.compose.material3.SheetState,androidx.compose.ui.graphics.Shape,androidx.compose.ui.graphics.Color,androidx.compose.ui.graphics.Color,androidx.compose.ui.unit.Dp,androidx.compose.ui.graphics.Color,kotlin.Function0,kotlin.Function1\)) overlays the main UI and can be dismissed when users interact outside of the sheet region, similar to a dialog. It provides clear separation from the main UI, making it easy for users to focus on the sheet content.

![Modal bottom sheet](<https://lh3.googleusercontent.com/4tEM--mM93iOqZ-4O1JBTarOaF4SiF3nb1nK9hA9mKY33DgLw6RK9t3YRaWhfUUUA9VRdEELyhycc89mqiphANGJdcPyoHhK7caQ_9cbjcMwNUR2gGA>)

Modal bottom sheet

You can use the modal bottom sheet as a standalone component and invoke it using `ModalBottomSheetState`. The component also provides `onDismissRequest` function to be invoked on dismiss.

**Date pickers**

The 1.1 release also brings brand new date selection components: [`DatePicker()`](https://developer.android.com/reference/kotlin/androidx/compose/material3/package-summary#DatePicker\(androidx.compose.material3.DatePickerState,androidx.compose.ui.Modifier,androidx.compose.material3.DatePickerFormatter,kotlin.Function1,kotlin.Function0,kotlin.Function0,kotlin.Boolean,androidx.compose.material3.DatePickerColors\)) and [`DateRangePicker()`](https://developer.android.com/reference/kotlin/androidx/compose/material3/package-summary#DateRangePicker\(androidx.compose.material3.DateRangePickerState,androidx.compose.ui.Modifier,androidx.compose.material3.DatePickerFormatter,kotlin.Function1,kotlin.Function0,kotlin.Function0,kotlin.Boolean,androidx.compose.material3.DatePickerColors\)).

Both the date picker and date range picker support date input mode by updating the `initalDisplayMode` parameter to input type, which sets the initial display mode to input type. The user can switch back to default selection mode by toggle.

![Selection of date pickers](<https://lh3.googleusercontent.com/t_lB-9MPlXlLlkTtQG9fKLeqDvBikAK8E8s50o0qt5BWcv42k-qiem7Ga-LqqF-nutSMg0YqXriP3b0QVKp2xJq0UyHk9iFduGXSpYGrCiUFXR89Rw>)

Left: Date picker in input mode, Middle: Date picker in display mode, Right: Date range picker

Compose Material 3 also provides a [`DatePickerDialog()`](https://developer.android.com/reference/kotlin/androidx/compose/material3/package-summary#DatePickerDialog\(kotlin.Function0,kotlin.Function0,androidx.compose.ui.Modifier,kotlin.Function0,androidx.compose.ui.graphics.Shape,androidx.compose.ui.unit.Dp,androidx.compose.material3.DatePickerColors,androidx.compose.ui.window.DialogProperties,kotlin.Function1\)) composable to show a date picker in  a dialog, with the option to confirm or dismiss it.

![Date picker dialog switching between picker and input display mode](<https://lh3.googleusercontent.com/qkNHOHz6ZFWossHsS4STzxlmihni9sqWO6M8nzHfrEYu7yeLSM1C2whRQ8W_q4nJj7f7aTQ02wgquCOIQZqVB2xA1woAKmXewuWNra-xd5CxFYHc_Q>)

Date picker dialog switching between picker and input display mode

**Time pickers**

Compose Material 3 also includes new time picker components for choosing the desired time.

There are two versions available: one with a horizontal layout in which users enter the time using the keyboard, and another with a vertical layout in which users can also use a slide dial to select the time.

![Image](<https://lh3.googleusercontent.com/r9uHWAqLRgHwQOQMjSqZlldGdpM82FfenTiPv2f-ZonlSDyY4HSUcA5H79kL74MugaokXB3AvDQv1R-VedaSKozAJMfQ3fZrydkBXFTnTZ4KevPAo8k>)

Left: Time picker in vertical layout, Right: Time picker in horizontal layout

Compose provides a single [`TimePicker`](https://developer.android.com/reference/kotlin/androidx/compose/material3/package-summary#TimePicker\(androidx.compose.material3.TimePickerState,androidx.compose.ui.Modifier,androidx.compose.material3.TimePickerColors,androidx.compose.material3.TimePickerLayoutType\)) composable function, where you can define the layoutType parameter to choose between a horizontal or vertical layout.

**Search bars**

The 1.1 release brings new search bar components, which provide users the ability to search queries and display dynamic search results.

![Search bar and search result view](<https://lh3.googleusercontent.com/AAQu2xACSSDqMw8zPEiQhvT9KGCOvAr47gpB8ujE02E_CxyrUojzbX22y5eibklRQbT7inNqtV9ReCo75BMZwrhSFa94Ds9VLL-WlxwWTWyeL4fZeNA>)

Search bar and search result view

Material 3 provides two options, [search bar](https://developer.android.com/reference/kotlin/androidx/compose/material3/package-summary#SearchBar\(kotlin.String,kotlin.Function1,kotlin.Function1,kotlin.Boolean,kotlin.Function1,androidx.compose.ui.Modifier,kotlin.Boolean,kotlin.Function0,kotlin.Function0,kotlin.Function0,androidx.compose.ui.graphics.Shape,androidx.compose.material3.SearchBarColors,androidx.compose.ui.unit.Dp,androidx.compose.foundation.layout.WindowInsets,androidx.compose.foundation.interaction.MutableInteractionSource,kotlin.Function1\)) and [docked search bar](https://developer.android.com/reference/kotlin/androidx/compose/material3/package-summary#DockedSearchBar\(kotlin.String,kotlin.Function1,kotlin.Function1,kotlin.Boolean,kotlin.Function1,androidx.compose.ui.Modifier,kotlin.Boolean,kotlin.Function0,kotlin.Function0,kotlin.Function0,androidx.compose.ui.graphics.Shape,androidx.compose.material3.SearchBarColors,androidx.compose.ui.unit.Dp,androidx.compose.foundation.interaction.MutableInteractionSource,kotlin.Function1\)), to give you the flexibility to choose the right fit for your product.

When active, the search bar expands into a search view that displays dynamic results. The active search bar takes up all available space to display the results, and can also expand to full screen.

The docked search bar is great for medium to large devices where the search view doesn’t need to take up all the screen space and results can be shown in a docked view.

![Docked search bar and search view on a tablet](<https://lh3.googleusercontent.com/A5BFskNiJtnpu2xJYw9cevEukwyTCVQ1ntR42B7GMBn1r8-lxsMA1CtNlxOrD7-NXTsn9LA28zpsGcyuiiS1O-5gBkie550NiMrLnOH3ULlZ-2l05Nx2>)

Docked search bar and search view on a tablet

The implementation of the docked search bar is similar to the search bar to keep both the components consistent.

**Tooltips**

Tooltips are another new addition to the latest 1.1 release. Tooltips are informative text labels that provide additional context to a button or other UI element.

Material 3 provides two types of tooltips: [plain tooltip](https://developer.android.com/reference/kotlin/androidx/compose/material3/package-summary#PlainTooltipBox\(kotlin.Function0,androidx.compose.ui.Modifier,androidx.compose.material3.PlainTooltipState,androidx.compose.ui.graphics.Shape,androidx.compose.ui.graphics.Color,androidx.compose.ui.graphics.Color,kotlin.Function1\)) and [rich tooltip](https://developer.android.com/reference/kotlin/androidx/compose/material3/package-summary#RichTooltipBox\(kotlin.Function0,androidx.compose.ui.Modifier,androidx.compose.material3.RichTooltipState,kotlin.Function0,kotlin.Function0,androidx.compose.ui.graphics.Shape,androidx.compose.material3.RichTooltipColors,kotlin.Function1\)).

Plain tooltips briefly describe a UI element. Plain tooltips are great for labeling UI elements with no text, like icon-only and field elements.

![Plain tooltip on an icon button](<https://lh3.googleusercontent.com/0be1_Ojz3qPiI2xZJj3oirD5dTbt_FMnXqop8u1LvHwTylXo0OIMed-KI076eJCGfjdoj7P84DdM8z9dbQVwBqhMBu-6nsgGkqJpR6i44tvdTKPxtf-p>)

Plain tooltip on an icon button

You can use the `PlainTooltipBox()` composable to add a plain tooltip. To apply the tooltip to any component, wrap the component with the tooltip composable and add `Modifier.tooltipAnchor()` to the component’s modifier.

Rich tooltips are great for longer texts like definitions or explanations. Rich tooltips provide additional context to a UI element and can include a button or hyperlink.

![Rich tooltip with description and text button as action](<https://lh3.googleusercontent.com/oaCfOW1cPFvGlLFvokdfBtDlxbx5jkQCJqUZfurMh0so6n6Mg-bGWjgr0uYKKm-vQUmsWzJxMVwqD2pxXYeFLHc7Ql-D9RwtAknu0kRLQgvTvBDjKi8>)

Rich tooltip with description and text button as action

You can have either persistent or non-persistent rich tooltips by providing the isPersistent parameter to `RichTooltipState()`. To dismiss persistent tooltips, you need to tap outside the tooltip area or invoke the dismiss action on tooltip state.

Non-persistent tooltips will be dismissed automatically after a short duration.

To add rich tooltips, you can use the `RichTooltipBox()` composable and modify tooltip state to control the visibility of the tooltip.

## Stable components

With the 1.1 release, many key components have graduated from the experimental stage and are ready for production apps.

Components like Scaffold, Surface, Navigation drawers, and many others are building blocks of your apps, and you can now be assured that they will not have any major breaking changes.

![Navigation rail and Navigation drawer](<https://lh3.googleusercontent.com/Hm0xbN7qc4H49z88n1yv4foM80eieMA5SRQCV88E3gdwjEF6dIUGqdj4mBK7TcaCdNAdTe2eOUALWPBKKY_56DwWrV0ay_Jap4vYt0XtsfWpjeaJJYY>)

Navigation rail and Navigation drawer

When updating to the latest release, you can remove the `@OptIn(ExperimentalMaterial3Api::class)` annotation from the stable components.

## Motion and animations

Material 3 components in Compose come with built-in motion and animations to provide interactive and expressive experiences to users.

Components also provide behavior control to give you flexibility in choosing the animation you need for your product.

**Top app bar motion and animation**

For top app bars, you can choose between pinned scroll behavior or enter always scroll behavior.

In pinned scroll behavior, the top app bar is always pinned at the top, but changes its container color when content is scrolled.

![Top app bar with pinned scroll behavior](<https://lh3.googleusercontent.com/U1gUUb8937W5VphSnJ3N3zp8UWxcHifADhVNdUwm5slE9SCz0B7lP4AuHvR2KmkUyH-TqcJDhtEOlDiyFQtuVpSF_p8VevQonPixlLK7abUHPV05iGF2>)

Top app bar with pinned scroll behavior

To add the behavior to the app bar, define a scroll behavior `TopAppBarDefaults.pinnedScrollBehavior()` that links to the scaffold’s `Modifier.nestedScroll()` to listen to the scrolling changes.

In the enter always scroll behavior, the top app bar disappears when the user scrolls through the content, and shows up again when the user scrolls down.

![Top app bar with enter-always scroll behavior](<https://lh3.googleusercontent.com/rb0EIvJyXg44CDwYkDuRtGQ2aHpa8jAkiSbf0ybtFxRW8ufCPSkIyk67sMko4HX1nwzJ8XlsegNeDIQUW-aLUWWy8unmDAQJKPC4kyi4fiAQUsWfFw>)

Top app bar with enter-always scroll behavior.

To achieve the enter always behavior, define a scroll behavior `TopAppBarDefaults.enterAlwaysScrollBehavior()` that links to the scaffold’s `Modifier.nestedScroll()` to listen to the scrolling changes.

Material 3 provides all these animations as part of the components, with easy ways to customize them for your project.

## Resources

With the latest 1.1 release, Compose Material 3 is ready for production apps. Check out the resources below for more information:

-   Fully Material 3 and Compose sample: [Reply 💌](https://github.com/android/compose-samples/tree/main/Reply)
-   [Theming in Compose](https://developer.android.com/codelabs/jetpack-compose-theming#0) codelab
-   The [Material 3 guidance](https://developer.android.com/jetpack/compose/themes/material3) to start adding Material 3 to your apps
-   The Material 2 to Material 3 [migration guide](https://developer.android.com/jetpack/compose/themes/material2-material3)
-   The [Jetpack Compose Samples GitHub repository](https://github.com/android/compose-samples), where you’ll find a variety of up-to-date samples using Material 3
-   The Compose community on [StackOverflow](https://stackoverflow.com/questions/tagged/material-design) and the [Kotlin Slack group](http://slack.kotlinlang.org/)
-   The [bug tracker](https://issuetracker.google.com/issues/new?component=742043), where you can report an issue and track feature request

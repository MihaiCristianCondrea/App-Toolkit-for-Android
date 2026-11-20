---
title: "Material Design 3 for Compose version 1.3"
source_url: "https://m3.material.io/blog/material-3-compose-1-3"
lastmod: "2026-05-11"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "blog"
assets:
  - "assets/images/c366e8eb9483-DrLnPC8-STsu-5P55vb_gt03HeiUMVfx4k_KPbiGueXrh1XOuuYzAcacYSFVwlwngMK7IO8rqEZnA0Yge84tQpjWnJsLJo9k.png"
  - "assets/images/3e0d389f64e6-DEPT123BmoDhy3YqCUG8pjPYxj7TeLJaQbk2L2XU2SG1pv6CvdV8ZjKZyx8YyJXmnkrHXLkrNvIxlhkKRREEjq9IadiLj4hh.png"
  - "assets/images/85916d200d90-eD3MsG445cg94jaQ-XdCmvlgmpufz1OdpUqm1coFKg4RyBBlK1_u_oIQPYYzfIkYm4VAEtdjuRFh7_NKseJ6Es-E0C18V3ZY.png"
  - "assets/images/ccd0913e3fcd-4Mv5AUboQH_OCRrnI5VMutyelAGAvPcM6ydy7PhgOqhihNr0XcxVJOQZfgp6ep6xRakUPJ7w5kc_iEMyxXAHMXUgcYWafPeO.png"
  - "assets/images/68532d67ab47-jjoVgAzqpayTA72T5Y3mTnR4Ql-5ASIcslSN2ckzWFMzQUncxj7_R4RsZd9yhHLfKX9tJG4D50dQZkD7-C0bf_M5AChp2XG2.png"
  - "assets/images/6dea54c896ee-1OU9xdvZ1ouk40epthZkt3tXGZVQjhrFrsrSQaMWQUHB0NnqG31O9xpNNEVt3r7OHcctqRgdl8ch4KZ8J_UUrpmmtSXtkdYE.png"
  - "assets/images/5fbbd49cbecf-6JQqGQk4PPb1DizJTjNnThLMQZ6AYvXBh920Jwm5D493QB9FPVL4oYoO3h-otGmHdRpntKFJ2nJLPKS9rrhhASGuGTP-t-7d.png"
  - "assets/images/872ce8e354c6-671iyxq3rm67ducjeWnafRj1dWm2LOcmFPdZ2UwJMOdSPRQs4FcOhMinu3B8AY0_QCylAyc3xxEFpHm5SgfQphSi20GS7dVe.png"
  - "assets/images/26aa149c1cf6-b94lGmuynALaLTkcyC-d7x8T0eTb9JC-0qZ0aDrTRGpJFwNXTd119F02BaaaTGdPSEf7ndQ3AV60s91UivV_vDzmqW4aK_d0.png"
  - "assets/images/ae46fb917d07-V_JToogTe7oilw8FmfBShEKDTXv_I0lf1rcaLBr6R5yO5wVChnBdmL1LE-dzlDoWQ5Js7eRwQwUAgRuNnCmTxTtR5oaCSiMO.png"
  - "assets/videos/70d39329e4d5-compose_material_3_v1_3_carousel_header.mp4"
  - "assets/videos/e9a8d6bf71b7-compose_material_3_v1_3_carousel_multi_browse_short.mp4"
  - "assets/videos/43c9ee4a2907-compose_material_3_v1_3_carousel_uncontained_short.mp4"
  - "assets/videos/58615f05cc1b-lpu8t23e-Bottom_Sheet-Back-3P-1.mp4"
---

# Material Design 3 for Compose version 1.3

Exploring the 1.3 release of Material Design 3 for Compose

![Image](<https://lh3.googleusercontent.com/DrLnPC8-STsu-5P55vb_gt03HeiUMVfx4k_KPbiGueXrh1XOuuYzAcacYSFVwlwngMK7IO8rqEZnA0Yge84tQpjWnJsLJo9kzz6ndQ7UJ-TJrE97oRU>)

Published: 2024-09-10T13:00:00

Version 1.3 of the Compose Material 3 Components is here and with it come a few changes for Material Components and theming!

Version 1.3 introduces:

-   Behavior changes for ripple APIs for performance improvements
-   A brand-new component: Carousel
-   Built-in predictive back support for some Material Components.
-   Color token alignment to be more colorful while still having accessible color contrast
-   Previously experimental APIs have been stabilized

## Breaking API changes

To improve performance, the Material components have been migrated to use the new Ripple APIs, and no longer query `RippleTheme`. If you have set a custom ripple in your applications, you will need to perform a migration to use the new APIs as Material will no longer query `LocalRippleTheme`. See the “[Migrate to Indication and Ripple APIs](https://developer.android.com/develop/ui/compose/touch-input/user-interactions/migrate-indication-ripple)” documentation for more information about how to migrate your ripple theme.

## Carousel component

[Video: Video](https://kstatic.googleusercontent.com/files/f642afa49f4a9005de10c8fe3b55346b3df54172b96d7c6d86282577a42010beff6c79d0b7f8ba1d21d69c6ca3c695504e218cc12aabe6f45bc85a1266ffa338)

Material 1.3 introduces two experimental versions of the [Material 3 Carousel](../components/carousel/overview.md) - `HorizontalMultiBrowseCarousel` and `HorizontalUncontainedCarousel`.

The [`HorizontalMultiBrowseCarousel`](https://developer.android.com/reference/kotlin/androidx/compose/material3/carousel/package-summary#HorizontalMultiBrowseCarousel\(androidx.compose.material3.carousel.CarouselState,androidx.compose.ui.unit.Dp,androidx.compose.ui.Modifier,androidx.compose.ui.unit.Dp,androidx.compose.foundation.gestures.TargetedFlingBehavior,androidx.compose.ui.unit.Dp,androidx.compose.ui.unit.Dp,androidx.compose.foundation.layout.PaddingValues,kotlin.Function2\)) shows at least one large, medium, and small carousel item.

![carousel component](<https://lh3.googleusercontent.com/DEPT123BmoDhy3YqCUG8pjPYxj7TeLJaQbk2L2XU2SG1pv6CvdV8ZjKZyx8YyJXmnkrHXLkrNvIxlhkKRREEjq9IadiLj4hh1UUrG6iTgPRcNPP4hg>)

[Video: Video](https://kstatic.googleusercontent.com/files/65ab7ea86de837fd6bfc7171d4350d30d260087f3727ae7d94b3b977010fc3b935487616a8177b331ff04155dc32a0e2dade3a172969648cd551e999987bb628)

The  [`HorizontalUncontainedCarousel`](https://developer.android.com/reference/kotlin/androidx/compose/material3/carousel/package-summary#HorizontalUncontainedCarousel\(androidx.compose.material3.carousel.CarouselState,androidx.compose.ui.unit.Dp,androidx.compose.ui.Modifier,androidx.compose.ui.unit.Dp,androidx.compose.foundation.gestures.TargetedFlingBehavior,androidx.compose.foundation.layout.PaddingValues,kotlin.Function2\))  displays its items at a given size and one item at the end that peeks in to show that there are more items in the list.

![carousel component with equal width items](<https://lh3.googleusercontent.com/eD3MsG445cg94jaQ-XdCmvlgmpufz1OdpUqm1coFKg4RyBBlK1_u_oIQPYYzfIkYm4VAEtdjuRFh7_NKseJ6Es-E0C18V3ZYmtcg5Ek_tdJgl_7_EG3U>)

Similar to using the `HorizontalPager`, the Carousel applies a few design specifications on top of the `Pager`, such as the parallax effect for item content and how the items on each end should size themselves. An example of using the `HorizontalUncontainedCarousel` can be found below:

[Video: Video](https://kstatic.googleusercontent.com/files/6c041e35c36164d0f08f8cf4f79f58d0707fb887d2482b4587496624671235002f8d8c7a8efb29606bd56de5828db7cc515551016c299f24407ff9d0c46a3268)

As part of the Carousel component, we’ve added two new modifiers to `CarouselItemScope` - [maskClip](https://developer.android.com/reference/kotlin/androidx/compose/material3/carousel/CarouselItemScope#\(androidx.compose.ui.Modifier\).maskClip\(androidx.compose.ui.graphics.Shape\)) and [maskBorder](https://developer.android.com/reference/kotlin/androidx/compose/material3/carousel/CarouselItemScope#\(androidx.compose.ui.Modifier\).maskBorder\(androidx.compose.foundation.BorderStroke,androidx.compose.ui.graphics.Shape\)) - to easily add a shape and border to any carousel item. This is needed over the regular `clip` and `border` modifiers to be able to apply it on a container level as the item moves inside the Carousel, if you had to apply the standard `clip` modifier, the items would be statically clipped and not apply the parallax effect that this component introduces.

For example, if you wanted to surround your Carousel items with a border, you can do so by placing the `maskBorder` modifier onto the individual item, as well as provide a different shape to the items with `maskClip`:

![carousel with bordered items](<https://lh3.googleusercontent.com/4Mv5AUboQH_OCRrnI5VMutyelAGAvPcM6ydy7PhgOqhihNr0XcxVJOQZfgp6ep6xRakUPJ7w5kc_iEMyxXAHMXUgcYWafPeOzqATriJhG5_WG7dIV-sT>)

## Component updates

The following components have been updated to match the Material 3 specification, there are also contrast updates to these components to improve accessibility out of the box.

**Progress Indicator**

The [progress indicator](../components/progress-indicators/overview.md) components have been updated to a new visual style to match the Material 3 specification:

![linear and circular progress indicators](<https://lh3.googleusercontent.com/jjoVgAzqpayTA72T5Y3mTnR4Ql-5ASIcslSN2ckzWFMzQUncxj7_R4RsZd9yhHLfKX9tJG4D50dQZkD7-C0bf_M5AChp2XG2DzRqbx_MclLJtiKmqNA>)

1.  [`LinearProgessIndicator`](https://developer.android.com/reference/kotlin/androidx/compose/material3/package-summary#LinearProgressIndicator\(androidx.compose.ui.Modifier,androidx.compose.ui.graphics.Color,androidx.compose.ui.graphics.Color\)) is a straight line progress indicator, the visual style has changed to use rounded `strokeCap` by default, this can be changed with the `strokeCap` parameter. New parameter for specifying the `gapSize` between the progress indicator and track is now available. To change or disable the visual indicator at the end - use the `drawStopIndicator` parameter.
2.  [`CircularProgressIndicator`](https://developer.android.com/reference/kotlin/androidx/compose/material3/package-summary#CircularProgressIndicator\(androidx.compose.ui.Modifier,androidx.compose.ui.graphics.Color,androidx.compose.ui.unit.Dp\)) is a circle shaped progress indicator, the visual style has changed to use rounded `strokeCap`, this can be changed with the `strokeCap` parameter. New parameter for specifying the `gapSize` between the progress indicator and track is now available.

**Slider**

The [slider](../components/sliders/overview.md) component has been updated to a new visual style to match the Material 3 specification, including more prominent handles and an updated shape spec that can be customized:

1.  Continuous slider: `Slider`

    ![continuous slider](<https://lh3.googleusercontent.com/1OU9xdvZ1ouk40epthZkt3tXGZVQjhrFrsrSQaMWQUHB0NnqG31O9xpNNEVt3r7OHcctqRgdl8ch4KZ8J_UUrpmmtSXtkdYEJ59iroXNMJAT2ey_zAE>)

2.  Discrete slider: `Slider` with `steps` parameter set

    ![discrete slider](<https://lh3.googleusercontent.com/6JQqGQk4PPb1DizJTjNnThLMQZ6AYvXBh920Jwm5D493QB9FPVL4oYoO3h-otGmHdRpntKFJ2nJLPKS9rrhhASGuGTP-t-7dW5Z5yaUf8OvtHTItZ8Y>)

3.  Range slider: `RangeSlider` 

    ![range slider](<https://lh3.googleusercontent.com/671iyxq3rm67ducjeWnafRj1dWm2LOcmFPdZ2UwJMOdSPRQs4FcOhMinu3B8AY0_QCylAyc3xxEFpHm5SgfQphSi20GS7dVeIzi9hpRRniLxyrPi20E>)


**Bottom Sheet**

Bottom sheets have been updated to fix bugs and offer improvements to the API. [`ModalBottomSheet`](https://developer.android.com/reference/kotlin/androidx/compose/material3/package-summary#ModalBottomSheet\(kotlin.Function0,androidx.compose.ui.Modifier,androidx.compose.material3.SheetState,androidx.compose.ui.unit.Dp,androidx.compose.ui.graphics.Shape,androidx.compose.ui.graphics.Color,androidx.compose.ui.graphics.Color,androidx.compose.ui.unit.Dp,androidx.compose.ui.graphics.Color,kotlin.Function0,kotlin.Function0,androidx.compose.material3.ModalBottomSheetProperties,kotlin.Function1\)) more accurately draws scrim over status bar when edge to edge is enabled.

Modal bottom sheet content can now consume window insets, allowing for visible content above the navigation bar. `ModalBottomSheet` parameter `windowInsets` is renamed to `contentWindowInsets` to specify where the insets will be applied, and the insets are no longer tied to window logic. In addition, the bottom sheet includes predictive back support.

**Pull to refresh API changes**

The pull to refresh experimental APIs have been overhauled and updated to fix a few bugs with the component. The updated APIs for [`PullToRefreshBox`](https://developer.android.com/reference/kotlin/androidx/compose/material3/pulltorefresh/package-summary#PullToRefreshBox\(kotlin.Boolean,kotlin.Function0,androidx.compose.ui.Modifier,androidx.compose.material3.pulltorefresh.PullToRefreshState,androidx.compose.ui.Alignment,kotlin.Function1,kotlin.Function1\)) simplify the state to use fractional values instead of dp units, and separate out the nested scroll connection from `PullToRefreshState`. A simple example of using pull to refresh:

## Predictive back support

Support for predictive back is now built into some of the Material Components.  Predictive back allows a user to swipe left or right on certain components as a way to navigate to a previous destination. Before completing the swipe, the user can decide to continue to the previous view or stay in the current view, where the component will indicate a preview of where going back would take the user.

[Video: Video](https://kstatic.googleusercontent.com/files/601d3cdba9de5c8790825f1f9edcf6a6ab18a59e11a31700fb822985e494699051a1a986a06f45f210520603fb263972e795810c860c73d180c0ccc123177dee)

The following components will now automatically handle predictive back on devices that support it:

-   `ModalBottomSheet`
-   `SearchBar`
-   `ModalDrawerSheet`
-   `DismissibleDrawerSheet`

## Color Token Alignment / Tonal Surface Colors

[Tone-based surface color roles](https://material.io/blog/tone-based-surface-color-m3) have replaced the previous “surfaces at +1 to +5 elevation” approach. The new color roles are not tied to elevation, and offer more flexibility and support for color features such as user-controlled contrast. All Material Components will **automatically update to use the new surface container color roles**.

For developers using the previous opacity-based surface model for custom color mappings, we recommend remapping these to the new roles:

![updated color mapping diagram](<https://lh3.googleusercontent.com/b94lGmuynALaLTkcyC-d7x8T0eTb9JC-0qZ0aDrTRGpJFwNXTd119F02BaaaTGdPSEf7ndQ3AV60s91UivV_vDzmqW4aK_d0T-12-IcLMjDQvlOnbGk>)

Instead of using `ColorScheme.surfaceColorAtElevation()`, use the following colors instead:

-   `surfaceContainerLowest` is a new role
-   Surface at elevation +1 becomes `surfaceContainerLow`
-   Surface at elevation +2 becomes `surfaceContainer`
-   Surface at elevation +3 becomes `surfaceContainerHigh`
-   Surface at elevation +4 and +5 are being deprecated, it is recommended to use `surfaceContainerHighest` by default as a replacement. As an alternative `surfaceContainerHigh`, or `surfaceDim` can be used depending on the specific use case.
-   Surface Variant becomes `surfaceContainerHighest`

For more information on tone-based surface color roles and the exact changes, see [the blog post](https://material.io/blog/tone-based-surface-color-m3).

In addition to the introduction of tone based surface color roles, the following color roles have been updated in the default `lightColorScheme` to be more colorful while still having accessible color contrast:

-   `onPrimaryContainer`
-   `onSecondaryContainer`
-   `onTertiaryContainer`
-   `onErrorContainer`

![updated color scheme applied to components](<https://lh3.googleusercontent.com/V_JToogTe7oilw8FmfBShEKDTXv_I0lf1rcaLBr6R5yO5wVChnBdmL1LE-dzlDoWQ5Js7eRwQwUAgRuNnCmTxTtR5oaCSiMOdY9qQNoMah5806Ueig>)

Affected components:

-   Badges
-   Bottom app bar
-   Buttons
-   Common buttons
-   Extended FAB
-   FAB
-   Icon buttons
-   Segmented buttons
-   Chips
-   Lists
-   Menus
-   Navigation bar
-   Navigation drawer
-   Navigation rail
-   Switches

Promotion of experimental APIs to Stable

The experimental Material components continue to be evaluated and adjusted based on your feedback and bug reports. In this release, we’ve graduated more APIs and removed the experimental annotations from:

-   `SegmentedButton` and associated APIs are now stable.
-   `SwipeToDismissBox` and associated APIs are now stable.

## Learn more

The components or features that are highlighted in these posts are only a subset of the work that lands in each release. Check out the [release notes](https://developer.android.com/jetpack/androidx/releases/compose-material3) for a full listing of all the changes.

You can file [bug reports](https://issuetracker.google.com/issues/new?component=742043&template=1590761) and follow [open issues](https://issuetracker.google.com/issues?q=componentid:742043%20status:open) on Buganizer. You can also follow the progress of new versions on [cs.android.com](https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:compose/material3/material3/src/commonMain/kotlin/androidx/compose/material3/;bpv=1;bpt=0). Install the [catalog app](https://play.google.com/store/apps/details?id=androidx.compose.material.catalog) on Google play to see the latest components in action.

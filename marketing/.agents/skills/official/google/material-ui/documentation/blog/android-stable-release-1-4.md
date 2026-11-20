---
title: "Material Design Components for Android 1.4.0"
source_url: "https://m3.material.io/blog/android-stable-release-1-4"
lastmod: "2024-12-26"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "blog"
assets:
  - "assets/images/037d789cfa7d-iT2cU1VgnPw3KhRpDdRPCMvy86s4AR-SQxUORT_bCxmGF5UoZG_hezxrAYbEG4If_4VKM75Bo6sEVmyKQatN-b1CBaAyceoP.png"
  - "assets/images/e3a686415ef8-5VzYIwV_8ou2j22qFy-9Oftt5qTt4EcR3pPK25bSBNlI7wx7CYzsDxzliGOyWWk6ClDr7nQ4yrXdGNHw7EnMaE2fbnSWR4YS.png"
  - "assets/videos/eb0f26fbe8c1-material-motion.mp4"
---

# Material Design Components for Android 1.4.0

NavigationRailView, motion theming, and more

![Image](<https://lh3.googleusercontent.com/iT2cU1VgnPw3KhRpDdRPCMvy86s4AR-SQxUORT_bCxmGF5UoZG_hezxrAYbEG4If_4VKM75Bo6sEVmyKQatN-b1CBaAyceoP20WJNSfaId21yi4qWQ>)

Published: 2021-07-07T13:00:00

We’re excited to announce the release of Material Design Components (MDC) 1.4.0! A host of exciting new features have been added along with many bug fixes and accessibility improvements. Get the rundown below.

Make sure to check out the [release notes](https://github.com/material-components/material-components-android/releases/tag/1.4.0), and if you’re using MDC for the first time, take a look at our [getting started guide](https://material.io/develop/android/docs/getting-started).

## What’s new in 1.4.0?

A fair bit has changed since `1.3.0` launched in February – we added the navigation rail component, added support for motion theming, and more. The things you loved from the alpha, beta, and RC releases of `1.4.0` are now officially stable. If you haven’t yet started using `1.4.0` of MDC, there’s never been a better time to update.

![closeup example of a navigation rail](<https://lh3.googleusercontent.com/5VzYIwV_8ou2j22qFy-9Oftt5qTt4EcR3pPK25bSBNlI7wx7CYzsDxzliGOyWWk6ClDr7nQ4yrXdGNHw7EnMaE2fbnSWR4YSAkuTG2MPyIwRx1B83M0>)

#### NavigationRailView

Navigation rails provide ergonomic movement between primary destinations in apps. The rail is a side navigation component that displays three to seven app destinations and, optionally, a Floating Action Button. Each destination is represented by an icon and a text label.

MDC `1.4.0` allows you to use navigation rails in your Android app with the `NavigationRailView` widget. It has a similar API to `NavigationView` but has additional features to support Material Design Guidance for Large Screens.

Learn more about how to implement navigation rails in our [documentation](https://material.io/components/navigation-rail/android).

[Video: example animations from the Material motion library](https://kstatic.googleusercontent.com/files/3f02013896d66b3c549d00dc7a08e945975aa1dbf737716139e1663e84d49ec785e07787bbc8384612d418346513c19a1cf858fc8f905f8004609863b7aa300b)

#### Motion theming

[Material motion](https://material.io/design/motion/the-motion-system.html) provides a set of transition patterns that help users understand and navigate an app.

We introduced material motion in MDC [`1.2.1`](https://material.io/blog/android-stable-release-1-2) and have added many improvements since then. MDC `1.4.0` allows you to specify duration, easing, and path values for the Material motion classes — `MaterialContainerTransform`, `MaterialSharedAxis`, `MaterialFadeThrough`, and `MaterialFade` — with new theme attributes, similar to color, typography, and shape attributes.

Learn more about how to implement motion and how the new theme attributes map to the relevant classes in our [documentation](https://material.io/develop/android/theming/motion).

#### … and more

A variety of smaller features and bug fixes made it into this release. Check out the [releases page](https://github.com/material-components/material-components-android/releases) as well as the [diff](https://github.com/material-components/material-components-android/compare/1.3.0...1.4.0) between `1.3.0` and `1.4.0` for a full list.

Here are some of the highlights:

-   [Updated the way BottomSheet handles insets](https://github.com/material-components/material-components-android/commit/b163458a3a7919d7b7c76de81a7a9b5c940c8def)
-   [Updated "indicatorSize" in CircularProgressIndicatorSpec to be never less than twice of "trackThickness"](https://github.com/material-components/material-components-android/commit/1fb57bcaa274f9de70553184e5eed35daf8153c8)
-   [Updated CollapsingToolbarLayout to support framework android.widget.Toolbar (&lt;Toolbar)](https://github.com/material-components/material-components-android/commit/036cff7c33b6ce285ee5a4d49d2612249e0bce1a)
-   [Adjusted background of collapsed hint to not overlap with edit text's background](https://github.com/material-components/material-components-android/commit/6015a4e901dc55a02f86e12703820d520684f95e)
-   [Localized numbers in MaterialDatePicker month](https://github.com/material-components/material-components-android/commit/3e799428717a8f82b27f753c49f9748175b24121)
-   [Added support for centering MaterialToolbar title and subtitle](https://github.com/material-components/material-components-android/commit/cbf528e3a6deaa2bc39d0836b3a850b27c2ada49)
-   [Added support for CollapsingToolbarLayout fade out/in title collapse mode](https://github.com/material-components/material-components-android/commit/9c75ad07f7323387c7dae59455ea6ebc711b5f12)
-   [Fix badge offset bug when attached and detached from a menu item on a toolbar](https://github.com/material-components/material-components-android/commit/948738e618416783694c9e4691454beb9701eeee)
-   [Fixed issues with MaterialButtonToggleGroup uncheck and check methods](https://github.com/material-components/material-components-android/commit/ffb2ad14ffaeb82901ff31ec79051f3c1ac068a3)
-   [Updated Material Button style to set the preferred maximum width to 320dp](https://github.com/material-components/material-components-android/commit/eb5453cd7ee0cc8e0610b57d39b44b26cd95f31e)

#### What’s next for MDC?

The next feature release of MDC — `1.5.0` — is expected to land in alpha soon! Exciting new updates include [`MaterialDivider`](https://github.com/material-components/material-components-android/blob/master/lib/java/com/google/android/material/divider/MaterialDivider.java) and [`MaterialDividerItemDecoration`](https://github.com/material-components/material-components-android/blob/master/lib/java/com/google/android/material/divider/MaterialDividerItemDecoration.java), and various improvements to existing components. As always, we encourage you to [file bug reports](https://github.com/material-components/material-components-android/issues/new?assignees=&labels=bug&template=bug_report.md&title=%5BComponent+name%5D+Short+description+of+issue) and [feature requests](https://github.com/material-components/material-components-android/issues/new?assignees=&labels=feature+request&template=feature_request.md&title=%5BComponent+name%5D+Short+description+of+request) on GitHub. Also be sure to check out our Android [example apps](https://github.com/material-components/material-components-android-examples) and [Build a Material Theme](https://material.io/resources/build-a-material-theme/).

We highly encourage trying out MDC `1.4.0`. If you’re using its features in your Android app, leave a comment below or reach out to us on Twitter [@materialdesign](https://www.twitter.com/materialdesign). We’d love to see it.

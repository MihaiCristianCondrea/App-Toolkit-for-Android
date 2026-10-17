---
title: "Material Android is Compose-first"
source_url: "https://m3.material.io/blog/material-is-compose-first"
lastmod: "2026-05-19"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "blog"
assets:
  - "assets/images/b19104129b5f-Hu5ftqOU8SihIUjv8_X9Zq85GX32Q2OgWziCcMjxoMTeXtrAfbj8TyDlwRij01gjSL6D3G-o5KNPnoDUswrG7s0hn4SCyBpj.png"
  - "assets/images/aeac3fa29a4c-3x5u5He_G5aM-hAbFOEmjdikmgjpDNQZs3LKWT9itRuF6o3H5PI1n1KR9mN_LiYY7uJvGoTWY_rn7Up0b3R5JOskbizhaUJ3.png"
  - "assets/videos/4211c50ce50e-16x9_Homepage_Hero_050825.mp4"
  - "assets/videos/13d603f9f454-03-Shapes.mp4"
  - "assets/videos/14bfa7586b5a-Shrine-Wide.mov"
  - "assets/videos/50db08217386-05-ADAPTIVE_REFLOW_BUILD_RENDER.mp4"
---

# Material Android is Compose-first

Start migrating to Compose to get the latest from Material

![Image](<https://lh3.googleusercontent.com/Hu5ftqOU8SihIUjv8_X9Zq85GX32Q2OgWziCcMjxoMTeXtrAfbj8TyDlwRij01gjSL6D3G-o5KNPnoDUswrG7s0hn4SCyBpjSVnsPUiL8_qd0XiwL08>)

Published: 2026-05-19T08:00:00

At [Google I/O 2026](https://io.google/2026), Android announced that they’re [officially Compose-first](https://android-developers.googleblog.com/2026/05/android-ui-development-is-compose-first.html), marking a new chapter in Android development. Material Android is excited to announce we’re also going “all-in” on Compose.

Later in 2026, we’ll promote M3 Expressive experimental APIs, making [Material Compose](https://developer.android.com/develop/ui/compose/designsystems/material3) 1.5.0 stable. Additionally, we’ll be integrating the new [Styles API](https://developer.android.com/develop/ui/compose/styles) for easier, more flexible customization, and have already integrated [Navigation3](https://developer.android.com/develop/ui/compose/layouts/adaptive/build-adaptive-navigation) into the Material adaptive library.

To focus on Compose going forward, [Material Views 1.14.0](https://github.com/material-components/material-components-android/releases/tag/1.14.0) (MDC-Android) will be our final stable release for the Views library. There’s never been a better time to migrate to [Jetpack Compose](http://goo.gle/compose-migration).

![Text: Compose Material 3 is going stable.](<https://lh3.googleusercontent.com/3x5u5He_G5aM-hAbFOEmjdikmgjpDNQZs3LKWT9itRuF6o3H5PI1n1KR9mN_LiYY7uJvGoTWY_rn7Up0b3R5JOskbizhaUJ3rB8B6HhFPx1O73vYng>)

## The final Material Views release

For years, Material Design has been on a journey to redefine how to build UI on Android. We’ve seen [Jetpack Compose](https://developer.android.com/compose) grow from an ambitious idea into the engine behind the world’s most beautiful apps. As Android’s modern, declarative toolkit, it’s designed to simplify and accelerate UI development with less code and powerful native tools.

While Compose continues to accelerate, our updates for Views have become more focused. For the last few years, we’ve been preparing for a fundamental shift in Android development.

[Video: A collection of expressive UIs, including media players, loaders, alarms, chat interfaces, and more.](https://kstatic.googleusercontent.com/files/309f40c5040c1df86bc0cbfb81a2f51be5bc61244908d1d4f5c8cb69edb34258dde5112139a8ecdd651f04291654624fd3a864e1b781f1aaf975578586a8c813)

Earlier this month, we reached a major milestone: the stable release of [Material Views 1.14.0](https://github.com/material-components/material-components-android/releases/tag/1.14.0) (MDC-Android). This release officially brings Material 3 Expressive to the Views framework, as well as other improvements, unlocking more delightful experiences for Android makers:

-   Expressive themes
-   Expressive list component
-   Emphasized type scale
-   Expressive styles for 11 existing components, like buttons, sliders, progress indicators, and more

With this release, the Material Views library is now entering **maintenance mode**. It won’t get any new features, but will receive critical bug fixes.

## Going Compose-first

Moving forward, Material Android is transitioning to focus all feature development on the [Material Compose library](https://developer.android.com/develop/ui/compose/designsystems/material3).

Later in 2026, Material Android will release Compose 1.5.0, which will promote M3 experimental APIs to stable, including M3 Expressive. M3 Expressive APIs are like an expansion pack to M3; you can opt in to them to deliver more premium Android experiences. This includes Material 3 Expressive components, motion systems, and more from the Expressive update. [Read about M3 Expressive](building-with-m3-expressive.md)

For designers, the [Material 3 Figma Design Kit](https://www.figma.com/community/file/1035203688168086460) and our [design guidelines](http://m3.material.io) will continue to reflect the latest of what’s available in the Material Compose library.

## Styles API & adaptive navigation

We also announced the upcoming integration of Compose [Styles](https://developer.android.com/develop/ui/compose/styles) and the recently-launched [Adaptive Navigation](https://developer.android.com/develop/ui/compose/layouts/adaptive/build-adaptive-navigation) update [with the new Navigation3 library](https://developer.android.com/guide/navigation/navigation-3).

[Video: As Styles API callout is triggered, Material Components callout changes in shape and color.](https://kstatic.googleusercontent.com/files/74ca942112a8fd1e234c1ac6a3d8437063e3903042fecf1eff3c8eef581f0c6d9809c0312ccce8d449413012c603fbdb2c1970b22aea096245973a95478818ea)

The [Compose Styles API](https://developer.android.com/develop/ui/compose/styles) on Android will make it easier than ever to customize Material components. This will improve overall app performance by skipping the composition phase during style updates and will simplify creating cohesive brand experiences. Integration is ongoing, so stay tuned!

[Video: A navigation bar on mobile is a navigation rail on tablet. The color themes change dynamically using styles.](https://kstatic.googleusercontent.com/files/d0d0945b6d9a4baf0d5d2729b0aa6b1e3d6a9cead413fce337f6a7dab09990f01c313c8d4611db3d1d6011cf1a938ba49500176dafdfe8696d896330870bd8ab)

For a long time, building adaptive layouts for foldables, tablets, and desktops was complex, requiring makers to manage and switch between navigation frameworks throughout the app. To solve this, we’ve deeply integrated the Material Adaptive library with [Navigation3](https://developer.android.com/guide/navigation/navigation-3). Since the navigation library now understands the intent of your screens through metadata, it handles the heavy lifting for you, making implementation much more efficient. [Build adaptive UIs with Material components today](https://developer.android.com/develop/adaptive-apps)

[Video: An adaptive pane layout transitions all panes to the left when a button is selected on the rightmost pane.](https://kstatic.googleusercontent.com/files/ab49db9b1df92f954aed3964996faded190159410bd8f229ab3305c01f81ce0824e78813a117ef560c15f832cb320171f40f74ec2647035f7c32212945ceeb88)

## Migrating from Views to Compose

We know how much you’ve built on Views, but there’s never been a better time to [migrate to Jetpack Compose](http://goo.gle/compose-migration). Get started by migrating your apps screen-by-screen with our [new migration Android skill](https://github.com/android/skills), which you can use with Android Studio, Android CLI, or any other AI tool.

Stay tuned for more updates coming to Material Compose!

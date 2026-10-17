---
title: "Material Design for XR (Developer Preview)"
source_url: "https://m3.material.io/blog/material-design-xr-dev-preview"
lastmod: "2026-05-08"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "blog"
assets:
  - "assets/images/35dab38d49b2-LmC6YZQHA_FFP9yZNFbLYTedVEd0Yg3CGCd4XLkllaafZWYb6paPqED0tEFAx7o-uLwAEX2gBUqnUs5B8I8-XPCbQ5E1pfJ2.png"
  - "assets/images/86cb9f894782-SGb0xm8dlDBTnd2euS-dOVAv_j4O6uk_bCMp7BkQB97dDXQuVn72Dcq3IpOVwjyuMMuHh4RyPzcPR6q4UfgLmPXqjL86G_9N.png"
  - "assets/images/996fba3057ac-Ntu9h5U4fS56NUU75O-hEvs1maLHRESck4dFrtldYJ2zeuAnVuQeXWkXfBxZbqkj84Brp5T_czmDcXIgWLq3iF1mbcES-TRr.png"
  - "assets/images/224fb478361e-aKNVM5nF1ukFod-lQUc0SQF_6yF-T5P6UwyAPkkxNeWhyN0c48m3AkAkjs3a1gYk_x9gzJN40NHd95x1MCw2aDK0IjxK5pYw.png"
  - "assets/images/5d993b99758c-6n2h_icI8q2ESzqESIQNS69kRNmN2ncZdLRdo8oOUO02JbLKLBrZOKRrba_g54FnormKcqZIz-ggW4_mwLjXc8LHWZMteLe-.png"
  - "assets/images/26b8bfeda629-z8haeB4KGw8L0GsKnA1ZmokvtnM_Is3vriHC8SbW48bCzDNURkAjNRVwOUA99P_9BVxGKQ5E_njZRBEh1QEE2mFEPluVkSTH.png"
  - "assets/images/09dafd96add9-oJcnR0EUitzuHwx7o15Pk101VywYBL8aVJsDF-175GTlZ6kUqcJhMW-f8HZ7uoTNZ_i9QWj1OQAtlHbjcfw6EMk_9-KzAYzl.png"
  - "assets/images/c3a74fd6a2fd-oB6Czgh-4EbsCabSbemY9uBHoAQlbONHRz9AOCwXbVzBwL8dW2mgqCikjkVmQIfkS3FWVPvJWZWvnGXWdGPNot4N6UN4Id5F.png"
  - "assets/images/2a47310a481a-UQNZ7IU2o0QU_UdKJsqehLTRQNUDnZS_hOoDkQhu8VnSlUcYuWF7ohD5GH_rBbwA8sUOMb8QsjYu_KhQDw57j_HYkA3meoR4.png"
  - "assets/images/7bfb700dedd9-3SZDiYGeMQ6OcrI_jplqKKUuVdhRlh-e6x-8Of-5BWv-8B7hIOW8w8khkYpubXqArQ5h2MXT06XNKcHN7v8jNvasK1OAMf__.png"
  - "assets/images/eb1a31436558-B-JqNbwGerfkdwPGe_S407ZHnxcWiObalnK0qBRPfDiSaDew2ScsZqYkybNUz961qd7IhADr67eJDxeVIIQ4nYcYfg4IlfMF.png"
  - "assets/images/43d2cb7a33dc-UD_39RYFdzRYFthsxXXgBmjdLOuoT_E3OetgJF9MEHUx1wQxq9aq11NNbeKvdKXQGeFXTzg2Bp8wvJHCW-EeP97_HoNehlL7.png"
---

# Material Design for XR (Developer Preview)

Building UI that adapts for XR with familiar frameworks and tools

![spatialized material design layout](<https://lh3.googleusercontent.com/LmC6YZQHA_FFP9yZNFbLYTedVEd0Yg3CGCd4XLkllaafZWYb6paPqED0tEFAx7o-uLwAEX2gBUqnUs5B8I8-XPCbQ5E1pfJ2usXj8d92AzQ_988aQg8>)

Published: 2024-12-12T13:00:00

Android is expanding into extended reality, so your app can be viewed in 3D space, whether in virtual reality or blended with the user's surroundings through passthrough.

Today, we’re releasing the [Android XR SDK](https://developer.android.com/develop/xr) developer preview, a software development kit to let you build XR apps using familiar Android frameworks and tools. This release also includes [Android XR design guidelines](https://developer.android.com/design/ui/xr) and [developer documentation](https://developer.android.com/develop/xr) to help you create immersive experiences.

The [Jetpack XR SDK](https://developer.android.com/develop/xr/jetpack-xr-sdk) allows you to enhance your existing Android app for XR and see it come to life in the Android XR emulator in [Android Studio](https://developer.android.com/develop/xr/jetpack-xr-sdk/studio-tools). [Jetpack Compose for XR](https://developer.android.com/develop/xr/jetpack-xr-sdk/develop-ui) provides an easy onboarding to declaratively build spatial UI layouts, with established concepts such as rows and columns. Material Design for XR provides design guidance and code for spatial adaptations of components and layouts, to help you prepare your apps for this new environment.

In addition, we are launching advanced perception capabilities with [ARCore for Jetpack XR](https://developer.android.com/develop/xr/jetpack-xr-sdk/work-with-arcore), as well as [SceneCore](https://developer.android.com/develop/xr/jetpack-xr-sdk#jetpack-scenecore), our new 3D scene graph library for custom 3D manipulation.

## Android apps in XR

Android XR is a flexible platform that lets you bring your app into extended reality. [Compatible](https://developer.android.com/develop/xr/get-started#app-manifest) Android mobile and [large-screen apps](https://developer.android.com/docs/quality-guidelines/large-screen-app-quality) will work with no development lift and are automatically available in the Play Store.

If you already have a mobile or large screen app on Android, the Jetpack XR SDK can extend your app into a new dimension by spatializing existing layouts and enhancing your experiences with 3D models and immersive environments. Learn how to [bring your Android app into 3D with XR](https://developer.android.com/develop/xr/jetpack-xr-sdk/add-xr-to-existing).

The [Android XR design guidelines](https://developer.android.com/design/ui/xr/) provide a roadmap for creating exceptional XR experiences. Explore key considerations, system foundations, and best practices for designing interactions, spatial UI, 3D content, and immersive environments. Learn how to [develop UI with Jetpack Compose for XR](https://developer.android.com/develop/xr/jetpack-xr-sdk/develop-ui).

To help you build integrated and boundless experiences, Android XR includes [multimodal inputs](https://developer.android.com/design/ui/xr/guides/foundations#design-multimodal), and [spatial capabilities](http://developer.android.com/design/ui/xr/guides/spatial-ui). Users can experience your app in two modes: [Home Space](https://developer.android.com/design/ui/xr/guides/foundations) and [Full Space](https://developer.android.com/design/ui/xr/guides/foundations). In Home Space, a user is able to multitask with your app running side by side with other apps. In Full Space, your app takes center stage as the focus of the user's experience with full access to the spatial and 3D capabilities of Android XR.

![Woman standing at a table with 3 apps open around her.](<https://lh3.googleusercontent.com/SGb0xm8dlDBTnd2euS-dOVAv_j4O6uk_bCMp7BkQB97dDXQuVn72Dcq3IpOVwjyuMMuHh4RyPzcPR6q4UfgLmPXqjL86G_9Nt7I7PRK7A2oi1Aqr5Q>)

In Home Space, a user is able to multitask with your app running side by side with other apps.

![Woman standing in a virtual moon crater with an app open in front of her. The app has four spatial panels, an orbiter, and a 3D pyramid.](<https://lh3.googleusercontent.com/Ntu9h5U4fS56NUU75O-hEvs1maLHRESck4dFrtldYJ2zeuAnVuQeXWkXfBxZbqkj84Brp5T_czmDcXIgWLq3iF1mbcES-TRrhpmmSky2060oSh3NkZhM>)

In Full Space, your app takes center stage as the focus of the user's experience with full access to the spatial and 3D capabilities of Android XR.

## Material Design for XR

The Material 3 (or M3) library now includes adaptations to help your app stand out in spatial environments. We've also added design guidance for navigation components and layouts, making it easier to create immersive and user-friendly XR experiences.

When designing for XR, you may want to use [spatial UI](http://developer.android.com/design/ui/xr/guides/spatial-ui) to place content in a user’s physical or virtual environment. You can break out your app into spatial panels, orbiters, and add spatial elevation.

Spatial panels allow you to take advantage of the infinite canvas that Android XR provides, breaking out your existing UI content into panels that are resizable and moveable by the user. Orbiters are floating UI elements that control the content within spatial panels. They give the app’s main content more space, and users can quickly access features while the content remains visible. Adding spatial elevation to a component raises it above the spatial panel on the Z-axis. This can help get a user's attention, create better visual hierarchy, and improve legibility.

Spatial UI behaviors in Material Design components and adaptive layouts will help your app adapt naturally to 3D space.

![Material adaptive layout adjusting between home space and full space modes](<https://lh3.googleusercontent.com/aKNVM5nF1ukFod-lQUc0SQF_6yF-T5P6UwyAPkkxNeWhyN0c48m3AkAkjs3a1gYk_x9gzJN40NHd95x1MCw2aDK0IjxK5pYwu9P7twKkgBX6rIKpGuw>)

Material Design adaptive components and layouts for XR

## XR adaptations

Android XR gives you the flexibility to design a spacious UI that adapts seamlessly to an unlimited display. If you’re building with the latest alpha of Material Design components and [adaptive layouts](https://developer.android.com/reference/kotlin/androidx/compose/material3/adaptive/layout/package-summary), you may add the `EnableXrComponentOverrides` wrapper to opt into XR changes in your app. This includes placing navigation controls in orbiters—such as the navigation rail and navigation bar—as well as placing panes into spatial panels.

### Navigation rail

The navigation rail in any Compose layout including [`NavigationSuiteScaffold`](https://developer.android.com/develop/ui/compose/layouts/adaptive/build-adaptive-navigation) will automatically adapt to an XR orbiter. For more information, read [Material Design guidelines](https://m3.material.io/components/navigation-rail/xr).

![Home space app with navigation rail](<https://lh3.googleusercontent.com/6n2h_icI8q2ESzqESIQNS69kRNmN2ncZdLRdo8oOUO02JbLKLBrZOKRrba_g54FnormKcqZIz-ggW4_mwLjXc8LHWZMteLe-zpuv5wG88RMPRMS4vg>)

Non-spatialized navigation rail

![Full space app with navigation rail](<https://lh3.googleusercontent.com/z8haeB4KGw8L0GsKnA1ZmokvtnM_Is3vriHC8SbW48bCzDNURkAjNRVwOUA99P_9BVxGKQ5E_njZRBEh1QEE2mFEPluVkSTHMjh10UMnpMPtdet5NlY>)

Spatialized (XR-adapted) navigation rail

### Navigation bar

Likewise, the navigation bar in any Compose layout including [`NavigationSuiteScaffold`](https://developer.android.com/develop/ui/compose/layouts/adaptive/build-adaptive-navigation) will automatically adapt to an XR orbiter. For more information, read [Material Design guidelines](https://m3.material.io/components/navigation-bar/xr).

![Compact layout in home space mode with nav bar](<https://lh3.googleusercontent.com/oJcnR0EUitzuHwx7o15Pk101VywYBL8aVJsDF-175GTlZ6kUqcJhMW-f8HZ7uoTNZ_i9QWj1OQAtlHbjcfw6EMk_9-KzAYzlkFl2cQdAxlxiw9aipA>)

Non-spatialized navigation bar

![Expanded layout in full space with nav bar](<https://lh3.googleusercontent.com/oB6Czgh-4EbsCabSbemY9uBHoAQlbONHRz9AOCwXbVzBwL8dW2mgqCikjkVmQIfkS3FWVPvJWZWvnGXWdGPNot4N6UN4Id5FXcELvIGqAJhYszHWAA>)

Spatialized (XR-adapted) navigation bar

### List-detail layout for XR

[Compose Material 3 Adaptive Layouts](https://developer.android.com/jetpack/androidx/releases/compose-material3-adaptive) in XR have a 1:1 mapping where each pane becomes an XR spatial panel. Learn more about [`ListDetailPaneScaffold`](https://developer.android.com/develop/ui/compose/layouts/adaptive/list-detail) and [adaptive design guidelines](https://m3.material.io/foundations/adaptive-design).

![List-detail layout in home space](<https://lh3.googleusercontent.com/UQNZ7IU2o0QU_UdKJsqehLTRQNUDnZS_hOoDkQhu8VnSlUcYuWF7ohD5GH_rBbwA8sUOMb8QsjYu_KhQDw57j_HYkA3meoR4bxie8BFOz3NhpGGdbw>)

Non-spatialized ListDetailPaneScaffold

![List-detail layout in full space](<https://lh3.googleusercontent.com/3SZDiYGeMQ6OcrI_jplqKKUuVdhRlh-e6x-8Of-5BWv-8B7hIOW8w8khkYpubXqArQ5h2MXT06XNKcHN7v8jNvasK1OAMf__wUGA7N6fNKcK6dwsFkA>)

Spatialized (XR-adapted) ListDetailPaneScaffold

### Supporting pane layout for XR

[Compose Material 3 Adaptive Layouts](https://developer.android.com/jetpack/androidx/releases/compose-material3-adaptive) in XR also have a 1:1 mapping where each pane becomes an XR spatial panel. Learn more about [`SupportingPaneScaffold`](https://developer.android.com/develop/ui/compose/layouts/adaptive/build-a-supporting-pane-layout) and [adaptive design guidelines](https://m3.material.io/foundations/adaptive-design).

![supporting pane layout in home space](<https://lh3.googleusercontent.com/B-JqNbwGerfkdwPGe_S407ZHnxcWiObalnK0qBRPfDiSaDew2ScsZqYkybNUz961qd7IhADr67eJDxeVIIQ4nYcYfg4IlfMFjSiGqMUTcllM22Wr828>)

Non-spatialized SupportingPaneScaffold

![supporting pane layout in full space](<https://lh3.googleusercontent.com/UD_39RYFdzRYFthsxXXgBmjdLOuoT_E3OetgJF9MEHUx1wQxq9aq11NNbeKvdKXQGeFXTzg2Bp8wvJHCW-EeP97_HoNehlL7ruT0gazFOIKp2PpD8kfZ>)

Spatialized (XR-adapted) SupportingPaneScaffold

## Start building with Material Design for XR

### Design with the Material 3 Design Kit for Figma

In the Figma design kit, you’ll be able to start designing with navigation components as orbiters.

Download the [Material 3 Design Kit](https://www.figma.com/community/file/1035203688168086460) to get started

### EnableXrComponentOverrides to adapt your existing app

All M3 Compose UI inside of the [`EnableXrComponentOverrides`](https://developer.android.com/reference/kotlin/androidx/xr/compose/material3/package-summary#EnableXrComponentOverrides\(androidx.xr.compose.material3.XrComponentOverrideEnabler,kotlin.Function0\)) wrapper will adapt on XR devices. Add the [`EnableXrComponentOverrides`](https://developer.android.com/reference/kotlin/androidx/xr/compose/material3/package-summary#EnableXrComponentOverrides\(androidx.xr.compose.material3.XrComponentOverrideEnabler,kotlin.Function0\)) wrapper to [adapt your app to Material Design for XR](https://developer.android.com/develop/xr/jetpack-xr-sdk/material-design).

### Develop spatial UI with Jetpack Compose for XR

With Jetpack Compose for XR, you can declaratively build your spatial UI layouts using familiar Compose concepts such as rows and columns, including new components, spatial panels and orbiters. Start building with our [new subspace and spatial components](https://developer.android.com/develop/xr/jetpack-xr-sdk/develop-ui).

## Designing the future together

This is our initial developer preview of the Android XR SDK. We’re continuing to extend the design and developer tools you know and love to Android XR. We welcome your feedback, and want to make our developer platform work better for you.

[Share your feedback](https://developer.android.com/develop/xr/support)

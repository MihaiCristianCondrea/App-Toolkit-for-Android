---
title: "Material Design Components for Android 1.10.0"
source_url: "https://m3.material.io/blog/android-stable-release-1-10-0"
lastmod: "2024-12-19"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "blog"
assets:
  - "assets/images/6546cfac5e8f-pR75LC3YTcz6E8rmRyB0FvLZPrvqzHwa-cHtTrrBe3yrNo1YUZLXQzHzvOKVGZZjjoc75U-KG8cvGFUthsfuYBugrUm5MKZN.png"
  - "assets/images/bfb5874df3b8-CCu0KuZwrZXgfaI_z1tvyzOpx1iip6IK7vXp9HD1iLRplnJGk5TE9LepSC7OvilkEz4tlfWVf-q4hdo0acUMkOW_GS_C0_2F.png"
  - "assets/images/ae9a2437eb04-I2FShTZJ7bV7dszsQ437Y8Zu3u0CTdb_T0JJB-zE6EZB0TxpR74ULHo5tqKlByJlu1K97PeHyOzCCTbg8-ahllLzedwwLGXY.png"
  - "assets/images/9497005d2c56-B24uqNiYvH9lJnZf6UnjbNx5VCQ-yGb8xbHsewo91MfBC0n0cQx5d4h891OLAYqTWvDrBSomScR6O5u1A0kWCr9V8MdZqZCV.png"
  - "assets/videos/df32a2e82c60-lg549708-3P-Bottom_Sheet_Predictive_Back.mp4"
---

# Material Design Components for Android 1.10.0

Updates, refinements and a new way to transition between destinations

![abstract flower and leaf illustration](<https://lh3.googleusercontent.com/pR75LC3YTcz6E8rmRyB0FvLZPrvqzHwa-cHtTrrBe3yrNo1YUZLXQzHzvOKVGZZjjoc75U-KG8cvGFUthsfuYBugrUm5MKZNwvH37cw4NqM8EzkPCBI>)

Published: 2023-10-25T13:00:00

We’re happy to announce the release of Material Design Components for Android (MDC-Android) 1.10.0. It offers a mix of functional changes and updates so make sure to check the release notes for a complete listing.

## (In-App) Predictive Back

Predictive back allows a user to see where the swipe gesture will take them before they execute the action, usually by scaling the view to reveal the destination. At any time during the gesture, the user can cancel and remain at the current destination.

Starting with Android 14/API level 34, Android supports predictive back inside your app between destinations in addition to the predictive back-to-home introduced in Android 13/API 33.

The general steps to opt-in to predictive back are:

-   Implementing the “back callback” APIs ([`OnBackAnimationCallback`](https://developer.android.com/reference/android/window/OnBackAnimationCallback), [`OnBackPressedCallback`](https://developer.android.com/reference/androidx/activity/OnBackPressedCallback), [`OnBackPressedDispatcher`](https://developer.android.com/reference/androidx/activity/OnBackPressedDispatcher), etc)
-   Setting the `android:enableOnBackInvokedCallback` manifest flag to true
-   Moving away from legacy back navigation (`Activity#onBackPressed`, `KeyEvent.KEYCODE_BACK`, etc)

[Video: predictive-back animation on android](https://kstatic.googleusercontent.com/files/12607e68170650bb6a3e8279fb697f03347a022216efbc3c004183b912a657587fbf61a339729a76dae17a7efd61423dded1a17e7a8dbdf7e66f11197f991fd6)

Once your app is migrated, destinations involving supported components will get predictive back navigation gestures and animations for free.

At the time of writing, they are :

-   Search Bar / SearchView
-   Bottom sheet
-   Side sheet
-   Navigation drawer / NavigationView

To follow a detailed guide to implementing predictive back in your apps, please check out the documentation on [developers.android.com](http://developers.android.com).

Our MDC [Predictive Back documentation page](https://github.com/material-components/material-components-android/blob/master/docs/foundations/PredictiveBack.md) is the best place to learn about the growing number of components that support predictive back and future slated components.

## Updates and Changes

Starting with this release the minimum `compileSdkVersion` is 34.

**Side Sheet**

Side sheets, first mentioned in the blog post announcing version [1.8.0](https://material.io/blog/android-stable-release-1-8-0), contain supplementary information related to a screen. They were previously anchored only to the right side of the screen. Starting with version 1.10.0, they may be anchored to either side of the screen. To learn more about the different side sheet types, refer to that [blog post](https://material.io/blog/android-stable-release-1-8-0) or the [documentation page](https://github.com/material-components/material-components-android/blob/master/docs/components/SideSheet.md) for side sheet.

**Changes to Carousel**

Carousels, introduced in the [1.9.0 announcement post](https://material.io/blog/android-stable-release-1-9-0), have a new variant (`HeroCarouselStrategy`) that aligns content to the start and uses a “hero” strategy, allocating most of the space in the carousel to one main element.

![carousel in the material catalog app](<https://lh3.googleusercontent.com/CCu0KuZwrZXgfaI_z1tvyzOpx1iip6IK7vXp9HD1iLRplnJGk5TE9LepSC7OvilkEz4tlfWVf-q4hdo0acUMkOW_GS_C0_2FcwuSmnC06zUbV1xj5FW3>)

**BadgeDrawable**

BadgeDrawables, often used to annotate items in navigation components with text such as a new/unread message count or unlabeled shape, have undergone a small tweak.

Previously, you could align the badge to any of the four corners of the anchor view. That has been reduced to two alignments: `TOP_START` and `TOP_END` (default).

![Bottom nav bar with badges](<https://lh3.googleusercontent.com/I2FShTZJ7bV7dszsQ437Y8Zu3u0CTdb_T0JJB-zE6EZB0TxpR74ULHo5tqKlByJlu1K97PeHyOzCCTbg8-ahllLzedwwLGXYkVmP3Gupm_vjW1NUaw>)

TOP\_START Alignment

![Bottom nav bar with badges](<https://lh3.googleusercontent.com/B24uqNiYvH9lJnZf6UnjbNx5VCQ-yGb8xbHsewo91MfBC0n0cQx5d4h891OLAYqTWvDrBSomScR6O5u1A0kWCr9V8MdZqZCVhrH3YEDgdEZuxusbDQ>)

TOP\_END Alignment (Default)

`BOTTOM_START` and `BOTTOM_END` are deprecated and usage is discouraged. To read more about the changes, please see the [BadgeDrawable developer documentation](https://github.com/material-components/material-components-android/blob/master/docs/components/BadgeDrawable.md).

## What’s next for MDC?

We’re hard at work on the 1.11.0 release with some clarifying updates to how we use color in components. The components or features we highlight in these posts are only a fraction of the work that lands in each release. Check out the [release notes](https://github.com/material-components/material-components-android/releases/tag/1.10.0) for a full listing.

You can follow the progress of new versions, file [bug reports](https://github.com/material-components/material-components-android/issues/new?assignees=&labels=bug&template=bug_report.md&title=%5BComponent+name%5D+Short+description+of+issue) and submit [feature requests](https://github.com/material-components/material-components-android/issues/new?assignees=&labels=feature+request&template=feature_request.md&title=%5BComponent+name%5D+Short+description+of+request) on GitHub. Check out the [catalog app](https://github.com/material-components/material-components-android/releases/download/1.10.0/catalog-debug.apk) to see the components in action. Also feel free to reach out to us on Twitter [@materialdesign](https://twitter.com/materialdesign).

---
title: "Material Design Components for Android 1.8.0"
source_url: "https://m3.material.io/blog/android-stable-release-1-8-0"
lastmod: "2024-12-26"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "blog"
assets:
  - "assets/images/2445d4d873b7-vm6V3zhog18syLSWP-kG-RE0d9Fgvpa9vOi_eUXpfNMLXQNtocRYepPqPYvPxcpv2k3K8aYVMsaSEzk1KQSPmG6b36ZfVU3o.png"
  - "assets/images/18fce64e9834-YKEh4KUMPUe6-p8iWpsE2XbtCj1CSEd7x0M4dHzjcIp4BU4dPdlg-z93QwQJK8xRefQo4wvLxoMW1M8doxZxEONm8-CZL6MW.png"
  - "assets/images/7de49130781d-E_Ed2P0q3XX68AsukK4hLjHN39yT4k78idTQGbWAtQ5W9xeB7rwLfZsehnD894x68I53JEGFxHgW7qHiLc_VtGW5-3dW5Lyy.png"
  - "assets/images/8dfdcab0d1a0-lxorRjf7EzUemlTDRAhSam4wurh_2UGomsMB9v_xjHjuwD1COB5uNuA1dmVL6PD56sbRH6YCXL9s21P6NwqxkjnUhLDl6xAI.png"
  - "assets/images/b9354f9c1ebf-9jQ84_sJ_9UEKkSksm7xObgRdXjmd7UCYTMdvFF2Sz_krc7LWvlhSJWDPHB4N0kAh8CSJvFgED_5KgkBC33CwHI8zzdOkZXi.png"
  - "assets/images/40e95b367ad4-nc9Bp-o4jKcOuTrV_T0GqpqafSTLGYXz2sMoMp8CtTQfwJA5-8IwKRmgcCPyi75iz6fc7_uPylWnUT3khJrsyQuObUoP3eVA.png"
  - "assets/images/9f6fa92aa438-b_uSGOnnXJXD1C-3YpIIoTa-DzyYagX6ubo2ZIttWiWdfxNRInJ8b9kLrcCI7IY9TVSSwkMwmAMaz3e57DDBsPAgU-JCaxIZ.png"
---

# Material Design Components for Android 1.8.0

New ways to navigate in and interact with your applications

![Image](<https://lh3.googleusercontent.com/vm6V3zhog18syLSWP-kG-RE0d9Fgvpa9vOi_eUXpfNMLXQNtocRYepPqPYvPxcpv2k3K8aYVMsaSEzk1KQSPmG6b36ZfVU3obasxEZGDQUxIw5VHDMI>)

Published: 2023-02-02T13:00:00

The new year brings with it two new components that can redefine how users navigate in and interact with your applications: search and side sheets.

## SearchBar

The SearchBar component allows you to bring a well-understood navigation pattern to your apps that your users will have seen many times before across Google apps.

![Search bar positioned over a long passage of text on a mobile screen](<https://lh3.googleusercontent.com/YKEh4KUMPUe6-p8iWpsE2XbtCj1CSEd7x0M4dHzjcIp4BU4dPdlg-z93QwQJK8xRefQo4wvLxoMW1M8doxZxEONm8-CZL6MWER6eLmUFUek1UW61EhT0>)

![Search bar positioned over a tab bar and cards on a mobile screen](<https://lh3.googleusercontent.com/E_Ed2P0q3XX68AsukK4hLjHN39yT4k78idTQGbWAtQ5W9xeB7rwLfZsehnD894x68I53JEGFxHgW7qHiLc_VtGW5-3dW5LyyOLk4ZEnbBLXwGKmfXv7b>)

SearchBar provides a floating search field that extends Toolbar. That means you can use navigation icons, menu items, or any other APIs you’ve already been using with Toolbars. When using SearchBar within a CoordinatorLayout parent, you can set SearchBar’s scrolling behavior to control how it responds to scrolling.

## SearchView

SearchView offers a full-screen search view. When used with SearchBar, SearchView provides an immersive search experience, with seamless transitions between the smaller SearchBar and the full-screen SearchView.

SearchView's header is similar to SearchBar; it supports navigation, text entry/hinting, and actions. Below the header, SearchView also holds search results, previous search queries, and suggestions/helper text.

![SearchView opened on a mobile screen, showing a search bar and several suggestions, plus an open keyboard at the bottom of the screen](<https://lh3.googleusercontent.com/lxorRjf7EzUemlTDRAhSam4wurh_2UGomsMB9v_xjHjuwD1COB5uNuA1dmVL6PD56sbRH6YCXL9s21P6NwqxkjnUhLDl6xAIzG-4Jj5QE08lnAhgjBfb>)

For more information on integrating the new Material Search components into your app, check out the full [developer documentation](https://github.com/material-components/material-components-android/blob/master/docs/components/Search.md).

## Side Sheets

Side sheets are surfaces that contain supplementary content related to a screen anchored to the side. [Design guidance](https://m2.material.io/components/sheets-side) has existed for a while, but this release provides a working implementation (see [Material 3 guidance](../components/side-sheets/overview.md)). Similar to bottom sheets, there is no explicit SideSheet tag. A `CoordinatorLayout` child view with a SideSheetBehavior, either in layout XML or programmatically in code, will be recognized as a side sheet.

Your application’s needs and screen real estate will determine which side sheet option will work best.

## Types of Side Sheets

#### **Standard Side Sheets**

Standard side sheets allow the user to view and interact with the side sheet and the visible portion of the main content at the same time. These are well suited to large screens or foldables in tablet mode and are not recommended for smaller screens; opt for modal side sheets on narrower screens.

![A standard sidesheet overlapping an app layout on the right edge of the screen](<https://lh3.googleusercontent.com/9jQ84_sJ_9UEKkSksm7xObgRdXjmd7UCYTMdvFF2Sz_krc7LWvlhSJWDPHB4N0kAh8CSJvFgED_5KgkBC33CwHI8zzdOkZXiTzRV4d8T9asZxbToeQ>)

#### **Modal Side Sheets**

Modal side sheets are more useful on smaller screens. They appear above the main content and block interaction with content outside their bounds. User actions outside the side sheet will dismiss the modal sheet.

![A modal sidesheet overlapping an app layout on the right side of the screen. The app layout is darkened, highlighting the sidesheet.](<https://lh3.googleusercontent.com/nc9Bp-o4jKcOuTrV_T0GqpqafSTLGYXz2sMoMp8CtTQfwJA5-8IwKRmgcCPyi75iz6fc7_uPylWnUT3khJrsyQuObUoP3eVAld2hLxo7KUox-WVi2oI>)

#### **Coplanar Side Sheets**

Coplanar side sheets are a more responsive version of standard sheets; coplanar sheets squash the main content as they expand, resizing the container's dimensions during the sheet's expansion animation. The main content remains fully visible and open for interaction while the sheet is expanded. When the sheet is dismissed, the main content container automatically resizes to its former dimensions alongside the sheet's dismiss animation.

Like standard sheets, coplanar sheets are recommended for large screens and foldable devices, and are not recommended for smaller screens.

![A co-planar sidesheet that compresses the main app layout from the right edge of the screen.](<https://lh3.googleusercontent.com/b_uSGOnnXJXD1C-3YpIIoTa-DzyYagX6ubo2ZIttWiWdfxNRInJ8b9kLrcCI7IY9TVSSwkMwmAMaz3e57DDBsPAgU-JCaxIZbMbJrNY9VpTaw0bc3RQ>)

For more information on integrating the new Material Side Sheet components into your app, check out the full [developer documentation](https://github.com/material-components/material-components-android/blob/master/docs/components/SideSheet.md).

## What’s next for MDC?

We’re hard at work on the 1.9.0 release featuring [a Carousel component powered by RecyclerView](https://github.com/material-components/material-components-android/releases/tag/1.9.0-alpha01). The components or features we highlight in these posts are only a fraction of the work that lands in each release. Check out the [1.8.0 release notes](https://github.com/material-components/material-components-android/releases/tag/1.8.0) for a full listing.

You can follow the progress of new versions, file [bug reports](https://github.com/material-components/material-components-android/issues/new?assignees=&labels=bug&template=bug_report.md&title=%5BComponent+name%5D+Short+description+of+issue) and submit [feature requests](https://github.com/material-components/material-components-android/issues/new?assignees=&labels=feature+request&template=feature_request.md&title=%5BComponent+name%5D+Short+description+of+request) on GitHub. Check out the [catalog app](https://github.com/material-components/material-components-android/releases/download/1.8.0/catalog-debug.apk) to see the components in action. Also feel free to reach out to us on Twitter [@materialdesign](https://twitter.com/materialdesign).

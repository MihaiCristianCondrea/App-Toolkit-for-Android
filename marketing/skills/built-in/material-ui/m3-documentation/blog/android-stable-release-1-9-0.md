---
title: "Material Design Components for Android 1.9.0"
source_url: "https://m3.material.io/blog/android-stable-release-1-9-0"
lastmod: "2024-04-15"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "blog"
assets:
  - "assets/images/f440c627bc54-4lVyZj_pYByZSDxuL1o-us6nihWwgD9F5d6HjS6IyP4uxtSO-P3WNqA48hWSVq70q5riU9cJGXCGjbrmFp6KCk-G9MAXyZGe.png"
  - "assets/images/98fb07a3824c-6IdEWOsFfVrq_d0Yr-NSgnP0iMH-2jsQxlt0pB3hbWTUvVsevd3TdlUWS32V8ZloH9ymGp4zOjQR5XciIDrSvdpVwhY2kz9W.png"
  - "assets/images/e4fb51d99f65-Q5PAFl0kJtvPGHel0TWAXN21Ym5Bal6SlaAkk33imuaKmDaQarDmkn6EgtPE31w0mMYFouo7R92edY5Iae_W81NHqWyv0rXF.png"
  - "assets/images/29ab184b12a9-6vKpHerJbKyf8Xm_3u8Kzkg_LpA-AEjxDuzzkK5hhfqbIkqA_oqJlAZ5E1p14Sus8Y7mRRhb4f1MVM7GzlGj4VtV18PeSRd4.png"
  - "assets/videos/0401dcc0021f-carousel-animation.mp4"
---

# Material Design Components for Android 1.9.0

New ways to navigate in and interact with your applications

![abstract illustration of a plant sprout emerging from a cube](<https://lh3.googleusercontent.com/4lVyZj_pYByZSDxuL1o-us6nihWwgD9F5d6HjS6IyP4uxtSO-P3WNqA48hWSVq70q5riU9cJGXCGjbrmFp6KCk-G9MAXyZGehRqgnndJxZrEbCMRiXc>)

Published: 2023-05-17T13:00:00

Fast on the heels of Google I/O, we’re happy to announce the release of Material Design Components for Android (MDC-Android) 1.9.0.

The new Carousel component allows you to create experiences like photo galleries and interact with lists of items in more flexible ways than a `RecyclerView`.

[Video: animated carousel](https://kstatic.googleusercontent.com/files/e584c6e0e54dc6c7e5fe4b80fcc93a6f1de49fce23eb4ea7473134ea848dbf99285e512a2b855519185cb7a5fb22f1555abb63d3760396c0e1c66721ed698b1c)

It uses `RecyclerView` as a core so you can rest assured that it will be performant and integrate easily into your designs.

## Making your RecyclerView a Carousel

To use a `Carousel`, start with a working `RecyclerView` and wrap each of your item layout files with a `MaskableFrameLayout`. They will instruct the Carousel how to adjust items in relation to each other.

Next, instead of using a `LinearLayoutManager`, use a `CarouselLayoutManager`.

Masking in `MaskableFrameLayout` ranges from 0% to 100% (0.0 to 1.0 in the code). When the layout manager needs to mask an image, it calculates a masking rectangle. The image below shows different masks applied to the same image and how each will reduce the image’s visible area.

![diagram of how images are masked in a carousel](<https://lh3.googleusercontent.com/6IdEWOsFfVrq_d0Yr-NSgnP0iMH-2jsQxlt0pB3hbWTUvVsevd3TdlUWS32V8ZloH9ymGp4zOjQR5XciIDrSvdpVwhY2kz9WQhc7Vok0xAUIsKTqhFk>)

If you are using images in your Carousel with a custom image matrix or other components such as text, verify these objects work well with Carousel’s masking or optionally let them react to changes in mask size using an `OnMaskChangedListener` on your `MaskableFrameLayout`.

## How Items in A Carousel Are Masked

`MultiBrowseCarouselStrategy` allows a carousel to optimally display large, medium, and small items in the allotted space. It attempts to find the optimal amount of large items that will fit into the display with the least amount of size adjustment. After that it will add medium and small items.

In the images below, at the start of the carousel, we can see two large items, a medium item and a small item. At the end of the carousel, the arrangement shifts to become one small item, followed by a medium item, and finally two large items.

| ![screenshot of carousel in catalog app with scroll position at 0](<https://lh3.googleusercontent.com/Q5PAFl0kJtvPGHel0TWAXN21Ym5Bal6SlaAkk33imuaKmDaQarDmkn6EgtPE31w0mMYFouo7R92edY5Iae_W81NHqWyv0rXF3mqFuNHVMrzICiLVTQk>) | ![screenshot of carousel in catalog app with scroll position at 100](<https://lh3.googleusercontent.com/6vKpHerJbKyf8Xm_3u8Kzkg_LpA-AEjxDuzzkK5hhfqbIkqA_oqJlAZ5E1p14Sus8Y7mRRhb4f1MVM7GzlGj4VtV18PeSRd4bcSSWNL1frlFC1jGSfM>) |
| --- | --- |

For more information on integrating the new Carousel component into your app, check out the full [developer documentation](https://github.com/material-components/material-components-android/blob/master/docs/components/Carousel.md).

## What’s next for MDC ?

We’re hard at work on the 1.10.0 release with updates targeting Android U. The components or features we highlight in these posts are only a fraction of the work that lands in each release. Check out the [release notes](https://github.com/material-components/material-components-android/releases/tag/1.9.0) for a full listing.

You can follow the progress of new versions, file [bug reports](https://github.com/material-components/material-components-android/issues/new?assignees=&labels=bug&template=bug_report.md&title=%5BComponent+name%5D+Short+description+of+issue) and submit [feature requests](https://github.com/material-components/material-components-android/issues/new?assignees=&labels=feature+request&template=feature_request.md&title=%5BComponent+name%5D+Short+description+of+request) on GitHub. Check out the [catalog app](https://github.com/material-components/material-components-android/releases/download/1.9.0/catalog-debug.apk) to see the components in action. Also feel free to reach out to us on Twitter [@materialdesign](https://twitter.com/materialdesign).

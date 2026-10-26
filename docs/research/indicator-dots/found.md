Yes. I found the exact Android 10 page indicator in AOSP, including the original animation implementation.

It's called `PageIndicator`, and it belongs to Android's SystemUI Quick Settings implementation.

## 1. The original Android 10 implementation

The relevant class is:

[PageIndicator.java (Android 10 AOSP)](https://android.googlesource.com/platform/frameworks/base/+/refs/tags/android-10.0.0_r2/packages/SystemUI/src/com/android/systemui/qs/PageIndicator.java?utm_source=chatgpt.com)

[PagedTileLayout.java (Android 10 AOSP)](https://android.googlesource.com/platform/frameworks/base/+/refs/tags/android-10.0.0_r2/packages/SystemUI/src/com/android/systemui/qs/PagedTileLayout.java?utm_source=chatgpt.com)

The two classes have different responsibilities:

- `PageIndicator` draws the dots and controls their animations.
- `PagedTileLayout` handles the actual swipeable pages and informs the indicator when the page changes.

And importantly, Android 10 did not simply animate the color or opacity of two circles. It used `AnimatedVectorDrawable` resources to animate the shapes themselves.

The original implementation specifies a 250 ms animation and gives the inactive dot approximately 42% opacity.&#x20;

[image](https://www.google.com/s2/favicons?domain=https://android.googlesource.com\&sz=32)

Git at Google

+1



## 2. How that animation actually worked

The interesting part is the shape transformation. When swiping from page 1 to page 2:

Simplified illustration of the original shape transition, not an exact frame-by-frame rendering.

The active dot stretches into a rounded capsule, the shapes translate horizontally, and the capsule contracts into a circle at the destination.

The XML confirms the implementation uses:

- Vector path morphing (`pathData`).
- Horizontal translation (`translateX`).
- A 250 ms `fast_out_slow_in` interpolator for each phase.
- Separate animations for the prominent and secondary dots.&#x20;

  [image](https://www.google.com/s2/favicons?domain=https://android.googlesource.com\&sz=32)

  Git at Google

  +3



You can inspect the original animation resources here:

[Animated vector configuration](https://android.googlesource.com/platform/frameworks/base/+/refs/tags/android-10.0.0_r2/packages/SystemUI/res/drawable/major_a_b_animation.xml?utm_source=chatgpt.com)

[Circle-to-capsule morph animation](https://android.googlesource.com/platform/frameworks/base/+/refs/tags/android-10.0.0_r2/packages/SystemUI/res/anim/major_a_b_dot_animation.xml?utm_source=chatgpt.com)

[Capsule-to-circle morph animation](https://android.googlesource.com/platform/frameworks/base/+/refs/tags/android-10.0.0_r2/packages/SystemUI/res/anim/major_b_c_dot_animation.xml?utm_source=chatgpt.com)

One subtle detail: although the indicator receives the ViewPager's scroll offset, it converts that value into discrete animation states. It doesn't continuously reshape the dots according to the exact finger position.&#x20;

[image](https://www.google.com/s2/favicons?domain=https://android.googlesource.com\&sz=32)

Git at Google

+1



## 3. Can you use it in a modern Android app?

Yes, including in Jetpack Compose.

It is not a public Android SDK component that you can simply import. It is internal SystemUI code, but its source and animation assets are available under the Apache 2.0 license.

For a reusable Compose implementation, I would preserve the original geometry, transition states, and timing, but implement the drawing with Compose Canvas and animated paths rather than depending on the old SystemUI `ViewGroup`.

That would make it usable with `HorizontalPager`, Material 3, and potentially App Toolkit without bringing in unrelated SystemUI dependencies.

The nice discovery here is that the animation wasn't lost entirely. Its original source, vector paths, and motion definitions are still preserved in AOSP, so it can be recreated with much greater fidelity than simply guessing what the old animation looked like.
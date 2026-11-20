---
title: "MDC-Android Stable release 1.7.0"
source_url: "https://m3.material.io/blog/android-stable-release-1-7-0"
lastmod: "2024-04-15"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "blog"
assets:
  - "assets/images/300fd407915c-RFeqhT5kDBq9Fo4GyqNq4EE7g1WKi-NBZ_IWBXOf235ribRpuquQmcjEhLfxPd5LycBauHBWoTHFNIo6eFWYEToksbVSaZdH.png"
  - "assets/images/af091017affe-0_eEy3Q32oN80EE37jR_99ZazPpBb6cDvM3NImsqfKiIiKxt4srqT5BNp47XsEeug7pobgPcTVpF-1dKIsvXfYdeA2ze2nX_.png"
  - "assets/images/2e22500722f5-WtZFwRC4kInGlczgoR1iz1yQtq2JlLQPdMTLg6M9FugjDmhH49D6U31FH4cQj6kUUL4of-oi0DiO9T4mCndm9SRlHtoFpjxr.png"
  - "assets/images/f0ca12bcc321-zThUgoPFDY4L8WPwyBDMUCXX2C9Iyt04wg8Bhr3GaPTmcu0pvUeOr55vv7ZsRmRCylio-B93o-KfR52TVwtYii6_b7vzBuIx.png"
  - "assets/images/38624bdf8fae-JNSz_5_2WViMK2UQpSvJzhZW-4kGBzxHXfKv5Jc6IyxBKy4XKn26raXhCrY2JExhF9bWX2OT7YprkBbmXWfmFoplDddL5fbE.png"
---

# MDC-Android Stable release 1.7.0

with Material Design 3 shape guidance and improved a11y

![Image](<https://lh3.googleusercontent.com/RFeqhT5kDBq9Fo4GyqNq4EE7g1WKi-NBZ_IWBXOf235ribRpuquQmcjEhLfxPd5LycBauHBWoTHFNIo6eFWYEToksbVSaZdHv3fSpcQdNfJVwQP85tvw>)

Published: 2022-10-20T10:00:00

The latest releases of Material Design Components (MDC) - 1.7.0 brings updates to Material You styling, accessibility and size coherence and new minimum version requirements

MDC 1.7.0 has new minimum version requirements:

-   Java 8 (1.8), previously Java 7 (1.7)
-   Android Gradle Plugin (AGP) 7.3.3, previously 4.0.0
-   Android Studio Chipmunk, version 2021.2.1

This is a fairly large jump in terms of the Gradle plugin version, so make sure to secure changes in your build files first before moving on to UI code. As always, our release notes contain the full details of what has been updated. There are a couple standout updates we’d like to highlight.

## MaterialSwitch component

The Switch component has undergone a visual refresh that increases contrast and accessibility. The `MaterialSwitch` class replaces the previous `SwitchMaterial` class.

It now differentiates between the on and off states more by making the “on” thumb larger and able to contain an icon in addition to an on state color. The “off” state has a smaller thumb with less contrast.

![Image](<https://lh3.googleusercontent.com/0_eEy3Q32oN80EE37jR_99ZazPpBb6cDvM3NImsqfKiIiKxt4srqT5BNp47XsEeug7pobgPcTVpF-1dKIsvXfYdeA2ze2nX_Umw_qZYZnfrQRGmQmbdW>)

Much of the new component’s core API aligns with the obsolete `SwitchMaterial` class so to get started, you can simply replace the class references.

For more information on how the obsolete component stacks against the new implementation, [check the documentation on GitHub](https://github.com/material-components/material-components-android/blob/master/docs/components/Switch.md).

## Shape Theming

A component’s shape is one way to express your brand. In addition to providing a custom [MaterialShapeDrawable](https://developer.android.com/reference/com/google/android/material/shape/MaterialShapeDrawable), there is also a means to more simply customize shape theming using rounded or cut corners.

Material 3 components have been updated to apply one of the seven styles ranging from None to Full. A component’s shape is defined by two properties: its Shape family, either rounded or cut, and its value, usually described in dp. Where a “none” style always results in a rectangular shape, the resulting shape for full depends on the shape family. Rounded returns a rectangle with fully rounded edges, while Cut returns a hexagonal shape.

You are able to set the shape family and value individually and arbitrarily on each edge but there are set intervals and baseline values.

| Shape Style | Value |
| --- | --- |
| None | 0dp |
| Extra Small | 4dp |
| Small | 8dp |
| Medium | 12dp |
| Large | 16dp |
| Extra Large | 28dp |
| Full | N/A |

Default theming will apply the same shape family and value on all four edges. You can apply shape family and shape style (or alternatively a custom value) independently on each corner.

![Image](<https://lh3.googleusercontent.com/WtZFwRC4kInGlczgoR1iz1yQtq2JlLQPdMTLg6M9FugjDmhH49D6U31FH4cQj6kUUL4of-oi0DiO9T4mCndm9SRlHtoFpjxrm1vkAIOZ3JRK-5yCsviq>)

The Shape Theming card in the Catalog app allows you to see how different values affect rounded or cut corners.

![Image](<https://lh3.googleusercontent.com/zThUgoPFDY4L8WPwyBDMUCXX2C9Iyt04wg8Bhr3GaPTmcu0pvUeOr55vv7ZsRmRCylio-B93o-KfR52TVwtYii6_b7vzBuIx_lu2LgbGO7O46Y5D_mo>)

![Image](<https://lh3.googleusercontent.com/JNSz_5_2WViMK2UQpSvJzhZW-4kGBzxHXfKv5Jc6IyxBKy4XKn26raXhCrY2JExhF9bWX2OT7YprkBbmXWfmFoplDddL5fbEs8ggbHfuiHhzV7Fd1HY>)

## What’s next for MDC ?

We’re fast at work on the next major version of MDC. You can follow the progress, file [bug reports](https://github.com/material-components/material-components-android/issues/new?assignees=&labels=bug&template=bug_report.md&title=%5BComponent+name%5D+Short+description+of+issue) and [feature requests](https://github.com/material-components/material-components-android/issues/new?assignees=&labels=feature+request&template=feature_request.md&title=%5BComponent+name%5D+Short+description+of+request) on GitHub. Also feel free to reach out to us on [Twitter](https://twitter.com/materialdesign).

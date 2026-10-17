# TV app icon design guidelines

This guide describes creating banners and launcher icons for Android TV.

## Key takeaways

The following are the key takeaways from this page:

- There are two icon types for Android TV OS apps in AndroidManifest.xml:
    - `android:icon` (standard, mandatory)
    - `android:banner` (banner, mandatory)
- Adaptive icons are highly recommended.
- Both the icon and banner must comply with the design guidelines outlined in this guide.
- Use the [official figma template](https://www.figma.com/community/file/1283953738855070149) for generating the banner \& icons
- Android TV OS doesn't support themed icons.

| **Note:** `android:roundIcon` is deprecated and shouldn't be used in favor of adaptive icons.

## Overview

Google TV and Android OS make use of iconography provided through your
`AndroidManifest.xml` in three ways:

- Launcher icon (1x1 aspect ratio)
- Round launcher icon (1x1 aspect ratio, but circular)
- Banner logo (16x9 aspect ratio)

These are used in different places for different use cases, such as the
**Your apps** row, Settings, or installation progress.

## Banner

The Banner logo is a 16x9 aspect ratio logo that is used in Android TV OS to
show your app launcher. We recommend that TV apps provide an adaptive
16:9 banner with the following specifications. You can also provide
xhdpi resources with size of `320 x 180px` when using API level 25 or lower.  
![TV Banner icon sizes](https://developer.android.com/static/design/ui/tv/guides/system/images/icons/banner-sizes.webp)

| Density |  Min Size  | Folder location (under res) | Pixel Ratio |
|---------|------------|-----------------------------|-------------|
| mdpi    | 160x90 px  | mipmap-mdpi                 | 1           |
| hdpi    | 240x135 px | mipmap-hdpi                 | 1.5         |
| xhdpi   | 320x180 px | mipmap-xhdpi                | 2           |
| xxhdpi  | 480x270 px | mipmap-xxhdpi               | 3           |
| xxxhdpi | 640x360 px | mipmap-xxxhdpi              | 4           |

| **Note:** Text must be included in the image. If your app is available in more than one language, you must provide separate versions of the banner with text for each supported language.

## Launcher icon

The Launcher icon is a 1x1 aspect ratio resource that is used in multiple
places such as Settings and Media session integrations (Now playing card)
on Android TV. The launcher icon can also be used in **Your apps** row
on Google TV.  
![Launcher icon sizes](https://developer.android.com/static/design/ui/tv/guides/system/images/icons/launcher-sizes.webp)

| Density |  Min Size  | Folder location (under res) | Pixel Ratio |
|---------|------------|-----------------------------|-------------|
| mdpi    | 80x80 px   | mipmap-mdpi                 | 1           |
| hdpi    | 120x120 px | mipmap-hdpi                 | 1.5         |
| xhdpi   | 160x160 px | mipmap-xhdpi                | 2           |
| xxhdpi  | 240x240 px | mipmap-xxhdpi               | 3           |
| xxxhdpi | 320x320 px | mipmap-xxxhdpi              | 4           |

## Adaptive icons

As of the Android 8.0 release (API level 26), there is support for
adaptive launcher icons, which allows for more flexibility and interesting
visual effects when it comes to app icons. For developers, that means that
your app icon is made up of two layers: a foreground and a background layer.

### Adaptive banner

You can also provide an adaptive banner along with a legacy banners,
similar to launcher icon banners that also have two layers.  
![TV Adaptive Banner](https://developer.android.com/static/design/ui/tv/guides/system/images/icons/banner-adaptive-safe.webp)

### Adaptive launcher icon

To ensure that your adaptive icon supports different shapes, and visual
effects the design must meet the following requirements:

Provide two layers for the color version of the icon:
one for the foreground, and one for the background.  
![TV Adaptive Launcher Icon](https://developer.android.com/static/design/ui/tv/guides/system/images/icons/launcher-adaptive-safe.webp)

Adaptive icons are defined using foreground and background layers. The 72 x 72
safe zone in the first image shows where your icon and foreground layers are
never be clipped by a shaped mask.

A monochrome version of the icon is not required as Android TV
does not support themed icons.
| **Note:** Android or Google TV don't support themed icons.

## Examples

Below are some do's and don'ts to consider when designing a TV app icon.

### Banner examples

![](https://developer.android.com/static/design/ui/tv/guides/system/images/icons/200.webp)  
check_circle

### Do

Follow the guidelines, keep the logo in safe area  
![](https://developer.android.com/static/design/ui/tv/guides/system/images/icons/000.webp)  
cancel

### Don't

Avoid using text or graphic elements to indicate any additional information.  
![](https://developer.android.com/static/design/ui/tv/guides/system/images/icons/001.webp)  
cancel

### Don't

Don't use text or graphic elements that can mislead users  
![](https://developer.android.com/static/design/ui/tv/guides/system/images/icons/002.webp)  
cancel

### Don't

Do not spill the logo out of safe area  
![](https://developer.android.com/static/design/ui/tv/guides/system/images/icons/003.webp)  
cancel

### Don't

Avoid adding any border around the logo as they get cropped and create unpolished visuals.  
![](https://developer.android.com/static/design/ui/tv/guides/system/images/icons/004.webp)  
cancel

### Don't

Avoid cropping the logo  
![](https://developer.android.com/static/design/ui/tv/guides/system/images/icons/005.webp)  
warning

### Caution

When using a banner its recommend you show your full logo, icon + text.

### Launcher examples

![](https://developer.android.com/static/design/ui/tv/guides/system/images/icons/201.webp)  
check_circle

### Do

Follow the guidelines, keep the logo in safe area  
![](https://developer.android.com/static/design/ui/tv/guides/system/images/icons/100.webp)  
cancel

### Don't

Don't use text or graphic elements to indicate any additional information.  
![](https://developer.android.com/static/design/ui/tv/guides/system/images/icons/101.webp)  
cancel

### Don't

Don't use text or graphic elements that can mislead users  
![](https://developer.android.com/static/design/ui/tv/guides/system/images/icons/102.webp)  
cancel

### Don't

Do not spill the logo out of safe area  
![](https://developer.android.com/static/design/ui/tv/guides/system/images/icons/103.webp)  
cancel

### Don't

Avoid adding any border around the logo as they get cropped and create unpolished visuals.  
![](https://developer.android.com/static/design/ui/tv/guides/system/images/icons/104.webp)  
cancel

### Don't

Avoid cropping the logo

## Resources

- [Official figma template](https://www.figma.com/community/file/1283953738855070149) for banner \& icons

## Related reading

- [Adaptive icons mobile](https://developer.android.com/develop/ui/views/launch/icon_design_adaptive)
- [Using the adaptive icons](https://developer.android.com/codelabs/basic-android-kotlin-compose-training-change-app-icon#2)
- [Designing adaptive icons](https://codelabs.developers.google.com/design-android-launcher#0)
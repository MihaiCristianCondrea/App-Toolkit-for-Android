---
title: "How to Find Device Metrics for Any Screen"
source_url: "https://m3.material.io/blog/device-metrics"
lastmod: "2024-12-23"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "blog"
assets:
  - "assets/images/7bf85e44b90e-VSXJYJl6FT8-ehDuH_XzkjkJmlGbHJMu9hBQQa0aTCHEC51Jf5eb8mHYs5OPF3V_ifEriOvE8pkq0uLpfs_Jjho8fp5ii5Ke.png"
---

# How to Find Device Metrics for Any Screen

Calculate the right measurements for design across devices

![Image](<https://lh3.googleusercontent.com/VSXJYJl6FT8-ehDuH_XzkjkJmlGbHJMu9hBQQa0aTCHEC51Jf5eb8mHYs5OPF3V_ifEriOvE8pkq0uLpfs_Jjho8fp5ii5KeGyS3Nws>)

Published: 2020-05-19T13:00:00

New devices are always coming online, offering new formats, screen sizes, and pixel densities that you’ll want your product to accommodate. If Googling doesn’t give you the numbers you need, or you just want to show off your math skills, here’s a relatively easy way to determine the relevant metrics for your designs.

## Do it yourself 🛠

There are six pieces of information you’ll want in the end: the screen diagonal measurement, screen dimensions, aspect ratio, pixel resolution, dp (or [density-independent pixel](https://developer.android.com/training/multiscreen/screendensities), Android’s logical pixel) and the density bucket to which each device belongs. Some of those specifications can be found on each device’s product page, or through other sites that collect information about specific devices.

It’s easiest to visualize this information if we have an example. So, to get a feel for the process of finding these values, let’s start with the Pixel 4.

The screen diagonal, aspect ratio, and pixel resolution can all be found on the [Pixel 4 product page](https://store.google.com/product/pixel_4_specs). For devices that don’t have in-depth or easily accessible specification pages, sites like [the GSMArena Phone Finder](https://www.gsmarena.com/search.php3) can be a good resource.

## Finding Screen Dimensions 📐

After filling in the readily available info, we have three more specs to solve for. The first one is the screen’s dimensions in terms of width and height. The formulas for this — which I learned from an [Omni Calculator page by Hanna Pamula](https://www.omnicalculator.com/other/screen-size) — are fairly easy to use, and only require a diagonal measurement and an aspect ratio (AR).

_Width = diagonal / √(AR²+1)_

_Height = (AR)×Width_

So for our example, we know the screen diagonal is 5.7 and the aspect ratio is 19:9 (which we’ll write as 19/9 in the formula).

_Width = 5.7 / √((19/9)²+1)_

**_Width = 2.44”_**

And now that we know Width, we can solve for Height.

_Height = (19/9)×2.44_

**Height = 5.15”**

So our screen dimensions are **2.44×5.15”**

## Finding dp Resolution 📏

Density-independent pixels are Android’s logical pixel. Measuring in dp allows designers and developers to ensure that what they create appears at the same physical size across screens, no matter what density those screens happen to be. So knowing the dp resolution of a device can be really helpful in targeting that device with your design. You can easily set up artboards and assets that focus on specific form factors, and reliably reproduce your design across them.

For this formula (which you can find in the [Android Developers documentation on pixel density](https://developer.android.com/training/multiscreen/screendensities)), we need to know the screen’s pixel resolution, the dimensions we calculated before, and the screen’s ppi. The screen’s dpi (written as ppi) should be available at one of the sources mentioned above.

_px = dp×(dpi/160)_

In our example, we know the screen’s pixel resolution is 1080×2280px, and its physical dimensions are 2.44×5.15” so we can plug those values into the formula, starting again with width.

_1080 = dp×(444/160)_

_1080 = dp×2.775_

_dp = 1080/2.775_

**_dp = 389_**

Next we’ll do the same calculation for the screen’s height in density-independent pixels.

_2280 = dp×2.775_

_dp = 2280/2.775_

**_dp = 822_**

## Finding the density bucket 🔍

The [Android Developers documentation on pixel density](https://developer.android.com/training/multiscreen/screendensities) also outlines the notion of “density qualifiers,” which Android uses to serve bitmap drawable resources in an app. If there are non-vector assets like photos or illustrations in your design, it can be useful to know which density buckets you’re targeting, serving the right asset to each device to speed up loading and to avoid distortion and “out of memory” errors.

Finding the density bucket is as easy as looking at the table in the documentation linked above and comparing it to our dpi value. For the Pixel 4’s ~444ppi, we would assign the XXHDPI density qualifier.

## Putting it all together 🧩

Having worked through those calculations, we now have a complete set of device metrics for our example device, the Pixel 4.

For further reading on pixel density, layout, and how the two interact, see the [Material Design guidance on Layout](https://material.io/design/layout/understanding-layout.html#usage).

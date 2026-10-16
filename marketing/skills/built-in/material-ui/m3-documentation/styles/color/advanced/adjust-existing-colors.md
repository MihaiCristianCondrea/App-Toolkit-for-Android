---
title: "Advanced customizations"
source_url: "https://m3.material.io/styles/color/advanced/adjust-existing-colors"
lastmod: "2025-09-26"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "styles"
assets:
  - "assets/images/46586b0b0fba-lwt7zkg8-1.png"
  - "assets/images/ec4ab91ce275-lwt809t2-2.png"
  - "assets/images/4c04d8198d0c-lwt80nic-3.png"
  - "assets/images/d750964423f0-lwt814il-4.png"
  - "assets/images/646d5fc17e7c-lwt81svm-5.png"
---

# Advanced customizations

Apply, define, or adjust colors to create a fine-tuned, unique color experience

## Adjust existing colors

You can control the color algorithm’s output to adjust the appearance of colors within the roles provided by default.

## Define your own baseline scheme

You can input colors to define your own baseline scheme.

### Why

You may want to define your own baseline scheme so your app’s colors stay static (ie. does not change with dynamic color), such as to reflect your brand colors. By providing your own custom input colors for the primary, secondary, tertiary, and neutral colors in the scheme, Material will provide back the scheme’s regular color roles with values derived from your reference colors.

![Above, a logo of two trees featuring dark green, orange, and pale blue colors. Below, each of those colors is show as a circle, with an arrow pointing from it to a set of chips showing color roles produced from the original color.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwt7zkg8-1.png?alt=media&token=d6aa53db-1686-443c-b47a-89cb0c4a6cfd>)

_You can input your own colors to produce a static baseline scheme. In this example, colors from the logo are inputted to produce primary, secondary, and tertiary colors._

### How

Using the Material Theme Builder, input your own colors for primary, secondary, tertiary, neutral, and neutral variant. The Theme Builder will generate a color scheme with values based on your inputs, and the given color roles can be used in the same manner as those from any other Material scheme.

### Best practices

-   Conventionally, primary and tertiary colors are the most visually prominent in the scheme, with tertiary appearing complementary to primary by changing its hue. Secondary, neutral variant, and neutral colors match primary in hue but are progressively less chromatic in that order. Input your colors into the appropriate category to maintain similar relationships as designed by Material, and ensure expected and visually pleasing results when those colors are mapped to components.
-   If the colors provided back from your input color appear differently than expected, you can enable or disable [color fidelity](adjust-existing-colors.md#cb49eeb4-3bbd-4521-9612-0856c27f91ef). Color fidelity is a feature that adjusts colors’ tones to match that of your input color.
-   If the 26+ standard color roles do not meet your needs, you may need to [define custom color roles](define-new-colors.md#baed14ce-4be8-46aa-8223-ace5d45af005).

## Define your own dynamic scheme

You can define color algorithm rules to produce your own dynamic scheme.

### Why

Control the appearance of your app’s colors while respecting dynamic color. For example, you may want your app to match the user’s wallpaper theme, but appear more vibrant than the default dynamic theme colors.

![Thumbnail of a red floral wallpaper with two arrows leading out of it, labeled 1 and 2. Arrow 1 leads to a set of red colors chips produced from the wallpaper. Arrow 2 points to another set of red color chips, which appear more vibrant than the first.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwt809t2-2.png?alt=media&token=2f6a951b-89fe-4154-a188-61939ae892dd>)

_You can define your own dynamic scheme to reflect a user's wallpaper but control other aspects such as the colors' vibrancy. Colors produced dynamically from a user's red wallpaper following default specs; Colors produced dynamically from the same wallpaper following custom-defined specs_

### How

-   Material generates the color scheme by following hue and chroma values specified for each group of colors (primary, secondary, tertiary, neutral, and neutral variant). For more information, see [how the system works](../system/how-the-system-works.md). To adjust the appearance of these colors and produce your own dynamic scheme, you must provide your own hue and chroma values for each of these color groups.
-   Once these values are known, you may define your own scheme variant and call Material Color Utilities (MCU) to dynamically generate the scheme and provide color values for each role in the scheme.

### Best practices

-   Defining custom color roles should be considered only if you cannot achieve your desired colors with other Material color solutions.
-   If the colors provided back from your input color appear differently than intended, you can enable or disable [color fidelity](adjust-existing-colors.md#cb49eeb4-3bbd-4521-9612-0856c27f91ef). Color fidelity is a feature that adjusts colors’ tones to match that of your input color.
-   If the color roles provided by Material out of the box do not meet your needs, you may need to [define custom color roles](define-new-colors.md#baed14ce-4be8-46aa-8223-ace5d45af005) for greater control over their appearance.

## Use color fidelity

You can apply color fidelity to make scheme colors better match your input colors.

### Why

Material scheme colors are mapped to tones (lightness or darkness) to achieve visually accessible color pairings with sufficient contrast between foreground and background elements. In some cases, these tones can prevent colors from appearing as intended, such as when a color is too light to appear vibrant. Color fidelity is a feature that adjusts tones in these cases to produce the intended visual results without harming visual contrast.

![Above, a dark purple circle with an arrow labeled 1 pointing to a set of color chips, whose colors appear similarly dark.
Below, a dark purple circle with an arrow labeled 2 pointing to a set of color chips, whose colors appear lighter.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwt80nic-3.png?alt=media&token=3c8ada3b-6184-4d34-8763-5b20288ddb22>)

_Color fidelity adjusts tones in color roles to produce the closest match to your input color. In this example, colors are produced from a dark purple input with and without color fidelity. Color roles produced with color fidelity; Color roles produced without color fidelity_

### How

-   In the Material Theme Builder, you can toggle the “match color” option on your input color to enable or disable fidelity. By default, fidelity is enabled when you use Theme Builder to [create a custom baseline scheme](adjust-existing-colors.md#c6810874-a320-4684-8df6-3869887ea49c) or [define static colors](define-new-colors.md#f13116d1-3023-44b9-b0b5-2ee07dc1af5f).
-   In code, you can flag color roles in your scheme with a boolean which will enable or disable fidelity for those colors.

### Best practices

-   When producing a custom baseline scheme or defining static colors, you may wish to toggle fidelity on and off to determine which setting better suits your desired design.
-   Because color fidelity adjusts tones (lightness or darkness of colors), to ensure accessible contrast, remember to pair appropriate [colors roles](../roles.md) together, such as a background color with its corresponding foreground “on” color.

## Harmonize colors

In dynamic schemes, you can automatically adjust the hue of static colors so they look better alongside the scheme’s primary color.

### Why

Static colors may visually clash with a  scheme’s dynamically changing colors. To improve visual harmony, Material provides an optional ‘harmonize’ function that slightly adjusts static colors to look better in dynamic schemes.

Colors that are closer in hue appear more pleasing together than colors with hues farther apart. Based on this principle, harmonization adjusts the hue of static colors, making them closer to the hue of the scheme’s primary color.

![Diagram comparing static colors in an app to harmonized static colors](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwt814il-4.png?alt=media&token=c451da1d-f5cb-43ef-ad1c-d1e8e23c68bd>)

_In this example, the color scheme has:Green as the primary color; Static blue; Static orange;; When harmonized, those static colors change hue, moving closer to the primary color on the color wheel. The resulting colors appear more visually pleasing together because they are closer in hue._

To preserve the semantic meaning of static colors (such as a red to communicate errors), harmonization limits the amount that a color’s hue can change. Harmonized colors will become warmer or cooler in hue without appearing like another type of color.

![Diagram showing the limited range of harmonized hues](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwt81svm-5.png?alt=media&token=75856d62-6223-4263-9919-8996940fa1c6>)

_To preserve the semantic meaning of colors, harmonization limits the amount that a color’s hue can change. For example, a red color (1) can become cooler (2) or warmer (3) in hue, but will not appear purple or orange._

### How

-   In the Material Theme Builder, you can toggle harmonization on and off within the overflow menu for each static color you have added to the scheme.
-   In code, use the ‘Blend’ function from [Material Color Utilities](https://github.com/material-foundation/material-color-utilities) to harmonize colors

### Best practices

-   Harmonization will adjust a static color differently depending on the scheme’s primary color, so check the results under a variety of schemes to see the range of how they can appear in dynamic color.
-   Don’t harmonize colors whose appearance should stay absolutely consistent, such as brand colors.

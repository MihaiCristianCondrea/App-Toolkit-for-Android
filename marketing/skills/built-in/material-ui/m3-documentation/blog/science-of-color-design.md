---
title: "The Science of Color & Design"
source_url: "https://m3.material.io/blog/science-of-color-design"
lastmod: "2024-11-29"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "blog"
assets:
  - "assets/images/885bea39b296-FgfF6od_qjYXbXowPPgL3IQ8T0QnEAZnoehlRcXHSXK7QXfHe_LvBm3SeYIkxhSRn3gBjjH4GEO6DYOex8btaN34lVDSzP7Z.png"
  - "assets/images/a000f1f37910-wB5PDoi6DRex1AOSXv9F-Pr_0QxVnJ8C6kUB-UEqrQ35Hb5vMtyWWtKOBolpoyMhEJuDVs07rEUnOEJqdPsEhRblGDs3eFrH.png"
  - "assets/images/06ca8251054b-PWM-JWdCtOxcGHHoiZfm1HkSNNcMRXyGE4CRapU1lrFHo93W1dqHACB24x7FCuqFKA-6LTBMhEtSHsMkZcu8EsOrNaUWE6ix.png"
  - "assets/images/6471ff1a9c1f-V8hsEuYzWThQiqE6FdY0IHRwLsWjrmsOlNUEDMIsO_1LQyunBLiX71BM7VmnXam9V7sxTr6RZFcoit0-kD0TgZh3NG9VFxTk.png"
  - "assets/images/ff7f02cbc5c6-UwzVMjQUmpgj6k3F8oAb_7TuyjL7RqrVFTJtwdnu36DMq8tNiF40AFe59CDjP4YvNMWFK9Xh5FjJQY1QLu2bF5Y7LlVRxWxE.png"
  - "assets/images/4f0a7c6b524f-lpMERRSJV0-EYstnNp-H6UIc_kfyhLFTXP8j9lvIIS8XkmDxAXxbZswwOp9pLaq8Z50zFJKPRWRlH0GZKJvLSYpcmonsiGXs.png"
  - "assets/images/dc521d11f389-25H1ut2RHsWhTK6Nr9jFuwBYpaXyOJtBlGhOTBcNhDWE4q8VoNWRExUxtLYyPyM4swITzlLYJF3pJ5WipEcVr9xdvyHtaCAA.png"
  - "assets/images/4919d2405f39-E_Te2ABKYJaO9sHwRChTB2JNgnpS_RMhqI79XAZfRb0aGCf__Wtm4drYJ2SdaRP9GNJBOCVODy66wPNU2ERQnlUSWQKPU__6.png"
  - "assets/images/10b6eb1f41fb-459crB0shmfQBhnWHtw5UPjX1x42hQ_1GNBxOjiwYootCbqzxno4rleDZ4e8ceuB1RDqyvyrtJTP7QkANIreOmp6Jwefk_yN.png"
---

# The Science of Color & Design

How Material used color science to make design easier and more expressive than ever before

![Image](<https://lh3.googleusercontent.com/FgfF6od_qjYXbXowPPgL3IQ8T0QnEAZnoehlRcXHSXK7QXfHe_LvBm3SeYIkxhSRn3gBjjH4GEO6DYOex8btaN34lVDSzP7ZULMMSqoE7bsxyCWA0Q>)

_HCT, a new color space developed by Google for design_

Published: 2022-02-17T10:00:00

I joined Google as a software engineer with a background in print and digital design. My first project was helping build a new user interface for the Assistant in Android 10. A challenging part of the design was the background—it was unclear where to place text to make sure it was legible, and there was debate over whether it was visually pleasing as it faded from black to translucent.

Miranda Kephart, a fellow engineer on the project, pointed out whether it was visually pleasing was based solely on the perceived smoothness of the color distribution while transitioning from black to transparent.

My initial reaction was this couldn’t be the case. Designers never talked about color distribution like that, or even talked about altering it, it must be correct already. As soon as I said that, I was struck by the questions it raised: What if design and engineering aren’t completely separate? What does it even mean for a set of colors to be ‘smooth’? What does it mean for colors to be organized smoothly? Are there even answers to those questions? If we can get answers to those big questions, do they even help us know where to put the text?

Finding answers to those questions took me a couple years and a deep dive into the history of color, both scientifically and in design. The answers pointed to a new color space that would empower designers creatively, and improve everything we do with color.

## Toward a perceptually accurate color system

The way to construct a smooth distribution of colors is to use a perceptually accurate color system. These systems organize colors in a 3D space by matching how people see them, and they are built by creating formulas to fit data gathered from surveying thousands of people in thousands of experiments over decades. These experiments, generally, show people two colors and ask them to choose the one that is lighter / more colorful.

The three dimensions the eye uses to organize colors are hue, colorfulness, and lightness. Hue tells us what angle on the color wheel to use, say, red versus purple. Colorfulness describes how vibrant or neutral (close to gray) a color looks—the further away colors are from the center of the color space, the more colorful it is. Lightness describes how close to white or black a color appears. In color space, lightness is vertical, like floors in a building.

![Image](<https://lh3.googleusercontent.com/wB5PDoi6DRex1AOSXv9F-Pr_0QxVnJ8C6kUB-UEqrQ35Hb5vMtyWWtKOBolpoyMhEJuDVs07rEUnOEJqdPsEhRblGDs3eFrHpN8BINeCNrxpxjuh9I0>)

In the early 1900s, MassArt professor Albert Munsell created the perceptually accurate color system¹ Source by SharkD, derivative work of Datumizer² CC BY-SA 3.0 license³

The color system used by designers today is HSL, or hue, saturation, lightness. HSL isn’t remotely accurate, and doesn’t try to be: it was built to make computing colors fast on 1970s computers.

Our brand new, perceptually accurate, color system is called HCT, which stands for hue, chroma, tone.

Even though HCT is brand new, it is built on existing work: color science defines many perceptually accurate color spaces. For simplicity, let’s focus on two of them: L\*a\*b, also known as LCH, (lightness, chroma, hue) and CAM16.

HCT’s lightness measure, tone, is the same as L\*a\*b\*’s lightness. Using that lightness measure, along with some math tricks, meant we could measure contrast with HCT, directly integrating contrast checker algorithms and accessibility requirements.

HCT’s hue and colorfulness measures, hue and chroma, are the same as CAM16’s hue and chroma. You may wonder why we didn’t just use L\*a\*b\*’s hue and chroma measures, then, we could have just use L\*a\*b\*! However, when we tried using it in design, L\*a\*b\* was too inconsistent perceptually.

For the first time, designers have a color system that truly reflects what users see, taking into account a range of variables to ensure appropriate color contrast, accessibility standards, and consistent lightness/colorfulness across hues.

![Image](<https://lh3.googleusercontent.com/PWM-JWdCtOxcGHHoiZfm1HkSNNcMRXyGE4CRapU1lrFHo93W1dqHACB24x7FCuqFKA-6LTBMhEtSHsMkZcu8EsOrNaUWE6ixtknM1Sv7hhpHwvLWcG1->)

At HSL “lightness” 50, accurately measured lightness ranges from 33 to 96!

![Image](<https://lh3.googleusercontent.com/V8hsEuYzWThQiqE6FdY0IHRwLsWjrmsOlNUEDMIsO_1LQyunBLiX71BM7VmnXam9V7sxTr6RZFcoit0-kD0TgZh3NG9VFxTkr4gHwBoZ_UFbyPGgsg>)

Viewing LCH & HCT color spaces from the top, we can visualize Lab’s more inconsistent scaling

## Exposing and solving for contrast

Color contrast has long been a challenge for design.

When I was a print designer, contrast meant whether there was enough difference between colors to be readable. Put simply, black text on a black background had no contrast because there was no difference between the colors.

When I became an engineer at Google, I was shocked to learn there is a precise definition and formula for contrast: the difference in luminance, or, perceptually accurate lightness, between two colors. Also, design was bound by contrast: any software we ship must meet minimum contrast standards. ([Material has an excellent page diving into this more](https://material.io/design/usability/accessibility.html#color-and-contrast))

In the past, designers would plug color pairs into a contrast checker, receiving a number called a contrast ratio that would indicate whether or not there was an appropriate difference between colors. Adjusting the paired colors to meet the requirement wasn’t easy: no color system could adjust lightness while maintaining colorfulness and hue, leaving designers to guess new colors that would meet contrast ratio.

The HCT color system makes meeting accessibility standards much easier. Instead of using the unintuitive measure of a contrast ratio, the system converts those same requirements to a simple difference in tone, HCT’s measure of lightness. Contrast is guaranteed simply by picking colors whose tone values are far enough apart—no complex calculations required.

For example, to meet [WCAG contrast requirements](https://www.w3.org/WAI/WCAG21/Understanding/contrast-minimum.html), smaller elements (less than ¼” or 40 dp) require a tone difference of 50 with their background, larger elements require a tone difference of 40. This principle works consistently for any pair of colors.

![Image](<https://lh3.googleusercontent.com/UwzVMjQUmpgj6k3F8oAb_7TuyjL7RqrVFTJtwdnu36DMq8tNiF40AFe59CDjP4YvNMWFK9Xh5FjJQY1QLu2bF5Y7LlVRxWxEbDTqnCg0U-v2SXk30nw>)

Claude Monet’s “Impression, Sunrise” shows why contrast measures legibility for all, not just non-standard vision: the sun and sky have exactly the same luminance, creating a hazy effect that makes the sun hard to focus on. When viewing the image in black and white, only seeing luminance, the sun disappears completely.

## Creating a scalable design system

Just as design systems enable design at scale, HCT supports color at scale. HCT’s simplification and integration of contrast via tone, makes the color system the right foundation for any scalable design system.

When creating a design system, colors are given names, so they can be referenced in design specifications of screens and UI elements. An example in Material Design is “primary,” which refers to a color used for buttons.

Then, the tone of each color is defined. Colors work in pairs in a user interface, such as a button color and button text color, and tones are chosen to ensure contrast between those pairs.

The other components of color, hue and chroma, are open to any values, enabling more creativity. Material You completes its design system by determining hue and chroma dynamically based on the user’s wallpaper, creating a dynamic, personalized color system.

## Dynamic color: Personalized color at scale

![Image](<https://lh3.googleusercontent.com/lpMERRSJV0-EYstnNp-H6UIc_kfyhLFTXP8j9lvIIS8XkmDxAXxbZswwOp9pLaq8Z50zFJKPRWRlH0GZKJvLSYpcmonsiGXs9Du0glI03BLIZb6kRVc>)

From left to right: 1.) Original image, 2.) Image quantized using Google’s previous algorithm, 3.) Image quantized using Google’s implementation of M. Emre Celebi’s quantization algorithm (arxiv.org/abs/1101.0395), performance enables many more colors, retaining colorfulness.

Material You’s colors are dynamic: it chooses colors in the user's wallpaper, lets the user pick one, then creates an entire UI theme around that color. All of the algorithms behind this are infused with creativity and a designer’s eye, creating a beautiful theme for any color.

First, the wallpaper is quantized, reducing the thousands of colors in it to a smaller number by merging them in color space. The reduced color set is small enough to run statistical algorithms against with efficiency. These algorithms are used to score and filter colors; Android 12 gives colors points for colorfulness and how much of the image they represent, and it filters out colors close to monochrome.

One color, defaulting to the top-ranked color by the algorithm, or chosen by the user in the wallpaper picker, becomes the source color. Its hue and chroma influence the overall color scheme, enabling a vibrant blue scheme, or a muted green one, based on the user’s choice of color.

Using the source color, we create the core palette, which is a set of 5 tonal palettes. A tonal palette is defined by a hue and chroma; the colors in the palette come from varying tones. These tonal palettes reduce cognitive load for designers when creating a design system: instead of specifying hue and chroma for each role, a tonal palette can be substituted.

Finally, we fill out the table that defines the hue chroma and tone of each color role, then use those values and HCT to create the colors used in the theme.

![Tonal palettes generated from Google Blue, #4285F4](<https://lh3.googleusercontent.com/25H1ut2RHsWhTK6Nr9jFuwBYpaXyOJtBlGhOTBcNhDWE4q8VoNWRExUxtLYyPyM4swITzlLYJF3pJ5WipEcVr9xdvyHtaCAA8zPn8v2h6Lcru5-tAh0>)

Tonal palettes generated from Google Blue, #4285F4

## A bright future, built with you

A design system is only as strong as the tools provided to implement it, and Google firmly believes in sharing our work through open source code.

Material Color Utilities is a cross-platform code library for color, containing everything needed to implement Material You. It was initially built for Pixel; as covered in a [blog post](https://android-developers.googleblog.com/2022/02/material-you-coming-to-more-android.html) last week, Android phone manufacturers use it as well. The library is open source, anyone, inside or outside of Google, can contribute to it. The library will be expanded to include new platforms and modules in the near future. We have [Dart, Java, and Typescript libraries](https://github.com/material-foundation/material-color-utilities), and we will open source libraries for iOS, CSS via SASS, and GLSL shaders.

We'll continue updating this tool and adding new code modules as we develop new color features. [Color harmony](https://material.io/blog/dynamic-color-harmony), available today, adjusts colors to dynamically fit the source. By the end of March, Material Color Utilities will add modules for:

-   Legible text on any background—HCT’s tone integrates with opacity, unifying contrast with scrims & shadows
-   Image filters and blend modes that use HCT, maintaining creative intent and legibility, and a GLSL shader for HCT.
-   A new type of gradient that blends realistically, like our work on color harmony. Using HCT produces accurate blends that behave like paints, avoiding issues present in other color spaces.

![Image](<https://lh3.googleusercontent.com/E_Te2ABKYJaO9sHwRChTB2JNgnpS_RMhqI79XAZfRb0aGCf__Wtm4drYJ2SdaRP9GNJBOCVODy66wPNU2ERQnlUSWQKPU__6hwnJ2_TeneTNap6ZjYQ>)

Scrims and shadows that ensure text is legible on any background.

![Image](<https://lh3.googleusercontent.com/459crB0shmfQBhnWHtw5UPjX1x42hQ_1GNBxOjiwYootCbqzxno4rleDZ4e8ceuB1RDqyvyrtJTP7QkANIreOmp6Jwefk_yNGL42ZDSnXXCHmuXpvA>)

Gradients in HCT respect colorfulness and maintain legibility

Behind all of these features, current and future, is our HCT color system. It makes designing with color easier and more successful, from generating wallpaper-based color schemes to adjusting colors for accessibility requirements. HCT drives the tools our designers and engineers use, Material Theme Builder and Material Color Utilities—the same tools used inside Google—which let you deliver consistent, beautiful, colors everywhere, every time.

Have fun, we can’t wait to see what you make with it! Breaking the stasis of unfeeling minimalism in the flat design era will allow people to create things we can’t even imagine. Paraphrasing [Tag Savage](https://sexpigeon.tumblr.com/post/16729718345/path-puts-a-silly-amount-of-trust-in-its-avatars): “Modern museums are pretty in a designy way, but eventually, the particulars around them fall out of fashion. This is good for the museum. Now they can really mess up the place.”

## Resources

-   Mark Fairchild’s [Color Appearance Models](https://onlinelibrary.wiley.com/doi/book/10.1002/9781118653128) provides a strong foundation and survey of color science.
-   Andrew Elliot, Mark Fairchild, and Anna Franklin’s [Handbook of Color Psychology](https://www.cambridge.org/core/books/handbook-of-color-psychology/5A29A2BBA251510F1DCB9CBB746EE7D5) is a compilation of research on many color topics
-   Josef Albers’ [Interaction of Color](https://yalebooks.yale.edu/book/9780300179354/interaction-color) discusses color science phenomena from design’s perspective
-   Bruce Lindbloom’s [website](http://www.brucelindbloom.com/) has reference equations for converting between color spaces.
-   Nico Schlomer’s work, such as the [colorio library](https://github.com/nschloe/colorio) and [algorithmic improvements to CAM16](https://arxiv.org/abs/1802.06067)
-   Jacob Rus’ [CAM16 live document](https://observablehq.com/@jrus/cam16)

¹ [Munsell.com](https://munsell.com)

² [Image source](https://commons.wikimedia.org/wiki/File:Munsell_1943_color_solid_cylindrical_coordinates.png)

³ [CC BY-SA 3.0 License](https://creativecommons.org/licenses/by-sa/3.0/)

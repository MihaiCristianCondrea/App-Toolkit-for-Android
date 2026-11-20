---
title: "Start building with Material You"
source_url: "https://m3.material.io/blog/start-building-with-material-you"
lastmod: "2024-12-23"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "blog"
assets:
  - "assets/images/ee7d4cc880e8-M9d5b8pDzcsKHhfmCFIilmHUYHr-v5NKwxcQw5g-CIFb0-25Cf5Vr0wwU0shAw6N3mgbW9xmkhN9tV0Bb2egGCwfqrg9OlCE.png"
  - "assets/images/9fe686d451a4-DtAwRJg286xmkSgB6c4vgvnQzOjBglC8vYam9ZXJ3l1fz93nHL5RZyNXiIW0rhFk9t3aJ3C5YFpun7VatpyJ9F1eNv_cdR7L.png"
  - "assets/images/c5ae5406f019-GoQAugdtVdzlWcJ0HrFnjI4DIBuTc39m4A8ZE_kd2CXtKID0XU777gSE9etkc6gBCtm9HLYIcWmpAz9FARKPuaCouFNKeJtN.png"
  - "assets/images/21f65f120c92-2Vfz6cFD4fZQRPgDIsdHnc5SF4fe1znBNQ9vxdDHBGfi-2wd6G2HI-38DufeiFIlHSviPoRvw8hbwNdxNJdmx4Otzp1gaso6.png"
  - "assets/images/149fbf41cb6a-Zmv4G2DTWbKsF3ww2OKJrqfDax4OvECjsrIvO0wN-ZByNd7JumgLXy1p7q0wPHCI3fi4sfDNexNm9Y4zGm-USm0GBYpq8qeK.png"
  - "assets/images/bb70d58ba2da-qzrrSWrlt2gdtffYvJSgy33F_Wjfakmzbkx1r4xuwSVyPgixaii0Hs-hdScWBmBYt8qLXI1-13TOxmga0_ABD5ZCbUqFdXoU.png"
---

# Start building with Material You

Implement dynamic color and explore some of the latest personalization features of Material Design 3

![Image](<https://lh3.googleusercontent.com/M9d5b8pDzcsKHhfmCFIilmHUYHr-v5NKwxcQw5g-CIFb0-25Cf5Vr0wwU0shAw6N3mgbW9xmkhN9tV0Bb2egGCwfqrg9OlCEU_QN6f_Je0g0i2uF2Js>)

Published: 2021-10-27T09:00:00

We [unveiled](https://material.io/blog/announcing-material-you) Material You—a set of design system updates that enables more beautiful, personalized expression—at Google I/O this year, and you’ve seen glimpses of it in our [products](https://workspaceupdates.googleblog.com/2021/09/material-you-for-android.html). Now we’re excited to share Material You with our community.

You’ll be able to access everything you need to start building for Android 12 on the new home for [Material Design 3](../index.md), along with new videos from the [Android Developer Summit](https://developer.android.com/events/dev-summit). Designers and developers will be able to start using dynamic color today and take advantage of new tools that make using Material easier than ever.

## Dynamic color makes your app more personal

![Image](<https://lh3.googleusercontent.com/DtAwRJg286xmkSgB6c4vgvnQzOjBglC8vYam9ZXJ3l1fz93nHL5RZyNXiIW0rhFk9t3aJ3C5YFpun7VatpyJ9F1eNv_cdR7LoGyuBUsNzN2jtb9T6iJG>)

Creating a dynamic, personal approach to color that prioritizes accessibility is no simple task—for design or engineering—but new Material You features can take care of it for you.

Dynamic color plays a key role in Material You, creating individualized and expressive experiences for your users and opening up new possibilities for the role of color in your app. With dynamic color, custom palettes that emphasize expression and accessibility are algorithmically generated based on your brand or—starting with Pixel—an individual’s wallpaper. These colors can manifest as dynamic light and dark themes, and can be adjusted based on preference.

#### What’s new

-   [New dynamic color guidelines](https://m3.material.io/styles/color/dynamic-color/overview) describe how the dynamic color system works in detail, from generating tonal palettes to how color gets applied to each component.
-   [Guidance on customizing for brand expression](../foundations/customization.md) describes how you can use the new color system to keep your app unique while adapting for every user.

## Material Components get a visual refresh

![Image](<https://lh3.googleusercontent.com/GoQAugdtVdzlWcJ0HrFnjI4DIBuTc39m4A8ZE_kd2CXtKID0XU777gSE9etkc6gBCtm9HLYIcWmpAz9FARKPuaCouFNKeJtNF3gSqg5qzV8svGwHSJU>)

Along with dynamic color, we’ve also updated several Material Components to support Material Design 3 with a new visual style that incorporates improved use of white space and new state indicators. We’ve designed these components to be responsive to dynamic color, applying Material’s new color roles in a consistent and comprehensive way, sensitive to usability, accessibility, and your brand. The updates allow you to maintain and blend your brand colors with dynamic colors resulting in a one-of-a-kind, personalized experience for the user.

#### What’s new

-   [Updated Figma Design Kit](https://goo.gle/m3-design-kit) helps designers easily build with Material Design 3 in Figma

## New tools simplify design and implementation

![Image](<https://lh3.googleusercontent.com/2Vfz6cFD4fZQRPgDIsdHnc5SF4fe1znBNQ9vxdDHBGfi-2wd6G2HI-38DufeiFIlHSviPoRvw8hbwNdxNJdmx4Otzp1gaso6XEuioJ1fAYbG81Dmd70>)

Want to start exploring how Material’s new color system can work in your app? New tools for developers and designers make it easy, providing simple controls that give you access to the complex Material You color generation system.

The [Material Theme Builder](http://material.io/material-theme-builder) is a tool that helps you see how Material Design 3’s dynamic color and new type scale will look in your app, so that you can enable it with confidence. The tool allows you to visualize dynamically extracted colors, either from an image or of your own selection. After creating a color scheme and type scale, you can export the theming code to multiple formats: MDC-Android (XML), Compose Material (Kotlin) and now [Design System Package (DSP)](https://github.com/AdobeXD/design-system-package-dsp). The Material Theme Builder is available as a [Figma plugin](https://goo.gle/material-theme-builder-figma) and [web tool](https://goo.gle/material-theme-builder-web) which you can use today.

We are also releasing an updated version of Jetpack Compose Material that supports the updated Material Components. Don’t miss the session from [Android Developer Summit](http://d.android.com/dev-summit).

#### What’s new

-   [Material Theme Builder](http://material.io/material-theme-builder) lets you preview how your app will look with dynamic color as well as design a custom Material Design 3 theme for your product.
-   Updates to [Jetpack Compose Material](https://m3.material.io/libraries/additional) and [MDC-Android](https://m3.material.io/libraries/mdc-android/getting-started)

## Design tokens improve collaboration and cohesion at scale

![Image](<https://lh3.googleusercontent.com/Zmv4G2DTWbKsF3ww2OKJrqfDax4OvECjsrIvO0wN-ZByNd7JumgLXy1p7q0wPHCI3fi4sfDNexNm9Y4zGm-USm0GBYpq8qeKP8yJRzaZXmhest9ZVg>)

Our new tools are backed by a concept that can greatly help your team’s efficiency, collaboration, and cohesion across experiences: design tokens.

Design tokens represent design system styles as data, providing a source of truth for all products (and contributors!) of that system to stay in sync as the system evolves.

Tokens for things like type styles and color roles will help keep your design and implementation aligned through a Design System Package (DSP) that bundles up all your tokens to make them portable across tools and platforms. Find token values for individual components in the new guidelines.

#### What’s new

-   [New guidelines for building with design tokens](../foundations/design-tokens/overview.md) describe how to incorporate design tokens to streamline your workflow and improve consistency across your product.

## Getting started and looking forward to the next step

![Image](<https://lh3.googleusercontent.com/qzrrSWrlt2gdtffYvJSgy33F_Wjfakmzbkx1r4xuwSVyPgixaii0Hs-hdScWBmBYt8qLXI1-13TOxmga0_ABD5ZCbUqFdXoUeihtj6ZoQwZ4R0ZGQiU>)

Today’s updates are just step one on the Material Design journey, and there’s still a lot to look forward to. Keep an eye out on Material.io and [subscribe to our newsletter](https://services.google.com/fb/forms/materialdesignnewsletter/) for updates.

By  starting with these latest Material updates today, you can prepare your app for adoption of Material Design 3 to come. Below, see the list of sessions from this week’s [Android Developer Summit](http://d.android.com/dev-summit) you won’t want to miss.

-   Material You in Jetpack Compose
-   Material You: Applying dynamic color and your brand
-   Designing beautiful apps on foldables and large screens
-   Design to Code: Turning handoffs into high-fives
-   #AskAndroid: Jetpack Compose with Material You
-   #AskAndroid: Developing for large screens

## What’s coming next...

-   Adaptive layout guidance will continue to expand, [building on what we launched at Google I/O 2021](https://material.io/blog/material-design-for-large-screens) with guidance and system behaviors tailored for new form factors, including foldables.
-   Our vision for experiences that adapt to every need will continue to drive accessibility updates focused on using dynamic color and adaptive layouts to make it easier for developers and designers to create apps that work for every user.
